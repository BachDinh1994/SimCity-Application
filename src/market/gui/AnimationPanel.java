package market.gui;

import javax.swing.*;

import SimCity.Gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class AnimationPanel extends JPanel implements ActionListener 
{
    private final int WINDOWX = 450;
    private final int WINDOWY = 350;
    static final int x = 100;static final int x2 = 200;static final int x3 = 300;
    static final int y = 150;
    static final int width = 50;
    static final int height = 50;
    @SuppressWarnings("unused")
	private Image bufferImage;
    @SuppressWarnings("unused")
	private Dimension bufferSize;
    private List<Gui> guis = new ArrayList<Gui>();

    public AnimationPanel() 
    {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        bufferSize = this.getSize();
    	Timer timer = new Timer(20, this );
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) 
	{
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g) 
    {
        Graphics2D g2 = (Graphics2D)g;
        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );
        
        //Here are the GUI components
        g2.setColor(Color.GRAY);
        g2.fillRect(380, 270, 30, 60);
        g2.setColor(Color.RED);
        g2.drawString("Fridge",373,265);
        g2.setColor(Color.ORANGE);
        g2.fillRect(x, y, width, height);
        g2.setColor(Color.ORANGE);
        g2.fillRect(x, y, width, height);
        g2.fillRect(x2, y, width, height);
        g2.fillRect(x3, y, width, height);
        g2.setColor(Color.RED);
        g2.drawString("Waiter Home Position",150,35);
        g2.drawLine(30, 20, 450, 20);
        g2.setColor(Color.BLUE);
        g2.drawString("Waiting Area",35,343);
        g2.drawLine(30, 20, 30, 350);
        g2.setColor(Color.PINK);
        g2.fillRect(100, 250, 250, 30);
        g2.setColor(Color.RED);
        g2.drawString("Plating Area",190,268);
        g2.setColor(Color.RED);
        g2.fillRect(100, 300, 250, 30);
        g2.setColor(Color.darkGray);
        g2.fillRect(100, 300, 30, 30);
        g2.fillRect(200, 300, 30, 30);
        g2.fillRect(300, 300, 30, 30);
        g2.setColor(Color.WHITE);
        g2.drawString("Cooking Area",185,320);
        

        for(Gui gui : guis) 
        {
            if (gui.isPresent()) 
            {
                gui.updatePosition();
            }
        }

        for(Gui gui : guis) 
        {
            if (gui.isPresent()) 
            {
                gui.draw(g2);
            }
        }
    }
    public void addGui(Gui gui) 
    {
        guis.add(gui);
    }
}
