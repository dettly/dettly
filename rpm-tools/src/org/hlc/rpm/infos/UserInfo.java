package org.hlc.rpm.infos;

public class UserInfo {
	// 打包者姓名
	private String packager;
	// 供给商
	private String vendor;
	// 发行版
	private String distribution;
	// 网页地址
	private String url;

	public String getPackager() {
		return packager;
	}

	public void setPackager(String packager) {
		this.packager = packager;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getDistribution() {
		return distribution;
	}

	public void setDistribution(String distribution) {
		this.distribution = distribution;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "UserInfo [packager=" + packager + ", vendor=" + vendor
				+ ", distribution=" + distribution + ", url=" + url + "]";
	}

}
