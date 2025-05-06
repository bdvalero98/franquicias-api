package com.miempresa.franquicias.service.impl;

import com.miempresa.franquicias.entity.Branch;
import com.miempresa.franquicias.entity.Franchise;
import com.miempresa.franquicias.entity.Product;
import com.miempresa.franquicias.repository.FranchiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FranchiseServiceImplTest {

    private FranchiseRepository franchiseRepository;
    private FranchiseServiceImpl franchiseService;

    @BeforeEach
    void setUp() {
        franchiseRepository = Mockito.mock(FranchiseRepository.class);
        franchiseService = new FranchiseServiceImpl(franchiseRepository);
    }

    @Test
    void createFranchise_shouldSaveFranchiseCorrectly() {
        Franchise franchise = new Franchise(null, "Café X", null);
        Franchise saved = new Franchise("1", "Café X", null);

        when(franchiseRepository.save(franchise)).thenReturn(Mono.just(saved));

        StepVerifier.create(franchiseService.createFranchise(franchise))
                .expectNext(saved)
                .verifyComplete();

        verify(franchiseRepository).save(franchise);
    }

    @Test
    void addBranch_shouldAddNewBranchToFranchise() {
        String franchiseId = "1";
        String branchName = "North Branch";
        Franchise franchise = new Franchise(franchiseId, "Café X", null);
        Franchise updated = new Franchise(franchiseId, "Café X",
                List.of(new Branch(branchName, List.of())));

        when(franchiseRepository.findById(franchiseId)).thenReturn(Mono.just(franchise));
        when(franchiseRepository.save(any(Franchise.class))).thenReturn(Mono.just(updated));

        StepVerifier.create(franchiseService.addBranch(franchiseId, branchName))
                .expectNextMatches(f -> f.getBranches().getFirst().getName().equals(branchName))
                .verifyComplete();

        verify(franchiseRepository).findById(franchiseId);
        verify(franchiseRepository).save(any(Franchise.class));
    }

    @Test
    void addProduct_shouldAddProductToExistingBranch() {
        String franchiseId = "1";
        String branchName = "North";
        String productName = "Latte";
        int stock = 10;

        List<Product> products = new ArrayList<>();
        Branch branch = new Branch(branchName, products);
        Franchise franchise = new Franchise(franchiseId, "Café X", new ArrayList<>(List.of(branch)));

        Product addedProduct = new Product(productName, stock);
        branch.getProducts().add(addedProduct);

        when(franchiseRepository.findById(franchiseId)).thenReturn(Mono.just(franchise));
        when(franchiseRepository.save(any(Franchise.class))).thenReturn(Mono.just(franchise));

        StepVerifier.create(franchiseService.addProduct(franchiseId, branchName, productName, stock))
                .expectNextMatches(f -> f.getBranches().stream()
                        .anyMatch(b -> b.getName().equalsIgnoreCase(branchName) &&
                                b.getProducts().stream()
                                        .anyMatch(p -> p.getName().equals(productName) && p.getStock() == stock)))
                .verifyComplete();

        verify(franchiseRepository).findById(franchiseId);
        verify(franchiseRepository).save(any(Franchise.class));
    }

    @Test
    void deleteProduct_shouldRemoveProductFromBranch() {
        String franchiseId = "1";
        String branchName = "North Branch";
        String productName = "Latte";

        Product product = new Product(productName, 10);
        Branch branch = new Branch(branchName, new ArrayList<>(List.of(product)));
        Franchise franchise = new Franchise(franchiseId, "Café X", List.of(branch));

        when(franchiseRepository.findById(franchiseId)).thenReturn(Mono.just(franchise));
        when(franchiseRepository.save(any(Franchise.class))).thenReturn(Mono.just(franchise));

        StepVerifier.create(franchiseService.deleteProduct(franchiseId, branchName, productName))
                .expectNextMatches(f -> f.getBranches().getFirst().getProducts().isEmpty())
                .verifyComplete();

        verify(franchiseRepository).findById(franchiseId);
        verify(franchiseRepository).save(any(Franchise.class));
    }

    @Test
    void updateProductStock_shouldUpdateProductStock() {
        String franchiseId = "1";
        String branchName = "North Branch";
        String productName = "Latte";
        int newStock = 20;

        Product product = new Product(productName, 10);
        Branch branch = new Branch(branchName, new ArrayList<>(List.of(product)));
        Franchise franchise = new Franchise(franchiseId, "Café X", List.of(branch));

        when(franchiseRepository.findById(franchiseId)).thenReturn(Mono.just(franchise));
        when(franchiseRepository.save(any(Franchise.class))).thenReturn(Mono.just(franchise));

        StepVerifier.create(franchiseService.updateProductStock(franchiseId, branchName, productName, newStock))
                .expectNextMatches(f -> f.getBranches().getFirst().getProducts().getFirst().getStock() == newStock)
                .verifyComplete();

        verify(franchiseRepository).findById(franchiseId);
        verify(franchiseRepository).save(any(Franchise.class));
    }

    @Test
    void getTopStockProductPerBranch_shouldReturnCorrectProducts() {
        String franchiseId = "1";

        Product p1 = new Product("Latte", 10);
        Product p2 = new Product("Cappuccino", 15);
        Product p3 = new Product("Tea", 8);

        Branch branch1 = new Branch("North", List.of(p1, p2));
        Branch branch2 = new Branch("South", List.of(p3));

        Franchise franchise = new Franchise(franchiseId, "Café X", List.of(branch1, branch2));

        when(franchiseRepository.findById(franchiseId)).thenReturn(Mono.just(franchise));

        StepVerifier.create(franchiseService.getTopStockProductPerBranch(franchiseId))
                .expectNext("Branch: North -> Product: Cappuccino(Stock: 15)")
                .expectNext("Branch: South -> Product: Tea(Stock: 8)")
                .verifyComplete();

        verify(franchiseRepository).findById(franchiseId);
    }

    @Test
    void updateFranchiseName_shouldUpdateNameCorrectly() {
        String franchiseId = "1";
        String newName = "Modern Café";
        Franchise original = new Franchise(franchiseId, "Old Café", List.of());
        Franchise updated = new Franchise(franchiseId, newName, List.of());

        when(franchiseRepository.findById(franchiseId)).thenReturn(Mono.just(original));
        when(franchiseRepository.save(any(Franchise.class))).thenReturn(Mono.just(updated));

        StepVerifier.create(franchiseService.updateFranchiseName(franchiseId, newName))
                .expectNextMatches(f -> f.getName().equals(newName))
                .verifyComplete();

        verify(franchiseRepository).findById(franchiseId);
        verify(franchiseRepository).save(any(Franchise.class));
    }

    @Test
    void updateBranchName_shouldUpdateNameCorrectly() {
        String franchiseId = "1";
        String currentName = "North";
        String newName = "Center";

        Branch branch = new Branch(currentName, List.of());
        Franchise original = new Franchise(franchiseId, "Café X", new ArrayList<>(List.of(branch)));

        branch.setName(newName);
        Franchise updated = new Franchise(franchiseId, "Café X", List.of(branch));

        when(franchiseRepository.findById(franchiseId)).thenReturn(Mono.just(original));
        when(franchiseRepository.save(any(Franchise.class))).thenReturn(Mono.just(updated));

        StepVerifier.create(franchiseService.updateBranchName(franchiseId, currentName, newName))
                .expectNextMatches(f -> f.getBranches().getFirst().getName().equals(newName))
                .verifyComplete();

        verify(franchiseRepository).findById(franchiseId);
        verify(franchiseRepository).save(any(Franchise.class));
    }

    @Test
    void updateProductName_shouldUpdateNameCorrectly() {
        String franchiseId = "1";
        String branchName = "North";
        String currentProductName = "Latte";
        String newProductName = "Special Latte";

        Product product = new Product(currentProductName, 10);
        Branch branch = new Branch(branchName, new ArrayList<>(List.of(product)));
        Franchise original = new Franchise(franchiseId, "Café X", new ArrayList<>(List.of(branch)));

        product.setName(newProductName);
        Franchise updated = new Franchise(franchiseId, "Café X", List.of(branch));

        when(franchiseRepository.findById(franchiseId)).thenReturn(Mono.just(original));
        when(franchiseRepository.save(any(Franchise.class))).thenReturn(Mono.just(updated));

        StepVerifier.create(franchiseService.updateProductName(franchiseId, branchName, currentProductName, newProductName))
                .expectNextMatches(f -> f.getBranches().getFirst().getProducts().getFirst().getName().equals(newProductName))
                .verifyComplete();

        verify(franchiseRepository).findById(franchiseId);
        verify(franchiseRepository).save(any(Franchise.class));
    }
}
