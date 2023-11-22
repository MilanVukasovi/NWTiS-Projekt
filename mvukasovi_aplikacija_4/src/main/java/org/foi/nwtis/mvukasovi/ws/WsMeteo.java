package org.foi.nwtis.mvukasovi.ws;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mvukasovi.pomocnici.Pomocnik;
import org.foi.nwtis.mvukasovi.slusaci.Slusac;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Lokacija;
import org.foi.nwtis.rest.klijenti.NwtisRestIznimka;
import org.foi.nwtis.rest.klijenti.OWMKlijent;
import org.foi.nwtis.rest.podaci.MeteoPodaci;
import jakarta.annotation.Resource;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.core.Context;

/**
 * Klasa WsMeteo.
 */
@WebService(serviceName = "meteo")
public class WsMeteo {

  /** Baza podataka. */
  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  /** Kontekst. */
  @Context
  ServletContext kontekst;

  /**
   * Daj meteo. Vraća meteo podatke za određeni aerodrom.
   *
   * @param icao ICAO
   * @return MeteoPodaci
   */
  @WebMethod
  public MeteoPodaci dajMeteo(@WebParam String icao) {
    Aerodrom aerodrom = null;
    Properties konfiguracija = ucitajKonfiguraciju();
    String apiKljuc = konfiguracija.getProperty("OpenWeatherMap.apikey");
    String adresa = (String) konfiguracija.getProperty("adresa.wa_2");
    aerodrom = Pomocnik.dajAerodrom(icao, adresa);
    MeteoPodaci meteoPodaci = null;
    OWMKlijent owmKlijent = new OWMKlijent(apiKljuc);
    try {
      meteoPodaci = owmKlijent.getRealTimeWeather(aerodrom.getLokacija().getLatitude(),
          aerodrom.getLokacija().getLongitude());
    } catch (NwtisRestIznimka e) {
      Logger.getGlobal().log(Level.INFO, e.toString());
    }
    return meteoPodaci;
  }


  /**
   * Obradi aerodrom. Obrađuje resultset i vraća podatke o aerodromu.
   *
   * @param rs ResultSet
   * @return Aerodrom
   * @throws SQLException SQL iznimka
   */
  private Aerodrom obradiAerodrom(ResultSet rs) throws SQLException {
    String icao = rs.getString("ICAO");
    String naziv = rs.getString("NAME");
    String drzava = rs.getString("ISO_COUNTRY");
    String lok = rs.getString("COORDINATES");
    String[] koordinate = lok.split(", ");
    Lokacija lokacija = new Lokacija(koordinate[0], koordinate[1]);
    var aerodrom = new Aerodrom(icao, naziv, drzava, lokacija);
    return aerodrom;
  }

  /**
   * Učitaj konfiguraciju.
   *
   * @return Properties
   */
  private Properties ucitajKonfiguraciju() {
    Properties konfiguracija = new Properties();
    ServletContext kontekst = Slusac.dajKontekst();
    Properties konfig = (Properties) kontekst.getAttribute("konfig");
    konfiguracija = konfig;
    return konfiguracija;
  }
}
