package Server_Client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import jade.lang.acl.ACLMessage;

public class ClientGui extends JFrame{
private Client myAgent;
    
    
	private JLabel priceLabel;
	
	ClientGui(Client a) {
		super(a.getLocalName());
		
		myAgent = a;
		
		JPanel p = new JPanel();
		p.setPreferredSize(new Dimension(400,50));

		priceLabel = new JLabel();
		p.add(priceLabel);
		getContentPane().add(p, BorderLayout.NORTH);
		
		JButton sendButton = new JButton("Cere pretul minim");
		sendButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				try {
	
					myAgent.action();
					
					Thread.sleep(100);
					//Am folosit sleep pentru a putea astepta mesajul de raspuns
					
					ACLMessage msgprimit = myAgent.receive();
					String content = msgprimit.getContent();
					priceLabel.setText(content);
				}
				catch (Exception e) {
				}
			}
		} );
		p = new JPanel();
		p.add(sendButton);
		getContentPane().add(p, BorderLayout.SOUTH);
		
		// Make the agent terminate when the user closes 
		// the GUI using the button on the upper right corner	
		addWindowListener(new	WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				myAgent.doDelete();
			}
		} );
		
		setResizable(false);
	}
	
	public void showGui() {
		pack();
		setLocation(50 , 550);
		super.setVisible(true);
	}
	
}
