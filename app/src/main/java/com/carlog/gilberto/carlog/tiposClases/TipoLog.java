package com.carlog.gilberto.carlog.tiposClases;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Gilberto on 22/11/2014.
 */
public class TipoLog implements Serializable {

    public final static String TIPO_ACEITE = "Cambio de aceite y filtro";
    public final static String TIPO_REV_GENERAL = "Revisión general";
    public final static String TIPO_CORREA = "Cambio correa de distribución";
    public final static String TIPO_BOMBA_AGUA = "Cambio bomba de agua";
    public final static String TIPO_FILTRO_GASOLINA = "Cambio de filtro de gasolina";
    public final static String TIPO_ITV = "I.T.V.";


    private String tipo;
    private Date fecha;
    private String datetxt;
    private int int_fecha;
    private int aceite;
    private int revgral;
    private String matricula;
    private int realizado;
    private int kms;

    public TipoLog(String tipo, Date fecha, String datetxt, int int_fecha, int aceite, int revgral, String matricula, int realizado, int kms) {
        this.tipo = tipo;
        this.fecha = fecha;
        this.datetxt = datetxt;
        this.int_fecha = int_fecha;
        this.aceite = aceite;
        this.revgral = revgral;
        this.matricula = matricula;
        this.realizado = realizado;
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

    public int getFechaint(TipoLog miTipo) {
        return(miTipo.int_fecha);
    }

    public String getCoche(TipoLog miTipo) {
        return(miTipo.matricula);
    }

    public int getAceite(TipoLog miTipo) {
        return(miTipo.aceite);
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

    public void setAceite(TipoLog miTipo, int aceite) {  miTipo.aceite = aceite;    }

    public void setRevgral(TipoLog miTipo, int revgral) {  miTipo.revgral = revgral;    }

}
