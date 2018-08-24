package Transaction;

public interface TransactionPoolInterface {

	// adding transaction to the pool
	public void addTransaction(StateTransaction tr);
	
	// getting state transaction by transaction id
	public StateTransaction getTransactionByTrId(String trId);
	
	// getting transaction based on the transaction address
	// it is the from address in case of a Transfer transaction
	public StateTransaction getTransactionByAddress(String trAddress);
	
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
}
