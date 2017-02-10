package gui;

import java.io.File;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.Enumeration;

import javax.swing.JFileChooser;

/**
 * The Class StartServerInterface.
 * @author HuanPC
 */
public class LoginGUI extends javax.swing.JFrame {

	/** The chooser. */
	JFileChooser chooser;
	
	// Variables declaration - do not modify
	/** The bt server folder. */
	private javax.swing.JButton btServerFolder;

	/** The bt start. */
	private javax.swing.JButton btStart;

	/** The bt stop. */
	private javax.swing.JButton btStop;

	/** The j label1. */
	private javax.swing.JLabel jLabel1;

	/** The j label2. */
	private javax.swing.JLabel jLabel2;

	/** The tf server folder. */
	private javax.swing.JTextField tfServerFolder;

	// End of variables declaration
	/**
	 * Creates new form StartServerInterface.
	 */
	public LoginGUI() {
		initComponents();
	}
	
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		btServerFolder = new javax.swing.JButton();
		tfServerFolder = new javax.swing.JTextField();
		btStart = new javax.swing.JButton();
		btStop = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		jLabel1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
		jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		jLabel1.setText("MY STORAGE SERVER");

		jLabel2.setText("Server Folder");

		btServerFolder.setText("Choose Server Folder");
		btServerFolder.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btServerFolderActionPerformed(evt);
			}
		});

		tfServerFolder.setText("C:\\ServerFolder");
		tfServerFolder.setEnabled(false);

		btStart.setText("Start");
		btStart.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					btStartActionPerformed(evt);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		btStop.setText("Stop");
		btStop.setEnabled(false);
		btStop.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					btStopActionPerformed(evt);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addGroup(
														layout.createSequentialGroup()
																.addGap(107,
																		107,
																		107)
																.addComponent(
																		jLabel1,
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		194,
																		javax.swing.GroupLayout.PREFERRED_SIZE))
												.addGroup(
														layout.createSequentialGroup()
																.addGap(41, 41,
																		41)
																.addComponent(
																		jLabel2,
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		79,
																		javax.swing.GroupLayout.PREFERRED_SIZE)
																.addGap(57, 57,
																		57)
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.LEADING,
																				false)
																				.addComponent(
																						tfServerFolder)
																				.addComponent(
																						btServerFolder,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						160,
																						Short.MAX_VALUE))))
								.addContainerGap(63, Short.MAX_VALUE))
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap(
										javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(btStart,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										70,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(76, 76, 76)
								.addComponent(btStop,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										70,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(90, 90, 90)));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addGap(38, 38, 38)
								.addComponent(jLabel1,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										27,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(41, 41, 41)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(
														jLabel2,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														24,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(btServerFolder))
								.addGap(18, 18, 18)
								.addComponent(tfServerFolder,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED,
										32, Short.MAX_VALUE)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(
														btStop,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														48,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(
														btStart,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														48,
														javax.swing.GroupLayout.PREFERRED_SIZE))
								.addGap(52, 52, 52)));

		pack();
	}// </editor-fold>

	/**
	 * Bt server folder action performed.
	 *
	 * @param evt
	 *            the evt
	 */
	private void btServerFolderActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
		chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("C:\\"));
		chooser.setDialogTitle("Choose Sync Folder");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			tfServerFolder.setText("" + chooser.getSelectedFile());
		} else {
			System.out.println("No Selection ");
		}
	}

	/**
	 * Bt start action performed.
	 *
	 * @param evt
	 *            the evt
	 * @throws Exception
	 *             the exception
	 */
	private void btStartActionPerformed(java.awt.event.ActionEvent evt)
			throws Exception {

		if (tfServerFolder.getText().equals("C:\\ServerFolder")) {
			File defaultFile = new File("C:\\ServerFolder");
			if (!defaultFile.exists()) {
				if (defaultFile.mkdir()) {
					System.out.println("Directory created!");
				} else {
					System.out.println("Failed create directory!");
				}
			}
		} else {
			new MainGUI(chooser.getSelectedFile().getAbsolutePath());
		}		
		this.setVisible(false);		
		btStart.setEnabled(false);
		btStop.setEnabled(true);
	}

	/**
	 * Bt stop action performed.
	 *
	 * @param evt
	 *            the evt
	 * @throws Exception
	 *             the exception
	 */
	private void btStopActionPerformed(java.awt.event.ActionEvent evt)
			throws Exception {
		// TODO add your handling code here:		
		btStart.setEnabled(true);
		btStop.setEnabled(false);
	}
	
	public static void main(String args[]) {		
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
					.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(
					LoginGUI.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(
					LoginGUI.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(
					LoginGUI.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(
					LoginGUI.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		}
		// </editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new LoginGUI().setVisible(true);

			}
		});
	}

}
