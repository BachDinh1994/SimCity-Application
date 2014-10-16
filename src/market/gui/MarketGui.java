package market.gui;

import market.MarketAgent;
import market.MarketEmployeeAgent;
import market.gui.MarketEmployeeGui.placeType;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import SimCity.Address;

import SimCity.Gui;

public class MarketGui implements Gui 
{

	private MarketAgent agent = null;
	
	public MarketGui(MarketAgent a){
		agent = a;
	}
	
	private int xPos = 160, yPos = 130, xFood, yFood;
	private int xDestination = 160, yDestination = 130;
	private int xItem = 160, yItem = 160; 
	private int xHome = 160, yHome = 130;
	
    private int wSize = 20; //waitergui size
    private int fSize = 10;
    private boolean activateFood = false;
    private String food = null;
    
    public enum placeType
    {atHome, atItem, none, wrapItem};
    placeType whereAmI = placeType.none;
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
    
    public void msgGiveItem(String food){
    	//gives itemm to the customer
    	//animation  hack
    	xDestination = xItem;
    	yDestination =yItem;
    	whereAmI = placeType.atItem;
    }
    public void msgWrapIntoDelivery() {
		// TODO Auto-generated method stub
		//wrap it into deliverypackage
    	xDestination = xItem;
    	yDestination = yItem;
    	whereAmI = placeType.wrapItem;
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
        if (xPos == xDestination && yPos == yDestination)
        {
        	if(whereAmI == placeType.atItem){
        		whereAmI = placeType.none;
        		giveItem();
        		goHome();
        	}
        	if(whereAmI == placeType.wrapItem){
        		whereAmI = placeType.none;
        		agent.getGui().msgWrapIntoDelivery();
        		goHome();
        	}
        	
        }
        if(activateFood){
        	if(xFood <120){
        		xFood--;
        	}
        	if(xFood == 120){
        		activateFood = false;
        	}
        	
        }
        
	}

	
	private void goHome(){
		xDestination = xHome;
		yDestination = yHome;
	}
	private void giveItem(){
		activateFood = true;
		xFood = xPos-5;
		yFood = yPos +10;
	
	}
	
	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		g.setColor(Color.orange);
        g.fillRect(xPos, yPos, wSize, wSize);
        g.setColor(Color.black);
        g.drawString("M", xPos+5, yPos+10);
  
       
        
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return true;
	}


	
}
	