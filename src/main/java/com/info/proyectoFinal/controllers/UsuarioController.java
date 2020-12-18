package com.info.proyectoFinal.controllers;

import com.info.proyectoFinal.holder.PutUsuarioHolder;
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
        return usuarioRepository.findAll();
    }


    @PostMapping(path = "/crear")
    public @ResponseBody ResponseEntity<String> postUsuario(@RequestBody Usuario usuario){
        try {
            usuario.setCreacion(LocalDate.now());
            usuarioRepository.save(usuario);

            return new ResponseEntity<>(
                    "CREATED: Se ha creado un nuevo usuario.",
                    HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>(
                    "CONFLICT: Verifique que el email sea unico y que todos los campos esten cargados.",
                    HttpStatus.CONFLICT);
        }
    }


    @PutMapping(path = "/actualizar")
    public ResponseEntity<String> putUsuario(@RequestBody PutUsuarioHolder usuarioHolder){
        Optional<Usuario> resultado = usuarioRepository.findById(usuarioHolder.getId());

        if(!(resultado.isPresent())){
            return new ResponseEntity<>(
                    "NOT FOUND: No se ha encontrado ningun usuario que coincida con la ID especificada.",
                    HttpStatus.NOT_FOUND);
        }
        else{
            LocalDate fechaOriginal = resultado.get().getCreacion();
            usuarioHolder.getUsuario().setId(usuarioHolder.getId());
            usuarioHolder.getUsuario().setCreacion(fechaOriginal);
            usuarioRepository.save(usuarioHolder.getUsuario());

            return new ResponseEntity<>(
                    "OK: Se ha actualizado la informacion del usuario.",
                    HttpStatus.OK);
        }
    }

    @DeleteMapping(path = "/borrar")
    public ResponseEntity<String> deleteUsuario(@RequestBody PutUsuarioHolder usuarioHolder){
        Optional<Usuario> resultado = usuarioRepository.findById(usuarioHolder.getId());

        if(!(resultado.isPresent())){
            return new ResponseEntity<>(
                    "NOT FOUND: No se ha encontrado ningun usuario que coincida con la ID especificada.",
                    HttpStatus.NOT_FOUND);
        }
        else{
            usuarioRepository.delete(resultado.get());

            return new ResponseEntity<>(
                    "OK: Se ha eliminado al usuario del sistema.",
                    HttpStatus.OK);
        }
    }

    @GetMapping(path = "/filtrar")
    public @ResponseBody Iterable<Usuario> filtrarUsuario(@RequestParam String tipo, @RequestParam String valor){
        return filtrar(tipo, valor);
    }

    private LinkedList<Usuario> filtrar(String tipo, String valor){
        LinkedList<Usuario> resultadoFiltrado = new LinkedList<Usuario>();
        Iterable<Usuario> resultado = usuarioRepository.findAll();
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

            default:
                LinkedList<Usuario> lista = new LinkedList<>();

                for (Usuario actual : resultado){
                    lista.add(actual);
                }
                return lista;
        }

        return resultadoFiltrado;
    }
}
