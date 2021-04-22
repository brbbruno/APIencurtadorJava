package br.com.api.encurtador.util;

import br.com.api.encurtador.bean.ShortenResponse;
import br.com.api.encurtador.service.URLConverterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Utils {
    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);
    private static final String URL_CONVERTIDA_BASE = "http://localhost:8080/";
    private static final String MSG_URL_VALIDA = "Válida";
    private static final String MSG_URL_EXPIRADA = "Expirada";
    private static final int INDICE_URL = 0;
    private static final int INDICE_ID = 1;
    private static URLConverterService urlConverterService;

    public static List<ShortenResponse> estruturaDadosRetorno(HashMap<String, String> mapUrls) {
        List<ShortenResponse> dadosConvertidosCollection = new ArrayList<>();
        mapUrls.forEach((s, v) -> {
            String[] listDadosUrls = s.split(":");
            dadosConvertidosCollection.add(new ShortenResponse(listDadosUrls[INDICE_ID],
                    mapUrls.get(s).split("\\|")[INDICE_URL],
                    URL_CONVERTIDA_BASE + (onlyDigits(listDadosUrls[INDICE_ID]) ? getConverter(Long.parseLong(listDadosUrls[INDICE_ID])) : listDadosUrls[INDICE_ID]),
                    (mapUrls.get(s).split("\\|").length > 1 ? mapUrls.get(s).split("\\|")[INDICE_ID] : null),
                    (mapUrls.get(s).split("\\|").length > 1 ? validaDataExpirada(mapUrls.get(s).split("\\|")[INDICE_ID]) : null)));
        });

        return dadosConvertidosCollection;
    }

    public static String validaDataExpirada(String data) {
        LocalDate localDate = LocalDate.parse(data, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        if (dataValida(data)) {
            return MSG_URL_VALIDA;
        } else {
            return MSG_URL_EXPIRADA;
        }
    }

    public static String getConverter(Long converter) {
        LOGGER.info("Tratamento de valor para base 62 {}", converter);
        return IDConverter.INSTANCE.createUniqueID(converter);
    }

    public static boolean onlyDigits(String parseLong) {
        char[] c = parseLong.toCharArray();
        boolean d = true;

        // verifica se o char não é um dígito
        for (char value : c)
            if (Character.isDigit(value)) {
                return d;
            }
        return false;
    }

    public static String getDataExpiracao(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public static void extrairUrlHttps(String redirectUrlString, RedirectView redirectView) {
        if (!redirectUrlString.contains("https://")) {
            redirectView.setUrl("https://" + redirectUrlString);
        } else {
            redirectView.setUrl(redirectUrlString);
        }
    }

    public static boolean dataValida(String dataExpiracao) {
        return ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.parse(dataExpiracao, DateTimeFormatter.ofPattern("yyyy-MM-dd"))) > 0;
    }
}
