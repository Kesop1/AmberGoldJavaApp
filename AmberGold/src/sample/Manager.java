package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;

import static sample.Controller.downLabel;
import static sample.Main.activeManager;
import static sample.Main.program;

class Manager extends User {
    private ArrayList<Transaction> tempTransactions;

    Manager(String name) {
        super(name, "M");
        this.tempTransactions = new ArrayList<>();
    }

    ArrayList<Transaction> getTempTransactions() {
        return tempTransactions;
    }

    TreeView<String> managerTreeView() {
        TreeItem<String> root;
        root = new TreeItem<>();
        root.setExpanded(true);

        makeBranch("Approve/reject transactions", root);
        makeBranch("View your branch", root);
        makeBranch("Add a branch transaction", root);
        makeBranch("List all clients in the branch", root);
        makeBranch("List all employees in the branch", root);
        makeBranch("Generate a branch report", root);

        javafx.scene.control.TreeView<java.lang.String> treeView = new TreeView<>(root);
        treeView.setShowRoot(false);
        treeView.setMaxWidth(200);
        root.setExpanded(true);
        return treeView;
    }

    private TreeItem<String> makeBranch(String title, TreeItem<String> parent) {
        TreeItem<String> item = new TreeItem<>(title);
        item.setExpanded(false);
        parent.getChildren().add(item);
        return item;
    }

//    -------------------------------  AUTHORIZE TEMP TRANSACTION   --------------------------

    GridPane approveTempTransactions() {
        downLabel.setText("Choose a transaction");
        GridPane gridPane = new GridPane();
        Label mainLabel = new Label("Authorize a transaction");

        Button approveButton = new Button("Approve");
        Button rejectButton = new Button("Reject");
        Button cancelButton = new Button("Cancel");

        TableView<Transaction> transactionTableView = transactionTableView(tempTransactionsList());
        transactionTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        GridPane.setHalignment(mainLabel, HPos.CENTER);
        GridPane.setValignment(mainLabel, VPos.CENTER);
        mainLabel.setFont(Font.font(20));
        GridPane.setColumnSpan(mainLabel, 3);
        GridPane.setColumnSpan(transactionTableView, 3);
        GridPane.setHalignment(approveButton, HPos.CENTER);
        GridPane.setHalignment(rejectButton, HPos.CENTER);
        GridPane.setHalignment(cancelButton, HPos.CENTER);


        gridPane.getColumnConstraints().add(new ColumnConstraints(200));
        gridPane.getColumnConstraints().add(new ColumnConstraints(200));
        gridPane.getColumnConstraints().add(new ColumnConstraints(200));

        GridPane.setConstraints(mainLabel, 0, 0);
        GridPane.setConstraints(transactionTableView, 0, 1);
        GridPane.setConstraints(cancelButton, 0, 2);
        GridPane.setConstraints(rejectButton, 1, 2);
        GridPane.setConstraints(approveButton, 2, 2);

        gridPane.getChildren().addAll(mainLabel, transactionTableView, approveButton, rejectButton, cancelButton);

        approveButton.setOnAction(e -> {
            ObservableList<Transaction> list = transactionTableView.getSelectionModel().getSelectedItems();
            if (list != null) {
                for (Transaction transaction : list) {
                    Client clientSender = program.findClientByAccount(transaction.getAccountNumberSender());
                    boolean enoughFunds = true;
                    if (clientSender != null) {
                        Branch clientSenderBranch = program.findBranchByClientSs(clientSender.getSocialSecurityNumber());
                        if (clientSenderBranch == program.findBranchByManager(activeManager)) {
                            if((!transaction.getType().equals("deposit")) && (transaction.getAmount()>clientSender.getBalance()))
                                enoughFunds = false;
                            else if(transaction.getType().equals("withdrawal") && (transaction.getAmount()>clientSenderBranch.getCashInBranch()))
                                enoughFunds = false;
                            if(enoughFunds) {
                                clientSender.transaction(transaction);
                                clientSenderBranch.getTransactions().add(transaction);
                                System.out.println("Transaction approved");
                                downLabel.setText("Transaction approved");
                            }else {
                                System.out.println("Not enough funds");
                                downLabel.setText("Not enough funds");
                                tempTransactions.remove(transaction);
                            }
                        }
                    }
                    if (transaction.getType().equals("Transfer")) {
                        Client clientReceiver = program.findClientByAccount(transaction.getAccountNumberReceiver());
                        if (clientReceiver != null) {
                            Branch clientReceiverBranch = program.findBranchByClientSs(clientReceiver.getSocialSecurityNumber());
                            if (clientReceiverBranch != program.findBranchByManager(activeManager)) {
                                clientReceiver.transaction(transaction);
                                clientReceiverBranch.getTransactions().add(transaction);
                            }
                        }
                    }
                    tempTransactions.remove(transaction);
                }
                gridPane.getChildren().clear();
            }
        });

        rejectButton.setOnAction(e -> {
            ObservableList<Transaction> list = transactionTableView.getSelectionModel().getSelectedItems();
            if (list != null) {
                for (Transaction transaction : list) {
                    tempTransactions.remove(transaction);
                }
            }
            gridPane.getChildren().clear();
        });

        cancelButton.setOnAction(e -> {
            gridPane.getChildren().clear();
            downLabel.setText("What do you want to do now?");
        });

        return gridPane;
    }

