
public class CustomerNode {
	
	public int id; 
	public String arrivalTime; 
	public int seconds;
	public int counter; 
	public int wait;
	
	public CustomerNode next = null; 

	public void timeInSeconds(String arrivalTime) {
		
		String[] seg = arrivalTime.split(":");
		this.seconds = (3600*Integer.parseInt(seg[0])) + (60 * Integer.parseInt(seg[1])) + Integer.parseInt(seg[2]);
	
	}
	
	
}
