package SimCity;

import java.awt.Color;
import java.awt.Graphics2D;

import Role.Landlord;

import SimCity.interfaces.Resident;

public class LiveGui {
	
	protected Resident agent;
	protected PersonGui personGui;
	static final int width = 20, height = 20;
	public int xPos, yPos;
	public Clock clock;
	
	int xDestination;
	int yDestination;
	int xHome;
	int yHome;
	int xBed;
	int yBed;
	int xTable;
	int yTable;
	int xKitchen;
	int yKitchen;
	int xFrige;
	int yFrige;
	int xCooking;
	int yCooking;
	int xLord = 55;
	int yLord = 70;
	
	LiveGui(PersonGui p,Resident person){
		personGui = p;
		this.agent = person;
		clock = personGui.getAgent().getClock();
	}
	public LiveGui(Resident res){
		this.agent = res;
	}
	
	
	public void goToBed() {
		// TODO Auto-generated method stub
		xDestination = xBed;
		yDestination =yBed;
	}
	
	public void goToLandlord(){
		xDestination = xLord;
		yDestination = yLord;
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