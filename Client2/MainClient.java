package lock;

/**
 * A main class that starts the client.
 * 
 * @author Jesper Hansen, Peter Johansson, Andree Höög, Qasim Ahmad, Andreas Flink, Gustav Frigren
 */
public class MainClient {
	public static void main( String[] args ) {
		new ConnectGUI( "src/lock/ID.txt" ); 
	}
}
