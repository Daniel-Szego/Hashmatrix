package Node;

import java.util.ArrayList;
import java.util.Collections;

import Block.BlockBase;
import Chain.ExtendedBlock;
import ServiceBus.*;
import Transaction.*;
import Utils.Severity;

// service base provides the remote service implementation
public class NetworkServiceBase extends ServiceBase implements NetworkRemoteInterface {

	
	
	// constructor
	public NetworkServiceBase(ServiceBus _bus) {
		super(ServiceBus.crypto.getRandomString(), _bus);		
	}
	
	// REMOTE FUNCTIONS
	public String getClienVersion() {
	  ServiceBus.logger.log("getPeerList called", Severity.INFO);
	  return Constants.ClientVersion;
	}
	
	// returning the known peers
    public ArrayList<Peer> getPeerList (Peer _callee) 
    {
    	// if  peer is not contained, add to the list
    	node.serviceBus.addEvent("getPeerList called, host :" + _callee.peerHost + " port : " + _callee.peerPort);
    	boolean contains = false;
    	for(Peer peer: peers) {
    		if (peer.peerId.equals(_callee.peerId))
    			contains = true;
    	} 
    	if (!contains)
    		peers.add(_callee);
    	return peers;
    }

    // if it gets back some information, it means, the peer is still alive
    public boolean isPeerAlive() {
  	  ServiceBus.logger.log("isPeerAlive called", Severity.INFO);
    	return true;
    }
    
    // getting a bradcasted transaction from another peer
    public void boradcastTransaction (StateTransaction tr) {
		// SINGLE HOP -> transactions are not propagated further
    	// can be used in fully connected networks
    	
    	node.serviceBus.addEvent("bradcast transaction called, transaction Id :" + tr.getTransctionId());
    	node.serviceBus.addEventTransactionReceived("Transaction received", null, tr, true);
    	node.pool.addTransaction(tr);
    }
    
    // getting a broadcasted block from another peer
    public void broadcastBlock(BlockBase block) {
		// SINGLE HOP -> transactions are not propagated further
    	// can be used in fully connected networks    	
    	node.serviceBus.addEvent("bradcast block called, transaction Id :" + block.blockId);
    	node.serviceBus.addEventBlockReceived("block received",null,block,true);
    	node.blockchain.addBlock(block);	
    }
   
    // giving back the max block height
    public int getMaxBlockHeight () {
    	int blockHeight = node.blockchain.getBlockchinHeight();
    	node.serviceBus.addEvent("getMaxBlockHeigh has been called : " + blockHeight);
    	return blockHeight;
    }
    
    // getting block header Id-s
    public ArrayList<String> getInventar(int from, int to) {
    	node.serviceBus.addEvent("getInventar has been called, from : " + from + " to : " + to);
    	ArrayList<String> inventar = new ArrayList<String>();
    	for(ExtendedBlock block: node.blockchain.getBlockchain()) {
    		if ((from <= block.blockHeight) && (to >= block.blockHeight)) {
    			inventar.add(block.internBlock.blockId);
    		}
    	}
    	Collections.reverse(inventar);
    	return inventar;
    }
    
    // getting a block specified by the Id
    public BlockBase getBlock(String blockId) {
    	node.serviceBus.addEvent("getBloock has been called, Id : " + blockId);
    	for(ExtendedBlock block: node.blockchain.getBlockchain()) {
    		if (block.internBlock.blockId.equals(blockId)) {
    			return block.internBlock;
    		}
    	}
    	return null;
    }

	
}
