package com.mypay.billing.subscription.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mypay.billing.subscription.SubscriptionServiceApplication;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = { SubscriptionServiceApplication.class })
class SubscriptionRepositoryTests {

	@Autowired
	SubscriptionRepository repository;

	@Test
	public void testFindSubscriptions() {

		List<Subscription> subscriptions = repository.findAll();

		assertNotNull(subscriptions);
		assertTrue(!subscriptions.isEmpty());
		assertNotNull(subscriptions.get(0));
		assertNotNull(subscriptions.get(0).getId());
	}
	
	@Test
	public void testCreateSubscription() {
		Subscription subscription = new Subscription();
		subscription.setAmount(new Amount(BigInteger.valueOf(100), Currency.AUD));
		subscription.setSubscription_type(SubscriptionType.MONTHLY);
		subscription.setDate_of_month(1);
		subscription.setStart_date(LocalDate.of(2019, 1, 1));
		subscription.setEnd_date(LocalDate.of(2019, 3, 1));

		repository.save(subscription);

		assertNotNull(subscription);
		assertNotNull(subscription.getId());
		
		Optional<Subscription> optional = repository.findById(subscription.getId());
		
		assertNotNull(optional);
		assertNotNull(optional.get());
		assertNotNull(optional.get().getId());
	}
	
	@Test
	public void testUpdateSubscription() {
		Subscription subscription = new Subscription();
		subscription.setAmount(new Amount(BigInteger.valueOf(100), Currency.AUD));
		subscription.setSubscription_type(SubscriptionType.MONTHLY);
		subscription.setDate_of_month(1);
		subscription.setStart_date(LocalDate.of(2019, 1, 1));
		subscription.setEnd_date(LocalDate.of(2019, 3, 1));

		repository.save(subscription);

		assertNotNull(subscription);
		assertNotNull(subscription.getId());
		
		subscription.setAmount(new Amount(BigInteger.valueOf(1991), Currency.CAD));
		repository.save(subscription);
		
		assertNotNull(subscription);
		assertNotNull(subscription.getAmount());
		assertNotNull(subscription.getAmount().getValue());
		assertEquals(subscription.getAmount().getValue().intValue(), 1991);
		assertEquals(subscription.getAmount().getCurrency(), Currency.CAD);
	}
	
	
	@Test
	public void testDeleteSubscription() {
		
		Subscription subscription = new Subscription();
		subscription.setAmount(new Amount(BigInteger.valueOf(100), Currency.AUD));
		subscription.setSubscription_type(SubscriptionType.MONTHLY);
		subscription.setDate_of_month(1);
		subscription.setStart_date(LocalDate.of(2019, 1, 1));
		subscription.setEnd_date(LocalDate.of(2019, 3, 1));

		repository.save(subscription);

		assertNotNull(subscription);
		assertNotNull(subscription.getId());
		
		UUID id = subscription.getId();
		
		repository.delete(subscription);
		
		Optional<Subscription> optional = repository.findById(id);
		
		assertTrue(!optional.isPresent());
		
		
	}
	
	

}
