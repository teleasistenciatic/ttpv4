package com.local.android.teleasistenciaticplus.lib.detectorCaidas;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Clase para construir la red neuronal completa.
 * Se trata de un perceptrón multicapa.
 *
 * @author SAMUAN
 */
public class Red {
    private double[][] vector_datos;
    
    private double[] vector_entrada;
    private double[] vector_parcial;
    private double[] vector_salida;

    private double[][] sinapsisA; //relaciona la entrada con la capa oculta
    private double[][] sinapsisB; //relaciona la capa oculta con la capa de salida

    private double[] biasA; //capa oculta
    private double[] biasB; //capa de salida

    private double[] target;
    private double[] errorA; //errores en la capa oculta
    private double[] errorB; //errores en la capa de salida

    private double ratioAprendizaje;
    private double momento;
    private int numEntradas;
    private int numCapaOculta;
    private int numCapaSalida;
    private int ciclos; //veces que se presentan el mismo conjunto de datos a la red. 1 ciclo(epoch) n ejemplos.
    

    /**
     * Inicia los pesos con valores en el intervalo (-0.5,0.5)
     * 
     * Cada sinapsis independiente porque cada capa tiene 
     * distintas neuronas.
     *
     */
    public void iniciarSinapsis(){   
        System.out.println("sinapsis A");
        for(int i=0;i<sinapsisA.length;i++){
            for(int j=0;j<sinapsisA[0].length;j++){        
               sinapsisA[i][j]=Math.random()-0.5;
               System.out.print( sinapsisA[i][j]+" ");               
            }
            System.out.println();
        }           
          System.out.println();
           System.out.println("sinapsis B");
        for(int i=0;i<sinapsisB.length;i++){
            for(int j=0;j<sinapsisB[0].length;j++) {
                sinapsisB[i][j]=Math.random()-0.5;
                 System.out.print( sinapsisB[i][j]+" ");
            }
            System.out.println();
        }        
         System.out.println();
    }

    /**
     * Iniciar los datos básicos de la red.
     * Se utiliza al preparar la red para aprendizaje.
     *
     * @param ratioAprendizaje
     * @param numEntradas Cantidad de datos de entrada
     * @param numCapaOculta Cantidad de neuronas en capa oculta
     * @param numCapaSalida Cantidad de neuronas en capa de salida
     */
    public void iniciarRed(double ratioAprendizaje,double momento, int numEntradas, int numCapaOculta, int numCapaSalida ){
        this.ratioAprendizaje=ratioAprendizaje;
        this.momento=momento;
        this.numEntradas=numEntradas;
        this.numCapaOculta=numCapaOculta;
        this.numCapaSalida=numCapaSalida;

        vector_entrada=new double[this.numEntradas];
        vector_parcial=new double[this.numCapaOculta];
        vector_salida=new double[this.numCapaSalida];

        sinapsisA=new double[numCapaOculta][numEntradas]; //1 neurona por fila
        sinapsisB=new double[numCapaSalida][numCapaOculta]; //cada columna son pesos que llegan a la neurona.

        biasA=new double[numCapaOculta];
        biasB=new double[numCapaSalida];

        target=new double[numCapaSalida];
        errorA=new double[numCapaOculta];
        errorB=new double[numCapaSalida];
    }
    
    /**
     * Inicia la red para calcular salidas.
     * 
     * @param numEntradas Cantidad de datos de entrada
     * @param numCapaOculta Cantidad de neuronas en capa oculta
     * @param numCapaSalida Cantidad de neuronas en capa de salida
     */
    public void iniciarRed(int numEntradas, int numCapaOculta, int numCapaSalida){
        this.numEntradas=numEntradas;
        this.numCapaOculta=numCapaOculta;
        this.numCapaSalida=numCapaSalida;
        
        vector_entrada=new double[this.numEntradas];
        vector_parcial=new double[this.numCapaOculta];
        vector_salida=new double[this.numCapaSalida];

        sinapsisA=new double[numCapaOculta][numEntradas]; //1 neurona por fila
        sinapsisB=new double[numCapaSalida][numCapaOculta]; //cada columna son pesos que llegan a la neurona.

        biasA=new double[numCapaOculta];
        biasB=new double[numCapaSalida];
    }

