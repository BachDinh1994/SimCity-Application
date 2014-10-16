package amyRestaurant.gui;



import java.util.*;
import java.awt.*;

import amyRestaurant.AmyCookAgent;
import amyRestaurant.AmyCustomerRole;
import amyRestaurant.AmyWaiterRole;
import amyRestaurant.gui.AmyWaiterGui.placeType;


public class AmyCookGui implements Gui {

    private AmyCookAgent agent = null;
    public boolean pickup = false;
    private int xPos = 400, yPos = 420, xHome, yHome;//default waiter position
    private int xDestination = 370, yDestination = 400;//default start position
    
    private String currFood;
    private int wSize = 20; //waitergui size
    private String foodItem="";
    
    public boolean dontUpdate = true;
    public enum placeType
    {cooking, nothing, plating};
    placeType whereAmI = placeType.nothing;
    
   
    private RestaurantGui restGui;
   
    public String msg = "None";

 

    public AmyCookGui(AmyCookAgent agent) {
        this.agent = agent;
        
    }
    
 
    
    public AmyCookGui(AmyCookAgent c, RestaurantGui gui) {
    	
    		agent = c;
    		xPos = 450;
    		yPos = 800;
    		xDestination = 500;
    		yDestination = 610;
    		xHome = 500;
    		yHome = 610;
    		foodItem = "";
    		wSize= 20; 
    		restGui = gui;
    		whereAmI = placeType.nothing;
    	
	}
    
    public void setHome(int x, int y){
    	xHome = x;
    	yHome = y;
    }
    
    public int getXHome(){
    	return xHome;
    }

    public int getYHome(){
    	return yHome;
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

        if(xPos == xDestination && yPos == yDestination
        		& (xDestination == 560) & (yDestination == 610) & whereAmI == placeType.cooking){
        	whereAmI = placeType.nothing;
        	goPlating();
        	createCustomerFood(currFood);
        }
        
        if(xPos == xDestination && yPos == yDestination
        		& whereAmI == placeType.plating){
        	whereAmI = placeType.nothing;
        	createCustomerFood("");
        	goHome();
        }
        
    }
    
   
    public void draw(Graphics2D g) {
    	g.setColor(Color.WHITE);
        g.fillRect(xPos, yPos, wSize, wSize);
        g.setColor(Color.black);
        g.drawRect(xPos, yPos, wSize, wSize);
        
    	g.setColor(Color.BLACK);
		g.setFont(new Font(null, Font.PLAIN, 20));
		g.drawString(foodItem, xPos + 10,yPos +5);
 
    }
    

    public boolean isPresent() {
        return true;
    }
    
    public void goHome(){
    	xDestination = xHome;
    	yDestination = yHome;
    	whereAmI = placeType.nothing;
    	
    	
    }
    
    public void goPlating(){
    	xDestination = 470;
    	yDestination = 600;
    	whereAmI = placeType.plating;
    	
    	
    }

    public void DoCooking(String food){
    	xDestination = 560;
    	yDestination = 610;
    	whereAmI = placeType.cooking;
    	currFood = food;
    	
    
    	
    }
    
    public void goToWaiter(){
    	xDestination = 560;
    	yDestination = 645;
    }
    
    

	public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
    public void createCustomerFood(String food){
    	if(food == "Steak")
    		foodItem = "St";
    	else if(food == "Chicken")
    		foodItem = "Ch";
    	else if(food == "Salad")
    		foodItem = "Sa";
    	else if(food == "Pizza")
    		foodItem = "Pi";
    	else if(food == "Check")
    		foodItem = "$";
    	else
    		foodItem ="";
    }
    


}
