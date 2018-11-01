package controles;
import java.util.ArrayList;

import fonte_controle.Fonte;

public class ControlFonte {
    private ArrayList<Fonte> fontes;
    private Boolean primeiraCarga;

    public ArrayList<Fonte> mostrarFontes(){
        return fontes;

    }

    public void adicionarFonte(ArrayList<Fonte> fonte){
        fontes=fonte;
    }


    public Boolean getPrimeiraCarga() {
        return primeiraCarga;
    }

    public void setPrimeiraCarga(Boolean primeiraCarga) {
        this.primeiraCarga = primeiraCarga;
    }
}
