package mystorage.rmi;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

// TODO: Auto-generated Javadoc
/**
 * The Interface FileServerInt.
 */
public interface FileServerInt extends Remote {
	
	/**
	 * Connect.
	 *
	 * @param fileCI the file ci
	 * @return true, if successful
	 * @throws RemoteException the remote exception
	 */
	public boolean connect(FileClientInt fileCI) throws RemoteException;
	
	/**
	 * Disconnect.
	 *
	 * @param fileCI the file ci
	 * @throws RemoteException the remote exception
	 */
	public void disconnect(FileClientInt fileCI) throws RemoteException;

	/**
	 * Show sync state.
	 *
	 * @param fileCI the file ci
	 * @throws RemoteException the remote exception
	 */
	public void showSyncState(FileClientInt fileCI) throws RemoteException;

	/**
	 * Start.
	 *
	 * @throws Exception the exception
	 */
	public void start() throws Exception;

	/**
	 * Stop.
	 *
	 * @throws Exception the exception
	 */
	public void stop() throws Exception;

	/**
	 * Checks if is start.
	 *
	 * @return true, if is start
	 * @throws RemoteException the remote exception
	 */
	public boolean isStart() throws RemoteException;

	/**
	 * Gets the server file.
	 *
	 * @return the server file
	 * @throws RemoteException the remote exception
	 */
	public File getServerFile() throws RemoteException;

	/**
	 * Sets the file.
	 *
	 * @param serverFie the new file
	 * @throws RemoteException the remote exception
	 */
	public void setFile(File serverFie) throws RemoteException;

	/**
	 * Gets the file output stream.
	 *
	 * @param f the f
	 * @return the file output stream
	 * @throws Exception the exception
	 */
	public OutputStream getFileOutputStream(File f) throws Exception;

	/**
	 * Gets the file input stream.
	 *
	 * @param f the f
	 * @return the file input stream
	 * @throws Exception the exception
	 */
	public InputStream getFileInputStream(File f) throws Exception;

	/**
	 * Gets the connected.
	 *
	 * @return the connected
	 * @throws RemoteException the remote exception
	 */
	public Vector getConnected() throws RemoteException;
}
