package io.github.mathiasberwig.bigbrother.data.model;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Registro {
    private static final String TAG = "Registro";

    private static final SimpleDateFormat SERVER_DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private static final SimpleDateFormat PRINT_DATETIME_FORMAT = new SimpleDateFormat("dd/MM/yyyy – HH:mm:ss", Locale.getDefault());

    private int id_registro;
    private String data_hora;
    private String tag;
    private String nome;
    private String foto;

    public Registro(int id_registro, String data_hora, String tag, String nome, String foto) {
        this.id_registro = id_registro;
        this.data_hora = data_hora;
        this.tag = tag;
        this.nome = nome;
        this.foto = foto;
    }

    public int getIdRegistro() {
        return id_registro;
    }

    public Date getDataHora() throws ParseException {
        return SERVER_DATETIME_FORMAT.parse(data_hora);
    }

    public String getDataHoraFormatada() {
        try {
            return PRINT_DATETIME_FORMAT.format(getDataHora());
        } catch (ParseException e) {
            Log.e(TAG, "getDataHoraFormatada: Não foi possível obter a data e hora do registro.", e);
            return "";
        }
    }

    public String getTag() {
        return tag;
    }

    public String getNome() {
        return nome;
    }

    public String getFoto() {
        return foto;
    }

    @Override
    public String toString() {
        return "Registro{" +
                "id_registro=" + id_registro +
                ", data_hora='" + data_hora + '\'' +
                ", tag='" + tag + '\'' +
                ", nome='" + nome + '\'' +
                ", foto='" + foto + '\'' +
                '}';
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Registro registro = (Registro) o;

        if (id_registro != registro.id_registro) return false;
        if (data_hora != null ? !data_hora.equals(registro.data_hora) : registro.data_hora != null) return false;
        if (tag != null ? !tag.equals(registro.tag) : registro.tag != null) return false;
        if (nome != null ? !nome.equals(registro.nome) : registro.nome != null) return false;
        if (foto != null ? !foto.equals(registro.foto) : registro.foto != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id_registro;
        result = 31 * result + (data_hora != null ? data_hora.hashCode() : 0);
        result = 31 * result + (tag != null ? tag.hashCode() : 0);
        result = 31 * result + (nome != null ? nome.hashCode() : 0);
        result = 31 * result + (foto != null ? foto.hashCode() : 0);
        return result;
    }
}