
import java.awt.*;
import java.util.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by William TASSOUX on 05/05/2014.
 */
public class CrossRoadsLogicConcurrent
{
    private final double apparitionProbability = 0.15;
    private int size = Consts.size;
    private int roadSize = Consts.roadSize;
    private ArrayList<CrossRoadCaseConcurrent> cells;
    private ArrayList<Position> ins;
    private ArrayList<Position> outs;
    private ArrayList<CarConcurrent> cars;
    private Random random;
    private int carId;
    
    private CyclicBarrier barrierNextMove;
    private CyclicBarrier barrierStep;
    
    private Map<Position, MutableInt> nextCarsMove;
    
    private ArrayList<CarConcurrent> carsOnCrossRoad;
    private ArrayList<CarConcurrent> carsWaiting;

	public CrossRoadsLogicConcurrent()
    {
        cells = new ArrayList<CrossRoadCaseConcurrent>(size * size);
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
                CrossRoadCaseConcurrent cell = new CrossRoadCaseConcurrent(col, row, isRelevant, isCrossroad);
                cells.add(cell);
            }
        }
        carId = 0;
        random = new Random();
        cars = new ArrayList<CarConcurrent>();
        ins = new ArrayList<Position>();
        outs = new ArrayList<Position>();
        nextCarsMove= new HashMap<Position, MutableInt>();
        
        carsOnCrossRoad = new ArrayList<CarConcurrent>();
        carsWaiting  = new ArrayList<CarConcurrent>();
     
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
                CarConcurrent newCar = new CarConcurrent(carId, new Color((int)(Math.random() * 0x1000000)), 1, it, this);
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

    public CrossRoadCaseConcurrent getCellFromPosition(Position pos)
    {
        return (cells.get(pos.getX() * size + pos.getY()));
    }
    public void setCarToCell(CarConcurrent car)
    {
        getCellFromPosition(car.getPosition()).setContent(car);
    }

    public void unSetCarFromCell(CarConcurrent car)
    {
        getCellFromPosition(car.getPosition()).setContent(null);
    }
    
    private void debugCounts()
    {
        System.out.println("Car counts: " + cars.size());
        int carCount = 0;
        for (CrossRoadCaseConcurrent cell: cells)
        {
            if (cell.getContent() != null)
                carCount++;
        }
        System.out.println("Real car count: " + carCount);
    }

    

    public void step()
    {
    	
    	int nbCar = cars.size();
        barrierNextMove = new CyclicBarrier(nbCar);
        barrierStep = new CyclicBarrier(nbCar +1);    
        createNewCars();
        try {
			barrierStep.await();
		} catch (Exception e) {
			e.printStackTrace();
		}
       
    }

    public ArrayList<CrossRoadCaseConcurrent> getCells()
    {
        return cells;
    }
    
    public ArrayList<CarConcurrent> getCars(){
    	return cars;
    }
    
    public int getNextCarsMove(Position next) {
		return nextCarsMove.get(next).get();
	}

	public void addNextCarsMove(Position next) {
		MutableInt count = nextCarsMove.get(next);
		if (count == null) {
			nextCarsMove.put(next, new MutableInt());
		}
		else {
		    count.increment();
		}
	}
	
	public ArrayList<CarConcurrent> getCarsWaiting(){
		return carsWaiting;
	}
	
	public void deleteCarWaiting(CarConcurrent car){
		carsWaiting.remove(car);
	}
	
	public void addCarWaiting(CarConcurrent car){
		carsWaiting.add(car);
	}
	
	public ArrayList<CarConcurrent> getCarsOnCrossRoad(){
		return carsOnCrossRoad;
	}
	
	public void deleteCarsOnCrossRoad(CarConcurrent car){
		carsOnCrossRoad.remove(car);
	}
	
	public void addCarsOnCrossRoad(CarConcurrent car){
		carsOnCrossRoad.add(car);
	}
	
	public void deleteCars(CarConcurrent car){
		cars.remove(car);
	}
	
	public boolean isPositionCrossRoad(Position p){
		return getCellFromPosition(p).isCrossroad();
	}
}
