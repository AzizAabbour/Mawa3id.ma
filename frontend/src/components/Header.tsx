import React from 'react';
import { useAuth } from '../contexts/AuthContext';
import { useLanguage } from '../contexts/LanguageContext';
import { Globe, Bell } from 'lucide-react';

export const Header: React.FC<{ title: string; subtitle?: string }> = ({ title, subtitle }) => {
  const { user } = useAuth();
  const { language, setLanguage } = useLanguage();

  return (
    <header style={{
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'space-between',
      padding: '1.25rem 2rem',
      backgroundColor: 'var(--color-surface)',
      borderBottom: '1px solid var(--color-border)'
    }}>
      <div>
        <h1 style={{ fontSize: '1.5rem', fontWeight: 800, color: 'var(--color-text-main)' }}>{title}</h1>
        {subtitle && <p style={{ fontSize: '0.875rem', color: 'var(--color-text-muted)', marginTop: '0.125rem' }}>{subtitle}</p>}
      </div>

      <div style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
        {/* Language switch */}
        <button
          onClick={() => setLanguage(language === 'fr' ? 'ar' : 'fr')}
          className="btn btn-secondary"
          style={{ padding: '0.4rem 0.75rem', fontSize: '0.85rem' }}
        >
          <Globe size={16} />
          <span>{language === 'fr' ? 'العربية' : 'Français'}</span>
        </button>

        {/* User avatar badge */}
        <div style={{
          display: 'flex',
          alignItems: 'center',
          gap: '0.625rem',
          padding: '0.35rem 0.75rem',
          backgroundColor: 'var(--color-surface-subtle)',
          borderRadius: 'var(--radius-full)'
        }}>
          <div style={{
            width: '28px',
            height: '28px',
            borderRadius: '50%',
            backgroundColor: 'var(--color-primary)',
            color: '#FFFFFF',
            fontWeight: 700,
            fontSize: '0.8rem',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center'
          }}>
            {user?.firstName?.[0] || 'A'}
          </div>
          <span style={{ fontSize: '0.85rem', fontWeight: 600 }}>{user?.firstName}</span>
        </div>
      </div>
    </header>
  );
};
