package restaurant_rancho.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import SimCity.Clock;
import SimCity.PersonGui;
import restaurant_rancho.RanchoManager;

public class ManagerGui implements Gui{
	
	RanchoManager agent;
	Clock clock = null;
	int hour,min,sec;
	PersonGui personGui;
	int xPos = -50,yPos = -50;
	int xDestination =200,yDestination=20;
	boolean active =true;
	
	ManagerGui(RanchoManager agent,PersonGui gui){
		this.agent = agent;
		clock = agent.getPersonAgent().getGui().getAgent().getClock();
		this.personGui = gui;
	}

	
	public void updatePosition() {
		
		if (xPos < xDestination)
            xPos=xPos+1;
        else if (xPos > xDestination)
            xPos=xPos-1;
        
        if (yPos < yDestination)
            yPos=yPos+1;
        else if (yPos > yDestination)
            yPos=yPos-1;
        
		// TODO Auto-generated method stub
		hour = clock.getHour();
		min = clock.getMin();
		sec = clock.getSec();

		if (hour==6 && min==0 && active){
			active =false;
			agent.msgDayOff();
		}
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		g.setColor(Color.GREEN);
		g.drawString("Manager", xPos-10, yPos-10);
		g.setColor(Color.BLUE);
		g.fillRect(xPos,yPos , 40, 40);
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return true;
	}

}
