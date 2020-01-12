package com.mypay.billing.subscription.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.mypay.billing.subscription.entity.Subscription;
import com.mypay.billing.subscription.entity.SubscriptionRepository;
import com.mypay.billing.subscription.exception.SubscriptionNotFoundException;
import com.mypay.billing.subscription.util.SubscriptionUtil;

@RestController
public class SubscriptionController {

	
	@Autowired
	private SubscriptionRepository subscriptionRepository;

	
	/**
	 * Get all scubscriptions
	 * 
	 * @return List<Subscription>
	 */
	@RequestMapping(path = "/subscriptions", method = RequestMethod.GET)
	public List<Subscription> getAllSubscriptions() {
		List<Subscription> subscriptions = subscriptionRepository.findAll();
		
		subscriptions = subscriptions.stream().map(s -> {
			s.setInvoice_dates(SubscriptionUtil.getSubscriptionInvoiceDates(s));
			return s;
		}).collect(Collectors.toList());
		
		return subscriptions;
	}
	

	/**
	 * Get subscription
	 * 
	 * @param id
	 * @return {@link Subscription}
	 */
	@RequestMapping(path = "/subscriptions/{id}", method = RequestMethod.GET)
	public Subscription getSubscriptionById(@PathVariable UUID id) {

		Optional<Subscription> optional = subscriptionRepository.findById(id);

		if (!optional.isPresent()) {
			throw new SubscriptionNotFoundException("Subscription not found for id: " + id);
		}
		Subscription subscription = optional.get();
		
		subscription.setInvoice_dates(SubscriptionUtil.getSubscriptionInvoiceDates(subscription));

		return subscription;
	}
	

	/**
	 * Create subscription
	 * 
	 * @param {@link Subscription}
	 */
	@RequestMapping(path = "/subscriptions", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> createSubscription(@Valid @RequestBody Subscription subscription) {

		Subscription savedSubscription = subscriptionRepository.save(subscription);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedSubscription.getId()).toUri();

		return ResponseEntity.created(location).build();
	}
	

	/**
	 * Update subscription
	 * 
	 * @param {@link Subscription}
	 */
	@RequestMapping(path = "/subscriptions/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateSubscription(@PathVariable UUID id, @Valid @RequestBody Subscription subscription) {
		boolean is_present = subscriptionRepository.existsById(id);

		if (!is_present) {
			throw new SubscriptionNotFoundException("Subscription not found for id: " + id);
		}

		subscription.setId(id);
		subscriptionRepository.save(subscription);
	}
	

	/**
	 * Delete subscription
	 * 
	 * @param id
	 */
	@RequestMapping(path = "/subscriptions/{id}", method = RequestMethod.DELETE)
	public void deleteSubscription(@PathVariable UUID id) {

		boolean is_present = subscriptionRepository.existsById(id);

		if (!is_present) {
			throw new SubscriptionNotFoundException("Subscription not found for id: " + id);
		}
		
		subscriptionRepository.deleteById(id);

	}

}
