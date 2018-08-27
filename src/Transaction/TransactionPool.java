package Transaction;

import java.util.ArrayList;

import Node.*;

// transaction pool for the incomming but still not processed transactions
public class TransactionPool implements TransactionPoolInterface {
	
	public final Node node;
	
	public TransactionPool(Node _node) {
		this.node = _node;
	}
	
	protected ArrayList<TransactionInterface> transactions = new ArrayList<TransactionInterface>();
	
	// adding transaction to the pool
	public void addTransaction(TransactionInterface tr) {
		transactions.add(tr);
	}
	
	// getting state transaction by transaction id
	public TransactionInterface getTransactionByTrId(String trId) {
		for(TransactionInterface tr: transactions) {
			if(tr.getTransctionId().equals(trId))
				return tr;
		}
		return null;
	}
	
	// getting transaction based on the transaction address
	// it is the from address in case of a Transfer transaction
	public TransactionInterface getTransactionByAddress(String trAddress) {
		for(TransactionInterface tr: transactions) {
			if (tr instanceof StateDataTransaction ) {
				if(((StateDataTransaction)tr).address.equals(trAddress))
					return tr;	
			}else if (tr instanceof StateTransferTransaction) {
				if(((StateTransferTransaction)tr).from.equals(trAddress))
					return tr;					
			}else if (tr instanceof StateRuleTransaction) {
				if(((StateRuleTransaction)tr).address.equals(trAddress))
					return tr;						
			} 
		}
		return null;		
	}
	
	// transaction id is contained in the pool
	public boolean containsId(String trId) {
		if (getTransactionByTrId(trId) != null)
			return true;
		else
			return false;
	}
	
	// transaction id is contained in the pool
	public boolean containsAddress(String trAddress) {
		if (getTransactionByAddress(trAddress) != null)
			return true;
		else
			return false;		
	}

	// transaction id is contained in the pool
	public void removeTransactionbyId(String trId) {
		TransactionInterface tr = this.getTransactionByTrId(trId);
		transactions.remove(tr);
	}
	
	// transaction address is contained in the pool
	public void removeTransactionByAddress(String trAddress) {
		TransactionInterface tr = this.getTransactionByAddress(trAddress);
		transactions.remove(tr);
	}
	
	// getting the size of the pool
	public int getPoolSize() {
		return transactions.size();
	}

}
