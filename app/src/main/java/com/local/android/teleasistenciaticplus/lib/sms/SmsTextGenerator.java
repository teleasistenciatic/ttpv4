package com.local.android.teleasistenciaticplus.lib.sms;

import com.local.android.teleasistenciaticplus.R;
import com.local.android.teleasistenciaticplus.lib.helper.AppSharedPreferences;
import com.local.android.teleasistenciaticplus.lib.helper.AppTime;
import com.local.android.teleasistenciaticplus.lib.sanitize.DataSanitize;

/**
 * Created by FESEJU on 25/03/2015.
 * Clase helper encargada de generar el texto de los mensajes SMS que luego enviará SmsDispatcher
 */
public class SmsTextGenerator {

    String nombre;
    String apellidos;
    String currentDateandTime = new AppTime().getTimeDate();
    String nombreApp = "TELEASISTENCI@TIC+";

    public SmsTextGenerator() {

        String[] nombreApellidos = new AppSharedPreferences().getUserData();
        //Para evitar los problemas al enviar SMS's, se eliminan los caracteres como los acentos
        DataSanitize miDataSanitize = new DataSanitize();
        this.nombre = miDataSanitize.cambiaCaracteresEspanolesPorIngleses(nombreApellidos[0]);
        this.apellidos = miDataSanitize.cambiaCaracteresEspanolesPorIngleses(nombreApellidos[1]);

    }

    public String getTextGenerateSmsIamOK(String phoneNumberDestination) {
        // Andres García comunica que se encuentra bien a las 12:00 del día 12/03/2015

        String smsBodyText = nombreApp + ": " + nombre + " " + apellidos + " comunica que se encuentra bien a las " + currentDateandTime;
        return smsBodyText;
    }

    public String getTextGenerateSmsAviso(String phoneNumberDestination) {
        // Andres García comunica que se encuentra bien a las 12:00 del día 12/03/2015

        String smsBodyText = nombreApp + ": " + nombre + " " + apellidos + " ha generado un aviso " + currentDateandTime;
        return smsBodyText;
    }

    public String getTextGenerateSmsDucha(String phoneNumberDestination) {

        String smsBodyText = nombreApp + ": " + nombre + " " + apellidos + " ha generado un aviso de ducha" + currentDateandTime;
        return smsBodyText;

    }

    public String getTextGenerateSmsCaida(String phoneNumberDestination){
        String smsBodyText = nombreApp + ": " + nombre + " " + apellidos + " ha generado un aviso de caida" + currentDateandTime;
        return smsBodyText;
    }

}
