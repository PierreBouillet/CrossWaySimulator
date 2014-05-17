import java.awt.*;
import java.util.*;


/**
 * Created by William TASSOUX on 05/05/2014.
 */
public class CrossRoadsLogic
{
    private final double apparitionProbability = 0.15;
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
                boolean isCrossroad = false;
                if (col >= size / 2 - roadSize &&  col < size / 2 + roadSize
                        || row >= size / 2 - roadSize &&  row < size / 2 + roadSize)
                    isRelevant = true;
                if (col >= size / 2 - roadSize &&  col < size / 2 + roadSize
                        && row >= size / 2 - roadSize &&  row < size / 2 + roadSize)
                    isCrossroad = true;
                CrossRoadsCase cell = new CrossRoadsCase(col, row, isRelevant, isCrossroad);
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

        westOut = new Position(size / 2 - roadSize, 0); // westOut
        westIn = new Position(size / 2 + roadSize - 1, 0); // westIn
        eastOut = new Position(size / 2 + roadSize - 1, size - 1); // eastOut
        eastIn = new Position(size / 2 - roadSize, size - 1); // eastIn
        northOut = new Position(0, size / 2 + roadSize - 1); // northOut
        northIn = new Position(0, size / 2 - roadSize); // northIn
        southOut = new Position(size - 1, size / 2 - roadSize); // southOut
        southIn = new Position(size - 1, size / 2 + roadSize - 1); //southIn

