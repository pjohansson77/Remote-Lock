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
 * Class that handles a verification choice sequence.
 * 
 * @author Peter Johansson, Andree Höög, Jesper Hansen
 */
public class VerifyGUI {
	private JFrame frame;
	private JPanel panel = new JPanel( new GridLayout( 1, 2 ) );
	private JPanel panel2 = new JPanel( new BorderLayout() );
	private JLabel infoDisplayLbl = new JLabel("", JLabel.CENTER);
	private JButton yesBtn = new JButton("YES");
	private JButton noBtn = new JButton("NO");
	private AdminGUI gui;
	private Admin admin;
	private int userIndex;
	private boolean reset = false, remove = false;
	
	/**
	 * Constructor for VerifyGUI class.
	 * 
	 * @param gui A reference to the AdminGUI class.
	 * @param admin A reference to the Admin class.
	 * @param userIndex A int that represent a place in the list.
	 */
	public VerifyGUI(AdminGUI gui, Admin admin, int userIndex) {
		this.userIndex = userIndex;
		frame = new JFrame();
		this.gui = gui;
		this.admin = admin;
		
		yesBtn.setFocusable(false);
		noBtn.setFocusable(false);
		
		panel.add( yesBtn );
		panel.add( noBtn );
		
		panel2.add(infoDisplayLbl, BorderLayout.NORTH);
		panel2.add(panel, BorderLayout.CENTER);
		
		panel.setBackground( new Color( 255, 255, 255 ) );
		panel2.setBackground( new Color( 255, 255, 255 ) );
		
		infoDisplayLbl.setFont( new Font( "DialogInput", Font.BOLD, 14 ) );
		infoDisplayLbl.setPreferredSize( new Dimension( 400, 90 ) );
		panel.setPreferredSize( new Dimension( 400, 40 ) );
		
		yesBtn.addActionListener( new ButtonListener() );
		noBtn.addActionListener( new ButtonListener() );
		
		showChoices();
	}
	
	/**
	 * Function that activates VerifyGUI.
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
	 * Function that sets VerifyGUI visible to false.
	 */
	public void hideFrame() {
		frame.setVisible( false );
	}
	
	/**
	 * Function that sets the boolean reset.
	 * 
	 * @param reset true or false.
	 */
	public void setReset( boolean reset ) {
		this.reset = reset;
	}
	
	/**
	 * Function that sets the boolean remove.
	 * 
	 * @param remove true or false.
	 */
	public void setRemove( boolean remove ) {
		this.remove = remove;
	}
	
	/**
	 * Button listener that listens to all user inputs in VerifyGUI.
	 */
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if( e.getSource() == yesBtn && remove && !reset ) {
				admin.remove(userIndex);
				gui.showFrame(true);
				frame.dispose();
			} 
			if( e.getSource() == noBtn & remove && !reset ) {
				gui.showFrame(true);
				frame.dispose();
			}
			if( e.getSource() == yesBtn && reset && !remove ) {
				admin.resetPassword(userIndex);
				gui.showFrame(true);
				frame.dispose();
			} 
			if( e.getSource() == noBtn && reset && !remove ) {
				frame.dispose();
				gui.showFrame(true);
			}
		}			
	}
}
