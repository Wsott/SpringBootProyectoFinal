package com.info.proyectoFinal.repositories;

import com.info.proyectoFinal.models.Usuario;
import org.springframework.data.repository.CrudRepository;

public interface UsuarioRepository extends CrudRepository<Usuario, Integer> {
}
