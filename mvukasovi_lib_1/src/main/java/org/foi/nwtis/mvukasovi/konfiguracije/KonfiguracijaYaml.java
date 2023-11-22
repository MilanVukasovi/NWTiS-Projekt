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
import java.util.Map;
import java.util.Properties;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.NeispravnaKonfiguracija;
import org.yaml.snakeyaml.Yaml;

/**
 * Klasa KonfiguracijaYaml za rad s postavkama konfiguracije u yaml formatu.
 * 
 * @author Milan Vukasović
 *
 */
public class KonfiguracijaYaml extends KonfiguracijaApstraktna {

  /** Konstanta TIP. */
  public static final String TIP = "yaml";

  /**
   * Instancija klasu KonfiguracijaBin.
   *
   * @param nazivDatoteke Naziv datoteke
   */
  public KonfiguracijaYaml(String nazivDatoteke) {
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
    if (tip == null || tip.compareTo(KonfiguracijaTxt.TIP) != 0) {
      throw new NeispravnaKonfiguracija(
          "Datoteka '" + datoteka + "' nema tip " + KonfiguracijaTxt.TIP);
    } else if (Files.exists(putanja)
        && (Files.isDirectory(putanja) || !Files.isWritable(putanja))) {
      throw new NeispravnaKonfiguracija(
          "Datoteka '" + datoteka + "' nije datoteka ili nije moguće upisati u nju.");
    }
    try (BufferedWriter pisac = new BufferedWriter(new FileWriter(datoteka))) {
    } catch (IOException e) {
      throw new NeispravnaKonfiguracija(
          "Datoteka '" + datoteka + "' nije moguće čitati" + e.getMessage());
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
    if (tip == null || tip.compareTo(KonfiguracijaYaml.TIP) != 0) {
      throw new NeispravnaKonfiguracija(
          "Datoteka '" + datoteka + "' nema tip " + KonfiguracijaYaml.TIP);
    } else if (!Files.exists(putanja) || Files.isDirectory(putanja) || !Files.isReadable(putanja)) {
      throw new NeispravnaKonfiguracija(
          "Datoteka '" + datoteka + "' nije datoteka ili nije moguće čitati.");
    }
    try (BufferedReader citac = new BufferedReader(new FileReader(datoteka))) {
      Yaml yaml = new Yaml();
      Map<String, Object> podatci = yaml.load(citac);
      Properties postavke = new Properties();
      for (Map.Entry<String, Object> unos : podatci.entrySet()) {
        postavke.setProperty(unos.getKey(), unos.getValue().toString());
      }
      this.postavke = postavke;
    } catch (IOException e) {
      throw new NeispravnaKonfiguracija(
          "Datoteka '" + datoteka + "' nije moguće čitati" + e.getMessage());
    }
  }

}
