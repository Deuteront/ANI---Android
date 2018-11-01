package com.example.aluno.pimesmo;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import banco_dados.dadosViaGet;
import banco_dados.resgatarImagemBanco;
import fonte_controle.Noticia;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class NoticiaAdapter extends ArrayAdapter<Noticia>{

    private ArrayList<Noticia> noticias;
    private Context context;
    private resgatarImagemBanco imagemBanco= new resgatarImagemBanco();

    NoticiaAdapter(Context context, ArrayList<Noticia> noticias) {
        super(context,R.layout.linha_list_noticia,noticias);
        this.noticias = noticias;
        this.context  = context;
    }



@NonNull
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public View getView(int position, View convertView, @NonNull ViewGroup parent){
    LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    assert inflater != null;
    @SuppressLint("ViewHolder") View rowView =inflater.inflate(R.layout.linha_list_noticia,parent,false);

    TextView tituloNoticia=(TextView) rowView.findViewById(R.id.TituloNoticia);
    TextView dataNoticia=(TextView) rowView.findViewById(R.id.dataNoticia);
    ImageView imagemNoticia=(ImageView) rowView.findViewById(R.id.imagemNoticia);
    tituloNoticia.setText(noticias.get(position).getTitulo());
    dataNoticia.setText(noticias.get(position).getDataPostagem());
    imagemNoticia.setImageURI(resgatarImagemBanco.getBitMapFromURL(noticias.get(position).getImagem(),getContext()));

    return rowView;
}



}