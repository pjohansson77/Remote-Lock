package lock;

/**
 * A main class that starts the client.
 * 
 * @author Peter Johansson, Andree Höög, Jesper Hansen
 */
public class MainClient {
	public static void main( String[] args ) {
		new ConnectGUI( "src/lock/ID.txt" ); 
	}
}
