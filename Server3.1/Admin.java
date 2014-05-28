package lock;

import java.util.Iterator;
import java.util.Random;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 * An Admin class that handles all the requests from the AdminGUI
 * 
 * @author Andree Höög, Jesper Hansen, Peter Johansson
 */
public class Admin {
	private AdminGUI gui;  
	private User[] users;
	private HashtableOH <String, User> table;
	private final String SELECT_USER = "You have to select a user!";

	/**
	 * A constructor that receives a reference to the AdminGUI
	 * and a reference to the ListenForClient which contains the hashtable
	 * 
	 * @param gui a reference to the AdminGUI-class
	 * @param server a reference to the ListenForClients-class
	 */
	public Admin(AdminGUI gui, ListenForClients server) {
		this.gui = gui;
		this.table = server.getTable();
	}

	/**
	 * A function that updates and creates a list of users from the hashtable
	 */
	public void updateUserList() {
		int index = 0;
		users = new User[table.size()];
		Iterator<User> values = table.values();
		while(values.hasNext()) {
			users[index] = values.next();
			index++;
		}
	}

	/**
	 * A function that returns a username from an array.
	 * 
	 * @param selectedIndex index in an array.
	 * @return a username.
	 */
	public String getUsername( int selectedIndex ) {
		return users[selectedIndex].getName();
	}

	/**
	 * A function that creates a new DefaultListModel and adds the
	 * names of all users to it.
	 * 
	 * @return listModel a DefaultListModel containing all the names of the users. 
	 */
	public DefaultListModel getModel() { 
		updateUserList();
		DefaultListModel listModel = new DefaultListModel();
		for (int i=0; i< users.length; i++) {
			listModel.addElement(users[i].getName());
		}
		return listModel;

	}

	/**
	 * A function that removes a user from the database and the hashtable
	 * 
	 * @param selectedIndex the index of the user in the list 
	 */
	public void remove(int selectedIndex) {
		getModel().remove(selectedIndex);
		gui.updateList();
		MySQL.deleteSpecificUser( table, users[selectedIndex].getID() );
		gui.showTextUser("User: " + users[selectedIndex].getName() + "\nhas been removed.");
	}

	/**
	 * A function that resets the password for a specific user
	 * 
	 * @param selectedIndex the index for a specific user in the array
	 */
	public void resetPassword(int selectedIndex) {
		MySQL.updateMySQLPassword(MD5.encryption("0000"), users[selectedIndex].getID());
		table.get( users[selectedIndex].getID() ).setPassword( MD5.encryption("0000") );
		gui.showTextUser("Password has been reset for\nuser: " + users[selectedIndex].getName() + ".");
	}

	/**
	 * A function that changes the temporary password to a random 
	 * generated password, then stores it in the database
	 */
	public void changeTempPassword() {
		MySQL.updateTempPWMySQL();
		gui.showTextAdmin("Temporary password changed!");	
	}

	/**
	 * A function that shows the current temporary password in the database
	 */
	public void showTempPassword() {	
		String[] tempNamePW = MySQL.readTempMySQL();
		String username = tempNamePW[0];
		String password = tempNamePW[1];		
		gui.showTextAdmin("Username: " + username + "\nPassword: " + password);
	}	
}
