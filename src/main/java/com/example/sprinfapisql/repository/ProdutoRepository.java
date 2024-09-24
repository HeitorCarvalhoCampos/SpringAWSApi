package com.example.sprinfapisql.repository;

import com.example.sprinfapisql.models.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

// a interface vai estender a JpaRepository usando um objeto da classe Produto e um Id do tipo long
// o JpaRepository fornece métodos e funcionalidades para realizar operações de CRUD em entidades
// também incluir muitos métodos padrão para trabalhar com entidades
public interface    ProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findByNomeLikeIgnoreCase(String nome);

    int countByQuantidadeEstoqueIsLessThanEqual(int quant);

    void deleteByQuantidadeEstoqueIsLessThanEqual(int quant);

    List <Produto> findByNomeLikeIgnoreCaseAndPrecoLessThan(String nome, double preco);
    // criação de métodos
    //    Long deleteProdutoById(Long id);

    // modificar métodos já existentes
    @Modifying
    @Query ("DELETE FROM Produto obj WHERE :id = obj.id")
    void deleteById(Long id);
}
