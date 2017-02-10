package gui;

import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

import core.ClientInterface;
import core.ServerInterface;
import core.Synchronization;

// TODO: Auto-generated Javadoc
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * The Class MainClientInterface.
 *
 * @author HuanPC
 */
public class MainGUI extends JFrame {

	/** The icon_home. */
	ImageIcon icon_home = new ImageIcon(getClass().getResource("pic/home.png"));

	/** The icon_right. */
	ImageIcon icon_right = new ImageIcon(getClass().getResource(
			"pic/right-arrow.png"));

	/** The icon_left. */
	ImageIcon icon_left = new ImageIcon(getClass().getResource(
			"pic/left-arrow.png"));

	/** The is done. */
	private static boolean isDone = false;

	/** The jtable. */
	private JTable folderTable, fileTable;

	/** The jsp,jsp2,stateScrollPane. */
	private JScrollPane jsp, jsp2, stateScrollPane;

	/** The State JTextArea. */
	private static JTextArea stateArea;

	/** The buttons. */
	private JButton btnNext, btnPrev, btnSyn, btnAddFolder, btnAddFile, btnDel,
			btnHome;

	/** The current path. */
	private String currentPath = null;

	/** The current folder. */
	private String currentFolder = null;

	/** The lb folder. */
	private JLabel lbFolder;

	/** The is. */
	private InputStream is = null;

	/** The os. */
	private OutputStream os = null;

	/** The root file path. */
	private static String rootFilePath;

	/** The client. */
	private static ClientInterface client;

	/** The server. */
	private static ServerInterface server;

	/** The sync state. */
	private static String syncState = "Prepare to sync";

