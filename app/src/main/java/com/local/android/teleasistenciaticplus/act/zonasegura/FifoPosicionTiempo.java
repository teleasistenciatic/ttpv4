package com.local.android.teleasistenciaticplus.act.zonasegura;

import java.util.LinkedList;

/**
 *
 * @author FESEJU La clase mantiene una lista enlazada de posiciones en una
 * lista FIFO de un tama�o que se fija en el constructor
 */
public class FifoPosicionTiempo {

    ////////////////////////////// VARIABLES DE CLASE //////////////////////////
    private int sizeFifoPool; //El tama�o de pool de PosicionTiempo que se va a mantener

    private final LinkedList<PosicionTiempo> posiciones; //Lista enlazada con el n�mero de elementos

    ////////////////////////////// GETTERS SETTERS /////////////////////////////
    public void setSizeFifoPool(int sizeFifoPool) {
        this.sizeFifoPool = sizeFifoPool;
    }

    ////////////////////////////// CONSTRUCTOR// ///////////////////////////////
    FifoPosicionTiempo(int i) {
        this.posiciones = new LinkedList<>();
        sizeFifoPool = i;
    }

    ////////////////////////////// METODOS DE CLASE ///////////////////////////
    /**
     * A�adir Posiciones en una lista enlazada para que s�lo tengamos un numero
     * de elementos m�ximo
     *
     * @param miPosicionTiempo objeto que se va a a�adir a la lista
     */
    public void addPosiciones(PosicionTiempo miPosicionTiempo) {

        if (posiciones.size() < sizeFifoPool) {

            posiciones.addFirst(miPosicionTiempo);

        } else {

            posiciones.removeLast();
            posiciones.addFirst(miPosicionTiempo);

        }

    }

    /*
     La cola FIFO que se crea tiene como objetivo no dar un falso positivo
     en la detecci�n de salida de una zona segura.

     Cuando se tienen un numero de muestras de distancias y todas est�n
     fuera de la ZONA SEGURA, se considerar� -en otra clase- que la persona
     se halla fuera de ella.

     El GPS puede ofrecer lecturas periodicas y alteras entre dos puntos,
     de esta forma se "normalizan" los datos.

    Si todos los valores son FALSE, quiere decir que las ultimas muestras
    est�n fuera de la ZonaSegura y se devuelve
     */
    public boolean listaPosicionTiempoAllNotInZone() {

        int numeroTrue = 0;
        int numeroFalse = 0;

        // Contamos los trues y falses de la zonaSegura
        for (PosicionTiempo posicion : posiciones) {

            if ( posicion.inZone == true ) {
                numeroTrue++;
            } else {
                numeroFalse++;
            }

        }

        //La condici�n para que todos est�n en ZonaSegura es que
        //todas las mediciones tomadas contengan TRUE
        //con un s�lo FALSE no se considera que haya salido de
        //la ZonaSegura. Esto es necesario refutarlo con pruebas

        //Si todos los valores son FALSE asumimos que ha salido
        // de la Zona Segura


        if ( numeroTrue == 0 ) {
            return true;
        } else {
            //False es un valor que no nos vale puesto
            //que se generar�a s�lo en el caso de todos
            //no sean FALSE. Y eso es un resultado
            //no concluyente
            return false;
        }

    }

    /**
     * M�todo con fines depurativos
     */
    public void printPosiciones() {

        for (PosicionTiempo posicion : posiciones) {
            System.out.println(posicion);
        }

        /*
         Iterator<PosicionTiempo> iterator = posiciones.iterator();

         int indice = 0;
         while (iterator.hasNext()) {
         PosicionTiempo posicion = iterator.next();
         System.out.println(indice++ + ": " + posicion);
         }*/
        System.out.println("----------" + listaPosicionTiempoAllNotInZone() );
    }

}