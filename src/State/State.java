package State;

import java.security.PublicKey;
import java.util.ArrayList;

import Crypto.StringUtil;
import Crypto.CryptoUtil;
import java.util.Random;
import ServiceBus.*;
import SmartContract.SimpleRule;
import Transaction.*;
import Utils.LoggerConsole;
import Utils.Severity;


public class State implements StateInterface {

	protected ArrayList<AccountBase> accounts = new ArrayList<AccountBase>();
	public final String stateId;

	// default constructor, state id is initialized
	public State () {
		this.stateId = generateStateId();
	}
	
	public State (String _stateId) {
		this.stateId = _stateId;
	}
	
	// adding an account to the state
	public void addAccount(AccountBase _account) {
		if (!isAccountContained(_account)) {
			accounts.add(_account);
		}
	}
	
	// querying if an account is in the state 
	public boolean isAccountContained(AccountBase _account) {
		return isAccountContained(_account.address);
	}
	
	// querying if an account is in the state 
	public boolean isAccountContained(String _address) {
		for (AccountBase account: accounts) {
			if (account.address.equals(_address))
				return true;
		}
		return false;
	}

	// getting an account based on the address
	public AccountBase getAccount(String _address) {
		for (AccountBase account: accounts) {
			if (account.address.equals(_address))
				return account;
		}
		return null;		
	}
	
	// gettint merkle root of the state
	public String getMerkleRoot() {
		int count = accounts.size();
		ArrayList<String> previousTreeLayer = new ArrayList<String>();
		for(AccountBase account : accounts) {
			previousTreeLayer.add(account.getAddress());
		}
		
		ArrayList<String> treeLayer = previousTreeLayer;
		while(count > 1) {
			treeLayer = new ArrayList<String>();
			for(int i=1; i < previousTreeLayer.size(); i++) {
				treeLayer.add(CryptoUtil.applySha256(previousTreeLayer.get(i-1) + previousTreeLayer.get(i)));
			}
			count = treeLayer.size();
			previousTreeLayer = treeLayer;
		}
		String merkleRoot = (treeLayer.size() == 1) ? treeLayer.get(0) : "";
		return merkleRoot;		
	}

	// return all the accounts
	public ArrayList<AccountBase> getAccounts() {
		return accounts;
	}
	
	// hard copy a state with all of the accounts
	public State copyState() {
		State newState = new State();
		for (AccountBase account: accounts) {
			AccountBase newAccount = account.copyAccount();
			newState.addAccount(newAccount);
		}
		return newState;
	}
	
	// generating a state id string
	// first implementation, random and hash
	protected String generateStateId() {
		Random rand = new Random();
		int  n = rand.nextInt(60000);
		String Id = ServiceBus.crypto.applyHash(String.valueOf(n));
		return Id;
	}
	
	// getting the number of accounts
	public int getAccounsSize() {
		return accounts.size();
	}
	
	// STANDARD VALIDATION
	
	// checks if a certain transaction is compatible with a give state
	public boolean isTransactionValid(TransactionInterface tr) {
		if (!tr.verifySignature()) {
			ServiceBus.logger.log("Signature is not valid, Id : " + tr.getTransctionId(), Severity.ERROR);
			return false;
		}		
		
		if (tr instanceof StateDataTransaction) {
			return this.isDataTransactionValid((StateDataTransaction)tr);
		}
		else if (tr instanceof StateTransferTransaction) {
			return this.isTransferTransactionValid((StateTransferTransaction)tr);
		}
		else if (tr instanceof StateRuleTransaction) {
			return this.isRuleTransactionValid((StateRuleTransaction)tr);
		}
		else {
			ServiceBus.logger.log("Unknown transaction, Id : " + tr.getTransctionId() , Severity.WARNING);
		}
		return false;
	}
	
