package com.example.gps;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;

public class MainActivity extends AppCompatActivity {

    LocationManager lm;
    LocationListener llis;
    Button b,b1;
    ImageView iv;
    public static final int CAM_REQUEST_CODE = 8874;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1= findViewById(R.id.button);

        //init the location manager class

        lm=(LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //use location listener
        llis= new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                //logic to display current location
                Toast.makeText(getApplicationContext(),"Lattitude is "+location.getLatitude()+"\n"+location.getLongitude(),Toast.LENGTH_LONG).show();
            }
        };
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},4);
                    return;
                }
                lm.requestLocationUpdates(lm.GPS_PROVIDER,0,0,llis);

            }
        });
        b = findViewById(R.id.button2);
            iv= findViewById(R.id.imageView);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i , CAM_REQUEST_CODE);


            }
        });
    }
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
            if(requestCode==4)
            {
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                            lm.requestLocationUpdates(lm.GPS_PROVIDER,0,0,llis);
                        }
                }
                else {
                    Toast.makeText(MainActivity.this,"Permission Denied!!! Can't Access Location ",Toast.LENGTH_LONG).show();
                }
            }
    }
    protected void onActivityResult(int requestCode , int resultCode , Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAM_REQUEST_CODE)
        {
            Bitmap image =(Bitmap) data.getExtras().get("data");
            iv.setImageBitmap(image);
        }
    }
}