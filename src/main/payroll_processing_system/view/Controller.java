package payroll_processing_system.view;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import payroll_processing_system.application.*;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Controller {
    private Company company;
    private String userInput;
    private String[] tokens;
    private Node selectedEmployeeType;
    private int selectedManagerCode;
    private DateTimeFormatter formatters;
    private final String SALARY_LABEL = "Yearly Salary";
    private final String HOURLY_RATE_LABEL = "Hourly Rate";
    private final String HOURS_WORKED_LABEL = "Hours Worked";
    private final String MANAGEMENT_CODE_LABEL = "Manager Code";

    @FXML // fx:id="setHoursButton"
    private Button setHoursButton; // Value injected by FXMLLoader

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


    @FXML // fx:id="propertyTwoLabel"
    private Text propertyTwoLabel; // Value injected by FXMLLoader

    @FXML // fx:id="CS"
    private RadioButton CS; // Value injected by FXMLLoader

    @FXML // fx:id="ECE"
    private RadioButton ECE; // Value injected by FXMLLoader

    @FXML // fx:id="IT"
    private RadioButton IT; // Value injected by FXMLLoader

    @FXML // fx:id="manager"
    private RadioButton manager; // Value injected by FXMLLoader

    @FXML // fx:id="departmentHead"
    private RadioButton departmentHead; // Value injected by FXMLLoader

    @FXML // fx:id="director"
    private RadioButton director; // Value injected by FXMLLoader

    @FXML // fx:id="propertyOneGroup"
    private Group propertyOneGroup; // Value injected by FXMLLoader

    @FXML // fx:id="propertyTwoGroup"
    private Group propertyTwoGroup; // Value injected by FXMLLoader

    @FXML // fx:id="propertyTwoTextField"
    private TextField propertyTwoTextField; // Value injected by FXMLLoader

    @FXML // fx:id="propertyTwoRadioGroup"
    private Group propertyTwoRadioGroup; // Value injected by FXMLLoader

    private String selectedDepartmentCode;

    /**
     * default constructor for Controller, creates a new company and initializes formatters
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
        return input.split(",");
    }

    /* -------------- Helper Methods -------------- */

    /**
     * helper method to determine if given Date object is valid
     *
     * @param date Date object to be evaluated
     * @return true if date is valid, false otherwise
     */
    private boolean isValidDate(Date date) {
        if (!date.isValid()) {
            printToTextArea(String.format(IoFields.INVALID_DATE_LOG, date));
            return false;
        }

        return true;
    }

    /**
     * helper method to determine if given NAme is valid
     * @param n name to be evaluated
     * @return false if name field blank in GUI, true otherwise
     */
    private boolean isValidName(String n){
        if (n == null || n.equals(","))
            return false;
        else
            return true;
    }

    /**
     * helper method to determine if department code is valid
     *
     * @param code deptcode to be evaluated
     * @return true if code is valid, false otherwise
     */
    private boolean isValidDeptCode(String code) {
        for (String departmentCode : Company.DEPARTMENT_CODES) {
            if (departmentCode.equals(code)) {
                return true;
            }
        }
        printToTextArea(String.format(IoFields.INVALID_DEPARTMENT_CODE_LOG, code));
        return false;
    }

    /**
     * helper method to determine if hourlyRate is valid
     *
     * @param rate rate to be evaluated
     * @return true if rate is greater than 0, false otherwise
     */
    private boolean isValidHourlyRate(double rate) {
        final int HOURLY_RATE_LOWER_BOUND = 0;

        if (rate < HOURLY_RATE_LOWER_BOUND) {
            printToTextArea(IoFields.INVALID_PAY_RATE_LOG);
            return false;
        }

        return true;
    }

    /**
     * helper method to determine if salary is valid
     *
     * @param salary salary to be evaluated
     * @return true if salary is a double greater than 0, false otherwise
     */
    private boolean isValidSalary(double salary) {
        final int SALARY_LOWER_BOUND = 0;

        if (salary < SALARY_LOWER_BOUND) {
            printToTextArea(IoFields.INVALID_SALARY_LOG);
            return false;
        }
        return true;
    }

    /**
     * helper method to determine if management code is valid
     *
     * @param code code to be evaluated
     * @return true if code is integer between 1 and 3, false otherwise
     */
    private boolean isValidMgmtCode(int code) {
        for (int managerCode : Company.MANAGER_CODES) {
            if (code == managerCode) {
                return true;
            }
        }

        printToTextArea(IoFields.INVALID_MANAGER_CODE_LOG);
        return false;
    }

    /**
     * helper method to determine if hours are valid
     *
     * @param hours num of hours to be evaluated
     * @return true if hours is int between 0 and 100 inclusive, false otherwise
     */
    private boolean isValidHours(int hours) {

        if (hours < Company.HOURS_LOWER_BOUND) {
            printToTextArea(IoFields.SET_NEGATIVE_HOURS_FAILURE_LOG);
            return false;
        }

        if (hours > Company.HOURS_UPPER_BOUND) {
            printToTextArea(IoFields.SET_OVER_ONE_HUNDRED_HOURS_FAILURE_LOG);
            return false;
        }

        return true;
    }

    /**
     * helper method to determine if various fields are valid
     *
     * @param deptCode department code to be evaluated
     * @param date     date object to be evaluated
     * @param salary   salary to be evaluated
     * @param mgmtCode management code to be evaluated
     * @return true if all are valid, false otherwise
     */
    private boolean isValidFields(String deptCode, Date date, double salary, int mgmtCode) {
        return isValidDeptCode(deptCode) && isValidDate(date) && isValidSalary(salary) && isValidMgmtCode(mgmtCode);
    }

    /**
     * helper method to determine if various fields are valid
     *
     * @param deptCode department code to be evaluated
     * @param date     date object to be evaluated
     * @return true if all are valid, false otherwise
     */
    private boolean isValidFields(String deptCode, Date date) {
        return isValidDeptCode(deptCode) && isValidDate(date);
    }

    /**
     * helper method for adding any employee, if true prints success log, if false prints fail log
     *
     * @param employee Employee object to be added
     */
    private void addEmployee(Employee employee) {
        if (!company.add(employee)) {
            printToTextArea(IoFields.EMPLOYEE_ADD_FAILURE_LOG);
            return;
        }

        printToTextArea(IoFields.EMPLOYEE_ADD_SUCCESS_LOG);
    }

    /**
     * processes user input when adding a Parttime employee
     */
    private void handleAddParttime() {
        // get input fields
        final String NAME;
        final String DEPARTMENT;
        final Date DATE_HIRED;
        final double RATE;

        try {
            NAME = firstName.getText() + "," + lastName.getText();
            DEPARTMENT = selectedDepartmentCode;
            DATE_HIRED = new Date(hireDate.getValue().format(formatters));
            RATE = Double.parseDouble(propertyOne.getText());
        } catch (Exception e) {
            printToTextArea(IoFields.MISSING_PARAMS_LOG);
            return;
        }

        // validate entry
        if (!isValidFields(DEPARTMENT, DATE_HIRED)) {
            return;
        }

        if (!isValidHourlyRate(RATE)) {
            return;
        }

        if(!isValidName(NAME)){
            printToTextArea(IoFields.NULL_NAME);
            return;
        }

        // try add
        addEmployee(new Parttime(NAME, DEPARTMENT, DATE_HIRED, RATE));
    }

    /**
     * processes input when importing from file that tries to add Parttime employee
     */
    private void handleAddParttimeFile() {
        // get input fields
        final String NAME;
        final String DEPARTMENT;
        final Date DATE_HIRED;
        final double RATE;

        try {
            NAME = tokens[1];
            DEPARTMENT = tokens[2];
            DATE_HIRED = new Date(tokens[3]);
            RATE = Double.parseDouble(tokens[4]);
        } catch (Exception e) {
            System.out.println(IoFields.MISSING_PARAMS_LOG);
            return;
        }

        // validate entry
        if (!isValidFields(DEPARTMENT, DATE_HIRED)) {
            return;
        }

        if (!isValidHourlyRate(RATE)) {
            return;
        }

        // try add
        addEmployee(new Parttime(NAME, DEPARTMENT, DATE_HIRED, RATE));
    }


    /**
     * handles user input when adding Fulltime employee
     */
    private void handleAddFulltime() {
        // get input fields
        final String NAME;
        final String DEPARTMENT;
        final Date DATE_HIRED;
        final double SALARY;

        try {
            NAME = firstName.getText() + "," + lastName.getText();
            DEPARTMENT = selectedDepartmentCode;
            DATE_HIRED = new Date(hireDate.getValue().format(formatters));
            SALARY = Double.parseDouble(propertyOne.getText());

        } catch (Exception e) {
            printToTextArea(IoFields.MISSING_PARAMS_LOG);
            return;
        }

        // validate entry
        if (!isValidFields(DEPARTMENT, DATE_HIRED)) {
            return;
        }

        if (!isValidSalary(SALARY)) {
            return;
        }

        // try add
        addEmployee(new Fulltime(NAME, DEPARTMENT, DATE_HIRED, SALARY));
    }

    /**
     *   processes input when importing from file that tries to add Fulltime employee
     */
    private void handleAddFulltimeFile() {
// get input fields
        final String NAME;
        final String DEPARTMENT;
        final Date DATE_HIRED;
        final double SALARY;

        try {
            NAME = tokens[1];
            DEPARTMENT = tokens[2];
            DATE_HIRED = new Date(tokens[3]);
            SALARY = Double.parseDouble(tokens[4]);
        } catch (Exception e) {
            System.out.println(IoFields.MISSING_PARAMS_LOG);
            return;
        }

        // validate entry
        if (!isValidFields(DEPARTMENT, DATE_HIRED)) {
            return;
        }

        if (!isValidSalary(SALARY)) {
            return;
        }

        // try add
        addEmployee(new Fulltime(NAME, DEPARTMENT, DATE_HIRED, SALARY));
    }

    /**
     * handles user input when adding Management employee
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
            DEPARTMENT = selectedDepartmentCode;
            DATE_HIRED = new Date(hireDate.getValue().format(formatters));
            SALARY = Double.parseDouble(propertyOne.getText());
            MGMT_CODE = selectedManagerCode;
        } catch (Exception e) {
            printToTextArea(IoFields.MISSING_PARAMS_LOG);
            return;
        }

        // validate entry
        if (!isValidFields(DEPARTMENT, DATE_HIRED, SALARY, MGMT_CODE)) {
            return;
        }

        // try add
        addEmployee(new Management(NAME, DEPARTMENT, DATE_HIRED, SALARY, MGMT_CODE));
    }

    /**
     * processes input when importing from file that tries to add Management employee
     */
    private void handleAddManagerFile() {
        // get input fields
        final String NAME;
        final String DEPARTMENT;
        final Date DATE_HIRED;
        final double SALARY;
        final int MGMT_CODE;

        try {
            NAME = tokens[1];
            DEPARTMENT = tokens[2];
            DATE_HIRED = new Date(tokens[3]);
            SALARY = Double.parseDouble(tokens[4]);
            MGMT_CODE = Integer.parseInt(tokens[5]);
        } catch (Exception e) {
            System.out.println(IoFields.MISSING_PARAMS_LOG);
            return;
        }

        // validate entry
        if (!isValidFields(DEPARTMENT, DATE_HIRED, SALARY, MGMT_CODE)) {
            return;
        }

        // try add
        addEmployee(new Management(NAME, DEPARTMENT, DATE_HIRED, SALARY, MGMT_CODE));
    }

    /**
     * handles user input  when removing employee
     */
    private void handleRemoveEmployee() {
        final String NAME;
        final String DEPARTMENT;
        final Date DATE_HIRED;
        final Employee targetEmployee;

        try {
            NAME = firstName.getText() + "," + lastName.getText();
            DEPARTMENT = selectedDepartmentCode;
            DATE_HIRED = new Date(hireDate.getValue().format(formatters));
            targetEmployee = new Employee(NAME, DEPARTMENT, DATE_HIRED);
        } catch (Exception e) {
            printToTextArea(IoFields.MISSING_PARAMS_LOG);
            return;
        }

        if (DBIsEmpty()) {
            return;
        }

        if (!company.remove(targetEmployee)) {
            printToTextArea(IoFields.INVALID_EMPLOYEE_LOG);
            return;
        }

        printToTextArea(IoFields.EMPLOYEE_REMOVE_SUCCESS_LOG);
    }

    /**
     * handles user input when calculating payment
     */
    public void handleCalculatePayment() {
        if (DBIsEmpty()) {
            return;
        }

        company.processPayments();
        printToTextArea(IoFields.PAYMENT_PROCESS_COMPLETE_LOG);
    }

    /**
     * handles user input  when setting hours for Parttime employee
     */
    private void handleSetHours() {
        if (DBIsEmpty()) {
            return;
        }

        final String NAME;
        final String DEPARTMENT;
        final Date DATE_HIRED;
        final int HOURS;
        final Employee targetEmployee;

        try {
            NAME = firstName.getText() + "," + lastName.getText();
            DEPARTMENT = selectedDepartmentCode;
            DATE_HIRED = new Date(hireDate.getValue().format(formatters));
            HOURS = Integer.valueOf(propertyTwoTextField.getText());
            targetEmployee = new Parttime(NAME, DEPARTMENT, DATE_HIRED, HOURS);
        } catch (Exception e) {
            printToTextArea(IoFields.MISSING_PARAMS_LOG);
            return;
        }

        // validate department code && hire date
        if (!isValidFields(DEPARTMENT, DATE_HIRED)) {
            return;
        }

        // validate hours
        if (!isValidHours(HOURS)) {
            return;
        }

        // try set
        if (!company.setHours(targetEmployee)) {
            printToTextArea(IoFields.INVALID_EMPLOYEE_LOG);
            return;
        }

        printToTextArea(IoFields.SET_HOURS_SUCCESS_LOG);
    }

    /**
     * handles user input  when printing earnings for all employees
     */
    @FXML
    private void handlePrintAll() {
        if (DBIsEmpty()) {
            return;
        }
        printToTextArea(IoFields.PRINT_PROMPT);
        printToTextArea(company.print());
    }

    /**
     * handles user input  when printing earnings statements in order of date hired
     */
    @FXML
    private void handlePrintByHireDate() {
        if (DBIsEmpty()) {
            return;
        }
        printToTextArea(IoFields.PRINT_BY_DATE_PROMPT);
        printToTextArea(company.printByDate());
    }

    /**
     * handles user input when printing earnings statements grouped by dept
     */
    @FXML
    private void handlePrintByDepartment() {
        if (DBIsEmpty()) {
            return;
        }
        printToTextArea(IoFields.PRINT_BY_DEPT_PROMPT);
        printToTextArea(company.printByDepartment());
    }

    /**
     * helper method to check if company is empty  ( when numemployees = 0 )
     *
     * @return True if there are no records in data structure/database
     */
    private boolean DBIsEmpty() {
        if (company.isEmpty()) {
            printToTextArea(IoFields.EMPTY_DB_LOG);
            return true;
        }
        return false;
    }

    /**
     * helper method to export employee database
     */
    private void handleExport() {

        printToTextArea(company.exportDatabase());

    }

    /**
     * method to handle importing an employee database from file
     */
    private void handleImport() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Source File for the Import");
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        Stage stage = new Stage();
        File sourceFile = chooser.showOpenDialog(stage); //get the reference of the source file
        if (sourceFile == null){
            printToTextArea(IoFields.NULL_FILE_LOG);
            return;}
        //write code to read from the file
        try (Scanner sc = new Scanner(sourceFile, StandardCharsets.UTF_8.name())) {
            do {
                tokens = tokenize(sc.nextLine());
                userInput = tokens[0];
                if (userInput.equals("P")) {
                    handleAddParttimeFile();
                } else if (userInput.equals("F")) {
                    handleAddFulltimeFile();
                } else if (userInput.equals("M")) {
                    handleAddManagerFile();
                }
                else {
                    printToTextArea(IoFields.INCORRECT_IMPORT_FORMAT);
                }
            } while (sc.hasNextLine());


        } catch (IOException e) {
            printToTextArea(IoFields.FILE_ERROR);
        }

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
                if (!userInput.equals(Commands.QUIT)) {
                    // handleUserInput();
                }
            } while (!userInput.equals(Commands.QUIT) && sc.hasNextLine());
        } catch (IOException e) {
            printToTextArea(IoFields.FILE_ERROR);
        }
    }

    @FXML
    /**
     * defines action when add employee button is clicked
     */
    public void onAddClick() {
        if (selectedEmployeeType == parttime) {
            handleAddParttime();
        } else if (selectedEmployeeType == fulltime) {
            handleAddFulltime();
        } else if (selectedEmployeeType == management) {
            handleAddManager();
        } else {
            printToTextArea(IoFields.MISSING_PARAMS_LOG);
        }
    }

    @FXML
    /**
     * defines action when remove button is clicked
     */
    public void onRemoveClick() {
        handleRemoveEmployee();
    }

    @FXML
    /**
     * defines action when set hours button is clicked
     */
    public void onSetHoursClick() {
        handleSetHours();
    }

    @FXML
    /**
     * defines action when calculate payment button is clicked
     */
    public void onCalculatePaymentClick() {
        handleCalculatePayment();
    }

    @FXML
    /**
     * defines action when export button is clicked
     */
    public void exportClick() {
        handleExport();

    }

    @FXML
    /**
     * defines action when import button is clicked
     */
    public void importClick() {
        handleImport();
    }

