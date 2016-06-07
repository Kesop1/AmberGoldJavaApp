package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;

import static sample.Main.activeAdmin;

class Program {
    ArrayList<Branch> branches;
    ArrayList<Admin> admins;
    ArrayList<Role> roles;

    Program() {
        this.branches = new ArrayList<>();
        this.admins = new ArrayList<>();
        this.roles = new ArrayList<>();
    }

    ObservableList<String> allBranches() {
        ObservableList<String> branches = FXCollections.observableArrayList();
        for (Branch branch : this.branches) {
            branches.add(branch.getName());
        }
        return branches;
    }

    private ObservableList<Branch> allBranchesList() {
        ObservableList<Branch> branches = FXCollections.observableArrayList();
        for (Branch branch : this.branches) {
            branches.add(branch);
        }
        return branches;
    }

    ObservableList<String> allRoles() {
        ObservableList<String> roles = FXCollections.observableArrayList();
        for (Role role : this.roles) {
            roles.add(role.getName());
        }
        return roles;
    }

    private ObservableList<Role> allRolesList() {
        ObservableList<Role> roles = FXCollections.observableArrayList();
        for (Role role : this.roles) {
            roles.add(role);
        }
        return roles;
    }

    ObservableList<String> allAdmins() {
        ObservableList<String> admins = FXCollections.observableArrayList();
        admins.addAll(this.admins.stream().map(Admin::getName).collect(Collectors.toList()));
        return admins;
    }

    private ObservableList<Admin> allAdminsList() {
        ObservableList<Admin> admins = FXCollections.observableArrayList();
        for (Admin admin : this.admins) {
            admins.add(admin);
        }
        return admins;
    }

    ObservableList<String> allManagers() {
        ObservableList<String> managers = FXCollections.observableArrayList();
        for (Branch branch : branches) {
            if (branch.getManager() != null) {
                managers.add(branch.getManager().getName());
            }
        }
        return managers;
    }

    ObservableList<Manager> allManagersList() {
        ObservableList<Manager> managers = FXCollections.observableArrayList();
        for (Branch branch : branches) {
            if (branch.getManager() != null) {
                managers.add(branch.getManager());
            }
        }
        return managers;
    }

    ObservableList<String> allEmployees() {
        ObservableList<String> employees = FXCollections.observableArrayList();
        for (Branch branch : branches) {
            if (branch.getEmployees() != null) {
                for (Employee employeeInBranch : branch.getEmployees()) {
                    employees.add(employeeInBranch.getName());
                }
            }
        }
        return employees;
    }

    ObservableList<Employee> allEmployeesList() {
        ObservableList<Employee> employees = FXCollections.observableArrayList();
        for (Branch branch : branches) {
            if (branch.getEmployees() != null) {
                employees.addAll(branch.getEmployees());
            }
        }
        return employees;
    }

    ArrayList<Employee> allEmployeesArray() {
        ArrayList<Employee> allEmployees = new ArrayList<>();
        for (Branch branch : branches) {
            allEmployees.addAll(branch.getEmployees());
        }
        return allEmployees;
    }

    TableView<Admin> adminTableView() {
        TableView<Admin> adminTableView = new TableView<>();

        TableColumn<Admin, String> idColumn = new TableColumn<>("ID");
        idColumn.setMinWidth(100);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Admin, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(200);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Admin, Integer> attemptsColumn = new TableColumn<>("Attempts");
        attemptsColumn.setMinWidth(150);
        attemptsColumn.setCellValueFactory(new PropertyValueFactory<>("attempts"));

        TableColumn<Admin, Boolean> passColumn = new TableColumn<>("Password change");
        passColumn.setMinWidth(150);
        passColumn.setCellValueFactory(new PropertyValueFactory<>("changePassword"));

        adminTableView.setItems(allAdminsList());
        //noinspection unchecked
        adminTableView.getColumns().addAll(idColumn, nameColumn, attemptsColumn, passColumn);
        return adminTableView;
    }

    TableView<Manager> managerTableView(ObservableList<Manager> listToPrint) {
        TableView<Manager> managerTableView = new TableView<>();

        TableColumn<Manager, String> idColumn = new TableColumn<>("ID");
        idColumn.setMinWidth(100);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Manager, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(150);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Manager, Integer> attemptsColumn = new TableColumn<>("Attempts");
        attemptsColumn.setMinWidth(100);
        attemptsColumn.setCellValueFactory(new PropertyValueFactory<>("attempts"));

        TableColumn<Manager, Boolean> passColumn = new TableColumn<>("Password change");
        passColumn.setMinWidth(100);
        passColumn.setCellValueFactory(new PropertyValueFactory<>("changePassword"));

        TableColumn<Manager, String> branchColumn = new TableColumn<>("Branch");
        branchColumn.setMinWidth(150);
        branchColumn.setCellValueFactory(new PropertyValueFactory<>("branchName"));

        managerTableView.setItems(listToPrint);
        //noinspection unchecked
        managerTableView.getColumns().addAll(idColumn, nameColumn, attemptsColumn, passColumn, branchColumn);
        return managerTableView;
    }

