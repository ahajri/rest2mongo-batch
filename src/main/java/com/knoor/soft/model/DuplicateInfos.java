package com.knoor.soft.model;

import java.io.Serializable;
import java.util.List;

public class DuplicateInfos implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1506214647097395579L;

	private String hadith;
	private List<Integer> uniqueIds;

	public DuplicateInfos() {

	}

	public String getHadith() {
		return hadith;
	}

	public void setHadith(String hadith) {
		this.hadith = hadith;
	}

	public List<Integer> getUniqueIds() {
		return uniqueIds;
	}

	public void setUniqueIds(List<Integer> uniqueIds) {
		this.uniqueIds = uniqueIds;
	}

}
