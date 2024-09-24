function submitForm() {
    // Captura os valores dos campos do formulário
    const nome = document.getElementById("nome").value;
    const preco = document.getElementById("preco").value;
    const quantidade = document.getElementById("quantidadeEstoque").value;
    const descricao = document.getElementById("descricao").value;
  
    // Cria um objeto com os dados
    const formData = {
      "nome": nome,
      "preco": preco,
      "quantidadeEstoque": quantidade,
      "descricao": descricao
    };
  
    // Converte o objeto para JSON
    const jsonData = JSON.stringify(formData);
  
    // Envia os dados para a API (substitua a URL pela sua)
    fetch('http://localhost:8080/api/produtos/inserir', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: jsonData
    })
    .then(response => {
      if (response.ok) {
        return response.json();
      }
      throw new Error('Erro ao enviar dados para a API.');
    })
    .then(data => {
      console.log('Resposta da API:', data);
      // Faça algo com a resposta da API, se necessário
    })
    .catch(error => {
      console.error('Erro:', error);
    });
  }
  