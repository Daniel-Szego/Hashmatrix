package Block;

public class HashPointer {
	public String blockHash;
	Integer resetPolicy;
	Integer actualCount;
	
	public HashPointer(String _blockHash, Integer _resetPolicy, Integer _actualCount){
		blockHash = _blockHash;
		resetPolicy = _resetPolicy;
		actualCount = _actualCount;
	}
	
	public String ToHashString() {
		return  resetPolicy.toString() + actualCount.toString() + blockHash;		
	}
	
	public String ToResetString() {
		return  resetPolicy.toString() + actualCount.toString();		
	}	
}
