package banco_dados;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import fonte_controle.Fonte;
import fonte_controle.Noticia;
public class RequisicaoHTTP {
    private String ipBanco="http://64.137.238.22/";
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
            String decodedToUTF8;
            for (int i = 0; i < count; i++) {
                Fonte fonte=new Fonte();
                jsonFontes = jsonObjectArrayFonte.getJSONObject(i);
                fonte.setIdJornal(Integer.parseInt(jsonFontes.getString("idFonte")));
                fonte.setFonteAssinada("Assinar");

                ret =  new String(jsonFontes.getString("linkFonte").getBytes("ISO-8859-1"), "UTF-8");

                fonte.setLinkURL(ret);

                ret = new String(jsonFontes.getString("nomeFonte").getBytes("ISO-8859-1"), "UTF-8");

                fonte.setNome(ret);

                ret =new String(jsonFontes.getString("conteudoFonte").getBytes("ISO-8859-1"), "UTF-8");

                fonte.setConteudo(ret);

                fontes.add(fonte);
            }


            JSONObject jsonObjectNoticia = new JSONObject(bufferNoticia.toString());
            JSONArray jsonObjectArrayNoticia = jsonObjectNoticia.getJSONArray("noticias");
            int count2 = jsonObjectArrayNoticia.length();
            JSONObject jsonNoticias;
            Fonte fonteTemp;
            ArrayList<Fonte> fontesComNoticias =fontes;
            for (int i = 0; i < count2; i++) {
                Noticia noticia=new Noticia();
                jsonNoticias = jsonObjectArrayNoticia.getJSONObject(i);


                noticia.setIdFonte(Integer.parseInt(jsonNoticias.getString("idFonte")));


                ret = new String( jsonNoticias.getString("link").getBytes("ISO-8859-1"), "UTF-8");
                noticia.setLink(ret);


                ret = "    "+ new String(jsonNoticias.getString("conteudo").getBytes("ISO-8859-1"), "UTF-8");
                noticia.setConteudo(ret);
                Log.i("FINAL",ret);
                ret = jsonNoticias.getString("dataPostagem");
                noticia.setDataPostagem(ret);


                noticia.setIdNoticia(Integer.parseInt(jsonNoticias.getString("idNoticia")));


                ret = new String(jsonNoticias.getString("imagem").getBytes("ISO-8859-1"), "UTF-8");
                noticia.setImagem(ret);

                ret = new String(jsonNoticias.getString("autor").getBytes("ISO-8859-1"), "UTF-8");
                noticia.setAutor(ret);

                ret =  new String(jsonNoticias.getString("noticiaLida").getBytes("ISO-8859-1"), "UTF-8");
                noticia.setNoticiaLida(ret);

                ret =  new String(jsonNoticias.getString("titulo").getBytes("ISO-8859-1"), "UTF-8");
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
        String id ="id=";
        String meio="";
        //if(JsonPost.size()<=1){
           // id ="{\"id\":"+JsonPost.get(0)+"}";
       // } else {
            for (int i = 0; i < JsonPost.size(); i++) {
                if ( JsonPost.size()!=i-1) {
                    meio = meio + JsonPost.get(i) + ",";
                }else{
                    meio = meio + JsonPost.get(i);
                }
            }

            meio = meio.substring(0, meio.length() - 1);

            id = id + meio+",0";
      //  }
        JSONObject  jsonRootObject = new JSONObject();
            Log.i("AAAAAAAAAAAAAAAAAAAA",id);
        try {

             jsonRootObject = new JSONObject(id);

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
        urlConnectionFonte.setRequestProperty("Content-type", "application/json");
        urlConnectionFonte.setRequestProperty("Accept", "application/json");
        urlConnectionFonte.setRequestProperty("Accept-Charset", charset);
        urlConnectionFonte.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
        PrintStream printStreamFonte= new PrintStream(urlConnectionFonte.getOutputStream());

        printStreamFonte.println(id);
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
        urlConnectionNoticia.setRequestProperty("Content-type", "application/json");
        urlConnectionNoticia.setRequestProperty("Accept", "application/json");
        urlConnectionNoticia.setRequestProperty("Accept-Charset", charset);
        urlConnectionNoticia.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
        PrintStream printStreamNoticia= new PrintStream(urlConnectionNoticia.getOutputStream());
        printStreamNoticia.println(id);
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

                ret =  new String(jsonFontes.getString("linkFonte").getBytes("ISO-8859-1"), "UTF-8");

                fonte.setLinkURL(ret);

                ret = new String(jsonFontes.getString("nomeFonte").getBytes("ISO-8859-1"), "UTF-8");

                fonte.setNome(ret);

                ret =new String(jsonFontes.getString("conteudoFonte").getBytes("ISO-8859-1"), "UTF-8");

                fonte.setConteudo(ret);

                fontes.add(fonte);
            }

            JSONObject jsonObjectNoticia = new JSONObject(bufferNoticia.toString());
            JSONArray jsonObjectArrayNoticia = jsonObjectNoticia.getJSONArray("noticias");
            int count2 = jsonObjectArrayNoticia.length();
            JSONObject jsonNoticias;
            Fonte fonteTemp = null;
            ArrayList<Fonte> fontesComNoticias =fontes;
            for (int i = 0; i < count2; i++) {
                Noticia noticia=new Noticia();
                jsonNoticias = jsonObjectArrayNoticia.getJSONObject(i);

                noticia.setIdFonte(Integer.parseInt(jsonNoticias.getString("idFonte")));


                ret = new String( jsonNoticias.getString("link").getBytes("ISO-8859-1"), "UTF-8");
                noticia.setLink(ret);


                ret = "    "+ new String(jsonNoticias.getString("conteudo").getBytes("ISO-8859-1"), "UTF-8");
                noticia.setConteudo(ret);
                Log.i("FINAL",ret);
                ret = jsonNoticias.getString("dataPostagem");
                noticia.setDataPostagem(ret);


                noticia.setIdNoticia(Integer.parseInt(jsonNoticias.getString("idNoticia")));


                ret = new String(jsonNoticias.getString("imagem").getBytes("ISO-8859-1"), "UTF-8");
                noticia.setImagem(ret);

                ret = new String(jsonNoticias.getString("autor").getBytes("ISO-8859-1"), "UTF-8");
                noticia.setAutor(ret);

                ret =  new String(jsonNoticias.getString("noticiaLida").getBytes("ISO-8859-1"), "UTF-8");
                noticia.setNoticiaLida(ret);

                ret =  new String(jsonNoticias.getString("titulo").getBytes("ISO-8859-1"), "UTF-8");
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