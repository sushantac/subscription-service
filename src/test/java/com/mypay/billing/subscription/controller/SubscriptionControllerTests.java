package com.mypay.billing.subscription.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mypay.billing.subscription.SubscriptionServiceApplication;
import com.mypay.billing.subscription.entity.Amount;
import com.mypay.billing.subscription.entity.Currency;
import com.mypay.billing.subscription.entity.Subscription;
import com.mypay.billing.subscription.entity.SubscriptionRepository;
import com.mypay.billing.subscription.entity.SubscriptionType;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SubscriptionController.class)
@ContextConfiguration(classes = { SubscriptionRepository.class, SubscriptionServiceApplication.class })
class SubscriptionControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SubscriptionRepository subscriptionRepository;

	@Test
	void testGetAllSubscriptions() throws Exception {

		Subscription subscription = new Subscription();
		subscription.setAmount(new Amount(BigInteger.valueOf(100), Currency.AUD));
		subscription.setId(UUID.fromString("14335d51-265a-4e01-ad4d-306be659a48f"));
		subscription.setSubscription_type(SubscriptionType.MONTHLY);
		subscription.setDate_of_month(1);
		subscription.setStart_date(LocalDate.of(2019, 1, 1));
		subscription.setEnd_date(LocalDate.of(2019, 3, 1));

		List<Subscription> subscription_list = new ArrayList<>();
		subscription_list.add(subscription);

		Mockito.when(subscriptionRepository.findAll()).thenReturn(subscription_list);

		mockMvc.perform(get("/subscriptions").accept(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].id", is("14335d51-265a-4e01-ad4d-306be659a48f")))
				.andExpect(jsonPath("$[0].subscription_type", is("MONTHLY")))
				.andExpect(jsonPath("$[0].start_date", is("2019-01-01")))
				.andExpect(jsonPath("$[0].end_date", is("2019-03-01")))
				.andExpect(jsonPath("$[0].amount.value", is(100)))
				.andExpect(jsonPath("$[0].amount.currency", is("AUD")))
				.andExpect(jsonPath("$[0].invoice_dates[0]", is("2019-01-01")))
				.andExpect(jsonPath("$[0].invoice_dates[1]", is("2019-02-01")))
				.andExpect(jsonPath("$[0].invoice_dates[2]", is("2019-03-01")));

	}

	@Test
	void testGetSubscription() throws Exception {

		String id = "94335d51-265a-4e01-ad4d-306be659a48f";

		Subscription subscription = new Subscription();
		subscription.setAmount(new Amount(BigInteger.valueOf(100), Currency.AUD));
		subscription.setId(UUID.fromString(id));
		subscription.setSubscription_type(SubscriptionType.MONTHLY);
		subscription.setDate_of_month(1);
		subscription.setStart_date(LocalDate.of(2019, 1, 1));
		subscription.setEnd_date(LocalDate.of(2019, 3, 1));

		Optional<Subscription> optional = Optional.of(subscription);

		Mockito.when(subscriptionRepository.findById(Mockito.any(UUID.class))).thenReturn(optional);

		mockMvc.perform(get("/subscriptions/" + id).accept(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isOk()).andExpect(jsonPath("$.id", is(id)))
				.andExpect(jsonPath("$.subscription_type", is("MONTHLY")))
				.andExpect(jsonPath("$.start_date", is("2019-01-01")))
				.andExpect(jsonPath("$.end_date", is("2019-03-01"))).andExpect(jsonPath("$.amount.value", is(100)))
				.andExpect(jsonPath("$.amount.currency", is("AUD")))
				.andExpect(jsonPath("$.invoice_dates[0]", is("2019-01-01")))
				.andExpect(jsonPath("$.invoice_dates[1]", is("2019-02-01")))
				.andExpect(jsonPath("$.invoice_dates[2]", is("2019-03-01")));

	}

	@Test
	void testPostSubscription() throws Exception {
		String id = "94335d51-265a-4e01-ad4d-306be659a48f";

		Subscription subscription = new Subscription();
		subscription.setAmount(new Amount(BigInteger.valueOf(100), Currency.AUD));
		subscription.setId(UUID.fromString(id));
		subscription.setSubscription_type(SubscriptionType.MONTHLY);
		subscription.setDate_of_month(1);

		subscription.setStart_date(LocalDate.of(2019, 1, 1));
		subscription.setEnd_date(LocalDate.of(2019, 3, 1));

		Mockito.when(subscriptionRepository.save(Mockito.any(Subscription.class))).thenReturn(subscription);

		mockMvc.perform(post("/subscriptions").content(
				"{\"amount\": {\"value\": 100000,\"currency\": \"EUR\"},\"subscription_type\": \"MONTHLY\",\"date_of_month\": 29,\"day_of_week\": null,\"start_date\": \"2019-01-05\",\"end_date\": \"2019-03-04\"}")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

	}

	@Test
	void testPutSubscription() throws Exception {
		String id = "94335d51-265a-4e01-ad4d-306be659a48f";

		Subscription subscription = new Subscription();
		subscription.setAmount(new Amount(BigInteger.valueOf(100), Currency.AUD));
		subscription.setId(UUID.fromString(id));
		subscription.setSubscription_type(SubscriptionType.MONTHLY);
		subscription.setDate_of_month(1);

		subscription.setStart_date(LocalDate.of(2019, 1, 1));
		subscription.setEnd_date(LocalDate.of(2019, 3, 1));

		Mockito.when(subscriptionRepository.existsById(Mockito.any(UUID.class))).thenReturn(true);

		Mockito.when(subscriptionRepository.save(Mockito.any(Subscription.class))).thenReturn(subscription);

		mockMvc.perform(put("/subscriptions/" + id).content(
				"{\"amount\": {\"value\": 100000,\"currency\": \"EUR\"},\"subscription_type\": \"MONTHLY\",\"date_of_month\": 29,\"day_of_week\": null,\"start_date\": \"2019-01-05\",\"end_date\": \"2019-03-04\"}")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	void testDeleteSubscription() throws Exception {
		String id = "94335d51-265a-4e01-ad4d-306be659a48f";

		Subscription subscription = new Subscription();
		subscription.setAmount(new Amount(BigInteger.valueOf(100), Currency.AUD));
		subscription.setId(UUID.fromString(id));
		subscription.setSubscription_type(SubscriptionType.MONTHLY);
		subscription.setDate_of_month(1);

		subscription.setStart_date(LocalDate.of(2019, 1, 1));
		subscription.setEnd_date(LocalDate.of(2019, 3, 1));

		// Mockito.when(subscriptionRepository.delete(Mockito.any(Subscription.class))).thenReturn(subscription);
		Mockito.when(subscriptionRepository.existsById(Mockito.any(UUID.class))).thenReturn(true);
		
		Optional<Subscription> optional = Optional.of(subscription);
		
		Mockito.when(subscriptionRepository.deleteById(Mockito.any(UUID.class))).thenReturn(optional);
		
		mockMvc.perform(delete(("/subscriptions/" + id)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
