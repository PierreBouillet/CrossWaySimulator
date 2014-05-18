package iterative;

import java.util.ArrayList;


/**
 * Created by achabert on 04/05/2014.
 */
public class Itineraire
{
    private ArrayList<Position> itineraire;

    public Itineraire(ArrayList<Position> itineraire)
    {
        this.itineraire = itineraire;
    }

    public ArrayList<Position> getItineraire()
    {
        return itineraire;
    }
}
