package lock;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;

/**
 * A class that verifys choice with a Yes or No choice.
 * 
 * @author Peter Johansson, Andree Höög, Jesper Hansen
 *
 */
public class VerifyRequest {
	private JFrame frame;
	private JLabel infoDisplayLbl = new JLabel("", JLabel.CENTER);
	private JLabel paddingLbl = new JLabel();
	private JPanel panel = new JPanel(new BorderLayout());
	private JPanel panel2 = new JPanel(new BorderLayout());
	private JPanel panel3 = new JPanel(new BorderLayout());
	private JPanel panel4 = new JPanel(new GridLayout(1, 2));
	private JPanel panel6 = new JPanel( new BorderLayout());
	private JButton btnYes = new JButton("YES");
	private JButton btnNo = new JButton("NO");
	private boolean status;
	
	/**
	 * Constructor for the Verify class
	 * 
	 * @param gui The ConnectGUI.
	 * @param message The message that a user verifys.
	 */
	public VerifyRequest() {
		frame = new JFrame();
		
		infoDisplayLbl.setFont( new Font( "DialogInput", Font.BOLD, 14 ) );	
		
		panel.add(infoDisplayLbl, BorderLayout.CENTER);
		
		panel2.add(paddingLbl, BorderLayout.CENTER);
		
		panel3.add(panel, BorderLayout.CENTER);
		panel3.add(panel2, BorderLayout.NORTH);
		
		panel4.add(btnYes);
		panel4.add(btnNo);
		
		panel6.add(panel3, BorderLayout.NORTH);
		panel6.add(panel4, BorderLayout.CENTER);
		
		panel2.setPreferredSize(new Dimension(400, 20));
		panel3.setPreferredSize(new Dimension(400, 80));
		panel6.setPreferredSize(new Dimension(400, 160));
		
		btnYes.addActionListener(new ButtonListener());
		btnNo.addActionListener(new ButtonListener());
		
		showLogIn();
	}
	
	/**
	 * Returns the status boolean.
	 * @return status.
	 */
	public boolean getStatus() {
		return status;
	}
	
	public void setText(String message) {
		infoDisplayLbl.setText(message);
	}
	
	public void showFrame(boolean b) {
		frame.setVisible(b);
	}
	
	public void showLogIn() {
		frame.setVisible( false );
		frame.setResizable( false );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.getContentPane().add( panel6, BorderLayout.CENTER );
		frame.setLocation( 200, 100 );
		frame.pack();
		frame.getRootPane().setDefaultButton(btnNo);
	}
	
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == btnYes) {
				status = true;
				frame.dispose();
			}
			if(e.getSource() == btnNo) {
				status = false;
				frame.dispose();
			}
		}
		
	}
}
