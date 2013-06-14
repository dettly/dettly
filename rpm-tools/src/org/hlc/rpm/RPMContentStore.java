package org.hlc.rpm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import org.hlc.rpm.infos.BasicInfo;
import org.hlc.rpm.infos.RPMFile;
import org.hlc.rpm.infos.RPMLink;
import org.hlc.rpm.infos.UserInfo;

public class RPMContentStore {

	/**
	 * get the content of XML file
	 * @param xmlPath the path of XML file
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static RPMContent read(String xmlPath)
			throws ParserConfigurationException, SAXException, IOException {
		RPMContent rpmContent = new RPMContent();
		BasicInfo basic = new BasicInfo();
		UserInfo userinfo = new UserInfo();
		RPMFile rpmFile = new RPMFile();
		RPMLink rpmLink = new RPMLink();
		// ���ý�����������
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory
				.newInstance();
		// ���ý�����
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		// builder.parse()�����������ļ������ݽ���Ϊһ�� XML �ĵ��� ���ҷ���һ���µ� DOM Document����
		Document document = builder.parse(new File(xmlPath));
		// ��ȡ���ڵ�
		NodeList allNodes = document.getChildNodes();
		// ��ȡ���ڵ�����Խڵ���Ϣ
		Node rootNode = allNodes.item(0);
		if (rootNode.getNodeName().equals("rpm")) {
			// ��ȡ��len���ڵ�����Խڵ�
			NamedNodeMap child_1 = rootNode.getAttributes();
			for (int i = 0; i < child_1.getLength(); i++) {
				Node childAttr = child_1.item(i);
				// get the destination
				if (childAttr.getNodeName().equals("destination")) {
					rpmContent.setDestination(childAttr.getNodeValue());
				}
				// get the preInstallScript
				else if( childAttr.getNodeName().equals("preInstallScript")) {
					rpmContent.setPreInstallScript(childAttr.getNodeValue()) ;
				}
				// get the postInstallScript
				else if( childAttr.getNodeName().equals("postInstallScript")){
					rpmContent.setPostInstallScript(childAttr.getNodeValue()) ;
				}
				// get the preUninstallScript
				else if( childAttr.getNodeName().equals("preUninstallScript")){
					rpmContent.setPreUninstallScript(childAttr.getNodeValue()) ;
				}
				// get the postUninstallScript
				else if( childAttr.getNodeName().equals("postUninstallScript")){
					rpmContent.setPostUninstallScript(childAttr.getNodeValue()) ;
				}
			}
		}
		/**
		 * ��DOM����ʱ�Ὣ���лس�����Ϊ n �ڵ���ӽڵ�
		 */
		// ��ȡ�ĵ��ĸ�Ԫ�أ���ֵ��rootElement����
		Element rootElement = document.getDocumentElement();
		// ��ȡrootElement�������ӽڵ㣨���������Խڵ㣩������һ��NodeList����
		NodeList rootChildNodes = rootElement.getChildNodes();
		for (int k = 0; k < rootChildNodes.getLength(); k++) {
			// ��ȡrootChildNodes�ĵ�k���ڵ�
			Node childNode = rootChildNodes.item(k);
			// ��ȡbasic��ǩ�Ľڵ���Ϣ
			if (childNode.getNodeName().equals("basic")) {
				// ��ȡ���е��ӽڵ�
				NodeList child_1 = childNode.getChildNodes();
				for (int j = 0; j < child_1.getLength(); j++) {
					// ��ȡ�ӽڵ�,RPM������name
					Node child_1_child = child_1.item(j);
					if (child_1_child.getNodeName().equals("name")) {
						basic.setName(child_1_child.getFirstChild()
								.getNodeValue());
					}
					// �汾��version
					else if (child_1_child.getNodeName().equals("version")) {
						basic.setVersion(child_1_child.getFirstChild()
								.getNodeValue());
					}
					// �ͳ���release
					else if (child_1_child.getNodeName().equals("release")) {
						basic.setRelease(child_1_child.getFirstChild()
								.getNodeValue());
					}
					// ��������provides
					else if (child_1_child.getNodeName().equals("provides")) {
						basic.setProvides(child_1_child.getFirstChild()
								.getNodeValue());
					}
					// RPM������
					else if (child_1_child.getNodeName().equals("summary")) {
						basic.setSummary(child_1_child.getFirstChild()
								.getNodeValue());
					}
					// RPM������
					else if (child_1_child.getNodeName().equals("description")) {
						basic.setDescription(child_1_child.getFirstChild()
								.getNodeValue());
					}
					// RPM�����
					else if (child_1_child.getNodeName().equals("group")) {
						basic.setGroup(child_1_child.getFirstChild()
								.getNodeValue());
					}
					// RPM���֤���Ȩ����
					else if (child_1_child.getNodeName().equals("license")) {
						basic.setLicense(child_1_child.getFirstChild()
								.getNodeValue());
					}
					rpmContent.setBasicInfo(basic) ;
				}
			}
			// ��ȡuser��ǩ�ڵ���Ϣ
			else if (childNode.getNodeName().equals("user")) {
				NodeList child_2 = childNode.getChildNodes();
				for (int j = 0; j < child_2.getLength(); j++) {
					// ��ȡ��j���ڵ���Ϣ
					Node child_2_child = child_2.item(j);
					// �����packager
					if (child_2_child.getNodeName().equals("packager")) {
						userinfo.setPackager(child_2_child.getFirstChild()
								.getNodeValue());
					}
					// ��Ӧ����Ϣ
					else if (child_2_child.getNodeName().equals("vendor")) {
						userinfo.setVendor(child_2_child.getFirstChild()
								.getNodeValue());
					}
					// ���а���Ϣ
					else if (child_2_child.getNodeName().equals("distribution")) {
						userinfo.setDistribution(child_2_child.getFirstChild()
								.getNodeValue());
					}
					// ������ҳ
					else if (child_2_child.getNodeName().equals("url")) {
						userinfo.setUrl(child_2_child.getFirstChild()
								.getNodeValue());
					}
					rpmContent.setUserinfo(userinfo);
				}
			}
			// ��ȡfileset��ǩ�ڵ���Ϣ
			else if (childNode.getNodeName().equals("fileset")) {
				NodeList child_3 = childNode.getChildNodes();
				for (int p = 0; p < child_3.getLength(); p++) {
					// ��ȡ��p���ڵ���Ϣ
					Node child_3_child = child_3.item(p);
					if (child_3_child.getNodeName().equals("file")) {
						NamedNodeMap child_3_childAttr = child_3_child
								.getAttributes();
						for (int h = 0; h < child_3_childAttr.getLength(); h++) {
							// ��ȡ��h�����Խڵ�
							Node child_3Attr = child_3_childAttr.item(h);
							// ��װ·�����Խڵ�
							if (child_3Attr.getNodeName().equals("prefix")) {
								rpmFile.setPrefix(child_3Attr.getNodeValue());
							}
							// ��ȡԴ�ļ�·��
							else if (child_3Attr.getNodeName().equals("file")) {
								rpmFile.setFile(child_3Attr.getNodeValue());
							}
							// ��ȡ��װģʽ
							else if (child_3Attr.getNodeName().equals(
									"filemode")) {
								rpmFile.setFilemode(child_3Attr.getNodeValue());
							}
							// ��ȡ�û���
							else if (child_3Attr.getNodeName().equals(
									"username")) {
								rpmFile.setUsername(child_3Attr.getNodeValue());
							}
							// ��ȡ�û�����
							else if (child_3Attr.getNodeName().equals("group")) {
								rpmFile.setGroup(child_3Attr.getNodeValue());
							}
						}
						// ����ȡ��fileset��ǩ���ݷ�װ��������
						rpmContent.addFile(rpmFile);
						rpmFile = new RPMFile();
					}
				}
			}
			// ��ȡlinkset��ǩ�ڵ���Ϣ
			else if (childNode.getNodeName().equals("linkset")) {
				NodeList child_4 = childNode.getChildNodes();
				for (int f = 0; f < child_4.getLength(); f++) {
					// ��ȡ��f���ڵ���Ϣ
					Node child_4_child = child_4.item(f);
					if (child_4_child.getNodeName().equals("link")) {
						NamedNodeMap child_4_childAttr = child_4_child
								.getAttributes();
						for (int a = 0; a < child_4_childAttr.getLength(); a++) {
							Node child_4Attr = child_4_childAttr.item(a);
							// ��ȡpath ���Խڵ�
							if (child_4Attr.getNodeName().equals("path")) {
								rpmLink.setPath(child_4Attr.getNodeValue());
							}
							// ��ȡtarget ���Խڵ�
							else if (child_4Attr.getNodeName().equals("target")) {
								rpmLink.setTarget(child_4Attr.getNodeValue());
							}
						}
						// �洢��ǩlinkset�Ľڵ���Ϣ
						rpmContent.addLink(rpmLink);
						rpmLink = new RPMLink();
					}
				}
			}
		}
		return rpmContent;
	}

	public static boolean write(RPMContent content, String xmlPath) throws ParserConfigurationException, SAXException, IOException, TransformerConfigurationException {
		// ���ý�����������
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory
				.newInstance();
		// ���ý�����
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		// �����µ� DOM Document����
		Document document = builder.newDocument();

		// <rpm></rpm> 
		Element root = document.createElement("rpm");
		// �������Խڵ㣬������ֵ
		Attr destination = document.createAttribute("destination") ;
		destination.setNodeValue(content.getDestination()) ;
		Attr preInstallScript = document.createAttribute("preInstallScript") ;
		preInstallScript.setNodeValue(content.getPreInstallScript()) ;
		Attr postInstallScript = document.createAttribute("postInstallScript") ;
		postInstallScript.setNodeValue(content.getPostInstallScript()) ;
		Attr preUninstallScript = document.createAttribute("preUninstallScript") ;
		preUninstallScript.setNodeValue(content.getPreUninstallScript()) ;
		Attr postUninstallScript = document.createAttribute("postUninstallScript") ;
		postUninstallScript.setNodeValue(content.getPostUninstallScript()) ;
		
		root.setAttributeNode(destination) ;
		root.setAttributeNode(preInstallScript) ;
		root.setAttributeNode(postInstallScript) ;
		root.setAttributeNode(preUninstallScript) ;
		root.setAttributeNode(postUninstallScript) ;
		document.appendChild(root);
		
		// <basic></basic>
		Element basic = document.createElement("basic") ;
		root.appendChild(basic) ;
		
		Element name = document.createElement("name") ;
		Text nameText = document.createTextNode(content.getBasicInfo().getName()) ;
		name.appendChild(nameText) ;
		basic.appendChild(name) ;
		
		Element version = document.createElement("version") ;
		Text versionText = document.createTextNode(content.getBasicInfo().getVersion()) ;
		version.appendChild(versionText) ;
		basic.appendChild(version) ;
		
		Element release = document.createElement("release") ;
		Text releaseText = document.createTextNode(content.getBasicInfo().getRelease()) ;
		release.appendChild(releaseText) ;
		basic.appendChild(release) ;
		
		Element provides = document.createElement("provides") ;
		Text providesText = document.createTextNode(content.getBasicInfo().getProvides()) ;
		provides.appendChild(providesText) ;
		basic.appendChild(provides) ;
		
		Element summary = document.createElement("summary") ;
		Text summaryText = document.createTextNode(content.getBasicInfo().getSummary()) ;
		summary.appendChild(summaryText) ;
		basic.appendChild(summary) ;
		
		Element description = document.createElement("description") ;
		Text descriptionText = document.createTextNode(content.getBasicInfo().getDescription()) ;
		description.appendChild(descriptionText) ;
		basic.appendChild(description) ;
		
		Element group = document.createElement("group") ;
		Text groupText = document.createTextNode(content.getBasicInfo().getGroup()) ;
		group.appendChild(groupText) ;
		basic.appendChild(group) ;
		
		Element license = document.createElement("license") ;
		Text licenseText = document.createTextNode(content.getBasicInfo().getLicense()) ;
		license.appendChild(licenseText) ;
		basic.appendChild(license) ;
		
		// <user></user>
		Element user = document.createElement("user") ;
		root.appendChild(user) ;
		
		Element packager = document.createElement("packager") ;
		Text packagerText = document.createTextNode(content.getUserinfo().getPackager()) ;
		packager.appendChild(packagerText) ;
		user.appendChild(packager) ;
		
		Element vendor = document.createElement("vendor") ;
		Text vendorText = document.createTextNode(content.getUserinfo().getVendor()) ;
		vendor.appendChild(vendorText) ;
		user.appendChild(vendor) ;
		
		Element distribution = document.createElement("distribution") ;
		Text distributionText = document.createTextNode(content.getUserinfo().getDistribution()) ;
		distribution.appendChild(distributionText) ;
		user.appendChild(distribution) ;
		
		Element url = document.createElement("url") ;
		Text urlText = document.createTextNode(content.getUserinfo().getUrl()) ;
		url.appendChild(urlText) ;
		user.appendChild(url) ;
		
		// <fileset></fileset>
		Element fileset = document.createElement("fileset") ;
		root.appendChild(fileset) ;
		 
		for( int len = 0 ; len < content.getFiles().size() ; len ++ ) {
			Element file = document.createElement("file") ;
			file.setAttribute("prefix", content.getFiles().get(len).getPrefix()) ;
			file.setAttribute("file", content.getFiles().get(len).getFile()) ;
			file.setAttribute("filemode", content.getFiles().get(len).getFilemode()) ;
			file.setAttribute("username", content.getFiles().get(len).getUsername()) ;
			file.setAttribute("group", content.getFiles().get(len).getGroup()) ;
			fileset.appendChild(file) ;
		}
		
		
		// <linkset></linkset>
		Element linkset = document.createElement("linkset") ;
		root.appendChild(linkset) ;
		
		for( int i = 0 ; i < content.getLinks().size() ; i ++ ){
			Element link = document.createElement("link") ;
			link.setAttribute("path", content.getLinks().get(i).getPath()) ;
			link.setAttribute("target", content.getLinks().get(i).getTarget()) ;
			linkset.appendChild(link) ;
		}
		
		// ����ƴ��XML 
        TransformerFactory transformerFactory = TransformerFactory.newInstance();  
        Transformer transformer = transformerFactory.newTransformer();  
        
        // xml�����λ�� 
        File fileXML = new File(xmlPath);
        if( !fileXML.exists()){
        	// ����Ŀ¼
        	String directoryPath = xmlPath.substring(0, xmlPath.length()- fileXML.getName().length());
        	File directoryFile = new File(directoryPath) ;
        	directoryFile.mkdirs() ;
        	// ��Ŀ¼�´���XML�ļ�
        	File xmlFile = new File(directoryFile ,fileXML.getName()) ;
        	xmlFile.createNewFile() ;
        }
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes"); 
        DOMSource source = new DOMSource(document);
        PrintWriter printWriter = new PrintWriter(new FileOutputStream(fileXML));
        StreamResult result = new StreamResult(printWriter);
        try {
			transformer.transform(source, result);
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return true;
	}

	public static void main(String args[]) throws ParserConfigurationException, SAXException, IOException, TransformerConfigurationException {
//		RPMContent rpmContent = new RPMContent() ;
//		rpmContent = RPMContentStore.read("./soft.xml") ;
//		String xmlPath = "E:" + File.separator + "soft.xml" ;
//		RPMContentStore.write(rpmContent, xmlPath) ;
//		System.out.println(rpmContent.getPreInstallScript());
//		System.out.println(rpmContent.getPostInstallScript());
//		System.out.println(rpmContent.getPreUninstallScript());
//		System.out.println(rpmContent.getPostUninstallScript());
//		System.out.println("***********Start***************");
//		System.out.println(rpmContent.getDestination());
//		System.out.println(rpmContent.getBasicInfo().toString());
//		System.out.println(rpmContent.getFiles().get(0).toString());
//		System.out.println(rpmContent.getLinks().get(0).toString());
//		System.out.println(rpmContent.getUserinfo().toString());
//		System.out.println("************End****************");
	}
}
