package com.local.android.teleasistenciaticplus.lib.detectorCaidas;

/**
 *
 * @author SAMUAN
 */
public class Normalizador {


    private static double[] media= new double[0];
    private static double[] desviacion= new double[0];

    // media y desviación de entrenamiento de 4 categorias  80 muestras. caidas de archivo con 9 nuestras.
    //  {0.21123594347223373, 420.7475, 2.7482660329252924, 0.4620161398687167, 71.96573660249996, 0.43213230260799584, 0.9720177307483179, 2.085};
    // {0.1349383134113968, 398.43562057595955, 0.5682768751620355, 0.30148904481529387, 30.776677771240518, 0.2451117907495492, 0.22830585944542162, 2.6273132664377883};


    //media y desviacion de entrenamiento de 3 categorias 80 muestras.
    // {0.2307569297387361, 344.8705501618123, 2.770761227668249, 0.49964679410724205, 73.55031275404525, 0.4159930687299282, 0.9501738815007089, 2.3042071197411005};
    // {0.1460517755829162, 394.62044695960327, 0.6243849490135424, 0.3204488187147163, 31.45060141689851, 0.26180542315468414, 0.22846595085516763, 2.9259493692042455};


    public double[] normaliza(double[] resul) {
        double[] valor=new double[resul.length];
        for(int carac=0;carac<valor.length;carac++){
            valor[carac] = (resul[carac]-media[carac])/desviacion[carac];
        }
        return valor;      
    }

    public static double[] getMedia() {
        return media;
    }

    public static void setMedia(double[] media) {
        Normalizador.media = media;
    }

    public static double[] getDesviacion() {
        return desviacion;
    }

    public static void setDesviacion(double[] desviacion) {
        Normalizador.desviacion = desviacion;
    }  
   
}
