package lock;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
/**
 * Class that handles the graphic user interface for the admin where he/she can remove users, show passwords etc. 
 * @author Andree Höög, Jesper Hansen, Peter Johansson
 *
 */
public class AdminGUI {
	private JFrame frame = new JFrame("Administration");
	private JPanel pnlCont = new JPanel();
	private JPanel pnlConnect = new JPanel(new GridLayout(2,2));
	private JPanel pnlMainOptions = new JPanel(new BorderLayout());
	private JPanel pnlMainOptionButtons = new JPanel(new GridLayout(1,3));
	private JPanel pnlMainInputSTD = new JPanel();
	private JPanel pnlUserSettings = new JPanel(new BorderLayout());
	private JPanel pnlUserSettingsBtns = new JPanel();
	private JPanel panel = new JPanel(new GridLayout(1,2));
	private JPanel pnlAdminSettings = new JPanel(new BorderLayout());
	private JButton btnLogin = new JButton("Login");
	private JButton btnExit = new JButton("Exit");
	private JButton btnLogout = new JButton("Logout");
	private JButton btnUserSettings = new JButton("User Settings");
	private JButton btnAdminSettings = new JButton("Admin Settings");
	private JButton btnRemoveUser = new JButton("Remove User");
	private JButton btnResetPW = new JButton("Reset Password");
	private JButton btnShowPW = new JButton("Show Current Password");
	private JButton btnUpdate = new JButton("Update List");
	private JButton btnChangeTempPW = new JButton("Change Temp Password");
	private JButton btnShowTempPW = new JButton("Show Temp Password");
	private JTextField txtUsername = new JTextField();
	private JTextArea txtAreaAdmin, txtAreaUser;
	private JList list;
	private JPasswordField txtPassword = new JPasswordField();
	private CardLayout c1 = new CardLayout();
	private Admin admin;
	private ListenForClients server;
	private JScrollPane scrollPane;
	
	/**
	* Constructs a AdminGUI with a reference to the server
	* @param server A reference to the server
	*/
	public AdminGUI(ListenForClients server) {
		this.server = server;
		this.admin = new Admin(this, server);
		pnlCont.setLayout(c1);
		pnlMainInputSTD.setLayout(c1);
		
		txtUsername.setBorder(BorderFactory.createTitledBorder("Username"));
		txtPassword.setBorder(BorderFactory.createTitledBorder("Password"));
		pnlConnect.add(txtUsername);
		pnlConnect.add(txtPassword);
		pnlConnect.add(btnLogin);
		pnlConnect.add(btnExit);
		
		pnlMainOptions.add(pnlMainOptionButtons, BorderLayout.NORTH);
		pnlMainOptions.add(pnlMainInputSTD, BorderLayout.CENTER);
		
		// addar knapparna i nordpanelen
		pnlMainOptionButtons.add(btnLogout);
		pnlMainOptionButtons.add(btnUserSettings);
		pnlMainOptionButtons.add(btnAdminSettings);
		
		
		
		// builds the panel for user settings
		list = new JList(admin.getModel());
		list.setBorder(BorderFactory.createTitledBorder("Users"));
		scrollPane = new JScrollPane(list);	
		pnlUserSettings.add(scrollPane, BorderLayout.CENTER);		
		txtAreaUser = new JTextArea();
		txtAreaUser.setPreferredSize(new Dimension(250,95));
		txtAreaUser.setBorder(BorderFactory.createTitledBorder("Console"));
		txtAreaUser.setEditable(false);
		btnRemoveUser.setPreferredSize(new Dimension(250,35));
		btnShowPW.setPreferredSize(new Dimension(250,35));
		btnUpdate.setPreferredSize(new Dimension(250,35));
		btnResetPW.setPreferredSize(new Dimension(250,35));
		pnlUserSettingsBtns.setPreferredSize(new Dimension(250,250));
		pnlUserSettingsBtns.add(btnShowPW);
		pnlUserSettingsBtns.add(btnResetPW);
		pnlUserSettingsBtns.add(btnRemoveUser);
		pnlUserSettingsBtns.add(btnUpdate);
		pnlUserSettingsBtns.add(txtAreaUser);
		pnlUserSettingsBtns.setBackground(Color.WHITE);
		pnlUserSettings.add(pnlUserSettingsBtns, BorderLayout.EAST);
		
		
		// Builds the panel for admin settings
		txtAreaAdmin = new JTextArea();
		txtAreaAdmin.setPreferredSize(new Dimension(390,70));
		txtAreaAdmin.setEditable(false);
		txtAreaAdmin.setBorder(BorderFactory.createTitledBorder("Console"));
		panel.add(btnShowTempPW);
		panel.add(btnChangeTempPW);
		pnlAdminSettings.add(txtAreaAdmin, BorderLayout.CENTER);
		pnlAdminSettings.add(panel, BorderLayout.SOUTH);
		panel.setPreferredSize(new Dimension(195,35));
		pnlAdminSettings.setBackground(Color.WHITE);
		
		
		
		pnlCont.add(pnlConnect, "1");
		pnlCont.add(pnlMainOptions, "2");
		c1.show(pnlCont, "1");
		
		
		// add listener to buttons
		btnLogin.addActionListener(new ButtonListener());	
		btnLogout.addActionListener(new ButtonListener());		
		btnUserSettings.addActionListener(new ButtonListener());	
		btnAdminSettings.addActionListener(new ButtonListener());
		btnExit.addActionListener(new ButtonListener());
		btnRemoveUser.addActionListener(new ButtonListener());
		btnShowPW.addActionListener(new ButtonListener());
		btnResetPW.addActionListener(new ButtonListener());
		btnChangeTempPW.addActionListener(new ButtonListener());
		btnShowTempPW.addActionListener(new ButtonListener());
		btnUpdate.addActionListener(new ButtonListener());

		
		frame.add(pnlCont);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.pack();
		frame.setSize(400, 100);
		frame.setResizable(false);
		frame.getRootPane().setDefaultButton(btnLogin);
	}
	
