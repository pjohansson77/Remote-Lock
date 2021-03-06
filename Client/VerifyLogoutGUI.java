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
public class VerifyLogoutGUI {
	private JFrame frame;
	private JPanel panel = new JPanel( new GridLayout( 1, 2 ) );
	private JPanel panel2 = new JPanel( new BorderLayout() );
	private JLabel infoDisplayLbl = new JLabel("Log out?", JLabel.CENTER);
	private JButton yesBtn = new JButton("YES");
	private JButton noBtn = new JButton("NO");
	private LoginToServer login;
	private ChoicesGUI choices;
	
	/**
	 * Constructor for VerifyLogoutGUI class.
	 * 
	 * @param login A reference to the LoginToServer class.
	 * @param choice A reference to the ChoiceGUI class. 
	 */
	public VerifyLogoutGUI(LoginToServer login, ChoicesGUI choices ) {
		frame = new JFrame();
		this.login = login;
		this.choices = choices;
		
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
	 * Function that activates VerifyLogoutGUI.
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
	 * Function that sets VerifyLogoutGUI visible to false.
	 */
	public void hideFrame() {
		frame.setVisible( false );
	}
	
	/**
	 * Button listener that listens to all user inputs in VerifyLogoutGUI.
	 */
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if( e.getSource() == yesBtn ) {
				login.choices("0");
				frame.dispose();
			} 
			if( e.getSource() == noBtn ) {
				choices.showChoices();
				frame.dispose();
			}
		}			
	}
}
