package concurrent;

import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by William TASSOUX on 18/05/2014.
 */
 public class SharedRessources {
    private CyclicBarrier barrierEndTurn;
    private Lock lockStart;
    private Condition conditionStart;
    private Condition conditionEnd;
    private Condition conditionWait;
    private boolean hasStarted;
    private int countWaiting;
    private CyclicBarrier barrierEndCrossroadChallenge;
    private ArrayList<CrossRoadsCase> cells;
    private int numberMoves;
    private int turnNb;
    private ArrayList<ThreadCar> cars;

    public SharedRessources()
    {
        lockStart = new ReentrantLock();
        conditionStart = lockStart.newCondition();
        conditionEnd = lockStart.newCondition();
        conditionWait = lockStart.newCondition();
        countWaiting = 0;
        hasStarted = false;
    }

    synchronized public void reset()
    {
        countWaiting = 0;
        hasStarted = false;
    }

    synchronized public ArrayList<ThreadCar> getCars() {
        return cars;
    }

    synchronized public void setCars(ArrayList<ThreadCar> cars) {
        this.cars = cars;
    }

    synchronized  public ArrayList<CrossRoadsCase> getCells() {
        return cells;
    }

    synchronized public int getCountWaiting() {
        return countWaiting;
    }

    synchronized public Condition getConditionWait() {
        return conditionWait;
    }

    synchronized public void incrementCountWaiting() {
        this.countWaiting++;
    }

    public Condition getConditionEnd() {
        return conditionEnd;
    }

    synchronized public Lock getLockStart() {
        return lockStart;
    }

    synchronized public Condition getConditionStart() {
        return conditionStart;
    }


    synchronized public boolean hasStarted() {
        return hasStarted;
    }

    synchronized public void setHasStarted(boolean hasStarted) {
        this.hasStarted = hasStarted;
    }

    synchronized public int getNumberMoves() {
        return numberMoves;
    }

    synchronized public void incrementNumberMoves() {
        this.numberMoves++;
    }

    synchronized public void resetNumberMoves() {
        this.numberMoves = 0;
    }

    synchronized public int getTurnNb() {
        return turnNb;
    }

    synchronized public void incrementTurnNb() {
        this.turnNb++;
    }

    synchronized public void setCells(ArrayList<CrossRoadsCase> cells) {
        this.cells = cells;
    }

    synchronized public CyclicBarrier getBarrierEndTurn() {
        return barrierEndTurn;
    }

    synchronized public void setBarrierEndTurn(CyclicBarrier barrierEndTurn) {
        this.barrierEndTurn = barrierEndTurn;
    }

    synchronized public CyclicBarrier getBarrierEndCrossroadChallenge() {
        return barrierEndCrossroadChallenge;
    }

    synchronized public void setBarrierEndCrossroadChallenge(CyclicBarrier barrierEndCrossroadChallenge) {
        this.barrierEndCrossroadChallenge = barrierEndCrossroadChallenge;
    }

    synchronized public CrossRoadsCase getCellFromPosition(Position pos)
    {
        return (cells.get(pos.getX() * Consts.size + pos.getY()));
    }

    synchronized public void setCarToCell(Car car)
    {
        getCellFromPosition(car.getPosition()).setContent(car);
    }

    synchronized public void unSetCarFromCell(Car car)
    {
        getCellFromPosition(car.getPosition()).setContent(null);
    }


}
