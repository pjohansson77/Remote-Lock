package test;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * Class that handles the login new user sequence.
 * 
 * @author Jesper Hansen, Peter Johansson, Andree Höög, Qasim Ahmad, Andreas Flink, Gustav Frigren
 */
public class LoginNewUserGUI {
	private JFrame frame;
	private JLabel infoDisplayLbl = new JLabel("", JLabel.CENTER);
	private JLabel lbl = new JLabel("Ange användarnamn:");
	private JLabel lbl2 = new JLabel("Ange lösenord:");
	private JPanel panel = new JPanel( new BorderLayout() );
	private JPanel panel2 = new JPanel( new GridLayout( 2, 1 ) );
	private JPanel panel3 = new JPanel( new GridLayout( 2, 1 ) );
	private JPanel panel4 = new JPanel( new GridLayout( 1, 2 ) );
	private JPanel panel5 = new JPanel( new GridLayout( 1, 2 ) );
	private JPanel panel6 = new JPanel( new BorderLayout() );
	private JTextField userTextField = new JTextField();
	private JTextField passwordTextField = new JTextField();
	private JButton okBtn = new JButton("OK");
	private JButton disconnectBtn = new JButton("Disconnect");
	private Client client;
	private ConnectGUI gui;
	private ClientID id;
	
	/**
	 * Constructor for LoginNewUser class.
	 * 
	 * @param client A reference to the Client class
	 * @param gui A reference to the ConnectGUI class
	 * @param id A reference to the ClientID class
	 */
	public LoginNewUserGUI( Client client, ConnectGUI gui, ClientID id ) {
		frame = new JFrame();
		this.client = client;
		this.gui = gui;
		this.id = id;
		
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
		
		panel.add(infoDisplayLbl, BorderLayout.CENTER);
		
		panel2.add(lbl);
		panel2.add(userTextField);
		
		panel3.add(lbl2);
		panel3.add(passwordTextField);
		
		panel4.add(panel2);
		panel4.add(panel3);
		
		panel5.add(okBtn);
		panel5.add(disconnectBtn);
		
		panel6.add(panel, BorderLayout.NORTH);
		panel6.add(panel4, BorderLayout.CENTER);
		panel6.add(panel5, BorderLayout.SOUTH);
		
		okBtn.setPreferredSize( new Dimension( 400, 40 ) );
		panel.setPreferredSize( new Dimension( 400, 60 ) );
		panel6.setPreferredSize( new Dimension( 400, 160 ) );
		
		okBtn.addActionListener( new ButtonListener() );
		disconnectBtn.addActionListener( new ButtonListener() );
		showLogIn();
	}
	
	/**
	 * Function that activates LoginNewUserGUI.
	 */
	public void showLogIn() {
		frame.setVisible( true );
		frame.setResizable( false );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.getContentPane().add( panel6, BorderLayout.CENTER );
		frame.setLocation( 200, 100 );
		frame.pack();
	}
	
	/**
	 * Function that sends a message to GUI.
	 * 
	 * @param txt Message in a String.
	 */
	public void setInfoDisplay( String txt ) {
		infoDisplayLbl.setText( txt );
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
	 * Function that sets LoginNewUserGUI visible to false.
	 */
	public void hideFrame() {
		frame.setVisible( false );
	}
	
	/**
	 * Button listener that does what the name suggest.
	 * Listens to all user inputs in LoginNewUserGUI.
	 */
	private class ButtonListener implements ActionListener {		
		public void actionPerformed( ActionEvent e ) {
			if( e.getSource() == okBtn ) {
				client.startNewUserLogin( userTextField.getText(), passwordTextField.getText(), id );
				clearUserTextField();
				clearPasswordTextField();
			}
			if( e.getSource() == disconnectBtn ) {
				hideFrame();
				gui.setInfoDisplay( "" );
				client.disconnect();
			}
		}
	}
}
