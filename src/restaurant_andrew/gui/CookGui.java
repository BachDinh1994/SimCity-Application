package restaurant_andrew.gui;

import restaurant_andrew.AndrewCookAgent;
import restaurant_andrew.AndrewCustomerRole;

import java.awt.*;
import java.util.*;
import java.util.concurrent.Semaphore;


public class CookGui implements Gui {

	Semaphore cAtCook = new Semaphore(0, true);
	
    private AndrewCookAgent agent = null;

    private int xPos = 320, yPos = 20;

    private static final int SIZE = 30;
    private static final int H_OFFSET = 15;
    private static final int V_OFFSET = 50;
    
    HashMap<Integer, String> cooking = new HashMap<Integer, String>();
    HashMap<Integer, String> plated = new HashMap<Integer, String>();

    public CookGui(AndrewCookAgent agent) {
        this.agent = agent;
        
        cooking.put(1, null);
        cooking.put(2, null);
        cooking.put(3, null);
        cooking.put(4, null);
        plated.put(1, null);
        plated.put(2, null);
        plated.put(3, null);
        plated.put(4, null);
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.CYAN);
        
        g.fillRect(xPos, yPos, SIZE, SIZE);
        if (cooking.get(1) != null) g.drawString(cooking.get(1), xPos, yPos);
        
        g.fillRect(xPos + SIZE + H_OFFSET, yPos, SIZE, SIZE);
        if (cooking.get(2) != null) g.drawString(cooking.get(2), xPos + SIZE + H_OFFSET, yPos);
        
        g.fillRect(xPos + 2*SIZE + 2*H_OFFSET, yPos, SIZE, SIZE);
        if (cooking.get(3) != null) g.drawString(cooking.get(3),xPos + 2*SIZE + 2*H_OFFSET, yPos);
        
        g.fillRect(xPos + 3*SIZE + 3*H_OFFSET, yPos, SIZE, SIZE);
        if (cooking.get(4) != null) g.drawString(cooking.get(4), xPos + 3*SIZE + 3*H_OFFSET, yPos);
        
        g.fillRect(xPos, yPos + V_OFFSET, SIZE, SIZE);
        if (plated.get(1) != null) g.drawString(plated.get(1), xPos, yPos + V_OFFSET);
        
        g.fillRect(xPos + SIZE + H_OFFSET, yPos + V_OFFSET, SIZE, SIZE);
        if (plated.get(2) != null) g.drawString(plated.get(2), xPos + SIZE + H_OFFSET, yPos + V_OFFSET);
        
        g.fillRect(xPos + 2*SIZE + 2*H_OFFSET, yPos + V_OFFSET, SIZE, SIZE);
        if (plated.get(3) != null) g.drawString(plated.get(3), xPos + 2*SIZE + 2*H_OFFSET, yPos + V_OFFSET);
        
        g.fillRect(xPos + 3*SIZE + 3*H_OFFSET, yPos + V_OFFSET, SIZE, SIZE);
        if (plated.get(4) != null) g.drawString(plated.get(4), xPos + 3*SIZE + 3*H_OFFSET, yPos + V_OFFSET);
    }

    public boolean isPresent() {
        return true;
    }

	@Override
	public void updatePosition() {
		//unused (doesn't change position)
	}
	
	public void DoPutOnGrill(String choice) {
		for (int i = 1; i < 5; i++) {
			if (cooking.get(i) == null) cooking.put(i, choice);
			break;
		}
	}
	public void DoPlate(String choice) {
		for (int i = 1; i < 5; i++) {
			if (cooking.get(i) == choice) {
				cooking.put(i, null);
				break;
			}
		}
		for (int i = 1; i < 5; i++) {
			if (plated.get(i) == null) {
				plated.put(i, choice);
				break;
			}
		}
	}
	public void DoSend(String choice) {
		for (int i = 1; i < 5; i++) {
			if (plated.get(i) == choice) {
				plated.put(i, null);
				break;
			}
		}
	}

}
