package com.example.sprinfapisql.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

@Entity
@Schema(description = "Representa um produto no sistema")
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador do produto", example = "777")
    private long id;

    @NotNull(message = "O nome não pode ser nulo")
    @Size(min=2,message = "O nome deve ter pelo menos dois caracteres")
    @Schema(description = "Nome do produto", example = "Batata")
    private String nome;

    @Schema(description = "Descrição do produto", example = "Batata Frita")
    private String descricao;

    @NotNull(message = "O preço não pode ser nulo")
    @Min(value=0,message = "O preço deve ser pelo menos 0")
    @Schema(description = "Preço do produto", example = "48.80")
    private double preco;

    @Column(name="quantidadeestoque")
    @NotNull(message = "A quantidade não pode ser nulo")
    @Min(value=0,message = "A quantidade deve ser no minimo 0")
    @Schema(description = "Quantidade em estoque do produto", example = "1")
    private int quantidadeEstoque;

    // métodos construtores
    public Produto(){};

    public Produto(String nome, String descricao, double preco, int quantidadeEstoque) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidadeEstoque = quantidadeEstoque;
    }

    // métodos getters e setters
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPreco() {
        return this.preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public Integer getquantidadeEstoque() {
        return this.quantidadeEstoque;
    }

    public void setquantidadeEstoque(Integer quantidadeestoque) {
        this.quantidadeEstoque = quantidadeestoque;
    }

    // método toString

    @Override
    public String toString() {
        return "\n======================================" +
                "\nid: " + id +
                "\nnome: " + nome +
                "\ndescricao: " + descricao +
                String.format("\npreco: R$%.2f", preco) +
                "\nquantidadeEstoque: " + quantidadeEstoque +
                "======================================";
    }
}
