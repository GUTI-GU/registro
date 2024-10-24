package com.registro.usuarios.controlador;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.registro.usuarios.controlador.dto.UsuarioRegistroDTO;
import com.registro.usuarios.servicio.UsuarioServicio;

@Controller
@RequestMapping("/nuevo-registro")
public class NuevoRegistroControlador {

    private final UsuarioServicio usuarioServicio;

    public NuevoRegistroControlador(UsuarioServicio usuarioServicio) {
        this.usuarioServicio = usuarioServicio;
    }

    @ModelAttribute("usuario")
    public UsuarioRegistroDTO retornarNuevoUsuarioRegistroDTO() {
        return new UsuarioRegistroDTO();
    }

    @GetMapping
    public String mostrarFormularioDeNuevoRegistro(Model modelo) {
        return "nuevo_registro"; // La vista para el nuevo registro
    }

@PostMapping
public String registrarNuevoUsuario(@ModelAttribute("usuario") UsuarioRegistroDTO registroDTO, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
        return "nuevo_registro"; // Retornar a la vista con errores
    }
    
    // Aquí puedes verificar si el correo electrónico ya existe
    if (usuarioServicio.existeEmail(registroDTO.getEmail())) {
        bindingResult.rejectValue("email", "error.usuario", "Este correo ya está registrado");
        return "nuevo_registro"; // Retornar a la vista con errores
    }

    usuarioServicio.guardar(registroDTO);
    return "redirect:/nuevo-registro?exito"; // Redirigir después de registrar
}




}
