package org.foi.nwtis.podaci;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor()
public class AerodromStatusPreuzimanja {
  @Getter
  @Setter
  private Aerodrom aerodrom;

  @Getter
  @Setter
  private boolean status;

  public AerodromStatusPreuzimanja() {}
}