	// validation function for data transaction
	protected boolean isDataTransactionValid(StateDataTransaction tr) {
		String address = ((StateDataTransaction)tr).address;
		String newData = ((StateDataTransaction)tr).newValue;
		int transactionNonce = ((StateDataTransaction)tr).getNonce();
		
		if (newData.equals("")){
			// checking null data, is that an error ?
			ServiceBus.logger.log("new data is null in the transaction, Id : " + tr.getTransctionId());
		}
		
		int accountFound = 0;
		// getting the account to be modified
		AccountInterface accountToModify = null;
		for(AccountInterface account: accounts) {
			String accountAddressString = account.getAddress();
			if (accountAddressString.equals(address)){
				accountToModify = account;
				accountFound++;
			}
		}
		
		if (accountFound > 1) {
			// more than one account matches -> error
			ServiceBus.logger.log("more than one account has been found at transaction, Id : " + tr.getTransctionId());
			return false;
		}
		else if (accountFound == 1) {
			// one account has been found to match
			if (tr.getNonce() != accountToModify.getNonce() + 1){
				// nonce is not valid, possible replay attack
				ServiceBus.logger.log("Nonce is not valid at transaction, possible replay aatack, Id : " + tr.getTransctionId());
				return false;
			}
			else {
				// seems everything cool
				return true;
			}
			
		}else if (accountFound == 0){
			// no account is found -> a new account has to be added
			// as this is called by the validating not at the mining, this case is invalid
			ServiceBus.logger.log("Target account is not found at transactio validation, Id : " + tr.getTransctionId());
			return false;
		}		
		// if nothing matches return false
		return false;
	}
	
	// validation function for a transfer transaction
	protected boolean isTransferTransactionValid(StateTransferTransaction tr) {
		String fromAddressPublicKey = ((StateTransferTransaction)tr).from;
		String toAddressPublicKey = ((StateTransferTransaction)tr).to;				
		double amount = ((StateTransferTransaction)tr).amount;
		int nonce = ((StateTransferTransaction)tr).getNonce();
			
		// getting the fromAccount to be modified
		AccountInterface accountFromModify = null;
		int accountFromModifyFound = 0;
		boolean accountFromIsGood = false;
		for(AccountInterface account: accounts) {
			if (account.getAddress().equals(fromAddressPublicKey)){
				accountFromModify = account;
				accountFromModifyFound ++;
			}
		}
		
		if (accountFromModifyFound > 1) {
			// more than one account is found ?? -> raise error
			ServiceBus.logger.log("more than one matching account has been found at transfer transaction TrID : " + tr.getTransctionId());
			return false;
		}
		else if (accountFromModifyFound == 1) {
			if (tr.getNonce() != accountFromModify.getNonce() + 1) {
				// error -> nonce not matching -> pssible replay attack
				ServiceBus.logger.log("Nonce is not valid at transaction, possible replay aatack");
				return false;
			}
			else {				
				if (accountFromModify.getBalance() < amount){
					// not enoguh fund on the account
					ServiceBus.logger.log("Not enoguh fund on the account at transfer transaction TrID : " + tr.getTransctionId());
					return false;
				}
				// all cool but still not return because the second account has to be checked as well
				else {
					accountFromIsGood = true;
				}
			}
				
		}else if (accountFromModifyFound == 0) {
			// error, if the account does not exist, you can not transfer money from that
			ServiceBus.logger.log("The account from which you want to transfer the fund does not exist, TrID : " + tr.getTransctionId());
			return false;
		}
		

		// getting the toAccount to be modified
		AccountInterface accountToModify = null;
		int accountToModifyFound = 0;
		boolean accountToIsGood = false;
		for(AccountInterface account: accounts) {
			if (account.getAddress().equals(toAddressPublicKey)){
				accountToModify = account;
				accountToModifyFound ++;
			}
		}
		
		if (accountToModifyFound > 1) {
			// more than one account is found ?? -> raise error
			ServiceBus.logger.log("More than one account is found at matching the transfer transaction TrID : " + tr.getTransctionId());
			return false;
		}
		else if (accountToModifyFound == 1) {
			accountToIsGood = true;
				
		}else if (accountToModifyFound == 0) {
			// error, if the account does not exist, you can not transfer money from that
			ServiceBus.logger.log("The to account does not exist, TrID : " + tr.getTransctionId());
			return false;
		}
		
		// transfer is only possible if the asset type is the same, otherwise it is an exchange
		if (!accountToModify.getAssetType().equals(accountFromModify.getAssetType())){
			// error, from and to asset type must be the same
			ServiceBus.logger.log("From and to asset type must be the same, TrID : " + tr.getTransctionId(), Severity.WARNING);
			return false;			
		}
				
		// final check if all good return true
		if (accountFromIsGood && accountToIsGood){
			return true;
		}
		
		// default value
		return false;			
	}

