package hanabi.model;

import java.util.UUID;
import org.bson.codecs.pojo.annotations.BsonId;

public class Tenant {
    @BsonId
    private UUID tenantId;
    private String tenantName;
    private String password;
    private String cafeName;
    private String fullName;
    private String email;
    private Boolean status;

    public Tenant() {}

    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    public String getTenantName() { return tenantName; }
    public void setTenantName(String tenantName) { this.tenantName = tenantName; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getCafeName() { return cafeName; }
    public void setCafeName(String cafeName) { this.cafeName = cafeName; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }
}
