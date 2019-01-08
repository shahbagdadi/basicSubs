package com.netflix.subscription.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the product database table.
 * 
 */
@Entity
@Table(name="product")
@NamedQuery(name="Product.findAll", query="SELECT p FROM Product p")
public class Product implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="entitlement_set_id")
	private int entitlementSetId;

//	private int id;
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

	private String sku;

	private String status;

	public Product() {
	}

	public int getEntitlementSetId() {
		return this.entitlementSetId;
	}

	public void setEntitlementSetId(int entitlementSetId) {
		this.entitlementSetId = entitlementSetId;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSku() {
		return this.sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}