package org.foi.nwtis.mvukasovi.pomocnici;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mvukasovi.slusaci.Slusac;
import jakarta.servlet.ServletContext;

/**
 * Klasa Pomocnik.
 */
public class Pomocnik {

  /** Konfiguracija. */
  private Properties konfiguracija;

  /**
   * Instanca klase Pomocnik.
   */
  public Pomocnik() {
    this.konfiguracija = ucitajKonfiguraciju();
  }

  /**
   * Spoji se na posluzitelj. Šalje poruku aplikaciji 1.
   *
   * @param zahtjev Poruka koja se šalje aplikaciji 1
   * @return String -- odgovor na poruku
   */
  public String spojiSeNaPosluzitelj(String zahtjev) {
    String adresa = this.konfiguracija.getProperty("wa_1.adresa");
    int mreznaVrata = Integer.parseInt(this.konfiguracija.getProperty("wa_1.port"));
    try {
      var mreznaUticnica = new Socket(adresa, mreznaVrata);
      var citac = new BufferedReader(
          new InputStreamReader(mreznaUticnica.getInputStream(), Charset.forName("UTF-8")));
      var pisac = new BufferedWriter(
          new OutputStreamWriter(mreznaUticnica.getOutputStream(), Charset.forName("UTF-8")));

      pisac.write(zahtjev);
      pisac.flush();
      mreznaUticnica.shutdownOutput();
      var poruka = new StringBuilder();
      while (true) {
        var redak = citac.readLine();
        if (redak == null) {
          break;
        }
        Logger.getGlobal().log(Level.INFO, redak);
        poruka.append(redak);
      }
      mreznaUticnica.shutdownInput();
      mreznaUticnica.close();
      return poruka.toString();
    } catch (IOException e) {
      Logger.getGlobal().log(Level.SEVERE, "ERROR 29: " + e.getMessage());
      return null;
    }
  }

  /**
   * Učitava konfiguraciju
   *
   * @return Properties - konfiguraciju
   */
  private Properties ucitajKonfiguraciju() {
    Properties konfiguracija = new Properties();
    ServletContext kontekst = Slusac.dajKontekst();
    Properties konfig = (Properties) kontekst.getAttribute("konfig");
    konfiguracija = konfig;
    return konfiguracija;
  }
}
