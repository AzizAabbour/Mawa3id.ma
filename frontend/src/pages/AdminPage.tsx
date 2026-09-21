import React, { useEffect, useState } from 'react';
import { Sidebar } from '../components/Sidebar';
import { Header } from '../components/Header';
import { StatCard } from '../components/StatCard';
import { apiFetch } from '../services/api';
import { ShieldAlert, Building2, TrendingUp, Bell, CheckCircle, Ban } from 'lucide-react';

export const AdminPage: React.FC = () => {
  const [stats, setStats] = useState<any>(null);
  const [businesses, setBusinesses] = useState<any[]>([]);

  useEffect(() => {
    loadAdminData();
  }, []);

  const loadAdminData = async () => {
    try {
      const [statsRes, bizRes] = await Promise.all([
        apiFetch<any>('/admin/statistics'),
        apiFetch<any>('/admin/businesses')
      ]);
      setStats(statsRes);
      setBusinesses(bizRes.content || []);
    } catch {
      // Demo fallback data
      setStats({
        totalBusinesses: 128,
        activeSubscriptions: 94,
        monthlyRecurringRevenueMad: 14006,
        totalAppointmentsPlatformWide: 14820,
        totalNotificationsSent: 28450,
        totalFailedNotifications: 12
      });
      setBusinesses([
        { id: 1, name: 'Salon Riad Beauty', slug: 'salon-riad-beauty', city: 'Casablanca', active: true, phone: '+212612345678' },
        { id: 2, name: 'Clinique Al Amal', slug: 'clinique-al-amal', city: 'Rabat', active: true, phone: '+212537123456' },
        { id: 3, name: 'Barber Medina', slug: 'barber-medina', city: 'Marrakech', active: true, phone: '+212661998877' },
        { id: 4, name: 'Garage Atlas', slug: 'garage-atlas', city: 'Tanger', active: true, phone: '+212539112233' }
      ]);
    }
  };

  const handleToggle = async (id: number, currentActive: boolean) => {
    try {
      await apiFetch(`/admin/businesses/${id}/status`, {
        method: 'PATCH',
        body: JSON.stringify({ active: !currentActive })
      });
      loadAdminData();
    } catch (err: any) {
      alert(err.message || 'Erreur lors de la modération');
    }
  };

  return (
    <div style={{ display: 'flex', minHeight: '100vh', backgroundColor: 'var(--color-bg)' }}>
      <Sidebar />

      <div style={{ flex: 1, display: 'flex', flexDirection: 'column' }}>
        <Header 
          title="Super Administration Mawa3id.ma" 
          subtitle="Suivi de la plateforme, MRR en MAD et modération des comptes"
        />

        <main style={{ padding: '2rem', flex: 1 }}>
          {/* Top Admin KPI Cards */}
          <div className="grid-cols-4" style={{ marginBottom: '2rem' }}>
            <StatCard
              title="Total Établissements"
              value={stats?.totalBusinesses || 0}
              icon={<Building2 size={20} />}
              color="var(--color-primary)"
            />
            <StatCard
              title="MRR (Revenus Récurrents)"
              value={`${stats?.monthlyRecurringRevenueMad || 0} DH`}
              subtitle="Par mois"
              icon={<TrendingUp size={20} />}
              color="var(--color-gold)"
            />
            <StatCard
              title="Abonnements Payants"
              value={stats?.activeSubscriptions || 0}
              icon={<CheckCircle size={20} />}
              color="var(--color-success)"
            />
            <StatCard
              title="Notifications Envoyées"
              value={stats?.totalNotificationsSent || 0}
              subtitle="WhatsApp & SMS"
              icon={<Bell size={20} />}
              color="#25D366"
            />
          </div>

          {/* Business Management Table */}
          <div className="card" style={{ padding: 0, overflow: 'hidden' }}>
            <div style={{ padding: '1.25rem 1.5rem', borderBottom: '1px solid var(--color-border)' }}>
              <h3 style={{ fontSize: '1.1rem', fontWeight: 700 }}>Toutes les entreprises clientes</h3>
            </div>

            <table style={{ width: '100%', borderCollapse: 'collapse' }}>
              <thead>
                <tr style={{ backgroundColor: 'var(--color-surface-subtle)', borderBottom: '1px solid var(--color-border)' }}>
                  <th style={{ padding: '0.875rem 1.25rem', fontSize: '0.8rem', fontWeight: 700, color: 'var(--color-text-muted)' }}>ENTREPRISE</th>
                  <th style={{ padding: '0.875rem 1.25rem', fontSize: '0.8rem', fontWeight: 700, color: 'var(--color-text-muted)' }}>VILLE</th>
                  <th style={{ padding: '0.875rem 1.25rem', fontSize: '0.8rem', fontWeight: 700, color: 'var(--color-text-muted)' }}>CONTACT</th>
                  <th style={{ padding: '0.875rem 1.25rem', fontSize: '0.8rem', fontWeight: 700, color: 'var(--color-text-muted)' }}>STATUT</th>
                  <th style={{ padding: '0.875rem 1.25rem', fontSize: '0.8rem', fontWeight: 700, color: 'var(--color-text-muted)' }}>MODÉRATION</th>
                </tr>
              </thead>
              <tbody>
                {businesses.map((biz) => (
                  <tr key={biz.id} style={{ borderBottom: '1px solid var(--color-border-subtle)' }}>
                    <td style={{ padding: '1rem 1.25rem' }}>
                      <div style={{ fontWeight: 700 }}>{biz.name}</div>
                      <div style={{ fontSize: '0.75rem', color: 'var(--color-text-muted)' }}>/booking/{biz.slug}</div>
                    </td>
                    <td style={{ padding: '1rem 1.25rem', fontSize: '0.875rem' }}>{biz.city}</td>
                    <td style={{ padding: '1rem 1.25rem', fontSize: '0.875rem' }}>{biz.phone}</td>
                    <td style={{ padding: '1rem 1.25rem' }}>
                      <span className={`badge ${biz.active ? 'badge-success' : 'badge-danger'}`}>
                        {biz.active ? 'Actif' : 'Suspendu'}
                      </span>
                    </td>
                    <td style={{ padding: '1rem 1.25rem' }}>
                      <button
                        onClick={() => handleToggle(biz.id, biz.active)}
                        className={`btn ${biz.active ? 'btn-secondary' : 'btn-primary'}`}
                        style={{ padding: '0.35rem 0.75rem', fontSize: '0.8rem' }}
                      >
                        {biz.active ? 'Suspendre' : 'Activer'}
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </main>
      </div>
    </div>
  );
};
