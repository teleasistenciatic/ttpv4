package com.local.android.teleasistenciaticplus.lib.bateria;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.BatteryManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.local.android.teleasistenciaticplus.R;
import com.local.android.teleasistenciaticplus.act.main.actMain;
import com.local.android.teleasistenciaticplus.lib.helper.AppLog;
import com.local.android.teleasistenciaticplus.lib.helper.AppSharedPreferences;
import com.local.android.teleasistenciaticplus.lib.sound.SintetizadorVoz;
import com.local.android.teleasistenciaticplus.modelo.GlobalData;

/**
 * Created by MORUGE on 14/05/2015.
 */
public class MonitorBateria
{
    // Atributos de la clase.
    private static Boolean activarAlInicio = false, receiverActivado = false, notificado = false;
    private static int nivelAlerta=0, nivel = 0, estado = 0;
    private static BroadcastReceiver mBatInfoReceiver = null;

    // Constructor sin parámetros.
    public MonitorBateria()
    {
        // Llamo al método que lee de las SharedPreferences y asigna los valores iniciales.
        cargaPreferencias();

        // El receiver de eventos de bateria, declarado inline por exigencias androidianas
        mBatInfoReceiver = new BroadcastReceiver() // Terminado
        {
            // private int nivel, estado;

            @Override
            public void onReceive( final Context context, final Intent intent )
            {
                // Extraigo los datos de nivel de carga y estado de batería del intent recibido.
                nivel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                estado = intent.getIntExtra(BatteryManager.EXTRA_STATUS,0);
                // Actualizo datos del Layout y compruebo que el nivel de la batería esté por encima
                // del nivel de alerta y que la batería no esté en carga.
                // mostrarDatos(nivel, estado);
                if((nivel<=nivelAlerta && estado != BatteryManager.BATTERY_STATUS_CHARGING))
                    // Lanzo una notificación
                    notificacion();
                // Si estoy cargando la batería reestablezco el flag de notificado a false.
                if(estado == BatteryManager.BATTERY_STATUS_CHARGING)
                    notificado = false;
            }
        };

        if(activarAlInicio) // Activo el receiver si está configurado arrancarlo al inicio.
            activaReceiver();
    }

    private static void cargaPreferencias() // Termminado
    {
        // Saco el nivel de alerta y la opción de si se debe iniciar el receiver con la actividad.
        AppSharedPreferences miSharedPref = new AppSharedPreferences();
        if(miSharedPref.hasPreferenceData("NivelAlerta") && miSharedPref.hasPreferenceData("ActivarAlInicio"))
        {
            // Hay valores guardados, los leo
            nivelAlerta = Integer.parseInt(miSharedPref.getPreferenceData("NivelAlerta"));
            activarAlInicio = Boolean.parseBoolean(miSharedPref.getPreferenceData("ActivarAlInicio"));
        }
        else
        {
            // No hay valores guardados, pongo valores por defecto.
            activarAlInicio = false;
            nivelAlerta = 30;
        }
        AppLog.i("OjeadorBateria", "Preferencias cargadas: nivelAlerta = " + nivelAlerta + ", activarAlInicio = " + activarAlInicio);
    }

    private static void guardaPreferencias() // Terminado
    {
        // Creo un editor para guardar las preferencias.
        AppSharedPreferences miSharedPref = new AppSharedPreferences();
        miSharedPref.setPreferenceData("NivelAlerta", Integer.toString(nivelAlerta));
        miSharedPref.setPreferenceData("ActivarAlInicio", Boolean.toString(activarAlInicio));
        Toast.makeText(GlobalData.getAppContext(), "Configuración Guardada", Toast.LENGTH_SHORT).show();
        Log.i("guardaPreferencias","Preferencias guardadas con valores: nivelAlerta = " +
                nivelAlerta + ", activarAlInicio = " + activarAlInicio);
    }

    public String textoEstado() // Terminado
    {
        String strEstado;
        switch(estado)
        {
            case BatteryManager.BATTERY_STATUS_CHARGING:
                strEstado = "Bateria en carga...";
                break;
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                strEstado = "Descargando bateria...";
                break;
            case BatteryManager.BATTERY_STATUS_FULL:
                strEstado = "Bateria a plena carga...";
                break;
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                strEstado = "La bateria no esta cargando...";
                break;
            case BatteryManager.BATTERY_STATUS_UNKNOWN:
                strEstado = "Estado de la bateria desconocido...";
                break;
            default:
                strEstado = "Figureseeeeeeeee...";
                break;
        }
        return strEstado;
    }

    public String textoNivel() // Terminado
    {
        return "Nivel de carga: " + String.valueOf(nivel) + "%";
    }

