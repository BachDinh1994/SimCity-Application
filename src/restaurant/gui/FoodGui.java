package restaurant.gui;

import java.awt.*;

import SimCity.Gui;

public class FoodGui implements Gui
{
	static final int width = 20, height = 20;
    int[] table = new int[3];
    private String choice;
    private int speed=1;
    private int tablenum;
	private int xPos=100, yPos=400;
	private int xDestination=100,yDestination=400;
	public static final int yTable = 150;

	public FoodGui()
	{ 
		choice = " ";
        for (int i=0;i<3;i++)
        {
        	table[i] =(i+1)*100; 
        }
	}
	public void updatePosition() //food only moves vertically to allow simplicity of animation
	{
        if (xPos < xDestination)
            xPos+=speed;
        else if (xPos > xDestination)
            xPos-=speed;
		if (yPos < yDestination)
			yPos+=speed;
		else if (yPos > yDestination)
			yPos-=speed;
	}
	public void draw(Graphics2D g) 
	{
		g.setColor(Color.BLUE);
		g.drawString(choice,xPos,yPos+40);
		g.setColor(Color.RED);
		g.fillRect(xPos, yPos, width, height);
	}
	public boolean isPresent() 
	{
		return true;
	}
	public void MoveToPlatingArea()
	{
		//yPos = yTable+100;
		yDestination = yTable+100;
	}
	public void MoveToTable() //lets waiter carry food to table. Make sure the substring operation is correct
	{
		if (choice.charAt(choice.length()-1) == '?')
		{
			choice = choice.substring(0, choice.length()-1);
		}
		yDestination = yTable+20;
	}
	public void showChoice() //show the food order state. Make sure the append operation is correct
	{
		if (choice.charAt(choice.length()-1) != '?')
		{
			choice = choice+"?";
		}
		yPos = yTable-20;
		xDestination = table[tablenum-1];
		yDestination = yTable+150;
	}
	public void MoveOut() //move the food once the customer's done
	{
		yPos=yTable+250;
		yDestination = yTable+250;
	}
	public void MoveIn() //move the food once the customer's done
	{
		yPos=yTable+150;
		yDestination = yTable+150;
	}
	public void setInfo(String choice,int tablenum)
	{
		this.choice = choice;
		this.tablenum = tablenum;
		xPos = table[tablenum-1];
	}
	public void FridgeToPosition()
	{
		xPos = 380;
		yPos = 300;
		xDestination = table[tablenum-1];
		yDestination = 300;
	}
	public void setSpeed(int speed)
	{
		this.speed = speed;
	}
}