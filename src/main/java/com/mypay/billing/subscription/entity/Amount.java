package com.mypay.billing.subscription.entity;

import java.math.BigInteger;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import io.micrometer.core.lang.NonNull;

@Embeddable
public class Amount {
	
	@NonNull
	private BigInteger value;
	
	@NonNull
	@Enumerated(EnumType.STRING)
	private Currency currency;
	
	public Amount() {
		
	}
	
	public Amount(BigInteger value, Currency currency) {
		super();
		this.value = value;
		this.currency = currency;
	}
	
	public BigInteger getValue() {
		return value;
	}
	public void setValue(BigInteger value) {
		this.value = value;
	}
	public Currency getCurrency() {
		return currency;
	}
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	
}

