package Test;

import java.security.*;
import java.util.ArrayList;

import CLI.*;
import Chain.Blockchain;
import Chain.ExtendedBlock;
import Crypto.*;
import Node.*;
import SmartContract.SimpleRule;
import State.*;
import Utils.*;

public class TestCLI {

	public static void main(String[] args) {
		CliArgs cliArgs = new CliArgs(args);
		LoggerConsole.Log("Start");
		
		// starting security provider, not sure if this is the right place
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		
		// TODO Auto-generated method stub
		
		// starting the network

		if (cliArgs.switchPresent("-nodeOne")){
			
			LoggerConsole.Log("Starting node one");
		
			LoggerConsole.Log("STARTING NETWORK");
			String[] paramss = {"-startNetwork", "-port", "8425"};
			Cli.main(paramss);	
			LoggerConsole.Log("");	
						
			// test new account generation
			LoggerConsole.Log("TEST ACCOUNT GENERATION");
			String[] params = {"-createAccount"};
			Cli.main(params);	
			System.out.println("");	
	
			//GENESIS BLOCK
			LoggerConsole.Log("GENESIS BLOCK");
			String[] paramsg = {"-createGenesisBlock"};
			Cli.main(paramsg);	
			LoggerConsole.Log("genesis block created");
			LoggerConsole.Log("");
			
			String accountString = Cli.node.wallet.getAccounts().get(1).account.getAddressString();
			String accountToString = Cli.node.wallet.getAccounts().get(0).account.getAddressString();
			String ownerString = Cli.node.wallet.getAccounts().get(1).getOwnerString();
			
			// test transactions
			LoggerConsole.Log("TEST TRANSACTION DATA");	
			LoggerConsole.Log("Create:");	
			String[] params2 = {"-createTransaction", "-state", "-address", accountString, "-value", "'hello world' ", "-sign", ownerString};
			Cli.main(params2);	
			System.out.println("");	
			
			// mine one transaction
			LoggerConsole.Log("MINER ONE STEP");
			String[] paramsm = {"-runMinerOne"};
			Cli.main(paramsm);	
			LoggerConsole.Log("");	

			LoggerConsole.Log("TEST TRANSACTION DATA");	
			LoggerConsole.Log("Transfer:");	
			String[] params3 = {"-createTransaction", "-transfer", "-from", accountString, "-to", accountToString,"-amount", "22", "-sign", ownerString};
			Cli.main(params3);	
			LoggerConsole.Log("");	

			// mine second transaction
			LoggerConsole.Log("MINER ONE MORE STEP");
			String[] paramsm1 = {"-runMinerOne"};
			Cli.main(paramsm1);	
			LoggerConsole.Log("");	
			
			// test account mined
			System.out.println("Account value");
			String[] paramsd = {"-getAccountData", "-account", accountString};
			Cli.main(paramsd);	
			LoggerConsole.Log("");	
	
			// test account mined
			LoggerConsole.Log("Account value");
			String[] paramsb = {"-getAccountBalance", "-account", accountString};
			Cli.main(paramsb);	
			LoggerConsole.Log("");	
			
			// showing result on desktop wallet
			LoggerConsole.Log("START WEB UI");
			String[] paramui = {"-startUI"};
			Cli.main(paramui);
			LoggerConsole.Log("");		

			
/*			// stopping the network
			Logger.Log("STOPPING NETWORK");
			String[] paramsstop = {"-stopNetwork"};
			Cli.main(paramsstop);	
			Logger.Log("");	 */
		}
		else if (cliArgs.switchPresent("-nodeTwo")){

			LoggerConsole.Log("Starting node two");
			
			LoggerConsole.Log("STARTING NETWORK");
			String[] paramss = {"-startNetwork", "-port", "8426"};
			Cli.main(paramss);	
			LoggerConsole.Log("");	
			
			LoggerConsole.Log("CONNECT PEER");
			String[] paramsp = {"-connectPeer", "-peerAddress", "localhost", "-peerPort", "8425"};
			Cli.main(paramsp);
 			for(Peer peer: Cli.node.network.peers) 
 				LoggerConsole.Log("Peer Info, host : " + peer.peerHost + " port : " + peer.peerPort);		
			LoggerConsole.Log("");		
						
			LoggerConsole.Log("SYNC BLOCKCHAIN");
			String[] paramssync = {"-syncBlockchain"};
			Cli.main(paramssync);
			LoggerConsole.Log("");		
			
			// test new account generation
			LoggerConsole.Log("TEST ACCOUNT GENERATION");
			String[] params = {"-createAccount"};
			Cli.main(params);	
			LoggerConsole.Log("");			
			
			String accountStringNewest = Cli.node.wallet.getAccounts().get(0).account.getAddressString();
			
			// test transactions
			LoggerConsole.Log("TEST TRANSACTION DATA");	
			LoggerConsole.Log("Create:");	
			String[] params2 = {"-createTransaction", "-state", "-address", accountStringNewest, "-value", "'hello blockchain'"};
			Cli.main(params2);	
			System.out.println("");
			
			// mine second transaction
			LoggerConsole.Log("MINER ONE MORE STEP");
			String[] paramsm1 = {"-runMinerOne"};
			Cli.main(paramsm1);	
			LoggerConsole.Log("");	
			
			// testing rules
			LoggerConsole.Log("CREATE RULE TRANSACTION");
			String ruleString = "IF " + accountStringNewest + " CONTAINS hello THEN " +  accountStringNewest + " Hallo";
			String[] paramsr = {"-createTransaction", "-rule", ruleString};
			Cli.main(paramsr);	
			LoggerConsole.Log("");	
			
			// mine third transaction
			LoggerConsole.Log("MINER ONE MORE STEP");
			String[] paramsm3 = {"-runMinerOne"};
			Cli.main(paramsm3);	
			LoggerConsole.Log("");	

			LoggerConsole.Log("Account Data : " + Cli.node.wallet.getAccounts().get(0).account.accountData);	
			
		}
	}
}		
