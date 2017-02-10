package core;

import java.net.Socket;

import gui.LoginGUI;

public class ClientInstance implements ClientInterface{
	/** The client address. */
	public Socket clientAddress;

	/** The client gui. */
	public LoginGUI clientStartGUI;

	/** The sync state. */
	public String syncState;

	/**
	 * Instantiates a new file client impl.
	 *
	 * @param clientAddress
	 *            the client address
	 * @throws RemoteException
	 *             the remote exception
	 */
	public ClientInstance(Socket clientAddress) throws Exception {
		super();
		// TODO Auto-generated constructor stub
		this.clientAddress = clientAddress;
	}

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see mystorage.rmi.FileClientInt#getAddress()
	 */
	@Override
	public Socket getAddress() throws Exception {
		// TODO Auto-generated method stub
		return clientAddress;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mystorage.rmi.FileClientInt#setSyncState(int)
	 */
	@Override
	public void setSyncState(int state) throws Exception {
		// TODO Auto-generated method stub
		if (state == 1) {
			syncState = "Uploading from " + getAddress() + " to server";
		} else if (state == 2) {
			syncState = "Downloading from server to " + getAddress();
		} else {
			syncState = "Server and " + getAddress() + " is synchronized";
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mystorage.rmi.FileClientInt#getSyncState()
	 */
	@Override
	public String getSyncState() throws Exception {
		// TODO Auto-generated method stub
		return syncState;
	}

	/**
	 * Sets the client gui.
	 *
	 * @param clientStartGUI the new client gui
	 */
	public void setClientGUI(LoginGUI clientStartGUI) {
		this.clientStartGUI = clientStartGUI;
	}
}
