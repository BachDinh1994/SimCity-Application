package restaurant_rancho.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class AnimationPanel extends JPanel implements ActionListener {
    
    private final int WINDOWX = 450;
    private final int WINDOWY = 450;
    private final int c1=50,c2=150,c3=250,c4=350;
    private final int r1=50,r2=150,r3=250,r4=350;
    //private final int[] tablesX ={c1,c2,c3,c4,c1,c2,c3,c4,c1,c2,c3,c4,c1,c2,c3,c4};
    //private final int[] tablesY ={r1,r1,r1,r1,r2,r2,r2,r2,r3,r3,r3,r3,r4,r4,r4,r4};
    
    private final int[] tablesX={c2,c3,c2,c3};
    private final int[] tablesY={r2,r2,r3,r3};
    
    private final int side=50;
    private final int timerinter=20;
    private JButton addTableButton;
    private JButton removeTableButton;
    private Timer timer;
    private int buttonCount=1;
    
    
    private List<Gui> guis = new ArrayList<Gui>();
    
    public AnimationPanel() {
    	addTableButton=new JButton("Add a Table");
    	removeTableButton = new JButton("Remove a Table");
    	//addWaiterButton = new JButton("add a waiter");
    	//removeWaiterButton=new JButton("remove a Waiter");
    	removeTableButton.setEnabled(false);
    	//removeWaiterButton.setEnabled(false);
    	addTableButton.addActionListener(this);
    	removeTableButton.addActionListener(this);
    	//addWaiterButton.addActionListener(this);
    	//removeWaiterButton.addActionListener(this);
    	add(addTableButton);
    	add(removeTableButton);
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        //bufferSize = this.getSize();
        
    	timer = new Timer(timerinter, this );
    	timer.start();
    }
    
    public void startTimer(){
    	timer.start();
    }
    
    public void stopTimer(){
    	timer.stop();
    }
    
    
    
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==timer){
			repaint();  //Will have paintComponent called
		}
		else if(e.getSource()==addTableButton){
			buttonCount++;
			updateButtons();
			repaint();
		}
		else if(e.getSource()==removeTableButton){
			buttonCount--;
			updateButtons();
			repaint();
		}
	}
    
    private void updateButtons() {
		// TODO Auto-generated method stub
		if(buttonCount>1){
			removeTableButton.setEnabled(true);
		}
		else{
			removeTableButton.setEnabled(false);
		}
		if(buttonCount<tablesX.length){
			addTableButton.setEnabled(true);
		}
		else{
			addTableButton.setEnabled(false);
		}
	}

	public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        
        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );
        
        //Here is the table
        for(int i=0;i<buttonCount;i++){
        	g2.setColor(Color.ORANGE);
        	g2.fillRect(tablesX[i], tablesY[i], side, side);//200 and 250 need to be table params
        }
        
        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }
        
        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }
    }
    
	public void addGui(CustomerGui gui){
		guis.add(gui);
	}
	
    public void addGui(CookGui gui) {
        guis.add(gui);
    }
    
    public void addGui(WaiterGui gui) {
        guis.add(gui);
    }
    
    public void removeGui(WaiterGui gui){
    	guis.remove(gui);
    }
}
