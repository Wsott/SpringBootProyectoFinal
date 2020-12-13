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

    @PostMapping(path = "/{autor}/crear")
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
}
