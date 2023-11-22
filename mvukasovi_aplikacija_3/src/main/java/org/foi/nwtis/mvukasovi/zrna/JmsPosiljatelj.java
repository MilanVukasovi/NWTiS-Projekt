package org.foi.nwtis.mvukasovi.zrna;

import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import jakarta.jms.MessageProducer;
import jakarta.jms.Queue;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;

/**
 * Klasa JmsPosiljatelj.
 */
@Stateless
public class JmsPosiljatelj {

  /** Brojač poruka. */
  static int brojacPoruka = 0;

  /** Tvornica konekcija. */
  @Resource(mappedName = "jms/nwtis_qf_dz3")
  private ConnectionFactory connectionFactory;

  /** Red. */
  @Resource(mappedName = "jms/NWTiS_mvukasovi")
  private Queue queue;

  /**
   * šalji poruku. Šalje poruku klijentu.
   *
   * @param tekstPoruke Tekst poruke
   * @return true, ako je uspješno, inače false
   */
  public boolean saljiPoruku(String tekstPoruke) {
    boolean status = true;

    try {
      Connection connection = connectionFactory.createConnection();
      Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
      MessageProducer messageProducer = session.createProducer(queue);
      TextMessage message = session.createTextMessage();
      String poruka = tekstPoruke;
      System.out.println("Šaljem poruku: " + poruka);
      message.setText(poruka);
      messageProducer.send(message);
      messageProducer.close();
      connection.close();
    } catch (JMSException ex) {
      ex.printStackTrace();
      status = false;
    }
    return status;
  }
}
