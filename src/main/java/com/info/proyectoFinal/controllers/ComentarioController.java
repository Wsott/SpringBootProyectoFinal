package com.info.proyectoFinal.controllers;

import com.info.proyectoFinal.holder.ComentarioHolder;
import com.info.proyectoFinal.models.Comentario;
import com.info.proyectoFinal.models.Post;
import com.info.proyectoFinal.models.Usuario;
import com.info.proyectoFinal.repositories.ComentarioRepository;
import com.info.proyectoFinal.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping(path="/comentario")
public class ComentarioController {
    @Autowired
    ComentarioRepository comentarioRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @GetMapping(path = "/")
    public @ResponseBody
    Iterable<Comentario> getComentario(){
        return comentarioRepository.findAll();
    }

    @PostMapping(path = "/crear")
    public ResponseEntity postPost(@RequestBody ComentarioHolder comentarioHolder){
        Optional<Usuario> posibleAutor = usuarioRepository.findById(comentarioHolder.getIdAutor());

        if(!(posibleAutor.isPresent())){
            return ResponseEntity.ok(HttpStatus.NOT_FOUND);
        }
        else{
            comentarioHolder.getComentario().setAutorComentario(posibleAutor.get());
            comentarioRepository.save(comentarioHolder.getComentario());

            return ResponseEntity.ok(HttpStatus.OK);
        }
    }
}
