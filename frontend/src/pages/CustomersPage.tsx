import React, { useEffect, useState } from 'react';
import { Sidebar } from '../components/Sidebar';
import { Header } from '../components/Header';
import { useLanguage } from '../contexts/LanguageContext';
import { apiFetch } from '../services/api';
import { Users, Plus, Search, Phone, Mail, Award } from 'lucide-react';

export const CustomersPage: React.FC = () => {
  const { isRTL } = useLanguage();
  const [customers, setCustomers] = useState<any[]>([]);
  const [searchQuery, setSearchQuery] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [formData, setFormData] = useState({ fullName: '', phone: '', whatsappNumber: '', email: '', notes: '' });

  useEffect(() => {
    loadCustomers();
  }, [searchQuery]);

  const loadCustomers = async () => {
    try {
      const endpoint = searchQuery ? `/customers?query=${encodeURIComponent(searchQuery)}` : '/customers';
      const res = await apiFetch<any>(endpoint);
      setCustomers(res.content || []);
    } catch (err) {
      // Demo fallback data
      setCustomers([
        { id: 1, fullName: 'Karim Bennani', phone: '+212612345678', whatsappNumber: '+212612345678', email: 'karim@gmail.com', totalAppointments: 14, totalSpentMad: 1120, notes: 'Client fidèle depuis 2025' },
        { id: 2, fullName: 'Sara Mansouri', phone: '+212622334455', whatsappNumber: '+212622334455', email: 'sara.m@yahoo.fr', totalAppointments: 8, totalSpentMad: 2400, notes: 'Préfère les rendez-vous le samedi matin' },
        { id: 3, fullName: 'Mehdi Chraibi', phone: '+212633445566', whatsappNumber: '+212633445566', email: 'mehdi@outlook.com', totalAppointments: 5, totalSpentMad: 450, notes: '' },
        { id: 4, fullName: 'Khadija Alami', phone: '+212644556677', whatsappNumber: '+212644556677', email: 'khadija@gmail.com', totalAppointments: 11, totalSpentMad: 3850, notes: 'Soin capillaire' }
      ]);
    }
  };

  const handleCreate = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await apiFetch('/customers', { method: 'POST', body: JSON.stringify(formData) });
      setShowModal(false);
      setFormData({ fullName: '', phone: '', whatsappNumber: '', email: '', notes: '' });
      loadCustomers();
    } catch (err: any) {
      alert(err.message || 'Erreur lors de la création');
    }
  };

  return (
    <div style={{ display: 'flex', minHeight: '100vh', backgroundColor: 'var(--color-bg)' }}>
      <Sidebar />

      <div style={{ flex: 1, display: 'flex', flexDirection: 'column' }}>
        <Header 
          title={isRTL ? 'دليل الزبناء' : 'Répertoire Clients'} 
          subtitle={isRTL ? 'إدارة بيانات الزبناء، السجل والتفاعل' : 'Consultez la fidélité, dépenses et coordonnées'}
        />

        <main style={{ padding: '2rem', flex: 1 }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem', gap: '1rem', flexWrap: 'wrap' }}>
            <div style={{ position: 'relative', maxWidth: '320px', width: '100%' }}>
              <Search size={18} style={{ position: 'absolute', top: '50%', transform: 'translateY(-50%)', left: isRTL ? 'auto' : '12px', right: isRTL ? '12px' : 'auto', color: 'var(--color-text-muted)' }} />
              <input
                type="text"
                placeholder={isRTL ? 'بحث بالاسم أو الهاتف...' : 'Rechercher par nom ou téléphone...'}
                className="form-input"
                style={{ paddingLeft: isRTL ? '1rem' : '2.5rem', paddingRight: isRTL ? '2.5rem' : '1rem' }}
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
              />
            </div>

            <button onClick={() => setShowModal(true)} className="btn btn-primary">
              <Plus size={18} />
              <span>{isRTL ? 'إضافة زبون جديد' : 'Nouveau client'}</span>
            </button>
          </div>

          <div className="card" style={{ padding: 0, overflow: 'hidden' }}>
            <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: isRTL ? 'right' : 'left' }}>
              <thead>
                <tr style={{ backgroundColor: 'var(--color-surface-subtle)', borderBottom: '1px solid var(--color-border)' }}>
                  <th style={{ padding: '0.875rem 1.25rem', fontSize: '0.8rem', fontWeight: 700, color: 'var(--color-text-muted)' }}>CLIENT</th>
                  <th style={{ padding: '0.875rem 1.25rem', fontSize: '0.8rem', fontWeight: 700, color: 'var(--color-text-muted)' }}>TÉLÉPHONE / WHATSAPP</th>
                  <th style={{ padding: '0.875rem 1.25rem', fontSize: '0.8rem', fontWeight: 700, color: 'var(--color-text-muted)' }}>EMAIL</th>
                  <th style={{ padding: '0.875rem 1.25rem', fontSize: '0.8rem', fontWeight: 700, color: 'var(--color-text-muted)' }}>TOTAL RDV</th>
                  <th style={{ padding: '0.875rem 1.25rem', fontSize: '0.8rem', fontWeight: 700, color: 'var(--color-text-muted)' }}>TOTAL DÉPENSÉ</th>
                  <th style={{ padding: '0.875rem 1.25rem', fontSize: '0.8rem', fontWeight: 700, color: 'var(--color-text-muted)' }}>NOTES</th>
                </tr>
              </thead>
              <tbody>
                {customers.map((c) => (
                  <tr key={c.id} style={{ borderBottom: '1px solid var(--color-border-subtle)' }}>
                    <td style={{ padding: '1rem 1.25rem' }}>
                      <div style={{ fontWeight: 700, fontSize: '0.9rem' }}>{c.fullName}</div>
                    </td>
                    <td style={{ padding: '1rem 1.25rem', fontSize: '0.875rem' }}>
                      <span style={{ color: '#25D366', fontWeight: 600 }}>{c.phone}</span>
                    </td>
                    <td style={{ padding: '1rem 1.25rem', fontSize: '0.85rem', color: 'var(--color-text-muted)' }}>
                      {c.email || '—'}
                    </td>
                    <td style={{ padding: '1rem 1.25rem', fontWeight: 700 }}>
                      <span className="badge badge-neutral">{c.totalAppointments} RDV</span>
                    </td>
                    <td style={{ padding: '1rem 1.25rem', fontWeight: 700, color: 'var(--color-gold)' }}>
                      {c.totalSpentMad} DH
                    </td>
                    <td style={{ padding: '1rem 1.25rem', fontSize: '0.8rem', color: 'var(--color-text-muted)' }}>
                      {c.notes || '—'}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          {/* Modal */}
          {showModal && (
            <div style={{ position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, backgroundColor: 'rgba(0,0,0,0.5)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 100 }}>
              <div className="card" style={{ maxWidth: '440px', width: '100%', padding: '2rem' }}>
                <h3 style={{ fontSize: '1.25rem', fontWeight: 800, marginBottom: '1.25rem' }}>Nouveau client</h3>
                <form onSubmit={handleCreate}>
                  <div className="form-group">
                    <label className="form-label">Nom complet</label>
                    <input type="text" required className="form-input" value={formData.fullName} onChange={(e) => setFormData({ ...formData, fullName: e.target.value })} />
                  </div>
                  <div className="form-group">
                    <label className="form-label">Numéro de téléphone / WhatsApp</label>
                    <input type="tel" required className="form-input" placeholder="+212612345678" value={formData.phone} onChange={(e) => setFormData({ ...formData, phone: e.target.value, whatsappNumber: e.target.value })} />
                  </div>
                  <div className="form-group">
                    <label className="form-label">Email (Optionnel)</label>
                    <input type="email" className="form-input" value={formData.email} onChange={(e) => setFormData({ ...formData, email: e.target.value })} />
                  </div>
                  <div className="form-group">
                    <label className="form-label">Notes</label>
                    <textarea className="form-textarea" rows={2} value={formData.notes} onChange={(e) => setFormData({ ...formData, notes: e.target.value })} />
                  </div>
                  <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '0.75rem' }}>
                    <button type="button" onClick={() => setShowModal(false)} className="btn btn-secondary">Annuler</button>
                    <button type="submit" className="btn btn-primary">Enregistrer</button>
                  </div>
                </form>
              </div>
            </div>
          )}
        </main>
      </div>
    </div>
  );
};
