package com.example.sprinfapisql.service;

import com.example.sprinfapisql.models.Produto;
import com.example.sprinfapisql.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Map;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public List<Produto> buscarTodosProdutos() {
        return produtoRepository.findAll();
    }

    @Transactional
    public Produto salvarProduto(Produto produto){
        return produtoRepository.save(produto);
    }

    public Produto buscarProdutoPorId(Long id){
        return produtoRepository.findById(id).orElseThrow(() ->
            new RuntimeException("Produto não encontrado com id: "+id));
    }

    public List<Produto> buscarProdutosPorNome(String nome){
        return produtoRepository.findByNomeLikeIgnoreCase(nome);
    }

    public int contarQuantidadeEstoqueMenorIgual(int quant){
        return produtoRepository.countByQuantidadeEstoqueIsLessThanEqual(quant);
    }

    public void excluirProdutosPorQuantidade(int quant){
        produtoRepository.deleteByQuantidadeEstoqueIsLessThanEqual(quant);
    }

    public List<Produto> buscarPorNomeEPreco(String nome, double preco){
        return produtoRepository.findByNomeLikeIgnoreCaseAndPrecoLessThan(nome, preco);
    }

    @Transactional
    public Produto excluirProduto(Long id){
        Produto produto = buscarProdutoPorId(id);
        produtoRepository.delete(produto);
        return produto;
    }
}
