package org.foi.nwtis.mvukasovi.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.foi.nwtis.mvukasovi.slusaci.Slusac;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Udaljenost;
import org.foi.nwtis.podaci.UdaljenostAerodrom;
import org.foi.nwtis.podaci.UdaljenostiIzmeduAerodroma;
import com.google.gson.Gson;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;

/**
 * Klasa RestKlijentAerodroma.
 */
public class RestKlijentAerodroma {

  /**
   * Instancira novu klasu RestKlijentAerodrom.
   */
  public RestKlijentAerodroma() {}

  /**
   * Daj sve aerodrome. Uzima početni broj koji dohvaćamo za straničenje i broj podataka koji želimo
   * uzeti.
   *
   * @param odBroja the od broja
   * @param broj the broj
   * @param traziNaziv the trazi naziv
   * @param traziDrzavu the trazi drzavu
   * @return Lista aerodroma
   */
  public List<Aerodrom> dajSveAerodrome(int odBroja, int broj, String traziNaziv,
      String traziDrzavu) {
    RestKKlijent rc = new RestKKlijent();
    Aerodrom[] jsonAerodromi = rc.dajSveAerodrome(odBroja, broj, traziNaziv, traziDrzavu);
    List<Aerodrom> aerodromi;
    if (jsonAerodromi == null) {
      aerodromi = new ArrayList<>();
    } else {
      aerodromi = Arrays.asList(jsonAerodromi);
    }
    rc.close();
    return aerodromi;
  }

  /**
   * Daj aerodrom. Vraća aerodrom s određenim icao.
   *
   * @param icao ICAO
   * @return Aerodrom
   */
  public Aerodrom dajAerodrom(String icao) {
    RestKKlijent rc = new RestKKlijent();
    Aerodrom aerodrom = rc.dajAerodrom(icao);
    rc.close();
    return aerodrom;
  }

  /**
   * Daj udaljenosti između dva aerodoma sa zadanim polazišnim i odredišnim icao.
   *
   * @param icaoOd ICAO id
   * @param icaoDo ICAO do
   * @return Lista udaljenosti
   */
  public List<Udaljenost> dajUdaljenostiIzmeduDvaAerodoma(String icaoOd, String icaoDo) {
    RestKKlijent rc = new RestKKlijent();
    Udaljenost[] jsonUdaljenost = rc.dajUdaljenostiIzmeduDvaAerodoma(icaoOd, icaoDo);
    List<Udaljenost> udaljenosti;
    if (jsonUdaljenost == null) {
      udaljenosti = new ArrayList<>();
    } else {
      udaljenosti = Arrays.asList(jsonUdaljenost);
    }
    rc.close();
    return udaljenosti;
  }

  /**
   * Daj udaljenosti između svih aerodroma s određenim aerodromom.
   *
   * @param icaoOd Icao od
   * @param odBroja Od broja
   * @param broj Broj
   * @return Lista UdaljenostAerodrom
   */
  public List<UdaljenostAerodrom> dajUdaljenostiIzmeduSvihAerodoma(String icaoOd, int odBroja,
      int broj) {
    RestKKlijent rc = new RestKKlijent();
    UdaljenostAerodrom[] jsonUdaljenost =
        rc.dajUdaljenostiIzmeduSvihAerodoma(icaoOd, odBroja, broj);
    List<UdaljenostAerodrom> udaljenosti;
    if (jsonUdaljenost == null) {
      udaljenosti = new ArrayList<>();
    } else {
      udaljenosti = Arrays.asList(jsonUdaljenost);
    }
    rc.close();
    return udaljenosti;
  }

  /**
   * Daje udaljenost između polazišnog i odredišnog aerodroma.
   *
   * @param icaoOd Icao od
   * @param icaoDo Icao do
   * @return String - udaljenost
   */
  public String dajIzracunIzmeduDvaAerodroma(String icaoOd, String icaoDo) {
    RestKKlijent rc = new RestKKlijent();
    String udaljenost = rc.dajIzracunIzmeduDvaAerodroma(icaoOd, icaoDo);
    rc.close();
    return udaljenost;
  }

