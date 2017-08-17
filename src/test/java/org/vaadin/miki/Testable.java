package org.vaadin.miki;

import com.marklogic.client.pojo.annotation.Id;

import java.util.Objects;

/**
 * Testable pojo.
 */
public class Testable {

    private String name;
    private int number;

    public Testable() {
    }

    public Testable(String name, int number) {
        this.name = name;
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Testable testable = (Testable) o;
        return getNumber() == testable.getNumber() &&
                Objects.equals(getName(), testable.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getNumber());
    }

    @Id
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
