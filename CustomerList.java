import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class CustomerList {
	
	public int servetime; 
	public CustomerNode first; 
	public int longestLength;
	public int numServed; 
	
	public ArrayList<Query> queryList = new ArrayList<Query>();
	
	
	//initialize customerList
	public CustomerList() {
		first = null;
	}
	
	//check if empty
	public Boolean isEmpty() {
		if (first == null) {
			return true;
		}
		
		else { 
			return false; 
		}
	}
	
	//count length
	public int currentLength() {
		
		int ans = 0; 
		CustomerNode current = first; 
		while (current != null) {
			ans++;
			current = current.next;
		}
		
		return ans; 
		
	}
	//enqueue
	public void enqueue(CustomerNode addition) {
		
		addition.next = null; 
		
		//set current to first
		CustomerNode current = first;
		
		//special case since first doesnt have .next
		if (current == null) {
			first = addition; 
			return;
		}
		
		//while current is not the last element
		while (current.next != null) {
			//increment current
			current = current.next;
		}
		//add the addition at the end of the list. addition.next defaults to null
		current.next = addition; 
		
	}
	
	public CustomerNode dequeue() {
		
		CustomerNode ans = first; 
	
		if (first.next == null)  {
			first = null; 
		}
		else {
			first = first.next; 
		}
		return ans; 
		
	}
	
	public void displayCustomers() {
		
		CustomerNode current = first; 
		//iterate through and display
		while (current != null) {
			System.out.printf("%-10s%-20s%s\n", "ID: "+ current.id, " Arrival Time: "+ current.arrivalTime, " Seconds: " +current.seconds);
			current = current.next;
		}
		
	}
	
	public void displayQueries() {
		
		for (int i = 0; i < queryList.size(); i ++) {
			
			System.out.printf("%-30s%-10d\n", queryList.get(i).name, queryList.get(i).queryCode);
			
		}
		
	}
	
	public void createList(File customers) throws FileNotFoundException {
		
		try {
			
			Scanner custscan = new Scanner(customers);
			
			ArrayList<String> custContent = new ArrayList<String>();
					
			servetime = Integer.parseInt(custscan.nextLine());
			
			//while file has not been exhausted
			while (custscan.hasNextLine()) {
				
				String line = custscan.nextLine();
				
				//skip over blank lines
				if (line.isEmpty()) {
					continue;
				}
				
				//disregard the label, store only the data
				String[] split = line.split(" ");
				
				custContent.add(split[1]);		
			}
			
			//use the collected data to make new CustomerNodes
			for (int i = 0; i < custContent.size(); i+=2) {
				
				CustomerNode a = new CustomerNode();
				a.id = Integer.parseInt(custContent.get(i));
				a.arrivalTime = custContent.get(i+1);
				a.timeInSeconds(a.arrivalTime);
				this.enqueue(a);
				
			}
			
			custscan.close();
			
			
		}
		//catch is file is not found
		catch (FileNotFoundException err) {
			
			System.out.println("File not found");
			
		}
	}
	
	public void getQueries(File queries) throws FileNotFoundException {
		
		//scan info and tokenize
		ArrayList<String> queryContent = new ArrayList<String>();
		
		Scanner querscan = new Scanner(queries);
		
		while (querscan.hasNextLine()) {
			
			String line = querscan.nextLine(); 
			
			String[] split = line.split(" ");
			
			for (int i = 0; i < split.length; i++) {
				
				queryContent.add(split[i]);
	
			}
		}
		
		querscan.close();
		
		//create query objects from tokens
		for (int i = 0; i < queryContent.size(); i ++) {
			
			Query a = new Query();
			
			if (queryContent.get(i).equals("NUMBER-OF-CUSTOMERS-SERVED")) {
				a.name = "NUMBER-OF-CUSTOMERS-SERVED";
				a.queryCode = 1;
			}
			
			else if (queryContent.get(i).equals("LONGEST-BREAK-LENGTH")) {
				a.name = "LONGEST-BREAK-LENGTH";
				a.queryCode = 2; 
			}
			
			else if (queryContent.get(i).equals("TOTAL-IDLE-TIME")) {
				a.name = "TOTAL-IDLE-TIME";
				a.queryCode = 3; 
			}
			
			else if (queryContent.get(i).equals("MAXIMUM-NUMBER-OF-PEOPLE-IN-QUEUE-AT-ANY-TIME")) {
				a.name = "MAXIMUM-NUMBER-OF-PEOPLE-IN-QUEUE-AT-ANY-TIME";
				a.queryCode = 4; 
			}
			
			else if (queryContent.get(i).equals("WAITING-TIME-OF")) {
				a.name = "WAITING-TIME-OF";
				a.queryCode = 5; 
				a.CustomerID = Integer.parseInt(queryContent.get(i+1));
				i++;
			}
			
			queryList.add(a);
			
		}
	}
	
}

