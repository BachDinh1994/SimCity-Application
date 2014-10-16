package SimCity;



public class Contact {
	///arbitrary x and y coordinate for the address
	///perhaps this maps it into the pixel world 
	
	
	//everyone should have t
	
	public Address myAddress = new Address(0, 0);
	public String  myType; // (ie. "Cook")
	public Object  myIdentity = new Object(); //(ie. "cookAgent")
	
	
	
	
	//////////////these are for workplace contacts which the personAgent won't use//////////////
	public Object  myBoss = new Object(); //since this is a food based world, myTransactioner is the 
							//person that the market and other franchises will contact for problems
							// (ie. for cookAgent, will list the cashierAgent when they couldn't pay the bill fully	
	
	public String specialty; //this is more for the restaurants
	public int stars;
	public Hours workingHours;
    //////////////////////////////////////////////////////////////////////////////////////
	
	public static class Hours{
		public int startHour;
		public int endHour;
		public boolean weekendOpen;
		public Hours(){}
		public Hours(int s, int e, boolean weekend){
			startHour =s;
			endHour =e;
			weekendOpen = weekend;
		}
	}
	
	/**
	 * 
	 * @param address: Address
	 * @param type: String type
	 * @param identity: Object role
	 * @param boss: Object
	 * @param hour: class Hour (working hours)
	 * @param rating: stars int
	 */
	public Contact(Address address, String type, Object identity, Object boss, Hours hour, int rating){
		myAddress = address;
		myType = type;
		myIdentity =  identity;
		myBoss = boss;
		workingHours = hour;
		stars = rating;
		
		
	}
	/**
	 * 
	 * @param address
	 */
	public Contact(Address address){
		myAddress=  address;
	}
	
	/**
	 * 
	 * @param address
	 * @param type
	 */
	public Contact(Address address, String type){
		myAddress = address;
		myType = type;
	}
	
	/**
	 * 
	 * @param address
	 * @param type
	 * @param identity
	 */
	public Contact(Address address, String type, Object identity){
		myAddress = address;
		myType = type;
		myIdentity = identity;
	}
	
	
	public Contact(Address address, String type, Object identity, Object boss){
		myAddress = address;
		myType = type;
		myIdentity = identity;
		myBoss =boss;
	}
	
	/**
	 * 
	 * @param st
	 * @param spec
	 * @param working
	 */
	public void setRestaurantInfo(int st, String spec, Hours working){
		stars = st;
		specialty = spec;
		workingHours = working;
	}
	
	public void setIdentity(Object m){
		myIdentity = m;
	}
	

	
	
	
}
