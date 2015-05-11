package com.local.android.teleasistenciaticplus.act.main;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.local.android.teleasistenciaticplus.R;
import com.local.android.teleasistenciaticplus.act.debug.actMainDebug;
import com.local.android.teleasistenciaticplus.act.ducha.actModoDucha;
import com.local.android.teleasistenciaticplus.act.user.actUserOptions;
import com.local.android.teleasistenciaticplus.act.user.actUserOptionsDatosPersonales;
import com.local.android.teleasistenciaticplus.act.user.actUserOptionsPersonaContacto;
import com.local.android.teleasistenciaticplus.lib.detectorCaidas.ServicioMuestreador;
import com.local.android.teleasistenciaticplus.lib.helper.AppDialog;
import com.local.android.teleasistenciaticplus.lib.helper.AppLog;
import com.local.android.teleasistenciaticplus.lib.helper.AppSharedPreferences;
import com.local.android.teleasistenciaticplus.lib.playsound.PlaySound;
import com.local.android.teleasistenciaticplus.lib.sms.SmsLauncher;
import com.local.android.teleasistenciaticplus.modelo.Constants;
import com.local.android.teleasistenciaticplus.modelo.DebugLevel;
import com.local.android.teleasistenciaticplus.modelo.TipoAviso;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Actividad principal
 *
 * @author TeleasistenciaTIC+ Team
 */

public class actMain extends FragmentActivity implements AppDialog.AppDialogNeutralListener {

    //TAG para depuración
    private final String TAG = getClass().getSimpleName() + "--> ";

    private ImageButton SMSAlertButton;
    private ImageButton SMSOKButton;

