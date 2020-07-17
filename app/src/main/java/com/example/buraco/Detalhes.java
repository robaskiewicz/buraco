package com.example.buraco;

import androidx.annotation.NonNull;
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
import android.os.Environment;
import android.util.Base64;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Detalhes extends AppCompatActivity implements OnMapReadyCallback {

    Solicita solicita = null;
    private MapView mapView;
    private GoogleMap gmap;
    Double latitude, longitude;
    private static final String MAP_VIEW_BUNDLE_KEY = "";
    ImageView imageView1 ;



    private DatabaseReference databaseReference;
    public List<Solicita> solicitacoes= new ArrayList<>();
    private FirebaseUser user;

    private Solicita sol = new Solicita();
    StorageReference mStorageRef;
    private String urlll ="";


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        imageView1 = (ImageView) findViewById(R.id.imageView3);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("ID_SOLICITA")) {

            //pega a chave do itemPara visualizar
            String chave =  bundle.getString("ID_SOLICITA");

            user = FirebaseAuth.getInstance().getCurrentUser();
            //pega o caminho dos nos no FireBase diretamente na chave do Item
            databaseReference = FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("solicitacao").child(chave);

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    TextView titulo = (TextView) findViewById(R.id.textTitulo);
                    titulo.setText((String) dataSnapshot.child("titulo").getValue());

                    TextView desc = (TextView) findViewById(R.id.textDescricao);
                    desc.setText((String) dataSnapshot.child("descricao").getValue());

                    TextView cep = (TextView) findViewById(R.id.textCep);
                    cep.setText((String) dataSnapshot.child("cep").getValue());

                    String imagem1 = (String) dataSnapshot.child("imagem1").getValue();
                    String imagem2 = (String) dataSnapshot.child("imagem2").getValue();

                    Double loclongitude = (Double) dataSnapshot.child("longitude").getValue();
                    Double loclatitude = (Double) dataSnapshot.child("latitude").getValue();

                    sol.setTitulo((String) dataSnapshot.child("titulo").getValue());
                    sol.setDescricao((String) dataSnapshot.child("descricao").getValue());


                    if (loclongitude != null && loclatitude != null) {
                    Bundle mapViewBundle = null;
                        if (savedInstanceState != null) {
                            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
                        }
                            latitude = loclatitude;
                            longitude = loclongitude;

                            mapView = findViewById(R.id.mapView);
                            mapView.onCreate(mapViewBundle);
                            mapView.getMapAsync(Detalhes.this);
                    }

                    // decodifica a imagem vindo do firebase
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    byte[] imageBytes = outputStream.toByteArray();

                    urlll = dataSnapshot.child("imagem1").getValue().toString();
                    imageBytes = Base64.decode(dataSnapshot.child("imagem1").getValue().toString(), Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                    imageView1.setImageBitmap(bitmap);



                    ImageView btnSend = (ImageView) findViewById(R.id.imageView3);
                    btnSend .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Dialog builder = new Dialog(Detalhes.this);
                            builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            builder.getWindow().setBackgroundDrawable(
                                    new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                }
                            });
                            imageView1 = new ImageView(Detalhes.this);

                            // decodifica a imagem vindo do firebase
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            byte[] imageBytes = outputStream.toByteArray();
                            imageBytes = Base64.decode(urlll, Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                            imageView1.setImageBitmap(bitmap);
                            builder.addContentView(imageView1, new RelativeLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT));
                            builder.show();
                        }
                    });



                    }


                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    //Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        }


    }

    public void enviarEmail(View view) {
        String[] TO = {"emailQueVaiReceber@gmail.com"};
//        String[] CC = {"micherobask@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");

        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
//        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, sol.getTitulo());
        emailIntent.putExtra(Intent.EXTRA_TEXT, sol.getDescricao());

        try {
            startActivity(Intent.createChooser(emailIntent, "Enviar mail..."));
            finish();
            Toast.makeText(this, "Envio de e-mail concluído...", Toast.LENGTH_SHORT).show();

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

//    public void verImagem(View view) {
//        Dialog builder = new Dialog(this);
//        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        builder.getWindow().setBackgroundDrawable(
//                new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//            }
//        });
//         imageView1 = new ImageView(this);
//
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 3;
//        Bitmap bitmap = BitmapFactory.decodeFile(solicita.getImagem1(), options);
//        imageView1.setImageBitmap(bitmap);
//        builder.addContentView(imageView1, new RelativeLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT));
//        builder.show();
//    }
}