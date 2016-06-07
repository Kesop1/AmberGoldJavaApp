package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static sample.Controller.downLabel;
import static sample.Main.activeAdmin;
import static sample.Main.program;


public class Admin extends User {
    private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static SecureRandom rnd = new SecureRandom();
    ArrayList<TempUser> tempUsers = new ArrayList<>();
    ArrayList<TempRole> tempRoles = new ArrayList<>();
    ArrayList<TempBranch> tempBranches = new ArrayList<>();

    public Admin(String name) {
        super(name, "A");
        tempUsers = new ArrayList<>();
        tempRoles = new ArrayList<>();
        tempBranches = new ArrayList<>();
    }


//    ++++++++++++++++++++++++++++++++   PASSWORD   +++++++++++++++++++++++++++++

    private String passwordReset(User user) {
        String randomPass = randomString(8);
        user.setPassword(randomPass);
        return randomPass;
    }

    private String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

//    ________________ TREE VIEW _________________________


    TreeView<String> adminTreeView() {
        TreeItem<String> root, adminBranch, userBranch, roleBranch, branchBranch;
        root = new TreeItem<>();
        root.setExpanded(true);
        adminBranch = makeBranch("Admin", root);

        userBranch = makeBranch("User", adminBranch);
        makeBranch("Add user", userBranch);
        makeBranch("Modify user", userBranch);
        makeBranch("List users", userBranch);
        makeBranch("Remove user", userBranch);
        makeBranch("Transfer managers", userBranch);
        makeBranch("Password reset", userBranch);
        makeBranch("Authorize user", userBranch);

        roleBranch = makeBranch("Role", adminBranch);
        makeBranch("Add role", roleBranch);
        makeBranch("Modify role", roleBranch);
        makeBranch("List roles", roleBranch);
        makeBranch("Remove role", roleBranch);
        makeBranch("Authorize role", roleBranch);

        branchBranch = makeBranch("Branch", adminBranch);
        makeBranch("Add branch", branchBranch);
        makeBranch("Modify branch", branchBranch);
        makeBranch("List branch", branchBranch);
        makeBranch("Remove branch", branchBranch);
        makeBranch("Authorize branch", branchBranch);

        TreeView<String> treeView = new TreeView<>(root);
        treeView.setShowRoot(false);
        treeView.setMaxWidth(200);
        adminBranch.setExpanded(true);

        return treeView;
    }

    private TreeItem<String> makeBranch(String title, TreeItem<String> parent) {
        TreeItem<String> item = new TreeItem<>(title);
        item.setExpanded(false);
        parent.getChildren().add(item);
        return item;
    }


//    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%   Add User   %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    GridPane addUser() {
        downLabel.setText("Adding a new user");
        GridPane gridPane = new GridPane();
        Label mainLabel = new Label("Add a new user");

        Label userTypeLabel = new Label("Please choose the user type:");
        ChoiceBox<String> userTypeChoice = new ChoiceBox<>();
        userTypeChoice.getItems().addAll("Admin", "Manager", "Employee");

        Label userNameLabel = new Label("Please insert the user's name:");
        TextField nameField = new TextField();

        Label branchLabel = new Label("Please choose a branch for the new user:");
        ChoiceBox<String> chooseBranch = new ChoiceBox<>();
        chooseBranch.getItems().addAll(program.allBranches());

        Label roleLabel = new Label("Please choose a role:");
        ChoiceBox<String> roleChoice = new ChoiceBox<>();
        roleChoice.getItems().addAll(program.allRoles());

        Button addButton = new Button("Add");
        Button cancelButton = new Button("Cancel");


        GridPane.setHalignment(mainLabel, HPos.CENTER);
        GridPane.setValignment(mainLabel, VPos.CENTER);
        mainLabel.setFont(Font.font(20));
        GridPane.setColumnSpan(mainLabel, 2);
        gridPane.getColumnConstraints().add(new ColumnConstraints(300));
        gridPane.getColumnConstraints().add(new ColumnConstraints(300));
        GridPane.setHalignment(addButton, HPos.CENTER);
        GridPane.setHalignment(cancelButton, HPos.CENTER);


        GridPane.setConstraints(mainLabel, 0, 0);
        GridPane.setConstraints(userTypeLabel, 0, 1);
        GridPane.setConstraints(userTypeChoice, 1, 1);
        GridPane.setConstraints(userNameLabel, 0, 2);
        GridPane.setConstraints(nameField, 1, 2);
        GridPane.setConstraints(branchLabel, 0, 3);
        GridPane.setConstraints(chooseBranch, 1, 3);
        GridPane.setConstraints(roleLabel, 0, 4);
        GridPane.setConstraints(roleChoice, 1, 4);
        GridPane.setConstraints(cancelButton, 0, 5);
        GridPane.setConstraints(addButton, 1, 5);

        gridPane.getChildren().addAll(mainLabel, userTypeLabel, userTypeChoice, userNameLabel, nameField, branchLabel, chooseBranch, addButton, cancelButton);


