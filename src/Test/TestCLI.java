package Test;

import java.security.*;

import CLI.*;
import Crypto.CryptoUtil;
import State.*;

public class TestCLI {

	public static void main(String[] args) {
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
		
	}
	
	
}
