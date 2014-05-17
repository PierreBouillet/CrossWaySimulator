import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class SharedRessources {

	private Map<Position, MutableInt> nextCarsMove;

	private ArrayList<CarConcurrent> carsOnCrossRoad;
	private ArrayList<CarConcurrent> carsWaitingCrossRoad;
	
	private ArrayList<Position> debugPos;
	private int currentThreads;
	private int initializedThreads;

	public SharedRessources() {

		nextCarsMove= new HashMap<Position, MutableInt>();

		carsOnCrossRoad = new ArrayList<CarConcurrent>();
		carsWaitingCrossRoad  = new ArrayList<CarConcurrent>();

		currentThreads = 0;
		initializedThreads = 0;

		debugPos = new ArrayList<Position>();
	}

	synchronized public int getNextCarsMove(Position next) {
		return nextCarsMove.get(next).get();
	}

	synchronized public void addNextCarsMove(Position next) {
		debugPos.add(next);
		MutableInt count = nextCarsMove.get(next);
		if (count==null) {
			nextCarsMove.put(next, new MutableInt());
		}
		else {
			count.increment();
		}
	}

	synchronized public ArrayList<CarConcurrent> getCarsWaitingCrossRoad(){
		return carsWaitingCrossRoad;
	}

	synchronized public void deleteCarWaitingCrossRoad(CarConcurrent car){
		carsWaitingCrossRoad.remove(car);
	}

	synchronized public void addCarWaitingCrossRoad(CarConcurrent car){
		carsWaitingCrossRoad.add(car);
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
		debugPos.clear();
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
	synchronized public boolean debug(Position p1){
		int confllict=0;
		for(int i = 0; i< debugPos.size();i++){
				if (p1.equals(debugPos.get(i))){
					confllict++;
					}
			}
	if(confllict>1)
		return false;
	else return true;

	}

}
