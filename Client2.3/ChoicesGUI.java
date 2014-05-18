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
	private JPanel panel = new JPanel( new GridLayout( 2, 2 ) );
	private JPanel panel2 = new JPanel( new BorderLayout() );
	private JLabel infoDisplayLbl = new JLabel("", JLabel.CENTER);
	private JButton btnVal1 = new JButton("Unlock door");
	private JButton btnVal2 = new JButton("Lock door");
	private JButton btnVal3 = new JButton("Change password");
	private JButton btnVal4 = new JButton("Log out");
	private LoginToServer loginToServer;
	
	/**
	 * Constructor for ChoicesGUI class.
	 * 
	 * @param loginToServer A reference to the LoginToServer class.
	 */
	public ChoicesGUI( LoginToServer loginToServer ) {
		frame = new JFrame();
		this.loginToServer = loginToServer;
		
		btnVal1.setFocusable(false);
		btnVal2.setFocusable(false);
		btnVal3.setFocusable(false);
		btnVal4.setFocusable(false);
		
		panel.add( btnVal1 );
		panel.add( btnVal2 );
		panel.add( btnVal3 );
		panel.add( btnVal4 );
		
		panel2.add(infoDisplayLbl, BorderLayout.NORTH);
		panel2.add(panel, BorderLayout.CENTER);
		
		panel.setBackground( new Color( 255, 255, 255 ) );
		panel2.setBackground( new Color( 255, 255, 255 ) );
		
		infoDisplayLbl.setFont( new Font( "DialogInput", Font.BOLD, 14 ) );
		infoDisplayLbl.setPreferredSize( new Dimension( 400, 90 ) );
		panel.setPreferredSize( new Dimension( 400, 120 ) );
		
		btnVal1.addActionListener( new ButtonListener() );
		btnVal2.addActionListener( new ButtonListener() );
		btnVal3.addActionListener( new ButtonListener() );
		btnVal4.addActionListener( new ButtonListener() );
		
		showChoices();
	}
	
	/**
	 * Function that activates ChoicesGUI.
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
	public void unlockedChoice() {
		btnVal1.setEnabled( true );
		btnVal2.setEnabled( false );
	}
	
	/**
	 * Function that dims a button.
	 */
	public void lockedChoice() {
		btnVal1.setEnabled( false );
		btnVal2.setEnabled( true );
	}
	
	/**
	 * Function that dims both buttons.
	 */
	public void openChoice() {
		btnVal1.setEnabled( false );
		btnVal2.setEnabled( false );
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
			if( e.getSource() == btnVal1 ) {
				loginToServer.choices( "1" );
			} else if( e.getSource() == btnVal2 ) {
				loginToServer.choices( "2" );
			} else if( e.getSource() == btnVal3 ) {
				loginToServer.choices( "3" );
			} else if( e.getSource() == btnVal4 ) {
				loginToServer.choices( "0" );
				frame.dispose();
			}
		}			
	}
}
