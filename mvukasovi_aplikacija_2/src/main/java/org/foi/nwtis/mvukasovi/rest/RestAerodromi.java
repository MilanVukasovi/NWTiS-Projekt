package org.foi.nwtis.mvukasovi.rest;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mvukasovi.pomocnici.Pomocnik;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Lokacija;
import org.foi.nwtis.podaci.Udaljenost;
import org.foi.nwtis.podaci.UdaljenostAerodrom;
import org.foi.nwtis.podaci.UdaljenostiIzmeduAerodroma;
import com.google.gson.Gson;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Klasa RestAerodromi služi za rad s aerodromima.
 */
@Path("aerodromi")
@RequestScoped
public class RestAerodromi {

  /** Datasource. */
  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  /** Pomocnik. */
  Pomocnik pomocnik = new Pomocnik();

  /**
   * Daj sve aerodrome. Uzima početni broj koji dohvaćamo za straničenje i broj podataka koji želimo
   * uzeti. Moguće filtrirati pomoću traziNaziv i traziDrzavu
   *
   * @param traziNaziv Trazi naziv
   * @param traziDrzavu Trazi drzavu
   * @param odBroja Od broja
   * @param broj Bbroj
   * @return Response - odgovor REST servisa, svi aerodromi
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajSveAerodrome(@QueryParam("traziNaziv") String traziNaziv,
      @QueryParam("traziDrzavu") String traziDrzavu,
      @QueryParam("odBroja") @DefaultValue("1") int odBroja,
      @QueryParam("broj") @DefaultValue("20") int broj) {
    var aerodromi = new ArrayList<Aerodrom>();
    PreparedStatement pstmt = null;
    String query = "SELECT * FROM AIRPORTS";
    query += (traziNaziv != null || traziDrzavu != null) ? " WHERE" : "";
    query += (traziNaziv != null) ? " NAME LIKE ?" : "";
    query += (traziNaziv != null && traziDrzavu != null) ? " AND" : "";
    query += (traziDrzavu != null) ? " ISO_COUNTRY LIKE ?" : "";
    query += " ORDER BY ICAO OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
    try (var con = ds.getConnection()) {
      pstmt = con.prepareStatement(query);
      int index = 1;
      if (traziNaziv != null)
        pstmt.setString(index++, "%" + traziNaziv + "%");
      if (traziDrzavu != null)
        pstmt.setString(index++, "%" + traziDrzavu + "%");
      pstmt.setInt(index++, odBroja);
      pstmt.setInt(index++, broj);
      ResultSet rs = pstmt.executeQuery();

      while (rs.next()) {
        String icao = rs.getString("ICAO");
        String naziv = rs.getString("NAME");
        String drzava = rs.getString("ISO_COUNTRY");
        String lok = rs.getString("COORDINATES");
        String[] koordinate = lok.split(", ");
        Lokacija lokacija = new Lokacija(koordinate[0], koordinate[1]);
        var aerodrom = new Aerodrom(icao, naziv, drzava, lokacija);
        aerodromi.add(aerodrom);
      }
      rs.close();
      pstmt.close();
      con.close();
    } catch (Exception e) {
      Logger.getGlobal().log(Level.INFO, e.toString());
    } finally {
      try {
        if (pstmt != null && pstmt.isClosed()) {
          pstmt.close();
        }
      } catch (SQLException e) {
        Logger.getGlobal().log(Level.INFO, e.toString());
      }
    }

    var gson = new Gson();
    var jsonUdaljenosti = gson.toJson(aerodromi);
    var odgovor = Response.ok().entity(jsonUdaljenosti).build();
    return odgovor;
  }

  /**
   * Daj aerodrom. Vraća aerodrom s određenim icao.
   *
   * @param icao Icao
   * @return Response - odgovor REST servisa, aerodrom s zadanim icao
   */
  @GET
  @Path("{icao}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajAerodrom(@PathParam("icao") String icao) {
    Aerodrom aerodrom = null;
    aerodrom = obradiAerodrom(icao);
    if (aerodrom == null) {
      return Response.status(404).build();
    } else {
      var gson = new Gson();
      var jsonUdaljenosti = gson.toJson(aerodrom);
      var odgovor = Response.ok().entity(jsonUdaljenosti).build();
      return odgovor;
    }
  }

