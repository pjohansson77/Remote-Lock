package lock;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class that handles encryption functions.
 * 
 * @author Peter Johansson, Andree Höög, Jesper Hansen
 */
public class MD5 {
	public static String encryption( String message ){ 
		String digest = ""; 
		try { 
			MessageDigest md = MessageDigest.getInstance( "MD5" ); 
			byte[] hash = md.digest( message.getBytes( "UTF-8" ) ); 
			
			for( int i = 0; i < hash.length; i++ ){ 
				digest += Integer.toHexString( 0xff & hash[ i ] ); 
			} 
		} catch (UnsupportedEncodingException ex) {
		} catch (NoSuchAlgorithmException ex) {}
		return digest;
	}
}