    TableView<Employee> employeeTableView(ObservableList<Employee> list) {
        TableView<Employee> employeeTableView = new TableView<>();

        TableColumn<Employee, String> idColumn = new TableColumn<>("ID");
        idColumn.setMinWidth(50);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setSortType(TableColumn.SortType.DESCENDING);

        TableColumn<Employee, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(150);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Employee, Integer> attemptsColumn = new TableColumn<>("Attempts");
        attemptsColumn.setMinWidth(100);
        attemptsColumn.setCellValueFactory(new PropertyValueFactory<>("attempts"));

        TableColumn<Employee, Boolean> passColumn = new TableColumn<>("Password change");
        passColumn.setMinWidth(100);
        passColumn.setCellValueFactory(new PropertyValueFactory<>("changePassword"));

        TableColumn<Employee, String> roleColumn = new TableColumn<>("Role");
        roleColumn.setMinWidth(50);
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        TableColumn<Employee, String> branchColumn = new TableColumn<>("Branch");
        branchColumn.setMinWidth(150);
        branchColumn.setCellValueFactory(new PropertyValueFactory<>("branchName"));

        employeeTableView.setItems(list);
        //noinspection unchecked
        employeeTableView.getColumns().addAll(idColumn, nameColumn, attemptsColumn, passColumn, roleColumn, branchColumn);
        return employeeTableView;
    }

    TableView<Role> roleTableView() {
        TableView<Role> roleTableView = new TableView<>();

        TableColumn<Role, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(100);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Role, String> depositLimit = new TableColumn<>("Deposit");
        depositLimit.setMinWidth(100);
        depositLimit.setCellValueFactory(new PropertyValueFactory<>("depositLimit"));

        TableColumn<Role, String> withdrawalLimit = new TableColumn<>("Withdrawal");
        withdrawalLimit.setMinWidth(100);
        withdrawalLimit.setCellValueFactory(new PropertyValueFactory<>("withdrawalLimit"));

        TableColumn<Role, String> transferLimit = new TableColumn<>("Transfer");
        transferLimit.setMinWidth(100);
        transferLimit.setCellValueFactory(new PropertyValueFactory<>("transferLimit"));

        TableColumn<Role, String> paymentLimit = new TableColumn<>("Payment");
        paymentLimit.setMinWidth(100);
        paymentLimit.setCellValueFactory(new PropertyValueFactory<>("paymentLimit"));

        TableColumn<Role, String> onlineLimit = new TableColumn<>("Online");
        onlineLimit.setMinWidth(100);
        onlineLimit.setCellValueFactory(new PropertyValueFactory<>("onlineLimit"));

        roleTableView.setItems(allRolesList());
        //noinspection unchecked
        roleTableView.getColumns().addAll(nameColumn, depositLimit, withdrawalLimit, transferLimit, paymentLimit, onlineLimit);
        return roleTableView;
    }

    TableView<Branch> branchTableView() {
        TableView<Branch> branchTableView = new TableView<>();

        TableColumn<Branch, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(250);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Branch, String> addressColumn = new TableColumn<>("Address");
        addressColumn.setMinWidth(250);
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<Branch, String> managerColumn = new TableColumn<>("Manager");
        managerColumn.setMinWidth(100);
        managerColumn.setCellValueFactory(new PropertyValueFactory<>("managerId"));

        branchTableView.setItems(allBranchesList());
        //noinspection unchecked
        branchTableView.getColumns().addAll(nameColumn, addressColumn, managerColumn);
        return branchTableView;
    }


//    ******************************* FIND *******************************

    Client findClientByAccount(String account) {
        if ((account!=null) && (!branches.isEmpty())) {
            for (Branch branch : branches) {
                for (Client client : branch.getClients()) {
                    if (client.getAccountNumber().equals(account)) {
                        System.out.println(client.getLastName() + " found in " + branch.getName());
                        return client;
                    }
                }
            }
        }
        System.out.println("Client not found");
        return null;
    }

    Branch findBranchByClientSs(int ssNumber) {
        if ((ssNumber > 0) && (!branches.isEmpty())) {
            for (Branch branch : branches) {
                for (Client client : branch.getClients()) {
                    if (client.getSocialSecurityNumber() == ssNumber) {
                        System.out.println(client.getLastName() + " found in " + branch.getName());
                        return branch;
                    }
                }
            }
        }
        System.out.println("Client not found");
        return null;
    }

    User findUserById(String findId) {
        if (findId!=null) {
            Admin admin = findAdminById(findId);
            if (admin != null)
                return admin;
            Employee employee = findEmployeeById(findId);
            if (employee != null)
                return employee;
            Manager manager;
            if (!branches.isEmpty()) {
                for (Branch branch : branches) {
                    manager = branch.getManager();
                    if (manager != null)
                        if (manager.getId().equals(findId))
                            return manager;
                }
            }
        }
        return null;
    }

    Admin findAdminById(String findId) {
        if ((findId!=null) && (!admins.isEmpty())) {
            for (Admin admin : admins) {
                if (admin.getId().equals(findId)) {
                    System.out.println(admin.getName() + " found");
                    return admin;
                }
            }
        }
        System.out.println(findId + " not found");
        return null;
    }

    Admin findAdminByName(String findName) {
        if ((findName!=null) && (!admins.isEmpty())) {
            for (Admin admin : admins) {
                if (admin.getName().equals(findName)) {
                    System.out.println(admin.getName() + " found");
                    return admin;
                }
            }
        }
        System.out.println(findName + " not found");
        return null;
    }

    Employee findEmployeeByName(String findName) {
        if ((findName!=null) && (!branches.isEmpty())) {
            for (Branch branch : branches) {
                Employee employee = branch.findEmployeeByName(findName);
                if (employee != null) {
                    System.out.println(findName + " found in " + branch.getName());
                    return employee;
                }
            }
        }
        System.out.println(findName + " not found");
        return null;
    }

    Employee findEmployeeById(String findId) {
        if ((findId!=null) && (!branches.isEmpty())) {
            for (Branch branch : branches) {
                Employee employee = branch.findEmployeeById(findId);
                if (employee != null) {
                    System.out.println(findId + " found in " + branch.getName());
                    return employee;
                }
            }
        }
        System.out.println(findId + " not found");
        return null;
    }

