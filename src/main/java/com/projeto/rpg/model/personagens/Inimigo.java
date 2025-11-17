package com.projeto.rpg.model.personagens;

public class Inimigo extends Personagem {
    public Inimigo(String nome, int hp, int ataque, int defesa, int nivel) {
        super(nome, hp, ataque, defesa, nivel);
    }

    public Inimigo(Inimigo outro) { super(outro); }

    @Override
    public Inimigo clone() { return new Inimigo(this); }
}
