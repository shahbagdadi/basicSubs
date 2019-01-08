package com.netflix.subscription.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * The persistent class for the price database table.
 * 
 */
@Entity
@Table(name="price")
@NamedQuery(name="Price.findAll", query="SELECT p FROM Price p")
public class Price implements Serializable {
	private static final long serialVersionUID = 1L;

	private float amount;

	@Column(name="country_code")
	private String countryCode;

	@Column(name="currency_code")
	private String currencyCode;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="effective_date")
	private Date effectiveDate;

	//private int id;
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

	@Column(name="product_id")
	private long productId;

	private String status;

	public Price() {
	}

	public float getAmount() {
		return this.amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public String getCountryCode() {
		return this.countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCurrencyCode() {
		return this.currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public Date getEffectiveDate() {
		return this.effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getProductId() {
		return this.productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}