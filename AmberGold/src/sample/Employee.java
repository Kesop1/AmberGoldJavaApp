package sample;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static sample.Controller.downLabel;
import static sample.Main.activeEmployee;
import static sample.Main.program;

public class Employee extends User {
    private Role role;

    Employee(String name) {
        super(name, "E");
        this.role = null;
    }

    public Role getRole() {
        return role;
    }

    void setRole(Role role) {
        this.role = role;
    }

//    _________________ ADD NEW CLIENT  _____________________________

    GridPane addNewClient() {
        downLabel.setText("Adding a new client");
        GridPane gridPane = new GridPane();
        Label mainLabel = new Label("Add a new client");
        GridPane.setColumnSpan(mainLabel, 2);

        Label firstNameLabel = new Label("First name");
        TextField firstNameText = new TextField();

        Label lastNameLabel = new Label("Last name");
        TextField lastNameText = new TextField();

        Label ssNumberLabel = new Label("Social Security Number");
        TextField ssNumberText = new TextField();

        Label addressLabel = new Label("Address");
        TextField addressText = new TextField();

        Label dateOfBirthLabel = new Label("Date of birth");
        DatePicker dateOfBirthText = new DatePicker();

        Button addButton = new Button("Add");
        Button cancelButton = new Button("Cancel");

        GridPane.setHalignment(mainLabel, HPos.CENTER);
        GridPane.setValignment(mainLabel, VPos.CENTER);
        mainLabel.setFont(Font.font(20));
        GridPane.setColumnSpan(mainLabel, 2);
        GridPane.setHalignment(addButton, HPos.CENTER);
        GridPane.setHalignment(cancelButton, HPos.CENTER);
        gridPane.getColumnConstraints().add(new ColumnConstraints(300));
        gridPane.getColumnConstraints().add(new ColumnConstraints(300));

        GridPane.setConstraints(mainLabel, 0, 0);
        GridPane.setConstraints(firstNameLabel, 0, 1);
        GridPane.setConstraints(firstNameText, 1, 1);
        GridPane.setConstraints(lastNameLabel, 0, 2);
        GridPane.setConstraints(lastNameText, 1, 2);
        GridPane.setConstraints(ssNumberLabel, 0, 3);
        GridPane.setConstraints(ssNumberText, 1, 3);
        GridPane.setConstraints(addressLabel, 0, 4);
        GridPane.setConstraints(addressText, 1, 4);
        GridPane.setConstraints(dateOfBirthLabel, 0, 5);
        GridPane.setConstraints(dateOfBirthText, 1, 5);
        GridPane.setConstraints(cancelButton, 0, 6);
        GridPane.setConstraints(addButton, 1, 6);

        gridPane.getChildren().addAll(mainLabel, firstNameLabel, firstNameText, lastNameLabel, lastNameText, ssNumberLabel,
                ssNumberText, addressLabel, addressText, dateOfBirthLabel, dateOfBirthText, addButton, cancelButton);

        addButton.setOnAction(e -> {
            String message;
            if ((firstNameText.getText().equals("")) || (!lettersOnly(firstNameText.getText())))
                message = "First name is incorrect";
            else if ((lastNameText.getText().equals("")) || (!lettersOnly(lastNameText.getText())))
                message = "Last name is incorrect";
            else if (addressText.getText().equals(""))
                message = "Please input client's address";
            else if (ssNumberText.getText().equals(""))
                message = "Social Security number is incorrect";
            else if ((dateOfBirthText.getValue() == null) || (dateOfBirthText.getValue().isAfter(LocalDate.now().minusDays(1))))
                message = "Date of birth is incorrect";
            else {
                int ssNumber = -1;
                try {
                    ssNumber = Integer.parseInt(ssNumberText.getText());
                } catch (NumberFormatException event) {
                    System.out.println(event.getMessage());
                }
                if ((ssNumber > 0) && (program.findBranchByEmployee(activeEmployee).findClientBySsNumber(ssNumber) == null)) {
                    Client client = new Client(firstNameText.getText(), lastNameText.getText(), ssNumber, addressText.getText(), dateOfBirthText.getValue());
                    program.findBranchByEmployee(activeEmployee).getClients().add(client);
                    message = "Client added successfully";
                    gridPane.getChildren().clear();
                } else {
                    message = "Social Security must contain only digits and be unique";
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

    //    _________________ Modify & VIEW & REMOVE  CLIENT  _____________________________

    GridPane existingClient(String action) {
        downLabel.setText(action + " a client");
        GridPane gridPane = new GridPane();
        Label mainLabel = new Label(action + " a client");
        GridPane.setColumnSpan(mainLabel, 2);

        Label ssNumberInputLabel = new Label("Please input client's Social Security number");
        TextField ssNumberInputText = new TextField();

        Label firstNameLabel = new Label("First name");
        TextField firstNameText = new TextField();

        Label lastNameLabel = new Label("Last name");
        TextField lastNameText = new TextField();

        Label ssNumberLabel = new Label("Social Security Number");
        TextField ssNumberText = new TextField();
        ssNumberText.setEditable(false);

        Label addressLabel = new Label("Address");
        TextField addressText = new TextField();

        Label dateOfBirthLabel = new Label("Date of birth");
        DatePicker dateOfBirthText = new DatePicker();

        Label balanceLabel = new Label("Balance");
        TextField balanceText = new TextField();
        balanceText.setEditable(false);

        Label accountNoLabel = new Label("Account number");
        TextField accountNoText = new TextField();
        accountNoText.setEditable(false);

        Label transactionsLabel = new Label("Transactions");

        Button confirmButton = new Button("Confirm");
        Button cancelButton = new Button("Cancel");
        Button searchButton = new Button("Search");
        Button removeButton = new Button("Remove");

        GridPane.setHalignment(mainLabel, HPos.CENTER);
        GridPane.setValignment(mainLabel, VPos.CENTER);
        mainLabel.setFont(Font.font(20));
        GridPane.setColumnSpan(mainLabel, 3);
        GridPane.setHalignment(confirmButton, HPos.CENTER);
        GridPane.setHalignment(cancelButton, HPos.CENTER);
        GridPane.setHalignment(searchButton, HPos.CENTER);
        GridPane.setHalignment(removeButton, HPos.CENTER);

        gridPane.getColumnConstraints().add(new ColumnConstraints(250));
        gridPane.getColumnConstraints().add(new ColumnConstraints(250));
        gridPane.getColumnConstraints().add(new ColumnConstraints(100));

        GridPane.setConstraints(mainLabel, 0, 0);
        GridPane.setConstraints(ssNumberInputLabel, 0, 1);
        GridPane.setConstraints(ssNumberInputText, 1, 1);
        GridPane.setConstraints(searchButton, 2, 1);
        GridPane.setConstraints(firstNameLabel, 0, 2);
        GridPane.setConstraints(firstNameText, 1, 2);
        GridPane.setConstraints(lastNameLabel, 0, 3);
        GridPane.setConstraints(lastNameText, 1, 3);
        GridPane.setConstraints(ssNumberLabel, 0, 4);
        GridPane.setConstraints(ssNumberText, 1, 4);
        GridPane.setConstraints(addressLabel, 0, 5);
        GridPane.setConstraints(addressText, 1, 5);
        GridPane.setConstraints(dateOfBirthLabel, 0, 6);
        GridPane.setConstraints(dateOfBirthText, 1, 6);
        GridPane.setConstraints(accountNoLabel, 0, 7);
        GridPane.setConstraints(accountNoText, 1, 7);
        GridPane.setConstraints(balanceLabel, 0, 8);
        GridPane.setConstraints(balanceText, 1, 8);
        GridPane.setConstraints(cancelButton, 0, 9);
        GridPane.setConstraints(confirmButton, 1, 9);
        GridPane.setConstraints(removeButton, 1, 9);
        GridPane.setConstraints(transactionsLabel, 0, 10);


        gridPane.getChildren().addAll(mainLabel, ssNumberInputLabel, ssNumberInputText, searchButton);

        searchButton.setOnAction(e -> {
            int ssNumber = -1;
            try {
                ssNumber = Integer.parseInt(ssNumberInputText.getText());
            } catch (NumberFormatException e1) {
                System.out.println("Social Security number format is incorrect");
                downLabel.setText("Social Security number format is incorrect");
            }
            Client client = findClientBySsNumber(ssNumber);
            if (client != null) {
                ssNumberInputText.setDisable(true);
                gridPane.getChildren().remove(searchButton);
                TableView<Transaction> transactionTableView = new TableView<>();
                gridPane.getChildren().addAll(firstNameLabel, firstNameText, lastNameLabel, lastNameText, ssNumberLabel,
                        ssNumberText, addressLabel, addressText, dateOfBirthLabel, dateOfBirthText, accountNoLabel, accountNoText,
                        confirmButton, cancelButton);
                firstNameText.setText(client.getFirstName());
                lastNameText.setText(client.getLastName());
                ssNumberText.setText(ssNumberInputText.getText());
                addressText.setText(client.getAddress());
                dateOfBirthText.setValue(client.getDateOfBirth());
                accountNoText.setText(client.getAccountNumber());

                if (!action.equals("Modify")) {
                    gridPane.getChildren().remove(confirmButton);
                    if (action.equals("View")) {
                        transactionTableView = client.transactionTableView();
                        GridPane.setConstraints(transactionTableView, 0, 11);
                        GridPane.setColumnSpan(transactionTableView, 2);
                        gridPane.getChildren().addAll(transactionsLabel, transactionTableView);
                    } else gridPane.getChildren().add(removeButton);
                    gridPane.getChildren().addAll(balanceLabel, balanceText);
                    balanceText.setText(Double.toString(client.getBalance()));
                    firstNameText.setEditable(false);
                    lastNameText.setEditable(false);
                    firstNameText.setEditable(false);
                    addressText.setEditable(false);
                    dateOfBirthText.setDisable(true);

                    removeButton.setOnAction(e1 -> {
                        String message;
                        if (client.getBalance() > 0) {
                            message = "Client still has some money on the account";
                        } else {
                            message = "Client removed successfully";
                            program.findBranchByEmployee(activeEmployee).getClients().remove(client);
                            gridPane.getChildren().clear();
                        }
                        downLabel.setText(message);
                        System.out.println(message);
                    });

                }

                confirmButton.setOnAction(e1 -> {
                    String message;
                    if ((firstNameText.getText().equals("")) || (!lettersOnly(firstNameText.getText())))
                        message = "First name is incorrect";
                    else if ((lastNameText.getText().equals("")) || (!lettersOnly(lastNameText.getText())))
                        message = "Last name is incorrect";
                    else if (addressText.getText().equals(""))
                        message = "Please input client's address";
                    else if ((dateOfBirthText.getValue() == null) || (dateOfBirthText.getValue().isAfter(LocalDate.now().minusDays(1))))
                        message = "Date of birth is incorrect";
                    else {
                        boolean changed = false;
                        if (!client.getFirstName().equals(firstNameText.getText())) {
                            client.setFirstName(firstNameText.getText());
                            changed = true;
                        }
                        if (!client.getLastName().equals(lastNameText.getText())) {
                            client.setLastName(lastNameText.getText());
                            changed = true;
                        }
                        if (!client.getAddress().equals(addressText.getText())) {
                            client.setAddress(addressText.getText());
                            changed = true;
                        }
                        if (!client.getDateOfBirth().equals(dateOfBirthText.getValue())) {
                            client.setDateOfBirth(dateOfBirthText.getValue());
                            changed = true;
                        }
                        if (changed) {
                            message = "Client modified successfully";
                            gridPane.getChildren().clear();
                        } else
                            message = "Nothing to change";
                    }

                    downLabel.setText(message);
                    System.out.println(message);
                });
            } else {
                System.out.println("Client not found");
                downLabel.setText("Client not found");
            }
        });

        cancelButton.setOnAction(e -> {
            gridPane.getChildren().clear();
            downLabel.setText("What do you want to do now?");
        });
        return gridPane;
    }

    //    _________________ LIST ALL CLIENTS  _____________________________

    GridPane listClients() {
        downLabel.setText("Listing clients");
        GridPane gridPane = new GridPane();
        Label mainLabel = new Label("Clients in the branch");

        Button cancelButton = new Button("Cancel");

        GridPane.setHalignment(mainLabel, HPos.CENTER);
        GridPane.setValignment(mainLabel, VPos.CENTER);
        mainLabel.setFont(Font.font(20));
        GridPane.setHalignment(cancelButton, HPos.CENTER);
        gridPane.getColumnConstraints().add(new ColumnConstraints(600));

        GridPane.setConstraints(mainLabel, 0, 0);
        GridPane.setConstraints(cancelButton, 0, 2);

        TableView<Client> clientTableView = program.findBranchByEmployee(activeEmployee).clientTableView();
        GridPane.setConstraints(clientTableView, 0, 1);
        gridPane.getChildren().addAll(mainLabel, clientTableView, cancelButton);

        return gridPane;
    }

    //    _________________ ADD CLIENT TRANSACTION  _____________________________

    GridPane addClientTransaction() {
        downLabel.setText("Add a client transaction");
        GridPane gridPane = new GridPane();
        Label mainLabel = new Label("Add a client transaction");
        GridPane.setColumnSpan(mainLabel, 2);

        Label ssNumberInputLabel = new Label("Please input client's Social Security number");
        TextField ssNumberInputText = new TextField();

        Label balanceLabel = new Label("Balance");
        TextField balanceText = new TextField();
        balanceText.setEditable(false);

        Label accountNoLabel = new Label("Account number");
        TextField accountNoText = new TextField();
        accountNoText.setEditable(false);

        Label transactionTypeLabel = new Label("Choose the transaction type");
        ChoiceBox<String> transactionTypeChoice = new ChoiceBox<>();
        transactionTypeChoice.getItems().addAll("Deposit", "Withdrawal", "Transfer", "Payment", "Online");

        Label amountLabel = new Label("Amount");
        TextField amountText = new TextField();

        Label dateLabel = new Label("Date");
        DatePicker dateText = new DatePicker();

        Label receiverLabel = new Label("Receiver");
        TextField receiverText = new TextField();

        CheckBox cashBox = new CheckBox("Cash?");

        Button confirmButton = new Button("Confirm");
        Button cancelButton = new Button("Cancel");
        Button searchButton = new Button("Search");
        Button approveButton = new Button("Get approval");

        GridPane.setHalignment(mainLabel, HPos.CENTER);
        GridPane.setValignment(mainLabel, VPos.CENTER);
        mainLabel.setFont(Font.font(20));
        GridPane.setColumnSpan(mainLabel, 3);
        GridPane.setHalignment(confirmButton, HPos.CENTER);
        GridPane.setHalignment(cancelButton, HPos.CENTER);
        GridPane.setHalignment(searchButton, HPos.CENTER);
        GridPane.setHalignment(approveButton, HPos.CENTER);
        gridPane.getColumnConstraints().add(new ColumnConstraints(250));
        gridPane.getColumnConstraints().add(new ColumnConstraints(250));
        gridPane.getColumnConstraints().add(new ColumnConstraints(100));

        GridPane.setConstraints(mainLabel, 0, 0);
        GridPane.setConstraints(ssNumberInputLabel, 0, 1);
        GridPane.setConstraints(ssNumberInputText, 1, 1);
        GridPane.setConstraints(searchButton, 2, 1);
        GridPane.setConstraints(accountNoLabel, 0, 2);
        GridPane.setConstraints(accountNoText, 1, 2);
        GridPane.setConstraints(balanceLabel, 0, 3);
        GridPane.setConstraints(balanceText, 1, 3);
        GridPane.setConstraints(transactionTypeLabel, 0, 4);
        GridPane.setConstraints(transactionTypeChoice, 1, 4);
        GridPane.setConstraints(amountLabel, 0, 5);
        GridPane.setConstraints(amountText, 1, 5);
        GridPane.setConstraints(cashBox, 1, 6);
        GridPane.setConstraints(dateLabel, 0, 7);
        GridPane.setConstraints(dateText, 1, 7);
        GridPane.setConstraints(receiverLabel, 0, 8);
        GridPane.setConstraints(receiverText, 1, 8);
        GridPane.setConstraints(cancelButton, 0, 9);
        GridPane.setConstraints(confirmButton, 1, 9);
        GridPane.setConstraints(approveButton, 2, 9);


        gridPane.getChildren().addAll(mainLabel, ssNumberInputLabel, ssNumberInputText, searchButton);

        searchButton.setOnAction(e -> {
            int ssNumber = -1;
            try {
                ssNumber = Integer.parseInt(ssNumberInputText.getText());
            } catch (NumberFormatException e1) {
                System.out.println("Social Security number format is incorrect");
                downLabel.setText("Social Security number format is incorrect");
            }
            Client client = findClientBySsNumber(ssNumber);
            if (client != null) {
                ssNumberInputText.setDisable(true);
                gridPane.getChildren().remove(searchButton);
                gridPane.getChildren().addAll(accountNoLabel, accountNoText, balanceLabel, balanceText, transactionTypeLabel,
                        transactionTypeChoice, amountLabel, amountText, dateLabel, dateText, confirmButton, cancelButton);
                accountNoText.setText(client.getAccountNumber());
                balanceText.setText(Double.toString(client.getBalance()));

                transactionTypeChoice.getSelectionModel().selectedItemProperty().addListener
                        ((v, oldValue, newValue) -> {
                            switch (newValue) {
                                case "Deposit":
                                    gridPane.getChildren().removeAll(cashBox, receiverLabel, receiverText);
                                    gridPane.getChildren().add(cashBox);
                                    break;
                                case "Withdrawal":
                                    gridPane.getChildren().removeAll(cashBox, receiverLabel, receiverText);
                                    break;
                                default:
                                    gridPane.getChildren().removeAll(cashBox, receiverLabel, receiverText);
                                    gridPane.getChildren().addAll(receiverLabel, receiverText);
                                    break;
                            }
                        });
                confirmButton.setOnAction(e1 -> {
                    String message;
                    String type = transactionTypeChoice.getValue();
                    if (type.equals(""))
                        message = "Please choose the transaction type";
                    else if (amountText.getText().equals(""))
                        message = "Please input transaction amount";
                    else if (dateText.getValue() == null)
                        message = "Please choose a date";
                    else if (receiverLabel.getText().equals(""))
                        message = "Please input the account number of the receiver";
                    else if (((type.equals("Transfer")) || (type.equals("Payment")) || (type.equals("Online")))
                            && (receiverText.getText().equals("")))
                        message = "Please input a receiver";
                    else {
                        Double amount = 0.0;
                        try {
                            boolean approval = false;
                            amount = Double.parseDouble(amountText.getText());
                            Transaction transaction = new Transaction(client.getAccountNumber(), receiverText.getText(),
                                    type.toLowerCase(), amount, dateText.getValue(), activeEmployee.getId());
                            if ((type.equals("Withdrawal")) && ((activeEmployee.getRole().getLimits().get(1) < amount)
                                    || (amount > program.findBranchByEmployee(activeEmployee).getCashInBranch()))) {
                                if (activeEmployee.getRole().getLimits().get(1) < amount) {
                                    message = "You are not allowed to perform " + type.toLowerCase() + " operation for that amount";
                                    approval = approveBox(program.findBranchByEmployee(activeEmployee));
                                } else
                                    message = "The branch only has " + program.findBranchByEmployee(activeEmployee).getCashInBranch() + " in cash";
                            } else if ((type.equals("Deposit")) && (activeEmployee.getRole().getLimits().get(0) < amount)) {
                                message = "You are not allowed to perform " + type.toLowerCase() + " operation for that amount";
                                approval = approveBox(program.findBranchByEmployee(activeEmployee));
                            } else if ((type.equals("Transfer")) && (activeEmployee.getRole().getLimits().get(2) < amount)) {
                                message = "You are not allowed to perform " + type.toLowerCase() + " operation for that amount";
                                approval = approveBox(program.findBranchByEmployee(activeEmployee));
                            } else if ((type.equals("Payment")) && (activeEmployee.getRole().getLimits().get(3) < amount)) {
                                message = "You are not allowed to perform " + type.toLowerCase() + " operation for that amount";
                                approval = approveBox(program.findBranchByEmployee(activeEmployee));
                            } else if ((type.equals("Online")) && (activeEmployee.getRole().getLimits().get(4) < amount)) {
                                message = "You are not allowed to perform " + type.toLowerCase() + " operation for that amount";
                                approval = approveBox(program.findBranchByEmployee(activeEmployee));
                            } else {
                                gridPane.getChildren().clear();
                                message = client.transaction(transaction);
                                Branch senderBranch = program.findBranchByClientSs(client.getSocialSecurityNumber());
                                senderBranch.getTransactions().add(transaction);
                                if (type.equals("Transfer")) {
                                    Client receiver = program.findClientByAccount(receiverText.getText());
                                    if (receiver != null) {
                                        System.out.println(receiver.transaction(transaction));
                                        message += ". Also added funds for " + receiver.getLastName();
                                        Branch receiverBranch = program.findBranchByClientSs(receiver.getSocialSecurityNumber());
                                        if (senderBranch != receiverBranch)
                                            receiverBranch.getTransactions().add(transaction);
                                    }
                                } else if ((type.equals("Deposit")) && (cashBox.isSelected()))
                                    program.findBranchByEmployee(activeEmployee).setCashInBranch
                                            (program.findBranchByEmployee(activeEmployee).getCashInBranch() + amount);

                            }
                            if (approval) {
                                program.findBranchByEmployee(activeEmployee).getManager().getTempTransactions().add(transaction);
                                message = "Transaction sent to the manager for approval";
                                gridPane.getChildren().clear();
                            }
                        } catch (NumberFormatException e2) {
                            message = "Invalid amount";
                            System.out.println(e2.getMessage());
                        }
                    }
                    downLabel.setText(message);
                    System.out.println(message);
                });
            } else {
                System.out.println("Client not found");
                downLabel.setText("Client not found");
            }
        });

        cancelButton.setOnAction(e -> {
            gridPane.getChildren().clear();
            downLabel.setText("What do you want to do now?");
        });
        return gridPane;
    }

    private boolean approveBox(Branch branch) {
        if (branch.getManager() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Not sufficient access");
            alert.setHeaderText("Ask manager for approval?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) return true;
            else return false;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Not sufficient access");
            alert.setHeaderText("You do not have a manager to ask for approval!");
            Optional<ButtonType> result = alert.showAndWait();
            return false;
        }
    }

//    _____________________  ADD TRANSACTION FROM ANOTHER BANK  ______________________

    GridPane addClientDeposit() {
        downLabel.setText("Add a client deposit");
        GridPane gridPane = new GridPane();
        Label mainLabel = new Label("Add a client deposit");
        GridPane.setColumnSpan(mainLabel, 2);
        Branch branch = program.findBranchByEmployee(activeEmployee);

        Label senderIdLabel = new Label("Please input the sender's ID");
        TextField senderIdText = new TextField();

        Label clientLabel = new Label("Choose the client");
        ChoiceBox<Integer> clientChoice = new ChoiceBox<>();
        clientChoice.getItems().addAll(branch.clientsListString());

        Label firstNameLabel = new Label("First name");
        TextField firstNameText = new TextField();
        firstNameText.setEditable(false);

        Label lastNameLabel = new Label("Last name");
        TextField lastNameText = new TextField();
        lastNameText.setEditable(false);

        Label ssNumberLabel = new Label("Social Security Number");
        TextField ssNumberText = new TextField();
        ssNumberText.setEditable(false);

        Label addressLabel = new Label("Address");
        TextField addressText = new TextField();
        addressText.setEditable(false);

        Label dateOfBirthLabel = new Label("Date of birth");
        TextField dateOfBirthText = new TextField();
        dateOfBirthText.setEditable(false);

        Label accountNoLabel = new Label("Account number");
        TextField accountNoText = new TextField();
        accountNoText.setEditable(false);

        Label amountLabel = new Label("Amount");
        TextField amountText = new TextField();

        Label dateLabel = new Label("Date");
        DatePicker dateText = new DatePicker();

        Button confirmButton = new Button("Confirm");
        Button cancelButton = new Button("Cancel");
        Button searchButton = new Button("Search");

        GridPane.setHalignment(mainLabel, HPos.CENTER);
        GridPane.setValignment(mainLabel, VPos.CENTER);
        mainLabel.setFont(Font.font(20));
        GridPane.setColumnSpan(mainLabel, 3);
        GridPane.setHalignment(confirmButton, HPos.CENTER);
        GridPane.setHalignment(cancelButton, HPos.CENTER);
        GridPane.setHalignment(searchButton, HPos.CENTER);
        gridPane.getColumnConstraints().add(new ColumnConstraints(250));
        gridPane.getColumnConstraints().add(new ColumnConstraints(250));
        gridPane.getColumnConstraints().add(new ColumnConstraints(100));


        GridPane.setConstraints(mainLabel, 0, 0);
        GridPane.setConstraints(senderIdLabel, 0, 1);
        GridPane.setConstraints(senderIdText, 1, 1);
        GridPane.setConstraints(clientLabel, 0, 2);
        GridPane.setConstraints(clientChoice, 1, 2);
        GridPane.setConstraints(searchButton, 2, 2);
        GridPane.setConstraints(firstNameLabel, 0, 3);
        GridPane.setConstraints(firstNameText, 1, 3);
        GridPane.setConstraints(lastNameLabel, 0, 4);
        GridPane.setConstraints(lastNameText, 1, 4);
        GridPane.setConstraints(ssNumberLabel, 0, 5);
        GridPane.setConstraints(ssNumberText, 1, 5);
        GridPane.setConstraints(addressLabel, 0, 6);
        GridPane.setConstraints(addressText, 1, 6);
        GridPane.setConstraints(dateOfBirthLabel, 0, 7);
        GridPane.setConstraints(dateOfBirthText, 1, 7);
        GridPane.setConstraints(accountNoLabel, 0, 8);
        GridPane.setConstraints(accountNoText, 1, 8);
        GridPane.setConstraints(dateLabel, 0, 9);
        GridPane.setConstraints(dateText, 1, 9);
        GridPane.setConstraints(amountLabel, 0, 10);
        GridPane.setConstraints(amountText, 1, 10);
        GridPane.setConstraints(cancelButton, 0, 11);
        GridPane.setConstraints(confirmButton, 1, 11);

        gridPane.getChildren().addAll(mainLabel, senderIdLabel, senderIdText, clientLabel, clientChoice, searchButton);

        searchButton.setOnAction(e -> {
            Client client = findClientBySsNumber(clientChoice.getValue());
            if (client != null) {
                clientChoice.setDisable(true);
                gridPane.getChildren().remove(searchButton);
                gridPane.getChildren().addAll(firstNameLabel, firstNameText, lastNameLabel, lastNameText, ssNumberLabel,
                        ssNumberText, addressLabel, addressText, dateOfBirthLabel, dateOfBirthText, accountNoLabel, accountNoText,
                        dateLabel, dateText, amountLabel, amountText, cancelButton, confirmButton);
                firstNameText.setText(client.getFirstName());
                lastNameText.setText(client.getLastName());
                ssNumberText.setText(String.valueOf(client.getSocialSecurityNumber()));
                addressText.setText(client.getAddress());
                dateOfBirthText.setText(client.getDateOfBirth().toString());
                accountNoText.setText(client.getAccountNumber());

                confirmButton.setOnAction(e1 -> {
                    String message;
                    int amount = -1;
                    try {
                        amount = Integer.parseInt(amountText.getText());
                        message = "Amount is correct";
                    } catch (NumberFormatException e2) {
                        message = "Please input a valid amount";
                        System.out.println(e2.getMessage());
                    }
                    if (dateText.getValue() == null)
                        message = "Please insert a date";
                    else if (amount > 0) {
                        Transaction transaction = new Transaction(senderIdText.getText(), client.getAccountNumber(),
                                "deposit", amount, dateText.getValue(), activeEmployee.getId());
                        client.transaction(transaction);
                        branch.getTransactions().add(transaction);
                        message = transaction.getTransactionId() + " added to " + client.getAccountNumber() + " and " +
                                branch.getName();
                        gridPane.getChildren().clear();
                    }
                    downLabel.setText(message);
                    System.out.println(message);
                });
            } else {
                System.out.println("Client not found");
                downLabel.setText("Client not found");
            }
        });

        cancelButton.setOnAction(e -> {
            gridPane.getChildren().clear();
            downLabel.setText("What do you want to do now?");
        });
        return gridPane;
    }


    //    _________________ VIEW ACCESS  _____________________________

    GridPane viewAccess() {
        downLabel.setText("View my access");
        GridPane gridPane = new GridPane();
        Label mainLabel = new Label("View my access");
        GridPane.setColumnSpan(mainLabel, 2);

        Label loginLabel = new Label("Login");
        TextField loginText = new TextField(activeEmployee.getId());
        loginText.setEditable(false);

        Label roleLabel = new Label("Role");
        TextField roleText = new TextField(activeEmployee.getRole().getName());
        roleText.setEditable(false);

        Label limitsLabel = new Label("Limits");

        Label depositLabel = new Label("Deposit");
        TextField depositText = new TextField(String.valueOf(activeEmployee.getRole().getDepositLimit()));
        depositText.setEditable(false);

        Label withdrawalLabel = new Label("Withdrawal");
        TextField withdrawalText = new TextField(String.valueOf(activeEmployee.getRole().getWithdrawalLimit()));
        withdrawalText.setEditable(false);

        Label transferLabel = new Label("Transfer");
        TextField transferText = new TextField(String.valueOf(activeEmployee.getRole().getTransferLimit()));
        transferText.setEditable(false);

        Label paymentLabel = new Label("Payment");
        TextField paymentText = new TextField(String.valueOf(activeEmployee.getRole().getPaymentLimit()));
        paymentText.setEditable(false);

        Label onlineLabel = new Label("Online");
        TextField onlineText = new TextField(String.valueOf(activeEmployee.getRole().getOnlineLimit()));
        onlineText.setEditable(false);

        Button cancelButton = new Button("Cancel");

        GridPane.setHalignment(mainLabel, HPos.CENTER);
        GridPane.setValignment(mainLabel, VPos.CENTER);
        mainLabel.setFont(Font.font(20));
        GridPane.setColumnSpan(mainLabel, 2);
        GridPane.setColumnSpan(limitsLabel, 2);
        GridPane.setHalignment(cancelButton, HPos.CENTER);
        GridPane.setHalignment(limitsLabel, HPos.CENTER);
        gridPane.getColumnConstraints().add(new ColumnConstraints(300));
        gridPane.getColumnConstraints().add(new ColumnConstraints(300));


        GridPane.setConstraints(mainLabel, 0, 0);
        GridPane.setConstraints(loginLabel, 0, 1);
        GridPane.setConstraints(loginText, 1, 1);
        GridPane.setConstraints(roleLabel, 0, 2);
        GridPane.setConstraints(roleText, 1, 2);
        GridPane.setConstraints(limitsLabel, 0, 3);
        GridPane.setConstraints(depositLabel, 0, 4);
        GridPane.setConstraints(depositText, 1, 4);
        GridPane.setConstraints(withdrawalLabel, 0, 5);
        GridPane.setConstraints(withdrawalText, 1, 5);
        GridPane.setConstraints(transferLabel, 0, 6);
        GridPane.setConstraints(transferText, 1, 6);
        GridPane.setConstraints(paymentLabel, 0, 7);
        GridPane.setConstraints(paymentText, 1, 7);
        GridPane.setConstraints(onlineLabel, 0, 8);
        GridPane.setConstraints(onlineText, 1, 8);
        GridPane.setConstraints(cancelButton, 0, 9);


        gridPane.getChildren().addAll(mainLabel, loginLabel, loginText, roleLabel, roleText, limitsLabel, depositLabel, depositText,
                withdrawalLabel, withdrawalText, transferLabel, transferText, paymentLabel, paymentText, onlineLabel, onlineText, cancelButton);

        cancelButton.setOnAction(e -> {
            gridPane.getChildren().clear();
            downLabel.setText("What do you want to do now?");
        });
        return gridPane;
    }

    //    _________________  CREATE A REPORT  _____________________________

    GridPane createReport() {
        downLabel.setText("Create a report");
        GridPane gridPane = new GridPane();
        Label mainLabel = new Label("Create a report");
        GridPane.setColumnSpan(mainLabel, 2);

        Label dateFromLabel = new Label("Date from");
        DatePicker dateFromText = new DatePicker();

        Label dateToLabel = new Label("Date to");
        DatePicker dateToText = new DatePicker();

        final ToggleGroup group = new ToggleGroup();

        RadioButton allClients = new RadioButton("All clients");
        allClients.setToggleGroup(group);
        allClients.setSelected(true);

        RadioButton oneClient = new RadioButton("Specific client");
        oneClient.setToggleGroup(group);

        Label clientNumberLabel = new Label("Client's account number");
        TextField clientNumberText = new TextField();

        Button confirmButton = new Button("Confirm");
        Button cancelButton = new Button("Cancel");
        Button exportButton = new Button("Export to CSV");

        GridPane.setHalignment(mainLabel, HPos.CENTER);
        GridPane.setValignment(mainLabel, VPos.CENTER);
        mainLabel.setFont(Font.font(20));
        GridPane.setColumnSpan(mainLabel, 2);
        GridPane.setHalignment(confirmButton, HPos.CENTER);
        GridPane.setHalignment(cancelButton, HPos.CENTER);
        gridPane.getColumnConstraints().add(new ColumnConstraints(300));
        gridPane.getColumnConstraints().add(new ColumnConstraints(300));

        GridPane.setConstraints(mainLabel, 0, 0);
        GridPane.setConstraints(dateFromLabel, 0, 1);
        GridPane.setConstraints(dateFromText, 1, 1);
        GridPane.setConstraints(dateToLabel, 0, 2);
        GridPane.setConstraints(dateToText, 1, 2);
        GridPane.setConstraints(allClients, 0, 3);
        GridPane.setConstraints(oneClient, 0, 4);
        GridPane.setConstraints(clientNumberLabel, 0, 5);
        GridPane.setConstraints(clientNumberText, 1, 5);
        GridPane.setConstraints(cancelButton, 0, 6);
        GridPane.setConstraints(confirmButton, 1, 6);
        GridPane.setConstraints(exportButton, 1, 9);

        gridPane.getChildren().addAll(mainLabel, dateFromLabel, dateFromText, dateToLabel, dateToText, allClients,
                oneClient, confirmButton, cancelButton);

        group.selectedToggleProperty().addListener((v, oldValue, newValue) -> {
            if (newValue.equals(oneClient)) {
                gridPane.getChildren().removeAll(clientNumberLabel, clientNumberText);
                gridPane.getChildren().addAll(clientNumberLabel, clientNumberText);

            } else {
                gridPane.getChildren().removeAll(clientNumberLabel, clientNumberText);
            }
        });
        confirmButton.setOnAction(e -> {
            TableView<Transaction> reportTableView = new TableView<>();
            String message2;
            ArrayList<Client> clientArrayList = new ArrayList<>();
            if ((dateFromText.getValue() != null) && (dateToText.getValue() != null) &&
                    dateFromText.getValue().isBefore(dateToText.getValue().plusDays(1))) {
                if (!group.getSelectedToggle().equals(allClients)) {
                    if (clientNumberText.getText() != null) {
                        Client client = program.findClientByAccount(clientNumberText.getText());
                        if (program.findBranchByEmployee(activeEmployee).getClients().contains(client)) {
                            message2 = "Will generate a report for " + client.getLastName() + ", " + client.getFirstName()
                                    + " between " + dateFromText.getValue() + " and " + dateToText.getValue();
                            clientArrayList.add(client);
                            Report report = new Report(clientArrayList, dateFromText.getValue(), dateToText.getValue());
                            reportTableView = report.transactionTableView(report.transactionListClient());
                            gridPane.getChildren().remove(confirmButton);
                            GridPane.setColumnSpan(reportTableView, 2);
                            GridPane.setConstraints(reportTableView, 0, 7);
                            gridPane.getChildren().addAll(reportTableView, exportButton);

                            exportButton.setOnAction(e1 -> program.exportTransactions(activeEmployee, report.transactionListClient()));
                        } else message2 = "Cannot find this client in your branch";
                    } else message2 = "Please input the client's account number";
                } else {
                    message2 = "Will generate a report for all clients between " +
                            dateFromText.getValue() + " and " + dateToText.getValue();
                    clientArrayList.addAll(program.findBranchByEmployee(activeEmployee).getClients());
                    Report report = new Report(clientArrayList, dateFromText.getValue(), dateToText.getValue());
                    reportTableView = report.transactionTableView(report.transactionListClient());
                    gridPane.getChildren().remove(confirmButton);
                    GridPane.setColumnSpan(reportTableView, 2);
                    GridPane.setConstraints(reportTableView, 0, 8);
                    gridPane.getChildren().addAll(reportTableView, exportButton);

                    exportButton.setOnAction(e1 -> program.exportTransactions(activeEmployee, report.transactionListClient()));
                }
            } else message2 = "Please choose the correct dates";

            downLabel.setText(message2);
            System.out.println(message2);
        });

        cancelButton.setOnAction(e -> {
            gridPane.getChildren().clear();
            downLabel.setText("What do you want to do now?");
        });
        return gridPane;
    }


    private Client findClientBySsNumber(int ssNumber) {
        if (ssNumber > -1) {
            for (Client client : program.findBranchByEmployee(activeEmployee).getClients()) {
                if (client.getSocialSecurityNumber() == ssNumber) {
                    System.out.println(client.getLastName() + " found");
                    return client;
                }
            }
        }
        return null;
    }

    private boolean lettersOnly(String name) {
        char[] chars = name.toCharArray();
        for (char c : chars) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }

    @Override
    String saveString() {
        String string = (super.saveString() + ",");
        if (this.getRole() != null) string += this.role.getName();
        return string;
    }

    TreeView<String> employeeTreeView() {
        TreeItem<String> root;
        root = new TreeItem<>();
        root.setExpanded(true);

        makeBranch("Add a new client", root);
        makeBranch("Modify client's details", root);    //szukaj po PESELu
        makeBranch("View client's details", root);  //szukaj po PESELu
        makeBranch("Remove a client", root);    //szukaj po PESELu
        makeBranch("List all clients", root);   //nie pokazuj PESELu
        makeBranch("Add a client transaction", root);   //szukaj po PESELu
        makeBranch("Add a client deposit", root);
        makeBranch("View my access", root);
        makeBranch("Create a report", root);

        TreeView<String> treeView = new TreeView<>(root);
        treeView.setShowRoot(false);
        treeView.setMaxWidth(200);

        return treeView;
    }

    private TreeItem<String> makeBranch(String title, TreeItem<String> parent) {
        TreeItem<String> item = new TreeItem<>(title);
        item.setExpanded(false);
        parent.getChildren().add(item);
        return item;
    }

}
