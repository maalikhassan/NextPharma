package edu.icet.service;

import edu.icet.dto.SupplierDto;
import edu.icet.entity.SupplierEntity;
import edu.icet.repository.SupplierRepositoryImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierServiceImpl implements CrudService<SupplierDto, String> {

    private final SupplierRepositoryImpl repository = new SupplierRepositoryImpl();

    @Override
    public boolean save(SupplierDto entity) throws SQLException {
        SupplierEntity supplierEntity = new SupplierEntity(
                entity.getSupplierId(),
                entity.getName(),
                entity.getContactNumber()
        );
        return repository.save(supplierEntity);
    }

    @Override
    public boolean update(SupplierDto entity) throws SQLException {
        SupplierEntity supplierEntity = new SupplierEntity(
                entity.getSupplierId(),
                entity.getName(),
                entity.getContactNumber()
        );
        return repository.update(supplierEntity);
    }

    @Override
    public boolean delete(String id) throws SQLException {
        return repository.delete(id);
    }

    @Override
    public SupplierDto search(String id) throws SQLException {
        SupplierEntity entity = repository.search(id);
        if (entity != null) {
            return new SupplierDto(
                    entity.getSupplierId(),
                    entity.getName(),
                    entity.getContactNumber()
            );
        }
        return null;
    }

    @Override
    public List<SupplierDto> getAll() throws SQLException {
        List<SupplierEntity> all = repository.getAll();
        List<SupplierDto> dtoList = new ArrayList<>();
        for (SupplierEntity entity : all) {
            dtoList.add(new SupplierDto(
                    entity.getSupplierId(),
                    entity.getName(),
                    entity.getContactNumber()
            ));
        }
        return dtoList;
    }
}

