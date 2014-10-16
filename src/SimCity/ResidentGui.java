package SimCity;

import java.awt.Color;
import java.awt.Graphics2D;

import Role.ResidentRole;
import SimCity.interfaces.Resident;

public class ResidentGui extends LiveGui implements Gui{

	int hour,min,sec;
	boolean hungryActive = true;
	boolean sleepActive = true;
	boolean wakeActive = true;
	
	ResidentGui(PersonGui p, Resident person) {
		super(p, person);
		// TODO Auto-generated constructor stub
		xDestination = 100;
		yDestination = 100;
		xHome = 110;
		yHome = 140;
		xBed = 310;
		yBed = 40;
		xTable = 280;
		yTable =180;
		xKitchen = 200;
		yKitchen = 320;
		xFrige = 110;
		yFrige = 310;
		xCooking = 320;
		yCooking = 310;
	}

	
	
	
	
	
	
	
	
	public ResidentGui(ResidentRole resident) {
		super(resident);
		// TODO Auto-generated constructor stub
	}









	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
		hour = clock.getHour();
		min = clock.getMin();
		sec = clock.getSec();

		
		
		if(hour == 6 && min == 30 && hungryActive){
			hungryActive = false;
			agent.msgGotHungry();
		}
		if(hour == 6 && min == 35){
			hungryActive = true;
		}
		
		if(hour == 10 && min ==0 && sleepActive){
			sleepActive = false;
			agent.msgGoToSleep();
		}
		if(hour == 10 && min ==5){
			sleepActive = true;
		}
		
		if(hour == 2 && min ==0 && wakeActive){
			wakeActive = false;
			agent.backToHomePosition();
		}
		if(hour == 2 && min ==5){
			wakeActive = true;
		}
		
		
		
		
		
		
		
		if (xPos < xDestination)
			xPos += 2;
		else if (xPos > xDestination)
			xPos -= 2;

		if (yPos < yDestination)
			yPos += 2;
		else if (yPos > yDestination)
			yPos -= 2;
		if(xPos == xDestination && yPos == yDestination && xDestination ==-50 &&yDestination == -50){
			xPos = -60;
			yPos = -60;
			xDestination = -60;
			yDestination = -60;
			agent.msgAtDoor();
			personGui.goToRestaurant();
		}
		if(xPos == xDestination && yPos == yDestination && xDestination == xTable && yDestination == yTable){
			
			xDestination += 2;
			yDestination += 2;
			agent.msgAtTable();
		}
		if(xPos == xDestination && yPos == yDestination && xDestination == xFrige && yDestination == yFrige){
			
			xDestination += 2;
			yDestination += 2;
			agent.msgAtFrige();
		}
		if(xPos == xDestination && yPos == yDestination && xDestination == xCooking && yDestination == yCooking){
			
			xDestination += 2;
			yDestination += 2;
			agent.msgAtCooking();
		}
		if(xPos == xDestination && yPos == yDestination && xDestination == xKitchen && yDestination == yKitchen){
			
			xDestination += 2;
			yDestination += 2;
			agent.msgAtKitchen();
		}
		if(xPos == xDestination && yPos == yDestination && xDestination == xBed && yDestination == yBed){

			xDestination += 2;
			yDestination += 2;
			agent.msgAtBed();
		}
		if(xPos == xDestination && yPos == yDestination && xDestination == xHome && yDestination == yHome){
	
			xDestination += 2;
			yDestination += 2;
			agent.msgAtHomePos();
		}
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, width, height);
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return true;
	}

	public void goToBed() {
		// TODO Auto-generated method stub
		xDestination = xBed;
		yDestination =yBed;
	}
	
	public void goToKitchen(){
		xDestination = xKitchen;
		yDestination = yKitchen;
	}
	
	public void goToHomePosition(){
		xDestination = xHome;
		yDestination = yHome;
	}

	public void goToDoor() {
		// TODO Auto-generated method stub
		xDestination = 0;
		yDestination = 0;
	}

	public void goToFrige() {
		// TODO Auto-generated method stub
		xDestination =xFrige;
		yDestination =yFrige;
		
	}

	public void goToCooking() {
		// TODO Auto-generated method stub
		xDestination = xCooking;
		yDestination = yCooking;
	}

	public void goToTable() {
		// TODO Auto-generated method stub
		xDestination =xTable;
		yDestination =yTable;
	}

	public void exitHome() {
		// TODO Auto-generated method stub
		xDestination = -50;
		yDestination = -50;
	}


}
