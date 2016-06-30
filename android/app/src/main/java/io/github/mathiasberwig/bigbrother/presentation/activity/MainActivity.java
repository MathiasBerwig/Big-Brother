package io.github.mathiasberwig.bigbrother.presentation.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.borax12.materialdaterangepicker.date.DatePickerDialog;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import io.github.mathiasberwig.bigbrother.R;
import io.github.mathiasberwig.bigbrother.data.model.Registro;
import io.github.mathiasberwig.bigbrother.data.model.Usuario;
import io.github.mathiasberwig.bigbrother.data.remote.RDAO;
import io.github.mathiasberwig.bigbrother.presentation.adapter.RegistrosAdapter;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private RecyclerView recyclerView;
    private TextView tvEmptyState;
    private RegistrosAdapter registrosAdapter;

    private Usuario[] usuarios;

    private enum Consulta {getRegistros, getRegistrosPorData, getRegistrosPorTag};
    private Consulta ultimaConsulta;

    private Usuario ultimoUsuario;
    private Date ultimaDataInicio, ultimaDataFim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        localizarViews();
        prepararRecyclerView();
        consultarRegistros();
        consultarUsuarios();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            // Filtrar pessoas
            case R.id.action_filtrar_pessoa: {
                // Verifica se os usuários foram baixados do WS
                if (usuarios == null) {
                    Toast.makeText(MainActivity.this, R.string.erro_consultar_usuarios, Toast.LENGTH_LONG).show();
                    return true;
                }

                // Prepara a lista de nomes
                final String[] nomes = Arrays.copyOf(Usuario.getNomes(usuarios), usuarios.length + 1);
                nomes[usuarios.length] = getString(R.string.item_mostrar_todos);

                // Mostra o diálogo com a lista
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(R.string.titulo_filtrar_pessoa)
                        .setSingleChoiceItems(nomes, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == nomes.length - 1) consultarRegistros();
                                else consultarRegistros(usuarios[which]);
                                dialog.dismiss();
                            }
                        }).show();
                return true;
            }

            // Filtrar datas
            case R.id.action_filtrar_data: {
                final Calendar calen = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {

                                final Calendar dataInicio = Calendar.getInstance();
                                dataInicio.set(year, monthOfYear, dayOfMonth);

                                final Calendar dataFim = Calendar.getInstance();
                                dataFim.set(yearEnd, monthOfYearEnd, dayOfMonthEnd);

                                consultarRegistros(dataInicio.getTime(), dataFim.getTime());
                            }
                        },
                        calen.get(Calendar.YEAR),
                        calen.get(Calendar.MONTH),
                        calen.get(Calendar.DAY_OF_MONTH));
                dpd.show(getFragmentManager(), null);
                return true;
            }
            // Atualizar dados
            case R.id.action_atualizar: {
                switch (ultimaConsulta) {
                    case getRegistros: consultarRegistros(); break;
                    case getRegistrosPorTag: consultarRegistros(ultimoUsuario); break;
                    case getRegistrosPorData: consultarRegistros(ultimaDataInicio, ultimaDataFim); break;
                }
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
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
        ultimaConsulta = Consulta.getRegistros;
        RDAO.getInstance(this).getRegistros(
                registrosResponseListener,
                registrosErrorListener
        );
    }

    private void consultarRegistros(Usuario usuario) {
        ultimaConsulta = Consulta.getRegistrosPorTag;
        ultimoUsuario = usuario;

        RDAO.getInstance(this).getRegistros(
                usuario.getTag(),
                registrosResponseListener,
                registrosErrorListener
        );
    }

    private void consultarRegistros(Date dataInicio, Date dataFim) {
        ultimaConsulta = Consulta.getRegistrosPorData;
        ultimaDataInicio = dataInicio;
        ultimaDataFim = dataFim;

        RDAO.getInstance(this).getRegistros(
                dataInicio,
                dataFim,
                registrosResponseListener,
                registrosErrorListener
        );
    }

    private void consultarUsuarios() {
        RDAO.getInstance(this).getUsuarios(
                usuariosResponseListener,
                usuariosErrorListener
        );
    }

    private Response.Listener<Registro[]> registrosResponseListener = new Response.Listener<Registro[]>() {
        @Override
        public void onResponse(Registro[] response) {
            if (response != null && response.length > 0) {
                atualizarAdapter(response);
                mostrarRecyclerView();
            } else {
                mostrarEmptyState(R.string.erro_resposta_vazia);
            }
        }
    };

    private Response.ErrorListener registrosErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            mostrarEmptyState(R.string.erro_consultar_registros);
            Log.e(TAG, "consultarRegistros: não foi possível consultar a lista de registros.", error);
        }
    };

    private Response.Listener<Usuario[]> usuariosResponseListener = new Response.Listener<Usuario[]>() {
        @Override
        public void onResponse(Usuario[] response) {
            usuarios = response;
        }
    };

    private Response.ErrorListener usuariosErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(TAG, "consultarUsuarios: não foi possível baixar a lista de usuários.", error);
        }
    };
}
