import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Car extends Thread{

	private int num;
	private Color clr;
	private int speed;
    private Itineraire itineraire;
    private int indexItineraire;

	public static final int MAXSPEED = 3;
	public static final int MINSPEED = 0;


	public Car(int num, Color clr, int speed, Itineraire iti)
    {
		this.num = num;
		this.clr = clr;
		this.speed = speed;
		this.indexItineraire = 0;
		this.itineraire = iti;
	}

    public Car(Color clr)
    {
        this.clr = clr;
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
		return itineraire.getItineraire().get(indexItineraire);
	}

    public boolean moveCar()
    {
        if (indexItineraire + 1 < itineraire.getItineraire().size())
        {
            ++indexItineraire;
            return true;
        }
        return false;
    }

}
