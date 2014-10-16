package market.gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import java.util.HashMap;
import java.util.Map;

import market.MarketEmployeeAgent;
import SimCity.Gui;

public class MarketEmployeeGui implements Gui
{
	private int xPos = 220, yPos = 320;
	private int xDestination, yDestination;
	private int xCashier = 160, yCashier = 180; 
	private int xFood, yFood;
	private int xHome = 220, yHome = 330;
    private int wSize = 20; //waitergui size
    private int fSize = 10;
    private boolean activateFood = false;
    private String food = null;
    Location check = new Location(xPos,yPos);
    
    //food item colors
    Map <String, Color> foodColor = new HashMap<String, Color>();
    {
    	foodColor.put("Pork", Color.black);
		foodColor.put("Chicken", Color.pink);
		foodColor.put("Fogra", Color.orange);
		foodColor.put("Fried Rice", Color.yellow);
		foodColor.put("Steak", Color.gray);
		foodColor.put("Beef Tenderloin", Color.green);
    }
    
    public boolean dontUpdate = true;
    public enum placeType
    {atHome, atCashier, atStorage,outDoor, inDoor, none};
    placeType whereAmI = placeType.none;
    
    private class Location{
    	public int x;
    	public int y;
    	Location(int xloc, int yloc){
    		x = xloc;
    		y = yloc;
    	}	
    }

    private  Map<String,Location> storageLot = new HashMap<String,Location>();
    {
    	addStorage();
    }

   
	private MarketEmployeeAgent agent = null;

	public MarketEmployeeGui(MarketEmployeeAgent a){
		agent = a;
		addStorage();
		xDestination = xHome;
		yDestination = yHome;
		
	}
		
	@Override
	public void updatePosition() {
	
		if (xPos < xDestination)
		{
			if(checkPosition(check.x +1, check.y, yDestination))
				xPos++;
			
		}
        else if (xPos > xDestination){
        	if(checkPosition(check.x -1, check.y, yDestination))
        		xPos--;
        }
        if (yPos < yDestination)
        {
        	if(checkPosition(check.x , check.y+1, yDestination))
            yPos++;
        }
        else if (yPos > yDestination)
        {
        	if(checkPosition(check.x , check.y-1, yDestination))
            yPos--;
        }
        if (xPos == xDestination && yPos == yDestination)
        {
        	if(whereAmI == placeType.atStorage){
        		whereAmI= placeType.outDoor;
        		activateFood = true;
            	goToDoor();
        	}
        	else if(whereAmI == placeType.atCashier){
        		whereAmI = placeType.inDoor;
        		activateFood = false;
        		
        		goToDoor();
        	}
        	else if (whereAmI == placeType.inDoor){
        		whereAmI = placeType.atHome;
        		agent.msgDoneRetrieving();
        		goHome();
        	}
        	else if(whereAmI == placeType.outDoor){
        		whereAmI= placeType.atCashier;
            	goToCashier();
        	}

        }
        check.x =xPos;
        check.y = yPos;
      
	}

	private boolean checkPosition(int x, int y, int destination) {
		// TODO Auto-generated method stub
		//hack not allowing certrain areas to be walked through
		
		if(x <=220 && x >=180){
			if((y>=10 && y<=240) || (y>=280 && y<=350)){
				return false;
			}
			return true;
		}
		else if (x >=260 && x<=450){
			
			if (destination < 40){
				if (y> 40)
					return false;
			}	
			else if (destination < 100){
				if (y> 100)
					return false;
			}	
			else if (destination < 160){
				if (y>160)
					return false;
			}
			
			
			if(y>20 && y<60){
				return false;
			}
			else if(y>80 && y<120){
				return false;
			}
			else if(y>140 && y<180){
				return false;
			}
			else if(y>210 && y<350){
				return false;
			}
			
			
				
			return true;
		}
		
		else{
			return true;
		}
		
	}

	private void goToDoor(){
		xDestination = 200;
		yDestination = 250;
	}
	private void goToCashier() {
		// TODO Auto-generated method stub
		
		xDestination = xCashier;
		yDestination = yCashier;
	}
	private void goHome() {
		// TODO Auto-generated method stub
		xDestination = xHome;
		yDestination = yHome;
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		g.setColor(Color.CYAN);
        g.fillRect(xPos, yPos, wSize, wSize);
        g.setColor(Color.black);
        g.drawString("E", xPos+5, yPos+10);
  
        if(activateFood){
        	//show food gui
        	g.setColor(foodColor.get(food));
        	g.fillRect(xPos-5, yPos-5, fSize,fSize);
        }
        
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return true;
	}

	public void getItems(Map<String, Integer> order, String choice) {
		// TODO Auto-generated method stub
		xDestination = storageLot.get(choice).x;
		yDestination = storageLot.get(choice).y;
		xFood = xDestination;
		yFood = yDestination;
		food = choice;
		whereAmI = placeType.atStorage;
	}

	public void returnHomeBase() {
		// TODO Auto-generated method stub
		
	}

	public void setStorage(Map<String, Location> map){
		storageLot = map;
	}
	
	public Map<String,Location> getStorage(){
		return storageLot;
	}
	
	private void addStorage(){
		Location pork = new Location(280+140, 20);
		storageLot.put("Pork", pork);
		Location chicken = new Location( 280 +40, 60);
		storageLot.put("Chicken", chicken);
		Location fogra = new Location( 280 +10, 80);
		storageLot.put("Fogra", fogra);
		Location fried = new Location( 280 +120, 120);
		storageLot.put("Fried Rice", fried);
		Location steak = new Location( 280 +80 ,140);
		storageLot.put("Steak", steak);
		Location beef = new Location( 280 , 180);
		storageLot.put("Beef Tenderloin", beef);
	}
    
}