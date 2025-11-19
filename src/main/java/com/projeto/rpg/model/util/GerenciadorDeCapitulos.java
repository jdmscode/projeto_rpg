package com.projeto.rpg.model.util;

import com.projeto.rpg.interfaces.IO;
import com.projeto.rpg.model.personagens.Inimigo;
import com.projeto.rpg.model.inventario.Item;
import com.projeto.rpg.model.inventario.Efeito;
import java.io.IOException;

public class GerenciadorDeCapitulos {

    private final GerenciadorDeSalvamento salvamento;
    private final IO io;

    public GerenciadorDeCapitulos(IO io, GerenciadorDeSalvamento salvamento) {
        this.io = io;
        this.salvamento = salvamento;
    }

    public boolean executarCapitulo(EstadoDoJogo estado, GerenciadorDeCombate combate) throws IOException {
       if (estado.isFugiu()) {
            estado.setFugiu(false);
            estado.setSubEtapa(0);
            return false;
        }
        return switch (estado.getCapituloAtual()) {
            case 1 -> capitulo1(estado, combate);
            case 2 -> capitulo2(estado, combate);
            case 3 -> capitulo3(estado, combate);
            default -> {
                io.println("Você já completou toda a história!");
                yield false;
            }
        };
    }

    private boolean capitulo1(EstadoDoJogo estado, GerenciadorDeCombate combate) throws IOException {
        if (estado.getSubEtapa() == 0) estado.setSubEtapa(1);

        if (estado.getSubEtapa() == 1) {
            io.println("\nA floresta enevoada envolve você. Sons estranhos ecoam entre as árvores.");
            io.println("Você vê três caminhos:");
            io.println("1) Um caminho estreito, coberto por raízes e sombras.");
            io.println("2) Um portão de ferro enferrujado com uma alavanca ao lado.");
            io.println("3) Um tronco caído que parece formar uma ponte sobre um riacho.");

            io.print("Escolha seu caminho (1/2/3): ");
            int escolha = lerIntEntre(1, 3);

            switch (escolha) {
                case 1 -> explorarCaminhoEstreito(estado);
                case 2 -> explorarPortao(estado);
                case 3 -> explorarTronco(estado);
                default -> io.println("Você hesita e escolhe o caminho estreito por instinto.");
            }

            Inimigo inimigo = criarInimigoPorEscolha(escolha);
            estado.setInimigoAtual(inimigo);

            narrativaAparicaoInimigo(inimigo);

            pausar("\nPressione Enter para continuar");
            estado.setSubEtapa(2);
        }

        return iniciarCombateSeSubEtapa2(estado, combate);
    }

    private boolean capitulo2(EstadoDoJogo estado, GerenciadorDeCombate combate) throws IOException {
        if (estado.getSubEtapa() == 0) estado.setSubEtapa(1);

        if (estado.getSubEtapa() == 1) {
            io.println("\nVocê chega a ruínas antigas, cobertas de hera.");
            io.println("Entre as pedras, há três opções de exploração:");
            io.println("1) Torre desmoronada");
            io.println("2) Pátio aberto");
            io.println("3) Poço sombrio");

            io.print("Escolha: ");
            int escolha = lerIntEntre(1, 3);

            switch (escolha) {
                case 1 -> io.println("Subindo a torre, você escuta sons de morcegos e rachaduras no chão...");
                case 2 -> io.println("O pátio está silencioso, mas uma sombra se move rapidamente...");
                case 3 -> {
                    io.println("Ao investigar o poço, algo se move na água...");
                    if (Dado.rolar(100) < 40) danoJogador(estado, 4);
                }
            }

            Inimigo inimigo = new Inimigo("Espírito Ancestral", 100, 10, 4, 3);
            estado.setInimigoAtual(inimigo);
            narrativaAparicaoInimigo(inimigo);

            pausar("\nPressione Enter para continuar para o combate...");
            estado.setSubEtapa(2);
        }

        return iniciarCombateSeSubEtapa2(estado, combate);
    }

    private boolean capitulo3(EstadoDoJogo estado, GerenciadorDeCombate combate) throws IOException {
        if (estado.getSubEtapa() == 0) estado.setSubEtapa(1);

        if (estado.getSubEtapa() == 1) {
            io.println("\nVocê chega ao templo corrompido, envolto em chamas negras.");
            io.println("Há duas opções para entrar:");
            io.println("1) Forçar a pesada porta de pedra");
            io.println("2) Procurar uma entrada lateral secreta");

            io.print("Escolha: ");
            int escolha = lerIntEntre(1, 2);

            if (escolha == 1) {
                io.println("Com esforço, você abre a porta. Uma presença maligna observa...");
            } else {
                io.println("Você encontra a passagem lateral, mas sente algo se mover nas sombras...");
                if (Dado.rolar(100) < 50) danoJogador(estado, 6);
            }

            Inimigo inimigo = new Inimigo("Senhor das Sombras", 120, 15, 6, 4);
            estado.setInimigoAtual(inimigo);
            narrativaAparicaoInimigo(inimigo);

            pausar("\nPressione Enter para continuar para o combate...");
            estado.setSubEtapa(2);
        }

        if (estado.getSubEtapa() == 2) {
            estado.setSubEtapa(3);
            combate.setEstado(estado);

            io.println("\nA batalha final começa! O ar vibra com energia maligna...");
            pausar("\nPressione Enter para iniciar a batalha final...");
            combate.iniciarCombate(estado.getInimigoAtual(), salvamento, estado);

            if (estado.getJogador().estaMorto()) return false;

            io.println("\nO Senhor das Sombras é derrotado! A escuridão desaparece lentamente.");
            estado.setCapituloAtual(1);
            estado.setSubEtapa(0);
            estado.setInimigoAtual(null);
            return true;
        }

        return true;
    }

