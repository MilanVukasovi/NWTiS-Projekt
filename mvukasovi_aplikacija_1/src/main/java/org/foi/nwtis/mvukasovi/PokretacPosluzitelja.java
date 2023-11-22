package org.foi.nwtis.mvukasovi;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.NeispravnaKonfiguracija;

/**
 * Klasa služi za provjeru argumenata, učitavnje postavki i pokretanje klase GlavniPoslužitelj.
 * 
 * @author Milan Vukasović
 */
public class PokretacPosluzitelja {

  /**
   * Instancira klasu PokretacPosluzitelja.
   */
  public PokretacPosluzitelja() {}

  /**
   * Glavna metoda.
   *
   * @param args Argumenti
   */
  public static void main(String[] args) {

    var pokretacPosluzitelja = new PokretacPosluzitelja();
    if (!pokretacPosluzitelja.provjeriArgumente(args)) {
      Logger.getLogger(PokretacPosluzitelja.class.getName()).log(Level.SEVERE,
          "Nije upisan naziv datoteke.");
      return;
    }
    try {
      var konfiguracija = pokretacPosluzitelja.ucitajPostavke(args[0]);
      var glavniPosluzitelj = new GlavniPosluzitelj(konfiguracija);
      glavniPosluzitelj.pokreniPosluzitelja();
    } catch (NeispravnaKonfiguracija e) {
      Logger.getLogger(PokretacPosluzitelja.class.getName()).log(Level.SEVERE,
          "Pogreška kod datoteke postavki. " + e.getMessage());
    }
  }

  /**
   * Provjeri argumente.
   *
   * @param args Argumenti
   * @return true, ako postoji jedan argument, inače false
   */
  boolean provjeriArgumente(String[] args) {
    return args.length == 1 ? true : false;
  }

  /**
   * Učitaj postavke.
   *
   * @param nazivDatoteke Naziv datoteke
   * @return Konfiguracija
   * @throws NeispravnaKonfiguracija ako nije ispravna konfiguracija
   */
  Konfiguracija ucitajPostavke(String nazivDatoteke) throws NeispravnaKonfiguracija {
    return KonfiguracijaApstraktna.preuzmiKonfiguraciju(nazivDatoteke);
  }

}
