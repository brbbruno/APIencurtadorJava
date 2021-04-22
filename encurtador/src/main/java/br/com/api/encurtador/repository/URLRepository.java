package br.com.api.encurtador.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

import java.time.LocalDate;
import java.util.*;

@Repository
public class URLRepository {
    private final Jedis jedis;
    private final String idKey;
    private final String urlKey;
    private static final Logger LOGGER = LoggerFactory.getLogger(URLRepository.class);

    @Value("${redis.client.port}")
    private int redisPort;

    public URLRepository() {
        this.jedis = new Jedis();
        this.idKey = "id";
        this.urlKey = "url:";
    }

    public URLRepository(Jedis jedis, String idKey, String urlKey) {
        this.jedis = jedis;
        this.idKey = idKey;
        this.urlKey = urlKey;
    }

    public Long incrementID() {
        Long id = jedis.incr(idKey);
        LOGGER.info("Incrementing ID: {}", id - 1);
        return id - 1;
    }

    public HashMap<String, String> listUrls() {
        return (HashMap<String, String>) jedis.hgetAll(urlKey);
    }

    public void saveUrl(String key, String longUrl, LocalDate data) {
        LOGGER.info("Saving: {} at {}", longUrl, key);
        jedis.hset(urlKey, key, longUrl + "|" + data.toString());
    }

    public String getUrl(Long id) throws Exception {
        LOGGER.info("Retrieving at {}", id);
        String url = jedis.hget(urlKey, "url:" + id);
        if (url == null) {
            return null;
        }
        return jedis.hget(urlKey, "url:" + id).split("\\|")[0];
    }
    public String getData(Long id) throws Exception {
        LOGGER.info("Retrieving at {}", id);
        String url = jedis.hget(urlKey, "url:" + id);
        if (url == null) {
            return null;
        }
        return jedis.hget(urlKey, "url:" + id).split("\\|")[1];
    }

    public String getUrl(String id) throws Exception {
        LOGGER.info("Retrieving at {}", id);
        String url = jedis.hget(urlKey, "url:" + id);
        if (url == null) {
            return null;
        }
        return jedis.hget(urlKey, "url:" + id).split("\\|")[0];
    }

    public String getData(String id) throws Exception {
        LOGGER.info("Retrieving at {}", id);
        String url = jedis.hget(urlKey, "url:" + id);
        if (url == null) {
            return null;
        }
        return jedis.hget(urlKey, "url:" + id).split("\\|")[1];
    }

    @Bean
    public void clientConfiguration() {
        jedis.getClient().setPort(redisPort);
    }

    public boolean alreadyExists(String nomeEscolhido) {
        return jedis.hget(urlKey, "url:" + nomeEscolhido) != null;
    }
}