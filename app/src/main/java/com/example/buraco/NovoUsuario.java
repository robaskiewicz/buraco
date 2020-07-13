package com.example.buraco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NovoUsuario extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_usuario);

        mAuth = FirebaseAuth.getInstance();

    }

    public void voltarLogin(View view) {
        finish();
    }

    public  void cadastrar(View view){

        EditText editText1=(EditText) findViewById(R.id.input_email);
        EditText editText2=(EditText) findViewById(R.id.input_senha);
        if(editText2.getText().toString().length()>5) {
            mAuth.createUserWithEmailAndPassword(editText1.getText().toString(), editText2.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(NovoUsuario.this, "Conta criada com sucesso!!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(NovoUsuario.this, "Deu erro!!",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }else{
            Toast.makeText(this, "A senha precisa ter 6 caracteres ou mais!!", Toast.LENGTH_SHORT).show();

        }
    }

}
