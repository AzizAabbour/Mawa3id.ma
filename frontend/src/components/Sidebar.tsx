import React from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { useLanguage } from '../contexts/LanguageContext';
import { 
  LayoutDashboard, 
  Calendar, 
  Users, 
  Scissors, 
  UserCheck, 
  Bell, 
  Clock, 
  CreditCard, 
  Settings, 
  LogOut, 
  ExternalLink,
  ShieldAlert
} from 'lucide-react';

export const Sidebar: React.FC = () => {
  const { user, logout } = useAuth();
  const { isRTL } = useLanguage();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const navItems = [
    { to: '/dashboard', icon: <LayoutDashboard size={19} />, label: isRTL ? 'لوحة التحكم' : 'Tableau de bord' },
    { to: '/appointments', icon: <Calendar size={19} />, label: isRTL ? 'المواعيد' : 'Rendez-vous' },
    { to: '/customers', icon: <Users size={19} />, label: isRTL ? 'الزبناء' : 'Clients' },
    { to: '/services', icon: <Scissors size={19} />, label: isRTL ? 'الخدمات & الأسعار' : 'Prestations' },
    { to: '/employees', icon: <UserCheck size={19} />, label: isRTL ? 'فريق العمل' : 'Collaborateurs' },
    { to: '/notifications', icon: <Bell size={19} />, label: isRTL ? 'الواتساب والرسائل' : 'WhatsApp & SMS' },
    { to: '/working-hours', icon: <Clock size={19} />, label: isRTL ? 'أوقات العمل' : 'Horaires' },
    { to: '/subscription', icon: <CreditCard size={19} />, label: isRTL ? 'الاشتراك' : 'Abonnement' },
    { to: '/settings', icon: <Settings size={19} />, label: isRTL ? 'الإعدادات' : 'Paramètres' },
  ];

  return (
    <aside style={{
      width: '260px',
      backgroundColor: 'var(--color-surface)',
      borderRight: isRTL ? 'none' : '1px solid var(--color-border)',
      borderLeft: isRTL ? '1px solid var(--color-border)' : 'none',
      height: '100vh',
      position: 'sticky',
      top: 0,
      display: 'flex',
      flexDirection: 'column',
      justifyContent: 'space-between',
      padding: '1.25rem 0.875rem',
      zIndex: 40
    }}>
      <div>
        {/* Brand */}
        <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem', padding: '0.5rem 0.75rem', marginBottom: '1.5rem' }}>
          <div style={{
            width: '36px',
            height: '36px',
            borderRadius: '8px',
            backgroundColor: 'var(--color-primary)',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            color: '#FFFFFF'
          }}>
            <Calendar size={18} color="#C5982E" />
          </div>
          <div>
            <div style={{ fontWeight: 800, fontSize: '1.15rem', color: 'var(--color-primary)' }}>
              Mawa3id<span style={{ color: 'var(--color-gold)' }}>.ma</span>
            </div>
            <div style={{ fontSize: '0.75rem', color: 'var(--color-text-muted)' }}>
              {user?.businessName || 'SaaS Maroc'}
            </div>
          </div>
        </div>

        {/* Public Booking Link Badge */}
        {user?.businessSlug && (
          <a
            href={`/booking/${user.businessSlug}`}
            target="_blank"
            rel="noopener noreferrer"
            style={{
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'space-between',
              padding: '0.625rem 0.875rem',
              backgroundColor: 'var(--color-primary-light)',
              borderRadius: 'var(--radius-md)',
              color: 'var(--color-primary)',
              fontSize: '0.8rem',
              fontWeight: 600,
              marginBottom: '1.25rem',
              textDecoration: 'none'
            }}
          >
            <span>{isRTL ? 'رابط الحجز المباشر' : 'Lien de réservation'}</span>
            <ExternalLink size={14} />
          </a>
        )}

        {/* Nav Items */}
        <nav style={{ display: 'flex', flexDirection: 'column', gap: '0.25rem' }}>
          {navItems.map((item) => (
            <NavLink
              key={item.to}
              to={item.to}
              style={({ isActive }) => ({
                display: 'flex',
                alignItems: 'center',
                gap: '0.75rem',
                padding: '0.625rem 0.875rem',
                borderRadius: 'var(--radius-md)',
                fontWeight: isActive ? 700 : 500,
                color: isActive ? 'var(--color-primary)' : 'var(--color-text-muted)',
                backgroundColor: isActive ? 'var(--color-primary-light)' : 'transparent',
                transition: 'all 0.15s ease'
              })}
            >
              {item.icon}
              <span style={{ fontSize: '0.9rem' }}>{item.label}</span>
            </NavLink>
          ))}

          {/* Super Admin link if applicable */}
          {user?.role === 'SUPER_ADMIN' && (
            <NavLink
              to="/admin"
              style={({ isActive }) => ({
                display: 'flex',
                alignItems: 'center',
                gap: '0.75rem',
                padding: '0.625rem 0.875rem',
                borderRadius: 'var(--radius-md)',
                fontWeight: isActive ? 700 : 500,
                color: isActive ? 'var(--color-secondary)' : 'var(--color-secondary)',
                backgroundColor: isActive ? 'var(--color-secondary-light)' : 'transparent',
                marginTop: '0.5rem'
              })}
            >
              <ShieldAlert size={19} />
              <span style={{ fontSize: '0.9rem' }}>Administration</span>
            </NavLink>
          )}
        </nav>
      </div>

      {/* User Footer & Logout */}
      <div style={{ borderTop: '1px solid var(--color-border)', paddingTop: '1rem' }}>
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', padding: '0.25rem 0.5rem' }}>
          <div>
            <div style={{ fontWeight: 600, fontSize: '0.85rem' }}>{user?.firstName} {user?.lastName}</div>
            <div style={{ fontSize: '0.75rem', color: 'var(--color-text-muted)' }}>{user?.email}</div>
          </div>
          <button
            onClick={handleLogout}
            style={{
              border: 'none',
              background: 'transparent',
              color: 'var(--color-danger)',
              cursor: 'pointer',
              padding: '0.4rem',
              borderRadius: '6px'
            }}
            title="Se déconnecter"
          >
            <LogOut size={18} />
          </button>
        </div>
      </div>
    </aside>
  );
};
