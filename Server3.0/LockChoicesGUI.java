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
public class LockChoicesGUI {
	private JFrame frame;
	private JPanel panel = new JPanel( new GridLayout( 1, 2 ) );
	private JPanel panel2 = new JPanel( new BorderLayout() );
	private JLabel infoDisplayLbl = new JLabel("Choose lock", JLabel.CENTER);
	private JButton btnChoice1 = new JButton("Front door");
	private JButton btnChoice2 = new JButton("Back door");
	
	/**
	 * Constructor for ChoicesGUI class.
	 * 
	 * @param loginToServer A reference to the LoginToServer class.
	 */
	public LockChoicesGUI() {
		frame = new JFrame();
		
		btnChoice1.setFocusable(false);
		btnChoice2.setFocusable(false);
		
		panel.add( btnChoice1 );
		panel.add( btnChoice2 );
		
		panel2.add(infoDisplayLbl, BorderLayout.NORTH);
		panel2.add(panel, BorderLayout.CENTER);
		
		panel.setBackground( new Color( 255, 255, 255 ) );
		panel2.setBackground( new Color( 255, 255, 255 ) );
		
		infoDisplayLbl.setFont( new Font( "DialogInput", Font.BOLD, 14 ) );
		infoDisplayLbl.setPreferredSize( new Dimension( 400, 90 ) );
		panel.setPreferredSize( new Dimension( 400, 120 ) );
		
		btnChoice1.addActionListener( new ButtonListener() );
		btnChoice2.addActionListener( new ButtonListener() );
		
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

			} else if( e.getSource() == btnChoice2 ) {

			}
		}			
	}
}
