package ma.emsi.repository.api.common;

import java.util.List;

public interface CrudRepo <T,ID>{
    List<T> findAll();
    T findById(ID id);
    void create(T newElement);
    void update(T newValuesElement);
    void delete(T patient);
    void deleteById(ID id);
}
