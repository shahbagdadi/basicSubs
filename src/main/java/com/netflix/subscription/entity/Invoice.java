package com.netflix.subscription.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the Invoice database table.
 * 
 */
@Entity
@NamedQuery(name="Invoice.findAll", query="SELECT i FROM Invoice i")
public class Invoice implements Serializable {
	private static final long serialVersionUID = 1L;

	private float balance;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_date")
	private Date createdDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="due_date")
	private Date dueDate;

	private int id;

	@Column(name="payment_id")
	private int paymentId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="service_period_end")
	private Date servicePeriodEnd;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="service_period_start")
	private Date servicePeriodStart;

	@Column(name="subscription_id")
	private int subscriptionId;

	public Invoice() {
	}

	public float getBalance() {
		return this.balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getDueDate() {
		return this.dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPaymentId() {
		return this.paymentId;
	}

	public void setPaymentId(int paymentId) {
		this.paymentId = paymentId;
	}

	public Date getServicePeriodEnd() {
		return this.servicePeriodEnd;
	}

	public void setServicePeriodEnd(Date servicePeriodEnd) {
		this.servicePeriodEnd = servicePeriodEnd;
	}

	public Date getServicePeriodStart() {
		return this.servicePeriodStart;
	}

	public void setServicePeriodStart(Date servicePeriodStart) {
		this.servicePeriodStart = servicePeriodStart;
	}

	public int getSubscriptionId() {
		return this.subscriptionId;
	}

	public void setSubscriptionId(int subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

}