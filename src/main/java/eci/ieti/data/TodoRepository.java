package eci.ieti.data;


import eci.ieti.data.model.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface TodoRepository extends CrudRepository<Todo, Long> {

    Page<Todo> findByDescriptionContaining(String description, Pageable pageable);
    Page<Todo> findByResponsible(String responsible, Pageable pageable);
    List<Todo> findByDueDateBefore(String dueDate);
    List<Todo> findByResponsibleEqualsAndPriorityIsGreaterThanEqual(String responsible, int priority);
    List<Todo> findByDescriptionMatchesRegex(String regex);
}
