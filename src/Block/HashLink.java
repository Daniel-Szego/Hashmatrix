package Block;

// administrating a pair of hashlinks
public class HashLink {
	public String hashOne; // first hash structure for the 
	public String hashTwo; // second hash structure
	public int resetPolicy; // after how many blocks should be reseted
	public int resetCount; // how many blocks were executed after the last reset
	public boolean lastResetedHash; //true last reseted was one, false last reseted was two
	public boolean singleHash; // if true the hash is immutable, so only the first should be consideerd without reset policy
	
	public HashLink(String _hashOne, String _hashTwo, int _resetPolicy, int _resetCount, boolean _lastResetedHash, boolean _singleHash){
		this.hashOne = _hashOne; 
		if (!_singleHash) {
			this.hashTwo =  _hashTwo;
			this.resetPolicy = _resetPolicy;
			this.resetCount= _resetCount;
			this.lastResetedHash = _lastResetedHash;
		}
		this.singleHash = _singleHash;
	}
	
	public String ToHashString() {
		if (singleHash){
					return hashOne;
		}
		else {
			return  Integer.toString(resetPolicy) + 
					Integer.toString(resetCount) + 
					Boolean.toString(lastResetedHash) + 
					Boolean.toString(singleHash) +
					hashOne +
					hashTwo;								
		}
	}
	
	public String ToResetStringOne() {
		
		if (singleHash){
			return hashOne;
		}
		else {
			return  Integer.toString(resetPolicy) + 
					Integer.toString(resetCount) + 
					Boolean.toString(lastResetedHash) + 
					Boolean.toString(singleHash) +
					"0" +
					hashTwo;								
			}
	}	
	
	public String ToResetStringTwo() {
		if (singleHash){
			// Exception handling, this part should not be called
			return hashOne;
		}
		else {
			return  Integer.toString(resetPolicy) + 
					Integer.toString(resetCount) + 
					Boolean.toString(lastResetedHash) + 
					Boolean.toString(singleHash) +
					hashOne +
					"0";								
			}
	}		
}
