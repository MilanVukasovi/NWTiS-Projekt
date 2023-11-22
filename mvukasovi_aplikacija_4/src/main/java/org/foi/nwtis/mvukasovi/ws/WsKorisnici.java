package org.foi.nwtis.mvukasovi.ws;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.podaci.Korisnik;
import jakarta.annotation.Resource;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.ws.rs.QueryParam;
import jakarta.xml.ws.WebFault;
import jakarta.xml.ws.WebServiceException;

/**
 * Klasa WsKorisnici.
 */
@WebService(serviceName = "korisnici")
public class WsKorisnici {
  /** Baza podataka. */
  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  /**
   * Vraća korisnike iz baze.
   *
   * @param korisnik Korisnik
   * @param lozinka Lozinka
   * @param traziImeKorisnika Trazi ime korisnika
   * @param traziPrezimeKorisnika Trazi prezime korisnika
   * @return List<Korisnik>
   */
  @WebMethod
  public List<Korisnik> dajKorisnike(@QueryParam("korisnik") String korisnik,
      @QueryParam("lozinka") String lozinka,
      @QueryParam("traziImeKorisnika") String traziImeKorisnika,
      @QueryParam("traziPrezimeKorisnika") String traziPrezimeKorisnika) {
    ArrayList<Korisnik> korisnici = new ArrayList<Korisnik>();
    if (!this.autentikacijaKorisnika(korisnik, lozinka)) {
      throw new UnauthorizedException("Pogrešno korisničko ime ili lozinka");
    }
    PreparedStatement pstmt = null;
    String query = "SELECT KOR_IME, IME, PREZIME FROM KORISNICI";
    query += (traziImeKorisnika != null || traziPrezimeKorisnika != null) ? " WHERE" : "";
    query += (traziImeKorisnika != null) ? " IME LIKE ?" : "";
    query += (traziImeKorisnika != null && traziPrezimeKorisnika != null) ? " AND" : "";
    query += (traziPrezimeKorisnika != null) ? " PREZIME LIKE ?" : "";
    try (var con = ds.getConnection()) {
      pstmt = con.prepareStatement(query);
      int index = 1;
      if (traziImeKorisnika != null) {
        pstmt.setString(index, "%" + traziImeKorisnika + "%");
        index++;
      }
      if (traziPrezimeKorisnika != null) {
        pstmt.setString(index, "%" + traziPrezimeKorisnika + "%");
      }
      ResultSet rs = pstmt.executeQuery();

      while (rs.next()) {
        Korisnik korisnikIzBaze =
            new Korisnik(rs.getString("KOR_IME"), "", rs.getString("IME"), rs.getString("PREZIME"));
        korisnici.add(korisnikIzBaze);
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
    return korisnici;
  }

  /**
   * Vraća korisnika po njegovom korisničkom imenu
   *
   * @param korisnik Korisnik
   * @param lozinka Lozinka
   * @param traziKorisnika Trazi korisnika
   * @return Korisnik
   */
  @WebMethod
  public Korisnik dajKorisnika(@QueryParam("korisnik") String korisnik,
      @QueryParam("lozinka") String lozinka, @QueryParam("traziKorisnika") String traziKorisnika) {
    Korisnik korisnikIzBaze = null;
    if (!this.autentikacijaKorisnika(korisnik, lozinka)) {
      throw new UnauthorizedException("Pogrešno korisničko ime ili lozinka");
    }
    PreparedStatement pstmt = null;
    String query = "SELECT KOR_IME, IME, PREZIME FROM KORISNICI WHERE KOR_IME = ?";
    try (var con = ds.getConnection()) {
      pstmt = con.prepareStatement(query);
      pstmt.setString(1, traziKorisnika);
      ResultSet rs = pstmt.executeQuery();

      while (rs.next()) {
        korisnikIzBaze =
            new Korisnik(rs.getString("KOR_IME"), "", rs.getString("IME"), rs.getString("PREZIME"));
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
    return korisnikIzBaze;
  }

  /**
   * Dodaj korisnika u bazu.
   *
   * @param korisnik Korisnik
   * @return true ako je dodavanje uspješno, inače false
   */
  @WebMethod
  public boolean dodajKorisnika(Korisnik korisnik) {
    boolean dodan = false;
    PreparedStatement pstmt = null;
    String query = "INSERT INTO KORISNICI VALUES (?,?,?,?)";
    try (var con = ds.getConnection()) {
      pstmt = con.prepareStatement(query);
      pstmt.setString(1, korisnik.getKorIme());
      pstmt.setString(2, korisnik.getLozinka());
      pstmt.setString(3, korisnik.getIme());
      pstmt.setString(4, korisnik.getPrezime());
      int redak = pstmt.executeUpdate();
      dodan = (redak != 0);
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
   * Autentikacija korisnika. Provjerava korisničko ime i lozinku korisnika.
   *
   * @param korisnik Korisnik
   * @param lozinka Lozinka
   * @return true, ako korisnik postoji, inače false
   */
  @WebMethod
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
