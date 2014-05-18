package iterative;

import java.awt.*;

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
        /*
        if (indexItineraire < itineraire.getItineraire().size() - 1)
        {
            return (itineraire.getItineraire().get(indexItineraire + 1));
        }
        else
        {
            return null;
        }*/
        return (getNthMove(1));
    }

    public Position getNthMove(int i)
    {
        if (indexItineraire + i < itineraire.getItineraire().size())
        {
            return (itineraire.getItineraire().get(indexItineraire + i));
        }
        else
        {
            return (null);
        }
    }

}
