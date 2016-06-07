package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

import static sample.Main.*;

public class Controller {

    //    log in screen
    @FXML
    TextField passwordField;
    @FXML
    TextField userNameField;
    @FXML
    Button loginButton;
    @FXML
    Label loginLabel;


    static Label downLabel = new Label();
    private PasswordChange passwordChange = new PasswordChange();

    @FXML
    private void login(ActionEvent event) throws Exception {
        User userType;
        Object outcome = userLogin(userNameField.getText().toUpperCase(), passwordField.getText());
        if (outcome == null) {
            System.out.println("Login incorrect");
            loginLabel.setText("Invalid user name");
        } else if (outcome.getClass().equals(String.class)) {
            System.out.println(outcome);
            loginLabel.setText((String) outcome);
        } else {
            ((Node) (event.getSource())).getScene().getWindow().hide();
            if (outcome.getClass().equals(Admin.class)) {
                System.out.println("Admin logging in");
                userType = (Admin) outcome;
                activeAdmin = (Admin) outcome;
                program.loadTemp();
            } else if (outcome.getClass().equals(Manager.class)) {
                System.out.println("Manager logging in");
                userType = (Manager) outcome;
                activeManager = (Manager) outcome;
                program.loadClients();
                program.loadTransactions();
                program.loadTempTransactions();
            } else {
                System.out.println("Employee logging in");
                userType = (Employee) outcome;
                activeEmployee = (Employee) outcome;
                program.loadClients();
                program.loadTransactions();
                program.loadTempTransactions();
            }
            program.saveUsers();
            mainMenu(userType);
        }
    }

    private Object userLogin(String findId, String password) {
        boolean login = true;
        Admin admin = program.findAdminById(findId);
        if (admin != null) {
            admin.setAttempts(admin.getAttempts() + 1);
            if (admin.getAttempts() < 4) {
                System.out.println(admin.getAttempts());
                if (admin.getPassword().equals(password)) {
                    System.out.println("Password correct");
                    admin.setAttempts(0);
                    String oldPass = admin.getPassword();
                    if (admin.isChangePassword()) {
                        login = false;
                        passwordChange.change(admin);
                    }
                    if (!login)
                        if (!oldPass.equals(admin.getPassword()))
                            login = true;
                    if (login)
                        return admin;
                }
                return "Invalid password";
            } else {
                return "Too many failed attempts. User is disabled";
            }
        }
        Employee employee = program.findEmployeeById(findId);
        if (employee != null) {
            employee.setAttempts(employee.getAttempts() + 1);
            if (employee.getAttempts() < 4) {
                System.out.println(employee.getAttempts());
                if (employee.getPassword().equals(password)) {
                    System.out.println("Password correct");
                    employee.setAttempts(0);
                    String oldPass = employee.getPassword();
                    if (employee.isChangePassword()) {
                        login = false;
                        passwordChange.change(employee);
                    }
                    if (!login)
                        if (!oldPass.equals(employee.getPassword()))
                            login = true;
                    if (login)
                        return employee;
                }
                return "Invalid password";
            } else {
                return "Too many failed attempts. User is disabled";
            }
        }
        Manager manager;
        for (Branch branch : program.branches) {
            manager = branch.getManager();
            if (manager != null) {
                if (manager.getId().equals(findId)) {
                    manager.setAttempts(manager.getAttempts() + 1);
                    if (manager.getAttempts() < 4) {
                        System.out.println(manager.getAttempts());
                        if (manager.getPassword().equals(password)) {
                            System.out.println("Password correct");
                            manager.setAttempts(0);
                            System.out.println(manager.getName() + " found");
                            String oldPass = manager.getPassword();
                            if (manager.isChangePassword()) {
                                login = false;
                                passwordChange.change(manager);
                            }
                            if (!login)
                                if (!oldPass.equals(manager.getPassword()))
                                    login = true;
                            if (login)
                                return manager;
                        }
                        return "Invalid password";
                    } else {
                        return "Too many failed attempts. User is disabled";
                    }
                }
            }
        }
        return null;
    }

