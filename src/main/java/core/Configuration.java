

package core;

public interface Configuration {

    /**
     * List of supported operating systems that the Ebselen framework will support
     */
    public static enum osList {

        WINDOWS, LINUX, OSX
    }

    ;

    /**
     * List of websites that the framework is aware of
     * This should be modified to match the list of sites you are going to test
     * Each entry in this enum needs a corresponding entry in default.properties (in all lowercase)
     */
    public enum selectSite {

        SAMPLETEST
    }

    /**
     * Set the roles that can be applied to users in the website you are testing
     * This is used by the UserHandler class when applying roles to a UserHandler object
     */
    public static enum UserRole {

        ADMINISTRATOR, STANDARD_USER
    }

    ;
}
