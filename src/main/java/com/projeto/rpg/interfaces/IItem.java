package com.projeto.rpg.interfaces;

import com.projeto.rpg.model.inventario.Efeito;

public interface IItem {
    String getNome();
    String getDescricao();
    Efeito getEfeito();
    int getValorDoEfeito();
    int getQuantidade();
}
