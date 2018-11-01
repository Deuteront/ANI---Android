package controles;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import fonte_controle.Noticia;

public class controlnoticia {
    private ArrayList<Noticia> noticias;

    private void excluirNoticiaAntiga(SimpleDateFormat dataTime) {
        Noticia noticia;

        for (int i = 0; i < noticias.size(); i++) {
            noticia = noticias.get(i);
            if (noticia.getNoticiaLida() == "dataAtual") {
                noticias.remove(i);
            }


        }
    }

    public ArrayList<Noticia> mostrarNoticias() {
        return noticias;
    }

    public void adicionarNoticia(ArrayList<Noticia> noticias) {
        this.noticias= noticias;
    }

    private Noticia visualiarNoticia(int IdNoticia) {
        Noticia noticia;
        for (int i = 0; i < noticias.size(); i++) {
            noticia = noticias.get(i);
            if (noticia.getIdNoticia() == IdNoticia) {
                return noticia;
            }
        }
        return null;


    }
    private ArrayList<Noticia> pesquisarNoticia(String tituloNoticia){
        ArrayList<Noticia> noticiasTituloEspecifico = new ArrayList<Noticia>();
        Noticia noticia;
        for (int i = 0; i < noticias.size(); i++) {
            noticia = noticias.get(i);
            if (noticia.getTitulo().equals(tituloNoticia)) {
                noticiasTituloEspecifico.add(noticia);
            }
        }
        return noticiasTituloEspecifico;
    }
}