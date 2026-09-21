import React, { useEffect, useState } from 'react';
import { Sidebar } from '../components/Sidebar';
import { Header } from '../components/Header';
import { useLanguage } from '../contexts/LanguageContext';
import { apiFetch } from '../services/api';
import { Settings, Save, Store, Bell, Clock } from 'lucide-react';

export const SettingsPage: React.FC = () => {
  const { isRTL } = useLanguage();
  const [business, setBusiness] = useState<any>({ name: '', phone: '', whatsappNumber: '', email: '', address: '', city: 'Casablanca' });
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    loadSettings();
  }, []);

  const loadSettings = async () => {
    try {
      const data = await apiFetch<any>('/business');
      if (data) setBusiness(data);
    } catch {
      setBusiness({
        name: 'Salon Riad Beauty',
        phone: '+212 5 22 12 34 56',
        whatsappNumber: '+212 6 12 34 56 78',
        email: 'contact@riadbeauty.ma',
        address: '14 Bd d’Anfa, Casablanca',
        city: 'Casablanca'
      });
    }
  };

  const handleSave = async (e: React.FormEvent) => {
    e.preventDefault();
    setSaving(true);
    try {
      await apiFetch('/business', {
        method: 'PUT',
        body: JSON.stringify(business)
      });
      alert('Informations mises à jour avec succès !');
    } catch (err: any) {
      alert(err.message || 'Erreur lors de la mise à jour');
    } finally {
      setSaving(false);
    }
  };

  return (
    <div style={{ display: 'flex', minHeight: '100vh', backgroundColor: 'var(--color-bg)' }}>
      <Sidebar />

      <div style={{ flex: 1, display: 'flex', flexDirection: 'column' }}>
        <Header 
          title={isRTL ? 'إعدادات الحساب والمحل' : "Paramètres de l'établissement"} 
          subtitle={isRTL ? 'تعديل العنوان، أرقام التواصل وبيانات المحل' : 'Coordonnées, adresse et informations visibles par les clients'}
        />

        <main style={{ padding: '2rem', flex: 1 }}>
          <div className="card" style={{ maxWidth: '640px' }}>
            <h3 style={{ fontSize: '1.2rem', fontWeight: 800, marginBottom: '1.5rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
              <Store size={20} color="var(--color-primary)" />
              <span>Profil de l'entreprise</span>
            </h3>

            <form onSubmit={handleSave}>
              <div className="form-group">
                <label className="form-label">Nom de l'établissement</label>
                <input
                  type="text"
                  required
                  className="form-input"
                  value={business.name || ''}
                  onChange={(e) => setBusiness({ ...business, name: e.target.value })}
                />
              </div>

              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                <div className="form-group">
                  <label className="form-label">Téléphone Fixe / Mobile</label>
                  <input
                    type="tel"
                    className="form-input"
                    value={business.phone || ''}
                    onChange={(e) => setBusiness({ ...business, phone: e.target.value })}
                  />
                </div>
                <div className="form-group">
                  <label className="form-label">Numéro WhatsApp (Notifications)</label>
                  <input
                    type="tel"
                    className="form-input"
                    value={business.whatsappNumber || ''}
                    onChange={(e) => setBusiness({ ...business, whatsappNumber: e.target.value })}
                  />
                </div>
              </div>

              <div className="form-group">
                <label className="form-label">Adresse Email</label>
                <input
                  type="email"
                  className="form-input"
                  value={business.email || ''}
                  onChange={(e) => setBusiness({ ...business, email: e.target.value })}
                />
              </div>

              <div className="form-group">
                <label className="form-label">Adresse Complète</label>
                <input
                  type="text"
                  className="form-input"
                  value={business.address || ''}
                  onChange={(e) => setBusiness({ ...business, address: e.target.value })}
                />
              </div>

              <div className="form-group">
                <label className="form-label">Ville</label>
                <select
                  className="form-select"
                  value={business.city || 'Casablanca'}
                  onChange={(e) => setBusiness({ ...business, city: e.target.value })}
                >
                  <option value="Casablanca">Casablanca</option>
                  <option value="Rabat">Rabat</option>
                  <option value="Marrakech">Marrakech</option>
                  <option value="Tanger">Tanger</option>
                  <option value="Fès">Fès</option>
                  <option value="Agadir">Agadir</option>
                </select>
              </div>

              <div style={{ display: 'flex', justifyContent: 'flex-end', marginTop: '1.5rem' }}>
                <button type="submit" disabled={saving} className="btn btn-primary">
                  <Save size={16} />
                  <span>{saving ? 'Enregistrement...' : 'Enregistrer les modifications'}</span>
                </button>
              </div>
            </form>
          </div>
        </main>
      </div>
    </div>
  );
};
