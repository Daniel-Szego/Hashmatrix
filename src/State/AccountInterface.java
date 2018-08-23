package State;

// interface for the functionalities of an account
public interface AccountInterface {
	
	// getting the address of the account which is directly the public key 
	// or the hash of the public key
	// address is also the primary key
	public String getAddress();
		
	// getting data associated with the account
	public String getData();

	// setting new data value
	public void setData(String data);

	// getting the balance 
	public double getBalance();
	
	// increasing balance
	public void increaseBalance(double amount);

	// decreasing balance
	public void decreaseBalance(double amount);
		
	// gettig the asset type
	public char[] getAssetType();

	// setting the data type
	public void setAssetType(String typeDescription);
	
	// getting the nonce
	public int getNonce();
	
	// increasing nonce
	public void increaseNonce();	
	
	// copying an account with all of its parmeters
	public AccountBase copyAccount();
}
