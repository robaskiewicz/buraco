package com.example.buraco;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.buraco.model.Solicita;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MinhasSolicitacoes extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    ListView listView;
    private DatabaseReference databaseReference;
    public List<Solicita> solicitacoes= new ArrayList<>();
    private FirebaseAuth mAuth;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_solicitacoes);

        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("solicitacao");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                solicitacoes.clear();
                Integer i = 0;

                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {

                    Solicita sol = new Solicita();
                    String chave = databaseReference.child(user.getUid()).child("solicitacao").push().getKey();
                    String chave3 = (String) messageSnapshot.child("chave").getValue();
                    String titulo = (String) messageSnapshot.child("titulo").getValue();

                    i= i+1;
                    sol.setId(Long.valueOf(i));
                    sol.setTitulo(titulo);
                    sol.setChave(chave3);

                    solicitacoes.add(sol);

                }


                carregaListaMinhasSolicitacoes();
            }



            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Solicita soli = (Solicita) parent.getItemAtPosition(position);
//                Intent intent = new Intent(this, Detalhes.class);
//                intent.putExtra("ID_SOLICITA", soli.getId());
//                startActivity(intent);
//
//                Log.e(null, String.valueOf(position)); //apenas para setar log da posição
//            }
//
//        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Solicita soli = (Solicita) parent.getItemAtPosition(position);
            Intent intent = new Intent(this, Detalhes.class);
            intent.putExtra("ID_SOLICITA", soli.getChave());
            startActivity(intent);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final Solicita solicita = (Solicita) parent.getItemAtPosition(position);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Deseja excluir essa Solicitação?").setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //remover
                Solicita.delete(solicita);
                carregaListaMinhasSolicitacoes();
            }
        }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();

        return true;
    }



    private void carregaListaMinhasSolicitacoes(){
//        List<Solicita> solicitacoes =  Solicita.find(Solicita.class, "id>0");
//        ListaAdapter adapter = new ListaAdapter(this, solicitacoes);
//        listView.setAdapter(adapter);



        ListView listView = (ListView) findViewById(R.id.listViewRegistros);
        ListaAdapter adapter = new ListaAdapter(this, solicitacoes);
        listView.setAdapter(adapter);
    }


}
