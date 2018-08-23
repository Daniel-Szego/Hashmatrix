package State;
import java.io.Serializable;
import java.security.*;
import java.security.spec.ECGenParameterSpec;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;

import Crypto.CryptoUtil;
import Crypto.StringUtil;
import Utils.LoggerConsole;
import Utils.Severity;
import ServiceBus.*;

// general account base
public class AccountBase implements Serializable, AccountInterface {
	
	// address 
	protected final String address; 
	protected int nonce;
	protected String data;
	protected double balance;
	protected char[] type;
	
	// default constructor
	public AccountBase(String _address) {
		this.address = _address;
		this.nonce = 0;
		this.data = null;
		this.balance = 0;
		this.type = new char[] {'D','F','T'};
	}
	
	// constructor used in copy
	public AccountBase(String _address, int _nonce, String _data, double _balance,  char[] _type) {
		this.address = _address;
		this.nonce = _nonce;
		this.data = _data;
		this.balance = _balance;
		this.type = _type;
	}
	
	
	public String getAddress() {
		if (this.address == null)
			ServiceBus.logger.log("Account address is queried before initializing", Severity.ERROR);
		else
			return address;	
	return null;	
	}
	
	// getting data associated with the account
	public String getData() {
		if (this.data == null)
			ServiceBus.logger.log("Account data is queried before initializing", Severity.WARNING);
		else
			return this.data;	
	return null;		
	}

	// setting new data value
	public void setData(String data) {
		this.data = data;
	}

	// getting the balance 
	public double getBalance() {
		return balance;
	}
	
	// increasing balance
	public void increaseBalance(double amount) {
		balance += amount;
	}

	// decreasing balance
	public void decreaseBalance(double amount) {
		balance += amount;
	}
		
	// gettig the asset type
	public char[] getAssetType() {
		return type;
	}

	// setting the data type
	public void setAssetType(String typeDescription) {
		// experimental implementation
		char[] chars = typeDescription.substring(0, 2).toCharArray();
		type = chars;
	}
	
	// getting the nonce
	public int getNonce() {
		return nonce;
	}
	
	// increasing nonce
	public void increaseNonce() {
		nonce++;
	}	
	
	public AccountBase copyAccount() {
		AccountBase copy = new AccountBase(
				this.address, 
				this.nonce,
				this.data,
				this.balance,
				this.type);
		return copy;
	}
}
