package com.example.buraco;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAuth = FirebaseAuth.getInstance();

        TextView textView = (TextView) findViewById(R.id.textNome);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String email = user.getEmail();
            textView.setText(email);
            String uid = user.getUid();
        }

    }


    public void chamarNovaSolicitacao(View view){
        Intent intent = new Intent(this, NovaSolicitacao.class);
        startActivity(intent);
    }

    public void chamarMinhasSolicitacoes(View view){
        Intent intent = new Intent(this, MinhasSolicitacoes.class);
        startActivity(intent);
    }

    public void sair(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Deseja sair?").setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //FirebaseAuth.getInstance().signOut();
                mAuth.signOut();
                finish();
            }
        }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

}
