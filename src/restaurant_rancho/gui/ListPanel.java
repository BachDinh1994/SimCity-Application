package restaurant_rancho.gui;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import restaurant_rancho.RanchoCustomerRole;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
public class ListPanel extends JPanel implements ActionListener,KeyListener {
    private RestaurantPanel restPanel;
    private final int WINDOWX=500;
    private final int WINDOWY=450;
    //customerData
    
    public JScrollPane customerPane =
    new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel customerView = new JPanel();
    private JPanel customerPanel=new JPanel();
    private JPanel customerInputPanel = new JPanel();
    private JTextField customerNameField = new JTextField();
    private JCheckBox hungryBox = new JCheckBox("Hungry?");
    private JButton addCustomerButton = new JButton("Add");
    private ArrayList<JButton> customerButtonList = new ArrayList<JButton>();
    
    //waiterData
    
    private ArrayList<JButton> waiterButtonList = new ArrayList<JButton>();
    private JPanel waiterPanel = new JPanel();
	private JButton addWaiterButton=new JButton("Add a waiter");
	private JPanel waiterInputPanel=new JPanel();
	public JScrollPane waiterPane =
		    new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	private JPanel waiterView=new JPanel();
	private JTextField waiterNameField = new JTextField();
    
    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public ListPanel(RestaurantPanel rp, String type) {
        restPanel = rp;
        setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
        
        //The size of the whole list Panel
        Dimension personDim = new Dimension((int)(WINDOWX*.7),(int)(WINDOWY*.5));
        setPreferredSize(personDim);
    	setMinimumSize(personDim);
    	setMaximumSize(personDim);
        
    	//Add Listeners 
        customerNameField.addKeyListener(this);
        hungryBox.addActionListener(this);
        addCustomerButton.addActionListener(this);
        addWaiterButton.addActionListener(this);
        
        
        //initialize hungryBox
        hungryBox.setEnabled(false);
        
        //Add two subpanels customerPanel & waiterPanel
        add(customerPanel);
        add(waiterPanel);
        
        //Specify the layout for the two panels
        customerPanel.setLayout(new BoxLayout(customerPanel, BoxLayout.Y_AXIS));
        waiterPanel.setLayout(new BoxLayout(waiterPanel, BoxLayout.Y_AXIS));
        
        //customerPanel
        customerPanel.add(new JLabel("Customers"));
        customerPanel.add(customerInputPanel);
        customerPanel.add(customerPane);
        customerView.setLayout(new BoxLayout((Container) customerView, BoxLayout.Y_AXIS));
        customerPane.setViewportView(customerView);
        customerPanel.setPreferredSize(new Dimension((int)(0.4*WINDOWX),(int)(0.5*WINDOWY)));
        customerPanel.setMaximumSize(new Dimension((int)(0.4*WINDOWX),(int)(0.5*WINDOWY)));
        customerPanel.setMinimumSize(new Dimension((int)(0.4*WINDOWX),(int)(0.5*WINDOWY)));
        customerInputPanel.setPreferredSize(new Dimension((int)(0.4*WINDOWX),(int)(0.1*WINDOWY)));
        customerInputPanel.setMaximumSize(new Dimension((int)(0.4*WINDOWX),(int)(0.1*WINDOWY)));
        customerInputPanel.setMinimumSize(new Dimension((int)(0.4*WINDOWX),(int)(0.1*WINDOWY)));
        customerInputPanel.setLayout(new GridLayout(2,2,0,0));
        customerInputPanel.add(customerNameField);
        customerInputPanel.add(hungryBox);
        customerInputPanel.add(addCustomerButton);
        
        
        //waiterPanel
        waiterPanel.add(new JLabel("Waiters"));
        waiterPanel.add(waiterInputPanel);
        waiterPanel.add(waiterPane);
        waiterView.setLayout(new BoxLayout((Container)waiterView,BoxLayout.Y_AXIS));
        waiterPane.setViewportView(waiterView);  
        waiterPanel.setPreferredSize(new Dimension((int)(0.3*WINDOWX),(int)(0.5*WINDOWY)));
        waiterPanel.setMaximumSize(new Dimension((int)(0.3*WINDOWX),(int)(0.5*WINDOWY)));
        waiterPanel.setMinimumSize(new Dimension((int)(0.3*WINDOWX),(int)(0.5*WINDOWY)));
        waiterInputPanel.setPreferredSize(new Dimension((int)(0.3*WINDOWX),(int)(0.1*WINDOWY)));
        waiterInputPanel.setMaximumSize(new Dimension((int)(0.3*WINDOWX),(int)(0.1*WINDOWY)));
        waiterInputPanel.setMinimumSize(new Dimension((int)(0.3*WINDOWX),(int)(0.1*WINDOWY)));
        waiterInputPanel.setLayout(new GridLayout(2,1,0,0));
        waiterInputPanel.add(waiterNameField );
        waiterInputPanel.add(addWaiterButton);
    }
    
    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    public void actionPerformed(ActionEvent e) {
        
    	//Add a Waiter Button is triggered
    	if(e.getSource()==addWaiterButton){
    		addWaiter(waiterNameField.getText());
    		waiterNameField.setText("");
    		return;
    	}	
    	
    	//Add a Customer Button is triggered
    	if(e.getSource()==addCustomerButton){
    		addCustomer(customerNameField.getText());
    		customerNameField.setText("");
    		hungryBox.setEnabled(false);
    		return;
    	}
    	
    	//A customer button is triggered
        for (JButton temp:customerButtonList){
            if (e.getSource() == temp){
                restPanel.showInfo("Customers", temp.getText());
                return;
            }
        }
    	
        //A  button is triggered
    	for (JButton temp:waiterButtonList){
    		if(e.getSource()==temp){
    			restPanel.showInfo("Waiters",temp.getText());
    			return;
    		}
    	}
    }
    
    
    /**
     * If the add button is pressed, this function creates
     * a spot for it in the scroll pane, and tells the restaurant panel
     * to add a new person.
     *
     * @param name name of new person
     */
    public void addCustomer(String name) {
        if (name != null&&!name.equals("")) {
        	//add Customer buttons
            JButton button = new JButton(name);
            button.setBackground(Color.white);
            Dimension paneSize = customerPane.getSize();
            Dimension buttonSize = new Dimension((int)(paneSize.width*0.7),
                                                 (int) (paneSize.height / 7));
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.addActionListener(this);
            customerButtonList.add(button);
            customerView.add(button);
            
            //add CustomerAgents and customerGuis
            restPanel.addPerson("Customers", name);//puts customer on list
            restPanel.showInfo("Customers", name);//puts hungry button on panel
            validate();
        }
    }
    
    
    
