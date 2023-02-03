package models;

public class Users {
    private final String userName, userPassword;
    private int userID;

    /**
     * constructor for Users
     * @param userID, userName, userPassword
     * */
    public Users(int userID, String userName, String userPassword) {
        this.userID = userID;
        this.userName = userName;
        this.userPassword = userPassword;
    }

    /**
     * @param userID, userName, userPassword
     * @return Users object
     * */
    public static Users create(int userID, String userName, String userPassword) {
        if (userID <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        if (userName == null || userName.isEmpty()) {
            throw new IllegalArgumentException("Invalid user name");
        }
        if (userPassword == null || userPassword.isEmpty()) {
            throw new IllegalArgumentException("Invalid user password");
        }
        return new Users(userID, userName, userPassword);
    }

    /**
     * @return userName
     * */
    public String getUserName() {
        return userName;
    }

    /**
     * @return userPassword
     * */
    public String getUserPassword() {
        return userPassword;
    }

    /**
     * @return userID
     * */
    public int getUserID() {
        return userID;
    }
}
