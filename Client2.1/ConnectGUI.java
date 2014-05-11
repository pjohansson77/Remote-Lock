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
	private JPanel panel = new JPanel( new BorderLayout() );
	private JPanel panel4 = new JPanel( new GridLayout( 1, 2 ) );
	private JPanel panel5 = new JPanel( new GridLayout( 1, 2 ) );
	private JPanel panel6 = new JPanel( new BorderLayout() );
	private JTextField ipTextField = new JTextField("192.168.2.70");
	private JTextField portTextField = new JTextField("5555");
	private JButton connectBtn = new JButton("CONNECT");
	private JButton closeBtn = new JButton("CLOSE");
	private ConnectGUI gui;
	private String idTextFile;
	
	/**
	 * Constructor for ConnectGUI class.
	 * 
	 * @param idTextFile The client id textfile.
	 */
	public ConnectGUI( String idTextFile ) {
		frame = new JFrame();
		gui = this;
		this.idTextFile = idTextFile;
		
		ipTextField.setBorder(BorderFactory.createTitledBorder("Enter IP-address"));
		portTextField.setBorder(BorderFactory.createTitledBorder("Enter port"));
		
		connectBtn.setFocusable(false);
		closeBtn.setFocusable(false);
		ipTextField.setFocusable(true);
		portTextField.setFocusable(true);
		
		infoDisplayLbl.setFont( new Font( "DialogInput", Font.BOLD, 14 ) );
		panel.setBackground( new Color( 255, 255, 255 ) );
		panel4.setBackground( new Color( 255, 255, 255 ) );
		panel5.setBackground( new Color( 255, 255, 255 ) );
		
		panel.add(infoDisplayLbl, BorderLayout.CENTER);
		
		panel4.add(ipTextField);
		panel4.add(portTextField);
		
		panel5.add(connectBtn);
		panel5.add(closeBtn);
		
		panel6.add(panel, BorderLayout.NORTH);
		panel6.add(panel4, BorderLayout.CENTER);
		panel6.add(panel5, BorderLayout.SOUTH);
		
		connectBtn.setPreferredSize( new Dimension( 400, 40 ) );
		panel.setPreferredSize( new Dimension( 400, 80 ) );
		panel6.setPreferredSize( new Dimension( 400, 160 ) );
		
		connectBtn.addActionListener( new ButtonListener() );
		closeBtn.addActionListener( new ButtonListener() );
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
	 * Button listener that listens to all user inputs in ConnectGUI.
	 */
	private class ButtonListener implements ActionListener {
		public void actionPerformed( ActionEvent e ) {
			if( e.getSource() == connectBtn ) {
				hideFrame();
				new Client( ipTextField.getText(), Integer.parseInt(portTextField.getText() ), gui, idTextFile ); 
			}
			if( e.getSource() == closeBtn ) {
				System.exit(0);
			}
		}
	}
}
