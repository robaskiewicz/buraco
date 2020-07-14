package com.example.buraco;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.buraco.model.Solicita;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class ListaAdapter extends BaseAdapter {

    Context context;
    List<Solicita> solicitacoes;


    public ListaAdapter(Context context, List<Solicita> solicitacoes) {
        this.context = context;
        this.solicitacoes = solicitacoes;
    }

    @Override
    public int getCount() {
        return solicitacoes.size();
    }

    @Override
    public Object getItem(int position) {
        return solicitacoes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return solicitacoes.get(position).getId();
    }

    public String getChave(int position) {
        return solicitacoes.get(position).getChave();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View viewLinha = LayoutInflater.from(context).inflate(R.layout.linha, parent, false);
        Solicita solicita = solicitacoes.get(position);
        String chave = solicita.getChave();

        TextView titulo = (TextView) viewLinha.findViewById(R.id.textTituloLinha);
        titulo.setText(solicita.getTitulo());

        return viewLinha;
    }
}
