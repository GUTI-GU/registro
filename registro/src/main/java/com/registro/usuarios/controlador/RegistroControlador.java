package com.registro.usuarios.controlador;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.registro.usuarios.controlador.dto.UsuarioRegistroDTO;
import com.registro.usuarios.modelo.Usuario;
import com.registro.usuarios.servicio.UsuarioServicio;


@Controller
public class RegistroControlador {

    @Autowired
    private UsuarioServicio servicio;
	

	@GetMapping("/login")
	public String iniciarSesion() {
		return "login";
	}
	
   @GetMapping("/")
    public String verPaginaDeInicio(Model modelo) {
        modelo.addAttribute("usuarios", servicio.listarUsuarios());
        return "index"; // Tu vista de inicio
    }

    @GetMapping("/usuario/editar/{id}")
    public String mostrarFormularioDeEdicion(@PathVariable Long id, Model modelo) {
        Optional<Usuario> usuario = servicio.obtenerUsuarioPorId(id);
        if (usuario.isPresent()) {
            modelo.addAttribute("usuario", usuario.get());
            return "editar"; // Vista para editar usuario
        } else {
            return "redirect:/?error"; // Redirigir si no se encuentra el usuario
        }
    }

    @PostMapping("/usuario/editar/{id}")
    public String actualizarUsuario(@PathVariable Long id, @ModelAttribute("usuario") UsuarioRegistroDTO registroDTO) {
        servicio.actualizarUsuario(id, registroDTO);
        return "redirect:/?exito"; // Redirigir después de actualizar
    }

    @GetMapping("/usuario/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id) {
        servicio.eliminarUsuario(id);
        return "redirect:/?exito"; // Redirigir después de eliminar
    }

   
}