        userTypeChoice.getSelectionModel().selectedItemProperty().addListener(
                (v, oldValue, newValue) -> {
                    userTypeChoice.setDisable(true);
                    if (userTypeChoice.getValue().equals("Admin"))
                        gridPane.getChildren().removeAll(branchLabel, chooseBranch);
                    else if (userTypeChoice.getValue().equals("Employee"))
                        gridPane.getChildren().addAll(roleLabel, roleChoice);
                    addButton.setOnAction(e -> {
                        String message;
                        String userName = nameField.getText();
                        if (!userName.equals("")) {
                            TempUser tempUser = findTempUserByName(userName);
                            if (userTypeChoice.getValue().equals("Admin")) {
                                if ((program.findAdminByName(userName) != null) ||
                                        ((tempUser != null) && (tempUser.getTempUser().getUserType().equals("A")))) {
                                    message = "Please insert a unique name";
                                } else {
                                    Admin admin = new Admin(userName);
                                    tempUsers.add(new TempUser("Add", admin, null, null, activeAdmin));
                                    gridPane.getChildren().clear();
                                    message = admin.getId() + " is awaiting authorization";
                                }
                            } else {
                                Branch branch = program.findBranchByName(chooseBranch.getValue());
                                if (branch != null) {
                                    if (userTypeChoice.getValue().equals("Manager")) {
                                        if ((program.findManagerByName(userName) != null) ||
                                                ((tempUser != null) && (tempUser.getTempUser().getUserType().equals("M")))) {
                                            message = "Please insert a unique name";
                                        } else {
                                            if (branch.getManager() == null) {
                                                Manager manager = new Manager(userName);
                                                if (onlyManagerToBranch(branch, manager)) {
                                                    tempUsers.add(new TempUser("Add", manager, branch, null, activeAdmin));
                                                    message = manager.getId() + " is awaiting authorization";
                                                    gridPane.getChildren().clear();
                                                } else message = branch.getName() + " already has a manager";
                                            } else message = branch.getName() + " already has a manager";
                                        }
                                    } else {
                                        if ((program.findEmployeeByName(userName) != null) ||
                                                ((tempUser != null) && (tempUser.getTempUser().getUserType().equals("E")))) {
                                            message = "Please insert a unique name";
                                        } else {
                                            Role role = program.findRoleByName(roleChoice.getValue());
                                            if (role != null) {
                                                Employee employee = new Employee(userName);
                                                tempUsers.add(new TempUser("Add", employee, branch, role, activeAdmin));
                                                gridPane.getChildren().clear();
                                                message = employee.getId() + " is awaiting authorization";
                                            } else message = "Please choose a role";
                                        }
                                    }
                                } else message = "Please fill in all the fields";
                            }
                        } else message = "Please fill in all the fields";
                        downLabel.setText(message);
                        System.out.println(message);
                    });
                });
        cancelButton.setOnAction(e -> {
            gridPane.getChildren().clear();
            downLabel.setText("What do you want to do now?");
        });
        return gridPane;
    }

    //    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%   Modify User   %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    GridPane modifyUser() {
        downLabel.setText("Modify a user");
        GridPane gridPane = new GridPane();
        Label mainLabel = new Label("Modify a user");

        Label ussrTypeLabel = new Label("Please choose the user type:");
        ChoiceBox<String> userTypeChoice = new ChoiceBox<>();
        userTypeChoice.getItems().addAll("Admin", "Manager", "Employee");

        Label userChooseLabel = new Label("Choose a user");
        ChoiceBox<String> userChooseChoice = new ChoiceBox<>();

        Label allBranchesLabel = new Label("Choose a branch");
        ChoiceBox<String> allBranchesList = new ChoiceBox<>();
        allBranchesList.getItems().add("All");
        allBranchesList.getItems().addAll(program.allBranches());

        TextField nameField = new TextField();
        Label nameLabel = new Label("Please insert the user's name:");

        TextField idField = new TextField();
        Label idLabel = new Label("ID");
        idField.setEditable(false);

        Label roleLabel = new Label("Role");
        ChoiceBox<String> roleChoice = new ChoiceBox<>();
        roleChoice.getItems().addAll(program.allRoles());

        Label newBranchLabel = new Label("Choose a new branch");
        ChoiceBox<String> newBranchList = new ChoiceBox<>();
        newBranchList.getItems().addAll(program.allBranches());

        Button confirmButton = new Button("Confirm");
        Button cancelButton = new Button("Cancel");


        GridPane.setHalignment(mainLabel, HPos.CENTER);
        GridPane.setValignment(mainLabel, VPos.CENTER);
        mainLabel.setFont(Font.font(20));
        GridPane.setColumnSpan(mainLabel, 2);
        gridPane.getColumnConstraints().add(new ColumnConstraints(300));
        gridPane.getColumnConstraints().add(new ColumnConstraints(300));
        GridPane.setHalignment(confirmButton, HPos.CENTER);
        GridPane.setHalignment(cancelButton, HPos.CENTER);

        GridPane.setConstraints(mainLabel, 0, 0);
        GridPane.setConstraints(ussrTypeLabel, 0, 1);
        GridPane.setConstraints(userTypeChoice, 1, 1);
        GridPane.setConstraints(allBranchesLabel, 0, 2);
        GridPane.setConstraints(allBranchesList, 1, 2);
        GridPane.setConstraints(userChooseLabel, 0, 3);
        GridPane.setConstraints(userChooseChoice, 1, 3);
        GridPane.setConstraints(nameLabel, 0, 4);
        GridPane.setConstraints(nameField, 1, 4);
        GridPane.setConstraints(idLabel, 0, 5);
        GridPane.setConstraints(idField, 1, 5);
        GridPane.setConstraints(newBranchLabel, 0, 6);
        GridPane.setConstraints(newBranchList, 1, 6);
        GridPane.setConstraints(roleLabel, 0, 7);
        GridPane.setConstraints(roleChoice, 1, 7);
        GridPane.setConstraints(confirmButton, 1, 8);
        GridPane.setConstraints(cancelButton, 0, 8);

        gridPane.getChildren().addAll(mainLabel, ussrTypeLabel, userTypeChoice);

        userTypeChoice.getSelectionModel().selectedItemProperty().addListener(
                (v, oldValue, newValue) -> {
                    userTypeChoice.setDisable(true);
                    gridPane.getChildren().addAll(userChooseLabel, userChooseChoice);
                    switch (newValue) {
                        case "Admin":
                            userChooseChoice.getItems().addAll(program.allAdmins());
                            userChooseChoice.getSelectionModel().selectedItemProperty().addListener(
                                    (v1, oldValue1, newValue1) -> {
                                        userChooseChoice.setDisable(true);
                                        Admin admin = program.findAdminByName(newValue1);
                                        gridPane.getChildren().addAll(nameField, nameLabel, idLabel, idField, confirmButton, cancelButton);
                                        nameField.setText(admin.getName());
                                        idField.setText(admin.getId());
                                        confirmButton.setOnAction(e -> {
                                            String message;
                                            if (admin.equals(activeAdmin)) {
                                                message = "You are not allowed to modify your own details";
                                            } else {
                                                if (findTempUserById(admin.getId()) != null) {
                                                    message = admin.getName() + " is already awaiting authorization";
                                                } else {
                                                    if ((program.findAdminByName(nameField.getText()) != null) ||
                                                            ((findTempUserByName(nameField.getText()) != null) &&
                                                                    (findTempUserByName(nameField.getText()).getTempUser().getUserType().equals("A")))) {
                                                        message = "Admin with this name already exists";
                                                    } else {
//                                                    name change doesn't require authorization
                                                        message = admin.getName() + "'s name modified to " + nameField.getText();
                                                        admin.setName(nameField.getText());
                                                        gridPane.getChildren().clear();
                                                    }
                                                }
                                            }
                                            downLabel.setText(message);
                                            System.out.println(message);
                                        });
                                    });
//                                                              Manager modify
                            break;
                        case "Manager":
                            gridPane.getChildren().addAll(allBranchesLabel, allBranchesList);
                            allBranchesList.setValue("All");
                            userChooseChoice.getItems().addAll(program.allManagers());
                            allBranchesList.getSelectionModel().selectedItemProperty().addListener(
                                    (v1, oldValue1, newValue1) -> {
                                        userChooseChoice.getItems().addAll(program.allManagers());
                                        Branch branch = program.findBranchByName(newValue1);
                                        if (branch == null) {
                                            userChooseChoice.getItems().clear();
                                            userChooseChoice.getItems().addAll(program.allManagers());
                                        } else {
                                            userChooseChoice.getItems().clear();
                                            if (branch.getManager() != null)
                                                userChooseChoice.getItems().add(branch.getManager().getName());
                                        }
                                        userChooseChoice.getSelectionModel().selectedItemProperty().addListener(
                                                (v2, oldValue2, newValue2) -> {
                                                    Manager manager = program.findManagerByName(newValue2);
                                                    userChooseChoice.setDisable(true);
                                                    allBranchesList.setDisable(true);
                                                    gridPane.getChildren().addAll(nameLabel, nameField, idLabel, idField,
                                                            newBranchLabel, newBranchList, confirmButton, cancelButton);
                                                    Branch branchCurrent = program.findBranchByManager(manager);
                                                    nameField.setText(manager.getName());
                                                    idField.setText(manager.getId());
                                                    newBranchList.setValue(branchCurrent.getName());
                                                    confirmButton.setOnAction(e -> {
                                                        String message;
                                                        if (findTempUserById(manager.getId()) != null) {
                                                            message = manager.getName() + " is already awaiting authorization";
                                                        } else {
                                                            Branch newBranch = program.findBranchByName(newBranchList.getValue());
                                                            if (!manager.getName().equals(nameField.getText())) {
                                                                if ((program.findManagerByName(nameField.getText()) != null)
                                                                        || ((findTempUserByName(nameField.getText()) != null)
                                                                        && (findTempUserByName(nameField.getText()).getTempUser().getUserType().equals("M")))) {
                                                                    message = nameField.getText() + " already exists";
                                                                } else {
                                                                    message = manager.getName() + "'s name modified to " + nameField.getText();
                                                                    manager.setName(nameField.getText());   //zmiana nazwiska nie wymaga autoryzacji
                                                                    gridPane.getChildren().clear();
                                                                }
                                                            } else {
                                                                message = "Nothing to change";
                                                            }
                                                            if (!branchCurrent.equals(newBranch)) {
                                                                if ((newBranch.getManager() == null) && (onlyManagerToBranch(newBranch, manager))) {
                                                                    message = newValue2 + " is awaiting authorization";
                                                                    tempUsers.add(new TempUser("Modify", manager, newBranch, null, activeAdmin));
                                                                    gridPane.getChildren().clear();
                                                                } else {
                                                                    message = newBranchList.getValue() + " already has a manager";
                                                                }
                                                            }

                                                        }
                                                        downLabel.setText(message);
                                                        System.out.println(message);
                                                    });
                                                });
                                    });

//                                                              Employee modify
                            break;
                        default:
                            gridPane.getChildren().addAll(allBranchesLabel, allBranchesList);
                            allBranchesList.setValue("All");
                            userChooseChoice.getItems().addAll(program.allEmployees());
                            allBranchesList.getSelectionModel().selectedItemProperty().addListener(
                                    (v1, oldValue1, newValue1) -> {
                                        Branch branch = program.findBranchByName(newValue1);
                                        if (branch == null) {
                                            userChooseChoice.getItems().clear();
                                            userChooseChoice.getItems().addAll(program.allEmployees());
                                        } else {
                                            userChooseChoice.getItems().clear();
                                            userChooseChoice.getItems().addAll(branch.employeesInBranch());
                                        }
                                        userChooseChoice.getSelectionModel().selectedItemProperty().addListener(
                                                (v2, oldValue2, newValue2) -> {
                                                    Employee employee = program.findEmployeeByName(newValue2);
                                                    userChooseChoice.setDisable(true);
                                                    allBranchesList.setDisable(true);
                                                    gridPane.getChildren().removeAll(nameLabel, nameField, idLabel, idField,
                                                            roleLabel, roleChoice, newBranchLabel, newBranchList, confirmButton, cancelButton);
                                                    gridPane.getChildren().addAll(nameLabel, nameField, idLabel, idField,
                                                            roleLabel, roleChoice, newBranchLabel, newBranchList, confirmButton, cancelButton);
                                                    Branch branchCurrent = program.findBranchByEmployee(employee);
                                                    nameField.setText(employee.getName());
                                                    idField.setText(employee.getId());
                                                    roleChoice.setValue(employee.getRole().getName());
                                                    newBranchList.setValue(branchCurrent.getName());
                                                    confirmButton.setOnAction(e -> {
                                                        String message = "Modify " + employee.getName();
                                                        if (findTempUserById(employee.getId()) != null) {
                                                            message = employee.getName() + " is already awaiting authorization";
                                                        } else {
                                                            Branch newBranch = program.findBranchByName(newBranchList.getValue());
                                                            Role newRole = program.findRoleByName(roleChoice.getValue());
                                                            if ((employee.getName().equals(nameField.getText())) &&
                                                                    (branchCurrent.equals(newBranch)) && (employee.getRole().equals(newRole))) {
                                                                message = "Nothing to change";
                                                            } else {
                                                                if ((program.findEmployeeByName(nameField.getText()) != null) ||
                                                                        ((findTempUserByName(nameField.getText()) != null) &&
                                                                                (findTempUserByName(nameField.getText()).getTempUser().getUserType().equals("A")))) {
                                                                    employee.setName(nameField.getText());
                                                                    message = employee.getName() + "'s name modified to " + nameField.getText();
                                                                } else {
                                                                    message = nameField.getText() + " already exists";
                                                                }
                                                                if ((!branchCurrent.equals(newBranch)) || (!employee.getRole().equals(newRole))) {
                                                                    message = employee.getName() + " is awaiting authorization";
                                                                    tempUsers.add(new TempUser("Modify", employee, newBranch, newRole, activeAdmin));
                                                                }
                                                                gridPane.getChildren().clear();
                                                            }
                                                        }
                                                        downLabel.setText(message);
                                                        System.out.println(message);
                                                    });
                                                });
                                    });
                            break;
                    }
                });
        cancelButton.setOnAction(e -> {
            downLabel.setText("What do you want to do now?");
            gridPane.getChildren().clear();
        });


        return gridPane;
    }

    //    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%   Transfer managers   %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    GridPane transferManagers() {
        downLabel.setText("Transfer managers");
        GridPane gridPane = new GridPane();
        Label mainLabel = new Label("Transfer managers");

        Label branchLabel = new Label("Choose a branch");
        ChoiceBox<String> branchChoice1 = new ChoiceBox<>();
        branchChoice1.getItems().addAll(program.allBranches());

        ChoiceBox<String> branchChoice2 = new ChoiceBox<>();
        branchChoice2.getItems().addAll(program.allBranches());


        Label managerLabel = new Label("Choose a manager");
        Label managerLabel1 = new Label();

        Label managerLabel2 = new Label();

        Button confirmButton = new Button("Confirm");
        Button cancelButton = new Button("Cancel");


        GridPane.setHalignment(mainLabel, HPos.CENTER);
        GridPane.setValignment(mainLabel, VPos.CENTER);
        mainLabel.setFont(Font.font(20));
        GridPane.setColumnSpan(mainLabel, 3);
        gridPane.getColumnConstraints().add(new ColumnConstraints(200));
        gridPane.getColumnConstraints().add(new ColumnConstraints(200));
        gridPane.getColumnConstraints().add(new ColumnConstraints(200));
        GridPane.setHalignment(confirmButton, HPos.CENTER);
        GridPane.setHalignment(cancelButton, HPos.CENTER);

        GridPane.setConstraints(mainLabel, 0, 0);
        GridPane.setConstraints(branchLabel, 0, 1);
        GridPane.setConstraints(branchChoice1, 1, 1);
        GridPane.setConstraints(branchChoice2, 2, 1);
        GridPane.setConstraints(managerLabel, 0, 2);
        GridPane.setConstraints(managerLabel1, 1, 2);
        GridPane.setConstraints(managerLabel2, 2, 2);

        GridPane.setConstraints(confirmButton, 2, 4);
        GridPane.setConstraints(cancelButton, 0, 4);

        gridPane.getChildren().addAll(mainLabel, branchLabel, branchChoice1);

        branchChoice1.getSelectionModel().selectedItemProperty().addListener(
                (v, oldValue, newValue) -> {
                    Branch branch1 = program.findBranchByName(newValue);
                    if ((branch1 != null) && (branch1.getManager() != null)) {
                        branchChoice1.setDisable(true);
                        managerLabel1.setText(branch1.getManager().getName());
                        gridPane.getChildren().addAll(managerLabel, managerLabel1, branchChoice2);

                        branchChoice2.getSelectionModel().selectedItemProperty().addListener(
                                (v1, oldValue1, newValue1) -> {
                                    Branch branch2 = program.findBranchByName(newValue1);
                                    if ((branch2 != null) && (branch2.getManager() != null) && (branch2 != branch1)) {
                                        branchChoice2.setDisable(true);
                                        managerLabel2.setText(branch2.getManager().getName());
                                        gridPane.getChildren().addAll(managerLabel2, confirmButton, cancelButton);
                                        confirmButton.setOnAction(e -> {
                                            if ((onlyManagerToBranch(branch1, branch1.getManager())) && (onlyManagerToBranch(branch2, branch2.getManager()))
                                                    && (findTempUserById(branch1.getManager().getId()) == null) && (findTempUserById(branch2.getManager().getId()) == null)) {
                                                downLabel.setText("Transfer is awaiting authorization");
                                                System.out.println("Transfer is awaiting authorization");
                                                tempUsers.add(new TempUser("Transfer with:" + branch2.getManager().getId(), branch1.getManager(), branch2, null, activeAdmin));
                                                tempUsers.add(new TempUser("Transfer with:" + branch1.getManager().getId(), branch2.getManager(), branch1, null, activeAdmin));
                                                gridPane.getChildren().clear();
                                            } else downLabel.setText("Cannot perform this operation");
                                        });
                                    } else downLabel.setText("No manager to transfer here");
                                });
                    } else downLabel.setText("No manager here");
                });
        cancelButton.setOnAction(e -> {
            downLabel.setText("What do you want to do now?");
            gridPane.getChildren().clear();
        });


        return gridPane;
    }


    //    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%   List Users   %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    GridPane listUsers() {
        downLabel.setText("List users");
        GridPane gridPane = new GridPane();
        Label mainLabel = new Label("List users");

        Label ussrTypeLabel = new Label("Please choose the user type:");
        ChoiceBox<String> userTypeChoice = new ChoiceBox<>();
        userTypeChoice.getItems().addAll("Admin", "Manager", "Employee");

        Button cancelButton = new Button("Cancel");

        GridPane.setHalignment(mainLabel, HPos.CENTER);
        GridPane.setValignment(mainLabel, VPos.CENTER);
        mainLabel.setFont(Font.font(20));
        GridPane.setColumnSpan(mainLabel, 2);
        gridPane.getColumnConstraints().add(new ColumnConstraints(300));
        gridPane.getColumnConstraints().add(new ColumnConstraints(300));
        GridPane.setHalignment(cancelButton, HPos.CENTER);

        GridPane.setConstraints(mainLabel, 0, 0);
        GridPane.setConstraints(ussrTypeLabel, 0, 1);
        GridPane.setConstraints(userTypeChoice, 1, 1);
        GridPane.setConstraints(cancelButton, 0, 3);

        gridPane.getChildren().addAll(mainLabel, ussrTypeLabel, userTypeChoice, cancelButton);

        userTypeChoice.getSelectionModel().selectedItemProperty().addListener(
                (v, oldValue, newValue) -> {
                    userTypeChoice.setDisable(true);
                    switch (newValue) {
                        case "Admin":
                            TableView<Admin> adminTable = new TableView<>();
                            adminTable = program.adminTableView();
                            GridPane.setConstraints(adminTable, 0, 2);
                            GridPane.setColumnSpan(adminTable, 2);
                            gridPane.getChildren().add(adminTable);
                            break;
                        case "Manager":
                            TableView<Manager> managerTableView = new TableView<>();
                            managerTableView = program.managerTableView(program.allManagersList());
                            GridPane.setConstraints(managerTableView, 0, 2);
                            GridPane.setColumnSpan(managerTableView, 2);
                            gridPane.getChildren().add(managerTableView);
                            break;
                        default:
                            TableView<Employee> employeeTableView = program.employeeTableView(program.allEmployeesList());
                            GridPane.setConstraints(employeeTableView, 0, 2);
                            GridPane.setColumnSpan(employeeTableView, 2);
                            gridPane.getChildren().add(employeeTableView);
                            break;
                    }
                });

        cancelButton.setOnAction(e -> {
            gridPane.getChildren().clear();
            downLabel.setText("What do you want to do now?");
        });

        return gridPane;
    }

    //    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%   Remove User   %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    GridPane removeUser() {
        downLabel.setText("Choose a user");
        GridPane gridPane = new GridPane();
        Label mainLabel = new Label("Remove a user");

        Label ussrTypeLabel = new Label("Please choose the user type:");
        ChoiceBox<String> userTypeChoice = new ChoiceBox<>();
        userTypeChoice.getItems().addAll("Admin", "Manager", "Employee");

        Label userChooseLabel = new Label("Choose a user");
        ChoiceBox<String> userChooseChoice = new ChoiceBox<>();

        Button confirmButton = new Button("Confirm");
        Button cancelButton = new Button("Cancel");

        GridPane.setHalignment(mainLabel, HPos.CENTER);
        GridPane.setValignment(mainLabel, VPos.CENTER);
        mainLabel.setFont(Font.font(20));
        GridPane.setColumnSpan(mainLabel, 2);
        gridPane.getColumnConstraints().add(new ColumnConstraints(300));
        gridPane.getColumnConstraints().add(new ColumnConstraints(300));
        GridPane.setHalignment(confirmButton, HPos.CENTER);
        GridPane.setHalignment(cancelButton, HPos.CENTER);


        GridPane.setConstraints(mainLabel, 0, 0);
        GridPane.setConstraints(ussrTypeLabel, 0, 1);
        GridPane.setConstraints(userTypeChoice, 1, 1);
        GridPane.setConstraints(userChooseLabel, 0, 2);
        GridPane.setConstraints(userChooseChoice, 1, 2);
        GridPane.setConstraints(cancelButton, 0, 3);
        GridPane.setConstraints(confirmButton, 1, 3);

        gridPane.getChildren().addAll(mainLabel, ussrTypeLabel, userTypeChoice);

        userTypeChoice.getSelectionModel().selectedItemProperty().addListener(
                (v, oldValue, newValue) -> {
                    userTypeChoice.setDisable(true);
                    gridPane.getChildren().addAll(userChooseLabel, userChooseChoice);
                    switch (newValue) {
                        case "Admin":
                            userChooseChoice.getItems().addAll(program.allAdmins());
                            userChooseChoice.getSelectionModel().selectedItemProperty().addListener(
                                    (v1, oldValue1, newValue1) -> {
                                        userChooseChoice.setDisable(true);
                                        Admin admin = program.findAdminByName(newValue1);
                                        if (findTempUserById(admin.getId()) != null) {
                                            downLabel.setText(admin.getName() + " is already awaiting authorization");
                                            System.out.println(admin.getName() + " is already awaiting authorization");
                                        } else {
                                            if (admin.equals(activeAdmin)) {
                                                downLabel.setText("You are not allowed to remove your own account");
                                            } else {
                                                gridPane.getChildren().addAll(confirmButton, cancelButton);
                                                confirmButton.setOnAction(e -> {
                                                    downLabel.setText(admin.getName() + " is awaiting authorization");
                                                    System.out.println(admin.getName() + " is awaiting authorization");
                                                    tempUsers.add(new TempUser("Remove", admin, null, null, activeAdmin));
                                                    gridPane.getChildren().clear();
                                                });
                                            }
                                        }
                                    });
                            break;
                        case "Manager":
                            gridPane.getChildren().addAll(confirmButton, cancelButton);
                            userChooseChoice.getItems().addAll(program.allManagers());
                            userChooseChoice.getSelectionModel().selectedItemProperty().addListener(
                                    (v1, oldValue1, newValue1) -> {
                                        userChooseChoice.setDisable(true);
                                        Manager manager = program.findManagerByName(newValue1);
                                        if (findTempUserById(manager.getId()) != null) {
                                            downLabel.setText(manager.getName() + " is already awaiting authorization");
                                            System.out.println(manager.getName() + " is already awaiting authorization");
                                        } else {
                                            confirmButton.setOnAction(e -> {
                                                downLabel.setText(manager.getName() + " is awaiting authorization");
                                                System.out.println(manager.getName() + " is awaiting authorization");
                                                tempUsers.add(new TempUser("Remove", manager, program.findBranchByManager(manager), null, activeAdmin));
                                                gridPane.getChildren().clear();
                                            });
                                        }
                                    });
                            break;
                        default:
                            userChooseChoice.getItems().clear();
                            gridPane.getChildren().addAll(confirmButton, cancelButton);
                            userChooseChoice.getItems().addAll(program.allEmployees());
                            userChooseChoice.getSelectionModel().selectedItemProperty().addListener(
                                    (v1, oldValue1, newValue1) -> {
                                        userChooseChoice.setDisable(true);
                                        Employee employee = program.findEmployeeByName(newValue1);
                                        if (findTempUserById(employee.getId()) != null) {
                                            downLabel.setText(employee.getName() + " is already awaiting authorization");
                                            System.out.println(employee.getName() + " is already awaiting authorization");
                                        } else {
                                            confirmButton.setOnAction(e -> {
                                                downLabel.setText(employee.getName() + " is awaiting authorization");
                                                System.out.println(employee.getName() + " is awaiting authorization");
                                                tempUsers.add(new TempUser("Remove", employee, program.findBranchByEmployee(employee), employee.getRole(), activeAdmin));
                                                gridPane.getChildren().clear();
                                            });
                                        }
                                    });
                            break;
                    }
                });
        cancelButton.setOnAction(e -> {
            downLabel.setText("What do you want to do now?");
            gridPane.getChildren().clear();
        });
        return gridPane;
    }

    //    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%   Reset User   %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    GridPane reset() {
        downLabel.setText("Choose a user");
        GridPane gridPane = new GridPane();
        Label mainLabel = new Label("Reset a user");

        Label ussrTypeLabel = new Label("Please choose the user type:");
        ChoiceBox<String> userTypeChoice = new ChoiceBox<>();
        userTypeChoice.getItems().addAll("Admin", "Manager", "Employee");

        Label userChooseLabel = new Label("Choose a user");
        ChoiceBox<String> userChooseChoice = new ChoiceBox<>();

        Button confirmButton = new Button("Confirm");
        Button cancelButton = new Button("Cancel");

        GridPane.setHalignment(mainLabel, HPos.CENTER);
        GridPane.setValignment(mainLabel, VPos.CENTER);
        mainLabel.setFont(Font.font(20));
        GridPane.setColumnSpan(mainLabel, 2);
        gridPane.getColumnConstraints().add(new ColumnConstraints(300));
        gridPane.getColumnConstraints().add(new ColumnConstraints(300));
        GridPane.setHalignment(confirmButton, HPos.CENTER);
        GridPane.setHalignment(cancelButton, HPos.CENTER);


        GridPane.setConstraints(mainLabel, 0, 0);
        GridPane.setConstraints(ussrTypeLabel, 0, 1);
        GridPane.setConstraints(userTypeChoice, 1, 1);
        GridPane.setConstraints(userChooseLabel, 0, 2);
        GridPane.setConstraints(userChooseChoice, 1, 2);
        GridPane.setConstraints(cancelButton, 0, 3);
        GridPane.setConstraints(confirmButton, 1, 3);

        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();

        gridPane.getChildren().addAll(mainLabel, ussrTypeLabel, userTypeChoice);

        userTypeChoice.getSelectionModel().selectedItemProperty().addListener(
                (v, oldValue, newValue) -> {
                    userTypeChoice.setDisable(true);
                    gridPane.getChildren().addAll(userChooseLabel, userChooseChoice);
                    switch (newValue) {
                        case "Admin":
                            userChooseChoice.getItems().addAll(program.allAdmins());
                            userChooseChoice.getSelectionModel().selectedItemProperty().addListener(
                                    (v1, oldValue1, newValue1) -> {
                                        userChooseChoice.setDisable(true);
                                        Admin admin = program.findAdminByName(newValue1);
                                        if (admin.equals(activeAdmin)) {
                                            downLabel.setText("You are not allowed to reset your own account");
                                            gridPane.getChildren().clear();
                                        } else {
                                            gridPane.getChildren().addAll(confirmButton, cancelButton);
                                            confirmButton.setOnAction(e -> {
                                                String newPass = passwordReset(admin);
                                                downLabel.setText(admin.getName() + "'s new password is: " + newPass);
                                                System.out.println(admin.getName() + "'s new password is: " + newPass);
                                                admin.setChangePassword(true);
                                                content.putString(newPass);
                                                clipboard.setContent(content);
                                                admin.setAttempts(0);
                                                gridPane.getChildren().clear();
                                            });
                                        }
                                    });
                            break;
                        case "Manager":
                            gridPane.getChildren().addAll(confirmButton, cancelButton);
                            userChooseChoice.getItems().addAll(program.allManagers());
                            userChooseChoice.getSelectionModel().selectedItemProperty().addListener(
                                    (v1, oldValue1, newValue1) -> {
                                        userChooseChoice.setDisable(true);
                                        Manager manager = program.findManagerByName(newValue1);
                                        confirmButton.setOnAction(e -> {
                                            String newPass = passwordReset(manager);
                                            downLabel.setText(manager.getName() + "'s new password is: " + newPass);
                                            System.out.println(manager.getName() + "'s new password is: " + newPass);
                                            manager.setChangePassword(true);
                                            content.putString(newPass);
                                            clipboard.setContent(content);
                                            manager.setAttempts(0);
                                            gridPane.getChildren().clear();
                                        });
                                    });
                            break;
                        default:
                            userChooseChoice.getItems().clear();
                            gridPane.getChildren().addAll(confirmButton, cancelButton);
                            userChooseChoice.getItems().addAll(program.allEmployees());
                            userChooseChoice.getSelectionModel().selectedItemProperty().addListener(
                                    (v1, oldValue1, newValue1) -> {
                                        userChooseChoice.setDisable(true);
                                        Employee employee = program.findEmployeeByName(newValue1);
                                        confirmButton.setOnAction(e -> {
                                            String newPass = passwordReset(employee);
                                            downLabel.setText(employee.getName() + "'s new password is: " + newPass);
                                            System.out.println(employee.getName() + "'s new password is: " + newPass);
                                            employee.setChangePassword(true);
                                            content.putString(newPass);
                                            clipboard.setContent(content);
                                            employee.setAttempts(0);
                                            gridPane.getChildren().clear();
                                        });
                                    });
                            break;
                    }
                });
        cancelButton.setOnAction(e -> {
            downLabel.setText("What do you want to do now?");
            gridPane.getChildren().clear();
        });

        return gridPane;
    }

    //    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%   Authorize User   %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    GridPane authorizeUser() {
        downLabel.setText("Choose a user");
        GridPane gridPane = new GridPane();
        Label mainLabel = new Label("Authorize a user");

        Label userChooseLabel = new Label("Choose a user");
        ChoiceBox<User> userChooseChoice = new ChoiceBox<>();

        Label userNameLabel = new Label("User name");
        TextField userNameText = new TextField();
        Label userTypeLabel = new Label("User type");
        TextField userTypeText = new TextField();
        Label userLoginLabel = new Label("User login");
        TextField userLoginText = new TextField();
        Label userBranchLabel = new Label("User branch");
        TextField userBranchText = new TextField();
        Label newUserBranchLabel = new Label(">>");
        TextField newUserBranchText = new TextField();
        Label userRoleLabel = new Label("User role");
        TextField userRoleText = new TextField();
        Label newUserRoleLabel = new Label(">>");
        TextField newUserRoleText = new TextField();
        Label userCreatorLabel = new Label("Action by");
        TextField userCreatorText = new TextField();
        Label actionTypeLabel = new Label("Action type");
        TextField actionTypeText = new TextField();


        Button confirmButton = new Button("Confirm");
        Button cancelButton = new Button("Cancel");
        Button undoButton = new Button("Undo");

        GridPane.setHalignment(mainLabel, HPos.CENTER);
        GridPane.setValignment(mainLabel, VPos.CENTER);
        mainLabel.setFont(Font.font(20));
        GridPane.setColumnSpan(mainLabel, 4);
        gridPane.getColumnConstraints().add(new ColumnConstraints(100));
        gridPane.getColumnConstraints().add(new ColumnConstraints(250));
        gridPane.getColumnConstraints().add(new ColumnConstraints(50));
        gridPane.getColumnConstraints().add(new ColumnConstraints(200));
        GridPane.setHalignment(confirmButton, HPos.CENTER);
        GridPane.setHalignment(undoButton, HPos.CENTER);
        GridPane.setHalignment(cancelButton, HPos.CENTER);
        GridPane.setHalignment(newUserBranchLabel, HPos.CENTER);
        GridPane.setHalignment(newUserRoleLabel, HPos.CENTER);
        userLoginText.setEditable(false);
        userNameText.setEditable(false);
        userTypeText.setEditable(false);
        userBranchText.setEditable(false);
        newUserBranchText.setEditable(false);
        userRoleText.setEditable(false);
        newUserRoleText.setEditable(false);
        userCreatorText.setEditable(false);
        actionTypeText.setEditable(false);


        GridPane.setConstraints(mainLabel, 0, 0);
        GridPane.setConstraints(userChooseLabel, 0, 1);
        GridPane.setConstraints(userChooseChoice, 1, 1);
        GridPane.setConstraints(userNameLabel, 0, 2);
        GridPane.setConstraints(userNameText, 1, 2);
        GridPane.setConstraints(userTypeLabel, 0, 3);
        GridPane.setConstraints(userTypeText, 1, 3);
        GridPane.setConstraints(userLoginLabel, 0, 4);
        GridPane.setConstraints(userLoginText, 1, 4);
        GridPane.setConstraints(userBranchLabel, 0, 5);
        GridPane.setConstraints(userBranchText, 1, 5);
        GridPane.setConstraints(newUserBranchLabel, 2, 5);
        GridPane.setConstraints(newUserBranchText, 3, 5);
        GridPane.setConstraints(userRoleLabel, 0, 6);
        GridPane.setConstraints(userRoleText, 1, 6);
        GridPane.setConstraints(newUserRoleLabel, 2, 6);
        GridPane.setConstraints(newUserRoleText, 3, 6);
        GridPane.setConstraints(userCreatorLabel, 0, 7);
        GridPane.setConstraints(userCreatorText, 1, 7);
        GridPane.setConstraints(actionTypeLabel, 0, 8);
        GridPane.setConstraints(actionTypeText, 1, 8);
        GridPane.setConstraints(cancelButton, 0, 9);
        GridPane.setConstraints(undoButton, 1, 9);
        GridPane.setConstraints(confirmButton, 3, 9);

        gridPane.getChildren().addAll(mainLabel, userChooseLabel, userChooseChoice);
        userChooseChoice.getItems().addAll(usersToAuthorize());

        userChooseChoice.getSelectionModel().selectedItemProperty().addListener(
                (v, oldValue, newValue) -> {
                    userChooseChoice.setDisable(true);
                    TempUser tempUser = findTempUserById(newValue.getId());
                    gridPane.getChildren().addAll(userNameLabel, userNameText, userTypeLabel, userTypeText, userLoginLabel,
                            userLoginText, userBranchLabel, userBranchText, userCreatorLabel, userCreatorText, actionTypeLabel,
                            actionTypeText, cancelButton, undoButton, confirmButton);
                    userNameText.setText(tempUser.getTempUser().getName());
                    userLoginText.setText(tempUser.getTempUser().getId());
                    userCreatorText.setText(tempUser.getAdmin().getId());
                    actionTypeText.setText(tempUser.getAction());


//                                      AUTHORIZE ADMIN
                    switch (newValue.getUserType()) {
                        case "A":
                            gridPane.getChildren().removeAll(userBranchLabel, userBranchText);
                            Admin admin = (Admin) tempUser.getTempUser();
                            userTypeText.setText("Admin");
                            confirmButton.setOnAction(e -> {
                                String message;
                                if (tempUser.getAdmin().equals(activeAdmin)) {
                                    message = "You are not allowed to authorize your own action";
                                } else {
                                    message = tempUser.getAction() + " " + admin.getName() + " authorized";
                                    if (tempUser.getAction().equals("Add")) {
                                        program.admins.add(admin);
                                    } else if (tempUser.getAction().equals("Remove")) {
                                        program.admins.remove(admin);
                                    }
                                    tempUsers.remove(tempUser);
                                    gridPane.getChildren().clear();
                                }
                                downLabel.setText(message);
                                System.out.println(message);
                            });
//                                      AUTHORIZE MANAGER
                            break;
                        case "M": {
                            Manager manager = (Manager) tempUser.getTempUser();
                            Branch branch = program.findBranchByManager(manager);
                            Branch tempBranch = tempUser.getTempBranch();
                            if ((tempUser.getAction().equals("Modify")) || (tempUser.getAction().contains("Transfer"))) {
                                gridPane.getChildren().addAll(newUserBranchLabel, newUserBranchText);
                                newUserBranchText.setText(tempBranch.getName());
                                userBranchText.setText(branch.getName());
                            } else {
                                userBranchText.setText(tempBranch.getName());
                            }
                            userTypeText.setText("Manager");
                            confirmButton.setOnAction(e -> {
                                String message;
                                if (tempUser.getAdmin().equals(activeAdmin)) {
                                    message = "You are not allowed to authorize your own action";
                                } else {
                                    message = tempUser.getAction() + " " + manager.getName() + " authorized";
                                    if (tempUser.getAction().equals("Remove")) {
                                        if (branch != null) {
                                            branch.setManager(null);
                                            manager.setBranchName(null);
                                        }
                                    } else {
                                        if (tempUser.getAction().contains("Transfer")) {
                                            String[] mgr2 = tempUser.getAction().split(":");
                                            Manager manager2 = program.findManagerById(mgr2[1]);
                                            if (manager2 != null) {
                                                branch.setManager(null);
                                                tempBranch.setManager(null);
                                                branch.setManager(manager2);
                                                manager2.setBranchName(branch.getName());
                                                tempBranch.setManager(manager);
                                                manager2.setBranchName(branch.getName());
                                                tempUsers.remove(findTempUserById(manager2.getId()));
                                                message = " Transfer successful";
                                            }
                                        } else {
                                            if ((tempBranch.getManager() == null) && (onlyManagerToBranch(tempBranch, manager))) {
                                                tempBranch.setManager(manager);
                                                manager.setBranchName(tempBranch.getName());
                                                if (branch != null) branch.setManager(null);
                                            } else {
                                                message = tempBranch.getName() + " already has a manager";
                                            }
                                        }
                                    }
                                    tempUsers.remove(tempUser);
                                    gridPane.getChildren().clear();
                                }
                                downLabel.setText(message);
                                System.out.println(message);
                            });
//                                              AUTHORIZE EMPLOYEE
                            break;
                        }
                        case "E": {
                            gridPane.getChildren().addAll(userRoleLabel, userRoleText);
                            Employee employee = (Employee) tempUser.getTempUser();
                            Branch branch = program.findBranchByEmployee(employee);
                            Role role = tempUser.getTempRole();
                            if (program.findEmployeeByName(employee.getName()) != null)
                                role = program.findEmployeeByName(employee.getName()).getRole();
                            Branch tempBranch = tempUser.getTempBranch();
                            Role tempRole = tempUser.getTempRole();
                            if (tempUser.getAction().equals("Modify")) {
                                gridPane.getChildren().addAll(newUserBranchLabel, newUserBranchText, newUserRoleLabel, newUserRoleText);
                                newUserBranchText.setText(tempBranch.getName());
                                userBranchText.setText(branch.getName());
                                newUserRoleText.setText(tempRole.getName());
                                userRoleText.setText(role.getName());
                            } else {
                                userBranchText.setText(tempBranch.getName());
                                userRoleText.setText(tempRole.getName());
                            }
                            userTypeText.setText("Employee");
                            confirmButton.setOnAction(e -> {
                                if (tempUser.getAction().equals("Modify"))
                                    System.out.println("Was: " + employee.saveString() + " in: " + userBranchText.getText());
                                String message;
                                if (tempUser.getAdmin().equals(activeAdmin)) {
                                    message = "You are not allowed to authorize your own action";
                                } else {
                                    message = tempUser.getAction() + " " + employee.getName() + " authorized";
                                    if (tempUser.getAction().equals("Remove")) {
                                        tempBranch.getEmployees().remove(employee);
                                    } else {
                                        if (!tempBranch.getEmployees().contains(employee)) {
                                            tempBranch.getEmployees().add(employee);
                                            employee.setBranchName(tempBranch.getName());
                                            if (branch != null) {
                                                branch.getEmployees().remove(employee);
                                            }
                                        }
                                        employee.setRole(tempRole);
                                        System.out.println("Now: " + employee.saveString() + " in: " + tempBranch.getName());
                                    }
                                    tempUsers.remove(tempUser);
                                    gridPane.getChildren().clear();
                                }
                                downLabel.setText(message);
                                System.out.println(message);
                            });
                            break;
                        }
                    }

                    undoButton.setOnAction(e -> {
                        downLabel.setText("Authorization undone for " + newValue.getId());
                        tempUsers.remove(tempUser);
                        gridPane.getChildren().clear();
                    });

                });
        cancelButton.setOnAction(e -> {
            downLabel.setText("What do you want to do now?");
            gridPane.getChildren().clear();
        });


        return gridPane;
    }

    //    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%   Temp User   %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    private ObservableList<User> usersToAuthorize() {
        ObservableList<User> users = FXCollections.observableArrayList();
        users.addAll(tempUsers.stream().map(TempUser::getTempUser).collect(Collectors.toList()));
        return users;
    }

    private TempUser findTempUserById(String findId) {
        if ((findId != null) && (!tempUsers.isEmpty())) {
            for (TempUser temp : tempUsers) {
                if (temp.getTempUser().getId().equals(findId))
                    return temp;
            }
        }
        return null;
    }

    private TempUser findTempUserByName(String findName) {
        if ((findName != null) && (!tempUsers.isEmpty())) {
            for (TempUser temp : tempUsers) {
                if (temp.getTempUser().getName().equals(findName))
                    return temp;
            }
        }
        return null;
    }

    private boolean onlyManagerToBranch(Branch findBranch, Manager findManager) {
        if ((findBranch != null) && (findManager != null)) {
            for (TempUser tempUser : tempUsers) {
                if (tempUser.getTempBranch() != null) {
                    if (tempUser.getTempBranch().getManager() != null) {
                        System.out.println(tempUser.getTempBranch().getName() + " has a manager " + tempUser.getTempBranch().getManager().getName());
                    } else {
                        if (tempUser.getTempBranch().equals(findBranch)) {
                            if ((tempUser.getTempUser().getUserType().equals(findManager.getUserType())) && (!tempUser.getTempUser().equals(findManager))) {
                                System.out.println(tempUser.getTempBranch().getName() + " waiting for a manager " + tempUser.getTempUser().getName());
                                return false;
                            }
                        } else
                            System.out.println(tempUser.getTempUser().getName() + " waiting to be added to " + tempUser.getTempBranch().getName());
                    }
                } else System.out.println("No branch set to " + tempUser.getTempUser().getName());
            }
            System.out.println(findBranch.getName() + " is ready for a new manager");
            return true;
        }
        return false;
    }

    boolean loadTempUsers(String action, User user, Branch branch, Role role, Admin admin) {
        if ((action != null) && (user != null) && (admin != null)) {
            tempUsers.add(new TempUser(action, user, branch, role, admin));
            System.out.println(action + " for " + user.getId() + " temp loaded");
            return true;
        }
        return false;
    }

    GridPane addRole() {
        downLabel.setText("Adding a new role");
        GridPane gridPane = new GridPane();
        Label mainLabel = new Label("Add a new role");

        Label roleNameLabel = new Label("Please insert the role's name:");
        TextField roleNameField = new TextField();

        Label depositLabel = new Label("Deposit limit");
        TextField depositText = new TextField();

        Label withdrawalLabel = new Label("Withdrawal limit");
        TextField withdrawalText = new TextField();

        Label transferLabel = new Label("Transfer limit");
        TextField transferText = new TextField();

        Label paymentLabel = new Label("Payment limit");
        TextField paymentText = new TextField();

        Label onlineLabel = new Label("Online limit");
        TextField onlineText = new TextField();

        Button addButton = new Button("Add");
        Button cancelButton = new Button("Cancel");


        GridPane.setHalignment(mainLabel, HPos.CENTER);
        GridPane.setValignment(mainLabel, VPos.CENTER);
        mainLabel.setFont(Font.font(20));
        GridPane.setColumnSpan(mainLabel, 2);
        gridPane.getColumnConstraints().add(new ColumnConstraints(300));
        gridPane.getColumnConstraints().add(new ColumnConstraints(300));
        GridPane.setHalignment(addButton, HPos.CENTER);
        GridPane.setHalignment(cancelButton, HPos.CENTER);

        depositText.setText("0.0");
        withdrawalText.setText("0.0");
        transferText.setText("0.0");
        paymentText.setText("0.0");
        onlineText.setText("0.0");

        GridPane.setConstraints(mainLabel, 0, 0);
        GridPane.setConstraints(roleNameLabel, 0, 1);
        GridPane.setConstraints(roleNameField, 1, 1);
        GridPane.setConstraints(depositLabel, 0, 2);
        GridPane.setConstraints(depositText, 1, 2);
        GridPane.setConstraints(withdrawalLabel, 0, 3);
        GridPane.setConstraints(withdrawalText, 1, 3);
        GridPane.setConstraints(transferLabel, 0, 4);
        GridPane.setConstraints(transferText, 1, 4);
        GridPane.setConstraints(paymentLabel, 0, 5);
        GridPane.setConstraints(paymentText, 1, 5);
        GridPane.setConstraints(onlineLabel, 0, 6);
        GridPane.setConstraints(onlineText, 1, 6);
        GridPane.setConstraints(cancelButton, 0, 7);
        GridPane.setConstraints(addButton, 1, 7);

        gridPane.getChildren().addAll(mainLabel, roleNameLabel, roleNameField, depositLabel, depositText, withdrawalLabel,
                withdrawalText, transferLabel, transferText, paymentLabel, paymentText, onlineLabel, onlineText, addButton, cancelButton);

        addButton.setOnAction(e -> {
            String message;
            if ((findTempRoleByName(roleNameField.getText()) != null) || (program.findRoleByName(roleNameField.getText()) != null)) {
                message = "Role already exists";
            } else {
                ArrayList<String> roleLimitsString = new ArrayList<>(5);
                roleLimitsString.add(depositText.getText());
                roleLimitsString.add(withdrawalText.getText());
                roleLimitsString.add(transferText.getText());
                roleLimitsString.add(paymentText.getText());
                roleLimitsString.add(onlineText.getText());
                ArrayList<Double> roleLimitsDouble = new ArrayList<>();
                boolean correct = true;
                for (String limit : roleLimitsString) {
                    try {
                        roleLimitsDouble.add(Double.parseDouble(limit));
                    } catch (NumberFormatException event) {
                        System.out.println("Error while reading limits:" + event.getMessage());
                        correct = false;
                    }
                }
                if (correct) {
                    String roleName = roleNameField.getText();
                    if ((program.findRoleByName(roleName) == null) && (!roleName.equals(""))) {
                        Role role = new Role(roleName);
                        tempRoles.add(new TempRole("Add", role, roleLimitsDouble, activeAdmin));
                        message = roleName + " is awaiting verification";
                        gridPane.getChildren().clear();
                    } else {
                        message = roleName + " cannot be added";
                    }
                } else message = "Please insert valid limit";
            }
            downLabel.setText(message);
            System.out.println(message);
        });

        cancelButton.setOnAction(e -> {
            gridPane.getChildren().clear();
            downLabel.setText("What do you want to do now?");
        });
        return gridPane;
    }


