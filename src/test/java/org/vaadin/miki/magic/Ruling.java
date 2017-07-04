package org.vaadin.miki.magic;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Pojo for card rulings.
 */
public class Ruling {

    private LocalDate date;
    private String text;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ruling ruling = (Ruling) o;
        return Objects.equals(getDate(), ruling.getDate()) &&
                Objects.equals(getText(), ruling.getText());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDate(), getText());
    }

    @Override
    public String toString() {
        return String.format("%s - %s", this.getDate().format(DateTimeFormatter.ISO_DATE), this.getText());
    }
}
