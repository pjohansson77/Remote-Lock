package lock;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * Class that handles the login info sequence.
 * 
 * @author Jesper Hansen, Peter Johansson, Andree Höög, Qasim Ahmad, Andreas Flink, Gustav Frigren
 */
public class LoginInfoGUI {
	private JFrame frame;
	private JLabel infoDisplayLbl = new JLabel("New user", JLabel.CENTER);
	private JLabel lbl = new JLabel("Enter a new username:");
	private JLabel lbl2 = new JLabel("Enter a new password:");
	private JLabel statusLbl = new JLabel("Status: ");
	private JLabel statusLbl2 = new JLabel("");
	private JPanel panel = new JPanel( new BorderLayout() );
	private JPanel panel2 = new JPanel( new GridLayout( 2, 1 ) );
	private JPanel panel3 = new JPanel( new GridLayout( 2, 1 ) );
	private JPanel panel4 = new JPanel( new GridLayout( 1, 2 ) );
	private JPanel panel5 = new JPanel( new GridLayout( 1, 2 ) );
	private JPanel panel6 = new JPanel( new BorderLayout() );
	private JPanel panel7 = new JPanel( new BorderLayout() );
	private JPanel panel8 = new JPanel( new BorderLayout() );
	private JTextField userTextField = new JTextField();
	private JTextField passwordTextField = new JTextField();
	private JButton okBtn = new JButton("OK");
	private JButton disconnectBtn = new JButton("Disconnect");
	private Client client;
	private LoginNewUserToServer loginNewUserToServer;
	
	/**
	 * Constructor for LoginInfoGUI class.
	 * 
	 * @param client Client
	 */
	public LoginInfoGUI( Client client, LoginNewUserToServer loginNewUserToServer ) {
		frame = new JFrame();
		this.client = client;
		this.loginNewUserToServer = loginNewUserToServer;
		
		okBtn.setFocusable(false);
		disconnectBtn.setFocusable(false);
		userTextField.setFocusable(true);
		passwordTextField.setFocusable(true);
		
		infoDisplayLbl.setFont( new Font( "DialogInput", Font.BOLD, 14 ) );
		panel.setBackground( new Color( 255, 255, 255 ) );
		panel2.setBackground( new Color( 255, 255, 255 ) );
		panel3.setBackground( new Color( 255, 255, 255 ) );
		panel4.setBackground( new Color( 255, 255, 255 ) );
		panel5.setBackground( new Color( 255, 255, 255 ) );
		panel6.setBackground( new Color( 255, 255, 255 ) );
		
		panel.add(infoDisplayLbl, BorderLayout.CENTER);
		
		panel2.add(lbl);
		panel2.add(userTextField);
		
		panel3.add(lbl2);
		panel3.add(passwordTextField);
		
		panel4.add(panel2);
		panel4.add(panel3);
		
		panel5.add(okBtn);
		panel5.add(disconnectBtn);
		
		panel6.add(statusLbl, BorderLayout.WEST);
		panel6.add(statusLbl2, BorderLayout.CENTER);
		
		panel7.add(panel5, BorderLayout.CENTER);
		panel7.add(panel6, BorderLayout.SOUTH);
		
		panel8.add(panel, BorderLayout.NORTH);
		panel8.add(panel4, BorderLayout.CENTER);
		panel8.add(panel7, BorderLayout.SOUTH);
		
		okBtn.setPreferredSize( new Dimension( 400, 40 ) );
		panel.setPreferredSize( new Dimension( 400, 60 ) );
		panel5.setPreferredSize( new Dimension( 400, 50 ) );
		panel6.setPreferredSize( new Dimension( 400, 30 ) );
		
		okBtn.addActionListener( new ButtonListener() );
		disconnectBtn.addActionListener( new ButtonListener() );
		showLogIn();
	}
	
	/**
	 * Function that activates LoginInfoGUI.
	 */
	public void showLogIn() {
		frame.setVisible( true );
		frame.setResizable( false );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.getContentPane().add( panel8, BorderLayout.CENTER );
		frame.setLocation( 200, 100 );
		frame.pack();
	}
	
	/**
	 * Function that sends a message to the GUI.
	 * 
	 * @param txt Message in a String.
	 */
	public void setInfoDisplay( String txt ) {
		infoDisplayLbl.setText( txt );
	}
	
	/**
	 * Function that sends a message to the GUI.
	 * 
	 * @param txt Message in a String.
	 */
	public void setStatusDisplay( String txt ) {
		statusLbl2.setText( txt );
	}
	
	/**
	 * Function that sends a message to the GUI.
	 * 
	 * @param txt Message in a String.
	 */
	public void setlbl( String txt ) {
		lbl.setText( txt );
	}
	
	/**
	 * Function that sends a message to the GUI.
	 * 
	 * @param txt Message in a String.
	 */
	public void setlbl2( String txt ) {
		lbl2.setText( txt );
	}
	
	/**
	 * Function that clears the username textfield.
	 */
	public void clearUserTextField() {
		userTextField.setText( "" );
	}
	
	/**
	 * Function that clears the password textfield.
	 */
	public void clearPasswordTextField() {
		passwordTextField.setText( "" );
	}
	
	/**
	 * Function that sets LoginInfoGUI visible to false.
	 */
	public void hideFrame() {
		frame.setVisible( false );
	}
	
	/**
	 * Button listener that listens to all user inputs in LoginInfoGUI.
	 */
	private class ButtonListener implements ActionListener {		
		public void actionPerformed( ActionEvent e ) {
			if( e.getSource() == okBtn && !userTextField.getText().equals( "" ) && !passwordTextField.getText().equals( "" ) ) {
				loginNewUserToServer.startInfoLogin( userTextField.getText(), passwordTextField.getText() );
				frame.dispose();
			} else
				setInfoDisplay( "Username and password required" );
			if( e.getSource() == disconnectBtn ) {
				client.disconnect();
				frame.dispose();
			}
			clearUserTextField();
			clearPasswordTextField();
		}
	}
}
