/**
 * 
 */
package org.foi.nwtis.mvukasovi.konfiguracije;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.NeispravnaKonfiguracija;

/**
 * Klasa KonfiguracijaBin za rad s postavkama konfiguracije u bin formatu.
 *
 * @author Milan Vukasović
 */
public class KonfiguracijaBin extends KonfiguracijaApstraktna {

  /** Konstanta TIP. */
  public static final String TIP = "bin";

  /**
   * Instancija klasu KonfiguracijaBin.
   *
   * @param nazivDatoteke Naziv datoteke
   */
  public KonfiguracijaBin(String nazivDatoteke) {
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
    if (tip == null || tip.compareTo(KonfiguracijaBin.TIP) != 0) {
      throw new NeispravnaKonfiguracija(
          "Datoteka '" + datoteka + "' nema tip " + KonfiguracijaBin.TIP);
    } else if (Files.exists(putanja)
        && (Files.isDirectory(putanja) || !Files.isWritable(putanja))) {
      throw new NeispravnaKonfiguracija(
          "Datoteka '" + datoteka + "' nije datoteka ili nije moguće upisati u nju.");
    }
    try (ObjectOutputStream izlazniTok = new ObjectOutputStream(Files.newOutputStream(putanja))) {
      izlazniTok.writeObject(this.postavke);
      izlazniTok.close();
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
    if (tip == null || tip.compareTo(KonfiguracijaBin.TIP) != 0) {
      throw new NeispravnaKonfiguracija(
          "Datoteka '" + datoteka + "' nema tip " + KonfiguracijaBin.TIP);
    } else if (!Files.exists(putanja) || Files.isDirectory(putanja) || !Files.isReadable(putanja)) {
      throw new NeispravnaKonfiguracija(
          "Datoteka '" + datoteka + "' nije datoteka ili nije moguće čitati.");
    }
    try (ObjectInputStream ulazniTok = new ObjectInputStream(Files.newInputStream(putanja))) {
      Object objekt = ulazniTok.readObject();
      if (objekt instanceof Properties) {
        this.postavke = (Properties) objekt;
      }
      ulazniTok.close();
    } catch (ClassNotFoundException | IOException e) {
      throw new NeispravnaKonfiguracija(e.getMessage());
    }
  }

}
