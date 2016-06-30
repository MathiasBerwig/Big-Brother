package io.github.mathiasberwig.bigbrother.presentation.adapter;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import io.github.mathiasberwig.bigbrother.R;
import io.github.mathiasberwig.bigbrother.data.model.Registro;

public class RegistrosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private AppCompatActivity activity;
    private List<Registro> registros;

    public RegistrosAdapter(AppCompatActivity activity, @NonNull List<Registro> registros) {
        this.activity = activity;
        this.registros = registros;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_registro, parent, false);
        return new RegistroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, int pos) {
        // Preenche o nome e data-hora do registro
        ((RegistroViewHolder) h).tvNome.setText(registros.get(pos).getNome());
        ((RegistroViewHolder) h).tvDataHora.setText(registros.get(pos).getDataHoraFormatada());

        // Carrega a imagem de modo assíncrono usando o Glide
        Glide.with(activity)
                .load(registros.get(pos).getFoto())
                .centerCrop()
                .placeholder(R.drawable.ic_person_black_48dp)
                .into(((RegistroViewHolder) h).imgAvatar);
    }

    @Override
    public int getItemCount() {
        return registros.size();
    }

    public void setRegistros(List<Registro> registros) {
        this.registros = registros;
        notifyDataSetChanged();
    }

    public static class RegistroViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar;
        TextView tvNome;
        TextView tvDataHora;

        public RegistroViewHolder(View v) {
            super(v);
            imgAvatar = (ImageView) v.findViewById(R.id.civAvatar);
            tvNome = (TextView) v.findViewById(R.id.tvNome);
            tvDataHora = (TextView) v.findViewById(R.id.tvDataHora);
        }
    }
}