    private void explorarCaminhoEstreito(EstadoDoJogo estado) {
        io.println("\nVocê segue pelo caminho estreito...");
        if (Dado.rolar(100) < 30) {
            io.println("Uma armadilha de raízes quase o faz cair!");
            danoJogador(estado, 5);
        }
    }

    private void explorarPortao(EstadoDoJogo estado) throws IOException {
        io.println("\nO portão range. A alavanca parece antiga.");
        io.println("1) Puxar a alavanca");
        io.println("2) Ignorar e seguir");
        int acao = lerIntEntre(1, 2);

        if (acao == 1) {
            io.println("Um compartimento secreto se abre revelando uma poção grande!\nVocê pega o item... \nAo tentar pegar a poção, surge um inimigo de forma repentina!");
            estado.getJogador().getInventario().adicionar(
                    new Item("Poção Grande", "Restaura muita vida", Efeito.CURA, 15, 1)
            );
            pausar("\nPressione Enter para continuar...");
        } else {
            io.println("Você decide não mexer na alavanca.");
        }
    }

    private void explorarTronco(EstadoDoJogo estado) throws IOException {
        io.println("\nO tronco parece instável...");
        if (Dado.rolar(100) < 50) {
            danoJogador(estado, 3);
        } else {
            io.println("Você atravessa o tronco com cuidado e encontra uma poção pequena");
            estado.getJogador().getInventario().adicionar(
                    new Item("Poção pequena", "Restaura um pouco de vida", Efeito.CURA, 8, 1)
            );
            pausar("\nPressione Enter para continuar...");
        }
    }

    private Inimigo criarInimigoPorEscolha(int escolha) {
        return switch (escolha) {
            case 1 -> new Inimigo("Serpente do Lago", 100, 13, 3, 1);
            case 2 -> new Inimigo("Bandido", 100, 14, 4, 2);
            case 3 -> new Inimigo("Goblin Selvagem", 80, 12, 2, 1);
            default -> new Inimigo("Serpente do Lago", 100, 13, 3, 1);
        };
    }


    private void narrativaAparicaoInimigo(Inimigo inimigo) {
        io.println("\nO ar se torna pesado. Um movimento sutil entre as sombras chama sua atenção...");
        int suspense = Dado.rolar(100);

        switch (inimigo.getNome()) {
            case "Serpente do Lago" -> io.println(suspense < 50 ?
                    "Você sente a água se agitar silenciosamente, algo rasteja sob a superfície..." :
                    "Um frio percorre sua espinha. Reflexos ondulam na água, mas você não vê nada...");
            case "Bandido" -> io.println(suspense < 50 ?
                    "Passos rápidos ecoam entre as árvores. Alguém observa seus movimentos..." :
                    "Um farfalhar de folhas denuncia que você não está sozinho...");
            case "Goblin Selvagem" -> io.println(suspense < 50 ?
                    "Um odor fétido invade suas narinas. Algo pequeno e ágil se aproxima sorrateiro..." :
                    "Um brilho fugaz entre as sombras indica presença de algo hostil...");
            case "Espírito Ancestral" -> io.println(suspense < 50 ?
                    "Uma névoa branca envolve a área e você ouve sussurros distantes..." :
                    "O vento carrega vozes antigas que parecem lamentar em sua direção...");
            case "Senhor das Sombras" -> io.println(suspense < 50 ?
                    "Chamas negras dançam nas paredes do templo. Uma presença imensa observa cada movimento seu..." :
                    "O chão treme levemente e sombras alongam-se de forma antinatural...");
            default -> io.println("Algo se move nas sombras, você sente perigo iminente...");
        }

        io.println("De repente, a ameaça se revela! Prepare-se para o combate!\n");
    }

    private boolean iniciarCombateSeSubEtapa2(EstadoDoJogo estado, GerenciadorDeCombate combate) throws IOException {
        if (estado.getSubEtapa() == 2) {
            estado.setSubEtapa(3);
            combate.setEstado(estado);

            pausar("\nPressione Enter para iniciar o combate...");
            combate.iniciarCombate(estado.getInimigoAtual(), salvamento, estado);

            if (estado.getJogador().estaMorto()) return false;
            estado.setSubEtapa(4);
            return true;
        }
        return true;
    }

    private void danoJogador(EstadoDoJogo estado, int dano) {
        estado.getJogador().setPontosVida(Math.max(0, estado.getJogador().getPontosVida() - dano));
        io.println("Você recebe " + dano + " de dano. HP atual: " + estado.getJogador().getPontosVida());
    }

    private int lerIntEntre(int min, int max) throws IOException {
        while (true) {
            try {
                int v = Integer.parseInt(io.lerLinha().trim());
                if (v >= min && v <= max) return v;
            } catch (Exception ignored) {}
            io.println("Opção inválida. Tente novamente: ");
        }
    }

    private void pausar(String msg) throws IOException {
        io.println(msg);
        io.lerLinha();
    }
}
