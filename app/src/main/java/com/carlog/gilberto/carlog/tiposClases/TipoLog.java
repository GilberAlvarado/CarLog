package com.carlog.gilberto.carlog.tiposClases;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Gilberto on 22/11/2014.
 */
public class TipoLog implements Serializable {

    public final static String TIPO_ACEITE = "Aceite";
    public final static String TIPO_REV_GENERAL = "Revisión general";
    public final static String TIPO_CORREA = "Correa de distribución";
    public final static String TIPO_BOMBA_AGUA = "Bomba de agua";
    public final static String TIPO_FILTRO_ACEITE = "Filtro de aceite";
    public final static String TIPO_FILTRO_GASOLINA = "Filtro de gasolina";
    public final static String TIPO_ITV = "I.T.V.";
    public final static String TIPO_BUJIAS = "Bujías";
    public final static String TIPO_FILTRO_AIRE = "Filtro de aire";
    public final static String TIPO_FRENOS = "Discos y pastillas de freno";
    public final static String TIPO_LIQUIDO_FRENOS = "Líquido de frenos";
    public final static String TIPO_LIMPIAPARABRISAS = "Limpiaparabrisas";
    public final static String TIPO_LUCES = "Luces";
    public final static String TIPO_RUEDAS = "Ruedas";
    public final static String TIPO_EMBRAGUE = "Embrague";


    private String tipo;
    private Date fecha;
    private String datetxt;
    private long long_fecha;
    private int aceite;
    private int veces_fil_aceite;
    private int contador_fil_aceite;
    private int revgral;
    private String matricula;
    private int realizado;
    private int fmodificada;
    private int kms;

    public TipoLog(String tipo, Date fecha, String datetxt, long long_fecha, int aceite, int veces_fil_aceite, int contador_fil_aceite, int revgral, String matricula, int realizado, int fmodificada, int kms) {
        this.tipo = tipo;
        this.fecha = fecha;
        this.datetxt = datetxt;
        this.long_fecha = long_fecha;
        this.aceite = aceite;
        this.veces_fil_aceite = veces_fil_aceite;
        this.contador_fil_aceite = contador_fil_aceite;
        this.revgral = revgral;
        this.matricula = matricula;
        this.realizado = realizado;
        this.fmodificada = fmodificada;
        this.kms = kms;
    }

    public String getTipo(TipoLog miTipo) {
        return(miTipo.tipo);
    }

    public Date getFecha(TipoLog miTipo) {
        return(miTipo.fecha);
    }

    public String getFechatxt(TipoLog miTipo) {
        return(miTipo.datetxt);
    }

    public long getFechalong(TipoLog miTipo) {
        return(miTipo.long_fecha);
    }

    public String getCoche(TipoLog miTipo) {
        return(miTipo.matricula);
    }

    public int getAceite(TipoLog miTipo) {
        return(miTipo.aceite);
    }

    public int getVecesFilAceite(TipoLog miTipo) {
        return(miTipo.veces_fil_aceite);
    }

    public int getContadorFilAceite(TipoLog miTipo) {
        return(miTipo.contador_fil_aceite);
    }

    public int getRevgral(TipoLog miTipo) {
        return(miTipo.revgral);
    }

    public int getKms(TipoLog miTipo) {
        return(miTipo.kms);
    }

    public int getRealizado(TipoLog miTipo) {
        return(miTipo.realizado);
    }

    public int getFmodificada(TipoLog miTipo) {
        return(miTipo.fmodificada);
    }

    public void setAceite(TipoLog miTipo, int aceite) {  miTipo.aceite = aceite;    }

    public void setVecesFiAceite(TipoLog miTipo, int veces_fil_aceite) {  miTipo.veces_fil_aceite = veces_fil_aceite;    }

    public void setContadorFiAceite(TipoLog miTipo, int contador_fil_aceite) {  miTipo.contador_fil_aceite = contador_fil_aceite;    }

    public void setRevgral(TipoLog miTipo, int revgral) {  miTipo.revgral = revgral;    }

}
