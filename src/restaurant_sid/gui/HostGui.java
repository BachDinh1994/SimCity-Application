package restaurant_sid.gui;

import restaurant_sid.SidHostAgent;

import java.awt.*;

public class HostGui implements Gui {

    private SidHostAgent agent = null;
    
    public static final int xTable = 100;
    public static final int yTable = 125;
    public static final int tableSize = 50;

    public HostGui(SidHostAgent agent) {
        this.agent = agent;
    }

    public void updatePosition() {
    }

    public void draw(Graphics2D g) {
        for (int i = 0; i < agent.getNumTables(); i++){
        	g.setColor(Color.ORANGE);
        	g.fillRect(xTable + (100*i), yTable, tableSize, tableSize);
        }
    }

    public boolean isPresent() {
        return true;
    }

}
