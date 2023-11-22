package org.foi.nwtis.mvukasovi.ws;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.AerodromStatusPreuzimanja;
import org.foi.nwtis.podaci.Lokacija;
import jakarta.annotation.Resource;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.ws.rs.QueryParam;
import jakarta.xml.ws.WebFault;
import jakarta.xml.ws.WebServiceException;

/**
 * Klasa WsAerodromi.
 */
@WebService(serviceName = "aerodromi")
public class WsAerodromi {

  /** Baza podataka. */
  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  /**
   * Vraća aerodrome i status aktivnosti za preuzimanje njihovih podataka o letovima iz baze.
   *
   * @param korisnik Korisnik
   * @param lozinka Lozinka
   * @return List<AerodromStatusPreuzimanja>
   */
  @WebMethod
  public List<AerodromStatusPreuzimanja> dajAerodromeZaLetove(
      @QueryParam("korisnik") String korisnik, @QueryParam("lozinka") String lozinka) {
    if (!this.autentikacijaKorisnika(korisnik, lozinka)) {
      throw new UnauthorizedException("Pogrešno korisničko ime ili lozinka");
    }
    ArrayList<AerodromStatusPreuzimanja> aerodromi = new ArrayList<AerodromStatusPreuzimanja>();
    PreparedStatement pstmt = null;
    String query =
        "SELECT AIRPORTS.ICAO, AIRPORTS.NAME, AIRPORTS.ISO_COUNTRY, AIRPORTS.COORDINATES, AERODROMI_LETOVI.AKTIVAN FROM AIRPORTS JOIN AERODROMI_LETOVI ON AERODROMI_LETOVI.icao = AIRPORTS.icao;";
    try (var con = ds.getConnection()) {
      pstmt = con.prepareStatement(query);
      ResultSet rs = pstmt.executeQuery();

      while (rs.next()) {
        Aerodrom aerodrom = obradiAerodrom(rs);
        aerodromi.add(new AerodromStatusPreuzimanja(aerodrom, rs.getBoolean("AKTIVAN")));
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
    return aerodromi;
  }

  /**
   * Dodaj aerodrom u tablicu AERODROMI_LETOVI i postavlja mu aktivnost na "true"
   *
   * @param korisnik Korisnik
   * @param lozinka Lozinka
   * @param icao Icao
   * @return true ako je dodavanje uspješno, inače false
   */
  @WebMethod
  public boolean dodajAerodromZaLetove(@QueryParam("korisnik") String korisnik,
      @QueryParam("lozinka") String lozinka, @QueryParam("icao") String icao) {
    if (!this.autentikacijaKorisnika(korisnik, lozinka)) {
      throw new UnauthorizedException("Pogrešno korisničko ime ili lozinka");
    }
    boolean dodan = false;
    PreparedStatement pstmt = null;
    String query = "INSERT INTO AERODROMI_LETOVI VALUES (?, ?)";
    try (var con = ds.getConnection()) {
      pstmt = con.prepareStatement(query);
      pstmt.setString(1, icao);
      pstmt.setBoolean(2, true);
      int rowsAffected = pstmt.executeUpdate();
      dodan = (rowsAffected != 0);

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
    return dodan;
  }

  /**
   * Pauziraj aerodrom za preuzimanje njegovih podataka o letovima u baze.
   *
   * @param korisnik Korisnik
   * @param lozinka Lozinka
   * @param icao Icao
   * @return true ako je uspješno pauzirano, inače false
   */
  @WebMethod
  public boolean pauzirajAerodromZaLetove(@QueryParam("korisnik") String korisnik,
      @QueryParam("lozinka") String lozinka, @QueryParam("icao") String icao) {
    if (!this.autentikacijaKorisnika(korisnik, lozinka)) {
      throw new UnauthorizedException("Pogrešno korisničko ime ili lozinka");
    }
    boolean pauziran = false;
    PreparedStatement pstmt = null;
    String query = "UPDATE AERODROMI_LETOVI SET AKTIVAN = ? WHERE icao = ?";
    try (var con = ds.getConnection()) {
      pstmt = con.prepareStatement(query);
      pstmt.setBoolean(1, false);
      pstmt.setString(2, icao);
      int rowsAffected = pstmt.executeUpdate();
      pauziran = (rowsAffected != 0);

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
    return pauziran;
  }

  /**
   * Aktiviraj aerodrom za preuzimanje njegovih podataka o letovima u baze.
   *
   * @param korisnik Korisnik
   * @param lozinka Lozinka
   * @param icao Icao
   * @return true ako je uspješno pauzirano, inače false
   */
  @WebMethod
  public boolean aktivirajAerodromZaLetove(@QueryParam("korisnik") String korisnik,
      @QueryParam("lozinka") String lozinka, @QueryParam("icao") String icao) {
    if (!this.autentikacijaKorisnika(korisnik, lozinka)) {
      throw new UnauthorizedException("Pogrešno korisničko ime ili lozinka");
    }
    boolean aktiviran = false;
    PreparedStatement pstmt = null;
    String query = "UPDATE AERODROMI_LETOVI SET AKTIVAN = ? WHERE icao = ?";
    try (var con = ds.getConnection()) {
      pstmt = con.prepareStatement(query);
      pstmt.setBoolean(1, true);
      pstmt.setString(2, icao);
      int rowsAffected = pstmt.executeUpdate();
      aktiviran = (rowsAffected != 0);

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

    return aktiviran;
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
    Aerodrom aerodrom = new Aerodrom(icao, naziv, drzava, lokacija);
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
    } finally {
      try {
        if (pstmt != null && pstmt.isClosed()) {
          pstmt.close();
        }
      } catch (SQLException e) {
      }
    }
    return autentikacija;
  }

  /**
   * The Class UnauthorizedException.
   */
  @WebFault(name = "UnauthorizedFault")
  class UnauthorizedException extends WebServiceException {

    /**
     * Instantiates a new unauthorized exception.
     *
     * @param message the message
     */
    public UnauthorizedException(String message) {
      super(message);
    }
  }
}
