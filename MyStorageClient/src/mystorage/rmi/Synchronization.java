package mystorage.rmi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.joda.time.DateTimeUtils;

public class Synchronization implements Runnable {
	private static boolean isDone;
	public static int state = 0;
	private static File clientFile;
	private static File serverFile;
	private static FileServerInt server;
	private static FileClientInt client;

	public Synchronization(FileClientInt client, FileServerInt server,
			File serverFile, File clientFile, boolean isDone) {
		// TODO Auto-generated constructor stub
		this.client = client;
		this.server = server;
		this.clientFile = clientFile;
		this.serverFile = serverFile;
		this.isDone = isDone;
	}

	public static void synchronize(File source, File destination, boolean smart)
			throws Exception {
		synchronize(source, destination, smart,
				Constants.DEFAULT_COPY_BUFFER_SIZE);
	}

	public static void synchronize(File source, File destination,
			boolean smart, long chunkSize) throws Exception {

		if (chunkSize <= 0) {
			System.out
					.println("Chunk size must be positive: using default value.");
			chunkSize = Constants.DEFAULT_COPY_BUFFER_SIZE;
		}
		if (source.isDirectory()) {
			if (!destination.exists()) {
				if (!destination.mkdirs()) {
					throw new IOException("Could not create path "
							+ destination);
				}
			} else if (!destination.isDirectory()) {
				throw new IOException(
						"Source and Destination not of the same type:"
								+ source.getCanonicalPath() + " , "
								+ destination.getCanonicalPath());
			}
			String[] sources = source.list();
			Set<String> srcNames = new HashSet<String>(Arrays.asList(sources));
			String[] dests = destination.list();

			// delete files not present in source
			for (String fileName : dests) {
				if (!srcNames.contains(fileName)) {
					delete(new File(destination, fileName));
				}
			}
			// copy each file from source
			for (String fileName : sources) {
				File srcFile = new File(source, fileName);
				File destFile = new File(destination, fileName);
				synchronize(srcFile, destFile, smart, chunkSize);
			}
		} else {
			if (destination.exists() && destination.isDirectory()) {
				delete(destination);
			}

			copyFile(source, destination, chunkSize);
		}
	}

	private static void copyFile(File srcFile, File destFile, long chunkSize)
			throws Exception {
		InputStream is = null;
		OutputStream os = null;

		try {
			if (state == 1) { // state = 1 copy file from server to client
				is = server.getFileInputStream(srcFile);
				os = new FileOutputStream(destFile, false);
			} else if (state == 2) { // state = 2,copy file from client to
										// server
				is = new FileInputStream(srcFile);
				os = server.getFileOutputStream(destFile);
			} else {
				// state = 0 do nothing
			}

			System.out.println("Using byte[] read/write");
			byte[] byteBuff = new byte[Constants.DEFAULT_COPY_BUFFER_SIZE];
			int len = 0;
			while ((len = is.read(byteBuff)) >= 0) {
				os.write(byteBuff, 0, len);
			}
		} finally {
			if (is != null) {
				is.close();
			}
			if (os != null) {
				os.close();
			}
		}

		boolean successTimestampOp = destFile.setLastModified(srcFile
				.lastModified());
		if (!successTimestampOp) {
			System.out
					.println("Could not change timestamp for {}. Index synchronization may be slow. "
							+ destFile);
		}
	}

	public static void delete(File file) {
		if (file.isDirectory()) {
			for (File subFile : file.listFiles()) {
				delete(subFile);
			}
		}
		if (file.exists()) {
			if (!file.delete()) {
				System.out.println("Could not delete {}" + file);
			}
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		boolean smart = true;
		try {
			while (!isDone) {
				if (clientFile.length() > serverFile.length()
						|| clientFile.lastModified() > serverFile
								.lastModified()) {
					// state = 1 sync from client to server
					state = 1;
					synchronize(clientFile, serverFile, smart);
				} else if (serverFile.length() > clientFile.length()
						|| clientFile.lastModified() < serverFile
								.lastModified()) {
					// state = 2 sync from server to client
					state = 2;
					System.out.println("Syncing from server to client");
					synchronize(serverFile, clientFile, smart);
				} else {
					// state = 0 do nothing
					state = 0;
				}

				client.setSyncState(state);
				server.showSyncState(client);
				Thread.sleep(10000);

				if (server.isStart())
					continue;
				else {
					System.out.println("Server just stopped");
					server.disconnect(client);
					break;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void stopSync() {
		System.out.println("Stop sync");
		DateTimeUtils.setCurrentMillisSystem();
		System.out.println("Current time" + DateTimeUtils.currentTimeMillis());
		isDone = true;
	}

}
