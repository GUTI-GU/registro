package com.registro.usuarios.servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.registro.usuarios.controlador.dto.UsuarioRegistroDTO;
import com.registro.usuarios.modelo.Usuario;

public interface UsuarioServicio extends UserDetailsService {

    public Usuario guardar(UsuarioRegistroDTO registroDTO);
    
    public List<Usuario> listarUsuarios();

    public Optional<Usuario> obtenerUsuarioPorId(Long id);

    public Usuario actualizarUsuario(Long id, UsuarioRegistroDTO registroDTO);

    public void eliminarUsuario(Long id);
    
    // Método para verificar si el email existe
    public boolean existeEmail(String email);
}