	// validation function for a rule transaction
	protected boolean isRuleTransactionValid(StateRuleTransaction tr) {
		String addressEffect = ((StateRuleTransaction)tr).address;
		String ruleCode = ((StateRuleTransaction)tr).code;
		int transactionNonce = ((StateRuleTransaction)tr).getNonce();
				
		if (ruleCode.equals("")){
			// checking null data, is that an error ?
			ServiceBus.logger.log("rule must containt information, Id : " +tr.getTransctionId());
		}
		
		SimpleRule rule = new SimpleRule(ruleCode);
		
		int accountFound = 0;
		boolean effectAccountFound = false;
		// getting the account to be modified
		AccountInterface accountToModify = null;
		for(AccountInterface account: accounts) {
			String accountAddressString = account.getAddress();
			if (accountAddressString.equals(addressEffect)){
				accountToModify = account;
				accountFound++;
			}
		}
		
		if (accountFound > 1) {
			// more than one account matches -> error
			ServiceBus.logger.log("more than one account has been found, Id : " + tr.getTransctionId());
			return false;
		}
		else if (accountFound == 1) {
			// one account has been found to match
			if (tr.getNonce() != accountToModify.getNonce() + 1){
				// nonce is not valid, possible replay attack
				ServiceBus.logger.log("Nonce is not valid at transaction, possible replay aatack");
				return false;
			}
			else {
				// seems everything cool
				effectAccountFound = true;
			}
			
		}else if (accountFound == 0){
			ServiceBus.logger.log("Effected account not found, Id : " + tr.getTransctionId());
			return false;
		}		
		
		boolean conditionAccountFound = false;
		accountFound = 0;
		// getting the account to be modified
		AccountInterface accountCondition = null;
		for(AccountInterface account: accounts) {
			String accountAddressString = account.getAddress();
			if (accountAddressString.equals(rule.account_condition)){
				accountCondition = account;
				accountFound++;
			}
		}
		
		if (accountFound > 1) {
			// more than one account matches -> error
			ServiceBus.logger.log("more than one condition account has been found, Id : " + tr.getTransctionId());
			return false;
		}
		else if (accountFound == 1) {
			conditionAccountFound = true;
			
		}else if (accountFound == 0){
			ServiceBus.logger.log("Condition account not found, Id : " + tr.getTransctionId());
			return false;
		}		

		if (conditionAccountFound && effectAccountFound)
			return true;

		// if nothing matches return false
		return false;
	}
	
	// EXTENDED VALIDATION
	// ADDS ACCOUNT TO THE STATE

	
	public boolean isTransactionValidEx(TransactionInterface tr) {
		if (!tr.verifySignature()) {
			ServiceBus.logger.log("Signature is not valid, Id : " + tr.getTransctionId(), Severity.ERROR);
			return false;
		}		
		
		if (tr instanceof StateDataTransaction) {
			return this.isDataTransactionValidEx((StateDataTransaction)tr);
		}
		else if (tr instanceof StateTransferTransaction) {
			return this.isTransferTransactionValidEx((StateTransferTransaction)tr);
		}
		else if (tr instanceof StateRuleTransaction) {
			return this.isRuleTransactionValidEx((StateRuleTransaction)tr);
		}
		else {
			ServiceBus.logger.log("Unknown transaction, Id : " + tr.getTransctionId() , Severity.WARNING);
		}
		return false;
	}
	
	
	
	// validation function for data transaction
	protected boolean isDataTransactionValidEx(StateDataTransaction tr) {
		String address = ((StateDataTransaction)tr).address;
		String newData = ((StateDataTransaction)tr).newValue;
		int transactionNonce = ((StateDataTransaction)tr).getNonce();
		
		if (newData.equals("")){
			// checking null data, is that an error ?
			ServiceBus.logger.log("new data is null in the transaction, Id : " + tr.getTransctionId());
		}
		
		int accountFound = 0;
		// getting the account to be modified
		AccountInterface accountToModify = null;
		for(AccountInterface account: accounts) {
			String accountAddressString = account.getAddress();
			if (accountAddressString.equals(address)){
				accountToModify = account;
				accountFound++;
			}
		}
		
		if (accountFound > 1) {
			// more than one account matches -> error
			ServiceBus.logger.log("more than one account has been found at transaction, Id : " + tr.getTransctionId());
			return false;
		}
		else if (accountFound == 1) {
			// one account has been found to match
			if (tr.getNonce() != accountToModify.getNonce() + 1){
				// nonce is not valid, possible replay attack
				ServiceBus.logger.log("Nonce is not valid at transaction, possible replay aatack, Id : " + tr.getTransctionId());
				return false;
			}
			else {
				// seems everything cool
				// apply state 
				accountToModify.setData(newData);				
			}
			
		}else if (accountFound == 0){
			// no account is found -> a new account has to be added
			// default data transaction
			AccountBase account = new AccountBase(
					((StateDataTransaction)tr).address,
					((StateDataTransaction)tr).getNonce(),
					((StateDataTransaction)tr).newValue,
					0,
					new char[] {'D','F','T'});	
			accounts.add(account);
		}		
		// if nothing matches return false
		return false;
	}
	
