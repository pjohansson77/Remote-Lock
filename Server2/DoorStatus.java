package lock;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;

import javax.swing.Timer;

public class DoorStatus implements Runnable {
	private Socket clientSocket, arduinoSocket;
	private DataInputStream arduinoInput;
	private DataOutputStream clientOutput, arduinoOutput;
	private int arduinoStatus;
	private String status;
	private ArduinoChoices choices;


	public DoorStatus( Socket socket, DataOutputStream output, ArduinoChoices choices ) {
		this.clientSocket = socket;
		this.clientOutput = output;
		this.choices = choices;
	}

	public void run() {
		while( !clientSocket.isClosed() ) {
			try{
//				arduinoSocket = new Socket( InetAddress.getByName( "192.168.1.104" ), 6666 );
//
//				arduinoOutput = new DataOutputStream( arduinoSocket.getOutputStream() );
//				arduinoInput = new DataInputStream( arduinoSocket.getInputStream() );
//
//				arduinoOutput.write( 8 ); // Message to Arduino
//				arduinoOutput.flush();
//				arduinoStatus = arduinoInput.read(); // Message from Arduino
				Random rand = new Random();
				int num = rand.nextInt( 3 ) + 1;
				if( choices.getArduinoStatus() != num ){
					arduinoStatus = num; // Tas bort sen
					choices.setArduinoStatus( arduinoStatus );
					statusToClient();
					System.out.println( "Ändrad status" );
				} else {
					System.out.println( "Ingen ändrad status" );
				}
				Thread.sleep(5000);
			} catch(Exception e) {}
		}
	}

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
}
