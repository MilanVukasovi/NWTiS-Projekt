package org.foi.nwtis.mvukasovi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.Konfiguracija;

/**
 * Klasa mrežni radnik obrađuje zahtjeve klijenata.
 *
 * @author Milan Vukasović
 */
public class MrezniRadnik extends Thread {

  /** Mrežna utičnica. */
  protected Socket mreznaUticnica;

  /** Konfiguracija. */
  protected Konfiguracija konfig;

  /** Regex koji provjeruje zahtjeve. */
  public static String glavniPosluziteljRegex =
      "^((?<command>STATUS|KRAJ|INIT|PAUZA)|(INFO (?<info>DA|NE))|(UDALJENOST (((?<gpsSirina1>[0-9.]+) (?<gpsDuzina1>[0-9.]+) (?<gpsSirina2>[0-9.]+) (?<gpsDuzina2>[0-9.]+)))))$";

  /**
   * Instancira klasu MrezniRadnik.
   *
   * @param mreznaUticnica Mrežna utičnica
   * @param konfig Konfig
   */
  public MrezniRadnik(Socket mreznaUticnica, Konfiguracija konfig) {
    super();
    this.mreznaUticnica = mreznaUticnica;
    this.konfig = konfig;
  }

  /**
   * Započinje rad dretve.
   */
  @Override
  public synchronized void start() {
    super.start();
  }

  /**
   * Početak rada dretve. Čita zahtjev poslan od SimulatorMeteo ili GlavniKlijent, obrađuje ga i
   * odgovara.
   */
  @Override
  public void run() {
    try {
      var citac = new BufferedReader(
          new InputStreamReader(this.mreznaUticnica.getInputStream(), Charset.forName("UTF-8")));
      var pisac = new BufferedWriter(
          new OutputStreamWriter(this.mreznaUticnica.getOutputStream(), Charset.forName("UTF-8")));
      var poruka = new StringBuilder();
      while (true) {
        var redak = citac.readLine();
        if (redak == null) {
          break;
        }
        if (GlavniPosluzitelj.ispis == 1) {
          Logger.getGlobal().log(Level.INFO, redak);
        }
        poruka.append(redak);
      }
      this.mreznaUticnica.shutdownInput();
      var odgovor = this.obradiZahtjev(poruka.toString());
      pisac.write(odgovor);
      pisac.flush();
      this.mreznaUticnica.shutdownOutput();
      this.mreznaUticnica.close();
      GlavniPosluzitelj.smanjiBrojAktivnihDretvi();
    } catch (IOException e) {
      if (GlavniPosluzitelj.ispis == 1)
        Logger.getGlobal().log(Level.SEVERE, "ERROR 05 " + e.getMessage());
    }
  }

  /**
   * Obradi zahtjev. Provjerava se koji zahtjev je poslan od klijenta, te se šalje u iduću metodu na
   * daljnju obradu.
   *
   * @param komanda Komanda
   * @return String --odgovor na odgovarajući zahtjev
   */
  public String obradiZahtjev(String komanda) {
    String[] dijeloviKomande = provjeriKomandu(komanda);
    String odgovor = "OK";
    if (dijeloviKomande[0].contentEquals("STATUS")) {
      odgovor = obradiStatus();
    }
    if (dijeloviKomande[0].contentEquals("KRAJ")) {
      odgovor = obradiKraj();
    }
    if (dijeloviKomande[0].contentEquals("INIT")) {
      odgovor = obradiInit();
    }
    if (dijeloviKomande[0].contentEquals("PAUZA")) {
      odgovor = obradiPauza();
    }
    if (dijeloviKomande[0].contentEquals("INFO")) {
      odgovor = obradiInfo(dijeloviKomande[1]);
    }
    if (dijeloviKomande[0].contentEquals("UDALJENOST")) {
      odgovor = vratiUdaljenost(dijeloviKomande);
    }
    if (GlavniPosluzitelj.ispis == 1)
      Logger.getGlobal().log(Level.INFO, "Odgovaram: " + odgovor);
    return odgovor;
  }

  /**
   * Obradi pauzu. Provjerava je li pauza već postavljena, te ako nije je postavlja i vraća
   * brojZahtjeva
   * 
   * @return String -- broj zahtjeva
   */
  private String obradiPauza() {
    if (GlavniPosluzitelj.pauza == true) {
      return "ERROR 01 Poslužitelj je već u pauzi!";
    }
    GlavniPosluzitelj.pauza = true;
    return "OK " + GlavniPosluzitelj.dohvatiBrojZahtjeva();
  }

  /**
   * Obradi init komandu. Provjerava je li aplikacija već aktivna, te ako nije postavlja je
   * aktivnom-
   *
   * @return String -- OK ako je aktivirana, ERROR ako je već bila aktivna
   */
  private String obradiInit() {
    if (GlavniPosluzitelj.pauza == false) {
      return "ERROR 02 Poslužitelj je već aktivan!";
    }
    GlavniPosluzitelj.postaviBrojZahtjevaNaNula();
    GlavniPosluzitelj.pauza = false;
    return "OK";
  }

