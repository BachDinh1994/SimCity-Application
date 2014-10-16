package SimCity;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

@SuppressWarnings("serial")
public class SimCityTest extends JFrame implements ActionListener
{
	JComboBox combo;
	JLabel txt;
	SimCity city;
	JButton addPerson = new JButton("Add a Person");
	JCheckBox sidwaiter = new JCheckBox("Waiter at Sid's restaurant");
	JCheckBox bachwaiter = new JCheckBox("Waiter at Bach's restaurant");
	JCheckBox ranchowaiter = new JCheckBox("Waiter at Rancho's restaurant");
	JCheckBox andrewwaiter = new JCheckBox("Waiter at Andrew's restaurant");
	JCheckBox amywaiter = new JCheckBox("Waiter at Amy's restaurant");
	
	JCheckBox sidnormalwaiter = new JCheckBox("Normal Waiter at Sid's restaurant");
	JCheckBox bachnormalwaiter = new JCheckBox("Normal Waiter at Bach's restaurant");
	JCheckBox ranchonormalwaiter = new JCheckBox("Normal Waiter at Rancho's restaurant");
	JCheckBox andrewnormalwaiter = new JCheckBox("Normal Waiter at Andrew's restaurant");
	JCheckBox amynormalwaiter = new JCheckBox("Normal Waiter at Amy's restaurant");

	JCheckBox sidcook = new JCheckBox("Cook at Sid's restaurant");
	JCheckBox bachcook = new JCheckBox("Cook at Bach's restaurant");
	JCheckBox ranchocook = new JCheckBox("Cook at Rancho's restaurant");
	JCheckBox andrewcook = new JCheckBox("Cook at Andrew's restaurant");
	JCheckBox amycook = new JCheckBox("Cook at Amy's restaurant");
	
	JCheckBox sidmanager = new JCheckBox("Manager at Sid's restaurant");
	JCheckBox bachmanager = new JCheckBox("Manager at Bach's restaurant");
	JCheckBox ranchomanager = new JCheckBox("Manager at Rancho's restaurant");
	JCheckBox andrewmanager = new JCheckBox("Manager at Andrew's restaurant");
	JCheckBox amymanager = new JCheckBox("Manager at Amy's restaurant");
	

	JCheckBox bankteller = new JCheckBox("Bankteller");
	JCheckBox bankhost = new JCheckBox("Bankhost");
	JCheckBox bankrobber = new JCheckBox("Bankrobber");
	JCheckBox bankcustomer = new JCheckBox("Bankcustomer");
	
	JCheckBox unemployed = new JCheckBox("Unemployed");
	JCheckBox car = new JCheckBox("Owns a car");
	JCheckBox home = new JCheckBox("Owns a home");
	
	JTextField personSpeed = new JTextField(5);
	JTextField carSpeed = new JTextField(5);
	JTextField personXPos = new JTextField(5);
	JTextField personYPos = new JTextField(5);
	JTextField destination = new JTextField(15);
	
	Vector<JCheckBox> boxes = new Vector<JCheckBox>();
	
    public SimCityTest(SimCity c) 
    {
    	setLayout(new GridLayout(40,1));
    	city = c;
    	c.cityPanel.c = this;
    	
    	addPerson.addActionListener(this);
    	add(addPerson);
    	txt = new JLabel("Person Speed");
    	add(txt);
    	add(personSpeed);
    	txt = new JLabel("Car Speed");
    	add(txt);
    	add(carSpeed);
    	txt = new JLabel("Person X Position");
    	add(txt);
    	add(personXPos);
    	txt = new JLabel("Person Y Position");
    	add(txt);
    	add(personYPos);
    	txt = new JLabel("Person Destination");
    	add(txt);
    	add(destination);
    	
    	boxes.add(unemployed);
    	
    	boxes.add(bachwaiter);
    	boxes.add(sidwaiter);
    	boxes.add(ranchowaiter);
    	boxes.add(andrewwaiter);
    	boxes.add(amywaiter);
    	
    	boxes.add(bachnormalwaiter);
    	boxes.add(sidnormalwaiter);
    	boxes.add(ranchonormalwaiter);
    	boxes.add(andrewnormalwaiter);
    	boxes.add(amynormalwaiter);
    	
    	boxes.add(bachcook);
    	boxes.add(sidcook);
    	boxes.add(ranchocook);
    	boxes.add(andrewcook);
    	boxes.add(amycook);
    	
    	boxes.add(bachmanager);
    	boxes.add(sidmanager);
    	boxes.add(ranchomanager);
    	boxes.add(andrewmanager);
    	boxes.add(amymanager);
    	
    	boxes.add(bankteller);
    	boxes.add(bankcustomer);
    	boxes.add(bankrobber);
    	boxes.add(bankhost);
    	
    	boxes.add(car);
    	boxes.add(home);
    	
    	//Vector<String> v = new Vector<String>();
    	for (JCheckBox b:boxes)
    	{
    		//v.add(b.getText());
    		add(b);
    		b.addActionListener(this);
    	}
    	//combo = new JComboBox(v);
    	//add(combo);
    	unemployed.setSelected(true);
    	car.setSelected(true);
    	home.setSelected(true);
    	Disable(unemployed);
    } 
    public void Disable(JCheckBox j)
    {
    	for (JCheckBox b:boxes)
    	{
    		if (b != j && b != car && b != home)
    		{
    			b.setEnabled(false);
    		}
    	}
    }
    public void Enable(JCheckBox j)
    {
    	for (JCheckBox b:boxes)
    	{
    		if (b != j && b != car && b != home)
    		{
    			b.setEnabled(true);
    		}
    	}
    }
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == addPerson)
		{
			city.cityPanel.addPerson();
		}
		else 
		{
	    	for (JCheckBox b:boxes)
	    	{
	    		if (e.getSource() == b && b != car && b != home)
	    		{
					if (b.isSelected())
					{
						Disable(b);
					}
					else
					{
						Enable(b);
					}
					break;
	    		}
	    	}
		}
	}
}