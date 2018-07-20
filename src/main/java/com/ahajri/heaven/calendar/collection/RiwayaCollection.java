package com.ahajri.heaven.calendar.collection;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ahajri.heaven.calendar.utils.HCDateUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = false)
@Document(collection = "RiwayaCollection")
public class RiwayaCollection implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -539518209768860190L;
	
	

	public  RiwayaCollection() {
		
	}

	@Id
	private String idRiwaya;

	private String sanad;

	private String matn;

	private List<BookCollection> books;



	public String getIdRiwaya() {
		return idRiwaya;
	}

	public void setIdRiwaya(String idRiwaya) {
		this.idRiwaya = idRiwaya;
	}

	public String getSanad() {
		return sanad;
	}

	public void setSanad(String sanad) {
		this.sanad = sanad;
	}

	public String getMatn() {
		return matn;
	}

	public void setMatn(String matn) {
		this.matn = matn;
	}

	public List<BookCollection> getBooks() {
		return books;
	}

	public void setBooks(List<BookCollection> books) {
		this.books = books;
	}

	
}
