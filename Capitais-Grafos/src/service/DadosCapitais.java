package service;

import model.Capital;
import model.Grafo;
import java.util.*;

public class DadosCapitais {
  public static List<Capital> getTodasCapitais() {
    return Arrays.asList(
        new Capital("Aracaju", "SE"),
        new Capital("Belo Horizonte", "MG"),
        new Capital("Belém", "PA"),
        new Capital("Boa Vista", "RR"),
        new Capital("Brasília", "DF"),
        new Capital("Campo Grande", "MS"),
        new Capital("Cuiabá", "MT"),
        new Capital("Curitiba", "PR"),
        new Capital("Florianópolis", "SC"),
        new Capital("Fortaleza", "CE"),
        new Capital("Goiânia", "GO"),
        new Capital("João Pessoa", "PB"),
        new Capital("Macapá", "AP"),
        new Capital("Maceió", "AL"),
        new Capital("Manaus", "AM"),
        new Capital("Natal", "RN"),
        new Capital("Palmas", "TO"),
        new Capital("Porto Alegre", "RS"),
        new Capital("Porto Velho", "RO"),
        new Capital("Recife", "PE"),
        new Capital("Rio de Janeiro", "RJ"),
        new Capital("Rio Branco", "AC"),
        new Capital("Salvador", "BA"),
        new Capital("São Luís", "MA"),
        new Capital("São Paulo", "SP"),
        new Capital("Teresina", "PI"),
        new Capital("Vitória", "ES"));
  }

  public static void inicializarConexoes(Grafo grafo) {
    Map<String, Capital> mapaCapitais = new HashMap<>();

    // Mapeia capitais por nome e sigla
    for (Capital capital : getTodasCapitais()) {
      mapaCapitais.put(capital.getNome().toLowerCase(), capital);
      mapaCapitais.put(capital.getEstado().toLowerCase(), capital);
      grafo.adicionarCapital(capital);
    }

    // Conecta todas as regiões
    conectarSudeste(grafo, mapaCapitais);
    conectarSul(grafo, mapaCapitais);
    conectarNordeste(grafo, mapaCapitais);
    conectarCentroOeste(grafo, mapaCapitais);
    conectarNorte(grafo, mapaCapitais);
    conectarInterRegioes(grafo, mapaCapitais);
  }

  private static void conectarSudeste(Grafo grafo, Map<String, Capital> mapaCapitais) {
    // Sudeste
    grafo.adicionarConexao(mapaCapitais.get("são paulo"), mapaCapitais.get("rio de janeiro"), 429);
    grafo.adicionarConexao(mapaCapitais.get("são paulo"), mapaCapitais.get("belo horizonte"), 586);
    grafo.adicionarConexao(mapaCapitais.get("são paulo"), mapaCapitais.get("vitória"), 882);
    grafo.adicionarConexao(mapaCapitais.get("rio de janeiro"), mapaCapitais.get("belo horizonte"), 435);
    grafo.adicionarConexao(mapaCapitais.get("rio de janeiro"), mapaCapitais.get("vitória"), 521);
    grafo.adicionarConexao(mapaCapitais.get("belo horizonte"), mapaCapitais.get("vitória"), 524);
  }

  private static void conectarSul(Grafo grafo, Map<String, Capital> mapaCapitais) {
    // Sul
    grafo.adicionarConexao(mapaCapitais.get("são paulo"), mapaCapitais.get("curitiba"), 408);
    grafo.adicionarConexao(mapaCapitais.get("curitiba"), mapaCapitais.get("florianópolis"), 300);
    grafo.adicionarConexao(mapaCapitais.get("florianópolis"), mapaCapitais.get("porto alegre"), 476);
  }

