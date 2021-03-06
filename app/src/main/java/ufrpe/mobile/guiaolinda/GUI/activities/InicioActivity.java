package ufrpe.mobile.guiaolinda.GUI.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ufrpe.mobile.guiaolinda.R;
import ufrpe.mobile.guiaolinda.Tools.GlobalVariables;
import ufrpe.mobile.guiaolinda.Tools.ItemsListActivity;

public class InicioActivity extends Activity {

    private static final String CATEGORIA_ITENS = "categoria";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final GlobalVariables globalVariables = (GlobalVariables) getApplicationContext();

        setContentView(R.layout.tela_inicio);

        /*Button bt_carnaval = findViewById(R.id.bt_carnaval);
        bt_carnaval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InicioActivity.this, CarnavalActivity.class);
                globalVariables.setCategoria("Carnaval");
                startActivity(intent);
            }
        });*/

        Button bt_gastronomia = findViewById(R.id.bt_gastronomia);
        bt_gastronomia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criaIntent("Gastronomia", globalVariables);
            }
        });

        Button bt_hotelaria = findViewById(R.id.bt_hotelaria);
        bt_hotelaria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criaIntent("Hospedagem", globalVariables);
            }
        });

        Button bt_igrejas = findViewById(R.id.bt_igrejas);
        bt_igrejas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criaIntent("Igrejas", globalVariables);
            }
        });

        Button bt_monumentos = findViewById(R.id.bt_monumentos);
        bt_monumentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criaIntent("Monumentos", globalVariables);
            }
        });

        Button bt_eventos = findViewById(R.id.bt_eventos);
        bt_eventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criaIntent("Eventos", globalVariables);
            }
        });
    }

    private void criaIntent(String tipoLocal, GlobalVariables globalVariables) {
        Intent intent = new Intent(InicioActivity.this, ItemsListActivity.class);
        intent.putExtra(CATEGORIA_ITENS, tipoLocal);
        globalVariables.setCategoria(tipoLocal);
        startActivity(intent);
    }
}