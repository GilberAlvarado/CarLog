package com.carlog.gilberto.carlog;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Gilberto on 22/11/2014.
 */
public class TipoLog implements Serializable {
    private String tipo;
    private Date fecha;
    private String datetxt;
    private int int_fecha;

    public TipoLog(String tipo, Date fecha, String datetxt, int int_fecha) {
        this.tipo = tipo;
        this.fecha = fecha;
        this.datetxt = datetxt;
        this.int_fecha = int_fecha;
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

}
