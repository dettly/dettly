package org.hlc.rpm;

import static org.freecompany.redline.header.Architecture.NOARCH;
import static org.freecompany.redline.header.Os.LINUX;
import static org.freecompany.redline.header.RpmType.BINARY;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.apache.tools.zip.UnixStat;
import org.freecompany.redline.Builder;
import org.freecompany.redline.header.Architecture;
import org.freecompany.redline.header.Os;
import org.freecompany.redline.header.RpmType;

import org.hlc.rpm.infos.RPMFile;

public class RPMBuild {
	private RpmType type = BINARY;
	private Architecture architecture = NOARCH;
	private Os os = LINUX;

	public boolean build(RPMContent content, String savePath)
			throws NoSuchAlgorithmException, IOException {
		boolean createRPMFlag = true;
		String name = content.getBasicInfo().getName();
		String group = content.getBasicInfo().getGroup();
		String version = content.getBasicInfo().getVersion();
		String release = content.getBasicInfo().getRelease();
		String summary = content.getBasicInfo().getSummary();
		String description = content.getBasicInfo().getDescription();
		String license = content.getBasicInfo().getLicense();
		String provides = content.getBasicInfo().getProvides();
		String packager = content.getUserinfo().getPackager();
		String distribution = content.getUserinfo().getDistribution();
		String vendor = content.getUserinfo().getVendor();
		String url = content.getUserinfo().getUrl();

		File preInstallScript = new File(content.getPreInstallScript());
		File postInstallScript = new File(content.getPostInstallScript());
		File preUninstallScript = new File(content.getPreUninstallScript());
		File postUninstallScript = new File(content.getPostUninstallScript());

		File destination = new File(savePath);
		if (!destination.exists()) {
			destination.mkdirs();
		}
		String host = null;
		try {
			host = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			host = "";
		}
		Builder builder = new Builder();
		builder.setPackage(name, version, release);
		builder.setType(type);
		builder.setPlatform(architecture, os);
		builder.setGroup(group);
		builder.setBuildHost(host);
		builder.setSummary(summary);
		builder.setDescription(description);
		builder.setLicense(license);
		builder.setPackager(packager);

		builder.setDistribution(distribution);
		builder.setVendor(vendor);
		builder.setUrl(url);
		builder.setProvides(provides == null ? name : provides);

		if (null != preInstallScript && preInstallScript.exists()) {
			builder.setPreInstallScript(preInstallScript);
			builder.setPreInstallProgram("");
		}
		if (null != postInstallScript && postInstallScript.exists()) {
			builder.setPostInstallScript(postInstallScript);
			builder.setPostInstallProgram("");
		}
		if (null != preUninstallScript && preUninstallScript.exists()) {
			builder.setPreUninstallScript(preUninstallScript);
			builder.setPreUninstallProgram("");
		}
		if (null != postUninstallScript && postUninstallScript.exists()) {
			builder.setPostUninstallScript(postUninstallScript);
			builder.setPostUninstallProgram("");
		}

		List<RPMFile> rpmfiles = content.getFiles();

		if (rpmfiles != null && rpmfiles.size() > 0) {
			for (int i = 0; i < rpmfiles.size(); i++) {
				RPMFile rpmfile = rpmfiles.get(i);
				String prefix = rpmfile.getPrefix();
				File file = new File(rpmfile.getFile());
				String entry = file.getName();
								
				int filemode = Integer.parseInt(rpmfile.getFilemode(), 8) | UnixStat.FILE_FLAG;
				int dirmode = -1;
				// Directive directive ;
				String username = rpmfile.getUsername();
				String usergroup = rpmfile.getGroup();
				try {
					builder.addFile(prefix + File.separator + entry, file,
							filemode, dirmode, username, usergroup);
				} catch (NoSuchAlgorithmException e) {
					createRPMFlag = false;
					e.printStackTrace();
				} catch (IOException e) {
					createRPMFlag = false;
					e.printStackTrace();
				}
			}
		}
		// This generates a RPM file in the current directory named by the
		// package and type settings.
		builder.build(destination);
		return createRPMFlag;
	}

}
