import React from 'react';

interface StatCardProps {
  title: string;
  value: string | number;
  subtitle?: string;
  icon: React.ReactNode;
  color?: string;
  trend?: {
    value: string;
    positive?: boolean;
  };
}

export const StatCard: React.FC<StatCardProps> = ({ title, value, subtitle, icon, color = 'var(--color-primary)', trend }) => {
  return (
    <div className="card" style={{ position: 'relative', overflow: 'hidden' }}>
      <div style={{ display: 'flex', alignItems: 'flex-start', justifyContent: 'space-between', marginBottom: '0.75rem' }}>
        <span style={{ fontSize: '0.875rem', fontWeight: 600, color: 'var(--color-text-muted)' }}>
          {title}
        </span>
        <div style={{
          width: '38px',
          height: '38px',
          borderRadius: '10px',
          backgroundColor: `${color}15`,
          color: color,
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center'
        }}>
          {icon}
        </div>
      </div>

      <div style={{ fontSize: '1.75rem', fontWeight: 800, color: 'var(--color-text-main)', letterSpacing: '-0.02em' }}>
        {value}
      </div>

      {(subtitle || trend) && (
        <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', marginTop: '0.375rem', fontSize: '0.8rem' }}>
          {trend && (
            <span style={{
              fontWeight: 700,
              color: trend.positive ? 'var(--color-success)' : 'var(--color-danger)'
            }}>
              {trend.value}
            </span>
          )}
          {subtitle && <span style={{ color: 'var(--color-text-muted)' }}>{subtitle}</span>}
        </div>
      )}
    </div>
  );
};
