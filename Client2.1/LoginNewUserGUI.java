package lock;

import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.swing.*;

/**
 * Class that handles the login new user sequence and the user info choice sequence.
 * 
 * @author Peter Johansson, Andree Höög, Jesper Hansen
 */
public class LoginNewUserGUI {
	private JFrame frame;
	private JLabel infoDisplayLbl = new JLabel("Type server login info", JLabel.CENTER);
	private JPanel panel = new JPanel( new BorderLayout() );
	private JPanel panel4 = new JPanel( new GridLayout( 1, 2 ) );
	private JPanel panel5 = new JPanel( new GridLayout( 1, 2 ) );
	private JPanel panel8 = new JPanel( new BorderLayout() );
	private JTextField userTextField = new JTextField();
	private JTextField passwordTextField = new JTextField();
	private JButton okBtn = new JButton("OK");
	private JButton disconnectBtn = new JButton("CANCEL");
	private Client client;
	private DataInputStream input;
	private DataOutputStream output;
	private ConnectGUI gui;
	private ClientID id;
	private String idTextFile;
	private boolean login = false;
	private LoginNewUserToServer loginNewUserToServer;
	private LoginNewUserGUI gui3;
	
	/**
	 * Constructor for LoginNewUserGUI class.
	 * 
	 * @param client A reference to the Client class
	 * @param output The active OutputStream.
	 * @param input The active InputStream.
	 * @param gui A reference to the ConnectGUI class.
	 * @param id A reference to the ClientID class
	 * @param idTextFile The client id textfile.
	 */
	public LoginNewUserGUI( Client client, DataOutputStream output, DataInputStream input, ConnectGUI gui, ClientID id, String idTextFile ) {
		frame = new JFrame();
		this.client = client;
		this.input = input;
		this.output = output;
		this.gui = gui;
		this.id = id;
		this.idTextFile = idTextFile;
		this.gui3 = this;
		
		userTextField.setBorder(BorderFactory.createTitledBorder("Enter username"));
		passwordTextField.setBorder(BorderFactory.createTitledBorder("Enter password"));
		
		okBtn.setFocusable(false);
		disconnectBtn.setFocusable(false);
		userTextField.setFocusable(true);
		passwordTextField.setFocusable(true);
		
		infoDisplayLbl.setFont( new Font( "DialogInput", Font.BOLD, 14 ) );
		panel.setBackground( new Color( 255, 255, 255 ) );
		panel4.setBackground( new Color( 255, 255, 255 ) );
		panel5.setBackground( new Color( 255, 255, 255 ) );
		
		panel.add(infoDisplayLbl, BorderLayout.CENTER);
		
		panel4.add(userTextField);
		panel4.add(passwordTextField);
		
		panel5.add(okBtn);
		panel5.add(disconnectBtn);
		
		panel8.add(panel, BorderLayout.NORTH);
		panel8.add(panel4, BorderLayout.CENTER);
		panel8.add(panel5, BorderLayout.SOUTH);
		
		okBtn.setPreferredSize( new Dimension( 400, 40 ) );
		panel.setPreferredSize( new Dimension( 400, 60 ) );
		panel5.setPreferredSize( new Dimension( 400, 40 ) );
		
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
		frame.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
		frame.getContentPane().add( panel8, BorderLayout.CENTER );
		frame.setLocation( 200, 100 );
		frame.pack();
		frame.getRootPane().setDefaultButton(okBtn);
	}
	
	/**
	 * Function that sets a boolean.
	 * 
	 * @param login True or false.
	 */
	public void setLogin( boolean login ) {
		this.login = login;
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
	 * Function that clears the username textfield.
	 * 
	 */
	public void clearUserTextField() {
		userTextField.setText( "" );
	}
	
	/**
	 * Function that clears the password textfield.
	 * 
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
	 * Button listener that listens to all user inputs in LoginNewUserGUI.
	 */
	private class ButtonListener implements ActionListener {		
		public void actionPerformed( ActionEvent e ) {
			if( e.getSource() == okBtn && !userTextField.getText().equals( "" ) && !passwordTextField.getText().equals( "" ) && !login ) {
				hideFrame();
				loginNewUserToServer = new LoginNewUserToServer( userTextField.getText(), passwordTextField.getText(), client, output, input, gui, id, idTextFile, gui3 );
			} else if( e.getSource() == okBtn && !userTextField.getText().equals( "" ) && !passwordTextField.getText().equals( "" ) && login ) {
				loginNewUserToServer.startInfoLogin( userTextField.getText(), passwordTextField.getText() );
				frame.dispose();
			} else if( e.getSource() == disconnectBtn ) {
				gui.setInfoDisplay( "Not connected" );
				client.disconnect();
				frame.dispose();
			} else {
				setInfoDisplay( "Username and password required" );
			}
			clearUserTextField();
			clearPasswordTextField();
		}
	}
}
