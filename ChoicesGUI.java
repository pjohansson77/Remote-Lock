package test;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChoicesGUI {
	private JFrame frame;
	private JPanel panel = new JPanel( new GridLayout( 2, 2 ) );
	private JPanel panel2 = new JPanel( new BorderLayout() );
	private JLabel infoDisplayLbl = new JLabel("", JLabel.CENTER);
	private JLabel statusLbl = new JLabel("Status: ");
	private JButton btnVal1 = new JButton("Lampa1");
	private JButton btnVal2 = new JButton("Lampa2");
	private JButton btnVal3 = new JButton("Disco");
	private JButton btnVal4 = new JButton("Logga ut");
	private Client client;
	
	public ChoicesGUI( Client client ) {
		frame = new JFrame();
		this.client = client;
		
		panel.add( btnVal1 );
		panel.add( btnVal2 );
		panel.add( btnVal3 );
		panel.add( btnVal4 );
		
		panel2.add(infoDisplayLbl, BorderLayout.NORTH);
		panel2.add(panel, BorderLayout.CENTER);
		panel2.add(statusLbl, BorderLayout.SOUTH);
		
		panel.setBackground( new Color( 255, 255, 255 ) );
		panel2.setBackground( new Color( 255, 255, 255 ) );
		
		infoDisplayLbl.setPreferredSize( new Dimension( 400, 60 ) );
		panel.setPreferredSize( new Dimension( 400, 150 ) );
		statusLbl.setPreferredSize( new Dimension( 400, 60 ) );
		
		btnVal1.addActionListener( new ButtonListener() );
		btnVal2.addActionListener( new ButtonListener() );
		btnVal3.addActionListener( new ButtonListener() );
		btnVal4.addActionListener( new ButtonListener() );
		
		showChoices();
	}
	
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String res = "Status: ";
			if( e.getSource() == btnVal1 ) {
				client.choices( "1" );
			} else if( e.getSource() == btnVal2 ) {
				client.choices( "2" );
			} else if( e.getSource() == btnVal3 ) {
				client.choices( "4" );
			} else if( e.getSource() == btnVal4 ) {
				client.choices( "0" );
			}
			statusLbl.setText( res );
		}			
	}
	
	public void showChoices() {
		frame.setVisible( true );
		frame.setResizable( false );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.getContentPane().add( panel2, BorderLayout.CENTER );
		frame.setLocation( 200, 100 );
		frame.pack();
	}
	
	public void setInfoDisplay( String txt ) {
		infoDisplayLbl.setText( txt );
	}
	
	public void frameStatus( boolean status ) {
		frame.setVisible( status );
	}
}