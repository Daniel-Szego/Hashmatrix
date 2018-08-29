package Transaction;

import java.util.ArrayList;

public interface TransactionPoolInterface {

	// adding transaction to the pool
	public void addTransaction(TransactionInterface tr);
	
	// getting state transaction by transaction id
	public TransactionInterface getTransactionByTrId(String trId);
	
	// getting transaction based on the transaction address
	// it is the from address in case of a Transfer transaction
	public TransactionInterface getTransactionByAddress(String trAddress);
	
	// transaction id is contained in the pool
	public boolean containsId(String trId);
	
	// transaction id is contained in the pool
	public boolean containsAddress(String trAddress);

	// transaction id is contained in the pool
	public void removeTransactionbyId(String trId);
	
	// transaction address is contained in the pool -> removing the first match
	public void removeTransactionByAddress(String trAddress);
		
	// getting the size of the pool
	public int getPoolSize();
	
	//getting the transactions
	//it is needed for validations, transactions can be validated only with the state
	public ArrayList<TransactionInterface> getTransactions();
}
