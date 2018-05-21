package com.ahajri.heaven.calendar.beans;

import java.util.List;

public class Riwaya {

	private String sanad, matn;
	
	private List<Book> books;
	
	public String getMatn() {
		return matn;
	}
	
	
	public String getSanad() {
		return sanad;
	}


	public void setMatn(String matn) {
		this.matn = matn;
	}


	public void setSanad(String sanad) {
		this.sanad = sanad;
	}
	
	public List<Book> getBooks() {
		return books;
	}
	public void setBooks(List<Book> books) {
		this.books = books;
	}
	
}
