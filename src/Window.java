import java.awt.Color;
import java.util.Vector;

import javax.swing.JFrame;
public class Window extends JFrame{
	
	private Panel pan = new Panel();
	private Road r1=new Road(1,"national");
	
	public Window(){
		this.setTitle("Animation");
		this.setSize(720,755);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setContentPane(pan);
		this.setVisible(true);

		go();
	}
	private void go(){
		Light f1=new Light(20,Color.green,180,450);
		Light f2=new Light(20,Color.red,220,180);
		Light f3=new Light(20,Color.green,490,220);
		Light f4=new Light(20,Color.red,450,490);
		Car v1=new Car(50,Color.red,5,10,400);
		Car v2=new Car(50,Color.red,-5,720,300);
		r1.addFeu(f1);
		r1.addFeu(f2);
		r1.addFeu(f3);
		r1.addFeu(f4);
		r1.addVehicule(v1);
		r1.addVehicule(v2);
		Vector<Light> f=r1.getF();
		Vector<Car> v=r1.getVehicules();

		for(int i=0;i<f.size();i++)
		{
			f.get(i).start();
		}

		for(int i=0;i<v.size();i++)
		{
			v.get(i).start();
		}

		while(true)
		{

			//System.out.println(v1.getX());
			//System.out.println(f1.getClr());
			try {
				Thread.sleep(60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			pan.setFeu(r1.getF());
			pan.setVehicule(r1.getVehicules());
			v1.setOnMove(true);
			v2.setOnMove(true);
			if(v1.getX()==f1.getX()-20 && f1.getClr().equals(Color.red))
				v1.setOnMove(false);

			if(v2.getX()>f3.getX() && v2.getX()<f3.getX()+20 && f3.getClr().equals(Color.red))
				v2.setOnMove(false);

			if(v1.getX()>pan.getWidth())
				v1.setX(0);
			if(v2.getX()<0)
				v2.setX(pan.getWidth());

			pan.repaint();
		}
	}

	public static void main(String[] args) {
		Window f=new Window();
	}
}

