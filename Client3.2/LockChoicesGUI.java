package lock;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class that handles the lock choices sequence.
 * 
 * @author Peter Johansson, Andree Höög, Jesper Hansen
 */
public class LockChoicesGUI {
	private JFrame frame;
	private JPanel panel = new JPanel( new GridLayout( 1, 2 ) );
	private JPanel panel2 = new JPanel( new BorderLayout() );
	private JLabel infoDisplayLbl = new JLabel("Choose lock", JLabel.CENTER);
	private JButton lockBtn1 = new JButton("Lock 1");
	private JButton lockBtn2 = new JButton("Lock 2");
	private LoginToServer loginToServer;
	
	/**
	 * Constructor for LockChoicesGUI class.
	 * 
	 * @param loginToServer A reference to the LoginToServer class.
	 */
	public LockChoicesGUI( LoginToServer loginToServer ) {
		frame = new JFrame();
		this.loginToServer = loginToServer;
		
		lockBtn1.setFocusable(false);
		lockBtn2.setFocusable(false);
		
		panel.add( lockBtn1 );
		panel.add( lockBtn2 );
		
		panel2.add(infoDisplayLbl, BorderLayout.NORTH);
		panel2.add(panel, BorderLayout.CENTER);
		
		panel.setBackground( new Color( 255, 255, 255 ) );
		panel2.setBackground( new Color( 255, 255, 255 ) );
		
		infoDisplayLbl.setFont( new Font( "DialogInput", Font.BOLD, 14 ) );
		infoDisplayLbl.setPreferredSize( new Dimension( 400, 90 ) );
		panel.setPreferredSize( new Dimension( 400, 40 ) );
		
		lockBtn1.addActionListener( new ButtonListener() );
		lockBtn2.addActionListener( new ButtonListener() );
		
		showChoices();
	}
	
	/**
	 * Function that activates LockChoicesGUI.
	 */
	public void showChoices() {
		frame.setVisible( true );
		frame.setResizable( false );
		frame.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
		frame.getContentPane().add( panel2, BorderLayout.CENTER );
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
	 * Function that dims a button.
	 */
	public void lock1() {
		lockBtn1.setEnabled( false );
		lockBtn2.setEnabled( true );
	}
	
	/**
	 * Function that dims a button.
	 */
	public void lock2() {
		lockBtn1.setEnabled( true );
		lockBtn2.setEnabled( false );
	}
	
	/**
	 * Function that sets LockChoicesGUI visible to false.
	 */
	public void hideFrame() {
		frame.setVisible( false );
	}
	
	/**
	 * Button listener that listens to all user inputs in LockChoicesGUI.
	 */
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if( e.getSource() == lockBtn1 ) {
				loginToServer.changeArduino( "1" );
				frame.dispose();
			} else if( e.getSource() == lockBtn2 ) {
				loginToServer.changeArduino( "2" );
				frame.dispose();
			}
		}			
	}
}
