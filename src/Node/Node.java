package Node;

import Block.*;
import Chain.*;
import Miner.*;
import Transaction.*;
import Utils.Logger;
import Utils.Severity;
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
			if (!(this.network.selfPeer.peerHost.equals(pr.peerHost) &&
					 this.network.selfPeer.peerPort == pr.peerPort)){
				pr.boradcastTransaction(tr);
			}
		}		
	}
	
	// broadcasting a block to the network
	public void broadcastBlock(Block _block) {	
		// gossiping the block on the network
		for(Peer pr: this.network.peers) {
			if (!(this.network.selfPeer.peerHost.equals(pr.peerHost)) &&
				 (this.network.selfPeer.peerPort == pr.peerPort)){
				pr.broadcastBlock(_block);
			}
		}				
	}
	
	// starting the miner - only one round implementation
	public ExtendedBlock startMinerOneRound() {
		Block proposedBlock = miner.mineNextBlock(this.blockchain.getTopBlock().internBlock, this.pool);
		ExtendedBlock exBlock = this.blockchain.addBlock(proposedBlock);
		// syncing the state is should not necessarily be here, as we still do not know if we won the race
		wallet.syncAccounts();
		this.broadcastBlock(proposedBlock);
		return exBlock;
	}
	
	// syncing the blockchain 
	public void syncBlockchain() {
		int localBlockchainHeight = this.blockchain.getBlockchinHeight();
		int remoteBlockchainHeight = -1;
		for(Peer peer: this.network.peers){
			int remotePeerHeight = peer.getMaxBlockHeight();
			if (remotePeerHeight >  remoteBlockchainHeight)
				remoteBlockchainHeight = remotePeerHeight;			
		}
		if (localBlockchainHeight < remoteBlockchainHeight){
			this.blockchain.isSynced = false;
			Logger.Log("Syncronisation needed", Severity.INFO);
			Logger.Log("Local blockheight : " + localBlockchainHeight);
			Logger.Log("Remote blockheight : " + remoteBlockchainHeight) ;
		}
		// Starting Syncronization
		
		
		
		this.blockchain.isSynced = true;
	}
}
