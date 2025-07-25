package com.ecommerce.model;

import java.time.LocalDate;

import jakarta.persistence.Column;

public class PaymentInformation 
{
	@Column(name="cardholder_name")
	private String cardHolderName;
	@Column(name="card_number")
	private String cardName;
	@Column(name="expiration_date")
	private LocalDate expirationDate;
	@Column(name="cvv")
	private String cvv;
	public PaymentInformation() {
		super();
		// TODO Auto-generated constructor stub
	}
	public PaymentInformation(String cardHolderName, String cardName, LocalDate expirationDate, String cvv) {
		super();
		this.cardHolderName = cardHolderName;
		this.cardName = cardName;
		this.expirationDate = expirationDate;
		this.cvv = cvv;
	}
	public String getCardHolderName() {
		return cardHolderName;
	}
	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}
	public String getCardName() {
		return cardName;
	}
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	public LocalDate getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(LocalDate expirationDate) {
		this.expirationDate = expirationDate;
	}
	public String getCvv() {
		return cvv;
	}
	public void setCvv(String cvv) {
		this.cvv = cvv;
	}
	@Override
	public String toString() {
		return "PaymentInformation [cardHolderName=" + cardHolderName + ", cardName=" + cardName + ", expirationDate="
				+ expirationDate + ", cvv=" + cvv + "]";
	}
	
}
