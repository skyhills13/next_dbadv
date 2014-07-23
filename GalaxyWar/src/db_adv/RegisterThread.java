package db_adv;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class RegisterThread extends Thread
{
	private final static String CLOUD1 = "10.73.45.50";
	private final static String CLOUD2 = "10.73.45.54";
	private final static String MAC1 = "10.73.38.244";
	private boolean running = true;

	public void finish()
	{
		running = false;
	}

	@Override
	public void run()
	{
		Connection connGlobal = null;
		Connection connLocal[] = new Connection[2];
		try
		{
			connGlobal		= DriverManager.getConnection( "jdbc:mysql://" + MAC1, "shard_practice", "db1004" );
			connLocal[0]	= DriverManager.getConnection( "jdbc:mysql://" + CLOUD1, "shard_practice", "db1004" );
			connLocal[1]	= DriverManager.getConnection( "jdbc:mysql://" + CLOUD2, "shard_practice", "db1004" );

			while ( running )
			{
				CallableStatement stmtGlobal = connGlobal.prepareCall( "{ CALL adduser( ?, ?, ? ) }" );
				stmtGlobal.registerOutParameter( 1, Types.INTEGER );
				stmtGlobal.registerOutParameter( 2, Types.INTEGER );
				stmtGlobal.registerOutParameter( 3, Types.TINYINT );
				Boolean hasResult = stmtGlobal.execute();
				
				if ( ! hasResult )
				{
					System.out.println( "Register Failed" );
					running = false;
					break;
				}

				ResultSet rs = stmtGlobal.getResultSet();
				int ruid = rs.getInt( 0 );
				int rdbid = rs.getInt( 1 );
				int rgid = rs.getInt( 2 );
				stmtGlobal.close();

				CallableStatement stmtLocal = connLocal[rdbid].prepareCall( "{ CALL adduser( ?, ? ) }" );
				stmtLocal.setInt( 1, ruid );
				stmtLocal.setInt( 2, rgid );
				Boolean success = stmtLocal.execute();
				
				if ( ! success )
					System.out.println( "Register Failed" );
			}
		}
		catch ( SQLException e )
		{
			running = false;
		}
	}
}
