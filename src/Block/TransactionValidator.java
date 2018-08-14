package Block;
import java.security.PublicKey;

import State.Account;
import Transaction.*;
import java.util.ArrayList;
import Utils.*;


// helper class to validate transactions with the help of a given state
public class TransactionValidator {

	// validates data transaction, if required, new account is added to the state
	public static boolean validateDataTransaction (StateDataTransaction tr, ArrayList<Account> state) {
		String address = ((StateDataTransaction)tr).GetAddressString();
		PublicKey addressPublicKey = ((StateDataTransaction)tr).address;
		String newData = ((StateDataTransaction)tr).newValue;
		int transactionNonce = ((StateDataTransaction)tr).getNonce();
		
		if (!tr.verifiySignature()) 
			return false;
		
		if (newData.equals("")){
			// checking null data, is that an error ?
			Logger.Log("new data is null in the transaction");
		}
		
		int accountFound = 0;
		// getting the account to be modified
		Account accountToModify = null;
		for(Account account: state) {
			String accountAddressString = account.getAddressString();
			if (accountAddressString.equals(address)){
				accountToModify = account;
				accountFound++;
			}
		}
		
		if (accountFound > 1) {
			// more than one account matches -> error
			Logger.Log("more than one account has been found");
			return false;
		}
		else if (accountFound == 1) {
			// one account has been found to match
			if (tr.getNonce() != accountToModify.nonce + 1){
				// nonce is not valid, possible replay attack
				Logger.Log("Nonce is not valid at transaction, possible replay aatack");
				return false;
			}
			else {
				// seems everything cool
				return true;
			}
			
		}else if (accountFound == 0){
			// no account is found -> a new account has to be added 
			Account account = new Account();
			account.setAddress(addressPublicKey);
			account.accountData = newData;
			account.nonce = 0;
			state.add(account);
		}		

		// if nothing matches return false
		return false;
	}
	
	// validates transform transaction, if required, new account is added to the state
	public static boolean validateTransferTransaction (StateTransferTransaction tr, ArrayList<Account> state) {
		PublicKey fromAddressPublicKey = ((StateTransferTransaction)tr).fromAddress;
		PublicKey toAddressPublicKey = ((StateTransferTransaction)tr).toAddress;				
		Float amount = ((StateTransferTransaction)tr).amount;
		int nonce = ((StateTransferTransaction)tr).getNonce();
	
		// checking signature
		if (!tr.verifiySignature()) 
			return false;
		
		// getting the fromAccount to be modified
		Account accountFromModify = null;
		int accountFromModifyFound = 0;
		boolean accountFromIsGood = false;
		for(Account account: state) {
			if (account.getAddress().equals(fromAddressPublicKey)){
				accountFromModify = account;
				accountFromModifyFound ++;
			}
		}
		
		if (accountFromModifyFound > 1) {
			// more than one account is found ?? -> raise error
			Logger.Log("more than one matching account has been found at transfer transaction");
			return false;
		}
		else if (accountFromModifyFound == 1) {
			if (tr.getNonce() != accountFromModify.nonce + 1) {
				// error -> nonce not matching -> pssible replay attack
				return false;
			}
			else {				
				if (accountFromModify.accountBalance < amount){
					// not enoguh fund on the account
					Logger.Log("Not enoguh fund on the account at transfer transaction");
					return false;
				}
				// all cool but still not return because the second account has to be checked as well
				else {
					accountFromIsGood = true;
				}
			}
				
		}else if (accountFromModifyFound == 0) {
			// error, if the account does not exist, you can not transfer money from that
			Logger.Log("The account from which you want to transfert the fund does not exist");
			return false;
		}
		

		// getting the toAccount to be modified
		Account accountToModify = null;
		int accountToModifyFound = 0;
		boolean accountToIsGood = false;
		for(Account account: state) {
			if (account.getAddress().equals(toAddressPublicKey)){
				accountToModify = account;
				accountToModifyFound ++;
			}
		}
		
		if (accountToModifyFound > 1) {
			// more than one account is found ?? -> raise error
			Logger.Log("More than one account is found at matching the transfer transaction");
			return false;
		}
		else if (accountToModifyFound == 1) {
			if (tr.getNonce() != accountToModify.nonce + 1) {
				// error -> nonce not matching -> pssible replay attack
				return false;
			}
			else {				
				accountToIsGood = true;
			}
				
		}else if (accountToModifyFound == 0) {
			// new to account can be added to the chain
			Account account = new Account();
			account.setAddress(toAddressPublicKey);
			account.accountBalance += amount;
			account.nonce = 0;
			state.add(account);
			accountToIsGood = true;
		}
		
		// final check if all good return true
		if (accountFromIsGood && accountToIsGood){
			return true;
		}
		
		// default value
		return false;	
	}	
}
	