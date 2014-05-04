package sequentiel;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import Commun.Position;
public class Car extends Thread{

	private int num;
	private Color clr;
	private int speed;
    private ArrayList<Position> itineraire;
    private int indexItineraire;
    private Road road;
    private CyclicBarrier barrier;

	public static final int MAXSPEED = 3;
	public static final int MINSPEED = 0;


	public Car(int num, Color clr, int speed, ArrayList<Position> iti, Road r, CyclicBarrier b) {
		this.num = num;
		this.clr = clr;
		this.speed = speed;
		this.indexItineraire = 0;
		this.itineraire = iti;
		this.road = r;
		this.barrier = b;
	}


	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
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
		return itineraire.get(indexItineraire);
	}

	public void tryMove()
	{
		if(indexItineraire + speed < itineraire.size()){
			ArrayList<Position> reserve = new ArrayList<Position>();
			for(int i=0; i<=speed; i++)
				reserve.add(itineraire.get(i+indexItineraire));
			road.reserveCase(reserve,this);
		}
			
	}
}
