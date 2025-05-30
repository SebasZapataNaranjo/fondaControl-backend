package co.edu.uco.FondaControl.data.dao.entity.categoria;

import java.util.UUID;

import co.edu.uco.FondaControl.data.dao.entity.CreateDAO;
import co.edu.uco.FondaControl.data.dao.entity.DeleteDAO;
import co.edu.uco.FondaControl.data.dao.entity.RetrieveDAO;
import co.edu.uco.FondaControl.data.dao.entity.UpdateDAO;
import co.edu.uco.FondaControl.entity.CategoriaEntity;

public interface CategoriaDAO extends CreateDAO<CategoriaEntity>, RetrieveDAO<CategoriaEntity, UUID>,
		UpdateDAO<CategoriaEntity, UUID>, DeleteDAO<UUID> {
}
