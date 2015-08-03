package com.carlog.gilberto.carlog.tiposClases;

/**
 * Created by Gilberto on 02/06/2015.
 */
public class cocheEsNuevo {
    private static cocheEsNuevo mInstance= null;

    public int coche_es_nuevo;

    protected cocheEsNuevo(int coche_es_nuevo){
        this.coche_es_nuevo = coche_es_nuevo;
    }

    public static synchronized cocheEsNuevo getInstance(){
        if(null == mInstance){
            mInstance = new cocheEsNuevo(0);
        }
        return mInstance;
    }
}
