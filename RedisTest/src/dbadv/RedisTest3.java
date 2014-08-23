package dbadv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class RedisTest3
{
	private final static String SERVER_ADDRESS = "10.73.45.56";

	public static void main(String[] args) throws NumberFormatException, IOException
	{
		BufferedReader br = new BufferedReader( new FileReader( new File( "input.txt" ) ) );
		List<Integer> list = new ArrayList<Integer>();
		LinkedHashMap<Integer, String> map = new LinkedHashMap<Integer, String>();
		
		
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
				
				String value = map.get(key);
				
				if(value ==null){
					
					ResultSet rs =  stmt.executeQuery("SELECT * FROM ctest WHERE k = " + key);
					
					rs.next();
					value = rs.getString("v");
					
					if(map.size() <= 700){
						map.put(key, value);
					}else{
						Integer a =  getFirst(map);
						map.remove(key);
						map.put(key, value);
					}

					rs.close();
				}else {
					
					map.remove(key);
					map.put(key, value);
					
				}

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

	private static Integer getFirst(LinkedHashMap<Integer, String> map) {
		Iterator<Integer> keys = map.keySet().iterator();
		if(keys.hasNext()){
			return keys.next();
		}else{
			return null;
		}
	}
}
