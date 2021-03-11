package payroll_processing_system.controller;

import javafx.scene.Node;
import javafx.scene.text.Text;
import payroll_processing_system.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Controller {
    private Company company;
    private String userInput;
    private String[] tokens;
    private Node selectedEmployeeType;
    private DateTimeFormatter formatters;
    private final String SALARY_LABEL = "Yearly Salary";
    private final String HOURLY_RATE_LABEL = "Hourly Rate";
    private final String HOURS_WORKED_LABEL = "Hours Worked";
    private final String MANAGEMENT_CODE_LABEL = "Manager Code";

    @FXML // fx:id="textArea"
    private TextArea textArea; // Value injected by FXMLLoader

    @FXML // fx:id="parttime"
    private RadioButton parttime; // Value injected by FXMLLoader

    @FXML // fx:id="fulltime"
    private RadioButton fulltime; // Value injected by FXMLLoader

    @FXML // fx:id="management"
    private RadioButton management; // Value injected by FXMLLoader

    @FXML // fx:id="firstName"
    private TextField firstName; // Value injected by FXMLLoader

    @FXML // fx:id="lastName"
    private TextField lastName; // Value injected by FXMLLoader

    @FXML // fx:id="hireDate"
    private DatePicker hireDate; // Value injected by FXMLLoader

    @FXML // fx:id="propertyOne"
    private TextField propertyOne; // Value injected by FXMLLoader

    @FXML // fx:id="propertyOneLabel"
    private Text propertyOneLabel; // Value injected by FXMLLoader

    @FXML // fx:id="departmentCode"
    private TextField departmentCode; // Value injected by FXMLLoader

    @FXML // fx:id="propertyTwoLabel"
    private Text propertyTwoLabel; // Value injected by FXMLLoader

    @FXML // fx:id="propertyTwo"
    private TextField propertyTwo; // Value injected by FXMLLoader

    /**
     * default constructor for PayrollProcessing
     */
    public Controller() {
        company = new Company();
        formatters = DateTimeFormatter.ofPattern("MM/dd/uuuu");
    }

    /**
     * tokenizes a given input String
     *
     * @param input string to be tokenized
     * @return String array of tokens (Strings split with ,)
     */
    private String[] tokenize(String input) {
        return input.split(" ");
    }

    /* -------------- Helper Methods -------------- */

    /**
     * helper method to determine if given Date object is valid
     * @param date Date object to be evaluated
     * @return true if date is valid, false otherwise
     */
    private boolean isValidDate(Date date) {
        if(!date.isValid()) {
            textArea.appendText(String.format(IoFields.INVALID_DATE_LOG, date));
            return false;
        }

        return true;
    }

    /**
     * helper method to determine if department code is valid
     * @param code deptcode to be evaluated
     * @return true if code is valid, false otherwise
     */
    private boolean isValidDeptCode(String code) {
        for (String departmentCode : Company.DEPARTMENT_CODES) {
            if (departmentCode.equals(code)) {
                return true;
            }
        }
        textArea.appendText(String.format(IoFields.INVALID_DEPARTMENT_CODE_LOG, code));
        return false;
    }

    /**
     * helper method to determine if hourlyRate is valid
     * @param rate rate to be evaluated
     * @return true if rate is greater than 0, false otherwise
     */
    private boolean isValidHourlyRate(double rate) {
        final int HOURLY_RATE_LOWER_BOUND = 0;

        if( rate < HOURLY_RATE_LOWER_BOUND ) {
            textArea.appendText(IoFields.INVALID_PAY_RATE_LOG);
            return false;
        }

        return true;
    }

    /**
     * helper method to determine if salary is valid
     * @param salary salary to be evaluated
     * @return true if salary is a double greater than 0, false otherwise
     */
    private boolean isValidSalary(double salary) {
        final int SALARY_LOWER_BOUND = 0;

        if( salary < SALARY_LOWER_BOUND ) {
            textArea.appendText(IoFields.INVALID_SALARY_LOG);
            return false;
        }
        return true;
    }

    /**
     * helper method to determine if management code is valid
     * @param code code to be evaluated
     * @return true if code is integer between 1 and 3, false otherwise
     */
    private boolean isValidMgmtCode(int code) {
        for (int managerCode : Company.MANAGER_CODES) {
            if (code == managerCode) {
                return true;
            }
        }

        textArea.appendText(IoFields.INVALID_MANAGER_CODE_LOG);
        return false;
    }

    /**
     * helper method to determine if hours are valid
     * @param hours num of hours to be evaluated
     * @return true if hours is int between 0 and 100 inclusive, false otherwise
     */
    private boolean isValidHours(int hours) {

        if(hours < Company.HOURS_LOWER_BOUND) {
            textArea.appendText(IoFields.SET_NEGATIVE_HOURS_FAILURE_LOG);
            return false;
        }

        if(hours > Company.HOURS_UPPER_BOUND) {
            textArea.appendText(IoFields.SET_OVER_ONE_HUNDRED_HOURS_FAILURE_LOG);
            return false;
        }

        return true;
    }

    /**
     * helper method to determine if various fields are valid
     * @param deptCode department code to be evaluated
     * @param date date object to be evaluated
     * @param salary salary to be evaluated
     * @param mgmtCode management code to be evaluated
     * @return true if all are valid, false otherwise
     */
    private boolean isValidFields(String deptCode, Date date, double salary, int mgmtCode ) {
        return isValidDeptCode(deptCode) && isValidDate(date) && isValidSalary(salary) && isValidMgmtCode(mgmtCode);
    }

    /**
     * helper method to determine if various fields are valid
     * @param deptCode department code to be evaluated
     * @param date date object to be evaluated
     * @return true if all are valid, false otherwise
     */
    private boolean isValidFields(String deptCode, Date date) {
        return isValidDeptCode(deptCode) && isValidDate(date);
    }

    /**
     * helper method for adding any employee, if true prints success log, if false prints fail log
     * @param employee Employee object to be added
     */
    private void addEmployee(Employee employee) {
        if(!company.add(employee)) {
            textArea.appendText(IoFields.EMPLOYEE_ADD_FAILURE_LOG);
            return;
        }

        textArea.appendText(IoFields.EMPLOYEE_ADD_SUCCESS_LOG);
    }

    /**
     * processes user input from command line when adding a Parttime employee
     */
    private void handleAddParttime() {
        // get input fields
        final String NAME;
        final String DEPARTMENT;
        final Date DATE_HIRED;
        final double RATE;

        try {
            NAME = firstName.getText() + "," + lastName.getText();
            DEPARTMENT = departmentCode.getText();
            DATE_HIRED = new Date(hireDate.getValue().format(formatters));
            RATE = Double.parseDouble(propertyOne.getText());
        }
        catch(Exception e) {
            textArea.appendText(IoFields.MISSING_PARAMS_LOG);
            return;
        }

        // validate entry
        if(!isValidFields(DEPARTMENT, DATE_HIRED)) {
            return;
        }

        if(!isValidHourlyRate(RATE)) {
            return;
        }

        // try add
        addEmployee(new Parttime(NAME, DEPARTMENT, DATE_HIRED, RATE));
    }

    /**
     * handles user input from command line when adding Fulltime employee
     */
    private void handleAddFulltime() {
        // get input fields
        final String NAME;
        final String DEPARTMENT;
        final Date DATE_HIRED;
        final double SALARY;

        try {
            NAME = firstName.getText() + "," + lastName.getText();
            DEPARTMENT = departmentCode.getText();
            DATE_HIRED = new Date(hireDate.getValue().format(formatters));
            SALARY = Double.parseDouble(propertyOne.getText());
        }
        catch(Exception e) {
            textArea.appendText(IoFields.MISSING_PARAMS_LOG);
            return;
        }

        // validate entry
        if(!isValidFields(DEPARTMENT, DATE_HIRED)) {
            return;
        }

        if(!isValidSalary(SALARY)) {
            return;
        }

        // try add
        addEmployee(new Fulltime(NAME, DEPARTMENT, DATE_HIRED, SALARY));
    }

    /**
     * handles user input from command line when adding Management employee
     */
    private void handleAddManager() {
        // get input fields
        final String NAME;
        final String DEPARTMENT;
        final Date DATE_HIRED;
        final double SALARY;
        final int MGMT_CODE;

        try {
            NAME = firstName.getText() + "," + lastName.getText();
            DEPARTMENT = departmentCode.getText();
            DATE_HIRED = new Date(hireDate.getValue().format(formatters));
            SALARY = Double.parseDouble(propertyOne.getText());
            MGMT_CODE = Integer.parseInt(propertyTwo.getText());
        }
        catch(Exception e) {
            textArea.appendText(IoFields.MISSING_PARAMS_LOG);
            return;
        }

        // validate entry
        if(!isValidFields(DEPARTMENT, DATE_HIRED, SALARY, MGMT_CODE)) {
            return;
        }

        // try add
        addEmployee(new Management(NAME, DEPARTMENT, DATE_HIRED, SALARY, MGMT_CODE));
    }

    /**
     * handles user input from command line when removing employee
     */
    private void handleRemoveEmployee() {
        final String NAME;
        final String DEPARTMENT;
        final Date DATE_HIRED;
        final Employee targetEmployee;

        try {
            NAME = firstName.getText() + "," + lastName.getText();
            DEPARTMENT = departmentCode.getText();
            DATE_HIRED = new Date(hireDate.getValue().format(formatters));
            targetEmployee = new Employee(NAME, DEPARTMENT, DATE_HIRED);
        }
        catch(Exception e) {
            textArea.appendText(IoFields.MISSING_PARAMS_LOG);
            return;
        }

        if(DBIsEmpty()) {
            return;
        }

        if(!company.remove(targetEmployee)) {
            System.out.println(IoFields.INVALID_EMPLOYEE_LOG);
            return;
        }

        textArea.appendText(IoFields.EMPLOYEE_REMOVE_SUCCESS_LOG);
    }

    /**
     * handles user input from command line when calculating payment
     */
    public void handleCalculatePayment() {
        if(DBIsEmpty()) {
            return;
        }

        company.processPayments();
        textArea.appendText(IoFields.PAYMENT_PROCESS_COMPLETE_LOG);
    }

    /**
     * handles user input from command line when setting hours for Parttime employee
     */
    private void handleSetHours() {
        if(DBIsEmpty()) {
            return;
        }

        final String NAME;
        final String DEPARTMENT;
        final Date DATE_HIRED;
        final int HOURS;
        final Employee targetEmployee;

        try {
            NAME = firstName.getText() + "," + lastName.getText();
            DEPARTMENT = departmentCode.getText();
            DATE_HIRED = new Date(hireDate.getValue().format(formatters));
            HOURS = Integer.parseInt(propertyTwo.getText());
            targetEmployee = new Parttime(NAME, DEPARTMENT, DATE_HIRED, HOURS);
        }
        catch(Exception e) {
            System.out.println(IoFields.MISSING_PARAMS_LOG);
            return;
        }

        // validate department code && hire date
        if(!isValidFields(DEPARTMENT, DATE_HIRED)) {
            return;
        }

        // validate hours
        if(!isValidHours(HOURS)){
            return;
        }

        // try set
        if(!company.setHours(targetEmployee)) {
            textArea.appendText(IoFields.INVALID_EMPLOYEE_LOG);
        }

        textArea.appendText(IoFields.SET_HOURS_SUCCESS_LOG);
    }

    /**
     * handles user input from command line when printing earnings for all employees
     */
    @FXML
    private void handlePrintAll() {
        if(DBIsEmpty()) {
            return;
        }
        textArea.appendText(IoFields.PRINT_PROMPT);
        textArea.appendText(company.print());
    }

    /**
     * handles user input from command line when printing earnings statements in order of date hired
     */
    @FXML
    private void handlePrintByHireDate() {
        if(DBIsEmpty()) {
            return;
        }
        textArea.appendText(IoFields.PRINT_BY_DATE_PROMPT);
        textArea.appendText(company.printByDate());
    }

    /**
     * handles user input from command line when printing earnings statements grouped by dept
     */
    @FXML
    private void handlePrintByDepartment() {
        if(DBIsEmpty()) {
            return;
        }
        textArea.appendText(IoFields.PRINT_BY_DEPT_PROMPT);
        textArea.appendText(company.printByDepartment());
    }

    /**
     * helper method to check if company is empty  ( when numemployees = 0 )
     * @return True if there are no records in data structure/database
     */
    private boolean DBIsEmpty() {
        if(company.isEmpty()){
            textArea.appendText(IoFields.EMPTY_DB_LOG);
            return true;
        }
        return false;
    }

    /**
     * handles run file command, reads input from src/main.payroll_processing_system/testCases.txt
     */
    public void handleRunFile() {
        File file = new File("src/main/payroll_processing_system/model/testCases.txt");

        try (Scanner sc = new Scanner(file, StandardCharsets.UTF_8.name())) {
            do {
                tokens = tokenize(sc.nextLine());
                userInput = tokens[0];
                if(!userInput.equals(Commands.QUIT)){
                    // handleUserInput();
                }
            } while(!userInput.equals(Commands.QUIT) && sc.hasNextLine());
        }
        catch (IOException e) {
            textArea.appendText(IoFields.FILE_ERROR);
        }
    }

    @FXML
    public void onAddClick() {
        if(selectedEmployeeType == parttime) {
            handleAddParttime();
        }
        else if (selectedEmployeeType == fulltime) {
            handleAddFulltime();
        }
        else if (selectedEmployeeType == management) {
            handleAddManager();
        }
    }

    @FXML
    public void onRemoveClick() {
        handleRemoveEmployee();
    }

    @FXML
    public void onSetHoursClick() {
        handleSetHours();
    }

    @FXML
    public void onCalculatePaymentClick() {
        handleCalculatePayment();
    }

    private void toggleRadioButtons(RadioButton button1, RadioButton button2) {
        button1.setDisable(!button1.isDisabled());
        button2.setDisable(!button2.isDisabled());
    }

    private void clearProperties() {
        propertyOne.setText("");
        propertyTwo.setText("");
    }

    private void setParttimeUI() {
        toggleRadioButtons(fulltime, management);
        clearProperties();

        propertyTwo.setVisible(true);
        propertyTwoLabel.setVisible(true);

        propertyOneLabel.setText(HOURLY_RATE_LABEL);
        propertyTwoLabel.setText(HOURS_WORKED_LABEL);
    }

    private void setFulltimeUI() {
        toggleRadioButtons(parttime, management);
        clearProperties();

        propertyTwo.setVisible(false);
        propertyTwoLabel.setVisible(false);

        propertyOneLabel.setText(SALARY_LABEL);

        propertyOne.setText("");
        propertyTwo.setText("");
    }

    private void setManagementUI() {
        toggleRadioButtons(parttime, fulltime);
        clearProperties();

        propertyTwo.setVisible(true);
        propertyTwoLabel.setVisible(true);

        propertyOneLabel.setText(SALARY_LABEL);
        propertyTwoLabel.setText(MANAGEMENT_CODE_LABEL);

        propertyOne.setText("");
        propertyTwo.setText("");
    }

    @FXML
    public void onRadioButtonClick(ActionEvent e) {
        final Node source = (Node) e.getSource();
        selectedEmployeeType = source;

        if(selectedEmployeeType == parttime) {
            setParttimeUI();
        }
        else if(selectedEmployeeType == fulltime) {
            setFulltimeUI();
        }
        else if(selectedEmployeeType == management) {
            setManagementUI();
        }
    }
}
