package Explorer;

import Crypto.*;
import Chain.*;

import java.security.*;

import Block.*;
import State.*;
import Wallet.*;
import Node.*;
import ServiceBus.*;
import Transaction.*;

// explorer functionalities for the blockchain
public class Explorer implements ServiceListenerInterface {
	
	public final NodeServiceBase node;
	public ExplorerUI ui;
	
	public Explorer(NodeServiceBase _node) {
		this.node = _node;
	}
	
	// getting account data by public key
	public String getAccountData(PublicKey _publicKey) {
		for(AccountBase account: this.node.blockchain.getTopStableBlock().internBlock.accounts){
			if(account.getAddress().equals(_publicKey))
				return account.getAddress();
		}
		// account not found -> log, or error 
		return null;
	}
	
	// getting account data by public key string
	public String getAccountData(String _publicKeyString) {
		return this.getAccountData(CryptoUtil.getPublicKeyFromString(_publicKeyString));
	}

	// getting account balance by public key
	public float getAccountBalance(PublicKey _publicKey) {
		for(AccountBase account: this.node.blockchain.getTopStableBlock().internBlock.accounts){
			if(account.getAddress().equals(_publicKey))
				return account.getNonce();
		}
		// account not found -> log, or error 
		return -1;
	}
	
	// getting account balance by public key string
	public float getAccountBalance(String _publicKeyString) {
		return this.getAccountBalance(CryptoUtil.getPublicKeyFromString(_publicKeyString));
	}

	// getting info based on the transaction id 
	public TransactionInfo getTransactionInfo(String transactionId){
		TransactionInfo trInfo = new TransactionInfo();
		for (ExtendedBlock block: node.blockchain.getBlockchain()) {
			for(StateTransaction tr: block.internBlock.transactions){
				trInfo.transactionId = tr.getTransctionId();
				trInfo.nonce = tr.getNonce();
				trInfo.signature = tr.getSignature();
				
				if (tr instanceof StateDataTransaction) {
					trInfo.fromAddress = ((StateDataTransaction)tr).GetAddressString();
					trInfo.newValue = ((StateDataTransaction)tr).newValue;										
				}
				else if (tr instanceof StateTransferTransaction){
					trInfo.fromAddress = CryptoUtil.getStringFromKey(((StateTransferTransaction)tr).fromAddress);
					trInfo.toAddress = CryptoUtil.getStringFromKey(((StateTransferTransaction)tr).toAddress);
					trInfo.amount = ((StateTransferTransaction)tr).amount;															
				}
				else {
					// error unknown transaction in the chain
				}
			}
		}	
		return trInfo;
	}
	
	// EVENT LISTENERS
	public  void EventRaised (ServiceEvent event) {
		if (event instanceof ServiceEventBlockReceived) {
			ui.addBlock(((ServiceEventBlockReceived)event).block);
		}
		else if (event instanceof ServiceEventTransactionReceived) {
			ui.addTransaction(((ServiceEventTransactionReceived)event).transaction);
		}
	} 
}
