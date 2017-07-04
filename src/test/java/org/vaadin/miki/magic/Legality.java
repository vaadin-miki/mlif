package org.vaadin.miki.magic;

import java.util.Objects;

/**
 * Stores legality information.
 */
public class Legality {

    private String format;
    private String legality;

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getLegality() {
        return legality;
    }

    public void setLegality(String legality) {
        this.legality = legality;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Legality legality1 = (Legality) o;
        return Objects.equals(getFormat(), legality1.getFormat()) &&
                Objects.equals(getLegality(), legality1.getLegality());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFormat(), getLegality());
    }
}
