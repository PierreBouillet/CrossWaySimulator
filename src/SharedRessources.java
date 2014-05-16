import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;


public class SharedRessources {
	
	private Map<Position, MutableInt> nextCarsMove;

	private ArrayList<CarConcurrent> carsOnCrossRoad;
	private ArrayList<CarConcurrent> carsWaiting;

	private int currentThreads;
	private int initializedThreads;
	
	public SharedRessources() {
		
		nextCarsMove= new HashMap<Position, MutableInt>();

		carsOnCrossRoad = new ArrayList<CarConcurrent>();
		carsWaiting  = new ArrayList<CarConcurrent>();
		
		currentThreads = 0;
		initializedThreads = 0;
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
	
	synchronized public int getCurrentThreads() {
		return currentThreads;
	}

	synchronized public void setCurrentThreads(int currentThreads) {
		this.currentThreads = currentThreads;
	}

	synchronized public int getInitializedThreads() {
		return initializedThreads;
	}

	synchronized public void setInitializedThreads(int initializedThreads) {
		this.initializedThreads = initializedThreads;
	}

}
