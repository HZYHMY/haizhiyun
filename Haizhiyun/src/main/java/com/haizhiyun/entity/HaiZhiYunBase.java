package com.haizhiyun.entity;

import java.io.Serializable;

public class HaiZhiYunBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String uuid;

	public HaiZhiYunBase() {
		super();
	}

	public HaiZhiYunBase(Long id, String uuid) {
		super();
		this.id = id;
		this.uuid = uuid;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}
