package com.example.aluno.pimesmo;
import android.database.Cursor;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.FormatFlagsConversionMismatchException;

import banco_dados.dadosViaGet;
import fonte_controle.Fonte;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;
import static com.example.aluno.pimesmo.R.id.checkbox_fonte;

@SuppressLint("Registered")
public class ActivityFonte extends AppCompatActivity {
    public dadosViaGet dados=new dadosViaGet();
    public Fonte fonte;
    public SQLiteDatabase bd;
    public String nomeFonte;
    public int Intvalor;
    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd= openOrCreateDatabase("bancoPI", MODE_PRIVATE, null);
        setContentView(R.layout.activity_fonte);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ArrayList<Fonte> fontes = new ArrayList<Fonte>();
        CheckBox checkBox = findViewById(checkbox_fonte);
        try {
            fontes = dados.doGET();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int idSelected = getIntent().getIntExtra("ID",0);
        for(int i=0;i<fontes.size();i++){
            Log.i("3532425",fontes.get(i).getNome());
        }
        fonte=fontes.get(idSelected);

        Cursor cursor = bd.rawQuery("SELECT idFonte,nomeFonte,fonteAssinada FROM fontes where nomeFonte = '"+fonte.getNome()+"' ",null);
        cursor.moveToFirst();


        if(fonte.getNome().equals(cursor.getString(1))) {
            fonte.setFonteAssinada(cursor.getString(2));
            if(fonte.getFonteAssinada().equals("Assinar")){
                checkBox.setChecked(false);
            }else{

                checkBox.setChecked(true);

        }
        }



        cursor.close();



        nomeFonte=fonte.getNome();
        Intvalor=idSelected;
        TextView conteudoFonte=findViewById(R.id.conteudo_fonte);
        TextView tituloFonte=findViewById(R.id.titulo_fonte);

        checkBox.setText(fonte.getFonteAssinada());
        tituloFonte.setText(fonte.getNome());
        conteudoFonte.setText(fonte.getConteudo());

        tituloFonte.setTypeface(null, Typeface.BOLD);

    }


    public void voltarTela(View view){
        Intent intent =new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    @SuppressLint("SetTextI18n")
    public void itemClicked(View v) {
        //code to check if this checkbox is checked!
        CheckBox checkBox = findViewById(checkbox_fonte);
        TextView checkBoxText= findViewById(checkbox_fonte);
        if(checkBox.isChecked()){
            checkBox.setText("Assinado");
            fonte.setFonteAssinada("Assinado");


            bd.execSQL("UPDATE fontes set fonteAssinada = 'Assinado' where nomeFonte = '"+nomeFonte +"' ;");



        }
        else{
            checkBox.setText("Assinar");
            fonte.setFonteAssinada("Assinar");
            bd.execSQL("UPDATE fontes set fonteAssinada = 'Assinar' where nomeFonte = '"+nomeFonte +"' ;");
        }

    }

}




