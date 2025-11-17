package com.projeto.rpg.interfaces;

import com.projeto.rpg.model.personagens.Personagem;

public interface Atacavel {
    String getNome();
    int getPontosVida();
    int getVidaMaxima();
    void setPontosVida(int pontosVida);
    boolean estaMorto();
    void receberDano(int dano);
    int atacar(Personagem alvo);
}
