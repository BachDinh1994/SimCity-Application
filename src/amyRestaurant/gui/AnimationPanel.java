package amyRestaurant.gui;

import javax.swing.*;
import java.util.Hashtable;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.List;
import java.util.ArrayList;

public class AnimationPanel extends JPanel implements ActionListener {

	public static Hashtable<Integer, Integer> hashTable = new Hashtable<Integer, Integer>();
	{hashTable.put(1,150);
	hashTable.put(2, 270);
	hashTable.put(3, 390);}
	
	
	
	private final int frameInterval = 7;
	
	private final int WINDOW_W = 450;
    private final int WINDOW_H = 400;
    private final int WINDOW_X = 0;
    private final int WINDOW_Y = 0;
    
    private final int TABLE_Y = 250;
    private final int TABLE_H = 75;
    private final int TABLE_W =50;
    
    private Image bufferImage;
    private Dimension bufferSize;

    private List<Gui> guis = new ArrayList<Gui>();
    public boolean paused = false;

    public AnimationPanel() {
    	setSize(WINDOW_W, WINDOW_H);
        setVisible(true);
        
        bufferSize = this.getSize();
 
    	Timer timer = new Timer(frameInterval, this );
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g) 
    {
        Graphics2D g2 = (Graphics2D)g;
       
        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(WINDOW_X, WINDOW_Y, this.getWidth(), this.getHeight() );

        //draw boundary
        g2.setColor(Color.BLACK);
        g2.drawRect(WINDOW_X, WINDOW_Y, 635, this.getHeight());
        
        //draw patio for waiter
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(1,this.getHeight()-140, 75 , 140);
        g2.setColor(Color.BLACK);
        g2.drawRect(1,this.getHeight()-140, 75 , 140);
        
       
        //draw cook station
        g2.setColor(Color.cyan);
        g2.fillRect(this.getWidth()-250,this.getHeight()-100, 230 , 100);
        
        //plating station
        g2.setColor(Color.GREEN);
        g2.fillRect(this.getWidth()-250,this.getHeight()-100, 150 , 35);
        g2.setColor(Color.BLACK);
        g2.drawRect(this.getWidth()-250,this.getHeight()-100, 150 , 35);
       
        //cooking station
        g2.setColor(Color.BLUE);
        g2.fillRect(this.getWidth()- 70,this.getHeight()-60, 50 , 60);
        g2.setColor(Color.WHITE);
        g2.drawOval(this.getWidth()- 60,this.getHeight()-55, 22, 22);
        g2.drawOval(this.getWidth()- 57,this.getHeight()-52, 15, 15);
        g2.drawOval(this.getWidth()- 60,this.getHeight()-25, 22, 22);
        g2.drawOval(this.getWidth()- 57,this.getHeight()-22, 15, 15);
     	g2.setColor(Color.BLACK);
     	g2.setFont(new Font(null, Font.PLAIN, 15));
     	g2.drawString("St", 410,this.getHeight()-90 );
     	g2.drawString("Ch", 440,this.getHeight()-90 );
     	g2.drawString("Sa", 475,this.getHeight()-90 );
     	g2.drawString("Pi", 510,this.getHeight()-90 );
		
	    //cashier station
     	g2.setColor(Color.CYAN);
        g2.fillRect(520,2, 70,100);
       
        //draw waiting station for customer
        g2.setColor(Color.black);
        g2.fillRect(1, 1, 80, 220);
        g2.setColor(Color.LIGHT_GRAY);
        
        //draw waiter home station
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(100,2, 300 , 70);
        g2.setColor(Color.BLACK);
        g2.drawRect(100,2, 300 , 70);
        
        for (int i = 0; i < 6; i ++){
        	 g2.setColor(Color.BLACK);
             g2.drawRect(100 + i*50,2, 35 , 35);
        }
        //draw cashier station
        //Here is the table
        g2.setColor(Color.YELLOW);
        
        g2.fillRect(hashTable.get(1),TABLE_Y, TABLE_W,TABLE_H);//200 and 250 need to be table parameter
        g2.fillRect(hashTable.get(2), TABLE_Y, TABLE_W,TABLE_H);
        g2.fillRect(hashTable.get(3), TABLE_Y,  TABLE_W	, TABLE_H);
        
        for(Gui gui : guis) {
            if (gui.isPresent()) {
            	if(!paused){
                gui.updatePosition();}
            }
        }

        for(Gui gui : guis) {
        	
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }
    }
    
    public void addGui(AmyWaiterGui gui){
    	guis.add(gui);
    }

    public void addGui(AmyCustomerGui gui) {
        guis.add(gui);
    }
    
    public void addGui(AmyCookGui gui) {
        guis.add(gui);
    }

  
}
