package amyRestaurant.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;

import Role.CustomerRole;
import SimCity.PersonGui;
import amyRestaurant.AmyCustomerRole;
import amyRestaurant.gui.AmyWaiterGui.placeType;




public class AmyCustomerGui implements Gui{

	private AmyCustomerRole agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;
	PersonGui personGui;

	//private HostAgent host;
	RestaurantGui gui;

	private int xPos, yPos, cSize, yCord = 100;
	private int offCord = -20;
	private int xDestination, yDestination, xHome, yHome;
	private enum Command {noCommand, GoToWaiting, GoToSeat, GoToCashier, GoToCook,LeaveRestaurant};
	private Command command=Command.noCommand;
	private enum FoodAnimation{none,waiting,thinking,steak, chicken,salad, pizza, paying};
	private FoodAnimation foodAnimate = FoodAnimation.none;

	Image img;

	public AmyCustomerGui(AmyCustomerRole c, RestaurantGui gui){ //HostAgent m) {
		agent = c;
		xPos = -40;
		yPos = -40;
		xDestination = -40;
		yDestination = -40;
		cSize= 20; 
		this.gui = gui;
	}
	public AmyCustomerGui(AmyCustomerRole c, PersonGui p){ //HostAgent m) {
		personGui = p;
		agent = c;
		xPos = 0;
		yPos = 0;
		xDestination = 10;
		yDestination = 10;
		cSize= 20; 
	}
	
	public void setDestination(int x, int y){
		xDestination = x;
		yDestination = y;
		xHome = x;
		yHome = y;
		command = Command.GoToWaiting;
	}
	
	public void goToWaiting(){
		xDestination = xHome;
		yDestination = yHome;
		command = Command.GoToWaiting;	
	}
	
	public int getX(){
		return xDestination;
		
	}
	public int getY(){
		return yDestination;
		
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

		if (xPos == xDestination && yPos == yDestination) {
			if (command==Command.GoToSeat) agent.msgAnimationFinishedGoToSeat();
			else if (command==Command.LeaveRestaurant) {
				agent.msgAnimationFinishedLeaveRestaurant();
				//System.out.println("about to call gui.setCustomerEnabled(agent);");
				isHungry = false;
				//gui.setCustomerEnabled(agent);
			}
			else if (command == Command.GoToCashier){
				agent.msgAtCashier();
				System.out.println(agent.getName() + " at cashier");
			}
			else if (command == Command.GoToCook){
				agent.msgAtCook();
				System.out.println(agent.getName() + " goes cleaning.");
			}
			else if (command == Command.GoToWaiting){
				agent.imInRestaurant();
				System.out.println(agent.getName() + " goes to restaurant.");
			}
			
			command=Command.noCommand;
		}
		if (xPos == -20 && yPos == -20)
		{
			xPos = -25;
			yPos = -25;
	    	xDestination = -25;
	    	yDestination = -25;
			personGui.backToResident(AmyCustomerRole.class);
		}
	}

	public void draw(Graphics2D g) {

		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, cSize, cSize);


		g.setColor(Color.BLACK);
		g.setFont(new Font(null, Font.PLAIN, 20));
		if(foodAnimate == FoodAnimation.thinking)
			g.drawString("?", xPos,yPos+10); // 6 is for the off ypos to be on top of the customer
		else if(foodAnimate == FoodAnimation.chicken)
			g.drawString("Ch", xPos,yPos+10);
		else if(foodAnimate == FoodAnimation.pizza)
			g.drawString("Pi", xPos,yPos+10);
		else if(foodAnimate == FoodAnimation.salad)
			g.drawString("Sa", xPos,yPos+10);
		else if(foodAnimate == FoodAnimation.steak)
			g.drawString("St", xPos,yPos+10);
		else if(foodAnimate == FoodAnimation.waiting)
			g.drawString("...", xPos,yPos+10);
		else if(foodAnimate == FoodAnimation.paying)
			g.drawString("$", xPos,yPos+10);
		else
		{}

	}

	public void createThinkingFood(){

		foodAnimate = FoodAnimation.thinking;

	}
	public void waitingMyFood(){

		foodAnimate = FoodAnimation.waiting;

	}
	
	public void createPaying(){
		foodAnimate = FoodAnimation.paying;

	}
	
	

	public void createMyFood(String myFood)
	{
		if (myFood == "Steak")
			foodAnimate = FoodAnimation.steak;
		else if(myFood == "Chicken")
			foodAnimate = FoodAnimation.chicken;
		else if(myFood == "Salad")
			foodAnimate = FoodAnimation.salad;
		else if(myFood == "Pizza")
			foodAnimate = FoodAnimation.pizza;
		else
			foodAnimate = FoodAnimation.none;

	}

	public boolean isPresent() {
		return isPresent;
	}
	public void setHungry() {
		isHungry = true;
		agent.gotHungry();
		setPresent(true);
	}
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}

	public void goTo(int x, int y) {//later you will map seatnumber to table coordinates.

		xDestination = x;
		yDestination = y;

		command = Command.GoToSeat;
	}

	public void DoGoToCook(){

		xDestination = -20;
		yDestination = 200;
		command = Command.GoToCook;
		System.out.println("going to cook");

	}


	public void goToCashier()
	{
		createPaying();
		command  = Command.GoToCashier;
		System.out.println("going to cashier");
		xDestination = 500;
		yDestination = 30;
	}
	public void DoExitRestaurant() {
		xDestination = offCord;
		yDestination = offCord;
		command = Command.LeaveRestaurant;
	}
}
