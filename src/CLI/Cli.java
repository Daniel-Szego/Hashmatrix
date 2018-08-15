package CLI;
import java.security.*;

import Block.*;
import Chain.*;
import Crypto.CryptoUtil;
import Miner.*;
import Node.*;
import State.*;
import Transaction.*;
import Wallet.*;
import Utils.*;

// command line interface
public class Cli {

	public static Node node;

	public static void main(String[] args) {
		CliArgs cliArgs = new CliArgs(args);
				
		// starting security provider, not sure if this is the right place
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		
		// creating a test node, but only if still not exisit 
		if (node == null)
			node = new Node();
		
		// starting network interface
		if (cliArgs.switchPresent("-startNetwork")){
			int port = 0;
			port = Integer.parseInt(cliArgs.switchValue("-port"));
			
			node.network.startNetwork(port);		
			Logger.Log("Network has started");
		}
		else if (cliArgs.switchPresent("-connectPeer")) {			
			String peerAddress = cliArgs.switchValue("-peerAddress");
			String peerPort = cliArgs.switchValue("-peerPort");	
			int peerInt = Integer.parseInt(peerPort);
			node.network.syncPeers(peerAddress, peerInt);
			Logger.Log("Peer sync has started (one hop)");
		}
		// stopping network interface
		else if (cliArgs.switchPresent("-stopNetwork")){
			node.network.stopNetwork();		
			Logger.Log("Network is stopping");
		}
		// creating account - only memory
		else if (cliArgs.switchPresent("-createAccount")) {
			AccountWallet account = node.wallet.createNewAccount();
			
			Logger.Log("Account has been generated and added to the default wallet");
			Logger.Log("Address : " + account.account.getAddressString());
			Logger.Log("Owner priv key : " + account.getOwnerString());					
		}
		
		// creating a transaction
		else if (cliArgs.switchPresent("-createTransaction")) {
			if (cliArgs.switchPresent("-state")) {
				String address = cliArgs.switchValue("-address");
				String newValue = cliArgs.switchValue("-value");
				String privKey = cliArgs.switchValue("-sign");
				AccountWallet accountWallet = node.wallet.getAccountbyPublicKey(address);				
				StateDataTransaction tr = node.wallet.createDataTransaction(accountWallet, newValue);
				
				Logger.Log("State transaction has been created and broadcasted to the network");				
				Logger.Log("Transaction Id : " + tr.getTransctionId());
				Logger.Log("Address : " + tr.GetAddressString());
				Logger.Log("Value : " + tr.newValue);	
			}
			else if (cliArgs.switchPresent("-transfer")){
				String fromAddress = cliArgs.switchValue("-from");
				String toAddress = cliArgs.switchValue("-to");				
				String amount = cliArgs.switchValue("-amount");
				Float amountFloat = Float.parseFloat(amount);
				String privKey = cliArgs.switchValue("-sign");

				AccountWallet accountWallet = node.wallet.getAccountbyPublicKey(fromAddress);				
				StateTransferTransaction tr = node.wallet.createTransferTransaction(accountWallet, toAddress, amountFloat);

				Logger.Log("State transaction has been created and broadcasted to the network");
				Logger.Log("Transaction Id : " + tr.getTransctionId());
				Logger.Log("From Address : " +  CryptoUtil.getStringFromKey(tr.fromAddress));
				Logger.Log("To Address : " +  CryptoUtil.getStringFromKey(tr.toAddress));				
				Logger.Log("Amount : " + tr.amount);					
			}
			else {
				Logger.Log("invalid arguments transaction", Severity.CRITICAL);
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
			accountWallet.account.accountBalance = 100;

			System.out.println("Genesis account has been generated");
			System.out.println("Address : " + accountWallet.account.getAddressString());
			System.out.println("Owner priv key : " + accountWallet.getOwnerString());		
			
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
			Block genesisBlock = new Block();
			genesisBlock.accounts.add(accountWallet.account);			
			genesisBlock.matrix.add(hashLink);
			genesisBlock.calculateStateRoot();
			genesisBlock.calculateTransactionRoot();
			genesisBlock.setBlockId();
			
			// add genesis block to the blockchain
			node.blockchain.addGenesisBlock(genesisBlock);
			Logger.Log("Genesis Block has been created");
			Logger.Log("Genesis Block ID : " + genesisBlock.blockId);
		
//			Logger.LogObject(genesisBlock);
		}
		else if (cliArgs.switchPresent("-runMinerOne")) {
			node.startMinerOneRound();
			Logger.Log("One block has been mined succesfully");		
			Logger.Log("Block hash : " + node.blockchain.getTopBlock().internBlock.blockId);	
			Logger.Log("Nr of transactions : " + node.blockchain.getTopBlock().internBlock.transactions.size());			
			Logger.Log("Nr of accounts : " + node.blockchain.getTopBlock().internBlock.accounts.size());						
			Logger.Log("Blockchain height : " + node.blockchain.getBlockchinHeight());						
		}
		else if (cliArgs.switchPresent("-getAccountData")) {
			String acountAddress = cliArgs.switchValue("-account");
			String data = node.explorer.getAccountData(acountAddress);
			Logger.Log("Account Data : " + data);
		}
		else if (cliArgs.switchPresent("-getAccountBalance")) {
			String acountAddress = cliArgs.switchValue("-account");
			float value = node.explorer.getAccountBalance(acountAddress);
			Logger.Log("Account Balance : " + value);
		}
		else if (cliArgs.switchPresent("-syncBlockchain")) {
			node.syncBlockchain();		
		}
	}
}
