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
            DriverManager.setLoginTimeout( 5 );
            connection = DriverManager.getConnection("jdbc:mysql://195.178.232.7:4040/AC9457","AC9457","Fqqhc3kb");
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
			ResultSet result = statement.executeQuery( "SELECT * FROM AC9457.users" );
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
     * A static function that reads temporary password
     * from a database and stores them in a String array
     * @return An array containing temporary passwords
     */
    public static String[] readTempMySQL() {
    	String[] values = null;
		try {
			connect();
			ResultSet result = statement.executeQuery( "SELECT * FROM AC9457.temp" );
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
    		
			String insert = "INSERT INTO ac9457.users VALUES ('" + id + "','" + username + "','" + password + "')";
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
    public static void updateMySQLPassword( String newClientPassword, String id ) {
    	try {
    		connect();
    		
			String insert = "UPDATE ac9457.users SET password = '" + newClientPassword + "' WHERE id = '" + id + "'";
			statement.executeUpdate( insert );
			
			
			
			disconnect();
		} catch(SQLException e) {
			System.out.println(e);
		}
    }
    
    /**
     * A static function that updates the temp password 
     * in the database with a new random password.
     * 
     * @param newTempPassword A new temporary password
     */
    public static void updateTempPWMySQL() {
    	try {
    		String newTempPassword = "";
    		Random rand = new Random();
    		
    		for(int i = 0; i < 8; i++) {
    			if (rand.nextBoolean()) {
    				newTempPassword += (char)(rand.nextInt(26) + 'A');
    			} else {
    				newTempPassword += (char)(rand.nextInt(26) + 'a');
    			}
    		}
    		connect();
    		
			String insert = "UPDATE ac9457.temp SET tempPW = '" + newTempPassword + "' WHERE tempName = 'admin'";
			statement.executeUpdate( insert );
			
			disconnect();
		} catch(SQLException e) {
			System.out.println(e);
		}
    }
    
    /**
     * A static function that deletes a specific user from the database and hashtable
     * @param table A hashtable
     * @param id The ID of the specific user
     */
    public static void deleteSpecificUser(HashtableOH<String, User> table, String id) {
    	try {
			connect();

			String insert = "DELETE FROM AC9457.users WHERE id='" + id + "'";
			statement.executeUpdate( insert );
			
			table.remove( id );
			
			disconnect();
		} catch(SQLException e) {
			System.out.println(e);
		}

    }
    
    /**
     * A static function that deletes all the users from the database
     */
    public static void deleteAllUsers() {
    	try {
			connect();

			String insert = "DELETE FROM AC9457.users";
			statement.executeUpdate( insert );
			
			disconnect();
		} catch(SQLException e) {
			System.out.println(e);
		}
    }

	public static boolean checkDatabase() {
		try{
			connect();
		} catch (SQLException e) {
			return false;
		}
		return true;
	}
}