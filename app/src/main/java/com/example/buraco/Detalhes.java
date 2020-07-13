package com.example.buraco;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buraco.model.Solicita;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Detalhes extends AppCompatActivity implements OnMapReadyCallback {

    Solicita solicita = null;
    private MapView mapView;
    private GoogleMap gmap;
    Double latitude, longitude;
    private static final String MAP_VIEW_BUNDLE_KEY = "";
    ImageView imageView1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("ID_SOLICITA")) {

            Long id = bundle.getLong("ID_SOLICITA");
            solicita = Solicita.findById(Solicita.class, id);

            TextView titulo = (TextView) findViewById(R.id.textTitulo);
            titulo.setText(solicita.getTitulo());

            TextView data = (TextView) findViewById(R.id.textData);
            DateFormat datinha = new SimpleDateFormat("dd/MM/yyyy");
            data.setText(datinha.format(solicita.getDataCadastro()));

            TextView descricao = (TextView) findViewById(R.id.textDescricao);
            descricao.setText(solicita.getDescricao());

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 3;
            if (solicita.getImagem1() != null && solicita.getImagem1().length() > 2) {
                Bitmap bitmap = BitmapFactory.decodeFile(solicita.getImagem1(), options);
                ImageView imageView1 = (ImageView) findViewById(R.id.imageView1);
                imageView1.setBackground(null);
                imageView1.setImageBitmap(bitmap);
            }


            if (solicita.getLongitude() != null && solicita.getLongitude() != null) {
                Bundle mapViewBundle = null;
                if (savedInstanceState != null) {
                    mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
                }

                latitude = solicita.getLatitude();
                longitude = solicita.getLongitude();

                Toast.makeText(this, "Latitude!" +  solicita.getLatitude() + "Longitude!" + solicita.getLongitude(), Toast.LENGTH_SHORT).show();


                mapView = findViewById(R.id.mapView);
                mapView.onCreate(mapViewBundle);
                mapView.getMapAsync(this);
            }
        }


    }

    public void enviarEmail(View view) {
        Log.i("Send email", "");

        String[] TO = {"emailQueVaiReceber@gmail.com"};
//        String[] CC = {"micherobask@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");

        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
//        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, solicita.getTitulo());
        emailIntent.putExtra(Intent.EXTRA_TEXT, solicita.getDescricao());

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Toast.makeText(this, "Finished sending email...", Toast.LENGTH_SHORT).show();

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Detalhes.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        LatLng ny = new LatLng(latitude, longitude);
        gmap.moveCamera(CameraUpdateFactory.newLatLng(ny));
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(ny);
        gmap.addMarker(markerOptions);
    }

    public void verImagem(View view) {
        Dialog builder = new Dialog(this);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
            }
        });
        ImageView imageView = new ImageView(this);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 3;
        Bitmap bitmap = BitmapFactory.decodeFile(solicita.getImagem1(), options);
        imageView.setImageBitmap(bitmap);
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        builder.show();
    }
}