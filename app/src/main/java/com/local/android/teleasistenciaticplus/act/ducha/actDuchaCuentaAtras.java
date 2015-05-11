package com.local.android.teleasistenciaticplus.act.ducha;

import android.app.DialogFragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.local.android.teleasistenciaticplus.R;
import com.local.android.teleasistenciaticplus.act.main.actMain;
import com.local.android.teleasistenciaticplus.lib.helper.AppDialog;
import com.local.android.teleasistenciaticplus.lib.playsound.PlaySound;
import com.local.android.teleasistenciaticplus.lib.sms.SmsLauncher;
import com.local.android.teleasistenciaticplus.modelo.Constants;
import com.local.android.teleasistenciaticplus.modelo.GlobalData;
import com.local.android.teleasistenciaticplus.modelo.TipoAviso;

import java.util.Date;

public class actDuchaCuentaAtras extends FragmentActivity implements AppDialog.AppDialogNeutralListener {

    //TAG para depuración
    private final String TAG = getClass().getSimpleName();

    private CountDownTimer TheCountDown; //clase para la cuenta atrás

    //párametros para la clase CountDownTimer
    private int futureTime; //tiempo total de la cuenta atrás
    private int interval;  //intervalo de refresco del minutero

    //Referencia al layout para modificar el color de fondo
    private RelativeLayout rl;
    private boolean changeBgColor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_act_ducha_cuenta_atras);

        rl = (RelativeLayout) findViewById(R.id.miRelativeLayout);
        changeBgColor = true;

        //Recuperamos del intent los minutos seleccionados por el usuario
        futureTime = (int) getIntent().getExtras().get("minutos");

        //El intervalo de refresco se estable a segundos
        interval = 1000;

        //Iniciamos la cuenta atrás
        startCountDown();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_ducha_cuenta_atras, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void startCountDown() {

        //Capturamos el tiempo (en minutos) introducido por el usuario
        final TextView mTextField = (TextView) findViewById(R.id.mTextField);

        futureTime = futureTime * 60000;

        //Generamos una notificación sonora
        final Notification beep_sound = new Notification.Builder(getApplicationContext())
                .setSound(Uri.parse("android.resource://" + GlobalData.getAppContext().getPackageName() + "/" + R.raw.beep_07))
                .build();

        final NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        //futureTime = 5000;
        TheCountDown = new CountDownTimer(futureTime, interval) {

            Boolean alarmaDisparada = false;

            @Override
            public void onTick(long millisUntilFinished) {

                //menos de un minuto
                if (millisUntilFinished  < 60000) {
                    mTextField.setText("00:" + millisUntilFinished / 1000);

                    if (millisUntilFinished  < 10000) {
                        mTextField.setText("00:0" + millisUntilFinished / 1000);
                    }

                    notificationManager.notify(0, beep_sound);

                    rl.setBackgroundColor(  (changeBgColor) ? 0xffff0000 : 0xff33b5e5);
                    changeBgColor = !changeBgColor;

                } else {

                    int minutos = (int) (millisUntilFinished / 60000);
                    int segundos = (int) ( ( (millisUntilFinished / 1000) - (minutos * 60)) );
                    if(millisUntilFinished > 600000) {
                        mTextField.setText("" + minutos + ":" + segundos);

                        if (segundos  < 10) {
                            mTextField.setText("" + minutos +":0" + segundos);
                        }


                    }else {
                        mTextField.setText("0" + minutos + ":" + segundos);
                        if (segundos  < 10) {
                            mTextField.setText("0" + minutos +":0" + segundos);
                        }
                    }

                }

            }

            @Override
            public void onFinish() {

                //enviamos el sms
                SmsLauncher miSmsLauncher = new SmsLauncher( TipoAviso.DUCHANOATENDIDA  );
                Boolean hayListaContactos = miSmsLauncher.generateAndSend();

                if(hayListaContactos) {

                    //mostramos diálogo de sms enviado
                    AppDialog newFragment = AppDialog.newInstance(AppDialog.tipoDialogo.SIMPLE, 1,
                            "SMS ENVIADO",
                            "Se ha enviado un SMS a sus contactos por una alerta de ducha",
                            "Cerrar",
                            "sin_uso");
                    newFragment.show(getFragmentManager(), "dialog");

                    //pasamos la fecha de envío del SMS a la actividad principal actMAin
                    actMain.getInstance().actualizarUltimoSMSEnviado(new Date());
                } else{
                    //TODO: error en el envío por no haber contactos
                }


                if( Constants.SHOW_ANIMATION ) {

                    overridePendingTransition(R.anim.animation2, R.anim.animation1);

                }

            }
        }.start();

    }

    public void cancelCountDown(View v) {

        TheCountDown.cancel();

        if( Constants.PLAY_SOUNDS ) PlaySound.play(R.raw.modo_ducha_cancelado);

        finish();

    }

    //Implementación del interfaz de diálogo
    public void onAccionNeutral(DialogFragment dialog){

        finish();

    }

}
