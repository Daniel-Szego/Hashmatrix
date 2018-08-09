package Test;

import java.security.*;

import CLI.*;
import Crypto.CryptoUtil;
import State.*;

public class TestCLI {

	public static void main(String[] args) {
		
		// starting security provider, not sure if this is the right place
		//Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		
		// TODO Auto-generated method stub
		
		// test new account generation
		System.out.println("TEST ACCOUNT GENERATION");
		String[] params = {"-createAccount"};
		Cli.main(params);	
		System.out.println("");	
		
		String accountString = Cli.node.wallet.getAccounts().get(0).getOwnerString();
		
		// test transactions
		System.out.println("TEST TRANSACTIONS");	
		System.out.println("Create:");	
		String[] params2 = {"-createTransaction", "-state", "-address", accountString, "-value", "'hello world'"};
		Cli.main(params2);	
		System.out.println("");	
		System.out.println("Transfer:");	
		String[] params3 = {"-createTransaction", "-transfer", "-from", accountString, "-to", accountString,"-amount", "22"};
		Cli.main(params3);	
		System.out.println("");	

		// test transactions
		System.out.println("TRANSACTION SIGNATURES");	

		
	}
	
	
}
