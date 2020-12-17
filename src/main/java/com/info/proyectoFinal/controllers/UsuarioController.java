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

/**
 * Controller de la entidad usuario del sistema
 *
 * @author Emiliano Gómez Olivera
 * @Version 1.0
 */
@Controller
@RequestMapping(path="/usuario")
public class UsuarioController {
    @Autowired
    UsuarioRepository usuarioRepository;

    /**
    *    Funcion que solicita todos los usuarios de la base de datos
    *
    *   Parametros:
    *       -
    *   Return:
    *        Iterable<Usuario> => Un iterable con todos los usuarios almacenados en la base de datos
    *    URL:
    *        localhost:8080/usuario/
    * */

    @GetMapping(path = "/")
    public @ResponseBody Iterable<Usuario> getUsuario(){
        return usuarioRepository.findAll();
    }



    /*
     *   Funcion que recibe un body con la informacion necesaria para crear un nuevo usuario en la base de datos
     *
     *   Parametros:
     *       @RequestBody Usuario usuario => Json con los datos del usuario
     *   Return:
     *       ResponseEntity => Respuesta sobre si la operacion se completo con exito o si se produjo un error.
     *   URL:
     *       localhost:8080/usuario/crear
     *   Body template:
     *       {
     *           "nombre": "NOMBRE_DEL_USUARIO",
     *           "apellido": "APELLIDO_DEL_USUARIO",
     *           "email": "EMAIL_DEL_USUARIO",
     *           "password": "PASS_DEL_USUARIO",
     *           "ciudad": "CIUDAD_DEL_USUARIO",
     *           "provincia": "PROVINCIA_DEL_USUARIO",
     *           "pais": "PAIS_DEL_USUARIO"
     *       }
     * */
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



    /*
        Recibe un body con la informacion actualizada de un usuario, ademas, recibe como parametro el ID del usuario
        que tendra las modificaciones

        Parametros:
            @PathVariable int id => ID del usuario que se va a actualizar
            @RequestBody Usuario usuario => Json con los datos del usuario
        Return:
            ResponseEntity => Respuesta sobre si la operacion se completo con exito o si se produjo un error.
        URL:
            localhost:8080/usuario/crear
        Body template:
            {
                "nombre": "NOMBRE_DEL_USUARIO",
                "apellido": "APELLIDO_DEL_USUARIO",
                "email": "EMAIL_DEL_USUARIO",
                "password": "PASS_DEL_USUARIO",
                "ciudad": "CIUDAD_DEL_USUARIO",
                "provincia": "PROVINCIA_DEL_USUARIO",
                "pais": "PAIS_DEL_USUARIO"
            }
    */
    @PutMapping(path = "/actualizar")
    public ResponseEntity putUsuario(@RequestBody PutUsuarioHolder usuarioHolder){
        Optional<Usuario> resultado = usuarioRepository.findById(usuarioHolder.getId());

        if(!(resultado.isPresent())){
            return ResponseEntity.ok(HttpStatus.NOT_FOUND);
        }
        else{
            LocalDate fechaOriginal = resultado.get().getCreacion();
            usuarioHolder.getUsuario().setId(usuarioHolder.getId());
            usuarioHolder.getUsuario().setCreacion(fechaOriginal);
            usuarioRepository.save(usuarioHolder.getUsuario());

            return ResponseEntity.ok(HttpStatus.OK);
        }
    }

    @DeleteMapping(path = "/borrar")
    public ResponseEntity deleteUsuario(@RequestBody PutUsuarioHolder usuarioHolder){
        Optional<Usuario> resultado = usuarioRepository.findById(usuarioHolder.getId());

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
                return (LinkedList<Usuario>) resultado;
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
