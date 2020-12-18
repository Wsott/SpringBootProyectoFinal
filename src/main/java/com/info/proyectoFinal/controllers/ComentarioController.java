package com.info.proyectoFinal.controllers;

import com.info.proyectoFinal.holder.ComentarioHolder;
import com.info.proyectoFinal.models.Comentario;
import com.info.proyectoFinal.models.Post;
import com.info.proyectoFinal.models.Usuario;
import com.info.proyectoFinal.repositories.ComentarioRepository;
import com.info.proyectoFinal.repositories.PostRepository;
import com.info.proyectoFinal.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping(path="/comentario")
public class ComentarioController {
    @Autowired
    ComentarioRepository comentarioRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    PostRepository postRepository;

    @GetMapping(path = "/")
    public @ResponseBody
    Iterable<Comentario> getComentario(){
        return comentarioRepository.findAll();
    }

    @PostMapping(path = "/crear")
    public ResponseEntity postComentario(@RequestBody ComentarioHolder comentarioHolder){
        Optional<Usuario> posibleAutor = usuarioRepository.findById(comentarioHolder.getIdAutor());
        Optional<Post> posiblePost = postRepository.findById(comentarioHolder.getIdPost());

        if(!(posibleAutor.isPresent() && posiblePost.isPresent())){
            return ResponseEntity.ok(HttpStatus.NOT_FOUND);
        }
        else{
            comentarioHolder.getComentario().setAutorComentario(posibleAutor.get());
            comentarioHolder.getComentario().setPostPerteneciente(posiblePost.get());
            comentarioHolder.getComentario().setCreacion(LocalDate.now());

            comentarioRepository.save(comentarioHolder.getComentario());

            return ResponseEntity.ok(HttpStatus.OK);
        }
    }

    @DeleteMapping(path = "/borrar")
    public ResponseEntity deleteComentario(@RequestBody ComentarioHolder comentarioHolder){
        Optional<Usuario> posibleAutor = usuarioRepository.findById(comentarioHolder.getIdAutor());
        Optional<Post> posiblePost = postRepository.findById(comentarioHolder.getIdPost());
        Optional<Comentario> posibleComentario = comentarioRepository.findById(comentarioHolder.getIdComentario());

        if(!(posibleAutor.isPresent() && posiblePost.isPresent() && posibleComentario.isPresent())){
            return ResponseEntity.ok(HttpStatus.NOT_FOUND);
        }
        else{
            comentarioRepository.delete(posibleComentario.get());

            return ResponseEntity.ok(HttpStatus.OK);
        }
    }

    @PutMapping(path = "/actualizar")
    public ResponseEntity putComentario(@RequestBody ComentarioHolder comentarioHolder){
        Optional<Usuario> posibleAutor = usuarioRepository.findById(comentarioHolder.getIdAutor());
        Optional<Post> posiblePost = postRepository.findById(comentarioHolder.getIdPost());
        Optional<Comentario> posibleComentario = comentarioRepository.findById(comentarioHolder.getIdComentario());

        if(!(posibleAutor.isPresent() && posiblePost.isPresent() && posibleComentario.isPresent())){
            return ResponseEntity.ok(HttpStatus.NOT_FOUND);
        }
        else{
            LocalDate fechaOriginal = posibleComentario.get().getCreacion();

            comentarioHolder.getComentario().setCreacion(fechaOriginal);
            comentarioHolder.getComentario().setId(posibleComentario.get().getId());
            comentarioHolder.getComentario().setAutorComentario(posibleAutor.get());
            comentarioHolder.getComentario().setPostPerteneciente(posiblePost.get());

            comentarioRepository.save(comentarioHolder.getComentario());

            return ResponseEntity.ok(HttpStatus.OK);
        }
    }
}
