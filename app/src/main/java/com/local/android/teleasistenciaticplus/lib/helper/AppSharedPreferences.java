package com.local.android.teleasistenciaticplus.lib.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.local.android.teleasistenciaticplus.modelo.Constants;
import com.local.android.teleasistenciaticplus.modelo.GlobalData;


/**
 * Created by FESEJU on 23/03/2015.
 * Leera y guardará preferencias de la aplicación mediante SharedPreferences
 */
public class AppSharedPreferences implements Constants {

    public void setUserData (String nombre, String apellidos) {
        SharedPreferences.Editor editor = GlobalData.getAppContext().getSharedPreferences( APP_SHARED_PREFERENCES_FILE , Context.MODE_PRIVATE ).edit();
        editor.putString("nombre", nombre);
        editor.putString("apellidos", apellidos);
        editor.commit();
    }

    /**
     * Recupera los datos de nombre y apellidos en el modo offline
     * @return String[0] Nombre String[1] Apellidos
     */
    public String[] getUserData() {

        SharedPreferences prefs = GlobalData.getAppContext().getSharedPreferences( APP_SHARED_PREFERENCES_FILE , Context.MODE_PRIVATE);
        String nombre = prefs.getString("nombre", "");//"No name defined" is the default value.
        String apellidos = prefs.getString("apellidos", ""); //0 is the default value.

        String[] datosPersonalesUsuario = {nombre, apellidos};

        return datosPersonalesUsuario;
    }

    /**
     * ¿Tiene datos personales?
     * @return boolean tiene datos personales
     */
    public boolean hasUserData() {

        SharedPreferences prefs = GlobalData.getAppContext().getSharedPreferences( APP_SHARED_PREFERENCES_FILE , Context.MODE_PRIVATE);
        String nombre = prefs.getString("nombre", "");
        String apellidos = prefs.getString("apellidos", "");

        if ( ( nombre.length() > 0) && (apellidos.length() > 0) ) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * Borrar datos personales
     */
    public void deleteUserData() {

        SharedPreferences.Editor editor = GlobalData.getAppContext().getSharedPreferences( APP_SHARED_PREFERENCES_FILE , Context.MODE_PRIVATE ).edit();
        editor.putString("nombre", "");
        editor.putString("apellidos", "");
        editor.commit();

    }

    /**
     * Función para almacenar en el shared preferences los datos de personas de contacto
     */
    public void setPersonasContacto (String nombre1, String telefono1, String nombre2, String telefono2, String nombre3, String telefono3) {
        SharedPreferences.Editor editor = GlobalData.getAppContext().getSharedPreferences( APP_SHARED_PREFERENCES_FILE , Context.MODE_PRIVATE ).edit();

        editor.putString("nombre1", nombre1);
        editor.putString("telefono1", telefono1);

        editor.putString("nombre2", nombre2);
        editor.putString("telefono2", telefono2);

        editor.putString("nombre3", nombre3);
        editor.putString("telefono3", telefono3);

        editor.commit();
    }

    /**
     * Borrar los datos del sharedPreferences
     */
    public void deletePersonasContacto () {
        deletePersonasContactobyId(0);
        deletePersonasContactobyId(1);
        deletePersonasContactobyId(2);
    }

    /**
     * Borrar los datos de contacto seleccionado del sharedPreferences
     */
    public void deletePersonasContactobyId (int contactoABorrar) {
        SharedPreferences.Editor editor = GlobalData.getAppContext().getSharedPreferences( APP_SHARED_PREFERENCES_FILE , Context.MODE_PRIVATE ).edit();

        if ( contactoABorrar == 0) {
            editor.putString("nombre1", "");
            editor.putString("telefono1", "");
        } else if ( contactoABorrar == 1) {
            editor.putString("nombre2", "");
            editor.putString("telefono2", "");
        } else if ( contactoABorrar == 2) {
            editor.putString("nombre3", "");
            editor.putString("telefono3", "");
        }

        editor.commit();
    }

    /**
     * Devuelve un array con los teléfonos de contacto
     * @return
     */

    public String[] getPersonasContacto() {

        SharedPreferences prefs = GlobalData.getAppContext().getSharedPreferences( APP_SHARED_PREFERENCES_FILE , Context.MODE_PRIVATE);
        String nombre1 = prefs.getString("nombre1", "");
        String telefono1 = prefs.getString("telefono1", "");
        String nombre2 = prefs.getString("nombre2", "");
        String telefono2 = prefs.getString("telefono2", "");
        String nombre3 = prefs.getString("nombre3", "");
        String telefono3 = prefs.getString("telefono3", "");

        String[] personasContacto = {nombre1, telefono1,
                                     nombre2, telefono2,
                                     nombre3, telefono3 };

        return personasContacto;
    }

    /**
     * Comprobación de si hay datos de contacto
     * @return boolean hay alguna persona de contacto
     */
    public Boolean hasPersonasContacto() {

        SharedPreferences prefs = GlobalData.getAppContext().getSharedPreferences( APP_SHARED_PREFERENCES_FILE , Context.MODE_PRIVATE);
        String nombre1 = prefs.getString("nombre1", "");
        String telefono1 = prefs.getString("telefono1", "");
        String nombre2 = prefs.getString("nombre2", "");
        String telefono2 = prefs.getString("telefono2", "");
        String nombre3 = prefs.getString("nombre3", "");
        String telefono3 = prefs.getString("telefono3", "");

        if ( telefono1.length() > 0 || telefono2.length() > 0 || telefono3.length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    //Estos tres metodos son genéricos y permiten leer cualquier valor de las AppSharedPreferences

    public void setPreferenceData (String map, String valor) {
        SharedPreferences.Editor editor = GlobalData.getAppContext().getSharedPreferences( APP_SHARED_PREFERENCES_FILE , Context.MODE_PRIVATE ).edit();
        editor.putString(map, valor);
        editor.commit();
    }

    public void deletePreferenceData(String map) {
        setPreferenceData(map,"");
    }

    public String getPreferenceData(String map) {

        SharedPreferences prefs = GlobalData.getAppContext().getSharedPreferences( APP_SHARED_PREFERENCES_FILE , Context.MODE_PRIVATE);
        String value = prefs.getString(map, "");

        return value;
    }

    public boolean hasPreferenceData(String map) {

        SharedPreferences prefs = GlobalData.getAppContext().getSharedPreferences( APP_SHARED_PREFERENCES_FILE , Context.MODE_PRIVATE);
        String value = prefs.getString(map, "");

        if  ( value.length() > 0)  {
            return true;
        } else {
            return false;
        }
    }
}
