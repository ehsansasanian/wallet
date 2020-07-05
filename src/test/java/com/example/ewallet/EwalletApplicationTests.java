package com.example.ewallet;

import com.example.ewallet.base.GenerateRandomChars;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EwalletApplicationTests {

	@Test
	void contextLoads() {
		String securityCode = GenerateRandomChars.generatedCode(5);

		System.out.println(securityCode);

	}
	@Test
	void test() {



	}
}
