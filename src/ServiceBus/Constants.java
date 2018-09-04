package ServiceBus;

// Summary of constants related to the system
public class Constants {
	
	// maximum transactions at a block
	public static int MaxTransactionInABlock = 100;
	
	// first default valus of the difficulty for testing
	public static int Difficulty = 2;
	
	// blocktime in seconds
	public static int BlockTime = 150;
	
	//default port standard
	public static int DefaultPort = 8425;

	//default port standard extended
	public static int DefaultPortExt = 8426;

	// naming basis for remote interface
	public static String ServerNameBase = "BlockchainInterface";
	
	// maximum peer number
	public static int MaxPeers = 1;
	
	// version of the client
	public String ClientVersion = "0.0.2";

}
