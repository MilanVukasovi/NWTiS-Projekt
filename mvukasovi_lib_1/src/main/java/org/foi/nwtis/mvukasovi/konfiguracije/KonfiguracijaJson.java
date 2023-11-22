/**
 * 
 */
package org.foi.nwtis.mvukasovi.konfiguracije;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.NeispravnaKonfiguracija;
import com.google.gson.Gson;

/**
 * Klasa KonfiguracijaJson za rad s postavkama konfiguracije u json formatu.
 * 
 * @author Milan Vukasović
 *
 */
public class KonfiguracijaJson extends KonfiguracijaApstraktna {

  /** Konstanta TIP. */
  public static final String TIP = "json";

  /**
   * Instancija klasu KonfiguracijaBin.
   *
   * @param nazivDatoteke Naziv datoteke
   */
  public KonfiguracijaJson(String nazivDatoteke) {
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
    if (tip == null || tip.compareTo(KonfiguracijaJson.TIP) != 0) {
      throw new NeispravnaKonfiguracija(
          "Datoteka '" + datoteka + "' nema tip " + KonfiguracijaJson.TIP);
    } else if (Files.exists(putanja)
        && (Files.isDirectory(putanja) || !Files.isWritable(putanja))) {
      throw new NeispravnaKonfiguracija(
          "Datoteka '" + datoteka + "' nije datoteka ili nije moguće upisati u nju.");
    }
    try (BufferedWriter pisac = new BufferedWriter(new FileWriter(datoteka))) {
      Gson gson = new Gson();
      gson.toJson(this.postavke, pisac);
    } catch (IOException e) {
      throw new NeispravnaKonfiguracija(e.getMessage());
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
    if (tip == null || tip.compareTo(KonfiguracijaJson.TIP) != 0) {
      throw new NeispravnaKonfiguracija(
          "Datoteka '" + datoteka + "' nema tip " + KonfiguracijaJson.TIP);
    } else if (!Files.exists(putanja) || Files.isDirectory(putanja) || !Files.isReadable(putanja)) {
      throw new NeispravnaKonfiguracija(
          "Datoteka '" + datoteka + "' nije datoteka ili nije moguće čitati.");
    }
    try (BufferedReader citac = new BufferedReader(new FileReader(datoteka))) {
      Gson gson = new Gson();
      this.postavke = gson.fromJson(citac, Properties.class);
    } catch (IOException e) {
      throw new NeispravnaKonfiguracija(e.getMessage());
    }
  }

}
