package lock;
import java.sql.*;
import java.util.Random;

public class MySQL {
    public static Connection connection;
    public static Statement statement;
    
    /**
   	 * A static function that connects to a database.
   	 */
    public static void connect() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://195.178.232.7:4040/ad1067","ad1067","Alfa2013");
            statement = connection.createStatement();
        } catch(ClassNotFoundException e1) {
            System.out.println("Databas-driver hittades ej: "+e1);
        }
    }
    
    /**
   	 * A static function that disconnects from a database.
   	 */
    public static void disconnect() throws SQLException {
        statement.close();
        connection.close();
    }
    
    /**
	 * A static function that reads users from
	 * a database and stores them in a hashtable.
	 * 
	 * @param table A hashtable.
	 * @param users A reference to a user.
	 */
    public static void readMySQL( HashtableOH<String, User> table, User user ) {
    	String[] values;
		try {
			connect();
			ResultSet result = statement.executeQuery( "SELECT * FROM ad1067.users" );
			ResultSetMetaData meta = result.getMetaData();
 
			values = new String[ meta.getColumnCount() ];
			while( result.next() ) {
				for( int i = 0; i < values.length; i++ ) {
					values[ i ] = result.getObject( i + 1 ).toString();
				}
				user = new User( values[ 0 ], values[ 1 ], values[ 2 ] );
				table.put( values[ 0 ], user );
			}
			disconnect();
		} catch(SQLException e) {
			System.out.println(e);
		}
    }
    
    /**
	 * A static function that reads a temp username
	 * and a temp password from a database and returns them.
	 * 
	 * @return an array with a temp username and password
	 */
    public static String[] readTempMySQL() {
    	String[] values = null;
		try {
			connect();
			ResultSet result = statement.executeQuery( "SELECT * FROM ad1067.temp" );
			ResultSetMetaData meta = result.getMetaData();
 
			values = new String[ meta.getColumnCount() ];
			while( result.next() ) {
				for( int i = 0; i < values.length; i++ ) {
					values[ i ] = result.getObject( i + 1 ).toString();
				}
			}
			disconnect();
		} catch(SQLException e) {
			System.out.println(e);
		}
		return values;
    }
    
    /**
	 * A static function that writes new users to a database.
	 * 
	 * @param id User id.
	 * @param clientUsername Username.
	 * @param clientPassword User password.
	 */
    public static void writeToMySQL( String id, String username, String password ) {
    	try {
    		connect();
    		
			String insert = "INSERT INTO ad1067.users VALUES ('" + id + "','" + username + "','" + password + "')";
			statement.executeUpdate( insert );

			disconnect();
		} catch(SQLException e) {
			System.out.println(e);
		}
    }
    
    /**
	 * A static function that updates the user password in the database.
	 * 
	 * @param newClientPassword A new user password.
	 * @param id User id.
	 */
    public static void updateMySQL( String newClientPassword, String id ) {
    	try {
    		connect();
    		
			String insert = "UPDATE ad1067.users SET password = '" + newClientPassword + "' WHERE id = '" + id + "'";
			statement.executeUpdate( insert );

			disconnect();
		} catch(SQLException e) {
			System.out.println(e);
		}
    }
    
    /**
	 * A static function that updates the admin password in the database.
	 * 
	 * @param newTempPassword A new temp user password.
	 */
    public static void updateTempMySQL() {
    	String newTempPassword = "";
		Random rand = new Random();
		
		for( int i = 0; i < 8; i++ ) {
			if( rand.nextBoolean() ) {
				newTempPassword += ( char )( rand.nextInt( 26 ) + 'A' );
			} else {
				newTempPassword += ( char )( rand.nextInt( 26 ) + 'a' );
			}
		}
		
    	try {
    		connect();
    		
			String insert = "UPDATE ad1067.temp SET password = '" + newTempPassword + "' WHERE tempusername = 'admin'";
			statement.executeUpdate( insert );

			disconnect();
		} catch(SQLException e) {
			System.out.println(e);
		}
    }
}