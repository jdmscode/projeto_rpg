package com.projeto.rpg.model.personagens;

import com.projeto.rpg.interfaces.*;
import com.projeto.rpg.model.inventario.Inventario;
import com.projeto.rpg.model.util.Dado;

public abstract class Personagem implements Atacavel, Cloneable {

    private String nome;
    private int pontosVida;
    private int vidaMaxima;
    private int ataque;
    private int defesa;
    private int nivel;
    private Inventario inventario;

    protected Personagem(String nome, int hp, int ataque, int defesa, int nivel) {
        setNome(nome);
        this.vidaMaxima = hp;
        setPontosVida(hp);
        setAtaque(ataque);
        setDefesa(defesa);
        setNivel(nivel);
        this.inventario = new Inventario();
    }


    protected Personagem(Personagem outro) {
        this.nome = outro.nome;
        this.pontosVida = outro.pontosVida;
        this.vidaMaxima = outro.vidaMaxima;
        this.ataque = outro.ataque;
        this.defesa = outro.defesa;
        this.nivel = outro.nivel;
        this.inventario = outro.inventario.clone();
    }

    public String getNome() { return nome; }
    public final void setNome(String nome) {
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("Nome inválido");
        this.nome = nome;
    }

    public int getPontosVida() { return pontosVida; }
    public final void setPontosVida(int pontosVida) {
        if (pontosVida < 0) pontosVida = 0;
        if (pontosVida > this.vidaMaxima) pontosVida = this.vidaMaxima;
        this.pontosVida = pontosVida;
    }

    public int getVidaMaxima() { return vidaMaxima; }

    public int getAtaque() { return ataque; }
    public final void setAtaque(int ataque) {
        if (ataque < 0) throw new IllegalArgumentException("Ataque não pode ser negativo");
        this.ataque = ataque;
    }

    public int getDefesa() { return defesa; }
    public final void setDefesa(int defesa) {
        if (defesa < 0) throw new IllegalArgumentException("Defesa não pode ser negativa");
        this.defesa = defesa;
    }

    public int getNivel() { return nivel; }
    public final void setNivel(int nivel) {
        if (nivel <= 0) throw new IllegalArgumentException("Nível deve ser >= 1");
        this.nivel = nivel;
    }

    public Inventario getInventario() { return inventario; }
    public void setInventario(Inventario inv) {
        if (inv == null) throw new IllegalArgumentException("Inventário não pode ser null");
        this.inventario = inv;
    }

    @Override
    public void receberDano(int dano) {
        if (dano < 0) throw new IllegalArgumentException("Dano negativo");
        setPontosVida(this.pontosVida - dano);
    }

    @Override
    public boolean estaMorto() {
        return this.pontosVida <= 0;
    }

    public int calcularAtaqueEfetivo() {
        return this.ataque + Dado.rolar(6);
    }

    protected int aplicarEfeitosDeAtaque(int danoBase) {
        return danoBase;
    }

    public boolean temEfeitoDeAtaque() {
        return false;
    }

    public int atacar(Personagem alvo) {
        int ataqueTotal = calcularAtaqueEfetivo();
        int danoSemCritico = Math.max(0, ataqueTotal - alvo.getDefesa());
        int danoFinal = aplicarEfeitosDeAtaque(danoSemCritico);
        alvo.receberDano(danoFinal);
        return danoFinal;
    }

    @Override
    public String toString() {
        return String.format("%s (HP:%d/%d Atq:%d Def:%d Nv:%d)",
                nome, pontosVida, vidaMaxima, ataque, defesa, nivel);
    }

    @Override
    public abstract Personagem clone();
}
