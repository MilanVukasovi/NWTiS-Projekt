package org.foi.nwtis.mvukasovi.zrna;

import java.util.ArrayList;
import jakarta.annotation.Resource;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.enterprise.inject.Default;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.Queue;

/**
 * Klasa SakupljacJmsPoruka.
 */
@Default
@Singleton
@Startup
public class SakupljacJmsPoruka {
  /** Tvornica konekcija. */
  @Resource(mappedName = "jms/nwtis_qf_dz3")
  private ConnectionFactory connectionFactory;

  /** Red. */
  @Resource(mappedName = "jms/NWTiS_mvukasovi")
  private Queue queue;

  /** Kolekcija poruka. */
  ArrayList<String> kolekcijaPoruka = new ArrayList<String>();

  /**
   * Dohvaća kolekciju poruka.
   *
   * @return ArrayList<String> poruka
   */
  public ArrayList<String> dohvatiPoruke() {
    return this.kolekcijaPoruka;
  }

  /**
   * Dodaje poruku u kolekciju poruka
   *
   * @param poruka Poruka
   */
  public void postaviPoruku(String poruka) {
    this.kolekcijaPoruka.add(poruka);
  }

  /**
   * Briše poruke iz kolekcije poruka
   */
  public void obrisiPoruke() {
    this.kolekcijaPoruka.clear();
  }
}