    /**
     * Establece el vector con todos los datos para entrenamiento.
     * 
     */
    public void setVector_datos(double[][] vector_datos) {
        this.vector_datos = vector_datos;
    }
    
    /**
     *
     * algoritmo de entrenamiento
     *
     * ejecución hacia adelante
     * calculo errores en ultima capa
     * modificación pesos de sinapsis B
     * calculo error capa oculta
     * modificación pesos sinapsis A
     */
    public void entrenamiento(int ciclos){
        this.ciclos=ciclos;
        //iniciar sinapsis
        iniciarSinapsis();
        //capturar datos de prueba + numero de iteraciones

        //cada entrada será 8 valores de entrada + 4 de salida.
        //setVector_entrada
        //setTarget

        //Para cada muestra de entrenamiento se hace el procesamiento
        //backpropagation 1 vez.
        //epoch es presentar 1 vez todos los datos
        //realizar varias.   
        
        for( int i=0;i<ciclos;i++){
            LinkedList lista=new LinkedList( Arrays.asList(vector_datos) );   
            while(lista.size()>0){
               // System.out.println("tamaño lista "+lista.size());
                int cual=(int) (Math.random() * lista.size());
              //  System.out.println("cual "+cual);
                double[] vector=(double[])lista.remove(cual);
                for(int j=0;j<vector.length;j++){
                     if(j<vector_entrada.length){
                        vector_entrada[j]=vector[j];
                    }else{
                        target[j-vector_entrada.length]=vector[j];
                    }
                }  
            
                //hacer proceso.
                System.out.println("***************");
                System.out.println("Vector entrada");
                imprimirVector(vector_entrada);
                System.out.println("Target");
                imprimirVector(target);             
            
                calcular();
                calcularErroresNeuronaSalida();
                modificarPesosSinapsisB();
                calcularErrorCapaOculta();
                modificarPesosSinapsisA();
            }
        }
       
        System.out.println("FIN ENTRENAMIENTO");
    }

     /**
     * Calcula los valores de salida de la red completa.
     *
     */
    public void calcular(){
        //vector entrada por sinapsisA
        double suma=0;
        for(int i=0;i<sinapsisA.length;i++){ //i es cada neurona de esta capa
            suma=0;
            for(int j=0;j<sinapsisA[i].length;j++) { //j es numero de entradas que llegan a la neurona
                suma +=vector_entrada[j]*sinapsisA[i][j];
                //System.out.println("CALCULAR calculo: "+vector_entrada[j]+" "+sinapsisA[i][j]+" "+suma );
            }
            suma=suma+biasA[i];
           // System.out.println("Suma "+suma);
            vector_parcial[i]=logsig(suma);
        }
        
        System.out.println("Vector parcial generado ");
        imprimirVector(vector_parcial);
        

        //vector parcial por sinapsisB
        for(int i=0;i<sinapsisB.length;i++){
            suma=0;
            for(int j=0;j<sinapsisB[i].length;j++) {
                suma +=vector_parcial[j]*sinapsisB[i][j];
            }
            suma=suma+biasB[i];
            vector_salida[i]=logsig(suma);
        }
        
        System.out.println("Vector salida generado ");
        imprimirVector(vector_salida);
    }

    /**
     * Función de activación
     * Se utiliza una función sigmoidal.
     *
     * @param val valor de x
     * @return resultado
     */
    private double logsig(double val){
        return 1/(1+Math.exp(-val));
    }

    /**
     * Calcula los errores en la última capa.
     */
    public void calcularErroresNeuronaSalida(){
        for(int i=0;i<vector_salida.length;i++){
            errorB[i]=vector_salida[i]*(1-vector_salida[i])*(target[i]-vector_salida[i]);
            //System.out.println("SALIDA error "+i+" "+errorB[i]);
        }
        System.out.println("Vector error salida");
        imprimirVector(errorB);
        
    }

