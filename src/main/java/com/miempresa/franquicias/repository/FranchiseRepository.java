package com.miempresa.franquicias.repository;

import com.miempresa.franquicias.entity.Franchise;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface FranchiseRepository extends ReactiveMongoRepository<Franchise, String> {

    Mono<Franchise> findByName(String name);
}
