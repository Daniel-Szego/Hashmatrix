package Test;

import static org.junit.Assert.*;

import org.junit.Test;

import Crypto.CryptoProvider;
import Crypto.KeyPairString;
import Transaction.*;

public class TestTrasactions {

	@Test
	public void testTransactions() {
		
		CryptoProvider prov = new CryptoProvider();

		// Test generating key pairs
		KeyPairString keys =  prov.generateKeyPair();
		assertTrue(keys.privateKey.length() > 0);
		assertTrue(keys.publicKey.length() > 0);
		System.out.println("Public key : " + keys.publicKey);	
		System.out.println("Private key : " + keys.privateKey);	
		
		StateDataTransaction tr1 = new StateDataTransaction(keys.publicKey, "hello world");
		tr1.signTransaction(keys.privateKey);
		assertTrue(tr1.verifySignature());

		StateTransferTransaction tr2 = new StateTransferTransaction(keys.publicKey, keys.publicKey, 10);
		tr2.signTransaction(keys.privateKey);
		assertTrue(tr2.verifySignature());

		StateRuleTransaction tr3 = new StateRuleTransaction(keys.publicKey, "blank rule");
		tr3.signTransaction(keys.privateKey);
		assertTrue(tr3.verifySignature());
		
		TransactionPool pool = new TransactionPool(null);
		pool.addTransaction(tr1);
		pool.addTransaction(tr2);
		pool.addTransaction(tr3);
		assertTrue(pool.getPoolSize() == 3);
		assertTrue(pool.containsAddress(keys.publicKey));
		
		pool.removeTransactionbyId(tr1.getTransctionId());
		assertTrue(pool.getPoolSize() == 2);
		assertFalse(pool.containsId(tr1.getTransctionId()));
		
	}

}
