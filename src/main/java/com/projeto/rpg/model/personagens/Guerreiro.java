package com.projeto.rpg.model.personagens;

import com.projeto.rpg.model.util.Dado;

public class Guerreiro extends Personagem {

    private final int chanceCritico; 
    public Guerreiro(String nome, int hp, int ataque, int defesa, int nivel) {
        this(nome, hp, ataque, defesa, nivel, 20);
    }

    public Guerreiro(String nome, int hp, int ataque, int defesa, int nivel, int chanceCritico) {
        super(nome, hp, ataque, defesa, nivel);
        if (chanceCritico < 0 || chanceCritico > 100)
            throw new IllegalArgumentException("Chance de crítico deve ser 0-100%");
        this.chanceCritico = chanceCritico;
    }

    public Guerreiro(Guerreiro outro) {
        super(outro);
        this.chanceCritico = outro.chanceCritico;
    }

    @Override
    protected int aplicarEfeitosDeAtaque(int danoBase) {
        // Rola um dado de 1 a 100 para chance de crítico
        int rolagem = Dado.rolar(100);
        System.out.println("Guerreiro rola dado de crítico: " + rolagem + " (precisa ≤ " + chanceCritico + ")");
        if (rolagem <= chanceCritico) {
            System.out.println("\nCRITICO! Dano aumentado!");
            return danoBase * 2;
        }
        return danoBase;
    }

    @Override
    public boolean temEfeitoDeAtaque() {
        return true;
    }

    @Override
    public Guerreiro clone() {
        return new Guerreiro(this);
    }
}
