package com.carlog.gilberto.carlog;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Gilberto on 22/11/2014.
 */
public class TipoLog implements Serializable {
    private String tipo;
    private Date date;


    public TipoLog(String tipo, Date fecha) {
        this.tipo = tipo;
        this.date = date;
    }

    public String getTipo(TipoLog miTipo) {
        return(miTipo.tipo);
    }

    public Date getFecha(TipoLog miTipo) {
        return(miTipo.date);
    }

}
