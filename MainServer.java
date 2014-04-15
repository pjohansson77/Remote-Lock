package test;

/**
 * A main class that starts the server.
 * @author Jesper Hansen, Peter Johansson, Andree Höög, Qasim Ahmad, Andreas Flink, Gustav Frigren
 *
 */
public class MainServer {

	/**
	 * A constructor that starts a new connect thread.
	 * @param port The port that the server listens on.
	 */
	public MainServer( int port ) {
		Thread connectThread = new Thread( new ConnectToServer( port ) ); 
		connectThread.start();
	}
	
	public static void main( String[] args ) {
		new MainServer( 5555 );
	} 
}