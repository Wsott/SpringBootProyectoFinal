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
import java.util.LinkedList;
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
    public @ResponseBody Iterable<Comentario> getComentario(){
        return comentarioRepository.findAll();
    }

    @GetMapping(path = "/buscar")
    public @ResponseBody
    Iterable<Comentario> getComentarioFiltrado(@RequestParam int postId, @RequestParam(required = false) Integer cantidad){
        Iterable<Comentario> resultadosSinFiltrar = comentarioRepository.findAll();

        if(cantidad == null){
            LinkedList<Comentario> resultados = new LinkedList<>();

            for (Comentario actual : resultadosSinFiltrar){
                if (actual.getPostPerteneciente().getId() == postId){
                    resultados.add(actual);
                }
            }

            return resultados;
        }
        else{
            LinkedList<Comentario> lista = new LinkedList<>();

            for (Comentario actual : resultadosSinFiltrar){
                lista.add(actual);
            }

            LinkedList<Comentario> resultados = new LinkedList<>();

            int i = 0;

            while(i < lista.size()){
                if(!(resultados.size() < cantidad)){
                    break;
                }
                else{
                    if (lista.get(i).getId() == postId){
                        resultados.add(lista.get(i));
                    }
                    i++;
                }
            }

            return resultados;
        }
    }

    @PostMapping(path = "/crear")
    public ResponseEntity<String> postComentario(@RequestBody ComentarioHolder comentarioHolder){
        Optional<Usuario> posibleAutor = usuarioRepository.findById(comentarioHolder.getIdAutor());
        Optional<Post> posiblePost = postRepository.findById(comentarioHolder.getIdPost());

        if(!(posibleAutor.isPresent() && posiblePost.isPresent())){
            return new ResponseEntity<>(
                    "NOT FOUND: No se ha encontrado ningun usuario y/o post que coincida con las IDs especificadas.",
                    HttpStatus.NOT_FOUND);
        }
        else{
            comentarioHolder.getComentario().setAutorComentario(posibleAutor.get());
            comentarioHolder.getComentario().setPostPerteneciente(posiblePost.get());
            comentarioHolder.getComentario().setCreacion(LocalDate.now());

            comentarioRepository.save(comentarioHolder.getComentario());

            return new ResponseEntity<>(
                    "CREATED: Se ha creado un nuevo comentario.",
                    HttpStatus.CREATED);
        }
    }

    @DeleteMapping(path = "/borrar")
    public ResponseEntity<String> deleteComentario(@RequestBody ComentarioHolder comentarioHolder){
        Optional<Usuario> posibleAutor = usuarioRepository.findById(comentarioHolder.getIdAutor());
        Optional<Post> posiblePost = postRepository.findById(comentarioHolder.getIdPost());
        Optional<Comentario> posibleComentario = comentarioRepository.findById(comentarioHolder.getIdComentario());

        if(!(posibleAutor.isPresent() && posiblePost.isPresent() && posibleComentario.isPresent())){
            return new ResponseEntity<>(
                    "NOT FOUND: No se ha encontrado ningun usuario y/o post que coincida con las IDs especificadas.",
                    HttpStatus.NOT_FOUND);
        }
        else{
            if (posibleComentario.get().getAutorComentario().equals(posibleAutor.get())) {
                comentarioRepository.delete(posibleComentario.get());

                return new ResponseEntity<>(
                        "OK: Se ha eliminado el comentario del sistema.",
                        HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(
                        "FORBIDDEN: El usuario no puede borrar el comentario debido a que no es el autor.",
                        HttpStatus.FORBIDDEN);
            }
        }
    }

    @PutMapping(path = "/actualizar")
    public ResponseEntity<String> putComentario(@RequestBody ComentarioHolder comentarioHolder){
        Optional<Usuario> posibleAutor = usuarioRepository.findById(comentarioHolder.getIdAutor());
        Optional<Post> posiblePost = postRepository.findById(comentarioHolder.getIdPost());
        Optional<Comentario> posibleComentario = comentarioRepository.findById(comentarioHolder.getIdComentario());

        if(!(posibleAutor.isPresent() && posiblePost.isPresent() && posibleComentario.isPresent())){
            return new ResponseEntity<>(
                    "NOT FOUND: No se ha encontrado ningun usuario y/o post que coincida con las IDs especificadas.",
                    HttpStatus.NOT_FOUND);
        }
        else{
            if (posibleComentario.get().getAutorComentario().equals(posibleAutor.get())) {
                LocalDate fechaOriginal = posibleComentario.get().getCreacion();

                comentarioHolder.getComentario().setCreacion(fechaOriginal);
                comentarioHolder.getComentario().setId(posibleComentario.get().getId());
                comentarioHolder.getComentario().setAutorComentario(posibleAutor.get());
                comentarioHolder.getComentario().setPostPerteneciente(posiblePost.get());

                comentarioRepository.save(comentarioHolder.getComentario());

                return new ResponseEntity<>(
                        "OK: Se ha actualizado el comentario.",
                        HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(
                        "FORBIDDEN: El usuario no puede borrar el comentario debido a que no es el autor.",
                        HttpStatus.FORBIDDEN);
            }
        }
    }
}
