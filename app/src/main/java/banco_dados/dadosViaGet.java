package banco_dados;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ImageSpan;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

import controles.ControlFonte;
import controles.controlnoticia;
import fonte_controle.Fonte;
import fonte_controle.Noticia;

import static android.content.ContentValues.TAG;
public class dadosViaGet {
    private String ipBanco="http://pi.rbeninca.com.br/html/";
    private ArrayList<Fonte> fontes=new ArrayList<Fonte>();
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)

    public String getIpBanco(){
        return ipBanco;
    }

    public ArrayList<Fonte> doGET()  throws IOException  {

        //FONTE
        URL urlFonte = new URL(ipBanco+"fonte.php");
        Log.i("AAAAAAAAAAAB",urlFonte.toString());
        HttpURLConnection urlConnectionFonte = (HttpURLConnection) urlFonte.openConnection();
        urlConnectionFonte.setRequestMethod("GET");
        urlConnectionFonte.connect();
        InputStream inputStreamFonte = urlConnectionFonte.getInputStream();
        BufferedReader readerFonte = new BufferedReader(new InputStreamReader(inputStreamFonte));
        String linhaFonte;
        StringBuilder bufferFonte = new StringBuilder();

        URL urlNoticia = new URL(ipBanco+"noticia.php");
        HttpURLConnection urlConnectionNoticia = (HttpURLConnection) urlNoticia.openConnection();
        urlConnectionNoticia.setRequestMethod("GET");
        urlConnectionNoticia.connect();
        InputStream inputStreamNoticia = urlConnectionNoticia.getInputStream();
        BufferedReader readerNoticia = new BufferedReader(new InputStreamReader(inputStreamNoticia));
        String linhaNoticia;
        StringBuilder bufferNoticia = new StringBuilder();

        while ((linhaFonte = readerFonte.readLine()) != null) {
            bufferFonte.append(linhaFonte); }
        while ((linhaNoticia = readerNoticia.readLine()) != null) {
            bufferNoticia.append(linhaNoticia); }
        try {

            JSONObject jsonObjectFonte = new JSONObject(bufferFonte.toString());
            JSONArray jsonObjectArrayFonte = jsonObjectFonte.getJSONArray("fontes");
            int count = jsonObjectArrayFonte.length();

            JSONObject jsonFontes;
            String ret;
            for (int i = 0; i < count; i++) {
                Fonte fonte=new Fonte();
                jsonFontes = jsonObjectArrayFonte.getJSONObject(i);
                fonte.setIdJornal(Integer.parseInt(jsonFontes.getString("idFonte")));
                fonte.setFonteAssinada("Assinar");

                ret = jsonFontes.getString("linkFonte");

                fonte.setLinkURL(ret);

                ret = jsonFontes.getString("nomeFonte");

                fonte.setNome(ret);

                ret = jsonFontes.getString("conteudoFonte");

                fonte.setConteudo(ret);

                fontes.add(fonte);
            }
            ControlFonte controlFonte=new ControlFonte();
            controlFonte.adicionarFonte(fontes);
            JSONObject jsonObjectNoticia = new JSONObject(bufferNoticia.toString());
            JSONArray jsonObjectArrayNoticia = jsonObjectNoticia.getJSONArray("noticias");
            int count2 = jsonObjectArrayNoticia.length();
            JSONObject jsonNoticias;
            Fonte fonteTemp;
            ArrayList<Fonte> fontesComNoticias =controlFonte.mostrarFontes();
            for (int i = 0; i < count2; i++) {
                Noticia noticia=new Noticia();
                jsonNoticias = jsonObjectArrayNoticia.getJSONObject(i);


                noticia.setIdFonte(Integer.parseInt(jsonNoticias.getString("idFonte")));


                ret = jsonNoticias.getString("link");
                noticia.setLink(ret);


                ret = "    "+jsonNoticias.getString("conteudo");
                noticia.setConteudo(ret);
                Log.i("FINAL",ret);
                ret = jsonNoticias.getString("dataPostagem");
                noticia.setDataPostagem(ret);


                noticia.setIdNoticia(Integer.parseInt(jsonNoticias.getString("idNoticia")));


                ret = jsonNoticias.getString("imagem");
                noticia.setImagem(ret);

                ret = jsonNoticias.getString("autor");
                noticia.setAutor(ret);

                ret = jsonNoticias.getString("noticiaLida");
                noticia.setNoticiaLida(ret);

                ret = jsonNoticias.getString("titulo");
                noticia.setTitulo(ret);

                fonteTemp=fontesComNoticias.get(noticia.getIdFonte()-1);
                fonteTemp.adicionarNoticia(noticia);


            }
            return fontesComNoticias;


        } catch (JSONException e) {
        }
        return null;
    }


    public ArrayList<Fonte> doPOST(ArrayList<Integer>  JsonPost)  throws IOException  {


        if(!JsonPost.isEmpty()){
        Log.i("AAAAAAAAAAAAAAAAA","{\"phonetype\":\"N95\",\"cat\":\"WP\"}");
        Log.i("AAAAAAAAAAAAAAAAAAAA",JsonPost.toString());
        String id ="{\"id\":[";
        String meio="";
        //if(JsonPost.size()<=1){
           // id ="{\"id\":"+JsonPost.get(0)+"}";
       // } else {
            for (int i = 0; i < JsonPost.size(); i++) {
                meio = meio + JsonPost.get(i) + ",";
            }


            meio = meio.substring(0, meio.length() - 1);

            String final1 = ",0]}";
            id = id + meio + final1;
      //  }
        JSONObject  jsonRootObject = new JSONObject();

        try {

             jsonRootObject = new JSONObject(id);
            Log.i("AAAAAAAAAAAAAAAAAAAA",jsonRootObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }



        //FONTE
        URL urlFonte = new URL(ipBanco+"fonteassinada.php");
        HttpURLConnection urlConnectionFonte = (HttpURLConnection) urlFonte.openConnection();
        urlConnectionFonte.setDoOutput(true);
        urlConnectionFonte.setDoInput(true);
        urlConnectionFonte.setRequestMethod("POST");
        String charset = "UTF-8";
        urlConnectionFonte.setRequestProperty("Accept-Charset", charset);
        urlConnectionFonte.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
        PrintStream printStreamFonte= new PrintStream(urlConnectionFonte.getOutputStream());

        printStreamFonte.println(jsonRootObject);
        urlConnectionFonte.connect();
        InputStream inputStreamFonte = urlConnectionFonte.getInputStream();
        BufferedReader readerFonte = new BufferedReader(new InputStreamReader(inputStreamFonte));
        String linhaFonte;
        StringBuilder bufferFonte = new StringBuilder();
        urlConnectionFonte.disconnect();

        URL urlNoticia = new URL(ipBanco+"noticiaassinada.php");
        HttpURLConnection urlConnectionNoticia = (HttpURLConnection) urlNoticia.openConnection();
        urlConnectionNoticia.setDoOutput(true);
        urlConnectionNoticia.setDoInput(true);
        urlConnectionNoticia.setRequestMethod("POST");
        urlConnectionNoticia.setRequestProperty("Accept-Charset", charset);
        urlConnectionNoticia.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
        PrintStream printStreamNoticia= new PrintStream(urlConnectionNoticia.getOutputStream());
        printStreamNoticia.println(jsonRootObject);
        urlConnectionNoticia.connect();
        InputStream inputStreamNoticia = urlConnectionNoticia.getInputStream();
        BufferedReader readerNoticia = new BufferedReader(new InputStreamReader(inputStreamNoticia));
        String linhaNoticia;
        StringBuilder bufferNoticia = new StringBuilder();
        urlConnectionNoticia.disconnect();

        while ((linhaFonte = readerFonte.readLine()) != null) {
            bufferFonte.append(linhaFonte); }
        while ((linhaNoticia = readerNoticia.readLine()) != null) {
            bufferNoticia.append(linhaNoticia); }
        try {

            JSONObject jsonObjectFonte = new JSONObject(bufferFonte.toString());
            JSONArray jsonObjectArrayFonte = jsonObjectFonte.getJSONArray("fontes");
            int count = jsonObjectArrayFonte.length();

            JSONObject jsonFontes;
            String ret;
            for (int i = 0; i < count; i++) {
                Fonte fonte=new Fonte();
                jsonFontes = jsonObjectArrayFonte.getJSONObject(i);
                fonte.setIdJornal(Integer.parseInt(jsonFontes.getString("idFonte")));
                fonte.setFonteAssinada("Assinar");

                ret = jsonFontes.getString("linkFonte");

                fonte.setLinkURL(ret);

                ret = jsonFontes.getString("nomeFonte");

                fonte.setNome(ret);

                ret = jsonFontes.getString("conteudoFonte");

                fonte.setConteudo(ret);

                fontes.add(fonte);
            }
            ControlFonte controlFonte=new ControlFonte();
            controlFonte.adicionarFonte(fontes);
            JSONObject jsonObjectNoticia = new JSONObject(bufferNoticia.toString());
            JSONArray jsonObjectArrayNoticia = jsonObjectNoticia.getJSONArray("noticias");
            int count2 = jsonObjectArrayNoticia.length();
            JSONObject jsonNoticias;
            Fonte fonteTemp = null;
            ArrayList<Fonte> fontesComNoticias =controlFonte.mostrarFontes();
            for (int i = 0; i < count2; i++) {
                Noticia noticia=new Noticia();
                jsonNoticias = jsonObjectArrayNoticia.getJSONObject(i);

                noticia.setIdFonte(Integer.parseInt(jsonNoticias.getString("idFonte")));

                ret = jsonNoticias.getString("link");
                noticia.setLink(ret);

                ret = "    "+jsonNoticias.getString("conteudo");
                noticia.setConteudo(ret);
                Log.i("FINAL",ret);
                ret = jsonNoticias.getString("dataPostagem");
                noticia.setDataPostagem(ret);

                noticia.setIdNoticia(Integer.parseInt(jsonNoticias.getString("idNoticia")));


                ret = jsonNoticias.getString("imagem");
                noticia.setImagem(ret);

                ret = jsonNoticias.getString("autor");
                noticia.setAutor(ret);

                ret = jsonNoticias.getString("noticiaLida");
                noticia.setNoticiaLida(ret);

                ret = jsonNoticias.getString("titulo");
                noticia.setTitulo(ret);
                for(int a=0;a<fontesComNoticias.size();a++) {
                    if(fontesComNoticias.get(a).getIdJornal()==noticia.getIdFonte()){
                        fonteTemp = fontesComNoticias.get(a);
                }}
                assert fonteTemp != null;
                fonteTemp.adicionarNoticia(noticia);


            }
            return fontesComNoticias;


        } catch (JSONException e) {
        }}

        return null;
    }


    }