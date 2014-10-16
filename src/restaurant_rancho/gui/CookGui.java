package restaurant_rancho.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import restaurant_rancho.RanchoCook;
import restaurant_rancho.RanchoWaiterRole;
import restaurant_rancho.interfaces.Waiter;

public class CookGui implements Gui {
	private RanchoCook agent =null;
	private RestaurantGui gui;
	private List<Label> labelList = new ArrayList<Label>();
	public enum LabelState{moving,fixing,hiding};
	
	private int xPos = 200, yPos = 380;//default waiter position
    private int xDestination = 200, yDestination = 380;//default start position
	
    private int xFrige = 350;
    private int yFrige = 390;
    
    private int xCooking =300;
    private int yCooking = 340;
    
    private int xPlating = 100;
    private int yPlating = 340;
    
    private int xHome = 200;
    private int yHome = 380;
	
	public CookGui(RanchoCook c, RestaurantGui g) {
		// TODO Auto-generated constructor stub
		this.agent=c;
		this.gui=g;
	}
	public CookGui(RanchoCook c) {
		// TODO Auto-generated constructor stub
		this.agent=c;
	}

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		if (xPos < xDestination)
            xPos=xPos+1;
        else if (xPos > xDestination)
            xPos=xPos-1;
        
        if (yPos < yDestination)
            yPos=yPos+1;
        else if (yPos > yDestination)
            yPos=yPos-1;
        
        if (xPos == xDestination && yPos == yDestination
                && (xDestination == xFrige) && (yDestination == yFrige)) {
            	agent.msgAtFrige();
            }
            else if(xPos == xDestination && yPos == yDestination
                    & (xDestination == xCooking) & (yDestination == yCooking)) {       
            	agent.msgAtCookingArea(); 
            }
            else if(xPos == xDestination&& yPos == yDestination
                    & (xDestination == xPlating) & (yDestination == yPlating)) {       
            	agent.msgAtPlatingArea(); 
            	for(Label l:labelList){
        			if(l.state==LabelState.moving){
        				l.state=LabelState.fixing;
        				return;
        			}
            	}
            }
            
        
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		g.drawString("Waiters' Home",5,130);
		g.drawString("Customers' Waiting Area", 5, 40);
		g.drawString("Cashier", 300, 50);
		
		int count=0;
		for(Label l:labelList){
			if(l.state==LabelState.moving){
				g.drawString("?"+l.choice,xPos+25,yPos+10);
			}
			else if(l.state==LabelState.fixing){
				g.drawString(l.choice,count*50,yPlating+10);
				count++;
			}
		}
		
		
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, 20, 20);
		
		g.setColor(Color.BLUE);
		g.drawString("Refridgerator",350,380);
		g.drawString("Plating Area",40,330);
		g.drawString("Cooking Area", 290, 330);
		g.drawRect(310, 335, 30, 30);
		g.drawString("Grill",310,355);
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return true;
	}

	public void moveToFrige() {
		// TODO Auto-generated method stub
		xDestination=xFrige;
		yDestination=yFrige;
		
	}

	public void moveToCookingArea() {
		// TODO Auto-generated method stub
		xDestination=xCooking;
		yDestination=yCooking;
	}

	public void moveToHome() {
		// TODO Auto-generated method stub
		xDestination=xHome;
		yDestination=yHome;
	}

	public void moveToPlatingArea(String choice,Waiter waiter) {
		// TODO Auto-generated method stub
		xDestination =xPlating;
		yDestination =yPlating;
		labelList.add(new Label(choice,waiter));
	}

	
	
	class Label{
		private String choice;
		private Waiter waiter;
		private LabelState state;
		
		Label(String s,Waiter w){
			this.choice=s;
			this.waiter=w;
			this.state=LabelState.moving;
		}
	}



	public void beingPickedUp(Waiter w) {
		// TODO Auto-generated method stub
		for(Label l:labelList){
			if(l.state==LabelState.fixing && l.waiter==w){
				l.state=LabelState.hiding;
				return;
			}
    	}
	}
	
	
	
}
