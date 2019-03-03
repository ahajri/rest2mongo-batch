package com.knoor.soft.batch.beans;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BCountry implements Serializable{

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 8418291147157086455L;

	private String name;
	
	private List<BCity> cities;

	public BCountry() {
		super();
	}

	public BCountry(String name, List<BCity> cities) {
		super();
		this.name = name;
		this.cities = cities;
	}

	public BCountry(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<BCity> getCities() {
		return cities;
	}

	public void setCities(List<BCity> cities) {
		this.cities = cities;
	}

	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cities == null) ? 0 : cities.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BCountry other = (BCountry) obj;
		if (cities == null) {
			if (other.cities != null)
				return false;
		} else if (!cities.equals(other.cities))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BCountry [name=" + name + ", cities=" + cities + "]";
	}

	
}
