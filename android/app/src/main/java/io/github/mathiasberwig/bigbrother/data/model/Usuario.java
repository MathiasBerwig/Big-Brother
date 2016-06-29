package io.github.mathiasberwig.bigbrother.data.model;

import android.util.Log;

import java.util.Arrays;

public class Usuario {

    private String tag;
    private String nome;
    private String foto;

    public Usuario(String tag, String nome, String foto) {
        this.tag = tag;
        this.nome = nome;
        this.foto = foto;
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

    public static String[] getNomes(Usuario[] usuarios) {
        String[] nomes = new String[usuarios.length];
        for (int i = 0; i < usuarios.length; i++) {
            nomes[i] = usuarios[i].getNome();
        }
        return nomes;
    }

    @Override
    public String toString() {
        return nome;
    }

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Usuario usuario = (Usuario) o;

        if (tag != null ? !tag.equals(usuario.tag) : usuario.tag != null) return false;
        if (nome != null ? !nome.equals(usuario.nome) : usuario.nome != null) return false;
        return foto != null ? foto.equals(usuario.foto) : usuario.foto == null;

    }

    @Override
    public int hashCode() {
        int result = tag != null ? tag.hashCode() : 0;
        result = 31 * result + (nome != null ? nome.hashCode() : 0);
        result = 31 * result + (foto != null ? foto.hashCode() : 0);
        return result;
    }
}
