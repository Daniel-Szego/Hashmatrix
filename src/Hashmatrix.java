import java.util.ArrayList;
import com.google.gson.*;


public class Hashmatrix {
	
	public static int difficulty = 2;
	
	public static ArrayList<Block> blockchain = new ArrayList<Block>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HashPointer h1 = new HashPointer("", 2, 0);
		HashPointer h2 = new HashPointer("", 2, 1);
		
		Block genesisBlock = new Block("Hi im the first block", h1,h2);
		blockchain.add(genesisBlock);
		System.out.println("Trying to Mine block 1... ");
		blockchain.get(0).mineBlock(difficulty);
		
		Block secondBlock = new Block("Yo im the second block",blockchain.get(blockchain.size()-1).hashOne, blockchain.get(blockchain.size()-1).hashTwo);
		blockchain.add(secondBlock);
		System.out.println("Trying to Mine block 2... ");
		blockchain.get(1).mineBlock(difficulty);
		
		Block thirdBlock = new Block("Yo im the third block",blockchain.get(blockchain.size()-1).hashOne, blockchain.get(blockchain.size()-1).hashTwo);
		blockchain.add(thirdBlock);
		System.out.println("Trying to Mine block 3... ");
		blockchain.get(2).mineBlock(difficulty);
		
		System.out.println("\nBlockchain is Valid: " + isChainValid());
		
		String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
		System.out.println("\nThe block chain: ");
		System.out.println(blockchainJson);
	}
	

	public static Boolean isChainValid() {
		Block currentBlock; 
		Block previousBlock;
		
		//loop through blockchain to check hashes:
		for(int i=1; i < blockchain.size(); i++) {
			currentBlock = blockchain.get(i);
			previousBlock = blockchain.get(i-1);
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
		}
		return true;
	}
	
}
