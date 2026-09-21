import React, { useEffect, useState } from 'react';
import { Sidebar } from '../components/Sidebar';
import { Header } from '../components/Header';
import { useLanguage } from '../contexts/LanguageContext';
import { apiFetch } from '../services/api';
import { CreditCard, Check, Sparkles, AlertCircle } from 'lucide-react';

export const SubscriptionPage: React.FC = () => {
  const { isRTL } = useLanguage();
  const [plans, setPlans] = useState<any[]>([]);
  const [currentSub, setCurrentSub] = useState<any>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadSubscriptionData();
  }, []);

  const loadSubscriptionData = async () => {
    try {
      const [plansRes, subRes] = await Promise.all([
        apiFetch<any[]>('/subscription/plans'),
        apiFetch<any>('/subscription')
      ]);
      setPlans(plansRes || []);
      setCurrentSub(subRes);
    } catch (err) {
      // Demo fallback data
      setPlans([
        { id: 1, name: 'Gratuit', slug: 'free', priceMonthlyMad: 0, maxAppointmentsPerMonth: 30, maxCustomers: 50, features: ['30 rendez-vous / mois', '50 clients max', '1 collaborateur', 'Page publique'] },
        { id: 2, name: 'Starter', slug: 'starter', priceMonthlyMad: 79, maxAppointmentsPerMonth: 300, maxCustomers: -1, features: ['300 rendez-vous / mois', 'Clients illimités', '2 collaborateurs', 'Rappels WhatsApp'] },
        { id: 3, name: 'Business', slug: 'business', priceMonthlyMad: 149, maxAppointmentsPerMonth: -1, maxCustomers: -1, features: ['Rendez-vous illimités', 'Clients illimités', '10 collaborateurs', 'WhatsApp & SMS', 'Support prioritaire 7j/7'] },
        { id: 4, name: 'Pro', slug: 'pro', priceMonthlyMad: 299, maxAppointmentsPerMonth: -1, maxCustomers: -1, features: ['Tout en illimité', 'Multi-succursales', 'API & Intégrations', 'Support 24/7'] }
      ]);
      setCurrentSub({
        plan: { name: 'Business', slug: 'business', priceMonthlyMad: 149 },
        status: 'ACTIVE',
        currentMonthAppointmentsCount: 48,
        totalCustomersCount: 42,
        totalEmployeesCount: 2
      });
    } finally {
      setLoading(false);
    }
  };

  const handleUpgrade = async (planSlug: string) => {
    try {
      await apiFetch('/subscription/upgrade', {
        method: 'POST',
        body: JSON.stringify({ planSlug, paymentMethod: 'CARD' })
      });
      alert('Paiement CMI validé avec succès ! Votre forfait a été mis à jour.');
      loadSubscriptionData();
    } catch (err: any) {
      alert(err.message || 'Erreur lors du paiement');
    }
  };

  return (
    <div style={{ display: 'flex', minHeight: '100vh', backgroundColor: 'var(--color-bg)' }}>
      <Sidebar />

      <div style={{ flex: 1, display: 'flex', flexDirection: 'column' }}>
        <Header 
          title={isRTL ? 'الاشتراك والباقات' : 'Abonnement & Facturation'} 
          subtitle={isRTL ? 'إدارة باقة الاشتراك والدفع بالدرهم المغربي' : 'Gérez votre forfait SaaS et vos paiements en Dirhams'}
        />

        <main style={{ padding: '2rem', flex: 1 }}>
          {/* Current Quota Status */}
          <div className="card" style={{ marginBottom: '2.5rem', backgroundColor: 'var(--color-primary-light)', borderColor: 'var(--color-primary-glow)' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '1rem' }}>
              <div>
                <span className="badge badge-success" style={{ marginBottom: '0.5rem' }}>
                  Forfait Actuel : {currentSub?.plan?.name || 'Business'}
                </span>
                <h3 style={{ fontSize: '1.25rem', fontWeight: 800, color: 'var(--color-primary)' }}>
                  {currentSub?.status === 'ACTIVE' ? 'Abonnement Actif' : 'Période d’essai'}
                </h3>
                <p style={{ fontSize: '0.85rem', color: 'var(--color-text-muted)', marginTop: '0.25rem' }}>
                  Utilisation ce mois-ci : <strong>{currentSub?.currentMonthAppointmentsCount || 48}</strong> rendez-vous • <strong>{currentSub?.totalCustomersCount || 42}</strong> clients
                </p>
              </div>

              <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', fontSize: '0.85rem', color: 'var(--color-primary)', fontWeight: 700 }}>
                <CreditCard size={18} />
                <span>Paiement sécurisé CMI Maroc (MAD)</span>
              </div>
            </div>
          </div>

          {/* Pricing Grid */}
          <h2 style={{ fontSize: '1.25rem', fontWeight: 800, marginBottom: '1.25rem' }}>
            {isRTL ? 'اختر الباقة المناسبة لك' : 'Changer ou renouveler de forfait'}
          </h2>

          <div className="grid-cols-4">
            {plans.map((plan) => {
              const isCurrent = currentSub?.plan?.slug === plan.slug;
              return (
                <div
                  key={plan.id || plan.slug}
                  className="card"
                  style={{
                    display: 'flex',
                    flexDirection: 'column',
                    justifyContent: 'space-between',
                    borderColor: isCurrent ? 'var(--color-primary)' : 'var(--color-border)',
                    boxShadow: isCurrent ? '0 8px 20px var(--color-primary-glow)' : 'var(--shadow-sm)'
                  }}
                >
                  <div>
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '0.5rem' }}>
                      <h3 style={{ fontSize: '1.2rem', fontWeight: 700 }}>{plan.name}</h3>
                      {isCurrent && <span className="badge badge-success">Actuel</span>}
                    </div>

                    <div style={{ fontSize: '1.85rem', fontWeight: 800, color: 'var(--color-primary)', marginBottom: '1.25rem' }}>
                      {plan.priceMonthlyMad} <span style={{ fontSize: '0.9rem', fontWeight: 600 }}>DH/mois</span>
                    </div>

                    <ul style={{ listStyle: 'none', display: 'flex', flexDirection: 'column', gap: '0.625rem', fontSize: '0.85rem' }}>
                      {plan.features?.map((feat: string, idx: number) => (
                        <li key={idx} style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                          <Check size={14} color="var(--color-primary)" />
                          <span>{feat}</span>
                        </li>
                      ))}
                    </ul>
                  </div>

                  <button
                    disabled={isCurrent}
                    onClick={() => handleUpgrade(plan.slug)}
                    className={`btn ${isCurrent ? 'btn-secondary' : 'btn-primary'}`}
                    style={{ marginTop: '1.5rem', width: '100%' }}
                  >
                    {isCurrent ? 'Forfait actuel' : 'Choisir ce forfait'}
                  </button>
                </div>
              );
            })}
          </div>
        </main>
      </div>
    </div>
  );
};
