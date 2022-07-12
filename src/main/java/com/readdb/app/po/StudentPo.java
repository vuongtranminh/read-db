package com.readdb.app.po;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

public class StudentPo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String studentName;
	private Byte updatedAt;
	private BigDecimal price;
	private String description;
	private Long id;


	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName == null ? null : studentName.trim();
	}
	public Byte getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Byte updatedAt) {
		this.updatedAt = updatedAt;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description == null ? null : description.trim();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

}