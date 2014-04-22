package lock;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * Class that handles the login sequence.
 * 
 * @author Jesper Hansen, Peter Johansson, Andree Höög, Qasim Ahmad, Andreas Flink, Gustav Frigren
 * 
 */
public class LoginInfoGUI {
	private JFrame frame;
	private JLabel infoDisplayLbl = new JLabel("Server info", JLabel.CENTER);
	private JLabel lbl = new JLabel("Ange ditt namn:");
	private JLabel lbl2 = new JLabel("Ange ett lösenord:");
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
	private ConnectGUI gui;
	
	/**
	 * Constructor for Login class.
	 * 
	 * @param controller
	 *            Controller
	 */
	public LoginInfoGUI( Client client, ConnectGUI gui ) {
		frame = new JFrame();
		this.client = client;
		this.gui = gui;
		
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
	 * Function that activates login GUI.
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
	 * Function that sends a message to GUI.
	 * 
	 * @param txt
	 *            Message in a String.
	 */
	public void setInfoDisplay( String txt ) {
		infoDisplayLbl.setText( txt );
	}
	
	public void setStatusDisplay( String txt ) {
		statusLbl2.setText( txt );
	}
	
	public void setlbl( String txt ) {
		lbl.setText( txt );
	}
	
	public void setlbl2( String txt ) {
		lbl2.setText( txt );
	}
	
	/**
	 * Function that sets textField in GUI with String.
	 * 
	 * @param txt
	 *            String to be set in textField.
	 */
	public void clearUserTextField() {
		userTextField.setText( "" );
	}
	
	public void clearPasswordTextField() {
		passwordTextField.setText( "" );
	}
	
	/**
	 * Sets Login GUI visible to false.
	 */
	public void hideFrame() {
		frame.setVisible( false );
	}
	
	/**
	 * Button listener that does what the name suggest.
	 * Listens to all user inputs in Login GUI.
	 */
	private class ButtonListener implements ActionListener {		
		public void actionPerformed( ActionEvent e ) {
			if( e.getSource() == okBtn ) {
				client.startInfoLogin( userTextField.getText(), passwordTextField.getText() );
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
