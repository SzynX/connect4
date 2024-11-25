package com.connect4;

/**
 * A Player osztály a Connect4 játékosokat reprezentálja.
 */
public class Player { // A 'final' kulcsszó eltávolítva
    /** A játékos neve. */
    private String playerName;

    /** A játékos tokenje ('X' vagy 'O'). */
    private final char playerToken;

    /** A hashCode számításához használt szorzó. */
    private static final int HASH_MULTIPLIER = 31;

    /**
     * Konstruktor a Player osztályhoz.
     *
     * @param name  A játékos neve.
     * @param token A játékos tokenje.
     */
    public Player(final String name, final char token) {
        this.playerName = name;
        this.playerToken = token;
    }

    /**
     * Visszaadja a játékos nevét.
     *
     * @return A játékos neve.
     */
    public String getName() {
        return playerName;
    }

    /**
     * Beállítja a játékos nevét.
     *
     * @param name Az új játékos neve.
     */
    public void setName(final String name) {
        this.playerName = name;
    }

    /**
     * Visszaadja a játékos tokenjét.
     *
     * @return A játékos tokenje.
     */
    public char getToken() {
        return playerToken;
    }

    /**
     * Ellenőrzi, hogy két Player objektum egyenlő-e.
     *
     * @param o Az összehasonlítandó objektum.
     * @return True, ha egyenlő, különben false.
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Player)) {
            return false;
        }
        Player player = (Player) o;

        boolean tokensEqual = playerToken == player.playerToken;
        boolean namesEqual = playerName.equals(player.playerName);

        return tokensEqual && namesEqual;
    }

    /**
     * Visszaadja a Player objektum hash kódját.
     *
     * @return A Player hash kódja.
     */
    @Override
    public int hashCode() {
        int result = playerName.hashCode();
        result = HASH_MULTIPLIER * result + (int) playerToken;
        return result;
    }
}
