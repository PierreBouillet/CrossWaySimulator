package concurrent;

import java.awt.*;
import java.util.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;


/**
 * Created by William TASSOUX on 05/05/2014.
 */
public class CrossRoadsLogic
{
    private final double apparitionProbability = Consts.apparitionProbability;
    private int size = Consts.size;
    private int roadSize = Consts.roadSize;
    private ArrayList<Position> ins;
    private ArrayList<Position> outs;
    private SharedRessources sharedRessources;
    private Random random;
    private int carId;

    public CrossRoadsLogic()
    {
        ArrayList<CrossRoadsCase> cells = new ArrayList<CrossRoadsCase>(size * size);
        sharedRessources = new SharedRessources();

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

        sharedRessources.setCells(cells);
        sharedRessources.setCars(new ArrayList<ThreadCar>());
        carId = 0;
        random = new Random();
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

    public ArrayList<CrossRoadsCase> getCells()
    {
        return sharedRessources.getCells();
    }

    public void createNewCars()
    {
        ArrayList<Integer> possibleFromPos = new ArrayList<Integer>();
        ArrayList<Integer> possibleToPos = new ArrayList<Integer>(outs.size());

        for (int i = 0; i < ins.size(); ++i)
        {
            possibleToPos.add(i);
            // Si la cellule contient déjà une voiture on ne peut pas y faire apparaître une autre voiture
            if (sharedRessources.getCellFromPosition(ins.get(i)).getContent() == null)
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
                ThreadCar newCar = new ThreadCar(carId, new Color((int)(Math.random() * 0x1000000)), it, sharedRessources);
                sharedRessources.setCarToCell(newCar.getCar());
                sharedRessources.getCars().add(newCar);
                newCar.start();
                // On remet la direction précédemment enlevée dans la liste possible des destinations d'arrivées
                possibleToPos.add(from);
            }
            // On supprime cette case de départ de la liste.
            possibleFromPos.remove(from);
        }
    }



    private void debugCounts()
    {
        System.out.println("concurrent.Car counts: " + sharedRessources.getCars().size());
        int carCount = 0;
        for (CrossRoadsCase cell: sharedRessources.getCells())
        {
            if (cell.getContent() != null)
                carCount++;
        }
        System.out.println("Real car count: " + carCount);
    }

    private void removeOutOfScreenCars()
    {
        for (Iterator<ThreadCar> it = sharedRessources.getCars().iterator(); it.hasNext();)
        {
            ThreadCar threadCar = it.next();
            if (threadCar.getCar().getNextMove() == null)
            {
                sharedRessources.unSetCarFromCell(threadCar.getCar());
                it.remove();
            }
        }
    }

    private int getNumberOfAliveThreads()
    {
        int size = 0;
        for (ThreadCar c: sharedRessources.getCars())
        {
            if (c.getCar().getNextMove() != null)
                ++size;
        }
        return size;
    }

    private void moveCarAlgorithm()
    {
        if (!sharedRessources.getCars().isEmpty())
        {
            sharedRessources.incrementTurnNb();
            do {
                // On attend que toutes les threads soient prêtes à démarrer
                sharedRessources.resetNumberMoves();
                sharedRessources.getLockStart().lock();
                while (sharedRessources.getCountWaiting() != getNumberOfAliveThreads()) {
                    try {
                        sharedRessources.getConditionWait().await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                sharedRessources.setBarrierEndTurn(new CyclicBarrier(getNumberOfAliveThreads() + 1));
                sharedRessources.setHasStarted(true);
                sharedRessources.getConditionStart().signalAll();
                sharedRessources.getLockStart().unlock();
                try {
                    sharedRessources.getBarrierEndTurn().await();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                sharedRessources.getLockStart().lock();
                sharedRessources.reset();
                sharedRessources.getConditionEnd().signalAll();
                sharedRessources.getLockStart().unlock();
            }
            while (sharedRessources.getNumberMoves() != 0);
        }
    }
    public void step()
    {
        removeOutOfScreenCars();
        moveCarAlgorithm();
        createNewCars();
    }
}
