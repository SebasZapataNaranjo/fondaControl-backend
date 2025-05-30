package co.edu.uco.FondaControl.businesslogic.facade.imp;

import co.edu.uco.FondaControl.businesslogic.businesslogic.UsuarioBusinessLogic;
import co.edu.uco.FondaControl.businesslogic.businesslogic.assembler.Usuario.dto.UsuarioDTOAssembler;
import co.edu.uco.FondaControl.businesslogic.facade.UsuarioFacade;
import co.edu.uco.FondaControl.crosscutting.excepciones.BusinessLogicFondaControlException;
import co.edu.uco.FondaControl.crosscutting.excepciones.FondaControlException;
import co.edu.uco.FondaControl.crosscutting.utilitarios.UtilObjeto;
import co.edu.uco.FondaControl.dto.UsuarioDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public final class UsuarioImp implements UsuarioFacade {

    private final UsuarioBusinessLogic businessLogic;

    public UsuarioImp(final UsuarioBusinessLogic businessLogic) {
        this.businessLogic = businessLogic;
    }

    @Override
    public void registrarNuevoUsuario(final UsuarioDTO usuario) throws FondaControlException {
        validarUsuario(usuario);
        final var domain = UsuarioDTOAssembler.getInstancia().toDomain(usuario);
        businessLogic.registrarNuevoUsuario(domain);
    }

    @Override
    public void modificarUsuario(final UsuarioDTO usuario) throws FondaControlException {
        validarUsuario(usuario);
        final var domain = UsuarioDTOAssembler.getInstancia().toDomain(usuario);
        businessLogic.modificarUsuario(domain);
    }

    @Override
    public void eliminarUsuario(final UsuarioDTO usuario) throws FondaControlException {
        validarUsuario(usuario);
        final var domain = UsuarioDTOAssembler.getInstancia().toDomain(usuario);
        businessLogic.eliminarUsuario(domain);
    }

    @Override
    public void iniciarSesion(final UsuarioDTO usuario, final String tipoUsuario) throws FondaControlException {
        if (UtilObjeto.esNulo(usuario) || UtilObjeto.esNulo(tipoUsuario)) {
            throw new IllegalArgumentException("El usuario y el tipo de usuario no pueden ser nulos o vacíos.");
        }

        try {
            final var domain = UsuarioDTOAssembler.getInstancia().toDomain(usuario);
            businessLogic.iniciarSesion(domain, tipoUsuario);
        } catch (Exception exception) {
            throw BusinessLogicFondaControlException.reportar(
                    "Se ha producido un error al intentar iniciar sesión.",
                    "Error técnico durante el inicio de sesión: " + exception.getMessage(),
                    exception
            );
        }
    }

    @Override
    public List<UsuarioDTO> consultarUsuarios(final UsuarioDTO filtro) throws FondaControlException {
        try {
            var assembler = UsuarioDTOAssembler.getInstancia();
            var domainFilter = UtilObjeto.esNulo(filtro)
                    ? assembler.toDomain(null)
                    : assembler.toDomain(filtro);
            var domainList = businessLogic.consultarUsuarios(domainFilter);
            return domainList.stream()
                    .map(assembler::toDto)
                    .collect(Collectors.toList());
        } catch (FondaControlException ex) {
            throw ex;
        } catch (Exception ex) {
            throw BusinessLogicFondaControlException.reportar(
                    "Se ha producido un error al consultar los usuarios.",
                    "Error técnico durante consultarUsuarios: " + ex.getMessage(),
                    ex
            );
        }
    }

    @Override
    public UsuarioDTO consultarUsuarioPorCodigo(final UUID codigo) throws FondaControlException {
        if (UtilObjeto.esNulo(codigo)) {
            throw new IllegalArgumentException("El código del usuario no puede ser nulo.");
        }
        try {
            var assembler = UsuarioDTOAssembler.getInstancia();
            var tempDto = new UsuarioDTO();
            tempDto.setCodigoRol(codigo);
            var domainInput = assembler.toDomain(tempDto);
            var domainResult = businessLogic.consultarUsuarioPorCodigo(domainInput);
            return assembler.toDto(domainResult);
        } catch (FondaControlException ex) {
            throw ex;
        } catch (Exception ex) {
            throw BusinessLogicFondaControlException.reportar(
                    "Se ha producido un error al consultar el usuario por código.",
                    "Error técnico durante consultarUsuarioPorCodigo: " + ex.getMessage(),
                    ex
            );
        }
    }

    private void validarUsuario(final UsuarioDTO usuario) {
        if (UtilObjeto.esNulo(usuario)) {
            throw new IllegalArgumentException("El usuario no puede ser nulo.");
        }
    }
}
