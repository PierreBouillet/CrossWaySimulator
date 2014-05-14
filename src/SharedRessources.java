import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;


public class SharedRessources {
	
	private Map<Position, MutableInt> nextCarsMove;

	private ArrayList<CarConcurrent> carsOnCrossRoad;
	private ArrayList<CarConcurrent> carsWaiting;
	
	public SharedRessources() {
		
		nextCarsMove= new HashMap<Position, MutableInt>();

		carsOnCrossRoad = new ArrayList<CarConcurrent>();
		carsWaiting  = new ArrayList<CarConcurrent>();
	}
	
	synchronized public int getNextCarsMove(Position next) {
		return nextCarsMove.get(next).get();
	}

	synchronized public void addNextCarsMove(Position next) {
		
		MutableInt count = nextCarsMove.get(next);
		if (count == null) {
			nextCarsMove.put(next, new MutableInt());
		}
		else {
			count.increment();
		}
	}

	synchronized public ArrayList<CarConcurrent> getCarsWaiting(){
		return carsWaiting;
	}

	synchronized public void deleteCarWaiting(CarConcurrent car){
		carsWaiting.remove(car);
	}

	synchronized public void addCarWaiting(CarConcurrent car){
		carsWaiting.add(car);
	}

	synchronized public ArrayList<CarConcurrent> getCarsOnCrossRoad(){
		return carsOnCrossRoad;
	}

	synchronized public void deleteCarsOnCrossRoad(CarConcurrent car){
		carsOnCrossRoad.remove(car);
	}

	synchronized public void addCarsOnCrossRoad(CarConcurrent car){
		carsOnCrossRoad.add(car);
	}
	
	synchronized public void clearNextCarsMove(){
		nextCarsMove.clear();
	}

}