	// validation function for a transfer transaction
	protected boolean isTransferTransactionValidEx(StateTransferTransaction tr) {
		String fromAddressPublicKey = ((StateTransferTransaction)tr).from;
		String toAddressPublicKey = ((StateTransferTransaction)tr).to;				
		double amount = ((StateTransferTransaction)tr).amount;
		int nonce = ((StateTransferTransaction)tr).getNonce();
			
		// getting the fromAccount to be modified
		AccountInterface accountFromModify = null;
		int accountFromModifyFound = 0;
		boolean accountFromIsGood = false;
		for(AccountInterface account: accounts) {
			if (account.getAddress().equals(fromAddressPublicKey)){
				accountFromModify = account;
				accountFromModifyFound ++;
			}
		}
		
		if (accountFromModifyFound > 1) {
			// more than one account is found ?? -> raise error
			ServiceBus.logger.log("more than one matching account has been found at transfer transaction TrID : " + tr.getTransctionId());
			return false;
		}
		else if (accountFromModifyFound == 1) {
			if (tr.getNonce() != accountFromModify.getNonce() + 1) {
				// error -> nonce not matching -> pssible replay attack
				ServiceBus.logger.log("Nonce is not valid at transaction, possible replay aatack");
				return false;
			}
			else {				
				if (accountFromModify.getBalance() < amount){
					// not enoguh fund on the account
					ServiceBus.logger.log("Not enoguh fund on the account at transfer transaction TrID : " + tr.getTransctionId());
					return false;
				}
				// all cool but still not return because the second account has to be checked as well
				else {
					accountFromIsGood = true;
				}
			}
				
		}else if (accountFromModifyFound == 0) {
			// error, if the account does not exist, you can not transfer money from that
			ServiceBus.logger.log("The account from which you want to transfer the fund does not exist, TrID : " + tr.getTransctionId());
			return false;
		}
		

		// getting the toAccount to be modified
		AccountInterface accountToModify = null;
		int accountToModifyFound = 0;
		boolean accountToIsGood = false;
		for(AccountInterface account: accounts) {
			if (account.getAddress().equals(toAddressPublicKey)){
				accountToModify = account;
				accountToModifyFound ++;
			}
		}
		
		if (accountToModifyFound > 1) {
			// more than one account is found ?? -> raise error
			ServiceBus.logger.log("More than one account is found at matching the transfer transaction TrID : " + tr.getTransctionId());
			return false;
		}
		else if (accountToModifyFound == 1) {
			accountToIsGood = true;
				
		}else if (accountToModifyFound == 0) {
			// to account can be added to the state, asset type logic is questionable
			
			AccountBase account = new AccountBase(
					((StateTransferTransaction)tr).from,
					((StateTransferTransaction)tr).getNonce(),
					"",
					((StateTransferTransaction)tr).amount,
					accountFromModify.getAssetType());	
			accounts.add(account);
			
			accountToIsGood = true;
		}
		
		// transfer is only possible if the asset type is the same, otherwise it is an exchange
		if (!accountToModify.getAssetType().equals(accountFromModify.getAssetType())){
			// error, from and to asset type must be the same
			ServiceBus.logger.log("From and to asset type must be the same, TrID : " + tr.getTransctionId(), Severity.WARNING);
			return false;			
		}
				
		// final check if all good return true
		if (accountFromIsGood && accountToIsGood){
			return true;
		}
		
		// default value
		return false;			
	}

