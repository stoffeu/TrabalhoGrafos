package model;

import java.util.Objects;

public class Capital {
    private String nome;
    private String estado;
    
    public Capital(String nome, String estado) {
        this.nome = nome;
        this.estado = estado;
    }
    
    public String getNome() {
        return nome;
    }
    
    public String getEstado() {
        return estado;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Capital capital = (Capital) obj;
        return nome.equals(capital.nome) && estado.equals(capital.estado);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(nome, estado);
    }
    
    @Override
    public String toString() {
        return nome + " (" + estado + ")";
    }
}