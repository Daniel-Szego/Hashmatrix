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
		
		// creating account - only memory
		if (cliArgs.switchPresent("-createAccount")) {
			Account account = new Account();
			PrivateKey owner = account.generateAccount();
			System.out.println("Account has been generated");
			System.out.println("Address : " + CryptoUtil.getStringFromKey(account.getAddress()));
			System.out.println("Owner priv key : " + CryptoUtil.getStringFromKey(owner));		
		}
		
		// creating a transaction
		if (cliArgs.switchPresent("-createTransaction")) {
			if (cliArgs.switchPresent("-state")) {
				String address = cliArgs.switchValue("-address");
				String newValue = cliArgs.switchValue("-value");
				
				
			}
			else if (cliArgs.switchPresent("-transfer")){
				
			}
			else {
				throw new RuntimeException(new Exception("invalid arguments transaction"));
			}
		}

	}
}
