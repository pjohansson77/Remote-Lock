package lock;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Class that handles the connection sequence.
 * 
 * @author Peter Johansson, Andree Höög, Jesper Hansen
 */
public class ConnectGUI {
	private JFrame frame;
	private JLabel infoDisplayLbl = new JLabel("Not connected", JLabel.CENTER);
	private JLabel paddingLbl = new JLabel();
	private JPanel panel = new JPanel( new BorderLayout() );
	private JPanel panel2 = new JPanel( new BorderLayout() );
	private JPanel panel3 = new JPanel( new BorderLayout() );
	private JPanel panel4 = new JPanel( new GridLayout( 1, 2 ) );
	private JPanel panel5 = new JPanel( new GridLayout( 1, 2 ) );
	private JPanel panel6 = new JPanel( new BorderLayout() );
	private JTextField ipTextField = new JTextField("192.168.2.70");
	private JTextField portTextField = new JTextField("5555");
	private JButton connectBtn = new JButton("CONNECT");
	private JButton closeBtn = new JButton("CLOSE");
	private JButton deleteBtn = new JButton("Delete ID");
	private ConnectGUI gui;
	private Client client;
	
	/**
	 * Constructor for ConnectGUI class.
	 * 
	 * @param idTextFile The client id textfile.
	 */
	public ConnectGUI( String idTextFile ) {
		this.gui = this;
		deleteBtn.setEnabled( false );
		client = new Client( gui, idTextFile ); 
		frame = new JFrame();
		
		ipTextField.setBorder(BorderFactory.createTitledBorder("Enter IP-address"));
		portTextField.setBorder(BorderFactory.createTitledBorder("Enter port"));
		
		deleteBtn.setFocusable(false);
		connectBtn.setFocusable(false);
		closeBtn.setFocusable(false);
		ipTextField.setFocusable(true);
		portTextField.setFocusable(true);
		
		infoDisplayLbl.setFont( new Font( "DialogInput", Font.BOLD, 14 ) );
		panel.setBackground( new Color( 255, 255, 255 ) );
		panel2.setBackground( new Color( 255, 255, 255 ) );
		panel4.setBackground( new Color( 255, 255, 255 ) );
		panel5.setBackground( new Color( 255, 255, 255 ) );
		
		panel.add(infoDisplayLbl, BorderLayout.CENTER);
		
		panel2.add(paddingLbl, BorderLayout.CENTER);
		panel2.add(deleteBtn, BorderLayout.EAST);
		
		panel3.add(panel, BorderLayout.CENTER);
		panel3.add(panel2, BorderLayout.NORTH);
		
		panel4.add(ipTextField);
		panel4.add(portTextField);
		
		panel5.add(connectBtn);
		panel5.add(closeBtn);
		
		panel6.add(panel3, BorderLayout.NORTH);
		panel6.add(panel4, BorderLayout.CENTER);
		panel6.add(panel5, BorderLayout.SOUTH);
		
		panel2.setPreferredSize( new Dimension( 400, 20 ) );
		panel3.setPreferredSize( new Dimension( 400, 80 ) );
		panel5.setPreferredSize( new Dimension( 400, 40 ) );
		panel6.setPreferredSize( new Dimension( 400, 160 ) );
		
		connectBtn.addActionListener( new ButtonListener() );
		closeBtn.addActionListener( new ButtonListener() );
		deleteBtn.addActionListener( new ButtonListener() );
		showLogIn();
	}
	
	/**
	 * Function that activates ConnectGUI.
	 */
	public void showLogIn() {
		frame.setVisible( true );
		frame.setResizable( false );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.getContentPane().add( panel6, BorderLayout.CENTER );
		frame.setLocation( 200, 100 );
		frame.pack();
		frame.getRootPane().setDefaultButton(connectBtn);
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
	 * Function that sets ConnectGUI visible to false.
	 */
	public void hideFrame() {
		frame.setVisible( false );
	}
	
	/**
	 * Function that dims a button.
	 */
	public void showDeleteIDBtn( boolean bool ) {
		deleteBtn.setEnabled( bool );
	}
	
	/**
	 * Button listener that listens to all user inputs in ConnectGUI.
	 */
	private class ButtonListener implements ActionListener {
		public void actionPerformed( ActionEvent e ) {
			if( e.getSource() == connectBtn ) {
				hideFrame();
				client.connect( ipTextField.getText(), Integer.parseInt( portTextField.getText() ) );
			}
			if( e.getSource() == deleteBtn ) {
				hideFrame();
				new VerifyGUI( gui, client );
			}
			if( e.getSource() == closeBtn ) {
				System.exit(0);
			}
		}
	}
}
