package br.com.trabalhobd2.algoritmos;

import br.com.trabalhobd2.app.Application;
import br.com.trabalhobd2.daos.CodigoNCMDAO;
import br.com.trabalhobd2.daos.ProdutoDAO;
import br.com.trabalhobd2.entidades.*;

import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HashJoin {
  List<Produto> produtos = null;
  List<CodigoNCM> codigoNCMs = null;
  List<ProdutoNCM> resultado = null;

  public void iniciar() {
    hashJoin();
    ListarTabela listarTabela = new ListarTabela(resultado);
    listarTabela.pesquisar();
  }

  public void hashJoin() {
    long tempoInicial = System.currentTimeMillis();
    ProdutoDAO produtoDAO = new ProdutoDAO();
    CodigoNCMDAO fabricanteDAO = new CodigoNCMDAO();
    resultado = new ArrayList<>();
    Map<String, CodigoNCM> map = new HashMap<>();

    try {
      produtos = produtoDAO.getProdutos();
      codigoNCMs = fabricanteDAO.getCodigoNCM();
      for (CodigoNCM codigoNCM : codigoNCMs) {
        map.put(codigoNCM.getCodNCM(), codigoNCM);
      }

      for (Produto produto : produtos) {
        CodigoNCM fabricante = map.get(produto.getCodNCM());
        if (fabricante != null) {
          ProdutoNCM produtoFabricante = new ProdutoNCM(produto, fabricante);
          resultado.add(produtoFabricante);
        }
      }
      long tempo = System.currentTimeMillis() - tempoInicial;
      ExibirInformacoes exibirInformacoes = new ExibirInformacoes("Hash Join", produtos.size(), codigoNCMs.size(), resultado.size(), tempo);
      exibirInformacoes.setMengasem();
    } catch (PropertyVetoException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
}