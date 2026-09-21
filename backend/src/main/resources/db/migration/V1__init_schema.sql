-- =======================================================
-- Mawa3id.ma Initial Schema Migration
-- =======================================================

-- 1. USERS
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    public_id UUID NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20),
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    business_id BIGINT,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    email_verified BOOLEAN NOT NULL DEFAULT FALSE,
    email_verification_token VARCHAR(255),
    password_reset_token VARCHAR(255),
    password_reset_expires_at TIMESTAMP,
    avatar_url VARCHAR(500),
    language VARCHAR(5) DEFAULT 'fr',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_phone ON users(phone);
CREATE INDEX idx_users_business_id ON users(business_id);
CREATE INDEX idx_users_public_id ON users(public_id);

-- 2. BUSINESSES
CREATE TABLE businesses (
    id BIGSERIAL PRIMARY KEY,
    public_id UUID NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL,
    slug VARCHAR(100) NOT NULL UNIQUE,
    logo_url VARCHAR(500),
    cover_image_url VARCHAR(500),
    description TEXT,
    phone VARCHAR(20),
    whatsapp_number VARCHAR(20),
    email VARCHAR(255),
    address TEXT,
    city VARCHAR(100),
    region VARCHAR(100),
    google_maps_url VARCHAR(500),
    category VARCHAR(30),
    timezone VARCHAR(50) DEFAULT 'Africa/Casablanca',
    owner_id BIGINT NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX idx_businesses_slug ON businesses(slug);
CREATE INDEX idx_businesses_owner_id ON businesses(owner_id);
CREATE INDEX idx_businesses_public_id ON businesses(public_id);
CREATE INDEX idx_businesses_city ON businesses(city);
CREATE INDEX idx_businesses_category ON businesses(category);

-- 3. EMPLOYEES
CREATE TABLE employees (
    id BIGSERIAL PRIMARY KEY,
    public_id UUID NOT NULL UNIQUE,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    business_id BIGINT NOT NULL REFERENCES businesses(id) ON DELETE CASCADE,
    title VARCHAR(100),
    color VARCHAR(7) DEFAULT '#1B5E3C',
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX idx_employees_business_id ON employees(business_id);
CREATE INDEX idx_employees_user_id ON employees(user_id);
CREATE INDEX idx_employees_public_id ON employees(public_id);

-- 4. REFRESH TOKENS
CREATE TABLE refresh_tokens (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(500) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    expires_at TIMESTAMP NOT NULL,
    revoked BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_refresh_tokens_token ON refresh_tokens(token);
CREATE INDEX idx_refresh_tokens_user_id ON refresh_tokens(user_id);
CREATE INDEX idx_refresh_tokens_expires_at ON refresh_tokens(expires_at);

-- 5. AUDIT LOGS
CREATE TABLE audit_logs (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    business_id BIGINT,
    action VARCHAR(100) NOT NULL,
    entity_type VARCHAR(50),
    entity_id BIGINT,
    details JSONB,
    ip_address VARCHAR(45),
    user_agent TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_audit_logs_user_id ON audit_logs(user_id);
CREATE INDEX idx_audit_logs_business_id ON audit_logs(business_id);
CREATE INDEX idx_audit_logs_action ON audit_logs(action);
CREATE INDEX idx_audit_logs_created_at ON audit_logs(created_at);

-- 6. SERVICES
CREATE TABLE services (
    id BIGSERIAL PRIMARY KEY,
    public_id UUID NOT NULL UNIQUE,
    business_id BIGINT NOT NULL REFERENCES businesses(id) ON DELETE CASCADE,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    duration_minutes INTEGER NOT NULL,
    price_mad NUMERIC(10, 2) NOT NULL,
    color VARCHAR(7) DEFAULT '#1B5E3C',
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX idx_services_business_id ON services(business_id);
CREATE INDEX idx_services_public_id ON services(public_id);
CREATE INDEX idx_services_active ON services(active);

-- 7. SERVICE_EMPLOYEES (Join Table)
CREATE TABLE service_employees (
    service_id BIGINT NOT NULL REFERENCES services(id) ON DELETE CASCADE,
    employee_id BIGINT NOT NULL REFERENCES employees(id) ON DELETE CASCADE,
    PRIMARY KEY (service_id, employee_id)
);

-- 8. CUSTOMERS
CREATE TABLE customers (
    id BIGSERIAL PRIMARY KEY,
    public_id UUID NOT NULL UNIQUE,
    business_id BIGINT NOT NULL REFERENCES businesses(id) ON DELETE CASCADE,
    full_name VARCHAR(150) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    whatsapp_number VARCHAR(20),
    email VARCHAR(255),
    notes TEXT,
    total_appointments INTEGER NOT NULL DEFAULT 0,
    total_spent_mad NUMERIC(10, 2) NOT NULL DEFAULT 0.00,
    last_appointment_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT uk_customer_business_phone UNIQUE (business_id, phone)
);

CREATE INDEX idx_customers_business_id ON customers(business_id);
CREATE INDEX idx_customers_phone ON customers(phone);
CREATE INDEX idx_customers_public_id ON customers(public_id);

-- 9. APPOINTMENTS
CREATE TABLE appointments (
    id BIGSERIAL PRIMARY KEY,
    public_id UUID NOT NULL UNIQUE,
    business_id BIGINT NOT NULL REFERENCES businesses(id) ON DELETE CASCADE,
    customer_id BIGINT NOT NULL REFERENCES customers(id) ON DELETE RESTRICT,
    service_id BIGINT NOT NULL REFERENCES services(id) ON DELETE RESTRICT,
    employee_id BIGINT NOT NULL REFERENCES employees(id) ON DELETE RESTRICT,
    appointment_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    notes TEXT,
    cancellation_reason TEXT,
    cancelled_at TIMESTAMP,
    completed_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX idx_appointments_business_id ON appointments(business_id);
CREATE INDEX idx_appointments_customer_id ON appointments(customer_id);
CREATE INDEX idx_appointments_service_id ON appointments(service_id);
CREATE INDEX idx_appointments_employee_id ON appointments(employee_id);
CREATE INDEX idx_appointments_date ON appointments(appointment_date);
CREATE INDEX idx_appointments_status ON appointments(status);
CREATE INDEX idx_appointments_public_id ON appointments(public_id);

-- 10. WORKING HOURS
CREATE TABLE working_hours (
    id BIGSERIAL PRIMARY KEY,
    public_id UUID NOT NULL UNIQUE,
    business_id BIGINT NOT NULL REFERENCES businesses(id) ON DELETE CASCADE,
    employee_id BIGINT REFERENCES employees(id) ON DELETE CASCADE,
    day_of_week VARCHAR(15) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    break_start_time TIME,
    break_end_time TIME,
    is_day_off BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX idx_working_hours_business_id ON working_hours(business_id);
CREATE INDEX idx_working_hours_employee_id ON working_hours(employee_id);

-- 11. NOTIFICATIONS
CREATE TABLE notifications (
    id BIGSERIAL PRIMARY KEY,
    public_id UUID NOT NULL UNIQUE,
    business_id BIGINT NOT NULL REFERENCES businesses(id) ON DELETE CASCADE,
    appointment_id BIGINT REFERENCES appointments(id) ON DELETE SET NULL,
    customer_id BIGINT REFERENCES customers(id) ON DELETE SET NULL,
    recipient VARCHAR(255) NOT NULL,
    type VARCHAR(30) NOT NULL,
    channel VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    provider_message_id VARCHAR(100),
    message_content TEXT NOT NULL,
    retry_count INTEGER NOT NULL DEFAULT 0,
    error_message TEXT,
    sent_at TIMESTAMP,
    delivered_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX idx_notifications_business_id ON notifications(business_id);
CREATE INDEX idx_notifications_appointment_id ON notifications(appointment_id);
CREATE INDEX idx_notifications_status ON notifications(status);
CREATE INDEX idx_notifications_created_at ON notifications(created_at);

-- 12. NOTIFICATION TEMPLATES
CREATE TABLE notification_templates (
    id BIGSERIAL PRIMARY KEY,
    public_id UUID NOT NULL UNIQUE,
    business_id BIGINT NOT NULL REFERENCES businesses(id) ON DELETE CASCADE,
    type VARCHAR(30) NOT NULL,
    channel VARCHAR(20) NOT NULL,
    language VARCHAR(10) NOT NULL DEFAULT 'fr',
    subject VARCHAR(255),
    body TEXT NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT uk_template_business_type_channel_lang UNIQUE (business_id, type, channel, language)
);

CREATE INDEX idx_notif_templates_business_id ON notification_templates(business_id);

-- 13. NOTIFICATION CONFIGS
CREATE TABLE notification_configs (
    id BIGSERIAL PRIMARY KEY,
    public_id UUID NOT NULL UNIQUE,
    business_id BIGINT NOT NULL UNIQUE REFERENCES businesses(id) ON DELETE CASCADE,
    whatsapp_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    sms_enabled BOOLEAN NOT NULL DEFAULT FALSE,
    email_enabled BOOLEAN NOT NULL DEFAULT FALSE,
    reminder_24h_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    reminder_2h_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    custom_reminder_minutes INTEGER,
    default_language VARCHAR(10) DEFAULT 'fr',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

-- 14. SUBSCRIPTION PLANS
CREATE TABLE subscription_plans (
    id BIGSERIAL PRIMARY KEY,
    public_id UUID NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    slug VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    price_monthly_mad NUMERIC(10, 2) NOT NULL,
    max_appointments_per_month INTEGER NOT NULL,
    max_customers INTEGER NOT NULL,
    max_employees INTEGER NOT NULL,
    whatsapp_notifications_included BOOLEAN NOT NULL DEFAULT TRUE,
    sms_notifications_included BOOLEAN NOT NULL DEFAULT FALSE,
    features JSONB,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    display_order INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

-- 15. SUBSCRIPTIONS
CREATE TABLE subscriptions (
    id BIGSERIAL PRIMARY KEY,
    public_id UUID NOT NULL UNIQUE,
    business_id BIGINT NOT NULL UNIQUE REFERENCES businesses(id) ON DELETE CASCADE,
    plan_id BIGINT NOT NULL REFERENCES subscription_plans(id) ON DELETE RESTRICT,
    status VARCHAR(20) NOT NULL DEFAULT 'TRIAL',
    current_period_start TIMESTAMP NOT NULL,
    current_period_end TIMESTAMP NOT NULL,
    trial_ends_at TIMESTAMP,
    cancelled_at TIMESTAMP,
    cancellation_reason TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX idx_subscriptions_business_id ON subscriptions(business_id);
CREATE INDEX idx_subscriptions_plan_id ON subscriptions(plan_id);
CREATE INDEX idx_subscriptions_status ON subscriptions(status);

-- 16. PAYMENTS
CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,
    public_id UUID NOT NULL UNIQUE,
    business_id BIGINT NOT NULL REFERENCES businesses(id) ON DELETE CASCADE,
    subscription_id BIGINT REFERENCES subscriptions(id) ON DELETE SET NULL,
    amount_mad NUMERIC(10, 2) NOT NULL,
    currency VARCHAR(5) NOT NULL DEFAULT 'MAD',
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    provider_name VARCHAR(50),
    provider_transaction_id VARCHAR(150),
    payment_method VARCHAR(50),
    paid_at TIMESTAMP,
    error_message TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX idx_payments_business_id ON payments(business_id);
CREATE INDEX idx_payments_subscription_id ON payments(subscription_id);
CREATE INDEX idx_payments_status ON payments(status);

-- =======================================================
-- SEED INITIAL DATA
-- =======================================================

-- Seed Moroccan Subscription Plans
INSERT INTO subscription_plans (public_id, name, slug, description, price_monthly_mad, max_appointments_per_month, max_customers, max_employees, whatsapp_notifications_included, sms_notifications_included, features, active, display_order, created_at)
VALUES 
(gen_random_uuid(), 'Gratuit (Free)', 'free', 'Idéal pour démarrer votre activité sans frais', 0.00, 30, 50, 1, false, false, '["30 rendez-vous / mois", "50 clients max", "1 collaborateur", "Page de réservation publique", "Tableau de bord de base"]', true, 1, CURRENT_TIMESTAMP),
(gen_random_uuid(), 'Starter', 'starter', 'Parfait pour les professionnels indépendants et petits salons', 79.00, 300, -1, 2, true, true, '["300 rendez-vous / mois", "Clients illimités", "2 collaborateurs", "Rappels WhatsApp automatiques", "Rappels SMS", "Statistiques de base"]', true, 2, CURRENT_TIMESTAMP),
(gen_random_uuid(), 'Business', 'business', 'Pour les salons, cliniques et cabinets en pleine croissance', 149.00, -1, -1, 10, true, true, '["Rendez-vous illimités", "Clients illimités", "Jusqu''à 10 collaborateurs", "WhatsApp & SMS automatiques", "Personnalisation des modèles", "Statistiques avancées", "Support prioritaire"]', true, 3, CURRENT_TIMESTAMP),
(gen_random_uuid(), 'Pro', 'pro', 'La solution complète pour les franchises et centres multi-services', 299.00, -1, -1, -1, true, true, '["Tout en illimité", "Collaborateurs illimités", "Multi-succursales", "Automatisation WhatsApp poussée", "Accès API & intégrations", "Gestionnaire de compte dédié", "Support 24/7"]', true, 4, CURRENT_TIMESTAMP);
