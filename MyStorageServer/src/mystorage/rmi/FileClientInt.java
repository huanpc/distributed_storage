package mystorage.rmi;

import java.net.InetAddress;
import java.rmi.Remote;
import java.rmi.RemoteException;

// TODO: Auto-generated Javadoc
/**
 * The Interface FileClientInt.
 */
public interface FileClientInt extends Remote {
	
	/**
	 * Gets the address.
	 *
	 * @return the address
	 * @throws RemoteException the remote exception
	 */
	public InetAddress getAddress() throws RemoteException;

	/**
	 * Sets the sync state.
	 *
	 * @param state the new sync state
	 * @throws RemoteException the remote exception
	 */
	public void setSyncState(int state) throws RemoteException;

	/**
	 * Gets the sync state.
	 *
	 * @return the sync state
	 * @throws RemoteException the remote exception
	 */
	public String getSyncState() throws RemoteException;
}
