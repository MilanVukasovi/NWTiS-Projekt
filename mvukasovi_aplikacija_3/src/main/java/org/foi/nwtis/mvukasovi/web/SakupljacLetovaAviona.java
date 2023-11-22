package org.foi.nwtis.mvukasovi.web;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.foi.nwtis.mvukasovi.slusaci.Slusac;
import org.foi.nwtis.mvukasovi.zrna.JmsPosiljatelj;
import org.foi.nwtis.rest.klijenti.NwtisRestIznimka;
import org.foi.nwtis.rest.klijenti.OSKlijentBP;
import org.foi.nwtis.rest.podaci.LetAviona;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.core.Context;

/*** Klasa SakupljacLetovaAviona. */

public class SakupljacLetovaAviona extends Thread {

  /** Kontekst. */
  @Context
  ServletContext kontekst;

  /** Baza podataka */
  DataSource ds = null;

  /** Instanca JmsPosiljatelj. */
  JmsPosiljatelj jmsPosiljatelj = null;

  /** Konfiguracija. */
  private Properties konfiguracija;

  /** Aerodromi. */
  private static ArrayList<String> aerodromi = new ArrayList<String>();

  /**
   * Instancira klasu SakupljacLetovaAviona.
   *
   * @param jmsPosiljatelj2 JmsPosiljatelj
   * @param ds2 DataSource
   */
  public SakupljacLetovaAviona(JmsPosiljatelj jmsPosiljatelj2, javax.sql.DataSource ds2) {
    this.jmsPosiljatelj = jmsPosiljatelj2;
    this.ds = ds2;
  }

  /**
   * Start.
   */
  @Override
  public synchronized void start() {
    this.konfiguracija = ucitajKonfiguraciju();
    dohvatiAerodrome();
    super.start();
  }

  /**
   * Run.
   *
   * Sakuplja letove i šalje ih klijentu u ciklusima
   */
  @Override
  public void run() {
    String preuzimanjeOd = this.konfiguracija.getProperty("preuzimanje.od");
    String preuzimanjeDo = this.konfiguracija.getProperty("preuzimanje.do");
    int ciklus = Integer.parseInt(this.konfiguracija.getProperty("ciklus.trajanje"));
    long odVremena = konvertirajVrijeme(preuzimanjeOd);
    long doVremena = konvertirajVrijeme(preuzimanjeDo);
    odVremena = provjeriVrijemePocetka(odVremena);
    while (odVremena < doVremena) {
      dohvatiAerodrome();
      long vrijemePocetkaDretve = (long) System.currentTimeMillis();
      int brojUnosa = pripremiPoruku(odVremena);
      Date datum = new Date(odVremena * 1000);
      Format format = new SimpleDateFormat("yyyy-MM-dd");
      String poruka =
          "Na dan " + format.format(datum) + " preuzeto ukupno " + brojUnosa + " letova aviona";
      this.jmsPosiljatelj.saljiPoruku(poruka);
      long vrijemeKrajaDretve = (long) System.currentTimeMillis();
      try {
        Thread.sleep((ciklus * 1000) - (vrijemeKrajaDretve - vrijemePocetkaDretve));
      } catch (InterruptedException e) {
        Logger.getGlobal().log(Level.INFO, e.toString());
      }
      odVremena += 86400;
    }
  }

  /**
   * Dohvaća aerodrome iz baze i sprema ih u listu aerodroma.
   *
   */

