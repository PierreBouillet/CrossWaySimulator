 import java.awt.Color;
import java.awt.Graphics;
import java.util.Vector;

import javax.swing.JPanel;
public class Panel extends JPanel {

private Vector<Light> f;
private Vector<Car> v;

public Panel(){
	f= new Vector<Light>();
	v = new Vector<Car>();
}

public void paintComponent(Graphics g){
      
       g.setColor(Color.white);
       g.fillRect(0, 0, this.getWidth(), this.getHeight());
      
       /* route  horizontale */
       g.setColor(Color.black);
       g.fillRect(0, 250, 720, 200);
      
       g.setColor(Color.white);
       g.fillRect(10,340,60,20);
      
      

       g.setColor(Color.white);
       g.fillRect(100,340,60,20);
      
       /* passage du pieton */
       g.setColor(Color.white);
       g.fillRect(190,420, 60,20);
      
       g.setColor(Color.white);
       g.fillRect(190,380,60,20);
      
       g.setColor(Color.white);
       g.fillRect(190,340,60,20);
      
       g.setColor(Color.white);
       g.fillRect(190,300, 60,20);
      
       g.setColor(Color.white);
       g.fillRect(190,260,60,20);
      
       /*FIN PASSAGE*/
      
       /* passage du pieton */
       g.setColor(Color.white);
       g.fillRect(450,420, 60,20);
      
       g.setColor(Color.white);
       g.fillRect(450,380,60,20);
      
       g.setColor(Color.white);
       g.fillRect(450,340,60,20);
      
       g.setColor(Color.white);
       g.fillRect(450,300, 60,20);
      
       g.setColor(Color.white);
       g.fillRect(450,260,60,20);
      
       /*FIN PASSAGE*/
       g.setColor(Color.white);
       g.fillRect(540,340,60,20);
      
       g.setColor(Color.white);
       g.fillRect(630,340,60,20);
       /*fin route*/
      
       /*route  verticale*/
       g.setColor(Color.black);
       g.fillRect(250,0,200 ,720 );
      
       g.setColor(Color.white);
       g.fillRect(340,10,20,60);
      
      
       g.setColor(Color.white);
       g.fillRect(340,100,20,60);
      

       /* passage du pieton */
       g.setColor(Color.white);
       g.fillRect(420,190,20,60);
      
       g.setColor(Color.white);
       g.fillRect(380,190,20,60);
      
       g.setColor(Color.white);
       g.fillRect(340,190,20,60);
      
       g.setColor(Color.white);
       g.fillRect(300,190,20,60);
      
       g.setColor(Color.white);
       g.fillRect(260,190,20,60);
      
       /*FIN PASSAGE*/
      
       /* passage du pieton */
       g.setColor(Color.white);
       g.fillRect(420,450,20 ,60);
      
       g.setColor(Color.white);
       g.fillRect(380,450,20,60);
      
       g.setColor(Color.white);
       g.fillRect(340,450,20,60);
      
       g.setColor(Color.white);
       g.fillRect(300,450,20,60);
      
       g.setColor(Color.white);
       g.fillRect(260,450,20,60);
      
       /*FIN PASSAGE*/
       g.setColor(Color.white);
       g.fillRect(340,540,20,60);
      
       g.setColor(Color.white);
       g.fillRect(340,630,20,60);
       /*fin route*/
      
       for(int i=0;i<f.size();i++)
       {
             g.setColor(f.get(i).getClr());
             g.fillOval(f.get(i).getX(),f.get(i).getY(),30,30);
       }
      
       for(int i=0;i<v.size();i++)
       {
             g.setColor(v.get(i).getClr());
             g.fillRect(v.get(i).getX(),v.get(i).getY(),30,30);
       }
      
       }
      

       public void setFeu(Vector<Light> f1) {
             f=f1;
       }
       public void setVehicule(Vector<Car> v1) {
             v=v1;
       }
       }