package lock;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class DeleteID {
	public static void main( String[] args ) {
		try {
			BufferedWriter writer = new BufferedWriter( new FileWriter( "src/lock/ID.txt" ) );

			writer.write( "" );

			writer.close();
		} catch( IOException e ) {
			System.out.println( e );
		}
	}
}
