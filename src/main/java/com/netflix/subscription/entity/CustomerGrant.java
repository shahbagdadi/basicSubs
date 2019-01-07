package com.netflix.subscription.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the customer_grant database table.
 * 
 */
@Entity
@Table(name="customer_grant")
@NamedQuery(name="CustomerGrant.findAll", query="SELECT c FROM CustomerGrant c")
public class CustomerGrant implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="customer_id")
	private int customerId;

	@Column(name="entitlement_set_id")
	private int entitlementSetId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date expiry;

	public CustomerGrant() {
	}

	public int getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public int getEntitlementSetId() {
		return this.entitlementSetId;
	}

	public void setEntitlementSetId(int entitlementSetId) {
		this.entitlementSetId = entitlementSetId;
	}

	public Date getExpiry() {
		return this.expiry;
	}

	public void setExpiry(Date expiry) {
		this.expiry = expiry;
	}

}