  /**
   * Daje aerodrome s udaljenostima do odredišnog aerodroma kojima je udaljenost manja od
   * udaljenosti polazišnog i odredišnog aerodroma.
   *
   * @param icaoOd Icao od
   * @param icaoDo Icao do
   * @return List<UdaljenostiIzmeduAerodroma>
   */
  public List<UdaljenostiIzmeduAerodroma> dajUdaljenost1(String icaoOd, String icaoDo) {
    RestKKlijent rc = new RestKKlijent();
    UdaljenostiIzmeduAerodroma[] jsonUdaljenost = rc.dajUdaljenost1(icaoOd, icaoDo);
    List<UdaljenostiIzmeduAerodroma> udaljenosti;
    if (jsonUdaljenost == null) {
      udaljenosti = new ArrayList<>();
    } else {
      udaljenosti = Arrays.asList(jsonUdaljenost);
    }
    rc.close();
    return udaljenosti;
  }

  /**
   * Daje aerodrome s udaljenostima do odredišnog aerodroma kojima je udaljenost manja od parametra
   * km, koji se nalaze u određenoj državi
   *
   * @param icaoOd Icao od
   * @param drzava Drzava
   * @param km Km
   * @return List<UdaljenostiIzmeduAerodroma>
   */
  public List<UdaljenostiIzmeduAerodroma> dajUdaljenost2(String icaoOd, String drzava, String km) {
    RestKKlijent rc = new RestKKlijent();
    UdaljenostiIzmeduAerodroma[] jsonUdaljenost = rc.dajUdaljenost2(icaoOd, drzava, km);
    List<UdaljenostiIzmeduAerodroma> udaljenosti;
    if (jsonUdaljenost == null) {
      udaljenosti = new ArrayList<>();
    } else {
      udaljenosti = Arrays.asList(jsonUdaljenost);
    }
    rc.close();
    return udaljenosti;
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
      webTarget = client.target(BASE_URI).path("aerodromi");
    }

