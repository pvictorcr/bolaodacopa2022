package com.pvictorcr.bolaodacopa.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pvictorcr.bolaodacopa.constants.Credenciais;
import com.pvictorcr.bolaodacopa.constants.Provider;
import com.pvictorcr.bolaodacopa.model.Aposta;
import com.pvictorcr.bolaodacopa.model.Usuario;
import com.pvictorcr.bolaodacopa.repositories.UsuarioRepository;

@Service
public class UserService {
 
    @Autowired
    private UsuarioRepository repo;
     
    public boolean processOAuthPostLogin(String username, String nome) {
        Optional<Usuario> existUser = repo.getUsuarioByEmail(username);
         
        if (!existUser.isPresent()) {
            Usuario newUser = new Usuario();
            newUser.setEmail(username);
            newUser.setNome(nome);
            newUser.setProvider(Provider.GOOGLE);
            newUser.setCredencial(Credenciais.USUARIO);
            newUser.setAposta(new Aposta());
             
            repo.save(newUser); 
            
            return true;
        }
        
        return false;
    }
     
}
