package Wallet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import Node.*;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;

import CLI.Cli;

import javax.swing.JButton;  


// minimal experimenatl wallet User Interface
public class WalletUI extends Frame implements ActionListener {
	public final Node node;
	Panel panel;
	Label lblSync = new Label("Blockchain Syncing : ");
	Label lblSyncValue = new Label();
	Label lblBalance = new Label("Wallet Balance : ");
	Label lblBalanceValue = new Label();
	Label lblAccounts = new Label ("Accounts of the wallet : ");
	List lstAccount = new List();
	Label lblTransfer = new Label ("Transfer : ");
	TextField txtTransferValue = new TextField();
	Button btnTransfer = new Button("Data");
	Label lblLog = new Label("Log : ");
	List lstLog = new List();
		
	public WalletUI(Node _node) {
		node = _node;
		setTitle("Wallet UI");
		setSize(800,600);//frame size 300 width and 300 height  
		lblSync.setBounds(20, 50, 100, 20);
		this.add(lblSync);
		lblSyncValue.setBounds(120, 50, 100, 20);
		this.add(lblSyncValue);
		lblBalance.setBounds(20, 80, 100, 20);
		this.add(lblBalance);
		lblBalanceValue.setBounds(120, 80, 100, 20);
		this.add(lblBalanceValue);
		lblAccounts.setBounds(20, 110, 150, 20);
		this.add(lblAccounts);
		lstAccount.setBounds(170, 110, 550, 100);
		this.add(lstAccount);
		lblTransfer.setBounds(20, 220, 100, 20);
		this.add(lblTransfer);
		txtTransferValue.setBounds(120, 220, 100, 20);
		this.add(txtTransferValue);
		btnTransfer.setBounds(20, 250, 60, 20);
		btnTransfer.addActionListener(this);
		this.add(btnTransfer);
		this.addWindowListener(new WindowAdapter(){
			  public void windowClosing(WindowEvent we){
			    System.exit(0);
			  }
			});
		lblLog.setBounds(20, 280, 150, 20);
		this.add(lblLog);
		lstLog.setBounds(170, 310, 550, 100);
		this.add(lstLog);
	
		init();
		setLayout(null);//no layout manager  
		setVisible(true);//now frame will be visible, by default not visible
	}
	
	public void init() {
		float walletBalance =  node.wallet.getWalletBalance();
		lblBalanceValue.setText(String.valueOf(node.wallet.getWalletBalance()));
		ArrayList<AccountWallet> accounts = node.wallet.getAccounts();
		lstAccount.removeAll();
		for (AccountWallet account: accounts) {
			String accountAddress = account.account.getAddressString();
			String accountBalance = String.valueOf(account.account.accountBalance);
			String accountData = String.valueOf(account.account.accountData);
			String accountInfo = accountAddress + " data: " + accountData + " balance : " + accountBalance;
			lstAccount.add(accountInfo);
		}
		boolean isSynced = node.blockchain.isSynced;
		if (isSynced) 
			lblSyncValue.setText("Syncronized");
		else
			lblSyncValue.setText("Syncing");	
	}
	
	public void actionPerformed(ActionEvent e) {
		String str = e.getActionCommand();	
	    System.out.println("You clicked " + str + " button");  
		if (str.equals("Data")) {
			int selectedIndex = lstAccount.getSelectedIndex();
			AccountWallet account = node.wallet.getAccounts().get(selectedIndex);
			String newValue = txtTransferValue.getText();
			lstLog.add("Data setting started on : " + account.account.getAddressString() + " Data : " + newValue);
			
			String[] params2 = {"-createTransaction", "-state", "-address", account.account.getAddressString(), "-value", newValue};
			Cli.main(params2);	
			lstLog.add("Transaction initiated");

			String[] paramsm = {"-runMinerOne"};
			Cli.main(paramsm);	
			lstLog.add("Mined");

			init();			
		}
	}
		

	
}
