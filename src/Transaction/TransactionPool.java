package Transaction;

import java.util.ArrayList;

import Node.*;

// transaction pool for the incomming but still not processed transactions
public class TransactionPool {
	
	public final Node node;
	
	public TransactionPool(Node _node) {
		this.node = _node;
	}
	
	public ArrayList<StateTransaction> pool = new ArrayList<StateTransaction>();
	
	// adding transaction to the pool
	public void addTransaction(StateTransaction tr) {
		pool.add(tr);
	}
	
	
	
}
