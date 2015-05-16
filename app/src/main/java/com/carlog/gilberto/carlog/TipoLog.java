package com.carlog.gilberto.carlog;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Gilberto on 22/11/2014.
 */
public class TipoLog implements Serializable {
    private String tipo;
    private Date date;
    private String datetxt;

    public TipoLog(String tipo, Date fecha, String datetxt) {
        this.tipo = tipo;
        this.date = date;
        this.datetxt = datetxt;
    }

    public String getTipo(TipoLog miTipo) {
        return(miTipo.tipo);
    }

    public Date getFecha(TipoLog miTipo) {
        return(miTipo.date);
    }

    public String getFechatxt(TipoLog miTipo) {
        return(miTipo.datetxt);
    }

}
