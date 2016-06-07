package sample;

import java.util.ArrayList;

public class Role {
    private String name;
    private ArrayList<Double> limits = new ArrayList<>(5);
    private double depositLimit;
    private double withdrawalLimit;
    private double transferLimit;
    private double paymentLimit;
    private double onlineLimit;

            /*1 - deposit,
            2 - withdrawal,
            3 - transfer,
            4 - payment,
            5 - online*/

    public Role(String name) {
        this.limits = new ArrayList<>();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    ArrayList<Double> getLimits() {
        return limits;
    }

    void setLimits(ArrayList<Double> limits) {
        this.limits = limits;
        depositLimit = limits.get(0);
        withdrawalLimit = limits.get(1);
        transferLimit = limits.get(2);
        paymentLimit = limits.get(3);
        onlineLimit = limits.get(4);
    }

    String saveString() {
        return (getName() + "," + getDepositLimit() + "," + getWithdrawalLimit() + "," + getTransferLimit() + "," +
                getPaymentLimit() + "," + getOnlineLimit());
    }

    @Override
    public String toString() {
        return name;
    }


    public double getDepositLimit() {
        return depositLimit;
    }

    public double getWithdrawalLimit() {
        return withdrawalLimit;
    }

    public double getTransferLimit() {
        return transferLimit;
    }

    public double getPaymentLimit() {
        return paymentLimit;
    }

    public double getOnlineLimit() {
        return onlineLimit;
    }
}


