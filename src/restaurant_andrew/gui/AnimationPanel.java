package restaurant_andrew.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class AnimationPanel extends JPanel implements ActionListener {

	private static final int WINDOWX = 640;
    private static final int WINDOWY = 400;
    private static final int TABLEH = 50;
    private static final int TABLEW = 50;
    private static final int ORIGIN = 0;
    private static final int TIMERDELAY = 20;
    private Image bufferImage;
    private Dimension bufferSize;
    
    HashMap<Integer, Coord> tablePositions = new HashMap<Integer, Coord>();

    private List<Gui> guis = new ArrayList<Gui>();

    public AnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
 
        // populate map
        tablePositions.put(1, new Coord(2*WINDOWX/6, 10*WINDOWY/12));
        tablePositions.put(2, new Coord(3*WINDOWX/6, 9*WINDOWY/12));
        tablePositions.put(3, new Coord(4*WINDOWX/6, 8*WINDOWY/12));
        tablePositions.put(4, new Coord(5*WINDOWX/6, 7*WINDOWY/12));
        tablePositions.put(5, new Coord(300, 20)); // cook top row (orders in)
        tablePositions.put(6, new Coord(300, 70)); // cook top row (food out)
        
    	Timer timer = new Timer(TIMERDELAY, this );
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g) {
        Graphics2D t1 = (Graphics2D)g;
        Graphics2D t2 = (Graphics2D)g;
        Graphics2D t3 = (Graphics2D)g;
        Graphics2D t4 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        t1.setColor(getBackground());
        t1.fillRect(ORIGIN, ORIGIN, WINDOWX, WINDOWY);

        //Tables
        t1.setColor(Color.ORANGE);
        t1.fillRect(tablePositions.get(1).getX(), tablePositions.get(1).getY(), TABLEH, TABLEW);
        t2.setColor(Color.ORANGE);
        t2.fillRect(tablePositions.get(2).getX(), tablePositions.get(2).getY(), TABLEH, TABLEW);
        t3.setColor(Color.ORANGE);
        t3.fillRect(tablePositions.get(3).getX(), tablePositions.get(3).getY(), TABLEH, TABLEW);
        t4.setColor(Color.ORANGE);
        t4.fillRect(tablePositions.get(4).getX(), tablePositions.get(4).getY(), TABLEH, TABLEW);

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(t1);
                gui.draw(t2);
                gui.draw(t3);
                gui.draw(t4);
            }
        }
    }

    public void addGui(CustomerGui gui) {
        guis.add(gui);
    }

    public void addGui(WaiterGui gui) {
        guis.add(gui);
        gui.setMap(tablePositions);
    }

    public void addGui(CookGui gui) {
        guis.add(gui);
    }
}
