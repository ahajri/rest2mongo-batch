package com.knoor.soft.model;

import java.util.List;

public class BCountry {

	private String name;
	private List<BCity> cities;

	public BCountry() {
		super();
		// TODO Auto-generated constructor stub
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
	public String toString() {
		return "BCountry [name=" + name + ", cities=" + cities.toString() + "]";
	}

}
