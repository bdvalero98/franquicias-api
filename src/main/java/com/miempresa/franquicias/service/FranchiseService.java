package com.miempresa.franquicias.service;

import com.miempresa.franquicias.entity.Franchise;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranchiseService {

    Mono<Franchise> createFranchise(Franchise franchise);

    Mono<Franchise> addBranch(String franchiseId, String branchName);

    Mono<Franchise> addProduct(String franchiseId, String branchName, String productName, int stock);

    Mono<Franchise> deleteProduct(String franchiseId, String branchName, String productName);

    Mono<Franchise> updateProductStock(String franchiseId, String branchName, String productName, int newStock);

    Flux<String> getTopStockProductPerBranch(String franchiseId);

    Mono<Franchise> updateFranchiseName(String franchiseId, String newName);

    Mono<Franchise> updateBranchName(String franchiseId, String currentBranchName, String newName);

    Mono<Franchise> updateProductName(String franchiseId, String branchName, String currentProductName, String newName);

}
