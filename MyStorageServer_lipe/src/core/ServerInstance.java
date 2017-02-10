package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Vector;

import javax.imageio.ImageIO;

import com.healthmarketscience.rmiio.SerializableInputStream;
import com.healthmarketscience.rmiio.SerializableOutputStream;

import lipermi.handler.CallHandler;
import lipermi.net.IServerListener;
import lipermi.net.Server;

import org.apache.commons.codec.binary.Base64;

public class ServerInstance implements ServerInterface {
	Server server;
	// ServerListener serverListener;
	/** The conn vec. */
	public Vector connVec = new Vector();

	/** The server file. */
	public File serverFile;

	/** The is start. */
	public boolean isStart = false;

	private final String ENCODING = "UTF-8";

	/**
	 * Instantiates a new file server impl.
	 *
	 * @param serverFile
	 *            the server file
	 * @throws RemoteException
	 *             the remote exception
	 */
	public ServerInstance(File serverFile) {
		super();
		// TODO Auto-generated constructor stub
		this.serverFile = serverFile;
	}

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see mystorage.rmi.FileServerInt#connect(mystorage.rmi.FileClientInt)
	 */
	@Override
	public boolean connect(ClientInterface fileCI) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(fileCI.getAddress() + " got connected!");
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mystorage.rmi.FileServerInt#disconnect(mystorage.rmi.FileClientInt)
	 */
	@Override
	public void disconnect(ClientInterface fileCI) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(fileCI.getAddress() + " just disconnected");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mystorage.rmi.FileServerInt#showSyncState(mystorage.rmi.FileClientInt)
	 */
	@Override
	public void showSyncState(ClientInterface fileCI) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(fileCI.getSyncState());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mystorage.rmi.FileServerInt#start()
	 */
	@Override
	public void start() throws Exception {
		// TODO Auto-generated method stub
		// set the RMI
		isStart = true;
		//
		System.setProperty("java.rmi.server.hostname", Constants.SERVER_IP);
		CallHandler callHandler = new CallHandler();
		callHandler.registerGlobal(ServerInterface.class, this);
		callHandler.exportObject(ServerInterface.class, this);
		server = new Server();
		server.bind(Constants.port, callHandler);
		System.out.println("Server started");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mystorage.rmi.FileServerInt#stop()
	 */
	@Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
		isStart = false;
		server.close();
		// server.removeServerListener(serverListener);
		System.out.println("Server stopped");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mystorage.rmi.FileServerInt#getConnected()
	 */
	@Override
	public Vector getConnected() {
		// TODO Auto-generated method stub
		return connVec;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mystorage.rmi.FileServerInt#getServerFile()
	 */
	@Override
	public File getServerFile() {
		// TODO Auto-generated method stub
		return serverFile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mystorage.rmi.FileServerInt#setFile(java.io.File)
	 */
	@Override
	public void setFile(File serverFile) {
		// TODO Auto-generated method stub
		this.serverFile = serverFile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mystorage.rmi.FileServerInt#getFileOutputStream(java.io.File)
	 */
	@Override
	public OutputStream getFileOutputStream(String filePath) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Sync file from client: " + filePath);
		return new SerializableOutputStream(new FileOutputStream((new File(filePath))));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mystorage.rmi.FileServerInt#getFileInputStream(java.io.File)
	 */
	@Override
	public InputStream getFileInputStream(String filePath) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Sync file from server: " + filePath);
		return new SerializableInputStream(new FileInputStream((new File(filePath))));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mystorage.rmi.FileServerInt#isStart()
	 */
	@Override
	public boolean isStart() {
		// TODO Auto-generated method stub
		return isStart;
	}

	@Override
	public Server getServer() {
		return server;
	}

	@Override
	public boolean isFileExist(String filePath) {
		// TODO Auto-generated method stub
		return new File(filePath).exists();
	}

	@Override
	public boolean mkdirs(String filePath) {
		// TODO Auto-generated method stub
		return new File(filePath).mkdirs();
	}

	@Override
	public boolean isDirectory(String filePath) {
		// TODO Auto-generated method stub
		return new File(filePath).isDirectory();
	}

	@Override
	public String getListFileName(String filePath) {
		// TODO Auto-generated method stub
		// return new File(filePath).list();
		String joinedString = String.join(";", new File(filePath).list());
		return joinedString;
	}

	@Override
	public boolean deleteFile(String filePath) {
		// TODO Auto-generated method stub
		File f = new File(filePath);
		if(f.isDirectory()){
			for (File file : f.listFiles()) {
				deleteFile(file.getAbsolutePath());
			}
			return f.delete();
		}else{
			return f.delete(); 
		}		
//		return new File(filePath).delete();
	}

	@Override
	public long lastModified(String filePath) {
		// TODO Auto-generated method stub
		return new File(filePath).lastModified();
	}

	@Override
	public boolean setLastModified(String filePath, long time) {
		// TODO Auto-generated method stub
		return new File(filePath).setLastModified(time);
	}

	@Override
	public File getFile(String filePath) {
		// TODO Auto-generated method stub
		return new File(filePath);
	}

	@Override
	public long getFileSize(String filePath) {
		// TODO Auto-generated method stub
		return new File(filePath).length();
	}

	@Override
	public void uploadFileToServer(String filePath, String stringToByte) throws Exception {
		// TODO Auto-generated method stub
		OutputStream os = null;
		try {
			byte[] imageByteArray = Base64.decodeBase64(stringToByte);
			// Write a image byte array into file system
			FileOutputStream imageOutFile = null;
			try {
				imageOutFile = new FileOutputStream(new File(filePath));
				imageOutFile.write(imageByteArray);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (imageOutFile != null)
					try {
						imageOutFile.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		} finally {
			if (os != null) {
				os.close();
			}
		}
	}

	@Override
	public String downloadFileFromServer(String filePath) throws Exception {
		InputStream is = null;
		try {
			try {
				File f = new File(filePath);
				FileInputStream fileInputStream = new FileInputStream(f);
				byte imageData[] = new byte[(int) f.length()];
				fileInputStream.read(imageData);
				// Converting Image byte array into Base64 String
				String imageDataString = Base64.encodeBase64URLSafeString(imageData);
				return imageDataString;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} finally {
			if (is != null) {
				is.close();
			}
		}
		return " ";
	}

}
