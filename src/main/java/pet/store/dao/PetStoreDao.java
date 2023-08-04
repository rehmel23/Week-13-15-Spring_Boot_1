package pet.store.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import pet.store.entity.PetStore;

/**
 * Interface for the PetStoreDao
 * @author clayr
 *
 */
public interface PetStoreDao extends JpaRepository<PetStore, Long> {

}