  /**
   * Daj udaljenosti između dva aerodoma sa zadanim polazišnim i odredišnim icao.
   *
   * @param icaoOd ICAO od
   * @param icaoDo ICAO do
   * @return Response - odgovor REST servisa, vraća udaljenosti
   */
  @Path("{icaoOd}/{icaoDo}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajUdaljenostiIzmeđuDvaAerodoma(@PathParam("icaoOd") String icaoOd,
      @PathParam("icaoDo") String icaoDo) {
    var udaljenosti = new ArrayList<Udaljenost>();
    PreparedStatement pstmt = null;
    String query = "select ICAO_FROM, ICAO_TO, COUNTRY, DIST_CTRY from AIRPORTS_DISTANCE_MATRIX "
        + "WHERE ICAO_FROM = ? AND ICAO_TO = ?";

    try (var con = ds.getConnection()) {
      pstmt = con.prepareStatement(query);
      pstmt.setString(1, icaoOd);
      pstmt.setString(2, icaoDo);
      ResultSet rs = pstmt.executeQuery();

      while (rs.next()) {
        String drzava = rs.getString("COUNTRY");
        float udaljDrzava = rs.getFloat("DIST_CTRY");
        var put = new Udaljenost(drzava, udaljDrzava);
        udaljenosti.add(put);
      }
      rs.close();
      pstmt.close();
      con.close();
    } catch (Exception e) {
      Logger.getGlobal().log(Level.INFO, e.toString());
    } finally {
      try {
        if (pstmt != null && pstmt.isClosed()) {
          pstmt.close();
        }
      } catch (SQLException e) {
        Logger.getGlobal().log(Level.INFO, e.toString());
      }
    }

    var gson = new Gson();
    var jsonUdaljenosti = gson.toJson(udaljenosti);
    var odgovor = Response.ok().entity(jsonUdaljenosti).build();
    return odgovor;
  }

  /**
   * Daj udaljenosti između svih aerodroma s određenim aerodromom.
   *
   * @param icao ICAO
   * @param odBroja Od broja
   * @param broj broj
   * @return Response - odgovor REST servisa, vraća udaljesnost aerodrom
   */
  @GET
  @Path("{icao}/udaljenosti")
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajUdaljenostiIzmeduSvihAerodroma(@PathParam("icao") String icao,
      @QueryParam("odBroja") @DefaultValue("1") int odBroja,
      @QueryParam("broj") @DefaultValue("20") int broj) {
    var udaljenosti = new ArrayList<UdaljenostAerodrom>();
    PreparedStatement pstmt = null;
    String query = "SELECT ICAO_FROM, ICAO_TO, DIST_TOT FROM AIRPORTS_DISTANCE_MATRIX "
        + "WHERE ICAO_FROM = ? GROUP BY ICAO_FROM, ICAO_TO, DIST_TOT OFFSET "
        + "? ROWS FETCH NEXT ? ROWS ONLY";

    try (var con = ds.getConnection()) {
      pstmt = con.prepareStatement(query);
      pstmt.setString(1, icao);
      pstmt.setInt(2, odBroja);
      pstmt.setInt(3, broj);
      ResultSet rs = pstmt.executeQuery();

      while (rs.next()) {
        String icaoDo = rs.getString("ICAO_TO");
        float udaljDrzava = rs.getFloat("DIST_TOT");
        var put = new UdaljenostAerodrom(icaoDo, udaljDrzava);
        udaljenosti.add(put);
      }

      rs.close();
      pstmt.close();
      con.close();
    } catch (Exception e) {
      Logger.getGlobal().log(Level.INFO, e.toString());
    } finally {
      try {
        if (pstmt != null && pstmt.isClosed()) {
          pstmt.close();
        }
      } catch (SQLException e) {
        Logger.getGlobal().log(Level.INFO, e.toString());
      }
    }
    var gson = new Gson();
    var jsonUdaljenosti = gson.toJson(udaljenosti);
    var odgovor = Response.ok().entity(jsonUdaljenosti).build();
    return odgovor;
  }

  /**
   * Izracunaj udaljenost između polazišnog i odredišnog aerodroma.
   *
   * @param icaoOd Icao od
   * @param icaoDo Icao do
   * @return Response - odgovor REST servisa, vraća udaljenost između aerodroma
   */
  @Path("{icaoOd}/izracunaj/{icaoDo}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response izracunajUdaljenosti(@PathParam("icaoOd") String icaoOd,
      @PathParam("icaoDo") String icaoDo) {
    String odgovorApp1 = null;
    ArrayList<Lokacija> lokacije = new ArrayList<Lokacija>();
    lokacije = dohvatiLokacijeIzBaze(icaoOd, icaoDo);
    String poruka =
        "UDALJENOST " + lokacije.get(0).getLatitude() + " " + lokacije.get(0).getLongitude() + " "
            + lokacije.get(1).getLatitude() + " " + lokacije.get(1).getLongitude();
    odgovorApp1 = this.pomocnik.spojiSeNaPosluzitelj(poruka.toString());
    var gson = new Gson();
    var jsonOdgovor = gson.toJson(odgovorApp1.substring(3));
    var odgovor = Response.ok().entity(jsonOdgovor).build();
    return odgovor;
  }

