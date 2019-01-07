package com.netflix.subscription.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the entitlement_set database table.
 * 
 */
@Entity
@Table(name="entitlement_set")
@NamedQuery(name="EntitlementSet.findAll", query="SELECT e FROM EntitlementSet e")
public class EntitlementSet implements Serializable {
	private static final long serialVersionUID = 1L;

	private String description;

	private int id;

	private String name;

	public EntitlementSet() {
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}