package org.foi.nwtis.mvukasovi.pomocnici;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.podaci.Aerodrom;
import com.google.gson.Gson;
import jakarta.annotation.Resource;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;

/**
 * Klasa Pomocnik.
 */
public class Pomocnik {
  /** Baza podataka. */
  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  /**
   * Daj aerodrom. Dohvaća aerodrom REST servisom iz aplikacije 2.
   *
   * @param icao Icao
   * @param adresa Adresa
   * @return Aerodrom
   * @throws ClientErrorException
   */
  public static Aerodrom dajAerodrom(String icao, String adresa) throws ClientErrorException {
    WebTarget webTarget;
    Client client;
    String BASE_URI = adresa;
    client = ClientBuilder.newClient();
    webTarget = client.target(BASE_URI).path("aerodromi");
    WebTarget resource = webTarget;
    resource = resource.path(java.text.MessageFormat.format("{0}", new Object[] {icao}));
    Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
    if (request.get(String.class).isEmpty()) {
      return null;
    }
    Gson gson = new Gson();
    Aerodrom aerodrom = gson.fromJson(request.get(String.class), Aerodrom.class);
    return aerodrom;
  }

  /**
   * Autentikacija korisnika. Provjerava korisničko ime i lozinku korisnika.
   *
   * @param korisnik Korisnik
   * @param lozinka Lozinka
   * @return true, ako korisnik postoji, inače false
   */
  public boolean autentikacijaKorisnika(String korisnik, String lozinka) {
    PreparedStatement pstmt = null;
    boolean autentikacija = false;
    String query = "SELECT * FROM KORISNICI WHERE KOR_IME = ? AND LOZINKA = ?";
    try (var con = ds.getConnection()) {
      pstmt = con.prepareStatement(query);
      pstmt.setString(1, korisnik);
      pstmt.setString(2, lozinka);
      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        autentikacija = true;
      }
      rs.close();
      pstmt.close();
      con.close();
    } catch (Exception e) {
      Logger.getGlobal().log(Level.INFO, e.toString());
    } finally {
      try {
        if (pstmt != null && pstmt.isClosed()) {
          pstmt.close();
        }
      } catch (SQLException e) {
        Logger.getGlobal().log(Level.INFO, e.toString());
      }
    }
    return autentikacija;
  }
}