//    private void toggleRadioButtons(RadioButton button1, RadioButton button2) {
//        button1.setDisable(!button1.isDisabled());
//        button2.setDisable(!button2.isDisabled());
//    }

    /**
     * clears forms for propertyone and propertyTwoTextField
     */
    private void clearProperties() {
        propertyOne.setText("");
        propertyTwoTextField.setText("");
    }

    /**
     * creates the UI and respective textfields when parttime is selected
     */
    private void setParttimeUI() {
        // disable other radio buttons
        // toggleRadioButtons(fulltime, management);

        // clear forms
        clearProperties();

        // set labels
        propertyOneLabel.setText(HOURLY_RATE_LABEL);
        propertyTwoLabel.setText(HOURS_WORKED_LABEL);

        // show propertyOne
        propertyOneGroup.setVisible(true);

        // hide propertyTwoRadioGroup
        propertyTwoRadioGroup.setVisible(false);

        // show propertyTwo
        propertyTwoGroup.setVisible(true);

        // show propertyTwoLabel
        propertyTwoLabel.setVisible(true);

        // show propertyTwoTextField
        propertyTwoTextField.setVisible(true);

        // enable setHours button
        setHoursButton.setDisable(false);
    }

    /**
     * creates the UI and respective textfields when fulltime is selected
     */
    private void setFulltimeUI() {
        // disable other radio buttons
        //toggleRadioButtons(parttime, management);

        // disable setHours button
        setHoursButton.setDisable(true);

        // clear forms
        clearProperties();

        // set labels
        propertyOneLabel.setText(SALARY_LABEL);

        // show propertyOne
        propertyOneGroup.setVisible(true);

        // hide propertyTwo
        propertyTwoGroup.setVisible(false);
    }

    /**
     * creates the UI and respective textfields when management is selected
     */
    private void setManagementUI() {
        // disable other radio buttons
        //toggleRadioButtons(parttime, fulltime);

        // disable setHours button
        setHoursButton.setDisable(true);

        // clear forms
        clearProperties();

        // clear radio selections
        manager.setSelected(false);
        departmentHead.setSelected(false);
        director.setSelected(false);

        // set labels
        propertyOneLabel.setText(SALARY_LABEL);
        propertyTwoLabel.setText(MANAGEMENT_CODE_LABEL);

        // show propertyOne
        propertyOneGroup.setVisible(true);

        // hide propertyTwoTextArea
        propertyTwoTextField.setVisible(false);

        // show propertyTwoRadioGroup
        propertyTwoRadioGroup.setVisible(true);

        // show propertyTwoLabel
        propertyTwoLabel.setVisible(true);

        // show propertyTwo
        propertyTwoGroup.setVisible(true);
    }

    @FXML
    /**
     * defines action when department code is selected
     */
    public void handleDepartmentCode(ActionEvent e) {
        Node selectedOption = (Node) e.getSource();

        if (selectedOption == CS) {
            // toggleRadioButtons(ECE, IT);
            selectedDepartmentCode = CS.getId();
        } else if (selectedOption == ECE) {
            // toggleRadioButtons(CS, IT);
            selectedDepartmentCode = ECE.getId();
        } else if (selectedOption == IT) {
            //toggleRadioButtons(CS, ECE);
            selectedDepartmentCode = IT.getId();
        }
    }

    @FXML
    /**
     * defines action when manager code is selected
     */
    public void handleManagerCode(ActionEvent e) {
        Node selectedOption = (Node) e.getSource();

        if (selectedOption == manager) {
            // toggleRadioButtons(departmentHead, director);
            selectedManagerCode = 1;
        } else if (selectedOption == departmentHead) {
            // toggleRadioButtons(manager, director);
            selectedManagerCode = 2;
        } else if (selectedOption == director) {
            // toggleRadioButtons(manager, departmentHead);
            selectedManagerCode = 3;
        }

    }

    @FXML
    /**
     * defines action when employee type is selected
     */
    public void handleEmployeeType(ActionEvent e) {
        selectedEmployeeType = (Node) e.getSource();



        if (selectedEmployeeType == parttime) {
            setParttimeUI();
        } else if (selectedEmployeeType == fulltime) {
            setFulltimeUI();
        } else if (selectedEmployeeType == management) {
            setManagementUI();
        }
    }

    /**
     * outputs text to textarea
     * @param text to be displayed on textarea
     */
    public void printToTextArea(String text) {
        printNewLine(text);
    }

    /**
     * outputs a text to textarea with newline
     * @param text to be displayed on textarea
     */
    public void printNewLine(String text) {
        textArea.appendText(text + "\n");
    }
}
