package Commun;

import java.util.ArrayList;
import java.util.List;
import sequentiel.Road;



/**
 * Created by achabert on 04/05/2014.
 */
public enum Itineraire {
	Nord_Sud,
	Nord_Est,
	Nord_Ouest,
	Est_Nord,
	Est_Sud,
	Est_Ouest,
	Ouest_Nord,
	Ouest_Sud,
	Ouest_Est,
	Sud_Nord,
	Sud_Est,
	Sud_Ouest;

	public static ArrayList<Position> getPositionsFromIntineraire(Itineraire it)
	{
		ArrayList<Position> l = new ArrayList<Position>();

		switch(it)
		{
		case Nord_Sud:
			for(int y=Road.SIZE-1 ; y>=0 ; y-- )
			{
				l.add(new Position(Road.SIZE/2,y));
			}
			break;
		case Sud_Nord:
			for(int y=0 ; y<Road.SIZE ; y++ )
			{
				l.add(new Position(Road.SIZE/2 +1,y));
			}
			break;
		case Est_Ouest:
			for(int i =Road.SIZE; i>=0; i--)
			{
				l.add(new Position(i,Road.SIZE/2));
			}
			break;
		case Ouest_Est:
			for(int i=0; i<Road.SIZE;i++)
			{
				l.add(new Position(0,Road.SIZE/2+1));
			}
			break;
		case Nord_Est:
			for(int j=Road.SIZE-1 ; j>= Road.SIZE/2+1 ; j--)
			{
				l.add(new Position(Road.SIZE/2,j));
			}
			for(int i=Road.SIZE/2 ; i> Road.SIZE ; i++ )
			{
				l.add(new Position(i,Road.SIZE+1));
			}
			break;
		case Est_Nord:
			for(int i = Road.SIZE-1 ; i > Road.SIZE/2+1 ; i--)
			{
				l.add(new Position(i,Road.SIZE/2));
			}
			for(int j = Road.SIZE/2 ; j<Road.SIZE ; j++)
			{
				l.add(new Position(Road.SIZE/2+1,j));
			}
			break;
		case Nord_Ouest:
			for(int j = Road.SIZE-1 ; j>= Road.SIZE/2 ; j--)
			{
				l.add(new Position(Road.SIZE,j));
			}
			for(int i = Road.SIZE/2 ; i>=0 ; i--)
			{
				l.add(new Position(i,Road.SIZE/2));
			}
			break;
		case Est_Sud:
			for(int i = Road.SIZE-1 ; i>= Road.SIZE/2 ; i--)
			{
				l.add(new Position(i, Road.SIZE/2));
			}
			for(int j=Road.SIZE/2 ; j>= 0 ; j--)
			{
				l.add(new Position(Road.SIZE/2,j));
			}
			break;
		case Ouest_Nord:
			for(int i=0 ; i<= Road.SIZE/2+1 ; i++)
			{
				l.add(new Position(i,Road.SIZE/2+1));
			}
			for(int j =Road.SIZE/2+1 ; j<= Road.SIZE-1 ; j++)
			{
				l.add(new Position(Road.SIZE/2+1,j));
			}
			break;
		case Ouest_Sud:
			for(int i=0 ; i<= Road.SIZE/2 ; i++)
			{
				l.add(new Position(i,Road.SIZE/2+1));
			}
			for(int j=Road.SIZE/2+1 ; j>= 0 ; j--)
			{
				l.add(new Position(Road.SIZE/2 , j));
			}
			break;
		case Sud_Est:
			for(int j =0 ; j<= Road.SIZE/2+1 ; j++)
			{
				l.add(new Position(Road.SIZE/2+1,j));
			}
			for(int i = Road.SIZE/2+1 ; i<= Road.SIZE-1 ; i++)
			{
				l.add(new Position(i,Road.SIZE/2+1));
			}
			break;
		case Sud_Ouest:
			for(int j = 0 ; j<= Road.SIZE/2 ; j++)
			{
				l.add(new Position(Road.SIZE/2+1,j));
			}
			for(int i=Road.SIZE/2+1 ; i>=0 ; i--)
			{
				l.add(new Position(i,Road.SIZE/2));
			}
		}
		return l;
	}
}
