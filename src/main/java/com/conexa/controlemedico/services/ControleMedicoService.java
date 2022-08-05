package com.conexa.controlemedico.services;

import com.conexa.controlemedico.models.ControleMedicoModel;
import com.conexa.controlemedico.repositories.ControleMedicoRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ControleMedicoService {

    final ControleMedicoRepository medicoRepository;

    public ControleMedicoService(ControleMedicoRepository medicoRepository) {
        this.medicoRepository = medicoRepository;
    }

    @Transactional
    public ControleMedicoModel save(ControleMedicoModel medicoModel) {
        return medicoRepository.save(medicoModel);

    }
    public boolean existsByEmail(String email) {
        return medicoRepository.existsByEmail(email);
    }
    public boolean existsByCpf(String cpf) {
        return medicoRepository.existsByCpf(cpf);
    }

    public List<ControleMedicoModel> findAll() {
        return medicoRepository.findAll();
    }



    public Optional<ControleMedicoModel> findById(Long id) {
        return medicoRepository.findById(id);
    }

    @Transactional
    public void delete(ControleMedicoModel medicoModel) {
        medicoRepository.delete(medicoModel);
    }
}
