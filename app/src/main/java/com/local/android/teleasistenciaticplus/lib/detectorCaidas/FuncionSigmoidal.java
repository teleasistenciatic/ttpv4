package com.local.android.teleasistenciaticplus.lib.detectorCaidas;

/**
 * Ejecuta la función sigmoidal como función de activación. 
 * @author SAMUAN
 */
public class FuncionSigmoidal implements IFuncionActivacion{

    @Override
    public double[] activar(double[] val) {
        double[] respuesta=new double[val.length];
        for(int i=0;i<val.length;i++){
            respuesta[i]= 1/(1+Math.exp(-val[i]));
        }
        return respuesta;
    }
    
}
