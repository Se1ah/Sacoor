package com.zhyar;


public final class UserSession {

    private static UserSession instance;
    private static Integer userID;
    private static String userName;
    private static String privileges;

    private UserSession(Integer userID, String userName, String privileges) {
        this.userID = userID;
        this.userName = userName;
        this.privileges = privileges;
    }

    public static UserSession getInstace(Integer userID, String userName, String privileges) {
        if(instance == null) {
            instance = new UserSession(userID, userName, privileges);
        }
        return instance;
    }

    public static Integer getUserID() { return userID;}

    public static String getUserName() {
        return userName;
    }

    public static String getPrivileges() {
        return privileges;
    }

    public void cleanUserSession() {
        userID = null;
        userName = "";// or null
        privileges = null;// or null
    }

    @Override
    public String toString() {
        return "UserSession{" +
                "ID='" + userID +'\''+
                "userName='" + userName + '\'' +
                ", privileges=" + privileges +
                '}';
    }
}