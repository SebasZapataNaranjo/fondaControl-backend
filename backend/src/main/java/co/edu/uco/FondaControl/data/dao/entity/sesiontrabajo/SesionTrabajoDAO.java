package co.edu.uco.FondaControl.data.dao.entity.sesiontrabajo;

import co.edu.uco.FondaControl.crosscutting.excepciones.DataFondaControlException;
import co.edu.uco.FondaControl.data.dao.entity.CreateDAO;
import co.edu.uco.FondaControl.data.dao.entity.UpdateDAO;
import co.edu.uco.FondaControl.entity.SesionTrabajoEntity;

import java.util.ArrayList;
import java.util.UUID;

public interface SesionTrabajoDAO extends
        CreateDAO<SesionTrabajoEntity>,
        UpdateDAO<SesionTrabajoEntity, UUID> {

    SesionTrabajoEntity findById(UUID codigo) throws DataFondaControlException;

    SesionTrabajoEntity findByUsuario(UUID idUsuario) throws DataFondaControlException;

    ArrayList<SesionTrabajoEntity> listByFilter(SesionTrabajoEntity entityFilter) throws DataFondaControlException;

}