package pet.store.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import pet.store.entity.Customer;

/**
 * Customer Data Access Object interface
 * 
 * @author clayr
 *
 */
public interface CustomerDao extends JpaRepository<Customer, Long> {

}
