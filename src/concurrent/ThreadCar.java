package concurrent;

import java.awt.*;
import java.util.concurrent.BrokenBarrierException;

/**
 * Created by William TASSOUX on 18/05/2014.
 */
public class ThreadCar extends Thread {
    private Car car;
    private SharedRessources sharedRessources;
    private int turnNb;

    public ThreadCar(int num, Color clr, Itineraire iti, SharedRessources sharedRessources)
    {
        this.sharedRessources = sharedRessources;
        this.car = new Car(num, clr, iti);
        turnNb = sharedRessources.getTurnNb();
    }

    public Car getCar()
    {
        return car;
    }

    private  void makeMove()
    {
        sharedRessources.unSetCarFromCell(car);
        car.moveCar();
        sharedRessources.setCarToCell(car);
        sharedRessources.incrementNumberMoves();

    }

    public int getTurnNb()
    {
        return turnNb;
    }

    private boolean intersectsOnCrossroad(Car c2)
    {
        for (Position p1: car.getItineraire().getItineraire())
        {
            for (Position p2: c2.getItineraire().getItineraire())
            {
                if (sharedRessources.getCellFromPosition(p1).isCrossroad() && sharedRessources.getCellFromPosition(p2).isCrossroad())
                {
                    if (p1.equals(p2))
                        return (true);
                }
            }
        }
        return (false);
    }

    public void run()
    {
        while (car.getNextMove() != null) {
            sharedRessources.getLockStart().lock();
            sharedRessources.incrementCountWaiting();
            sharedRessources.getConditionWait().signal();
            // On attend le début du prochain tour
            while (!sharedRessources.hasStarted()) {
                try {
                    sharedRessources.getConditionStart().await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            sharedRessources.getLockStart().unlock();

            // La voiture n'a pas joué son tour
            if (turnNb != sharedRessources.getTurnNb())
            {
                synchronized (sharedRessources)
                {
                    // Soit la voiture est sur le carrefour, elle peut donc avancer sans s'inquiéter des conséquences
                    // Soit c'est une voiture sur la route, qui ne s'apprête pas à rentrer sur le carrefour, auquel cas elle ne peut avancer que si elle n'a aucune voiture devant elle
                    if (sharedRessources.getCellFromPosition(car.getPosition()).isCrossroad() ||
                            (!sharedRessources.getCellFromPosition(car.getPosition()).isCrossroad() && car.getNextMove() != null
                                    && !sharedRessources.getCellFromPosition(car.getNextMove()).isCrossroad() && sharedRessources.getCellFromPosition(car.getNextMove()).getContent() == null))
                    {
                        makeMove();
                        turnNb = sharedRessources.getTurnNb();
                    }
                    // Si la voiture essaye de s'insérer dans le carrefour
                    else if (!sharedRessources.getCellFromPosition(car.getPosition()).isCrossroad() && car.getNextMove() != null && sharedRessources.getCellFromPosition(car.getNextMove()).isCrossroad())
                    {
                        boolean canMove = true;
                        for (ThreadCar c : sharedRessources.getCars())
                        {
                            if (c == this)
                                continue;
                            // Une voiture sur le carrefour n'a pas encore bougé, elle a donc la priorité absolue et notre voiture ne peut bouger
                            if (sharedRessources.getCellFromPosition(c.getCar().getPosition()).isCrossroad() && c.getTurnNb() <= turnNb)
                            {
                                canMove = false;
                                break;
                            }
                            // Une voiture s'apprête à rentrer sur le carrefour et est d'id inférieur, elle a donc la priorité absolue
                            else if (!sharedRessources.getCellFromPosition(c.getCar().getPosition()).isCrossroad() && c.getCar().getNextMove() != null && sharedRessources.getCellFromPosition(c.getCar().getNextMove()).isCrossroad() && c.getId() < car.getId())
                            {
                                canMove = false;
                                break;
                            }
                            // Notre voiture croise une voiture du carrefour qui a déjà bougé, mais qui va croiser son chemin
                            else if (sharedRessources.getCellFromPosition(c.getCar().getPosition()).isCrossroad() && intersectsOnCrossroad(c.getCar()))
                            {
                                canMove = false;
                                break;
                            }

                        }
                        if (canMove)
                        {
                            makeMove();
                            turnNb = sharedRessources.getTurnNb();
                        }
                    }
                }
            }
            try {
                sharedRessources.getBarrierEndTurn().await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            sharedRessources.getLockStart().lock();
            while (sharedRessources.hasStarted()) {
                try {
                    sharedRessources.getConditionEnd().await();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            sharedRessources.getLockStart().unlock();
        }
     }
}
