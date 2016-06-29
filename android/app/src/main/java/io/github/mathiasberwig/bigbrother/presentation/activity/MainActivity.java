package io.github.mathiasberwig.bigbrother.presentation.activity;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.Arrays;

import io.github.mathiasberwig.bigbrother.R;
import io.github.mathiasberwig.bigbrother.data.model.Registro;
import io.github.mathiasberwig.bigbrother.data.remote.RDAO;
import io.github.mathiasberwig.bigbrother.presentation.adapter.RegistrosAdapter;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private RecyclerView recyclerView;
    private TextView tvEmptyState;
    private RegistrosAdapter registrosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        localizarViews();
        prepararRecyclerView();
        consultarRegistros();
    }

    private void prepararRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

    private void localizarViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        tvEmptyState = (TextView) findViewById(R.id.tvEmptyState);
    }

    private void atualizarAdapter(Registro[] registros) {
        if (registrosAdapter == null) {
            registrosAdapter = new RegistrosAdapter(this, Arrays.asList(registros));
            recyclerView.setAdapter(registrosAdapter);
        } else {
            registrosAdapter.setRegistros(Arrays.asList(registros));
        }
    }

    private void mostrarEmptyState(@StringRes int mensagem) {
        tvEmptyState.setText(mensagem);
        recyclerView.setVisibility(View.GONE);
        tvEmptyState.setVisibility(View.VISIBLE);
    }

    private void mostrarRecyclerView() {
        tvEmptyState.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void consultarRegistros() {
        RDAO.getInstance(this).getRegistros(
                new Response.Listener<Registro[]>() {
                    @Override
                    public void onResponse(Registro[] response) {
                        if (response.length > 0) {
                            atualizarAdapter(response);
                            mostrarRecyclerView();
                        } else {
                            mostrarEmptyState(R.string.erro_resposta_vazia);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mostrarEmptyState(R.string.erro_consultar_registros);
                        Log.e(TAG, "consultarRegistros: " + error.getMessage(), error);
                    }
                }
        );
    }
}
