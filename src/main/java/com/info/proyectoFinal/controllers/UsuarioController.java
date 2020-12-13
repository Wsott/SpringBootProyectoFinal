package com.info.proyectoFinal.controllers;

import com.info.proyectoFinal.models.Usuario;
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
@RequestMapping(path="/usuario")
public class UsuarioController {
    @Autowired
    UsuarioRepository usuarioRepository;

    @GetMapping(path = "/")
    public @ResponseBody Iterable<Usuario> getUsuario(){
        return obtenerUsuarios();
    }

    @PostMapping(path = "/crear")
    public ResponseEntity postUsuario(@RequestBody Usuario usuario){
        try {
            usuario.setCreacion(LocalDate.now());
            usuarioRepository.save(usuario);

            return ResponseEntity.ok(HttpStatus.OK);
        }
        catch (Exception e){
            return ResponseEntity.ok(HttpStatus.CONFLICT);
        }
    }

    @PutMapping(path = "/actualizar/{id}")
    public ResponseEntity putUsuario(@RequestBody Usuario usuario, @PathVariable int id){
        Optional<Usuario> resultado = usuarioRepository.findById(id);

        if(!(resultado.isPresent())){
            return ResponseEntity.ok(HttpStatus.NOT_FOUND);
        }
        else{
            LocalDate fechaOriginal = resultado.get().getCreacion();
            usuario.setId(id);
            usuario.setCreacion(fechaOriginal);
            usuarioRepository.save(usuario);

            return ResponseEntity.ok(HttpStatus.OK);
        }
    }

    @DeleteMapping(path = "/borrar/{id}")
    public ResponseEntity deleteUsuario(@PathVariable int id){
        Optional<Usuario> resultado = usuarioRepository.findById(id);

        if(!(resultado.isPresent())){
            return ResponseEntity.ok(HttpStatus.NOT_FOUND);
        }
        else{
            usuarioRepository.delete(resultado.get());

            return ResponseEntity.ok(HttpStatus.OK);
        }
    }

    @GetMapping(path = "/filtrar")
    public @ResponseBody Iterable<Usuario> filtrarUsuario(@RequestParam String tipo, @RequestParam String valor){
        return filtrar(tipo, valor);
    }

    private LinkedList<Usuario> filtrar(String tipo, String valor){
        LinkedList<Usuario> resultadoFiltrado = new LinkedList<Usuario>();
        Iterable<Usuario> resultado = obtenerUsuarios();
        valor = valor.toLowerCase();

        switch (tipo.toLowerCase()) {
            case "nombre":
                for (Usuario actual : resultado){
                    if (actual.getNombre().toLowerCase().equals(valor)){
                        resultadoFiltrado.add(actual);
                    }
                }
                break;

            case "apellido":
                for (Usuario actual : resultado){
                    if (actual.getApellido().toLowerCase().equals(valor)){
                        resultadoFiltrado.add(actual);
                    }
                }
                break;

            case "ciudad":
                for (Usuario actual : resultado){
                    if (actual.getCiudad().toLowerCase().equals(valor)){
                        resultadoFiltrado.add(actual);
                    }
                }
                break;

            case "provincia":
                for (Usuario actual : resultado){
                    if (actual.getProvincia().toLowerCase().equals(valor)){
                        resultadoFiltrado.add(actual);
                    }
                }
                break;

            case "pais":
                for (Usuario actual : resultado){
                    if (actual.getPais().toLowerCase().equals(valor)){
                        resultadoFiltrado.add(actual);
                    }
                }
                break;

            case "creacion":
                for (Usuario actual : resultado){
                    LocalDate referencia = LocalDate.parse(valor);
                    if (actual.getCreacion().isAfter(referencia)){
                        resultadoFiltrado.add(actual);
                    }
                }
                break;
        }

        return resultadoFiltrado;
    }

    private Iterable<Usuario> obtenerUsuarios(){
        Iterable<Usuario> lista = usuarioRepository.findAll();

        for (Usuario actual : lista){
            actual.setPassword(null);
        }

        return lista;
    }
}
