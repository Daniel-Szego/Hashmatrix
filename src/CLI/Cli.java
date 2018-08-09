package CLI;
import java.security.*;

import Block.*;
import Chain.*;
import Crypto.CryptoUtil;
import Node.*;
import State.*;
import Transaction.*;
import Wallet.*;

// command line interface
public class Cli {

	public static Node node;

	public static void main(String[] args) {
		CliArgs cliArgs = new CliArgs(args);
				
		// starting security provider, not sure if this is the right place
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		
		// creating a test node
		node = new Node();
		
		// creating account - only memory
		if (cliArgs.switchPresent("-createAccount")) {
			AccountWallet account = node.wallet.createNewAccount();
			
			System.out.println("Account has been generated and added to the default wallet");
			System.out.println("Address : " + account.account.getAddressString());
			System.out.println("Owner priv key : " + account.getOwnerString());		
		}
		
		// creating a transaction
		else if (cliArgs.switchPresent("-createTransaction")) {
			if (cliArgs.switchPresent("-state")) {
				String address = cliArgs.switchValue("-address");
				String newValue = cliArgs.switchValue("-value");
				StateDataTransaction tr = new StateDataTransaction(CryptoUtil.getPublicKeyFromString(address),newValue);
				System.out.println("State transaction has been created");				
				System.out.println("Transaction Id : " + tr.getTransctionId());
				System.out.println("Address : " + tr.GetAddressString());
				System.out.println("Value : " + tr.newValue);				
			}
			else if (cliArgs.switchPresent("-transfer")){
				String fromAddress = cliArgs.switchValue("-from");
				String toAddress = cliArgs.switchValue("-to");				
				String amount = cliArgs.switchValue("-amount");
				Float amountFloat = Float.parseFloat(amount);
				StateTransferTransaction tr = new StateTransferTransaction(CryptoUtil.getPublicKeyFromString(fromAddress),CryptoUtil.getPublicKeyFromString(toAddress), amountFloat);
				System.out.println("State transaction has been created");
				System.out.println("Transaction Id : " + tr.getTransctionId());
				System.out.println("From Address : " +  CryptoUtil.getStringFromKey(tr.fromAddress));
				System.out.println("To Address : " +  CryptoUtil.getStringFromKey(tr.toAddress));				
				System.out.println("Amount : " + tr.amount);					
			}
			else {
				throw new RuntimeException(new Exception("invalid arguments transaction"));
			}
		}
		
		// signing a transaction
		else if (cliArgs.switchPresent("-signTransaction")) {
				String trId = cliArgs.switchValue("-transactionId");
				String privateKey = cliArgs.switchValue("-privateKeyId");				
		}
		else if (cliArgs.switchPresent("-createGenesisBlock")) {
			// creating a genesis account
			AccountWallet accountWallet = node.wallet.createNewAccount();

			System.out.println("Genesis account has been generated");
			System.out.println("Address : " + accountWallet.account.getAddressString());
			System.out.println("Owner priv key : " + accountWallet.getOwnerString());		
			
			// HashLink
			String hashOne = "0"; 
			String hashTwo =  "0";
			int resetPolicy = 2;
			int resetCount= 0;
			boolean lastResetedHash = true;
			boolean singleHash = false;
			int difficulty = 1;
			
			HashLink hashLink = new HashLink(hashOne, hashTwo, resetPolicy, resetCount,lastResetedHash, singleHash, difficulty);
			
			// genesis block
			Block genesisBlock = new Block(null);
			genesisBlock.accounts.add(accountWallet.account);			
			genesisBlock.matrix.add(hashLink);
			genesisBlock.calculateStateRoot();
			genesisBlock.calculateTransactionRoot();
			
			// add genesis block to the blockchain
			node.blockchain.addBlock(genesisBlock);
			
		}
		else if (cliArgs.switchPresent("-runMinerOne")) {
			
			
			
		}

	}
}
