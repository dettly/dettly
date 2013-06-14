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
	// 基本信息
	JLabel basicTitleJLabel, nameJLabel, versionJLabel, releaseJLabel,
			providesJLabel, summaryJLabel, descriptionJLabel, groupJLabel,
			licenseJLabel;
	// 打包着信息
	JLabel userTitleJLabel, packagerJLabel, vendorJLabel, urlJLabel,
			distributionJLabel;
	// 文件信息
	JLabel fileTitleJLabel, destinationJLabel, prefixJLabel, pathJLabel,
			fileJLabel, filemodeJLabel, usernameJLabel, usergroupJLabel,
			targetJLabel;
	// 安装脚本和卸载脚本
	JLabel preInstallScriptJLabel, postInstallScriptJLabel,
			preUninstallScriptJLabel, postUninstallScriptJLabel;
	// 基本信息
	JTextField nameJTextField, versionJTextField, releaseJTextField,
			providesJTextField, summaryJTextField, descriptionJTextField,
			groupJTextField, licenseJTextField, prefixJTextField,
			fileJTextField, filemodeJTextField, usernameJTextField,
			usergroupJTextField, rpmDirectoryJTextField;
	// 打包信息
	JTextField packagerJTextField, vendorJTextField, urlJTextField,
			pathJTextField, targetJTextField, distributionJTextField,
			preInstallScriptJTextField, postInstallScriptJTextField,
			preUninstallScriptJTextField, postUninstallScriptJTextField;
	// 按钮
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
	// 文件选择器
	JFileChooser jFileChooser = null;
	JDialog jDialog, fileJDialog, linkJDialog;

	JTable fileJTable, linkJTable;
	DefaultTableModel fileModel, linkModel;

	BasicInfo basicInfor = new BasicInfo();
	UserInfo userInfor = new UserInfo();
	String sourcePath = null;// XML文件路径
	String saveMark = null; // 保存，另存为标志

	public RPMInstallUI() {
		super("Welcome to use RPM tools");
		// 加载背景图片
		ImageIcon background = new ImageIcon("./files/background.jpg");
		// 把背景图片显示在一个标签里
		jLabelBackGround = new JLabel(background);
		// 添加图片到frame的第二层
		this.getLayeredPane().add(jLabelBackGround,
				new Integer(Integer.MIN_VALUE));
		// 把标签的大小位置设置为图片刚好填充整个面
		jLabelBackGround.setBounds(0, 0, background.getIconWidth(),
				background.getIconHeight());
		// 菜单栏
		JMenu fileJMenu = new JMenu("文件");
		JMenuItem open = new JMenuItem("打开");
		open.setAccelerator(KeyStroke.getKeyStroke('O',
				java.awt.Event.CTRL_MASK, false));
		final JMenuItem saveAs = new JMenuItem("另存为");
		saveAs.setAccelerator(KeyStroke.getKeyStroke('S',
				java.awt.Event.CTRL_MASK, false));
		// 初始状态另存菜单不可用
		saveAs.setEnabled(false);
		JMenuItem exit = new JMenuItem("退出");
		exit.setAccelerator(KeyStroke.getKeyStroke('X',
				java.awt.Event.CTRL_MASK, false));
		fileJMenu.add(open);
		fileJMenu.addSeparator();
		fileJMenu.add(saveAs);
		fileJMenu.addSeparator();
		fileJMenu.add(exit);

		JMenu helpJMenu = new JMenu("帮助");
		JMenuItem help = new JMenuItem("使用帮助");
		help.setAccelerator(KeyStroke.getKeyStroke('M',
				java.awt.Event.CTRL_MASK, false));
		helpJMenu.add(help);

		JMenuBar bar = new JMenuBar(); // 创建一个空的菜单条
		bar.setOpaque(false);
		bar.add(fileJMenu);
		bar.add(helpJMenu);
		bar.setBounds(0, 0, 950, 30);
		// 添加菜单栏到JFrame中
		getContentPane().add(bar);

		/*
		 * 添加事件
		 */
		// 打开XML文件
		open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				saveAs.setEnabled(true);
				saveMark = "SAVE";
				// 获取上次打开文件路径
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
				jFileChooser.setApproveButtonText("确定");
				String title = jFileChooser.getDialogTitle();
				if (title == null)
					jFileChooser.getUI().getDialogTitle(jFileChooser);
				// 文件选择器返回状态
				int state = jFileChooser.showOpenDialog(null); // 显示打开文件对话框

				// ‘确定’按钮激活
				if (state == JFileChooser.APPROVE_OPTION) {
					File file = jFileChooser.getSelectedFile(); // 得到选择的文件
					pref.put("lastPath", file.getPath());
					sourcePath = file.getPath();
					// 取得源文件列表）
					File sourcePathFile = new File(sourcePath);
					// 取得文件全名（包含扩展名）
					String fileFullName = sourcePathFile.getName();
					String fileType = new String("");
					// 取得文件扩展名
					fileType = fileFullName.substring(
							(fileFullName.length() - 4), fileFullName.length());
					// 判断是否为xml文档
					if (fileType.equals(".xml")) {
						try {
							// 打开xml文档
							open(sourcePath);
						} catch (ParserConfigurationException e1) {
							e1.printStackTrace();
						} catch (SAXException e1) {
							e1.printStackTrace();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					} else {
						JOptionPane.showMessageDialog(null, "格式不正确,请重新选择！");
					}
				}
			}
		});

		// 另存为
		saveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 获取上次打开文件路径
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
				jFileChooser.setApproveButtonText("确定");
				String title = jFileChooser.getDialogTitle();
				String saveXMLPath = null;
				if (title == null)
					jFileChooser.getUI().getDialogTitle(jFileChooser);
				int state = jFileChooser.showOpenDialog(null); // 显示打开文件对话框
				if (state == JFileChooser.APPROVE_OPTION) {
					File file = jFileChooser.getSelectedFile(); // 得到选择的文件
					saveXMLPath = file.getPath();
					// 清空content内容
					content.clear();

					// 保存基础信息
					basicInfor.setName(nameJTextField.getText());
					basicInfor.setVersion(versionJTextField.getText());
					basicInfor.setDescription(descriptionJTextField.getText());
					basicInfor.setGroup(groupJTextField.getText());
					basicInfor.setLicense(licenseJTextField.getText());
					basicInfor.setProvides(providesJTextField.getText());
					basicInfor.setRelease(releaseJTextField.getText());
					basicInfor.setSummary(summaryJTextField.getText());
					content.setBasicInfo(basicInfor);

					// 保存打包信息
					userInfor.setDistribution(distributionJTextField.getText());
					userInfor.setPackager(packagerJTextField.getText());
					userInfor.setUrl(urlJTextField.getText());
					userInfor.setVendor(vendorJTextField.getText());
					content.setUserinfo(userInfor);

					// 保存文件信息
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

					// 保存文件链接信息
					int linkRows = linkJTable.getRowCount();
					for (int len = 0; len < linkRows; len++) {
						RPMLink rpmLink = new RPMLink();
						rpmLink.setPath(linkModel.getValueAt(len, 0).toString());
						rpmLink.setTarget(linkModel.getValueAt(len, 1)
								.toString());
						content.addLink(rpmLink);
					}

					// 保存安装和卸载脚本信息
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
							JOptionPane.showMessageDialog(null, "\t保存成功！");
						} else {
							JOptionPane.showMessageDialog(null, "\t保存失败！");
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

		// ‘退出‘事件处理
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		// 帮助按钮
		help.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String helpfileurl= "./files/帮助文档.CHM";
				try {
					Runtime.getRuntime().exec("hh "+helpfileurl);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		});
		
		
		// 获取frame的最上层面板为了设置其背景颜色
		jPanel = (JPanel) getContentPane();
		jPanel.setOpaque(false);// 设置透明
		jPanel.setLayout(null);

		/**
		 * 基本信息显示 name ,version ,release ,provides ,summary ,description ,group
		 * ,license
		 */
		basicInforJPanel = new JPanel();
		basicInforJPanel.setLayout(null);
		basicInforJPanel.setOpaque(false);
		basicInforJPanel.setSize(950, 580);

		nameJLabel = new JLabel("软件名称");
		nameJLabel.setFont(new Font("Dialog", 0, 17));
		nameJLabel.setOpaque(false);
		nameJLabel.setBounds(10, 20, 80, 30);
		nameJTextField = new JTextField(150);
		nameJTextField.setOpaque(false);
		nameJTextField.setBounds(100, 20, 500, 30);
		basicInforJPanel.add(nameJLabel);
		basicInforJPanel.add(nameJTextField);

		versionJLabel = new JLabel("  版本号");
		versionJLabel.setFont(new Font("Dialog", 0, 17));
		versionJLabel.setOpaque(false);
		versionJLabel.setBounds(10, 60, 80, 30);
		versionJTextField = new JTextField(150);
		versionJTextField.setOpaque(false);
		versionJTextField.setBounds(100, 60, 500, 30);
		basicInforJPanel.add(versionJLabel);
		basicInforJPanel.add(versionJTextField);

		releaseJLabel = new JLabel("  释出号");
		releaseJLabel.setFont(new Font("Dialog", 0, 17));
		releaseJLabel.setOpaque(false);
		releaseJLabel.setBounds(10, 100, 80, 30);
		releaseJTextField = new JTextField(150);
		releaseJTextField.setOpaque(false);
		releaseJTextField.setBounds(100, 100, 500, 30);
		basicInforJPanel.add(releaseJLabel);
		basicInforJPanel.add(releaseJTextField);

		providesJLabel = new JLabel("主要功能");
		providesJLabel.setFont(new Font("Dialog", 0, 17));
		providesJLabel.setOpaque(false);
		providesJLabel.setBounds(10, 140, 80, 30);
		providesJTextField = new JTextField(150);
		providesJTextField.setOpaque(false);
		providesJTextField.setBounds(100, 140, 500, 30);
		basicInforJPanel.add(providesJLabel);
		basicInforJPanel.add(providesJTextField);

		summaryJLabel = new JLabel("软件概述");
		summaryJLabel.setFont(new Font("Dialog", 0, 17));
		summaryJLabel.setOpaque(false);
		summaryJLabel.setBounds(10, 190, 80, 30);
		summaryJTextField = new JTextField(150);
		summaryJTextField.setOpaque(false);
		summaryJTextField.setBounds(100, 190, 500, 30);
		basicInforJPanel.add(summaryJLabel);
		basicInforJPanel.add(summaryJTextField);

		descriptionJLabel = new JLabel("详细描述");
		descriptionJLabel.setFont(new Font("Dialog", 0, 17));
		descriptionJLabel.setOpaque(false);
		descriptionJLabel.setBounds(10, 230, 80, 30);
		descriptionJTextField = new JTextField(150);
		descriptionJTextField.setOpaque(false);
		descriptionJTextField.setBounds(100, 230, 500, 30);
		basicInforJPanel.add(descriptionJLabel);
		basicInforJPanel.add(descriptionJTextField);

		groupJLabel = new JLabel("软件类别");
		groupJLabel.setFont(new Font("Dialog", 0, 17));
		groupJLabel.setOpaque(false);
		groupJLabel.setBounds(10, 270, 80, 30);
		groupJTextField = new JTextField(150);
		groupJTextField.setOpaque(false);
		groupJTextField.setBounds(100, 270, 500, 30);
		groupJTextField.setText("Applications/Editors");
		basicInforJPanel.add(groupJLabel);
		basicInforJPanel.add(groupJTextField);

		licenseJLabel = new JLabel("  许可证  ");
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
		 * 打包信息显示 packager , verdor ,distribution ,url
		 */
		userInforJPanel = new JPanel();
		userInforJPanel.setLayout(null);
		userInforJPanel.setOpaque(false);
		userInforJPanel.setSize(950, 580);

		packagerJLabel = new JLabel("打包者");
		packagerJLabel.setFont(new Font("Dialog", 0, 17));
		packagerJLabel.setOpaque(false);
		packagerJLabel.setBounds(10, 20, 80, 30);
		packagerJTextField = new JTextField(150);
		packagerJTextField.setOpaque(false);
		packagerJTextField.setBounds(100, 20, 500, 30);
		userInforJPanel.add(packagerJLabel);
		userInforJPanel.add(packagerJTextField);

		vendorJLabel = new JLabel("供给商");
		vendorJLabel.setFont(new Font("Dialog", 0, 17));
		vendorJLabel.setOpaque(false);
		vendorJLabel.setBounds(10, 65, 80, 30);
		vendorJTextField = new JTextField(150);
		vendorJTextField.setOpaque(false);
		vendorJTextField.setBounds(100, 65, 500, 30);
		userInforJPanel.add(vendorJLabel);
		userInforJPanel.add(vendorJTextField);

		distributionJLabel = new JLabel("发行商");
		distributionJLabel.setFont(new Font("Dialog", 0, 17));
		distributionJLabel.setOpaque(false);
		distributionJLabel.setBounds(10, 105, 80, 30);
		distributionJTextField = new JTextField(150);
		distributionJTextField.setOpaque(false);
		distributionJTextField.setBounds(100, 105, 500, 30);
		userInforJPanel.add(distributionJLabel);
		userInforJPanel.add(distributionJTextField);

		urlJLabel = new JLabel("主网页");
		urlJLabel.setFont(new Font("Dialog", 0, 17));
		urlJLabel.setOpaque(false);
		urlJLabel.setBounds(10, 145, 80, 30);
		urlJTextField = new JTextField(150);
		urlJTextField.setOpaque(false);
		urlJTextField.setBounds(100, 145, 500, 30);
		userInforJPanel.add(urlJLabel);
		userInforJPanel.add(urlJTextField);

		/**
		 * 文件信息显示 文件信息添加 prefix , file ,filemode ,username ,usergroup
		 */
		fileInforJPanel = new JPanel();
		fileInforJPanel.setLayout(null);
		fileInforJPanel.setOpaque(false);
		fileInforJPanel.setSize(950, 245);

		String[] columnName = { "文件前缀", "源文件", "模式", "用户名", "用户组" }; // 表头
		Object[][] fileContent = new Object[0][5];
		fileModel = new DefaultTableModel(fileContent, columnName);
		fileJTable = new JTable(fileModel);// 创建表格

		fileJTable.setRowSelectionAllowed(true);
		fileJTable.setRowHeight(30);// 行高
		fileJTable.setOpaque(false);
		TableColumn column0 = fileJTable.getColumnModel().getColumn(0);// 获取某列，设置列宽
		column0.setPreferredWidth(150);
		TableColumn column1 = fileJTable.getColumnModel().getColumn(1);
		column1.setPreferredWidth(150);
		// 设置内容居中
		DefaultTableCellRenderer r = new DefaultTableCellRenderer();
		r.setHorizontalAlignment(JLabel.CENTER);
		fileJTable.setDefaultRenderer(Object.class, r);

		scrollFile = new JScrollPane(fileJTable); // 将fileJTextArea添加到JScrollPane中
		scrollFile.setBounds(0, 0, 910, 240);
		scrollFile.setOpaque(false);

		// 添加创建按钮
		createFileJButton = new JButton("添加");
		createFileJButton.setBounds(710, 245, 70, 28);
		createFileJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fileJDialog = new JDialog();
				fileJDialog.setSize(450, 260);
				fileJDialog.setLocation(450, 200);
				fileJDialog.setTitle("添加新的文件信息");
				fileJDialog.setLayout(null);

				prefixJLabel = new JLabel("文件前缀");
				prefixJLabel.setBounds(5, 10, 80, 30);
				prefixJTextField = new JTextField(200);
				prefixJTextField.setBounds(90, 10, 270, 30);
				fileJDialog.getContentPane().add(prefixJLabel);
				fileJDialog.getContentPane().add(prefixJTextField);

				filemodeJLabel = new JLabel("文件模式");
				filemodeJLabel.setBounds(5, 45, 80, 30);
				filemodeJTextField = new JTextField(200);
				filemodeJTextField.setBounds(90, 45, 270, 30);
				fileJDialog.getContentPane().add(filemodeJLabel);
				fileJDialog.getContentPane().add(filemodeJTextField);

				usernameJLabel = new JLabel("用户名");
				usernameJLabel.setBounds(5, 80, 80, 30);
				usernameJTextField = new JTextField(200);
				usernameJTextField.setBounds(90, 80, 270, 30);
				fileJDialog.getContentPane().add(usernameJLabel);
				fileJDialog.getContentPane().add(usernameJTextField);

				usergroupJLabel = new JLabel("用户组");
				usergroupJLabel.setBounds(5, 115, 80, 30);
				usergroupJTextField = new JTextField(200);
				usergroupJTextField.setBounds(90, 115, 270, 30);
				fileJDialog.getContentPane().add(usergroupJLabel);
				fileJDialog.getContentPane().add(usergroupJTextField);

				fileJLabel = new JLabel("源文件(多选)");
				fileJLabel.setBounds(5, 150, 80, 30);
				fileJTextField = new JTextField(200);
				fileJTextField.setBounds(90, 150, 270, 30);
				chooseJButton = new JButton("选择");
				chooseJButton.setSize(80, 28);
				chooseJButton.setBounds(365, 150, 70, 28);
				fileJDialog.getContentPane().add(fileJLabel);
				fileJDialog.getContentPane().add(fileJTextField);
				fileJDialog.getContentPane().add(chooseJButton);

				fileJDialogYesJButton = new JButton("确定");
				fileJDialogYesJButton.setSize(60, 28);
				fileJDialogYesJButton.setBounds(180, 185, 60, 28);
				fileJDialogNoJButton = new JButton("取消");
				fileJDialogNoJButton.setSize(60, 28);
				fileJDialogNoJButton.setBounds(245, 185, 60, 28);
				fileJDialog.getContentPane().add(fileJDialogYesJButton);
				fileJDialog.getContentPane().add(fileJDialogNoJButton);

				// 选择源文件路径
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
						// 选择多个文件
						jFileChooser.setMultiSelectionEnabled(true);
						jFileChooser.setApproveButtonText("确定");
						String title = jFileChooser.getDialogTitle();
						if (title == null)
							jFileChooser.getUI().getDialogTitle(jFileChooser);
						int state = jFileChooser.showOpenDialog(null); // 显示打开文件对话框
						// ‘确定’按钮激活
						if (state == JFileChooser.APPROVE_OPTION) {
							// 得到选择的文件
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

				// 单击‘确定’按钮事件处理
				fileJDialogYesJButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String filePaths[] = fileJTextField.getText()
								.split(",");
						for (int len = 0; len < filePaths.length; len++) {
							// 添加新的文件信息
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

				// 单击‘取消’按钮事件处理
				fileJDialogNoJButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						fileJDialog.dispose();
					}
				});
				fileJDialog.setVisible(true);
			}
		});
		// 选中某个单元格即选中这一行
		deleteFileJButton = new JButton("删除");
		deleteFileJButton.setBounds(790, 245, 70, 28);
		deleteFileJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int row = fileJTable.getSelectedRow();
				if (row == -1) {
					JOptionPane.showMessageDialog(null, "请选择要删除的行!");
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
		 * 文件链接
		 */
		linkInforJPanel = new JPanel();
		linkInforJPanel.setLayout(null);
		linkInforJPanel.setOpaque(false);
		linkInforJPanel.setSize(950, 245);

		String[] column = { "安装路径", "目标文件" }; // 表头
		Object[][] linkContent = new Object[0][2];
		linkModel = new DefaultTableModel(linkContent, column);
		linkJTable = new JTable(linkModel);// 创建表格

		linkJTable.setRowHeight(30);// 行高
		linkJTable.setOpaque(false);
		// 设置内容居中
		DefaultTableCellRenderer dtr = new DefaultTableCellRenderer();
		dtr.setHorizontalAlignment(JLabel.CENTER);
		linkJTable.setDefaultRenderer(Object.class, dtr);

		scrollLink = new JScrollPane(linkJTable);
		scrollLink.setBounds(0, 0, 910, 240);
		scrollLink.setOpaque(false);

		// 添加创建按钮
		createLinkJButton = new JButton("添加");
		createLinkJButton.setBounds(710, 245, 70, 28);
		createLinkJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				linkJDialog = new JDialog();
				linkJDialog.setSize(450, 260);
				linkJDialog.setLocation(450, 200);
				linkJDialog.setTitle("添加新的文件链接");
				linkJDialog.setLayout(null);

				pathJLabel = new JLabel("安装路径");
				pathJLabel.setBounds(5, 20, 80, 30);
				pathJTextField = new JTextField(200);
				pathJTextField.setBounds(90, 20, 270, 30);
				linkJDialog.getContentPane().add(pathJLabel);
				linkJDialog.getContentPane().add(pathJTextField);

				targetJLabel = new JLabel("目标文件");
				targetJLabel.setBounds(5, 60, 80, 30);
				targetJTextField = new JTextField(200);
				targetJTextField.setBounds(90, 60, 270, 30);
				linkJDialog.getContentPane().add(targetJLabel);
				linkJDialog.getContentPane().add(targetJTextField);

				linkJDialogYesJButton = new JButton("确定");
				linkJDialogYesJButton.setSize(60, 28);
				linkJDialogYesJButton.setBounds(180, 180, 60, 28);
				linkJDialogNoJButton = new JButton("取消");
				linkJDialogNoJButton.setSize(60, 28);
				linkJDialogNoJButton.setBounds(245, 180, 60, 28);
				linkJDialog.getContentPane().add(linkJDialogYesJButton);
				linkJDialog.getContentPane().add(linkJDialogNoJButton);

				// 单击‘确定’按钮事件处理
				linkJDialogYesJButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						// 添加新的文件链接
						linkModel.insertRow(linkModel.getRowCount(),
								new Object[] { pathJTextField.getText(),
										targetJTextField.getText() });
						linkJTable.repaint();
						linkJDialog.dispose();
					}
				});

				// 单击‘取消’按钮事件处理
				linkJDialogNoJButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						linkJDialog.dispose();
					}
				});
				linkJDialog.setVisible(true);
			}
		});
		// 选中某个单元格即选中这一行
		deleteLinkJButton = new JButton("删除");
		deleteLinkJButton.setBounds(790, 245, 70, 28);
		deleteLinkJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int row = linkJTable.getSelectedRow();
				if (row == -1) {
					JOptionPane.showMessageDialog(null, "请选择要删除的行!");
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
		 * 安装脚本
		 */
		installScriptJPanel = new JPanel();
		installScriptJPanel.setLayout(null);
		installScriptJPanel.setOpaque(false);
		installScriptJPanel.setSize(950, 245);

		preInstallScriptJLabel = new JLabel("安装前脚本");
		preInstallScriptJLabel.setFont(new Font("Dialog", 0, 15));
		preInstallScriptJLabel.setBounds(10, 10, 100, 30);
		preInstallScriptJTextField = new JTextField(550);
		preInstallScriptJTextField.setBounds(120, 10, 550, 30);
		preInstallScriptJButton = new JButton("选择");
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
				jFileChooser.setApproveButtonText("确定");
				String title = jFileChooser.getDialogTitle();
				if (title == null)
					jFileChooser.getUI().getDialogTitle(jFileChooser);
				int state = jFileChooser.showOpenDialog(null); // 显示打开文件对话框
				// ‘确定’按钮激活
				if (state == JFileChooser.APPROVE_OPTION) {
					File file = jFileChooser.getSelectedFile(); // 得到选择的文件
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
						JOptionPane.showMessageDialog(null, "格式不正确,请重新选择！");
					}
				}
			}
		});

		postInstallScriptJLabel = new JLabel("安装后脚本");
		postInstallScriptJLabel.setFont(new Font("Dialog", 0, 15));
		postInstallScriptJLabel.setBounds(10, 50, 100, 30);
		postInstallScriptJTextField = new JTextField(550);
		postInstallScriptJTextField.setBounds(120, 50, 550, 30);
		postInstallScriptJButton = new JButton("选择");
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
				jFileChooser.setApproveButtonText("确定");
				String title = jFileChooser.getDialogTitle();
				if (title == null)
					jFileChooser.getUI().getDialogTitle(jFileChooser);
				int state = jFileChooser.showOpenDialog(null); // 显示打开文件对话框
				// ‘确定’按钮激活
				if (state == JFileChooser.APPROVE_OPTION) {
					File file = jFileChooser.getSelectedFile(); // 得到选择的文件
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
						JOptionPane.showMessageDialog(null, "格式不正确,请重新选择！");
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
		 * 卸载脚本
		 */

		unInstallScriptJPanel = new JPanel();
		unInstallScriptJPanel.setLayout(null);
		unInstallScriptJPanel.setOpaque(false);
		unInstallScriptJPanel.setSize(950, 245);

		preUninstallScriptJLabel = new JLabel("卸载前脚本");
		preUninstallScriptJLabel.setFont(new Font("Dialog", 0, 15));
		preUninstallScriptJLabel.setBounds(10, 10, 100, 30);
		preUninstallScriptJTextField = new JTextField(550);
		preUninstallScriptJTextField.setBounds(120, 10, 550, 30);
		preUninstallScriptJButton = new JButton("选择");
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
				jFileChooser.setApproveButtonText("确定");
				String title = jFileChooser.getDialogTitle();
				if (title == null)
					jFileChooser.getUI().getDialogTitle(jFileChooser);
				int state = jFileChooser.showOpenDialog(null); // 显示打开文件对话框
				// ‘确定’按钮激活
				if (state == JFileChooser.APPROVE_OPTION) {
					File file = jFileChooser.getSelectedFile(); // 得到选择的文件
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
						JOptionPane.showMessageDialog(null, "格式不正确,请重新选择！");
					}
				}
			}
		});
		postUninstallScriptJLabel = new JLabel("卸载后脚本");
		postUninstallScriptJLabel.setFont(new Font("Dialog", 0, 15));
		postUninstallScriptJLabel.setBounds(10, 50, 100, 30);
		postUninstallScriptJTextField = new JTextField(150);
		postUninstallScriptJTextField.setBounds(120, 50, 550, 30);
		postUninstallScriptJButton = new JButton("选择");
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
				jFileChooser.setApproveButtonText("确定");
				String title = jFileChooser.getDialogTitle();
				if (title == null)
					jFileChooser.getUI().getDialogTitle(jFileChooser);
				int state = jFileChooser.showOpenDialog(null); // 显示打开文件对话框
				// ‘确定’按钮激活
				if (state == JFileChooser.APPROVE_OPTION) {
					File file = jFileChooser.getSelectedFile(); // 得到选择的文件
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
						JOptionPane.showMessageDialog(null, "格式不正确,请重新选择！");
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
		 * 编译后的目录
		 */
		rpmDirectoryJPanel = new JPanel();
		rpmDirectoryJPanel.setLayout(null);
		rpmDirectoryJPanel.setOpaque(false);
		rpmDirectoryJPanel.setSize(950, 245);

		rpmDirectoryJLabel = new JLabel("编译目录");
		rpmDirectoryJLabel.setFont(new Font("Dialog", 0, 15));
		rpmDirectoryJLabel.setBounds(10, 20, 100, 30);
		rpmDirectoryJTextField = new JTextField(150);
		rpmDirectoryJTextField.setBounds(120, 20, 550, 30);
		rpmDirectoryJButton = new JButton("选择");
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
				jFileChooser.setApproveButtonText("确定");
				String title = jFileChooser.getDialogTitle();
				if (title == null)
					jFileChooser.getUI().getDialogTitle(jFileChooser);
				int state = jFileChooser.showOpenDialog(null); // 显示打开文件对话框
				// ‘确定’按钮激活
				if (state == JFileChooser.APPROVE_OPTION) {
					File file = jFileChooser.getSelectedFile(); // 得到选择的文件
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
		 * 组合选项卡
		 * 
		 */
		JTabbedPane jTabbedPane = new JTabbedPane(JTabbedPane.LEFT); // 设置选项卡在坐标
		jTabbedPane.add("基本信息", basicInforJPanel);
		jTabbedPane.add("作者信息", userInforJPanel);
		jTabbedPane.add("文件信息", fileInforJPanel);
		jTabbedPane.add("文件链接", linkInforJPanel);
		jTabbedPane.add("安装脚本", installScriptJPanel);
		jTabbedPane.add("卸载脚本", unInstallScriptJPanel);
		jTabbedPane.add("编译目录", rpmDirectoryJPanel);
		jTabbedPane.setPreferredSize(new Dimension(740, 400));
		jTabbedPane.setBounds(0, 30, 950, 540);
		// 设置背景色为白色
		for (int j = 0; j < 7; j++) {
			jTabbedPane.setBackgroundAt(j, Color.white);
		}

		jPanel.add(jTabbedPane);

		bottomJPanel = new JPanel();
		bottomJPanel.setLayout(null);
		bottomJPanel.setOpaque(false);
		bottomJPanel.setBounds(0, 580, 950, 50);
		jPanel.add(bottomJPanel);

		saveJButton = new JButton("保存");
		saveJButton.setBounds(750, 0, 80, 28);
		bottomJPanel.add(saveJButton);

		createRPMJButton = new JButton("编译");
		createRPMJButton.setBounds(840, 0, 80, 28);
		bottomJPanel.add(createRPMJButton);

		// 按钮添加监听事件
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
					jFileChooser.setApproveButtonText("确定");
					String title = jFileChooser.getDialogTitle();
					if (title == null)
						jFileChooser.getUI().getDialogTitle(jFileChooser);
					int state = jFileChooser.showOpenDialog(null); // 显示打开文件对话框
					// ‘确定’按钮激活
					if (state == JFileChooser.APPROVE_OPTION) {
						File file = jFileChooser.getSelectedFile(); // 得到选择的文件
						pref.put("lastPath", file.getPath());
						sourcePath = file.getPath();
					}
				}
				// 清空所有的信息
				content.clear();
				// 保存基本信息
				basicInfor.setName(nameJTextField.getText());
				basicInfor.setVersion(versionJTextField.getText());
				basicInfor.setDescription(descriptionJTextField.getText());
				basicInfor.setGroup(groupJTextField.getText());
				basicInfor.setLicense(licenseJTextField.getText());
				basicInfor.setProvides(providesJTextField.getText());
				basicInfor.setRelease(releaseJTextField.getText());
				basicInfor.setSummary(summaryJTextField.getText());
				content.setBasicInfo(basicInfor);

				// 保存打包信息
				userInfor.setDistribution(distributionJTextField.getText());
				userInfor.setPackager(packagerJTextField.getText());
				userInfor.setUrl(urlJTextField.getText());
				userInfor.setVendor(vendorJTextField.getText());
				content.setUserinfo(userInfor);

				// 保存文件信息
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

				// 保存文件链接信息
				int linkRow = linkJTable.getRowCount();
				for (int len = 0; len < linkRow; len++) {
					RPMLink rpmLink = new RPMLink();
					rpmLink.setPath(linkModel.getValueAt(len, 0).toString());
					rpmLink.setTarget(linkModel.getValueAt(len, 1).toString());
					content.addLink(rpmLink);
				}
				content.setDestination(rpmDirectoryJTextField.getText());

				// 保存安装和卸载脚本信息
				content.setPreInstallScript(preInstallScriptJTextField
						.getText());
				content.setPostInstallScript(postInstallScriptJTextField
						.getText());
				content.setPreUninstallScript(preUninstallScriptJTextField
						.getText());
				content.setPostUninstallScript(postUninstallScriptJTextField
						.getText());

				// 更新XML文件
				try {
					boolean flag = update(content);
					if (flag) {
						JOptionPane.showMessageDialog(null, "\t保存成功！");
					} else {
						JOptionPane.showMessageDialog(null, "\t保存失败！");
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

		// 创建RPM包
		createRPMJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// 清空content内容
				content.clear();

				// 保存基础信息
				basicInfor.setName(nameJTextField.getText());
				basicInfor.setVersion(versionJTextField.getText());
				basicInfor.setDescription(descriptionJTextField.getText());
				basicInfor.setGroup(groupJTextField.getText());
				basicInfor.setLicense(licenseJTextField.getText());
				basicInfor.setProvides(providesJTextField.getText());
				basicInfor.setRelease(releaseJTextField.getText());
				basicInfor.setSummary(summaryJTextField.getText());
				content.setBasicInfo(basicInfor);

				// 保存打包信息
				userInfor.setDistribution(distributionJTextField.getText());
				userInfor.setPackager(packagerJTextField.getText());
				userInfor.setUrl(urlJTextField.getText());
				userInfor.setVendor(vendorJTextField.getText());
				content.setUserinfo(userInfor);

				// 保存编译路径
				content.setDestination(rpmDirectoryJTextField.getText());

				// 保存文件信息
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

				// 保存文件链接信息
				int linkRows = linkJTable.getRowCount();
				for (int len = 0; len < linkRows; len++) {
					RPMLink rpmLink = new RPMLink();
					rpmLink.setPath(linkModel.getValueAt(len, 0).toString());
					rpmLink.setTarget(linkModel.getValueAt(len, 1).toString());
					content.addLink(rpmLink);
				}

				// 保存安装和卸载脚本信息
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
						JOptionPane.showMessageDialog(null, "\t请选择编译目录！");
					} else {
						boolean flag = build(content, content.getDestination());
						if (flag) {
							JOptionPane.showMessageDialog(null, "\t编译成功！");
						} else {
							JOptionPane.showMessageDialog(null, "\t编译失败！");
						}
					}
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		// 刷新界面
		jPanel.repaint();
	}

	/**
	 * 打开XML文件
	 */
	public boolean open(String file) throws ParserConfigurationException,
			SAXException, IOException {
		// 获取XML文件内容
		content = RPMContentStore.read(file);
		/**
		 * 基本信息显示
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
		 * 打包信息显示
		 */
		packagerJTextField.setText(content.getUserinfo().getPackager());
		vendorJTextField.setText(content.getUserinfo().getVendor());
		distributionJTextField.setText(content.getUserinfo().getDistribution());
		urlJTextField.setText(content.getUserinfo().getUrl());

		rpmDirectoryJTextField.setText(content.getDestination());

		/**
		 * 文件信息显示 文件信息添加
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
		 * 文件链接
		 */
		for (int len = 0; len < content.getLinks().size(); len++) {
			linkModel.insertRow(linkModel.getRowCount(), new Object[] {
					content.getLinks().get(len).getPath(),
					content.getLinks().get(len).getTarget() });
		}

		/**
		 * 安装脚本
		 */
		preInstallScriptJTextField.setText(content.getPreInstallScript());
		postInstallScriptJTextField.setText(content.getPostInstallScript());

		/**
		 * 卸载脚本
		 */
		preUninstallScriptJTextField.setText(content.getPreUninstallScript());
		postUninstallScriptJTextField.setText(content.getPostUninstallScript());

		return false;
	}

	/*
	 * 更新
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
	 * 保存
	 */
	public boolean save(RPMContent content, String filePath)
			throws ParserConfigurationException, SAXException, IOException,
			TransformerConfigurationException {
		return RPMContentStore.write(content, filePath);
	}

	/*
	 * 构建RPM包
	 */
	public boolean build(RPMContent content, String rpmDirectory)
			throws NoSuchAlgorithmException, IOException {
		return new RPMBuild().build(content, rpmDirectory);
	}

}
