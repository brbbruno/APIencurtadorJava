package br.com.api.encurtador.service;


import br.com.api.encurtador.bean.ShortenResponse;
import br.com.api.encurtador.repository.URLRepository;
import br.com.api.encurtador.util.IDConverter;
import br.com.api.encurtador.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

@Service
public class URLConverterService {
    private static final Logger LOGGER = LoggerFactory.getLogger(URLConverterService.class);
    private final URLRepository urlRepository;
    private static String MSG_URL_NAO_ENCONTRADA = "www.urlnaoencontrada.com.br";

    @Autowired
    public URLConverterService(URLRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public ShortenResponse shortenURL(String localURL, String longUrl, String dataExpiracao) {
        LOGGER.info("Encurtando {}", longUrl);

        ShortenResponse shortenResponse = new ShortenResponse();
        Long id = urlRepository.incrementID();
        String uniqueID = IDConverter.INSTANCE.createUniqueID(id);
        LocalDate date = LocalDate.parse(dataExpiracao, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        urlRepository.saveUrl("url:" + id, longUrl, date);
        String baseString = formatLocalURLFromShortener(localURL);
        shortenResponse.setId(uniqueID);
        shortenResponse.setUrlbase(longUrl);
        shortenResponse.setUrlConvertida(baseString + uniqueID);
        return shortenResponse;
    }

    public ShortenResponse shortenURL(String localURL, String longUrl, String nomeEscolhido, String dataExpiracao) {
        LOGGER.info("Encurtando {}", longUrl);
        ShortenResponse shortenResponse = new ShortenResponse();
        LocalDate date = LocalDate.parse(dataExpiracao, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        if (!urlRepository.alreadyExists(nomeEscolhido)) {
            urlRepository.saveUrl("url:" + nomeEscolhido, longUrl, date);
        }
        String baseString = formatLocalURLFromShortener(localURL);
        shortenResponse.setId(nomeEscolhido);
        shortenResponse.setUrlbase(longUrl);
        shortenResponse.setUrlConvertida(baseString + nomeEscolhido);
        return shortenResponse;
    }

    public String getLongURLFromID(String uniqueID) throws Exception {
        Long dictionaryKey = IDConverter.INSTANCE.getDictionaryKeyFromUniqueID(uniqueID);
        String longUrl = urlRepository.getUrl(dictionaryKey);
        String dataExpiracao = urlRepository.getData(dictionaryKey);
        if (longUrl == null) {
            longUrl = urlRepository.getUrl(uniqueID);
            dataExpiracao = urlRepository.getData(uniqueID);
        }
        LOGGER.info("Converting shortened URL back to {}", longUrl);
        return verificaValidadeUrl(longUrl, dataExpiracao);
    }

    private String verificaValidadeUrl(String longUrl, String dataExpiracao) {
        if (longUrl != null && Utils.dataValida(dataExpiracao)) {
            return longUrl;
        } else {
            return MSG_URL_NAO_ENCONTRADA;
        }

    }

    public HashMap<String, String> listUrls() throws Exception {
        LOGGER.info("Listing urls");
        return urlRepository.listUrls();
    }

    private String formatLocalURLFromShortener(String localURL) {
        String[] addressComponents = localURL.split("/");
        // remove the endpoint (last index)
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < addressComponents.length - 1; ++i) {
            sb.append(addressComponents[i]);
        }
        sb.append('/');
        return sb.toString();
    }

}