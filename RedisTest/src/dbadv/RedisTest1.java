package dbadv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RedisTest1
{
	private final static String SERVER_ADDRESS = "10.73.45.56";

	public static void main(String[] args) throws NumberFormatException, IOException
	{
		BufferedReader br = new BufferedReader( new FileReader( new File( "input.txt" ) ) );
		List<Integer> list = new ArrayList<Integer>();

		String str;
		while ( ( str = br.readLine() ) != null && str != "" )
		{
			list.add( Integer.parseInt( str ) );
		}
		br.close();
		
		try
		{
			Connection conn = DriverManager.getConnection( "jdbc:mysql://" + SERVER_ADDRESS + "/redis", "root", "dddd!!@@##$$" );

			Statement stmt;
			int count = 0;
			
			// Start Time
			long startTimestamp = System.currentTimeMillis();
			
			for ( Integer key : list )
			{
				stmt = conn.createStatement();
				boolean result = stmt.execute( "SELECT * FROM ctest WHERE k = " + key );
				if ( result == false )
					System.out.println( "ERROR : SELECT #" + count + " Failed" );

				if ( (count % 500) == 0 )
				{
					long currentTimestamp = System.currentTimeMillis();
					System.out.println( "Count : " + count + " ( Elapsed Time : " + ( (currentTimestamp - startTimestamp) / 100) / 10.0f + "s )" );
				}

				count++;
				stmt.close();
			}
			long endTimestamp = System.currentTimeMillis();
			System.out.println( "Count : " + count + " ( Elapsed Time : " + ( (endTimestamp - startTimestamp) / 100) / 10.0f + "s ) completed.");

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}
