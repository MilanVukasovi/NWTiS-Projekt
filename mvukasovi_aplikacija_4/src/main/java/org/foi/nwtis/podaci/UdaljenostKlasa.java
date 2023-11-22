package org.foi.nwtis.podaci;

public class UdaljenostKlasa {
  private String drzava;
  private float km;

  public UdaljenostKlasa(String drzava, float km) {
    super();
    this.drzava = drzava;
    this.km = km;
  }

  /**
   * @return Država
   */
  public String getDrzava() {
    return drzava;
  }

  /**
   * @param Država
   */
  public void setDrzava(String drzava) {
    this.drzava = drzava;
  }

  /**
   * @return Km
   */
  public float getKm() {
    return km;
  }

  /**
   * @param Km
   */
  public void setKm(float km) {
    this.km = km;
  }

}
