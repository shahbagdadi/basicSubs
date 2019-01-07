package com.netflix.subscription.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the Payment_Method database table.
 * 
 */
@Entity
@NamedQuery(name="Payment_Method.findAll", query="SELECT p FROM Payment_Method p")
public class Payment_Method implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="customer_id")
	private int customerId;

	@Column(name="ID")
	private int id;

	@Column(name="payment_gateway")
	private String paymentGateway;

	@Column(name="payment_token")
	private String paymentToken;

	@Column(name="payment_type")
	private String paymentType;

	public Payment_Method() {
	}

	public int getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPaymentGateway() {
		return this.paymentGateway;
	}

	public void setPaymentGateway(String paymentGateway) {
		this.paymentGateway = paymentGateway;
	}

	public String getPaymentToken() {
		return this.paymentToken;
	}

	public void setPaymentToken(String paymentToken) {
		this.paymentToken = paymentToken;
	}

	public String getPaymentType() {
		return this.paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

}