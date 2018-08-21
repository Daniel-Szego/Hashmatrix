package Node;

import java.util.ArrayList;

import Block.*;
import Chain.*;
import Miner.*;
import ServiceBus.*;
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
	public WalletUI walletUI; //this parameter is optional
	public final ServiceBus serviceBus;
	public final Logger logger;
	
	// starting the node - test code, no persistance or communiction
	public Node() {
		wallet = new RandomWallet(this);
		miner = new MinerPOW(this);
		blockchain = new Blockchain(this);
		pool = new TransactionPool(this);
		explorer = new Explorer(this);
		network = new Network(this);
		
		// creating servicebus and registering services
		serviceBus = new ServiceBus(this);
		logger = new Logger();
		serviceBus.addServiceListener(logger);		
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
			if (!(this.network.selfPeer.peerHost.equals(pr.peerHost) &&
				 this.network.selfPeer.peerPort == pr.peerPort)){
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
			serviceBus.addEvent("Syncronisation needed");
			serviceBus.addEvent("Local blockheight : " + localBlockchainHeight);
			serviceBus.addEvent("Remote blockheight : " + remoteBlockchainHeight) ;

			ArrayList<String> inventar = new ArrayList<String>();
			String genesisBlock = "";
			// Starting Syncronization
			for(Peer peer: this.network.peers){
				ArrayList<String> peerInv = peer.getInventar(localBlockchainHeight, remoteBlockchainHeight);
				if (peerInv != null){
					for (String id: peerInv) {
						genesisBlock = peerInv.get(0);
						if (!inventar.contains(id)) 
							inventar.add(id);
					}
				}
			}
			
			for (String Id: inventar) {
				ArrayList<String> alreadyAdded = new ArrayList<String>();
				for(Peer peer: this.network.peers){
					if (!alreadyAdded.contains(Id)) {
						Block block = peer.getBlock(Id);
						if (block != null) {
							if (Id.equals(genesisBlock))
								this.blockchain.addGenesisBlock(block);
							else
								this.blockchain.addBlock(block);
							alreadyAdded.add(Id);
							serviceBus.addEvent("Adding Block, BlockId : " + Id );							
						}
					}
				}
			}	
		}
		
		this.wallet.syncAccounts();					
		serviceBus.addEvent("Blockchain synced : " + this.blockchain.getBlockchinHeight());
		this.blockchain.isSynced = true;
	}
}
