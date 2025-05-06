package com.miempresa.franquicias.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "franchises")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Franchise {

    @Id
    private String id;

    private String name;

    private List<Branch> branches;
}