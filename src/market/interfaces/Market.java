package market.interfaces;

import java.util.*;

import SimCity.myContact;


public interface Market
{
	public abstract void msgCallOrder(Map<String,Integer> supply, String choice, myContact orderContact);
	public abstract void msgPleaseStop();
	public abstract String getName();
}