package com.carlog.gilberto.carlog;

import java.io.Serializable;

/**
 * Created by Gilberto on 18/11/2014.
 */
@SuppressWarnings("serial")
public class TipoCoche implements Serializable {
    private String marca;
    private String modelo;
    private String year;
    private String kms;
    private String itv;


    public TipoCoche(String marca, String modelo, String year, String kms, String itv) {
        this.marca = marca;
        this.modelo = modelo;
        this.year = year;
        this.kms = kms;
        this.itv = itv;
    }

    public String getMarca(TipoCoche miCoche) {
        return(miCoche.marca);
    }

    public String getModelo(TipoCoche miCoche) {
        return(miCoche.modelo);
    }

    public String getYear(TipoCoche miCoche) {
        return(miCoche.year);
    }

    public String getKms(TipoCoche miCoche) {
        return(miCoche.kms);
    }

    public String getItv(TipoCoche miCoche) {
        return(miCoche.itv);
    }

}
