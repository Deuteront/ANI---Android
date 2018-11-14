package com.example.aluno.pimesmo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

import banco_dados.dadosViaGet;
import fonte_controle.Fonte;
import fonte_controle.Noticia;

public class MainActivity extends AppCompatActivity

        implements NavigationView.OnNavigationItemSelectedListener {
    private SQLiteDatabase bd;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        dadosViaGet dadosViaPost = new dadosViaGet();
        ArrayList<Fonte> fontes = new ArrayList<Fonte>();
        ArrayList<Noticia> noticias = new ArrayList<Noticia>();

        bd = openOrCreateDatabase("bancoPI", MODE_PRIVATE, null);
        //bd.execSQL("DROP TABLE IF EXISTS primeiraCarga");
       // bd.execSQL("DROP TABLE IF EXISTS fontes");
        //bd.execSQL("DROP TABLE IF EXISTS noticias");
        //  bd.execSQL("DROP TABLE IF EXISTS novaFonteAssinada");
        bd.execSQL("CREATE TABLE IF NOT EXISTS primeiraCarga (idCarga INT(22), valor int);");
        bd.execSQL("CREATE TABLE IF NOT EXISTS fontes (idFonte INT(22), nomeFonte VARCHAR,fonteAssinada VARCHAR);");
        bd.execSQL("CREATE TABLE IF NOT EXISTS novaFonteAssinada (id INT(22));");
        @SuppressLint("Recycle") Cursor cursorPrimeiraCarga = bd.rawQuery("SELECT idCarga,valor FROM primeiraCarga", null);
        if (cursorPrimeiraCarga.getCount() == 0) {

            bd.execSQL("insert into primeiraCarga(idCarga,valor) values (1,1);");
            try {
                fontes = dadosViaPost.doGET();
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < fontes.size(); i++) {
                bd.execSQL("INSERT INTO fontes (idFonte,nomeFonte,fonteAssinada) VALUES ('" + fontes.get(i).getIdJornal() + "', '" + fontes.get(i).getNome() + "','" + fontes.get(i).getFonteAssinada() + "');");

            }


        }
        Cursor cursorFontesRegistradas = bd.rawQuery("SELECT idFonte FROM fontes where FonteAssinada='Assinado'", null);
        cursorFontesRegistradas.moveToFirst();

        ArrayList<Integer> doPostFontesIndice = new  ArrayList<Integer>();
        for(int i=0;i<cursorFontesRegistradas.getCount();i++){
            doPostFontesIndice.add(Integer.valueOf(cursorFontesRegistradas.getString(0)));
            cursorFontesRegistradas.moveToNext();

        }
        cursorFontesRegistradas.close();
        if(doPostFontesIndice.size()>0) {
        try {
            fontes = dadosViaPost.doPOST(doPostFontesIndice);
        } catch (IOException e) {
            e.printStackTrace();


        }

        for (int j = 0; j < fontes.size(); j++) {
            noticias.addAll(fontes.get(j).getNoticias());

        }
    }



        ListView mListView = (ListView) findViewById(R.id.ListaNoticia);
        if (noticias.size() != 0) {
            ArrayAdapter adapter = new NoticiaAdapter(this, noticias);
            mListView.setAdapter(adapter);
        } else {
            ArrayList<String> strings = new ArrayList<String>();
            strings.add("BEM VINDO AO ANI-APLICATIVO DE NOTICIAS INSTITUCIONAIS, ELE É UM APLICATIVO DE NOTICIAS QUE ESTÁ EM FOCO OS CAMPUS DO IFSC. CLICANDO NOS 3 TRACINHOS AO LADO ESTÁ OS CAMPUS ONDE PODE-SE ASSINA-LOS E ASSIM TER ACESSO PELO MENU PRINCIAL (ONDE ESTÁ AGORA) AS NOTICIAS DAQUELE CAMPUS");


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strings);
            mListView.setAdapter(adapter);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent i1 = new Intent(MainActivity.this, NoticiaActivity.class);
                i1.putExtra("ID", id);
                i1.putExtra("POSITION", position);
                startActivity(i1);
                finish();
            }

        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.campus_ararangua) {
            Intent intent = new Intent(this, ActivityFonte.class);
            intent.putExtra("ID", 0);
            startActivity(intent);
            finish();
        } else if (id == R.id.campus_canoinhas) {
            Intent intent = new Intent(this, ActivityFonte.class);
            intent.putExtra("ID", 1);
            startActivity(intent);
            finish();
        } else if (id == R.id.campus_cacador) {
            Intent intent = new Intent(this, ActivityFonte.class);
            intent.putExtra("ID", 2);
            startActivity(intent);
            finish();
        } else if (id == R.id.campus_chapeco) {
            Intent intent = new Intent(this, ActivityFonte.class);
            intent.putExtra("ID", 3);
            startActivity(intent);
            finish();
        } else if (id == R.id.campus_criciuma) {
            Intent intent = new Intent(this, ActivityFonte.class);
            intent.putExtra("ID", 4);
            startActivity(intent);
            finish();
        } else if (id == R.id.campus_florianopolis) {
            Intent intent = new Intent(this, ActivityFonte.class);
            intent.putExtra("ID", 5);
            startActivity(intent);
            finish();
        } else if (id == R.id.campus_florianopolis_continente) {
            Intent intent = new Intent(this, ActivityFonte.class);
            intent.putExtra("ID", 6);
            startActivity(intent);
            finish();
        } else if (id == R.id.campus_guaropaba) {
            Intent intent = new Intent(this, ActivityFonte.class);
            intent.putExtra("ID", 7);
            startActivity(intent);
            finish();
        } else if (id == R.id.campus_gaspar) {
            Intent intent = new Intent(this, ActivityFonte.class);
            intent.putExtra("ID", 8);
            startActivity(intent);
            finish();
        } else if (id == R.id.campus_itajai) {
            Intent intent = new Intent(this, ActivityFonte.class);
            intent.putExtra("ID", 9);
            startActivity(intent);
        } else if (id == R.id.campus_jaragua_centro) {
            Intent intent = new Intent(this, ActivityFonte.class);
            intent.putExtra("ID", 10);
            startActivity(intent);
            finish();
        } else if (id == R.id.campus_jaragua_rau) {
            Intent intent = new Intent(this, ActivityFonte.class);
            intent.putExtra("ID", 11);
            startActivity(intent);
            finish();
        } else if (id == R.id.campus_joinville) {
            Intent intent = new Intent(this, ActivityFonte.class);
            intent.putExtra("ID", 12);
            startActivity(intent);
            finish();
        } else if (id == R.id.campus_lages) {
            Intent intent = new Intent(this, ActivityFonte.class);
            intent.putExtra("ID", 13);
            startActivity(intent);
            finish();
        } else if (id == R.id.campus_palhoca) {
            Intent intent = new Intent(this, ActivityFonte.class);
            intent.putExtra("ID", 14);
            startActivity(intent);
            finish();
        } else if (id == R.id.campus_carlos) {
            Intent intent = new Intent(this, ActivityFonte.class);
            intent.putExtra("ID", 15);
            startActivity(intent);
        } else if (id == R.id.campus_jose) {
            Intent intent = new Intent(this, ActivityFonte.class);
            intent.putExtra("ID", 16);
            startActivity(intent);
            finish();
        } else if (id == R.id.campus_lourenco) {
            Intent intent = new Intent(this, ActivityFonte.class);
            intent.putExtra("ID", 17);
            startActivity(intent);
            finish();
        } else if (id == R.id.campus_miguel) {
            Intent intent = new Intent(this, ActivityFonte.class);
            intent.putExtra("ID", 18);
            startActivity(intent);
            finish();
        } else if (id == R.id.campus_tubarao) {
            Intent intent = new Intent(this, ActivityFonte.class);
            intent.putExtra("ID", 19);
            startActivity(intent);
            finish();
        } else if (id == R.id.campus_urupema) {
            Intent intent = new Intent(this, ActivityFonte.class);
            intent.putExtra("ID", 20);
            startActivity(intent);
            finish();
        } else if (id == R.id.campus_xanxere) {
            Intent intent = new Intent(this, ActivityFonte.class);
            intent.putExtra("ID", 21);
            startActivity(intent);
            finish();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }



}

