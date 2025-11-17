package com.projeto.rpg.model.inventario;

import java.util.*;

public class Inventario implements Cloneable {
    private final Map<String, Item> itens = new LinkedHashMap<>();

    private String chaveDe(Item item) {
        return item.getNome().toLowerCase() + "|" + item.getEfeito().name();
    }

    public void adicionar(Item item) {
        if (item == null) throw new IllegalArgumentException("item null");
        if (item.getQuantidade() <= 0) throw new IllegalArgumentException("quantidade deve ser > 0");
        String k = chaveDe(item);
        Item existente = itens.get(k);
        if (existente == null) {
            itens.put(k, item.clone()); 
        } else {
            existente.incrementar(item.getQuantidade());
        }
    }

    public boolean remover(String nome, int quantidade) {
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("nome inválido");
        if (quantidade <= 0) throw new IllegalArgumentException("quantidade inválida");
       
        String keyFound = null;
        for (String k : itens.keySet()) {
            if (k.startsWith(nome.toLowerCase() + "|")) {
                keyFound = k;
                break;
            }
        }
        if (keyFound == null) return false;
        Item item = itens.get(keyFound);
        if (item.getQuantidade() < quantidade) return false;
        item.decrementar(quantidade);
        if (item.getQuantidade() == 0) itens.remove(keyFound);
        return true;
    }

    public Optional<Item> pegarCopia(String nome, int quantidade) {
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("nome inválido");
        if (quantidade <= 0) throw new IllegalArgumentException("quantidade inválida");
        String keyFound = null;
        for (String k : itens.keySet()) {
            if (k.startsWith(nome.toLowerCase() + "|")) { keyFound = k; break; }
        }
        if (keyFound == null) return Optional.empty();
        Item it = itens.get(keyFound);
        if (it.getQuantidade() < quantidade) return Optional.empty();

        Item copia = it.clone();
        copia.setQuantidade(quantidade);
        return Optional.of(copia);
    }

    public List<Item> listarOrdenado() {
        List<Item> copia = new ArrayList<>();
        for (Item it : itens.values()) copia.add(it.clone());
        Collections.sort(copia);
        return copia;
    }

    public boolean contem(String nome) {
        if (nome == null) return false;
        for (String k : itens.keySet()) if (k.startsWith(nome.toLowerCase() + "|")) return true;
        return false;
    }

    public boolean estaVazio() {
        return itens.isEmpty();
    }

    @Override
    public Inventario clone() {
        Inventario novo = new Inventario();
        for (Item it : this.itens.values()) {
            novo.itens.put(novoKey(it), it.clone());
        }
        return novo;
    }

    
    private String novoKey(Item it) {
        return it.getNome().toLowerCase() + "|" + it.getEfeito().name();
    }

    @Override
    public String toString() {
        if (itens.isEmpty()) return "(vazio)";
        StringBuilder sb = new StringBuilder();
        for (Item it : itens.values()) sb.append(it).append("\n");
        return sb.toString();
    }
}
