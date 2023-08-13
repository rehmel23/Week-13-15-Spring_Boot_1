package pet.store.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import pet.store.entity.Employee;
/**
 * Employee Data Access Object interface
 * @author clayr
 *
 */
public interface EmployeeDao extends JpaRepository<Employee, Long> {

}
