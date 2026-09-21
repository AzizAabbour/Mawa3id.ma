import React, { useEffect, useState } from 'react';
import { Sidebar } from '../components/Sidebar';
import { Header } from '../components/Header';
import { StatCard } from '../components/StatCard';
import { useAuth } from '../contexts/AuthContext';
import { useLanguage } from '../contexts/LanguageContext';
import { apiFetch } from '../services/api';
import { 
  Calendar, 
  Users, 
  TrendingUp, 
  MessageSquare, 
  CheckCircle2, 
  Clock, 
  XCircle, 
  AlertCircle,
  Plus,
  ExternalLink
} from 'lucide-react';
import { ResponsiveContainer, AreaChart, Area, XAxis, YAxis, Tooltip, CartesianGrid } from 'recharts';

export const DashboardPage: React.FC = () => {
  const { user } = useAuth();
  const { isRTL } = useLanguage();
  const [stats, setStats] = useState<any>(null);
  const [chartData, setChartData] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadDashboardData();
  }, []);

  const loadDashboardData = async () => {
    try {
      const [statsRes, chartRes] = await Promise.all([
        apiFetch<any>('/dashboard/statistics'),
        apiFetch<any[]>('/dashboard/charts/appointments')
      ]);
      setStats(statsRes);
      setChartData(chartRes);
    } catch (err) {
      console.error('Failed to load dashboard data', err);
      // Fallback demo data if backend not connected
      setStats({
        todayAppointmentsCount: 8,
        upcomingAppointmentsCount: 5,
        completedAppointmentsCount: 3,
        cancelledAppointmentsCount: 1,
        noShowCount: 0,
        noShowRatePercentage: 0,
        totalCustomersCount: 42,
        monthlyEstimatedRevenueMad: 4850,
        whatsappSentCount: 36,
        smsSentCount: 8,
        notificationDeliveryRatePercentage: 97.2,
        todayAppointments: [
          { id: 1, customerName: 'Yassine El Amrani', serviceName: 'Coupe & Barbe', employeeName: 'Rachid', startTime: '10:00', servicePriceMad: 80, status: 'COMPLETED' },
          { id: 2, customerName: 'Sara Bennani', serviceName: 'Soin Visage Hydratant', employeeName: 'Fatima', startTime: '11:30', servicePriceMad: 250, status: 'COMPLETED' },
          { id: 3, customerName: 'Mehdi Chraibi', serviceName: 'Coupe Dégradé', employeeName: 'Rachid', startTime: '14:00', servicePriceMad: 60, status: 'CONFIRMED' },
          { id: 4, customerName: 'Khadija Mansouri', serviceName: 'Coloration & Brushing', employeeName: 'Fatima', startTime: '15:30', servicePriceMad: 350, status: 'CONFIRMED' },
          { id: 5, customerName: 'Omar Tazi', serviceName: 'Coupe Classique', employeeName: 'Rachid', startTime: '17:00', servicePriceMad: 50, status: 'CONFIRMED' }
        ]
      });
      setChartData([
        { label: 'Lun', count: 6 },
        { label: 'Mar', count: 8 },
        { label: 'Mer', count: 11 },
        { label: 'Jeu', count: 9 },
        { label: 'Ven', count: 14 },
        { label: 'Sam', count: 18 },
        { label: 'Dim', count: 4 }
      ]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ display: 'flex', minHeight: '100vh', backgroundColor: 'var(--color-bg)' }}>
      <Sidebar />

      <div style={{ flex: 1, display: 'flex', flexDirection: 'column' }}>
        <Header 
          title={isRTL ? `مرحباً ${user?.firstName || 'عزيز'} 👋` : `Bonjour ${user?.firstName || 'Aziz'} 👋`}
          subtitle={isRTL ? `لديك ${stats?.todayAppointmentsCount || 0} مواعيد مبرمجة لليوم.` : `Vous avez ${stats?.todayAppointmentsCount || 0} rendez-vous programmés aujourd'hui.`}
        />

        <main style={{ padding: '2rem', flex: 1 }}>
          {/* Quick Booking Link Alert */}
          {user?.businessSlug && (
            <div style={{
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'space-between',
              backgroundColor: 'var(--color-primary-light)',
              border: '1px solid var(--color-primary-glow)',
              borderRadius: 'var(--radius-md)',
              padding: '1rem 1.25rem',
              marginBottom: '1.75rem'
            }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem' }}>
                <span style={{ fontSize: '1.25rem' }}>🔗</span>
                <div>
                  <div style={{ fontWeight: 700, fontSize: '0.9rem', color: 'var(--color-primary)' }}>
                    {isRTL ? 'رابط الحجز المباشر لزبنائك' : 'Votre lien de réservation en ligne'}
                  </div>
                  <div style={{ fontSize: '0.8rem', color: 'var(--color-text-muted)' }}>
                    Partagez ce lien sur vos réseaux sociaux pour recevoir des réservations 24h/24
                  </div>
                </div>
              </div>
              <a
                href={`/booking/${user.businessSlug}`}
                target="_blank"
                rel="noopener noreferrer"
                className="btn btn-primary"
                style={{ padding: '0.4rem 0.85rem', fontSize: '0.825rem' }}
              >
                <span>{isRTL ? 'معاينة الرابط' : 'Tester la page'}</span>
                <ExternalLink size={14} />
              </a>
            </div>
          )}

          {/* Top KPI Cards */}
          <div className="grid-cols-4" style={{ marginBottom: '2rem' }}>
            <StatCard
              title={isRTL ? 'مواعيد اليوم' : "Rendez-vous aujourd'hui"}
              value={stats?.todayAppointmentsCount || 0}
              subtitle={isRTL ? `${stats?.upcomingAppointmentsCount || 0} قادمة` : `${stats?.upcomingAppointmentsCount || 0} à venir`}
              icon={<Calendar size={20} />}
              color="var(--color-primary)"
            />
            <StatCard
              title={isRTL ? 'المداخيل التقديرية (شهرياً)' : 'Revenus estimés (Mois)'}
              value={`${stats?.monthlyEstimatedRevenueMad || 0} DH`}
              subtitle={isRTL ? 'بالدرهم المغربي' : 'Total prestations'}
              icon={<TrendingUp size={20} />}
              color="var(--color-gold)"
            />
            <StatCard
              title={isRTL ? 'رسائل الواتساب المرسلة' : 'WhatsApp Automatiques'}
              value={stats?.whatsappSentCount || 0}
              subtitle={`Taux délivrabilité: ${stats?.notificationDeliveryRatePercentage || 98}%`}
              icon={<MessageSquare size={20} />}
              color="#25D366"
            />
            <StatCard
              title={isRTL ? 'نسبة الغياب (No-Show)' : 'Taux de No-Show'}
              value={`${stats?.noShowRatePercentage || 0}%`}
              subtitle={isRTL ? 'منخفض بفضل التذكيرات' : 'Optimisé via WhatsApp'}
              icon={<AlertCircle size={20} />}
              color="var(--color-secondary)"
            />
          </div>

          {/* Charts & Today Appointments Grid */}
          <div style={{ display: 'grid', gridTemplateColumns: '1.6fr 1.4fr', gap: '1.5rem' }}>
            {/* Chart */}
            <div className="card">
              <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '1.25rem' }}>
                <h3 style={{ fontSize: '1.1rem', fontWeight: 700 }}>
                  {isRTL ? 'نشاط المواعيد خلال الأسبوع' : 'Activité des Rendez-vous (7 derniers jours)'}
                </h3>
              </div>
              <div style={{ height: '260px', width: '100%' }}>
                <ResponsiveContainer width="100%" height="100%">
                  <AreaChart data={chartData}>
                    <defs>
                      <linearGradient id="colorCount" x1="0" y1="0" x2="0" y2="1">
                        <stop offset="5%" stopColor="var(--color-primary)" stopOpacity={0.4}/>
                        <stop offset="95%" stopColor="var(--color-primary)" stopOpacity={0.0}/>
                      </linearGradient>
                    </defs>
                    <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#EFE8DA" />
                    <XAxis dataKey="label" stroke="#8E9E97" fontSize={12} />
                    <YAxis stroke="#8E9E97" fontSize={12} allowDecimals={false} />
                    <Tooltip contentStyle={{ backgroundColor: '#FFFFFF', borderRadius: '8px', border: '1px solid #E6DFD5' }} />
                    <Area type="monotone" dataKey="count" stroke="var(--color-primary)" strokeWidth={2.5} fillOpacity={1} fill="url(#colorCount)" />
                  </AreaChart>
                </ResponsiveContainer>
              </div>
            </div>

            {/* Today Appointments List */}
            <div className="card" style={{ display: 'flex', flexDirection: 'column' }}>
              <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '1rem' }}>
                <h3 style={{ fontSize: '1.1rem', fontWeight: 700 }}>
                  {isRTL ? 'برنامج اليوم' : "Planning d'aujourd'hui"}
                </h3>
                <span className="badge badge-info">{stats?.todayAppointments?.length || 0} RDV</span>
              </div>

              <div style={{ display: 'flex', flexDirection: 'column', gap: '0.75rem', overflowY: 'auto', maxHeight: '300px' }}>
                {stats?.todayAppointments?.map((app: any) => (
                  <div
                    key={app.id}
                    style={{
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'space-between',
                      padding: '0.75rem 1rem',
                      backgroundColor: 'var(--color-surface-subtle)',
                      borderRadius: 'var(--radius-md)',
                      borderLeft: isRTL ? 'none' : '3px solid var(--color-primary)',
                      borderRight: isRTL ? '3px solid var(--color-primary)' : 'none'
                    }}
                  >
                    <div>
                      <div style={{ fontWeight: 700, fontSize: '0.9rem' }}>{app.customerName}</div>
                      <div style={{ fontSize: '0.8rem', color: 'var(--color-text-muted)' }}>
                        {app.serviceName} • {app.employeeName}
                      </div>
                    </div>

                    <div style={{ textAlign: 'right' }}>
                      <div style={{ fontWeight: 700, fontSize: '0.9rem', color: 'var(--color-primary)' }}>
                        {app.startTime}
                      </div>
                      <div style={{ fontSize: '0.75rem', fontWeight: 600, color: 'var(--color-gold)' }}>
                        {app.servicePriceMad} DH
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          </div>
        </main>
      </div>
    </div>
  );
};
