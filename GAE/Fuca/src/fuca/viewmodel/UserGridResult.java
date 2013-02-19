package fuca.viewmodel;

import java.util.List;

import fuca.model.Termin;

public class UserGridResult {

	private int total;
	private int page;
	private int records;
	
	private List<fuca.model.User> rows;
	
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRecords() {
		return records;
	}
	public void setRecords(int records) {
		this.records = records;
	}
	public List<fuca.model.User> getRows() {
		return rows;
	}
	public void setRows(List<fuca.model.User> rows) {
		this.rows = rows;
	}

}
