package restaurant.gui;

import restaurant.CustomerAgent;
import restaurant.WaiterAgent;
import restaurant.interfaces.Customers;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
@SuppressWarnings("serial")
public class RestaurantGui extends JFrame implements ActionListener 
{
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	static final int rows=1,cols=2,hgap=10,vgap=10;
	JInternalFrame animationFrame = new JInternalFrame("Restaurant Animation");
	AnimationPanel animationPanel = new AnimationPanel();
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
    private RestaurantPanel restPanel = new RestaurantPanel(this);
    JScrollPane scrollPane = new JScrollPane(restPanel); 
    
    /* infoPanel holds information about the clicked customer, if there is one*/
    private JPanel infoPanel;
    private JLabel infoLabel; //part of infoPanel
    JCheckBox stateCB;//part of infoLabel
    public JCheckBox onBreak;
    JButton stop = new JButton("Stop");
    JButton resume = new JButton("Resume");
    Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RestaurantGui() 
    {
        int WINDOWX = 1400;
        int WINDOWY = 700;
        resume.setVisible(false);
        stop.addActionListener(this);
        resume.addActionListener(this);
        
    	animationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        animationFrame.setBounds(100+WINDOWX/2, 50 , WINDOWX/2+100, WINDOWY/2+100);
        animationFrame.add(stop,BorderLayout.NORTH);
        animationFrame.add(resume,BorderLayout.SOUTH);
        animationFrame.setVisible(true);
    	animationFrame.add(animationPanel); 
    	setBounds(50, 50, WINDOWX, WINDOWY);
        setLayout(new BoxLayout((Container) getContentPane(), 
        		BoxLayout.Y_AXIS));
      
        Dimension restDim = new Dimension(WINDOWX, (int) (WINDOWY * .6));
        restPanel.setPreferredSize(restDim);
        restPanel.setMinimumSize(restDim);
        restPanel.setMaximumSize(restDim);
        setLayout(new BorderLayout(1,1));
        add(scrollPane, BorderLayout.SOUTH);
        
        // Now, setup the info panel
        Dimension infoDim = new Dimension(WINDOWX, (int) (WINDOWY * 0.1));
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(infoDim);
        infoPanel.setMinimumSize(infoDim);
        infoPanel.setMaximumSize(infoDim);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));

        stateCB = new JCheckBox();
        onBreak = new JCheckBox("Go On Break?");
        stateCB.setVisible(false);
        onBreak.setVisible(false);
        stateCB.addActionListener(this);
        onBreak.addActionListener(this);

        infoLabel = new JLabel(); 
        infoLabel.setText("<html><pre><i>Click Add to make customers</i></pre></html>");
        infoPanel.add(infoLabel);
        infoPanel.add(stateCB);
        infoPanel.add(onBreak);
        
        add(infoPanel);
        add(restPanel.field,BorderLayout.NORTH);
        restPanel.add(animationFrame);
    }
    /**
     * updateInfoPanel() takes the given customer (or, for v3, Host) object and
     * changes the information panel to hold that person's info.
     *
     * @param person customer (or waiter) object
     */
    public void updateInfoPanel(Object person) 
    {
        currentPerson = person;

        if (person instanceof CustomerAgent) 
        {
            stateCB.setVisible(true);
            onBreak.setVisible(false);
            CustomerAgent customer = (CustomerAgent) person;
            stateCB.setText("Hungry?");
            if (restPanel.checking.isSelected() && customer.getInit() == 0) //Only allow customers to be set hungry during customer creation 
            {
                customer.getGui().setHungry();
                customer.incInit();
                stateCB.setSelected(true);
                stateCB.setEnabled(false);
            }
            else
            {
                stateCB.setSelected(customer.getGui().isHungry());
                stateCB.setEnabled(!customer.getGui().isHungry());
            }
            infoLabel.setText(
               "<html><pre>     Name: " + customer.getName() + " </pre></html>");
        }
        else if (person instanceof WaiterAgent) 
        {
        	stateCB.setVisible(false);
            onBreak.setVisible(true);
            WaiterAgent waiter = (WaiterAgent) person;
            if (waiter.getGui().isOnBreak())
            {
            	onBreak.setText("Back To Work?");
            }
            onBreak.setSelected(waiter.getGui().willBeOnBreak());
            onBreak.setEnabled(!waiter.getGui().willBeOnBreak());
            infoLabel.setText(
               "<html><pre>     Name: " + waiter.getName() + " </pre></html>");
        }
        infoPanel.validate();
    }
    /**
     * Action listener methods for checkbox and buttons
     */
    public void actionPerformed(ActionEvent e) 
    {
    	if (e.getSource() == stateCB) 
        {
            if (currentPerson instanceof CustomerAgent) 
            {
                CustomerAgent c = (CustomerAgent) currentPerson;
                c.incInit(); //If the person's been set to hungry, the initialized checkbox is rendered useless.
                c.getGui().setHungry();
                stateCB.setEnabled(false);
            }
        }
    	else if (e.getSource() == onBreak) //Scenario 3 - The Waiter Can Go on Break
        { 
    		if (currentPerson instanceof WaiterAgent) 
    		{
                WaiterAgent w = (WaiterAgent) currentPerson;
                if (w.getGui().isOnBreak())
                {
                	w.backToWork();
                }
                else
                {
                    w.getGui().setBreak();
                    w.getGui().setReadyBreak();
                    onBreak.setEnabled(false);
                }
    		}
        }
        else if (e.getSource() == stop)
        {
        	stop.setVisible(false);
        	resume.setVisible(true);
        	restPanel.host.msgPleaseStop();
        	restPanel.cook.msgPleaseStop();
        	if (restPanel.customers.size() > 0)
        	{
        		for (int i=0;i<restPanel.customers.size();i++)
        		{
            		restPanel.customers.get(i).msgPleaseStop();
        		}
        	}
        	if (restPanel.waiters.size() > 0)
        	{
        		for (int i=0;i<restPanel.waiters.size();i++)
        		{
            		restPanel.waiters.get(i).msgPleaseStop();
        		}
        	}
        }
        else if (e.getSource() == resume)
        {
        	stop.setVisible(true);
        	resume.setVisible(false);
        	restPanel.host.getThread().restart();
        	restPanel.cook.getThread().restart();
        	if (restPanel.customers.size() > 0)
        	{
        		for (int i=0;i<restPanel.customers.size();i++)
        		{
                	restPanel.customers.get(i).getThread().restart();
        		}
        	}
        	if (restPanel.waiters.size() > 0)
        	{
        		for (int i=0;i<restPanel.waiters.size();i++)
        		{
            		restPanel.waiters.get(i).getThread().restart();
        		}
        	}
        }
    }
    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
    public void setCustomerEnabled(Customers c) 
    {
        if (currentPerson instanceof Customers) 
        {
            Customers cust = (Customers) currentPerson;
            if (c.equals(cust)) 
            {
                stateCB.setEnabled(true);
                stateCB.setSelected(false);
            }
        }
    }
    public void setWaiterEnabled(WaiterAgent waiter) 
    {
        if (currentPerson instanceof WaiterAgent) 
        {
            WaiterAgent w = (WaiterAgent) currentPerson;
            if (waiter.equals(w)) 
            {
                onBreak.setEnabled(true);
                onBreak.setSelected(false);
            }
        }
    }
    /**
     * Main routine to get gui started
     */
    public static void main(String[] args) 
    {
        RestaurantGui gui = new RestaurantGui();
        gui.pack();
        gui.setTitle("csci201 Restaurant");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}