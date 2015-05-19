package com.local.android.teleasistenciaticplus.act.user;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.local.android.teleasistenciaticplus.R;
import com.local.android.teleasistenciaticplus.lib.bateria.MonitorBateria;
import com.local.android.teleasistenciaticplus.act.main.actMain;

public class actUserOptionsMonitorBateria extends Activity {
    private static TextView tvEstado, tvNivel, tvReceiver;
    private static EditText etNivelAlerta;
    private static Button btnLanzarReceiver, btnPararReceiver, btnAplicar, btnSalir;
    private static CheckBox cbIniciarAuto;
    // Pillo el monitor de batería
    private static MonitorBateria monitor = actMain.getInstance().getMonitorBateria();

    @Override
    public void onCreate(Bundle savedInstanceState) // Terminado
    {
        // Acciones a ejecutar al crear la actividad
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_user_option_monitor_bateria);

        // Inicializo el layout
        tvEstado = (TextView) findViewById(R.id.tvEstado);
        tvNivel = (TextView) findViewById(R.id.tvNivel);
        tvReceiver = (TextView) findViewById(R.id.tvReceiver);

        // Muestro los datos que tengo.
        mostrarDatos();

        etNivelAlerta = (EditText) findViewById(R.id.etNivelAlerta);
        etNivelAlerta.setText(Integer.toString(monitor.getNivelAlerta()));

        btnLanzarReceiver = (Button) findViewById(R.id.btnLanzarReceiver);
        btnLanzarReceiver.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                monitor.activaReceiver();
                mostrarDatos();
            }
        });

        btnPararReceiver = (Button) findViewById(R.id.btnPararReceiver);
        btnPararReceiver.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                monitor.desactivaReceiver();
                mostrarDatos();
            }
        });

        cbIniciarAuto = (CheckBox) findViewById(R.id.cbIniciarAuto);
        cbIniciarAuto.setChecked(monitor.getActivarAlInicio());

        /**************** No necesito recoger los eventos de marcar/desmarcar la casilla **********
         cbIniciarAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()

         {
         @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
         {
         activarAlInicio = isChecked;
         Log.i("onCheckedChanged","Establecido activarAlInicio a " + isChecked);
         }
         });
         ******************************************************************************************/

        btnAplicar = (Button) findViewById(R.id.btnAplicar);
        btnAplicar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Actualizo el valor de los atributos afectados por cambios.
                String alerta = etNivelAlerta.getText().toString();
                if (!alerta.isEmpty())
                    monitor.setNivelAlerta(Integer.parseInt(alerta));
                monitor.setActivarAlInicio(cbIniciarAuto.isChecked());
                monitor.commit();
                mostrarDatos();
            }
        });

        btnSalir = (Button) findViewById(R.id.btnSalir);
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static void mostrarDatos() // Terminado
    {

        if (monitor.receiverActivo())
        {
            tvNivel.setText(monitor.textoNivel());
            tvEstado.setText(monitor.textoEstado());
            tvReceiver.setText("Monitor Batería Activado");
        }
        else
        {
            tvNivel.setText("Sin recepción de datos");
            tvEstado.setText("Monitor de batería");
            tvReceiver.setText("Desactivado");
        }
    }
}



