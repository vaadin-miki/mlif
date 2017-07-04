package org.vaadin.miki;

import org.junit.Before;
import org.junit.Test;
import org.vaadin.miki.magic.Card;
import org.vaadin.miki.magic.Database;
import org.vaadin.miki.magic.Type;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Unit test for simple DataFeedingApp.
 */
public class JsonSerialisationTest {

    private Database database;

    @Before
    public void setup() throws IOException {
        database = new Database(new File(this.getClass().getClassLoader().getResource("AllCards-x.json").getFile()));
    }

    @Test
    public void testCardsRead() {
        // number of cards will change if you update AllCards-x.json
        assertEquals(17232, this.database.getSize());
        // check one particular card
        Card glimpse = this.database.getCard("Glimpse the Unthinkable");
        assertEquals("Glimpse the Unthinkable", glimpse.getName());
        assertEquals(2, glimpse.getCmc());
        assertArrayEquals(new Object[]{Type.SORCERY}, glimpse.getTypes().toArray());
    }

}
