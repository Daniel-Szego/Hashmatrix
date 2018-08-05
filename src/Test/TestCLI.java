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


		
		// test key conversion
		System.out.println("TEST KEY CONVERSION");	
		Account account = new Account();
		PrivateKey owner = account.generateAccount();
		PublicKey address = account.getAddress();
		System.out.println("Public Key : " + address);
		System.out.println("Private Key : " + owner);		
		String accountString = CryptoUtil.getStringFromKey(address);
		String ownerString = CryptoUtil.getStringFromKey(owner);	
		System.out.println("Public Key String : " + accountString);
		System.out.println("Private Key String: " + ownerString);
		PublicKey publicReconverted =  CryptoUtil.getPublicKeyFromString(accountString);
		PrivateKey privateReconverted = CryptoUtil.getPrivateKeyFromString(ownerString);
		System.out.println("Public Key String reconverted : " + publicReconverted);
		System.out.println("Private Key String reconverted: " + privateReconverted);
		
		System.out.println("");	

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
