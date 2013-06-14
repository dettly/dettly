package org.hlc.rpm;

import java.util.ArrayList;
import java.util.List;

import org.hlc.rpm.infos.BasicInfo;
import org.hlc.rpm.infos.RPMFile;
import org.hlc.rpm.infos.RPMLink;
import org.hlc.rpm.infos.UserInfo;

public class RPMContent {

	private BasicInfo basicInfo = null;

	private List<RPMFile> files = new ArrayList<RPMFile>();

	private List<RPMLink> links = new ArrayList<RPMLink>();

	private UserInfo userinfo = null;

	private String preInstallScript = "";

	private String postInstallScript = "";

	private String preUninstallScript = "";

	private String postUninstallScript = "";

	// 生成RPM包存放路径
	private String destination;
	
	public String getPostInstallScript() {
		return postInstallScript;
	}

	public void setPostInstallScript(String postInstallScript) {
		this.postInstallScript = postInstallScript;
	}

	public String getPreUninstallScript() {
		return preUninstallScript;
	}

	public void setPreUninstallScript(String preUninstallScript) {
		this.preUninstallScript = preUninstallScript;
	}

	public String getPostUninstallScript() {
		return postUninstallScript;
	}

	public void setPostUninstallScript(String postUninstallScript) {
		this.postUninstallScript = postUninstallScript;
	}

	public String getPreInstallScript() {
		return preInstallScript;
	}

	public void setPreInstallScript(String preInstallScript) {
		this.preInstallScript = preInstallScript;
	}

	public BasicInfo getBasicInfo() {
		return basicInfo;
	}

	public void setBasicInfo(BasicInfo basicInfo) {
		this.basicInfo = basicInfo;
	}

	public List<RPMFile> getFiles() {
		return files;
	}

	public void addFile(RPMFile file) {
		this.files.add(file);
	}

	public List<RPMLink> getLinks() {
		return links;
	}

	public void addLink(RPMLink rpmLink) {
		links.add(rpmLink);
	}

	public UserInfo getUserinfo() {
		return userinfo;
	}

	public void setUserinfo(UserInfo userinfo) {
		this.userinfo = userinfo;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public void clearRmpFile() {

	}

	public void clear() {
		files.clear();
		links.clear();
	}

}
