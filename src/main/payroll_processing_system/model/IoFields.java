package payroll_processing_system.model;

/**
 * IoFields class contains String messages that are used to display info about user commands
 * that are handled by the PayrollProcessing class
 *
 * @author Hugo De Moraes, Jonathan Dong
 */
public final class IoFields {
    public static final String INVALID_MANAGER_CODE_LOG = "invalid management code.\n";
    public static final String INVALID_DATE_LOG = "%s is not a valid Date!\n";
    public static final String INVALID_DEPARTMENT_CODE_LOG = "'%s' is not a valid department code.\n";
    public static final String INVALID_PAY_RATE_LOG = "Pay rate cannot be negative.\n";
    public static final String INVALID_SALARY_LOG = "Salary cannot be negative.\n";
    public static final String EMPLOYEE_ADD_SUCCESS_LOG = "Employee Added.\n";
    public static final String EMPLOYEE_ADD_FAILURE_LOG = "Employee is already in the list.\n";
    public static final String EMPLOYEE_REMOVE_SUCCESS_LOG = "Employee removed\n";
    public static final String INVALID_EMPLOYEE_LOG = "Employee does not exist.\n";
    public static final String EMPTY_DB_LOG = "Employee database is empty.\n";
    public static final String PAYMENT_PROCESS_COMPLETE_LOG = "Calculation of employee payments is done.\n";
    public static final String SET_HOURS_SUCCESS_LOG = "Working hours set.\n";
    public static final String SET_NEGATIVE_HOURS_FAILURE_LOG = "Working hours cannot be negative\n";
    public static final String SET_OVER_ONE_HUNDRED_HOURS_FAILURE_LOG = "Invalid Hours: over 100.\n";
    public static final String EMPLOYEE_STRING = "%s::%s::%s::Payment ";
    public static final String PARTTIME_EMPLOYEE_STRING = "$%s::PART TIME::Hourly Rate $%s::Hours worked this period: %s";
    public static final String FULLTIME_EMPLOYEE_STRING = "$%s::FULL TIME::Annual Salary $%s";
    public static final String MANAGER_STRING = "::%s Compensation $%s";
    public static final String PRINT_PROMPT = "--Printing earning statements for all employees--\n";
    public static final String PRINT_BY_DEPT_PROMPT = "\n--Printing earning statements by department--\n";
    public static final String PRINT_BY_DATE_PROMPT = "\n--Printing earning statements by date hired--\n";
    public static final String MISSING_PARAMS_LOG = "Error: missing parameters for this command\n";
    public static final String FILE_ERROR = "Error: testCases.txt not found!\n";
}
