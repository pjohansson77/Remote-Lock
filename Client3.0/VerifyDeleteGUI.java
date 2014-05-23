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
public class VerifyDeleteGUI {
	private JFrame frame;
	private JPanel panel = new JPanel( new GridLayout( 1, 2 ) );
	private JPanel panel2 = new JPanel( new BorderLayout() );
	private JLabel infoDisplayLbl = new JLabel("Delete ID?", JLabel.CENTER);
	private JButton btnChoice1 = new JButton("YES");
	private JButton btnChoice2 = new JButton("NO");
	private ConnectGUI gui;
	private Client client;
	
	/**
	 * Constructor for VerifyGUI class.
	 * 
	 * @param gui A reference to the ConnectGUI class.
	 * @param client A reference to the Client class.
	 */
	public VerifyDeleteGUI( ConnectGUI gui, Client client ) {
		frame = new JFrame();
		this.gui = gui;
		this.client = client;
		
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
		panel.setPreferredSize( new Dimension( 400, 40 ) );
		
		btnChoice1.addActionListener( new ButtonListener() );
		btnChoice2.addActionListener( new ButtonListener() );
		
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
	 * Button listener that listens to all user inputs in VerifyGUI.
	 */
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if( e.getSource() == btnChoice1 ) {
				client.deleteID();
				gui.showDeleteIDBtn( false );
				gui.showLogIn();
				frame.dispose();
			} 
			if( e.getSource() == btnChoice2 ) {
				gui.showLogIn();
				frame.dispose();
			}
		}			
	}
}
