package db_adv;

public class GalaxyWar
{
	public static void main(String[] args) throws InterruptedException
	{
		RegisterThread registerThread = new RegisterThread();
		AttackThread[] attackThreads = new AttackThread[4];
		
		registerThread.start();
		for( AttackThread attackThread : attackThreads )
		{
			attackThread = new AttackThread();
			attackThread.start();
		}
		
		Thread.sleep( 3000 );
		
		registerThread.finish();
		for( AttackThread attackThread : attackThreads )
		{
			attackThread.finish();
		}
	}
}
