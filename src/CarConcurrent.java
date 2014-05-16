import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import javax.swing.DropMode;

public class CarConcurrent extends Thread{

	private int num;
	private Color clr;
	private int speed;
	private Itineraire itineraire;
	private int indexItineraire;
	public static final int MAXSPEED = 3;
	public static final int MINSPEED = 0;

	private CrossRoadsLogicConcurrent crossRoad;

	private CyclicBarrier barrierNextMove;
	private CyclicBarrier barrierStep;

	private SharedRessources shared;
	private boolean CanMove;
	private Position nextPosition;
	private Direction direction;

	// 1=entering, CrossRoad 2= In crossRoad, 3=crossed, 0=else
	private int state;

	public CarConcurrent(int num, Color clr, int speed, Itineraire iti, CrossRoadsLogicConcurrent cr, SharedRessources sh, Direction dir, int from){
		this.num = num;
		this.clr = clr;
		this.speed = speed;
		this.indexItineraire = 0;
		this.itineraire = iti;
		this.crossRoad = cr;
		this.state = 0;
		this.barrierNextMove = new CyclicBarrier(1);
		this.barrierStep = new CyclicBarrier(1);
		this.shared = sh;
		this.direction = dir;
		this.comesFrom = from;
		System.out.println("car number " + num + " has been created");
	}

	public CarConcurrent(Color clr)
	{
		this.clr = clr;
	}


	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
	public Itineraire getItineraire() {
		return itineraire;
	}
	public Color getClr() {
		return clr;
	}

	public void setClr(Color clr) {
		this.clr = clr;
	}
	public int getSpeed() {
		return speed;
	}
	public void accelerate() {
		if(speed < MAXSPEED)
			speed++;
	}
	public void slowDown() {
		if(speed > MINSPEED)
			speed--;
	}
	public Position getPosition(){
		return itineraire.getItineraire().get(indexItineraire);
	}

	public void moveCar()
	{
		if (indexItineraire < itineraire.getItineraire().size() - 1)
		{
			crossRoad.unSetCarFromCell(this);
			++indexItineraire;
			crossRoad.setCarToCell(this);
		}
	}

	public Position getNextMove()
	{
		if (indexItineraire < itineraire.getItineraire().size() - 1)
		{
			return (itineraire.getItineraire().get(indexItineraire + 1));
		}
		else
		{
			return null;
		}
	}
	@Override
	public void run()
	{	

		while(indexItineraire < itineraire.getItineraire().size()-2){

			synchronized (shared) {

				while (!(shared.getCurrentThreads()==shared.getInitializedThreads())) {
					try {
						shared.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				shared.notifyAll();

			}

			SetNextMove();

			if(barrierNextMove.getNumberWaiting() == shared.getCurrentThreads()-1)
				shared.setInitializedThreads(0);

			try {
				barrierNextMove.await();
			} catch (Exception e) {
				e.printStackTrace();
			}

			//if there is no conflict the car can move
			if(shared.getNextCarsMove(nextPosition) == 1  && CanMove){
				moveCar();

				//if the new position is on the crossWay, the car change is state to on crossWay
				if(crossRoad.isPositionCrossRoad(getPosition()) && state == 1)
				{
					shared.addCarsOnCrossRoad(this);
					state = 2;
					shared.deleteCarWaiting(this);
				}
				//if the next position is on the crossWay, the car change is state to on onWaiting
				if(crossRoad.isPositionCrossRoad(getNextMove()) && state==0){
					shared.addCarWaiting(this);
					state=1;
				}
				if(state==2 && (!crossRoad.isPositionCrossRoad(getPosition()))){
					state=3;
					shared.deleteCarsOnCrossRoad(this);
				}
			}

			System.out.println("car number " + num +" have state " + state);

			try {
				barrierStep.await();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		System.out.println("car number " + num +" has gone ");
		crossRoad.deleteCars(this);
		crossRoad.unSetCarFromCell(this);
	}

	private boolean intersectsOnCrossroad(Itineraire t1, Itineraire t2)
	{
		if(t1.equals(t2))
			return false;
		for (Position p1: t1.getItineraire())
		{
			for (Position p2: t2.getItineraire())
			{
				if (crossRoad.isPositionCrossRoad(p1) && crossRoad.isPositionCrossRoad(p2))
				{
					if (p1.equals(p2))
						return (true);
				}
			}
		}
		return (false);
	}

	private void SetNextMove(){

		if(state==0 || state==2 || state==3){
			shared.addNextCarsMove(getNextMove());
			CanMove=true;
			nextPosition = getNextMove();
		}

		if(state==1){

			boolean canEnter = true;

			for (CarConcurrent alreadyOnCrossroadCar : shared.getCarsOnCrossRoad()) {
				if (intersectsOnCrossroad(alreadyOnCrossroadCar.getItineraire(), itineraire))
				{
					canEnter = false;
				}
			}

			for (CarConcurrent waitingCar : shared.getCarsWaiting()) {
				if (intersectsOnCrossroad(waitingCar.getItineraire(), itineraire))
				{
					if(checkPriority(waitingCar) == false)
						canEnter=false;
				}
			}

			if (canEnter)
			{
				shared.addNextCarsMove(getNextMove());
				CanMove = true;
				nextPosition=getNextMove();
			}
			else
			{
				shared.addNextCarsMove(getPosition());
				CanMove=false;
				nextPosition = getPosition();
			}
		}

		System.out.println("car number " + num + " has set his move");
	}


	private boolean checkPriority(CarConcurrent waitingCar) {
		
		Direction relativePosition = crossRoad.getDirection(comesFrom, waitingCar.getComesFrom());
		System.out.println("car num : " + num + " go on" + direction + ", car num " + waitingCar.getNum() + " go on " + waitingCar.getDirection() + " he is on the " +relativePosition);

		if(direction==Direction.RIGHT){
			return true;
		}
		if(direction==Direction.FRONT){
			if(relativePosition == Direction.RIGHT)
				return false;
			else 
				return true;
		}
		if(direction==Direction.LEFT){
			if(relativePosition == Direction.RIGHT)
				return false;
			else if(relativePosition == Direction.FRONT){
					return false;
			}
			else
				return true;
		}
		return false;
	}

	public CyclicBarrier getBarrierNextMove() {
		return barrierNextMove;
	}

	public void setBarrier(CyclicBarrier barrier) {
		this.barrierNextMove = barrier;
	}

	public CyclicBarrier getBarrierStep() {
		return barrierStep;
	}

	public void setBarrierStep(CyclicBarrier barrierStep) {
		this.barrierStep = barrierStep;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public int getComesFrom() {
		return comesFrom;
	}

	public void setComesFrom(int comesFrom) {
		this.comesFrom = comesFrom;
	}

	private int comesFrom;

}
