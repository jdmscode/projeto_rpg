package com.projeto.rpg;

import java.io.IOException;
import com.projeto.rpg.interfaces.IO;
import com.projeto.rpg.model.util.ConsoleIO;
import com.projeto.rpg.model.util.Dado;
import com.projeto.rpg.model.personagens.*;
import com.projeto.rpg.model.inventario.*;

public class Main {

    public static void main(String[] args) throws IOException {
        IO io = new ConsoleIO();

        configurarSeed(args, io);

        io.println("=== Bem-vindo ao RPG de Texto (POO/Java) ===\n");

        String nome = escolherNome(io);

        Personagem player = escolherClasse(nome, io);

        inicializarInventario(player);

        Jogo jogo = new Jogo(player, io);
        jogo.iniciar();
    }

    private static void configurarSeed(String[] args, IO io) {
        if (args.length >= 1) {
            try {
                long seed = Long.parseLong(args[0]);
                Dado.setSeed(seed);
                io.println("Usando seed: " + seed);
            } catch (NumberFormatException e) {
                io.println("Argumento de seed inválido. Ignorando.");
            }
        }
    }

    private static String escolherNome(IO io) throws IOException {
        io.print("Digite o nome do seu personagem: ");
        String nome = io.lerLinha().trim();
        if (nome.isEmpty()) nome = "Aventureiro";
        return nome;
    }

    private static Personagem escolherClasse(String nome, IO io) throws IOException {
        io.println("\nEscolha sua classe:");
        io.println("1) Guerreiro");
        io.println("2) Mago");
        io.println("3) Arqueiro");
        io.print("Digite o número da classe: ");
        String escolha = io.lerLinha().trim();

        return switch (escolha) {
            case "2" -> new Mago(nome, 100, 5, 6, 1, 5, 3);
            case "3" -> new Arqueiro(nome, 100, 6, 8, 1);
            default -> new Guerreiro(nome, 100, 7, 10, 1);
        };
    }

    private static void inicializarInventario(Personagem player) {
        Inventario inv = player.getInventario();
        inv.adicionar(new Item("Poção Pequena", "Cura 8 HP", Efeito.CURA, 8, 2));
        inv.adicionar(new Item("Bomba", "Causa dano", Efeito.DANO, 5, 2));
    }
}
