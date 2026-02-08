package com.fftmanager.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.regex.Pattern;

import com.fftmanager.dao.AlunoDAO;
import com.fftmanager.model.Aluno;

public class AlunoService {

	private static final int IDADE_MINIMA = 14;
	private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

	private final AlunoDAO alunoDAO = new AlunoDAO();

	private String formatarCpf(String cpf) {
		if (cpf == null) return null;
		return cpf.replaceAll("[^0-9]", "");
	}

	private boolean cpfValido(String cpf) {
		String numeros = formatarCpf(cpf);
		return numeros != null && numeros.length() == 11;
	}

	private void validarCpfUnico(String cpf, Integer idExcluir) throws SQLException {
		String cpfFormatado = formatarCpf(cpf);
		if (cpfFormatado == null || cpfFormatado.length() != 11) {
			throw new IllegalArgumentException("CPF deve conter 11 dígitos.");
		}
		Aluno existente = alunoDAO.buscarPorCpf(cpf);
		if (existente != null && (idExcluir == null || existente.getId() != idExcluir)) {
			throw new IllegalArgumentException("CPF já cadastrado para outro aluno.");
		}
	}

	private void validarEmail(String email) {
		if (email == null || email.trim().isEmpty()) {
			return;
		}
		if (!Pattern.compile(EMAIL_REGEX).matcher(email.trim()).matches()) {
			throw new IllegalArgumentException("E-mail inválido.");
		}
	}

	private void validarIdadeMinima(LocalDate dataNascimento) {
		if (dataNascimento == null) {
			throw new IllegalArgumentException("Data de nascimento é obrigatória.");
		}
		int idade = Period.between(dataNascimento, LocalDate.now()).getYears();
		if (idade < IDADE_MINIMA) {
			throw new IllegalArgumentException("Idade mínima para cadastro: " + IDADE_MINIMA + " anos.");
		}
	}

	private void validarAluno(Aluno aluno, Integer idExcluir) throws SQLException {
		if (aluno == null) {
			throw new IllegalArgumentException("Aluno não pode ser nulo.");
		}
		if (aluno.getNome() == null || aluno.getNome().trim().isEmpty()) {
			throw new IllegalArgumentException("Nome do aluno é obrigatório.");
		}
		validarCpfUnico(aluno.getCpf(), idExcluir);
		validarEmail(aluno.getEmail());
		validarIdadeMinima(aluno.getDataNascimento());
	}

	public Aluno cadastrar(Aluno aluno) throws SQLException {
		validarAluno(aluno, null);
		return alunoDAO.inserir(aluno);
	}

	public boolean atualizar(Aluno aluno) throws SQLException {
		if (aluno == null || aluno.getId() <= 0) {
			throw new IllegalArgumentException("Aluno inválido para atualização.");
		}
		if (alunoDAO.buscarPorId(aluno.getId()) == null) {
			throw new IllegalArgumentException("Aluno não encontrado.");
		}
		validarAluno(aluno, aluno.getId());
		return alunoDAO.atualizar(aluno);
	}

	public boolean deletar(int id) throws SQLException {
		if (id <= 0) {
			throw new IllegalArgumentException("ID inválido.");
		}
		if (alunoDAO.buscarPorId(id) == null) {
			throw new IllegalArgumentException("Aluno não encontrado.");
		}
		return alunoDAO.deletar(id);
	}

	public Aluno buscarPorId(int id) throws SQLException {
		return alunoDAO.buscarPorId(id);
	}

	public Aluno buscarPorCpf(String cpf) throws SQLException {
		return alunoDAO.buscarPorCpf(cpf);
	}

	public List<Aluno> listar() throws SQLException {
		return alunoDAO.listarTodos();
	}
}
