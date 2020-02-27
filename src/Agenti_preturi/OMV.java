package Agenti_preturi;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class OMV extends Agent {
	
	
	public OMVGui myGui;
	
	String price;
	
	protected void setup() {
		// Create and show the GUI 
		myGui = new OMVGui(this);
		myGui.showGui();
		
		// Register the OMV service in the yellow pages
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Furnizor OMV");
		sd.setName("JADE-OMV");
		dfd.addServices(sd);
		
		try {
			DFService.register(this, dfd);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}

	protected void takeDown() {
		// Deregister from the yellow pages
		try {
			DFService.deregister(this);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		// Close the GUI
		myGui.dispose();
		// Printout a dismissal message
		System.out.println("Furnizor "+getAID().getName()+" finalizat.");
	}
	public void action() {
		
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);

		msg.setContent(price);

		msg.addReceiver(new AID("Server",AID.ISLOCALNAME));
		send(msg);	
} // END of inner class WaitPingAndReplyBehaviour
		
}
