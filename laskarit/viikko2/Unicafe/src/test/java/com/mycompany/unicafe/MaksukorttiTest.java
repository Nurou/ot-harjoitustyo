package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MaksukorttiTest {

    Maksukortti kortti;

    @Before
    public void setUp() {
        kortti = new Maksukortti(10);
    }

    @Test
    public void luotuKorttiOlemassa() {
        assertTrue(kortti != null);
    }

    @Test
    public void kortinSaldoAlussaOikein() {
        assertEquals(10, kortti.saldo());

    }

    @Test
    public void saldoTulostuuAlussaOikein() {
        assertEquals("saldo: 0.10", kortti.toString());
    }

    @Test
    public void rahanLataaminenKasvattaaSaldoaOikein() {
        kortti.lataaRahaa(100);
        assertEquals(110, kortti.saldo());
    }

    @Test
    public void saldoVaheneeOikeinjosRahaaOnTarpeeksi() {
        kortti.lataaRahaa(100);
        kortti.otaRahaa(100);
        assertEquals(10, kortti.saldo());
    }

    @Test
    public void saldoEiMuutuJosRahaaEiOleTarpeeksi() {
        kortti.otaRahaa(100);
        assertEquals(10, kortti.saldo());
    }

    @Test
    public void metodiPalauttaaTrueKunRahatRiittaaMuutenFalse() {
        assertEquals(true, kortti.otaRahaa(10));
        assertEquals(false, kortti.otaRahaa(100));
    }

}