    private TableView<Transaction> transactionTableView(ObservableList<Transaction> list) {
        TableView<Transaction> transactionTableView = new TableView<>();

        TableColumn<Transaction, String> accountSenderColumn = new TableColumn<>("Sender");
        accountSenderColumn.setMinWidth(100);
        accountSenderColumn.setCellValueFactory(new PropertyValueFactory<>("accountNumberSender"));

        TableColumn<Transaction, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setMinWidth(50);
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Transaction, Double> amountColumn = new TableColumn<>("Amount");
        amountColumn.setMinWidth(50);
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<Transaction, LocalDate> dateColumn = new TableColumn<>("Date");
        dateColumn.setMinWidth(70);
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Transaction, String> accountReceiverColumn = new TableColumn<>("Receiver");
        accountReceiverColumn.setMinWidth(120);
        accountReceiverColumn.setCellValueFactory(new PropertyValueFactory<>("accountNumberReceiver"));

        TableColumn<Transaction, Integer> transactionIdColumn = new TableColumn<>("Transaction ID");
        transactionIdColumn.setMinWidth(110);
        transactionIdColumn.setCellValueFactory(new PropertyValueFactory<>("transactionId"));

        TableColumn<Transaction, String> employeeColumn = new TableColumn<>("Employee");
        employeeColumn.setMinWidth(50);
        employeeColumn.setCellValueFactory(new PropertyValueFactory<>("employeeId"));

        transactionTableView.setItems(list);
        //noinspection unchecked
        transactionTableView.getColumns().addAll(accountSenderColumn, typeColumn, amountColumn, dateColumn, accountReceiverColumn,
                transactionIdColumn, employeeColumn);
        return transactionTableView;
    }


    private ObservableList<Transaction> tempTransactionsList() {
        ObservableList<Transaction> temp = FXCollections.observableArrayList();
        tempTransactions.forEach(temp::addAll);
        return temp;
    }

    //    -------------------------------  VIEW YOUR BRANCH   --------------------------

    GridPane viewBranch() {
        downLabel.setText("View your branch");
        GridPane gridPane = new GridPane();
        Label mainLabel = new Label("View your branch");

        Branch branch = program.findBranchByManager(activeManager);

        Label nameLabel = new Label(branch.getName());

        Label addressLabel = new Label("Address");
        TextField addressText = new TextField(branch.getAddress());
        addressText.setEditable(false);

        Label cashLabel = new Label("Cash in the branch");
        TextField cashText = new TextField(String.valueOf(branch.getCashInBranch()));
        cashText.setEditable(false);

        Label transactionsLabel = new Label("Transactions");
        TableView<Transaction> transactionTableView = transactionTableView(branchTransactionsList());

        Button cancelButton = new Button("Cancel");

        GridPane.setHalignment(mainLabel, HPos.CENTER);
        GridPane.setValignment(mainLabel, VPos.CENTER);
        mainLabel.setFont(Font.font(20));
        GridPane.setColumnSpan(mainLabel, 2);
        GridPane.setColumnSpan(nameLabel, 2);
        nameLabel.setFont(Font.font(30));
        GridPane.setHalignment(nameLabel, HPos.CENTER);
        GridPane.setColumnSpan(transactionTableView, 2);
        GridPane.setHalignment(cancelButton, HPos.CENTER);
        gridPane.getColumnConstraints().add(new ColumnConstraints(300));
        gridPane.getColumnConstraints().add(new ColumnConstraints(300));

        GridPane.setConstraints(mainLabel, 0, 0);
        GridPane.setConstraints(nameLabel, 0, 1);
        GridPane.setConstraints(addressLabel, 0, 2);
        GridPane.setConstraints(addressText, 1, 2);
        GridPane.setConstraints(cashLabel, 0, 3);
        GridPane.setConstraints(cashText, 1, 3);
        GridPane.setConstraints(transactionsLabel, 0, 4);
        GridPane.setConstraints(transactionTableView, 0, 5);
        GridPane.setConstraints(cancelButton, 0, 6);

        gridPane.getChildren().addAll(mainLabel, nameLabel, addressLabel, addressText, cashLabel, cashText, transactionsLabel,
                transactionTableView, cancelButton);

        cancelButton.setOnAction(e -> {
            gridPane.getChildren().clear();
            downLabel.setText("What do you want to do now?");
        });

        return gridPane;
    }

