package com.readdb.app.util;

public class Pagination {

	private Long offset;
	private int limit;

	public Pagination(Long offset, int limit) {
		this.offset = offset;
		this.limit = limit;
	}

	public Long getOffset() { return offset; }
	public void setOffset(Long offset) { this.offset = offset; }
	public int getLimit() { return limit; }
	public void setLimit(int limit) { this.limit = limit; }
}