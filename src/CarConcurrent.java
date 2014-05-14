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
	private CyclicBarrier init;
	private CyclicBarrier clearMoveStep;
	
	private SharedRessources shared;

	// 1=entering, CrossRoad 2= In crossRoad, 0=else
	private int state;


	public CarConcurrent(int num, Color clr, int speed, Itineraire iti, CrossRoadsLogicConcurrent cr, SharedRessources sh)
	{
		this.num = num;
		this.clr = clr;
		this.speed = speed;
		this.indexItineraire = 0;
		this.itineraire = iti;
		this.crossRoad = cr;
		this.state = 0;
		this.barrierNextMove = new CyclicBarrier(1);
		this.barrierStep = new CyclicBarrier(1);
		this.init = new CyclicBarrier(1);
		this.clearMoveStep = new CyclicBarrier(1);
		shared = sh;
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
		
		while(indexItineraire < itineraire.getItineraire().size()){
			
			if(num==0)
				System.out.println(indexItineraire + " " + itineraire.getItineraire().size() + " " + init.getParties());
			
			try {
				init.await();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			SetNextMove();
			if(num==0)
				System.out.println(indexItineraire + " " + itineraire.getItineraire().size());
			try {
				barrierNextMove.await();
			} catch (Exception e) {
				e.printStackTrace();
			}

			//if there is no conflict the car can move
			if(shared.getNextCarsMove(getNextMove()) == 1){
				moveCar();
				//if the new position is on the crossWay, the car change is state to on crossWay
				if(crossRoad.isPositionCrossRoad(getPosition()))
				{
					shared.addCarsOnCrossRoad(this);
					state = 2;
					shared.deleteCarWaiting(this);
				}
				//if the next position is on the crossWay, the car change is state to on onWaiting
				if(crossRoad.isPositionCrossRoad(getNextMove()))
					shared.addCarWaiting(this);
				state=1;
			}
			if(num==0)
				System.out.println(indexItineraire + " " + itineraire.getItineraire().size());
			try {
				clearMoveStep.await();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(num==0)
				System.out.println(indexItineraire + " " + itineraire.getItineraire().size());
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
			shared.addNextCarsMove(getNextMove());

		if(state==1){

			boolean canEnter = true;

			for (CarConcurrent alreadyOnCrossroadCar : shared.getCarsOnCrossRoad()) {
				if (intersectsOnCrossroad(alreadyOnCrossroadCar.getItineraire(), itineraire))
				{
					canEnter = false;
				}
			}
			if (canEnter)
			{
				shared.addNextCarsMove(itineraire.getItineraire().get(indexItineraire + 1));
			}
			else
			{
				shared.addNextCarsMove(itineraire.getItineraire().get(indexItineraire));
			}
		}

		System.out.println("car number " + num + " has set his move");
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
	
	public CyclicBarrier getBarrierInit() {
		return init;
	}

	public void setBarrierInit(CyclicBarrier init) {
		this.init = init;
	}

	public CyclicBarrier getClearMoveStep() {
		return clearMoveStep;
	}

	public void setClearMoveStep(CyclicBarrier clearMoveStep) {
		this.clearMoveStep = clearMoveStep;
	}

}
