package Miner;

import Block.*;
import ServiceBus.*;

// abstract interface for miner and other validator services
public interface ValidatorServiceInterface extends ServiceInterface {
	
	// creating or mining one block
	public BlockInterface createOneBlock();
	
	// starting the mining
	public void startValidation();
	
	// starting the mining
	public void stopValidation();
	
	
}
