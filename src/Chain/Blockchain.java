package Chain;

import Block.*;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.*;

import Block.Block;
import Node.*;
import Transaction.*;
import Wallet.Wallet;

import java.security.*;
import org.bouncycastle.*;


public class Blockchain {
	
	public final Node node;
	
	public Blockchain(Node _node) {
		this.node = _node;
	}
	
	// simple implementation: future: forking has to be considrered
	public static ArrayList<Block> blockchain = new ArrayList<Block>();
	
	public void addBlock(Block _block) {
		blockchain.add(_block);
	}
	
	// experimental implementation, future: forking has to be considered !!!
	public Block getLatestBlock() {
		if (blockchain.size() == 0)
			return null;
		else 
			return blockchain.get(blockchain.size() - 1);			
	}
	

	// latest block is not necessarily the same as latest stable block
	// forking question ?
	public Block getLatestStableBlock() {
		return getLatestBlock();
	}

	public static void run() {	
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		
/*		//Create wallets:
		walletA = new Wallet();
		walletB = new Wallet();		
		Wallet coinbase = new Wallet();	
		
		// Testing blockchain
		HashPointer h1 = new HashPointer("0", 2, 0);
		HashPointer h2 = new HashPointer("0", 2, 1);
		
		genesisTransaction = new Transaction(coinbase.publicKey, walletA.publicKey, 100f, null);
		genesisTransaction.generateSignature(coinbase.privateKey);	 //manually sign the genesis transaction	
		genesisTransaction.transactionId = "0"; //manually set the transaction id
		genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.reciepient, genesisTransaction.value, genesisTransaction.transactionId)); //manually add the Transactions Output
		UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); //its important to store our first transaction in the UTXOs list.

		System.out.println("Creating and Mining Genesis block... ");
		
		Block genesis = new Block(h1,h2);
		genesis.addTransaction(genesisTransaction);
		addBlock(genesis);
		
		Block block1 = new Block(genesis.hashOne, genesis.hashTwo);
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("\nWalletA is Attempting to send funds (40) to WalletB...");
		block1.addTransaction(walletA.sendFunds(walletB.publicKey, 40f));
		addBlock(block1);
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());
		
		Block block2 = new Block(block1.hashOne,block1.hashTwo);
		System.out.println("\nWalletA Attempting to send more funds (1000) than it has...");
		block2.addTransaction(walletA.sendFunds(walletB.publicKey, 1000f));
		addBlock(block2);
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());
		
		Block block3 = new Block(block2.hashOne,block2.hashTwo);
		System.out.println("\nWalletB is Attempting to send funds (20) to WalletA...");
		block3.addTransaction(walletB.sendFunds( walletA.publicKey, 20));
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());
		
		isChainValid();	
	*/
	}
	

	public static Boolean isChainValid() {
		/*
		Block currentBlock; 
		Block previousBlock;
		
		//loop through blockchain to check hashes:
		for(int i=1; i < blockchain.size(); i++) {
			currentBlock = blockchain.get(i);
			previousBlock = blockchain.get(i-1);
			
			String hashTarget = new String(new char[difficulty]).replace('\0', '0');
			HashMap<String,TransactionOutput> tempUTXOs = new HashMap<String,TransactionOutput>(); //a temporary working list of unspent transactions at a given block state.
			tempUTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));

			
			//compare registered hash and calculated hash:
			if(!currentBlock.hashOne.blockHash.equals(currentBlock.calculateHash1()) ){
				System.out.println("Current Hashes One not equal " + i);			
				return false;
			}
			if(!currentBlock.hashTwo.blockHash.equals(currentBlock.calculateHash2()) ){
				System.out.println("Current Hashes Two not equal " + i);			
				return false;
			}
	
			//compare previous hash and registered previous hash
			if(!previousBlock.hashOne.blockHash.equals(currentBlock.previousHashOne.blockHash) ) {
				System.out.println("Previous Hashes One not equal " + i);
				return false;
			}
			//compare previous hash and registered previous hash
			if(!previousBlock.hashTwo.blockHash.equals(currentBlock.previousHashTwo.blockHash) ) {
				System.out.println("Previous Hashes Two not equal " + i);
				return false;
			}
			
			
			//loop thru blockchains transactions:
				TransactionOutput tempOutput;
				for(int t=0; t <currentBlock.transactions.size(); t++) {
					Transaction currentTransaction = currentBlock.transactions.get(t);
					
					if(!currentTransaction.verifiySignature()) {
						System.out.println("#Signature on Transaction(" + t + ") is Invalid");
						return false; 
					}
					if(currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
						System.out.println("#Inputs are note equal to outputs on Transaction(" + t + ")");
						return false; 
					}
					
					for(TransactionInput input: currentTransaction.inputs) {	
						tempOutput = tempUTXOs.get(input.transactionOutputId);
						
						if(tempOutput == null) {
							System.out.println("#Referenced input on Transaction(" + t + ") is Missing");
							return false;
						}
						
						if(input.UTXO.value != tempOutput.value) {
							System.out.println("#Referenced input Transaction(" + t + ") value is Invalid");
							return false;
						}
						
						tempUTXOs.remove(input.transactionOutputId);
					}
					
					for(TransactionOutput output: currentTransaction.outputs) {
						tempUTXOs.put(output.id, output);
					}
					
					if( currentTransaction.outputs.get(0).reciepient != currentTransaction.reciepient) {
						System.out.println("#Transaction(" + t + ") output reciepient is not who it should be");
						return false;
					}
					if( currentTransaction.outputs.get(1).reciepient != currentTransaction.sender) {
						System.out.println("#Transaction(" + t + ") output 'change' is not sender.");
						return false;
					}			
				}
			
		} */
		return true;
	}
	
}