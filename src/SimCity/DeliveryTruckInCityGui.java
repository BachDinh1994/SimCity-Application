package SimCity;

import java.awt.Color;
import java.awt.Graphics2D;

import market.DeliveryTruckAgent;
import market.gui.DeliveryTruckGui;
import market.gui.DeliveryTruckGui.placeType;

public class DeliveryTruckInCityGui implements Gui {
/*have to create a different gui for the truck to move in 
	the city, not in the building panel. This will need to have
	access to the deliveryTruckAgent*/
	private DeliveryTruckAgent agent = null;
	private DeliveryTruckGui myInsideGui = null;

	public DeliveryTruckInCityGui(DeliveryTruckAgent a){
		
		agent = a;
	
	}
	private int xHome= 0, yHome =0;
	int xPos = xHome;
	int yPos = yHome;
	private int xDelivery, yDelivery;
	private int xDestination = xHome, yDestination = yHome;
	private int personX = 390, personY = 355;
	private int personDestX = 390, personDestY = 355;
    int wSize = 36;
	int hSize = 14;
	private int fSize = 10;
    private Address myRestaurantAddress = new Address(0,0);
    //activates truck in the city
    public boolean inCity = false;

    
    private boolean createPackage = false;
    private boolean packageFollow = false;
    private boolean enablePerson = false; //hack deliveryTruckDriver, just the gui 
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
    {atHome,atPickUp, none, inCar, atDelivery, onRoad, returnParking};
    placeType whereAmI = placeType.none;
    //food item colors
    
    public void setLocation(Address s){
    	myRestaurantAddress= s;
    }

	public void msgCreatePackage() {
		// TODO Auto-generated method stub
		createPackage= true;
	}

	public void goToDelivery(Address myAddress) {
		// TODO Auto-generated method stub
		xDestination = xHome + 40;
		yDestination = yHome;
		
		xDelivery = myAddress.x;
		yDelivery = myAddress.y;
		whereAmI = placeType.onRoad;
		
	}
	
	private void outsideParking(){
		xDestination = xDelivery;
		yDestination = yDelivery;
		whereAmI = placeType.atDelivery;
	}

	public void goBackToMarket() {
		// TODO Auto-generated method stub
		xDestination = xHome + 40;
		yDestination = yHome;
		
		whereAmI = placeType.returnParking;
	
	}
	
	public void updatePosition() {
		 check.x = xPos;
	        check.y = yPos;
	
	    
		if (xPos < xDestination && checkPosition(check.x +1, check.y, xDestination))
		{
				xPos++;
		}
        else if (xPos > xDestination && (checkPosition(check.x -1, check.y, xDestination)))
        {
        	    xPos--;
        }
        else if (yPos < yDestination && (checkPosition(check.x , check.y+1, xDestination)))
        {
        		yPos++;
        }
        else if (yPos > yDestination && (checkPosition(check.x , check.y-1, xDestination)))
        {
        	    yPos--;
        }
        if (xPos == xDestination && yPos == yDestination)
        {        			

        	if(whereAmI == placeType.onRoad){
        		whereAmI = placeType.none;
        		outsideParking();
        	}
        	
        	else if(whereAmI == placeType.atDelivery){
        		whereAmI = placeType.none;
				agent.msgAtDelivery();
				
        	}
        	else if(whereAmI == placeType.atHome){
        		whereAmI = placeType.none;
        		agent.msgAtMarket();
        	}
        	else if(whereAmI == placeType.returnParking){
        		whereAmI = placeType.none;
        		goHome();
        	}
        	
        }
       
        
	}
	
	private boolean checkPosition(int x, int y, int destination) {
		// TODO Auto-generated method stub
		//hack not allowing certrain areas to be walked through
		if(whereAmI == placeType.onRoad){
			if(y == yHome && (x >= xHome && x <=(xHome+50))){
			return true;
			}
			else{
				return false;
			}
		}
		else if (whereAmI == placeType.atHome){
			return true;
		}
		else{
		
			if((x < xHome +40 || x>xHome +60) && y>150){
				return false;

			}
			else if ((x > ( xDelivery +60) || x < (xDelivery)) && y < 150){
				return false;

			}
			else{
				
				return true;
			}

		}
		//if( (x >= xHome+ 50 && x<=xHome+80) && y >150){
			//return true;
		//}
		
		
	}


	
	private void getOnCar(){
		//goes back to car
		whereAmI = placeType.inCar;
	}
	
	private void goHome(){
		whereAmI = placeType.atHome;
		xDestination = xHome;
		yDestination = yHome;
	}
	
	
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		
		/*if(enablePerson){
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
	    }*/
	        
		g.setColor(Color.gray);
		g.fillRect(xHome, yHome, wSize, hSize);
		g.setColor(Color.orange);
        g.fillRect(xPos, yPos, wSize, hSize);
        
        g.setColor(Color.black);
        g.drawString("Truck", xPos+5, yPos +10);
        
        
 
	}
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return true;
	}

	public void setHome(Address s){
		xHome = s.x;
		yHome = s.y;
		xPos = xHome;
		yPos = yHome;
		xDestination = xPos;
		yDestination = yPos;
	}

	
	




}
