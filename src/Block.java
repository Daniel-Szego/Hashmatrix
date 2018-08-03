import java.util.ArrayList;
import java.util.Date;

public class Block {

	public HashPointer hashOne;
	public HashPointer hashTwo;	
	public HashPointer previousHashOne;
	public HashPointer previousHashTwo;
	public String merkleRoot;
	public ArrayList<Transaction> transactions = new ArrayList<Transaction>();
	//private String data; //our data will be a simple message.
	private long timeStamp; //as number of milliseconds since 1/1/1970.
	private int nonceOne; // 
	private int nonceTwo; // 

	//Block Constructor.
	public Block(HashPointer previousHashOne,HashPointer previousHashTwo) {
		this.previousHashOne = previousHashOne;
		this.previousHashTwo = previousHashTwo;
		this.timeStamp = new Date().getTime();
		
		// calculate hash logic
		setHashOne(); 
		setHashTwo(); 	
	}
	
	// calculating and setting the hash
	public void setHashOne() {
		// reset one
		if (this.previousHashOne.actualCount == this.previousHashOne.resetPolicy){
			hashOne = new HashPointer(calculateHash1(),this.previousHashOne.resetPolicy,0);

		}
		else {
			hashOne = new HashPointer(calculateHash1(),this.previousHashOne.resetPolicy,previousHashOne.actualCount + 1);
		}				
	}
	
	public void setHashTwo() {
		// reset two
		if (this.previousHashTwo.actualCount == this.previousHashTwo.resetPolicy){
			hashTwo = new HashPointer(calculateHash2(),this.previousHashTwo.resetPolicy,0);

		}
		else {
			hashTwo = new HashPointer(calculateHash2(),this.previousHashTwo.resetPolicy,previousHashTwo.actualCount + 1);
		}				
	}

	
	public String calculateHash1() {
		String calculatedhashOne;
		
		// reset one
		if (this.previousHashOne.actualCount == this.previousHashOne.resetPolicy){
			calculatedhashOne = StringUtil.applySha256( 
					previousHashOne.ToResetString() +
				Long.toString(timeStamp) +
				Integer.toString(nonceOne) +
				merkleRoot 
			);
		}
		// reset none
		else {
			calculatedhashOne = StringUtil.applySha256( 
					previousHashOne.ToHashString() +
				Long.toString(timeStamp) +
				Integer.toString(nonceOne) +
				merkleRoot 
			);
		}
		return calculatedhashOne;
	}

	public String calculateHash2() {
		String calculatedhashTwo;
		
		// reset one
		if (this.previousHashTwo.actualCount == this.previousHashTwo.resetPolicy){
			calculatedhashTwo = StringUtil.applySha256( 
					previousHashTwo.ToResetString() +
				Long.toString(timeStamp) +
				Integer.toString(nonceTwo) +
				merkleRoot 
			);
		}
		// reset none
		else {
			calculatedhashTwo = StringUtil.applySha256( 
					previousHashTwo.ToHashString() +
				Long.toString(timeStamp) +
				Integer.toString(nonceTwo) +
				merkleRoot 
			);
		}
		return calculatedhashTwo;
	}	
	
	// mining both hashes for the same difficulty
	public void mineBlock(int difficulty) {
		merkleRoot = StringUtil.getMerkleRoot(transactions);
		String target = new String(new char[difficulty]).replace('\0', '0'); //Create a string with difficulty * "0" 
		while(!hashOne.blockHash.substring(0, difficulty).equals(target)) {
			nonceOne ++;
			setHashOne();
		}
		
		while(!hashTwo.blockHash.substring( 0, difficulty).equals(target)) {
			nonceTwo ++;
			setHashTwo();
		}
		
		System.out.println("Block Mined!!! : <" + hashOne.blockHash + " , " + hashTwo.blockHash + ">");
	}
	
	//Add transactions to this block
		public boolean addTransaction(Transaction transaction) {
			//process transaction and check if valid, unless block is genesis block then ignore.
			if(transaction == null) return false;		
			if((previousHashOne.blockHash != "0") && (previousHashTwo.blockHash != "0")) {
				if((transaction.processTransaction() != true)) {
					System.out.println("Transaction failed to process. Discarded.");
					return false;
				}
			}
			transactions.add(transaction);
			System.out.println("Transaction Successfully added to Block");
			return true;
		}
}


