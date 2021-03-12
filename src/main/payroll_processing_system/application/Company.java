package payroll_processing_system.application;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Company class is a container class that is designed to hold Employee objects and any subclasses of Employee.
 * Company also provides a variety of methods to make changes to its bag of Employees such as adding, removing
 * and processing payments for Employees, as well as different ways of printing the current Employees in the company.
 *
 * @author Hugo De Moraes, Jonathan Dong
 */
public class Company {
    private Employee[] empList;
    private int numEmployee;
    private final int SIZE_FACTOR = 4; // initialize here for use in constructor
    public static final int HOURS_LOWER_BOUND = 0;
    public static final int HOURS_UPPER_BOUND = 100;
    public static final String[] DEPARTMENT_CODES = {"CS", "ECE", "IT"};
    public static final int[] MANAGER_CODES = {1, 2, 3};

    /**
     * default constructor to create an empty bag with numEmployee = 0
     */
    public Company() {
        empList = new Employee[SIZE_FACTOR];
        numEmployee = 0;
    }

    /**
     * helper method to find an employee in the bag
     *
     * @param employee Employee object to be found
     * @return index of Employee in empList, -1 if Employee is not found
     */
    private int find(Employee employee) {
        int indexOfEmp = -1;
        for (int i = 0; i < empList.length; i++) {
            if (empList[i] != null && empList[i].equals(employee)) {
                indexOfEmp = i;
                break;
            }
        }
        return indexOfEmp;
    }

    /**
     * helper method to grow the capacity of empList by 4
     */
    private void grow() {
        // create new array of length current + 4
        Employee[] newBag = new Employee[empList.length + SIZE_FACTOR];

        // copy over all elements from current to new array
        for (int i = 0; i < empList.length; i++) {
            newBag[i] = empList[i];
        }

        // set new array as the emplist property
        empList = newBag;
    }

    /**
     * adds an Employee to the bag, grows bag if needed
     *
     * @param employee Employee object to be added
     * @return true if Employee successfully added, false if employee is already in empList
     */
    public boolean add(Employee employee) {
        // check if employee already in database
        if (find(employee) != -1) {
            return false;
        }

        // grow data structure if needed
        if (numEmployee == empList.length - 1) {
            grow();
        }


        empList[numEmployee] = employee;
        numEmployee++;

        return true;
    }

    //maintain the original sequence

    /**
     * removes an Employee if it exists
     *
     * @param employee Employee object to be removed
     * @return true if employee is removed successfully, false if employee is not found
     */
    public boolean remove(Employee employee) {
        int indexOfEmp = find(employee);

        // case employee not found
        if (indexOfEmp == -1) {
            return false;
        }

        for (int i = 0; i < empList.length; i++) {
            // last elem will always be null by virtue of one being removed
            if (i + 1 == empList.length) {
                empList[i] = null;
                break;
            }
            // from the target index to end, shift elements to the left
            if (i >= indexOfEmp) {
                empList[i] = empList[i + 1];
            }
        }

        numEmployee--;
        return true;
    }

    /**
     * sets working hours for a part-time employee
     *
     * @param employee Employee to set hours
     * @return true if hours have successfully been set, false otherwise
     */
    public boolean setHours(Employee employee) {
        int indexOfEmp = find(employee);

        // case employee not found
        if (indexOfEmp == -1) {
            return false;
        }

        if (!(employee instanceof Parttime)) {
            return false;
        }

        final Parttime TARGET_EMPLOYEE = (Parttime) empList[indexOfEmp];
        final Parttime ARG_EMPLOYEE = (Parttime) employee;

        TARGET_EMPLOYEE.setHours(ARG_EMPLOYEE.getHours());

        return true;
    }

    /**
     * processes payments for all employees
     */
    public void processPayments() {
        for (Employee employee : empList) {
            if (employee != null) {
                employee.calculatePayment();
            }
        }
    }

    /**
     * checks if number of employees in Company is 0
     *
     * @return true if number of employees in company is 0, false otherwise
     */
    public boolean isEmpty() {
        return numEmployee == 0;
    }

    /**
     * sorts employees by date of hire
     */
    private void sortByDate() {
        int n = numEmployee;
        final int EQUALS_CASE = 0;
        Date currIndexEmployeeDate;
        Date minIndexEmployeeDate;

        // One by one move boundary of unsorted subarray
        for (int i = 0; i < n - 1; i++) {
            // Find the minimum element in unsorted array
            int min_idx = i;
            for (int j = i + 1; j < n; j++) {
                currIndexEmployeeDate = empList[j].getProfile().getDateHired();
                minIndexEmployeeDate = empList[min_idx].getProfile().getDateHired();
                // if currIndexEmployeeDate is less than minIndexEmployeeDate
                if (currIndexEmployeeDate.compareTo(minIndexEmployeeDate) < EQUALS_CASE) {
                    min_idx = j;
                }
            }
            // Swap the found minimum element with the first
            // element
            Employee temp = empList[min_idx];
            empList[min_idx] = empList[i];
            empList[i] = temp;
        }

    }

    /**
     * prints earnings statements only for employees in CS Department
     * @return String containing all earnings statements for CS employees
     */
    private String printCSDepartment() {
        String output = "";
        for (Employee employee : empList) {
            if (employee != null && employee.getProfile().getDepartment().equals(DEPARTMENT_CODES[0])) {
                output += employee.toString() + "\n";
            }
        }
        return output;
    }

    /**
     * prints earnings statements only for employees in ECE Department
     * @return string containing earnings for ECE employees
     */
    private String printECEDepartment() {
        String output = "";
        for (Employee employee : empList) {
            if (employee != null && employee.getProfile().getDepartment().equals(DEPARTMENT_CODES[1])) {
                output += employee.toString() + "\n";
            }
        }
        return output;
    }

    /**
     * prints earnings statements only for employees in IT Department
     * @return string containing earnings for IT employees
     */
    private String printITDepartment() {
        String output = "";
        for (Employee employee : empList) {
            if (employee != null && employee.getProfile().getDepartment().equals(DEPARTMENT_CODES[2])) {
                output += employee.toString() + "\n";
            }
        }
        return output;
    }

    /**
     * print earning statements for all employees
     * @return string containing earnings for all employees
     */
    public String print() {
        String output = "";
        for (Employee employee : empList) {
            if (employee != null) {
                output += employee.toString() + "\n";
            }
        }
        return output;
    }

    /**
     * prints earnings statements for all employees in order of hire date
     *
     * @return  string containing earnings for all employees sorted by date
     */
    public String printByDate() {
        sortByDate();
        return print();
    }

    /**
     * prints earning statements for all employees by order of department
     * @return string containing earnings for all employees sorted by department
     */
    public String printByDepartment() {
        return printCSDepartment() + printECEDepartment() + printITDepartment();
    }

    /**
     * exports employee database to a file selected by user
     * @return string message that indicates if data is empty, if file not selected or if database successfully exported
     */
    public String exportDatabase() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Target File for the Export");
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        Stage stage = new Stage();
        try {
            File targetFile = chooser.showSaveDialog(stage); //get the reference of the target file
            //write code to write to the file.
            if (targetFile == null) {
                return IoFields.NULL_FILE_LOG;
            }

                FileWriter writer = new FileWriter(targetFile);
                // loop through all people in company
                if (isEmpty()) {
                    return IoFields.EMPTY_DB_LOG;

                }


                writer.write(print());
                writer.close();

            } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return IoFields.EXPORT_SUCCESS_LOG;
    }

}
