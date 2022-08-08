package restfulapi.cryptocompare.services;

import java.io.Reader;
import java.io.StringReader;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class apiService {

    @Value("${API_KEY}")
    private String apiKey;

    private ResponseEntity<String> fetch(String url) {
        RestTemplate template = new RestTemplate();
        RequestEntity<Void> req = RequestEntity.get(url).build();

        try {
            ResponseEntity<String> res = template.exchange(req, String.class);
            return res;
        } catch (Exception e) {
            System.err.print(e);
            return null;
        }
    }

    public JsonObject fetchApiSymbol(String symbol, String fiat, String app) {
        String apiStr = "https://min-api.cryptocompare.com/data/price";
        String url = UriComponentsBuilder.fromUriString(apiStr)
                .queryParam("fsym", symbol)
                .queryParam("tsyms", fiat)
                .queryParam("api_key", apiKey)
                .queryParam("extraParams", app)
                .toUriString();

        // Fetch API
        ResponseEntity<String> res = fetch(url);

        // Get response:
        String payload = res.getBody();
        System.out.printf(">> payload: %s\n", payload);

        // Read response:
        Reader in = new StringReader(payload);
        JsonReader jr = Json.createReader(in);

        // Manipulate response:
        JsonObject response = jr.readObject();
        Integer price = response.getInt(fiat);
        System.out.printf(">> Server Response: %s\n", response);
        System.out.printf(">> Server Response (price): %d\n", price);

        return response;
    }

    public Set<String> getBcList(String app) {
        String apiStr = "https://min-api.cryptocompare.com/data/blockchain/list";
        String url = UriComponentsBuilder.fromUriString(apiStr)
                .queryParam("api_key", apiKey)
                .queryParam("extraParams", app)
                .toUriString();

        // Fetch API
        ResponseEntity<String> res = fetch(url);

        // Get response:
        String payload = res.getBody();
        Reader in = new StringReader(payload);
        JsonReader jr = Json.createReader(in);

        // Manipulate response:
        JsonObject response = jr.readObject();
        JsonObject data = response.getJsonObject("Data");
        Set<String> setOfKeys = data.keySet();
        return setOfKeys;
    }

}
