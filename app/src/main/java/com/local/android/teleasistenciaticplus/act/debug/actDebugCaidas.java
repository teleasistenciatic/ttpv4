package com.local.android.teleasistenciaticplus.act.debug;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.local.android.teleasistenciaticplus.R;
import com.local.android.teleasistenciaticplus.lib.detectorCaidas.ServicioMuestreador;
import com.local.android.teleasistenciaticplus.lib.helper.AppLog;

public class actDebugCaidas extends Activity {

    private Intent intent;
    private TextView texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_debug_caidas);

        //arrancar servicio que comprueba las caidas
        intent=new Intent(this, ServicioMuestreador.class);
        texto=(TextView) findViewById(R.id.textoEstadoServicio);
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.button1:
                startService(intent);
                texto.setText("Servicio Iniciado");
                break;
            case R.id.button2:
                boolean que=stopService(intent);
                AppLog.i("CAIDAS","caidas servicio parado? "+que);
                texto.setText("Servicio Parado");
                break;

        }
    }
}
