package com.badis.productservice;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.badis.productservice.dto.ProductRequest;
import com.badis.productservice.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class ProductServiceApplicationTests {


	@Autowired
	private ProductRepository productRepository;
	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.3");
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
		dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@Test
	void shouldCreateProduct() throws Exception {
		ProductRequest ProductRequest = getProductRequest();
		//convert object to String because content only acccepts String
		String productRequestString = objectMapper.writeValueAsString(ProductRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
		 		.contentType(MediaType.APPLICATION_JSON)
		 		.content(productRequestString))
		 		.andExpect(status().isCreated());
				// .andExpect(result -> result.getResponse().getContentAsString().isEmpty());
	}
	@Test
	void NotEmpty() throws Exception {
		Assertions.assertThat(productRepository.findAll()).isNotEmpty();
	}

	private ProductRequest getProductRequest() {
		return ProductRequest.builder()
				.name("iphone 13")
				.description("gold")
				.price(BigDecimal.valueOf(1200))
				.build();
	}

}
