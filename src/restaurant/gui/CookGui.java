package restaurant.gui;

import SimCity.Gui;
import restaurant.CookAgent;

import java.awt.*;

public class CookGui implements SimCity.Gui 
{
    static final int width = 20, height = 20;
    private int xPos = 210, yPos = 280;
    private int xDestination=210;
    int[] table = new int[3];
    public static final int yTable = 150;
    RestaurantGui gui;
    
    public CookGui(CookAgent agent, RestaurantGui gui) 
    {
        for (int i=0;i<3;i++)
        {
        	table[i] =(i+1)*100; 
        }
        this.gui = gui;
    }
    public CookGui(CookAgent agent) 
    {
        for (int i=0;i<3;i++)
        {
        	table[i] =(i+1)*100; 
        }
    }
    public void MoveToHomePosition()
    {
    	xDestination = 210;
    }
    public void updatePosition() 
    {
        if (xPos < xDestination)
            xPos+=2;
        else if (xPos > xDestination)
            xPos-=2;
    }
    public void MoveToFridge()
    {
    	xDestination = 360;
    }
    public void MoveToTablePos(int tablenum)
    {
    	xDestination = table[tablenum-1]+20;
    }
    public void draw(Graphics2D g) 
    {
    	g.setColor(Color.CYAN);
        g.fillRect(xPos, yPos, width, height);
    }
    public boolean isPresent() 
    {
        return true;
    }
}