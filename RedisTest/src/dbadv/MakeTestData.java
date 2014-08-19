package dbadv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class MakeTestData
{
	private final static String SERVER_ADDRESS = "10.73.45.56";
	private final static Random rand = new Random();

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException
	{
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		
		BufferedWriter bw;
		try
		{
			bw = new BufferedWriter( new FileWriter( new File( "test.sql" ) ) );
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
			return;
		}
		
		try
		{
			Connection conn = DriverManager.getConnection( "jdbc:mysql://" + SERVER_ADDRESS + "/redis", "root", "dddd!!@@##$$" );

			Statement stmt = conn.createStatement();
			stmt.execute( "DELETE FROM ctest" );
			stmt.close();
			
			conn.setAutoCommit( false );

			for ( int i = 0; i < 10000; ++i )
			{
				StringBuffer strbuf = new StringBuffer();
				for ( int j = 0; j < 10000; ++j )
				{
					strbuf.append( (char)('a' + rand.nextInt(24)) );
				}
				
				stmt = conn.createStatement();
				String sql = "INSERT INTO ctest VALUES ( " + i + ", '" + strbuf.toString() + "' )";
//				stmt.execute( sql );
				System.out.println( "Row #" + i + " inserted" );
				bw.write( sql + "\n" );
				
//				stmt.close();
			}
			
			bw.close();
			conn.commit();
			conn.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}
