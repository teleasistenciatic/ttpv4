package com.local.android.teleasistenciaticplus.act.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.local.android.teleasistenciaticplus.R;
import com.local.android.teleasistenciaticplus.lib.detectorCaidas.ServicioMuestreador;
import com.local.android.teleasistenciaticplus.lib.helper.AppSharedPreferences;
import com.local.android.teleasistenciaticplus.modelo.Constants;

public class actUserOptionsCaidas extends Activity {

    private TextView texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_user_options_caidas);

        AppSharedPreferences userSharedPreferences = new AppSharedPreferences();

        CheckBox micheckbox=(CheckBox) findViewById(R.id.caidas_checkbox);
        texto = (TextView) findViewById(R.id.caidas_texto_estado);

        String valor=userSharedPreferences.getPreferenceData(Constants.CAIDAS);
        if( valor.equals(Constants.ACTIVO)){
            micheckbox.setChecked(true);
            texto.setText(R.string.caidas_texto_estado_activo);
        }else{
            micheckbox.setChecked(false);
            texto.setText(R.string.caidas_texto_estado_inactivo);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_user_options_caidas, menu);
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
        switch(v.getId()) {
            case R.id.caidas_checkbox:
                //modificar la cte que controla el inicio del servicio al comienzo de la app.
                AppSharedPreferences userSharedPreferences = new AppSharedPreferences();
                boolean checked = ((CheckBox) v).isChecked();
                if(checked){
                    userSharedPreferences.setPreferenceData(Constants.CAIDAS,Constants.ACTIVO);
                }else{
                    userSharedPreferences.setPreferenceData(Constants.CAIDAS,Constants.INACTIVO);
                }
                break;
            case R.id.caidas_boton_arrancar:
                //arrancar el servicio ?
                Intent intentA=new Intent(this, ServicioMuestreador.class);
                startService(intentA);
                texto.setText(R.string.caidas_texto_estado_activo);
                break;
            case R.id.caidas_boton_parar:
                //parar el servicio ?
                Intent intentB=new Intent(this, ServicioMuestreador.class);
                stopService(intentB);
                texto.setText(R.string.caidas_texto_estado_inactivo);
                break;
        }
    }
}
