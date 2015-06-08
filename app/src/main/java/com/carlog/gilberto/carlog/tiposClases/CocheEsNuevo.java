package com.carlog.gilberto.carlog.tiposClases;

/**
 * Created by Gilberto on 02/06/2015.
 */
public class CocheEsNuevo {
    private static CocheEsNuevo mInstance= null;

    public int coche_es_nuevo;

    protected CocheEsNuevo(int coche_es_nuevo){
        this.coche_es_nuevo = coche_es_nuevo;
    }

    public static synchronized CocheEsNuevo getInstance(){
        if(null == mInstance){
            mInstance = new CocheEsNuevo(0);
        }
        return mInstance;
    }
}
