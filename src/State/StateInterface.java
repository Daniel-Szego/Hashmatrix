package State;

import java.util.ArrayList;

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
}
