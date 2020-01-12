package com.mypay.billing.subscription.entity;

import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, String>{

	public Optional<Subscription> findById(UUID id);
	
	@Transactional
	public Optional<Subscription> deleteById(UUID id);
	
	public boolean existsById(UUID id);
	
}
