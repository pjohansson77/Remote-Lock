package lock;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.swing.JOptionPane;

public class Status implements Runnable {
	private DataOutputStream output;
	private DataInputStream input;
	private String message, newPassword;
	private ChoicesGUI choice;

	public Status( ChoicesGUI choice, DataOutputStream output, DataInputStream input ) {
		this.output = output;
		this.input = input;
		this.choice = choice;
	}

	public void run() {
		while( true ) {
			try {
				message = input.readUTF();
				if( message.equals( "sendnewpassword" ) ) {
					newPassword = JOptionPane.showInputDialog( "Type new password:" );
					output.writeUTF( newPassword );
					output.flush();
				} else if( message.equals( "wrongpassword" ) ){
					choice.setInfoDisplay( "Wrong password" );
				} else if( message.equals( "passwordchanged" ) ){
					choice.setInfoDisplay( "Password changed" );
				} else {
					choice.setInfoDisplay( "Door " + message );
					doorStatus();
				}
			} catch(Exception e) {}
		}
	}
	
	private void doorStatus() {
		if( message.equals( "unlocked" ) ) {
			choice.lockedChoice();
		} else if( message.equals( "locked" ) ) {
			choice.unlockedChoice();
		} 
	}
}
