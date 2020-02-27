package Server_Client;

import Agenti_preturi.LukoilGui;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class Client extends Agent{
public ClientGui myGui;
String pretul;
	
	
	protected void setup() {
		// Create and show the GUI 
		myGui = new ClientGui(this);
		myGui.showGui();
		
		// Register the book-selling service in the yellow pages
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Client");
		sd.setName("JADE-Client");
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
		System.out.println("Client "+getAID().getName()+" terminating.");
	}
	public void action() {
		
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);

		msg.setContent("Pretul");

		msg.addReceiver(new AID("Server",AID.ISLOCALNAME));
		send(msg);	
		
} // END of inner class WaitPingAndReplyBehaviour
}
