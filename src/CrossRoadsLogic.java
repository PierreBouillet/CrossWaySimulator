import java.awt.*;
import java.util.*;


/**
 * Created by William TASSOUX on 05/05/2014.
 */
public class CrossRoadsLogic
{
    private int size = Consts.size;
    private int roadSize = Consts.roadSize;
    private ArrayList<CrossRoadsCase> cells;
    private ArrayList<Position> ins;
    private ArrayList<Position> outs;
    private ArrayList<Car> cars;
    private Random random;
    private int carId;

    public CrossRoadsLogic()
    {
        cells = new ArrayList<CrossRoadsCase>(size * size);
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                boolean isRelevant = false;
                if (col >= size / 2 - roadSize &&  col < size / 2 + roadSize
                        || row >= size / 2 - roadSize &&  row < size / 2 + roadSize)
                    isRelevant = true;
                CrossRoadsCase cell = new CrossRoadsCase(col, row, isRelevant);
                cells.add(cell);
            }
        }
        carId = 0;
        random = new Random();
        cars = new ArrayList<Car>();
        ins = new ArrayList<Position>();
        outs = new ArrayList<Position>();
        generatePositions();
    }

    // TODO: Retirer les variables temporaires ?
    private void generatePositions()
    {
        Position northIn;
        Position northOut;
        Position eastIn;
        Position eastOut;
        Position westIn;
        Position westOut;
        Position southIn;
        Position southOut;

        northIn = new Position(size / 2 - roadSize, 0);
        northOut = new Position(size / 2 + roadSize - 1, 0);
        southIn = new Position(size / 2 + roadSize - 1, size - 1);
        southOut = new Position(size / 2 - roadSize, size - 1);
        westIn = new Position(0, size / 2 + roadSize - 1);
        westOut = new Position(0, size / 2 - roadSize);
        eastIn = new Position(size - 1, size / 2 - roadSize);
        eastOut = new Position(size - 1, size / 2 + roadSize - 1);

        ins.add(northIn);
        ins.add(southIn);
        ins.add(westIn);
        ins.add(eastIn);
        outs.add(northOut);
        outs.add(southOut);
        outs.add(westOut);
        outs.add(eastOut);

        createCar();
    }

    public void testItineraire(Itineraire it)
    {
        for (CrossRoadsCase cell: cells)
        {
            for (Position p: it.getItineraire())
            {
                if (cell.getX() == p.getX() && cell.getY() == p.getY())
                {
                    cell.setContent(new Car(Color.RED));
                }
            }
        }
    }

    public void test()
    {
        /*
            for (CrossRoadsCase cell: cells)
            {
                if (northIn.getX() == cell.getX() && northIn.getY() == cell.getY())
                    cell.setContent(new Car(Color.RED));
                if (southIn.getX() == cell.getX() && southIn.getY() == cell.getY())
                    cell.setContent(new Car(Color.RED));
                if (eastIn.getX() == cell.getX() && eastIn.getY() == cell.getY())
                    cell.setContent(new Car(Color.RED));
                if (westIn.getX() == cell.getX() && westIn.getY() == cell.getY())
                    cell.setContent(new Car(Color.RED));
                if (northOut.getX() == cell.getX() && northOut.getY() == cell.getY())
                    cell.setContent(new Car(Color.BLUE));
                if (southOut.getX() == cell.getX() && southOut.getY() == cell.getY())
                    cell.setContent(new Car(Color.BLUE));
                if (eastOut.getX() == cell.getX() && eastOut.getY() == cell.getY())
                    cell.setContent(new Car(Color.BLUE));
                if (westOut.getX() == cell.getX() && westOut.getY() == cell.getY())
                    cell.setContent(new Car(Color.BLUE));

            }
            */
    }

    private int getSign(int coord)
    {
        return (coord < 0 ? -1 : 1);
    }

    public Itineraire generateItineraire(Position from, Position to)
    {
        Position vector = Position.substract(to, from);

        if (from.getY() == 0 || from.getY() == size - 1)
        {
            return (generateItineraireYX(from, to, getSign(vector.getX()), getSign(vector.getY())));
        }
        else
        {
            return (generateItineraireXY(from, to, getSign(vector.getX()), getSign(vector.getY())));
        }
    }

    private Itineraire generateItineraireYX(Position from, Position to, int signX, int signY)
    {
        ArrayList<Position> pos = new ArrayList<Position>();
        Position currentPos = from;

        while (currentPos.getY() != to.getY())
        {
            pos.add(currentPos);
            currentPos = new Position(currentPos.getX(), currentPos.getY() + signY);
        }

        while (currentPos.getX() != to.getX())
        {
            pos.add(currentPos);
            currentPos = new Position(currentPos.getX() + signX, currentPos.getY());
        }

        pos.add(to);
        return (new Itineraire(pos));
    }

    private Itineraire generateItineraireXY(Position from, Position to, int signX, int signY)
    {
        ArrayList<Position> pos = new ArrayList<Position>();
        Position currentPos = from;

        while (currentPos.getX() != to.getX())
        {
            pos.add(currentPos);
            currentPos = new Position(currentPos.getX() + signX, currentPos.getY());
        }

        while (currentPos.getY() != to.getY())
        {
            pos.add(currentPos);
            currentPos = new Position(currentPos.getX(), currentPos.getY() + signY);
        }


        pos.add(to);
        return (new Itineraire(pos));
    }

    //TODO: Foolcheck sur l'occupation de la position d'arrivée
    public void createCar()
    {
        ArrayList<Integer> possiblePos = new ArrayList<Integer>(ins.size());
        Integer from;
        Integer to;

        for (int i = 0; i < ins.size(); ++i)
        {
            possiblePos.add(i);
        }

        from = possiblePos.get(random.nextInt(possiblePos.size()));
        possiblePos.remove(from);
        to = possiblePos.get(random.nextInt(possiblePos.size()));

        //TODO: Pourquoi pas generateItineraire(ins.get(from), outs.get(to)); ? Liste inversée ? Pourquoi ?
        Itineraire it = generateItineraire(outs.get(to), ins.get(from));
        Car newCar = new Car(carId, new Color((int)(Math.random() * 0x1000000)), 1, it);
        cars.add(newCar);
        setCarToCell(newCar);
    }

    private void setCarToCell(Car car)
    {
        cells.get(car.getPosition().getX() * size + car.getPosition().getY()).setContent(car);
    }

    private void unSetCarToCell(Car car)
    {
        cells.get(car.getPosition().getX() * size + car.getPosition().getY()).setContent(null);
    }

    public void step()
    {
        if (random.nextFloat() < 0.2)
            createCar();
        for (Iterator<Car> it = cars.iterator(); it.hasNext();)
        {
            Car car = it.next();
            unSetCarToCell(car);
            if (!car.moveCar())
                it.remove();
            else
                setCarToCell(car);
        }
    }

    public ArrayList<CrossRoadsCase> getCells()
    {
        return cells;
    }
}
