package br.com.api.encurtador.bean;

public class ShortenResponse {

    public String id;
    public String urlbase;
    public String urlConvertida;
    public String dataExpiracao;
    public String urlValida;
    public String erro;

    public ShortenResponse(String id, String urlbase, String urlConvertida, String dataExpiracao, String urlValida) {
        this.id = id;
        this.urlbase = urlbase;
        this.urlConvertida = urlConvertida;
        this.dataExpiracao = dataExpiracao;
        this.urlValida = urlValida;
    }

    public ShortenResponse(String id, String urlbase, String urlConvertida, String dataExpiracao) {
        this.id = id;
        this.urlbase = urlbase;
        this.urlConvertida = urlConvertida;
        this.dataExpiracao = dataExpiracao;
    }

    public ShortenResponse(String id, String urlbase, String urlConvertida) {
        this.id = id;
        this.urlbase = urlbase;
        this.urlConvertida = urlConvertida;
    }

    public ShortenResponse() {
    }

    public String getDataExpiracao() {
        return dataExpiracao;
    }

    public void setDataExpiracao(String dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }

    public String getErro() {
        return erro;
    }

    public void setErro(String erro) {
        this.erro = erro;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrlbase() {
        return urlbase;
    }

    public void setUrlbase(String urlbase) {
        this.urlbase = urlbase;
    }

    public String getUrlConvertida() {
        return urlConvertida;
    }

    public void setUrlConvertida(String urlConvertida) {
        this.urlConvertida = urlConvertida;
    }

    public String getUrlValida() {
        return urlValida;
    }

    public void setUrlValida(String urlValida) {
        this.urlValida = urlValida;
    }
}
