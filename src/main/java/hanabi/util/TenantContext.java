package hanabi.util;

import java.util.UUID;

public class TenantContext {
    private static final InheritableThreadLocal<UUID> currentTenantId = new InheritableThreadLocal<>();

    public static void setCurrentTenantId(UUID tenantId) {
        currentTenantId.set(tenantId);
    }

    public static UUID getCurrentTenantId() {
        return currentTenantId.get();
    }

    public static void clear() {
        currentTenantId.remove();
    }
}
