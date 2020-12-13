package com.info.proyectoFinal.controllers;

import com.info.proyectoFinal.models.Post;
import com.info.proyectoFinal.models.Usuario;
import com.info.proyectoFinal.repositories.PostRepository;
import com.info.proyectoFinal.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Optional;

@Controller
@RequestMapping(path="/post")
public class PostController {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping(path = "/")
    public @ResponseBody Iterable<Post> getPost(){
        return postRepository.findAll();
    }

    @PostMapping(path = "/crear/{autor}")
    public ResponseEntity postPost(@RequestBody Post post, @PathVariable int autor){
        Optional<Usuario> posibleAutor = usuarioRepository.findById(autor);

        if(!(posibleAutor.isPresent())){
            return ResponseEntity.ok(HttpStatus.NOT_FOUND);
        }
        else{
            post.setAutor(posibleAutor.get());
            postRepository.save(post);

            return ResponseEntity.ok(HttpStatus.OK);
        }
    }

    @PutMapping(path = "/actualizar/{autor}/{idPost}")
    public ResponseEntity putPost(@RequestBody Post post, @PathVariable int autor, @PathVariable int idPost){
        Optional<Usuario> posibleAutor = usuarioRepository.findById(autor);
        Optional<Post> posiblePost = postRepository.findById(idPost);

        if(!(posibleAutor.isPresent() && posiblePost.isPresent())){
            return ResponseEntity.ok(HttpStatus.NOT_FOUND);
        }
        else{
            if(posiblePost.get().getAutor().equals(posibleAutor.get())){
                LocalDate fechaOriginal = posiblePost.get().getCreacion();
                post.setId(idPost);
                post.setCreacion(fechaOriginal);
                post.setAutor(posibleAutor.get());
                postRepository.save(post);

                return ResponseEntity.ok(HttpStatus.OK);
            }
            else{
                return ResponseEntity.ok(HttpStatus.FORBIDDEN);
            }
        }
    }

    @DeleteMapping(path = "/borrar/{autor}/{idPost}")
    public ResponseEntity deletePost(@PathVariable int autor, @PathVariable int idPost){
        Optional<Usuario> posibleAutor = usuarioRepository.findById(autor);
        Optional<Post> posiblePost = postRepository.findById(idPost);

        if(!(posibleAutor.isPresent() && posiblePost.isPresent())){
            return ResponseEntity.ok(HttpStatus.NOT_FOUND);
        }
        else{
            if(posiblePost.get().getAutor().equals(posibleAutor.get())){
                postRepository.delete(posiblePost.get());

                return ResponseEntity.ok(HttpStatus.OK);
            }
            else{
                return ResponseEntity.ok(HttpStatus.FORBIDDEN);
            }
        }
    }

    @GetMapping(path = "/buscar/{palabra}")
    public @ResponseBody Iterable<Post> buscarPost(@PathVariable String palabra){
        Iterable<Post> resultados = postRepository.findAll();
        LinkedList<Post> resultadosFiltrados = new LinkedList<>();

        for (Post actual : resultados){
            if (actual.getTitulo().toLowerCase().contains(palabra.toLowerCase())){
                resultadosFiltrados.add(actual);
            }
        }

        return resultadosFiltrados;
    }

    @GetMapping(path = "/publicados")
    public @ResponseBody Iterable<Post> buscarPost(@RequestParam int estado){
        Iterable<Post> resultados = postRepository.findAll();
        LinkedList<Post> resultadosFiltrados = new LinkedList<>();
        boolean filtro = estado == 1;

        for (Post actual : resultados){
            if (actual.isPublicado() == filtro){
                resultadosFiltrados.add(actual);
            }
        }

        return resultadosFiltrados;
    }
}
