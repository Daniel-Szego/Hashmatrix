package CLI;
import java.security.*;

import Block.*;
import Chain.*;
import Crypto.*;
import Explorer.*;
import Miner.*;
import Node.*;
import State.*;
import Transaction.*;
import Wallet.*;
import Utils.*;

// command line interface
public class Cli {

	public static NodeBase node;

	public static void main(String[] args) {
		CliArgs cliArgs = new CliArgs(args);
				
		// starting security provider, not sure if this is the right place
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		
		// creating a test node, but only if still not exisit 
		if (node == null)
			node = new NodeBase();
		
		// starting network interface
		if (cliArgs.switchPresent("-startNetwork")){
			int port = 0;
			port = Integer.parseInt(cliArgs.switchValue("-port"));
			
			node.network.startNetwork(port);		
		 	node.serviceBus.addEvent("Network has started");
		}
		else if (cliArgs.switchPresent("-connectPeer")) {			
			String peerAddress = cliArgs.switchValue("-peerAddress");
			String peerPort = cliArgs.switchValue("-peerPort");	
			int peerInt = Integer.parseInt(peerPort);
			node.network.syncPeers(peerAddress, peerInt);
			node.serviceBus.addEvent("Peer sync has started (one hop)");
		}
		// stopping network interface
		else if (cliArgs.switchPresent("-stopNetwork")){
			node.network.stopNetwork();		
			node.serviceBus.addEvent("Network is stopping");
		}
		// creating account - only memory
		else if (cliArgs.switchPresent("-createAccount")) {
			AccountWallet account = node.wallet.createNewAccount();
			
			node.serviceBus.addEvent("Account has been generated and added to the default wallet");
			node.serviceBus.addEvent("Address : " + account.account.getAddress());
			node.serviceBus.addEvent("Owner priv key : " + account.getOwnerString());					
		}
		
		// creating a transaction
		else if (cliArgs.switchPresent("-createTransaction")) {
			if (cliArgs.switchPresent("-state")) {
				String address = cliArgs.switchValue("-address");
				String newValue = cliArgs.switchValue("-value");
				String privKey = cliArgs.switchValue("-sign");
				AccountWallet accountWallet = node.wallet.getAccountbyPublicKey(address);				
				StateDataTransaction tr = node.wallet.createDataTransaction(accountWallet, newValue);
				
				node.serviceBus.addEvent("State transaction has been created and broadcasted to the network");				
				node.serviceBus.addEvent("Transaction Id : " + tr.getTransctionId());
				node.serviceBus.addEvent("Address : " + tr.GetAddressString());
				node.serviceBus.addEvent("Value : " + tr.newValue);	
			}
			else if (cliArgs.switchPresent("-transfer")){
				String fromAddress = cliArgs.switchValue("-from");
				String toAddress = cliArgs.switchValue("-to");				
				String amount = cliArgs.switchValue("-amount");
				Float amountFloat = Float.parseFloat(amount);
				String privKey = cliArgs.switchValue("-sign");

				AccountWallet accountWallet = node.wallet.getAccountbyPublicKey(fromAddress);				
				StateTransferTransaction tr = node.wallet.createTransferTransaction(accountWallet, toAddress, amountFloat);

				node.serviceBus.addEvent("State transaction has been created and broadcasted to the network");
				node.serviceBus.addEvent("Transaction Id : " + tr.getTransctionId());
				node.serviceBus.addEvent("From Address : " +  CryptoUtil.getStringFromKey(tr.fromAddress));
				node.serviceBus.addEvent("To Address : " +  CryptoUtil.getStringFromKey(tr.toAddress));				
				node.serviceBus.addEvent("Amount : " + tr.amount);					
			}
			else if (cliArgs.switchPresent("-rule")){
				String ruleString = cliArgs.switchValue("-rule");
				StateRuleTransaction tr = node.wallet.createRuleTransaction(ruleString);

				node.serviceBus.addEvent("Rule transaction has been created and broadcasted to the network");
				node.serviceBus.addEvent("Transaction Id : " + tr.getTransctionId());
			}			
			else {
				LoggerConsole.Log("invalid arguments transaction", Severity.CRITICAL);
			}
		}
		
		// signing a transaction
		else if (cliArgs.switchPresent("-signTransaction")) {
				// to be implemented -> first version is only with one functionality: create, sign, voradcast
				String trId = cliArgs.switchValue("-transactionId");
				String privateKey = cliArgs.switchValue("-privateKeyId");				
		}
		else if (cliArgs.switchPresent("-createGenesisBlock")) {
			// creating a genesis account
			AccountWallet accountWallet = node.wallet.createNewAccount();

			node.serviceBus.addEvent("Genesis account has been generated");
			node.serviceBus.addEvent("Address : " + accountWallet.account.getAddress());
			node.serviceBus.addEvent("Owner priv key : " + accountWallet.getOwnerString());		
			
			// HashLink
			String hashOne = "00000000000000000000000000000000"; 
			String hashTwo =  "00000000000000000000000000000000";
			int resetPolicy = 2;
			int resetCount= 0;
			boolean lastResetedHash = true;
			boolean singleHash = false;
			int difficulty = 1;
			
			HashLink hashLink = new HashLink(hashOne, hashTwo, resetPolicy, resetCount,lastResetedHash, singleHash, difficulty);
			
			// genesis block
			BlockBase genesisBlock = new BlockBase();
			genesisBlock.accounts.add(accountWallet.account);			
			genesisBlock.matrix.add(hashLink);
			genesisBlock.calculateStateRoot();
			genesisBlock.calculateTransactionRoot();
			genesisBlock.setBlockId();
			
			// add genesis block to the blockchain
			node.blockchain.addGenesisBlock(genesisBlock);
			node.serviceBus.addEvent("Genesis Block has been created");
			node.serviceBus.addEvent("Genesis Block ID : " + genesisBlock.blockId);
		
//			Logger.LogObject(genesisBlock);
		}
		else if (cliArgs.switchPresent("-runMinerOne")) {
			node.startMinerOneRound();
			node.serviceBus.addEvent("One block has been mined succesfully");		
			node.serviceBus.addEvent("Block hash : " + node.blockchain.getTopBlock().internBlock.blockId);	
			node.serviceBus.addEvent("Nr of transactions : " + node.blockchain.getTopBlock().internBlock.transactions.size());			
			node.serviceBus.addEvent("Nr of accounts : " + node.blockchain.getTopBlock().internBlock.accounts.size());						
			node.serviceBus.addEvent("Blockchain height : " + node.blockchain.getBlockchinHeight());						
		}
		else if (cliArgs.switchPresent("-getAccountData")) {
			String acountAddress = cliArgs.switchValue("-account");
			String data = node.explorer.getAccountData(acountAddress);
			node.serviceBus.addEvent("Account Data : " + data);
		}
		else if (cliArgs.switchPresent("-getAccountBalance")) {
			String acountAddress = cliArgs.switchValue("-account");
			float value = node.explorer.getAccountBalance(acountAddress);
			node.serviceBus.addEvent("Account Balance : " + value);
		}
		else if (cliArgs.switchPresent("-syncBlockchain")) {
			node.syncBlockchain();		
		}
		else if (cliArgs.switchPresent("-startUI")) {
			node.walletUI = new WalletUI(node);	
			node.explorer.ui = new ExplorerUI(node);
		}
	}
}
