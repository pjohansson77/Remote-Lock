package test;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * Class that handles the login sequence.
 * 
 * @author Jesper Hansen, Peter Johansson, Andree Höög, Qasim Ahmad, Andreas Flink, Gustav Frigren
 * 
 */
public class ClientGUI {
	private JFrame frame;
	private JLabel infoDisplayLbl = new JLabel("", JLabel.CENTER);
	private JLabel lbl = new JLabel("Ange IP-adress:");
	private JLabel lbl2 = new JLabel("Ange port:");
	private JPanel panel = new JPanel( new BorderLayout() );
	private JPanel panel2 = new JPanel( new GridLayout( 2, 1 ) );
	private JPanel panel3 = new JPanel( new GridLayout( 2, 1 ) );
	private JPanel panel4 = new JPanel( new GridLayout( 1, 2 ) );
	private JPanel panel5 = new JPanel( new GridLayout( 1, 2 ) );
	private JPanel panel6 = new JPanel( new BorderLayout() );
	private JTextField ipTextField = new JTextField("10.2.13.151");
	private JTextField portTextField = new JTextField("5555");
	private JButton okBtn = new JButton("OK");
	private JButton closeBtn = new JButton("CLOSE");
	private ClientGUI gui;
	
	/**
	 * Constructor for Login class.
	 * 
	 * @param controller
	 *            Controller
	 */
	public ClientGUI() {
		frame = new JFrame();
		gui = this;
		
		okBtn.setFocusable(false);
		closeBtn.setFocusable(false);
		ipTextField.setFocusable(true);
		portTextField.setFocusable(true);
		
		infoDisplayLbl.setFont( new Font( "DialogInput", Font.BOLD, 14 ) );
		panel.setBackground( new Color( 255, 255, 255 ) );
		panel2.setBackground( new Color( 255, 255, 255 ) );
		panel3.setBackground( new Color( 255, 255, 255 ) );
		panel4.setBackground( new Color( 255, 255, 255 ) );
		panel5.setBackground( new Color( 255, 255, 255 ) );
		
		panel.add(infoDisplayLbl, BorderLayout.CENTER);
		
		panel2.add(lbl);
		panel2.add(ipTextField);
		
		panel3.add(lbl2);
		panel3.add(portTextField);
		
		panel4.add(panel2);
		panel4.add(panel3);
		
		panel5.add(okBtn);
		panel5.add(closeBtn);
		
		panel6.add(panel, BorderLayout.NORTH);
		panel6.add(panel4, BorderLayout.CENTER);
		panel6.add(panel5, BorderLayout.SOUTH);
		
		okBtn.setPreferredSize( new Dimension( 400, 40 ) );
		panel.setPreferredSize( new Dimension( 400, 60 ) );
		panel6.setPreferredSize( new Dimension( 400, 160 ) );
		
		okBtn.addActionListener( new ButtonListener() );
		closeBtn.addActionListener( new ButtonListener() );
		showLogIn();
	}
	
	/**
	 * Function that activates login GUI.
	 */
	public void showLogIn() {
		frame.setVisible( true );
		frame.setResizable( false );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.getContentPane().add( panel6, BorderLayout.CENTER );
		frame.setLocation( 200, 100 );
		frame.pack();
	}
	
	/**
	 * Function that sends a message to GUI.
	 * 
	 * @param txt
	 *            Message in a String.
	 */
	public void setInfoDisplay( String txt ) {
		infoDisplayLbl.setText( txt );
	}
	
//	/**
//	 * Function that sets textField in GUI with String.
//	 * 
//	 * @param txt
//	 *            String to be set in textField.
//	 */
//	public void setTextField( String txt ) {
//		textField.setText( txt );
//	}
	
	/**
	 * Sets Login GUI visible to false.
	 */
	public void frameStatus( boolean status ) {
		frame.setVisible( status );
	}
	
	/**
	 * Button listener that does what the name suggest.
	 * Listens to all user inputs in Login GUI.
	 */
	private class ButtonListener implements ActionListener {
		public void actionPerformed( ActionEvent e ) {
			if( e.getSource() == okBtn ) {
				new Client(ipTextField.getText(), Integer.parseInt(portTextField.getText()), gui );
				frameStatus( false );
			}
			if( e.getSource() == closeBtn ) {
				System.exit(0);
			}
		}
	}
}
