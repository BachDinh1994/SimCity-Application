package restaurant_andrew.gui;

import restaurant_andrew.AndrewCustomerRole;
import restaurant_andrew.AndrewWaiterRole;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class RestaurantGui extends JFrame implements ActionListener {
	AnimationPanel animationPanel = new AnimationPanel();
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
    private RestaurantPanel restPanel = new RestaurantPanel(this);
    
    /* infoPanel holds information about the clicked customer, if there is one*/
    private JPanel infoPanel;
    private JLabel infoLabel; //part of infoPanel
    private JCheckBox stateCB;//part of infoLabel
    
    private JPanel namePanel;
    private JLabel nameLabel;
    
    private JButton pauseButton;
    boolean paused = false;

    private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RestaurantGui() {
        int WINDOWX = 1280;
        int WINDOWY = 800;
    	
    	setBounds(/*x*/ 100, /*y*/ 100, /*w*/ WINDOWX, /*h*/ WINDOWY);

        setLayout(new GridLayout(/*rows*/ 2, /*cols*/ 2));
        
        add(animationPanel);
        
        Dimension restDim = new Dimension((int) ((WINDOWX) * .25), (int) (WINDOWY));
        restPanel.setPreferredSize(restDim);
        restPanel.setMinimumSize(restDim);
        restPanel.setMaximumSize(restDim);
        add(restPanel);

        pauseButton = new JButton("Pause");
        pauseButton.setVerticalTextPosition(AbstractButton.CENTER);
        pauseButton.setHorizontalTextPosition(AbstractButton.CENTER);
        pauseButton.addActionListener(this);
        
        // Now, setup the info panel
        Dimension infoDim = new Dimension((int) ((WINDOWX) * .5), (int) (WINDOWY * .25));
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(infoDim);
        infoPanel.setMinimumSize(infoDim);
        infoPanel.setMaximumSize(infoDim);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));

        stateCB = new JCheckBox();
        stateCB.setVisible(false);
        stateCB.addActionListener(this);

        infoPanel.setLayout(new GridLayout(/*rows*/ 1, /*cols*/ 0, /*hgap*/ 30, /*vgap*/ 0));
        
        infoLabel = new JLabel(); 
        infoLabel.setText("<html><pre><i>Click Add to make waiters and customers</i></pre></html>");
        infoPanel.add(pauseButton);
        infoPanel.add(infoLabel);
        infoPanel.add(stateCB);
        add(infoPanel);
        
        namePanel = new JPanel();
        namePanel.setPreferredSize(infoDim);
        namePanel.setMinimumSize(infoDim);
        namePanel.setMaximumSize(infoDim);
        namePanel.setBorder(BorderFactory.createTitledBorder("Hi"));
        nameLabel = new JLabel();
        nameLabel.setText("Modified by Andrew Lee");

        ImageIcon pic = new ImageIcon("body.png");
        JLabel picLabel = new JLabel(pic);
        
        namePanel.add(nameLabel);
        //namePanel.add(picLabel);
        add(namePanel);
    }
    /**
     * updateInfoPanel() takes the given customer (or, for v3, Host) object and
     * changes the information panel to hold that person's info.
     *
     * @param person customer (or waiter) object
     */
    public void updateInfoPanel(Object person) {
        stateCB.setVisible(true);
        currentPerson = person;

        if (person instanceof AndrewCustomerRole) {
            AndrewCustomerRole customer = (AndrewCustomerRole) person;
            stateCB.setText("Hungry?");
          //Should checkmark be there? 
            stateCB.setSelected(customer.getGui().isHungry());
          //Is customer hungry? Hack. Should ask customerGui
            stateCB.setEnabled(!customer.getGui().isHungry());
          // Hack. Should ask customerGui
            infoLabel.setText(
               "<html><pre>     Name: " + customer.getName() + " </pre></html>");
        }
        if (person instanceof AndrewWaiterRole) {
        	AndrewWaiterRole waiter = (AndrewWaiterRole) person;
            stateCB.setText("Break?");
            stateCB.setSelected(waiter.getGui().onBreak() || waiter.getGui().waitingForBreakApproval());
            stateCB.setEnabled(!waiter.getGui().waitingForBreakApproval());
            infoLabel.setText(
               "<html><pre>     Name: " + waiter.getName() + " </pre></html>");
        }
        infoPanel.validate();
    }
    /**
     * Action listener method that reacts to the checkbox being clicked;
     * If it's the customer's checkbox, it will make him hungry
     * For v3, it will propose a break for the waiter.
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == stateCB) {
            if (currentPerson instanceof AndrewCustomerRole) {
                AndrewCustomerRole c = (AndrewCustomerRole) currentPerson;
                c.getGui().setHungry();
                stateCB.setEnabled(false);
            }
            if (currentPerson instanceof AndrewWaiterRole) {
            	AndrewWaiterRole w = (AndrewWaiterRole) currentPerson;
                if (w.getGui().onBreak()) w.msgBreakDone();
                else w.getGui().wantBreak();
                stateCB.setEnabled(false);
            }
        }
    }
    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
    public void setCustomerEnabled(AndrewCustomerRole c) {
        if (currentPerson instanceof AndrewCustomerRole) {
            AndrewCustomerRole cust = (AndrewCustomerRole) currentPerson;
            if (c.equals(cust)) {
                stateCB.setEnabled(true);
                stateCB.setSelected(false);
            }
        }
    }
    /**
     * Main routine to get gui started
     */
    public static void main(String[] args) {
        RestaurantGui gui = new RestaurantGui();
        gui.setTitle("csci201 Restaurant");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