    //create waiter buttons, waiteragents and waiterguis
	private void addWaiter(String name) {
		if (name != null&&!name.equals("")) {
            //Add a waiter button
			JButton button = new JButton(name);
            button.setBackground(Color.white);
            Dimension paneSize = waiterPane.getSize();
            Dimension buttonSize = new Dimension((int)(paneSize.width*0.7),
                                                 (int) (paneSize.height / 7));
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.addActionListener(this);
            waiterButtonList.add(button);
            waiterView.add(button);
            
            //Add a waiterAgent and waiterGui
            restPanel.addPerson("Waiters",name);
            restPanel.showInfo("Waiters",name);
            validate();
		}
	}

	public void keyPressed(KeyEvent arg0) {
	}

	//When the user types something to the textfield, the hungry box is enabled.
	public void keyReleased(KeyEvent e) {
		if (e.getSource()==customerNameField){
			if(customerNameField.getText().equals("")){
				hungryBox.setEnabled(false);
				addCustomerButton.setEnabled(false);
			}
			else{
				hungryBox.setEnabled(true);
				addCustomerButton.setEnabled(true);
			}
    	}
	}

	public void keyTyped(KeyEvent arg0) {
	}
    
    

	public JCheckBox getHungryBox() {
		return hungryBox;
	}
}
