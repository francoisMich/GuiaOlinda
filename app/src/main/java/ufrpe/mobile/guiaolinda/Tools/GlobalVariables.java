package ufrpe.mobile.guiaolinda.Tools;

import android.app.Application;

public class GlobalVariables extends Application {

    private String categoria;
    private String mapa_filtrado = "0";

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getMapa_filtrado() {
        return mapa_filtrado;
    }

    public void setMapa_filtrado(String mapa_filtrado) {
        this.mapa_filtrado = mapa_filtrado;
    }
}
