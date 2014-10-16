 package restaurant_rancho.gui;


import java.awt.*;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import SimCity.PersonGui;
import restaurant_rancho.RanchoCustomerRole;
import restaurant_rancho.RanchoWaiterRole;
import restaurant_rancho.interfaces.Waiter;

public class WaiterGui implements Gui {
    
    private Waiter agent = null;
    
    RestaurantGui gui;
    int position;
    private PersonGui personGui;
    private int xPos = 0, yPos = 0;//default waiter position
    private int xDestination = 10, yDestination = 140;//default start position
    
    private int xTable = 150;
    private int yTable = 200;
    private int xDoor = 30;
    private int yDoor = 70;
    private int xCook = 30;
    private int yCook = 330;
    private int xStand = 10;
    private int yStand;
    private int xCashier = 300;
    private int yCashier = 55;
    private int xManager = 200;
    private int yManager = 20;
    private int xExit = -50;
    private int yExit = -50;
    
    private String obamaFilename="/imgres-1.jpg";
	private Image obamaImage;
	private boolean isApplying=false;
	private boolean isOnBreak=false;
	private String food=null;
    
    
    public WaiterGui(Waiter agent, int waitingposition,PersonGui gui) {
    	
    	obamaImage=new ImageIcon(getClass().getResource(obamaFilename)).getImage();
        this.agent = agent;
        this.position=waitingposition;
        yStand = 140 + position*60;
        xDestination = xStand;
        yDestination = yStand;
        this.personGui=gui;
    }
    public WaiterGui(Waiter agent,PersonGui gui) 
    {	
    	obamaImage=new ImageIcon(getClass().getResource(obamaFilename)).getImage();
        this.agent = agent;
        this.personGui=gui;
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
            && (xDestination == xTable + 20) && (yDestination == yTable - 20)) {
        	xDestination++;
        	yDestination++;
        	agent.msgAtTable();
        }
        else if(xPos == getxDestination() && yPos == getyDestination()
                & (getxDestination() == xCook) & (getyDestination() == yCook)) {
        	xDestination=xStand;
        	yDestination=yStand;
        	agent.msgAtCook();
        	
        }
        else if(xPos == xDestination&& yPos == yDestination
                & (xDestination == xDoor) & (yDestination == yDoor)) { 
        	xDestination++;
        	yDestination++;
        	agent.msgAtDoor(); 
        }
        else if(xPos == xDestination && yPos==yDestination &&xDestination==xCashier &&yDestination==yCashier){
        	xDestination++;
        	yDestination++;
        	agent.msgAtCashier();
        }
        else if(xPos == xDestination && yPos == yDestination && xDestination == xManager && yDestination == yManager){
        	xDestination++;
        	yDestination++;
        	agent.msgAtManager();
        }
        else if(xPos == xDestination && yPos == yDestination && xDestination == xExit && yDestination == yExit){
        	xDestination++;
        	yDestination++;
        	personGui.backToResident(RanchoWaiterRole.class);
        }
    }
    
    public void draw(Graphics2D g) 
    {
    	g.setColor(Color.BLUE);
        g.fillRect(xPos, yPos, 20, 20);
    	g.drawImage(obamaImage, xPos, yPos,new JPanel());
    	if(food!=null){
    		g.drawString(food, xPos-10, yPos+55);
    	}
    }
    
    public boolean isPresent() {
        return true;
    }
    
    public void DoGoToTable() {
        setxDestination(xTable + 20);
        setyDestination(yTable - 20);
    }
    
    public void DoGoToCook() {
		
		setxDestination(xCook);
		setyDestination(yCook);
	}
    
    public void DoGoToDoor() {
        setxDestination(xDoor);
        setyDestination(yDoor);
    }
    
    
    public int getXPos() {
        return xPos;
    }
    
    public int getYPos() {
        return yPos;
    }

	public int getxDestination() {
		return xDestination;
	}

	public void setxDestination(int xDestination) {
		this.xDestination = xDestination;
	}

	public int getyDestination() {
		return yDestination;
	}

	public void setyDestination(int yDestination) {
		this.yDestination = yDestination;
	}

	public void setTablePosition(int tableNumber) {
		// TODO Auto-generated method stub
		xTable=(tableNumber-1)%2*100+150;
		yTable=(int)((tableNumber-1)/2)*100+150;
	}

	public void DoGoToStand() {
		// TODO Auto-generated method stub
		setxDestination(xStand);
		setyDestination(yStand);
	}

	public void applyBreak() {
		// TODO Auto-generated method stub
		isApplying=true;
		agent.wantToGoOnBreak();
	}
	
	public boolean isApplying(){
		return isApplying;
	}
	
	public boolean isOnBreak(){
		return isOnBreak;
	}

	public void setOnBreak(boolean b) {
		// TODO Auto-generated method stub
		isOnBreak=b;
		gui.updateInfoPanel(agent);
	}

	public void setApplying(boolean b) {
		// TODO Auto-generated method stub
		isApplying=b;
		gui.updateInfoPanel(agent);
	}

	public void comeBack() {
		// TODO Auto-generated method stub
		agent.comeBackToWork();
		setOnBreak(false);
	}

	public void DoGoToCashier() {
		// TODO Auto-generated method stub
		setxDestination(xCashier);
		setyDestination(yCashier);
	}

	public void sendGood(String f) {
		// TODO Auto-generated method stub
		food=f;
	}

	public void doneServing() {
		// TODO Auto-generated method stub
		food=null;
	}
	public void DoGoToManager() {
		// TODO Auto-generated method stub
		setxDestination(xManager);
		setyDestination(yManager);
		
	}
	public void exitRestaurant() {
		// TODO Auto-generated method stub
		setxDestination(xExit);
		setyDestination(yExit);
	}
	
	
}
