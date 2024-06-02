package npu.deliverfoods.api.Service;

import java.util.List;

public interface IService<T> {

    public List<T> findAll();
    public T findById(long id);
    public void save(T model);
    public void update(T model);
    public void deleteById(long id);

}
