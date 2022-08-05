package com.conexa.controlemedico.repositories;

import com.conexa.controlemedico.models.ControleMedicoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ControleMedicoRepository extends JpaRepository<ControleMedicoModel, Long> {

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);

}
