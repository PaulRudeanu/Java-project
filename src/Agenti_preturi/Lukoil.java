package Agenti_preturi;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class Lukoil extends Agent{
public LukoilGui myGui;
	
	String price;
	
	protected void setup() {
		// Create and show the GUI 
		myGui = new LukoilGui(this);
		myGui.showGui();
		
		// Register the Lukoil service in the yellow pages
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Furnizor Lukoil");
		sd.setName("JADE-Lukoil");
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
		
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM_REF);

		msg.setContent(price);

		msg.addReceiver(new AID("Server",AID.ISLOCALNAME));
		send(msg);	
} // END of inner class WaitPingAndReplyBehaviour
}
