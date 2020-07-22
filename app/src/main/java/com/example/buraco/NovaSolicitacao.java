package com.example.buraco;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.buraco.model.Solicita;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NovaSolicitacao extends AppCompatActivity {

    private String currentPhotoPath;
    private static final int REQUEST_TAKE_PHOTO = 105;
    private int foto = 0;
    private Solicita solicita = new Solicita();
    ImageView imageView1 ;
    ImageView imageView2 ;
    private DatabaseReference databaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_solicitacao);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        imageView1 = (ImageView) findViewById(R.id.imageView1);
//        imageView2 = (ImageView) findViewById(R.id.imageView2);
        databaseReference = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void salvarSolicitacao(View view) {
        EditText titulo = (EditText) findViewById(R.id.inputTitulo);
        solicita.setTitulo(titulo.getText().toString());

        EditText desc = (EditText) findViewById(R.id.inputDesc);
        solicita.setDescricao(desc.getText().toString());

        EditText cep = (EditText) findViewById(R.id.inputCep);
        solicita.setCep(cep.getText().toString());

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                solicita.setLatitude(location.getLatitude());
                solicita.setLongitude(location.getLongitude());
            }
        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            solicita.setLatitude(location.getLatitude());
            solicita.setLongitude(location.getLongitude());
        }

        Toast.makeText(this, "Latitude!" + solicita.getLatitude() + "Longitude!" + solicita.getLongitude(), Toast.LENGTH_SHORT).show();



       // solicita.save();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        solicita.setEmail(user.getEmail()); // adiciona o E-mail que ja foi usado para logar no sistema
        String chave = databaseReference.child(user.getUid()).child("solicitacao").push().getKey();
        solicita.setChave(chave);
        databaseReference.child(user.getUid()).child("solicitacao").child(chave).setValue(solicita);

        enviarEmail();
        Toast.makeText(this, "Salvo com sucesso!", Toast.LENGTH_SHORT).show();
        finish();

    }

    private File createImageFile() throws IOException {
        // Create an image file name

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        storageDir.mkdir();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 105:{
                if (resultCode == RESULT_OK){
                    try {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 3;
                        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, options);

                        //Converter o bitmap em Base64 (string), que é útil para mandar a foto para um WS.
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, outputStream);
                        byte[] binario = outputStream.toByteArray();
                        String fotoString = Base64.encodeToString(binario, Base64.DEFAULT);

                        if(foto==1) {
                            imageView1.setImageBitmap(bitmap);
                            imageView1.setBackground(null);
                            solicita.setImagem1(fotoString);
                        }
//                        else if(foto==2){
//                            imageView2.setImageBitmap(bitmap);
//                            imageView2.setBackground(null);
//                            solicita.setImagem2(fotoString);
//                        }

                    }catch (Exception i){
                        Log.e("NovaSolicitacao", "DEU ERRO!!!!");
                    }
                }
                break;
            }
        }

    }

    private void chamarCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.buraco",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    public void chamarCamera1(View view){
        foto = 1;
        chamarCamera();
    }

    public void chamarCamera2(View view){
//        foto = 2;
//        chamarCamera();
    }


    protected void enviarEmail() {
        String[] TO = {"michelrobask@gmail.com"};
//        String[] CC = {"micherobask@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
//        emailIntent.setType("image/jpeg");

        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
//        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, solicita.getTitulo());
        emailIntent.putExtra(Intent.EXTRA_TEXT, "DESCRIÇÃO:" +solicita.getDescricao());
        emailIntent.putExtra(Intent.EXTRA_TEXT, "CEP:" +solicita.getCep());
        String localidadeGps = "Buraco nessa Localização: http://maps.google.com/maps?q=" + solicita.getLatitude().toString() + "," + solicita.getLongitude().toString();
        emailIntent.putExtra(Intent.EXTRA_TEXT, "LOCALIZAÇÃO:" + localidadeGps);

        try {
            startActivity(Intent.createChooser(emailIntent, "Enviar mail..."));
            finish();
            Toast.makeText(this, "Envio de e-mail...", Toast.LENGTH_SHORT).show();

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(NovaSolicitacao.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }



}
