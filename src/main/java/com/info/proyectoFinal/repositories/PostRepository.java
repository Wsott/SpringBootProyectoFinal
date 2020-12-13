package com.info.proyectoFinal.repositories;

import com.info.proyectoFinal.models.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Integer> {
}
