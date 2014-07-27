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
	private final static String MAC1 = "10.73.42.72";
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
			connGlobal		= DriverManager.getConnection( "jdbc:mysql://" + MAC1 + "/shard_practice", "shard_practice", "db1004" );
			connLocal[0]	= DriverManager.getConnection( "jdbc:mysql://" + CLOUD1 + "/shard_practice", "shard_practice", "db1004" );
			connLocal[1]	= DriverManager.getConnection( "jdbc:mysql://" + CLOUD2 + "/shard_practice", "shard_practice", "db1004" );
			
			while ( running )
			{
				CallableStatement stmtGlobal = connGlobal.prepareCall( "{ CALL adduser( ?, ?, ? ) }" );
				stmtGlobal.registerOutParameter( 1, Types.INTEGER );
				stmtGlobal.registerOutParameter( 2, Types.INTEGER );
				stmtGlobal.registerOutParameter( 3, Types.TINYINT );
				stmtGlobal.executeQuery();

				int ruid = (int)stmtGlobal.getObject(1);
				int rdbid = (int)stmtGlobal.getObject(2);
				int rgid = (int)stmtGlobal.getObject(3);
				stmtGlobal.close();

				CallableStatement stmtLocal = connLocal[rdbid].prepareCall( "{ CALL adduser( ?, ? ) }" );
				stmtLocal.setInt( 1, ruid );
				stmtLocal.setInt( 2, rgid );
				stmtLocal.executeQuery();
			}
		}
		catch ( SQLException e )
		{
			e.printStackTrace();
			running = false;
		}
	}
}
