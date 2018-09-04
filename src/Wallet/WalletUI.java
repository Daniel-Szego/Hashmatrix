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
	public final NodeServiceBase node;
	Panel panel;
	Label lblSync = new Label("Blockchain Syncing : ");
	Label lblSyncValue = new Label();
	Label lblBalance = new Label("Wallet Balance : ");
	Label lblBalanceValue = new Label();
	Label lblAccounts = new Label ("Accounts of the wallet : ");
	List lstAccount = new List();
	Label lblNewData = new Label ("New Data : ");
	TextField txtNewDataValue = new TextField();
	Button btnNewData = new Button("Data");
	Label lblLog = new Label("Log : ");
	List lstLog = new List();
	Label lblTransferAccount = new Label("Transfer Account : ");
	TextField txtTransferAccountValue = new TextField();
	Label lblTransferAmount = new Label("Transfer Amount : ");  
	TextField txtTransferAmountValue = new TextField();
	Button btnTransfer = new Button("Transfer");
	
	public WalletUI(NodeServiceBase _node) {
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
		
		lblNewData.setBounds(20, 220, 100, 20);
		this.add(lblNewData);
		txtNewDataValue.setBounds(120, 220, 100, 20);
		this.add(txtNewDataValue);
		btnNewData.setBounds(20, 250, 60, 20);
		btnNewData.addActionListener(this);
		this.add(btnNewData);
		this.addWindowListener(new WindowAdapter(){
			  public void windowClosing(WindowEvent we){
			    System.exit(0);
			  }
			});

		lblTransferAccount.setBounds(20, 280, 100, 20);
		this.add(lblTransferAccount);
		txtTransferAccountValue.setBounds(120, 280, 500, 20);
		this.add(txtTransferAccountValue);	
		lblTransferAmount.setBounds(20,310, 100, 20);
		this.add(lblTransferAmount);		
		txtTransferAmountValue.setBounds(120, 310, 100, 20);
		this.add(txtTransferAmountValue);	
		btnTransfer.setBounds(20, 340, 100, 20);
		this.add(btnTransfer);
		btnTransfer.addActionListener(this);
		
		lblLog.setBounds(20, 370, 150, 20);
		this.add(lblLog);
		lstLog.setBounds(170, 370, 550, 100);
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
			String newValue = txtNewDataValue.getText();
			lstLog.add("Data setting started on : " + account.account.getAddressString() + " Data : " + newValue);
			
			String[] params2 = {"-createTransaction", "-state", "-address", account.account.getAddressString(), "-value", newValue};
			Cli.main(params2);	
			lstLog.add("Transaction initiated");

			String[] paramsm = {"-runMinerOne"};
			Cli.main(paramsm);	
			lstLog.add("Mined");

			init();			
		}
		else if (str.equals("Transfer")) {
			int selectedIndex = lstAccount.getSelectedIndex();
			AccountWallet account = node.wallet.getAccounts().get(selectedIndex);
			String amount = txtTransferAmountValue.getText();
			String toAccount = txtTransferAccountValue.getText();
			lstLog.add("Transfer transaction started on : " + account.account.getAddressString() + " Amount : " + amount);
			
			String[] params3 = {"-createTransaction", "-transfer", "-from", account.account.getAddressString(), "-to", toAccount,"-amount", amount};
			Cli.main(params3);	
			lstLog.add("Transaction initiated");
			
			String[] paramsm = {"-runMinerOne"};
			Cli.main(paramsm);	
			lstLog.add("Mined");

			init();				
		}
	}
		

	
}