    private ObservableList<Transaction> branchTransactionsList() {
        ObservableList<Transaction> trans = FXCollections.observableArrayList();
        program.findBranchByManager(activeManager).getTransactions().forEach(trans::addAll);
        return trans;
    }

    //    -------------------------------  ADD BRANCH TRANSACTION   --------------------------

    GridPane addBranchTransaction() {
        downLabel.setText("Add a branch transaction");
        GridPane gridPane = new GridPane();
        Label mainLabel = new Label("Add a branch transaction");

        Branch branch = program.findBranchByManager(activeManager);

        Label typeLabel = new Label("Transaction type");
        ChoiceBox<String> typeText = new ChoiceBox<>();
        typeText.getItems().addAll("Deposit", "Withdrawal");

        Label cashLabel = new Label("Cash in branch");
        TextField cashText = new TextField(String.valueOf(branch.getCashInBranch()));
        cashText.setEditable(false);

        Label amountLabel = new Label("Amount");
        TextField amountText = new TextField();

        Label dateLabel = new Label("Date");
        DatePicker dateText = new DatePicker();

        Button confirmButton = new Button("Confirm");
        Button cancelButton = new Button("Cancel");

        GridPane.setHalignment(mainLabel, HPos.CENTER);
        GridPane.setValignment(mainLabel, VPos.CENTER);
        mainLabel.setFont(Font.font(20));
        GridPane.setColumnSpan(mainLabel, 2);
        GridPane.setHalignment(confirmButton, HPos.CENTER);
        GridPane.setHalignment(cancelButton, HPos.CENTER);
        gridPane.getColumnConstraints().add(new ColumnConstraints(300));
        gridPane.getColumnConstraints().add(new ColumnConstraints(300));

        GridPane.setConstraints(mainLabel, 0, 0);
        GridPane.setConstraints(cashLabel, 0, 1);
        GridPane.setConstraints(cashText, 1, 1);
        GridPane.setConstraints(typeLabel, 0, 2);
        GridPane.setConstraints(typeText, 1, 2);
        GridPane.setConstraints(amountLabel, 0, 3);
        GridPane.setConstraints(amountText, 1, 3);
        GridPane.setConstraints(dateLabel, 0, 4);
        GridPane.setConstraints(dateText, 1, 4);
        GridPane.setConstraints(cancelButton, 0, 5);
        GridPane.setConstraints(confirmButton, 1, 5);

        gridPane.getChildren().addAll(mainLabel, cashLabel, cashText, typeLabel, typeText, amountLabel, amountText,
                dateLabel, dateText, confirmButton, cancelButton);

        confirmButton.setOnAction(e -> {
            String message;
            if (typeText.getValue() == null) {
                message = "Please choose the transaction type";
            } else {
                double amount = -1;
                try {
                    amount = Double.parseDouble(amountText.getText());
                } catch (NumberFormatException e1) {
                    System.out.println(e1.getMessage());
                }
                LocalDate date = null;
                try {
                    date = dateText.getValue();
                } catch (DateTimeException e1) {
                    System.out.println(e1.getMessage());
                }
                if ((amount > 0) && (date != null)) {
                    if (typeText.getValue().equals("Withdrawal")) {
                        if (amount > branch.getCashInBranch()) {
                            message = "Not enough cash in the branch";
                        } else {
                            Transaction transaction = new Transaction(branch.getName(), branch.getName(), "withdrawal",
                                    amount, date, activeManager.getId());
                            branch.getTransactions().add(transaction);
                            branch.setCashInBranch(branch.getCashInBranch() - amount);
                            message = typeText.getValue() + " successful";
                        }
                    } else {
                        Transaction transaction = new Transaction(branch.getName(), branch.getName(), "deposit",
                                amount, date, activeManager.getId());
                        branch.getTransactions().add(transaction);
                        branch.setCashInBranch(branch.getCashInBranch() + amount);
                        message = typeText.getValue() + " successful";
                    }
                    gridPane.getChildren().clear();
                } else {
                    message = "Invalid date or amount";
                    System.out.println("Invalid date or amount");
                }
                downLabel.setText(message);
                System.out.println(message);
            }
        });

        cancelButton.setOnAction(e -> {
            gridPane.getChildren().clear();
            downLabel.setText("What do you want to do now?");
        });

        return gridPane;
    }

    //    -------------------------------  VIEW CLIENTS IN THE BRANCH   --------------------------

