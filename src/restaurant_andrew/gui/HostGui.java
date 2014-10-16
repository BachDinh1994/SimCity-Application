package restaurant_andrew.gui;

import restaurant_andrew.AndrewCustomerRole;
import restaurant_andrew.AndrewHostAgent;

import java.awt.*;


public class HostGui implements Gui {

    private AndrewHostAgent agent = null;

    private int xPos = -HOSTSIZE, yPos = -HOSTSIZE;//default waiter position
    //private int xDestination = -HOSTSIZE, yDestination = -HOSTSIZE;//default start position

    public static final int xTable1 = 640/4;
    public static final int yTable = 2*400/3;
    private static final int HOSTSIZE = 20;

    public HostGui(AndrewHostAgent agent) {
        this.agent = agent;
    }

    public void DoTellCustToWait(AndrewCustomerRole c, int listNum) {
    	c.getGui().DoGoToWaitSpot(20, 50 + 30 * listNum);
    }
    
    public void updatePosition() {
    	/*
        if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;

        boolean destIsTable = false;
        
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination != -HOSTSIZE) & (yDestination != -HOSTSIZE)) {
           agent.msgAtTable();
        }
        */
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, HOSTSIZE, HOSTSIZE);
    }

    public boolean isPresent() {
        return true;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}
