package com.registro.usuarios.servicio;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.registro.usuarios.controlador.dto.UsuarioRegistroDTO;
import com.registro.usuarios.modelo.Rol;
import com.registro.usuarios.modelo.Usuario;
import com.registro.usuarios.repositorio.UsuarioRepositorio;

@Service
public class UsuarioServicioImpl implements UsuarioServicio {

    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UsuarioServicioImpl(UsuarioRepositorio usuarioRepositorio) {
        super();
        this.usuarioRepositorio = usuarioRepositorio;
    }

    @Override
    public Usuario guardar(UsuarioRegistroDTO registroDTO) {
        Usuario usuario = new Usuario(
                registroDTO.getNombre(), 
                registroDTO.getApellido(),
                registroDTO.getTelefono(),
                registroDTO.getCedula(),
                registroDTO.getEmail(),
                passwordEncoder.encode(registroDTO.getPassword()), 
                Arrays.asList(new Rol("ROLE_USER"))
        );
        return usuarioRepositorio.save(usuario);
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepositorio.findAll();
    }

    @Override
    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        return usuarioRepositorio.findById(id);
    }

    @Override
    public Usuario actualizarUsuario(Long id, UsuarioRegistroDTO registroDTO) {
        Usuario usuarioExistente = usuarioRepositorio.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuarioExistente.setNombre(registroDTO.getNombre());
        usuarioExistente.setApellido(registroDTO.getApellido());
        usuarioExistente.setTelefono(registroDTO.getTelefono());
        usuarioExistente.setCedula(registroDTO.getCedula());
        usuarioExistente.setEmail(registroDTO.getEmail());
        usuarioExistente.setPassword(passwordEncoder.encode(registroDTO.getPassword()));
        return usuarioRepositorio.save(usuarioExistente);
    }

    @Override
    public void eliminarUsuario(Long id) {
        usuarioRepositorio.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.findByEmail(username);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario o password inválidos");
        }
        return new User(usuario.getEmail(), usuario.getPassword(), mapearAutoridadesRoles(usuario.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapearAutoridadesRoles(Collection<Rol> roles) {
        return roles.stream()
            .map(role -> new SimpleGrantedAuthority(role.getNombre()))
            .collect(Collectors.toList());
    }

    // Implementación del método existeEmail
    @Override
    public boolean existeEmail(String email) {
        return usuarioRepositorio.findByEmail(email) != null;
    }
}
