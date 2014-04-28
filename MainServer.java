package lock;

import javax.swing.SwingUtilities;

/**
 * A main class that starts the server.
 * 
 * @author Jesper Hansen, Peter Johansson, Andree Höög
 */
public class MainServer {	
	public static void main( String[] args ) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() { 
				Thread connectThread = new Thread( new ListenForClients( 5555 ) );
				connectThread.start();
			}
		});
	} 
}