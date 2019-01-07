package com.netflix.subscription.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the entitlement_set_mapping database table.
 * 
 */
@Entity
@Table(name="entitlement_set_mapping")
@NamedQuery(name="EntitlementSetMapping.findAll", query="SELECT e FROM EntitlementSetMapping e")
public class EntitlementSetMapping implements Serializable {
	private static final long serialVersionUID = 1L;

	private int entitlement_ID;

	@Column(name="entitlement_set_id")
	private int entitlementSetId;

	public EntitlementSetMapping() {
	}

	public int getEntitlement_ID() {
		return this.entitlement_ID;
	}

	public void setEntitlement_ID(int entitlement_ID) {
		this.entitlement_ID = entitlement_ID;
	}

	public int getEntitlementSetId() {
		return this.entitlementSetId;
	}

	public void setEntitlementSetId(int entitlementSetId) {
		this.entitlementSetId = entitlementSetId;
	}

}