import java.awt.*;
public class Car extends Thread{

	private int num;
	private Color clr;
	private int speed;
	private Position positionActuelle;
	private Position destination;
	private Direction direction;

	public static final int MAXSPEED = 3;
	public static final int MINSPEED = 0;


	public Car(int num, Color clr, int speed, Position positionDep, Position destination) {
		this.num = num;
		this.clr = clr;
		this.speed = speed;
		this.positionActuelle = positionDep;
		this.destination = destination;
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
		return positionActuelle;
	}

	public void move()
	{
		if()
	}

	public void run()
	{
		while(true){
			try {
				sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			move();
		}
	}
}
