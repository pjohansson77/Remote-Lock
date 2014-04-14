package test;

import java.net.*;
import java.util.*;
import java.io.*;

public class Server {
	private int port;
	boolean connected = false;

	public Server( int port ) {
		this.port = port;
		Thread connectThread = new Thread( new Connect() ); 
		connectThread.start();
	}

	private Date getTime() {
		Calendar cal = Calendar.getInstance();
		return cal.getTime();
	}

	//	private String getMacAdress( Socket socket ) {
	//		String macAdress = "";
	//
	//		try {
	//			NetworkInterface net = NetworkInterface.getByInetAddress( socket.getInetAddress() );
	//			if( net != null ) {
	//				byte[] mac = net.getHardwareAddress();
	//				for ( int i = 0; i < mac.length; i++ ) {
	//					macAdress += String.format("%02x", mac[i]) + ":";   
	//				}
	//				macAdress = macAdress.substring( 0, macAdress.length() - 1 );
	//			} else
	//				macAdress = "null";           
	//		} catch( IOException e1 ) {
	//			System.out.println( e1 );
	//		} 
	//		return macAdress;
	//	}

	private class Connect implements Runnable {
		ServerSocket serverSocket;
		Socket socket;
		String mac;
		DataOutputStream output;
		DataInputStream input;

		public void run() {
			try {
				serverSocket = new ServerSocket( port ); 
				while( true ) {
					socket = serverSocket.accept();
					connected = true;

					input = new DataInputStream( socket.getInputStream() );
					mac = input.readUTF();

					if( mac.equals( "" ) ) {
						output = new DataOutputStream( socket.getOutputStream() );
						output.writeUTF("Connected");
						output.flush();
						Thread clientThread = new Thread( new ConnectAndListenToClient( socket, output, input ) );
						clientThread.start();
					} else {
						output = new DataOutputStream( socket.getOutputStream() );
						output.writeUTF("Unknown");
						output.flush();
						Thread clientThread = new Thread( new ConnectNewAndListenToClient( socket, output, input ) );
						clientThread.start();
					}
					System.out.println( "Connected: " + getTime() + "\nIP-adress: " + socket.getInetAddress().getHostAddress()
							+ "\nMac-adress: " + mac + "\n" );
				}
			} catch( IOException e1 ) {
				System.out.println( e1 );
			}
			try {
				serverSocket.close();
			} catch( Exception e ) {}
		}
	}

	private class ConnectAndListenToClient implements Runnable { 
		Socket socket;
		DataInputStream input;
		DataOutputStream output;
		String password;

		public ConnectAndListenToClient( Socket socket, DataOutputStream output, DataInputStream input ) {
			this.socket = socket;
			this.input = input;
			this.output = output;
		}

		public void run() {
			try {
				password = input.readUTF();
				//				System.out.println( socket.getInetAddress().getHostName() + " sent: " + message + "\n" );
				if( password.toLowerCase().equals( "alfa" ) ) {
					//					System.out.println("Sant");
					output.writeBoolean( true );
					//					output.writeUTF("sant");
					output.flush();
					choices( socket, output, input );
				} else {
					//					System.out.println("Falskt");
					output.writeBoolean( false );
					//					output.writeUTF("falskt");
					output.flush();
				}
			} catch(IOException e) {} 
			try {
				System.out.println( "Disconnected: " + getTime() + "\nIP-adress: " + socket.getInetAddress().getHostAddress() + "\n" );
				socket.close();
			} catch( Exception e ) {}
		} 
	}

	private class ConnectNewAndListenToClient implements Runnable { 
		Socket socket;
		DataInputStream input;
		DataOutputStream output;
		String username, password;

		public ConnectNewAndListenToClient( Socket socket, DataOutputStream output, DataInputStream input ) {
			this.socket = socket;
			this.input = input;
			this.output = output;
		}

		public void run() {
			try {
				username = input.readUTF();
				password = input.readUTF();
				//				System.out.println( socket.getInetAddress().getHostName() + " sent: " + message + "\n" );
				if( username.toLowerCase().equals( "admin" ) && password.toLowerCase().equals( "alfa" ) ) {
					//					System.out.println("Sant");
					output.writeBoolean( true );
					//					output.writeUTF("sant");
					output.flush();
					choices( socket, output, input );
				} else {
					//				System.out.println("Falskt");
					output.writeBoolean( false );
					//				output.writeUTF("falskt");
					output.flush();
				} 
			} catch(IOException e) {} 
			try {
				System.out.println( "Disconnected: " + getTime() + "\nIP-adress: " + socket.getInetAddress().getHostAddress() + "\n" );
				socket.close();
			} catch( Exception e ) {}
		} 
	}
	
	private void choices( Socket socket, DataOutputStream output, DataInputStream input ) {
		String message;
		int num;
		try{
			while( connected ) {
				message = input.readUTF();
				num = Integer.parseInt( message );
				if( num == 0 ) {
					connected = false;
				} else {
					System.out.println( "IP-adress: " + socket.getInetAddress().getHostAddress() + " sent: " + message + "\n" );
					if( num == 1 ) {
						output.writeUTF( "Lampa1" );
					//								System.out.println( "Lampa1" );
					}
					else if( num == 2 ) {
						output.writeUTF( "Lampa2" );
					//								System.out.println( "Lampa2" );
					}
					else if( num == 4 ) {
						output.writeUTF( "Disco" );
					//								System.out.println( "Disco" );
					} 
					else {
						output.writeUTF("Fel val");
					//								System.out.println( "Fel val" );
					}
					output.flush();
				} 
			} 
		} catch(IOException e) {}
	}

	public static void main( String[] args ) {
		new Server( 5555 );
	} 
}