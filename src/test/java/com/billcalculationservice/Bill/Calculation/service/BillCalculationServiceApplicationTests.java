package com.billcalculationservice.Bill.Calculation.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
		"spring.main.banner-mode=off",
		"exchange.api.url=https://api.example.com",
		"exchange.api.key=test-key"
})
class BillCalculationServiceApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	public void testMainMethod() {
		BillCalculationServiceApplication.main(new String[]{});
	}

}
