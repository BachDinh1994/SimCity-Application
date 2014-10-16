package SimCity;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class Clock extends JPanel implements ActionListener
{
	private int hour,min,sec;
	Timer timer = new Timer(1 ,this);
	public Clock()
	{
		hour = 0;
		min = 0;
		sec = 0;
		timer.start();
	}
	public Clock(int hour, int min, int sec)
	{
		this.hour = hour;
		this.min = min;
		this.sec = sec;
	}
	public int getHour()
	{
		return hour%12;
	}
	public void setHour(int hour)
	{
		this.hour = hour;
		repaint();
	}
	public int getMin()
	{
		return min%60;
	}
	public void setMin(int min)
	{
		this.min = min;
		repaint();
	}
	public int getSec()
	{
		return sec%60;
	}
	public void setSec(int sec)
	{
		this.sec = sec;
		repaint();
	}
	public void actionPerformed(ActionEvent e)
	{
		repaint();
	}
	public void paintComponent(Graphics g)
	{
		sec++;
		if (sec%3600==0 && sec!=0)
		{
			hour++;
		}
		if (sec%60 == 0 && sec != 0)
		{
			min++;
		}

		super.paintComponent(g);
		String hourDisplay,minDisplay,secDisplay;
		hourDisplay = Integer.toString(hour%12);
		minDisplay = Integer.toString(min%60);
		secDisplay = Integer.toString(sec%60);
		
		if (hour%12 < 10)
		{
			hourDisplay = "0"+hour%12;
		}
		if (min%60 < 10)
		{
			minDisplay = "0"+min%60;
		}
		if (sec%60 < 10)
		{
			secDisplay = "0"+sec%60;
		}
		int radius = (int)(Math.min(getWidth(),getHeight())*0.8*0.5);
		int xCenter = getWidth()/2;
		int yCenter = getHeight()/2;
		
		//Midnight and normal hours
		if (hour%12 == 0)
		{
			g.drawString("12"+":"+minDisplay+":"+secDisplay, xCenter-25, yCenter+100);
		}
		else
		{
			g.drawString(hourDisplay+":"+minDisplay+":"+secDisplay, xCenter-25, yCenter+100);
		}
		
		g.setColor(Color.BLACK);
		g.drawOval(xCenter-radius,yCenter-radius,radius*2,radius*2);
		g.drawString("12", xCenter - 5, yCenter - radius + 12);
		g.drawString("9", xCenter - radius + 3, yCenter + 5);
		g.drawString("3", xCenter + radius - 10, yCenter + 3);
		g.drawString("6", xCenter - 3, yCenter + radius - 3);
		
		int sLength = (int)(radius*0.8);
		int xSecond = (int)(xCenter+sLength*Math.sin(sec*(2*Math.PI/60)));
		int ySecond = (int)(yCenter-sLength*Math.cos(sec*(2*Math.PI/60)));
		g.setColor(Color.red);
		g.drawLine(xCenter, yCenter, xSecond, ySecond);
		
		int mLength = (int)(radius*0.65);
		int xMinute = (int)(xCenter+mLength*Math.sin(sec/60*(2*Math.PI/60)));
		int yMinute = (int)(yCenter-mLength*Math.cos(sec/60*(2*Math.PI/60)));
		g.setColor(Color.blue);
		g.drawLine(xCenter, yCenter, xMinute, yMinute);
		
		int hLength = (int)(radius*0.5);
		int xHour = (int)(xCenter+hLength*Math.sin((sec/3600.0)*(2*Math.PI/12)));
		int yHour = (int)(yCenter-hLength*Math.cos((sec/3600.0)*(2*Math.PI/12)));
		g.setColor(Color.green);
		g.drawLine(xCenter, yCenter, xHour, yHour);
		
		g.setColor(Color.red);
		g.drawString("SimCity Clock", xCenter-35, 20);
	}
	public void setSpeed(int speed)
	{
		timer.stop();
		timer = new Timer(speed,this);
		timer.start();
	}
}