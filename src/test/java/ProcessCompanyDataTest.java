import org.bigcompany.Employee;
import org.bigcompany.ProcessCompanyData;
import org.bigcompany.ReadData;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProcessCompanyDataTest {

    @Test
    void testProcessCompanyDataMain() {
        ReadData mockReadData = mock(ReadData.class);
        List<Employee> employeeList = List.of(new Employee(550L,"Noah","Harrison", new BigDecimal(2000), 500L),
                new Employee(123L,"John","Doe", new BigDecimal(60000), null),
                new Employee(124L,"Martin","Chekov", new BigDecimal(45000), 123L),
                new Employee(125L,"Bob","Ronstad", new BigDecimal(47000), 123L),
                new Employee(300L,"Alice","Hasacat", new BigDecimal(50000), 124L),
                new Employee(305L,"Brett","Hardleaf", new BigDecimal(34000), 300L),
                new Employee(306L,"John","Good", new BigDecimal(14000), 305L),
                new Employee(440L,"Matt","Halsted", new BigDecimal(8000), 306L),
                new Employee(500L,"Scott","Dawson", new BigDecimal(5000), 400L),
                new Employee(556L,"Prince","Best", new BigDecimal(8000), 500L));
        when(mockReadData.fetchCompanyData()).thenReturn(employeeList);
        ProcessCompanyData processCompanyData = new ProcessCompanyData();
        processCompanyData.fetchAndPreProcessCompanyData();
        Map<Employee, BigDecimal> managersThatEarnLess = processCompanyData.getManagerThatEarnLess();
        Map<Employee, BigDecimal> managersThatEarnMore = processCompanyData.getManagerThatEarnMore();
        Map<Employee, Integer> employeesWithLongReportingLine = processCompanyData.getEmployeeThatHaveReportingLineGreaterThanFourBetweenThemAndCEO();
        assertEquals(2,managersThatEarnLess.size());
        assertEquals(3,managersThatEarnMore.size());
        assertEquals(3,employeesWithLongReportingLine.size());
    }

}
