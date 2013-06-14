package org.hlc.rpm.infos;

public class RPMLink {
	// 源路径
	String path;
	// 安装目标路径
	String target;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	@Override
	public String toString() {
		return "RPMLink [path=" + path + ", target=" + target + "]";
	}
}
