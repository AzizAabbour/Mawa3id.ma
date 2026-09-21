import React from 'react';
import { Link } from 'react-router-dom';
import { useLanguage } from '../contexts/LanguageContext';
import { useAuth } from '../contexts/AuthContext';
import { Calendar, Globe, Sparkles } from 'lucide-react';

export const Navbar: React.FC = () => {
  const { t, language, setLanguage } = useLanguage();
  const { isAuthenticated, user } = useAuth();

  return (
    <nav style={{
      position: 'sticky',
      top: 0,
      zIndex: 50,
      backgroundColor: 'rgba(250, 248, 245, 0.9)',
      backdropFilter: 'blur(10px)',
      borderBottom: '1px solid var(--color-border)',
      padding: '0.875rem 0'
    }}>
      <div className="container" style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        {/* Logo */}
        <Link to="/" style={{ display: 'flex', alignItems: 'center', gap: '0.625rem' }}>
          <div style={{
            width: '38px',
            height: '38px',
            borderRadius: '10px',
            backgroundColor: 'var(--color-primary)',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            color: '#FFFFFF',
            boxShadow: '0 2px 8px var(--color-primary-glow)'
          }}>
            <Calendar size={20} color="#C5982E" />
          </div>
          <div>
            <span style={{ fontWeight: 800, fontSize: '1.25rem', color: 'var(--color-primary)', letterSpacing: '-0.02em' }}>
              Mawa3id<span style={{ color: 'var(--color-gold)' }}>.ma</span>
            </span>
          </div>
        </Link>

        {/* Links */}
        <div style={{ display: 'flex', alignItems: 'center', gap: '1.75rem' }}>
          <a href="#features" style={{ fontWeight: 600, color: 'var(--color-text-muted)', fontSize: '0.925rem' }}>
            {t('nav_features')}
          </a>
          <a href="#whatsapp" style={{ fontWeight: 600, color: 'var(--color-text-muted)', fontSize: '0.925rem' }}>
            {t('nav_whatsapp')}
          </a>
          <a href="#pricing" style={{ fontWeight: 600, color: 'var(--color-text-muted)', fontSize: '0.925rem' }}>
            {t('nav_pricing')}
          </a>
        </div>

        {/* CTA & Lang Toggle */}
        <div style={{ display: 'flex', alignItems: 'center', gap: '0.875rem' }}>
          {/* Language Switcher */}
          <button
            onClick={() => setLanguage(language === 'fr' ? 'ar' : 'fr')}
            className="btn btn-secondary"
            style={{ padding: '0.4rem 0.75rem', fontSize: '0.85rem' }}
            title="Changer la langue"
          >
            <Globe size={16} />
            <span>{language === 'fr' ? 'العربية' : 'Français'}</span>
          </button>

          {isAuthenticated ? (
            <Link to="/dashboard" className="btn btn-primary" style={{ padding: '0.5rem 1rem' }}>
              <Sparkles size={16} />
              <span>{user?.firstName ? `Tableau de bord (${user.firstName})` : 'Tableau de bord'}</span>
            </Link>
          ) : (
            <>
              <Link to="/login" className="btn btn-secondary" style={{ padding: '0.5rem 1rem' }}>
                {t('nav_login')}
              </Link>
              <Link to="/register" className="btn btn-primary" style={{ padding: '0.5rem 1.15rem' }}>
                {t('start_free')}
              </Link>
            </>
          )}
        </div>
      </div>
    </nav>
  );
};
