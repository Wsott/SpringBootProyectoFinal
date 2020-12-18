package com.info.proyectoFinal.holder;

import com.info.proyectoFinal.models.Comentario;

public class ComentarioHolder {

    private int idAutor;

    private int idComentario;

    private int idPost;

    private int max = -1;

    private Comentario comentario;


    public int getIdAutor() {
        return idAutor;
    }

    public void setIdAutor(int idAutor) {
        this.idAutor = idAutor;
    }

    public int getIdComentario() {
        return idComentario;
    }

    public void setIdComentario(int idComentario) {
        this.idComentario = idComentario;
    }

    public int getIdPost() {
        return idPost;
    }

    public void setIdPost(int idPost) {
        this.idPost = idPost;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public Comentario getComentario() {
        return comentario;
    }

    public void setComentario(Comentario comentario) {
        this.comentario = comentario;
    }
}
