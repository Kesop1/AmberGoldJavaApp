package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.ArrayList;

class Report {
    private ArrayList<Client> clients;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private Branch branch;

    Report(ArrayList<Client> clients, LocalDate dateFrom, LocalDate dateTo) {
        this.clients = clients;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    Report(LocalDate dateFrom, LocalDate dateTo, Branch branch) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.branch = branch;
    }

    ObservableList<Transaction> transactionListClient() {
        ObservableList<Transaction> transactions = FXCollections.observableArrayList();
        ArrayList<Integer> transId = new ArrayList<>();
        for (Client client : this.clients) {
            for (Transaction transaction : client.getTransactions()) {
                if ((transaction.getDate().isAfter(this.dateFrom.minusDays(1))) && (transaction.getDate().isBefore(this.dateTo.plusDays(1))))
                    if (!transId.contains(transaction.getTransactionId())) {
                        transactions.add(transaction);
                        transId.add(transaction.getTransactionId());
                    }
            }
        }
        return transactions;
    }

    ObservableList<Transaction> transactionListBranch() {
        ObservableList<Transaction> transactions = FXCollections.observableArrayList();
        ArrayList<Integer> transId = new ArrayList<>();
        for (Transaction transaction : this.branch.getTransactions()) {
            if ((transaction.getDate().isAfter(this.dateFrom.minusDays(1))) && (transaction.getDate().isBefore(this.dateTo.plusDays(1))))
                if (!transId.contains(transaction.getTransactionId())) {
                    transactions.add(transaction);
                    transId.add(transaction.getTransactionId());
                }
        }
        return transactions;
    }


    TableView<Transaction> transactionTableView(ObservableList<Transaction> transactionObservableList) {
        TableView<Transaction> transactionTableView = new TableView<>();

        TableColumn<Transaction, String> accNumSenderColumn = new TableColumn<>("Sender");
        accNumSenderColumn.setMinWidth(50);
        accNumSenderColumn.setCellValueFactory(new PropertyValueFactory<>("accountNumberSender"));

        TableColumn<Transaction, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setMinWidth(50);
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Transaction, Double> amountColumn = new TableColumn<>("Amount");
        amountColumn.setMinWidth(50);
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<Transaction, LocalDate> dateColumn = new TableColumn<>("Date");
        dateColumn.setMinWidth(50);
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Transaction, String> accNumReceiverColumn = new TableColumn<>("Receiver");
        accNumReceiverColumn.setMinWidth(50);
        accNumReceiverColumn.setCellValueFactory(new PropertyValueFactory<>("accountNumberReceiver"));

        TableColumn<Transaction, String> employeeColumn = new TableColumn<>("Employee");
        employeeColumn.setMinWidth(50);
        employeeColumn.setCellValueFactory(new PropertyValueFactory<>("employeeId"));

        TableColumn<Transaction, String> transIdColumn = new TableColumn<>("Transaction ID");
        transIdColumn.setMinWidth(50);
        transIdColumn.setCellValueFactory(new PropertyValueFactory<>("transactionId"));

        transactionTableView.setItems(transactionObservableList);
        //noinspection unchecked
        transactionTableView.getColumns().addAll(accNumSenderColumn, typeColumn, amountColumn, dateColumn, accNumReceiverColumn,
                employeeColumn, transIdColumn);
        return transactionTableView;
    }
}
