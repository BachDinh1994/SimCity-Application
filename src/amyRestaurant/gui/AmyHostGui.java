package amyRestaurant.gui;

import static java.lang.System.*;



import java.util.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import amyRestaurant.AmyCustomerRole;
import amyRestaurant.AmyHostAgent;
import amyRestaurant.AmyWaiterRole;


public class AmyHostGui implements Gui {

    private AmyHostAgent agent = null;
    
    private int xPos = -20, yPos = -20;//default waiter position
    private int xDestination = -20, yDestination = -20;//default start position
   
    public AmyHostGui(AmyHostAgent agent) {
        this.agent = agent;
        
    }
    
    
    
    public void msgGrantedBreak(){
    	//tell 
    	
    	
    }
    
    public void msgDeniedBreak(){
    	
    }


	public void updatePosition() {
		
    	if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;

   
     
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



	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}
}
