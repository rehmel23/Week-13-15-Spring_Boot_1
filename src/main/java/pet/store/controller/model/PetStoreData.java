package pet.store.controller.model;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;
import pet.store.entity.Customer;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;

/**
 * Define information that is stored for a pet store.
 * 
 * @author clayr
 *
 */
@Data
@NoArgsConstructor
public class PetStoreData {
	// Pet store fields
	private Long petStoreId;
	private String petStoreName;
	private String petStoreAddress;
	private String petStoreCity;
	private String petStoreState;
	private Long petStoreZip;
	private String petStorePhone;
	private Set<PetStoreCustomer> customers = new HashSet<>();
	private Set<PetStoreEmployee> employees = new HashSet<>();

	/**
	 * Constructor when passed PetStore object as the parameter.
	 * 
	 * @param petStore
	 */
	public PetStoreData(PetStore petStore) {
		petStoreId = petStore.getPetStoreId();
		petStoreName = petStore.getPetStoreName();
		petStoreAddress = petStore.getPetStoreAddress();
		petStoreCity = petStore.getPetStoreCity();
		petStoreState = petStore.getPetStoreState();
		petStoreZip = petStore.getPetStoreZip();
		petStorePhone = petStore.getPetStorePhone();

		// Enhanced for loops to set customers and employees sets.
		for (Customer customer : petStore.getCustomers()) {
			customers.add(new PetStoreCustomer(customer));
		}

		for (Employee employee : petStore.getEmployees()) {
			employees.add(new PetStoreEmployee(employee));
		}
	}

	/**
	 * Inner class of a pet store customer.
	 * 
	 * @author clayr
	 *
	 */
	@Data
	@NoArgsConstructor
	public static class PetStoreCustomer {
		// Pet store customer fields.
		private Long customerId;
		private String customerFirstName;
		private String customerLastName;
		private String customerEmail;

		/**
		 * PetStoreCustomer constructor when passed customer parameter.
		 * 
		 * @param customer
		 */
		public PetStoreCustomer(Customer customer) {
			customerId = customer.getCustomerId();
			customerFirstName = customer.getCustomerFirstName();
			customerLastName = customer.getCustomerLastName();
			customerEmail = customer.getCustomerEmail();
		}
	}

	/**
	 * Pet store employee inner class.
	 * 
	 * @author clayr
	 *
	 */
	@Data
	@NoArgsConstructor
	public static class PetStoreEmployee {
		// Pet store employee fields
		private Long employeeId;
		private String employeeFirstName;
		private String employeeLastName;
		private String employeePhone;
		private String employeeJobTitle;

		/**
		 * PetStoreEmployee constructor when passed an employee object as a parameter.
		 * 
		 * @param employee
		 */
		public PetStoreEmployee(Employee employee) {
			employeeId = employee.getEmployeeId();
			employeeFirstName = employee.getEmployeeFirstName();
			employeeLastName = employee.getEmployeeLastName();
			employeePhone = employee.getEmployeePhone();
			employeeJobTitle = employee.getEmployeeJobTitle();
		}
	}
}
