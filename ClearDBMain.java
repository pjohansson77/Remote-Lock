package lock;

import java.sql.SQLException;
import java.sql.Statement;

public class ClearDBMain {
	public static void main( String[] args ) {
		try {
			Statement statement = MysqlDB.connect();

			String insert = "DELETE FROM ad1067.users";
			statement.executeUpdate( insert );
			
			MysqlDB.disconnect();
		} catch(SQLException e) {
			System.out.println(e);
		}
	}
}
