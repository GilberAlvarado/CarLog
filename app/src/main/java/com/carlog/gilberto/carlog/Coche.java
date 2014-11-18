package com.carlog.gilberto.carlog;

import java.io.Serializable;

/**
 * Created by Gilberto on 18/11/2014.
 */
@SuppressWarnings("serial")
public class Coche implements Serializable {
    private String marca;
    private String modelo;
    private String year;
    private String kms;
    private String itv;


    public Coche(String marca, String modelo, String year, String kms, String itv) {
        this.marca = marca;
        this.modelo = modelo;
        this.year = year;
        this.kms = kms;
        this.itv = itv;
    }

    public String getMarca(Coche miCoche) {
        return(miCoche.marca);
    }

    public String getModelo(Coche miCoche) {
        return(miCoche.modelo);
    }

    public String getYear(Coche miCoche) {
        return(miCoche.year);
    }

    public String getKms(Coche miCoche) {
        return(miCoche.kms);
    }

    public String getItv(Coche miCoche) {
        return(miCoche.itv);
    }

}
