package ServiceBus;

import java.util.ArrayList;

import Block.*;
import Chain.BlockchainServiceBase;
import Chain.BockchainServiceInterface;
import Crypto.*;
import Miner.ValidatorServiceInterface;
import Node.*;
import Transaction.*;
import Utils.*;
import Wallet.WalletServiceInterface;

// experimental implementation of the service bus
public class ServiceBus {
	
	// logger is statis as at the deep level there is not necesserily reference to the bus
	public static LoggerInterface logger;
	// cryptointerface is the same
	public static CryptoInterface crypto;
	
	static {
		logger = new LoggerProvider();
		((LoggerProvider)logger).addLogger(new LoggerConsole());
		crypto = new CryptoProvider();
	}
	
	public final Node node;
	ArrayList<ServiceEvent> events;
	ArrayList<ServiceListenerInfo> listeners;
	
	// Named services
	public BockchainServiceInterface blockchainService;
	public WalletServiceInterface walletService;
	public NetworkServiceInterface networkService;
	public ValidatorServiceInterface minerService;
	
	public ServiceBus (Node _node) {
		this.node = _node;
		listeners = new ArrayList<ServiceListenerInfo>();
		events = new ArrayList<ServiceEvent>();
		
		// setting up services
		blockchainService = new BlockchainServiceBase(_node);
	}	

	public void addEvent(String _message, Service _source, Severity _severity, boolean async)  {
		ServiceEvent event = new ServiceEvent(_message,_source, _severity);
		events.add(event);
		
		// calling listeners
		for(ServiceListenerInfo listenerInfo: listeners) {
			if (listenerInfo.typeOfEvent == null) {
				if (!async) 				
					listenerInfo.serviceListener.EventRaised(event);
				else {				
					new Thread(new Runnable() {
						public void run() {
							listenerInfo.serviceListener.EventRaised(event);
						}
					}).start();	
				}				
			}	
		}
	}

	public void addEventBlockMined(String _message, Service _source, BlockBase _block , boolean async)  {
		ServiceEventBlockMined event = new ServiceEventBlockMined(_message,_source, _block);
		events.add(event);
				
		// calling listeners
		for(ServiceListenerInfo listenerInfo: listeners) {
			if (listenerInfo.typeOfEvent == event.getClass()){
				if (!async) 				
					listenerInfo.serviceListener.EventRaised(event);
				else {				
					new Thread(new Runnable() {
						public void run() {
							listenerInfo.serviceListener.EventRaised(event);
						}
					}).start();	
				}
			}
		}	
	}

	public void addEventBlockValidated(String _message, Service _source, BlockBase _block , boolean async)  {
		ServiceEventBlockValidated event = new ServiceEventBlockValidated(_message,_source, _block);
		events.add(event);
				
		// calling listeners
		for(ServiceListenerInfo listenerInfo: listeners) {
			if (listenerInfo.typeOfEvent == event.getClass()){
				if (!async) 				
					listenerInfo.serviceListener.EventRaised(event);
				else {				
					new Thread(new Runnable() {
						public void run() {
							listenerInfo.serviceListener.EventRaised(event);
						}
					}).start();	
				}
			}
		}	
	}

	
	public void addEventBlockReceived(String _message, Service _source, BlockBase _block , boolean async)  {
		ServiceEventBlockReceived event = new ServiceEventBlockReceived(_message,_source, _block);
		events.add(event);
				
		// calling listeners
		for(ServiceListenerInfo listenerInfo: listeners) {
			if (listenerInfo.typeOfEvent == event.getClass()){
				if (!async) 				
					listenerInfo.serviceListener.EventRaised(event);
				else {				
					new Thread(new Runnable() {
						public void run() {
							listenerInfo.serviceListener.EventRaised(event);
						}
					}).start();	
				}
			}
		}	
	}

	public void addEventTransactionReceived(String _message, Service _source, StateTransaction _transaction , boolean async)  {
		ServiceEventTransactionReceived event = new ServiceEventTransactionReceived(_message,_source, _transaction);
		events.add(event);
				
		// calling listeners
		for(ServiceListenerInfo listenerInfo: listeners) {
			if (listenerInfo.typeOfEvent == event.getClass()){
				if (!async) 				
					listenerInfo.serviceListener.EventRaised(event);
				else {				
					new Thread(new Runnable() {
						public void run() {
							listenerInfo.serviceListener.EventRaised(event);
						}
					}).start();	
				}
			}
		}	
	}
	
	public void addEventTransactionInitiated(String _message, Service _source, StateTransaction _transaction , boolean async)  {
		ServiceEventTransactionInitiated event = new ServiceEventTransactionInitiated(_message,_source, _transaction);
		events.add(event);
				
		// calling listeners
		for(ServiceListenerInfo listenerInfo: listeners) {
			if (listenerInfo.typeOfEvent == event.getClass()){
				if (!async) 				
					listenerInfo.serviceListener.EventRaised(event);
				else {				
					new Thread(new Runnable() {
						public void run() {
							listenerInfo.serviceListener.EventRaised(event);
						}
					}).start();	
				}
			}
		}	
	}

	public void addEventBlockchainSyncronized(String _message, Service _source, boolean async)  {
		ServiceEventBlockchainSyncronized event = new ServiceEventBlockchainSyncronized(_message,_source);
		events.add(event);
				
		// calling listeners
		for(ServiceListenerInfo listenerInfo: listeners) {
			if (listenerInfo.typeOfEvent == event.getClass()){
				if (!async) 				
					listenerInfo.serviceListener.EventRaised(event);
				else {				
					new Thread(new Runnable() {
						public void run() {
							listenerInfo.serviceListener.EventRaised(event);
						}
					}).start();	
				}
			}
		}	
	}

	
	public void addEvent(String _message, Service _source, Severity _severity){
		addEvent(_message, _source, _severity, true);
	}
	
	public void addEvent(String _message, Service _source)  {
		addEvent(_message, _source, Severity.INFO, true);
	}
	
	public void addEvent(String _message)  {
		addEvent(_message, null, Severity.INFO, true);
	}

	public void addServiceListener(ServiceListenerInfo _listenerInfo) {
		listeners.add(_listenerInfo);
	}
	
	
}
