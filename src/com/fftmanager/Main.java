package com.fftmanager;

import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcao;
        
        do {
        	carregarMenu();
            opcao = scanner.nextInt();
            
            switch (opcao) {
                case 1:
                    System.out.println("\n>>> Gerenciar Alunos <<<\n");
                    break;
                case 2:
                    System.out.println("\n>>> Gerenciar Planos <<<\n");
                    break;
                case 3:
                    System.out.println("\n>>> Gerenciar Matrículas <<<\n");
                    break;
                case 4:
                    System.out.println("\n>>> Gerenciar Pagamentos <<<\n");
                    break;
                case 5:
                    System.out.println("\n>>> Relatórios <<<\n");
                    break;
                case 0:
                    System.out.println("\nEncerrando sistema...");
                    break;
                default:
                    System.out.println("\nOpção inválida! Tente novamente.\n");
            }
        } while (opcao != 0);
        
        scanner.close();
	}
	
	private static void carregarMenu() {
        System.out.println("=== SISTEMA DE GERENCIAMENTO DE ACADEMIA ===");
        System.out.println("1. Gerenciar Alunos");
        System.out.println("2. Gerenciar Planos");
        System.out.println("3. Gerenciar Matrículas");
        System.out.println("4. Gerenciar Pagamentos");
        System.out.println("5. Relatórios");
        System.out.println("0. Sair");
        System.out.print("\nEscolha uma opção: ");
	}
}
