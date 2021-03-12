package payroll_processing_system.application;

/**
 * IoFields class contains String messages that are used to display info about user commands
 * that are handled by the PayrollProcessing class
 *
 * @author Hugo De Moraes, Jonathan Dong
 */
public final class IoFields {
    public static final String INVALID_MANAGER_CODE_LOG = "invalid management code.";
    public static final String INVALID_DATE_LOG = "%s is not a valid Date!";
    public static final String INVALID_DEPARTMENT_CODE_LOG = "'%s' is not a valid department code.";
    public static final String INVALID_PAY_RATE_LOG = "Pay rate cannot be negative.";
    public static final String INVALID_SALARY_LOG = "Salary cannot be negative.";
    public static final String EMPLOYEE_ADD_SUCCESS_LOG = "Employee Added.";
    public static final String EMPLOYEE_ADD_FAILURE_LOG = "Employee is already in the list.";
    public static final String EMPLOYEE_REMOVE_SUCCESS_LOG = "Employee removed";
    public static final String INVALID_EMPLOYEE_LOG = "Employee does not exist.";
    public static final String EMPTY_DB_LOG = "Employee database is empty.";
    public static final String PAYMENT_PROCESS_COMPLETE_LOG = "Calculation of employee payments is done.";
    public static final String SET_HOURS_SUCCESS_LOG = "Working hours set.";
    public static final String SET_NEGATIVE_HOURS_FAILURE_LOG = "Working hours cannot be negative";
    public static final String SET_OVER_ONE_HUNDRED_HOURS_FAILURE_LOG = "Invalid Hours: over 100.";
    public static final String EMPLOYEE_STRING = "%s::%s::%s::Payment ";
    public static final String PARTTIME_EMPLOYEE_STRING = "$%s::PART TIME::Hourly Rate $%s::Hours worked this period: %s ";
    public static final String FULLTIME_EMPLOYEE_STRING = "$%s::FULL TIME::Annual Salary $%s";
    public static final String MANAGER_STRING = "::%s Compensation $%s";
    public static final String PRINT_PROMPT = "--Printing earning statements for all employees--";
    public static final String PRINT_BY_DEPT_PROMPT = "--Printing earning statements by department--";
    public static final String PRINT_BY_DATE_PROMPT = "--Printing earning statements by date hired--";
    public static final String MISSING_PARAMS_LOG = "Error: missing parameters for this command";
    public static final String FILE_ERROR = "Error: testCases.txt not found!";
    public static final String NULL_FILE_LOG = "Please select a file to import/export";
    public static final String EXPORT_SUCCESS_LOG = "Data has been succesfully exported";
    public static final String NULL_NAME = "Please enter a name";
    public static final String INCORRECT_IMPORT_FORMAT = "Import file incorrectly formatted";
}
