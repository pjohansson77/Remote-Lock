package lock;

import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class DeleteSpecificDBMain {
	public static void main( String[] args ) {
		try {
			Statement statement = MysqlDB.connect();

			String insert = "DELETE FROM ad1067.users WHERE id='" + JOptionPane.showInputDialog( "Skriv id:" ) + "'";
			statement.executeUpdate( insert );
			
			MysqlDB.disconnect();
		} catch(SQLException e) {
			System.out.println(e);
		}
	}
}
