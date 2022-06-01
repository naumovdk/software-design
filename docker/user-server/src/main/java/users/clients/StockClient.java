package users.clients;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import users.exceptions.StockBadRequest;
import users.exceptions.StockNotFound;
import users.model.Stock;

public class StockClient {
    private final RestTemplate restTemplate;
    private final String url;
    private final LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap();

    public StockClient(String host) {
        url = "http://" + host;
        // Create a new RestTemplate instance
        restTemplate = new RestTemplate();

        // Add the String message converter
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        Map<String, String> map = new HashMap<>();
        map.put("Content-Type", "application/json");
        headers.setAll(map);
    }

    public void buy(String name, int quantity) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url + "/buy?name={name}&quantity={quantity}",
                    String.class, Map.of("name", name, "quantity", quantity));
            System.out.println(response);
        } catch (HttpClientErrorException.NotFound e) {
            throw new StockNotFound();
        }
    }

    public Stock getStock(String name) {
        try {
            var methodUrl = buildUrl("", "name", name);
            ResponseEntity<Stock> response = restTemplate.getForEntity(methodUrl, Stock.class);
            return response.getBody();
        } catch (HttpClientErrorException.NotFound e) {
            throw new StockNotFound();
        } catch (HttpClientErrorException.BadRequest e) {
            throw new StockBadRequest(e.getMessage());
        }
    }

    public void sell(String name, int quantity) {
        var methodUrl = buildUrl("sell", "name", name, "quantity", Integer.toString(quantity));
        ResponseEntity<String> response = restTemplate.getForEntity(methodUrl, String.class);
    }

    public void create(String name, double price, int quantity) {
        var methodUrl = buildUrl("create",
                "name", name,
                "price", Double.toString(price),
                "quantity", Integer.toString(quantity));
        ResponseEntity<String> response = restTemplate.getForEntity(methodUrl, String.class);
    }

    private String buildUrl(String methodName, String... keyValues) {
        var res = new StringBuilder(url).append(methodName).append("?");
        for (int i = 0; i < keyValues.length; i += 2) {
            res.append(keyValues[i]).append('=').append(keyValues[i + 1]).append("&");
        }
        return res.toString();
    }
}
