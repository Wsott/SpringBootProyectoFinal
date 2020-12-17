package com.info.proyectoFinal.holder;

import com.info.proyectoFinal.models.Post;

public class PostHolder {
    private int idAutor;

    private int idPost;

    private Post post;

    public int getIdAutor() {
        return idAutor;
    }

    public void setIdAutor(int idAutor) {
        this.idAutor = idAutor;
    }

    public int getIdPost() {
        return idPost;
    }

    public void setIdPost(int idPost) {
        this.idPost = idPost;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