    private void mainMenu(Object userType) throws IOException {
        Stage primaryStage = new Stage();
        BorderPane borderPane = new BorderPane();
        Menu fileMenu = new Menu("File");
        MenuItem saveAndQuit = new MenuItem("Save and Quit");
        Menu helpMenu = new Menu("Help");
        MenuItem description = new MenuItem("Description");
        fileMenu.getItems().addAll(saveAndQuit);
        helpMenu.getItems().addAll(description);
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, helpMenu);
        primaryStage.setTitle("Amber Gold");
        borderPane.setTop(menuBar);
        TreeView<String> leftTree;
        borderPane.setBottom(downLabel);
        BorderPane.setAlignment(downLabel, Pos.CENTER);
        borderPane.setCenter(null);

        if (userType.getClass().equals(Admin.class)) {
            leftTree = activeAdmin.adminTreeView();
            downLabel.setText("Welcome " + activeAdmin.getName());
            borderPane.setLeft(leftTree);
            leftTree.getSelectionModel().selectedItemProperty()
                    .addListener((v, oldValue, newValue) -> {
                        if (newValue.getValue() != null) {
                            downLabel.setText("");
                            borderPane.setCenter(null);
                            System.out.println(newValue.getValue());
                            switch (newValue.getValue()) {
                                case "Add user":
                                    borderPane.setCenter(activeAdmin.addUser());
                                    break;
                                case "Modify user":
                                    borderPane.setCenter(activeAdmin.modifyUser());
                                    break;
                                case "List users":
                                    borderPane.setCenter(activeAdmin.listUsers());
                                    break;
                                case "Remove user":
                                    borderPane.setCenter(activeAdmin.removeUser());
                                    break;
                                case "Transfer managers":
                                    borderPane.setCenter(activeAdmin.transferManagers());
                                    break;
                                case "Password reset":
                                    borderPane.setCenter(activeAdmin.reset());
                                    break;
                                case "Authorize user":
                                    borderPane.setCenter(activeAdmin.authorizeUser());
                                    break;
                                case "Add role":
                                    borderPane.setCenter(activeAdmin.addRole());
                                    break;
                                case "Modify role":
                                    borderPane.setCenter(activeAdmin.modifyRole());
                                    break;
                                case "List roles":
                                    borderPane.setCenter(activeAdmin.listRole());
                                    break;
                                case "Remove role":
                                    borderPane.setCenter(activeAdmin.removeRole());
                                    break;
                                case "Authorize role":
                                    borderPane.setCenter(activeAdmin.authorizeRole());
                                    break;
                                case "Add branch":
                                    borderPane.setCenter(activeAdmin.addBranch());
                                    break;
                                case "Modify branch":
                                    borderPane.setCenter(activeAdmin.modifyBranch());
                                    break;
                                case "List branch":
                                    borderPane.setCenter(activeAdmin.listBranches());
                                    break;
                                case "Remove branch":
                                    borderPane.setCenter(activeAdmin.removeBranch());
                                    break;
                                case "Authorize branch":
                                    borderPane.setCenter(activeAdmin.authorizeBranch());
                                    break;
                            }
                        }
                    });
        } else if (userType.getClass().equals(Employee.class)) {
            leftTree = activeEmployee.employeeTreeView();
            downLabel.setText("Welcome " + activeEmployee.getName());
            borderPane.setLeft(leftTree);
            leftTree.getSelectionModel().selectedItemProperty()
                    .addListener((v, oldValue, newValue) -> {
                        if (newValue.getValue() != null) {
                            downLabel.setText("");
                            borderPane.setCenter(null);
                            System.out.println(newValue.getValue());
                            switch (newValue.getValue()) {
                                case "Add a new client":
                                    borderPane.setCenter(activeEmployee.addNewClient());
                                    break;
                                case "Modify client's details":
                                    borderPane.setCenter(activeEmployee.existingClient("Modify"));
                                    break;
                                case "View client's details":
                                    borderPane.setCenter(activeEmployee.existingClient("View"));
                                    break;
                                case "Remove a client":
                                    borderPane.setCenter(activeEmployee.existingClient("Remove"));
                                    break;
                                case "List all clients":
                                    borderPane.setCenter(activeEmployee.listClients());
                                    break;
                                case "Add a client transaction":
                                    borderPane.setCenter(activeEmployee.addClientTransaction());
                                    break;
                                case "Add a client deposit":
                                    borderPane.setCenter(activeEmployee.addClientDeposit());
                                    break;
                                case "View my access":
                                    borderPane.setCenter(activeEmployee.viewAccess());
                                    break;
                                case "Create a report":
                                    borderPane.setCenter(activeEmployee.createReport());
                                    break;
                            }
                        }
                    });
        } else if (userType.getClass().equals(Manager.class)) {
            leftTree = activeManager.managerTreeView();
            downLabel.setText("Welcome " + activeManager.getName());
            borderPane.setLeft(leftTree);

            leftTree.getSelectionModel().selectedItemProperty()
                    .addListener((v, oldValue, newValue) -> {
                        if (newValue.getValue() != null) {
                            System.out.println(newValue.getValue());
                            switch (newValue.getValue()) {
                                case "Approve/reject transactions":
                                    borderPane.setCenter(activeManager.approveTempTransactions());
                                    break;
                                case "View your branch":
                                    borderPane.setCenter(activeManager.viewBranch());
                                    break;
                                case "Add a branch transaction":
                                    borderPane.setCenter(activeManager.addBranchTransaction());
                                    break;
                                case "List all clients in the branch":
                                    borderPane.setCenter(activeManager.viewClients());
                                    break;
                                case "List all employees in the branch":
                                    borderPane.setCenter(activeManager.viewEmployees());
                                    break;
                                case "Generate a branch report":
                                    borderPane.setCenter(activeManager.createReport());
                                    break;
                            }
                        }
                    });
        }

