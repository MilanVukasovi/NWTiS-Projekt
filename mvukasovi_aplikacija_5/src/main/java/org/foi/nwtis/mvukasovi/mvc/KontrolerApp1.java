package org.foi.nwtis.mvukasovi.mvc;

import org.foi.nwtis.mvukasovi.rest.RestKlijentApp1;
import org.foi.nwtis.podaci.StatusApp;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

/**
 * Klasa KontrolerApp1.
 */
@Controller
@Path("app1")
@RequestScoped
public class KontrolerApp1 {

  /** Model. */
  @Inject
  private Models model;

  /**
   * Otvara početnu stranicu za upravljanje aplikacijom 1
   */
  @GET
  @View("app1.jsp")
  public void pocetak() {}

  /**
   * Šalje komandu REST servisu api/nadzor na aplikaciji 2 i dohvaća status određene komande.
   * 
   * @param komanda Komanda
   */
  @POST
  @Path("komanda")
  @View("app1.jsp")
  public void posaljiKomandu(@FormParam("action") String komanda) {
    StatusApp statusApp = null;
    RestKlijentApp1 rca = new RestKlijentApp1();
    if (komanda != null) {
      switch (komanda) {
        case "STATUS":
          statusApp = rca.dajStatus();
          break;
        case "KRAJ":
          statusApp = rca.dajKrajInitPauza(komanda);
          break;
        case "INIT":
          statusApp = rca.dajKrajInitPauza(komanda);
          break;
        case "PAUZA":
          statusApp = rca.dajKrajInitPauza(komanda);
          break;
        case "INFO_DA":
          statusApp = rca.dajInfo("DA");
          break;
        case "INFO_NE":
          statusApp = rca.dajInfo("NE");
          break;
      }
    }
    model.put("komanda", komanda);
    model.put("status", statusApp);
  }

}
