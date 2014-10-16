package market.gui;



import java.awt.*;
import java.util.Map;

import SimCity.Address;
import SimCity.DeliveryTruckInCityGui;

import market.DeliveryTruckAgent;
import market.gui.MarketGui.placeType;

import SimCity.Gui;

public class DeliveryTruckGui implements Gui
{

	private DeliveryTruckAgent agent = null;

	public DeliveryTruckGui(DeliveryTruckAgent a){
		agent = a;
	}
	private int xPos = 360, yPos = 355;
	private int xDestination = 360, yDestination = 355;
	private int pickUPx = 360, pickUPy = 355; 
	private int xHome = 360, yHome = 355;
	private int personX = 390, personY = 355;
	private int personDestX = 390, personDestY = 355;
	private int offScreenX = 560, offScreenY = 355;
    private int wSize = 80, hSize = 40, fSize = 10;
    
    //activates truck in the city
    public boolean inCity = false;
	private DeliveryTruckInCityGui myCityGui;

    
    private boolean createPackage = false;
    private boolean packageFollow = false;
    private int xBox =160 , yBox =170;
    private boolean enablePerson = false; //hack deliveryTruckDriver 
    private String food = null;
    private class Location{
    	public int x;
    	public int y;
    	Location(int xloc, int yloc){
    		x = xloc;
    		y = yloc;
    	}	
    }
    
    private Location check = new Location(xPos, yPos);
    public enum placeType
    {atHome, atItem,atPickUp, none, inCar, atDelivery};
    placeType whereAmI = placeType.none;
    //food item colors
    
    

	public void msgCreatePackage() {
		// TODO Auto-generated method stub
		createPackage= true;
	}


	public void goToDelivery(Address myAddress) {
		// TODO Auto-generated method stub
		enablePerson = false;
		createPackage = false;
		
		//panel goes off screen
		//now the TruckGui is used in the CityPanel
		xDestination = offScreenX;
		inCity = true;
		whereAmI = placeType.atDelivery;
	}
	
	public void goBackToMarket(){
		agent.msgAtMarket();
	}

	public void goToPickUp() {
		// TODO Auto-generated method stub
		xDestination = pickUPx;
		yDestination = pickUPy;
		
		personDestX = 160;
		personDestY = 190;
		
		enablePerson = true;
		whereAmI = placeType.atPickUp;
		//		agent.msgAtPickUpArea();

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

        	if(enablePerson){
        		if(personX < personDestX){
        			if(checkPosition(check.x +1, check.y, yDestination))
        				personX++;}
        		else if (personX >personDestX){
        			if(checkPosition(check.x -1, check.y, yDestination))
        			 personX--;}
        		
        		if (personY < personDestY){
        			if(checkPosition(check.x, check.y+1, yDestination))
        			personY++;}
        		else if (personY > personDestY){
        			if(checkPosition(check.x, check.y-1, yDestination))
            			personY--;

        		}
        		if(personY == personDestY && personX == personDestX){
        			if(whereAmI == placeType.atPickUp){
        				whereAmI = placeType.none;
        				 packageFollow = true;
        				 getOnCar();
        			}
        			else if (whereAmI == placeType.inCar){
        				whereAmI = placeType.none;
        				agent.msgAtPickUpArea();
        			}
        			
        		}
        		
        		
        	}
        	
        	if(whereAmI == placeType.atDelivery){
        		whereAmI = placeType.none;
        		//agent.msgAtDelivery();
				
        	}
        	
        }
        check.x = personX;
        check.y = personY;
        
        
	}
	
	private boolean checkPosition(int x, int y, int destination) {
		// TODO Auto-generated method stub
		//hack not allowing certrain areas to be walked through
		
		if(x>=200 && x<380 && y>=300)
			return false;
		else if(x>=230 && y<=280){
			return false;
		}
		else if(x <=220 && x >=180){
			if((y>=10 && y<=240) || (y>=260 && y<=350)){
				return false;
			}
			return true;
		}
		
		else{
			return true;
		}
		
	}




	
	private void getOnCar(){
		//goes back to car
		whereAmI = placeType.inCar;
		personDestX = pickUPx + 30;
		personDestY = pickUPy;
	}
	private void goHome(){
		xDestination = xHome;
		yDestination = yHome;
	}
	
	
	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		
		if(enablePerson){
        	g.setColor(Color.RED);
        	g.fillRect(personX, personY, 20, 20);
        }
		
		if(createPackage){
	        	//show food gui
	        	
	        if(packageFollow){
	       		//package moves with the truckdriver
	        	g.setColor(Color.yellow);
		      	g.fillRect(personX-5, personY+20, 14,14);
		       	g.setColor(Color.black);
		       	g.drawLine(personX+2, personY+20, personX+2, personY+34);
		       	g.drawLine(personX-5, personY+27, personX+9,personY+27);	
	       	}
	        else{	
	        	g.setColor(Color.yellow);
	        	g.fillRect(xBox, yBox, 14,14);
	        	g.setColor(Color.black);
		      	g.drawLine(xBox + 7, yBox, xBox+7, yBox+14);
		       	g.drawLine(xBox, yBox+7, xBox+14,yBox+7 );
	       	}
	    }
	        
		g.setColor(Color.orange);
        g.fillRect(xPos, yPos, wSize, hSize);
        g.setColor(Color.black);
        g.drawString("Truck", xPos+5, yPos +10);
        
        
      
       
        
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public void setInCityGui(DeliveryTruckInCityGui d){
		myCityGui = d;
	}





	
}