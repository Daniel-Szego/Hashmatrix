package Explorer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import Node.*;
import Transaction.*;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;

import Block.*;
import CLI.Cli;

import javax.swing.JButton;  


public class ExplorerUI extends Frame {
	public final Node node;
	Panel panel;
	Label lblBlocks = new Label("Blocks : ");
	Label lblTransaction = new Label("Transaction : ");
	List lstBlocks = new List();
	List lstTransactions = new List();
	
	public ExplorerUI(Node _node) {
		node = _node;
		setTitle("Explorer");
		setSize(800,600);//frame size 300 width and 300 height  
		lblBlocks.setBounds(20, 50, 100, 20);
		this.add(lblBlocks);
		lstBlocks.setBounds(20, 80, 500, 200);
		this.add(lstBlocks);
		lblTransaction.setBounds(20, 300, 100, 20);
		this.add(lblTransaction);
		lstTransactions.setBounds(20, 330, 500, 200);		
		this.add(lstTransactions);
		
		setLayout(null);//no layout manager  
		setVisible(true);//now frame will be visible, by default not visible
	}
	
	public void addBlock(BlockBase _block) {
		lstBlocks.add("Block added " + _block.blockId);
	}

	public void addTransaction(StateTransaction _transaction) {
		lstTransactions.add("Transaction added" + _transaction.getTransctionId());
	}

	
}
