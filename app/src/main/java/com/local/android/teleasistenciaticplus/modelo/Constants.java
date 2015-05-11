package com.local.android.teleasistenciaticplus.modelo;

/**
 * Created by FESEJU on 11/02/2015.
 * Interfaz de constantes generales de la aplicación
 */

public interface Constants {

    ////////////////////////////////////////////////
    // VALORES DE DESARROLLO DE LA APLICACIÓN
    ////////////////////////////////////////////////

        public static final DebugLevel DEBUG_LEVEL = DebugLevel.DEBUG;
        public static final boolean FAKE_SMS = true;

        public static final Boolean LOG_TO_FILE = true;
        public static final String DEBUG_LOG_FILE = "teleasistencia.log";
        public static final String RED_NEURONAL_FILE = "teleasistencia_red_neuronal.log";

    ////////////////////////////////////////////////
    // MISCELANEA
    ////////////////////////////////////////////////

        public static final long LOADING_SCREEN_DELAY = 3000; //Con 1000 a veces da problemas, no le ha dado tiempo a terminar de ejecutar la lectura del archivo
        public static final long SMS_SENDING_DELAY = 5000; //Frecuencia de envío de mensajes
        public static final long MEMORY_DIVIDER = 1048576L; //BytestoMegabytes
        public static final boolean SHOW_ANIMATION = true; //Decide si se realizan transiciones entre actividades o fragmentos
        public static final boolean PLAY_SOUNDS = false; //Decide si se reproducen sonidos en la aplicación

        /*
        1024 bytes      == 1 kilobyte
        1024 kilobytes  == 1 megabyte

        1024 * 1024     == 1048576*/


    ////////////////////////////////////////////////
    // OPERACIONES HTTP
    ////////////////////////////////////////////////

        public static final int HTTP_OPERATION_DELAY = 3000;

    ////////////////////////////////////////////////
    // FICHERO DE SHAREDPREFERENCES
    ////////////////////////////////////////////////

        public static final String APP_SHARED_PREFERENCES_FILE = "teleasistencia.prefs";

    ////////////////////////////////////////////////
    // VALORES DE SHAREDPREFERENCES
    ////////////////////////////////////////////////

        public static final String NOMBRE_APP_SHARED_PREFERENCES_NO_MOSTRAR_AVISO_TARIFICACION = "avisotarificacion";

        public static final String CAIDAS = "caidas";
        public static final String ACTIVO = "activo";
        public static final String INACTIVO = "inactivo";

        public static final String ARCHIVO_RED= "pesosprueba";

        public static final String NOMBRE_APP_SHARED_PREFERENCES_DATETIME_ULTIMO_SMS_ENVIADO = "ultimosmsenviado";
}

/*
    ////////////////////////////////////////////////
    // SERVIDOR TELEASISTENCIA
    ////////////////////////////////////////////////

public static final String SERVER_PROTOCOL = "http://";
public static final String SERVER_IP = "10.0.0.190";
//public static final String SERVER_ADDRESS = SERVER_PROTOCOL + "ttpserver.dev";
public static final String SERVER_URL = SERVER_PROTOCOL + SERVER_IP;

////////////////////////////////////////////////
// Controladores
////////////////////////////////////////////////
public static final String CONTROLLER_CHECK_ONLINE_SERVER = "/serverstatus";
public static final String CONTROLLER_CHECK_PHONE = "/phone/check/";
public static final String CONTROLLER_USER_NAME = "/phoneuser/name/";
public static final String CONTROLLER_AVISO_CHECK = "/aviso/check/";
public static final String CONTROLLER_AVISO_CREATE = "/aviso/create/";
public static final String CONTROLLER_AVISO_DELETE = "/aviso/delete/";
*/
