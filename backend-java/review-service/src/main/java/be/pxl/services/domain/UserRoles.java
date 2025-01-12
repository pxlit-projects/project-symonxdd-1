package be.pxl.services.domain;

public enum UserRoles {
    EDITOR("Editor"),
    USER1("Moka"),
    USER2("Andrew Tate");

    private final String displayName;

    UserRoles(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