	/**
	 * Instantiates a new main client interface.
	 *
	 * @param rootFilePath
	 *            the root file path
	 * @param client
	 *            the client
	 * @param server
	 *            the server
	 * @throws RemoteException
	 *             the remote exception
	 */
	public MainGUI(final String rootFilePath, ClientInterface client,
			ServerInterface server, String serverIP) throws Exception {
		this.rootFilePath = rootFilePath;
		this.client = client;
		this.server = server;

		folderTable = new JTable();
		fileTable = new JTable();

		folderTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		loadTable(folderTable, rootFilePath);
		jsp = new JScrollPane(folderTable);
		jsp.setBounds(50, 122, 205, 277);
		jsp.setVisible(true);
		getContentPane().add(jsp);

		fileTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		loadFileTable(fileTable, rootFilePath);
		jsp2 = new JScrollPane(fileTable);
		jsp2.setBounds(272, 122, 422, 277);
		jsp2.setVisible(true);
		getContentPane().add(jsp2);

		btnHome = new JButton("");
		btnHome.setBounds(50, 23, 60, 60);
		btnHome.setIcon(icon_home);
		getContentPane().add(btnHome);

		btnNext = new JButton("");
		btnNext.setBounds(194, 23, 60, 60);
		btnNext.setIcon(icon_right);
		getContentPane().add(btnNext);

		btnPrev = new JButton("");
		btnPrev.setBounds(122, 23, 60, 60);
		btnPrev.setIcon(icon_left);
		getContentPane().add(btnPrev);

		btnSyn = new JButton("Sync");
		btnSyn.setBounds(706, 122, 120, 60);
		getContentPane().add(btnSyn);

		btnAddFolder = new JButton("New folder");
		btnAddFolder.setBounds(706, 194, 120, 30);
		getContentPane().add(btnAddFolder);

		btnAddFile = new JButton("New file");
		btnAddFile.setBounds(706, 236, 120, 30);
		getContentPane().add(btnAddFile);

		btnDel = new JButton("Delete");
		btnDel.setBounds(706, 286, 120, 30);
		getContentPane().add(btnDel);

		currentPath = rootFilePath;

		lbFolder = new JLabel("Current Path: " + currentPath);
		lbFolder.setBounds(272, 66, 370, 20);
		getContentPane().add(lbFolder);
		stateScrollPane = new JScrollPane();
		stateScrollPane.setBounds(50, 438, 644, 130);
		getContentPane().add(stateScrollPane);
		
				stateArea = new JTextArea();
				stateScrollPane.setViewportView(stateArea);
				stateArea.setText("");
				stateArea.setVisible(true);

		btnHome.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				loadTable(folderTable, rootFilePath);
				loadFileTable(fileTable, rootFilePath);
				currentPath = rootFilePath;
				lbFolder.setText(currentPath);
			}
		});

		btnNext.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int row = folderTable.getSelectedRow();
				if (row < 0) {
					JOptionPane.showMessageDialog(null,
							"You must choose folder", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else {
					String direct = (String) folderTable.getValueAt(row, 1);
					currentPath = direct;
					lbFolder.setText("Current Path: " + currentPath);
					loadTable(folderTable, currentPath);
					loadFileTable(fileTable, currentPath);
				}
			}
		});

		btnPrev.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(currentPath);
				if (System.getProperty("os.name").equalsIgnoreCase("window")) {
					if ((int) currentPath.charAt(currentPath.length() - 2) == 58) {
						JOptionPane.showMessageDialog(null,
								"This is a root path", "Error",
								JOptionPane.ERROR_MESSAGE);
					} else {
						currentPath = getPath(currentPath);
						lbFolder.setText("Current Path: " + currentPath);
						loadTable(folderTable, currentPath);
						loadFileTable(fileTable, currentPath);
					}
				} else {
					if (currentPath.length() == 1) {
						JOptionPane.showMessageDialog(null,
								"This is a root path", "Error",
								JOptionPane.ERROR_MESSAGE);
					} else {
						currentPath = getPath(currentPath);
						System.out.println(currentPath);
						lbFolder.setText("Current Path: " + currentPath);
						loadTable(folderTable, currentPath);
						loadFileTable(fileTable, currentPath);
					}
				}
			}
		});

		btnAddFolder.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String folderName = JOptionPane.showInputDialog(null,
						"Folder name: ");
				File f;
				if (System.getProperty("os.name").equalsIgnoreCase("linux")) {
					f = new File(currentPath + "/" + folderName);
				} else {
					f = new File(currentPath + "\\" + folderName);
				}

				if (f.mkdir()) {
					loadTable(folderTable, currentPath);
					loadFileTable(fileTable, currentPath);
				} else {
					JOptionPane.showMessageDialog(null,
							"Making folder was not successfull", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		btnAddFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser newFileChooser = new JFileChooser();
				while (true) {
					int returnValue = newFileChooser.showOpenDialog(null);
					if (returnValue == JFileChooser.APPROVE_OPTION) {
						try {
							String fileName = newFileChooser.getSelectedFile()
									.getName();
							String filePath = newFileChooser.getSelectedFile()
									.getPath();
							File aFile = new File(filePath);
							System.out.println("" + currentPath);
							File bFile = new File(currentPath, fileName);
							is = new FileInputStream(aFile);
							os = new FileOutputStream(bFile);
							byte[] buffer = new byte[1024];
							int length;
							// copy the file content in bytes
							while ((length = is.read(buffer)) > 0) {
								os.write(buffer, 0, length);
							}
							is.close();
							os.close();
							System.out.println("File copied successfull!");
							new File(rootFilePath).setLastModified(System.currentTimeMillis());
							loadTable(folderTable, currentPath);
							loadFileTable(fileTable, currentPath);
							break;
						} catch (FileNotFoundException ex) {
							Logger.getLogger(
									MainGUI.class.getName()).log(
									Level.SEVERE, null, ex);
						} catch (IOException ex) {
							Logger.getLogger(
									MainGUI.class.getName()).log(
									Level.SEVERE, null, ex);
						}
					} else {
						break;
					}
				}
			}
		});

		btnDel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int rowFolder = folderTable.getSelectedRow();
				int rowFile = fileTable.getSelectedRow();
				if (rowFolder < 0 && rowFile < 0) {
					JOptionPane.showMessageDialog(null,
							"You must choose a file or folder", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else {
					if (rowFile >= 0) {
						String pathDelFile = (String) fileTable.getValueAt(
								rowFile, 3);						
						File delFile = new File(pathDelFile);
						delFile.delete();
						loadTable(folderTable, currentPath);
						loadFileTable(fileTable, currentPath);
						
					}

					if (rowFolder >= 0) {
						String pathDelFolder = (String) folderTable.getValueAt(
								rowFolder, 1);

						File delFolder = new File(pathDelFolder);

						deleteFolder(delFolder);
						delFolder.delete();
						loadTable(folderTable, currentPath);
						loadFileTable(fileTable, currentPath);
					}
				}
			}
		});

		btnSyn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					if (isDone
							|| btnSyn.getText().equalsIgnoreCase("Stop Sync")) {
						stopSync();
						btnSyn.setText("Start Sync");
					} else if (!isDone
							|| btnSyn.getText().equalsIgnoreCase("Start Sync")) {
						startSync();
						btnSyn.setText("Stop Sync");
					}
				}catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		// Reload Jtable to show change of sync folder
		Timer timer = new Timer(5000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				loadFileTable(fileTable, currentPath);
				loadTable(folderTable, currentPath);
				setTextArea();
			}
		});
		timer.start();

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		JLabel lblLog = new JLabel("Log");
		lblLog.setBounds(50, 411, 70, 15);
		getContentPane().add(lblLog);
		
		JLabel lblFolder = new JLabel("Folder");
		lblFolder.setBounds(50, 95, 70, 15);
		getContentPane().add(lblFolder);
		
		JLabel lblHostIp = new JLabel("Host IP:");
		lblHostIp.setText("Host IP: "+getMyIP());
		lblHostIp.setBounds(272, 23, 318, 15);
		getContentPane().add(lblHostIp);
		
		JLabel lblServerIp = new JLabel("Server IP:");
		lblServerIp.setText("Server IP: "+serverIP);
		lblServerIp.setBounds(272, 50, 318, 15);
		getContentPane().add(lblServerIp);
		
		JLabel lblFile = new JLabel("File");
		lblFile.setBounds(272, 95, 70, 15);
		getContentPane().add(lblFile);
		this.setLocationRelativeTo(null);
		this.setSize(850, 600);
		this.setLocation(90, 0);
		this.setTitle("My Storage");
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setResizable(false);
	}

	/**
	 * Delete folder.
	 *
	 * @param delFolder
	 *            the del folder
	 * @return true, if successful
	 */
	public boolean deleteFolder(File delFolder) {
		if (delFolder.isDirectory()) {
			if (delFolder.list().length == 0) {
				delFolder.delete();
				return true;
			} else {
				File[] allSubFiles = delFolder.listFiles();
				for (File file : allSubFiles) {
					if (file.isDirectory()) {
						deleteFolder(file);
					} else {
						file.delete();
					}
				}
			}
		} else {
			delFolder.delete();
			return true;
		}
		return false;
	}

	/**
	 * Gets the path.
	 *
	 * @param currentPath
	 *            the current path
	 * @return the path
	 */
	public String getPath(String currentPath) {
		int endIndex = 0;
		if (System.getProperty("os.name").equalsIgnoreCase("window")) {
			for (int i = currentPath.length() - 1; i >= 0; i--) {
				if ((int) currentPath.charAt(i) == 92) {// dau \
					endIndex = i;
					break;
				}
			}
			if ((int) currentPath.charAt(endIndex - 1) == 58) {// dau :
				String checkPath = currentPath.substring(0, endIndex + 1);
				return checkPath;
			} else {
				String checkPath = currentPath.substring(0, endIndex);
				return checkPath;
			}
		} else {
			for (int i = currentPath.length() - 1; i >= 0; i--) {
				if ((int) currentPath.charAt(i) == 47) {// dau /
					endIndex = i;
					break;
				}
			}
			String checkPath = currentPath.substring(0, endIndex);
			return checkPath;
		}
	}
	private String getMyIP(){
		String ip = "192.168.1.1";
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface iface = interfaces.nextElement();
				if (!iface.getDisplayName().contains("wlan"))
					continue;
				// filters out 127.0.0.1 and inactive interfaces
				if (iface.isLoopback() || !iface.isUp())
					continue;

				Enumeration<InetAddress> addresses = iface.getInetAddresses();
				while (addresses.hasMoreElements()) {
					InetAddress addr = addresses.nextElement();
					ip = addr.getHostAddress();
					if (!ip.contains("192."))
						continue;
					System.out.println(iface.getDisplayName() + " " + ip);
				}
			}
		} catch (SocketException e) {
			throw new RuntimeException(e);
		}
		return ip;
	}
	/**
	 * Load table.
	 *
	 * @param tb
	 *            the tb
	 * @param ROOT_FILE_PATH
	 *            the root file path
	 */
	public void loadTable(JTable tb, String ROOT_FILE_PATH) {
		String[] title1 = new String[2];
		title1[0] = "Folder";
		title1[1] = "Path";

		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(title1);

		String[] link = new String[100];
		String[] linkName = new String[100];
		String[] array = new String[100];
		int i = 0;
		File f = new File(ROOT_FILE_PATH);
		File[] allSubFiles = f.listFiles();
		for (File file : allSubFiles) {
			if (file.isDirectory()) {
				link[i] = file.getAbsolutePath();
				linkName[i] = file.getName();
				array[0] = linkName[i];
				array[1] = link[i];
				i++;
				model.addRow(array);
				// Steps for directory
			}
		}
		tb.setModel(model);
		tb.getColumnModel().getColumn(0).setPreferredWidth(300);
		tb.getColumnModel().getColumn(1).setPreferredWidth(245);
	}

	/**
	 * Load file table.
	 *
	 * @param tb
	 *            the tb
	 * @param ROOT_FILE_PATH
	 *            the root file path
	 */
	public void loadFileTable(JTable tb, String ROOT_FILE_PATH) {
		String[] title1 = new String[4];
		title1[0] = "File name";
		title1[1] = "Length";
		title1[2] = "Date Modified";
		title1[3] = "Path";

		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(title1);

		String[] fileName = new String[100];
		String[] fileType = new String[100];
		String[] array = new String[100];
		int i = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		File f = new File(ROOT_FILE_PATH);
		File[] allSubFiles = f.listFiles();
		for (File file : allSubFiles) {
			if (!file.isDirectory()) {
				Long length = file.length();
				array[0] = file.getName();
				array[1] = length.toString();
				array[2] = sdf.format(file.lastModified());
				array[3] = file.getPath();
				model.addRow(array);
			}
		}
		tb.setModel(model);

		tb.getColumnModel().getColumn(0).setPreferredWidth(150);
		tb.getColumnModel().getColumn(1).setPreferredWidth(50);
		tb.getColumnModel().getColumn(2).setPreferredWidth(50);
		tb.getColumnModel().getColumn(3).setPreferredWidth(150);
	}

	/**
	 * Start sync.
	 * @throws Exception 
	 */
	private static void startSync() throws Exception {
//		if (server.connect(client)) {
			File clientFile = new File(rootFilePath);
			File serverFile = server.getServerFile();
			Synchronization sync = new Synchronization(client, server,
					serverFile, clientFile, isDone);
			appendLog("Start sync");
			new Thread(sync).start();
//		}
	}

	/**
	 * Stop sync.
	 *
	 * @throws RemoteException
	 *             the remote exception
	 */
	private static void stopSync() throws Exception {
		Synchronization.stopSync();
	}

	/**
	 * Gets the sync state.
	 *
	 * @return the sync state
	 * @throws RemoteException
	 *             the remote exception
	 */
	private static String getSyncState() throws Exception {
		if (Synchronization.state == 1) {
			syncState = "Downloading....";
		} else if (Synchronization.state == 2) {
			syncState = "Upload....";
		} else if (Synchronization.state == 0) {
			syncState = "Server and client is synchronized";
		}
		return syncState;
	}

	/**
	 * Sets the text area.
	 */
	public static void setTextArea() {
		try {
			stateArea.append(getSyncState() + "\n");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void appendLog(String log) {
		try {
			stateArea.append(log + "\n");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
