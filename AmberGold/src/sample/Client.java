package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.ArrayList;

import static sample.Main.program;

public class Client {
    private String firstName;
    private String lastName;
    private int socialSecurityNumber;
    private String address;
    private LocalDate dateOfBirth;
    private double balance;
    private ArrayList<Transaction> transactions;
    private String accountNumber;

    public Client(String firstName, String lastName, int socialSecurityNumber, String address, LocalDate dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.socialSecurityNumber = socialSecurityNumber;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.balance = 0;
        this.transactions = new ArrayList<>();
        String random = randomString(10);
        while (program.findClientByAccount(random) != null)
            random = randomString(10);
        this.accountNumber = randomString(10);
    }

    String transaction(Transaction transaction) {
        String accountNumberSender = transaction.getAccountNumberSender();
        String accountNumberReceiver = transaction.getAccountNumberReceiver();
        String type = transaction.getType();
        Double amount = transaction.getAmount();
        switch (type) {
            case "withdrawal":
                if (balance >= amount) {
                    transactions.add(transaction);
                    balance -= amount;
                } else return "Not enough funds. Current balance is " + balance;
                break;
            case "deposit":
                transactions.add(transaction);
                balance += amount;
                break;
            case "transfer":
                if (accountNumberReceiver.equals(accountNumberSender))
                    return "Cannot transfer funds from and to the same client";
                else {
                    if (this.accountNumber.equals(accountNumberSender)) {
                        if (balance >= amount) {
                            transactions.add(transaction);
                            balance -= amount;
                        } else return "Not enough funds. Current balance is " + balance;
                    } else if (this.accountNumber.equals(accountNumberReceiver)) {
                        transactions.add(transaction);
                        balance += amount;
                    }
                }
                break;
            case "payment":
                if (balance >= amount) {
                    transactions.add(transaction);
                    balance -= amount;
                } else return "Not enough funds. Current balance is " + balance;
                break;
            case "online":
                if (balance >= amount) {
                    transactions.add(transaction);
                    balance -= amount;
                } else return "Not enough funds. Current balance is " + balance;
                break;
            default:
                return "Not a valid transaction type";
        }
        return type + " transaction successful for " + this.getLastName()
                + ". currently the balance is " + balance;
    }

    public String getFirstName() {
        return firstName;
    }

    void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    void setAddress(String address) {
        this.address = address;
    }

    public double getBalance() {
        return balance;
    }

    void setBalance(double balance) {
        this.balance = balance;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    private ObservableList<Transaction> clientTransactionsList() {
        ObservableList<Transaction> transactionsList = FXCollections.observableArrayList();
        transactionsList.addAll(this.getTransactions());
        return transactionsList;
    }

    TableView<Transaction> transactionTableView() {
        TableView<Transaction> transactionTableView = new TableView<>();

        TableColumn<Transaction, String> accountSenderColumn = new TableColumn<>("Sender");
        accountSenderColumn.setMinWidth(75);
        accountSenderColumn.setCellValueFactory(new PropertyValueFactory<>("accountNumberSender"));

        TableColumn<Transaction, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setMinWidth(100);
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Transaction, Double> amountColumn = new TableColumn<>("Amount");
        amountColumn.setMinWidth(75);
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<Transaction, LocalDate> dateColumn = new TableColumn<>("Date");
        dateColumn.setMinWidth(100);
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Transaction, String> accountReceiverColumn = new TableColumn<>("Receiver");
        accountReceiverColumn.setMinWidth(100);
        accountReceiverColumn.setCellValueFactory(new PropertyValueFactory<>("accountNumberReceiver"));

        TableColumn<Transaction, Integer> transactionIdColumn = new TableColumn<>("Transaction ID");
        transactionIdColumn.setMinWidth(100);
        transactionIdColumn.setCellValueFactory(new PropertyValueFactory<>("transactionId"));

        transactionTableView.setItems(clientTransactionsList());
        //noinspection unchecked
        transactionTableView.getColumns().addAll(accountSenderColumn, typeColumn, amountColumn, dateColumn, accountReceiverColumn, transactionIdColumn);
        return transactionTableView;
    }

    private static final String AB = "0123456789";
    private static SecureRandom rnd = new SecureRandom();

    private String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    String saveString() {
        return (this.getFirstName() + "," + this.getLastName() + "," + this.getSocialSecurityNumber()
                + "," + this.getAddress() + "," + this.getDateOfBirth()
                + "," + this.getBalance() + "," + this.getAccountNumber());
    }
}

