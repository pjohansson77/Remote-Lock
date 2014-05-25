package lock;

/**
 * A class that constantly checks the arduino status 
 * and sends it back to the client if it changes.
 * 
 * @author Peter Johansson, Andree Höög, Jesper Hansen
 */
public class HeartBeat implements Runnable {
	private TalkToArduino status;
	private boolean check = true;

	/**
	 * A class constructor that gets the current socket, current streams and a reference to the ArduinoChoices class.
	 * 
	 * @param socket The active socket.
	 * @param output The active OutputStream.
	 * @param gui A reference to the TalkToArduino class.
	 */
	public HeartBeat() {
		status = new TalkToArduino();
	}
	
	public void stopArduinoCheck() {
		check = false;
	}

	/**
	 * A function that checks the arduino status and informs the client if it changes.
	 */
	public void run() {
		while( check ) {
			try{	
				status.setArduino( "1" );
				status.arduinoLock( "8" );	
				Thread.sleep( 15000 );
			} catch(Exception e) {}
		}
	}
}
