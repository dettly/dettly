package org.hlc.rpm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.prefs.Preferences;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import org.xml.sax.SAXException;

import org.hlc.rpm.infos.BasicInfo;
import org.hlc.rpm.infos.RPMFile;
import org.hlc.rpm.infos.RPMLink;
import org.hlc.rpm.infos.UserInfo;

public class RPMInstallUI extends JFrame {

	private RPMContent content = new RPMContent();

	private static final long serialVersionUID = -8519147578282992394L;

	// define the JPanel
	JPanel jPanel, titleJPanel, basicInforJPanel, userInforJPanel,
			fileInforJPanel, bottomJPanel, linkInforJPanel,
			installScriptJPanel, unInstallScriptJPanel, rpmDirectoryJPanel;
	// define the JLabel
	JLabel jLabelBackGround, titleJLabel, directoryJLabel, rpmDirectoryJLabel;
	// ������Ϣ
	JLabel basicTitleJLabel, nameJLabel, versionJLabel, releaseJLabel,
			providesJLabel, summaryJLabel, descriptionJLabel, groupJLabel,
			licenseJLabel;
	// �������Ϣ
	JLabel userTitleJLabel, packagerJLabel, vendorJLabel, urlJLabel,
			distributionJLabel;
	// �ļ���Ϣ
	JLabel fileTitleJLabel, destinationJLabel, prefixJLabel, pathJLabel,
			fileJLabel, filemodeJLabel, usernameJLabel, usergroupJLabel,
			targetJLabel;
	// ��װ�ű���ж�ؽű�
	JLabel preInstallScriptJLabel, postInstallScriptJLabel,
			preUninstallScriptJLabel, postUninstallScriptJLabel;
	// ������Ϣ
	JTextField nameJTextField, versionJTextField, releaseJTextField,
			providesJTextField, summaryJTextField, descriptionJTextField,
			groupJTextField, licenseJTextField, prefixJTextField,
			fileJTextField, filemodeJTextField, usernameJTextField,
			usergroupJTextField, rpmDirectoryJTextField;
	// �����Ϣ
	JTextField packagerJTextField, vendorJTextField, urlJTextField,
			pathJTextField, targetJTextField, distributionJTextField,
			preInstallScriptJTextField, postInstallScriptJTextField,
			preUninstallScriptJTextField, postUninstallScriptJTextField;
	// ��ť
	JButton cancelJButton, saveJButton, createRPMJButton, createFileJButton,
			deleteFileJButton, fileJDialogYesJButton, fileJDialogNoJButton,
			createLinkJButton, deleteLinkJButton, linkJDialogYesJButton,
			preInstallScriptJButton, postInstallScriptJButton,
			preUninstallScriptJButton, postUninstallScriptJButton,
			linkJDialogNoJButton, chooseJButton, rpmDirectoryJButton;
	// define the JScrollPane
	JScrollPane scrollFile, scrollLink;
	// define the JTextArea
	JTextArea fileJTextArea, linkJTextArea;
	// �ļ�ѡ����
	JFileChooser jFileChooser = null;
	JDialog jDialog, fileJDialog, linkJDialog;

	JTable fileJTable, linkJTable;
	DefaultTableModel fileModel, linkModel;

	BasicInfo basicInfor = new BasicInfo();
	UserInfo userInfor = new UserInfo();
	String sourcePath = null;// XML�ļ�·��
	String saveMark = null; // ���棬���Ϊ��־

