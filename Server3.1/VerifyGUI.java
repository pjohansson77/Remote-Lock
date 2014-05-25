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
	private JButton btnVal1 = new JButton("YES");
	private JButton btnVal2 = new JButton("NO");
	private AdminGUI gui;
	private Admin admin;
	private int userIndex;
	private boolean reset = false, remove = false;
	
	/**
	 * Constructor for VerifyRemove class.
	 * 
	 * @param gui A reference to the AdminGUI class.
	 * @param client A reference to the Admin class.
	 * @param userIndex A int that represent the place in the list.
	 */
	public VerifyGUI(AdminGUI gui, Admin admin, int userIndex) {
		this.userIndex = userIndex;
		frame = new JFrame();
		this.gui = gui;
		this.admin = admin;
		
		btnVal1.setFocusable(false);
		btnVal2.setFocusable(false);
		
		panel.add( btnVal1 );
		panel.add( btnVal2 );
		
		panel2.add(infoDisplayLbl, BorderLayout.NORTH);
		panel2.add(panel, BorderLayout.CENTER);
		
		panel.setBackground( new Color( 255, 255, 255 ) );
		panel2.setBackground( new Color( 255, 255, 255 ) );
		
		infoDisplayLbl.setFont( new Font( "DialogInput", Font.BOLD, 14 ) );
		infoDisplayLbl.setPreferredSize( new Dimension( 400, 90 ) );
		panel.setPreferredSize( new Dimension( 400, 40 ) );
		
		btnVal1.addActionListener( new ButtonListener() );
		btnVal2.addActionListener( new ButtonListener() );
		
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
	 * Function that sets VerifyRemove visible to false.
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
	 * Button listener that listens to all user inputs in VerifyRemove.
	 */
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if( e.getSource() == btnVal1 && remove && !reset ) {
				admin.remove(userIndex);
				gui.updateModel();
				gui.showFrame(true);
				frame.dispose();
			} 
			if( e.getSource() == btnVal2 & remove && !reset ) {
				gui.showFrame(true);
				frame.dispose();
			}
			if( e.getSource() == btnVal1 && reset && !remove ) {
				admin.resetPassword(userIndex);
				gui.showFrame(true);
				frame.dispose();
			} 
			if( e.getSource() == btnVal2 && reset && !remove ) {
				frame.dispose();
				gui.showFrame(true);
			}
		}			
	}
}
