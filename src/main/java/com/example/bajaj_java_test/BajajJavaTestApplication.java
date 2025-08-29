package com.example.bajaj_java_test;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class BajajJavaTestApplication implements CommandLineRunner {

	private final RestTemplate restTemplate = new RestTemplate();

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(BajajJavaTestApplication.class);
		app.setWebApplicationType(WebApplicationType.NONE);
		app.run(args);

	}

	@Override
	public void run(String... args) throws Exception {
		// 1. Generate webhook + token
		String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

		Map<String, String> request = new HashMap<>();
		request.put("name", "Swayam Tandon");
		request.put("regNo", "22BCE9312");
		request.put("email", "swayam.22bce9312@vitapstudent.ac.in");

		ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

		if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
			System.out.println("Failed to generate webhook!");
			return;
		}

		String webhookUrl = (String) response.getBody().get("webhook");
		String accessToken = (String) response.getBody().get("accessToken");

		System.out.println("Webhook URL: " + webhookUrl);
		System.out.println("Access Token: " + accessToken);

		// 2. SQL Query (Question 2)
		String finalQuery =
				"""
						SELECT
						    e1.EMP_ID,
						    e1.FIRST_NAME,
						    e1.LAST_NAME,
						    d.DEPARTMENT_NAME,
						    COUNT(e2.EMP_ID) AS YOUNGER_EMPLOYEES_COUNT
						FROM EMPLOYEE e1
						JOIN DEPARTMENT d
						    ON e1.DEPARTMENT = d.DEPARTMENT_ID
						LEFT JOIN EMPLOYEE e2
						    ON e1.DEPARTMENT = e2.DEPARTMENT
						    AND e2.DOB > e1.DOB
						GROUP BY
						    e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME
						ORDER BY
						    e1.EMP_ID DESC;
				""";


		// 3. Submit the final SQL query to webhook
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization",accessToken);

		Map<String, String> body = new HashMap<>();
		body.put("finalQuery", finalQuery);

		HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

		ResponseEntity<String> submitResponse = restTemplate.postForEntity(webhookUrl, entity, String.class);

		System.out.println("Submission Response: " + submitResponse.getBody());
	}
}
