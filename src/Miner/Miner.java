package Miner;

import Node.*;
import Block.*;
import Chain.*;
import Transaction.*;

// abstract class for the Miner
public abstract class Miner {
	public final Node node;
	
	public Miner(Node _node) {
		this.node = _node;
	}
	
	public abstract BlockBase mineNextBlock(BlockBase previousBlock, TransactionPool pool);

}
