package restfulapi.cryptocompare.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import restfulapi.cryptocompare.models.CryptoSentiment;
import restfulapi.cryptocompare.repository.Repo;

@Service
public class RepoService {

    @Autowired
    private Repo repo;

    @Autowired
    private ApiService apiSvc;

    public void save(String symbol, String sentiment) {
        CryptoSentiment cryptoSentiment = new CryptoSentiment();
        cryptoSentiment.setSymbol(symbol);
        cryptoSentiment.setSentiment(sentiment);
        repo.save(cryptoSentiment);
    }

    public JsonObject getSentiment(String symbol) {
        CryptoSentiment cryptoSentiment = new CryptoSentiment();
        cryptoSentiment.setSymbol(symbol);
        Optional<String> sentiment = repo.get(symbol);

        if (sentiment.isEmpty()) {
            return apiSvc.getSignal(symbol);
        } else {
            JsonObject responseObj = Json.createObjectBuilder()
                    .add("symbol", symbol)
                    .add("sentiment", sentiment.get())
                    .build();
            return responseObj;
        }
    }
}
