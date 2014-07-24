package db_adv;

public class GalaxyWar
{
	public static void main(String[] args) throws InterruptedException, ClassNotFoundException, InstantiationException, IllegalAccessException
	{
		Class.forName("com.mysql.jdbc.Driver").newInstance();

		RegisterThread registerThread = new RegisterThread();
		AttackThread[] attackThreads = new AttackThread[4];
		
		registerThread.start();
		Thread.sleep( 1000 );
		for( int i = 0; i < 4; ++i )
		{
			attackThreads[i] = new AttackThread();
//			attackThreads[i].start();
		}
		
		boolean running = true;
		while ( running )
		{
			Thread.sleep( 100 );
//			for( int i = 0; i < 4; ++i )
//			{
//				if ( attackThreads[i].isRunning() == false )
//					running = false;
//			}
		}
		
		registerThread.finish();
		for( int i = 0; i < 4; ++i )
		{
			attackThreads[i].finish();
		}
	}
}
