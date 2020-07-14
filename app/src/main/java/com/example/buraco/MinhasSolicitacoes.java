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

public class MinhasSolicitacoes extends AppCompatActivity {


    private DatabaseReference databaseReference;
    public List<Solicita> solicitacoes= new ArrayList<>();
    private FirebaseUser user;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_solicitacoes);
        user = FirebaseAuth.getInstance().getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("solicitacao");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                solicitacoes.clear();
                Integer i = 0;

                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {

                    Solicita sol = new Solicita();

                    String chave = (String) messageSnapshot.child("chave").getValue();
                    String titulo = (String) messageSnapshot.child("titulo").getValue();

                    i= i+1;
                    sol.setId(Long.valueOf(i));
                    sol.setTitulo(titulo);
                    sol.setChave(chave);

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





    }

    public void chamarDetalhes(View view) {
        Intent itt = new Intent(this, Detalhes.class);
        startActivity(itt);
    }



//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//        Log.i("LOGGGGGGGGGGGGGGG", (String) listView.getItemAtPosition(position));
//
//
//        Intent intent = new Intent(this, Detalhes.class);
//        intent.putExtra("ID_SOLICITA", listView.getItemAtPosition(position).toString());
//        startActivity(intent);
//
////        Log.i("LOGGGGGGGGGGGGGGG", (String) parent.getSelectedItem());
////        Solicita solicita = (Solicita) parent.getSelectedItem();
////        Intent intent = new Intent(this, Detalhes.class);
////        intent.putExtra("ID_SOLICITA",solicita.getId());
////        startActivity(intent);
//    }
//
//
//    @Override
//    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//        final Solicita solicita = (Solicita) parent.getItemAtPosition(position);
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("Deseja excluir essa Solicitação?").setPositiveButton("Sim", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                //remover
//                Solicita.delete(solicita);
//                carregaListaMinhasSolicitacoes();
//            }
//        }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//        builder.show();
//
//        return true;
//    }



    private void carregaListaMinhasSolicitacoes(){
//        List<Solicita> solicitacoes =  Solicita.find(Solicita.class, "id>0");
//        ListaAdapter adapter = new ListaAdapter(this, solicitacoes);
//        listView.setAdapter(adapter);


        listView = (ListView) findViewById(R.id.listViewRegistros);

        ListaAdapter adapter = new ListaAdapter(MinhasSolicitacoes.this, solicitacoes);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Log.i("LOGGGGGGGGGGGGGGG",  listView.getItemAtPosition(position).toString());

                Solicita sol = (Solicita) listView.getItemAtPosition(position);

                Intent intent = new Intent(MinhasSolicitacoes.this, Detalhes.class);
                intent.putExtra("ID_SOLICITA", sol.getChave());
                startActivity(intent);
            }
        });
    }

}
