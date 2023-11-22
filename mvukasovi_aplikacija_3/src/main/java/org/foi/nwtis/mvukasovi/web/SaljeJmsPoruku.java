package org.foi.nwtis.mvukasovi.web;

import java.io.IOException;
import org.foi.nwtis.mvukasovi.zrna.JmsPosiljatelj;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Klasa SaljeJmsPoruku.
 */
@WebServlet(name = "SaljeJmsPoruku", urlPatterns = {"/SaljeJmsPoruku"})
public class SaljeJmsPoruku extends HttpServlet {

  /** SerialVersionUID. */
  private static final long serialVersionUID = -7835209271050609113L;

  /** JmsPosiljatelj. */
  @Inject
  JmsPosiljatelj jmsPosiljatelj;

  /**
   * Šalje poruku
   *
   * @param req Zahtjev
   * @param resp Odgovor
   * @throws ServletException Servlet iznimka
   * @throws IOException Signalizira iznimku ulaz/izlaz.
   */
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    try {
      String poruka = req.getParameter("poruka");
      if (poruka != null && poruka.trim().length() > 0) {
        jmsPosiljatelj.saljiPoruku(poruka);
        System.out.println("Poruka je poslana");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
