package org.hlc.rpm.infos;

public class RPMFile {
	// 安装路径
	String prefix;
	// 源文件路径
	String file;
	// 安装模式
	String filemode;
	// 安装用户名
	String username;
	// 用户分组
	String group;

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getFilemode() {
		return filemode;
	}

	public void setFilemode(String filemode) {
		this.filemode = filemode;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	@Override
	public String toString() {
		return "RPMFile [prefix=" + prefix + ", file=" + file + ", filemode="
				+ filemode + ", username=" + username + ", group=" + group
				+ "]";
	}
}
