package model;

import java.util.*;

public class Grafo {
    private Map<Capital, Map<Capital, Integer>> adjacencias;
    private List<Capital> capitais;
    
    public Grafo() {
        adjacencias = new HashMap<>();
        capitais = new ArrayList<>();
    }
    
    public void adicionarCapital(Capital capital) {
        if (!adjacencias.containsKey(capital)) {
            adjacencias.put(capital, new HashMap<>());
            capitais.add(capital);
        }
    }
    
    public void adicionarConexao(Capital origem, Capital destino, int distancia) {
        adicionarCapital(origem);
        adicionarCapital(destino);
        adjacencias.get(origem).put(destino, distancia);
        adjacencias.get(destino).put(origem, distancia);
    }
    
    public Map<Capital, Integer> getVizinhos(Capital capital) {
        return adjacencias.getOrDefault(capital, new HashMap<>());
    }
    
    public List<Capital> getCapitais() {
        return new ArrayList<>(capitais);
    }
}