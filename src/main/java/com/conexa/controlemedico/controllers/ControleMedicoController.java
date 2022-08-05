package com.conexa.controlemedico.controllers;

import com.conexa.controlemedico.dtos.ControleMedicoDto;
import com.conexa.controlemedico.models.ControleMedicoModel;
import com.conexa.controlemedico.services.ControleMedicoService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/v1/signup")
public class ControleMedicoController {

    final ControleMedicoService medicoService;

    public ControleMedicoController(ControleMedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @PostMapping
    public ResponseEntity<Object> saveControleMedico(@RequestBody @Valid ControleMedicoDto medicoDto) {
        if(medicoService.existsByEmail(medicoDto.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Este email já está em uso!");
        }
        if(medicoService.existsByCpf(medicoDto.getCpf())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Este cpf já está em uso!");
        }
        if(!Objects.equals(medicoDto.getConfirmacaoSenha(), medicoDto.getSenha())) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("Confirmação de senha incorreta!");
        }
        var medicoModel = new ControleMedicoModel();
        BeanUtils.copyProperties(medicoDto, medicoModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(medicoService.save(medicoModel));
    }

    @GetMapping
    public ResponseEntity<List<ControleMedicoModel>> getAllControleMedico() {
        return ResponseEntity.status(HttpStatus.OK).body(medicoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneControleMedico(@PathVariable(value = "id") Long id){
        Optional<ControleMedicoModel> controleOptional = medicoService.findById(id);
        if (!controleOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Registro não encontrado.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(controleOptional.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteControleMedico(@PathVariable(value = "id") Long id) {
        Optional<ControleMedicoModel> controleOptional = medicoService.findById(id);
        if (!controleOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Registro não encontrado.");
        }
        medicoService.delete(controleOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Registro apagado com sucesso!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateControleMedico(@PathVariable(value = "id") Long id,
                                                       @RequestBody @Valid ControleMedicoDto medicoDto) {
        Optional<ControleMedicoModel> controleOptional = medicoService.findById(id);
        if (!controleOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Registro não encontrado.");
        }
        var medicoModel = new ControleMedicoModel();
        BeanUtils.copyProperties(medicoDto, medicoModel);
        medicoModel.setId(controleOptional.get().getId());
        return ResponseEntity.status(HttpStatus.OK).body(medicoService.save(medicoModel));
    }

}
