package com.knoor.soft.batch.beans;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Batch event class
 * 
 * @author ahajri
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BEvent implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3488595320846672049L;

	private String type, name, recurring, description;

	private double lat, lon;

	private LocalDateTime startDatetime, endDatetime;

	public BEvent() {
		super();
	}

	public BEvent(String type, String name, String recurring, String description, double lat, double lon,
			LocalDateTime startDatetime, LocalDateTime endDatetime) {
		super();
		this.type = type;
		this.name = name;
		this.recurring = recurring;
		this.description = description;
		this.lat = lat;
		this.lon = lon;
		this.startDatetime = startDatetime;
		this.endDatetime = endDatetime;
	}

	public BEvent(String type, String name, String recurring, String description, LocalDateTime startDatetime,
			LocalDateTime endDatetime) {
		super();
		this.type = type;
		this.name = name;
		this.recurring = recurring;
		this.description = description;
		this.startDatetime = startDatetime;
		this.endDatetime = endDatetime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRecurring() {
		return recurring;
	}

	public void setRecurring(String recurring) {
		this.recurring = recurring;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public LocalDateTime getStartDatetime() {
		return startDatetime;
	}

	public void setStartDatetime(LocalDateTime startDatetime) {
		this.startDatetime = startDatetime;
	}

	public LocalDateTime getEndDatetime() {
		return endDatetime;
	}

	public void setEndDatetime(LocalDateTime endDatetime) {
		this.endDatetime = endDatetime;
	}

	@Override
	public String toString() {
		return "BEvent [type=" + type + ", name=" + name + ", recurring=" + recurring + ", description=" + description
				+ ", lat=" + lat + ", lon=" + lon + ", startDatetime=" + startDatetime + ", endDatetime=" + endDatetime
				+ "]";
	}

}