    Branch findBranchByName(String branchName) {
        if ((branchName!=null) && (!branches.isEmpty())) {
            for (Branch branch : branches) {
                if (branch.getName().equals(branchName)) {
                    System.out.println(branchName + " found");
                    return branch;
                }
            }
        }
        return null;
    }

    Role findRoleByName(String roleName) {
        if ((roleName!=null) && (!roles.isEmpty())) {
            for (Role role : roles) {
                if (role.getName().equals(roleName)) {
                    System.out.println(roleName + " found");
                    return role;
                }
            }
        }
        System.out.println(roleName + " not found.");
        return null;
    }

    Manager findManagerByName(String findName) {
        if ((findName!=null) && (!branches.isEmpty())) {
            for (Branch branch : branches) {
                Manager manager = branch.getManager();
                if (manager != null) {
                    if (manager.getName().equals(findName)) {
                        System.out.println(manager.getName() + " found in " + branch.getName());
                        return manager;
                    }
                }
            }
        }
        System.out.println(findName + " not found");
        return null;
    }

    Manager findManagerById(String findId) {
        if ((findId!=null) && (!branches.isEmpty())) {
            for (Branch branch : branches) {
                if (branch.getManager() != null) {
                    if (branch.getManager().getId().equals(findId)) {
                        System.out.println(branch.getManager().getName() + " found in " + branch.getName());
                        return branch.getManager();
                    }
                }
            }
        }
        System.out.println("Cannot find a manager with an ID " + findId);
        return null;
    }

    Branch findBranchByManager(Manager manager) {
        if ((manager != null) && (!branches.isEmpty())) {
            for (Branch branch : branches) {
                if (branch.getManager() != null)
                    if (branch.getManager().equals(manager)) {
                        System.out.println(manager.getName() + " found in " + branch.getName());
                        return branch;
                    }
            }
            System.out.println(manager.getName() + " was not found in any branch");
        }
        return null;
    }

    Branch findBranchByEmployee(Employee employee) {
        if ((employee != null) && (!branches.isEmpty())) {
            for (Branch branch : branches) {
                if (!branch.getEmployees().isEmpty())
                    if (branch.getEmployees().contains(employee)) {
                        System.out.println(employee.getName() + " found in " + branch.getName());
                        return branch;
                    }
            }
            System.out.println(employee.getName() + " was not found in any branch");
        }
        return null;
    }

    Transaction findTransactionById(int transId) {
        if (!branches.isEmpty()) {
            for (Branch branch : branches) {
                for (Transaction transaction : branch.getTransactions()) {
                    if (transaction.getTransactionId() == transId)
                        return transaction;
                }
            }
        }
        System.out.println("Transaction not found");
        return null;
    }

//    ************************* SAVE *************************

    void saveAdmin() {
        saveUsers();
        saveBranches();
        saveRoles();
        saveTemp();
    }

    void saveEmployee() {
        saveClients();
        saveTransactions();
        saveBranches();
        saveTempTransactions();
    }

    void saveManager() {
        saveClients();
        saveTransactions();
        saveBranches();
        saveTempTransactions();
    }

    boolean saveUsers() {
        String fileName = "Users.csv";
        System.out.println("Saving " + fileName);
        try {
            PrintWriter outputStream = new PrintWriter(fileName);
            save(outputStream, ("Branch name,User type,Name,Login,Password,Attempts,Change password,Role"));
            System.out.println("Saving admins");
            for (Admin admin : admins) {
                if (admins.isEmpty()) {
                    System.out.println("No admins to save");
                    break;
                }
                System.out.print(admin.getName());
                save(outputStream, ("," + admin.saveString()));
            }
            System.out.println("Admins saved");
            for (Branch branch : branches) {
                if (branch.getManager() == null) {
                    System.out.println("No manager in " + branch.getName());
                    break;
                }
                System.out.println("Saving manager in " + branch.getName());
                System.out.print(branch.getManager().getName());
                save(outputStream, (branch.getName() + "," + branch.getManager().saveString()));
            }
            System.out.println("Managers saved");
            for (Branch branch : branches) {
                if (branch.getEmployees().isEmpty()) {
                    System.out.println("No employees in " + branch.getName());
                }
                System.out.println("Saving employees in " + branch.getName());
                for (Employee employee : branch.getEmployees()) {
                    System.out.print(employee.getName());
                    save(outputStream, (branch.getName() + "," + employee.saveString()));
                }
            }
            System.out.println("Employees saved");
            outputStream.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error while saving " + fileName + ": " + e.getMessage());
        }
        return true;
    }

    private boolean saveBranches() {
        if (branches.isEmpty()) {
            System.out.println("No branches to save");
            return false;
        }
        String fileName = "Branches.csv";
        System.out.println("Saving " + fileName);
        try {
            PrintWriter outputStream = new PrintWriter(fileName);
            save(outputStream, ("Branch name,Address,Cash"));
            for (Branch branch : branches) {
                System.out.print(branch.getName());
                save(outputStream, (branch.saveString()));
            }
            outputStream.close();
            System.out.println("Branches saved");
        } catch (FileNotFoundException e) {
            System.out.println("Error while saving " + fileName + ": " + e.getMessage());
        }
        return true;
    }

    private boolean saveClients() {
        if (branches.isEmpty()) {
            System.out.println("No branches");
            return false;
        }
        String fileName = "Clients.csv";
        System.out.println("Saving " + fileName);
        try {
            PrintWriter outputStream = new PrintWriter(fileName);
            save(outputStream, ("Branch name,First name,Last name,Social security number," +
                    "Address,Date of birth,Cash,Account number"));
            for (Branch branch : branches) {
                System.out.println("Saving clients in " + branch.getName());
                for (Client client : branch.getClients()) {
                    System.out.println("Saving " + client.getLastName());
                    if (branch.getClients().isEmpty()) {
                        System.out.println("No clients in " + branch.getName());
                        break;
                    }
                    System.out.print(client.getLastName());
                    save(outputStream, (branch.getName() + "," + client.saveString()));
                }
            }
            System.out.println("Clients saved");
            outputStream.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error while saving " + fileName + ": " + e.getMessage());
        }
        return true;
    }

