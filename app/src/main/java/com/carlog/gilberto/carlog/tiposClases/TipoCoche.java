package com.carlog.gilberto.carlog.tiposClases;

import java.io.Serializable;

/**
 * Created by Gilberto on 18/11/2014.
 */
@SuppressWarnings("serial")
public class tipoCoche implements Serializable {

    public final static int PROFILE_ACTIVO = 1;
    public final static int PROFILE_INACTIVO = 0;

    private String marca;
    private String modelo;
    private int imgmodelocambiada;
    private String imgmodelopersonalizada;
    private int year;
    private int kms;
    private long itv;
    private int profile;
    private String matricula;
    private long fecha_ini;
    private int kms_ini;


    public tipoCoche(String matricula, String marca, String modelo, int imgmodelocambiada, String imgmodelopersonalizada, int year, int kms, long itv, int profile, long fecha_ini, int kms_ini) {
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.imgmodelocambiada = imgmodelocambiada;
        this.imgmodelopersonalizada = imgmodelopersonalizada;
        this.year = year;
        this.kms = kms;
        this.itv = itv;
        this.profile = profile;
        this.fecha_ini = fecha_ini;
        this.kms_ini = kms_ini;
    }

    public String getMarca(tipoCoche miCoche) {
        return(miCoche.marca);
    }
    public String getModelo(tipoCoche miCoche) {
        return(miCoche.modelo);
    }
    public int getImgModeloCambiada(tipoCoche miCoche) {
        return(miCoche.imgmodelocambiada);
    }
    public String getImgModeloPersonalizada(tipoCoche miCoche) { return(miCoche.imgmodelopersonalizada); }
    public int getYear(tipoCoche miCoche) {
        return(miCoche.year);
    }
    public String getMatricula(tipoCoche miCoche) {
        return(miCoche.matricula);
    }
    public int getKms(tipoCoche miCoche) {
        return(miCoche.kms);
    }
    public long getItv(tipoCoche miCoche) {
        return(miCoche.itv);
    }
    public int getProfile(tipoCoche miCoche) {
        return(miCoche.profile);
    }
    public long getFechaIni(tipoCoche miCoche) {
        return(miCoche.fecha_ini);
    }
    public int getKmsIni(tipoCoche miCoche) {
        return(miCoche.kms_ini);
    }
}
