package pet.store.service;

import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pet.store.controller.model.PetStoreData;
import pet.store.dao.PetStoreDao;
import pet.store.entity.PetStore;

@Service // Marks this class as a service class
public class PetStoreService {

	/**
	 * Instance variable for PetStoreDao interface
	 */
	@Autowired
	private PetStoreDao petStoreDao;

	/**
	 * Saves the pet store information and calls copyPetStoreFields to get the
	 * fields from the PetStoreData class
	 * 
	 * @param petStoreData
	 * @return
	 */
	@Transactional(readOnly = false)
	public PetStoreData savePetStore(PetStoreData petStoreData) {
		Long petStoreId = petStoreData.getPetStoreId();
		PetStore petStore = findOrCreatePetStore(petStoreId);

		copyPetSoreFields(petStore, petStoreData);

		PetStore dbPetStore = petStoreDao.save(petStore);
		return new PetStoreData(dbPetStore);
	}

	/**
	 * Copies the fields from PetStoreData and sets them in petStore
	 * 
	 * @param petStore
	 * @param petStoreData
	 */
	private void copyPetSoreFields(PetStore petStore, PetStoreData petStoreData) {
		petStore.setPetStoreId(petStoreData.getPetStoreId());
		petStore.setPetStoreName(petStoreData.getPetStoreName());
		petStore.setPetStoreAddress(petStoreData.getPetStoreAddress());
		petStore.setPetStoreCity(petStoreData.getPetStoreCity());
		petStore.setPetStoreState(petStoreData.getPetStoreState());
		petStore.setPetStoreZip(petStoreData.getPetStoreZip());
		petStore.setPetStorePhone(petStoreData.getPetStorePhone());
	}

	/**
	 * If petStoreId is null (empty), create new PetStore with constructor.
	 * Otherwise, find the petStore with provided ID.
	 * 
	 * @param petStoreId
	 * @return petStore with provided ID
	 */
	private PetStore findOrCreatePetStore(Long petStoreId) {
		PetStore petStore;

		if (Objects.isNull(petStoreId)) {
			petStore = new PetStore();
		} else {
			petStore = findPetStoreById(petStoreId);
		}

		return petStore;
	}

	/**
	 * If the ID provided in HTTP request does not exist in database, throw
	 * NoSuchElementException
	 * 
	 * @param petStoreId
	 * @return
	 */
	private PetStore findPetStoreById(Long petStoreId) {
		return petStoreDao.findById(petStoreId)
				.orElseThrow(() -> new NoSuchElementException("Pet store with ID=" + petStoreId + " does not exist."));
	}
}
