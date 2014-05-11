package lock;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

/**
 * Class that handles the server sequence.
 * 
 * @author Andree Höög, Peter Johansson, Jesper Hansen
 */
public class ServerGUI {
	private ListenForClients server;
	private JFrame frame;
	private JPanel panel = new JPanel( new BorderLayout() );
	private JPanel btnPanel = new JPanel( new GridLayout(1,2) );
	private JPanel topPanel = new JPanel( new BorderLayout() );
	private JButton btnStart = new JButton( "Start server" );
	private JButton btnStop = new JButton( "Stop server" );
	private JTextArea txtArea = new JTextArea();
	private JScrollPane scroll = new JScrollPane( txtArea );
	private JLabel ipLabel = new JLabel();
	private String consoleText = "";
	private int port;
	private ServerGUI gui;

	/**
	 * Constructor for ServerGUI class.
	 * 
	 * @param port The port that the server listens on.
	 */
	public ServerGUI( int port ) {
		frame = new JFrame( "Server - Remote Lock" );
		this.port = port;
		this.gui = this;
		
		DefaultCaret caret = ( DefaultCaret )txtArea.getCaret();
		caret.setUpdatePolicy( DefaultCaret.ALWAYS_UPDATE );
		
		btnPanel.add( btnStart );
		btnPanel.add( btnStop );
		
		topPanel.add( btnPanel, BorderLayout.CENTER );
		topPanel.add( ipLabel, BorderLayout.SOUTH );
		
		panel.add( topPanel, BorderLayout.NORTH );
		panel.add( scroll, BorderLayout.CENTER );
		
		panel.setPreferredSize( new Dimension( 425, 500 ) );
		
		ipLabel.setHorizontalAlignment( SwingConstants.CENTER );
		ipLabel.setPreferredSize( new Dimension(200, 20 ) );
		btnPanel.setPreferredSize( new Dimension(125, 50 ) );
		
		btnStop.setEnabled( false );
		txtArea.setEditable( false );

		btnStart.addActionListener( new ButtonListener() );
		btnStop.addActionListener( new ButtonListener() );
		
		showServerGUI();	
	}
	
	/**
	 * Function that activates ServerGUI.
	 */
	public void showServerGUI() {
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.getContentPane().add( panel, BorderLayout.CENTER );
		frame.setLocation( 600, 100 );
		frame.pack();
	}
	
	/**
	 * Function that sends the ip-address and port to the GUI.
	 */
	public void showServerInfo() {
		try {
			ipLabel.setText("Server-IP: " + InetAddress.getLocalHost().getHostAddress() + " Port: " + port );
		} catch ( UnknownHostException e ) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Function that sends text to the GUI.
	 * 
	 * @param txt Message in a String.
	 */
	public void showText( String str ) {
		consoleText += str+"\n";
		txtArea.setText( consoleText );
	}
	
	/**
	 * Button listener that listens to all user inputs in ServerGUI.
	 */
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if( e.getSource() == btnStart ) {
				showText( "Server started: " + Time.getTime() );
				btnStart.setEnabled(false);
				btnStop.setEnabled(true);
				showServerInfo();
				Thread connectThread = new Thread( server = new ListenForClients( port, gui ) );
				connectThread.start();
			}
			if( e.getSource() == btnStop ) {
				showText( "Server closed: " + Time.getTime() );
				btnStart.setEnabled(true);
				btnStop.setEnabled(false);
				server.terminate();
			}
		}
	}
}
