package lock;

/**
 * A class that acts as a heartbeat so the arduino knows the server is connected.
 * 
 * @author Peter Johansson, Andree Höög, Jesper Hansen
 */
public class HeartBeat implements Runnable {
	private TalkToArduino status;
	private boolean check = true;

	/**
	 * A class constructor for the HeartBeat class.
	 */
	public HeartBeat() {
		status = new TalkToArduino();
	}
	
	/**
	 * A function that stops the heartbeat.
	 */
	public void stopArduinoCheck() {
		check = false;
	}

	/**
	 * A function that connects to the arduino so it knows that the server is connected.
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
