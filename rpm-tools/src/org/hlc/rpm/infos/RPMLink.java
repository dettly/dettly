package org.hlc.rpm.infos;

public class RPMLink {
	// Դ·��
	String path;
	// ��װĿ��·��
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
