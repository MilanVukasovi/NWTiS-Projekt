package org.foi.nwtis.mvukasovi.mvc;

import java.util.List;
import org.foi.nwtis.mvukasovi.rest.RestKlijentAerodroma;
import org.foi.nwtis.mvukasovi.ws.WsAerodromi.endpoint.Aerodromi;
import org.foi.nwtis.mvukasovi.ws.WsMeteo.endpoint.Meteo;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.UdaljenostiIzmeduAerodroma;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.xml.ws.WebServiceRef;

/**
 * Klasa KontrolerAerodroma.
 */
@Controller
@Path("aerodromi")
@RequestScoped
public class KontrolerAerodroma {

  /** Servis aerodromi. */
  @WebServiceRef(wsdlLocation = "http://localhost:8080/mvukasovi_aplikacija_4/aerodromi?wsdl")
  private Aerodromi aerodromService;

  /** Servis meteo */
  @WebServiceRef(wsdlLocation = "http://localhost:8080/mvukasovi_aplikacija_4/meteo?wsdl")
  private Meteo meteoService;
  /** Model. */
  @Inject
  private Models model;

  /**
   * Otvara početnu stranicu za aerodrome.
   */
  @GET
  @View("aerodromi.jsp")
  public void pocetak() {}

  /**
   * Prikazuje sve aerodrome, sa straničenjem, moguće filtrirati prema nazivu ili državi
   *
   * @param odBroja Od broja
   * @param broj Broj
   * @param traziNaziv Trazi naziv
   * @param traziDrzavu Trazi drzavu
   * @return Aerodromi
   */
  @GET
  @Path("pregled")
  @View("aerodromSvi.jsp")
  public void getAerodromi(@QueryParam("odBroja") @DefaultValue("0") int odBroja,
      @QueryParam("broj") @DefaultValue("20") int broj, @QueryParam("traziNaziv") String traziNaziv,
      @QueryParam("traziDrzavu") String traziDrzavu) {
    try {
      RestKlijentAerodroma rca = new RestKlijentAerodroma();
      List<Aerodrom> aerodromi = rca.dajSveAerodrome(odBroja, broj, traziNaziv, traziDrzavu);
      model.put("aerodromi", aerodromi);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Dodaj aerodrom za let.
   *
   * @param icao Icao
   * @param request Zahtjev
   * @return Response
   */
  @POST
  @Path("dodajAerodrom/{icao}")
  public Response dodajAerodromZaLet(@PathParam("icao") String icao,
      @Context HttpServletRequest request) {
    try {
      HttpSession session = request.getSession();
      String korIme = (String) session.getAttribute("korIme");
      String lozinka = (String) session.getAttribute("lozinka");
      var port = aerodromService.getWsAerodromiPort();
      port.dodajAerodromZaLetove(korIme, lozinka, icao);
      return Response.ok().build();
    } catch (Exception e) {
      e.printStackTrace();
      return Response.serverError().build();
    }
  }

  /**
   * Unesi aerodrom.
   */
  @GET
  @Path("icao")
  @View("aerodromUnesiIcao.jsp")
  public void unesiAerodrom() {}

  /**
   * Daj aerodrom.
   *
   * @param icao the icao
   */
  @GET
  @Path("{icao}")
  @View("aerodromIcao.jsp")
  public void dajAerodrom(@PathParam("icao") String icao) {
    try {
      RestKlijentAerodroma rca = new RestKlijentAerodroma();
      var aerodrom = rca.dajAerodrom(icao);
      var port = meteoService.getWsMeteoPort();
      var meteo = port.dajMeteo(icao);
      model.put("aerodrom", aerodrom);
      model.put("meteo", meteo);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Daj aerodromi baza.
   *
   * @param request the request
   */
  @GET
  @Path("aerodromiBaza")
  @View("aerodromiBaza.jsp")
  public void dajAerodromiBaza(@Context HttpServletRequest request) {
    try {
      HttpSession session = request.getSession();
      String korIme = (String) session.getAttribute("korIme");
      String lozinka = (String) session.getAttribute("lozinka");
      var port = aerodromService.getWsAerodromiPort();
      var aerodromi = port.dajAerodromeZaLetove(korIme, lozinka);
      model.put("aerodromi", aerodromi);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Aktiviraj dohvat.
   *
   * @param icao the icao
   * @param request the request
   * @return the response
   */
  @POST
  @Path("aerodromiBazaAktiviraj")
  public Response aktivirajDohvat(@QueryParam("icao") String icao,
      @Context HttpServletRequest request) {
    try {
      HttpSession session = request.getSession();
      String korIme = (String) session.getAttribute("korIme");
      String lozinka = (String) session.getAttribute("lozinka");
      var port = aerodromService.getWsAerodromiPort();
      port.aktivirajAerodromZaLetove(korIme, lozinka, icao);
      return Response.ok().build();
    } catch (Exception e) {
      e.printStackTrace();
      return Response.serverError().build();
    }
  }

  /**
   * Pauziraj dohvat.
   *
   * @param icao the icao
   * @param request the request
   * @return the response
   */
  @POST
  @Path("aerodromiBazaPauziraj")
  public Response pauzirajDohvat(@QueryParam("icao") String icao,
      @Context HttpServletRequest request) {
    try {
      HttpSession session = request.getSession();
      String korIme = (String) session.getAttribute("korIme");
      String lozinka = (String) session.getAttribute("lozinka");
      var port = aerodromService.getWsAerodromiPort();
      port.pauzirajAerodromZaLetove(korIme, lozinka, icao);
      return Response.ok().build();
    } catch (Exception e) {
      e.printStackTrace();
      return Response.serverError().build();
    }
  }

  /**
   * Unesi aerodrom udaljenosti.
   */
  @GET
  @Path("udaljenosti2aerodromaUnesi")
  @View("aerodromUnesiUdaljenosti.jsp")
  public void unesiAerodromUdaljenosti() {}

  /**
   * Daj udaljenosti izmedu dva aerodoma.
   *
   * @param icaoOd the icao od
   * @param icaoDo the icao do
   */
  @GET
  @Path("{icaoOd}/{icaoDo}")
  @View("aerodromiUdaljenosti.jsp")
  public void dajUdaljenostiIzmeduDvaAerodoma(@PathParam("icaoOd") String icaoOd,
      @PathParam("icaoDo") String icaoDo) {
    try {
      RestKlijentAerodroma rca = new RestKlijentAerodroma();
      var udaljenosti = rca.dajUdaljenostiIzmeduDvaAerodoma(icaoOd, icaoDo);
      model.put("udaljenosti", udaljenosti);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @GET
  @Path("aerodromIcaoUdaljenostiUnesi")
  @View("aerodromIcaoUdaljenostUnesi.jsp")
  public void dajUdaljenostiIzmeduSvihAerodomaUnos() {

  }


  /**
   * Prikazuje udaljenosti između svih aerodoma s odđrenim aerodromom.
   *
   * @param icao ICAO
   * @param odBroja Od broja
   * @param broj Broj
   */
  @GET
  @Path("aerodromUdaljenosti")
  @View("aerodromIcaoUdaljenost.jsp")
  public void dajUdaljenostiIzmeduSvihAerodoma(@QueryParam("icao") String icao,
      @QueryParam("odBroja") int odBroja, @QueryParam("broj") int broj) {
    try {
      RestKlijentAerodroma rca = new RestKlijentAerodroma();
      var udaljenosti = rca.dajUdaljenostiIzmeduSvihAerodoma(icao, odBroja, broj);
      model.put("udaljenosti", udaljenosti);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Unesi izracun.
   */
  @GET
  @Path("izracunaj")
  @View("aerodromUnesiUdaljenosti2.jsp")
  public void unesiIzracun() {}

  /**
   * Daj izracun izmedu dva aerodoma.
   *
   * @param icaoOd the icao od
   * @param icaoDo the icao do
   */
  @GET
  @Path("{icaoOd}/izracunaj/{icaoDo}")
  @View("aerodromIzracun.jsp")
  public void dajIzracunIzmeduDvaAerodoma(@PathParam("icaoOd") String icaoOd,
      @PathParam("icaoDo") String icaoDo) {
    try {
      RestKlijentAerodroma rca = new RestKlijentAerodroma();
      String udaljenost = rca.dajIzracunIzmeduDvaAerodroma(icaoOd, icaoDo);
      model.put("udaljenost", udaljenost);
      model.put("icaoOd", icaoOd);
      model.put("icaoDo", icaoDo);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Unesi udaljenost 1.
   */
  @GET
  @Path("udaljenost1")
  @View("aerodromUnesiUdaljenosti3.jsp")
  public void unesiUdaljenost1() {}

  /**
   * Daj udaljesnost 1.
   *
   * @param icaoOd the icao od
   * @param icaoDo the icao do
   */
  @GET
  @Path("{icaoOd}/udaljenost1/{icaoDo}")
  @View("aerodromUdaljenost1.jsp")
  public void dajUdaljesnost1(@PathParam("icaoOd") String icaoOd,
      @PathParam("icaoDo") String icaoDo) {
    try {
      RestKlijentAerodroma rca = new RestKlijentAerodroma();
      List<UdaljenostiIzmeduAerodroma> udaljenost = rca.dajUdaljenost1(icaoOd, icaoDo);
      model.put("udaljenost", udaljenost);
      model.put("icaoOd", icaoOd);
      model.put("icaoDo", icaoDo);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Unesi udaljenost 2.
   */
  @GET
  @Path("udaljenost2")
  @View("aerodromUnesiUdaljenosti4.jsp")
  public void unesiUdaljenost2() {}

  /**
   * Daj udaljesnost 2.
   *
   * @param icaoOd the icao od
   * @param drzava the drzava
   * @param km the km
   */
  @GET
  @Path("{icaoOd}/udaljenost2/")
  @View("aerodromUdaljenost2.jsp")
  public void dajUdaljesnost2(@PathParam("icaoOd") String icaoOd,
      @QueryParam("drzava") String drzava, @QueryParam("km") String km) {
    try {
      if (drzava == null || km == null) {
        throw new Exception("Nije unijet država ili km");
      }
      RestKlijentAerodroma rca = new RestKlijentAerodroma();
      List<UdaljenostiIzmeduAerodroma> udaljenost = rca.dajUdaljenost2(icaoOd, drzava, km);
      model.put("udaljenost", udaljenost);
      model.put("icaoOd", icaoOd);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
