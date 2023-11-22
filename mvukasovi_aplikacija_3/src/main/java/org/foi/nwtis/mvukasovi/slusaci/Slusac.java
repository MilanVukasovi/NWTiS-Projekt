package org.foi.nwtis.mvukasovi.slusaci;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.foi.nwtis.mvukasovi.web.SakupljacLetovaAviona;
import org.foi.nwtis.mvukasovi.zrna.JmsPosiljatelj;
import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

// TODO: Auto-generated Javadoc
/**
 * Klasa Slušač.
 */
public class Slusac implements ServletContextListener {

  /** Kontekst. */
  private static ServletContext kontekst;

  /** JMS Posiljatelj */
  @Inject
  JmsPosiljatelj jmsPosiljatelj;

  /** Datasource */
  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  /** Dretva SakupljacLetovaAviona */
  SakupljacLetovaAviona dretva = null;

  /**
   * Inicijalizira kontekst.
   *
   * @param dogadaj Servlet kontekst događaj
   */
  @Override
  public void contextInitialized(ServletContextEvent dogadaj) {
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
    dretva = new SakupljacLetovaAviona(jmsPosiljatelj, ds);
    dretva.start();
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