	// validation function for a rule transaction
	protected boolean isRuleTransactionValidEx(StateRuleTransaction tr) {
		String addressEffect = ((StateRuleTransaction)tr).address;
		String ruleCode = ((StateRuleTransaction)tr).code;
		int transactionNonce = ((StateRuleTransaction)tr).getNonce();
				
		if (ruleCode.equals("")){
			// checking null data, is that an error ?
			ServiceBus.logger.log("rule must containt information, Id : " +tr.getTransctionId());
		}
		
		SimpleRule rule = new SimpleRule(ruleCode);
		
		int accountFound = 0;
		boolean effectAccountFound = false;
		// getting the account to be modified
		AccountInterface accountToModify = null;
		for(AccountInterface account: accounts) {
			String accountAddressString = account.getAddress();
			if (accountAddressString.equals(addressEffect)){
				accountToModify = account;
				accountFound++;
			}
		}
		
		if (accountFound > 1) {
			// more than one account matches -> error
			ServiceBus.logger.log("more than one account has been found, Id : " + tr.getTransctionId());
			return false;
		}
		else if (accountFound == 1) {
			// one account has been found to match
			if (tr.getNonce() != accountToModify.getNonce() + 1){
				// nonce is not valid, possible replay attack
				ServiceBus.logger.log("Nonce is not valid at transaction, possible replay aatack");
				return false;
			}
			else {
				// seems everything cool
				effectAccountFound = true;
			}
			
		}else if (accountFound == 0){
			ServiceBus.logger.log("Effected account not found, Id : " + tr.getTransctionId());
			return false;
		}		
		
		boolean conditionAccountFound = false;
		accountFound = 0;
		// getting the account to be modified
		AccountInterface accountCondition = null;
		for(AccountInterface account: accounts) {
			String accountAddressString = account.getAddress();
			if (accountAddressString.equals(rule.account_condition)){
				accountCondition = account;
				accountFound++;
			}
		}
		
		if (accountFound > 1) {
			// more than one account matches -> error
			ServiceBus.logger.log("more than one condition account has been found, Id : " + tr.getTransctionId());
			return false;
		}
		else if (accountFound == 1) {
			conditionAccountFound = true;
			
		}else if (accountFound == 0){
			ServiceBus.logger.log("Condition account not found, Id : " + tr.getTransctionId());
			return false;
		}		

		if (conditionAccountFound && effectAccountFound)
			return true;

		// if nothing matches return false
		return false;
	}
	
	
	// APPLY TRANSACTION -> TRANSFORM STATE
	
	// applying the transaction to the state after validation
	public boolean applyTransaction(TransactionInterface tr) {
		if (!tr.verifySignature()) {
			ServiceBus.logger.log("Signature is not valid, Id : " + tr.getTransctionId(), Severity.ERROR);
			return false;
		}		
		
		if (tr instanceof StateDataTransaction) {
			if (this.isDataTransactionValidEx((StateDataTransaction)tr)){
				return this.applyDataTransaction((StateDataTransaction)tr);
			}
		}
		else if (tr instanceof StateTransferTransaction) {
			if (this.isTransferTransactionValidEx((StateTransferTransaction)tr)) {
				return this.applyTransferTransaction((StateTransferTransaction)tr);
			}
		}
		else if (tr instanceof StateRuleTransaction) {
			if (this.isRuleTransactionValidEx((StateRuleTransaction)tr)) {
				return this.applyRuleTransaction((StateRuleTransaction)tr);
			}
		}
		else {
			ServiceBus.logger.log("Unknown transaction, Id : " + tr.getTransctionId() , Severity.WARNING);
		}
		return false;		
	}
	
	// applying the data transaction to the state after validation
	protected boolean applyDataTransaction(StateDataTransaction tr) {
		String address = ((StateDataTransaction)tr).address;
		String newData = ((StateDataTransaction)tr).newValue;

		for(AccountBase account: accounts) {
			if (account.getAddress().equals(address)){
				account.setData(newData);
				account.increaseNonce();							
			}
		}
		return true;
	}
	
	// applying the transfer transaction to the state after validation
	protected boolean applyTransferTransaction(StateTransferTransaction tr) {
		String fromAddress = ((StateTransferTransaction)tr).from;
		String toAddress = ((StateTransferTransaction)tr).to;				
		Double amount = ((StateTransferTransaction)tr).amount;

		for(AccountBase account: accounts) {
			if (account.getAddress().equals(fromAddress)){
				account.decreaseBalance(amount);
				account.decreaseBalance(amount);							
			}
		}

		for(AccountBase account: accounts) {
			if (account.getAddress().equals(toAddress)){
				account.decreaseBalance(amount);
			}
		}
		return true;
	}

	// applying the rule transaction to the state after validation
	protected boolean applyRuleTransaction(StateRuleTransaction tr) {	
		String ruleCode = ((StateRuleTransaction)tr).code;
		SimpleRule rule = new SimpleRule(ruleCode);
		
		boolean isConditionValid = false;
		for(AccountBase account: accounts) {
			if (account.getAddress().equals(rule.account_condition)){
				isConditionValid = rule.validateOperand(account.data); 
			}
		}

		if (isConditionValid) {
			for(AccountBase account: accounts) {
				if (account.getAddress().equals(rule.account_effect)){
					account.data = rule.value_effect;
					account.increaseNonce();							
				}
			}
		}
		return true;	
	}	
}