  private void dohvatiAerodrome() {
    PreparedStatement pstmt = null;
    String query = "SELECT ICAO FROM AERODROMI_LETOVI WHERE AKTIVAN = ?";
    try (var con = ds.getConnection()) {
      pstmt = con.prepareStatement(query);
      pstmt.setBoolean(1, true);
      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        this.aerodromi.add(rs.getString("ICAO"));
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
  }

  /**
   * Provjerava u bazi postoji li već upisani letovi s određenim vremenom, ako postoje vraća vrijeme
   * za jedan dan više
   *
   * @param long Od vremena
   * @return long, vrijeme potrebno za početak gledanja letova
   */
  private long provjeriVrijemePocetka(long odVremena) {
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    String vrijeme = null;
    long vrijemeZaUsporedbu = 0;
    String query =
        "SELECT CAST(CAST(STORED AS DATE) AS VARCHAR(15)) AS STORED FROM LETOVI_POLASCI WHERE STORED IS NOT NULL ORDER BY id DESC LIMIT 1";
    try (var con = ds.getConnection()) {
      pstmt = con.prepareStatement(query);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        vrijeme = rs.getString("STORED");
      }
      pstmt.close();
      con.close();
      if (vrijeme != null) {
        vrijemeZaUsporedbu = konvertirajVrijemeIzBaze(vrijeme);
      }
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

    if (vrijemeZaUsporedbu > odVremena || vrijemeZaUsporedbu == odVremena) {
      return vrijemeZaUsporedbu + 86400;
    }
    return odVremena;
  }

  /**
   * Pripremi poruku. Vraća broj pronađenih letova.
   *
   * @param long Od vremena
   * @return brojUnosa - broj unesenih letova u bazu
   */
  private int pripremiPoruku(long odVremena) {
    // String korisnik = konfiguracija.getProperty("OpenSkyNetwork.korisnik");
    // String lozinka = konfiguracija.getProperty("OpenSkyNetwork.lozinka");
    String korisnik = konfiguracija.getProperty("DrugiKlijent.korisnik");
    String lozinka = konfiguracija.getProperty("DrugiKlijent.lozinka");
    long doVremena = odVremena + 86400;
    int brojUnosa = 0;
    // OSKlijent osKlijent = new OSKlijent(korisnik, lozinka);
    OSKlijentBP osKlijent = new OSKlijentBP(korisnik, lozinka);
    try {
      for (String icao : this.aerodromi) {
        var letovi = osKlijent.getDepartures(icao, odVremena, doVremena);
        for (LetAviona let : letovi) {
          dodajLet(let, odVremena);
          brojUnosa++;
        }
      }
    } catch (NwtisRestIznimka e) {
      Logger.getGlobal().log(Level.INFO, e.toString());
    }
    return brojUnosa;
  }

  /**
   * Dodaj let. Dodaje let u bazu podataka
   *
   * @param letAviona Let
   * @param long dan leta
   */
  private void dodajLet(LetAviona let, long odVremena) {
    PreparedStatement pstmt = null;
    String query =
        "INSERT INTO LETOVI_POLASCI VALUES" + " (default, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    try (var con = ds.getConnection()) {
      pstmt = con.prepareStatement(query);
      pstmt.setString(1, let.getIcao24());
      pstmt.setInt(2, let.getFirstSeen());
      pstmt.setString(3, let.getEstDepartureAirport());
      pstmt.setInt(4, let.getLastSeen());
      pstmt.setString(5, let.getEstArrivalAirport());
      pstmt.setString(6, let.getCallsign());
      pstmt.setInt(7, let.getEstDepartureAirportHorizDistance());
      pstmt.setInt(8, let.getEstDepartureAirportVertDistance());
      pstmt.setInt(9, let.getEstArrivalAirportHorizDistance());
      pstmt.setInt(10, let.getEstArrivalAirportVertDistance());
      pstmt.setInt(11, let.getDepartureAirportCandidatesCount());
      pstmt.setInt(12, let.getArrivalAirportCandidatesCount());
      Timestamp ts = new Timestamp(odVremena * 1000);
      pstmt.setTimestamp(13, ts);
      pstmt.executeUpdate();
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
  }

  /**
   * Konvertiraj vrijeme. Konvertira vrijeme iz stringa u long.
   *
   * @param string Vrijeme
   * @return long Vrijeme
   */
  private long konvertirajVrijeme(String vrijeme) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    Date dan;
    try {
      dan = dateFormat.parse(vrijeme);
      return dan.getTime() / 1000;
    } catch (ParseException e) {
      Logger.getGlobal().log(Level.INFO, e.toString());
      return 0;
    }
  }

  /**
   * Konvertiraj vrijeme iz baze. Konvertira vrijeme iz stringa u long
   *
   * @param string Vrijeme
   * @return long Vrijeme
   */
  private long konvertirajVrijemeIzBaze(String vrijeme) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date dan;
    try {
      dan = dateFormat.parse(vrijeme);
      return dan.getTime() / 1000;
    } catch (ParseException e) {
      Logger.getGlobal().log(Level.INFO, e.toString());
      return 0;
    }
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

  /**
   * Interrupt.
   */
  @Override
  public void interrupt() {

  }

}
