package sample;

import java.text.DecimalFormat;

import static sample.Main.program;

public abstract class User {
    private String name;
    private String id;
    private String userType;
    private String password;
    private int attempts;
    private boolean changePassword;
    private String branchName;

    public User(String name, String userType) {
        this.name = name;
        int randomNumber = (int) (Math.random() * 1000);
        DecimalFormat decimalFormat = new DecimalFormat("000");
        String random = userType + String.valueOf(name.charAt(0)).toUpperCase() + decimalFormat.format(randomNumber);
        while (program.findUserById(random) != null)
            random = userType + String.valueOf(name.charAt(0)).toUpperCase() + decimalFormat.format(randomNumber);
        this.id = random;
        this.userType = userType;
        this.password = "new";
        this.attempts = 0;
        this.changePassword = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    void setId(String id) {
        char[] idArray = id.toCharArray();
        if ((id.length() == 5) && ((idArray[0] == 'M') || (idArray[0] == 'A') || (idArray[0] == 'E'))
                && (idArray[1] == this.getName().charAt(0))) {
            this.id = id;
        } else {
            System.out.println("ID must be 5 symbols long, contain a symbol for the user type " +
                    "and first letter of the user's name");
        }
    }

    public String getBranchName() {
        return branchName;
    }

    void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public boolean isChangePassword() {
        return changePassword;
    }

    void setChangePassword(boolean changePassword) {
        this.changePassword = changePassword;
    }

    public String getUserType() {
        return userType;
    }

    String getPassword() {
        return password;
    }

    void setPassword(String password) {
        this.password = password;
    }

    public int getAttempts() {
        return attempts;
    }

    void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    String saveString() {
        return (this.getUserType() + "," + this.getName() + "," + this.getId() + "," +
                this.getPassword() + "," + this.getAttempts() + "," + this.isChangePassword());
    }

    @Override
    public String toString() {
        return ("Name: " + this.name + ", ID: " + this.id + ", user type: " + this.userType);
    }
}
