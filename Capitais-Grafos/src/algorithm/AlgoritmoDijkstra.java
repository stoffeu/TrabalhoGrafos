package algorithm;

import model.Capital;
import model.Grafo;
import java.util.*;

public class AlgoritmoDijkstra {
    private Grafo grafo;
    
    public AlgoritmoDijkstra(Grafo grafo) {
        this.grafo = grafo;
    }
    
    public Resultado calcularMenorCaminho(Capital origem, Capital destino) {
        Map<Capital, Integer> distancias = new HashMap<>();
        Map<Capital, Capital> predecessores = new HashMap<>();
        PriorityQueue<Capital> filaPrioridade = new PriorityQueue<>(
            Comparator.comparingInt(distancias::get)
        );
        
        
        for (Capital capital : grafo.getCapitais()) {
            distancias.put(capital, Integer.MAX_VALUE);
        }
        distancias.put(origem, 0);
        filaPrioridade.add(origem);
        
       
        while (!filaPrioridade.isEmpty()) {
            Capital atual = filaPrioridade.poll();
            
            if (atual.equals(destino)) break;
            
            for (Map.Entry<Capital, Integer> vizinho : grafo.getVizinhos(atual).entrySet()) {
                Capital adjacente = vizinho.getKey();
                int distancia = vizinho.getValue();
                int distanciaTotal = distancias.get(atual) + distancia;
                
                if (distanciaTotal < distancias.get(adjacente)) {
                    distancias.put(adjacente, distanciaTotal);
                    predecessores.put(adjacente, atual);
                    filaPrioridade.add(adjacente);
                }
            }
        }
        
        
        List<Capital> caminho = new ArrayList<>();
        Capital passo = destino;
        
        if (predecessores.get(passo) == null && !passo.equals(origem)) {
            return new Resultado(Collections.emptyList(), Integer.MAX_VALUE);
        }
        
        while (passo != null) {
            caminho.add(passo);
            passo = predecessores.get(passo);
        }
        Collections.reverse(caminho);
        
        return new Resultado(caminho, distancias.get(destino));
    }
    
    public static class Resultado {
        private List<Capital> caminho;
        private int distanciaTotal;
        
        public Resultado(List<Capital> caminho, int distanciaTotal) {
            this.caminho = caminho;
            this.distanciaTotal = distanciaTotal;
        }
        
        public List<Capital> getCaminho() {
            return caminho;
        }
        
        public int getDistanciaTotal() {
            return distanciaTotal;
        }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Caminho: ");
            for (int i = 0; i < caminho.size(); i++) {
                if (i > 0) sb.append(" -> ");
                sb.append(caminho.get(i).getNome());
            }
            sb.append("\nDistância total: ").append(distanciaTotal).append(" km");
            return sb.toString();
        }
    }
}