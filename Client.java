package test;

import java.net.*;
import java.io.*;

import javax.swing.JOptionPane;

public class Client {
	private String serverIP;
	private int serverPort;
	private Socket socket;
	private ClientGUI gui;
	boolean connected = true;
	private LoginGUI gui2;
	private LoginNewUserGUI gui3;
	private DataOutputStream output;
	private DataInputStream input;
	private Client client;

	public Client( String serverIP, int serverPort, ClientGUI gui ) { 
		this.serverIP = serverIP;
		this.serverPort = serverPort;
		this.gui = gui;
		client = this;
		Thread thread = new Thread( new ConnectToServer() ); 
		thread.start();
	}
	
	private String getMacAdress( Socket socket ) {
		String macAdress = "";

		try {
			NetworkInterface net = NetworkInterface.getByInetAddress( socket.getLocalAddress());
			if( net != null ) {
				byte[] mac = net.getHardwareAddress();
				for ( int i = 0; i < mac.length; i++ ) {
					macAdress += String.format("%02x", mac[i]) + ":";   
				}
				macAdress = macAdress.substring( 0, macAdress.length() - 1 );
			} else
				macAdress = "null";           
		} catch( IOException e1 ) {
			System.out.println( e1 );
		} 
		return macAdress;
	}

	private class ConnectToServer implements Runnable {
		String message;
//		boolean connected = false;

		public void run() {
			try {
				socket = new Socket( InetAddress.getByName( serverIP ), serverPort );
				output = new DataOutputStream( socket.getOutputStream() );

				output.writeUTF( "1337" );
				output.flush(); 
				
				input = new DataInputStream( socket.getInputStream() );
				message = input.readUTF();
				
				if( message.equals( "Connected" ) ) {
					gui2 = new LoginGUI( client );
					gui2.setInfoDisplay( message );
				} else {
					gui3 = new LoginNewUserGUI( client );
					gui3.setInfoDisplay( message );
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
	
	private class LoginToServer implements Runnable {
		String message, respons, password;
		
		public LoginToServer( String password ) {
			this.password = password;
		}
		
		public void run() {
			try {
				
				output.writeUTF( password );
				output.flush();

//				connected = input.readBoolean();
				message = input.readUTF(); // Andreas fel ;-)
				if( message.equals( "sant" ) ) {
					gui2.setInfoDisplay( "Inloggad" );
					while( connected ) {
						respons = JOptionPane.showInputDialog( "1. Lampa1\n2. Lampa2\n4. Disco\n0. Logga ut" );
						if( respons.equals("0") ) {
							connected = false;
							logOut();
						} else {
							output.writeUTF( respons );
							output.flush();
							message = input.readUTF();
							gui2.setInfoDisplay( message );
						}
					}
				} else {
					System.out.println( "Fel användarnamn eller lösenord" );
					logOut();
				} 
			} catch(Exception e1 ) {
				System.out.println( e1 );
			}
		}
	}
	
	private class LoginNewUserToServer implements Runnable {
		String message, respons, username, password;
		
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

//				connected = input.readBoolean();
				message = input.readUTF(); // Andreas fel ;-)
				if( connected ) {
					gui3.setInfoDisplay( "Inloggad" );
					while(connected) {
						respons = JOptionPane.showInputDialog( "1. Lampa1\n2. Lampa2\n4. Disco\n0. Logga ut" );
						if( respons.equals("0") ) {
							connected = false;
							logOutNewUser();
						} else {
							output.writeUTF( respons );
							output.flush();
							message = input.readUTF();
							gui3.setInfoDisplay( message );
						}
					}
				} else {
					System.out.println( "Fel användarnamn eller lösenord" );
					logOutNewUser();
				} 
			} catch(Exception e1 ) {
				System.out.println( e1 );
			}
		}
	}
	
	public void logOut() {
		try {
			gui2.frameStatus( false );
			gui.frameStatus( true );
			gui.setInfoDisplay( "Utloggad" );
			socket.close();
		} catch( IOException e ) {
			System.out.println( e );
		}
	}
	
	public void logOutNewUser() {
		try {
			gui3.frameStatus( false );
			gui.frameStatus( true );
			gui.setInfoDisplay( "Utloggad" );
			socket.close();
		} catch( IOException e ) {
			System.out.println( e );
		}
	}
}