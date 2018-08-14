package Test;

import java.security.*;

import CLI.*;
import Crypto.*;
import Node.*;
import State.*;
import Utils.*;

public class TestCLI {

	public static void main(String[] args) {
		CliArgs cliArgs = new CliArgs(args);
		Logger.Log("Start");
		
		// starting security provider, not sure if this is the right place
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		
		// TODO Auto-generated method stub
		
		// starting the network

		if (cliArgs.switchPresent("-nodeOne")){
			
			Logger.Log("Starting node one");
		
			Logger.Log("STARTING NETWORK");
			String[] paramss = {"-startNetwork", "-port", "8425"};
			Cli.main(paramss);	
			Logger.Log("");	
						
			// test new account generation
			Logger.Log("TEST ACCOUNT GENERATION");
			String[] params = {"-createAccount"};
			Cli.main(params);	
			System.out.println("");	
	
			//GENESIS BLOCK
			Logger.Log("GENESIS BLOCK");
			String[] paramsg = {"-createGenesisBlock"};
			Cli.main(paramsg);	
			Logger.Log("genesis block created");
			Logger.Log("");
			
			String accountString = Cli.node.wallet.getAccounts().get(1).account.getAddressString();
			String accountToString = Cli.node.wallet.getAccounts().get(0).account.getAddressString();
			String ownerString = Cli.node.wallet.getAccounts().get(1).getOwnerString();
			
			// test transactions
			Logger.Log("TEST TRANSACTIONS");	
			Logger.Log("Create:");	
			String[] params2 = {"-createTransaction", "-state", "-address", accountString, "-value", "'hello world' ", "-sign", ownerString};
			Cli.main(params2);	
			System.out.println("");	
			Logger.Log("Transfer:");	
			String[] params3 = {"-createTransaction", "-transfer", "-from", accountString, "-to", accountToString,"-amount", "22", "-sign", ownerString};
			Cli.main(params3);	
			Logger.Log("");	
			
			// test miner
			Logger.Log("MINER ONE STEP");
			String[] paramsm = {"-runMinerOne"};
			Cli.main(paramsm);	
			Logger.Log("");	
	
			// test account mined
			System.out.println("Account value");
			String[] paramsd = {"-getAccountData", "-account", accountString};
			Cli.main(paramsd);	
			Logger.Log("");	
	
			// test account mined
			Logger.Log("Account value");
			String[] paramsb = {"-getAccountBalance", "-account", accountString};
			Cli.main(paramsb);	
			Logger.Log("");	
			
/*			// stopping the network
			Logger.Log("STOPPING NETWORK");
			String[] paramsstop = {"-stopNetwork"};
			Cli.main(paramsstop);	
			Logger.Log("");	 */
		}
		else if (cliArgs.switchPresent("-nodeTwo")){

			Logger.Log("Starting node two");
			
			Logger.Log("STARTING NETWORK");
			String[] paramss = {"-startNetwork", "-port", "8426"};
			Cli.main(paramss);	
			Logger.Log("");	
			
			Logger.Log("CONNECT PEER");
			String[] paramsp = {"-connectPeer", "-peerAddress", "localhost", "-peerPort", "8425"};
			Cli.main(paramsp);
 			for(Peer peer: Cli.node.network.peers) 
 				Logger.Log("Peer Info, host : " + peer.peerHost + " port : " + peer.peerPort);		
			Logger.Log("");		
			
			// test new account generation
			Logger.Log("TEST ACCOUNT GENERATION");
			String[] params = {"-createAccount"};
			Cli.main(params);	
			Logger.Log("");			
		}
	}


}		
