package Test;

import java.security.*;

import CLI.*;
import Crypto.CryptoUtil;
import Node.*;
import State.*;

public class TestCLI {

	public static void main(String[] args) {
		
		// starting security provider, not sure if this is the right place
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		
		// TODO Auto-generated method stub
		
		// starting the network

		System.out.println("STARTING NETWORK");
		String[] paramss = {"-startNetwork", "-port", "8426"};
		Cli.main(paramss);	
		System.out.println("");	

		
		System.out.println("CONNECT PEER");
		String[] paramsp = {"-connectPeer", "-peerAddress", "localhost", "-peerPort", "8425"};
		Cli.main(paramsp);	
		System.out.println("");		
		
		// test new account generation
		System.out.println("TEST ACCOUNT GENERATION");
		String[] params = {"-createAccount"};
		Cli.main(params);	
		System.out.println("");	

		//GENESIS BLOCK
		System.out.println("GENESIS BLOCK");
		String[] paramsg = {"-createGenesisBlock"};
		Cli.main(paramsg);	
		System.out.println("genesis block created");
		System.out.println("");
		
		String accountString = Cli.node.wallet.getAccounts().get(1).account.getAddressString();
		String accountToString = Cli.node.wallet.getAccounts().get(0).account.getAddressString();
		String ownerString = Cli.node.wallet.getAccounts().get(1).getOwnerString();
		
		// test transactions
		System.out.println("TEST TRANSACTIONS");	
		System.out.println("Create:");	
		String[] params2 = {"-createTransaction", "-state", "-address", accountString, "-value", "'hello world' ", "-sign", ownerString};
		Cli.main(params2);	
		System.out.println("");	
		System.out.println("Transfer:");	
		String[] params3 = {"-createTransaction", "-transfer", "-from", accountString, "-to", accountToString,"-amount", "22", "-sign", ownerString};
		Cli.main(params3);	
		System.out.println("");	
		
		// test miner
		System.out.println("MINER ONE STEP");
		String[] paramsm = {"-runMinerOne"};
		Cli.main(paramsm);	
		System.out.println("");	

		// test account mined
		System.out.println("Account value");
		String[] paramsd = {"-getAccountData", "-account", accountString};
		Cli.main(paramsd);	
		System.out.println("");	

		// test account mined
		System.out.println("Account value");
		String[] paramsb = {"-getAccountBalance", "-account", accountString};
		Cli.main(paramsb);	
		System.out.println("");	
		
		System.out.println("STOPPING NETWORK");
		String[] paramsstop = {"-stopNetwork"};
		Cli.main(paramsstop);	
		System.out.println("");	

	}		
}
