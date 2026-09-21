import React, { useEffect, useState } from 'react';
import { Sidebar } from '../components/Sidebar';
import { Header } from '../components/Header';
import { useLanguage } from '../contexts/LanguageContext';
import { apiFetch } from '../services/api';
import { Scissors, Plus, Clock, Tag, Trash2, Edit } from 'lucide-react';

export const ServicesPage: React.FC = () => {
  const { isRTL } = useLanguage();
  const [services, setServices] = useState<any[]>([]);
  const [showModal, setShowModal] = useState(false);
  const [formData, setFormData] = useState({ name: '', description: '', durationMinutes: 30, priceMad: 80, color: '#1B5E3C' });

  useEffect(() => {
    loadServices();
  }, []);

  const loadServices = async () => {
    try {
      const res = await apiFetch<any[]>('/services');
      setServices(res || []);
    } catch (err) {
      // Demo fallback data
      setServices([
        { id: 1, name: 'Coupe Homme & Coiffage', description: 'Shampoing, coupe ciseaux/tondeuse et coiffage pro', durationMinutes: 30, priceMad: 60, color: '#1B5E3C', active: true },
        { id: 2, name: 'Taille & Soin de Barbe', description: 'Taille précise, serviette chaude et huile d’argan marocaine', durationMinutes: 25, priceMad: 40, color: '#C5982E', active: true },
        { id: 3, name: 'Coupe + Barbe VIP', description: 'Formule complète premium avec masque noir détox', durationMinutes: 50, priceMad: 90, color: '#C75B39', active: true },
        { id: 4, name: 'Soin Visage Hydratant', description: 'Gommage naturel et vapeur ozonée', durationMinutes: 45, priceMad: 250, color: '#1E8A56', active: true }
      ]);
    }
  };

  const handleCreate = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await apiFetch('/services', { method: 'POST', body: JSON.stringify(formData) });
      setShowModal(false);
      setFormData({ name: '', description: '', durationMinutes: 30, priceMad: 80, color: '#1B5E3C' });
      loadServices();
    } catch (err: any) {
      alert(err.message || 'Erreur lors de la création de la prestation');
    }
  };

  return (
    <div style={{ display: 'flex', minHeight: '100vh', backgroundColor: 'var(--color-bg)' }}>
      <Sidebar />

      <div style={{ flex: 1, display: 'flex', flexDirection: 'column' }}>
        <Header 
          title={isRTL ? 'الخدمات والأسعار' : 'Catalogue des Prestations'} 
          subtitle={isRTL ? 'حدد مدة كل خدمة وأسعارها بالدرهم المغربي' : 'Définissez la durée et les tarifs de vos prestations en MAD'}
        />

        <main style={{ padding: '2rem', flex: 1 }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem' }}>
            <h2 style={{ fontSize: '1.25rem', fontWeight: 800 }}>
              {isRTL ? 'قائمة الخدمات' : 'Vos prestations'}
            </h2>
            <button onClick={() => setShowModal(true)} className="btn btn-primary">
              <Plus size={18} />
              <span>{isRTL ? 'إضافة خدمة جديدة' : 'Nouvelle prestation'}</span>
            </button>
          </div>

          <div className="grid-cols-3">
            {services.map((service) => (
              <div key={service.id} className="card" style={{ display: 'flex', flexDirection: 'column', justifyContent: 'space-between', borderTop: `4px solid ${service.color || 'var(--color-primary)'}` }}>
                <div>
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '0.75rem' }}>
                    <h3 style={{ fontSize: '1.15rem', fontWeight: 700 }}>{service.name}</h3>
                    <span style={{ fontSize: '1.25rem', fontWeight: 800, color: 'var(--color-gold)' }}>
                      {service.priceMad} DH
                    </span>
                  </div>

                  <p style={{ fontSize: '0.85rem', color: 'var(--color-text-muted)', marginBottom: '1.25rem', lineHeight: 1.5 }}>
                    {service.description || 'Prestation professionnelle'}
                  </p>
                </div>

                <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', borderTop: '1px solid var(--color-border-subtle)', paddingTop: '0.875rem' }}>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '0.35rem', fontSize: '0.85rem', fontWeight: 600, color: 'var(--color-text-muted)' }}>
                    <Clock size={16} />
                    <span>{service.durationMinutes} min</span>
                  </div>

                  <span className="badge badge-success">Actif</span>
                </div>
              </div>
            ))}
          </div>

          {/* Modal */}
          {showModal && (
            <div style={{ position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, backgroundColor: 'rgba(0,0,0,0.5)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 100 }}>
              <div className="card" style={{ maxWidth: '440px', width: '100%', padding: '2rem' }}>
                <h3 style={{ fontSize: '1.25rem', fontWeight: 800, marginBottom: '1.25rem' }}>Nouvelle prestation</h3>
                <form onSubmit={handleCreate}>
                  <div className="form-group">
                    <label className="form-label">Nom du service</label>
                    <input type="text" required className="form-input" placeholder="ex: Coupe & Barbe" value={formData.name} onChange={(e) => setFormData({ ...formData, name: e.target.value })} />
                  </div>
                  <div className="form-group">
                    <label className="form-label">Description</label>
                    <textarea className="form-textarea" rows={2} placeholder="Description courte..." value={formData.description} onChange={(e) => setFormData({ ...formData, description: e.target.value })} />
                  </div>
                  <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                    <div className="form-group">
                      <label className="form-label">Durée (minutes)</label>
                      <input type="number" required min={5} step={5} className="form-input" value={formData.durationMinutes} onChange={(e) => setFormData({ ...formData, durationMinutes: Number(e.target.value) })} />
                    </div>
                    <div className="form-group">
                      <label className="form-label">Prix (DH)</label>
                      <input type="number" required min={0} className="form-input" value={formData.priceMad} onChange={(e) => setFormData({ ...formData, priceMad: Number(e.target.value) })} />
                    </div>
                  </div>
                  <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '0.75rem', marginTop: '1rem' }}>
                    <button type="button" onClick={() => setShowModal(false)} className="btn btn-secondary">Annuler</button>
                    <button type="submit" className="btn btn-primary">Ajouter</button>
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
