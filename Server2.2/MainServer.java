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
				new ServerGUI( 5555 );
			}
		});
	} 
}