package Node;

import java.util.ArrayList;

import Block.*;
import Chain.*;
import Miner.*;
import ServiceBus.*;
import Transaction.*;
import Utils.LoggerConsole;
import Utils.Severity;
import Wallet.*;
import Explorer.*;

// initial version of the node
public class NodeServiceBase implements NodeServiceInterface {
	
	public final ServiceBus bus;
	
	
	// starting the node - test code, no persistance or communiction
	// node service starts the service bus
	public NodeServiceBase() {
		bus = new ServiceBus(this);
		
		
		initializeServiceListeners();
	}
	
	// adding service listeners
	protected void initializeServiceListeners() {
		// logger listens for everything
		bus.addServiceListener(new ServiceListenerInfo(logger, null));
		// explorer listens for new transactions
		// later on on validated transaction and blocks as well
		bus.addServiceListener(new ServiceListenerInfo(explorer, ServiceEventBlockReceived.class));
		bus.addServiceListener(new ServiceListenerInfo(explorer, ServiceEventTransactionReceived.class));

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
	public void broadcastBlock(BlockBase _block) {	
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
		BlockBase proposedBlock = miner.mineNextBlock(this.blockchain.getTopBlock().internBlock, this.pool);
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
						BlockBase block = peer.getBlock(Id);
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
	
	// getting the service bus
	public ServiceBus getServiceBus() {
		return this.serviceBus;
	}

}
