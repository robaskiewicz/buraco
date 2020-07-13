package com.example.buraco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class RecuperarSenha extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuper_senha);

        mAuth = FirebaseAuth.getInstance();

    }


    public void recuperar(View view) {
        EditText editText = (EditText) findViewById(R.id.input_emaill);
        mAuth.sendPasswordResetEmail(editText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RecuperarSenha.this, "E-mail enviado!!", Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    Toast.makeText(RecuperarSenha.this, "Algo deu errado, confira o endereço de e-mail!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
