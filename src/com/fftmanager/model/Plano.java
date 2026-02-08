package com.fftmanager.model;

import java.math.BigDecimal;

public class Plano {
	private int id;
    private String nome;
    private BigDecimal valor;
    private int duracaoMeses;
    private String descricao;
    
    // Construtor vazio
    public Plano() {}

    // Construtor completo
	public Plano(int id, String nome, BigDecimal valor, int duracaoMeses, String descricao) {
		this.id = id;
		this.nome = nome;
		this.valor = valor;
		this.duracaoMeses = duracaoMeses;
		this.descricao = descricao;
	}

	// Construtor sem o id
	public Plano(String nome, BigDecimal valor, int duracaoMeses, String descricao) {
		this.nome = nome;
		this.valor = valor;
		this.duracaoMeses = duracaoMeses;
		this.descricao = descricao;
	}

	// Gettes & Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public int getDuracaoMeses() {
		return duracaoMeses;
	}

	public void setDuracaoMeses(int duracaoMeses) {
		this.duracaoMeses = duracaoMeses;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return "Plano [id=" + id + ", nome=" + nome + ", valor=" + valor + ", duracaoMeses=" + duracaoMeses
				+ ", descricao=" + descricao + "]";
	}
    
}
