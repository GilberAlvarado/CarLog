package com.carlog.gilberto.carlog.tiposClases;

import android.app.Activity;

import com.carlog.gilberto.carlog.R;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Gilberto on 22/11/2014.
 */
public class tipoLog implements Serializable {
    private String tipo;
    private Date fecha;
    private String datetxt;
    private long long_fecha;
    private int aceite;
    private int veces_fil_aceite;
    private int contador_fil_aceite;
    private int revgral;
    private int correa;
    private int bombaagua;
    private int fgasolina;
    private int faire;
    private int bujias;
    private int embrague;
    private String matricula;
    private int realizado;
    private int fmodificada;
    private int kms;


    public static Boolean es_tipo_personalizado(String tipo, Activity act) {

        if((!tipo.equals(act.getString(R.string.tipoAceite))) && (!tipo.equals(act.getString(R.string.tipoRevGen))) && (!tipo.equals(act.getString(R.string.tipoCorrea))) && (!tipo.equals(act.getString(R.string.tipoBomba))) &&
                (!tipo.equals(act.getString(R.string.tipoFiltroAceite))) && (!tipo.equals(act.getString(R.string.tipoFiltroGasolina))) && (!tipo.equals(act.getString(R.string.tipoItv))) && (!tipo.equals(act.getString(R.string.tipoBujias))) &&
                (!tipo.equals(act.getString(R.string.tipoFiltroAire))) && (!tipo.equals(act.getString(R.string.tipoFrenos))) && (!tipo.equals(act.getString(R.string.tipoLiqFrenos))) && (!tipo.equals(act.getString(R.string.tipoLimpiaparabrisas))) &&
                (!tipo.equals(act.getString(R.string.tipoLuces))) && (!tipo.equals(act.getString(R.string.tipoRuedas))) && (!tipo.equals(act.getString(R.string.tipoEmbrague))) && (!tipo.equals(act.getString(R.string.tipoTaller)))) {
            return true;
        }
        else return false;
    }


    public tipoLog(String tipo, Date fecha, String datetxt, long long_fecha, int aceite, int veces_fil_aceite, int contador_fil_aceite, int revgral,
                   int correa, int bombaagua, int fgasolina, int faire, int bujias, int embrague, String matricula, int realizado, int fmodificada, int kms) {
        this.tipo = tipo;
        this.fecha = fecha;
        this.datetxt = datetxt;
        this.long_fecha = long_fecha;
        this.aceite = aceite;
        this.veces_fil_aceite = veces_fil_aceite;
        this.contador_fil_aceite = contador_fil_aceite;
        this.revgral = revgral;
        this.correa = correa;
        this.bombaagua = bombaagua;
        this.faire = faire;
        this.fgasolina = fgasolina;
        this.bujias = bujias;
        this.embrague = embrague;
        this.matricula = matricula;
        this.realizado = realizado;
        this.fmodificada = fmodificada;
        this.kms = kms;
    }

    public String getTipo(tipoLog miTipo) {
        return(miTipo.tipo);
    }
    public Date getFecha(tipoLog miTipo) {
        return(miTipo.fecha);
    }
    public String getFechatxt(tipoLog miTipo) {
        return(miTipo.datetxt);
    }
    public long getFechalong(tipoLog miTipo) {
        return(miTipo.long_fecha);
    }
    public String getCoche(tipoLog miTipo) {
        return(miTipo.matricula);
    }
    public int getAceite(tipoLog miTipo) {
        return(miTipo.aceite);
    }
    public int getVecesFilAceite(tipoLog miTipo) {
        return(miTipo.veces_fil_aceite);
    }
    public int getContadorFilAceite(tipoLog miTipo) {
        return(miTipo.contador_fil_aceite);
    }
    public int getRevgral(tipoLog miTipo) {
        return(miTipo.revgral);
    }
    public int getCorrea(tipoLog miTipo) {
        return(miTipo.correa);
    }
    public int getBombaagua(tipoLog miTipo) {
        return(miTipo.bombaagua);
    }
    public int getFgasolina(tipoLog miTipo) {
        return(miTipo.fgasolina);
    }
    public int getFaire(tipoLog miTipo) {
        return(miTipo.faire);
    }
    public int getBujias(tipoLog miTipo) {
        return(miTipo.bujias);
    }
    public int getEmbrague(tipoLog miTipo) {
        return(miTipo.embrague);
    }
    public int getKms(tipoLog miTipo) {
        return(miTipo.kms);
    }
    public int getRealizado(tipoLog miTipo) {
        return(miTipo.realizado);
    }
    public int getFmodificada(tipoLog miTipo) {
        return(miTipo.fmodificada);
    }
    public void setAceite(tipoLog miTipo, int aceite) {  miTipo.aceite = aceite;    }
    public void setKms(tipoLog miTipo, int kms) {  miTipo.kms = kms;    }
    public void setVecesFiAceite(tipoLog miTipo, int veces_fil_aceite) {  miTipo.veces_fil_aceite = veces_fil_aceite;    }
    public void setContadorFiAceite(tipoLog miTipo, int contador_fil_aceite) {  miTipo.contador_fil_aceite = contador_fil_aceite;    }
    public void setRevgral(tipoLog miTipo, int revgral) {  miTipo.revgral = revgral;    }
}
