package gui;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import core.ServerInstance;
import lipermi.handler.CallLookup;
import lipermi.net.IServerListener;
import lipermi.net.IServerListener;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.awt.Rectangle;
import java.awt.Dimension;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
/**
 * 
 * @author huanpc
 *
 */
public class MainGUI extends JFrame {		
	
	private DefaultTableModel mIpModel;
	private JTable mFileTable;
	private JTable mIPTable;
	private Thread updateUIThread;
	private ServerListener mServerListener;
	/** The server. */
	ServerInstance mServer;
	public MainGUI(String rootFile) {
		initServer(rootFile);
		initComponent(rootFile);
		updateUIThread = new Thread(new UpdateUI(rootFile));
		updateUIThread.start();
		this.setVisible(true);
	}	
	
	private void initComponent(String rootFolder){
		// North component
		JScrollPane northScrollPane = new JScrollPane();
		northScrollPane.setPreferredSize(new Dimension(900, 100));								
		getContentPane().add(northScrollPane, BorderLayout.NORTH);
		mIPTable = new JTable();
		northScrollPane.setViewportView(mIPTable);
		final String[] ipTableTitle = new String[3];
		ipTableTitle[0] = "Type";
		ipTableTitle[1] = "IP";
		ipTableTitle[2] = "State";
		mIpModel = new DefaultTableModel();
		mIpModel.setColumnIdentifiers(ipTableTitle);
		String[] oneRow = {"Server", getServerIP(), "Started"};
		mIpModel.addRow(oneRow);	
		mIPTable.setModel(mIpModel);
		
		JButton btnJ = new JButton("Stop server");
		btnJ.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(mServer != null){
					try {
						mServer.stop();
						updateUIThread.interrupt();						
						mServer.getServer().removeServerListener(mServerListener);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		northScrollPane.setRowHeaderView(btnJ);
		
		initTableView();
		File f = new File(rootFolder);		
		initTreeView(f);		
		this.setSize(900, 550);
	}
	
	private void initServer(String rootFilePath){
		mServer = new ServerInstance(new File(rootFilePath));
		try {
			mServer.start();			
			mServerListener = new ServerListener();
			mServer.getServer().addServerListener(mServerListener);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initTreeView(final File rootFile){
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootFile.getAbsolutePath());
		File [] listSubFile = rootFile.listFiles(); 
		for (File file : listSubFile) {
			if(!file.isFile()){
				root.add(new DefaultMutableTreeNode(file.getName()));
			}				
		}
		loadDataForTableView(listSubFile);
		JTree tree = new JTree(root);
		DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
		JScrollPane scrollPane_1 = new JScrollPane(tree);
		scrollPane_1.setPreferredSize(new Dimension(200, 400));
		getContentPane().add(scrollPane_1, BorderLayout.WEST);

		final JLabel selectedLabel = new JLabel();
		selectedLabel.setPreferredSize(new Dimension(900, 20));
		getContentPane().add(selectedLabel, BorderLayout.SOUTH);

		tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				if(e.getPath().getLastPathComponent().toString().equals(rootFile.getAbsolutePath())){
					selectedLabel.setText(rootFile.getName());							
					File[] allSubFiles = rootFile.listFiles();
					loadDataForTableView(allSubFiles);
				}else{
					selectedLabel.setText(rootFile.getName()+File.separator + e.getPath().getLastPathComponent().toString());		
					File f = new File(rootFile.getAbsolutePath()+File.separator + e.getPath().getLastPathComponent().toString());
					File[] allSubFiles = f.listFiles();
					loadDataForTableView(allSubFiles);
				}				
			}
		});
		
	}	
	
	private void initTableView(){
		// Center component	
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(700, 400));
		scrollPane.setSize(new Dimension(900, 400));
		scrollPane.setBounds(new Rectangle(0, 0, 0, 400));
		getContentPane().add(scrollPane, BorderLayout.CENTER);

		mFileTable = new JTable();
		scrollPane.setViewportView(mFileTable);				
	}
	private void loadDataForTableView(File[] allSubFiles){
		if(allSubFiles == null)
			return;
		final String[] title1 = new String[5];
		title1[0] = "Type";
		title1[1] = "Name";
		title1[2] = "Length";
		title1[3] = "Date Modified";
		title1[4] = "Path";

		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(title1);

		String[] array = new String[5];
		int i = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		for (File file : allSubFiles) {
			if (!file.isDirectory()) {
				Long length = file.length();
				array[0] = "File";
				array[1] = file.getName();
				array[2] = length.toString();
				array[3] = sdf.format(file.lastModified());
				array[4] = file.getPath();
				model.addRow(array);
			} else {
				array[0] = "Folder";
				array[1] = file.getName();
				array[2] = " ";
				array[3] = sdf.format(file.lastModified());
				array[4] = file.getPath();
				model.addRow(array);
			}
		}		
		mFileTable.setModel(model);
		mFileTable.getColumnModel().getColumn(0).setPreferredWidth(100);
		mFileTable.getColumnModel().getColumn(1).setPreferredWidth(200);
		mFileTable.getColumnModel().getColumn(2).setPreferredWidth(100);
		mFileTable.getColumnModel().getColumn(3).setPreferredWidth(100);
		mFileTable.getColumnModel().getColumn(4).setPreferredWidth(200);
	}
	private String getServerIP(){
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
	
	private void watchRootFileModify(String rootFile){
		 WatchService watcher;
		try {
			watcher = FileSystems.getDefault().newWatchService();
			Path dir = Paths.get(rootFile);
	        dir.register(watcher,StandardWatchEventKinds.ENTRY_MODIFY,StandardWatchEventKinds.ENTRY_CREATE,StandardWatchEventKinds.ENTRY_DELETE);	        
	        while (true) {
                WatchKey key;
                try {
                    key = watcher.take();
                } catch (InterruptedException ex) {
                    return;
                }
                 
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                     
                    @SuppressWarnings("unchecked")
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path fileName = ev.context();                    
                     
                    if (kind == StandardWatchEventKinds.ENTRY_MODIFY ||kind == StandardWatchEventKinds.ENTRY_CREATE||kind == StandardWatchEventKinds.ENTRY_DELETE) {
                        System.out.println(kind.name() + ": " + fileName);
                        loadDataForTableView((new File(rootFile).listFiles()));
                    }
                }
                 
                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}         
          
         System.out.println("Watch Service registered for dir: " + rootFile);
	}
	
	public static void main(String args[]) {
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(MainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(MainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(MainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(MainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		// </editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new MainGUI(null).setVisible(true);
			}
		});
	}
	
	private class UpdateUI implements Runnable{
		private String iRootFile;
		public UpdateUI(String rootFile) {
			iRootFile = rootFile;
		}
		@Override
		public void run() {
			watchRootFileModify(iRootFile);
		}
	}
	private class ServerListener implements IServerListener{
	    @Override
        public void clientDisconnected(Socket socket) {
            System.out.println("Client Disconnected: " + socket.getInetAddress());
        }

        @Override
        public void clientConnected(Socket socket) {
        	String[] oneRow = {"Client", socket.getInetAddress().toString(), "Connected!"};
    		mIpModel.addRow(oneRow);	
    		mIPTable.setModel(mIpModel);
            System.out.println("Client Connected: " + socket.getInetAddress());
        }
	}
}
