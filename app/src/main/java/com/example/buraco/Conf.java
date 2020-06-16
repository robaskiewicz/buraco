package com.example.buraco;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Conf extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conf);
    }

    public void salvarConf(View view){

        TextView campoTel = (TextView) findViewById(R.id.campoTel);
        String campoTelefone = campoTel.getText().toString();

        TextView campoMsg = (TextView) findViewById(R.id.campoMsg);
        String campoMensagem = campoMsg.getText().toString();

        TextView campoEmail = (TextView) findViewById(R.id.campoEmail);
        String campoEmails = campoEmail.getText().toString();

        TextView campoTemp = (TextView) findViewById(R.id.campoTempo);
        String campoTempo = campoTemp.getText().toString();



        /*
        - cadastrar Telefone
        - cadastrar mensagem texto
        - cadastrar Emails
        - cadastrar tempo de reenvio da mensagem

        */
    }


}
