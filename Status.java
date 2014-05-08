package lock;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Status implements Runnable {
	private DataInputStream input;
	private String message;
	private ChoicesGUI choice;

	public Status( ChoicesGUI choice, DataInputStream input ) {
		this.input = input;
		this.choice = choice;
	}

	public void run() {
		while( true ) {
			try {
				message = input.readUTF();
				choice.setInfoDisplay( "Door " + message );
				doorStatus();
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
