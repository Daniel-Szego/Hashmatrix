package Block;
import State.AccountBase;
import Transaction.*;

import java.security.PublicKey;
import java.util.ArrayList;

import SmartContract.*;
import Utils.*;


// helper class to apply transaction changes to the state
public class StateTransformer {

	// copying the whole state -> it is pretty much inefficient
	public static void copyState(BlockBase previousBlock, BlockBase currentBlock){
		if (previousBlock != null) {
			
		}
		else{
			// error handling calling copystate but previous block not initialized 
			LoggerConsole.Log("calling copystate but previous block not initialized ");
		}
	}

	// applying the data transaction to the state, without validation
	public static void applyDataTransactionToState(StateDataTransaction tr, ArrayList<AccountBase> state) {
		String address = ((StateDataTransaction)tr).GetAddressString();
		PublicKey addressPublicKey = ((StateDataTransaction)tr).address;
		String newData = ((StateDataTransaction)tr).newValue;

		for(AccountBase account: state) {
			if (account.getAddress().equals(address)){
				account.setData(newData);
				account.increaseNonce();							
			}
		}		
	}
	
	// applying the transfer transaction to the state, without validation
	public static void applyTransferTransactionToState(StateTransferTransaction tr, ArrayList<AccountBase> state) {
		PublicKey fromAddressPublicKey = ((StateTransferTransaction)tr).fromAddress;
		PublicKey toAddressPublicKey = ((StateTransferTransaction)tr).toAddress;				
		Float amount = ((StateTransferTransaction)tr).amount;

		for(AccountBase account: state) {
			if (account.getAddress().equals(fromAddressPublicKey)){
			//	account.accountBalance -= amount;
			//	account.nonce ++;							
			}
		}

		for(AccountBase account: state) {
			if (account.getAddress().equals(toAddressPublicKey)){
		//		account.accountBalance += amount;
			}
		}			
	}
	
	// applying the transfer transaction to the state, without validation
	public static void applyRuleTransactionToState(StateRuleTransaction tr, ArrayList<AccountBase> state) {
		String ruleCode = ((StateRuleTransaction)tr).ruleCode;
		SimpleRule rule = new SimpleRule(ruleCode);
		
		boolean isConditionValid = false;
		for(AccountBase account: state) {
	//		if (account.getAddressString().equals(rule.account_condition)){
	//			isConditionValid = rule.validateOperand(account.accountData); 
	//		}
		}

		if (isConditionValid) {
			for(AccountBase account: state) {
	//			if (account.getAddressString().equals(rule.account_effect)){
	//				account.accountData = rule.value_effect;
	//				account.nonce ++;							
	//			}
			}
		}
	}
}
