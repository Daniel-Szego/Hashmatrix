package SmartContract;

import java.util.ArrayList;

// simple smart contract implementation
// simple if-then based rule engine 
public class SmartContractLight extends SmartContract {
	
	public ArrayList<SimpleRule> rules = new ArrayList<SimpleRule>();
	
	public SmartContractLight() {
				
	}
	
	// adding smart contract to the list
	public void addRule(SimpleRule _rule) {
		rules.add(_rule);
	}
	
	public void applyRulesToState() {
		
	}
		
}
