package com.mypay.billing.subscription.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mypay.billing.subscription.entity.Amount;
import com.mypay.billing.subscription.entity.Currency;
import com.mypay.billing.subscription.entity.Subscription;
import com.mypay.billing.subscription.entity.SubscriptionType;

@ExtendWith(SpringExtension.class)
class SubscriptionUtilTest {
	
	Subscription subscription;
	
	@BeforeEach
	void init() {
		subscription = new Subscription();
		subscription.setId(UUID.fromString("14335d51-265a-4e01-ad4d-306be659a48f"));
		subscription.setAmount(new Amount(BigInteger.valueOf(100), Currency.AUD));
	}

	@Test
	void testMonthlyInvoiceDates() {
		
		subscription.setSubscription_type(SubscriptionType.MONTHLY);
		subscription.setDate_of_month(1);
		subscription.setStart_date(LocalDate.of(2019,1,1));
		subscription.setEnd_date(LocalDate.of(2019,3,1));
		
		List<LocalDate> invoiceDates = SubscriptionUtil.getSubscriptionInvoiceDates(subscription);
		System.out.println(invoiceDates);
		
		List<LocalDate> expectedInvoiceDates = new ArrayList<>();
		expectedInvoiceDates.add(LocalDate.of(2019,1,1));
		expectedInvoiceDates.add(LocalDate.of(2019,2,1));
		expectedInvoiceDates.add(LocalDate.of(2019,3,1));
		
		assertEquals(invoiceDates,expectedInvoiceDates);
	}
	
	@Test
	void testMonthlyInvoiceDates_forFebruaryLeapYear() {
		
	}
	
	@Test
	void testMonthlyInvoiceDates_forFebruaryNonLeapYear() {
		
	}
	
	@Test
	void testMonthlyInvoiceDates_forMonthWith31Days() {
		
	}
	
	@Test
	void testMonthlyInvoiceDates_forMonthWith30Days() {
		
	}
	
	
	@Test
	void testWeeklyInvoiceDates_startDateIsOnDayOfWeek() {
		
		subscription.setSubscription_type(SubscriptionType.WEEKLY);
		subscription.setDay_of_week(DayOfWeek.WEDNESDAY);
		subscription.setStart_date(LocalDate.of(2020,1,8));
		subscription.setEnd_date(LocalDate.of(2020,3,7));
		
		List<LocalDate> invoiceDates = SubscriptionUtil.getSubscriptionInvoiceDates(subscription);
		System.out.println(invoiceDates);
		
		List<LocalDate> expectedInvoiceDates = new ArrayList<>();
		expectedInvoiceDates.add(LocalDate.of(2020,1,8));
		expectedInvoiceDates.add(LocalDate.of(2020,1,15));
		expectedInvoiceDates.add(LocalDate.of(2020,1,22));
		expectedInvoiceDates.add(LocalDate.of(2020,1,29));
		expectedInvoiceDates.add(LocalDate.of(2020,2,5));
		expectedInvoiceDates.add(LocalDate.of(2020,2,12));
		expectedInvoiceDates.add(LocalDate.of(2020,2,19));
		expectedInvoiceDates.add(LocalDate.of(2020,2,26));
		expectedInvoiceDates.add(LocalDate.of(2020,3,04));
		
		assertEquals(invoiceDates,expectedInvoiceDates);
	}
	
	@Test
	void testWeeklyInvoiceDates_startDateIsBeforeDayOfWeek() {
		
		subscription.setSubscription_type(SubscriptionType.WEEKLY);
		subscription.setDay_of_week(DayOfWeek.WEDNESDAY);
		subscription.setStart_date(LocalDate.of(2020,1,6));
		subscription.setEnd_date(LocalDate.of(2020,3,7));
		
		List<LocalDate> invoiceDates = SubscriptionUtil.getSubscriptionInvoiceDates(subscription);
		System.out.println(invoiceDates);
		
		List<LocalDate> expectedInvoiceDates = new ArrayList<>();
		expectedInvoiceDates.add(LocalDate.of(2020,1,8));
		expectedInvoiceDates.add(LocalDate.of(2020,1,15));
		expectedInvoiceDates.add(LocalDate.of(2020,1,22));
		expectedInvoiceDates.add(LocalDate.of(2020,1,29));
		expectedInvoiceDates.add(LocalDate.of(2020,2,5));
		expectedInvoiceDates.add(LocalDate.of(2020,2,12));
		expectedInvoiceDates.add(LocalDate.of(2020,2,19));
		expectedInvoiceDates.add(LocalDate.of(2020,2,26));
		expectedInvoiceDates.add(LocalDate.of(2020,3,04));
		
		assertEquals(invoiceDates,expectedInvoiceDates);
	}
	
	@Test
	void testWeeklyInvoiceDates_startDateIsAfterDayOfWeek() {
		
		subscription.setSubscription_type(SubscriptionType.WEEKLY);
		subscription.setDay_of_week(DayOfWeek.WEDNESDAY);
		subscription.setStart_date(LocalDate.of(2020,1,10));
		subscription.setEnd_date(LocalDate.of(2020,3,7));
		
		List<LocalDate> invoiceDates = SubscriptionUtil.getSubscriptionInvoiceDates(subscription);
		System.out.println(invoiceDates);
		
		List<LocalDate> expectedInvoiceDates = new ArrayList<>();
		expectedInvoiceDates.add(LocalDate.of(2020,1,15));
		expectedInvoiceDates.add(LocalDate.of(2020,1,22));
		expectedInvoiceDates.add(LocalDate.of(2020,1,29));
		expectedInvoiceDates.add(LocalDate.of(2020,2,5));
		expectedInvoiceDates.add(LocalDate.of(2020,2,12));
		expectedInvoiceDates.add(LocalDate.of(2020,2,19));
		expectedInvoiceDates.add(LocalDate.of(2020,2,26));
		expectedInvoiceDates.add(LocalDate.of(2020,3,04));
		
		assertEquals(invoiceDates,expectedInvoiceDates);
	}
	
	
}
