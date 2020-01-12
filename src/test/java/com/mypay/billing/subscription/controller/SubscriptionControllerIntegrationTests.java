package com.mypay.billing.subscription.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mypay.billing.subscription.SubscriptionServiceApplication;
import com.mypay.billing.subscription.entity.Amount;
import com.mypay.billing.subscription.entity.Currency;
import com.mypay.billing.subscription.entity.Subscription;
import com.mypay.billing.subscription.entity.SubscriptionType;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SubscriptionServiceApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
class SubscriptionControllerIntegrationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	HttpHeaders headers = new HttpHeaders();

	@Test
	public void testGetAllSubscriptions() {
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		assertTrue(!restTemplate.exchange(getURLWithPort("/subscriptions"), HttpMethod.GET, entity, List.class)
				.getBody().isEmpty());

	}

	@Test
	public void testGetSubscription() {

		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		String id = "14335d51-265a-4e01-ad4d-306be659a48f";

		assertTrue(restTemplate
				.exchange(getURLWithPort("/subscriptions/" + id), HttpMethod.GET, entity, Subscription.class).getBody()
				.getId().equals(UUID.fromString(id)));
	}

	@Test
	public void testCreateSubscription() {

		Subscription subscription = new Subscription();
		subscription.setAmount(new Amount(BigInteger.valueOf(100), Currency.AUD));
		subscription.setSubscription_type(SubscriptionType.MONTHLY);
		subscription.setDate_of_month(1);
		subscription.setStart_date(LocalDate.of(2019, 1, 1));
		subscription.setEnd_date(LocalDate.of(2019, 3, 1));

		HttpEntity<Subscription> entity = new HttpEntity<Subscription>(subscription, headers);

		ResponseEntity<Object> response = restTemplate.exchange(getURLWithPort("/subscriptions"), HttpMethod.POST,
				entity, Object.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getHeaders().get(HttpHeaders.LOCATION));
		assertTrue(!response.getHeaders().get(HttpHeaders.LOCATION).isEmpty());
		assertTrue(response.getHeaders().get(HttpHeaders.LOCATION).get(0).contains("/subscriptions"));

	}

	@Test
	public void testUpdateSubscription() {

		HttpEntity<Subscription> entity = new HttpEntity<Subscription>(null, headers);

		BigInteger new_amount = BigInteger.valueOf(1991);

		// get existing subscription
		String id = "14335d51-265a-4e01-ad4d-306be659a48f";
		Subscription existingSubscription = restTemplate
				.exchange(getURLWithPort("/subscriptions/" + id), HttpMethod.GET, entity, Subscription.class).getBody();

		// change values
		existingSubscription.getAmount().setValue(new_amount);

		// update subscription
		entity = new HttpEntity<Subscription>(existingSubscription, headers);
		ResponseEntity<Object> response = restTemplate.exchange(getURLWithPort("/subscriptions/" + id), HttpMethod.PUT,
				entity, Object.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());

	}

	@Test
	public void testDeleteSubscription() {

		HttpEntity<Subscription> entity = new HttpEntity<Subscription>(null, headers);

		// get existing subscription
		String id = "21435d51-265a-4e01-ad4d-306be659a48f";
		Subscription existingSubscription = restTemplate
				.exchange(getURLWithPort("/subscriptions/" + id), HttpMethod.GET, entity, Subscription.class).getBody();

		// delete subscription
		entity = new HttpEntity<Subscription>(existingSubscription, headers);
		ResponseEntity<Object> response = restTemplate.exchange(getURLWithPort("/subscriptions/" + id), HttpMethod.DELETE,
				entity, Object.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());

	}

	private String getURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}

}
