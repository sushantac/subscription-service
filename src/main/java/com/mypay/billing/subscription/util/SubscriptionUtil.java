package com.mypay.billing.subscription.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.mypay.billing.subscription.entity.Subscription;
import com.mypay.billing.subscription.entity.SubscriptionType;

public class SubscriptionUtil {
	
	
	public static List<LocalDate> getSubscriptionInvoiceDates(Subscription subscription) {

		List<LocalDate> invoice_dates = null;

		if (subscription != null) {
			if (SubscriptionType.MONTHLY.equals(subscription.getSubscription_type())) {
				
				invoice_dates = getMonthlySubscriptionInvoiceDates(subscription.getDate_of_month(),
						subscription.getStart_date(), subscription.getEnd_date());
				
			} else if (SubscriptionType.WEEKLY.equals(subscription.getSubscription_type())) {
				
				invoice_dates = getWeeklySubscriptionInvoiceDates(subscription.getDay_of_week(),
						subscription.getStart_date(), subscription.getEnd_date());
			}
		}

		return invoice_dates;
	}
	

	/**
	 * Calculate and return qualifying invoice dates for Monthly Subscription
	 * 
	 * @param date_of_month
	 * @param start_date
	 * @param end_date
	 * @return
	 */
	public static List<LocalDate> getMonthlySubscriptionInvoiceDates(Integer date_of_month, LocalDate start_date, LocalDate end_date) {

		List<LocalDate> invoice_dates = new ArrayList<>();

		LocalDate date_in_question = start_date;

		LocalDate qualifying_date = null;
		LocalDate last_date_in_question = null;

		while (!date_in_question.isAfter(end_date)) {

			// e.g. start_date 28-feb, date_of_month 31
			// check the length of month ( 28 < 31 )
			if (date_in_question.lengthOfMonth() < date_of_month) {

				/** option 1 - charge on the last day of the current month */
				// get the last date of current month e.g. 28-feb
				/**
				 * qualifying_date = LocalDate.of(date_in_question.getYear(),
				 * date_in_question.getMonth(), date_in_question.lengthOfMonth());
				 */

				/**
				 * option 2 - charge on 30th day after the last charged day (do check later if
				 * it is before end_date!)
				 */
				// check if this is first subscription date, if yes - take the next month date
				if (last_date_in_question != null) {
					// get the 30th day from last charged date
					qualifying_date = last_date_in_question.plusDays(30);
				}

			} else {
				qualifying_date = LocalDate.of(date_in_question.getYear(), date_in_question.getMonth(), date_of_month);
			}

			// validate if the next calculated qualifying date is not past the end date
			if (!qualifying_date.isAfter(end_date)) {
				invoice_dates.add(qualifying_date);
			} else {
				// we went past the end_date, let's break out early!
				break;
			}

			// keep the last subscription date to use in the next iteration
			last_date_in_question = qualifying_date;

			// advance to next month
			date_in_question = qualifying_date.plusMonths(1);

		}

		return invoice_dates;
	}

	/**
	 * Calculate and return qualifying invoice dates for Weekly Subscription
	 * 
	 * @param day_of_week
	 * @param start_date
	 * @param end_date
	 * @return List<LocalDate>
	 */
	public static List<LocalDate> getWeeklySubscriptionInvoiceDates(DayOfWeek day_of_week, LocalDate start_date, LocalDate end_date) {

		List<LocalDate> invoice_dates = new ArrayList<>();

		LocalDate date_in_question = start_date;

		LocalDate qualifying_date = null;

		// 1 (Monday) to 7 (Sunday)
		// get the first qualifying date of week to charge
		DayOfWeek day_of_start_date = start_date.getDayOfWeek();

		// Wednesday(start_date) == WEDNESDAY - you got your first date!
		if (day_of_start_date.getValue() == day_of_week.getValue()) {

			qualifying_date = date_in_question;
		}
		// Friday(start_date) > WEDNESDAY - start date is after day_of_week, take the
		// date of day_of_week from next week
		else if (day_of_start_date.getValue() > day_of_week.getValue()) {

			qualifying_date = date_in_question.plusDays(7 - (day_of_start_date.getValue() - day_of_week.getValue()));
		}
		// Monday(start_date) < WEDNESDAY - start date is before day_of_week, take the
		// date of day_of_week from current week
		else if (day_of_start_date.getValue() < day_of_week.getValue()) {

			qualifying_date = date_in_question.plusDays(day_of_week.getValue() - day_of_start_date.getValue());
		}

		while (!qualifying_date.isAfter(end_date)) {
			// collect the qualifying invoice date
			invoice_dates.add(qualifying_date);

			// advance to next week
			qualifying_date = qualifying_date.plusDays(7);
		}

		return invoice_dates;
	}
}
