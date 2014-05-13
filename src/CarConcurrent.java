import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

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

	// 1=entering, CrossRoad 2= In crossRoad, 0=else
	private int state;


	public CarConcurrent(int num, Color clr, int speed, Itineraire iti, CrossRoadsLogicConcurrent cr)
	{
		this.num = num;
		this.clr = clr;
		this.speed = speed;
		this.indexItineraire = 0;
		this.itineraire = iti;
		this.crossRoad = cr;
		this.state = 0;
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
			++indexItineraire;
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

	public void run()
	{	
		SetNextMove();
		while(indexItineraire < itineraire.getItineraire().size()){

			try {
				barrierNextMove.await();
			} catch (Exception e) {
				e.printStackTrace();
			}

			//if there is no conflict the car can move
			if(crossRoad.getNextCarsMove(getNextMove()) == 1){
				moveCar();

				//if the new position is on the crossWay, the car change is state to on crossWay
				if(crossRoad.isPositionCrossRoad(getPosition()))
				{
					crossRoad.addCarsOnCrossRoad(this);
					state = 2;
					crossRoad.deleteCarWaiting(this);
				}
				//if the next position is on the crossWay, the car change is state to on onWaiting
				if(crossRoad.isPositionCrossRoad(getNextMove()))
					crossRoad.addCarWaiting(this);
				state=1;
			}

			try {
				barrierStep.await();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		crossRoad.deleteCars(this);
		crossRoad.unSetCarFromCell(this);
	}

	private boolean intersectsOnCrossroad(Itineraire t1, Itineraire t2)
	{
		for (Position p1: t1.getItineraire())
		{
			for (Position p2: t2.getItineraire())
			{
				if (crossRoad.getCellFromPosition(p1).isCrossroad() && crossRoad.getCellFromPosition(p2).isCrossroad())
				{
					if (p1.equals(p2))
						return (true);
				}
			}
		}
		return (false);
	}

	private void SetNextMove(){

		if(state==0 || state==2)
			crossRoad.addNextCarsMove(itineraire.getItineraire().get(indexItineraire + 1));

		if(state==1){

			boolean canEnter = true;

			for (CarConcurrent alreadyOnCrossroadCar : crossRoad.getCarsOnCrossRoad()) {
				if (intersectsOnCrossroad(alreadyOnCrossroadCar.getItineraire(), itineraire))
				{
					canEnter = false;
				}
			}
			if (canEnter)
			{
				crossRoad.addNextCarsMove(itineraire.getItineraire().get(indexItineraire + 1));
			}
			else
			{
				crossRoad.addNextCarsMove(itineraire.getItineraire().get(indexItineraire));
			}
		}

	}


	public CyclicBarrier getBarrierNextMove() {
		return barrierNextMove;
	}

	public void setBarrier(CyclicBarrier barrier) {
		this.barrierNextMove = barrierNextMove;
	}

	public CyclicBarrier getBarrierStep() {
		return barrierStep;
	}

	public void setBarrierStep(CyclicBarrier barrierStep) {
		this.barrierStep = barrierStep;
	}

}
