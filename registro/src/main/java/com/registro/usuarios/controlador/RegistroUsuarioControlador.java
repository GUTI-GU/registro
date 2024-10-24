package com.registro.usuarios.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.registro.usuarios.controlador.dto.UsuarioRegistroDTO;
import com.registro.usuarios.servicio.UsuarioServicio;

@Controller
@RequestMapping("/registro")
public class RegistroUsuarioControlador {

	private UsuarioServicio usuarioServicio;

	public RegistroUsuarioControlador(UsuarioServicio usuarioServicio) {
		super();
		this.usuarioServicio = usuarioServicio;
	}
	
	@ModelAttribute("usuario")
	public UsuarioRegistroDTO retornarNuevoUsuarioRegistroDTO() {
		return new UsuarioRegistroDTO();
	}

	@GetMapping
	public String mostrarFormularioDeRegistro() {
		return "registro";
	}
	
	@PostMapping
	public String registrarCuentaDeUsuario(@ModelAttribute("usuario") UsuarioRegistroDTO registroDTO, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
        return "registro"; // Retornar a la vista con errores
    }
    
    // Aquí puedes verificar si el correo electrónico ya existe
    if (usuarioServicio.existeEmail(registroDTO.getEmail())) {
        bindingResult.rejectValue("email", "error.usuario", "Este correo ya está registrado");
        return "registro"; // Retornar a la vista con errores
    }

    usuarioServicio.guardar(registroDTO);
    return "redirect:/registro?exito"; // Redirigir después de registrar
}




	
}
