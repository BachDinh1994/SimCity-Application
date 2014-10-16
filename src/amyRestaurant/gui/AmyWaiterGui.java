package amyRestaurant.gui;

import static java.lang.System.*;

import java.util.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import amyRestaurant.AmyCustomerRole;
import amyRestaurant.AmyWaiterRole;
import amyRestaurant.interfaces.AmyWaiter;


public class AmyWaiterGui implements Gui {

    private AmyWaiter agent = null;
    
    private int xPos = -20, yPos = -20;//default waiter position
    private int xDestination = -20, yDestination = -20;//default start position
    private AnimationPanel anim = new AnimationPanel();
    private int xHome, yHome;
    private int wSize = 20; //waitergui size
    private String foodItem="";
   public boolean dontUpdate = true;
    public enum placeType
    {pickUpCust, atOutside, atCashier, atCook, none};
    placeType whereAmI = placeType.none;
    public int xTable = AnimationPanel.hashTable.get(1);
    private RestaurantGui restGui;
    private AmyCustomerGui currentGui;
    public int yTable = 250;
    
    public String msg = "None";
    Image img;
    
    private boolean wantBreak = false; 

    public AmyWaiterGui(AmyWaiter agent) {
        this.agent = agent;
		xPos = 40;
		yPos = -20;
		xDestination = 40;
		yDestination = -20;
		xHome = 0;
		yHome = 0;
		foodItem = "";
		wSize= 20; 
    }
    
    
    
    public AmyWaiterGui(AmyWaiterRole w, RestaurantGui gui) {
    	
    		agent = w;
    		xPos = 40;
    		yPos = -20;
    		xDestination = 40;
    		yDestination = -20;
    		xHome = 0;
    		yHome = 0;
    		foodItem = "";
    		wSize= 20; 
    		restGui = gui;

    	
	}
    
    public void setHome(int x, int y){
    	xHome = x;
    	yHome = y;
    	xDestination = x;
    	yDestination = y;
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

        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xTable + 20) & (yDestination == yTable - 20)) {
        	agent.msgAtTable();
        }
        if(xPos == xDestination && yPos == yDestination && whereAmI == placeType.atCook){
        		//& (xDestination == 510) & (yDestination == 545)){
        	whereAmI = placeType.none;
        	agent.msgAtCook();
        }
        if(xPos == xDestination && yPos == yDestination
        		& (xDestination == 20) & (yDestination == 540) && whereAmI == placeType.atOutside){
        	whereAmI = placeType.none;
        	agent.msgAtOutside();}
        
        if(xPos == xDestination && yPos == yDestination 
        		&& whereAmI == placeType.pickUpCust){
        	whereAmI = placeType.none;
        	agent.msgAtHome();}
        
        if(xPos == xDestination && yPos == yDestination
        		& (xDestination == 500) & (yDestination == 30) & whereAmI == placeType.atCashier){
        	whereAmI = placeType.none;
        	agent.msgAtCashier();}
        
        
    }
    
    public void doGoToBreak(){
    	breakStatus("Granted");
    	whereAmI = placeType.atOutside;
    	xDestination = 20;
    	yDestination = 540;
    }


    public void draw(Graphics2D g) {
    	g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, wSize, wSize);
        
    	g.setColor(Color.BLACK);
		g.setFont(new Font(null, Font.PLAIN, 20));
		g.drawString(foodItem, xPos,yPos+6);
		
 
    }
    
    public void breakStatus(String statusB){
    	msg = statusB;
    }
    public void returnWork(){
    	msg = "None";
    	agent.msgWantToReturn();
    }
    
    public void setBreak(){
    	wantBreak = true;
    	agent.msgWantToGoToBreak();
    }
    
    public boolean wantBreak() {
		return wantBreak;
	}

    public boolean isPresent() {
        return true;
    }

    public void DoBringToTable(AmyCustomerRole customer, int tableNumber) {
    	
    	
    	xTable = anim.hashTable.get(tableNumber);
    	yTable = 250;
   
    	customer.getGui().goTo(xTable,yTable); //hack from host gui to customer gui to go to certain table
 
        xDestination = xTable + 20;
        yDestination = yTable - 20;
    
    	
    }
    
    
    
    public void goToCashier()
    {
    	whereAmI = placeType.atCashier;
		System.out.println("going to cashier");

    	xDestination = 500;
    	yDestination = 30;
    }

    public void goToTable(int tableNumber)
    {
    	xTable = anim.hashTable.get(tableNumber);
    	yTable = 250;
    	xDestination = xTable + 20;
    	yDestination = yTable - 20;
    }

    public void DoPickCustomer(AmyCustomerRole cust) {
		whereAmI = placeType.pickUpCust;
		System.out.println("picking customer");
        xDestination = cust.getGui().getX() + 20;
        yDestination = cust.getGui().getY()- 20;
        
    }
    
	public void DoLeaveCustomer() {
        xDestination =  xHome;
        yDestination = yHome;
    }
	
	public void doReturnWork(){
		xDestination = xHome;
        yDestination = yHome;	
        //set break button again
        restGui.updateInfoPanel("Waiter", (Object)agent, false);
	}
	
	public void DoGoToCook(){
		whereAmI = placeType.atCook;
		xDestination = 510;
		yDestination = 545;
	}
	
	public void DoPickUpFood(String food){
		whereAmI = placeType.atCook;
		yDestination = 545;
		if(food.equals("Steak")){
			xDestination = 410;
		}
		else if(food.equals("Chicken")){
			xDestination = 440;
		}else if(food.equals("Salad")){
			xDestination = 470;
		}else if(food.equals("Pizza")){
			xDestination = 510;
		}
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
