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
 * Class that handles the arduino choice sequence.
 * 
 * @author Peter Johansson, Andree Höög, Jesper Hansen
 */
public class ChoicesGUI {
	private JFrame frame;
	private JPanel panel = new JPanel( new BorderLayout() );
	private JPanel panel2 = new JPanel( new BorderLayout() );
	private JPanel panel3 = new JPanel( new GridLayout( 2, 2 ) );
	private JPanel panel4 = new JPanel( new BorderLayout() );
	private JLabel infoDisplayLbl = new JLabel("", JLabel.CENTER);
	private JLabel paddingLbl = new JLabel();
	private JButton unlockBtn = new JButton("Unlock door");
	private JButton lockBtn = new JButton("Lock door");
	private JButton changePassBtn = new JButton("Change password");
	private JButton changeLockBtn = new JButton("Change lock");
	private JButton logOutBtn = new JButton("Log out");
	private LoginToServer loginToServer;
	private ChoicesGUI choices;
	
	/**
	 * Constructor for ChoicesGUI class.
	 * 
	 * @param loginToServer A reference to the LoginToServer class.
	 */
	public ChoicesGUI( LoginToServer loginToServer ) {
		frame = new JFrame();
		this.loginToServer = loginToServer;
		this.choices = this;
		noChoice();
		
		unlockBtn.setFocusable(false);
		lockBtn.setFocusable(false);
		changePassBtn.setFocusable(false);
		changeLockBtn.setFocusable(false);
		logOutBtn.setFocusable(false);
		
		panel.add(paddingLbl, BorderLayout.CENTER);
		panel.add(changeLockBtn, BorderLayout.EAST);
		
		panel2.add(panel, BorderLayout.NORTH);
		panel2.add(infoDisplayLbl, BorderLayout.CENTER);
		
		panel3.add( unlockBtn );
		panel3.add( lockBtn );
		panel3.add( changePassBtn );
		panel3.add( logOutBtn );
		
		panel4.add(panel2, BorderLayout.NORTH);
		panel4.add(panel3, BorderLayout.CENTER);
		
		panel.setBackground( new Color( 255, 255, 255 ) );
		panel2.setBackground( new Color( 255, 255, 255 ) );
		panel3.setBackground( new Color( 255, 255, 255 ) );
		
		infoDisplayLbl.setFont( new Font( "DialogInput", Font.BOLD, 14 ) );
		panel.setPreferredSize( new Dimension( 400, 20 ) );
		infoDisplayLbl.setPreferredSize( new Dimension( 400, 90 ) );
		panel3.setPreferredSize( new Dimension( 400, 120 ) );
		
		unlockBtn.addActionListener( new ButtonListener() );
		lockBtn.addActionListener( new ButtonListener() );
		changePassBtn.addActionListener( new ButtonListener() );
		changeLockBtn.addActionListener( new ButtonListener() );
		logOutBtn.addActionListener( new ButtonListener() );
		
		showChoices();
	}
	
	/**
	 * Function that activates ChoicesGUI.
	 */
	public void showChoices() {
		frame.setVisible( true );
		frame.setResizable( false );
		frame.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
		frame.getContentPane().add( panel4, BorderLayout.CENTER );
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
	public void unlockedChoice() {
		unlockBtn.setEnabled( true );
		lockBtn.setEnabled( false );
	}
	
	/**
	 * Function that dims a button.
	 */
	public void lockedChoice() {
		unlockBtn.setEnabled( false );
		lockBtn.setEnabled( true );
	}
	
	/**
	 * Function that dims both buttons.
	 */
	public void noChoice() {
		unlockBtn.setEnabled( false );
		lockBtn.setEnabled( false );
	}
	
	/**
	 * Function that sets ChoicesGUI visible to false.
	 */
	public void hideFrame() {
		frame.setVisible( false );
	}
	
	/**
	 * Button listener that listens to all user inputs in ChoicesGUI.
	 */
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if( e.getSource() == unlockBtn ) {
				loginToServer.choices( "1" );
				noChoice();
			} 
			if( e.getSource() == lockBtn ) {
				loginToServer.choices( "2" );
				noChoice();
			}
			if( e.getSource() == changePassBtn ) {
				loginToServer.choices( "3" );
			} 
			if( e.getSource() == changeLockBtn ) {
				hideFrame();
				loginToServer.choices( "4" );
			}
			if( e.getSource() == logOutBtn ) {
				hideFrame();
				new VerifyLogoutGUI( loginToServer, choices );
			} 
		}			
	}
}
