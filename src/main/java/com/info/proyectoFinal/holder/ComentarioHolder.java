package com.info.proyectoFinal.holder;

import com.info.proyectoFinal.models.Comentario;

public class ComentarioHolder {

    private int idAutor;

    private Comentario comentario;

    public int getIdAutor() {
        return idAutor;
    }

    public void setIdAutor(int idAutor) {
        this.idAutor = idAutor;
    }

    public Comentario getComentario() {
        return comentario;
    }

    public void setComentario(Comentario comentario) {
        this.comentario = comentario;
    }
}
