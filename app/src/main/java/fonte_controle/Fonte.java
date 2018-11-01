package fonte_controle;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Fonte{
    private ArrayList<Noticia> Noticias;
    private String nome;
    private String linkURL;
    private String fonteAssinada;
    private int idJornal;
    private String conteudo;



    public Fonte() {
        Noticias= new ArrayList<Noticia>();
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdJornal() {
        return idJornal;
    }

    public void setIdJornal(int idJornal) {
        this.idJornal = idJornal;
    }

    public String getFonteAssinada() {
        return fonteAssinada;
    }

    public void setFonteAssinada(String fonteAssinada) {
        this.fonteAssinada = fonteAssinada;
    }

    public String getLinkURL() {
        return linkURL;
    }

    public void setLinkURL(String linkURL) {
        this.linkURL = linkURL;
    }

    public ArrayList<Noticia> getNoticias() {
        return Noticias;
    }

    public void adicionarNoticia(Noticia noticia) {
        this.Noticias.add(noticia);
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }
}
