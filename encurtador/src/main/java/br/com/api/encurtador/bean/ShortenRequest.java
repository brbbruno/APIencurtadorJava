package br.com.api.encurtador.bean;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ShortenRequest {
    private String url;
    private String nomeEscolhido;
    private String dataExpiracao;

    @JsonCreator
    public ShortenRequest(@JsonProperty("url") String url,
                          @JsonProperty("nomeEscolhido") String nomeEscolhido,
                          @JsonProperty("dataExpiracao") String dataExpiracao) {
        this.url = url;
        this.nomeEscolhido = nomeEscolhido;
        this.dataExpiracao = dataExpiracao;
    }

    public String getNomeEscolhido() {
        return nomeEscolhido;
    }

    public void setNomeEscolhido(String nomeEscolhido) {
        this.nomeEscolhido = nomeEscolhido;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDataExpiracao() {
        return dataExpiracao;
    }

    public void setDataExpiracao(String dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }
}