  /**
   * Obradi info komandu. Dopušta ispis primljene komande u konzolu.
   *
   * @return String -- OK ako je uspješno, ERROR ako je ispis već omogućen/onemogućen
   */
  private String obradiInfo(String podKomanda) {
    if (podKomanda.contentEquals("DA")) {
      if (GlavniPosluzitelj.ispis == 1) {
        return "ERROR 03 Ispis je već omogućen";
      }
      GlavniPosluzitelj.ispis = 1;
      return "OK";
    } else if (podKomanda.contentEquals("NE")) {
      if (GlavniPosluzitelj.ispis == 0) {
        return "ERROR 04 Ispis je već onemogućen";
      }
      GlavniPosluzitelj.ispis = 0;
      return "OK";
    } else {
      return "ERROR 05 Nije upisan DA ili NE";
    }
  }

  /**
   * Obradi status komandu.
   *
   * @return String -- OK 0 ako je pauza, inače OK 1
   */
  private String obradiStatus() {
    return (GlavniPosluzitelj.pauza == true) ? "OK 0" : "OK 1";
  }

  /**
   * Provjeravanje je li komanda koju je klijent poslao ispravna pomoću regex-a.
   *
   * @param komanda Komande
   * @return String[] --niz komande u dijelovima ako je dobro upisana, inače null
   */
  public String[] provjeriKomandu(String komanda) {
    Pattern uzorak = Pattern.compile(glavniPosluziteljRegex);
    Matcher m = uzorak.matcher(komanda);
    boolean status = m.matches();
    String dijeloviKomande[] = new String[10];
    if (status) {
      if (m.group("command") != null) {
        dijeloviKomande[0] = m.group("command");
      } else if (m.group("info") != null) {
        dijeloviKomande[0] = "INFO";
        dijeloviKomande[1] = m.group("info");
      } else if (m.group("gpsSirina1") != null) {
        dijeloviKomande[0] = "UDALJENOST";
        dijeloviKomande[1] = m.group("gpsSirina1");
        dijeloviKomande[2] = m.group("gpsDuzina1");
        dijeloviKomande[3] = m.group("gpsSirina2");
        dijeloviKomande[4] = m.group("gpsDuzina2");
      }
    }
    return dijeloviKomande;
  }


  /**
   * Zatvara glavnog poslužitelja.
   *
   * @param dijeloviKomande Dijelovi komande
   * @return string -- "OK"
   */
  private String obradiKraj() {
    GlavniPosluzitelj.kraj = true;
    return "OK";
  }

  /**
   * Vraća izračunatu udaljenost za dvije GPS lokacije.
   *
   * @param dijeloviKomande Dijelovi komande
   * @return string -- "OK" + udaljenost, ERROR ako je poslužitelj u pauzi
   */
  private String vratiUdaljenost(String[] komanda) {
    if (GlavniPosluzitelj.pauza == true) {
      return "ERROR 01 Poslužitelj je na pauzi";
    }
    Udaljenost trenutnaUdaljenost =
        new Udaljenost(Double.parseDouble(komanda[1]), Double.parseDouble(komanda[2]),
            Double.parseDouble(komanda[3]), Double.parseDouble(komanda[4]), 0);
    double udaljenost = izracunajUdaljenost(trenutnaUdaljenost);
    GlavniPosluzitelj.povecajBrojZahtjeva();
    return "OK " + udaljenost;
  }

  /**
   * Izračunava udaljenost za dvije GPS lokacije.
   *
   * @param trenutnaUdaljenost Udaljenost
   * @return double -- udaljenost
   */
  private double izracunajUdaljenost(Udaljenost trenutnaUdaljenost) {
    if ((trenutnaUdaljenost.gpsSirina1() == trenutnaUdaljenost.gpsSirina2())
        && (trenutnaUdaljenost.gpsDuzina1() == trenutnaUdaljenost.gpsDuzina2())) {
      return 0;
    } else {
      double theta = trenutnaUdaljenost.gpsDuzina1() - trenutnaUdaljenost.gpsDuzina2();
      double udaljenost = Math.sin(Math.toRadians(trenutnaUdaljenost.gpsSirina1()))
          * Math.sin(Math.toRadians(trenutnaUdaljenost.gpsSirina2()))
          + Math.cos(Math.toRadians(trenutnaUdaljenost.gpsSirina1()))
              * Math.cos(Math.toRadians(trenutnaUdaljenost.gpsSirina2()))
              * Math.cos(Math.toRadians(theta));
      udaljenost = Math.acos(udaljenost);
      udaljenost = Math.toDegrees(udaljenost);
      udaljenost = udaljenost * 60 * 1.1515;
      udaljenost = udaljenost * 1.609344;
      DecimalFormat df = new DecimalFormat("#####.##");
      return Double.valueOf(df.format(udaljenost));
    }
  }

  /**
   * Interrupt.
   */
  @Override
  public void interrupt() {
    super.interrupt();
  }
}
