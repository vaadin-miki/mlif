package org.vaadin.miki.magic;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Card database.
 */
public class Database {

    private Map<String, Card> cards;

    /**
     * Initialise database with given file.
     * @param file File.
     * @throws IOException when something goes wrong.
     */
    public Database(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true);
        MapType type = mapper.getTypeFactory().constructMapType(HashMap.class, String.class, Card.class);
        this.cards = mapper.readValue(file, type);
    }

    /**
     * Returns a card with given name.
     * @param name Name of card.
     * @return A {@link Card}, or {@code null} when card with given name is not found.
     */
    public Card getCard(String name) {
        return this.cards.get(name);
    }

    /**
     * Returns ALL the cards.
     * @return ALL the cards.
     */
    public Collection<Card> getCards() {
        return this.cards.values();
    }

    /**
     * Counts the cards.
     * @return Number of cards.
     */
    public int getSize() {
        return this.cards.size();
    }
}
