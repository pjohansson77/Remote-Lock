package test;

/**
 * A main class that starts the server.
 * 
 * @author Jesper Hansen, Peter Johansson, Andree Höög, Qasim Ahmad, Andreas Flink, Gustav Frigren
 */
public class MainServer {	
	public static void main( String[] args ) {
		Thread connectThread = new Thread( new ListenForClients( 5555 ) );
		connectThread.start();
	} 
}