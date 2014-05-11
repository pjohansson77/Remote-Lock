package lock;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;

/**
 * A class that constantly checks the arduino status 
 * and sends it back to the client if it changes status.
 * 
 * @author Peter Johansson, Jesper Hansen, Andree Höög
 */
public class DoorStatus implements Runnable {
	private Socket clientSocket, arduinoSocket;
	private DataInputStream arduinoInput;
	private DataOutputStream clientOutput, arduinoOutput;
	private int arduinoStatus, counter = 0;
	private String status;
	private ArduinoChoices choices;


	/**
	 * A class constructor that gets the current socket, current streams and a reference to the ArduinoChoices class.
	 * 
	 * @param socket The active socket.
	 * @param output The active OutputStream.
	 * @param gui A reference to the ArduinoChoices class.
	 */
	public DoorStatus( Socket socket, DataOutputStream output, ArduinoChoices choices ) {
		this.clientSocket = socket;
		this.clientOutput = output;
		this.choices = choices;
	}

	/**
	 * A function that checks the arduino status and informs the client if it changes.
	 */
	public void run() {
		while( !clientSocket.isClosed() ) {
			try{
				if( InetAddress.getByName( "10.228.0.123" ).isReachable(1000) ) {
					arduinoSocket = new Socket( InetAddress.getByName( "10.228.0.123" ), 6666 );

					arduinoOutput = new DataOutputStream( arduinoSocket.getOutputStream() );
					arduinoInput = new DataInputStream( arduinoSocket.getInputStream() );

					arduinoOutput.write( 8 ); // Message to Arduino
					arduinoOutput.flush();
					arduinoStatus = arduinoInput.read(); // Message from Arduino
					
					arduinoOutput.write( 0 );
					arduinoOutput.flush();
					arduinoSocket.close();
				} else { // Tas bort sen
					
					Random rand = new Random(); // Tas bort sen
					int num = rand.nextInt( 3 ) + 1; // Tas bort sen
					arduinoStatus = num; // Tas bort sen
					if( counter < 1) {
						if( choices.getArduinoStatus() != arduinoStatus ) {
							choices.setArduinoStatus( arduinoStatus );
							counter++;
							statusToClient();
							System.out.println( "Ändrad status" );
						} else {
							System.out.println( "Ändrar inte status" );
						}
						Thread.sleep(10000);
					} else {
						System.out.println( "Stänger tråd" );
						clientSocket.close();
					}
				}
			} catch(Exception e) {}
		}
		System.out.println( "Socket stängd" );
	}

	/**
	 * A private function that sends the arduinostatus to the client.
	 */
	private void statusToClient() {
		try{
			if( arduinoStatus == 1 ) {
				status = "unlocked";
			} else if( arduinoStatus == 2 ) {
				status = "locked";
			}else if( arduinoStatus == 3 ) {
				status = "open";
			}
			clientOutput.writeUTF( status );
			clientOutput.flush();
		} catch(IOException e) {}
	}
	
	/**
	 * A function that resets the counter if the client responds.
	 */
	public void resetCounter() {
		counter = 0;
	}
}
