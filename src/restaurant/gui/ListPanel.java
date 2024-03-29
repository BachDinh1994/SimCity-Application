package restaurant.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
@SuppressWarnings("serial")
public class ListPanel extends JPanel implements ActionListener 
{
    public JScrollPane pane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private List<JButton> list = new ArrayList<JButton>();
    private JButton addPersonB = new JButton("Add Customer"); //Allows addition of customers
    private JButton addWaiter = new JButton("Add Waiter"); //Allows addition of waiters

    private RestaurantPanel restPanel;
    private String type;
    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public ListPanel(RestaurantPanel rp, String type) 
    {
        restPanel = rp;
        this.type = type;

        setLayout(new BoxLayout((Container) this, BoxLayout.Y_AXIS));
        //add(new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>"));
        if (type == "Customers") //Add either customer or waiter
        {
            addPersonB.addActionListener(this);
            add(addPersonB);
        }
        else
        {
            addWaiter.addActionListener(this);;
            add(addWaiter);
        }

        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
        pane.setViewportView(view);
        add(pane);
    }

    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    public void actionPerformed(ActionEvent e) 
    {
        if (e.getSource() == addPersonB && restPanel.customers.size() < 13) 
        {
        	addPerson(restPanel.input.getText());
        	if (restPanel.input.getText().length() == 0) //If the customer's string has length 0, that customer cannot be set to hungry
        	{
        		restPanel.gui.stateCB.setEnabled(false);
        	}
        }
        else if (e.getSource() == addWaiter && restPanel.waiters.size() < 16)
        {
        	addPerson(restPanel.input.getText());
        }
        else 
        {
        	for (JButton temp:list)
        	{
                if (e.getSource() == temp) //update the info panel if customer or waiter is clicked
                    restPanel.showInfo(type, temp.getText());
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
    public void addPerson(String name) 
    {
        if (name != null) 
        {
            JButton button = new JButton(name);
            button.setBackground(Color.white);

            Dimension paneSize = pane.getSize();
            Dimension buttonSize = new Dimension(paneSize.width - 20,
                    (int) (paneSize.height / 7));
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.addActionListener(this);
            list.add(button);
            view.add(button);
            restPanel.addPerson(type, name);//puts customer on list
            restPanel.showInfo(type, name);//puts hungry button on panel
            validate();
        }
    }
}