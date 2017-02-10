package core;

import java.net.Socket;

public interface ClientInterface {
	/**
	 * Gets the address.
	 *
	 * @return the address
	 * @throws RemoteException the remote exception
	 */
	public Socket getAddress() throws Exception;

	/**
	 * Sets the sync state.
	 *
	 * @param state the new sync state
	 * @throws RemoteException the remote exception
	 */
	public void setSyncState(int state) throws Exception;

	/**
	 * Gets the sync state.
	 *
	 * @return the sync state
	 * @throws RemoteException the remote exception
	 */
	public String getSyncState() throws Exception;
}
