package test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * A class that prints out the users choices and sends the choice to the ArduinoClient class.
 * @author Jesper Hansen, Peter Johansson, Andree Höög, Qasim Ahmad, Andreas Flink, Gustav Frigren
 * 
 */
public class Choices {
	private String message;
	private int num;
	private boolean connected = true;
	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;

	/**
	 * A class constructor that gets the current socket and streams.
	 * @param socket The active socket.
	 * @param output The active OutputStream.
	 * @param input The active InputStream.
	 */
	public Choices( Socket socket, DataOutputStream output, DataInputStream input ) {
		this.socket = socket;
		this.input = input;
		this.output = output;
	}

	/**
	 * A method that sends the clients choices to the ArduinoClient class.
	 *
	 */
	public void arduinoChoices() {
		try{
			while( connected ) {

				message = input.readUTF();

				num = Integer.parseInt( message );
//				ArduinoClient arduino = new ArduinoClient("169.254.146.12", 6666);
//				arduino.TalkToArduino(num);

				if( num == 0 ) {
					connected = false;
				} else {
					System.out.println( "IP-adress: " + socket.getInetAddress().getHostAddress() + " sent: " + message + "\n" );
					if( num == 1 ) {
						output.writeUTF( "Lampa1" );
					}
					else if( num == 2 ) {
						output.writeUTF( "Lampa2" );
					}
					else if( num == 4 ) {
						output.writeUTF( "Disco" );
					} 
					else {
						output.writeUTF("Fel val");
					}
					output.flush();
				} 
			} 
		} catch(IOException e) {}
	}
}
