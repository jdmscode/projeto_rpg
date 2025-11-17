package com.projeto.rpg.model.util;

import com.projeto.rpg.model.personagens.Personagem;
import com.projeto.rpg.model.personagens.Inimigo;
import com.projeto.rpg.model.inventario.Item;
import com.projeto.rpg.model.inventario.Inventario;
import com.projeto.rpg.model.inventario.Efeito;
import com.projeto.rpg.interfaces.*;
import java.io.IOException;
import java.util.List;

public class GerenciadorDeCombate {

    private final IO io;
    private EstadoDoJogo estado;

    public GerenciadorDeCombate() {
        this(new ConsoleIO());
    }

    public GerenciadorDeCombate(IO io) {
        this.io = io;
    }

    public void setEstado(EstadoDoJogo estado) {
        this.estado = estado;
    }

    public void iniciarCombate(Inimigo inimigo, GerenciadorDeSalvamento salvamento, EstadoDoJogo estado) throws IOException {
        this.estado = estado;

        estado.setFugiu(false);
        estado.setInimigoAtual(inimigo);
        Personagem jogador = estado.getJogador();

        exibirInicioCombate(inimigo);

        while (combateAtivo(jogador, inimigo)) {
            exibirStatus(jogador, inimigo);

            String escolha = lerOpcao("1", "2", "3", "4");
            boolean turnoValido = executarAcaoDoJogador(escolha, jogador, inimigo, salvamento);
            if (!turnoValido) continue;

            pausar("\nPressione Enter para continuar...");

            if (estado.isFugiu() || inimigo.estaMorto() || jogador.estaMorto()) break;

            realizarTurnoDoInimigo(inimigo, jogador);
        }

        finalizarCombate(jogador, inimigo);
    }

    private boolean combateAtivo(Personagem jogador, Inimigo inimigo) {
        return !jogador.estaMorto() && !inimigo.estaMorto() && !estado.isFugiu();
    }

    private void exibirInicioCombate(Inimigo inimigo) {
        io.println("\n=== Um inimigo aparece! ===");
        io.println("Você se depara com " + inimigo.getNome() + "!");
        io.println("Prepare-se para o combate!\n");
    }

    private void exibirStatus(Personagem jogador, Inimigo inimigo) {
        io.println("\n--- Seu turno ---");
        io.printf("%s HP: %d/%d\n", jogador.getNome(), jogador.getPontosVida(), jogador.getVidaMaxima());
        io.printf("%s HP: %d/%d\n", inimigo.getNome(), inimigo.getPontosVida(), inimigo.getVidaMaxima());

        io.println("Escolha sua ação:");
        io.println("1) Atacar");
        io.println("2) Usar item");
        io.println("3) Fugir");
        io.println("4) Salvar checkpoint");
    }

    private boolean executarAcaoDoJogador(String acao, Personagem jogador, Inimigo inimigo, GerenciadorDeSalvamento salvamento) throws IOException {
        return switch (acao) {
            case "1" -> atacar(jogador, inimigo);
            case "2" -> usarItem(jogador, inimigo);
            case "3" -> tentarFugir();
            case "4" -> salvarCheckpoint(salvamento);
            default -> false;
        };
    }

    public void explorar(EstadoDoJogo estado, GerenciadorDeSalvamento salvamento) throws IOException {
        this.estado = estado;
        estado.setFugiu(false);

        io.println("\nVocê explora a região e encontra um inimigo!");
        Inimigo inimigo = new Inimigo("Goblin Selvagem", 50, 10, 2, 1);

        iniciarCombate(inimigo, salvamento, estado);
    }

    public void retomaCombate(EstadoDoJogo estado, GerenciadorDeSalvamento salvamento) throws IOException {
        this.estado = estado;
        estado.setFugiu(false);

        Inimigo inimigo = estado.getInimigoAtual();
        if (inimigo != null) {
            io.println("\nRetomando combate...");
            iniciarCombate(inimigo, salvamento, estado);
        } else {
            io.println("\nNão há combate para retomar.");
        }
    }


    private boolean atacar(Personagem jogador, Inimigo inimigo) {
        io.println("\nVocê avança para atacar...");
        int danoCausado = jogador.atacar(inimigo); // NÃO passa IO
        io.println(jogador.getNome() + " causou " + danoCausado + " de dano em " + inimigo.getNome() + "!");
        return true;
    }

    private boolean salvarCheckpoint(GerenciadorDeSalvamento salvamento) {
        salvamento.salvar(
                estado.getJogador(),
                estado.getCapituloAtual(),
                estado.isEmCapitulo(),
                estado.getSubEtapa(),
                estado.getInimigoAtual()
        );

        io.println("Checkpoint salvo com sucesso!");
        return false;
    }

    private void realizarTurnoDoInimigo(Inimigo inimigo, Personagem jogador) throws IOException {
        io.println("\nO inimigo se prepara para atacar!");
        int dano = inimigo.atacar(jogador); 
        io.println(inimigo.getNome() + " causou " + dano + " de dano em " + jogador.getNome() + "!");
        pausar("\nPressione Enter para próximo turno...");
    }

