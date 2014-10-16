package restaurant_rancho.gui;

import restaurant_rancho.RanchoCustomerRole;
import restaurant_rancho.RanchoWaiterRole;
import restaurant_rancho.interfaces.Customer;
import restaurant_rancho.interfaces.Waiter;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.*;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class RestaurantGui extends JFrame implements ActionListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	//JFrame animationFrame = new JFrame("Restaurant Animation");
	AnimationPanel animationPanel = new AnimationPanel();
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
    private RestaurantPanel restPanel = new RestaurantPanel(this);
    
    //add a control panel
    private JPanel leftPanel= new JPanel();
    
    /* infoPanel holds information about the clicked customer, if there is one*/
    private JPanel infoPanel;
    
    /* add a self introduction panel*/
    private JPanel introPanel;
    
    private JLabel introLabel; //part of introPanel
    //private JLabel infoLabel; //part of infoPanel
    private JCheckBox customerCB;//part of infoLabel
    
    /*private Object currentPerson;*//* Holds the agent that the info is about.
                                  Seems like a hack */
    private RanchoCustomerRole currentCustomer;
    private Waiter currentWaiter;
    private JButton pauseButton;
    private JLabel customerInfoLabel;
    private JPanel customerInfoPanel;
    private JPanel waiterInfoPanel;
    private JLabel waiterInfoLabel;
    private JCheckBox applyBreakCB;
    private JCheckBox offBreakCB;
    private JCheckBox statusCB;
    
    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RestaurantGui() {
        int WINDOWX = 500;
        int WINDOWY = 450;
        add(leftPanel);
    	add(animationPanel);
    	setBounds(50, 50, WINDOWX, WINDOWY);
        
    	//fixed size of non=animation panel
    	Dimension controlDim=new Dimension(WINDOWX,WINDOWY);
    	setLayout(new GridLayout(1,2));
    	leftPanel.setLayout(new FlowLayout(FlowLayout.CENTER,5,0));
    	leftPanel.setPreferredSize(controlDim);
    	leftPanel.setMinimumSize(controlDim);
    	leftPanel.setMaximumSize(controlDim);
    	
    	//fixed size of rest panel
        Dimension restDim = new Dimension(WINDOWX, (int) (WINDOWY * .5));
        restPanel.setPreferredSize(restDim);
        restPanel.setMinimumSize(restDim);
        restPanel.setMaximumSize(restDim);
        
        
        
        //lab3
        pauseButton = new JButton("Pause");
        pauseButton.addActionListener(this);
        
        
     // Now, setup the info panel
        Dimension infoDim = new Dimension(WINDOWX, (int) (WINDOWY * .25));
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(infoDim);
        infoPanel.setMinimumSize(infoDim);
        infoPanel.setMaximumSize(infoDim);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));
        
        
        
        //customer check box
        customerCB=new JCheckBox("Hungry?",false);
        customerCB.setEnabled(false);
        customerCB.addActionListener(this);
       
        //apply break check box
        applyBreakCB = new JCheckBox("Go Break");
        applyBreakCB.setEnabled(false);
        applyBreakCB.addActionListener(this);
        
        
        //off break check box
        offBreakCB = new JCheckBox("Come Back");
        offBreakCB.setEnabled(false);
        offBreakCB.addActionListener(this);
        
        //waiter status check box
        statusCB = new JCheckBox("On Break?");
        statusCB.setEnabled(false);
    
        
        //info Labels
        customerInfoLabel = new JLabel();
        waiterInfoLabel = new JLabel();
        
        
        //customer & waiter info Panels
        customerInfoPanel = new JPanel();
        customerInfoPanel.setLayout(new GridLayout(1, 3, 20, 0));
        customerInfoPanel.add(customerInfoLabel);
        customerInfoPanel.add(customerCB);
        customerInfoPanel.add(pauseButton);
        waiterInfoPanel = new JPanel();
        waiterInfoPanel.setLayout(new GridLayout(1,2,20,0));
        waiterInfoPanel.add(waiterInfoLabel);
        waiterInfoPanel.add(applyBreakCB);
        waiterInfoPanel.add(offBreakCB);
        waiterInfoPanel.add(statusCB);
        


        //overall info Panels
        infoPanel.setLayout(new GridLayout(2,1,0,0));
        infoPanel.add(customerInfoPanel);
        infoPanel.add(waiterInfoPanel);
        
        
        //Now, setup the intro panel
        Dimension introDim=new Dimension(WINDOWX,(int)(WINDOWY * .25));
        introPanel = new JPanel();
        introPanel.setPreferredSize(introDim);
        introPanel.setMinimumSize(introDim);
        introPanel.setMaximumSize(introDim);
        introPanel.setBorder(BorderFactory.createTitledBorder("Author Introduction"));
        introPanel.setLayout(new FlowLayout());
        introLabel=new JLabel();
        introLabel.setText("<html><pre><i>Developer's name : Shu Zhou</i></pre></html>");
        introPanel.add(introLabel);
        
        //add an image icon
        String filename = "/imgres.jpg";
        ImageIcon imageIcon = new ImageIcon(new ImageIcon(getClass().getResource(filename)).getImage());
        JLabel imageLabel=new JLabel(imageIcon);
        introPanel.add(imageLabel);
        
        //Add the three sub-panel of non-animation panel
        leftPanel.add(restPanel);
        leftPanel.add(infoPanel);
        leftPanel.add(introPanel);          
    }
    /**
     * updateInfoPanel() takes the given customer (or, for v3, Host) object and
     * changes the information panel to hold that person's info.
     *
     * @param person customer (or waiter) object
     */
    public void updateInfoPanel(Object person) {
        
        //currentPerson = person;
        
        if (person instanceof RanchoCustomerRole) {
        	currentCustomer = (RanchoCustomerRole)person;
            RanchoCustomerRole customer = currentCustomer;
            customerCB.setVisible(true);
            customerCB.setText("Hungry?");
            //Should checkmark be there? 
            customerCB.setSelected(customer.getGui().isHungry());
            //Is customer hungry? Hack. Should ask customerGui
            customerCB.setEnabled(!customer.getGui().isHungry());
             //Hack. Should ask customerGui
            TitledBorder border =new TitledBorder("Customer Name");
            customerInfoLabel.setBorder(border);
            customerInfoLabel.setText(customer.getName());
            customerInfoPanel.validate();
        }
        else if(person instanceof RanchoWaiterRole){
        	currentWaiter = (Waiter) person;
        	Waiter waiter = currentWaiter;
        	applyBreakCB.setVisible(true);
        	applyBreakCB.setText("Go Break?");
        	applyBreakCB.setSelected(waiter.getGui().isApplying());
        	applyBreakCB.setEnabled((!waiter.getGui().isApplying())&&(!waiter.getGui().isOnBreak()));
        	offBreakCB.setVisible(true);
        	offBreakCB.setText("Come Back");
        	offBreakCB.setSelected(false);
        	offBreakCB.setEnabled(waiter.getGui().isOnBreak());
        	statusCB.setVisible(true);
        	statusCB.setText("On Break?");
        	statusCB.setSelected(waiter.getGui().isOnBreak());
        	statusCB.setEnabled(false);
        	TitledBorder border = new TitledBorder("Waiter Name");
        	waiterInfoLabel.setBorder(border);
        	waiterInfoLabel.setText(waiter.getName());
        	waiterInfoPanel.validate();
        }
    }
    
    
    /**
     * Action listener method that reacts to the checkbox being clicked;
     * If it's the customer's checkbox, it will make him hungry
     * For v3, it will propose a break for the waiter.
     */
    public void actionPerformed(ActionEvent e) {
    	
        if (e.getSource() == customerCB) {
            //String name=customerInfoLabel.getText();
        	//restPanel.addPerson("Customers",name);
        	//restPanel.showInfo("Customers", name);
            /*if (currentPerson instanceof CustomerAgent) {
                CustomerAgent c = (CustomerAgent) currentPerson;
                c.getGui().setHungry();
                customerCB.setEnabled(false);   
            }*/
        	currentCustomer.getGui().setHungry();
        	customerCB.setEnabled(false);
        }
        else if(e.getSource()==applyBreakCB){
        	currentWaiter.getGui().applyBreak();
        	applyBreakCB.setEnabled(false);
        }
        else if(e.getSource()==offBreakCB){
        	currentWaiter.getGui().comeBack();
        	offBreakCB.setEnabled(false);
        }
        
        
        
        else if(e.getSource()==pauseButton){
        	if(pauseButton.getText().compareTo("Pause")==0){
        		pauseButton.setText("Restart");
        		restPanel.pause();
        		animationPanel.stopTimer();
        	}
        	else{
        		pauseButton.setText("Pause");
        		restPanel.restart();
        		animationPanel.startTimer();
        	}
        	
        }
    }
    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param agent reference to the customer
     */
    public void setCustomerEnabled(Customer agent) {
        
       if (agent.equals(currentCustomer)) {
           customerCB.setEnabled(true);
           customerCB.setSelected(false);
            
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
        gui.pack();
    }
}

