package com.netflix.subscription.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the invoice_line_item database table.
 * 
 */
@Entity
@Table(name="invoice_line_item")
@NamedQuery(name="InvoiceLineItem.findAll", query="SELECT i FROM InvoiceLineItem i")
public class InvoiceLineItem implements Serializable {
	private static final long serialVersionUID = 1L;

	private float amount;

	private String currency;

	private String description;

	private int id;

	@Column(name="Invoice_id")
	private int invoice_id;

	private String type;

	public InvoiceLineItem() {
	}

	public float getAmount() {
		return this.amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return this.currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
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

	public int getInvoice_id() {
		return this.invoice_id;
	}

	public void setInvoice_id(int invoice_id) {
		this.invoice_id = invoice_id;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

}