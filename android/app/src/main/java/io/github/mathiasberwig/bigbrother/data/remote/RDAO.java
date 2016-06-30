package io.github.mathiasberwig.bigbrother.data.remote;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.github.mathiasberwig.bigbrother.data.model.Registro;
import io.github.mathiasberwig.bigbrother.data.model.Usuario;

public class RDAO {
    private static final String TAG = "RDAO";

    // TODO: Definir endereço do servidor
    private static final String URL_SERVIDOR = "";
    private static final String TEMPLATE_URL_GET_REGISTROS = "%s/getRegistros";
    private static final String TEMPLATE_URL_GET_REGISTROS_TAG = "%s/getRegistros/%s";
    private static final String TEMPLATE_URL_GET_REGISTROS_TAG_DT_HR = "%s/getRegistros/%s/%s/%s";
    private static final String TEMPLATE_URL_GET_REGISTROS_DT_HR = "%s/getRegistros/%s/%s";
    private static final String TEMPLATE_URL_GET_USUARIOS = "%s/getUsuarios";

    private static final SimpleDateFormat SERVER_DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd+HH:mm:ss", Locale.getDefault());

    private RequestQueue requestQueue;

    private static RDAO instance;

    private RDAO(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public static RDAO getInstance(Context context) {
        if (instance == null) {
            instance = new RDAO(context);
        }
        return instance;
    }

    public void getRegistros(Response.Listener<Registro[]> listener,
                             Response.ErrorListener errorListener) {
        final String URL = String.format(TEMPLATE_URL_GET_REGISTROS, URL_SERVIDOR);
        requestQueue.add(new GsonRequest<>(URL, Registro[].class, null, listener, errorListener));
    }

    public void getRegistros(String tag, Response.Listener<Registro[]> listener,
                             Response.ErrorListener errorListener) {
        final String URL = String.format(TEMPLATE_URL_GET_REGISTROS_TAG, URL_SERVIDOR, tag);
        requestQueue.add(new GsonRequest<>(URL, Registro[].class, null, listener, errorListener));
    }

    public void getRegistros(String tag, Date dataInicio, Date dataFim,
                             Response.Listener<Registro[]> listener,
                             Response.ErrorListener errorListener) {
        String inicio = SERVER_DATETIME_FORMAT.format(dataInicio);
        String fim = SERVER_DATETIME_FORMAT.format(dataFim);
        final String URL = String.format(TEMPLATE_URL_GET_REGISTROS_TAG_DT_HR, URL_SERVIDOR, tag,
                inicio, fim);
        requestQueue.add(new GsonRequest<>(URL, Registro[].class, null, listener, errorListener));
    }

    public void getRegistros(Date dataInicio, Date dataFim,
                             Response.Listener<Registro[]> listener,
                             Response.ErrorListener errorListener) {
        String inicio = SERVER_DATETIME_FORMAT.format(dataInicio);
        String fim = SERVER_DATETIME_FORMAT.format(dataFim);
        final String URL = String.format(TEMPLATE_URL_GET_REGISTROS_DT_HR, URL_SERVIDOR, inicio, fim);
        requestQueue.add(new GsonRequest<>(URL, Registro[].class, null, listener, errorListener));
    }

    public void getUsuarios(Response.Listener<Usuario[]> listener,
                            Response.ErrorListener errorListener) {
        final String URL = String.format(TEMPLATE_URL_GET_USUARIOS, URL_SERVIDOR);
        requestQueue.add(new GsonRequest<>(URL, Usuario[].class, null, listener, errorListener));
    }
}
