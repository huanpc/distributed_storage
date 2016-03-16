package mystorage.rmi;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

// TODO: Auto-generated Javadoc
/**
 * The Class FileClientImpl.
 */
public class FileClientImpl extends UnicastRemoteObject implements
		FileClientInt {

	/** The client address. */
	public InetAddress clientAddress;

	/** The client gui. */
	public StartClientInterface clientStartGUI;

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
	protected FileClientImpl(InetAddress clientAddress) throws RemoteException {
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
	public InetAddress getAddress() throws RemoteException {
		// TODO Auto-generated method stub
		return clientAddress;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mystorage.rmi.FileClientInt#setSyncState(int)
	 */
	@Override
	public void setSyncState(int state) throws RemoteException {
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
	public String getSyncState() throws RemoteException {
		// TODO Auto-generated method stub
		return syncState;
	}

	/**
	 * Sets the client gui.
	 *
	 * @param clientStartGUI the new client gui
	 */
	public void setClientGUI(StartClientInterface clientStartGUI) {
		this.clientStartGUI = clientStartGUI;
	}
}
