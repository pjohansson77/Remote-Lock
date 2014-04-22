package lock;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

public class ServerGUI {
	private ListenForClients server;
	private ServerGUI gui = this;
	private JFrame frame;
	private JPanel panel = new JPanel(new BorderLayout());
	private JPanel btnPanel = new JPanel(new GridLayout(1,2));
	private JPanel topPanel = new JPanel(new BorderLayout());
	private JButton btnStart = new JButton("Starta servern");
	private JButton btnStop = new JButton("Stoppa servern");
	private JTextArea txtArea = new JTextArea();
	private JScrollPane scroll;
	private JLabel ipLabel = new JLabel();
	private String consoleText = "";
	private int port;
	private String user;

	public ServerGUI( int port, String user ) {
		frame = new JFrame("Server - Remote Lock");
		this.port = port;
		this.user = user;
		
		scroll = new JScrollPane(txtArea);
		DefaultCaret caret = (DefaultCaret)txtArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		btnPanel.add(btnStart);
		btnPanel.add(btnStop);
		
		topPanel.add(btnPanel, BorderLayout.CENTER);
		topPanel.add(ipLabel, BorderLayout.SOUTH);
		
		panel.add(topPanel, BorderLayout.NORTH);
		panel.add(scroll, BorderLayout.CENTER);
		
		panel.setPreferredSize(new Dimension(425, 500));
		
		ipLabel.setHorizontalAlignment(SwingConstants.CENTER);
		ipLabel.setPreferredSize(new Dimension(200, 20));
		btnPanel.setPreferredSize(new Dimension(125, 50));
		
		btnStop.setEnabled(false);
		txtArea.setEditable(false);

		btnStart.addActionListener(new ButtonListener());
		btnStop.addActionListener(new ButtonListener());
		
		showServerInfo();
		showServerGUI();	
	}
	
	public void showServerGUI() {
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		frame.setLocation(600, 100);
		frame.pack();
	}
	
	public void showServerInfo() {
		try {
			ipLabel.setText("Server-IP: " + InetAddress.getLocalHost().getHostAddress() + " Port: " + port );
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	
	}
	
	public void showText(String str) {
		consoleText += str+"\n";
		txtArea.setText(consoleText);
	}
	
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == btnStart) {
				showText("Server startad\n");
				btnStart.setEnabled(false);
				btnStop.setEnabled(true);
				
				Thread connectThread = new Thread( server = new ListenForClients( port, gui, user ) );
				connectThread.start();
			}
			if(e.getSource() == btnStop) {
				btnStart.setEnabled(true);
				btnStop.setEnabled(false);
				server.terminate();
				showText("Server stängd\n");
			}
		}
		
	}
	
//	public static void main(String[] args) {
//		new ServerGUI();
////		gui.showServerGUI();
//	}
}