    private boolean saveTransactions() {
        String fileName = "Transactions.csv";
        System.out.println("Saving " + fileName);
        ArrayList<Integer> transIds = new ArrayList<>();
        if (branches.isEmpty()) {
            System.out.println("No branches to save");
            return false;
        }
        try {
            PrintWriter outputStream = new PrintWriter(fileName);
            save(outputStream, ("Branch name,Sender,Receiver,Transaction type,Amount,Date,Employee ID, Transaction ID"));
            for (Branch branch : branches) {
                System.out.println("Saving transactions in " + branch.getName());
                for (Transaction transaction : branch.getTransactions()) {
                    if (branch.getTransactions().isEmpty()) {
                        System.out.println("No transactions in " + branch.getName());
                        break;
                    } else {
                        if (!transIds.contains(transaction.getTransactionId())) {
                            transIds.add(transaction.getTransactionId());
                            System.out.print(transaction.getTransactionId());
                            save(outputStream, (branch.getName() + "," + transaction.saveString()));
                        } else System.out.println(transaction.getTransactionId() + " was already saved");
                    }
                }
            }
            outputStream.close();
            System.out.println("Transactions saved");
        } catch (FileNotFoundException e) {
            System.out.println("Error while saving " + fileName + ": " + e.getMessage());
        }
        return true;
    }

