package com.example.sprinfapisql.controllers;

import com.example.sprinfapisql.models.Produto;
import com.example.sprinfapisql.repository.ProdutoRepository;
import com.example.sprinfapisql.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.Binding;
import java.util.*;
import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {
    private final ProdutoService produtoService;
    private final Validator validator;
    private final ProdutoRepository produtoRepository;

    // injeção de dependência, criando um bean
    @Autowired
    public ProdutoController(ProdutoService produtoService , Validator validator, ProdutoRepository produtoRepository) {
        this.produtoService = produtoService;
        this.validator = validator;
        this.produtoRepository = produtoRepository;
    }

    @GetMapping("/selecionar")
    @Operation(summary = "Lista todos os produtos",
            description = "Retorna uma lista de todos os produtos disponíveis")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de todos os produtos retornada com sucesso",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content)
    })
    public List<Produto> listarProdutos() {
        return produtoService.buscarTodosProdutos();
//        List<Produto> listaprodutos = produtoService.buscarTodosProdutos();
//        return ResponseEntity.ok(listaprodutos);
    }

    @PostMapping("/inserir")
    @Operation(summary = "Insere novo produto",
            description = "Insere/Registra um novo produto com base nas informações dele passadas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto inserido com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "404", description = "Erro ao inseririr o produto",
                    content = @Content)
    })
    // método post, o parâmetro vai estar no corpo da mensagem
    public ResponseEntity<String> inserirProduto(@Valid @RequestBody Produto produto, BindingResult resultado) {
        // @RequestBody - está falando que como parâmetro vamos passar um JSON, mas, ele que transforma
        // o objeto Produto em JSON, depois ele coloca no body da API um produto ao acionarmos a API.

        // método save que insere, faz a mesma coisa que o persist do JPA
        // além de inserir ele já salva também

        if (!resultado.hasErrors()) {
            try {
                produtoService.salvarProduto(produto);
                return ResponseEntity.ok("Produto inserido com sucesso");
            } catch (DataIntegrityViolationException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
        }
        else{
            StringBuilder erro = new StringBuilder("Erros ao inserir produto:");
            for (FieldError error : resultado.getFieldErrors()) {
                erro.append("\n- ").append(error.getDefaultMessage()).append(";");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro.toString());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erros ao inserir produto: "+resultado.getAllErrors());
        }

    }
    // está retornando uma mensagem de ok, código 200

    @DeleteMapping("/excluir/{id}")
    @Transactional
    public ResponseEntity<String> excluirProduto(@PathVariable Long id) {
        // a única coisa que muda é que um parâmetro será extraído da url, no caso, o ID
        // para chamar isso no request http: DELETE http://localhost:8080/api/produtos/excluir/<id>
        // exemplo: DELETE http://localhost:8080/api/produtos/excluir/11

        // @PathVariable pega o parâmetro

//        Optional<Produto> produtoExistente = produtoRepository.findById(id);
//
//        if (produtoExistente.isPresent()) {
//            produtoRepository.delete(produtoExistente.get());
//            return ResponseEntity.status(HttpStatus.OK).body("Produto excluído com sucesso");
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
//        }

//        Long produtoDeletado = produtoRepository.deleteProdutoById(id);
//        if (produtoDeletado > 0) {
//            return ResponseEntity.status(HttpStatus.OK).body("Produto excluído com sucesso");
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
//        }

        Produto produto = produtoService.buscarProdutoPorId(id);
        produtoService.excluirProduto(id);
        return ResponseEntity.status(HttpStatus.OK).body("Produto excluído com sucesso");
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizarProduto(@PathVariable Long id, @Valid @RequestBody Produto produtoAtualizado,BindingResult resultado) {

        // produtoAtualizado é o que você tá passando na requisição no body, como que a gente quer que o produto fique
        if(!resultado.hasErrors()) {
            Produto produto = produtoService.buscarProdutoPorId(id);
            // o Optional recebe ou um objeto Produto ou vazio
            // precisamos do Optional para verificar se o produto existe
            // save = se o id não existir, insere, se não, atualiza

//            Map erros=validarProduto(resultado);
//            return ResponseEntity.badRequest().body(erros);

            produto.setNome(produtoAtualizado.getNome());
            produto.setDescricao(produtoAtualizado.getDescricao());
            produto.setPreco(produtoAtualizado.getPreco());
            produto.setquantidadeEstoque(produtoAtualizado.getquantidadeEstoque());
            produtoService.salvarProduto(produto);
            return ResponseEntity.ok("Produto atualizado com sucesso");
        }else{
            StringBuilder erro = new StringBuilder("Erros ao inserir produto:");
            for (FieldError error : resultado.getFieldErrors()) {
                erro.append("\n- ").append(error.getDefaultMessage()).append(";");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro.toString());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erros ao inserir produto: "+resultado.getAllErrors());
        }
    }
    // Nome e quantidadeEstoque não podem ser nulos
    // alterar parcialmente
    // @PatchMapping -> verificar se no corpo da requisição o atributo está presente ou não
    // passo no body somente aquilo que quero alterar
    // pelo map conseguimos verificar se o atributo está presente ou não

    @PatchMapping("/atualizarParcial/{id}")
    public ResponseEntity<?> atualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        try {
            Produto produto = produtoService.buscarProdutoPorId(id);
            // atualiza apenas aqueles que estão presentes no corpo da requisição
            if (updates.containsKey("nome")) {
                produto.setNome((String) updates.get("nome"));
            }
            if (updates.containsKey("preco")) {
                String preco=updates.get("preco").toString();
                produto.setPreco(Double.parseDouble(preco));
            }
            if (updates.containsKey("descricao")) {
                produto.setDescricao((String) updates.get("descricao"));
            }
            if (updates.containsKey("quantidadeEstoque")) {
                produto.setquantidadeEstoque((int) updates.get("quantidadeEstoque"));
            }
            //Validar dados
            DataBinder binder = new DataBinder(produto);//Vincular o DataBinder ao produto
            binder.setValidator(validator);//Configura o validator no DataBinder
            binder.validate();//Executa o validador no objeto vinculado
            BindingResult resultado = binder.getBindingResult();
            if (resultado.hasErrors()) {
                Map erros = validarProduto(resultado);
                return ResponseEntity.badRequest().body(erros);
            }
            Produto produtoSalvo = produtoService.salvarProduto(produto);
            return ResponseEntity.ok(produtoSalvo);
        }catch (RuntimeException re){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(re.getMessage());
        }
    }

    @GetMapping("/buscarPorId/{id}")
    @Operation(summary = "Busca um produto por ID", description = "Retorna um produto pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",content = @Content),
    })
    public ResponseEntity<?> buscarPorId(@Parameter(description = "Id do produto a ser buscado") @PathVariable Long id) {
        try {
            Produto produto = produtoService.buscarProdutoPorId(id);
            return ResponseEntity.ok(produto);
        }catch (RuntimeException re){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
        }
    }

    @GetMapping("/buscarPorNome")
    @Operation(summary = "Busca um produto por nome", description = "Retorna um produto pelo seu nome")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",content = @Content),
    })
    public ResponseEntity<?> buscarProdutosPorNome(@Parameter(description = "Nome do produto a ser buscado.")  @RequestParam String nome) {
        List <Produto> listarProdutos = produtoService.buscarProdutosPorNome(nome);
        if(!listarProdutos.isEmpty()){
            return ResponseEntity.ok(listarProdutos);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum resultado encontrado");
        }
    }

    @GetMapping("/contarPorQuantidade/{quant}")
    public ResponseEntity<?> contarProdutosPorQuantidade(@PathVariable int quant) {
        int quantidade = produtoService.contarQuantidadeEstoqueMenorIgual(quant);
        if(quantidade>0){
            return ResponseEntity.status(HttpStatus.OK).body("Existem "+quantidade+" de produtos com estoque "+quant);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum resultado encontrado");
        }
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException re) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(re.getMessage());
    }
    public Map<String, String> validarProduto(BindingResult resultado) {
        Map<String, String> erros = new HashMap<>();
        for(FieldError error : resultado.getFieldErrors()){
            erros.put(error.getField(), error.getDefaultMessage());
        }
        return erros;
    }
}
