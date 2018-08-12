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
	// broadcasting a transaction to the network -> call from CLI
	public void broadcastTransaction(StateTransaction tr) {
		// adding only to the pool
		pool.addTransaction(tr);
		
		// gossiping the transaction on the network
		for(Peer pr: this.network.peers) {
			pr.boradcastTransaction(tr);
		}		
	}
	
	// broadcasting a block to the network
	public void broadcastBlock(Block _block) {
		// adding to the blockchain
		blockchain.addBlock(_block);
	
		// gossiping the block on the network
		for(Peer pr: this.network.peers) {
			pr.broadcastBlock(_block);
		}				
	}
	
	// starting the miner - only one round implementation
	public void startMiner() {
		Block proposedBlock = miner.mineNextBlock(this.blockchain.getLatestBlock(), this.pool);
		this.broadcastBlock(proposedBlock);
	}
}
