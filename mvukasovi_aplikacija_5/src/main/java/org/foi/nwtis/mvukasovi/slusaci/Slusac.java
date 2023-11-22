package org.foi.nwtis.mvukasovi.slusaci;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

/**
 * Klasa Slušač.
 */
public class Slusac implements ServletContextListener {

  /** Kontekst. */
  private static ServletContext kontekst;

  /**
   * Inicijalizira kontekst.
   *
   * @param dogadaj Servlet kontekst događaj
   */
  @Override
  public void contextInitialized(ServletContextEvent dogadaj) {
    System.out.println("Slušač pokrenut!");

    kontekst = dogadaj.getServletContext();
    Properties konfiguracija = new Properties();
    String konfig = kontekst.getInitParameter("konfiguracija");
    String putanja = kontekst.getRealPath("/WEB-INF") + java.io.File.separator;
    try {
      FileInputStream fis = new FileInputStream(putanja + konfig);
      konfiguracija.loadFromXML(fis);
      fis.close();
    } catch (IOException e) {
    }
    kontekst.setAttribute("konfig", konfiguracija);
  }

  /**
   * Daj kontekst. Vraća kontekst.
   *
   * @return ServletContext
   */
  public static ServletContext dajKontekst() {
    return kontekst;
  }
}