//    -------------------------------   ROLES   -----------------------------------------------------------


//    &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&   Add role   &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

    GridPane modifyRole() {
        downLabel.setText("Modifying a  role");
        GridPane gridPane = new GridPane();
        Label mainLabel = new Label("Modify a role");

        Label roleChoiceLabel = new Label("Choose a role");
        ChoiceBox<String> roleChoice = new ChoiceBox<>();
        roleChoice.setItems(program.allRoles());

        Label roleNameLabel = new Label("Role's name:");
        TextField roleNameField = new TextField();

        Label depositLabel = new Label("Deposit limit");
        TextField depositText = new TextField();

        Label withdrawalLabel = new Label("Withdrawal limit");
        TextField withdrawalText = new TextField();

        Label transferLabel = new Label("Transfer limit");
        TextField transferText = new TextField();

        Label paymentLabel = new Label("Payment limit");
        TextField paymentText = new TextField();

        Label onlineLabel = new Label("Online limit");
        TextField onlineText = new TextField();

        Button confirmButton = new Button("Confirm");
        Button cancelButton = new Button("Cancel");


        GridPane.setHalignment(mainLabel, HPos.CENTER);
        GridPane.setValignment(mainLabel, VPos.CENTER);
        mainLabel.setFont(Font.font(20));
        GridPane.setColumnSpan(mainLabel, 2);
        gridPane.getColumnConstraints().add(new ColumnConstraints(300));
        gridPane.getColumnConstraints().add(new ColumnConstraints(300));
        GridPane.setHalignment(confirmButton, HPos.CENTER);
        GridPane.setHalignment(cancelButton, HPos.CENTER);

        GridPane.setConstraints(mainLabel, 0, 0);
        GridPane.setConstraints(roleChoiceLabel, 0, 1);
        GridPane.setConstraints(roleChoice, 1, 1);
        GridPane.setConstraints(roleNameLabel, 0, 2);
        GridPane.setConstraints(roleNameField, 1, 2);
        GridPane.setConstraints(depositLabel, 0, 3);
        GridPane.setConstraints(depositText, 1, 3);
        GridPane.setConstraints(withdrawalLabel, 0, 4);
        GridPane.setConstraints(withdrawalText, 1, 4);
        GridPane.setConstraints(transferLabel, 0, 5);
        GridPane.setConstraints(transferText, 1, 5);
        GridPane.setConstraints(paymentLabel, 0, 6);
        GridPane.setConstraints(paymentText, 1, 6);
        GridPane.setConstraints(onlineLabel, 0, 7);
        GridPane.setConstraints(onlineText, 1, 7);
        GridPane.setConstraints(cancelButton, 0, 8);
        GridPane.setConstraints(confirmButton, 1, 8);

        gridPane.getChildren().addAll(mainLabel, roleChoiceLabel, roleChoice);

        roleChoice.getSelectionModel().selectedItemProperty().addListener(
                (v, oldValue, newValue) -> {
                    roleChoice.setDisable(true);
                    gridPane.getChildren().addAll(roleNameLabel, roleNameField, depositLabel, depositText, withdrawalLabel,
                            withdrawalText, transferLabel, transferText, paymentLabel, paymentText, onlineLabel, onlineText, confirmButton, cancelButton);
                    Role role = program.findRoleByName(newValue);
                    roleNameField.setText(role.getName());
                    depositText.setText(role.getLimits().get(0).toString());
                    withdrawalText.setText(role.getLimits().get(1).toString());
                    transferText.setText(role.getLimits().get(2).toString());
                    paymentText.setText(role.getLimits().get(3).toString());
                    onlineText.setText(role.getLimits().get(4).toString());
                    confirmButton.setOnAction(e -> {
                        String message;
                        if ((findTempRoleByName(roleNameField.getText()) != null) || (program.findRoleByName(roleNameField.getPromptText()) != null)) {
                            message = "Role already exists or is awaiting verification";
                        } else {
                            ArrayList<String> roleLimitsString = new ArrayList<>(5);
                            roleLimitsString.add(depositText.getText());
                            roleLimitsString.add(withdrawalText.getText());
                            roleLimitsString.add(transferText.getText());
                            roleLimitsString.add(paymentText.getText());
                            roleLimitsString.add(onlineText.getText());
                            ArrayList<Double> roleLimitsDouble = new ArrayList<>();
                            boolean correct = true;
                            for (String limit : roleLimitsString) {
                                try {
                                    roleLimitsDouble.add(Double.parseDouble(limit));
                                } catch (NumberFormatException event) {
                                    System.out.println("Error while reading limits:" + event.getMessage());
                                    correct = false;
                                }
                            }
                            if (correct) {
                                String roleName = roleNameField.getText();
                                if ((program.findRoleByName(roleName) == null) && (!roleName.equals(""))) {
                                    message = role.getName() + " modified to " + roleName;
                                    role.setName(roleName);

                                } else {
                                    message = roleName + " cannot be added";
                                }
                                boolean equal = true;
                                for (int i = 0; i < 5; i++) {
                                    if (!role.getLimits().get(i).equals(roleLimitsDouble.get(i)))
                                        equal = false;
                                }
                                if (!equal) {
                                    tempRoles.add(new TempRole("Modify", role, roleLimitsDouble, activeAdmin));
                                    message = roleName + " is awaiting verification";
                                }
                                gridPane.getChildren().clear();
                            } else message = "Please insert valid limit";
                        }
                        downLabel.setText(message);
                        System.out.println(message);
                    });

                    cancelButton.setOnAction(e -> {
                        gridPane.getChildren().clear();
                        downLabel.setText("What do you want to do now?");
                    });


                });
        return gridPane;
    }

    //    &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&   Modify role   &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

    GridPane listRole() {
        downLabel.setText("Listing roles");
        GridPane gridPane = new GridPane();
        Label mainLabel = new Label("All roles");

        Button cancelButton = new Button("Cancel");

        GridPane.setHalignment(mainLabel, HPos.CENTER);
        GridPane.setValignment(mainLabel, VPos.CENTER);
        mainLabel.setFont(Font.font(20));
        gridPane.getColumnConstraints().add(new ColumnConstraints(600));
        GridPane.setHalignment(cancelButton, HPos.CENTER);

        GridPane.setConstraints(mainLabel, 0, 0);
        GridPane.setConstraints(cancelButton, 0, 2);

        TableView<Role> roleTable = program.roleTableView();
        GridPane.setConstraints(roleTable, 0, 1);
        gridPane.getChildren().addAll(mainLabel, roleTable, cancelButton);

        cancelButton.setOnAction(e -> {
            gridPane.getChildren().clear();
            downLabel.setText("What do you want to do now?");
        });

        return gridPane;
    }

    //    &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&   List roles   &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

    GridPane removeRole() {
        downLabel.setText("Modifying a  role");
        GridPane gridPane = new GridPane();
        Label mainLabel = new Label("Modify a role");

        Label roleChoiceLabel = new Label("Choose a role");
        ChoiceBox<String> roleChoice = new ChoiceBox<>();
        roleChoice.setItems(program.allRoles());

        Label roleNameLabel = new Label("Role's name:");
        TextField roleNameField = new TextField();

        Label depositLabel = new Label("Deposit limit");
        TextField depositText = new TextField();

        Label withdrawalLabel = new Label("Withdrawal limit");
        TextField withdrawalText = new TextField();

        Label transferLabel = new Label("Transfer limit");
        TextField transferText = new TextField();

        Label paymentLabel = new Label("Payment limit");
        TextField paymentText = new TextField();

        Label onlineLabel = new Label("Online limit");
        TextField onlineText = new TextField();

        Button confirmButton = new Button("Confirm");
        Button cancelButton = new Button("Cancel");

        roleNameField.setEditable(false);
        depositText.setEditable(false);
        withdrawalText.setEditable(false);
        transferText.setEditable(false);
        paymentText.setEditable(false);
        onlineText.setEditable(false);

        GridPane.setHalignment(mainLabel, HPos.CENTER);
        GridPane.setValignment(mainLabel, VPos.CENTER);
        mainLabel.setFont(Font.font(20));
        GridPane.setColumnSpan(mainLabel, 2);
        gridPane.getColumnConstraints().add(new ColumnConstraints(300));
        gridPane.getColumnConstraints().add(new ColumnConstraints(300));
        GridPane.setHalignment(confirmButton, HPos.CENTER);
        GridPane.setHalignment(cancelButton, HPos.CENTER);

        GridPane.setConstraints(mainLabel, 0, 0);
        GridPane.setConstraints(roleChoiceLabel, 0, 1);
        GridPane.setConstraints(roleChoice, 1, 1);
        GridPane.setConstraints(roleNameLabel, 0, 2);
        GridPane.setConstraints(roleNameField, 1, 2);
        GridPane.setConstraints(depositLabel, 0, 3);
        GridPane.setConstraints(depositText, 1, 3);
        GridPane.setConstraints(withdrawalLabel, 0, 4);
        GridPane.setConstraints(withdrawalText, 1, 4);
        GridPane.setConstraints(transferLabel, 0, 5);
        GridPane.setConstraints(transferText, 1, 5);
        GridPane.setConstraints(paymentLabel, 0, 6);
        GridPane.setConstraints(paymentText, 1, 6);
        GridPane.setConstraints(onlineLabel, 0, 7);
        GridPane.setConstraints(onlineText, 1, 7);
        GridPane.setConstraints(cancelButton, 0, 8);
        GridPane.setConstraints(confirmButton, 1, 8);

        gridPane.getChildren().addAll(mainLabel, roleChoiceLabel, roleChoice);

        roleChoice.getSelectionModel().selectedItemProperty().addListener(
                (v, oldValue, newValue) -> {
                    roleChoice.setDisable(true);
                    gridPane.getChildren().addAll(roleNameLabel, roleNameField, depositLabel, depositText, withdrawalLabel,
                            withdrawalText, transferLabel, transferText, paymentLabel, paymentText, onlineLabel, onlineText, confirmButton, cancelButton);
                    Role role = program.findRoleByName(newValue);
                    roleNameField.setText(role.getName());
                    depositText.setText(role.getLimits().get(0).toString());
                    withdrawalText.setText(role.getLimits().get(1).toString());
                    transferText.setText(role.getLimits().get(2).toString());
                    paymentText.setText(role.getLimits().get(3).toString());
                    onlineText.setText(role.getLimits().get(4).toString());
                    confirmButton.setOnAction(e -> {
                        String message;
                        boolean exists = false;
                        for (TempRole temp : tempRoles) {
                            if (temp.getRole() == role)
                                exists = true;
                        }
                        if (exists)
                            message = newValue + " is already awaiting verification";
                        else {
                            boolean empty = true;
                            for (Employee employee : program.allEmployeesArray()) {
                                if (employee.getRole().equals(role))
                                    empty = false;
                            }
                            if (empty) {
                                tempRoles.add(new TempRole("Remove", role, role.getLimits(), activeAdmin));
                                message = role.getName() + " awaiting verification";
                            } else message = role.getName() + " is assigned to someone and therefore cannot be removed";
                            gridPane.getChildren().clear();
                        }

                        downLabel.setText(message);
                        System.out.println(message);
                    });

                    cancelButton.setOnAction(e -> {
                        gridPane.getChildren().clear();
                        downLabel.setText("What do you want to do now?");
                    });


                });
        return gridPane;
    }

    //    &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&   Remove role   &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

    GridPane authorizeRole() {
        downLabel.setText("Choose a role");
        GridPane gridPane = new GridPane();
        Label mainLabel = new Label("Authorize a role");

        Label roleChoiceLabel = new Label("Choose a role");
        ChoiceBox<String> roleChoice = new ChoiceBox<>();

        Label roleNameLabel = new Label("Role name");
        TextField roleNameField = new TextField();

        Label depositLabel = new Label("Deposit limit");
        TextField depositText = new TextField();
        TextField newDepositText = new TextField();

        Label withdrawalLabel = new Label("Withdrawal limit");
        TextField withdrawalText = new TextField();
        TextField newWithdrawalText = new TextField();

        Label transferLabel = new Label("Transfer limit");
        TextField transferText = new TextField();
        TextField newTransferText = new TextField();

        Label paymentLabel = new Label("Payment limit");
        TextField paymentText = new TextField();
        TextField newPaymentText = new TextField();

        Label onlineLabel = new Label("Online limit");
        TextField onlineText = new TextField();
        TextField newOnlineText = new TextField();

        Label actionLabel = new Label("Action type");
        TextField actionText = new TextField();

        Label adminLabel = new Label("Action by");
        TextField adminText = new TextField();

        Button confirmButton = new Button("Confirm");
        Button undoButton = new Button("Undo");
        Button cancelButton = new Button("Cancel");

        Label arrowsLabel = new Label(">>");


        GridPane.setHalignment(mainLabel, HPos.CENTER);
        GridPane.setValignment(mainLabel, VPos.CENTER);
        mainLabel.setFont(Font.font(20));
        GridPane.setColumnSpan(mainLabel, 2);
        gridPane.getColumnConstraints().add(new ColumnConstraints(150));
        gridPane.getColumnConstraints().add(new ColumnConstraints(200));
        gridPane.getColumnConstraints().add(new ColumnConstraints(50));
        gridPane.getColumnConstraints().add(new ColumnConstraints(200));
        GridPane.setHalignment(confirmButton, HPos.CENTER);
        GridPane.setHalignment(undoButton, HPos.CENTER);
        GridPane.setHalignment(cancelButton, HPos.CENTER);
        GridPane.setHalignment(arrowsLabel, HPos.CENTER);

        roleChoice.setItems(rolesToAuthorize());
        roleNameField.setEditable(false);
        depositText.setEditable(false);
        withdrawalText.setEditable(false);
        transferText.setEditable(false);
        paymentText.setEditable(false);
        onlineText.setEditable(false);
        actionText.setEditable(false);
        adminText.setEditable(false);

        GridPane.setConstraints(mainLabel, 0, 0);
        GridPane.setConstraints(roleChoiceLabel, 0, 1);
        GridPane.setConstraints(roleChoice, 1, 1);
        GridPane.setConstraints(roleNameLabel, 0, 2);
        GridPane.setConstraints(roleNameField, 1, 2);
        GridPane.setConstraints(depositLabel, 0, 3);
        GridPane.setConstraints(depositText, 1, 3);
        GridPane.setConstraints(newDepositText, 3, 3);
        GridPane.setConstraints(withdrawalLabel, 0, 4);
        GridPane.setConstraints(withdrawalText, 1, 4);
        GridPane.setConstraints(newWithdrawalText, 3, 4);
        GridPane.setConstraints(arrowsLabel, 2, 4);
        GridPane.setConstraints(transferLabel, 0, 5);
        GridPane.setConstraints(transferText, 1, 5);
        GridPane.setConstraints(newTransferText, 3, 5);
        GridPane.setConstraints(paymentLabel, 0, 6);
        GridPane.setConstraints(paymentText, 1, 6);
        GridPane.setConstraints(newPaymentText, 3, 6);
        GridPane.setConstraints(onlineLabel, 0, 7);
        GridPane.setConstraints(onlineText, 1, 7);
        GridPane.setConstraints(newOnlineText, 3, 7);
        GridPane.setConstraints(actionLabel, 0, 8);
        GridPane.setConstraints(actionText, 1, 8);
        GridPane.setConstraints(adminLabel, 0, 9);
        GridPane.setConstraints(adminText, 1, 9);
        GridPane.setConstraints(cancelButton, 0, 10);
        GridPane.setConstraints(undoButton, 1, 10);
        GridPane.setConstraints(confirmButton, 3, 10);

        gridPane.getChildren().addAll(mainLabel, roleChoiceLabel, roleChoice);

        roleChoice.getSelectionModel().selectedItemProperty().addListener(
                (v, oldValue, newValue) -> {
                    roleChoice.setDisable(true);
                    TempRole tempRole = findTempRoleByName(newValue);
                    gridPane.getChildren().addAll(roleNameLabel, roleNameField, depositLabel, depositText, withdrawalLabel,
                            withdrawalText, transferLabel, transferText, paymentLabel, paymentText, onlineLabel, onlineText,
                            actionLabel, actionText, adminLabel, adminText, confirmButton, undoButton, cancelButton);
                    if (tempRole != null) {
                        Role role = tempRole.getRole();
                        roleNameField.setText(role.getName());
                        depositText.setText(tempRole.getLimits().get(0).toString());
                        withdrawalText.setText(tempRole.getLimits().get(1).toString());
                        transferText.setText(tempRole.getLimits().get(2).toString());
                        paymentText.setText(tempRole.getLimits().get(3).toString());
                        onlineText.setText(tempRole.getLimits().get(4).toString());
                        actionText.setText(tempRole.getAction());
                        adminText.setText(tempRole.getAdmin().getId());
                        if (tempRole.getAction().equals("Modify")) {
//                        set fields for new limits
                            gridPane.getChildren().addAll(newDepositText, newWithdrawalText, newTransferText,
                                    newPaymentText, newOnlineText, arrowsLabel);
//                        set limits to values "before"
                            depositText.setText(program.findRoleByName(role.getName()).getLimits().get(0).toString());
                            withdrawalText.setText(program.findRoleByName(role.getName()).getLimits().get(1).toString());
                            transferText.setText(program.findRoleByName(role.getName()).getLimits().get(2).toString());
                            paymentText.setText(program.findRoleByName(role.getName()).getLimits().get(3).toString());
                            onlineText.setText(program.findRoleByName(role.getName()).getLimits().get(4).toString());
//                        set new limits values
                            newDepositText.setText(tempRole.getLimits().get(0).toString());
                            newWithdrawalText.setText(tempRole.getLimits().get(1).toString());
                            newTransferText.setText(tempRole.getLimits().get(2).toString());
                            newPaymentText.setText(tempRole.getLimits().get(3).toString());
                            newOnlineText.setText(tempRole.getLimits().get(4).toString());
                        }

                        confirmButton.setOnAction(e -> {
                            String message;
                            if (tempRole.getAdmin().equals(activeAdmin))
                                message = "You are not allowed to authorize your own action";
                            else {
                                message = tempRole.getAction() + " " + role.getName() + " authorized";
                                if (tempRole.getAction().equals("Remove"))
                                    program.roles.remove(role);
                                else {
                                    if ((tempRole.getAction().equals("Add")) && (program.findRoleByName(role.getName()) == null)) {
                                        program.roles.add(role);
                                    }
                                    role.setLimits(tempRole.getLimits());
                                }
                                tempRoles.remove(tempRole);
                                gridPane.getChildren().clear();
                            }
                            downLabel.setText(message);
                            System.out.println(message);
                        });
                    }

                    cancelButton.setOnAction(e -> {
                        gridPane.getChildren().clear();
                        downLabel.setText("What do you want to do now?");
                    });

                    undoButton.setOnAction(e -> {
                        tempRoles.remove(tempRole);
                        gridPane.getChildren().clear();
                        downLabel.setText("Role maintenance undone");
                    });


                });
        return gridPane;
    }

