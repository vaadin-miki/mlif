package org.vaadin.miki.magic;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Magic card type.
 */
public enum Type {
    ARTIFACT,
    CREATURE,
    ENCHANTMENT,
    INSTANT,
    LAND,
    PLANESWALKER,
    SORCERY,
    TRIBAL,
    VANGUARD(false),
    ENCHANT(false),
    PLAYER(false),
    SCARIEST(false),
    @JsonProperty("You'll") YOULL(false),
    EVER(false),
    SEE(false),
    EATURECRAY(false),
    PLANE(false),
    SCHEME(false),
    PHENOMENON(false),
    CONSPIRACY(false)
    ;

    private boolean tournament;

    Type() {
        this(true);
    }

    Type(boolean legal) {
        this.tournament = legal;
    }

    public boolean isTournamentLegal() {
        return this.tournament;
    }
}
