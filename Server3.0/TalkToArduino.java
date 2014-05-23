package lock;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Random;

public class TalkToArduino {
	private String status, arduino = "1";
	private int num, arduinoStatus = 0;
	private Socket arduinoSocket;
	private DataInputStream arduinoInput;
	private DataOutputStream arduinoOutput;
	private Random rand = new Random();
	
	/**
	 * A function that gets the current arduino lock.
	 * 
	 * @return 1 or 2.
	 */
	public String getArduino() {
		return arduino;
	}
	
	/**
	 * A function that sets the current arduino lock.
	 * 
	 * @param arduino sets the arduino lock in use.
	 */
	public void setArduino( String arduino ) {
		this.arduino = arduino;
	}
	
	/**
	 * A function that gets the current arduino status.
	 * 
	 * @return current arduino status from the arduino as an int.
	 */
	public int getArduinoStatus() {
		return arduinoStatus;
	}

	/**
	 * A function that sets the current arduino status.
	 * 
	 * @param arduinoStatus sets current arduino status in use.
	 */
	public void setArduinoStatus( int arduinoStatus ) {
		this.arduinoStatus = arduinoStatus;
	}
	
	/**
	 * A function that gets the current status.
	 * 
	 * @return current arduino status from the arduino as a String.
	 */
	public String getStatus() {
		return status;
	}
	
	/**
	 * A function that communicates with the arduino.
	 * 
	 * @param message A string that contains a message for the arduino.
	 */
	public void arduinoLock( String message ) {
		num = Integer.parseInt( message );
		try{
			if( arduino.equals( "1" ) ) {
				arduinoSocket = new Socket();
				arduinoSocket.connect(new InetSocketAddress( "10.228.0.123", 6666 ), 5000 );

				arduinoOutput = new DataOutputStream( arduinoSocket.getOutputStream() );
				arduinoInput = new DataInputStream( arduinoSocket.getInputStream() );

				arduinoOutput.write( num ); // Message to Arduino
				arduinoOutput.flush();
				arduinoStatus = arduinoInput.read(); // Message from Arduino

				arduinoOutput.write( 0 );
				arduinoOutput.flush();
				arduinoSocket.close();
			} else {
				if( num == 8 ) {
					num = rand.nextInt( 5 ) + 1;
				}
				arduinoStatus = num;
			}
		} catch(IOException e) {
			try{
				arduinoStatus = 4;
				arduinoSocket.close();
			} catch(IOException e2) {}
		}
	}

	/**
	 * A function that sends the arduinostatus to the client.
	 */
	public void statusToClient( DataOutputStream clientOutput ) {
		try{
			if( arduinoStatus == 1 ) {
				status = "unlocked";
			} else if( arduinoStatus == 2 ) {
				status = "locked";
			}else if( arduinoStatus == 3 ) {
				status = "open";
			} else if( arduinoStatus == 4 ){
				status = "unreachable";
			} else if( arduinoStatus == 5 ){
				status = "powerdown";
			}
			clientOutput.writeUTF( status );
			clientOutput.flush();
		} catch(IOException e) {}
	}
}