//&&&&&&&&&&&&&&&&&&&&&&&&&&&&   Authorize role   &&&&&&&&&&&&&&&&&&&&&&&&&&&

    private ObservableList<String> rolesToAuthorize() {
        ObservableList<String> roles = FXCollections.observableArrayList();
        roles.addAll(tempRoles.stream().map(role -> role.getRole().getName()).collect(Collectors.toList()));
        return roles;
    }

    private TempRole findTempRoleByName(String findName) {
        if ((!findName.equals("")) && (!tempRoles.isEmpty())) {
            for (TempRole tempRole : tempRoles) {
                if (tempRole.getRole().getName().equals(findName))
                    return tempRole;
            }
        }
        return null;
    }

    boolean loadTempRoles(String action, Role role, ArrayList<Double> limits, Admin admin) {
        if ((action != null) && (role != null) && (limits != null) && (admin != null)) {
            tempRoles.add(new TempRole(action, role, limits, admin));
            System.out.println(action + " for " + role.getName() + " temp loaded");
            return true;
        }
        return false;
    }

    GridPane addBranch() {
        downLabel.setText("Adding a new branch");
        GridPane gridPane = new GridPane();
        Label mainLabel = new Label("Add a new branch");

        Label branchNameLabel = new Label("Please insert the branch's name:");
        TextField branchNameText = new TextField();

        Label branchAddressLabel = new Label("Please insert the branch's address:");
        TextField branchAddressText = new TextField();

        Button addButton = new Button("Add");
        Button cancelButton = new Button("Cancel");


        GridPane.setHalignment(mainLabel, HPos.CENTER);
        GridPane.setValignment(mainLabel, VPos.CENTER);
        mainLabel.setFont(Font.font(20));
        GridPane.setColumnSpan(mainLabel, 2);
        gridPane.getColumnConstraints().add(new ColumnConstraints(300));
        gridPane.getColumnConstraints().add(new ColumnConstraints(300));
        GridPane.setHalignment(addButton, HPos.CENTER);
        GridPane.setHalignment(cancelButton, HPos.CENTER);

        GridPane.setConstraints(mainLabel, 0, 0);
        GridPane.setConstraints(branchNameLabel, 0, 1);
        GridPane.setConstraints(branchNameText, 1, 1);
        GridPane.setConstraints(branchAddressLabel, 0, 2);
        GridPane.setConstraints(branchAddressText, 1, 2);
        GridPane.setConstraints(cancelButton, 0, 3);
        GridPane.setConstraints(addButton, 1, 3);

        gridPane.getChildren().addAll(mainLabel, branchNameLabel, branchNameText, branchAddressLabel, branchAddressText, addButton, cancelButton);

        addButton.setOnAction(e -> {
            String message;
            String branchName = branchNameText.getText();
            if ((program.findBranchByName(branchName) != null) || (findTempBranchByName(branchName) != null)) {
                message = "Branch called " + branchName + " already exists";
            } else {
                if (branchAddressText.getText().equals(""))
                    message = "Please insert the branch's address";
                else {
                    tempBranches.add(new TempBranch("Add", new Branch(branchName, branchAddressText.getText()), null,
                            branchAddressText.getText(), activeAdmin));
                    message = branchName + " is now awaiting verification";
                    gridPane.getChildren().clear();
                }
            }

            downLabel.setText(message);
            System.out.println(message);
        });

        cancelButton.setOnAction(e -> {
            gridPane.getChildren().clear();
            downLabel.setText("What do you want to do now?");
        });
        return gridPane;
    }

    GridPane modifyBranch() {
        downLabel.setText("Choose a branch");
        GridPane gridPane = new GridPane();
        Label mainLabel = new Label("Modify a branch");

        Label branchChoiceLabel = new Label("Choose a branch");
        ChoiceBox<String> branchChoice = new ChoiceBox<>();

        Label branchNameLabel = new Label("Branch name");
        TextField branchNameField = new TextField();

        Label branchAddressLabel = new Label("Branch address");
        TextField branchAddressField = new TextField();

        Label branchManagerLabel = new Label("Branch manager");
        TextField branchManagerField = new TextField();

        Button confirmButton = new Button("Confirm");
        Button cancelButton = new Button("Cancel");

        GridPane.setHalignment(mainLabel, HPos.CENTER);
        GridPane.setValignment(mainLabel, VPos.CENTER);
        mainLabel.setFont(Font.font(20));
        GridPane.setColumnSpan(mainLabel, 20);
        gridPane.getColumnConstraints().add(new ColumnConstraints(300));
        gridPane.getColumnConstraints().add(new ColumnConstraints(300));
        GridPane.setHalignment(confirmButton, HPos.CENTER);
        GridPane.setHalignment(cancelButton, HPos.CENTER);

        branchChoice.setItems(program.allBranches());
        branchManagerField.setEditable(false);


        GridPane.setConstraints(mainLabel, 0, 0);
        GridPane.setConstraints(branchChoiceLabel, 0, 1);
        GridPane.setConstraints(branchChoice, 1, 1);
        GridPane.setConstraints(branchNameLabel, 0, 2);
        GridPane.setConstraints(branchNameField, 1, 2);
        GridPane.setConstraints(branchAddressLabel, 0, 3);
        GridPane.setConstraints(branchAddressField, 1, 3);
        GridPane.setConstraints(branchManagerLabel, 0, 4);
        GridPane.setConstraints(branchManagerField, 1, 4);
        GridPane.setConstraints(cancelButton, 0, 5);
        GridPane.setConstraints(confirmButton, 1, 5);

        gridPane.getChildren().addAll(mainLabel, branchChoiceLabel, branchChoice);

        branchChoice.getSelectionModel().selectedItemProperty().addListener(
                (v, oldValue, newValue) -> {
                    branchChoice.setDisable(true);
                    Branch branch = program.findBranchByName(newValue);
                    gridPane.getChildren().addAll(branchNameLabel, branchNameField, branchAddressLabel,
                            branchAddressField, branchManagerLabel, branchManagerField, confirmButton, cancelButton);
                    if (branch != null) {
                        branchNameField.setText(branch.getName());
                        branchAddressField.setText(branch.getAddress());
                        if (branch.getManager() != null)
                            branchManagerField.setText(branch.getManager().getId());
                        confirmButton.setOnAction(e -> {
                            String message;
                            if ((branch.getName().equals(branchNameField.getText())) && (branch.getAddress().equals(branchAddressField.getText())))
                                message = "No changes made";
                            else {
                                String newBranchName = "";
                                if ((!branch.getName().equals(branchNameField.getText())) && (program.findBranchByName(branchNameField.getText()) == null)
                                        && (findTempBranchByName(branchNameField.getText()) == null))
                                    newBranchName = branchNameField.getText();
                                else message = "Please insert a unique branch name";
                                String newBranchAddress = "";
                                if (!branch.getAddress().equals(branchAddressField.getText()))
                                    newBranchAddress = branchAddressField.getText();
                                tempBranches.add(new TempBranch("Modify", branch, newBranchName, newBranchAddress, activeAdmin));
                                message = branch.getName() + " is now awaiting verification";
                                gridPane.getChildren().clear();
                            }
                            downLabel.setText(message);
                            System.out.println(message);
                        });
                    }

                    cancelButton.setOnAction(e -> {
                        gridPane.getChildren().clear();
                        downLabel.setText("What do you want to do now?");
                    });

                });
        return gridPane;
    }


