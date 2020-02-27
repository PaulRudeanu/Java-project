package Agenti_preturi;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class MOLGui extends JFrame {
private MOL myAgent;
    
    
	private JTextField priceField;
	
	MOLGui(MOL a) {
		super(a.getLocalName());
		
		myAgent = a;
		
		JPanel p = new JPanel();
		p.add(new JLabel("Pret:"));
		priceField = new JTextField(10);
		p.add(priceField);
		p.add(new JLabel("lei"));
		getContentPane().add(p, BorderLayout.CENTER);
		
		JButton sendButton = new JButton("Send");
		sendButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				try {
					//String price = titleField.getText().trim();
					String price = priceField.getText().trim();
					
					myAgent.price=price;
					
					myAgent.action();
					
					priceField.setText("");
					//addButton.setEnabled(false);
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
		/*Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int centerX = (int)screenSize.getWidth() / 2;
		int centerY = (int)screenSize.getHeight() / 2;
		setLocation(centerX - getWidth() / 2, centerY - getHeight() / 2);*/
		setLocation(50 , 250);
		super.setVisible(true);
	}
}
