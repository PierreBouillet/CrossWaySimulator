package sequentiel;
 import java.util.*;
import java.util.concurrent.CyclicBarrier;

import Commun.Position;
 

public class Road{

       private HashMap<Position,Car> cars;
       
       public static final int SIZE = 16;
       private boolean[][] map;
       
       private Position entreeSud;
       private Position entreeNord;
       private Position entreeEst;
       private Position entreeOuest;
       
       private Position sortieSud;
       private Position sortieNord;
       private Position sortieEst;
       private Position sortieOuest;
       private CyclicBarrier barrier;
       
       private HashMap<Position, ArrayList<Car> > reservation;
      
       public Road() {
    	   
    	   cars = new HashMap<Position, Car>();
    	   map = new boolean[SIZE][SIZE];
    	   
    	   Position entreeSud = new Position(SIZE-1, SIZE/2+1);
           Position entreeNord = new Position(0, SIZE/2);
           Position entreeEst = new Position(SIZE/2+1, SIZE-1);
           Position entreeOuest = new Position(SIZE/2, 0);
           
           Position sortieSud =new Position(SIZE-1, SIZE/2);
           Position sortieNord = new Position(0, SIZE/2+1);
           Position sortieEst = new Position(SIZE/2, SIZE-1);
           Position sortieOuestnew = new Position(SIZE/2+1, 0);
           
           barrier = new CyclicBarrier(cars.size());
    	   

    	   for(int i=0;i<SIZE;i++){
    		   for(int j=0;j<SIZE;j++){
    		   if(i == SIZE/2 || i ==SIZE/2+1)
    			   map[i][j] = true;
    		   else if(j == SIZE/2 || j ==SIZE/2+1)
    			   map[i][j] = true;
    		   else
    			   map[i][j] = false;
    		   }
    	   }
       }
      
       public Car getCar(Position p)
       {
             return cars.get(p);
       }

	public void reserveCase(ArrayList<Position> reserve, Car car) {
				
		for(Position p : reserve){
			reservation.get(p).add(car);
		}
		
	}
   
}