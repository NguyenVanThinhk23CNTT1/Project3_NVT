package comk23cnt1.nvt.project3.nvt_enum;

/**
 * Enum định nghĩa các role trong hệ thống
 */
public enum Role {
    ADMIN,
    USER;

    /**
     * Lấy role name với prefix "ROLE_" cho Spring Security
     * 
     * @return Role name với prefix (VD: "ROLE_ADMIN")
     */
    public String getRoleWithPrefix() {
        return "ROLE_" + this.name();
    }

    /**
     * Lấy role name không có prefix
     * 
     * @return Role name (VD: "ADMIN")
     */
    public String getRoleName() {
        return this.name();
    }
}
