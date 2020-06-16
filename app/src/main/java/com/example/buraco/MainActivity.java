package com.example.buraco;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btnAlerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAlerta = (Button)findViewById(R.id.btnAlerta);
         btnAlerta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage("991422929",null,"TESTE 123",null,null);
                    Toast.makeText(MainActivity.this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Toast.makeText(MainActivity.this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void chamaTelaConfiguracoes(View view){
        Intent intent = new Intent(this, Conf.class);
        startActivity(intent);

    }



    public void acionaAlerta(View view){
        /*Ler as configurações
            - telefone
            - mensagem
            - email
            - whatsapp
            - sms
            - localização > ligar gps epagar posição atual; (cadastrar tempo de envio localização)
            -
        */
    }




}
