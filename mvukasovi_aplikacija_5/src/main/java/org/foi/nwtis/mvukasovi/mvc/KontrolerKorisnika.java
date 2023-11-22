package org.foi.nwtis.mvukasovi.mvc;

import org.foi.nwtis.mvukasovi.ws.WsKorisnici.endpoint.Korisnici;
import org.foi.nwtis.mvukasovi.ws.WsKorisnici.endpoint.Korisnik;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.xml.ws.WebServiceRef;

/**
 * Klasa KontrolerKorisnika.
 */
@Controller
@Path("korisnici")
@RequestScoped
public class KontrolerKorisnika {
  /** Servis korisnici. */
  @WebServiceRef(wsdlLocation = "http://localhost:8080/mvukasovi_aplikacija_4/korisnici?wsdl")
  private Korisnici service;

  /** Model. */
  @Inject
  private Models model;

  /**
   * Otvara početnu stranicu za upravljanje korisnicima
   */
  @GET
  @Path("")
  @View("korisnici.jsp")
  public void pocetak() {}

  /**
   * Otvara stranicu s formom za registraciju
   */
  @GET
  @Path("registracija")
  @View("registracija.jsp")
  public void registracija() {}

  /**
   * Šalje podatke WS servisu korisnici kako bi registrirao korisnika.
   * 
   * @param korIme Korisničko ime
   * @param lozinka Lozinka
   * @param ime Ime
   * @param prezime Prezime
   * @param request Zahtjev
   */
  @POST
  @Path("registriran")
  public Response registriraj(@FormParam("korIme") String korIme,
      @FormParam("lozinka") String lozinka, @FormParam("ime") String ime,
      @FormParam("prezime") String prezime, @Context HttpServletRequest request) {
    Korisnik korisnik = new Korisnik();
    korisnik.setKorIme(korIme);
    korisnik.setLozinka(lozinka);
    korisnik.setIme(ime);
    korisnik.setPrezime(prezime);
    var port = service.getWsKorisniciPort();
    boolean provjera = port.dodajKorisnika(korisnik);
    if (provjera) {
      HttpSession session = request.getSession();
      session.setAttribute("korIme", korIme);
      session.setAttribute("lozinka", lozinka);
      return Response.ok().build();
    } else {
      return Response.serverError().build();
    }
  }

  /**
   * Otvara stranicu s formom za prijavu
   */
  @GET
  @Path("prijava")
  @View("prijava.jsp")
  public void prijava() {}

  /**
   * Šalje podatke WS servisu korisnici kako bi prijavio korisnika u aplikaciju 5.
   * 
   * @param korIme Korisničko ime
   * @param lozinka Lozinka
   * @param request Zahtjev
   */
  @POST
  @Path("prijavljen")
  public Response prijavi(@FormParam("korIme") String korIme, @FormParam("lozinka") String lozinka,
      @Context HttpServletRequest request) {
    var port = service.getWsKorisniciPort();
    boolean provjera = port.autentikacijaKorisnika(korIme, lozinka);
    if (provjera) {
      HttpSession session = request.getSession();
      session.setAttribute("korIme", korIme);
      session.setAttribute("lozinka", lozinka);
      return Response.ok().build();
    } else {
      return Response.serverError().build();
    }
  }

  /**
   * Otvara stranicu s pregledom korisnika
   * 
   * @param traziImeKorisnika Traži ime korisnika
   * @param traziPrezimeKorisnika Treži prezime korisnika
   * @param request Zahtjev
   */
  @GET
  @Path("pregled")
  @View("korisniciPregled.jsp")
  public void pregledKorisnika(@QueryParam("traziImeKorisnika") String traziImeKorisnika,
      @QueryParam("traziPrezimeKorisnika") String traziPrezimeKorisnika,
      @Context HttpServletRequest request) {
    try {
      HttpSession session = request.getSession();
      String korIme = (String) session.getAttribute("korIme");
      String lozinka = (String) session.getAttribute("lozinka");
      var port = service.getWsKorisniciPort();
      var korisnici = port.dajKorisnike(korIme, lozinka, traziImeKorisnika, traziPrezimeKorisnika);
      model.put("korisnici", korisnici);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
