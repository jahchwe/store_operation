import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalTime;
import java.util.ArrayList;

public class StoreOperation {

	public static void main(String[] args) throws FileNotFoundException {
		
		int time = 0; 
		
		ArrayList<CustomerNode> complete = new ArrayList<CustomerNode>();
		
		String customerFile = args[0]; 
		String queriesFile = args[1];
		
		//File customers = new File("CustomersData.txt");
		//File queries = new File("QueriesContent.txt");
		
		File customers = new File(customerFile);
		File queries = new File(queriesFile);
		
		//one queue for all customers in a day
		CustomerList today = new CustomerList();
		today.createList(customers);
		
		//getting queries 
		today.getQueries(queries);
		
		//one queue to model customers in the store at any given time.
		CustomerList store = new CustomerList();
		
		Worker cashier = new Worker();
		int tempLongBreak = 0;
		
		int noon = 0; 
		
		//initialize first customer temp
		CustomerNode temp = today.dequeue(); 
		
		while (!today.isEmpty() || !store.isEmpty()) {
			
			//if noon, reset clock to be 0 - 11:59:59; 
			if (time == 43200) {
				noon = 1; 
				time = 0;
			}
			else if (time > 43200) {
				noon = 1; 
				time = time%43200; 
			}
			
			//customer enters store if its their time
			//will not enqueue customers after 5PM
			if ((noon == 0 && temp.seconds >= 18000 && time >= temp.seconds)|| (noon == 1 && time >= temp.seconds)) {//(time >= temp.seconds) {
				
				store.enqueue(temp);
				
				if (!today.isEmpty()) {
					//update temp
					temp = today.dequeue();
					continue;
				}
				
			}
			
			////if current number of people in the store is greater than the previous greatest, make greatest current
			int currentLength = store.currentLength();
			
			if (currentLength > today.longestLength) {
				today.longestLength = currentLength; 
			}
			
			////
			
			////if we are open (i.e. it is 9AM or later) and folks are at the door waiting;
			//32400 == 9AM
			if ((noon == 0 && time >= 32400 && !store.isEmpty()) || (noon == 1 && !store.isEmpty() && time < 18000)) {
				
				CustomerNode serving = store.dequeue(); 
				
				
				complete.add(serving);
				//calculate wait time by subtracting entering time from current time 
				serving.wait = Math.abs(time - serving.seconds);
				//add to total number served
				today.numServed++;
				
				
				//end of cashier break; check if break is longer than the previous longest.
				if (tempLongBreak > cashier.longest) {
					cashier.longest = tempLongBreak;
					tempLongBreak = 0; 
				}
				
				//problem: need to add all nodes that occur during 300 sec interval to list to correct
				//max list length 
				
				time += today.servetime; 
				
				//continue because time of serving already added; 
				continue;
				
			}
			
			//if its past closing time, set the rest of the customers' waiting times to 18000 - arrival time
			if (noon == 1 && time >= 18000) {
				
				temp.wait = Math.abs(18000 - temp.seconds);
				
				while (!store.isEmpty()) {
					//set wait for customers in store taht werent served
					CustomerNode current = store.dequeue(); 
					current.wait = Math.abs(18000 - current.seconds);
					complete.add(current);
					
				}
				break;
			}
			
			////
			
			//else if the store is empty; 
			if ((noon == 0 && time >= 32400 && store.isEmpty()) || (noon == 1 && time < 18000 && store.isEmpty())) {
				//calculate cashier idle and break
				cashier.totalIdle++;
				tempLongBreak++;
				
			}
			
			if (store.isEmpty() && today.isEmpty() && noon == 0) {
				//if no one else coming, cashier has rest of day off
				cashier.totalIdle += 43200 - time + 18000; 
				tempLongBreak += 43200 - time + 18000; 
				break; 
				
			}
			
			if (store.isEmpty() && today.isEmpty() && noon == 1) {
				
				cashier.totalIdle += 18000 - time; 
				tempLongBreak += 18000 - time; 
				break;
				
			}
			
			time++;
			
		}
		
	
		
		//end of cashier break; check if break is longer than the previous longest.
		if (tempLongBreak > cashier.longest) {
			cashier.longest = tempLongBreak;
			tempLongBreak = 0; 
		}
		
		//handle inputted queries
		handleQueries(today, cashier, complete);
		
		System.out.println("Done");
		
		
		// TODO Auto-generated method stub

	}
	
	public static void handleQueries(CustomerList input, Worker worker, ArrayList<CustomerNode> complete) {
		
		//use query info to respond. Query objects created in CustomerList class
		for (int i = 0; i < input.queryList.size(); i++) {
			
			switch (input.queryList.get(i).queryCode) {
			
			case 1:
				System.out.printf("%-30s%s%d%n", input.queryList.get(i).name+ ":", " ", input.numServed);	
				break;
			
			case 2: 
				System.out.printf("%-30s%s%d%n", input.queryList.get(i).name+ ":", " ", worker.longest);
				break;
				
			case 3: 
				System.out.printf("%-30s%s%d%n", input.queryList.get(i).name+ ":", " ", worker.totalIdle);
				break;
			
			case 4: 
				System.out.printf("%-30s%s%d%n", input.queryList.get(i).name+ ":", " ", input.longestLength);
				break;
				
			case 5: 
				CustomerNode asked = complete.get(input.queryList.get(i).CustomerID - 1);
				System.out.printf("%-30s%s%d%n", input.queryList.get(i).name+ ":" + " " + input.queryList.get(i).CustomerID, " ", asked.wait);
				break;
			}
			
		}
		
	}

}
