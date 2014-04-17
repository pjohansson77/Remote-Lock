package test;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * Class that handles the login sequence.
 * 
 * @author Jesper Hansen, Peter Johansson, Andree Höög, Qasim Ahmad, Andreas Flink, Gustav Frigren
 * 
 */
public class LoginGUI {
	private JFrame frame;
	private JLabel infoDisplayLbl = new JLabel("", JLabel.CENTER);
	private JLabel lbl = new JLabel("Ange lösenord:");
	private JPanel panel = new JPanel( new BorderLayout() );
	private JPanel panel2 = new JPanel( new GridLayout( 2, 1 ) );
	private JPanel panel3 = new JPanel( new GridLayout( 1, 2 ) );
	private JPanel panel4 = new JPanel( new BorderLayout() );
	private JPanel panel5 = new JPanel( new BorderLayout() );
	private JPanel panel6 = new JPanel( new BorderLayout() );
	private JTextField passwordTextField = new JTextField();
	private JLabel statusLbl = new JLabel("Status: ");
	private JLabel statusLbl2 = new JLabel("");
	private JButton okBtn = new JButton("OK");
	private JButton disconnectBtn = new JButton("Disconnect");
	private Client client;
	
	/**
	 * Constructor for Login class.
	 * 
	 * @param controller
	 *            Controller
	 */
	public LoginGUI( Client client ) {
		frame = new JFrame();
		this.client = client;
		
		okBtn.setFocusable(false);
		disconnectBtn.setFocusable(false);
		passwordTextField.setFocusable(true);
		
		infoDisplayLbl.setFont( new Font( "DialogInput", Font.BOLD, 14 ) );
		panel.setBackground( new Color( 255, 255, 255 ) );
		panel2.setBackground( new Color( 255, 255, 255 ) );
		panel3.setBackground( new Color( 255, 255, 255 ) );
		panel4.setBackground( new Color( 255, 255, 255 ) );
		
		panel.add(infoDisplayLbl, BorderLayout.CENTER);
		
		panel2.add(lbl);
		panel2.add(passwordTextField);
		
		panel3.add(okBtn);
		panel3.add(disconnectBtn);
		
		panel4.add(statusLbl, BorderLayout.WEST);
		panel4.add(statusLbl2, BorderLayout.CENTER);
		
		panel5.add(panel3, BorderLayout.CENTER);
		panel5.add(panel4, BorderLayout.SOUTH);
		
		panel6.add(panel, BorderLayout.NORTH);
		panel6.add(panel2, BorderLayout.CENTER);
		panel6.add(panel5, BorderLayout.SOUTH);
		
		okBtn.setPreferredSize( new Dimension( 400, 40 ) );
		panel.setPreferredSize( new Dimension( 400, 60 ) );
		panel3.setPreferredSize( new Dimension( 400, 50 ) );
		panel4.setPreferredSize( new Dimension( 400, 30 ) );
		
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
		frame.getContentPane().add( panel6, BorderLayout.CENTER );
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
	
	public void clearPasswordTextField() {
		passwordTextField.setText( "" );
	}
	
	/**
	 * Sets Login GUI visible to false.
	 */
	public void frameStatus( boolean status ) {
		frame.setVisible( status );
	}
	
	/**
	 * Button listener that does what the name suggest.
	 * Listens to all user inputs in Login GUI.
	 */
	private class ButtonListener implements ActionListener {		
		public void actionPerformed( ActionEvent e ) {
			if( e.getSource() == okBtn ) {
				client.startLogin( passwordTextField.getText() );
				clearPasswordTextField();
			}
			if( e.getSource() == disconnectBtn ) {
				client.disconnectUser();
			}
		}
	}
}