//    -------------------------   BRANCH   -------------------------------------


//    &&&&&&&&&&&&&&&&&&&& Add branch  &&&&&&&&&&&&&&&&&&&&&&&&&&&&&

    GridPane listBranches() {
        downLabel.setText("Listing branches");
        GridPane gridPane = new GridPane();
        Label mainLabel = new Label("All branches");

        Button cancelButton = new Button("Cancel");

        GridPane.setHalignment(mainLabel, HPos.CENTER);
        GridPane.setValignment(mainLabel, VPos.CENTER);
        mainLabel.setFont(Font.font(20));
        gridPane.getColumnConstraints().add(new ColumnConstraints(600));
        GridPane.setHalignment(cancelButton, HPos.CENTER);

        GridPane.setConstraints(mainLabel, 0, 0);
        GridPane.setConstraints(cancelButton, 0, 2);

        TableView<Branch> branchTableView = program.branchTableView();
        GridPane.setConstraints(branchTableView, 0, 1);
        gridPane.getChildren().addAll(mainLabel, branchTableView, cancelButton);

        cancelButton.setOnAction(e -> {
            gridPane.getChildren().clear();
            downLabel.setText("What do you want to do now?");
        });

        return gridPane;
    }

    //&&&&&&&&&&&&&&&&&&&&&&&&&&&&   Modify branch   &&&&&&&&&&&&&&&&&&&&&&&&&&&

    GridPane removeBranch() {
        downLabel.setText("Choose a branch");
        GridPane gridPane = new GridPane();
        Label mainLabel = new Label("Remove a branch");

        Label branchChoiceLabel = new Label("Choose a branch");
        ChoiceBox<String> branchChoice = new ChoiceBox<>();

        Label branchNameLabel = new Label("Branch name");
        TextField branchNameField = new TextField();

        Label branchAddressLabel = new Label("Branch address");
        TextField branchAddressField = new TextField();

        Label branchManagerLabel = new Label("Branch manager");
        TextField branchManagerField = new TextField();

        Button confirmButton = new Button("Confirm");
        Button cancelButton = new Button("Cancel");

        GridPane.setHalignment(mainLabel, HPos.CENTER);
        GridPane.setValignment(mainLabel, VPos.CENTER);
        mainLabel.setFont(Font.font(20));
        GridPane.setColumnSpan(mainLabel, 2);
        gridPane.getColumnConstraints().add(new ColumnConstraints(300));
        gridPane.getColumnConstraints().add(new ColumnConstraints(300));
        GridPane.setHalignment(confirmButton, HPos.CENTER);
        GridPane.setHalignment(cancelButton, HPos.CENTER);

        branchChoice.setItems(program.allBranches());
        branchNameField.setEditable(false);
        branchAddressField.setEditable(false);
        branchManagerField.setEditable(false);

        GridPane.setConstraints(mainLabel, 0, 0);
        GridPane.setConstraints(branchChoiceLabel, 0, 1);
        GridPane.setConstraints(branchChoice, 1, 1);
        GridPane.setConstraints(branchNameLabel, 0, 2);
        GridPane.setConstraints(branchNameField, 1, 2);
        GridPane.setConstraints(branchAddressLabel, 0, 3);
        GridPane.setConstraints(branchAddressField, 1, 3);
        GridPane.setConstraints(branchManagerLabel, 0, 4);
        GridPane.setConstraints(branchManagerField, 1, 4);
        GridPane.setConstraints(cancelButton, 0, 5);
        GridPane.setConstraints(confirmButton, 1, 5);

        gridPane.getChildren().addAll(mainLabel, branchChoiceLabel, branchChoice);

        branchChoice.getSelectionModel().selectedItemProperty().addListener(
                (v, oldValue, newValue) -> {
                    branchChoice.setDisable(true);
                    Branch branch = program.findBranchByName(newValue);
                    gridPane.getChildren().addAll(branchNameLabel, branchNameField, branchAddressLabel,
                            branchAddressField, branchManagerLabel, branchManagerField, confirmButton, cancelButton);
                    branchNameField.setText(branch.getName());
                    branchAddressField.setText(branch.getAddress());
                    if (branch.getManager() != null)
                        branchManagerField.setText(branch.getManager().getId());
                    confirmButton.setOnAction(e -> {
                        String message;
                        if (findTempBranchByName(newValue) != null)
                            message = newValue + " is already awaiting verification";
                        else {
                            if ((branch.getManager() != null) || (!branch.getEmployees().isEmpty()))
                                message = newValue + " still has employees or a manager";
                            else {
                                tempBranches.add(new TempBranch("Remove", branch, null, null, activeAdmin));
                                message = newValue + " is now awaiting verification";
                            }
                            gridPane.getChildren().clear();
                        }
                        downLabel.setText(message);
                        System.out.println(message);
                    });

                    cancelButton.setOnAction(e -> {
                        gridPane.getChildren().clear();
                        downLabel.setText("What do you want to do now?");
                    });

                });
        return gridPane;
    }

    //    &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&   List branches   &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

    GridPane authorizeBranch() {
        downLabel.setText("Choose a branch");
        GridPane gridPane = new GridPane();
        Label mainLabel = new Label("Authorize a branch");

        Label branchChoiceLabel = new Label("Choose a branch");
        ChoiceBox<String> branchChoice = new ChoiceBox<>();

        Label branchNameLabel = new Label("Branch name");
        TextField branchNameField = new TextField();
        TextField newBranchNameField = new TextField();

        Label branchAddressLabel = new Label("Branch address");
        TextField branchAddressField = new TextField();
        TextField newBranchAddressField = new TextField();

        Label branchManagerLabel = new Label("Branch manager");
        TextField branchManagerField = new TextField();

        Label actionLabel = new Label("Action type");
        TextField actionText = new TextField();

        Label adminLabel = new Label("Action by");
        TextField adminText = new TextField();

        Button confirmButton = new Button("Confirm");
        Button undoButton = new Button("Undo");
        Button cancelButton = new Button("Cancel");

        Label arrowsLabel = new Label(">>");


        GridPane.setHalignment(mainLabel, HPos.CENTER);
        GridPane.setValignment(mainLabel, VPos.CENTER);
        mainLabel.setFont(Font.font(20));
        GridPane.setColumnSpan(mainLabel, 4);
        gridPane.getColumnConstraints().add(new ColumnConstraints(150));
        gridPane.getColumnConstraints().add(new ColumnConstraints(200));
        gridPane.getColumnConstraints().add(new ColumnConstraints(50));
        gridPane.getColumnConstraints().add(new ColumnConstraints(200));
        GridPane.setHalignment(confirmButton, HPos.CENTER);
        GridPane.setHalignment(undoButton, HPos.CENTER);
        GridPane.setHalignment(cancelButton, HPos.CENTER);
        GridPane.setHalignment(arrowsLabel, HPos.CENTER);

        branchChoice.setItems(branchesToAuthorize());
        branchNameField.setEditable(false);
        newBranchNameField.setEditable(false);
        branchAddressField.setEditable(false);
        newBranchAddressField.setEditable(false);
        branchManagerField.setEditable(false);
        actionText.setEditable(false);
        adminText.setEditable(false);

        GridPane.setConstraints(mainLabel, 0, 0);
        GridPane.setConstraints(branchChoiceLabel, 0, 1);
        GridPane.setConstraints(branchChoice, 1, 1);
        GridPane.setConstraints(branchNameLabel, 0, 2);
        GridPane.setConstraints(branchNameField, 1, 2);
        GridPane.setConstraints(newBranchNameField, 3, 2);
        GridPane.setConstraints(branchAddressLabel, 0, 3);
        GridPane.setConstraints(branchAddressField, 1, 3);
        GridPane.setConstraints(newBranchAddressField, 3, 3);
        GridPane.setConstraints(branchManagerLabel, 0, 4);
        GridPane.setConstraints(branchManagerField, 1, 4);
        GridPane.setConstraints(arrowsLabel, 2, 2);
        GridPane.setConstraints(actionLabel, 0, 5);
        GridPane.setConstraints(actionText, 1, 5);
        GridPane.setConstraints(adminLabel, 0, 6);
        GridPane.setConstraints(adminText, 1, 6);
        GridPane.setConstraints(cancelButton, 0, 7);
        GridPane.setConstraints(undoButton, 1, 7);
        GridPane.setConstraints(confirmButton, 3, 7);

        gridPane.getChildren().addAll(mainLabel, branchChoiceLabel, branchChoice);

        branchChoice.getSelectionModel().selectedItemProperty().addListener(
                (v, oldValue, newValue) -> {
                    branchChoice.setDisable(true);
                    TempBranch tempBranch = findTempBranchByName(newValue);
                    if (tempBranch != null) {
                        Branch newBranch = tempBranch.getBranch();
                        Branch existingBranch = program.findBranchByName(newValue);
                        gridPane.getChildren().addAll(branchNameLabel, branchNameField, branchAddressLabel,
                                branchAddressField, branchManagerLabel, branchManagerField,
                                actionLabel, actionText, adminLabel, adminText, confirmButton, undoButton, cancelButton);
                        branchNameField.setText(newBranch.getName());
                        if (tempBranch.getAction().equals("Add"))
                            branchAddressField.setText(newBranch.getAddress());
                        else {
                            branchAddressField.setText(existingBranch.getAddress());
                            if (existingBranch.getManager() != null)
                                branchManagerField.setText(existingBranch.getManager().getId());
                        }
                        actionText.setText(tempBranch.getAction());
                        adminText.setText(tempBranch.getAdmin().getId());
                        if (tempBranch.getAction().equals("Modify")) {
                            gridPane.getChildren().addAll(newBranchNameField, newBranchAddressField, arrowsLabel);
                            branchNameField.setText(existingBranch.getName());
                            newBranchNameField.setText(tempBranch.getNewBranchName());
                            branchAddressField.setText(existingBranch.getAddress());
                            newBranchAddressField.setText(tempBranch.getNewBranchAddress());
                        }
                        confirmButton.setOnAction(e -> {
                            String message;
                            if (tempBranch.getAdmin().equals(activeAdmin))
                                message = "You are not allowed to authorize your own action";
                            else {
                                message = tempBranch.getAction() + " " + newBranch.getName() + " authorized";
                                if (tempBranch.getAction().equals("Remove"))
                                    program.branches.remove(existingBranch);
                                else {
                                    if ((tempBranch.getAction().equals("Add")) && (program.findBranchByName(newValue) == null))
                                        program.branches.add(newBranch);
                                    else if (tempBranch.getAction().equals("Modify")) {
                                        if ((!existingBranch.getName().equals(tempBranch.getNewBranchName())) &&
                                                (program.findBranchByName(tempBranch.getNewBranchName()) == null))
                                            existingBranch.setName(tempBranch.getNewBranchName());
                                        if (!existingBranch.getAddress().equals(tempBranch.getNewBranchAddress()))
                                            existingBranch.setAddress(tempBranch.getNewBranchAddress());
                                    }
                                }
                                tempBranches.remove(tempBranch);
                                gridPane.getChildren().clear();
                            }
                            downLabel.setText(message);
                            System.out.println(message);
                        });
                    }

                    cancelButton.setOnAction(e -> {
                        gridPane.getChildren().clear();
                        downLabel.setText("What do you want to do now?");
                    });

                    undoButton.setOnAction(e -> {
                        tempBranches.remove(tempBranch);
                        gridPane.getChildren().clear();
                        downLabel.setText("Branch maintenance undone");
                    });


                });
        return gridPane;
    }

    //&&&&&&&&&&&&&&&&&&&&&&&&&&&&   Remove branch   &&&&&&&&&&&&&&&&&&&&&&&&&&&

    private ObservableList<String> branchesToAuthorize() {
        ObservableList<String> branches = FXCollections.observableArrayList();
        branches.addAll(tempBranches.stream().map(branch -> branch.getBranch().getName()).collect(Collectors.toList()));
        return branches;
    }

    //&&&&&&&&&&&&&&&&&&&&&&&&&&&&   Authorize branch   &&&&&&&&&&&&&&&&&&&&&&&&&&&

    private TempBranch findTempBranchByName(String findName) {
        if ((!findName.equals("")) && (!tempBranches.isEmpty())) {
            for (TempBranch tempBranch : tempBranches) {
                if (tempBranch.getBranch().getName().equals(findName))
                    return tempBranch;
            }
        }
        return null;
    }

    boolean loadTempBranches(String action, Branch branch, String newBranchName, String newBranchAddress, Admin admin) {
        if ((action != null) && (branch != null) && (admin != null)) {
            tempBranches.add(new TempBranch(action, branch, newBranchName, newBranchAddress, admin));
            System.out.println(action + " for " + branch.getName() + " temp loaded");
            return true;
        }
        return false;
    }

    class TempUser {
        private String action;
        private User tempUser;
        private Branch tempBranch;
        private Role tempRole;
        private Admin admin;

        TempUser(String action, User tempUser, Branch tempBranch, Role tempRole, Admin admin) {
            this.action = action;
            this.tempUser = tempUser;
            this.tempBranch = tempBranch;
            this.tempRole = tempRole;
            this.admin = admin;
        }

        String getAction() {
            return action;
        }

        User getTempUser() {
            return tempUser;
        }

        Branch getTempBranch() {
            return tempBranch;
        }

        Role getTempRole() {
            return tempRole;
        }

        Admin getAdmin() {
            return admin;
        }

        String saveString() {
            String tempUserId = "";
            String newUserName = "";
            if (tempUser != null) {
                tempUserId = tempUser.getId();
                if (action.equals("Add"))
                    newUserName = tempUser.getName();
            }
            String tempBranchName = "";
            if (tempBranch != null)
                tempBranchName = tempBranch.getName();
            String tempRoleName = "";
            if (tempRole != null)
                tempRoleName = tempRole.getName();
            String tempAdmin = "";
            if (admin != null)
                tempAdmin = admin.getId();


            return (action + "," + tempUserId + "," + tempBranchName + ","
                    + tempRoleName + "," + tempAdmin + ",,,,,," + newUserName);
        }
    }

    class TempRole {
        private String action;
        private Role role;
        private ArrayList<Double> limits;
        private Admin admin;

        TempRole(String action, Role role, ArrayList<Double> limits, Admin admin) {
            this.action = action;
            this.role = role;
            this.limits = limits;
            this.admin = admin;
        }

        String getAction() {
            return action;
        }

        Role getRole() {
            return role;
        }

        ArrayList<Double> getLimits() {
            return limits;
        }

        Admin getAdmin() {
            return admin;
        }

        String saveString() {
            String limits = "";
            for (int i = 0; i < 5; i++) {
                limits += "," + getLimits().get(i);
            }
            String roleName = "";
            if (role != null)
                roleName = role.getName();
            String tempAdmin = "";
            if (admin != null)
                tempAdmin = admin.getId();
            return (action + ",,," + roleName + "," + tempAdmin + limits);
        }
    }

    class TempBranch {
        private String action;
        private Branch branch;
        private String newBranchName;
        private String newBranchAddress;
        private Admin admin;

        TempBranch(String action, Branch branch, String newBranchName, String newBranchAddress, Admin admin) {
            this.action = action;
            this.branch = branch;
            this.newBranchName = newBranchName;
            this.newBranchAddress = newBranchAddress;
            this.admin = admin;
        }

        String getAction() {
            return action;
        }

        Branch getBranch() {
            return branch;
        }

        String getNewBranchName() {
            return newBranchName;
        }

        String getNewBranchAddress() {
            return newBranchAddress;
        }

        Admin getAdmin() {
            return admin;
        }

        String saveString() {
            String branchName = "";
            String branchAddress = "";
            if (branch != null) {
                branchName = branch.getName();
                branchAddress = branch.getAddress();
            }
            String newTempName = "";
            if (newBranchName != null)
                newTempName = newBranchName;
            String tempAdmin = "";
            if (admin != null)
                tempAdmin = admin.getId();
            return (action + ",," + branchName + ",," + tempAdmin + ",,,,,," + newTempName + "," + branchAddress);
        }
    }


}