  /**
   * Vraća aerodrome s udaljenostima do odredišnog aerodroma kojima je udaljenost manja od
   * udaljenosti polazišnog i odredišnog aerodroma.
   *
   * @param icaoOd Icao od
   * @param icaoDo Icao do
   * @return Response - odgovor REST servisa, vraća aerodrome
   */
  @Path("{icaoOd}/udaljenost1/{icaoDo}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajUdaljnost1(@PathParam("icaoOd") String icaoOd,
      @PathParam("icaoDo") String icaoDo) {
    String odgovorApp1 = null;
    ArrayList<Lokacija> lokacije = new ArrayList<Lokacija>();
    lokacije = dohvatiLokacijeIzBaze(icaoOd, icaoDo);
    String poruka =
        "UDALJENOST " + lokacije.get(0).getLatitude() + " " + lokacije.get(0).getLongitude() + " "
            + lokacije.get(1).getLatitude() + " " + lokacije.get(1).getLongitude();
    odgovorApp1 = this.pomocnik.spojiSeNaPosluzitelj(poruka.toString());

    ArrayList<UdaljenostiIzmeduAerodroma> aerodromi = new ArrayList<UdaljenostiIzmeduAerodroma>();
    aerodromi = dohvatiAerodromeIzDrzave(icaoDo, lokacije.get(1).getLatitude(),
        lokacije.get(1).getLongitude(), odgovorApp1.substring(3));
    var gson = new Gson();
    var aerodromiJson = gson.toJson(aerodromi);
    var odgovor = Response.ok().entity(aerodromiJson).build();
    return odgovor;
  }

  /**
   * Vraća aerodrome s udaljenostima do odredišnog aerodroma kojima je udaljenost manja od parametra
   * km, koji se nalaze u određenoj državi
   * 
   * @param icaoOd Icao od
   * @param drzava Drzava
   * @param km Km
   * @return Response - odgovor REST servisa, vraća aerodrome
   */
  @Path("{icaoOd}/udaljenost2")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajUdaljnost2(@PathParam("icaoOd") String icaoOd,
      @QueryParam("drzava") String drzava, @QueryParam("km") String km) {
    if (drzava == null || km == null) {
      return Response.status(Status.NOT_ACCEPTABLE).entity("Nisu upisani drzava i kilometri")
          .build();
    }
    Aerodrom aerodrom = obradiAerodrom(icaoOd);
    ArrayList<UdaljenostiIzmeduAerodroma> aerodromi = new ArrayList<UdaljenostiIzmeduAerodroma>();
    aerodromi = dohvatiAerodromeIzDrzave(icaoOd, aerodrom.getLokacija().getLatitude(),
        aerodrom.getLokacija().getLongitude(), km);
    var gson = new Gson();
    var aerodromiJson = gson.toJson(aerodromi);
    var odgovor = Response.ok().entity(aerodromiJson).build();
    return odgovor;
  }

  /**
   * Obradi aerodrom. Pretražuje bazu za određeni aerodrom, kreira novu aerodrom instancu i
   * popunjuje je
   *
   * @param icao Icao
   * @return Aerodrom
   */
  private Aerodrom obradiAerodrom(String icao) {
    Aerodrom aerodrom = null;
    PreparedStatement pstmt = null;
    String query = "select * from AIRPORTS WHERE ICAO = ?";

    try (var con = ds.getConnection()) {
      pstmt = con.prepareStatement(query);
      pstmt.setString(1, icao);
      ResultSet rs = pstmt.executeQuery();

      while (rs.next()) {
        String naziv = rs.getString("NAME");
        String drzava = rs.getString("ISO_COUNTRY");
        String lok = rs.getString("COORDINATES");
        String[] koordinate = lok.split(", ");
        Lokacija lokacija = new Lokacija(koordinate[0], koordinate[1]);
        aerodrom = new Aerodrom(icao, naziv, drzava, lokacija);
      }

      rs.close();
      pstmt.close();
      con.close();
    } catch (Exception e) {
      Logger.getGlobal().log(Level.INFO, e.toString());
    } finally {
      try {
        if (pstmt != null && pstmt.isClosed()) {
          pstmt.close();
        }
      } catch (SQLException e) {
        Logger.getGlobal().log(Level.INFO, e.toString());
      }
    }
    return aerodrom;
  }

