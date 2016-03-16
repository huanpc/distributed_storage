package mystorage.rmi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ListIterator;
import java.util.Vector;

import com.healthmarketscience.rmiio.SerializableInputStream;
import com.healthmarketscience.rmiio.SerializableOutputStream;

// TODO: Auto-generated Javadoc
/**
 * The Class FileServerImpl.
 */
public class FileServerImpl extends UnicastRemoteObject implements
		FileServerInt {
	
	/** The rmi registry. */
	public Registry rmiRegistry;
	
	/** The conn vec. */
	public Vector connVec = new Vector();
	
	/** The server file. */
	public File serverFile;
	
	/** The is start. */
	public boolean isStart = false;

	/**
	 * Instantiates a new file server impl.
	 *
	 * @param serverFile the server file
	 * @throws RemoteException the remote exception
	 */
	protected FileServerImpl(File serverFile) throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
		this.serverFile = serverFile;
	}

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see mystorage.rmi.FileServerInt#connect(mystorage.rmi.FileClientInt)
	 */
	@Override
	public boolean connect(FileClientInt fileCI) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println(fileCI.getAddress() + " got connected!");
		return true;
	}
	
	/* (non-Javadoc)
	 * @see mystorage.rmi.FileServerInt#disconnect(mystorage.rmi.FileClientInt)
	 */
	@Override
	public void disconnect(FileClientInt fileCI) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println(fileCI.getAddress() + " just disconnected");
	}

	/* (non-Javadoc)
	 * @see mystorage.rmi.FileServerInt#showSyncState(mystorage.rmi.FileClientInt)
	 */
	@Override
	public void showSyncState(FileClientInt fileCI) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println(fileCI.getSyncState());
	}

	/* (non-Javadoc)
	 * @see mystorage.rmi.FileServerInt#start()
	 */
	@Override
	public void start() throws Exception {
		// TODO Auto-generated method stub
		// set the RMI
		isStart = true;
		rmiRegistry = LocateRegistry.createRegistry(Constants.port);
		rmiRegistry.rebind(Constants.connName, this);
		System.out.println("Server started");
	}

	/* (non-Javadoc)
	 * @see mystorage.rmi.FileServerInt#stop()
	 */
	@Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
		isStart = false;
		rmiRegistry.unbind(Constants.connName);
		unexportObject(this, true);
		unexportObject(rmiRegistry, true);
		System.out.println("Server stopped");
	}

	/* (non-Javadoc)
	 * @see mystorage.rmi.FileServerInt#getConnected()
	 */
	@Override
	public Vector getConnected() throws RemoteException {
		// TODO Auto-generated method stub
		return connVec;
	}

	/* (non-Javadoc)
	 * @see mystorage.rmi.FileServerInt#getServerFile()
	 */
	@Override
	public File getServerFile() throws RemoteException {
		// TODO Auto-generated method stub
		return serverFile;
	}

	/* (non-Javadoc)
	 * @see mystorage.rmi.FileServerInt#setFile(java.io.File)
	 */
	@Override
	public void setFile(File serverFile) throws RemoteException {
		// TODO Auto-generated method stub
		this.serverFile = serverFile;
	}

	/* (non-Javadoc)
	 * @see mystorage.rmi.FileServerInt#getFileOutputStream(java.io.File)
	 */
	@Override
	public OutputStream getFileOutputStream(File f) throws Exception {
		// TODO Auto-generated method stub
		return new SerializableOutputStream(new FileOutputStream(f));
	}

	/* (non-Javadoc)
	 * @see mystorage.rmi.FileServerInt#getFileInputStream(java.io.File)
	 */
	@Override
	public InputStream getFileInputStream(File f) throws Exception {
		// TODO Auto-generated method stub
		return new SerializableInputStream(new FileInputStream(f));
	}

	/* (non-Javadoc)
	 * @see mystorage.rmi.FileServerInt#isStart()
	 */
	@Override
	public boolean isStart() throws RemoteException {
		// TODO Auto-generated method stub
		return isStart;
	}
}
