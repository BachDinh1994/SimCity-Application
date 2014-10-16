package SimCity;



public class myContact {
	///arbitrary x and y coordinate for the address
	///perhaps this maps it into the pixel world 
	
	
	//everyone should have t
	
	public Address myAddress;
	public String  myType; // (ie. "Cook")
	public Object  myIdentity; //(ie. "cookAgent")
	public Object  myBoss; //since this is a food based world, myTransactioner is the 
							//person that the market and other franchises will contact for problems
							// (ie. for cookAgent, will list the cashierAgent when they couldn't pay the bill fully	
	
	public myContact(Address address, String type, Object identity, Object boss){
		myAddress = address;
		myType = type;
		myIdentity =  identity;
		myBoss = boss;
	}
}
