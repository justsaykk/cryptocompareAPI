package restfulapi.cryptocompare.controllers;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
// import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

import restfulapi.cryptocompare.services.apiService;

@Controller
@RequestMapping(path = "/api")
public class apiController {

    @Autowired
    private apiService apiSvc;

    private final String app = "restfulApiPractice";

    @GetMapping(path = "/list")
    public String getAll(Model model) {
        Set<String> setOfKeys = apiSvc.getBcList(app);
        JsonArrayBuilder arrOfKeys = Json.createArrayBuilder(setOfKeys);
        String jsonStr = Json.createObjectBuilder()
                .add("data", arrOfKeys)
                .build()
                .toString();
        model.addAttribute("list", jsonStr);
        return "getAll";
    }

    @GetMapping(path = "/{symbol}/{fiat}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> getSymbol(
            @PathVariable(value = "symbol") String symbol,
            @PathVariable(value = "fiat") String fiat) {
        JsonObject response = apiSvc.fetchApiSymbol(symbol, fiat, app);
        String responseStr = response.toString();
        return new ResponseEntity<String>(responseStr, HttpStatus.OK);
    }

    @GetMapping(path = "/signal", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> getSignal(
            @RequestParam(name = "symbol") String symbol) {

        String responseObj = apiSvc.getSignal(symbol, app).toString();
        return new ResponseEntity<String>(responseObj, HttpStatus.OK);
    }
}
