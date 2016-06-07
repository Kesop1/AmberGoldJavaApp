package sample;

import java.time.LocalDate;

import static sample.Main.program;


public class Transaction {
    private String accountNumberSender;
    private String type;
    private double amount;
    private LocalDate date;
    private String accountNumberReceiver;
    private String employeeId;
    private int transactionId;

    Transaction(String accountNumberSender, String accountNumberReceiver, String type, double amount, LocalDate date, String employeeId) {
        this.accountNumberSender = accountNumberSender;
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.accountNumberReceiver = accountNumberReceiver;
        this.employeeId = employeeId;
        int random = (int) (Math.random() * 1000000);
        while (program.findTransactionById(random) != null)
            random = (int) (Math.random() * 1000000);
        this.transactionId = random;
    }

    public String getAccountNumberSender() {
        return accountNumberSender;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getAccountNumberReceiver() {
        return accountNumberReceiver;
    }

    public int getTransactionId() {
        return transactionId;
    }

    void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    String saveString() {
        return (this.getAccountNumberSender() + "," + this.getAccountNumberReceiver() + "," + this.getType()
                + "," + this.getAmount() + "," + this.getDate() + "," + this.getEmployeeId() + "," + this.getTransactionId());
    }
}
