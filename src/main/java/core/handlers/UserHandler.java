

package core.handlers;

import java.util.*;

import core.Configuration;

public class UserHandler implements Configuration {

    private HashMap<Integer, UserRole> roles = new HashMap<Integer, UserRole>();
    private String environmentalHash;
    private String emailDomain = "@test.com";
    private String userName;
    private String rawUserName;
    private String firstName;
    private String lastName;
    private String password;
    private String description;
    private String telephoneNumber;
    private String userID;

    public UserHandler(String name, String defaultEmailDomain, String environmentalHash) throws Exception {
        setEnvironmentalHash(environmentalHash);
        setUserName(name);
        setEmailDomain(defaultEmailDomain);
    }

    public UserHandler(String name, String environmentalHash) throws Exception {
        setEnvironmentalHash(environmentalHash);
        setUserName(name);
    }

    /**
     * Sets the default e-mail domain to use when creating an user email address without specifying a domain
     * This will prepend the domain with @ if not supplied
     *
     * @param value
     * @throws Exception
     */
    private void setEmailDomain(String value) throws Exception {
        if (!value.startsWith("@")) {
            this.emailDomain = "@" + value;
        } else {
            this.emailDomain = value;
        }
    }

    /**
     * Return the default e-mail domain (always starts with @
     *
     * @return
     * @throws Exception
     */
    public String getEmailDomain() throws Exception {
        return this.emailDomain;
    }

    /**
     * Return the email address associated with this user object
     *
     * @return Full e-mail address
     * @throws Exception
     */
    public String getEmail() {
        return this.userName + this.emailDomain;
    }

    /**
     * Return the raw email address
     * (The email address without an environmental variable applied)
     *
     * @return Raw e-mail address
     * @throws Exception
     */
    public String getRawEmail() throws Exception {
        return this.rawUserName + this.emailDomain;
    }

    /**
     * Sets the UserHandler Username (Automatically applies an env var to support paralell runs)
     *
     * @param username
     */
    public final void setUserName(String username) {
        this.rawUserName = username;
        this.userName = username + '_' + this.environmentalHash;
    }

    /**
     * Gets the users current Username
     *
     * @return get the username associated with this user
     * @throws Exception
     */
    public String getUserName() throws Exception {
        return this.userName;
    }

    /**
     * Gets the raw username associated with user
     * (the username without and environmental variable applied)
     *
     * @return
     * @throws Exception
     */
    public String getRawUsername() throws Exception {
        return this.rawUserName;
    }

    /**
     * Sets the UserHandler FirstName
     *
     * @param firstname
     * @throws Exception
     */
    public void setFirstName(String firstname) throws Exception {
        this.firstName = firstname;
    }

    /**
     * gets the UserHandler pFirstName
     *
     * @return the FirstName for the Current UserHandler
     * @throws Exception
     */
    public String getFirstName() throws Exception {
        return this.firstName;
    }

    /**
     * Sets the UserHandler LastName
     *
     * @param lastname
     * @throws Exception
     */
    public void setLastName(String lastname) throws Exception {
        this.lastName = lastname;
    }

    /**
     * Gets the UserHandler pLastName
     *
     * @return the LastName for the Current UserHandler
     * @throws Exception
     */
    public String getLastName() throws Exception {
        return this.lastName;
    }

    /**
     * Sets the UserHandler Password
     *
     * @param value
     * @throws Exception
     */
    public void setPassword(String value) throws Exception {
        this.password = value;
    }

    /**
     * Gets the UserHandler pPassword
     *
     * @return the Password for the Current UserHandler
     * @throws Exception
     */
    public String getPassword() throws Exception {
        return this.password;
    }

    /**
     * Sets the UserHandler Description
     *
     * @param value
     * @throws Exception
     */
    public void setDescription(String value) throws Exception {
        this.description = value;
    }

    /**
     * Gets the UserHandler Description
     *
     * @return Description of user
     * @throws Exception
     */
    public String getDescription() throws Exception {
        return this.description;
    }

    /**
     * Sets the UserHandler TelephoneNumber
     *
     * @param value
     * @throws Exception
     */
    public void setTelephoneNumber(String value) throws Exception {
        this.telephoneNumber = value;
    }

    /**
     * Gets the UserHandler TelephoneNumber
     *
     * @return Telephone number in String format
     * @throws Exception
     */
    public String getTelephoneNumber() throws Exception {
        return this.telephoneNumber;
    }

    /**
     * Sets the UserHandler UserID
     * (This is assuming that your website has a unique user ID you may want to store.)
     * (Sometimes stored as int and sometimes as guid hence type String.)
     *
     * @param uid
     * @throws Exception
     */
    public void setUserID(String uid) throws Exception {
        this.userID = uid;
    }

    /**
     * Gets the UserHandler UserID
     *
     * @return User ID
     * @throws Exception
     */
    public String getUserID() throws Exception {
        return this.userID;
    }

    /**
     * Returns an ArrayList full of type <UserRole> roles
     *
     * @return
     * @throws Exception
     */
    public HashMap<Integer, UserRole> getRoles() throws Exception {
        return this.roles;
    }

    /**
     * Add a role
     *
     * @param role
     * @throws Exception
     */
    public void addRole(UserRole role) throws Exception {

        this.roles.put(role.ordinal(), role);
    }

    /**
     * Remove a role
     *
     * @param role
     * @throws Exception
     */
    public void removeRole(UserRole role) throws Exception {
        this.roles.remove(role);
    }

    /**
     * Clear out roles
     *
     * @throws Exception
     */
    public void removeAllRoles() throws Exception {
        this.roles.clear();
    }

    /**
     * Find out if a user has the specified role
     *
     * @param role
     * @return boolean
     * @throws Exception
     */
    public boolean doesUserHaveRole(UserRole role) throws Exception {
        return this.roles.containsKey(role.ordinal());
    }

    /**
     * Find out if a user has the specified role
     * (This function is case insensitive)
     *
     * @param role
     * @return boolean
     * @throws Exception
     */
    public boolean doesUserHaveRole(String role) throws Exception {
        for (UserRole roleType : UserRole.values()) {
            if (roleType.toString().toLowerCase().equals(role.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private void setEnvironmentalHash(String hash) {
        this.environmentalHash = hash;
    }

    public String getEnvironmentalHash() {
        return this.environmentalHash;
    }
}
