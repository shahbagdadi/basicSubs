package com.netflix.subscription.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the entitlement database table.
 * 
 */
@Entity
@Table(name="entitlement")
@NamedQuery(name="Entitlement.findAll", query="SELECT e FROM Entitlement e")
public class Entitlement implements Serializable {
	private static final long serialVersionUID = 1L;

	private String description;

	@Column(name="ID")
	private int id;

	private String name;

	public Entitlement() {
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