package restfulapi.cryptocompare.controllers;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

import restfulapi.cryptocompare.services.apiService;

@RestController
@RequestMapping(path = "/api")
public class apiController {

    @Autowired
    private apiService apiSvc;

    private final String nameOfApp = "restfulApiPractice";

    @GetMapping("/list")
    public ResponseEntity<String> getAll() {
        Set<String> setOfKeys = apiSvc.getBcList(nameOfApp);
        JsonArrayBuilder arrOfKeys = Json.createArrayBuilder(setOfKeys);
        String jsonStr = Json.createObjectBuilder()
                .add("data", arrOfKeys)
                .build()
                .toString();
        return new ResponseEntity<String>(jsonStr, HttpStatus.OK);
    }

    @GetMapping(path = "/{symbol}/{fiat}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getSymbol(
            @PathVariable(value = "symbol") String symbol,
            @PathVariable(value = "fiat") String fiat) {
        JsonObject response = apiSvc.fetchApiSymbol(symbol, fiat, nameOfApp);
        String responseStr = response.toString();
        return new ResponseEntity<String>(responseStr, HttpStatus.OK);
    }
}
