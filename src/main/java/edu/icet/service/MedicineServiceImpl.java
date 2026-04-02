package edu.icet.service;

import edu.icet.dto.MedicineDto;
import edu.icet.entity.MedicineEntity;
import edu.icet.repository.MedicineRepositoryImpl;
import edu.icet.repository.CrudRepository;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicineServiceImpl implements CrudService<MedicineDto, String> {

    // Dependency Injection (Instantiating the repository)
    private final CrudRepository<MedicineEntity, String> repository = new MedicineRepositoryImpl();

    @Override
    public boolean save(MedicineDto dto) throws SQLException {
        // Map DTO to Entity
        MedicineEntity entity = new MedicineEntity(
                dto.getMedicineCode(),
                dto.getName(),
                dto.getBrand(),
                dto.getSupplierId(),
                dto.getExpiryDate(),
                dto.getQtyOnHand(),
                dto.getUnitPrice()
        );
        return repository.save(entity);
    }

    @Override
    public boolean update(MedicineDto dto) throws SQLException {
        MedicineEntity entity = new MedicineEntity(
                dto.getMedicineCode(), dto.getName(), dto.getBrand(),
                dto.getSupplierId(), dto.getExpiryDate(), dto.getQtyOnHand(), dto.getUnitPrice()
        );
        return repository.update(entity);
    }

    @Override
    public boolean delete(String id) throws SQLException {
        return repository.delete(id);
    }

    @Override
    public MedicineDto search(String id) throws SQLException {
        MedicineEntity entity = repository.search(id);
        if (entity != null) {
            return new MedicineDto(
                    entity.getMedicineCode(),
                    entity.getName(),
                    entity.getBrand(),
                    entity.getSupplierId(),
                    entity.getExpiryDate(),
                    entity.getQtyOnHand(),
                    entity.getUnitPrice()
            );
        }
        return null;
    }
    @Override
    public List<MedicineDto> getAll() throws SQLException {
        List<MedicineEntity> entityList = repository.getAll();
        List<MedicineDto> dtoList = new ArrayList<>();

        for (MedicineEntity entity : entityList) {
            dtoList.add(new MedicineDto(
                    entity.getMedicineCode(),
                    entity.getName(),
                    entity.getBrand(),
                    entity.getSupplierId(),
                    entity.getExpiryDate(),
                    entity.getQtyOnHand(),
                    entity.getUnitPrice()
            ));
        }
        return dtoList;
    }
}