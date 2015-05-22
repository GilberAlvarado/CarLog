package com.carlog.gilberto.carlog;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Gilberto on 22/11/2014.
 */
public class TipoLog implements Serializable {

    public final static String TIPO_ACEITE = "Cambio de aceite";
    public final static String TIPO_REV_GENERAL = "Revisión general";
    public final static String TIPO_FILTRO_AIRE = "Cambio de filtro de aire";
    public final static String TIPO_FILTRO_ACEITE = "Cambio de filtro de aceite";
    public final static String TIPO_FILTRO_GASOLINA = "Cambio de filtro de gasolina";


    private String tipo;
    private Date fecha;
    private String datetxt;
    private int int_fecha;
    private int aceite;
    private String matricula;

    public TipoLog(String tipo, Date fecha, String datetxt, int int_fecha, int aceite,  String matricula) {
        this.tipo = tipo;
        this.fecha = fecha;
        this.datetxt = datetxt;
        this.int_fecha = int_fecha;
        this.aceite = aceite;
        this.matricula = matricula;
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

    public void setAceite(TipoLog miTipo, int aceite) {  miTipo.aceite = aceite;    }

}
