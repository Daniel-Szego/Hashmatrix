package SmartContract;
import State.*;
import Utils.LoggerConsole;

// simple rule
// If (account_1 operator value_1) then (account_2 = value_2) 
// Signed by account_2, because that is the account that will be changed
// signature is required in the smart contract as we do not transfer this objet on the net
public class SimpleRule {
	
	public String account_condition;
	public Operand operand;
	public String value_condition;
	public String account_effect;
	public String value_effect;	
	
	public SimpleRule(String _account_condition, String _value_condition, Operand _operand, String _account_effect, String _value_effect) {
		this.account_condition = _account_condition;
		this.value_condition = _value_condition;
		this.operand = _operand;
		this.account_effect = _account_effect;
		this.value_effect = _value_effect;		
	}

	public SimpleRule(String ruleString) {
		String[] array = ruleString.split("\\s+");		
		this.account_condition = array[1];
		this.operand = Operand.valueOf(array[2]);
		this.value_condition = array[3];		
		this.account_effect = array[5];
		this.value_effect = array[6];		
	}

	public String toString() {
		String commandString = "";
		commandString += "IF ";
		commandString += account_condition;
		commandString += " ";
		commandString += operand.toString();
		commandString += " ";
		commandString += value_condition;
		commandString += " THEN ";
		commandString += account_effect;
		commandString += " ";
		commandString += value_effect;
				
		return commandString;
	}
	
	public boolean validateOperand(String accountValue) {
		if (operand.equals(Operand.CONTAINS)) {
			if (accountValue.contains(value_condition))
				return true;
			else 
				return false;
		}
		else if (operand.equals(Operand.EQUALS)) {
			if (accountValue.equals(value_condition))
				return true;
			else 
				return false;

		}
		else if (operand.equals(Operand.STARTSWITH)) {
			if (accountValue.startsWith(value_condition))
				return true;
			else 
				return false;
		}
		else
			LoggerConsole.Log("Unknown Operand");
		return false;
	}
}