    /**
     * Daj sve aerodrome. Uzima početni broj koji dohvaćamo za straničenje i broj podataka koji
     * želimo uzeti. Moguće filtrirati prema nazivu ili državi
     *
     * @param odBroja the od broja
     * @param broj the broj
     * @param traziNaziv Trazi naziv
     * @param traziDrzavu Trazi državu
     * @return Polje aerodroma
     * @throws ClientErrorException the client error exception
     */
    public Aerodrom[] dajSveAerodrome(int odBroja, int broj, String traziNaziv, String traziDrzavu)
        throws ClientErrorException {
      WebTarget resource = webTarget;
      if (traziNaziv != null)
        resource = resource.queryParam("traziNaziv", new Object[] {traziNaziv});
      if (traziDrzavu != null)
        resource = resource.queryParam("traziDrzavu", new Object[] {traziDrzavu});
      resource = resource.queryParam("odBroja", new Object[] {odBroja});
      resource = resource.queryParam("broj", new Object[] {broj});
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      String response = request.get(String.class);
      if (response.isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      Aerodrom[] aerodromi = gson.fromJson(response, Aerodrom[].class);

      return aerodromi;
    }

    /**
     * Daj aerodrom. Vraća aerodrom s određenim icao.
     *
     * @param icao the icao
     * @return Aerodrom
     * @throws ClientErrorException the client error exception
     */
    public Aerodrom dajAerodrom(String icao) throws ClientErrorException {
      WebTarget resource = webTarget;
      resource = resource.path(java.text.MessageFormat.format("{0}", new Object[] {icao}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      String response = request.get(String.class);
      if (response.isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      Aerodrom aerodrom = gson.fromJson(response, Aerodrom.class);
      return aerodrom;
    }

    /**
     * Daj udaljenosti između dva aerodoma sa zadanim polazišnim i odredišnim icao.
     *
     * @param icaoOd ICAO id
     * @param icaoDo ICAO do
     * @return Polje udaljenosti
     * @throws ClientErrorException the client error exception
     */
    public Udaljenost[] dajUdaljenostiIzmeduDvaAerodoma(String icaoOd, String icaoDo)
        throws ClientErrorException {
      WebTarget resource = webTarget;
      resource =
          resource.path(java.text.MessageFormat.format("{0}/{1}", new Object[] {icaoOd, icaoDo}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      String response = request.get(String.class);
      if (response.isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      Udaljenost[] udaljenosti = gson.fromJson(response, Udaljenost[].class);
      return udaljenosti;
    }

    /**
     * Daj udaljenosti između svih aerodroma s određenim aerodromom.
     *
     * @param icao ICAO
     * @param odBroja Od broja
     * @param broj broj
     * @return Polje UdaljenostAerodrom
     */
    public UdaljenostAerodrom[] dajUdaljenostiIzmeduSvihAerodoma(String icao, int odBroja,
        int broj) {
      WebTarget resource = webTarget;
      resource = resource.path(java.text.MessageFormat.format("{0}", new Object[] {icao}));
      resource = resource.path("udaljenosti");
      resource = resource.queryParam("odBroja", new Object[] {odBroja});
      resource = resource.queryParam("broj", new Object[] {broj});
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      if (request.get(String.class).isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      UdaljenostAerodrom[] udaljenosti =
          gson.fromJson(request.get(String.class), UdaljenostAerodrom[].class);
      return udaljenosti;
    }

    /**
     * Daje udaljenost između polazišnog i odredišnog aerodroma.
     *
     * @param icaoOd Icao od
     * @param icaoDo Icao do
     * @return String - udaljenost
     * @throws ClientErrorException the client error exception
     */
    public String dajIzracunIzmeduDvaAerodroma(String icaoOd, String icaoDo)
        throws ClientErrorException {
      WebTarget resource = webTarget;
      resource = resource
          .path(java.text.MessageFormat.format("{0}/izracunaj/{1}", new Object[] {icaoOd, icaoDo}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      String response = request.get(String.class);
      if (response.isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      String udaljenost = gson.fromJson(response, String.class);
      return udaljenost;
    }

    /**
     * Daje aerodrome s udaljenostima do odredišnog aerodroma kojima je udaljenost manja od
     * udaljenosti polazišnog i odredišnog aerodroma.
     *
     * @param icaoOd Icao od
     * @param icaoDo Icao do
     * @return the udaljenosti izmedu aerodroma[]
     */
    public UdaljenostiIzmeduAerodroma[] dajUdaljenost1(String icaoOd, String icaoDo) {
      WebTarget resource = webTarget;
      resource = resource.path(
          java.text.MessageFormat.format("{0}/udaljenost1/{1}", new Object[] {icaoOd, icaoDo}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      String response = request.get(String.class);
      if (response.isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      UdaljenostiIzmeduAerodroma[] udaljenosti =
          gson.fromJson(response, UdaljenostiIzmeduAerodroma[].class);
      return udaljenosti;
    }

    /**
     * Daje aerodrome s udaljenostima do odredišnog aerodroma kojima je udaljenost manja od
     * parametra km, koji se nalaze u određenoj državi
     *
     * @param icaoOd Icao od
     * @param drzava Drzava
     * @param km Km
     * @return the udaljenosti izmedu aerodroma[]
     */
    public UdaljenostiIzmeduAerodroma[] dajUdaljenost2(String icaoOd, String drzava, String km) {
      WebTarget resource = webTarget;
      resource =
          resource.path(java.text.MessageFormat.format("{0}/udaljenost2", new Object[] {icaoOd,}));
      resource = resource.queryParam("drzava", new Object[] {drzava});
      resource = resource.queryParam("km", new Object[] {km});
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      String response = request.get(String.class);
      if (response.isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      UdaljenostiIzmeduAerodroma[] udaljenosti =
          gson.fromJson(response, UdaljenostiIzmeduAerodroma[].class);
      return udaljenosti;
    }

    /**
     * Zatvori.
     */
    public void close() {
      client.close();
    }
  }
}
