package com.fftmanager.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fftmanager.dao.AlunoDAO;
import com.fftmanager.dao.MatriculaDAO;
import com.fftmanager.dao.PagamentoDAO;
import com.fftmanager.model.Aluno;
import com.fftmanager.model.Matricula;
import com.fftmanager.model.Pagamento;

public class PagamentoService {

	private static final BigDecimal JUROS_DIARIO = new BigDecimal("0.001"); // 0,1% ao dia

	private final PagamentoDAO pagamentoDAO = new PagamentoDAO();
	private final MatriculaDAO matriculaDAO = new MatriculaDAO();
	private final AlunoDAO alunoDAO = new AlunoDAO();

	public void verificarMensalidadesVencidas() throws SQLException {
		List<Pagamento> pendentes = pagamentoDAO.buscarPendentes();
		LocalDate hoje = LocalDate.now();

		for (Pagamento p : pendentes) {
			if (p.getDataVencimento() != null && p.getDataVencimento().isBefore(hoje)) {
				p.setStatus("ATRASADO");
				pagamentoDAO.atualizar(p);
			}
		}
	}

	public BigDecimal calcularMultaJuros(Pagamento pagamento, LocalDate dataPagamento) {
		if (pagamento.getDataVencimento() == null || dataPagamento == null) {
			return BigDecimal.ZERO;
		}
		if (!dataPagamento.isAfter(pagamento.getDataVencimento())) {
			return BigDecimal.ZERO;
		}
		long diasAtraso = ChronoUnit.DAYS.between(pagamento.getDataVencimento(), dataPagamento);
		if (diasAtraso <= 0) {
			return BigDecimal.ZERO;
		}
		BigDecimal valorBase = pagamento.getValor() != null ? pagamento.getValor() : BigDecimal.ZERO;
		BigDecimal juros = valorBase.multiply(JUROS_DIARIO).multiply(BigDecimal.valueOf(diasAtraso));
		return juros.setScale(2, RoundingMode.HALF_UP);
	}

	public boolean registrarPagamento(int pagamentoId, LocalDate dataPagamento) throws SQLException {
		Pagamento pagamento = pagamentoDAO.buscarPorId(pagamentoId);
		if (pagamento == null) {
			throw new IllegalArgumentException("Pagamento não encontrado.");
		}
		if ("PAGO".equals(pagamento.getStatus())) {
			throw new IllegalArgumentException("Pagamento já foi registrado.");
		}

		verificarMensalidadesVencidas();

		BigDecimal valorFinal = pagamento.getValor();
		if (pagamento.getDataVencimento() != null && dataPagamento.isAfter(pagamento.getDataVencimento())) {
			BigDecimal multa = calcularMultaJuros(pagamento, dataPagamento);
			valorFinal = pagamento.getValor().add(multa);
		}

		pagamento.setDataPagamento(dataPagamento);
		pagamento.setValor(valorFinal);
		pagamento.setStatus("PAGO");
		return pagamentoDAO.atualizar(pagamento);
	}

	public boolean registrarPagamento(int pagamentoId) throws SQLException {
		return registrarPagamento(pagamentoId, LocalDate.now());
	}

	public List<Pagamento> listarPendentes() throws SQLException {
		verificarMensalidadesVencidas();
		return pagamentoDAO.buscarPendentes();
	}

	public List<Pagamento> listarAtrasados() throws SQLException {
		verificarMensalidadesVencidas();
		List<Pagamento> todos = pagamentoDAO.listarTodos();
		return todos.stream()
				.filter(p -> "ATRASADO".equals(p.getStatus()))
				.collect(Collectors.toList());
	}

	public List<InadimplenteInfo> relatorioInadimplencia() throws SQLException {
		verificarMensalidadesVencidas();
		List<Pagamento> naoPagos = pagamentoDAO.listarTodos().stream()
				.filter(p -> !"PAGO".equals(p.getStatus()))
				.collect(Collectors.toList());

		List<InadimplenteInfo> relatorio = new ArrayList<>();
		LocalDate hoje = LocalDate.now();

		for (Pagamento p : naoPagos) {
			boolean atrasado = p.getDataVencimento() != null && p.getDataVencimento().isBefore(hoje);
			relatorio.add(mapearInadimplente(p, hoje, atrasado));
		}

		relatorio.sort((a, b) -> {
			if (a.dataVencimento == null) return 1;
			if (b.dataVencimento == null) return -1;
			return a.dataVencimento.compareTo(b.dataVencimento);
		});
		return relatorio;
	}

	private InadimplenteInfo mapearInadimplente(Pagamento p, LocalDate hoje, boolean atrasado) throws SQLException {
		String alunoNome = "N/A";
		String cpf = "N/A";
		try {
			Matricula matricula = matriculaDAO.buscarPorId(p.getMatriculaId());
			if (matricula != null) {
				Aluno aluno = alunoDAO.buscarPorId(matricula.getAlunoId());
				if (aluno != null) {
					alunoNome = aluno.getNome();
					cpf = aluno.getCpf() != null ? aluno.getCpf() : "N/A";
				}
			}
		} catch (SQLException e) {
			// mantém N/A
		}

		long diasAtraso = 0;
		if (p.getDataVencimento() != null && p.getDataVencimento().isBefore(hoje)) {
			diasAtraso = ChronoUnit.DAYS.between(p.getDataVencimento(), hoje);
		}

		return new InadimplenteInfo(
				p.getId(),
				alunoNome,
				cpf,
				p.getMatriculaId(),
				p.getValor(),
				p.getDataVencimento(),
				diasAtraso,
				atrasado || diasAtraso > 0
		);
	}

	public static class InadimplenteInfo {
		public final int pagamentoId;
		public final String alunoNome;
		public final String cpf;
		public final int matriculaId;
		public final BigDecimal valor;
		public final LocalDate dataVencimento;
		public final long diasAtraso;
		public final boolean atrasado;

		public InadimplenteInfo(int pagamentoId, String alunoNome, String cpf, int matriculaId,
				BigDecimal valor, LocalDate dataVencimento, long diasAtraso, boolean atrasado) {
			this.pagamentoId = pagamentoId;
			this.alunoNome = alunoNome;
			this.cpf = cpf;
			this.matriculaId = matriculaId;
			this.valor = valor;
			this.dataVencimento = dataVencimento;
			this.diasAtraso = diasAtraso;
			this.atrasado = atrasado;
		}
	}
}
