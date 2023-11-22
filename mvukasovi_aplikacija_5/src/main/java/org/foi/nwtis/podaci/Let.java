package org.foi.nwtis.podaci;

import org.foi.nwtis.rest.podaci.LetAviona;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor()
public class Let {
  @Getter
  @Setter
  private int id;
  @Getter
  @Setter
  private LetAviona let;

  public Let() {}
}
