package CLI;
import java.security.*;

import Crypto.CryptoUtil;
import State.*;

// command line interface
public class Cli {

	public static void main(String[] args) {
		CliArgs cliArgs = new CliArgs(args);
		
		// starting security provider, not sure if this is the right place
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		
		// creating account
		if (cliArgs.switchPresent("-createAccount")) {
			Account account = new Account();
			PrivateKey owner = account.generateAccount();
			System.out.println("Account has been generated");
			System.out.println("Address : " + CryptoUtil.getStringFromKey(account.getAddress()));
			System.out.println("Owner priv key : " + CryptoUtil.getStringFromKey(owner));		
		}	
	}
}
