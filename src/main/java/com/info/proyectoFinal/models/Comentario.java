package com.info.proyectoFinal.models;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_autorComentario")
    private Usuario autorComentario;

    private LocalDate creacion;

    @Column(length = 200)
    private String comentario;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getAutorComentario() {
        return autorComentario;
    }

    public void setAutorComentario(Usuario autorComentario) {
        this.autorComentario = autorComentario;
    }

    public LocalDate getCreacion() {
        return creacion;
    }

    public void setCreacion(LocalDate creacion) {
        this.creacion = creacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
