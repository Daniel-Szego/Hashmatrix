package Block;

import State.AccountInterface;

public interface GenesisBlockInterface extends BlockInterface {

	// adding account to the state
	public void addAccount(AccountInterface account);
		
}
