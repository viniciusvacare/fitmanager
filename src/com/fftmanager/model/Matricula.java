package com.fftmanager.model;

import java.time.LocalDate;

public class Matricula {
	private int id;
    private int alunoId;
    private int planoId;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private boolean ativo;
    
    public Matricula() {}

    // Constructor completo
	public Matricula(int id, int alunoId, int planoId, LocalDate dataInicio, LocalDate dataFim, boolean ativo) {
		this.id = id;
		this.alunoId = alunoId;
		this.planoId = planoId;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.ativo = ativo;
	}

	// Constructor sem o id
	public Matricula(int alunoId, int planoId, LocalDate dataInicio, LocalDate dataFim) {
		this.alunoId = alunoId;
		this.planoId = planoId;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.ativo = true;
	}

	// Getters & Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAlunoId() {
		return alunoId;
	}

	public void setAlunoId(int alunoId) {
		this.alunoId = alunoId;
	}

	public int getPlanoId() {
		return planoId;
	}

	public void setPlanoId(int planoId) {
		this.planoId = planoId;
	}

	public LocalDate getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(LocalDate dataInicio) {
		this.dataInicio = dataInicio;
	}

	public LocalDate getDataFim() {
		return dataFim;
	}

	public void setDataFim(LocalDate dataFim) {
		this.dataFim = dataFim;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	@Override
	public String toString() {
		return "Matricula [id=" + id + ", alunoId=" + alunoId + ", planoId=" + planoId + ", dataInicio=" + dataInicio
				+ ", dataFim=" + dataFim + ", ativo=" + ativo + "]";
	}
    
    
}