    boolean exportTransactions(User user, ObservableList<Transaction> transactions) {
        String fileName = LocalDate.now() + "-" + user.getId() + "-Transactions.csv";
        System.out.println("Saving " + fileName);
//        ArrayList<Integer> transIds = new ArrayList<>();
        if (!transactions.isEmpty()) {
            try {
                PrintWriter outputStream = new PrintWriter(fileName);
                save(outputStream, ("Branch name,Sender,Receiver,Transaction type,Amount,Date,Employee ID, Transaction ID"));
                for (Transaction transaction : transactions) {
//                        if (!transIds.contains(transaction.getTransactionId())) {
//                            transIds.add(transaction.getTransactionId());
                    System.out.print(transaction.getTransactionId());
                    try {
                        if (user.getClass().equals(Employee.class)) {
                            try {
                                save(outputStream, (findBranchByEmployee((Employee) user).getName() + "," + transaction.saveString()));
                            } catch (ClassCastException e) {
                                System.out.println(e.getMessage());
                            }
                        } else if (user.getClass().equals(Manager.class)) {
                            try {
                                save(outputStream, (findBranchByManager((Manager) user).getName() + "," + transaction.saveString()));
                            } catch (ClassCastException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    } catch (ClassCastException e) {
                        System.out.println(e.getMessage());
                        return false;
                    }
                }
                outputStream.close();
                System.out.println("Transactions report saved");
            } catch (FileNotFoundException e) {
                System.out.println("Error while saving " + fileName + ": " + e.getMessage());
            }
        }
        return true;
    }

    private boolean saveTempTransactions() {
        String fileName = "TempTransactions.csv";
        System.out.println("Saving " + fileName);
        ArrayList<Integer> transIds = new ArrayList<>();
        if (branches.isEmpty()) {
            System.out.println("No branches to save");
            return false;
        }
        try {
            PrintWriter outputStream = new PrintWriter(fileName);
            save(outputStream, ("Branch name,Sender,Receiver,Transaction type,Amount,Date,Employee ID, Transaction ID"));
            for (Branch branch : branches) {
                System.out.println("Saving transactions in " + branch.getName());
                if (branch.getManager() != null) {
                    for (Transaction transaction : branch.getManager().getTempTransactions()) {
                        if (branch.getManager().getTempTransactions().isEmpty()) {
                            System.out.println("No transactions in " + branch.getName());
                            break;
                        } else {
                            if (!transIds.contains(transaction.getTransactionId())) {
                                transIds.add(transaction.getTransactionId());
                                System.out.print(transaction.getTransactionId());
                                save(outputStream, (branch.getName() + "," + transaction.saveString()));
                            } else System.out.println(transaction.getTransactionId() + " was already saved");
                        }
                    }
                }
            }
            outputStream.close();
            System.out.println("Transactions saved");
        } catch (FileNotFoundException e) {
            System.out.println("Error while saving " + fileName + ": " + e.getMessage());
        }
        return true;
    }

    private boolean saveRoles() {
        if (roles.isEmpty()) {
            System.out.println("No roles to save");
            return false;
        }
        String fileName = "Roles.csv";
        System.out.println("Saving " + fileName);
        try {
            PrintWriter outputStream = new PrintWriter(fileName);
            save(outputStream, ("Role name,Deposit limit, Withdrawal limit, Transfer limit, Payment limit, Online limit"));
            for (Role role : roles) {
                System.out.print(role.saveString());
                save(outputStream, (role.saveString()));
            }
            outputStream.close();
            System.out.println("Roles saved");
        } catch (FileNotFoundException e) {
            System.out.println("Error while saving " + fileName + ": " + e.getMessage());
        }
        return true;
    }

    private void save(PrintWriter input, String inputString) {
        inputString = inputString.replaceAll(" ", "_");
        input.println(inputString);
        System.out.println(" saved");
    }

    private boolean saveTemp() {
        if ((activeAdmin.tempUsers.isEmpty()) && (activeAdmin.tempBranches.isEmpty()) && (activeAdmin.tempRoles.isEmpty())) {
            System.out.println("No temp to save");
            return false;
        }
        String fileName = "Temp.csv";
        System.out.println("Saving " + fileName);
        try {
            PrintWriter outputStream = new PrintWriter(fileName);
            save(outputStream, ("Action,User,Branch,Role,Admin,Deposit,Withdrawal,Transfer,Payment,Online,New name,New address"));
            for (Admin.TempUser temp : activeAdmin.tempUsers) {
                System.out.println(temp.getAction() + " for " + temp.getTempUser().getId());
                save(outputStream, (temp.saveString()));
            }
            for (Admin.TempRole temp : activeAdmin.tempRoles) {
                System.out.println(temp.getAction() + " for " + temp.getRole().getName());
                save(outputStream, (temp.saveString()));
            }
            for (Admin.TempBranch temp : activeAdmin.tempBranches) {
                System.out.println(temp.getAction() + " for " + temp.getBranch().getName());
                save(outputStream, (temp.saveString()));
            }
            outputStream.close();
            System.out.println(fileName + " saved");
        } catch (FileNotFoundException e) {
            System.out.println("Error while saving " + fileName + ": " + e.getMessage());
        }
        return true;
    }

//    ****************************** LOAD ****************************

    private boolean loadUsers() {
        String fileName1 = "Users.csv";
        System.out.println("Loading " + fileName1);
        File file1 = new File(fileName1);
        try {
            Scanner inputStream = new Scanner(file1);
            while (inputStream.hasNext()) {
                String data = inputStream.next();
                data = data.replaceAll("_", " ");
                String[] dataSplitted = data.split(",");
                if (!dataSplitted[0].equals("Branch name")) {
                    Branch branch = findBranchByName(dataSplitted[0]);
                    String type = dataSplitted[1];
                    String name = dataSplitted[2];
                    String id = dataSplitted[3];
                    String password = dataSplitted[4];
                    int attempts = -1;
                    try {
                        attempts = Integer.parseInt(dataSplitted[5]);
                    } catch (NumberFormatException e) {
                        System.out.println("Error while setting unsuccessful attempts for the user " + name + ": " +
                                e.getMessage());
                    }
                    String change = dataSplitted[6];
                    boolean changePassword = Boolean.parseBoolean(change);
                    boolean added = false;      //for displaying success message
                    if ((type.equals("")) || (name.equals("")) ||
                            (id.equals("")) || (password.equals("")) || (attempts < 0) ||
                            ((!change.equals("true")) && (!change.equals("false")))) {
                        System.out.println("Wrong input");
                    } else {
                        switch (type) {
                            case "A":
                                Admin admin = new Admin(name);
                                admin.setId(id);
                                admin.setPassword(password);
                                admin.setAttempts(attempts);
                                admin.setChangePassword(changePassword);
                                admins.add(admin);
                                System.out.print("Admin ");
                                added = true;
                                break;
                            case "M":
                                if (branch == null) {
                                    System.out.println("Wrong input");
                                } else {
                                    Manager manager = new Manager(name);
                                    manager.setId(id);
                                    manager.setPassword(password);
                                    manager.setAttempts(attempts);
                                    manager.setChangePassword(changePassword);
                                    branch.setManager(manager);
                                    manager.setBranchName(branch.getName());
                                    System.out.print("Manager ");
                                    added = true;
                                }
                                break;
                            case "E":
                                Role role = findRoleByName(dataSplitted[7]);
                                if ((branch == null) || (role == null)) {
                                    System.out.println("Wrong input");
                                } else {
                                    Employee employee = new Employee(name);
                                    employee.setId(id);
                                    employee.setPassword(password);
                                    employee.setAttempts(attempts);
                                    employee.setChangePassword(changePassword);
                                    employee.setRole(role);
                                    employee.setBranchName(branch.getName());
                                    branch.getEmployees().add(employee);
                                    System.out.print("Employee ");
                                    added = true;
                                }
                                break;
                            default:
                                System.out.println("Incorrect user type");
                                added = false;
                        }
                        if (added) System.out.println(name + " loaded");
                    }
                }
            }
            System.out.println(fileName1 + " loaded successfully");
            inputStream.close();
            return true;
        } catch (FileNotFoundException e) {
            System.out.println(fileName1 + " not found");
            System.out.println(e.getMessage());
            return false;
        }
    }

    private boolean loadBranches() {
        String fileName1 = "Branches.csv";
        System.out.println("Loading " + fileName1);
        File file1 = new File(fileName1);
        try {
            Scanner inputStream = new Scanner(file1);
            while (inputStream.hasNext()) {
                String data = inputStream.next();
                data = data.replaceAll("_", " ");
                String[] dataSplitted = data.split(",");
                if (!dataSplitted[0].equals("Branch name")) {
                    String name = dataSplitted[0];
                    String address = dataSplitted[1];
                    Double cashInBranch = null;
                    try {
                        cashInBranch = Double.parseDouble(dataSplitted[2]);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("Error while setting cash in branch " + name + ": " + e.getMessage());
                    } catch (NumberFormatException e) {
                        System.out.println("Error while setting cash in branch " + name + ": " + e.getMessage());
                    }
                    if ((name.equals("")) || (address.equals("")) || (cashInBranch == null)) {
                        System.out.println("Invalid input");
                    } else {
                        Branch branch = new Branch(name, address);
                        branches.add(branch);
                        branch.setCashInBranch(cashInBranch);
                        System.out.println(branch.saveString() + " loaded");
                    }
                }
            }
            System.out.println(fileName1 + " loaded successfully");
            inputStream.close();
            return true;
        } catch (FileNotFoundException e) {
            System.out.println(fileName1 + " not found");
            System.out.println(e.getMessage());
            return false;
        }
    }

    private boolean loadRoles() {
        String fileName1 = "Roles.csv";
        System.out.println("Loading " + fileName1);
        File file1 = new File(fileName1);
        try {
            Scanner inputStream = new Scanner(file1);
            while (inputStream.hasNext()) {
                String data = inputStream.next();
                data = data.replaceAll("_", " ");
                String[] dataSplitted = data.split(",");
                String name = dataSplitted[0];
                if (!name.equals("Role name")) {
                    if ((name.equals("Role name")) || (name.equals(""))) {
                        System.out.println("Wrong input");
                    } else {
                        Role role = new Role(name);
                        roles.add(role);
                        ArrayList<Double> limits = new ArrayList<>(5);
                        for (int i = 1; i < 6; i++) {
                            String s = "";
                            switch (i) {
                                case 1:
                                    s = "deposit";
                                    break;
                                case 2:
                                    s = "withdrawal";
                                    break;
                                case 3:
                                    s = "transfer";
                                    break;
                                case 4:
                                    s = "payment";
                                    break;
                                case 5:
                                    s = "online";
                                    break;
                            }
                            Double limit = null;
                            try {
                                limit = Double.parseDouble(dataSplitted[i]);
                            } catch (Exception e) {
                                System.out.println("Error while setting " + s + " limit to role " + role.getName() + ":" + e.getMessage());
                            }
                            if (limit == null) limit = 0.0;
                            limits.add(limit);
                        }
                        role.setLimits(limits);
                        System.out.println(role.saveString() + " loaded");
                    }
                }
            }
            System.out.println(fileName1 + " loaded successfully");
            inputStream.close();
            return true;
        } catch (FileNotFoundException e) {
            System.out.println(fileName1 + " not found");
            System.out.println(e.getMessage());
            return false;
        }
    }

    boolean loadClients() {
        String fileName1 = "Clients.csv";
        System.out.println("Loading " + fileName1);
        File file1 = new File(fileName1);
        try {
            Scanner inputStream = new Scanner(file1);
            while (inputStream.hasNext()) {
                String data = inputStream.next();
                data = data.replaceAll("_", " ");
                String[] dataSplitted = data.split(",");
                if (!dataSplitted[0].equals("Branch name")) {
                    Branch branch = findBranchByName(dataSplitted[0]);
                    String firstName = dataSplitted[1];
                    String lastName = dataSplitted[2];
                    int ssNumber = -1;
                    try {
                        ssNumber = Integer.parseInt(dataSplitted[3]);
                    } catch (NumberFormatException e) {
                        System.out.println("Error while setting social security number to " + lastName + ": " + e.getMessage());
                    }
                    String address = dataSplitted[4];
                    String dob = dataSplitted[5];
                    String[] dateSplit = dob.split("-");
                    int year = -1;
                    int month = -1;
                    int day = -1;
                    try {
                        year = Integer.parseInt(dateSplit[0]);
                        month = Integer.parseInt(dateSplit[1]);
                        day = Integer.parseInt(dateSplit[2]);
                    } catch (NumberFormatException e) {
                        System.out.println(e.getMessage());
                    }
                    LocalDate dateOfBirth = null;
                    try {
                        dateOfBirth = LocalDate.of(year, month, day);
                    } catch (DateTimeException e) {
                        System.out.println(e.getMessage());
                    }
                    Double balance = null;
                    try {
                        balance = Double.parseDouble(dataSplitted[6]);
                    } catch (Exception e) {
                        System.out.println("Error while setting balance of " + lastName + ": " + e.getMessage());
                    }
                    String accountNumber = dataSplitted[7];
                    if ((branch == null) || (firstName.equals("")) || (lastName.equals("")) || (ssNumber < 0) ||
                            (address.equals("")) || (balance == null) || (accountNumber.equals(""))
                            || (dateOfBirth == null) || (dateOfBirth.isAfter(LocalDate.now().minusDays(1)))) {
                        System.out.println("Wrong input");
                    } else {
                        Client client = new Client(firstName, lastName, ssNumber, address, dateOfBirth);
                        branch.getClients().add(client);
                        client.setBalance(balance);
                        client.setAccountNumber(accountNumber);
                        System.out.println(client.getLastName() + " loaded in " + branch.getName());
                    }
                }
            }
            System.out.println(fileName1 + " loaded successfully");
            inputStream.close();
            return true;
        } catch (FileNotFoundException e) {
            System.out.println(fileName1 + " not found");
            System.out.println(e.getMessage());
            return false;
        }
    }

    boolean loadTransactions() {
        String fileName1 = "Transactions.csv";
        System.out.println("Loading " + fileName1);
        File file1 = new File(fileName1);
        try {
            Scanner inputStream = new Scanner(file1);
            while (inputStream.hasNext()) {
                String data = inputStream.next();
                data = data.replaceAll("_", " ");
                String[] dataSplitted = data.split(",");
                try {
                    if (!dataSplitted[0].equals("Branch name")) {
                        Branch branch = findBranchByName(dataSplitted[0]);
                        String sender = dataSplitted[1];
                        String receiver = dataSplitted[2];
                        String type = dataSplitted[3];
                        Double amount = null;
                        try {
                            amount = Double.parseDouble(dataSplitted[4]);
                        } catch (Exception e) {
                            System.out.println("Error while setting the transaction amount: " + e.getMessage());
                        }
                        String date = dataSplitted[5];
                        String[] dateSplit = date.split("-");
                        int year = -1;
                        int month = -1;
                        int day = -1;
                        try {
                            year = Integer.parseInt(dateSplit[0]);
                            month = Integer.parseInt(dateSplit[1]);
                            day = Integer.parseInt(dateSplit[2]);
                        } catch (NumberFormatException e) {
                            System.out.println(e.getMessage());
                        }
                        LocalDate date1 = null;
                        try {
                            date1 = LocalDate.of(year, month, day);
                        } catch (DateTimeException e) {
                            System.out.println(e.getMessage());
                        }
                        Employee employee = findEmployeeById(dataSplitted[6]);
                        int transId = -1;
                        try {
                            transId = Integer.parseInt(dataSplitted[7]);
                        } catch (NumberFormatException e) {
                            System.out.println(e.getMessage());
                        }
                        Manager manager = findManagerById(dataSplitted[6]);
                        if ((branch == null) || (sender.equals("")) || (type.equals("")) ||
                                (amount == null) || (date1 == null) || ((employee == null) && (manager == null)) || (transId < 0)) {
                            System.out.println("Wrong input");
                        } else {
                            String id;
                            if (employee != null) id = employee.getId();
                            else id = manager.getId();
                            Transaction transaction = new Transaction(sender, receiver, type, amount, date1, id);
                            transaction.setTransactionId(transId);
                            if (findTransactionById(transId) == null) {
                                System.out.println(transaction.getTransactionId() + " added to " + branch.getName());
                                branch.getTransactions().add(transaction);
                                Client clientSender = findClientByAccount(sender);
                                if (clientSender != null) {
                                    System.out.println(transaction.getTransactionId() + " added to " + clientSender.getLastName());
                                    clientSender.getTransactions().add(transaction);
                                }
                                Client clientReceiver = findClientByAccount(receiver);
                                if ((clientReceiver != null) && (clientReceiver != clientSender)) {
                                    System.out.println(transaction.getTransactionId() + " added to " + clientReceiver.getLastName());
                                    clientReceiver.getTransactions().add(transaction);
                                    Branch branchReceiver = findBranchByClientSs(clientReceiver.getSocialSecurityNumber());
                                    if (branch != branchReceiver) {
                                        System.out.println(transaction.getTransactionId() + " added to " + branchReceiver.getName());
                                        branchReceiver.getTransactions().add(transaction);
                                    }
                                }
                                System.out.println(transId + " loaded in " + branch.getName());
                            } else {
                                System.out.println("Transaction " + transId + " already exists");
                            }
                        }
                    }
                } catch (IndexOutOfBoundsException e) {
                    System.out.println(e.getMessage());
                }
            }
            System.out.println(fileName1 + " loaded successfully");
            inputStream.close();
            return true;
        } catch (FileNotFoundException e) {
            System.out.println(fileName1 + " not found");
            System.out.println(e.getMessage());
            return false;
        }
    }

    boolean loadTempTransactions() {
        String fileName1 = "TempTransactions.csv";
        System.out.println("Loading " + fileName1);
        File file1 = new File(fileName1);
        try {
            Scanner inputStream = new Scanner(file1);
            while (inputStream.hasNext()) {
                String data = inputStream.next();
                data = data.replaceAll("_", " ");
                String[] dataSplitted = data.split(",");
                try {
                    if (!dataSplitted[0].equals("Branch name")) {
                        Branch branch = findBranchByName(dataSplitted[0]);
                        String sender = dataSplitted[1];
                        String receiver = dataSplitted[2];
                        String type = dataSplitted[3];
                        Double amount = null;
                        try {
                            amount = Double.parseDouble(dataSplitted[4]);
                        } catch (Exception e) {
                            System.out.println("Error while setting the transaction amount: " + e.getMessage());
                        }
                        String date = dataSplitted[5];
                        String[] dateSplit = date.split("-");
                        int year = -1;
                        int month = -1;
                        int day = -1;
                        try {
                            year = Integer.parseInt(dateSplit[0]);
                            month = Integer.parseInt(dateSplit[1]);
                            day = Integer.parseInt(dateSplit[2]);
                        } catch (NumberFormatException e) {
                            System.out.println(e.getMessage());
                        }
                        LocalDate date1 = null;
                        try {
                            date1 = LocalDate.of(year, month, day);
                        } catch (DateTimeException e) {
                            System.out.println(e.getMessage());
                        }
                        Employee employee = findEmployeeById(dataSplitted[6]);
                        int transId = -1;
                        try {
                            transId = Integer.parseInt(dataSplitted[7]);
                        } catch (NumberFormatException e) {
                            System.out.println(e.getMessage());
                        }
                        if ((branch == null) || (sender.equals("")) || (type.equals("")) ||
                                (amount == null) || (date1 == null) || (employee == null) || (transId < 0)) {
                            System.out.println("Wrong input");
                        } else {
                            Transaction transaction = new Transaction(sender, receiver, type, amount, date1, employee.getId());
                            System.out.println("++++++++++++++++++++++++++++++++++++++++++");
                            System.out.println(transaction.saveString());
                            transaction.setTransactionId(transId);
                            System.out.println("___");
                            System.out.println(transaction.saveString());
                            if (findTransactionById(transId) == null) {
                                System.out.println(transaction.getTransactionId() + " added to " + branch.getName());
                                branch.getManager().getTempTransactions().add(transaction);
                                System.out.println(transId + " loaded in " + branch.getName());
                            } else {
                                System.out.println("Transaction " + transId + " already exists");
                            }
                        }
                    }
                } catch (IndexOutOfBoundsException e) {
                    System.out.println(e.getMessage());
                }
            }
            System.out.println(fileName1 + " loaded successfully");
            inputStream.close();
            return true;
        } catch (FileNotFoundException e) {
            System.out.println(fileName1 + " not found");
            System.out.println(e.getMessage());
            return false;
        }
    }

    boolean loadTemp() {
        String fileName1 = "Temp.csv";
        System.out.println("Loading " + fileName1);
        File file1 = new File(fileName1);
        try {
            Scanner inputStream = new Scanner(file1);
            while (inputStream.hasNext()) {
                String data = inputStream.next();
                data = data.replaceAll("_", " ");
                String[] dataSplitted = data.split(",");
                if (!dataSplitted[0].equals("Action")) {
                    String action = "";
                    String userId = "";
                    User user = null;
                    String branchName = "";
                    Branch branch = null;
                    String roleName = "";
                    Role role = null;
                    String adminId;
                    Admin admin = null;
                    ArrayList<Double> limitsArray = null;
                    String newName = "";
                    String newBranchAddress = "";
                    try {
                        action = dataSplitted[0];
                        userId = dataSplitted[1];
                        user = findUserById(userId);
                        branchName = dataSplitted[2];
                        branch = findBranchByName(branchName);
                        roleName = dataSplitted[3];
                        role = findRoleByName(roleName);
                        adminId = dataSplitted[4];
                        admin = findAdminById(adminId);
                        limitsArray = new ArrayList<>(5);
                        Double limit = 0.0;
                        for (int i = 5; i < 10; i++) {
                            try {
                                limit = Double.parseDouble(dataSplitted[i]);
                            } catch (NumberFormatException e) {
                                System.out.println("Error while reading limit double " + e.getMessage());
                            }
                            limitsArray.add(limit);
                        }
                        newName = dataSplitted[10];
                        newBranchAddress = dataSplitted[11];
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println(e.getMessage());
                    }
                    if (admin != null) {
                        switch (action) {
                            case "Add":
//                        Add new user
                                if (!userId.equals("")) {
                                    Character type = userId.charAt(0);
                                    if (!newName.equals("")) {
                                        if (type.equals('A')) {
                                            Admin admin1 = new Admin(newName);
                                            admin1.setId(userId);
                                            activeAdmin.loadTempUsers("Add", admin1, null, null, admin);
                                        } else if ((type.equals('M')) && (branch != null)) {
                                            Manager manager = new Manager(newName);
                                            manager.setId(userId);
                                            activeAdmin.loadTempUsers("Add", manager, branch, null, admin);
                                        } else if ((type.equals('E')) && (branch != null) && (role != null)) {
                                            Employee employee = new Employee(newName);
                                            employee.setId(userId);
                                            activeAdmin.loadTempUsers("Add", employee, branch, role, admin);
                                        } else {
                                            System.out.println("Wrong add user input");
                                        }
                                    } else {
                                        System.out.println("New name missing");
                                    }
                                }
//                        Add new branch
                                else if ((userId.equals("")) && (!branchName.equals("")) && (!newBranchAddress.equals(""))) {
                                    Branch newBranch = new Branch(branchName, newBranchAddress);
                                    activeAdmin.loadTempBranches("Add", newBranch, null, newBranchAddress, admin);
                                }
//                            Add new role
                                else if ((userId.equals("")) && (!roleName.equals(""))) {
                                    Role newRole = new Role(roleName);
//                                newRole.setLimits(limitsArray);
                                    activeAdmin.loadTempRoles("Add", newRole, limitsArray, admin);
                                }
                                break;
                            case "Modify":
//                            Modify user
                                if (!userId.equals("")) {
                                    Character type = userId.charAt(0);
                                    if (type.equals('M')) {
                                        Manager manager = (Manager) user;
                                        if ((manager != null) && (branch != null)) {
                                            activeAdmin.loadTempUsers("Modify", manager, branch, null, admin);
                                        }
                                    } else if (type.equals('E')) {
                                        Employee employee = (Employee) user;
                                        if ((employee != null) && (branch != null) && (role != null)) {
                                            activeAdmin.loadTempUsers("Modify", employee, branch, role, admin);
                                        }
                                    } else {
                                        System.out.println("Wrong user type");
                                    }
//                                Modify branch
                                } else if ((userId.equals("")) && (branch != null)) {
                                    activeAdmin.loadTempBranches("Modify", branch, newName, newBranchAddress, admin);
//                                Modify role
                                } else if ((userId.equals("")) && (role != null)) {
                                    activeAdmin.loadTempRoles("Modify", role, limitsArray, admin);
                                }
                                break;
                            case "Remove":
//                            Remove user
                                if (!userId.equals("")) {
                                    Character type = userId.charAt(0);
                                    if (type.equals('M')) {
                                        Manager manager = (Manager) user;
                                        if ((manager != null) && (branch != null)) {
                                            activeAdmin.loadTempUsers("Remove", manager, branch, null, admin);
                                        }
                                    } else if (type.equals('E')) {
                                        Employee employee = (Employee) user;
                                        if ((employee != null) && (branch != null) && (role != null)) {
                                            activeAdmin.loadTempUsers("Remove", employee, branch, role, admin);
                                        }
                                    } else {
                                        System.out.println("Wrong user type");
                                    }
//                                Modify branch
                                } else if ((userId.equals("")) && (branch != null)) {
                                    activeAdmin.loadTempBranches("Remove", branch, null, null, admin);
//                                Modify role
                                } else if ((userId.equals("")) && (role != null)) {
                                    activeAdmin.loadTempRoles("Remove", role, limitsArray, admin);
                                }
                                break;
                        }
                    } else {
                        System.out.println("Incorrect admin");
                    }
                }
            }
            System.out.println(fileName1 + " loaded successfully");
            inputStream.close();
            return true;
        } catch (FileNotFoundException e) {
            System.out.println(fileName1 + " not found");
            System.out.println(e.getMessage());
            return false;
        }
    }

    void loadAll() {
        loadBranches();
        loadRoles();
        loadUsers();
    }
}


