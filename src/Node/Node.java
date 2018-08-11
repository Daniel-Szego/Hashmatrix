package Node;

import Block.*;
import Chain.*;
import Miner.*;
import Transaction.*;
import Wallet.*;
import Explorer.*;

// initial version of the node
public class Node {
	
	public final RandomWallet wallet;
	public final MinerPOW miner ;
	public final Blockchain blockchain;
	public final TransactionPool pool;
	public final Explorer explorer;
	public final Network network;
	
	// starting the node - test code, no persistance or communiction
	public Node() {
		wallet = new RandomWallet(this);
		miner = new MinerPOW(this);
		blockchain = new Blockchain(this);
		pool = new TransactionPool(this);
		explorer = new Explorer(this);
		network = new Network(this);
	}
	
	// BROADCAST TRANSACTIONS	
	// broadcasting a transaction to the network
	public void broadcastTransaction(StateTransaction tr) {
		// real implementation
		
		
		// Test implementation, adding only to the pool
		pool.addTransaction(tr);
	}
	
	// broadcasting a block to the network
	public void broadcastBlock(Block _block) {
		blockchain.addBlock(_block);
	}
	
	// starting the miner - only one round implementation
	public void startMiner() {
		Block proposedBlock = miner.mineNextBlock(this.blockchain.getLatestBlock(), this.pool);
		this.broadcastBlock(proposedBlock);
	}
}
