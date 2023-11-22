package org.foi.nwtis.podaci;

public class Korisnik {

  private String korIme;
  private String lozinka;
  private String ime;
  private String prezime;


  public Korisnik(String korIme, String lozinka, String ime, String prezime) {
    super();
    this.korIme = korIme;
    this.lozinka = lozinka;
    this.ime = ime;
    this.prezime = prezime;
  }


  public Korisnik() {
    // TODO Auto-generated constructor stub
  }


  public String getKorIme() {
    return korIme;
  }


  public void setKorIme(String korIme) {
    this.korIme = korIme;
  }


  public String getLozinka() {
    return lozinka;
  }


  public void setLozinka(String lozinka) {
    this.lozinka = lozinka;
  }


  public String getIme() {
    return ime;
  }


  public void setIme(String ime) {
    this.ime = ime;
  }


  public String getPrezime() {
    return prezime;
  }


  public void setPrezime(String prezime) {
    this.prezime = prezime;
  }


}
