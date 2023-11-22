package org.foi.nwtis.mvukasovi.rest;

import org.foi.nwtis.mvukasovi.pomocnici.Pomocnik;
import org.foi.nwtis.podaci.StatusApp;
import com.google.gson.Gson;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Klasa RestNadzor.
 */
@Path("nadzor")
@RequestScoped
public class RestNadzor {

  /** Datasource. */
  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  /** Pomocnik. */
  Pomocnik pomocnik = new Pomocnik();

  /**
   * Daje status aplikacije 1.
   *
   * @return Response - odgovor REST servisa, status aplikacije 1
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajStatus() {
    String poruka = "STATUS";
    String odgovor = pomocnik.spojiSeNaPosluzitelj(poruka.toString());
    String[] odgovorDijelovi = odgovor.split(" ");
    if (odgovor == null) {
      return Response.status(Status.BAD_REQUEST).entity("Nije uključen").build();
    } else {
      StatusApp status = new StatusApp(Integer.parseInt(odgovorDijelovi[1]), odgovorDijelovi[0]);
      var gson = new Gson();
      var json = gson.toJson(status);
      return Response.ok().entity(json).build();
    }
  }

  /**
   * Obrađuje komande INIT, PAUZA i KRAJ
   *
   * @param komanda Komanda
   * @return Response - odgovor REST servisa, status i opis aplikacije 1
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("{komanda}")
  public Response dajKomandu(@PathParam("komanda") String komanda) {
    if (!komanda.contentEquals("KRAJ") && !komanda.contentEquals("INIT")
        && !komanda.contentEquals("PAUZA")) {
      return Response.status(Status.BAD_REQUEST).entity("Kriva komanda").build();
    }
    String poruka = komanda;
    String odgovor = pomocnik.spojiSeNaPosluzitelj(poruka.toString());
    var gson = new Gson();
    if (odgovor.contains("ERROR")) {
      String[] odgovorDijelovi = odgovor.split("\\s+", 3);
      StatusApp status = new StatusApp(Integer.parseInt(odgovorDijelovi[1]), odgovorDijelovi[2]);
      return Response.status(Status.BAD_REQUEST).entity(odgovor).build();
    } else if (komanda.contentEquals("INIT") || komanda.contentEquals("KRAJ")) {
      StatusApp status = new StatusApp(0, odgovor);
      var json = gson.toJson(status);
      return Response.ok().entity(json).build();
    } else if (komanda.contentEquals("PAUZA")) {
      String[] odgovorDijelovi = odgovor.split(" ");
      StatusApp status = new StatusApp(Integer.parseInt(odgovorDijelovi[1]), odgovorDijelovi[0]);
      var json = gson.toJson(status);
      return Response.ok().entity(json).build();
    }
    return Response.status(Status.BAD_REQUEST).build();
  }

  /**
   * Postavlja info na aplikaciji 1. Uzima da ili ne kao vrstu informacije.
   *
   * @param vrsta Vrsta
   * @return Response - odgovor REST servisa, status i opis aplikacije 1
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("INFO/{vrsta}")
  public Response dajInfo(@PathParam("vrsta") String vrsta) {
    String poruka = "INFO " + vrsta;
    String odgovor = pomocnik.spojiSeNaPosluzitelj(poruka.toString());
    var gson = new Gson();
    if (odgovor.contains("ERROR")) {
      String[] odgovorDijelovi = odgovor.split("\\s+", 3);
      StatusApp status = new StatusApp(Integer.parseInt(odgovorDijelovi[1]), odgovorDijelovi[2]);
      return Response.status(Status.BAD_REQUEST).entity(odgovor).build();
    } else {
      StatusApp status = new StatusApp(1, odgovor);
      var json = gson.toJson(status);
      return Response.ok().entity(json).build();
    }
  }

}
