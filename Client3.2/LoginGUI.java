package lock;

import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.*;

/**
 * Class that handles the login sequence and the change password sequence.
 * 
 * @author Peter Johansson, Andree Höög, Jesper Hansen
 */
public class LoginGUI {
	private JFrame frame;
	private JLabel infoDisplayLbl = new JLabel("Welcome", JLabel.CENTER);
	private JPanel panel = new JPanel( new BorderLayout() );
	private JPanel panel3 = new JPanel( new GridLayout( 1, 2 ) );
	private JPanel panel6 = new JPanel( new BorderLayout() );
	private JPasswordField passwordTextField = new JPasswordField();
	private JButton okBtn = new JButton("OK");
	private JButton cancelBtn = new JButton("CANCEL");
	private ConnectGUI gui;
	private LoginToServer loginToServer;
	private boolean changepassword = false, newpassword = false;
	private ChoicesGUI choice;
	private Socket socket;
	
	/**
	 * Constructor for LoginGUI class.
	 * 
	 * @param socket The active socket.
	 * @param output The active OutputStream.
	 * @param input The active InputStream.
	 * @param gui A reference to the ConnectGUI class.
	 */
	public LoginGUI( Socket socket, DataOutputStream output, DataInputStream input, ConnectGUI gui ) {
		loginToServer = new LoginToServer( socket, output, input, gui, this );
		this.socket = socket;
		frame = new JFrame();
		this.gui = gui;
		
		passwordTextField.setBorder(BorderFactory.createTitledBorder("Enter password"));
		
		okBtn.setFocusable(false);
		cancelBtn.setFocusable(false);
		passwordTextField.setFocusable(true);
		
		infoDisplayLbl.setFont( new Font( "DialogInput", Font.BOLD, 14 ) );
		panel.setBackground( new Color( 255, 255, 255 ) );
		panel3.setBackground( new Color( 255, 255, 255 ) );
		
		panel.add(infoDisplayLbl, BorderLayout.CENTER);
		
		panel3.add(okBtn);
		panel3.add(cancelBtn);
		
		panel6.add(panel, BorderLayout.NORTH);
		panel6.add(passwordTextField, BorderLayout.CENTER);
		panel6.add(panel3, BorderLayout.SOUTH);
		
		okBtn.setPreferredSize( new Dimension( 400, 40 ) );
		panel.setPreferredSize( new Dimension( 400, 70 ) );
		panel3.setPreferredSize( new Dimension( 400, 40 ) );
		
		okBtn.addActionListener( new ButtonListener() );
		cancelBtn.addActionListener( new ButtonListener() );
		showLogIn();
	}
	
	/**
	 * Function that activates LoginGUI.
	 */
	public void showLogIn() {
		frame.setVisible( true );
		frame.setResizable( false );
		frame.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
		frame.getContentPane().add( panel6, BorderLayout.CENTER );
		frame.setLocation( 200, 100 );
		frame.pack();
		frame.getRootPane().setDefaultButton(okBtn);
	}
	
	/**
	 * Function that converts an array of chars into a String
	 * 
	 * @param pw An array of chars
	 * @return the password array as a String instead
	 */
	private String charToString( char[] pw ) {
		String password = "";
		for( int i = 0; i < pw.length; i++ ) {
			password += pw[ i ];
		}
		return password;
	}
	
	/**
	 * Function that gets a reference to the ChoicesGUI class.
	 * 
	 * @param choice A reference to the ChoicesGUI class.
	 */
	public void getChoiceGUI( ChoicesGUI choice ) {
		this.choice = choice;
	}
	
	/**
	 * Function that sets a boolean.
	 * 
	 * @param changepassword True or false.
	 */
	public void setchangepassword( boolean changepassword ) {
		this.changepassword = changepassword;
	}
	
	/**
	 * Function that sets a boolean.
	 * 
	 * @param newpassword True or false.
	 */
	public void setnewpassword( boolean newpassword ) {
		this.newpassword = newpassword;
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
	 * Function that clears the password textfield.
	 */
	public void clearPasswordTextField() {
		passwordTextField.setText( "" );
	}
	
	/**
	 * Function that sets LoginGUI visible to false.
	 */
	public void hideFrame() {
		frame.setVisible( false );
	}
	
	/**
	 * Button listener that listens to all user inputs in LoginGUI.
	 */
	private class ButtonListener implements ActionListener {		
		public void actionPerformed( ActionEvent e ) {
			if( e.getSource() == okBtn && !changepassword && !newpassword ) {
				hideFrame();
				loginToServer.connected( charToString( passwordTextField.getPassword() ) );
			} else if( e.getSource() == okBtn && changepassword && !newpassword && !charToString( passwordTextField.getPassword() ).equals( "" ) ) {
				hideFrame();
				loginToServer.changePassword( charToString( passwordTextField.getPassword() ) );
			} else if( e.getSource() == cancelBtn && !changepassword && !newpassword ) {
				gui.setInfoDisplay( "Not connected" );
				gui.showLogIn();
				try{
					socket.close();
				} catch( IOException e1 ){}
				frame.dispose();
			} else if( e.getSource() == cancelBtn && changepassword && !newpassword ) {
				hideFrame();
				choice.showChoices();
			} else if( e.getSource() == cancelBtn && changepassword && newpassword ) {
				hideFrame();
				choice.showChoices();
			} else if( e.getSource() == okBtn && changepassword && newpassword && !charToString( passwordTextField.getPassword() ).equals( "" ) ) {
				hideFrame();
				loginToServer.newPassword( charToString( passwordTextField.getPassword() ) );
			} else {
				setInfoDisplay( "Password required" );
			}
			clearPasswordTextField();
		}
	}
}
