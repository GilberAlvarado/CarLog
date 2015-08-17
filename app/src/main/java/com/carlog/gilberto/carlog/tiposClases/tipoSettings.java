package com.carlog.gilberto.carlog.tiposClases;

import java.io.Serializable;

/**
 * Created by Gilberto on 13/08/2015.
 */
public class tipoSettings implements Serializable {
    public final static int ACTIVO = 1;
    public final static int INACTIVO = 0;

    private int notificaciones;
    private int aceite;
    private int amortiguadores;
    private int anticongelante;
    private int bateria;
    private int bombaagua;
    private int bujias;
    private int correa;
    private int embrague;
    private int filaceite;
    private int filaire;
    private int filgasolina;
    private int frenos;
    private int itv;
    private int limpiaparabrisas;
    private int liquidofrenos;
    private int luces;
    private int revgen;
    private int ruedas;

    public tipoSettings(int notificaciones, int aceite, int amortiguadores, int anticongelante, int bateria, int bombaagua, int bujias, int correa, int embrague
                        ,int filaceite, int filaire, int filgasolina, int frenos, int itv, int limpiaparabrisas, int liquidofrenos, int luces, int revgen, int ruedas) {
        this.notificaciones = notificaciones;
        this.aceite = aceite;
        this.amortiguadores = amortiguadores;
        this.anticongelante = anticongelante;
        this.bateria = bateria;
        this.bombaagua = bombaagua;
        this.bujias = bujias;
        this.correa = correa;
        this.embrague = embrague;
        this.filaceite = filaceite;
        this.filaire = filaire;
        this.filgasolina = filgasolina;
        this.frenos = frenos;
        this.itv = itv;
        this.limpiaparabrisas = limpiaparabrisas;
        this.liquidofrenos = liquidofrenos;
        this.luces = luces;
        this.revgen = revgen;
        this.ruedas = ruedas;
    }

    public int getNotificaciones(tipoSettings miSettings) {
        return(miSettings.notificaciones);
    }
    public int getAceite(tipoSettings miSettings) {
        return(miSettings.aceite);
    }
    public int getAmortiguadores(tipoSettings miSettings) {
        return(miSettings.amortiguadores);
    }
    public int getAnticongelante(tipoSettings miSettings) {
        return(miSettings.anticongelante);
    }
    public int getBateria(tipoSettings miSettings) {
        return(miSettings.bateria);
    }
    public int getBombaagua(tipoSettings miSettings) {
        return(miSettings.bombaagua);
    }
    public int getBujias(tipoSettings miSettings) {
        return(miSettings.bujias);
    }
    public int getCorrea(tipoSettings miSettings) {
        return(miSettings.correa);
    }
    public int getEmbrague(tipoSettings miSettings) { return(miSettings.embrague); }
    public int getFilaceite(tipoSettings miSettings) { return(miSettings.filaceite); }
    public int getFilaire(tipoSettings miSettings) {
        return(miSettings.filaire);
    }
    public int getFilgasolina(tipoSettings miSettings) {
        return(miSettings.filgasolina);
    }
    public int getFrenos(tipoSettings miSettings) {
        return(miSettings.frenos);
    }
    public int getItv(tipoSettings miSettings) {
        return(miSettings.itv);
    }
    public int getLimpiaparabrisas(tipoSettings miSettings) {
        return(miSettings.limpiaparabrisas);
    }
    public int getLiquidofrenos(tipoSettings miSettings) {
        return(miSettings.liquidofrenos);
    }
    public int getLuces(tipoSettings miSettings) {
        return(miSettings.luces);
    }
    public int getRevgen(tipoSettings miSettings) {
        return(miSettings.revgen);
    }
    public int getRuedas(tipoSettings miSettings) {
        return(miSettings.ruedas);
    }
}