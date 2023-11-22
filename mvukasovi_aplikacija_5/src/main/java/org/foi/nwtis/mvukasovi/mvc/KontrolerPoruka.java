package org.foi.nwtis.mvukasovi.mvc;

import org.foi.nwtis.mvukasovi.zrna.SakupljacJmsPoruka;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

/**
 * Klasa KontrolerPoruka.
 */
@Controller
@Path("poruke")
@RequestScoped
public class KontrolerPoruka {
  /** Model. */
  @Inject
  private Models model;

  /** Sakupljač JMS poruka. */
  @Inject
  SakupljacJmsPoruka sakupljacJmsPoruka;

  /**
   * Otvara stranicu za pregled poruka. Ako se šalje parametar obriši, briše poruke.
   *
   * @param obrisi Obrisi
   */
  @GET
  @Path("")
  @View("jmsPoruke.jsp")
  public void pregledPoruka(@QueryParam("obrisi") String obrisi) {
    if (obrisi != null && obrisi.contentEquals("obrisi")) {
      System.out.println("Brišem poruke");
      sakupljacJmsPoruka.obrisiPoruke();
    }
    model.put("poruke", sakupljacJmsPoruka.dohvatiPoruke());
  }
}