        primaryStage.setScene(new Scene(borderPane, 800, 600));
        primaryStage.setResizable(false);
        primaryStage.show();

        description.setOnAction(event -> infoBox());

        saveAndQuit.setOnAction(e->{
            if (activeAdmin != null)
                program.saveAdmin();
            else if (activeEmployee != null)
                program.saveEmployee();
            else if (activeManager != null)
                program.saveManager();
            primaryStage.close();
        });

        primaryStage.setOnCloseRequest(e -> {
            if (activeAdmin != null)
                program.saveAdmin();
            else if (activeEmployee != null)
                program.saveEmployee();
            else if (activeManager != null)
                program.saveManager();

        });
    }

    private void infoBox() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Aplikacja Amber Gold");
        alert.setHeaderText("Założenia aplikacji");
        alert.setContentText("Login:"+ "\n ma byc unikalny login i haslo, przy tworzeniu robi sie haslo new, \n" +
                "przy 1 loginie system wymusza zmiane hasla 3 niepoprawne hasla i konto sie blokuje, mozna wystawic reset i enable,\n" +
                " dostaniesz nowe randomowe haslo i reset prob, login generuje sie: Pierwsza litera typa, Pierwsza litera imienia, 3 losowe cyfry \n\n" +
                "Admin:\n " +
                "tworzy i modyfikuje uzytkownikow, nie moze siebie zmienic, robi resety hasla i loginow,\n" +
                "dodaje nowe role, userow i brancze, usuwa wszystko, niektore akcje wymagaja autoryzacji innego admina \n" +
                "ale zmiana imienia nie wymaga autoryzacji\n\n " +
                "Manager: przypisany do brancza, przymuje i wydaje w branczu kase, robi raporty branczowe, \n" +
                "duze transakcje maja byc autoryzowane przez managera\n\n" +
                "Employee: przypisany do brancza, dodaje i usuwa klientow i ich transakcje, robi raporty z nich, \n" +
                "musi miec przypisana jakas role, limity na orgach: jest 5, kazdy odpowiada za wykonywanie transakcji o dopuszczalnym limicie,\n" +
                "duze transakcje maja byc autoryzowane przez managera\n\n" +
                "Role: kazda rola ma swoje limity transakcji \nLimity: \n1 - deposit, \n2 - withdrawal, \n3 - transfer, \n4 - payment, \n5 - online\n\n" +
                "Branch:\n ma managera, employees i klientow,\n ma okreslona sume gotowki na skladzie\n\n" +
                "Report:\n mozna wyswietlic na ekranie a potem do pliku CSV ");
        alert.showAndWait();
    }
}
