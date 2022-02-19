package com.mss.chatbd.Model;

public class User {

    String UserId,UserEmail,UserFirstName,UserLastName,UserProfilePic;

    public User(String userId, String userEmail, String userFirstName, String userLastName, String userProfilePic) {
        UserId = userId;
        UserEmail = userEmail;
        UserFirstName = userFirstName;
        UserLastName = userLastName;
        UserProfilePic = userProfilePic;
    }

    public User() {
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public String getUserFirstName() {
        return UserFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        UserFirstName = userFirstName;
    }

    public String getUserLastName() {
        return UserLastName;
    }

    public void setUserLastName(String userLastName) {
        UserLastName = userLastName;
    }

    public String getUserProfilePic() {
        return UserProfilePic;
    }

    public void setUserProfilePic(String userProfilePic) {
        UserProfilePic = userProfilePic;
    }
}
