package dbadv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MakeTestKey
{
	private final static Random rand = new Random();

	public static void main(String[] args) throws IOException
	{
		List<Integer> list = new ArrayList<Integer>();
		BufferedWriter bw;
		try
		{
			bw = new BufferedWriter( new FileWriter( new File( "input.txt" ) ) );
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
			return;
		}

		for ( int i = 0; i < 1000; ++i )
		{
			int val = rand.nextInt( 1000 ) + 1;
			list.add( val );
			list.add( val );
			list.add( val );
			list.add( val );
			list.add( val );
			list.add( val );
			list.add( val );
			list.add( val );
			list.add( val );
		}
		
		for ( int i = 0; i < 1000; ++i )
		{
			list.add( rand.nextInt( 9000 ) + 1000 );
		}
		
		System.out.println( list.size() );
		Collections.shuffle( list, new Random( rand.nextInt() ) );

		for ( Integer val : list )
		{
			bw.write( "" + val + "\n" );
		}
		
		bw.close();
	}

}
