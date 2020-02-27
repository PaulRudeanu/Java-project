package Agenti_preturi;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class MOL extends Agent{
public MOLGui myGui;
	
	String price;
	
	protected void setup() {
		// Create and show the GUI 
		myGui = new MOLGui(this);
		myGui.showGui();
		
		// Register the MOL service in the yellow pages
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Furnizor MOL");
		sd.setName("JADE-MOL");
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
		
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM_IF);

		msg.setContent(price);

		msg.addReceiver(new AID("Server",AID.ISLOCALNAME));
		send(msg);	
} // END of inner class WaitPingAndReplyBehaviour
}
