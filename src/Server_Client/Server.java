package Server_Client;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

public class Server extends Agent {
	/**
	 * 
	 */
	private double[] preturi=new double[3];
	private static final long serialVersionUID = 7402568102072277358L;
	private Logger myLogger = Logger.getMyLogger(getClass().getName());

	private class WaitPingAndReplyBehaviour extends CyclicBehaviour {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1076419418599477447L;

		public WaitPingAndReplyBehaviour(Agent a) {
			super(a);
		}

		public void action() {
			ACLMessage msg = myAgent.receive();
			
			if (msg != null) {
				ACLMessage reply =msg.createReply();

				if (msg.getPerformative() == ACLMessage.REQUEST) {
					String content = msg.getContent();
					if ((content != null) ) {
						myLogger.log(Logger.INFO, "Agent " + getLocalName() + " - a primit mesaj de la"
								+ msg.getSender().getLocalName());
						reply.setPerformative(ACLMessage.INFORM);
						if(preturi[0]!=0&&preturi[1]!=0&&preturi[2]!=0)
						{
							if(preturi[0]<=preturi[1]&&preturi[0]<=preturi[2])
							{
								reply.setContent(String.format("Pretul cel mai mic este la benzinaria OMV:%f lei",preturi[0]));
							}
							else if(preturi[1]<=preturi[0]&&preturi[1]<=preturi[2])
							{
								reply.setContent(String.format("Pretul cel mai mic este la benzinaria MOL:%f lei",preturi[1]));
							}
							else if(preturi[2]<=preturi[0]&&preturi[2]<=preturi[1])
							{
								reply.setContent(String.format("Pretul cel mai mic este la benzinaria Lukoil:%f lei",preturi[2]));
							}
						}
						else
						{
							reply.setContent("Ne pare rau,nu avem date de la toate benzinariile!");
						}
						//reply.setContent("pong");
					} else {
						myLogger.log(Logger.INFO, "Agent " + getLocalName() + " - Cerere neasteptata" + content
								+ "de la " + msg.getSender().getLocalName());
						reply.setPerformative(ACLMessage.REFUSE);
						reply.setContent("Mesajul nu poate fi gol");
					}

				}
				else if(msg.getPerformative()== ACLMessage.INFORM)
				{
					String content = msg.getContent();
					if ((content != null) ) {
						myLogger.log(Logger.INFO, "Agent " + getLocalName() + " - a primit mesaj de la "
								+ msg.getSender().getLocalName());
					reply.setPerformative(ACLMessage.CONFIRM);
					reply.setContent("Multumim OMV!\nNoul pret al dumneavoastra a fost inregistrat.");
					
					try{
					preturi[0]=Double.parseDouble(content.toString());
					}
					catch(Exception e ){
						reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
						reply.setContent("S-a introdus o valoare invalida pentru pret!");}
					

					myLogger.log(Logger.INFO, "Agent "+getLocalName()+" - a primit mesaj de la "+msg.getSender().getLocalName());
					}
					else
					{
						reply.setPerformative(ACLMessage.REFUSE);
						reply.setContent("Mesajul nu poate fi gol");
						}
				}
				else if(msg.getPerformative()== ACLMessage.INFORM_IF)
				{
					String content = msg.getContent();
					if ((content != null) ) {
						myLogger.log(Logger.INFO, "Agent " + getLocalName() + " -a primit mesaj de la"
								+ msg.getSender().getLocalName());
					reply.setPerformative(ACLMessage.CONFIRM);
					reply.setContent("Multumim MOL!\nNoul pret al dumneavoastra a fost inregistrat.");
					
					try{
					preturi[1]=Double.parseDouble(content.toString());
					}
					catch(Exception e ){
						reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
						reply.setContent("S-a introdus o valoare invalida pentru pret!");}
					
					myLogger.log(Logger.INFO, "Agent "+getLocalName()+" - a primit mesaj de la "+msg.getSender().getLocalName());
					}
					else
					{
						reply.setPerformative(ACLMessage.REFUSE);
						reply.setContent("Mesajul nu poate fi gol");
						}
				}
				else if(msg.getPerformative()== ACLMessage.INFORM_REF)
				{
					String content = msg.getContent();
					if ((content != null) ) {
						myLogger.log(Logger.INFO, "Agent " + getLocalName() + " - a primit mesaj de la "
								+ msg.getSender().getLocalName());
						reply.setPerformative(ACLMessage.INFORM);
					reply.setPerformative(ACLMessage.CONFIRM);
					reply.setContent("Multumim Lukoil!\nNoul pret al dumneavoastra a fost inregistrat.");
					
					try{
					preturi[2]=Double.parseDouble(content.toString());
					}
					catch(Exception e ){
						reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
						reply.setContent("S-a introdus o valoare invalida pentru pret!");}
					
					myLogger.log(Logger.INFO, "Agent "+getLocalName()+" - a primit mesaj de la "+msg.getSender().getLocalName());
					}
					else
					{
						reply.setContent("Mesajul nu poate fi gol");
						}
				}
				else {
					myLogger.log(Logger.INFO,
							"Agent " + getLocalName() + " - Mesaj neasteptat "
									+ ACLMessage.getPerformative(msg.getPerformative()) + " primit de la "
									+ msg.getSender().getLocalName());
					reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
					reply.setContent("Mesajul primit nu este de tipul asteptat !");// + ACLMessage.getPerformative(msg.getPerformative()) );
				}
				send(reply);
			} else {
				block();
			}
		}
	} // END of inner class WaitPingAndReplyBehaviour

	protected void setup() {
		// Registration with the DF
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("ServerAgent");
		sd.setName(getName());
		sd.setOwnership("TILAB");
		dfd.setName(getAID());
		dfd.addServices(sd);
		
		try {
			DFService.register(this, dfd);
			WaitPingAndReplyBehaviour PingBehaviour = new WaitPingAndReplyBehaviour(this);
			addBehaviour(PingBehaviour);
		} catch (FIPAException e) {
			myLogger.log(Logger.SEVERE, "Agent " + getLocalName() + " - nu se poate inregistra la DF", e);
			doDelete();
		}
	}

}