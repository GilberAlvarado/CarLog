package com.carlog.gilberto.carlog;

import java.io.Serializable;

/**
 * Created by Gilberto on 18/11/2014.
 */
@SuppressWarnings("serial")
public class TipoCoche implements Serializable {
    private String marca;
    private String modelo;
    private int year;
    private int kms;
    private int itv;
    private String matricula;
    private int fecha_ini;
    private int kms_ini;


    public TipoCoche(String matricula, String marca, String modelo, int year, int kms, int itv, int fecha_ini, int kms_ini) {
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.year = year;
        this.kms = kms;
        this.itv = itv;
        this.fecha_ini = fecha_ini;
        this.kms_ini = kms_ini;
    }

    public String getMarca(TipoCoche miCoche) {
        return(miCoche.marca);
    }

    public String getModelo(TipoCoche miCoche) {
        return(miCoche.modelo);
    }

    public int getYear(TipoCoche miCoche) {
        return(miCoche.year);
    }

    public String getMatricula(TipoCoche miCoche) {
        return(miCoche.matricula);
    }

    public int getKms(TipoCoche miCoche) {
        return(miCoche.kms);
    }

    public int getItv(TipoCoche miCoche) {
        return(miCoche.itv);
    }

    public int getFechaIni(TipoCoche miCoche) {
        return(miCoche.fecha_ini);
    }

    public int getKmsIni(TipoCoche miCoche) {
        return(miCoche.kms_ini);
    }

}
