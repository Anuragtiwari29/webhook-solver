package com.apoorv.webhookapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.Map;

@Component
public class StartupRunner implements CommandLineRunner {

    private final RestTemplate restTemplate;

    public StartupRunner(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("Calling webhook generation API...");

        String url =
            "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", "Apoorv Singh");
        requestBody.put("regNo", "250850120037");
        requestBody.put("email", "apoorv8799@gmail.com");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request =
                new HttpEntity<>(requestBody, headers);

                ResponseEntity<WebhookResponse> response =
                restTemplate.postForEntity(url, request, WebhookResponse.class);
        

                WebhookResponse body = response.getBody();

                String webhookUrl = body.getWebhook();
                String accessToken = body.getAccessToken();
            
                System.out.println("Webhook URL: " + webhookUrl);
                System.out.println("Access Token: " + accessToken);

                
String finalQuery = "SELECT d.department_name, t.total_salary AS salary, CONCAT(e.first_name, ' ', e.last_name) AS employee_name, TIMESTAMPDIFF(YEAR, e.dob, CURDATE()) AS age FROM ( SELECT emp_id, SUM(amount) AS total_salary FROM payments WHERE DAY(payment_time) <> 1 GROUP BY emp_id ) t JOIN employee e ON e.emp_id = t.emp_id JOIN department d ON d.department_id = e.department JOIN ( SELECT e.department, MAX(t.total_salary) AS max_salary FROM ( SELECT emp_id, SUM(amount) AS total_salary FROM payments WHERE DAY(payment_time) <> 1 GROUP BY emp_id ) t JOIN employee e ON e.emp_id = t.emp_id GROUP BY e.department ) m ON m.department = e.department AND m.max_salary = t.total_salary;";

HttpHeaders submitHeaders = new HttpHeaders();
submitHeaders.setContentType(MediaType.APPLICATION_JSON);
submitHeaders.set("Authorization", accessToken);

Map<String, String> answerBody = new HashMap<>();
answerBody.put("finalQuery", finalQuery);

HttpEntity<Map<String, String>> submitRequest =
        new HttpEntity<>(answerBody, submitHeaders);

ResponseEntity<String> submitResponse =
        restTemplate.postForEntity(
                webhookUrl,
                submitRequest,
                String.class
        );

System.out.println("Submission Response:");
System.out.println(submitResponse.getBody());

    }
}