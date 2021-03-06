package lock;

import java.io.DataOutputStream;
import java.net.Socket;

/**
 * A class that constantly checks the arduino status 
 * and sends it back to the client if it changes.
 * 
 * @author Peter Johansson, Andree Höög, Jesper Hansen
 */
public class DoorStatus implements Runnable {
	private Socket clientSocket;
	private int counter = 0;
	private TalkToArduino talkToArduinoClient, talkToArduinoStatus;
	private DataOutputStream ClientOutput;

	/**
	 * A class constructor that gets the current socket, current streams and a reference to the ArduinoChoices class.
	 * 
	 * @param socket The active socket.
	 * @param output The active OutputStream.
	 * @param gui A reference to the TalkToArduino class.
	 */
	public DoorStatus( Socket socket, DataOutputStream output, TalkToArduino talkToArduinoClient ) {
		this.ClientOutput = output;
		this.talkToArduinoClient = talkToArduinoClient;
		talkToArduinoStatus = new TalkToArduino();
		this.clientSocket = socket;
	}

	/**
	 * A function that checks the arduino status and informs the client if it changes.
	 */
	public void run() {
		while( !clientSocket.isClosed() ) {
			try{	
				if( counter < 1 ) {
					talkToArduinoStatus.setArduino( talkToArduinoClient.getArduino() );
					talkToArduinoStatus.arduinoLock( "8" );	
					if( talkToArduinoClient.getArduinoStatus() != talkToArduinoStatus.getArduinoStatus() ) {
						talkToArduinoClient.setArduinoStatus( talkToArduinoStatus.getArduinoStatus() );
						counter++;
						talkToArduinoStatus.statusToClient( ClientOutput );
					}
					Thread.sleep( 2000 ); // Controls the time that checks arduino status
				} else {
					talkToArduinoStatus.arduinoLock( "8" );	
					talkToArduinoStatus.statusToClient( ClientOutput );
					Thread.sleep( 2000 );
				}
			} catch(Exception e) {}
		}
	}

	/**
	 * A function that resets the counter if the client responds.
	 */
	public void resetCounter() {
		counter = 0;
	}
}