  private static void conectarNordeste(Grafo grafo, Map<String, Capital> mapaCapitais) {
    // Nordeste
    grafo.adicionarConexao(mapaCapitais.get("salvador"), mapaCapitais.get("aracaju"), 356);
    grafo.adicionarConexao(mapaCapitais.get("aracaju"), mapaCapitais.get("maceió"), 294);
    grafo.adicionarConexao(mapaCapitais.get("maceió"), mapaCapitais.get("recife"), 285);
    grafo.adicionarConexao(mapaCapitais.get("recife"), mapaCapitais.get("joão pessoa"), 120);
    grafo.adicionarConexao(mapaCapitais.get("joão pessoa"), mapaCapitais.get("natal"), 185);
    grafo.adicionarConexao(mapaCapitais.get("natal"), mapaCapitais.get("fortaleza"), 537);
    grafo.adicionarConexao(mapaCapitais.get("fortaleza"), mapaCapitais.get("teresina"), 634);
    grafo.adicionarConexao(mapaCapitais.get("teresina"), mapaCapitais.get("são luís"), 446);
  }

  private static void conectarCentroOeste(Grafo grafo, Map<String, Capital> mapaCapitais) {
    // Centro-Oeste
    grafo.adicionarConexao(mapaCapitais.get("brasília"), mapaCapitais.get("goiânia"), 209);
    grafo.adicionarConexao(mapaCapitais.get("brasília"), mapaCapitais.get("cuiabá"), 1133);
    grafo.adicionarConexao(mapaCapitais.get("brasília"), mapaCapitais.get("campo grande"), 1134);
    grafo.adicionarConexao(mapaCapitais.get("goiânia"), mapaCapitais.get("cuiabá"), 934);
    grafo.adicionarConexao(mapaCapitais.get("cuiabá"), mapaCapitais.get("campo grande"), 694);
  }

  private static void conectarNorte(Grafo grafo, Map<String, Capital> mapaCapitais) {
    // Norte
    grafo.adicionarConexao(mapaCapitais.get("manaus"), mapaCapitais.get("boa vista"), 785);
    grafo.adicionarConexao(mapaCapitais.get("manaus"), mapaCapitais.get("porto velho"), 901);
    grafo.adicionarConexao(mapaCapitais.get("porto velho"), mapaCapitais.get("rio branco"), 544);
    grafo.adicionarConexao(mapaCapitais.get("manaus"), mapaCapitais.get("belém"), 3051);
    grafo.adicionarConexao(mapaCapitais.get("belém"), mapaCapitais.get("macapá"), 329);
    grafo.adicionarConexao(mapaCapitais.get("belém"), mapaCapitais.get("palmas"), 1283);
  }

  private static void conectarInterRegioes(Grafo grafo, Map<String, Capital> mapaCapitais) {
    // Conexões entre regiões
    grafo.adicionarConexao(mapaCapitais.get("belo horizonte"), mapaCapitais.get("brasília"), 716);
    grafo.adicionarConexao(mapaCapitais.get("belo horizonte"), mapaCapitais.get("salvador"), 1372);
    grafo.adicionarConexao(mapaCapitais.get("curitiba"), mapaCapitais.get("campo grande"), 991);
    grafo.adicionarConexao(mapaCapitais.get("salvador"), mapaCapitais.get("recife"), 839);
    grafo.adicionarConexao(mapaCapitais.get("palmas"), mapaCapitais.get("brasília"), 973);
    grafo.adicionarConexao(mapaCapitais.get("palmas"), mapaCapitais.get("teresina"), 1401);
    grafo.adicionarConexao(mapaCapitais.get("goiânia"), mapaCapitais.get("palmas"), 874);
    grafo.adicionarConexao(mapaCapitais.get("são paulo"), mapaCapitais.get("brasília"), 1015);
  }

  public static Map<String, String> getSiglasParaNomes() {
    Map<String, String> siglas = new HashMap<>();
    for (Capital capital : getTodasCapitais()) {
      siglas.put(capital.getEstado(), capital.getNome());
    }
    return siglas;
  }
}