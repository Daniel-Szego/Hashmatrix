package Block;

import Crypto.CryptoUtil;
import Miner.*;

// administrating a pair of hashlinks
// hashlink is practically like a block header
public class HashLink {
	public String hashOne; // first hash structure for the 
	public String hashTwo; // second hash structure
	public int resetPolicy; // after how many blocks should be reseted
	public int resetCount; // how many blocks were executed after the last reset
	public boolean lastResetedHash; //true last reseted was one, false last reseted was two
	public boolean singleHash; // if true the hash is immutable, so only the first should be consideerd without reset policy
	public int nonceOne; // 
	public int nonceTwo; // 
	public int difficulty; //leading zeros for the nonce, simplified version
	
	
	public HashLink(String _hashOne, String _hashTwo, int _resetPolicy, int _resetCount, boolean _lastResetedHash, boolean _singleHash, int _difficulty){
		this.hashOne = _hashOne; 
		if (!_singleHash) {
			this.hashTwo =  _hashTwo;
			this.resetPolicy = _resetPolicy;
			this.resetCount= _resetCount;
			this.lastResetedHash = _lastResetedHash;
			this.difficulty = _difficulty;
		}
		this.singleHash = _singleHash;
	}
	
	// hash one and two can be calculated independently
	public void calculateHashOne(String _stateRoot, String _transactionRoot, String _previousHashOne) {
		if (singleHash) {
			  // in singlehash case, rest policy is not considered	
			  hashOne = CryptoUtil.applySha256(
					  _stateRoot +
					  _transactionRoot +
					  _previousHashOne +
					  Integer.toString(resetPolicy) + 
					  Integer.toString(resetCount) + 
					  Boolean.toString(lastResetedHash) + 
					  Integer.toString(difficulty)		
					);
			}
			else {
			
				if (resetCount == 0) {
					if (lastResetedHash) {
						
						  hashOne = CryptoUtil.applySha256(
								  _stateRoot +
								  _transactionRoot +
								  _previousHashOne +
								  Integer.toString(resetPolicy) + 
								  Integer.toString(resetCount) + 
								  Boolean.toString(lastResetedHash) + 
								  Integer.toString(difficulty)		
								);
					}
					else {
						
						  hashOne = CryptoUtil.applySha256(
								  _stateRoot +
								  _transactionRoot +
								  "0" +
								  Integer.toString(resetPolicy) + 
								  Integer.toString(resetCount) + 
								  Boolean.toString(lastResetedHash) + 
								  Integer.toString(difficulty)		
								);
					}
									
				}else{

					  hashOne = CryptoUtil.applySha256(
							  _stateRoot +
							  _transactionRoot +
							  _previousHashOne +
							  Integer.toString(resetPolicy) + 
							  Integer.toString(resetCount) + 
							  Boolean.toString(lastResetedHash) + 
							  Integer.toString(difficulty)		
							);
				}
			}

		
	}

	public void calculateHashTwo(String _stateRoot, String _transactionRoot, String _previousHashTwo) {
		if (singleHash) {
			  // in singlehash case, rest policy is not considered	
			  // ERROR ? in single hash mode there is no hashtwo	
			}
			else {
			
				if (resetCount == 0) {
					if (lastResetedHash) {
						
						  hashTwo = CryptoUtil.applySha256(
								  _stateRoot +
								  _transactionRoot +
								  "0" +
								  Integer.toString(resetPolicy) + 
								  Integer.toString(resetCount) + 
								  Boolean.toString(lastResetedHash) + 
								  Integer.toString(difficulty)		
								);			  								
					}
					else {
						
						  hashTwo = CryptoUtil.applySha256(
								  _stateRoot +
								  _transactionRoot +
								  _previousHashTwo +
								  Integer.toString(resetPolicy) + 
								  Integer.toString(resetCount) + 
								  Boolean.toString(lastResetedHash) + 
								  Integer.toString(difficulty)		
								);			  													
					}
									
				}else{

					  hashTwo = CryptoUtil.applySha256(
							  _stateRoot +
							  _transactionRoot +
							  _previousHashTwo +
							  Integer.toString(resetPolicy) + 
							  Integer.toString(resetCount) + 
							  Boolean.toString(lastResetedHash) + 
							  Integer.toString(difficulty)		
							);			  			
				}
			}

	}

	public boolean validateHashOne(){		
		String target = new String(new char[difficulty]).replace('\0', '0'); //Create a string with difficulty * "0" 
		if(hashOne.substring( 0, difficulty).equals(target))
			return true;
		return false;
	}
	
	public boolean validateHashTwo(){
		String target = new String(new char[difficulty]).replace('\0', '0'); //Create a string with difficulty * "0" 
		if(hashTwo.substring( 0, difficulty).equals(target))
			return true;
		return false;		
	}
	
	public void calculateHash(String _stateRoot, String _transactionRoot, String _previousHashOne, String _previousHashTwo) {
		if (singleHash) {
		  // in singlehash case, rest policy is not considered	
		  	
		  hashOne = CryptoUtil.applySha256(
				  _stateRoot +
				  _transactionRoot +
				  _previousHashOne +
				  Integer.toString(resetPolicy) + 
				  Integer.toString(resetCount) + 
				  Boolean.toString(lastResetedHash) + 
				  Integer.toString(difficulty)		
				);
		}
		else {
		
			if (resetCount == 0) {
				if (lastResetedHash) {
					
					  hashOne = CryptoUtil.applySha256(
							  _stateRoot +
							  _transactionRoot +
							  _previousHashOne +
							  Integer.toString(resetPolicy) + 
							  Integer.toString(resetCount) + 
							  Boolean.toString(lastResetedHash) + 
							  Integer.toString(difficulty)		
							);

					  hashTwo = CryptoUtil.applySha256(
							  _stateRoot +
							  _transactionRoot +
							  "0" +
							  Integer.toString(resetPolicy) + 
							  Integer.toString(resetCount) + 
							  Boolean.toString(lastResetedHash) + 
							  Integer.toString(difficulty)		
							);			  								
				}
				else {
					
					  hashOne = CryptoUtil.applySha256(
							  _stateRoot +
							  _transactionRoot +
							  "0" +
							  Integer.toString(resetPolicy) + 
							  Integer.toString(resetCount) + 
							  Boolean.toString(lastResetedHash) + 
							  Integer.toString(difficulty)		
							);

					  hashTwo = CryptoUtil.applySha256(
							  _stateRoot +
							  _transactionRoot +
							  _previousHashTwo +
							  Integer.toString(resetPolicy) + 
							  Integer.toString(resetCount) + 
							  Boolean.toString(lastResetedHash) + 
							  Integer.toString(difficulty)		
							);			  													
				}
								
			}else{

				  hashOne = CryptoUtil.applySha256(
						  _stateRoot +
						  _transactionRoot +
						  _previousHashOne +
						  Integer.toString(resetPolicy) + 
						  Integer.toString(resetCount) + 
						  Boolean.toString(lastResetedHash) + 
						  Integer.toString(difficulty)		
						);
				  hashTwo = CryptoUtil.applySha256(
						  _stateRoot +
						  _transactionRoot +
						  _previousHashTwo +
						  Integer.toString(resetPolicy) + 
						  Integer.toString(resetCount) + 
						  Boolean.toString(lastResetedHash) + 
						  Integer.toString(difficulty)		
						);			  			
			}
		}
		
		
	}
	
	
}
