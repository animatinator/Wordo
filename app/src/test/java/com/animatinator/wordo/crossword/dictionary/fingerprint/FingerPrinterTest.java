package com.animatinator.wordo.crossword.dictionary.fingerprint;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@Config(manifest=Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class FingerPrinterTest {
    @Test
    public void emptyFingerPrint() {
        assertEquals("", FingerPrinter.getFingerprint("").toString());
    }

    @Test
    public void emptyFingerPrint_noCharacters() {
        assertEquals(0, FingerPrinter.getFingerprint("").getCharacters().length);
    }

    @Test
    public void simpleFingerPrint() {
        assertEquals("estt", FingerPrinter.getFingerprint("test").toString());
    }

    /**
     * Check that there's a sensible ordering.
     */
    @Test
    public void spanishWords() {
        assertEquals("aeinnrtté", FingerPrinter.getFingerprint("intentaré").toString());
        assertEquals("cmoí", FingerPrinter.getFingerprint("comí").toString());
        assertEquals("inoñ", FingerPrinter.getFingerprint("niño").toString());
        assertEquals("éíñ", FingerPrinter.getFingerprint("ñéí").toString());
    }

    @Test
    public void nonMatch() {
        assertNotEquals(FingerPrinter.getFingerprint("hello"), FingerPrinter.getFingerprint("hallo"));
    }

    @Test
    public void equalFingerPrints() {
        assertEquals(FingerPrinter.getFingerprint("cause"), FingerPrinter.getFingerprint("sauce"));
    }
}
