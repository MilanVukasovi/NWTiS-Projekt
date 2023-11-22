package org.foi.nwtis.mvukasovi.mvc;

import jakarta.enterprise.context.RequestScoped;
import jakarta.mvc.Controller;
import jakarta.mvc.View;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

/**
 * Klasa KontrolerPocetna.
 */
@Controller
@Path("")
@RequestScoped
public class KontrolerPocetna {

  /**
   * Pocetak. Otvara početnu stranicu.
   */
  @GET
  @View("index.jsp")
  public void pocetak() {}
}
