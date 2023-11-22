package org.foi.nwtis.mvukasovi.zrna;

import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.inject.Inject;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;

/**
 * Klasa JmsPrimatelj.
 */
@MessageDriven(mappedName = "jms/NWTiS_mvukasovi",
    activationConfig = {
        @ActivationConfigProperty(propertyName = "acknowledgeMode",
            propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "destinationType",
            propertyValue = "jakarta.jms.Queue")})
public class JmsPrimatelj implements MessageListener {

  /** Sakupljač JMS poruka */
  @Inject
  SakupljacJmsPoruka sakupljacJmsPoruka;

  /**
   * Postavlja poruku u sakupljača poruka.
   *
   * @param message Poruka
   */
  @Override
  public void onMessage(Message message) {
    if (message instanceof TextMessage) {
      try {
        var msg = (TextMessage) message;
        sakupljacJmsPoruka.postaviPoruku(msg.getText());
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }
}
