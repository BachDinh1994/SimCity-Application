package SimCity;
import java.awt.Image;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

//Building has x,y location, image, string of type and generalType,the building panel, and list of residents


@SuppressWarnings("serial")
public class Building extends Rectangle2D.Double 
{
	int x,y,width,height;
	ImageIcon img;
	Image i;
	String type,generaltype;
    public BuildingPanel myBuildingPanel;
    
    List<PersonAgent> residents = new ArrayList<PersonAgent>();
    public Building(int x, int y, int width, int height, String t) 
    {
    	super(x, y, width, height);
    	type = t;
    	this.x=x;this.y=y;this.width=width;this.height=height;
    	char c = t.charAt(t.length()-1);
    	if (c >= '0' && c <= '9')
    	{
    		generaltype = t.substring(0,t.length()-1);
        	img = new ImageIcon("Images/"+t.substring(0,t.length()-1).toLowerCase()+".png");
    	}
    	else
    	{
    		generaltype = type;
        	img = new ImageIcon("Images/"+t.toLowerCase()+".png");
    	}
    	i = img.getImage();
    } 
    public void displayBuilding() 
    {
        myBuildingPanel.displayBuildingPanel();
    }
    public void setBuildingPanel(BuildingPanel bp) 
    {
        myBuildingPanel = bp;
    }
    public String getType()
    {
    	return type;
    }
    
}