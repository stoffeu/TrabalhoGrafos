package service;

import model.Capital;
import model.Grafo;
import java.util.*;

public class CapitalService {
  private Grafo grafo;
  private Map<String, Capital> capitaisPorNome;

  public CapitalService(Grafo grafo) {
    this.grafo = grafo;
    this.capitaisPorNome = new HashMap<>();
    inicializarMapeamento();
  }

  private void inicializarMapeamento() {
    for (Capital capital : grafo.getCapitais()) {

      capitaisPorNome.put(capital.getNome().toLowerCase(), capital);

      capitaisPorNome.put(capital.getEstado().toLowerCase(), capital);

      String nomeSemAcento = removerAcentos(capital.getNome().toLowerCase());
      capitaisPorNome.put(nomeSemAcento, capital);
    }
  }

  private String removerAcentos(String str) {
    return str.replaceAll("[áàâãä]", "a")
        .replaceAll("[éèêë]", "e")
        .replaceAll("[íìîï]", "i")
        .replaceAll("[óòôõö]", "o")
        .replaceAll("[úùûü]", "u")
        .replaceAll("[ç]", "c");
  }

  public Capital buscarCapital(String nome) {
    nome = nome.trim().toLowerCase();

    Capital capital = capitaisPorNome.get(nome);
    if (capital != null)
      return capital;

    String nomeSemAcento = removerAcentos(nome);
    capital = capitaisPorNome.get(nomeSemAcento);
    if (capital != null)
      return capital;

    for (Capital c : capitaisPorNome.values()) {
      if (removerAcentos(c.getNome().toLowerCase()).contains(nomeSemAcento) ||
          c.getEstado().toLowerCase().contains(nome)) {
        return c;
      }
    }

    return null;
  }

  public void exibirCapitaisDisponiveis() {
    List<String> nomes = new ArrayList<>(capitaisPorNome.keySet());
    Collections.sort(nomes);

    int colunas = 3;
    int contador = 0;

    for (String nome : nomes) {
      Capital capital = capitaisPorNome.get(nome);
      System.out.printf("%-25s", capital.getNome() + " (" + capital.getEstado() + ")");
      contador++;

      if (contador % colunas == 0) {
        System.out.println();
      }
    }

    if (contador % colunas != 0) {
      System.out.println();
    }
  }

  public void exibirTodasConexoes() {
    System.out.println("\n=== Conexões Disponíveis entre Capitais ===");
    Set<String> conexoesExibidas = new HashSet<>();

    for (Capital origem : grafo.getCapitais()) {
      Map<Capital, Integer> vizinhos = grafo.getVizinhos(origem);

      for (Map.Entry<Capital, Integer> entrada : vizinhos.entrySet()) {
        Capital destino = entrada.getKey();
        int distancia = entrada.getValue();

        // Criar um identificador único para a conexão (ordem alfabética)
        String conexaoId;
        if (origem.getNome().compareTo(destino.getNome()) < 0) {
          conexaoId = origem.getNome() + "-" + destino.getNome();
        } else {
          conexaoId = destino.getNome() + "-" + origem.getNome();
        }

        // Exibir apenas se não foi mostrada antes
        if (!conexoesExibidas.contains(conexaoId)) {
          System.out.printf("%-25s <--> %-25s %5d km\n",
              origem.getNome() + " (" + origem.getEstado() + ")",
              destino.getNome() + " (" + destino.getEstado() + ")",
              distancia);
          conexoesExibidas.add(conexaoId);
        }
      }
    }
  }
}