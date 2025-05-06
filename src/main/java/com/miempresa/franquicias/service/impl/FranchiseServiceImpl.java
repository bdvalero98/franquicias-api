package com.miempresa.franquicias.service.impl;

import com.miempresa.franquicias.entity.Branch;
import com.miempresa.franquicias.entity.Franchise;
import com.miempresa.franquicias.entity.Product;
import com.miempresa.franquicias.repository.FranchiseRepository;
import com.miempresa.franquicias.service.FranchiseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FranchiseServiceImpl implements FranchiseService {

    private final FranchiseRepository franchiseRepository;

    @Override
    public Mono<Franchise> createFranchise(Franchise franchise) {
        return franchiseRepository.save(franchise);
    }

    @Override
    public Mono<Franchise> addBranch(String franchiseId, String branchName) {
        return franchiseRepository.findById(franchiseId)
                .map(
                        franchise -> {
                            if (franchise.getBranches() == null) franchise.setBranches(new ArrayList<>());
                            franchise.getBranches().add(new Branch(branchName, new ArrayList<>()));
                            return franchise;
                        }
                )
                .flatMap(franchiseRepository::save);
    }

    @Override
    public Mono<Franchise> addProduct(String franchiseId, String branchName, String productName, int stock) {
        return franchiseRepository.findById(franchiseId)
                .map(
                        franchise -> {
                            franchise.getBranches().forEach(
                                    branch -> {
                                        if (branch.getName().equalsIgnoreCase(branchName)) {
                                            if (branch.getProducts() == null)
                                                branch.setProducts(new ArrayList<>());
                                            branch.getProducts().add(new Product(productName, stock));
                                        }
                                    });
                            return franchise;
                        })
                .flatMap(franchiseRepository::save);
    }

    @Override
    public Mono<Franchise> deleteProduct(String franchiseId, String branchName, String productName) {
        return franchiseRepository.findById(franchiseId)
                .map(
                        franchise -> {
                            franchise.getBranches().forEach(
                                    branch -> {
                                        if (branch.getName().equalsIgnoreCase(branchName)) {
                                            if (branch.getProducts() != null) {
                                                branch.getProducts().removeIf(
                                                        p -> p.getName().equalsIgnoreCase(productName)
                                                );
                                            }
                                        }
                                    }
                            );
                            return franchise;
                        }
                )
                .flatMap(franchiseRepository::save);
    }

    @Override
    public Mono<Franchise> updateProductStock(String franchiseId, String branchName, String productName, int newStock) {
        return franchiseRepository.findById(franchiseId)
                .map(
                        franchise -> {
                            franchise.getBranches().forEach(
                                    branch -> {
                                        if (branch.getName().equalsIgnoreCase(branchName)) {
                                            if (branch.getProducts() != null) {
                                                branch.getProducts().forEach(
                                                        p -> {
                                                            if (p.getName().equalsIgnoreCase(productName)) {
                                                                p.setStock(newStock);
                                                            }
                                                        }
                                                );
                                            }
                                        }
                                    }
                            );
                            return franchise;
                        }
                )
                .flatMap(franchiseRepository::save);
    }

    @Override
    public Flux<String> getTopStockProductPerBranch(String franchiseId) {
        return franchiseRepository.findById(franchiseId)
                .flatMapMany(
                        franchise -> {
                            List<String> result = new ArrayList<>();
                            for (Branch branch : franchise.getBranches()) {
                                if (branch.getProducts() != null && !branch.getProducts().isEmpty()) {
                                    Product maxStock = Collections.max(branch.getProducts(),
                                            Comparator.comparingInt(Product::getStock));
                                    result.add("Branch: " + branch.getName() + " -> Product: " + maxStock.getName() + "(Stock: " + maxStock.getStock() + ")");
                                }
                            }
                            return Flux.fromIterable(result);
                        }
                );
    }

    @Override
    public Mono<Franchise> updateFranchiseName(String franchiseId, String newName) {
        return franchiseRepository.findById(franchiseId)
                .map(franchise -> {
                    franchise.setName(newName);
                    return franchise;
                })
                .flatMap(franchiseRepository::save);
    }

    @Override
    public Mono<Franchise> updateBranchName(String franchiseId, String currentBranchName,
                                            String newName) {
        return franchiseRepository.findById(franchiseId)
                .map(franchise -> {
                    franchise.getBranches().stream()
                            .filter(s -> s.getName().equalsIgnoreCase(currentBranchName))
                            .findFirst()
                            .ifPresent(s -> s.setName(newName));
                    return franchise;
                })
                .flatMap(franchiseRepository::save);
    }

    @Override
    public Mono<Franchise> updateProductName(String franchiseId, String branchName,
                                             String currentProductName, String newName) {
        return franchiseRepository.findById(franchiseId)
                .map(franchise -> {
                    franchise.getBranches().stream()
                            .filter(s -> s.getName().equalsIgnoreCase(branchName))
                            .findFirst().flatMap(branch -> branch.getProducts().stream()
                                    .filter(p -> p.getName().equalsIgnoreCase(currentProductName))
                                    .findFirst()).ifPresent(p -> p.setName(newName));
                    return franchise;
                })
                .flatMap(franchiseRepository::save);
    }
}
