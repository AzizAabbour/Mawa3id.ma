package ma.mawa3id.tenant;

/**
 * ThreadLocal storage for the current business (tenant) context.
 * Enables automatic multi-tenant data isolation.
 */
public final class TenantContext {

    private static final ThreadLocal<Long> CURRENT_BUSINESS_ID = new ThreadLocal<>();

    private TenantContext() {
    }

    public static void setCurrentBusinessId(Long businessId) {
        CURRENT_BUSINESS_ID.set(businessId);
    }

    public static Long getCurrentBusinessId() {
        return CURRENT_BUSINESS_ID.get();
    }

    public static void clear() {
        CURRENT_BUSINESS_ID.remove();
    }
}
