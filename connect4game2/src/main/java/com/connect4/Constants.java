package com.connect4;

/**
 * A Constants osztály tárolja a játék konfigurációs állandóit.
 */
public final class Constants {
    /** Az alapértelmezett sorok száma. */
    public static final int DEFAULT_ROWS = 6;

    /** Az alapértelmezett oszlopok száma. */
    public static final int DEFAULT_COLUMNS = 7;

    // Privát konstruktor, hogy megakadályozzuk az objektumok létrehozását.
    private Constants() {
        throw new UnsupportedOperationException(
                "Ez az osztály nem hozható létre."
        );
    }
}
