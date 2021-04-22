package br.com.api.encurtador.rest;

import br.com.api.encurtador.bean.ShortenRequest;
import br.com.api.encurtador.bean.ShortenResponse;
import br.com.api.encurtador.service.URLConverterService;
import br.com.api.encurtador.util.URLValidator;
import br.com.api.encurtador.util.Utils;
import com.google.gson.Gson;
import org.apache.commons.validator.GenericValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@RestController
public class URLController {
    private static final Logger LOGGER = LoggerFactory.getLogger(URLController.class);
    private final URLConverterService urlConverterService;

    public URLController(URLConverterService urlConverterService) {
        this.urlConverterService = urlConverterService;
    }

    @RequestMapping(value = "/shortener", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity shortenUrl(@RequestBody final ShortenRequest shortenRequest, HttpServletRequest request) throws Exception {
        LOGGER.info("Recebida uma url para encurtar: " + shortenRequest.getUrl());
        String longUrl = shortenRequest.getUrl();
        ShortenResponse shortenResponse = new ShortenResponse();
        if (shortenRequest.getDataExpiracao() != null) {
            if (!GenericValidator.isDate(shortenRequest.getDataExpiracao(), "dd/MM/yyyy", false)) {
                shortenResponse.setErro("Data informada incorreta - favor informar uma data válida");
                return ResponseEntity.badRequest().body(new Gson().toJson(shortenResponse));
            }
        } else {
            LocalDate date = LocalDate.now().plusDays(7);
            shortenRequest.setDataExpiracao(Utils.getDataExpiracao(date));
        }

        if (URLValidator.INSTANCE.validateURL(longUrl)) {
            String localURL = request.getRequestURL().toString();
            if (shortenRequest.getNomeEscolhido() == null) {
                shortenResponse = urlConverterService.shortenURL(localURL, shortenRequest.getUrl(), shortenRequest.getDataExpiracao());
            } else {
                shortenResponse = urlConverterService.shortenURL(localURL, shortenRequest.getUrl(), shortenRequest.getNomeEscolhido(), shortenRequest.getDataExpiracao());
            }
            LOGGER.info("URL encurtada para: " + shortenResponse.urlConvertida);
            return ResponseEntity.ok(new Gson().toJson(shortenResponse));
        }
        throw new Exception("Favor entrar com uma URL Válida!!");
    }

    @RequestMapping(value = "/listshorteners", method = RequestMethod.GET)
    public String listShortenerUrl() throws Exception {
        LOGGER.info("Lista de urls armazenadas !!");

        HashMap<String, String> mapUrls = urlConverterService.listUrls();

        Gson gsonConverter = new Gson();
        List<ShortenResponse> convertidosCollection = Utils.estruturaDadosRetorno(mapUrls);

        return gsonConverter.toJson(convertidosCollection);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public RedirectView redirectUrl(@PathVariable String id) throws Exception {
        LOGGER.debug("Received shortened url to redirect: " + id);
        String redirectUrlString = urlConverterService.getLongURLFromID(id);
        RedirectView redirectView = new RedirectView();
        Utils.extrairUrlHttps(redirectUrlString, redirectView);

        return redirectView;
    }


    @RequestMapping(value = "/tool/{converter}", method = RequestMethod.GET)
    public String getConverter(@PathVariable Long converter) throws Exception {
        LOGGER.debug("Recebido um valor para converção base 62 " + converter);
        return Utils.getConverter(converter);
    }
}