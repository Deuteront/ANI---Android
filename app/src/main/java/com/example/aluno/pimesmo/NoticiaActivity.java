package com.example.aluno.pimesmo;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import banco_dados.RequisicaoHTTP;
import banco_dados.resgatarImagemBanco;
import fonte_controle.Fonte;
import fonte_controle.Noticia;

public class NoticiaActivity extends AppCompatActivity {
    final RequisicaoHTTP banco=new RequisicaoHTTP();
    private SQLiteDatabase bd;
    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticia);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RequisicaoHTTP dadosViaPost = new RequisicaoHTTP();
        ArrayList<Fonte> fontes = new ArrayList<Fonte>();

        long idSelected = getIntent().getLongExtra("ID", 0);
        int positionSelected = getIntent().getIntExtra("POSITION", 0);

        try {
            fontes = dadosViaPost.doGET();
        } catch (IOException e) {
            e.printStackTrace();
        }
        bd = openOrCreateDatabase("bancoPI", MODE_PRIVATE, null);
        Cursor cursor = bd.rawQuery("SELECT idFonte,nomeFonte,fonteAssinada FROM fontes where fonteAssinada = 'Assinado' ", null);
        cursor.moveToFirst();

        ArrayList<Fonte> fontesRegistradas = new ArrayList<Fonte>();
        cursor.moveToFirst();
        do {
            Fonte fonteRegistrada = new Fonte();
            fonteRegistrada.setIdJornal(Integer.parseInt(cursor.getString(0)));
            fonteRegistrada.setFonteAssinada(cursor.getString(2));
            fonteRegistrada.setNome(cursor.getString(1));
            fontesRegistradas.add(fonteRegistrada);
        }
        while (cursor.moveToNext());


        Noticia noticia;
        ArrayList<Noticia> noticias = new ArrayList<Noticia>();
        for (int i = 0; i < fontesRegistradas.size(); i++) {
            for (int j = 0; j < fontes.size(); j++) {
                if (fontesRegistradas.get(i).getNome().equals(fontes.get(j).getNome())) {
                    for (int x=0;x<(fontes.get(j).getNoticias().size());x++){
                        Noticia noticiaa = new Noticia();
                        noticiaa = fontes.get(j).getNoticias().get(x);
                        noticiaa.setIdNoticia(x);
                        noticias.add(noticiaa);
                    }
                }

            }
        }

        noticia = noticias.get(positionSelected);
        Log.i("pqpqqq", noticia.getTitulo());
        TextView titulo =  findViewById(R.id.TituloImagem);
        TextView conteudo = findViewById(R.id.ConteudoNoticia);
        titulo.setText(noticia.getTitulo());
        conteudo.setText(noticia.getConteudo());




        ImageView iv = findViewById(R.id.imagemNoticia);
        Uri uri = resgatarImagemBanco.getBitMapFromURL(noticia.getImagem(),getApplicationContext());
        iv.setImageURI(uri);
        titulo.setTypeface(null, Typeface.BOLD);

    }

    public void voltarTela (View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
