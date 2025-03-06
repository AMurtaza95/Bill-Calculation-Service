package com.billcalculationservice.Bill.Calculation.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration"
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
