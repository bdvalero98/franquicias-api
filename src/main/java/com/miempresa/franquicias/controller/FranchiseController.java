package com.miempresa.franquicias.controller;

import com.miempresa.franquicias.entity.Franchise;
import com.miempresa.franquicias.service.FranchiseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/franchises")
@RequiredArgsConstructor
public class FranchiseController {

    private final FranchiseService franchiseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Franchise> createFranchise(@RequestBody Franchise franchise) {
        return franchiseService.createFranchise(franchise);
    }

    @PostMapping("/{franchiseId}/branches")
    public Mono<Franchise> addBranch(
            @PathVariable String franchiseId,
            @RequestParam String branchName) {
        return franchiseService.addBranch(franchiseId, branchName);
    }

    @PostMapping("/{franchiseId}/branches/{branchName}/products")
    public Mono<Franchise> addProduct(
            @PathVariable String franchiseId,
            @PathVariable String branchName,
            @RequestParam String productName,
            @RequestParam int stock) {
        return franchiseService.addProduct(franchiseId, branchName, productName, stock);
    }

    @DeleteMapping("/{franchiseId}/branches/{branchName}/products")
    public Mono<Franchise> deleteProduct(
            @PathVariable String franchiseId,
            @PathVariable String branchName,
            @RequestParam String productName) {
        return franchiseService.deleteProduct(franchiseId, branchName, productName);
    }

    @PatchMapping("/{franchiseId}/branches/{branchName}/products/stock")
    public Mono<Franchise> updateProductStock(
            @PathVariable String franchiseId,
            @PathVariable String branchName,
            @RequestParam String productName,
            @RequestParam int newStock) {
        return franchiseService.updateProductStock(franchiseId, branchName, productName, newStock);
    }

    @GetMapping("/{franchiseId}/top-stock-products")
    public Flux<String> getTopStockProductPerBranch(@PathVariable String franchiseId) {
        return franchiseService.getTopStockProductPerBranch(franchiseId);
    }

    @PatchMapping("/{franchiseId}/name")
    public Mono<Franchise> updateFranchiseName(
            @PathVariable String franchiseId,
            @RequestParam String newName) {
        return franchiseService.updateFranchiseName(franchiseId, newName);
    }

    @PatchMapping("/{franchiseId}/branches/name")
    public Mono<Franchise> updateBranchName(
            @PathVariable String franchiseId,
            @RequestParam String currentName,
            @RequestParam String newName) {
        return franchiseService.updateBranchName(franchiseId, currentName, newName);
    }

    @PatchMapping("/{franchiseId}/branches/{branchName}/products/name")
    public Mono<Franchise> updateProductName(
            @PathVariable String franchiseId,
            @PathVariable String branchName,
            @RequestParam String currentName,
            @RequestParam String newName) {
        return franchiseService.updateProductName(franchiseId, branchName, currentName, newName);
    }

}
