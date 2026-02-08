package com.fftmanager.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Pagamento {
	private int id;
    private int matriculaId;
    private BigDecimal valor;
    private LocalDate dataVencimento;
    private LocalDate dataPagamento;
    private String status; // PENDENTE, PAGO, ATRASADO
    
    public Pagamento() {}

	public Pagamento(int id, int matriculaId, BigDecimal valor, LocalDate dataVencimento, LocalDate dataPagamento, String status) {
		this.id = id;
		this.matriculaId = matriculaId;
		this.valor = valor;
		this.dataVencimento = dataVencimento;
		this.dataPagamento = dataPagamento;
		this.status = status;
	}

	public Pagamento(int matriculaId, BigDecimal valor, LocalDate dataVencimento) {
		this.matriculaId = matriculaId;
		this.valor = valor;
		this.dataVencimento = dataVencimento;
		this.status = "PENDENTE";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMatriculaId() {
		return matriculaId;
	}

	public void setMatriculaId(int matriculaId) {
		this.matriculaId = matriculaId;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public LocalDate getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(LocalDate dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public LocalDate getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(LocalDate dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Pagamento [id=" + id + ", matriculaId=" + matriculaId + ", valor=" + valor + ", dataVencimento="
				+ dataVencimento + ", dataPagamento=" + dataPagamento + ", status=" + status + "]";
	}
    
}
