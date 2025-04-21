package com.miempresa.franquicias.repository;

import com.miempresa.franquicias.entity.Franquicia;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface FranquiciaRepository extends ReactiveMongoRepository<Franquicia, String> {

    Mono<Franquicia> findByNombre(String nombre);
}
