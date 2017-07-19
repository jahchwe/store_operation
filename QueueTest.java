import java.io.File;
import java.io.FileNotFoundException;

public class QueueTest {

	public static void main(String[] args) throws FileNotFoundException {
		
		File customers = new File("CustomersData.txt");
		File queries = new File("QueriesContent.txt");
		
		//one queue for all customers in a day
		CustomerList today = new CustomerList();
		today.createList(customers);
		
		//getting queries 
		today.getQueries(queries);
		
		//one queue to model customers in the store at any given time.
		CustomerList store = new CustomerList();
		
		while (!today.isEmpty()) {
			
			System.out.println("Before");
			System.out.println("Today:");
			today.displayCustomers();
			
			System.out.println("Store");
			store.displayCustomers();
			
			CustomerNode current = today.dequeue(); 
			store.enqueue(current);
			
			System.out.println("After");
			System.out.println("Today:");
			today.displayCustomers();
			
			System.out.println("Store");
			store.displayCustomers();

		}
		
		
		// TODO Auto-generated method stub

	}

}