        ins.add(northIn);
        ins.add(southIn);
        ins.add(westIn);
        ins.add(eastIn);
        outs.add(northOut);
        outs.add(southOut);
        outs.add(westOut);
        outs.add(eastOut);
    }

    public void testItineraire(Itineraire it)
    {
            for (Position p: it.getItineraire()) {

                System.out.println(p);
            }
        System.out.println("ENDDEBUG");
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

    public void createNewCars()
    {
        ArrayList<Integer> possibleFromPos = new ArrayList<Integer>();
        ArrayList<Integer> possibleToPos = new ArrayList<Integer>(outs.size());

        for (int i = 0; i < ins.size(); ++i)
        {
            possibleToPos.add(i);
            // Si la cellule contient déjà une voiture on ne peut pas y faire apparaître une autre voiture
            if (getCellFromPosition(ins.get(i)).getContent() == null)
            {
                possibleFromPos.add(i);
            }
        }

        // Pour chaque case de départ non utilisée, on jette un dé afin de décider de l'apparition ou non d'une nouvelle voiture
        while (!possibleFromPos.isEmpty())
        {
            Integer from = possibleFromPos.get(random.nextInt(possibleFromPos.size()));
            if (random.nextFloat() < apparitionProbability)
            {
                // On retire la direction de départ de la liste de directions d'arrivé
                // En effet nous ne souhaitons pas qu'un véhicule fasse demi tour en arrivant
                // dans le carrefour
                possibleToPos.remove(from);
                Integer to = possibleToPos.get(random.nextInt(possibleToPos.size()));
                //TODO: Pourquoi pas generateItineraire(ins.get(from), outs.get(to)); ? Liste inversée ? Pourquoi ?
                // On crée la liste de cases permettant d'aller du départ à l'arrivée
                Itineraire it = generateItineraire(ins.get(from), outs.get(to));
                // On crée une nouvelle voiture d'une couleur aléatoire et possédant l'itinéraire généré ci-dessus
                Car newCar = new Car(carId, new Color((int)(Math.random() * 0x1000000)), 1, it);
                // On ajoute la voiture au système
                cars.add(newCar);
                setCarToCell(newCar);
                // On remet la direction précédemment enlevée dans la liste possible des destinations d'arrivées
                possibleToPos.add(from);
            }
            // On supprime cette case de départ de la liste.
            possibleFromPos.remove(from);
        }
    }

    private CrossRoadsCase getCellFromPosition(Position pos)
    {
        return (cells.get(pos.getX() * size + pos.getY()));
    }
    private void setCarToCell(Car car)
    {
        getCellFromPosition(car.getPosition()).setContent(car);
    }

    private void unSetCarFromCell(Car car)
    {
        getCellFromPosition(car.getPosition()).setContent(null);
    }


    private void debugCounts()
    {
        System.out.println("Car counts: " + cars.size());
        int carCount = 0;
        for (CrossRoadsCase cell: cells)
        {
            if (cell.getContent() != null)
                carCount++;
        }
        System.out.println("Real car count: " + carCount);
    }

    private void moveCarAlgorithm()
    {
        ArrayList<Car> carListCopy = new ArrayList<Car>(cars);
        ArrayList<Car> alreadyOnCrossroadCars = new ArrayList<Car>();
        moveAlreadyOnCrossroadCars(carListCopy, alreadyOnCrossroadCars);
        moveEnteringCrossroadCars(carListCopy, alreadyOnCrossroadCars);
        moveOtherCars(carListCopy);
    }

    private boolean intersectsOnCrossroad(Itineraire t1, Itineraire t2)
    {
        for (Position p1: t1.getItineraire())
        {
            for (Position p2: t2.getItineraire())
            {
                if (getCellFromPosition(p1).isCrossroad() && getCellFromPosition(p2).isCrossroad())
                {
                    if (p1.equals(p2))
                        return (true);
                }
            }
        }
        return (false);
    }

    private void moveOtherCars(ArrayList<Car> carListCopy)
    {
        boolean hasMoved = true;
        while (!carListCopy.isEmpty() && hasMoved)
        {
            hasMoved = false;
            for (Iterator<Car> it = carListCopy.iterator(); it.hasNext();)
            {
                Car car = it.next();
                if (getCellFromPosition(car.getNextMove()).getContent() == null)
                {
                    hasMoved = true;
                    unSetCarFromCell(car);
                    car.moveCar();
                    setCarToCell(car);
                    it.remove();
                }
            }
        }
    }


    private void moveEnteringCrossroadCars(ArrayList<Car> carListCopy, ArrayList<Car> alreadyOnCrossroadCars)
    {
        ArrayList<Car> enteringCrossroadCars = new ArrayList<Car>();
        for (Iterator<Car> it = carListCopy.iterator(); it.hasNext();)
        {
            Car car = it.next();
            if (getCellFromPosition(car.getNextMove()).isCrossroad())
            {
                enteringCrossroadCars.add(car);
                it.remove();
            }
        }
        for (Car enteringCar: enteringCrossroadCars)
        {
            boolean canEnter = true;
            for (Car alreadyOnCrossroadCar : alreadyOnCrossroadCars) {
                if (intersectsOnCrossroad(alreadyOnCrossroadCar.getItineraire(), enteringCar.getItineraire()))
                {
                    canEnter = false;
                }
            }
            if (canEnter)
            {
                unSetCarFromCell(enteringCar);
                enteringCar.moveCar();
                setCarToCell(enteringCar);
                alreadyOnCrossroadCars.add(enteringCar);
            }
        }
    }

    private void moveAlreadyOnCrossroadCars(ArrayList<Car> carListCopy, ArrayList<Car> alreadyOnCrossroadCars)
    {
        for (Iterator<Car> it = carListCopy.iterator(); it.hasNext();)
        {
            Car car = it.next();
            if (getCellFromPosition(car.getPosition()).isCrossroad())
            {
                alreadyOnCrossroadCars.add(car);
                unSetCarFromCell(car);
                car.moveCar();
                setCarToCell(car);
                it.remove();
            }
        }
    }

    private void removeOutOfScreenCars()
    {
        for (Iterator<Car> it = cars.iterator(); it.hasNext();)
        {
            Car car = it.next();
            if (car.getNextMove() == null)
            {
                unSetCarFromCell(car);
                it.remove();
            }
        }
    }

    public void step()
    {
        removeOutOfScreenCars();
        moveCarAlgorithm();
        createNewCars();
    }

    public ArrayList<CrossRoadsCase> getCells()
    {
        return cells;
    }
}
