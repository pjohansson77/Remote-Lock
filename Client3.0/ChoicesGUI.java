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
	private JButton btnChoice1 = new JButton("Unlock door");
	private JButton btnChoice2 = new JButton("Lock door");
	private JButton btnChoice3 = new JButton("Change password");
	private JButton btnChoice4 = new JButton("Log out");
	private JButton btnChoice5 = new JButton("Change lock");
	private LoginToServer loginToServer;
	
	/**
	 * Constructor for ChoicesGUI class.
	 * 
	 * @param loginToServer A reference to the LoginToServer class.
	 */
	public ChoicesGUI( LoginToServer loginToServer ) {
		frame = new JFrame();
		this.loginToServer = loginToServer;
		
		btnChoice1.setFocusable(false);
		btnChoice2.setFocusable(false);
		btnChoice3.setFocusable(false);
		btnChoice4.setFocusable(false);
		btnChoice5.setFocusable(false);
		
		panel.add(paddingLbl, BorderLayout.CENTER);
		panel.add(btnChoice5, BorderLayout.EAST);
		
		panel2.add(panel, BorderLayout.NORTH);
		panel2.add(infoDisplayLbl, BorderLayout.CENTER);
		
		panel3.add( btnChoice1 );
		panel3.add( btnChoice2 );
		panel3.add( btnChoice3 );
		panel3.add( btnChoice4 );
		
		panel4.add(panel2, BorderLayout.NORTH);
		panel4.add(panel3, BorderLayout.CENTER);
		
		panel.setBackground( new Color( 255, 255, 255 ) );
		panel2.setBackground( new Color( 255, 255, 255 ) );
		panel3.setBackground( new Color( 255, 255, 255 ) );
		
		infoDisplayLbl.setFont( new Font( "DialogInput", Font.BOLD, 14 ) );
		panel.setPreferredSize( new Dimension( 400, 20 ) );
		infoDisplayLbl.setPreferredSize( new Dimension( 400, 90 ) );
		panel3.setPreferredSize( new Dimension( 400, 120 ) );
		
		btnChoice1.addActionListener( new ButtonListener() );
		btnChoice2.addActionListener( new ButtonListener() );
		btnChoice3.addActionListener( new ButtonListener() );
		btnChoice4.addActionListener( new ButtonListener() );
		btnChoice5.addActionListener( new ButtonListener() );
		
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
		btnChoice1.setEnabled( true );
		btnChoice2.setEnabled( false );
	}
	
	/**
	 * Function that dims a button.
	 */
	public void lockedChoice() {
		btnChoice1.setEnabled( false );
		btnChoice2.setEnabled( true );
	}
	
	/**
	 * Function that dims both buttons.
	 */
	public void openChoice() {
		btnChoice1.setEnabled( false );
		btnChoice2.setEnabled( false );
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
			if( e.getSource() == btnChoice1 ) {
				loginToServer.choices( "1" );
			} else if( e.getSource() == btnChoice2 ) {
				loginToServer.choices( "2" );
			} else if( e.getSource() == btnChoice3 ) {
				loginToServer.choices( "3" );
			} else if( e.getSource() == btnChoice4 ) {
				loginToServer.choices( "0" );
				frame.dispose();
			} else if( e.getSource() == btnChoice5 ) {
				hideFrame();
				loginToServer.choices( "4" );
			}
		}			
	}
}
