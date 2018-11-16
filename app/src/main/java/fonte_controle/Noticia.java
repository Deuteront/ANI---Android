package fonte_controle;


public class Noticia {
    private int idNoticia;
    private String titulo;
    private String conteudo;
    private String autor;
    private String link;
    private String noticiaLida;
    private String imagem;
    private String dataPostagem;
    private int idFonte;

    public String getDataPostagem() {
        return dataPostagem;
    }
    public void setDataPostagem(String dataPostagem) {
        this.dataPostagem=dataPostagem;
    }

    public String getNoticiaLida() {
        return noticiaLida;
    }

    public void setNoticiaLida(String noticiaLida) {
        this.noticiaLida = noticiaLida;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public int getIdNoticia() {
        return idNoticia;
    }

    public void setIdNoticia(int idNoticia) {
        this.idNoticia = idNoticia;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getIdFonte() {
        return idFonte;
    }

    public void setIdFonte(int idFonte) {
        this.idFonte = idFonte;
    }


}
