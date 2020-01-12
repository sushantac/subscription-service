package com.mypay.billing.subscription.entity;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

/**
 * @author Sushant Chanmanwar
 * 
 */
@Entity
public class Subscription {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	@NotNull
	@Embedded
	private Amount amount;

	@NotNull
	@Enumerated(EnumType.STRING)
	private SubscriptionType subscription_type;

	@JsonProperty(access = Access.WRITE_ONLY)
	@Min(value = 1, message = "Day of month should be between 1 to 31")
	@Max(value = 31, message = "Day of month should be between 1 to 31")
	private Integer date_of_month;

	@JsonProperty(access = Access.WRITE_ONLY)
	@Enumerated(EnumType.STRING)
	private DayOfWeek day_of_week;

	@NotNull(message = "Start date is required")
	private LocalDate start_date;

	@NotNull(message = "End date is required")
	private LocalDate end_date;

	@Transient
	private List<LocalDate> invoice_dates = new ArrayList<>();

	public Subscription() {

	}

	public Subscription(UUID id, @NotNull Amount amount, @NotNull SubscriptionType subscription_type,
			@Min(1) @Max(31) Integer date_of_month, DayOfWeek day_of_week, @NotNull LocalDate start_date,
			LocalDate end_date, List<LocalDate> invoice_dates) {
		super();
		this.id = id;
		this.amount = amount;
		this.subscription_type = subscription_type;
		this.date_of_month = date_of_month;
		this.day_of_week = day_of_week;
		this.start_date = start_date;
		this.end_date = end_date;
		this.invoice_dates = invoice_dates;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Amount getAmount() {
		return amount;
	}

	public void setAmount(Amount amount) {
		this.amount = amount;
	}

	public SubscriptionType getSubscription_type() {
		return subscription_type;
	}

	public void setSubscription_type(SubscriptionType subscription_type) {
		this.subscription_type = subscription_type;
	}

	public Integer getDate_of_month() {
		return date_of_month;
	}

	public void setDate_of_month(Integer date_of_month) {
		this.date_of_month = date_of_month;
	}

	public DayOfWeek getDay_of_week() {
		return day_of_week;
	}

	public void setDay_of_week(DayOfWeek day_of_week) {
		this.day_of_week = day_of_week;
	}

	public LocalDate getStart_date() {
		return start_date;
	}

	public void setStart_date(LocalDate start_date) {
		this.start_date = start_date;
	}

	public LocalDate getEnd_date() {
		return end_date;
	}

	public void setEnd_date(LocalDate end_date) {
		this.end_date = end_date;
	}

	public List<LocalDate> getInvoice_dates() {
		return invoice_dates;
	}

	public void setInvoice_dates(List<LocalDate> invoice_dates) {
		this.invoice_dates = invoice_dates;
	}

}
