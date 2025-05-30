package co.edu.uco.FondaControl.data.dao.entity.subcategoria.impl;

import co.edu.uco.FondaControl.crosscutting.excepciones.DataFondaControlException;
import co.edu.uco.FondaControl.data.dao.entity.subcategoria.SubcategoriaDAO;
import co.edu.uco.FondaControl.entity.SubcategoriaEntity;
import co.edu.uco.FondaControl.crosscutting.utilitarios.UtilTexto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SubcategoriaPostgreSQLDAO implements SubcategoriaDAO {
    private final Connection conexion;

    private static final String SQL_INSERT =
        "INSERT INTO subcategoria(codigo, nombre, codigocategoria) VALUES (?, ?, ?)";
    private static final String SQL_UPDATE =
        "UPDATE subcategoria SET nombre = ?, codigocategoria = ? WHERE codigo = ?";
    private static final String SQL_DELETE =
        "DELETE FROM subcategoria WHERE codigo = ?";
    private static final String SQL_FIND_BY_ID =
        "SELECT * FROM subcategoria WHERE codigo = ?";
    private static final String SQL_LIST_ALL =
        "SELECT * FROM subcategoria";

    public SubcategoriaPostgreSQLDAO(final Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public void create(final SubcategoriaEntity entity) throws DataFondaControlException {
        try (PreparedStatement ps = conexion.prepareStatement(SQL_INSERT)) {
            ps.setObject(1, entity.getCodigo());
            ps.setString(2, entity.getNombre());
            ps.setObject(3, entity.getCodigoCategoria());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw DataFondaControlException.reportar(
                "Error al crear subcategoría",
                "create@subcategoria - " + e.getMessage(), e
            );
        }
    }

    @Override
    public void update(final UUID id, final SubcategoriaEntity entity) throws DataFondaControlException {
        try (PreparedStatement ps = conexion.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, entity.getNombre());
            ps.setObject(2, entity.getCodigoCategoria());
            ps.setObject(3, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw DataFondaControlException.reportar(
                "Error al actualizar subcategoría",
                "update@subcategoria - " + e.getMessage(), e
            );
        }
    }

    @Override
    public void delete(final UUID id) throws DataFondaControlException {
        try (PreparedStatement ps = conexion.prepareStatement(SQL_DELETE)) {
            ps.setObject(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw DataFondaControlException.reportar(
                "Error al eliminar subcategoría",
                "delete@subcategoria - " + e.getMessage(), e
            );
        }
    }

    @Override
    public SubcategoriaEntity findById(final UUID id) throws DataFondaControlException {
        try (PreparedStatement ps = conexion.prepareStatement(SQL_FIND_BY_ID)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) {
            throw DataFondaControlException.reportar(
                "Error al buscar subcategoría por ID",
                "findById@subcategoria - " + e.getMessage(), e
            );
        }
    }

    @Override
    public List<SubcategoriaEntity> listAll() throws DataFondaControlException {
        List<SubcategoriaEntity> results = new ArrayList<>();
        try (PreparedStatement ps = conexion.prepareStatement(SQL_LIST_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                results.add(mapRow(rs));
            }
            return results;
        } catch (SQLException e) {
            throw DataFondaControlException.reportar(
                "Error al listar subcategorías",
                "listAll@subcategoria - " + e.getMessage(), e
            );
        }
    }

    @Override
    public List<SubcategoriaEntity> listByFilter(final SubcategoriaEntity filter) throws DataFondaControlException {
        StringBuilder sql = new StringBuilder(SQL_LIST_ALL);
        List<Object> params = new ArrayList<>();
        if (!UtilTexto.getInstancia().esNula(filter.getNombre())) {
            sql.append(" WHERE LOWER(nombre) LIKE LOWER(?)");
            params.add("%" + filter.getNombre() + "%");
        }
        List<SubcategoriaEntity> results = new ArrayList<>();
        try (PreparedStatement ps = conexion.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRow(rs));
                }
            }
            return results;
        } catch (SQLException e) {
            throw DataFondaControlException.reportar(
                "Error al filtrar subcategorías",
                "listByFilter@subcategoria - " + e.getMessage(), e
            );
        }
    }

    @Override
    public List<SubcategoriaEntity> listByCodigo(final UUID codigo) throws DataFondaControlException {
        SubcategoriaEntity entity = findById(codigo);
        return entity == null ? List.of() : List.of(entity);
    }

    private SubcategoriaEntity mapRow(final ResultSet rs) throws SQLException {
        return SubcategoriaEntity.builder()
            .codigo(rs.getObject("codigo", UUID.class))
            .nombre(rs.getString("nombre"))
            .codigoCategoria(rs.getObject("codigocategoria", UUID.class))
            .crear();
    }
}