    private void finalizarCombate(Personagem jogador, Inimigo inimigo) throws IOException {
        if (estado.isFugiu()) {
            io.println("\nVocê escapou do combate.");
            return;
        }

        if (jogador.estaMorto()) {
            tratarDerrota(jogador);
            return;
        }

        tratarVitoria(jogador, inimigo);
    }

    private void tratarDerrota(Personagem jogador) {
        io.println("\nVocê caiu em batalha... mas desperta algum tempo depois.");
        int recuperado = jogador.getVidaMaxima() / 2;
        jogador.setPontosVida(recuperado);
        io.println("Você recupera " + recuperado + " PV e retorna ao menu principal.");
    }

    private void tratarVitoria(Personagem jogador, Inimigo inimigo) throws IOException {
        io.println("\nParabéns! " + inimigo.getNome() + " foi derrotado!");
        estado.setInimigoAtual(null);

        io.print("Deseja saquear o corpo? (s/n): ");
        String entrada = io.lerLinha().trim().toLowerCase();

        if (entrada.equals("s")) realizarSaque(jogador);
        else io.println("Você decide seguir em frente.");
    }

    private void realizarSaque(Personagem jogador) {
        io.println("\nVocê examina os pertences do inimigo...");
        int rolagem = Dado.rolar(100);
        io.println("Rolagem de saque: " + rolagem);

        if (rolagem <= 40) {
            io.println("Nada de útil encontrado.");
            return;
        }

        Item loot = gerarLoot(rolagem);
        jogador.getInventario().adicionar(loot);
        io.println("Você encontrou: " + loot.getNome() + "!");
    }

    private Item gerarLoot(int rolagem) {
        if (rolagem <= 70) return new Item("Poção Pequena", "Restaura vitalidade.", Efeito.CURA, 8, 1);
        if (rolagem <= 90) return new Item("Adaga Enferrujada", "Ainda causa ferimentos.", Efeito.DANO, 8, 1);
        return new Item("Poção Média", "Restaura mais vitalidade.", Efeito.CURA, 8, 1);
    }

    private boolean usarItem(Personagem jogador, Inimigo inimigo) throws IOException {
        Inventario inv = jogador.getInventario();

        if (inv.estaVazio()) {
            io.println("Inventário vazio.");
            return false;
        }

        while (true) {
            List<Item> itens = inv.listarOrdenado();
            exibirInventario(itens);

            int escolha = lerIndiceItem(itens.size());
            if (escolha == 0) {
                io.println("Ação cancelada.");
                return false;
            }
            if (escolha == -1) continue;

            Item item = itens.get(escolha - 1);
            inv.remover(item.getNome(), 1);

            String resultado = aplicarItem(item, jogador, inimigo);
            io.println(resultado);
            return true;
        }
    }

    private void exibirInventario(List<Item> itens) {
        io.println("\n--- Inventário ---");
        for (int i = 0; i < itens.size(); i++)
            io.println((i + 1) + ") " + itens.get(i));
        io.println("0) Cancelar");
    }

    private void pausar(String msg) throws IOException {
        io.println(msg);
        io.lerLinha();
    }

    private String lerOpcao(String... validas) throws IOException {
        while (true) {
            String entrada = io.lerLinha();
            if (entrada == null) return validas[0];

            entrada = entrada.trim();
            for (String v : validas) {
                if (v.equals(entrada)) return entrada;
            }

            io.println("Opção inválida.");
        }
    }

    private boolean tentarFugir() {
        boolean sucesso = Math.random() >= 0.5;
        if (sucesso) {
            io.println("Fuga bem-sucedida!");
            estado.setFugiu(true);
            estado.setInimigoAtual(null);
        } else {
            io.println("Fuga falhou!");
        }
        return true;
    }

    private int lerIndiceItem(int limite) throws IOException {
        try {
            int v = Integer.parseInt(io.lerLinha().trim());
            if (v >= 0 && v <= limite) return v;
        } catch (Exception ignored) {}
        io.println("Opção inválida.");
        return -1;
    }

    private String aplicarItem(Item item, Personagem jogador, Inimigo inimigo) {
        int valor = item.getValorDoEfeito();
        switch (item.getEfeito()) {
            case CURA -> {
                int vidaAtual = jogador.getPontosVida();
                int novaVida = Math.min(vidaAtual + valor, jogador.getVidaMaxima());
                jogador.setPontosVida(novaVida);
                return "Você usou " + item.getNome() + " e recuperou " + (novaVida - vidaAtual) + " de vida!";
            }
            case DANO -> {
                int vidaAtual = inimigo.getPontosVida();
                int novaVida = Math.max(vidaAtual - valor, 0);
                inimigo.setPontosVida(novaVida);
                return "Você usou " + item.getNome() + " e causou " + (vidaAtual - novaVida) + " de dano no inimigo!";
            }
            default -> {
                return "Efeito do item não implementado.";
            }
        }
    }
}
