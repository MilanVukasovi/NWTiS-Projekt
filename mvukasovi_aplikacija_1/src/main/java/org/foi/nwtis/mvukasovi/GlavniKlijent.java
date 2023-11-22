package org.foi.nwtis.mvukasovi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GlavniKlijent {

  public static void main(String[] args) {
    var glavniKlijent = new GlavniKlijent();
    String unos = String.join(" ", args);
    glavniKlijent.spojiSeNaPosluzitelj("localhost", 8000, unos, 0);
  }

  public void spojiSeNaPosluzitelj(String adresa, int mreznaVrata, String zahtjev, int cekanje) {
    try {
      var mreznaUticnica = new Socket(adresa, mreznaVrata);
      var citac = new BufferedReader(
          new InputStreamReader(mreznaUticnica.getInputStream(), Charset.forName("UTF-8")));
      var pisac = new BufferedWriter(
          new OutputStreamWriter(mreznaUticnica.getOutputStream(), Charset.forName("UTF-8")));
      Logger.getLogger(GlavniKlijent.class.getName()).log(Level.INFO, zahtjev.toString());

      pisac.write(zahtjev);
      pisac.flush();
      mreznaUticnica.shutdownOutput();
      var poruka = new StringBuilder();
      while (true) {
        var redak = citac.readLine();
        if (redak == null) {
          break;
        }
        Logger.getGlobal().log(Level.INFO, redak);
        poruka.append(redak);
      }
      mreznaUticnica.shutdownInput();
      mreznaUticnica.close();

    } catch (IOException e) {
      Logger.getGlobal().log(Level.SEVERE, "ERROR 29: " + e.getMessage());
    }
  }
}
