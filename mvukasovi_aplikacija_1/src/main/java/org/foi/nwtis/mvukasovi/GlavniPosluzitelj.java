package org.foi.nwtis.mvukasovi;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;

/**
 * Klasa GlavniPosluzitelj koja je zadužena za otvaranje veze na određenim mrežnim vratima/portu.
 * 
 * @author Milan Vukasović
 */
public class GlavniPosluzitelj {

  /** Konfiguracija. */
  protected Konfiguracija konfig;

  /** Ispis. */
  public static int ispis = 0;

  /** Mrežna vrata. */
  private int mreznaVrata = 8000;

  /** Broj čekaca. */
  private int brojCekaca = 10;

  /** Broj dretvi. */
  private int brojDretvi = 0;

  /** Broj radnika. */
  private int brojRadnika = 10;

  /** Broj aktivnih dretvi. */
  private static int brojAktivnihDretvi = 0;

  /** Pauza. */
  public static boolean pauza = true;

  /** Broj zahtjeva. */
  private static int brojZahtjeva = 0;

  /** Kraj. */
  public static boolean kraj = false;

  /**
   * Instancira klasu GlavniPosluzitelj uz danu konfiguraciju.
   *
   * @param konfig Konfiguracija
   */
  public GlavniPosluzitelj(Konfiguracija konfig) {
    super();
    this.konfig = konfig;
    this.ispis = Integer.parseInt(konfig.dajPostavku("ispis"));
    this.mreznaVrata = Integer.parseInt(konfig.dajPostavku("mreznaVrata"));
    this.brojCekaca = Integer.parseInt(konfig.dajPostavku("brojCekaca"));
    this.brojRadnika = Integer.parseInt(konfig.dajPostavku("brojRadnika"));
  }

  /**
   * Pokreće poslužitelja, učitava korisnike, lokacije i uređaje i otvara mrežna vrata.
   */
  public void pokreniPosluzitelja() {
    otvoriMreznaVrata();
  }

  /**
   * Otvara mrežna vrata
   */
  public void otvoriMreznaVrata() {
    try (var posluzitelj = new ServerSocket(this.mreznaVrata, this.brojCekaca)) {
      while (!GlavniPosluzitelj.kraj) {
        var uticnica = posluzitelj.accept();
        if (brojAktivnihDretvi <= brojRadnika) {
          var dretva = new MrezniRadnik(uticnica, this.konfig);
          dretva.setName("mvukasovi_" + brojDretvi++);
          povecajBrojAktivnihDretvi();
          dretva.start();
          dretva.join();
        } else {
          Logger.getGlobal().log(Level.SEVERE, "Sve dretve su zauzete");
        }
      }
    } catch (IOException e) {
      Logger.getGlobal().log(Level.SEVERE, "Mrežna vrata su zauzeta");
      return;
    } catch (InterruptedException e) {
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    }
  }

  /**
   * Povećava broj zahtjeva
   */
  public static synchronized void povecajBrojZahtjeva() {
    brojZahtjeva++;
  }

  /**
   * Dohvaća broj zahtjeva
   */
  public static synchronized int dohvatiBrojZahtjeva() {
    return brojZahtjeva;
  }

  /**
   * Postavlja broj zahtjeva na nula
   */
  public static synchronized void postaviBrojZahtjevaNaNula() {
    brojZahtjeva = 0;;
  }

  /**
   * Povećava broj aktivnih dretvi
   */
  public static synchronized void povecajBrojAktivnihDretvi() {
    brojAktivnihDretvi++;
  }

  /**
   * Smanjuje broj aktivnih dretvi
   */
  public static synchronized void smanjiBrojAktivnihDretvi() {
    brojAktivnihDretvi--;
  }

}
