package org.bigcompany;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;
public class ReadData {
    public List<Employee> fetchCompanyData() {
        List<Employee> companyData = new ArrayList<>();
        try {
            InputStream inputStream = ReadData.class.getClassLoader().getResourceAsStream("data.csv");
            if (inputStream == null) {
                System.out.println("Company data file not available");
                return companyData;
            }
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            CSVReader csvReader = new CSVReader(inputStreamReader);
            csvReader.readNext();
            String[] row;
            while ((row = csvReader.readNext()) != null) {
                Long id = Long.parseLong(row[0]);
                String firstName = row[1];
                String lastName = row[2];
                BigDecimal salary = new BigDecimal(row[3]);
                Long managerId = null;
                if (row.length > 4 && !row[4].isEmpty()) {
                   managerId = Long.parseLong(row[4]);
                }
                companyData.add(new Employee(id, firstName, lastName, salary, managerId));
            }
            csvReader.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return companyData;
    }
}
