package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author joelhassan
 */
public class KassapaateTest {

    Kassapaate kassapaate;
    Maksukortti maksukortti;

    @Before
    public void setUp() {
        kassapaate = new Kassapaate();
        maksukortti = new Maksukortti(10);
    }

    @Test
    public void luotuKassaPaateOnOlemassa() {
        assertTrue(kassapaate != null);
    }

    @Test
    public void kassarahaOnOikeaAlussa() {
        assertEquals(100000, kassapaate.kassassaRahaa());
    }

    @Test
    public void myytyjenLounaidenMaaraOnOikeaAlussa() {
        assertEquals(0, kassapaate.edullisiaLounaitaMyyty());
        assertEquals(0, kassapaate.maukkaitaLounaitaMyyty());
    }

    @Test
    public void rahanLataaminenKortilleKasvattaaMaaraaOikein() {
        kassapaate.lataaRahaaKortille(maksukortti, 1);
        assertEquals(11, maksukortti.saldo());
    }

    @Test
    public void negatiivisenMaaranLataaminenEpaOnnistuu() {
        kassapaate.lataaRahaaKortille(maksukortti, -1);
        assertEquals(100000, kassapaate.kassassaRahaa());
    }

    @Test
    public void kateisostoToimiiSekaEdullistenEttaMaukkaisenLounaidenOsalta() {
        kassapaate.syoEdullisesti(240);
        assertEquals(100240, kassapaate.kassassaRahaa());
        assertEquals(1, kassapaate.edullisiaLounaitaMyyty());
        assertEquals(239, kassapaate.syoEdullisesti(239));
        assertEquals(1, kassapaate.edullisiaLounaitaMyyty());

        kassapaate.syoMaukkaasti(400);
        assertEquals(100640, kassapaate.kassassaRahaa());
        assertEquals(1, kassapaate.maukkaitaLounaitaMyyty());
        assertEquals(399, kassapaate.syoMaukkaasti(399));
        assertEquals(1, kassapaate.maukkaitaLounaitaMyyty());

    }

    @Test
    public void syoEdullisestiOnnistuuKortillaJosRahaaRittaa() {
        maksukortti.lataaRahaa(230);
        assertEquals(true, kassapaate.syoEdullisesti(maksukortti));
        assertEquals(false, kassapaate.syoEdullisesti(maksukortti));

    }

    @Test
    public void korttiostoToimiiSekaEdullistenEttaMaukkaidenLounaidenOsalta() {
        maksukortti.lataaRahaa(390);
        // jos kortilla on tarpeeksi rahaa, veloitetaan summa kortilta ja palautetaan true
        assertEquals(true, kassapaate.syoMaukkaasti(maksukortti));
        //jos kortilla on tarpeeksi rahaa, myytyjen lounaiden määrä kasvaa
        assertEquals(1, kassapaate.maukkaitaLounaitaMyyty());
        // jos kortilla ei ole tarpeeksi rahaa, kortin rahamäärä ei muutu, myytyjen lounaiden määrä muuttumaton ja palautetaan false
        assertEquals(false, kassapaate.syoMaukkaasti(maksukortti));
        assertEquals(1, kassapaate.maukkaitaLounaitaMyyty());
        // kassassa oleva rahamäärä ei muutu kortilla ostettaessa
        assertEquals(100000, kassapaate.kassassaRahaa());

        // syoEdullisesti
        maksukortti.lataaRahaa(240);
        assertEquals(true, kassapaate.syoEdullisesti(maksukortti));
        assertEquals(1, kassapaate.edullisiaLounaitaMyyty());
        assertEquals(false, kassapaate.syoEdullisesti(maksukortti));
        assertEquals(100000, kassapaate.kassassaRahaa());

    }

    @Test
    public void syoMaukkaastiOnnistuuKortillaJosRahaaRittaa() {
        maksukortti.lataaRahaa(390);
        assertEquals(true, kassapaate.syoMaukkaasti(maksukortti));
        assertEquals(false, kassapaate.syoMaukkaasti(maksukortti));

    }
}
