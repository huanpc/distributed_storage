package core;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import lipermi.net.Server;

// TODO: Auto-generated Javadoc
/**
 * The Interface FileServerInt.
 */
public interface ServerInterface{

	/**
	 * Connect.
	 *
	 * @param fileCI the file ci
	 * @return true, if successful
	 * @throws RemoteException the remote exception
	 */
	public boolean connect(ClientInterface fileCI) throws Exception;

	/**
	 * Disconnect.
	 *
	 * @param fileCI the file ci
	 * @throws RemoteException the remote exception
	 */
	public void disconnect(ClientInterface fileCI) throws Exception;

	/**
	 * Show sync state.
	 *
	 * @param fileCI the file ci
	 * @throws RemoteException the remote exception
	 */
	public void showSyncState(ClientInterface fileCI) throws Exception;

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
	public boolean isStart();

	/**
	 * Gets the server file.
	 *
	 * @return the server file
	 * @throws RemoteException the remote exception
	 */
	public File getServerFile();

	/**
	 * Sets the file.
	 *
	 * @param serverFie the new file
	 * @throws RemoteException the remote exception
	 */
	public void setFile(File serverFie);

	/**
	 * Gets the file output stream.
	 *
	 * @param f the f
	 * @return the file output stream
	 * @throws Exception the exception
	 */
	public OutputStream getFileOutputStream(String filePath) throws Exception;

	/**
	 * Gets the file input stream.
	 *
	 * @param f the f
	 * @return the file input stream
	 * @throws Exception the exception
	 */
	public InputStream getFileInputStream(String filePath) throws Exception;

	/**
	 * Gets the connected.
	 *
	 * @return the connected
	 * @throws RemoteException the remote exception
	 */
	public Vector getConnected();
	public Server getServer();
	public boolean isFileExist(String filePath);
	public boolean mkdirs(String filePath);
	public boolean isDirectory(String filePath);
	public String getListFileName(String filePath);
	public boolean deleteFile(String filePath);
	public long lastModified(String filePath);
	public boolean setLastModified(String filePath, long time);
	public void uploadFileToServer(String filePath, String stringToByte) throws Exception;
	public String downloadFileFromServer(String filePath) throws Exception;
	public File getFile(String filePath);
	public long getFileSize(String filePath);
}
