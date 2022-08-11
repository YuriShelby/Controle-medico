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
@RequestMapping("/api/v1")
public class ControleMedicoController {

    final ControleMedicoService medicoService;

    public ControleMedicoController(ControleMedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @PostMapping("/signup")
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

        medicoDto.setSenha(medicoService.getPasswordEncoder().encode(medicoDto.getSenha()));
        medicoDto.setConfirmacaoSenha(medicoService.getPasswordEncoder().encode(medicoDto.getConfirmacaoSenha()));
        var medicoModel = new ControleMedicoModel();
        BeanUtils.copyProperties(medicoDto, medicoModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(medicoService.save(medicoModel));
    }

    @GetMapping("/all")
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

    @GetMapping("/login")
    public ResponseEntity<Object> medicoLogin(@RequestParam String email,
                                              @RequestParam String senha) {
        Optional<ControleMedicoModel> controleLogin = medicoService.findByEmail(email);

        if (controleLogin.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Cadastro inexistente.");
        }

        boolean controleSenha = medicoService.getPasswordEncoder().matches(senha, controleLogin.get().getSenha());
        HttpStatus status = (controleSenha) ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status).body((controleSenha) ? "Login efetuado com sucesso!" : "Senha incorreta.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteControleMedico(@PathVariable(value = "id") Long id, @RequestParam String email,
                                                       @RequestParam String senha) {
        Optional<ControleMedicoModel> controleLogin = medicoService.findByEmail(email);
        Optional<ControleMedicoModel> controleOptional = medicoService.findById(id);
        boolean controleSenha = medicoService.getPasswordEncoder().matches(senha, controleLogin.get().getSenha());

        if (!controleOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Registro não encontrado.");
        }
        if (controleSenha == false){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email e senha não combinam.");
        }
        if(!Objects.equals(controleOptional.get(), controleLogin.get())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acesso negado!");
        }

        medicoService.delete(controleOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Registro apagado com sucesso!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateControleMedico(@PathVariable(value = "id") Long id,
                                                       @RequestBody @Valid ControleMedicoDto medicoDto,
                                                       @RequestParam String email,
                                                       @RequestParam String senha
                                                       ) {
        Optional<ControleMedicoModel> controleLogin = medicoService.findByEmail(email);
        Optional<ControleMedicoModel> controleOptional = medicoService.findById(id);
        boolean controleSenha = medicoService.getPasswordEncoder().matches(senha, controleLogin.get().getSenha());

        if (!controleOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Registro não encontrado.");
        }
        if (controleSenha == false){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email e senha não combinam.");
        }
        if(!Objects.equals(controleOptional.get(), controleLogin.get())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acesso negado!");
        }

        var medicoModel = new ControleMedicoModel();
        BeanUtils.copyProperties(medicoDto, medicoModel);
        medicoModel.setId(controleOptional.get().getId());
        return ResponseEntity.status(HttpStatus.OK).body(medicoService.save(medicoModel));
    }

}
