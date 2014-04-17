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
public class ConnectGUI {
	private JFrame frame;
	private JLabel infoDisplayLbl = new JLabel("", JLabel.CENTER);
	private JLabel lbl = new JLabel("Ange IP-adress:");
	private JLabel lbl2 = new JLabel("Ange port:");
	private JPanel panel = new JPanel( new BorderLayout() );
	private JPanel panel2 = new JPanel( new GridLayout( 2, 1 ) );
	private JPanel panel3 = new JPanel( new GridLayout( 2, 1 ) );
	private JPanel panel4 = new JPanel( new GridLayout( 1, 2 ) );
	private JPanel panel5 = new JPanel( new GridLayout( 1, 2 ) );
	private JPanel panel6 = new JPanel( new BorderLayout() );
	private JPanel panel7 = new JPanel( new BorderLayout() );
	private JPanel panel8 = new JPanel( new BorderLayout() );
	private JLabel statusLbl = new JLabel("Status: ");
	private JLabel statusLbl2 = new JLabel("Disconnected");
	private JTextField ipTextField = new JTextField("195.178.234.223");
	private JTextField portTextField = new JTextField("5555");
	private JButton connectBtn = new JButton("CONNECT");
	private JButton closeBtn = new JButton("CLOSE");
	private ConnectGUI gui;
	private ClientID id;
	
	/**
	 * Constructor for Login class.
	 * 
	 * @param controller
	 *            Controller
	 */
	public ConnectGUI() {
		frame = new JFrame();
		gui = this;
		id = new ClientID();
		
		connectBtn.setFocusable(false);
		closeBtn.setFocusable(false);
		ipTextField.setFocusable(true);
		portTextField.setFocusable(true);
		
		infoDisplayLbl.setFont( new Font( "DialogInput", Font.BOLD, 14 ) );
		panel.setBackground( new Color( 255, 255, 255 ) );
		panel2.setBackground( new Color( 255, 255, 255 ) );
		panel3.setBackground( new Color( 255, 255, 255 ) );
		panel4.setBackground( new Color( 255, 255, 255 ) );
		panel5.setBackground( new Color( 255, 255, 255 ) );
		panel6.setBackground( new Color( 255, 255, 255 ) );
		
		panel.add(infoDisplayLbl, BorderLayout.CENTER);
		
		panel2.add(lbl);
		panel2.add(ipTextField);
		
		panel3.add(lbl2);
		panel3.add(portTextField);
		
		panel4.add(panel2);
		panel4.add(panel3);
		
		panel5.add(connectBtn);
		panel5.add(closeBtn);
		
		panel6.add(statusLbl, BorderLayout.WEST);
		panel6.add(statusLbl2, BorderLayout.CENTER);
		
		panel7.add(panel5, BorderLayout.CENTER);
		panel7.add(panel6, BorderLayout.SOUTH);
		
		panel8.add(panel, BorderLayout.NORTH);
		panel8.add(panel4, BorderLayout.CENTER);
		panel8.add(panel7, BorderLayout.SOUTH);
		
		connectBtn.setPreferredSize( new Dimension( 400, 40 ) );
		panel.setPreferredSize( new Dimension( 400, 60 ) );
		panel5.setPreferredSize( new Dimension( 400, 50 ) );
		panel6.setPreferredSize( new Dimension( 400, 30 ) );
		
		connectBtn.addActionListener( new ButtonListener() );
		closeBtn.addActionListener( new ButtonListener() );
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
			if( e.getSource() == connectBtn ) {
				new Client(ipTextField.getText(), Integer.parseInt(portTextField.getText()), gui, id );
				frameStatus( false );
			}
			if( e.getSource() == closeBtn ) {
				System.exit(0);
			}
		}
	}
}