    /**
     * Modifica los pesos de la sinapsis entre la capa oculta y la capa de salida.
     * WAα = WAα + η δα outA   WAβ = WAβ + η δβ outA
     * WBα = WBα + η δα outB   WBβ = WBβ + η δβ outB
     * WCα = WCα + η δα outC   WCβ = WCβ + η δβ outC
     */
    public void modificarPesosSinapsisB(){
        for(int i=0;i<sinapsisB.length;i++){
            for(int j=0;j<sinapsisB[0].length;j++){
                sinapsisB[i][j]=(momento*sinapsisB[i][j])+(ratioAprendizaje*errorB[i]*vector_parcial[j]);
               //System.out.println("MODIFICAR  "+i+" "+j+" "+sinapsisB[i][j]);
            }

        }
    }

    /**
     * Calcula los errores de la capa oculta utilizando los errores de la capa de salida
     *
     * δA = outA (1 – outA) (δα WAα + δβ WAβ)
     δB = outB (1 – outB) (δα WBα + δβWBβ)
     δC = outC (1 – outC) (δα WCα + δβWCβ)
     */
    public void calcularErrorCapaOculta(){
        double suma=0;
        for(int i=0;i<sinapsisB[0].length;i++){ //para cada neurona  de capa oculta        
            suma=0;
            for(int j=0;j<sinapsisB.length;j++) { //calculo la aportación de error a cada neurona de destino. Por eso lo recorro en columna.
                suma=suma+errorB[j]*sinapsisB[j][i];
            }
            errorA[i]=vector_parcial[i]*(1-vector_parcial[i])*suma;
        }
        System.out.println("Vector error capa oculta");
        imprimirVector(errorA);
    }

    /**
     * Modifica los pesos de la sinapsis entre los datos de entrada y la capa oculta.
     *
     * WλA = WλA + ηδA inλ     WΩA = WΩA + ηδA inΩ
     * WλB = WλB + ηδB inλ     WΩB = WΩB + ηδB inΩ
     * ...
     */
    public void modificarPesosSinapsisA(){
        for(int i=0;i<sinapsisA.length;i++){ //para cada neurona
            for(int j=0;j<sinapsisA[0].length;j++){ //para cada peso que llega a la neurona
                sinapsisA[i][j]=(momento*sinapsisA[i][j])+(ratioAprendizaje*errorA[i]*vector_entrada[j]);
            }
        }
    }


    /************* MÉTODOS GET SET ****************************/

    public double[] getVector_entrada() {
        return vector_entrada;
    }

    public void setVector_entrada(double[] vector_entrada) {
        this.vector_entrada = vector_entrada;
    }

    public double[] getVector_salida() {
        return vector_salida;
    }

    public double[] getVector_parcial() {
        return vector_parcial;
    }

    public void setVector_parcial(double[] vector_parcial) {
        this.vector_parcial = vector_parcial;
    }

    public void setVector_salida(double[] vector_salida) {
        this.vector_salida = vector_salida;
    }

    public double[] getBiasA() {
        return biasA;
    }

    public void setBiasA(double[] biasA) {
        this.biasA = biasA;
    }

    public double[] getBiasB() {
        return biasB;
    }

    public void setBiasB(double[] biasB) {
        this.biasB = biasB;
    }

    public double[][] getSinapsisB() {
        return sinapsisB;
    }

    public void setSinapsisB(double[][] sinapsisB) {
        this.sinapsisB = sinapsisB;
    }

    public double[][] getSinapsisA() {
        return sinapsisA;
    }

    public void setSinapsisA(double[][] sinapsisA) {
        this.sinapsisA = sinapsisA;
    }

    public double[] getTarget() {
        return target;
    }

    public void setTarget(double[] target) {
        this.target = target;
    }

    public void imprimirSinapsis(){
        for(int i=0;i<sinapsisA.length;i++) {
            String linea="";
            for (int j = 0; j < sinapsisA[0].length; j++) {
                linea += " "+sinapsisA[i][j];
            }
            System.out.println("SINAPSIS "+linea);
        }
    }
    
    public void imprimirVector(double[] vec){
        for(double dat:vec) System.out.print(dat+" ");
        System.out.println();
    }

}


    