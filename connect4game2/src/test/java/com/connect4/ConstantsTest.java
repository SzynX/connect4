package com.connect4;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException; // Importálás szükséges

public class ConstantsTest {

    @Test
    public void testDefaultRows() {
        assertEquals(6, Constants.DEFAULT_ROWS, "Az alapértelmezett sorok számának 6-nak kell lennie.");
    }

    @Test
    public void testDefaultColumns() {
        assertEquals(7, Constants.DEFAULT_COLUMNS, "Az alapértelmezett oszlopok számának 7-nek kell lennie.");
    }

    @Test
    public void testPrivateConstructor() {
        // Kísérlet a privát konstruktor meghívására
        Throwable exception = assertThrows(
                InvocationTargetException.class, // Várjuk az InvocationTargetException-t
                () -> {
                    // A konstruktor hívása a Reflection API segítségével
                    java.lang.reflect.Constructor<Constants> constructor = Constants.class.getDeclaredConstructor();
                    constructor.setAccessible(true); // Engedélyezzük a privát konstruktor elérését
                    constructor.newInstance(); // Példányosítjuk a Constants osztályt
                }
        );

        // Ellenőrizzük, hogy az eredeti kivétel a várt UnsupportedOperationException
        assertTrue(exception.getCause() instanceof UnsupportedOperationException);
        assertEquals("Ez az osztály nem hozható létre.", exception.getCause().getMessage());
    }
}
