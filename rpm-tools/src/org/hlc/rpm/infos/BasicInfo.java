package org.hlc.rpm.infos;

public class BasicInfo {
	// RPM包名称
	private String name;
	// RPM包版本号
	private String version;
	// 释出号
	private String release;
	// 功能描述
	private String provides;
	// RPM包概述
	private String summary;
	// RPM包描述
	private String description;
	// RPM包类别
	private String group;
	// 许可证或版权规则
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