    GridPane viewClients() {
        downLabel.setText("List of clients");
        GridPane gridPane = new GridPane();
        Label mainLabel = new Label("List of clients");

        Label clientLabel = new Label("Clients in " + program.findBranchByManager(activeManager).getName());

        TableView<Client> clientTableView = program.findBranchByManager(activeManager).clientTableView();

        Button cancelButton = new Button("Cancel");

        GridPane.setHalignment(mainLabel, HPos.CENTER);
        GridPane.setValignment(mainLabel, VPos.CENTER);
        mainLabel.setFont(Font.font(20));
        GridPane.setHalignment(cancelButton, HPos.CENTER);
        gridPane.getColumnConstraints().add(new ColumnConstraints(600));

        GridPane.setConstraints(mainLabel, 0, 0);
        GridPane.setConstraints(clientLabel, 0, 1);
        GridPane.setConstraints(clientTableView, 0, 2);
        GridPane.setConstraints(cancelButton, 0, 3);

        gridPane.getChildren().addAll(mainLabel, clientLabel, clientTableView, cancelButton);

        cancelButton.setOnAction(e -> {
            gridPane.getChildren().clear();
            downLabel.setText("What do you want to do now?");
        });

        return gridPane;
    }

    //    -------------------------------  VIEW EMPLOYEES IN THE BRANCH   --------------------------

    GridPane viewEmployees() {
        downLabel.setText("List of employees");
        GridPane gridPane = new GridPane();
        Label mainLabel = new Label("List of employees");

        Label employeeLabel = new Label("Employees in " + program.findBranchByManager(activeManager).getName());

        TableView<Employee> employeeTableView = program.employeeTableView(program.findBranchByManager(activeManager).employeesInBranchList());
        employeeTableView.getColumns().remove(2);
        employeeTableView.getColumns().remove(2);

        Button cancelButton = new Button("Cancel");

        GridPane.setHalignment(mainLabel, HPos.CENTER);
        GridPane.setValignment(mainLabel, VPos.CENTER);
        mainLabel.setFont(Font.font(20));
        GridPane.setHalignment(cancelButton, HPos.CENTER);
        gridPane.getColumnConstraints().add(new ColumnConstraints(600));

        GridPane.setConstraints(mainLabel, 0, 0);
        GridPane.setConstraints(employeeLabel, 0, 1);
        GridPane.setConstraints(employeeTableView, 0, 2);
        GridPane.setConstraints(cancelButton, 0, 3);

        gridPane.getChildren().addAll(mainLabel, employeeLabel, employeeTableView, cancelButton);

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

        Button confirmButton = new Button("Confirm");
        Button cancelButton = new Button("Cancel");
        Button exportButton = new Button("Export to CSV");

        GridPane.setHalignment(mainLabel, HPos.CENTER);
        GridPane.setValignment(mainLabel, VPos.CENTER);
        mainLabel.setFont(Font.font(20));
        GridPane.setColumnSpan(mainLabel, 2);
        GridPane.setHalignment(confirmButton, HPos.CENTER);
        GridPane.setHalignment(exportButton, HPos.CENTER);
        GridPane.setHalignment(cancelButton, HPos.CENTER);
        gridPane.getColumnConstraints().add(new ColumnConstraints(300));
        gridPane.getColumnConstraints().add(new ColumnConstraints(300));

        GridPane.setConstraints(mainLabel, 0, 0);
        GridPane.setConstraints(dateFromLabel, 0, 1);
        GridPane.setConstraints(dateFromText, 1, 1);
        GridPane.setConstraints(dateToLabel, 0, 2);
        GridPane.setConstraints(dateToText, 1, 2);
        GridPane.setConstraints(cancelButton, 0, 3);
        GridPane.setConstraints(confirmButton, 1, 3);
        GridPane.setConstraints(exportButton, 1, 5);

        gridPane.getChildren().addAll(mainLabel, dateFromLabel, dateFromText, dateToLabel, dateToText, confirmButton, cancelButton);

        confirmButton.setOnAction(e -> {
            String message;
            if ((dateFromText.getValue() != null) && (dateToText.getValue() != null) &&
                    dateFromText.getValue().isBefore(dateToText.getValue().plusDays(1))) {
                TableView<Transaction> reportTableView = new TableView<>();
                gridPane.getChildren().remove(confirmButton);

                Report report = new Report(dateFromText.getValue(), dateToText.getValue(), program.findBranchByManager(activeManager));
                reportTableView = report.transactionTableView(report.transactionListBranch());
                GridPane.setColumnSpan(reportTableView, 2);
                GridPane.setConstraints(reportTableView, 0, 4);
                gridPane.getChildren().addAll(reportTableView, exportButton);

                exportButton.setOnAction(e1 -> program.exportTransactions(activeManager, report.transactionListBranch()));

                message = "Will generate a branch report between " + dateFromText.getValue() + " and " + dateToText.getValue();

            } else {
                message = "Please choose the correct dates";
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

}
