package concurrent;

import java.awt.*;

public class Car extends Thread{

	private int num;
	private Color clr;
    private Itineraire itineraire;
    private int indexItineraire;

	public Car(int num, Color clr, Itineraire iti)
    {
		this.num = num;
		this.clr = clr;
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

}
