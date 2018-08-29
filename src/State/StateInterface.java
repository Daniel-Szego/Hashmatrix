package State;

import java.util.ArrayList;
import Transaction.*;


// public interface for the functionalities of a blockchain state
public interface StateInterface {
	
	// adding an account to the state
	public void addAccount(AccountBase _account);
	
	// querying if an account is in the state 
	public boolean isAccountContained(AccountBase _account);
	
	// querying if an account is in the state 
	public boolean isAccountContained(String _address);
	
	// getting an account based on the address
	public AccountBase getAccount(String _address);
	
	// gettint merkle root of the state
	public String getMerkleRoot();

	// return all the accounts
	public ArrayList<AccountBase> getAccounts();
	
	// creating a new state by copying the exiting one
	public State copyState();
	
	// getting the number of accounts
	public int getAccounsSize();
	
	//checks if a certain transaction is compatible with a given state
	// CALLED: at validation
	public boolean isTransactionValid(TransactionInterface tr);

	//checks if a certain transaction is compatible with a given state
	//BUT: accounts can be added to the state from transaction
	public boolean isTransactionValidEx(TransactionInterface tr);
	
	// applying the transaction to the state
	// CALLED: by Miner at assigning transactions to state
	// returning false if the transaction could not apply to the state
	public boolean applyTransaction(TransactionInterface tr);

}
