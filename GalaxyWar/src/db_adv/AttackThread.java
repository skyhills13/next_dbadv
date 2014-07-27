package db_adv;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

public class AttackThread extends Thread
{
	private final static String CLOUD1 = "10.73.45.50";
	private final static String CLOUD2 = "10.73.45.54";
	private final static String MAC1 = "10.73.42.72";
	private boolean running = true;
	
	public static int destroyed = -1;

	public void finish()
	{
		running = false;
	}
	
	public boolean isRunning()
	{
		return running;
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
				CallableStatement stmtGlobal = connGlobal.prepareCall( "{ CALL selectuser( ?, ? ) }" );
				stmtGlobal.registerOutParameter( 1, Types.INTEGER );
				stmtGlobal.registerOutParameter( 2, Types.INTEGER );
				stmtGlobal.executeQuery();

				int ruid = (int)stmtGlobal.getObject(1);
				int rdbid = (int)stmtGlobal.getObject(2);
				stmtGlobal.close();
				System.out.println( "selectuser result : [" + ruid + ", " + rdbid + "]" );

				CallableStatement stmtLocal = connLocal[rdbid].prepareCall( "{ CALL attack( ?, ?, ? ) }" );
				stmtLocal.setInt( 1, ruid );
				stmtLocal.registerOutParameter( 2, Types.INTEGER );
				stmtLocal.registerOutParameter( 3, Types.TINYINT );
				stmtLocal.executeQuery();
				
				int power = (int)stmtLocal.getObject(2);
				int gid = (int) stmtLocal.getObject(3);
				stmtLocal.close();
				
				Statement stmt = connLocal[ gid % 2 ].createStatement();
				int row = stmt.executeUpdate( "UPDATE galaxy SET hp = hp - " + power + " WHERE gid = " + gid );
				stmt.close();

				if ( row == 0 )
					System.out.println( "Update Failed" );

				stmt = connLocal[ gid % 2 ].createStatement();
				ResultSet rs = stmt.executeQuery( "SELECT gid FROM galaxy WHERE hp <= 0 " );
				
				if ( rs.next() )
				{
					destroyed = rs.getInt( 1 );
					running = false;
				}
				stmt.close();
			}
		}
		catch ( SQLException e )
		{
			e.printStackTrace();
			running = false;
		}
	}
}
