package pet.store.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreData.PetStoreCustomer;
import pet.store.controller.model.PetStoreData.PetStoreEmployee;
import pet.store.service.PetStoreService;

@RestController
@RequestMapping("/pet_store")
@Slf4j
public class PetStoreController {

	/**
	 * Instance variable of PetStoreService
	 */
	@Autowired
	private PetStoreService petStoreService;

	/**
	 * POST method, inserts a pet store into the database with provided field
	 * information. Shows 201 status code if successful.
	 * 
	 * @param petStoreData
	 * @return call savePetStore in PetStoreService class using petStoreData
	 *         provided from ARC
	 */
	@PostMapping("/pet_store")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreData insertPetStore(@RequestBody PetStoreData petStoreData) {
		log.info("Creating pet store {}", petStoreData);
		return petStoreService.savePetStore(petStoreData);
	}

	/**
	 * PUT method, modifies data in an existing pet store with given pet store ID.
	 * 
	 * @param petStoreId
	 * @param petStoreData
	 * @return save pet store data to the service class
	 */
	@PutMapping("/{petStoreId}")
	public PetStoreData updatePetStore(@PathVariable Long petStoreId, @RequestBody PetStoreData petStoreData) {
		petStoreData.setPetStoreId(petStoreId);
		log.info("Updating pet store {}", petStoreData);
		return petStoreService.savePetStore(petStoreData);
	}

	/**
	 * POST method, adds an employee to a specific pet store based on ID.
	 * 
	 * @param petStoreId
	 * @param petStoreEmployee
	 * @return
	 */
	@PostMapping("/{petStoreId}/employee")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreEmployee insertEmployee(@PathVariable Long petStoreId,
			@RequestBody PetStoreEmployee petStoreEmployee) {
		log.info("Creating employee {} for pet store with ID={}", petStoreEmployee.getEmployeeId(), petStoreId);

		return petStoreService.saveEmployee(petStoreId, petStoreEmployee);
	}

	/**
	 * POST method, adds customer to a specific pet store based on ID.
	 * 
	 * @param petStoreId
	 * @param petStoreCustomer
	 * @return
	 */
	@PostMapping("/{petStoreId}/customer")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreCustomer insertCustomer(@PathVariable Long petStoreId,
			@RequestBody PetStoreCustomer petStoreCustomer) {
		log.info("Creating customer {} for pet store with ID={}", petStoreCustomer.getCustomerId(), petStoreId);

		return petStoreService.saveCustomer(petStoreId, petStoreCustomer);
	}

	/**
	 * GET method, retrieves all pet stores stored in database. Does not list the
	 * customers or employees for the pet stores. I think that the "/pet_store" for
	 * the mapping is probably unnecessary and redundant. I tried without anything
	 * and it didn't work correctly. It worked with it, so I left it.
	 * 
	 * @return List of PetStoreData
	 */
	@GetMapping("/pet_store")
	public List<PetStoreData> listAllPetStores() {
		log.info("Listing all pet stores.");
		return petStoreService.retrieveAllPetStores();
	}

	/**
	 * GET method, retrieves all information associated with a single PetStore based
	 * on ID provided in URL.
	 * 
	 * @param petStoreId
	 * @return
	 */
	@GetMapping("/pet_store/{petStoreId}")
	public PetStoreData getPetStoreById(@PathVariable Long petStoreId) {
		log.info("Retrieving pet store with ID={}", petStoreId);
		return petStoreService.retrievePetStoreById(petStoreId);
	}

	/**
	 * DELETE method, deletes a single pet store based on given ID and all
	 * information (except customers) associated with the pet store
	 * 
	 * @param petStoreId
	 * @return
	 */
	@DeleteMapping("/{petStoreId}")
	public Map<String, String> deletePetStoreById(@PathVariable Long petStoreId) {
		log.info("Delete pet store with ID={}", petStoreId);
		petStoreService.deletePetStoreById(petStoreId);

		return Map.of("message", "Deletion of pet store with ID=" + petStoreId + " was successful.");
	}
}
