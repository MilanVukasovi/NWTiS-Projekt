package org.foi.nwtis.mvukasovi;

import java.io.Serializable;

public record Udaljenost(double gpsSirina1, double gpsDuzina1, double gpsSirina2, double gpsDuzina2,
    double udaljenost) implements Serializable {

}