    static actMain instanciaActMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instanciaActMain = this; //Se utiliza para obtener una instancia desde otra actividad

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);


        SMSAlertButton = (ImageButton) findViewById(R.id.tfmButton);
        SMSOKButton = (ImageButton) findViewById(R.id.btnIamOK);

        //Damos la bienvenida
        if (Constants.PLAY_SOUNDS) {
            PlaySound.play(R.raw.bienvenido);
        }

        /////////////////////////////////////////////////////////////
        // Si no tiene al menos un contacto de usuario, cargamos la ventana de contacto de usuario
        /////////////////////////////////////////////////////////////
        boolean hasContactData = new AppSharedPreferences().hasPersonasContacto();

        if ( !hasContactData) {
            Intent intent = new Intent(this, actUserOptionsPersonaContacto.class);
            startActivity(intent);
        }


        /////////////////////////////////////////////////////////////
        // Si no tiene datos personales (nombre + apellidos)
        /////////////////////////////////////////////////////////////
        boolean hasUserData = new AppSharedPreferences().hasUserData();

        if (!hasUserData) {
            //Carga de la ventana para la introducción del
            //Nombre y apellidos del usuario
            Intent intent = new Intent(this, actUserOptionsDatosPersonales.class);
            startActivity(intent);
        }

        //Si se envió con anterioridad algún sms, se actualiza el texto informativo
        boolean hasLastSMS = new AppSharedPreferences().hasPreferenceData(Constants.NOMBRE_APP_SHARED_PREFERENCES_DATETIME_ULTIMO_SMS_ENVIADO);
        if(hasLastSMS){

            TextView tvUltimoSMSEnviado = (TextView) findViewById(R.id.tvUltimoSMSEnviado);
            tvUltimoSMSEnviado.setText("Último SMS enviado el " +
                            new AppSharedPreferences().getPreferenceData(Constants.NOMBRE_APP_SHARED_PREFERENCES_DATETIME_ULTIMO_SMS_ENVIADO)
            );
        }

        ////////////////////////////////////////////////
        // Se inicia el servicio de detección de caidas
        /////////////////////////////////////////////
        AppSharedPreferences mispreferences = new AppSharedPreferences();
        String caidasActivas = mispreferences.getPreferenceData(Constants.CAIDAS);
        if(caidasActivas.equals(Constants.ACTIVO)){   //si esta indicado arranco
            Intent intentA= new Intent(this,ServicioMuestreador.class);
            startService(intentA);
            AppLog.i(TAG,"Caidas activo");
        }else if( caidasActivas.equals(Constants.INACTIVO)){  //no hago nada.
            AppLog.i(TAG,"Caidas inactivo");
        }else{ //no existe la preferencia. crear e iniciar el servicio.
            mispreferences.setPreferenceData(Constants.CAIDAS,Constants.ACTIVO);
            Intent intentA= new Intent(this,ServicioMuestreador.class);
            startService(intentA);
            AppLog.i(TAG,"caidas creado y activo");
        }

    }

    @Override
    protected void onStart() {

        super.onStart();

    }

    public static actMain getInstance(){
        return instanciaActMain;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // SE muestra un menu u otro según el modo en que estemos
        // Si estamos en modo de depuración
        if (Constants.DEBUG_LEVEL == DebugLevel.DEBUG) {
            getMenuInflater().inflate(R.menu.menu_act_main, menu);
        } else { //si estamos en modo de producción no mostramos el menu de depuración
            getMenuInflater().inflate(R.menu.menu_act_main_produccion, menu);
        }

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_act_main_exit_app) {
            finish();
        } else if (id == R.id.menu_act_main_debug_screen) {
            Intent intent = new Intent(this, actMainDebug.class);
            startActivity(intent);
        } else if (id == R.id.menu_act_user_options) {
            Intent intent = new Intent(this, actUserOptions.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    /////////////////////////////////////////////////////////////
    // Métodos asociados a los botones de la UI
    /////////////////////////////////////////////////////////////

    /**
     * Botón de acceso a la Configuración
     * Da acceso a la configuración de parámetros personales
     *
     * @param view vista del botón.
     */
    public void configuration_action_button(View view) {

        Intent intent = new Intent(this, actUserOptions.class);

        startActivity(intent);

        if (Constants.SHOW_ANIMATION) {

            overridePendingTransition(R.anim.animation2, R.anim.animation1);

        }
    }

    /**
     * Botón para volver a casa
     * Activa la opción de volver a casa
     *
     * @param view vista del botón.
     */
    public void backtohome_action_button(View view) {

        Toast.makeText(getBaseContext(), "Volver a Casa", Toast.LENGTH_LONG).show();
        //TODO implementar este método y la clase (actBackToHome)
        /*
        Intent intent = new Intent(this, actBackToHome.class);


        startActivity(intent);

        if( Constants.SHOW_ANIMATION ) {

            overridePendingTransition(R.animator.animation2, R.animator.animation1);

        }
        */
    }

    /**
     * Botón para activar el modo ducha
     * Activa el modo ducha
     *
     * @param view vista del botón.
     */
    public void showermode_action_button(View view) {

        Intent intent = new Intent(this, actModoDucha.class);

        startActivity(intent);

        if (Constants.SHOW_ANIMATION) {

            overridePendingTransition(R.anim.animation2, R.anim.animation1);

        }

    }


    /**
     * Botón para acceder a los contactos familiares
     * Atajo a los contactos familiares
     *
     * @param view Vista del botón
     */
    public void familiar_action_button(View view) {

        Intent intent = new Intent(this, actUserOptionsPersonaContacto.class);

        startActivity(intent);

        if (Constants.SHOW_ANIMATION) {

            overridePendingTransition(R.anim.animation2, R.anim.animation1);

        }
    }


    /**
     * Envio de los SMS a los familiares
     *
     * @param view Vista del botón
     */
    public void sendAvisoSms(View view) {

        //1. Leemos la lista de personas de contacto
        //2. Comprobamos el tiempo transcurrido desde el último SMS enviado
        //2. Se les envía SMS
        //3. Se muestra un mensaje de indicación

        Boolean hayPersonasContactoConTelefono = new AppSharedPreferences().hasPersonasContacto();

        if (!hayPersonasContactoConTelefono) {
            /*
            /////////
            //Genera una alerta en caso de que no tengamos asignados los contactos
            /////////
            //Se abre el menú de personas de contacto
            */
            AppDialog newFragment = AppDialog.newInstance(AppDialog.tipoDialogo.SIMPLE,1,
                    "Contactos no disponibles",
                    "No se han encontrado contactos almacenados. Introduzca al menos un contacto",
                    getResources().getString(R.string.aceptar),
                    "sin_uso");
            newFragment.show(getFragmentManager(),"dialog");
            //Fin del mensaje de información

        }

        Boolean hayListaContactos = new SmsLauncher(TipoAviso.AVISO).generateAndSend();


        //TODO: mejorar con el control de errores de SMS

        //Si se ha mandado algún SMS...

        if ( hayListaContactos ) {

            actualizarUltimoSMSEnviado(null);

            //Avisamos al usuario de que ha enviado el SMS con un sonido
            if (Constants.PLAY_SOUNDS) {

                PlaySound.play(R.raw.mensaje_enviado);

            }

            //Deshabilitamos el botón y cambiamos su aspecto
            view.setEnabled(false);

            view.setBackgroundResource(R.drawable.grey_button200);

            //Habilitamos el botón transcurridos unos segundos
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    SMSAlertButton.setEnabled(true);
                    SMSAlertButton.setBackgroundResource(R.drawable.red_button200);
                }
            }, Constants.SMS_SENDING_DELAY);
        }
    }

    /**
     * botón para enviar SMS de tranquilidad (I'm OK)
     *
     * @param view Vista del botón
     */
    public void sendAvisoIamOK(View view) {

        //1. Leemos la lista de personas de contacto
        //2. Se les envía SMS
        //3. Se muestra un mensaje de indicación

        Boolean hayPersonasContactoConTelefono = new AppSharedPreferences().hasPersonasContacto();

        if (!hayPersonasContactoConTelefono) {

            AppDialog newFragment = AppDialog.newInstance(AppDialog.tipoDialogo.SIMPLE,1,
                    "Contactos no disponibles",
                    "No se han encontrado contactos almacenados. Introduzca al menos un contacto",
                    getResources().getString(R.string.aceptar),
                    "sin_uso");
            newFragment.show(getFragmentManager(),"dialog");
            //Fin del mensaje de información

        }

        //Operación de envío de SMS

        String[] personasContacto = new AppSharedPreferences().getPersonasContacto();
        SmsLauncher miSmsLauncher = new SmsLauncher(TipoAviso.IAMOK);

        Boolean hayListaContactos = new SmsLauncher(TipoAviso.IAMOK).generateAndSend();


        //Si se ha mandado algún SMS...

        if ( hayListaContactos ) {

            actualizarUltimoSMSEnviado(null);

            //Avisamos al usuario de que ha enviado el SMS con un sonido
            if (Constants.PLAY_SOUNDS) {

                PlaySound.play(R.raw.mensaje_enviado);

            }

            //Deshabilitamos el botón y cambiamos su aspecto
            view.setEnabled(false);

            view.setBackgroundResource(R.drawable.iam_ok_pressed);

            //Habilitamos el botón transcurridos unos segundos
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    SMSOKButton.setEnabled(true);
                    SMSOKButton.setBackgroundResource(R.drawable.iam_ok);
                }
            }, Constants.SMS_SENDING_DELAY);

        }

    }


    public void actualizarUltimoSMSEnviado (Date date) {

        //Actualizamos el tiempo del envío del mensaje
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd-MM-yyyy 'a las' HH:mm:ss");
        String currentDateandTime;
        if (date == null) currentDateandTime = sdf.format(new Date());
        else currentDateandTime = sdf.format(date);

        //Mostramos el texto en la pantalla
        TextView tvUltimoSMSEnviado = (TextView) findViewById(R.id.tvUltimoSMSEnviado);

        tvUltimoSMSEnviado.setText("Último SMS enviado el " + currentDateandTime);

        new AppSharedPreferences().setPreferenceData(Constants.NOMBRE_APP_SHARED_PREFERENCES_DATETIME_ULTIMO_SMS_ENVIADO, currentDateandTime );
    }



    //Implementación del interfaz de diálogo
    public void onAccionNeutral(DialogFragment dialog){

        Intent intent = new Intent(this, actUserOptionsPersonaContacto.class);

        startActivity(intent);

        if (Constants.SHOW_ANIMATION) {

            overridePendingTransition(R.anim.animation2, R.anim.animation1);

        }

    }

}