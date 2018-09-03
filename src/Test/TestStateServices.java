package Test;

import static org.junit.Assert.*;
import org.junit.Test;

import ServiceBus.*;
import State.*;

public class TestStateServices {

	@Test
	public void testStateServices() {
		ServiceBus bus = new ServiceBus(null);
		State newState = new State();
		AccountBase newAccount1 = new AccountBase("testkey1"); 
		AccountBase newAccount2 = new AccountBase("testkey2"); 
		AccountBase newAccount3 = new AccountBase("testkey3"); 
		
		newState.addAccount(newAccount1);
		newState.addAccount(newAccount2);
		newState.addAccount(newAccount3);
		
		assertTrue(newState.getAccountsSize() == 3);
		
		StateInterface newestState = newState.copyState();
		AccountBase newAccount4 = new AccountBase("testkey4"); 
		newestState.addAccount(newAccount4);
		
		assertTrue(newState.getAccountsSize() == 3);
		assertTrue(newestState.getAccountsSize() == 4);
		assertTrue(newestState.getAccount("testkey4") != null);		
			
	}

}
