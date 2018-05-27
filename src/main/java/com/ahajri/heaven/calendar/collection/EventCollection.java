package com.ahajri.heaven.calendar.collection;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ahajri.heaven.calendar.beans.Riwaya;
import com.ahajri.heaven.calendar.utils.HCDateUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = false)
@Document(collection = "EventCollection")
public class EventCollection implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2228859318316143931L;
	
	public  EventCollection() {
		
	}

	@Id
	private String idEvent;

	@Indexed(unique = false, direction = IndexDirection.ASCENDING)
	private String title;

	private String recurring;

	private Date startDateTime;

	private Date endDateTime;

	private boolean isAllDay = false;
	
	private String description;
	
	private List<Riwaya> riwayats;

	public String getIdEvent() {
		return idEvent;
	}

	public void setIdEvent(String idEvent) {
		this.idEvent = idEvent;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRecurring() {
		return recurring;
	}

	public void setRecurring(String recurring) {
		this.recurring = recurring;
	}

	public Date getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}

	public Date getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(Date endDateTime) {
		if (endDateTime!=null) {
			this.endDateTime = endDateTime;
		}else{
			this.endDateTime = HCDateUtils.getMaxDate();
		}
		
	}

	public boolean isAllDay() {
		return isAllDay;
	}

	public void setAllDay(boolean isAllDay) {
		this.isAllDay = isAllDay;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public List<Riwaya> getRiwayats() {
		return riwayats;
	}
	
	public void setRiwayats(List<Riwaya> riwayats) {
		this.riwayats = riwayats;
	}
	
	

	@Override
	public String toString() {
		
		return "EventCollection [idEvent=" + idEvent + ", title=" + title + ", recurring=" + recurring
				+ ", startDateTime=" + startDateTime + ", endDateTime=" + endDateTime + ", isAllDay=" + isAllDay + "]";
	}

}
