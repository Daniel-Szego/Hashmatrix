package Miner;

import Block.BlockInterface;
import Chain.*;
import Node.*;
import ServiceBus.*;
import Transaction.*;
import Utils.Severity;

// base class for validator services
public class ValidatorServiceBase extends ServiceBase implements ValidatorServiceInterface {
	
	public final ServiceBus bus;
	protected final ValidatorInterface miner;
	protected Thread minerThread;
	
	public ValidatorServiceBase(ServiceBus _bus) {
		super(ServiceBus.crypto.getRandomString());
		this.bus = _bus;
		this.miner = new MinerPOWSimple();
	}

	// creating or mining one block
	public BlockInterface createOneBlock() {
		
		// getting top block
		ExtendedBlockInterface eBlock = bus.blockchainService.getInternalBlockchain().getTopBlock();
		TransactionPoolInterface trPool = bus.trPool;
		
 		// creating one new block
 		BlockInterface newBlock = miner.createNextBlock(eBlock.getInternBlock(), trPool);
		
		// clearing transactions from the pool
		for(TransactionInterface tr: newBlock.getTransactions().getTransactions()) {
			trPool.removeTransactionbyId(tr.getTransctionId());
		}
		
		// broadcasting the block
		bus.addEventBlockMined("new block mined locally", this, newBlock, true);
		
		// givving back the one block
		return newBlock;
	}
	
	// starting the mining
	public void startValidation() {
		if(minerThread.isAlive()) {
			ServiceBus.logger.log("Miner already running", Severity.ERROR);
		}
		else {
		ValidatorServiceBase serviceBase = this;
		
		minerThread = new Thread(new Runnable() {
				public void run() {
					while(true) {
						serviceBase.createOneBlock();
					}
				}
			});
		
		minerThread.start();
		}
	}
	
	// starting the mining
	public void stopValidation() {
		if(minerThread.isAlive()) {
			minerThread.stop();
		}		
	}

	
	
}
