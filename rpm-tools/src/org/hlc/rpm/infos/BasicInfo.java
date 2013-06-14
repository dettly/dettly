package org.hlc.rpm.infos;

public class BasicInfo {
	// RPM������
	private String name;
	// RPM���汾��
	private String version;
	// �ͳ���
	private String release;
	// ��������
	private String provides;
	// RPM������
	private String summary;
	// RPM������
	private String description;
	// RPM�����
	private String group;
	// ���֤���Ȩ����
	private String license;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getRelease() {
		return release;
	}

	public void setRelease(String release) {
		this.release = release;
	}

	public String getProvides() {
		return provides;
	}

	public void setProvides(String provides) {
		this.provides = provides;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	@Override
	public String toString() {
		return "BasicInfo [name=" + name + ", version=" + version
				+ ", release=" + release + ", provides=" + provides
				+ ", summary=" + summary + ", description=" + description
				+ ", group=" + group + ", license=" + license + "]";
	}

}
