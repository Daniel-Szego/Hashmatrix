import java.util.ArrayList;
import com.google.gson.*;


public class Hashmatrix {
	
	public static ArrayList<Block> blockchain = new ArrayList<Block>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HashPointer h1 = new HashPointer("", 2, 0);
		HashPointer h2 = new HashPointer("", 2, 1);
		
		Block genesisBlock = new Block("Hi im the first block", h1,h2);
		System.out.println("Hash for block 1 hashOne : " + genesisBlock.hashOne.blockHash);
		System.out.println("Hash for block 1 hashTwo : " + genesisBlock.hashTwo.blockHash);
		
		Block secondBlock = new Block("Yo im the second block",genesisBlock.previousHashOne, genesisBlock.previousHashTwo);
		System.out.println("Hash for block 2 hashOne : " + secondBlock.hashOne.blockHash);
		System.out.println("Hash for block 2 hashTwo : " + secondBlock.hashTwo.blockHash);
		
		Block thirdBlock = new Block("Yo im the third block",secondBlock.previousHashOne, secondBlock.previousHashTwo);
		System.out.println("Hash for block 3 hashOne : " + thirdBlock.hashOne.blockHash);
		System.out.println("Hash for block 4 hashTwo : " + thirdBlock.hashTwo.blockHash);
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
				System.out.println("Current Hashes One not equal");			
				return false;
			}
			if(!currentBlock.hashTwo.blockHash.equals(currentBlock.calculateHash2()) ){
				System.out.println("Current Hashes Two not equal");			
				return false;
			}
	
			//compare previous hash and registered previous hash
			if(!previousBlock.hashOne.blockHash.equals(currentBlock.hashOne.blockHash) ) {
				System.out.println("Previous Hashes One not equal");
				return false;
			}
			//compare previous hash and registered previous hash
			if(!previousBlock.hashTwo.blockHash.equals(currentBlock.hashTwo.blockHash) ) {
				System.out.println("Previous Hashes Two not equal");
				return false;
			}
		}
		return true;
	}
	
}
