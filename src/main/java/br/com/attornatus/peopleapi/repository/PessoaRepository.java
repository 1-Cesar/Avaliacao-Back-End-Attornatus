package br.com.attornatus.peopleapi.repository;

import br.com.attornatus.peopleapi.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoaRepository  extends JpaRepository<Pessoa, Integer> {
}