	public RPMInstallUI() {
		super("Welcome to use RPM tools");
		// ���ر���ͼƬ
		ImageIcon background = new ImageIcon("./files/background.jpg");
		// �ѱ���ͼƬ��ʾ��һ����ǩ��
		jLabelBackGround = new JLabel(background);
		// ���ͼƬ��frame�ĵڶ���
		this.getLayeredPane().add(jLabelBackGround,
				new Integer(Integer.MIN_VALUE));
		// �ѱ�ǩ�Ĵ�Сλ������ΪͼƬ�պ����������
		jLabelBackGround.setBounds(0, 0, background.getIconWidth(),
				background.getIconHeight());
		// �˵���
		JMenu fileJMenu = new JMenu("�ļ�");
		JMenuItem open = new JMenuItem("��");
		open.setAccelerator(KeyStroke.getKeyStroke('O',
				java.awt.Event.CTRL_MASK, false));
		final JMenuItem saveAs = new JMenuItem("���Ϊ");
		saveAs.setAccelerator(KeyStroke.getKeyStroke('S',
				java.awt.Event.CTRL_MASK, false));
		// ��ʼ״̬���˵�������
		saveAs.setEnabled(false);
		JMenuItem exit = new JMenuItem("�˳�");
		exit.setAccelerator(KeyStroke.getKeyStroke('X',
				java.awt.Event.CTRL_MASK, false));
		fileJMenu.add(open);
		fileJMenu.addSeparator();
		fileJMenu.add(saveAs);
		fileJMenu.addSeparator();
		fileJMenu.add(exit);

		JMenu helpJMenu = new JMenu("����");
		JMenuItem help = new JMenuItem("ʹ�ð���");
		help.setAccelerator(KeyStroke.getKeyStroke('M',
				java.awt.Event.CTRL_MASK, false));
		helpJMenu.add(help);

		JMenuBar bar = new JMenuBar(); // ����һ���յĲ˵���
		bar.setOpaque(false);
		bar.add(fileJMenu);
		bar.add(helpJMenu);
		bar.setBounds(0, 0, 950, 30);
		// ��Ӳ˵�����JFrame��
		getContentPane().add(bar);

		/*
		 * ����¼�
		 */
		// ��XML�ļ�
		open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				saveAs.setEnabled(true);
				saveMark = "SAVE";
				// ��ȡ�ϴδ��ļ�·��
				Preferences pref = Preferences.userRoot().node(
						this.getClass().getName());
				String lastPath = pref.get("lastPath", "");
				if (!lastPath.equals("")) {
					jFileChooser = new JFileChooser(lastPath);
				} else {
					jFileChooser = new JFileChooser();
				}
				jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				// set the name of Button
				jFileChooser.setApproveButtonText("ȷ��");
				String title = jFileChooser.getDialogTitle();
				if (title == null)
					jFileChooser.getUI().getDialogTitle(jFileChooser);
				// �ļ�ѡ��������״̬
				int state = jFileChooser.showOpenDialog(null); // ��ʾ���ļ��Ի���

				// ��ȷ������ť����
				if (state == JFileChooser.APPROVE_OPTION) {
					File file = jFileChooser.getSelectedFile(); // �õ�ѡ����ļ�
					pref.put("lastPath", file.getPath());
					sourcePath = file.getPath();
					// ȡ��Դ�ļ��б�
					File sourcePathFile = new File(sourcePath);
					// ȡ���ļ�ȫ����������չ����
					String fileFullName = sourcePathFile.getName();
					String fileType = new String("");
					// ȡ���ļ���չ��
					fileType = fileFullName.substring(
							(fileFullName.length() - 4), fileFullName.length());
					// �ж��Ƿ�Ϊxml�ĵ�
					if (fileType.equals(".xml")) {
						try {
							// ��xml�ĵ�
							open(sourcePath);
						} catch (ParserConfigurationException e1) {
							e1.printStackTrace();
						} catch (SAXException e1) {
							e1.printStackTrace();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					} else {
						JOptionPane.showMessageDialog(null, "��ʽ����ȷ,������ѡ��");
					}
				}
			}
		});

		// ���Ϊ
		saveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// ��ȡ�ϴδ��ļ�·��
				Preferences pref = Preferences.userRoot().node(
						this.getClass().getName());
				String lastPath = pref.get("lastPath", "");
				if (!lastPath.equals("")) {
					jFileChooser = new JFileChooser(lastPath);
				} else {
					jFileChooser = new JFileChooser();
				}
				jFileChooser
						.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				jFileChooser.setApproveButtonText("ȷ��");
				String title = jFileChooser.getDialogTitle();
				String saveXMLPath = null;
				if (title == null)
					jFileChooser.getUI().getDialogTitle(jFileChooser);
				int state = jFileChooser.showOpenDialog(null); // ��ʾ���ļ��Ի���
				if (state == JFileChooser.APPROVE_OPTION) {
					File file = jFileChooser.getSelectedFile(); // �õ�ѡ����ļ�
					saveXMLPath = file.getPath();
					// ���content����
					content.clear();

					// ���������Ϣ
					basicInfor.setName(nameJTextField.getText());
					basicInfor.setVersion(versionJTextField.getText());
					basicInfor.setDescription(descriptionJTextField.getText());
					basicInfor.setGroup(groupJTextField.getText());
					basicInfor.setLicense(licenseJTextField.getText());
					basicInfor.setProvides(providesJTextField.getText());
					basicInfor.setRelease(releaseJTextField.getText());
					basicInfor.setSummary(summaryJTextField.getText());
					content.setBasicInfo(basicInfor);

					// ��������Ϣ
					userInfor.setDistribution(distributionJTextField.getText());
					userInfor.setPackager(packagerJTextField.getText());
					userInfor.setUrl(urlJTextField.getText());
					userInfor.setVendor(vendorJTextField.getText());
					content.setUserinfo(userInfor);

					// �����ļ���Ϣ
					int fileRows = fileJTable.getRowCount();
					for (int len = 0; len < fileRows; len++) {
						RPMFile rpmFile = new RPMFile();
						rpmFile.setPrefix(fileModel.getValueAt(len, 0)
								.toString());
						rpmFile.setFile(fileModel.getValueAt(len, 1).toString());
						rpmFile.setFilemode(fileModel.getValueAt(len, 2)
								.toString());
						rpmFile.setUsername(fileModel.getValueAt(len, 3)
								.toString());
						rpmFile.setGroup(fileModel.getValueAt(len, 4)
								.toString());
						content.addFile(rpmFile);
					}

					// �����ļ�������Ϣ
					int linkRows = linkJTable.getRowCount();
					for (int len = 0; len < linkRows; len++) {
						RPMLink rpmLink = new RPMLink();
						rpmLink.setPath(linkModel.getValueAt(len, 0).toString());
						rpmLink.setTarget(linkModel.getValueAt(len, 1)
								.toString());
						content.addLink(rpmLink);
					}

					// ���氲װ��ж�ؽű���Ϣ
					content.setPreInstallScript(preInstallScriptJTextField
							.getText());
					content.setPostInstallScript(postInstallScriptJTextField
							.getText());
					content.setPreUninstallScript(preUninstallScriptJTextField
							.getText());
					content.setPostUninstallScript(postUninstallScriptJTextField
							.getText());

					try {
						boolean flag = RPMContentStore.write(content,
								saveXMLPath + File.separator + "soft.xml");
						if (flag) {
							JOptionPane.showMessageDialog(null, "\t����ɹ���");
						} else {
							JOptionPane.showMessageDialog(null, "\t����ʧ�ܣ�");
						}
					} catch (TransformerConfigurationException e1) {
						e1.printStackTrace();
					} catch (ParserConfigurationException e1) {
						e1.printStackTrace();
					} catch (SAXException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		// ���˳����¼�����
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		// ������ť
		help.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String helpfileurl= "./files/�����ĵ�.CHM";
				try {
					Runtime.getRuntime().exec("hh "+helpfileurl);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		});
		
		
		// ��ȡframe�����ϲ����Ϊ�������䱳����ɫ
		jPanel = (JPanel) getContentPane();
		jPanel.setOpaque(false);// ����͸��
		jPanel.setLayout(null);

		/**
		 * ������Ϣ��ʾ name ,version ,release ,provides ,summary ,description ,group
		 * ,license
		 */
		basicInforJPanel = new JPanel();
		basicInforJPanel.setLayout(null);
		basicInforJPanel.setOpaque(false);
		basicInforJPanel.setSize(950, 580);

		nameJLabel = new JLabel("�������");
		nameJLabel.setFont(new Font("Dialog", 0, 17));
		nameJLabel.setOpaque(false);
		nameJLabel.setBounds(10, 20, 80, 30);
		nameJTextField = new JTextField(150);
		nameJTextField.setOpaque(false);
		nameJTextField.setBounds(100, 20, 500, 30);
		basicInforJPanel.add(nameJLabel);
		basicInforJPanel.add(nameJTextField);

		versionJLabel = new JLabel("  �汾��");
		versionJLabel.setFont(new Font("Dialog", 0, 17));
		versionJLabel.setOpaque(false);
		versionJLabel.setBounds(10, 60, 80, 30);
		versionJTextField = new JTextField(150);
		versionJTextField.setOpaque(false);
		versionJTextField.setBounds(100, 60, 500, 30);
		basicInforJPanel.add(versionJLabel);
		basicInforJPanel.add(versionJTextField);

		releaseJLabel = new JLabel("  �ͳ���");
		releaseJLabel.setFont(new Font("Dialog", 0, 17));
		releaseJLabel.setOpaque(false);
		releaseJLabel.setBounds(10, 100, 80, 30);
		releaseJTextField = new JTextField(150);
		releaseJTextField.setOpaque(false);
		releaseJTextField.setBounds(100, 100, 500, 30);
		basicInforJPanel.add(releaseJLabel);
		basicInforJPanel.add(releaseJTextField);

		providesJLabel = new JLabel("��Ҫ����");
		providesJLabel.setFont(new Font("Dialog", 0, 17));
		providesJLabel.setOpaque(false);
		providesJLabel.setBounds(10, 140, 80, 30);
		providesJTextField = new JTextField(150);
		providesJTextField.setOpaque(false);
		providesJTextField.setBounds(100, 140, 500, 30);
		basicInforJPanel.add(providesJLabel);
		basicInforJPanel.add(providesJTextField);

		summaryJLabel = new JLabel("�������");
		summaryJLabel.setFont(new Font("Dialog", 0, 17));
		summaryJLabel.setOpaque(false);
		summaryJLabel.setBounds(10, 190, 80, 30);
		summaryJTextField = new JTextField(150);
		summaryJTextField.setOpaque(false);
		summaryJTextField.setBounds(100, 190, 500, 30);
		basicInforJPanel.add(summaryJLabel);
		basicInforJPanel.add(summaryJTextField);

		descriptionJLabel = new JLabel("��ϸ����");
		descriptionJLabel.setFont(new Font("Dialog", 0, 17));
		descriptionJLabel.setOpaque(false);
		descriptionJLabel.setBounds(10, 230, 80, 30);
		descriptionJTextField = new JTextField(150);
		descriptionJTextField.setOpaque(false);
		descriptionJTextField.setBounds(100, 230, 500, 30);
		basicInforJPanel.add(descriptionJLabel);
		basicInforJPanel.add(descriptionJTextField);

		groupJLabel = new JLabel("������");
		groupJLabel.setFont(new Font("Dialog", 0, 17));
		groupJLabel.setOpaque(false);
		groupJLabel.setBounds(10, 270, 80, 30);
		groupJTextField = new JTextField(150);
		groupJTextField.setOpaque(false);
		groupJTextField.setBounds(100, 270, 500, 30);
		groupJTextField.setText("Applications/Editors");
		basicInforJPanel.add(groupJLabel);
		basicInforJPanel.add(groupJTextField);

		licenseJLabel = new JLabel("  ���֤  ");
		licenseJLabel.setFont(new Font("Dialog", 0, 17));
		licenseJLabel.setOpaque(false);
		licenseJLabel.setBounds(10, 310, 80, 30);
		licenseJTextField = new JTextField(150);
		licenseJTextField.setOpaque(false);
		licenseJTextField.setBounds(100, 310, 500, 30);
		licenseJTextField.setText("MIT");
		basicInforJPanel.add(licenseJLabel);
		basicInforJPanel.add(licenseJTextField);

		/**
		 * �����Ϣ��ʾ packager , verdor ,distribution ,url
		 */
		userInforJPanel = new JPanel();
		userInforJPanel.setLayout(null);
		userInforJPanel.setOpaque(false);
		userInforJPanel.setSize(950, 580);

		packagerJLabel = new JLabel("�����");
		packagerJLabel.setFont(new Font("Dialog", 0, 17));
		packagerJLabel.setOpaque(false);
		packagerJLabel.setBounds(10, 20, 80, 30);
		packagerJTextField = new JTextField(150);
		packagerJTextField.setOpaque(false);
		packagerJTextField.setBounds(100, 20, 500, 30);
		userInforJPanel.add(packagerJLabel);
		userInforJPanel.add(packagerJTextField);

		vendorJLabel = new JLabel("������");
		vendorJLabel.setFont(new Font("Dialog", 0, 17));
		vendorJLabel.setOpaque(false);
		vendorJLabel.setBounds(10, 65, 80, 30);
		vendorJTextField = new JTextField(150);
		vendorJTextField.setOpaque(false);
		vendorJTextField.setBounds(100, 65, 500, 30);
		userInforJPanel.add(vendorJLabel);
		userInforJPanel.add(vendorJTextField);

		distributionJLabel = new JLabel("������");
		distributionJLabel.setFont(new Font("Dialog", 0, 17));
		distributionJLabel.setOpaque(false);
		distributionJLabel.setBounds(10, 105, 80, 30);
		distributionJTextField = new JTextField(150);
		distributionJTextField.setOpaque(false);
		distributionJTextField.setBounds(100, 105, 500, 30);
		userInforJPanel.add(distributionJLabel);
		userInforJPanel.add(distributionJTextField);

		urlJLabel = new JLabel("����ҳ");
		urlJLabel.setFont(new Font("Dialog", 0, 17));
		urlJLabel.setOpaque(false);
		urlJLabel.setBounds(10, 145, 80, 30);
		urlJTextField = new JTextField(150);
		urlJTextField.setOpaque(false);
		urlJTextField.setBounds(100, 145, 500, 30);
		userInforJPanel.add(urlJLabel);
		userInforJPanel.add(urlJTextField);

		/**
		 * �ļ���Ϣ��ʾ �ļ���Ϣ��� prefix , file ,filemode ,username ,usergroup
		 */
		fileInforJPanel = new JPanel();
		fileInforJPanel.setLayout(null);
		fileInforJPanel.setOpaque(false);
		fileInforJPanel.setSize(950, 245);

		String[] columnName = { "�ļ�ǰ׺", "Դ�ļ�", "ģʽ", "�û���", "�û���" }; // ��ͷ
		Object[][] fileContent = new Object[0][5];
		fileModel = new DefaultTableModel(fileContent, columnName);
		fileJTable = new JTable(fileModel);// �������

		fileJTable.setRowSelectionAllowed(true);
		fileJTable.setRowHeight(30);// �и�
		fileJTable.setOpaque(false);
		TableColumn column0 = fileJTable.getColumnModel().getColumn(0);// ��ȡĳ�У������п�
		column0.setPreferredWidth(150);
		TableColumn column1 = fileJTable.getColumnModel().getColumn(1);
		column1.setPreferredWidth(150);
		// �������ݾ���
		DefaultTableCellRenderer r = new DefaultTableCellRenderer();
		r.setHorizontalAlignment(JLabel.CENTER);
		fileJTable.setDefaultRenderer(Object.class, r);

		scrollFile = new JScrollPane(fileJTable); // ��fileJTextArea��ӵ�JScrollPane��
		scrollFile.setBounds(0, 0, 910, 240);
		scrollFile.setOpaque(false);

		// ��Ӵ�����ť
		createFileJButton = new JButton("���");
		createFileJButton.setBounds(710, 245, 70, 28);
		createFileJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fileJDialog = new JDialog();
				fileJDialog.setSize(450, 260);
				fileJDialog.setLocation(450, 200);
				fileJDialog.setTitle("����µ��ļ���Ϣ");
				fileJDialog.setLayout(null);

				prefixJLabel = new JLabel("�ļ�ǰ׺");
				prefixJLabel.setBounds(5, 10, 80, 30);
				prefixJTextField = new JTextField(200);
				prefixJTextField.setBounds(90, 10, 270, 30);
				fileJDialog.getContentPane().add(prefixJLabel);
				fileJDialog.getContentPane().add(prefixJTextField);

				filemodeJLabel = new JLabel("�ļ�ģʽ");
				filemodeJLabel.setBounds(5, 45, 80, 30);
				filemodeJTextField = new JTextField(200);
				filemodeJTextField.setBounds(90, 45, 270, 30);
				fileJDialog.getContentPane().add(filemodeJLabel);
				fileJDialog.getContentPane().add(filemodeJTextField);

				usernameJLabel = new JLabel("�û���");
				usernameJLabel.setBounds(5, 80, 80, 30);
				usernameJTextField = new JTextField(200);
				usernameJTextField.setBounds(90, 80, 270, 30);
				fileJDialog.getContentPane().add(usernameJLabel);
				fileJDialog.getContentPane().add(usernameJTextField);

				usergroupJLabel = new JLabel("�û���");
				usergroupJLabel.setBounds(5, 115, 80, 30);
				usergroupJTextField = new JTextField(200);
				usergroupJTextField.setBounds(90, 115, 270, 30);
				fileJDialog.getContentPane().add(usergroupJLabel);
				fileJDialog.getContentPane().add(usergroupJTextField);

				fileJLabel = new JLabel("Դ�ļ�(��ѡ)");
				fileJLabel.setBounds(5, 150, 80, 30);
				fileJTextField = new JTextField(200);
				fileJTextField.setBounds(90, 150, 270, 30);
				chooseJButton = new JButton("ѡ��");
				chooseJButton.setSize(80, 28);
				chooseJButton.setBounds(365, 150, 70, 28);
				fileJDialog.getContentPane().add(fileJLabel);
				fileJDialog.getContentPane().add(fileJTextField);
				fileJDialog.getContentPane().add(chooseJButton);

				fileJDialogYesJButton = new JButton("ȷ��");
				fileJDialogYesJButton.setSize(60, 28);
				fileJDialogYesJButton.setBounds(180, 185, 60, 28);
				fileJDialogNoJButton = new JButton("ȡ��");
				fileJDialogNoJButton.setSize(60, 28);
				fileJDialogNoJButton.setBounds(245, 185, 60, 28);
				fileJDialog.getContentPane().add(fileJDialogYesJButton);
				fileJDialog.getContentPane().add(fileJDialogNoJButton);

				// ѡ��Դ�ļ�·��
				chooseJButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						Preferences pref = Preferences.userRoot().node(
								this.getClass().getName());
						String lastPath = pref.get("lastPath", "");
						if (!lastPath.equals("")) {
							jFileChooser = new JFileChooser(lastPath);
						} else {
							jFileChooser = new JFileChooser();
						}
						// ѡ�����ļ�
						jFileChooser.setMultiSelectionEnabled(true);
						jFileChooser.setApproveButtonText("ȷ��");
						String title = jFileChooser.getDialogTitle();
						if (title == null)
							jFileChooser.getUI().getDialogTitle(jFileChooser);
						int state = jFileChooser.showOpenDialog(null); // ��ʾ���ļ��Ի���
						// ��ȷ������ť����
						if (state == JFileChooser.APPROVE_OPTION) {
							// �õ�ѡ����ļ�
							File[] files = jFileChooser.getSelectedFiles();
							pref.put("lastPath", files[0].getPath());
							String filePath = null;
							filePath = files[0].getPath();
							for (int len = 1; len < files.length; len++) {
								filePath = filePath + "," + files[len];
							}
							fileJTextField.setText(filePath);
						}
					}
				});

				// ������ȷ������ť�¼�����
				fileJDialogYesJButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String filePaths[] = fileJTextField.getText()
								.split(",");
						for (int len = 0; len < filePaths.length; len++) {
							// ����µ��ļ���Ϣ
							fileModel.insertRow(
									fileModel.getRowCount(),
									new Object[] { prefixJTextField.getText(),
											filePaths[len],
											filemodeJTextField.getText(),
											usernameJTextField.getText(),
											usergroupJTextField.getText() });
						}
						fileJTable.repaint();
						fileJDialog.dispose();
					}
				});

				// ������ȡ������ť�¼�����
				fileJDialogNoJButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						fileJDialog.dispose();
					}
				});
				fileJDialog.setVisible(true);
			}
		});
		// ѡ��ĳ����Ԫ��ѡ����һ��
		deleteFileJButton = new JButton("ɾ��");
		deleteFileJButton.setBounds(790, 245, 70, 28);
		deleteFileJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int row = fileJTable.getSelectedRow();
				if (row == -1) {
					JOptionPane.showMessageDialog(null, "��ѡ��Ҫɾ������!");
				} else {
					if (row == 0) {
						fileModel.removeRow(row);
					} else {
						fileModel.removeRow(row - 1);
					}
				}
			}
		});
		fileInforJPanel.add(scrollFile);
		fileInforJPanel.add(createFileJButton);
		fileInforJPanel.add(deleteFileJButton);

		/**
		 * �ļ�����
		 */
		linkInforJPanel = new JPanel();
		linkInforJPanel.setLayout(null);
		linkInforJPanel.setOpaque(false);
		linkInforJPanel.setSize(950, 245);

		String[] column = { "��װ·��", "Ŀ���ļ�" }; // ��ͷ
		Object[][] linkContent = new Object[0][2];
		linkModel = new DefaultTableModel(linkContent, column);
		linkJTable = new JTable(linkModel);// �������

		linkJTable.setRowHeight(30);// �и�
		linkJTable.setOpaque(false);
		// �������ݾ���
		DefaultTableCellRenderer dtr = new DefaultTableCellRenderer();
		dtr.setHorizontalAlignment(JLabel.CENTER);
		linkJTable.setDefaultRenderer(Object.class, dtr);

		scrollLink = new JScrollPane(linkJTable);
		scrollLink.setBounds(0, 0, 910, 240);
		scrollLink.setOpaque(false);

		// ��Ӵ�����ť
		createLinkJButton = new JButton("���");
		createLinkJButton.setBounds(710, 245, 70, 28);
		createLinkJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				linkJDialog = new JDialog();
				linkJDialog.setSize(450, 260);
				linkJDialog.setLocation(450, 200);
				linkJDialog.setTitle("����µ��ļ�����");
				linkJDialog.setLayout(null);

				pathJLabel = new JLabel("��װ·��");
				pathJLabel.setBounds(5, 20, 80, 30);
				pathJTextField = new JTextField(200);
				pathJTextField.setBounds(90, 20, 270, 30);
				linkJDialog.getContentPane().add(pathJLabel);
				linkJDialog.getContentPane().add(pathJTextField);

				targetJLabel = new JLabel("Ŀ���ļ�");
				targetJLabel.setBounds(5, 60, 80, 30);
				targetJTextField = new JTextField(200);
				targetJTextField.setBounds(90, 60, 270, 30);
				linkJDialog.getContentPane().add(targetJLabel);
				linkJDialog.getContentPane().add(targetJTextField);

				linkJDialogYesJButton = new JButton("ȷ��");
				linkJDialogYesJButton.setSize(60, 28);
				linkJDialogYesJButton.setBounds(180, 180, 60, 28);
				linkJDialogNoJButton = new JButton("ȡ��");
				linkJDialogNoJButton.setSize(60, 28);
				linkJDialogNoJButton.setBounds(245, 180, 60, 28);
				linkJDialog.getContentPane().add(linkJDialogYesJButton);
				linkJDialog.getContentPane().add(linkJDialogNoJButton);

				// ������ȷ������ť�¼�����
				linkJDialogYesJButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						// ����µ��ļ�����
						linkModel.insertRow(linkModel.getRowCount(),
								new Object[] { pathJTextField.getText(),
										targetJTextField.getText() });
						linkJTable.repaint();
						linkJDialog.dispose();
					}
				});

				// ������ȡ������ť�¼�����
				linkJDialogNoJButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						linkJDialog.dispose();
					}
				});
				linkJDialog.setVisible(true);
			}
		});
		// ѡ��ĳ����Ԫ��ѡ����һ��
		deleteLinkJButton = new JButton("ɾ��");
		deleteLinkJButton.setBounds(790, 245, 70, 28);
		deleteLinkJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int row = linkJTable.getSelectedRow();
				if (row == -1) {
					JOptionPane.showMessageDialog(null, "��ѡ��Ҫɾ������!");
				} else {
					if (row == 0) {
						linkModel.removeRow(row);
					} else {
						linkModel.removeRow(row - 1);
					}
				}
			}
		});
		linkInforJPanel.add(scrollLink);
		linkInforJPanel.add(createLinkJButton);
		linkInforJPanel.add(deleteLinkJButton);

		/**
		 * ��װ�ű�
		 */
		installScriptJPanel = new JPanel();
		installScriptJPanel.setLayout(null);
		installScriptJPanel.setOpaque(false);
		installScriptJPanel.setSize(950, 245);

		preInstallScriptJLabel = new JLabel("��װǰ�ű�");
		preInstallScriptJLabel.setFont(new Font("Dialog", 0, 15));
		preInstallScriptJLabel.setBounds(10, 10, 100, 30);
		preInstallScriptJTextField = new JTextField(550);
		preInstallScriptJTextField.setBounds(120, 10, 550, 30);
		preInstallScriptJButton = new JButton("ѡ��");
		preInstallScriptJButton.setBounds(680, 10, 80, 28);
		preInstallScriptJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Preferences pref = Preferences.userRoot().node(
						this.getClass().getName());
				String lastPath = pref.get("lastPath", "");
				if (!lastPath.equals("")) {
					jFileChooser = new JFileChooser(lastPath);
				} else {
					jFileChooser = new JFileChooser();
				}
				jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jFileChooser.setApproveButtonText("ȷ��");
				String title = jFileChooser.getDialogTitle();
				if (title == null)
					jFileChooser.getUI().getDialogTitle(jFileChooser);
				int state = jFileChooser.showOpenDialog(null); // ��ʾ���ļ��Ի���
				// ��ȷ������ť����
				if (state == JFileChooser.APPROVE_OPTION) {
					File file = jFileChooser.getSelectedFile(); // �õ�ѡ����ļ�
					pref.put("lastPath", file.getPath());
					String filePath = file.getPath();
					File sourcePathFile = new File(filePath);
					String fileFullName = sourcePathFile.getName();
					String fileType = new String("");
					fileType = fileFullName.substring(
							(fileFullName.length() - 3), fileFullName.length());
					if (fileType.equals(".sh")) {
						preInstallScriptJTextField.setText(filePath);
					} else {
						JOptionPane.showMessageDialog(null, "��ʽ����ȷ,������ѡ��");
					}
				}
			}
		});

		postInstallScriptJLabel = new JLabel("��װ��ű�");
		postInstallScriptJLabel.setFont(new Font("Dialog", 0, 15));
		postInstallScriptJLabel.setBounds(10, 50, 100, 30);
		postInstallScriptJTextField = new JTextField(550);
		postInstallScriptJTextField.setBounds(120, 50, 550, 30);
		postInstallScriptJButton = new JButton("ѡ��");
		postInstallScriptJButton.setBounds(680, 50, 80, 28);
		postInstallScriptJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Preferences pref = Preferences.userRoot().node(
						this.getClass().getName());
				String lastPath = pref.get("lastPath", "");
				if (!lastPath.equals("")) {
					jFileChooser = new JFileChooser(lastPath);
				} else {
					jFileChooser = new JFileChooser();
				}
				jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jFileChooser.setApproveButtonText("ȷ��");
				String title = jFileChooser.getDialogTitle();
				if (title == null)
					jFileChooser.getUI().getDialogTitle(jFileChooser);
				int state = jFileChooser.showOpenDialog(null); // ��ʾ���ļ��Ի���
				// ��ȷ������ť����
				if (state == JFileChooser.APPROVE_OPTION) {
					File file = jFileChooser.getSelectedFile(); // �õ�ѡ����ļ�
					pref.put("lastPath", file.getPath());
					String filePath = file.getPath();
					File sourcePathFile = new File(filePath);
					String fileFullName = sourcePathFile.getName();
					String fileType = new String("");
					fileType = fileFullName.substring(
							(fileFullName.length() - 3), fileFullName.length());
					if (fileType.equals(".sh")) {
						postInstallScriptJTextField.setText(filePath);
					} else {
						JOptionPane.showMessageDialog(null, "��ʽ����ȷ,������ѡ��");
					}
				}
			}
		});

		installScriptJPanel.add(preInstallScriptJLabel);
		installScriptJPanel.add(preInstallScriptJTextField);
		installScriptJPanel.add(preInstallScriptJButton);
		installScriptJPanel.add(postInstallScriptJLabel);
		installScriptJPanel.add(postInstallScriptJTextField);
		installScriptJPanel.add(postInstallScriptJButton);

		/**
		 * ж�ؽű�
		 */

		unInstallScriptJPanel = new JPanel();
		unInstallScriptJPanel.setLayout(null);
		unInstallScriptJPanel.setOpaque(false);
		unInstallScriptJPanel.setSize(950, 245);

		preUninstallScriptJLabel = new JLabel("ж��ǰ�ű�");
		preUninstallScriptJLabel.setFont(new Font("Dialog", 0, 15));
		preUninstallScriptJLabel.setBounds(10, 10, 100, 30);
		preUninstallScriptJTextField = new JTextField(550);
		preUninstallScriptJTextField.setBounds(120, 10, 550, 30);
		preUninstallScriptJButton = new JButton("ѡ��");
		preUninstallScriptJButton.setBounds(680, 10, 80, 28);
		preUninstallScriptJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Preferences pref = Preferences.userRoot().node(
						this.getClass().getName());
				String lastPath = pref.get("lastPath", "");
				if (!lastPath.equals("")) {
					jFileChooser = new JFileChooser(lastPath);
				} else {
					jFileChooser = new JFileChooser();
				}
				jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jFileChooser.setApproveButtonText("ȷ��");
				String title = jFileChooser.getDialogTitle();
				if (title == null)
					jFileChooser.getUI().getDialogTitle(jFileChooser);
				int state = jFileChooser.showOpenDialog(null); // ��ʾ���ļ��Ի���
				// ��ȷ������ť����
				if (state == JFileChooser.APPROVE_OPTION) {
					File file = jFileChooser.getSelectedFile(); // �õ�ѡ����ļ�
					pref.put("lastPath", file.getPath());
					String filePath = file.getPath();
					File sourcePathFile = new File(filePath);
					String fileFullName = sourcePathFile.getName();
					String fileType = new String("");
					fileType = fileFullName.substring(
							(fileFullName.length() - 3), fileFullName.length());
					if (fileType.equals(".sh")) {
						preUninstallScriptJTextField.setText(filePath);
					} else {
						JOptionPane.showMessageDialog(null, "��ʽ����ȷ,������ѡ��");
					}
				}
			}
		});
		postUninstallScriptJLabel = new JLabel("ж�غ�ű�");
		postUninstallScriptJLabel.setFont(new Font("Dialog", 0, 15));
		postUninstallScriptJLabel.setBounds(10, 50, 100, 30);
		postUninstallScriptJTextField = new JTextField(150);
		postUninstallScriptJTextField.setBounds(120, 50, 550, 30);
		postUninstallScriptJButton = new JButton("ѡ��");
		postUninstallScriptJButton.setBounds(680, 50, 80, 28);
		postUninstallScriptJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Preferences pref = Preferences.userRoot().node(
						this.getClass().getName());
				String lastPath = pref.get("lastPath", "");
				if (!lastPath.equals("")) {
					jFileChooser = new JFileChooser(lastPath);
				} else {
					jFileChooser = new JFileChooser();
				}
				jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jFileChooser.setApproveButtonText("ȷ��");
				String title = jFileChooser.getDialogTitle();
				if (title == null)
					jFileChooser.getUI().getDialogTitle(jFileChooser);
				int state = jFileChooser.showOpenDialog(null); // ��ʾ���ļ��Ի���
				// ��ȷ������ť����
				if (state == JFileChooser.APPROVE_OPTION) {
					File file = jFileChooser.getSelectedFile(); // �õ�ѡ����ļ�
					pref.put("lastPath", file.getPath());
					String filePath = file.getPath();
					File sourcePathFile = new File(filePath);
					String fileFullName = sourcePathFile.getName();
					String fileType = new String("");
					fileType = fileFullName.substring(
							(fileFullName.length() - 3), fileFullName.length());
					if (fileType.equals(".sh")) {
						postUninstallScriptJTextField.setText(filePath);
					} else {
						JOptionPane.showMessageDialog(null, "��ʽ����ȷ,������ѡ��");
					}
				}
			}
		});

		unInstallScriptJPanel.add(preUninstallScriptJLabel);
		unInstallScriptJPanel.add(preUninstallScriptJTextField);
		unInstallScriptJPanel.add(preUninstallScriptJButton);
		unInstallScriptJPanel.add(postUninstallScriptJLabel);
		unInstallScriptJPanel.add(postUninstallScriptJTextField);
		unInstallScriptJPanel.add(postUninstallScriptJButton);

		/**
		 * ������Ŀ¼
		 */
		rpmDirectoryJPanel = new JPanel();
		rpmDirectoryJPanel.setLayout(null);
		rpmDirectoryJPanel.setOpaque(false);
		rpmDirectoryJPanel.setSize(950, 245);

		rpmDirectoryJLabel = new JLabel("����Ŀ¼");
		rpmDirectoryJLabel.setFont(new Font("Dialog", 0, 15));
		rpmDirectoryJLabel.setBounds(10, 20, 100, 30);
		rpmDirectoryJTextField = new JTextField(150);
		rpmDirectoryJTextField.setBounds(120, 20, 550, 30);
		rpmDirectoryJButton = new JButton("ѡ��");
		rpmDirectoryJButton.setBounds(680, 20, 80, 28);
		rpmDirectoryJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Preferences pref = Preferences.userRoot().node(
						this.getClass().getName());
				String lastPath = pref.get("lastPath", "");
				if (!lastPath.equals("")) {
					jFileChooser = new JFileChooser(lastPath);
				} else {
					jFileChooser = new JFileChooser();
				}
				jFileChooser
						.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				jFileChooser.setApproveButtonText("ȷ��");
				String title = jFileChooser.getDialogTitle();
				if (title == null)
					jFileChooser.getUI().getDialogTitle(jFileChooser);
				int state = jFileChooser.showOpenDialog(null); // ��ʾ���ļ��Ի���
				// ��ȷ������ť����
				if (state == JFileChooser.APPROVE_OPTION) {
					File file = jFileChooser.getSelectedFile(); // �õ�ѡ����ļ�
					pref.put("lastPath", file.getPath());
					rpmDirectoryJTextField.setText(file.getPath());
					content.setDestination(file.getPath());
				}
			}
		});

		rpmDirectoryJPanel.add(rpmDirectoryJLabel);
		rpmDirectoryJPanel.add(rpmDirectoryJTextField);
		rpmDirectoryJPanel.add(rpmDirectoryJButton);

		/**
		 * 
		 * ���ѡ�
		 * 
		 */
		JTabbedPane jTabbedPane = new JTabbedPane(JTabbedPane.LEFT); // ����ѡ�������
		jTabbedPane.add("������Ϣ", basicInforJPanel);
		jTabbedPane.add("������Ϣ", userInforJPanel);
		jTabbedPane.add("�ļ���Ϣ", fileInforJPanel);
		jTabbedPane.add("�ļ�����", linkInforJPanel);
		jTabbedPane.add("��װ�ű�", installScriptJPanel);
		jTabbedPane.add("ж�ؽű�", unInstallScriptJPanel);
		jTabbedPane.add("����Ŀ¼", rpmDirectoryJPanel);
		jTabbedPane.setPreferredSize(new Dimension(740, 400));
		jTabbedPane.setBounds(0, 30, 950, 540);
		// ���ñ���ɫΪ��ɫ
		for (int j = 0; j < 7; j++) {
			jTabbedPane.setBackgroundAt(j, Color.white);
		}

		jPanel.add(jTabbedPane);

		bottomJPanel = new JPanel();
		bottomJPanel.setLayout(null);
		bottomJPanel.setOpaque(false);
		bottomJPanel.setBounds(0, 580, 950, 50);
		jPanel.add(bottomJPanel);

		saveJButton = new JButton("����");
		saveJButton.setBounds(750, 0, 80, 28);
		bottomJPanel.add(saveJButton);

		createRPMJButton = new JButton("����");
		createRPMJButton.setBounds(840, 0, 80, 28);
		bottomJPanel.add(createRPMJButton);

		// ��ť��Ӽ����¼�
		saveJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (saveMark == null) {
					Preferences pref = Preferences.userRoot().node(
							this.getClass().getName());
					String lastPath = pref.get("lastPath", "");
					if (!lastPath.equals("")) {
						jFileChooser = new JFileChooser(lastPath);
					} else {
						jFileChooser = new JFileChooser();
					}
					jFileChooser
							.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					jFileChooser.setApproveButtonText("ȷ��");
					String title = jFileChooser.getDialogTitle();
					if (title == null)
						jFileChooser.getUI().getDialogTitle(jFileChooser);
					int state = jFileChooser.showOpenDialog(null); // ��ʾ���ļ��Ի���
					// ��ȷ������ť����
					if (state == JFileChooser.APPROVE_OPTION) {
						File file = jFileChooser.getSelectedFile(); // �õ�ѡ����ļ�
						pref.put("lastPath", file.getPath());
						sourcePath = file.getPath();
					}
				}
				// ������е���Ϣ
				content.clear();
				// ���������Ϣ
				basicInfor.setName(nameJTextField.getText());
				basicInfor.setVersion(versionJTextField.getText());
				basicInfor.setDescription(descriptionJTextField.getText());
				basicInfor.setGroup(groupJTextField.getText());
				basicInfor.setLicense(licenseJTextField.getText());
				basicInfor.setProvides(providesJTextField.getText());
				basicInfor.setRelease(releaseJTextField.getText());
				basicInfor.setSummary(summaryJTextField.getText());
				content.setBasicInfo(basicInfor);

				// ��������Ϣ
				userInfor.setDistribution(distributionJTextField.getText());
				userInfor.setPackager(packagerJTextField.getText());
				userInfor.setUrl(urlJTextField.getText());
				userInfor.setVendor(vendorJTextField.getText());
				content.setUserinfo(userInfor);

				// �����ļ���Ϣ
				int fileRow = fileModel.getRowCount();
				for (int len = 0; len < fileRow; len++) {
					RPMFile rpmFile = new RPMFile();
					rpmFile.setPrefix(fileModel.getValueAt(len, 0).toString());
					rpmFile.setFile(fileModel.getValueAt(len, 1).toString());
					rpmFile.setFilemode(fileModel.getValueAt(len, 2).toString());
					rpmFile.setUsername(fileModel.getValueAt(len, 3).toString());
					rpmFile.setGroup(fileModel.getValueAt(len, 4).toString());
					content.addFile(rpmFile);
				}

				// �����ļ�������Ϣ
				int linkRow = linkJTable.getRowCount();
				for (int len = 0; len < linkRow; len++) {
					RPMLink rpmLink = new RPMLink();
					rpmLink.setPath(linkModel.getValueAt(len, 0).toString());
					rpmLink.setTarget(linkModel.getValueAt(len, 1).toString());
					content.addLink(rpmLink);
				}
				content.setDestination(rpmDirectoryJTextField.getText());

				// ���氲װ��ж�ؽű���Ϣ
				content.setPreInstallScript(preInstallScriptJTextField
						.getText());
				content.setPostInstallScript(postInstallScriptJTextField
						.getText());
				content.setPreUninstallScript(preUninstallScriptJTextField
						.getText());
				content.setPostUninstallScript(postUninstallScriptJTextField
						.getText());

				// ����XML�ļ�
				try {
					boolean flag = update(content);
					if (flag) {
						JOptionPane.showMessageDialog(null, "\t����ɹ���");
					} else {
						JOptionPane.showMessageDialog(null, "\t����ʧ�ܣ�");
					}
				} catch (TransformerConfigurationException e) {
					e.printStackTrace();
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				} catch (SAXException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		// ����RPM��
		createRPMJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// ���content����
				content.clear();

				// ���������Ϣ
				basicInfor.setName(nameJTextField.getText());
				basicInfor.setVersion(versionJTextField.getText());
				basicInfor.setDescription(descriptionJTextField.getText());
				basicInfor.setGroup(groupJTextField.getText());
				basicInfor.setLicense(licenseJTextField.getText());
				basicInfor.setProvides(providesJTextField.getText());
				basicInfor.setRelease(releaseJTextField.getText());
				basicInfor.setSummary(summaryJTextField.getText());
				content.setBasicInfo(basicInfor);

				// ��������Ϣ
				userInfor.setDistribution(distributionJTextField.getText());
				userInfor.setPackager(packagerJTextField.getText());
				userInfor.setUrl(urlJTextField.getText());
				userInfor.setVendor(vendorJTextField.getText());
				content.setUserinfo(userInfor);

				// �������·��
				content.setDestination(rpmDirectoryJTextField.getText());

				// �����ļ���Ϣ
				int fileRows = fileJTable.getRowCount();
				for (int len = 0; len < fileRows; len++) {
					RPMFile rpmFile = new RPMFile();
					rpmFile.setPrefix(fileModel.getValueAt(len, 0).toString());
					rpmFile.setFile(fileModel.getValueAt(len, 1).toString());
					rpmFile.setFilemode(fileModel.getValueAt(len, 2).toString());
					rpmFile.setUsername(fileModel.getValueAt(len, 3).toString());
					rpmFile.setGroup(fileModel.getValueAt(len, 4).toString());
					content.addFile(rpmFile);
				}

				// �����ļ�������Ϣ
				int linkRows = linkJTable.getRowCount();
				for (int len = 0; len < linkRows; len++) {
					RPMLink rpmLink = new RPMLink();
					rpmLink.setPath(linkModel.getValueAt(len, 0).toString());
					rpmLink.setTarget(linkModel.getValueAt(len, 1).toString());
					content.addLink(rpmLink);
				}

				// ���氲װ��ж�ؽű���Ϣ
				content.setPreInstallScript(preInstallScriptJTextField
						.getText());
				content.setPostInstallScript(postInstallScriptJTextField
						.getText());
				content.setPreUninstallScript(preUninstallScriptJTextField
						.getText());
				content.setPostUninstallScript(postUninstallScriptJTextField
						.getText());

				try {
					if (content.getDestination().equals("")) {
						JOptionPane.showMessageDialog(null, "\t��ѡ�����Ŀ¼��");
					} else {
						boolean flag = build(content, content.getDestination());
						if (flag) {
							JOptionPane.showMessageDialog(null, "\t����ɹ���");
						} else {
							JOptionPane.showMessageDialog(null, "\t����ʧ�ܣ�");
						}
					}
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		// ˢ�½���
		jPanel.repaint();
	}

	/**
	 * ��XML�ļ�
	 */
	public boolean open(String file) throws ParserConfigurationException,
			SAXException, IOException {
		// ��ȡXML�ļ�����
		content = RPMContentStore.read(file);
		/**
		 * ������Ϣ��ʾ
		 */
		nameJTextField.setText(content.getBasicInfo().getName());
		versionJTextField.setText(content.getBasicInfo().getVersion());
		releaseJTextField.setText(content.getBasicInfo().getRelease());
		providesJTextField.setText(content.getBasicInfo().getProvides());
		summaryJTextField.setText(content.getBasicInfo().getSummary());
		descriptionJTextField.setText(content.getBasicInfo().getDescription());
		groupJTextField.setText(content.getBasicInfo().getGroup());
		licenseJTextField.setText(content.getBasicInfo().getLicense());

		/**
		 * �����Ϣ��ʾ
		 */
		packagerJTextField.setText(content.getUserinfo().getPackager());
		vendorJTextField.setText(content.getUserinfo().getVendor());
		distributionJTextField.setText(content.getUserinfo().getDistribution());
		urlJTextField.setText(content.getUserinfo().getUrl());

		rpmDirectoryJTextField.setText(content.getDestination());

		/**
		 * �ļ���Ϣ��ʾ �ļ���Ϣ���
		 */

		for (int len = 0; len < content.getFiles().size(); len++) {
			fileModel.insertRow(fileModel.getRowCount(), new Object[] {
					content.getFiles().get(len).getPrefix(),
					content.getFiles().get(len).getFile(),
					content.getFiles().get(len).getFilemode(),
					content.getFiles().get(len).getUsername(),
					content.getFiles().get(len).getGroup() });
		}

		/**
		 * �ļ�����
		 */
		for (int len = 0; len < content.getLinks().size(); len++) {
			linkModel.insertRow(linkModel.getRowCount(), new Object[] {
					content.getLinks().get(len).getPath(),
					content.getLinks().get(len).getTarget() });
		}

		/**
		 * ��װ�ű�
		 */
		preInstallScriptJTextField.setText(content.getPreInstallScript());
		postInstallScriptJTextField.setText(content.getPostInstallScript());

		/**
		 * ж�ؽű�
		 */
		preUninstallScriptJTextField.setText(content.getPreUninstallScript());
		postUninstallScriptJTextField.setText(content.getPostUninstallScript());

		return false;
	}

	/*
	 * ����
	 */
	public boolean update(RPMContent content)
			throws TransformerConfigurationException,
			ParserConfigurationException, SAXException, IOException {
		boolean updateFlag = false;
		boolean flag = RPMContentStore.write(content, sourcePath);
		if (flag) {
			updateFlag = true;
		}
		return updateFlag;
	}

	/*
	 * ����
	 */
	public boolean save(RPMContent content, String filePath)
			throws ParserConfigurationException, SAXException, IOException,
			TransformerConfigurationException {
		return RPMContentStore.write(content, filePath);
	}

	/*
	 * ����RPM��
	 */
	public boolean build(RPMContent content, String rpmDirectory)
			throws NoSuchAlgorithmException, IOException {
		return new RPMBuild().build(content, rpmDirectory);
	}

}