	/**
	 * Function that sets the visibility of the frame to 
	 * true or false
	 * @param b boolean
	 */
	public void showFrame(boolean b) {
		frame.setVisible(b);
	}
	
	/**
	 * Function that shows a String in a textfield in adminsettings
	 * @param str String to be shown
	 */
	public void showTextAdmin(String str) {
		txtAreaAdmin.setText(str);
	}
	
	/**
	 * Function that shows a String in a textfield in usersettings
	 * @param str String to be shown
	 */
	public void showTextUser(String str) {
		txtAreaUser.setText(str);
	}	
	
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnLogin) {
				if (txtUsername.getText().equals("admin") && charToString(txtPassword.getPassword()).equals("alfa")) {
					c1.show(pnlCont, "2");
					pnlMainInputSTD.add(pnlUserSettings, "3");
					pnlMainInputSTD.add(pnlAdminSettings, "4");
					frame.setSize(400,50);
				}
				else JOptionPane.showMessageDialog(null, "Wrong username or password");
			}
			if (e.getSource() == btnLogout) {
				c1.show(pnlCont, "1");
				pnlMainInputSTD.remove(pnlUserSettings);
				pnlMainInputSTD.remove(pnlAdminSettings);
				clearAllDisplays();
				frame.setSize(400,100);
			}
			if (e.getSource() == btnUserSettings) {
				c1.show(pnlMainInputSTD, "3");
				frame.setSize(400,320);
				
			}
			if (e.getSource() == btnAdminSettings) {
				c1.show(pnlMainInputSTD, "4");
				frame.setSize(400, 175);
				list.setModel(admin.getModel());
			}
			if (e.getSource() == btnExit) {
				showFrame(false);
				server.enableAdminButton(true);
			}
			
			if (e.getSource() == btnRemoveUser) {
				admin.remove( list.getSelectedIndex() );
				list.setModel(admin.getModel());
			}
			
			if (e.getSource() == btnShowPW) {
				admin.showPassword(list.getSelectedIndex());
			}
			
			if (e.getSource() == btnResetPW) {
				admin.resetPassword(list.getSelectedIndex());
			}
			
			if (e.getSource() == btnChangeTempPW) {
				admin.changeTempPassword();
			}
			
			if (e.getSource() == btnShowTempPW) {
				admin.showTempPassword();
			}
			
			if (e.getSource() == btnUpdate) {
				clearAllDisplays();
				list.setModel(admin.getModel());
			}
			
		}
		
	}	
	
	/**
	 * Function that converts an array of chars to a String
	 * @param pw an array of chars
	 * @return password the array as a String instead
	 */
	private String charToString(char[] pw) {
		String password = "";
		for(int i = 0; i<pw.length; i++) {
			password += pw[i];
		}
		return password;
	}
	
	/**
	 * Clears the textarea in user settings and admin settings
	 * + clears the passwordfield
	 */
	private void clearAllDisplays() {
		showTextUser("");
		showTextAdmin("");
		txtPassword.setText("");
	}
	
}
