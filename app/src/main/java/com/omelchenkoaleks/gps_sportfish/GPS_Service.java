package com.omelchenkoaleks.gps_sportfish;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class GPS_Service extends Service {

    private LocationManager locationManager;
    private LocationListener locationListener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Intent intent = new Intent("location_update");
                intent.putExtra("coordinates", location.getLongitude()
                        + " " + location.getLatitude());
                sendBroadcast(intent);

                Toast.makeText(getBaseContext(), "New location latitude [" +
                        location.getLatitude() +"] longitude [" + location.getLongitude() + "]",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        };

        /**
         * Служба системного уровня. Используем служебный метод для получения
         * службы LocationManager. В параметрах передаем имя нужной службы.
         * Эта служба предоставит детали местоположения за счет использования
         * поставщиков местоложения.
         */
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, locationListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }
}
