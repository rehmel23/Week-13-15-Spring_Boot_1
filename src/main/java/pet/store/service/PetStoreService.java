package pet.store.service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreData.PetStoreCustomer;
import pet.store.controller.model.PetStoreData.PetStoreEmployee;
import pet.store.dao.CustomerDao;
import pet.store.dao.EmployeeDao;
import pet.store.dao.PetStoreDao;
import pet.store.entity.Customer;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;

@Service // Marks this class as a service class
public class PetStoreService {

	/**
	 * Instance variable for PetStoreDao interface
	 */
	@Autowired
	private PetStoreDao petStoreDao;

	@Autowired
	private EmployeeDao employeeDao;

	@Autowired
	private CustomerDao customerDao;

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

	/**
	 * Saves all associated info for a PetStore and Employee based on ID for each.
	 * Creates relationship between individual employee and pet store. Converts
	 * Employee object to PetStoreEmployee object.
	 * 
	 * @param petStoreId
	 * @param petStoreEmployee
	 * @return
	 */
	@Transactional(readOnly = false)
	public PetStoreEmployee saveEmployee(Long petStoreId, PetStoreEmployee petStoreEmployee) {
		PetStore petStore = findPetStoreById(petStoreId);
		Employee employee = findOrCreateEmployee(petStoreId, petStoreEmployee.getEmployeeId());

		copyEmployeeFields(employee, petStoreEmployee);
		employee.setPetStore(petStore);
		petStore.getEmployees().add(employee);

		return new PetStoreEmployee(employeeDao.save(employee));
	}

	/**
	 * Sets fields in Employee based on related fields in PetStoreEmployee.
	 * 
	 * @param employee
	 * @param petStoreEmployee
	 */
	private void copyEmployeeFields(Employee employee, PetStoreEmployee petStoreEmployee) {
		employee.setEmployeeId(petStoreEmployee.getEmployeeId());
		employee.setEmployeeFirstName(petStoreEmployee.getEmployeeFirstName());
		employee.setEmployeeLastName(petStoreEmployee.getEmployeeLastName());
		employee.setEmployeePhone(petStoreEmployee.getEmployeePhone());
		employee.setEmployeeJobTitle(petStoreEmployee.getEmployeeJobTitle());
	}

	/**
	 * If employeeId is null, create employee. Otherwise, find the employee based on
	 * ID and pet store ID.
	 * 
	 * @param petStoreId
	 * @param employeeId
	 * @return
	 */
	private Employee findOrCreateEmployee(Long petStoreId, Long employeeId) {
		if (Objects.isNull(employeeId)) {
			return new Employee();
		} else {
			return findEmployeeById(petStoreId, employeeId);
		}
	}

	/**
	 * Find employee by ID in the DAO, otherwise throw NoSuchElementException with
	 * message. If the employeeId and petStoreId don't go with each other (employee
	 * doesn't work at that store), then throw an IllegalArgumentException. Return
	 * found employee if successful.
	 * 
	 * @param petStoreId
	 * @param employeeId
	 * @return
	 */
	private Employee findEmployeeById(Long petStoreId, Long employeeId) {
		Employee employee = employeeDao.findById(employeeId)
				.orElseThrow(() -> new NoSuchElementException("Employee with ID=" + employeeId + " does not exist."));

		if (employee.getPetStore().getPetStoreId() != petStoreId) {
			throw new IllegalArgumentException(
					"Employee with ID=" + employeeId + " does not work at pet store with ID=" + petStoreId + ".");
		}

		return employee;
	}

	/**
	 * Same as saveEmployee, just with a customer. Added functionality to
	 * accommodate the many-to-many relationship between PetStore and Customer.
	 * 
	 * @param petStoreId
	 * @param petStoreCustomer
	 * @return
	 */
	@Transactional(readOnly = false)
	public PetStoreCustomer saveCustomer(Long petStoreId, PetStoreCustomer petStoreCustomer) {
		PetStore petStore = findPetStoreById(petStoreId);
		Customer customer = findOrCreateCustomer(petStoreId, petStoreCustomer.getCustomerId());

		copyCustomerFields(customer, petStoreCustomer);
		// Add petStore to Set in Customer
		customer.getPetStores().add(petStore);
		// Add customer to Set in PetStore
		petStore.getCustomers().add(customer);

		return new PetStoreCustomer(customerDao.save(customer));
	}

	/**
	 * Same as copyEmployeeFields except in Customer.
	 * 
	 * @param customer
	 * @param petStoreCustomer
	 */
	private void copyCustomerFields(Customer customer, PetStoreCustomer petStoreCustomer) {
		customer.setCustomerId(petStoreCustomer.getCustomerId());
		customer.setCustomerFirstName(petStoreCustomer.getCustomerFirstName());
		customer.setCustomerLastName(petStoreCustomer.getCustomerLastName());
		customer.setCustomerEmail(petStoreCustomer.getCustomerEmail());
	}

	/**
	 * Same as findOrCreateEmployee, but with Customer.
	 * 
	 * @param petStoreId
	 * @param customerId
	 * @return
	 */
	private Customer findOrCreateCustomer(Long petStoreId, Long customerId) {
		if (Objects.isNull(customerId)) {
			return new Customer();
		} else {
			return findCustomerById(petStoreId, customerId);
		}
	}

	/**
	 * Similar to the method in Employee, but had to deal with the fact that
	 * PetStores is a Set in Customer. Enhanced For loop searches through the Set
	 * until it finds the correct petStoreId, then breaks.
	 * 
	 * @param petStoreId
	 * @param customerId
	 * @return
	 */
	private Customer findCustomerById(Long petStoreId, Long customerId) {
		Customer customer = customerDao.findById(customerId)
				.orElseThrow(() -> new NoSuchElementException("Customer with ID=" + customerId + " does not exist."));
		boolean found = false;

		for (PetStore petStore : customer.getPetStores()) {
			if (petStore.getPetStoreId() == petStoreId) {
				found = true;
				break;
			}
		}
		if (!found) {
			throw new IllegalArgumentException(
					"Customer with ID=" + customerId + " does not shop at pet store with ID=" + petStoreId + ".");
		}

		return customer;
	}

	/**
	 * Stores PetStore information into new list of type PetStoreData.
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<PetStoreData> retrieveAllPetStores() {
		List<PetStoreData> petStoreData = new LinkedList<>();

		for (PetStore petStore : petStoreDao.findAll()) {
			PetStoreData psd = new PetStoreData(petStore);

			// Removes customer and employee data from data list before returning.
			psd.getCustomers().clear();
			psd.getEmployees().clear();

			petStoreData.add(psd);
		}

		return petStoreData;
	}

	/**
	 * Calls findPetStoreById method and converts the returned information into a
	 * PetStoreData object.
	 * 
	 * @param petStoreId
	 * @return
	 */
	@Transactional(readOnly = true)
	public PetStoreData retrievePetStoreById(Long petStoreId) {
		return new PetStoreData(findPetStoreById(petStoreId));
	}

	/**
	 * Deletes petStore by utilizing the delete method in the DAO.
	 * 
	 * @param petStoreId
	 */
	public void deletePetStoreById(Long petStoreId) {
		PetStore petStore = findPetStoreById(petStoreId);

		petStoreDao.delete(petStore);
	}
}
