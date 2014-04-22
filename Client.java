package lock;

import java.net.*;
import java.io.*;

public class Client {
	private String serverIP;
	private int serverPort;
	private Socket socket;
	private ConnectGUI gui;
	boolean connected = true;
	private LoginGUI gui2;
	private LoginNewUserGUI gui3;
	private LoginInfoGUI gui4;
	private DataOutputStream output;
	private DataInputStream input;
	private Client client;
	private ClientID id;
	private ChoicesGUI choice;

	public Client( String serverIP, int serverPort, ConnectGUI gui, ClientID id ) { 
		this.serverIP = serverIP;
		this.serverPort = serverPort;
		this.gui = gui;
		this.id = id;
		client = this;
		Thread thread = new Thread( new ConnectingToServer() ); 
		thread.start();
	}

	private class ConnectingToServer implements Runnable {
		String message;

		public void run() {
			try {
				socket = new Socket( InetAddress.getByName( serverIP ), serverPort );
				output = new DataOutputStream( socket.getOutputStream() );

				output.writeUTF( id.getID() );
				output.flush(); 

				input = new DataInputStream( socket.getInputStream() );
				message = input.readUTF();

				if( message.equals( "connected" ) ) {
					gui2 = new LoginGUI( client, gui );
					gui2.setInfoDisplay( "Welcome" );
					gui2.setStatusDisplay( "Pending" );
				} else if( message.equals( "newuser" ) ) {
					gui3 = new LoginNewUserGUI( client, gui );
					gui3.setInfoDisplay( "New user" );
					gui3.setStatusDisplay( "Pending" );
				} else {
					try {
						socket.close();
					} catch( IOException e ) {
						System.out.println( e );
					}
				}

			} catch(Exception e1 ) {
				System.out.println( e1 );
			}
		}
	}

	public void startLogin( String password ) {
		Thread newThread = new Thread( new LoginToServer( password ) );
		newThread.start();
	}

	public void startNewUserLogin( String username, String password ) {
		Thread newThread = new Thread( new LoginNewUserToServer( username, password ) );
		newThread.start();
	}
	
	public void startInfoLogin( String username, String password ) {
		try {
			output.writeUTF( username );
			output.flush();

			output.writeUTF( password );
			output.flush();

			gui4.hideFrame();
			gui2 = new LoginGUI( client, gui );
			gui2.setStatusDisplay( "Tillagd i server" );
		} catch(Exception e1 ) {
			System.out.println( e1 );
		}
	}

	private class LoginToServer implements Runnable {
		String message, password;

		public LoginToServer( String password ) {
			this.password = password;
		}

		public void run() {
			try {
				output.writeUTF( password );
				output.flush();

				message = input.readUTF();
				if( message.equals( "passwordtrue" ) ) {
					gui2.hideFrame();
					choice = new ChoicesGUI( client );
					choice.setStatusDisplay("Connected" );
				} else {
					gui.setInfoDisplay( "Fel användarnamn eller lösenord" );
					gui2.hideFrame();
					disconnect();
				} 
			} catch(Exception e1 ) {
				System.out.println( e1 );
			}
		}
	}

	public void choices( String str ) {
		String arduinoChoice, message;
		try {
			arduinoChoice = str;
			if( arduinoChoice.equals( "0" ) ) {
				connected = false;
				choice.hideFrame();
				gui.setInfoDisplay( "" );
				disconnect();
			} else {
				output.writeUTF( arduinoChoice );
				output.flush();
				message = input.readUTF();
				choice.setInfoDisplay( message );
			}
		} catch(Exception e1 ) {
			System.out.println( e1 );
		}
	}

	private class LoginNewUserToServer implements Runnable {
		String message, username, password;

		public LoginNewUserToServer( String username, String password ) {
			this.username = username;
			this.password = password;
		}

		public void run() {
			try {
				output.writeUTF( username );
				output.flush();

				output.writeUTF( password );
				output.flush();

				message = input.readUTF();
				if( message.equals( "tempfalse" ) ) {
					gui.setInfoDisplay( "Fel användarnamn eller lösenord" );
					gui3.hideFrame();
					disconnect();
				} else {
					id.setID( message );
					gui3.hideFrame();
					gui4 = new LoginInfoGUI( client, gui );
//					gui2.setInfoDisplay( "Ditt lösenord: " + message );
//					gui2.setStatusDisplay( "Tillagd i server" );
				}
			} catch(Exception e1 ) {
				System.out.println( e1 );
			}
		}
	}

	public void disconnect() {
		try {
			gui.frameStatus( true );
			gui.setStatusDisplay( "Disconnected" );
			socket.close();
		} catch( IOException e ) {
			System.out.println( e );
		}
	}
}