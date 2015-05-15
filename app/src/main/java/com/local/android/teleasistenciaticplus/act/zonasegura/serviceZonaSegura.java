package com.local.android.teleasistenciaticplus.act.zonasegura;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Servicio que comprueba la distancia con un punto dado
 */

public class serviceZonaSegura extends Service implements
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "ZonaSeguraService";

    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;

    Location mCurrentLocation;
    String mLastUpdateTime;

    int mStartMode;         //Indica como responde si el servicio se mata
    IBinder mBinder;        // interfaz

    /* Datos de posicion de zona segura */
    double zonaSeguraLatitud = 37.898;
    double zonaSeguraLongitud = -4.724;
    double zonaSeguraRadio = 50; //radio de seguridad

    /* Vector de actualizaciones de posición */

    List<PosicionTiempo> Posiciones = new ArrayList<>();

    class PosicionTiempo {

        Double latitude;
        Double longitude;
        float accuracy;
        String provider;
        String time;
        Boolean inZone;

        public PosicionTiempo(Double latitude,
                              Double longitude,
                              float accuracy,
                              String provider,
                              String time,
                              Boolean inZone) {

            this.latitude = latitude;
            this.longitude = longitude;
            this.accuracy = accuracy;
            this.provider = provider;
            this.time = time;
            this.inZone = inZone;
        }
    }


    private Timer mTimer = new Timer();
    private final Messenger mMessenger = new Messenger(new IncomingMessageHandler()); // Target we publish for clients to send messages to IncomingHandler.

    private static boolean serviceIsRunning = false;

    /**
     *
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     *
     */
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service Started.");
        Toast.makeText(getBaseContext(), (String) "Servicio iniciado",
                Toast.LENGTH_SHORT).show();

        //mTimer.scheduleAtFixedRate(new MyTask(), 0, 2000L);
        serviceIsRunning = true;

        //show error dialog if GooglePlayServices not available
        if (!isGooglePlayServicesAvailable()) {
            //TODO (ya no puede ser finish an ser un servicio)
        }
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();
    }

    /**
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // The service is starting, due to a call to startService()
        Log.i(TAG, "Received start id " + startId + ": " + intent);

        return START_STICKY; //// Run until explicitly stopped.
    }

    /**
     * @param intent
     * @return
     */
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind");
        return mMessenger.getBinder();
    }

    /**
     * @param intent
     * @return
     */
    @Override
    public boolean onUnbind(Intent intent) {
        // All clients have unbound with unbindService()
        return true; //si se puede Rebind
    }

    /**
     * @param intent
     */
    @Override
    public void onRebind(Intent intent) {
        // A client is binding to the service with bindService(),
        // after onUnbind() has already been called
    }

    /**
     *
     */
    @Override
    public void onDestroy() {
        // The service is no longer used and is being destroyed
        if (mTimer != null) {
            mTimer.cancel();
        }
        Log.i("MyService", "Service Stopped.");
        serviceIsRunning = false;

        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        Log.d(TAG, "Location update started ..............: ");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed: " + connectionResult.toString());
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Firing onLocationChanged..............................................");
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

        checkZonaSegura();

    }

    /**
     * CheckZonaSegura
     */
    private void checkZonaSegura() {

        Log.d(TAG, "Check Zona Segura initiated .............");
        if (null != mCurrentLocation) {

            /* Creación de la LatLong */
            float[] resultado = new float[3];

            Location.distanceBetween(mCurrentLocation.getLatitude(),
                    mCurrentLocation.getLongitude(),
                    zonaSeguraLatitud,
                    zonaSeguraLongitud,
                    resultado);


            boolean inSecureZone = personInSecureZone(mCurrentLocation.getLatitude(),
                    mCurrentLocation.getLongitude(),
                    zonaSeguraLatitud,
                    zonaSeguraLongitud,
                    zonaSeguraRadio, (float) mCurrentLocation.getAccuracy());

            String distancia = String.valueOf(resultado[0]);

            String lat = String.valueOf(mCurrentLocation.getLatitude());
            String lng = String.valueOf(mCurrentLocation.getLongitude());
            String mostrar = "At Time: " + mLastUpdateTime + "\n" +
                    "Latitude: " + lat + "\n" +
                    "Longitude: " + lng + "\n" +
                    "Accuracy: " + mCurrentLocation.getAccuracy() + "\n" +
                    "DISTANCIA: " + distancia + "\n" +
                    "ZONA SEGURA: " + inSecureZone + "\n" +
                    "Provider: " + mCurrentLocation.getProvider();


            PosicionTiempo miPosicionTiempo = new PosicionTiempo(mCurrentLocation.getLatitude(),
                    mCurrentLocation.getLongitude(),
                    mCurrentLocation.getAccuracy(),
                    mCurrentLocation.getProvider(),
                    mLastUpdateTime,
                    inSecureZone);

            /* Se añaden posiciones pero sólo diez */

            if ( Posiciones.size() < 9 ) {
                Posiciones.add(miPosicionTiempo);
            } else {

            }

            Log.d(TAG, mostrar);

            /* SONIDO */
            try {
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                r.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
            /* Fin sonido */

            Toast.makeText(getBaseContext(), (String) mostrar,
                    Toast.LENGTH_LONG).show();
        } else {
            Log.d(TAG, "location is null ...............");
        }
    }

    /**
     * @param pointALatitude
     * @param pointALongitude
     * @param pointBLatitude
     * @param pointBLongitude
     * @param secureZoneRadius
     * @param accuracy
     * @return
     */
    private boolean personInSecureZone(Double pointALatitude,
                                       Double pointALongitude,
                                       Double pointBLatitude,
                                       Double pointBLongitude,
                                       Double secureZoneRadius,
                                       float accuracy) {

        float[] resultado = new float[3];

        Location.distanceBetween(pointALatitude,
                pointALongitude,
                pointBLatitude,
                pointBLongitude,
                resultado);

        //Procesamos la distancia teniendo en cuenta el radio de la zona segura
        //y la precisión de la medición

        double distancia = resultado[0] + accuracy;

        // Mientras el beneficiario se encuentre a menos distancia que el radio se considera que está seguro
        // la distancia es entre el punto definido como casa y la posición actual de GPS
        // A la distancia que tenemos hay que sumarle la precisión. Si nos da 50m de distancia pero 78 metros de
        // precisión, en el peor caso estará a 50mm + 78mm.

        if ( distancia < secureZoneRadius ) {
            return true;
        } else {
            return false;
        }

    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        Log.d(TAG, "Location update stopped .......................");
    }

    public static boolean isRunning() {
        return serviceIsRunning;
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else { // España
            return false;
        }
    }


    /////////////////////////////////////////////////////////////////////////////////
    // CLASES ANIDADAS //////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////

    /**
     * Handle incoming messages from MainActivity
     */
    private class IncomingMessageHandler extends Handler { // Handler of incoming messages from clients.

        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "handleMessage: " + msg.what);
        }

    }

    private class MyTask extends TimerTask {
        @Override
        public void run() {
            Log.d(TAG, "Timer doing work.");

            try {
                //counter += incrementBy;
                //sendMessageToUI(counter);

            } catch (Throwable t) { //you should always ultimately catch all exceptions in timer tasks.
                Log.e("TimerTick", "Timer Tick Failed.", t);
            }
        }
    }
}