  /**
   * Dohvaća lokacije iz baze za polazišni i odredišni aerodrom
   *
   * @param icaoOd Icao od
   * @param icaoDo Icao do
   * @return ArrayList<Lokacija> - lokacije
   */
  public ArrayList<Lokacija> dohvatiLokacijeIzBaze(String icaoOd, String icaoDo) {
    PreparedStatement pstmt = null;
    String query = "SELECT COORDINATES FROM AIRPORTS WHERE ICAO = ? OR ICAO = ?";
    ArrayList<Lokacija> lokacije = new ArrayList<Lokacija>();
    String odgovorApp1 = null;
    try (var con = ds.getConnection()) {
      pstmt = con.prepareStatement(query);
      pstmt.setString(1, icaoOd);
      pstmt.setString(2, icaoDo);
      ResultSet rs = pstmt.executeQuery();

      while (rs.next()) {
        String koordinate = rs.getString("COORDINATES");
        String[] koordinata = koordinate.split(",");
        Lokacija lokacija = new Lokacija(koordinata[0].trim(), koordinata[1].trim());
        lokacije.add(lokacija);
      }
      rs.close();
      pstmt.close();
      con.close();

      String poruka =
          "UDALJENOST " + lokacije.get(0).getLatitude() + " " + lokacije.get(0).getLongitude() + " "
              + lokacije.get(1).getLatitude() + " " + lokacije.get(1).getLongitude();
      odgovorApp1 = this.pomocnik.spojiSeNaPosluzitelj(poruka.toString());
    } catch (Exception e) {
      Logger.getGlobal().log(Level.INFO, e.toString());
    } finally {
      try {
        if (pstmt != null && pstmt.isClosed()) {
          pstmt.close();
        }
      } catch (SQLException e) {
        Logger.getGlobal().log(Level.INFO, e.toString());
      }
    }
    return lokacije;
  }

  /**
   * Dohvaća aerodrome s udaljenostima od određenog aerodroma. Koristi se kod dajUdaljenost1 i 2
   *
   * @param icaoDo Icao do
   * @param lat Lat
   * @param lon Lon
   * @param duzina Duzina
   * @return ArrayList<UdaljenostiIzmeduAerodroma>
   */
  private ArrayList<UdaljenostiIzmeduAerodroma> dohvatiAerodromeIzDrzave(String icaoDo, String lat,
      String lon, String duzina) {
    PreparedStatement pstmt = null;
    double duzinaDouble = Double.parseDouble(duzina);
    String query =
        "SELECT ICAO, NAME, ISO_COUNTRY, COORDINATES FROM AIRPORTS WHERE ISO_COUNTRY = (SELECT ISO_COUNTRY FROM AIRPORTS WHERE ICAO = ?)";
    ArrayList<UdaljenostiIzmeduAerodroma> aerodromi = new ArrayList<UdaljenostiIzmeduAerodroma>();
    String odgovorApp1 = null;
    try (var con = ds.getConnection()) {
      pstmt = con.prepareStatement(query);
      pstmt.setString(1, icaoDo);
      ResultSet rs = pstmt.executeQuery();
      con.close();
      while (rs.next()) {
        String icao = rs.getString("ICAO");
        String naziv = rs.getString("NAME");
        String drzava = rs.getString("ISO_COUNTRY");
        String koordinate = rs.getString("COORDINATES");
        String[] koordinata = koordinate.split(",");
        String poruka = "UDALJENOST " + lat + " " + lon + " " + koordinata[0].trim() + " "
            + koordinata[1].trim();
        odgovorApp1 = this.pomocnik.spojiSeNaPosluzitelj(poruka.toString());
        if (duzinaDouble > Double.parseDouble(odgovorApp1.substring(3))) {
          UdaljenostiIzmeduAerodroma aerodrom =
              new UdaljenostiIzmeduAerodroma(icao, naziv, drzava, odgovorApp1.substring(3));
          aerodromi.add(aerodrom);
        }
      }
      rs.close();
      pstmt.close();
    } catch (Exception e) {
      Logger.getGlobal().log(Level.INFO, e.toString());
    } finally {
      try {
        if (pstmt != null && pstmt.isClosed()) {
          pstmt.close();
        }
      } catch (SQLException e) {
        Logger.getGlobal().log(Level.INFO, e.toString());
      }
    }
    return aerodromi;
  }
}
