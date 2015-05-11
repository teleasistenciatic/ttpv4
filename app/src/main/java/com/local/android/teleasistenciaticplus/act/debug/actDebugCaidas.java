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

public class actDebugCaidas extends Activity {

    private Intent intent;
    private TextView texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_debug_caidas);

        //arrancar servicio que comprueba las caidas
     //   Intent intent = new Intent(this, ServicioMuestreador.class);
     //   startService(intent);
        intent=new Intent(this, ServicioMuestreador.class);
        texto=(TextView) findViewById(R.id.textoEstadoServicio);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_debug_caidas, menu);
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

    public void onClick(View v){
        switch(v.getId()){
            case R.id.button1:
                Log.i("CAIDAS", "caidas boton 1");
                startService(intent);
                texto.setText("Servicio Iniciado");
                break;
            case R.id.button2:
                Log.i("CAIDAS", "caidas boton 2");
                boolean que=stopService(intent);
                Log.i("CAIDAS","caidas servicio parado? "+que);
                texto.setText("Servicio Parado");
                break;

        }
    }
}
