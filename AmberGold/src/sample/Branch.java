package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.stream.Collectors;


public class Branch {
    private String name;
    private String address;
    private ArrayList<Client> clients;
    private double cashInBranch;
    private ArrayList<Employee> employees;
    private Manager manager;
    private ArrayList<Transaction> transactions;
    private String managerId;

    public Branch(String name, String address) {
        this.name = name;
        this.address = address;
        this.clients = new ArrayList<>();
        this.cashInBranch = 0;
        this.manager = null;
        this.employees = new ArrayList<>();
        this.transactions = new ArrayList<>();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    double getCashInBranch() {
        return cashInBranch;
    }

    ArrayList<Employee> getEmployees() {
        return employees;
    }

    Manager getManager() {
        return manager;
    }

    void setManager(Manager manager) {
        this.manager = manager;
        if (manager != null)
            this.managerId = manager.getId();
    }

    public String getManagerId() {
        return managerId;
    }

    public String getAddress() {
        return address;
    }

    void setAddress(String address) {
        this.address = address;
    }

    ArrayList<Client> getClients() {
        return clients;
    }

    void setCashInBranch(double cashInBranch) {
        this.cashInBranch = cashInBranch;
    }

    ArrayList<Transaction> getTransactions() {
        return transactions;
    }

//    ******************************** FIND **********************

    Employee findEmployeeByName(String findName) {
        for (Employee employee : employees) {
            if (employee.getName().equals(findName)) {
                System.out.println(findName + " found");
                return employee;
            }
        }
        System.out.println(findName + " not found");
        return null;
    }

    Employee findEmployeeById(String findId) {
        for (Employee employee : employees) {
            if (employee.getId().equals(findId)) {
                System.out.println(employee.getName() + " found");
                return employee;
            }
        }
        System.out.println(findId + " not found");
        return null;
    }

    Client findClientBySsNumber(int findClient) {
        for (Client client : clients) {
            if (client.getSocialSecurityNumber() == (findClient)) {
                System.out.println(client.getLastName() + " found");
                return client;
            }
        }
        System.out.println("Client not found");
        return null;
    }

    ObservableList<String> employeesInBranch() {
        ObservableList<String> employeesList = FXCollections.observableArrayList();
        employeesList.addAll(employees.stream().map(Employee::getName).collect(Collectors.toList()));
        return employeesList;
    }

    ObservableList<Employee> employeesInBranchList() {
        ObservableList<Employee> employeesList = FXCollections.observableArrayList();
        employeesList.addAll(employees);
        return employeesList;
    }

    ObservableList<Integer> clientsListString() {
        ObservableList<Integer> clientsList = FXCollections.observableArrayList();
        clientsList.addAll(clients.stream().map(Client::getSocialSecurityNumber).collect(Collectors.toList()));
        return clientsList;
    }

    private ObservableList<Client> clientsList() {
        ObservableList<Client> clientsList = FXCollections.observableArrayList();
        clientsList.addAll(clients);
        return clientsList;
    }

    TableView<Client> clientTableView() {
        TableView<Client> clientTableView = new TableView<>();

        TableColumn<Client, String> firstNameColumn = new TableColumn<>("First name");
        firstNameColumn.setMinWidth(150);
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Client, String> lastNameColumn = new TableColumn<>("Last name");
        lastNameColumn.setMinWidth(150);
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<Client, String> addressColumn = new TableColumn<>("Address");
        addressColumn.setMinWidth(150);
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<Client, String> accountColumn = new TableColumn<>("Account number");
        accountColumn.setMinWidth(150);
        accountColumn.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));

        clientTableView.setItems(clientsList());
        //noinspection unchecked
        clientTableView.getColumns().addAll(firstNameColumn, lastNameColumn, addressColumn, accountColumn);
        return clientTableView;
    }

    String saveString() {
        return (this.getName() + "," + this.getAddress() + "," + this.getCashInBranch());
    }

}
