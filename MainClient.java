package lock;

import javax.swing.SwingUtilities;

public class MainClient {
	public static void main( String[] args ) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() { 
				new ConnectGUI( "src/lock/ID.txt" );
			}
		});
	}
}
