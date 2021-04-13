package pl.kzcwat.localincidentserver.security;

public enum ApplicationUserPermission {
    //When adding security please add here users permissions
    ;

    private final String permission;

    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
