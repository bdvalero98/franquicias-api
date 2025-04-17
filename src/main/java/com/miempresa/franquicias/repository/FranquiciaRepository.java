package com.miempresa.franquicias.repository;

import com.miempresa.franquicias.model.Franquicia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FranquiciaRepository extends JpaRepository<Franquicia, Long> {

    boolean existsByNombre(String nombre);
}
