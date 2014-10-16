package restaurant_andrew;

import java.util.ArrayList;

public class AndrewMenu {

	private class Choice {
		String choice; double price;
		public Choice(String choice, double price) {
			this.choice = choice;
			this.price = price;
		}
	}
	ArrayList<Choice> choices;
	
	public AndrewMenu() {
		choices = new ArrayList<Choice>();
		choices.add(new Choice("Steak",   15.99));
		choices.add(new Choice("Chicken", 10.99));
		choices.add(new Choice("Salad",   5.99));
		choices.add(new Choice("Pizza",   8.99));
	}
	public AndrewMenu(String removed) {
		choices = new ArrayList<Choice>();
		if (! removed.equals("Steak")) 	 choices.add(new Choice("Steak",   15.99));
		if (! removed.equals("Chicken")) choices.add(new Choice("Chicken", 10.99));
		if (! removed.equals("Salad"))   choices.add(new Choice("Salad",   5.99));
		if (! removed.equals("Pizza"))   choices.add(new Choice("Pizza",   8.99));
	}

	public String getChoice(int choiceNum) {
		return choices.get(choiceNum).choice;
	}
	
	public double getPrice(int choiceNum) {
		return choices.get(choiceNum).price;
	}
	
	public int getSize() {
		return choices.size();
	}
	
}
