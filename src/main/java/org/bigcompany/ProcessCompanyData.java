package org.bigcompany;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessCompanyData {
    public Map<Long, List<Employee>> employeesGroupedByManagerId;
    public Map<Long, Employee> employeeMap;
    public List<Employee> employeeList;
    public Map<Employee, BigDecimal> getManagerThatEarnLess() {
        Map<Employee, BigDecimal> mangerThatEarnLess = new HashMap<>();
        employeesGroupedByManagerId.forEach((managerId, employeeList) -> {
            if(managerId != null) {
                BigDecimal reoprteesAverageSalary = calculateAverageSalary(employeeList);
                Employee manager = employeeMap.get(managerId);
                BigDecimal managerSalary = manager.getSalary();
                BigDecimal twentyPercentOfAverage = reoprteesAverageSalary.multiply(new BigDecimal("0.20"));
                BigDecimal twentyPercentMoreThanAverageSalary = reoprteesAverageSalary.add(twentyPercentOfAverage);
                if (managerSalary.compareTo(twentyPercentMoreThanAverageSalary) < 0) {
                    BigDecimal difference = twentyPercentMoreThanAverageSalary.subtract(managerSalary);
                    mangerThatEarnLess.put(manager, difference);
                }
            }
        });
        return mangerThatEarnLess;
    }

    public Map<Employee, BigDecimal> getManagerThatEarnMore() {
        Map<Employee, BigDecimal> mangerThatEarnMore = new HashMap<>();
        employeesGroupedByManagerId.forEach((managerId, employeeList)-> {
            if (managerId != null) {
                BigDecimal reoprteesAverageSalary = calculateAverageSalary(employeeList);
                Employee manager = employeeMap.get(managerId);
                BigDecimal managerSalary = manager.getSalary();
                BigDecimal fiftyPercentOfAverage = reoprteesAverageSalary.multiply(new BigDecimal("0.50"));
                BigDecimal fiftyPercentMoreThanAverageSalary = reoprteesAverageSalary.add(fiftyPercentOfAverage);
                if (managerSalary.compareTo(fiftyPercentMoreThanAverageSalary) > 0) {
                    BigDecimal difference = managerSalary.subtract(fiftyPercentMoreThanAverageSalary);
                    mangerThatEarnMore.put(manager, difference);
                }
            }
        });
        return mangerThatEarnMore;
    }

    private BigDecimal calculateAverageSalary(List<Employee> employeeList) {
        BigDecimal totalSalary = BigDecimal.ZERO;
        for (Employee employee: employeeList) {
            totalSalary = totalSalary.add(employee.getSalary());
        }
        return totalSalary.divide(BigDecimal.valueOf(employeeList.size()), 2, RoundingMode.HALF_UP);
    }

    public Map<Employee, Integer> getEmployeeThatHaveReportingLineGreaterThanFourBetweenThemAndCEO() {
        Map<Employee, Integer> employeeMapWithReportingLineGreaterThanFourBetweenThemAndCEO = new HashMap<>();
        for (Employee employee: employeeList) {
            int length = 0;
            Long managerId = employee.getManagerId();
            while (managerId != null) {
                length++;
                Employee manager = employeeMap.get(managerId);
                if(manager == null) break;
                managerId = manager.getManagerId();
            }
            if (length > 5) {
                employeeMapWithReportingLineGreaterThanFourBetweenThemAndCEO.put(employee, length - 5);
            }
        }
        return employeeMapWithReportingLineGreaterThanFourBetweenThemAndCEO;
    }

    private Map<Long, List<Employee>> groupEmployeesByManagerId(List<Employee> employeeList) {
        Map<Long, List<Employee>> employeeMap = new HashMap<>();
        for (Employee employee: employeeList) {
            Long mangerId = employee.getManagerId();
            if (!employeeMap.containsKey(mangerId)) {
               employeeMap.put(mangerId, new ArrayList<>(List.of(employee)));
            } else {
               List<Employee> employees =  employeeMap.get(mangerId);
               employees.add(employee);
               employeeMap.put(mangerId, employees);
            }
        }
        return employeeMap;
    }

    private Map<Long, Employee> createEmployeeMap(List<Employee> employeeList) {
        Map<Long, Employee> salaryMap = new HashMap<>();
        for (Employee employee: employeeList) {
            salaryMap.put(employee.getId(), employee);
        }
        return salaryMap;
    }

    public void fetchAndPreProcessCompanyData() {
        ReadData readData = new ReadData();
        employeeList = readData.fetchCompanyData();
        employeesGroupedByManagerId = groupEmployeesByManagerId(employeeList);
        employeeMap = createEmployeeMap(employeeList);
    }

    public static void main(String[] args) {
        ProcessCompanyData processCompanyData = new ProcessCompanyData();
        processCompanyData.fetchAndPreProcessCompanyData();
        Map<Employee, BigDecimal> managersEarningLess = processCompanyData.getManagerThatEarnLess();
        managersEarningLess.forEach((employee, difference) ->
                System.out.println("Manager with employeeId " +employee.getId() +
                        " and first name "+ employee.getFirstName() +
                        " and last name "+ employee.getLastName()+
                        " earns a salary which is "+difference+
                        " less than what they should."));
        Map<Employee, BigDecimal> managersEarningMore = processCompanyData.getManagerThatEarnMore();
        managersEarningMore.forEach((employee, difference) ->
                System.out.println("Manager with employeeId " +employee.getId() +
                        " and first name "+ employee.getFirstName() +
                        " and last name "+ employee.getLastName() +
                        " earns a salary that is "+ difference +
                        " more than what they should."));
        Map<Employee, Integer> employeesWithReportingLineGreaterThanFour = processCompanyData.getEmployeeThatHaveReportingLineGreaterThanFourBetweenThemAndCEO();
        employeesWithReportingLineGreaterThanFour.forEach((employee, difference) ->
                System.out.println("Employee with employeeId " +employee.getId() +
                        " and first name "+ employee.getFirstName() +
                        " and last name "+ employee.getLastName() +
                        " has "+ difference +
                        " more than 4 reporting manager between them and the CEO."));
    }
}