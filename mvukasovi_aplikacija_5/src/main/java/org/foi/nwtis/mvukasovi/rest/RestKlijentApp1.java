package org.foi.nwtis.mvukasovi.rest;

import java.util.Properties;
import org.foi.nwtis.mvukasovi.slusaci.Slusac;
import org.foi.nwtis.podaci.StatusApp;
import com.google.gson.Gson;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;

/**
 * Klasa RestKlijentApp1.
 */
public class RestKlijentApp1 {

  /**
   * Instancira novu klasu RestKlijentApp1.
   */
  public RestKlijentApp1() {}

  /**
   * Daje status aplikacije 1.
   *
   * @return Status app
   */
  public StatusApp dajStatus() {
    RestKKlijent rc = new RestKKlijent();
    StatusApp jsonStatus = rc.dajStatus();
    rc.close();
    return jsonStatus;
  }

  /**
   * Daje status o komandama INIT, PAUZA ili KRAJ
   *
   * @param komanda Komanda
   * @return Status app
   */
  public StatusApp dajKrajInitPauza(String komanda) {
    RestKKlijent rc = new RestKKlijent();
    StatusApp jsonStatus = rc.dajKrajInitPauza(komanda);
    rc.close();
    return jsonStatus;
  }

  /**
   * Daje status o info komandi.
   *
   * @param komanda Komanda
   * @return Status app
   */
  public StatusApp dajInfo(String komanda) {
    RestKKlijent rc = new RestKKlijent();
    StatusApp jsonStatus = rc.dajInfo(komanda);
    rc.close();
    return jsonStatus;
  }

  /**
   * Klasa RestKKlijent.
   */
  static class RestKKlijent {

    /** Konfiguracija. */
    private final Properties konfiguracija;

    /** Web target. */
    private final WebTarget webTarget;

    /** Klijent. */
    private final Client client;

    /** BASE_URI. */
    private final String BASE_URI;

    /**
     * Instancira novu klasu RestKKlijent.
     */
    public RestKKlijent() {
      ServletContext kontekst = Slusac.dajKontekst();
      Properties konfig = (Properties) kontekst.getAttribute("konfig");
      this.konfiguracija = konfig;
      this.BASE_URI = (String) konfiguracija.getProperty("adresa.wa_2");
      client = ClientBuilder.newClient();
      webTarget = client.target(BASE_URI).path("nadzor");
    }

    /**
     * Daje status aplikacije 1.
     *
     * @return Status app
     * @throws ClientErrorException the client error exception
     */
    public StatusApp dajStatus() throws ClientErrorException {
      System.out.println("Ušao u  klijenta");
      WebTarget resource = webTarget;
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      String response = request.get(String.class);
      if (response.isEmpty()) {
        return null;
      }
      System.out.println("Response: " + response);
      Gson gson = new Gson();
      StatusApp status = gson.fromJson(response, StatusApp.class);

      return status;
    }

    /**
     * Daje status o komandama INIT, PAUZA ili KRAJ
     *
     * @param komanda Komanda
     * @return Status app
     * @throws ClientErrorException the client error exception
     */
    public StatusApp dajKrajInitPauza(String komanda) throws ClientErrorException {
      WebTarget resource = webTarget;
      resource = resource.path(komanda.toUpperCase());
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      String response = request.get(String.class);
      if (response.isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      StatusApp status = gson.fromJson(response, StatusApp.class);

      return status;
    }

    /**
     * Daje status o info komandi.
     *
     * @param komanda Komanda
     * @return Status app
     * @throws ClientErrorException the client error exception
     */
    public StatusApp dajInfo(String komanda) throws ClientErrorException {
      WebTarget resource = webTarget;
      resource = resource.path("INFO");
      resource = resource.path(komanda.toUpperCase());
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      String response = request.get(String.class);
      if (response.isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      StatusApp status = gson.fromJson(response, StatusApp.class);

      return status;
    }

    /**
     * Zatvori.
     */
    public void close() {
      client.close();
    }
  }
}
