/**
 * 
 */
package org.foi.nwtis.mvukasovi.konfiguracije;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.NeispravnaKonfiguracija;

/**
 * Klasa KonfiguracijaXml za rad s postavkama konfiguracije u xml formatu.
 * 
 * @author Milan Vukasović
 *
 */
public class KonfiguracijaXml extends KonfiguracijaApstraktna {

  /** Konstanta TIP. */
  public static final String TIP = "xml";

  /**
   * Instancija klasu KonfiguracijaBin.
   *
   * @param nazivDatoteke Naziv datoteke
   */
  public KonfiguracijaXml(String nazivDatoteke) {
    super(nazivDatoteke);
  }

  /**
   * Spremi konfiguraciju.
   *
   * @param datoteka Naziv datoteke
   * @throws NeispravnaKonfiguracija ako nije ispravna konfiguracija
   */
  @Override
  public void spremiKonfiguraciju(String datoteka) throws NeispravnaKonfiguracija {
    var putanja = Path.of(datoteka);
    var tip = Konfiguracija.dajTipKonfiguracije(datoteka);
    if (tip == null || tip.compareTo(KonfiguracijaXml.TIP) != 0) {
      throw new NeispravnaKonfiguracija(
          "Datoteka '" + datoteka + "' nema tip " + KonfiguracijaXml.TIP);
    } else if (Files.exists(putanja)
        && (Files.isDirectory(putanja) || !Files.isWritable(putanja))) {
      throw new NeispravnaKonfiguracija(
          "Datoteka '" + datoteka + "' nije datoteka ili nije moguće upisati u nju.");
    }
    try {
      this.postavke.storeToXML(Files.newOutputStream(putanja), "NWTiS mvukasovi 2023.");;
    } catch (IOException e) {
      throw new NeispravnaKonfiguracija(
          "Datoteka '" + datoteka + "' nije moguće upisati u nju " + e.getMessage());
    }
  }

  /**
   * Učitaj konfiguraciju.
   *
   * @throws NeispravnaKonfiguracija ako nije ispravna konfiguracija
   */
  @Override
  public void ucitajKonfiguraciju() throws NeispravnaKonfiguracija {
    String datoteka = this.nazivDatoteke;
    var putanja = Path.of(datoteka);
    var tip = Konfiguracija.dajTipKonfiguracije(datoteka);
    if (tip == null || tip.compareTo(KonfiguracijaXml.TIP) != 0) {
      throw new NeispravnaKonfiguracija(
          "Datoteka '" + datoteka + "' nema tip " + KonfiguracijaXml.TIP);
    } else if (!Files.exists(putanja) || Files.isDirectory(putanja) || !Files.isReadable(putanja)) {
      throw new NeispravnaKonfiguracija(
          "Datoteka '" + datoteka + "' nije datoteka ili nije moguće čitati.");
    }
    try {
      this.postavke.loadFromXML(Files.newInputStream(putanja));
    } catch (IOException e) {
      throw new NeispravnaKonfiguracija(
          "Datoteka '" + datoteka + "' nije moguće čitati" + e.getMessage());
    }
  }

}