    public void activaReceiver() // Terminado
    {
        if(receiverActivo())
            // El receiver está activo, devuelvo un aviso.
            Toast.makeText(GlobalData.getAppContext(),"El Monitor de Batería ya está Activo",Toast.LENGTH_SHORT).show();
        else
        {
            // Registro el receiver para activarlo.
            GlobalData.getAppContext().registerReceiver(mBatInfoReceiver,
                    new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            receiverActivado = true;
            Toast.makeText(GlobalData.getAppContext(),"Monitor Batería Activado",Toast.LENGTH_SHORT).show();
        }
    }

    public void desactivaReceiver() // Terminado
    {
        if(receiverActivo())
        {
            // La variable de control me dice que el receiver está registrado, lo quito.
            GlobalData.getAppContext().unregisterReceiver(mBatInfoReceiver);
            receiverActivado = false;
            /*************** Estos controles deben estar en la actividad de configuracion *********
            tvNivel.setText("Sin recepción de datos");
            tvEstado.setText("Monitor de batería");
            tvReceiver.setText("Desactivado");
            **************************************************************************************/
            Toast.makeText(GlobalData.getAppContext(), "Monitor Batería Desactivado", Toast.LENGTH_SHORT).show();
        }
        else
            // El receiver está desactivado, lo aviso.
            Toast.makeText(GlobalData.getAppContext(),"El Monitor de Batería ya está inactivo",Toast.LENGTH_SHORT).show();
    }

    public Boolean receiverActivo() { return receiverActivado; } // Terminado
    public int getNivelAlerta() { return nivelAlerta; } // Terminado
    public void setNivelAlerta(int alertLevel) { nivelAlerta = alertLevel; } // Terminado
    public Boolean getActivarAlInicio() { return activarAlInicio; } // Terminado
    public void setActivarAlInicio(Boolean alInicio) { activarAlInicio = alInicio; }
    public void commit(){ guardaPreferencias(); }

    public void notificacion() // Terminado
    {
        // Pillo el servicio de notificaciones del sistema.
        NotificationManager notificador = (NotificationManager)GlobalData.getAppContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if(!notificado)
        {
            int idNotificacion=0;
            // Construcción de la notificación
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(GlobalData.getAppContext())
                    .setSmallIcon(R.drawable.logo_transparente_1x1)
                    .setContentText(textoNivel())
                            // .setContentInfo(creaCadenaEstado(estado))
                    .setContentTitle("POCA BATERIA")
                    .setLargeIcon(BitmapFactory.decodeResource(GlobalData.getAppContext().getResources(),
                            R.drawable.logo_transparente_1x1))
                    .setAutoCancel(true)
                            // Asigno Intent vacio para que al pulsar quite la notificacion pero no haga nada.
                    .setContentIntent(PendingIntent.getActivity(
                            GlobalData.getAppContext().getApplicationContext(), 0, new Intent(), 0));
            //.setContentIntent(resultPendingIntent);

            // Lanzo la notificación.
            Notification notif = mBuilder.build();
            notif.defaults |= Notification.DEFAULT_VIBRATE;
            //notif.defaults |= Notification.DEFAULT_SOUND;
            notificador.notify(idNotificacion, notif);
            Log.i("Notificador", "Notificacion lanzada con id = " + idNotificacion);
            notificado = true;

            // Lanzo también el nivel de batería por voz.
            SintetizadorVoz loro = actMain.getInstance().getSintetizador();
            loro.hablaPorEsaBoquita(textoNivel()+". Por favor, ponga el móvil a cargar.");
        }
    }
}

///////////////////////////////////////////////////////////////////////
// El siguiente codigo crea una llamada a una actividad que será
// llamada al pulsar sobre la notificación generada, y además crea
// la pila de tareas para la navegación generada por la notificación,
// con el fin de saber a que actividad debe volver cuando pulsemos la
// tecla de volver o una posible acción "salir" de un menú. Se deja
// comentado para un posible uso futuro.
///////////////////////////////////////////////////////////////////////
        /*
        // Creo el Intent que llamará a la activity de configuracion.
         Intent lanzaConfig = new Intent(contexto,Configurador.class);

        // Creo la pila de navegación de notificación.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(GlobalData.getAppContext());

        // Añade la pila de vuelta atrás para el intent, pero no el intent en si mismo,
        stackBuilder.addParentStack(Configurador.class);

        // Añade el intent que lanza la actividad en el top de la pila
        stackBuilder.addNextIntent(lanzaConfig);

        // Obtengo el PendingIntent para la notificación y lo agrego al Builder.
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);
        */
