package co.edu.uco.FondaControl.businesslogic.businesslogic.domain;

import co.edu.uco.FondaControl.crosscutting.utilitarios.UtilTexto;
import co.edu.uco.FondaControl.crosscutting.utilitarios.UtilUUID;

import java.util.UUID;

public final class FormaPagoDomain {

    private final UUID codigo;
    private final String nombre;

    public FormaPagoDomain(final UUID codigo, final String nombre) {
        this.codigo = UtilUUID.obtenerValorDefecto(codigo);
        this.nombre = validarNombre(nombre);
    }

    public static FormaPagoDomain crear(final UUID codigo, final String nombre) {
        return new FormaPagoDomain(codigo, nombre);
    }

    public static FormaPagoDomain obtenerValorDefecto() {
        return new FormaPagoDomain(UtilUUID.obtenerValorDefecto(), "");
    }

    public UUID getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    private static String validarNombre(final String nombre) {
        var texto = UtilTexto.getInstancia().quitarEspaciosBlancoInicioFin(nombre);
        if (UtilTexto.getInstancia().esNula(texto)) {
            throw new IllegalArgumentException("El nombre de la forma de pago no puede ser nulo ni vacío.");
        }
        if (texto.length() > 50) {
            throw new IllegalArgumentException("El nombre de la forma de pago no puede exceder los 50 caracteres.");
        }
        return texto;
    }
}