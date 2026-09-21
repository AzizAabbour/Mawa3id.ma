import React, { useEffect, useState } from 'react';
import { Sidebar } from '../components/Sidebar';
import { Header } from '../components/Header';
import { useLanguage } from '../contexts/LanguageContext';
import { apiFetch } from '../services/api';
import { Calendar as CalendarIcon, Plus, CheckCircle, XCircle, Clock, Filter } from 'lucide-react';

export const AppointmentsPage: React.FC = () => {
  const { isRTL } = useLanguage();
  const [appointments, setAppointments] = useState<any[]>([]);
  const [services, setServices] = useState<any[]>([]);
  const [employees, setEmployees] = useState<any[]>([]);
  const [customers, setCustomers] = useState<any[]>([]);
  const [showModal, setShowModal] = useState(false);
  const [loading, setLoading] = useState(true);

  // New Appointment Form
  const [formData, setFormData] = useState({
    customerId: '',
    serviceId: '',
    employeeId: '',
    appointmentDate: new Date().toISOString().split('T')[0],
    startTime: '10:00',
    notes: ''
  });

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      const [apptsRes, servRes, empRes, custRes] = await Promise.all([
        apiFetch<any>('/appointments'),
        apiFetch<any[]>('/services'),
        apiFetch<any[]>('/employees'),
        apiFetch<any>('/customers')
      ]);
      setAppointments(apptsRes.content || []);
      setServices(servRes || []);
      setEmployees(empRes || []);
      setCustomers(custRes.content || []);
    } catch (err) {
      console.error('Failed to load appointments', err);
      // Demo fallback data
      setAppointments([
        { id: 1, customerName: 'Yassine El Amrani', customerPhone: '+212612345678', serviceName: 'Coupe & Barbe', servicePriceMad: 80, employeeName: 'Rachid', appointmentDate: '2026-09-22', startTime: '10:00', endTime: '10:30', status: 'CONFIRMED' },
        { id: 2, customerName: 'Sara Bennani', customerPhone: '+212622334455', serviceName: 'Soin Visage Hydratant', servicePriceMad: 250, employeeName: 'Fatima', appointmentDate: '2026-09-22', startTime: '11:30', endTime: '12:15', status: 'CONFIRMED' },
        { id: 3, customerName: 'Mehdi Chraibi', customerPhone: '+212633445566', serviceName: 'Coupe Dégradé', servicePriceMad: 60, employeeName: 'Rachid', appointmentDate: '2026-09-22', startTime: '14:00', endTime: '14:30', status: 'COMPLETED' },
        { id: 4, customerName: 'Khadija Mansouri', customerPhone: '+212644556677', serviceName: 'Coloration', servicePriceMad: 350, employeeName: 'Fatima', appointmentDate: '2026-09-22', startTime: '15:30', endTime: '16:30', status: 'CONFIRMED' }
      ]);
      setServices([
        { id: 1, name: 'Coupe & Barbe', priceMad: 80, durationMinutes: 30 },
        { id: 2, name: 'Soin Visage Hydratant', priceMad: 250, durationMinutes: 45 },
        { id: 3, name: 'Coupe Dégradé', priceMad: 60, durationMinutes: 30 }
      ]);
      setEmployees([
        { id: 1, fullName: 'Rachid (Coiffeur)' },
        { id: 2, fullName: 'Fatima (Esthéticienne)' }
      ]);
      setCustomers([
        { id: 1, fullName: 'Yassine El Amrani' },
        { id: 2, fullName: 'Sara Bennani' }
      ]);
    } finally {
      setLoading(false);
    }
  };

  const handleCreate = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await apiFetch('/appointments', {
        method: 'POST',
        body: JSON.stringify({
          customerId: Number(formData.customerId),
          serviceId: Number(formData.serviceId),
          employeeId: Number(formData.employeeId),
          appointmentDate: formData.appointmentDate,
          startTime: formData.startTime,
          notes: formData.notes
        })
      });
      setShowModal(false);
      loadData();
    } catch (err: any) {
      alert(err.message || 'Erreur lors de la création du rendez-vous');
    }
  };

  const handleStatusUpdate = async (id: number, status: string) => {
    try {
      await apiFetch(`/appointments/${id}/status`, {
        method: 'PATCH',
        body: JSON.stringify({ status })
      });
      loadData();
    } catch (err: any) {
      alert(err.message || 'Erreur lors de la mise à jour');
    }
  };

  return (
    <div style={{ display: 'flex', minHeight: '100vh', backgroundColor: 'var(--color-bg)' }}>
      <Sidebar />

      <div style={{ flex: 1, display: 'flex', flexDirection: 'column' }}>
        <Header 
          title={isRTL ? 'إدارة المواعيد' : 'Gestion des Rendez-vous'} 
          subtitle={isRTL ? 'عرض وتتبع جدول المواعيد وتأكيد الحجوزات' : 'Consultez le planning et mettez à jour les statuts en direct'}
        />

        <main style={{ padding: '2rem', flex: 1 }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem' }}>
            <h2 style={{ fontSize: '1.25rem', fontWeight: 800 }}>
              {isRTL ? 'قائمة المواعيد' : 'Tous les rendez-vous'}
            </h2>

            <button onClick={() => setShowModal(true)} className="btn btn-primary">
              <Plus size={18} />
              <span>{isRTL ? 'موعد جديد' : 'Nouveau rendez-vous'}</span>
            </button>
          </div>

          {/* Appointments Table */}
          <div className="card" style={{ padding: 0, overflow: 'hidden' }}>
            <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: isRTL ? 'right' : 'left' }}>
              <thead>
                <tr style={{ backgroundColor: 'var(--color-surface-subtle)', borderBottom: '1px solid var(--color-border)' }}>
                  <th style={{ padding: '0.875rem 1.25rem', fontSize: '0.8rem', fontWeight: 700, color: 'var(--color-text-muted)' }}>CLIENT</th>
                  <th style={{ padding: '0.875rem 1.25rem', fontSize: '0.8rem', fontWeight: 700, color: 'var(--color-text-muted)' }}>PRESTATION</th>
                  <th style={{ padding: '0.875rem 1.25rem', fontSize: '0.8rem', fontWeight: 700, color: 'var(--color-text-muted)' }}>DATE & HEURE</th>
                  <th style={{ padding: '0.875rem 1.25rem', fontSize: '0.8rem', fontWeight: 700, color: 'var(--color-text-muted)' }}>COLLABORATEUR</th>
                  <th style={{ padding: '0.875rem 1.25rem', fontSize: '0.8rem', fontWeight: 700, color: 'var(--color-text-muted)' }}>PRIX</th>
                  <th style={{ padding: '0.875rem 1.25rem', fontSize: '0.8rem', fontWeight: 700, color: 'var(--color-text-muted)' }}>STATUT</th>
                  <th style={{ padding: '0.875rem 1.25rem', fontSize: '0.8rem', fontWeight: 700, color: 'var(--color-text-muted)' }}>ACTIONS</th>
                </tr>
              </thead>
              <tbody>
                {appointments.map((app) => (
                  <tr key={app.id} style={{ borderBottom: '1px solid var(--color-border-subtle)' }}>
                    <td style={{ padding: '1rem 1.25rem' }}>
                      <div style={{ fontWeight: 700, fontSize: '0.9rem' }}>{app.customerName}</div>
                      <div style={{ fontSize: '0.75rem', color: 'var(--color-text-muted)' }}>{app.customerPhone}</div>
                    </td>
                    <td style={{ padding: '1rem 1.25rem', fontWeight: 600, fontSize: '0.875rem' }}>
                      {app.serviceName}
                    </td>
                    <td style={{ padding: '1rem 1.25rem' }}>
                      <div style={{ fontWeight: 700, fontSize: '0.875rem' }}>{app.appointmentDate}</div>
                      <div style={{ fontSize: '0.8rem', color: 'var(--color-primary)' }}>{app.startTime} - {app.endTime}</div>
                    </td>
                    <td style={{ padding: '1rem 1.25rem', fontSize: '0.875rem', color: 'var(--color-text-muted)' }}>
                      {app.employeeName}
                    </td>
                    <td style={{ padding: '1rem 1.25rem', fontWeight: 700, color: 'var(--color-gold)', fontSize: '0.9rem' }}>
                      {app.servicePriceMad} DH
                    </td>
                    <td style={{ padding: '1rem 1.25rem' }}>
                      <span className={`badge ${
                        app.status === 'COMPLETED' ? 'badge-success' :
                        app.status === 'CONFIRMED' ? 'badge-info' :
                        app.status === 'CANCELLED' ? 'badge-danger' : 'badge-warning'
                      }`}>
                        {app.status}
                      </span>
                    </td>
                    <td style={{ padding: '1rem 1.25rem' }}>
                      <div style={{ display: 'flex', gap: '0.5rem' }}>
                        {app.status === 'CONFIRMED' && (
                          <>
                            <button
                              onClick={() => handleStatusUpdate(app.id, 'COMPLETED')}
                              className="btn btn-secondary"
                              style={{ padding: '0.3rem 0.5rem', fontSize: '0.75rem', color: 'var(--color-success)' }}
                              title="Marquer comme terminé"
                            >
                              <CheckCircle size={14} />
                            </button>
                            <button
                              onClick={() => handleStatusUpdate(app.id, 'NO_SHOW')}
                              className="btn btn-secondary"
                              style={{ padding: '0.3rem 0.5rem', fontSize: '0.75rem', color: 'var(--color-warning)' }}
                              title="Marquer absent (No-show)"
                            >
                              <Clock size={14} />
                            </button>
                            <button
                              onClick={() => handleStatusUpdate(app.id, 'CANCELLED')}
                              className="btn btn-secondary"
                              style={{ padding: '0.3rem 0.5rem', fontSize: '0.75rem', color: 'var(--color-danger)' }}
                              title="Annuler"
                            >
                              <XCircle size={14} />
                            </button>
                          </>
                        )}
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          {/* Create Appointment Modal */}
          {showModal && (
            <div style={{
              position: 'fixed',
              top: 0,
              left: 0,
              right: 0,
              bottom: 0,
              backgroundColor: 'rgba(0,0,0,0.5)',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              zIndex: 100
            }}>
              <div className="card" style={{ maxWidth: '480px', width: '100%', padding: '2rem' }}>
                <h3 style={{ fontSize: '1.25rem', fontWeight: 800, marginBottom: '1.25rem' }}>
                  {isRTL ? 'إضافة موعد جديد' : 'Créer un rendez-vous'}
                </h3>

                <form onSubmit={handleCreate}>
                  <div className="form-group">
                    <label className="form-label">{isRTL ? 'الزبون' : 'Client'}</label>
                    <select
                      required
                      className="form-select"
                      value={formData.customerId}
                      onChange={(e) => setFormData({ ...formData, customerId: e.target.value })}
                    >
                      <option value="">Sélectionner un client</option>
                      {customers.map(c => <option key={c.id} value={c.id}>{c.fullName}</option>)}
                    </select>
                  </div>

                  <div className="form-group">
                    <label className="form-label">{isRTL ? 'الخدمة' : 'Prestation'}</label>
                    <select
                      required
                      className="form-select"
                      value={formData.serviceId}
                      onChange={(e) => setFormData({ ...formData, serviceId: e.target.value })}
                    >
                      <option value="">Sélectionner une prestation</option>
                      {services.map(s => <option key={s.id} value={s.id}>{s.name} ({s.priceMad} DH)</option>)}
                    </select>
                  </div>

                  <div className="form-group">
                    <label className="form-label">{isRTL ? 'الموظف / الحلاق' : 'Collaborateur'}</label>
                    <select
                      required
                      className="form-select"
                      value={formData.employeeId}
                      onChange={(e) => setFormData({ ...formData, employeeId: e.target.value })}
                    >
                      <option value="">Sélectionner un collaborateur</option>
                      {employees.map(emp => <option key={emp.id} value={emp.id}>{emp.fullName || emp.title}</option>)}
                    </select>
                  </div>

                  <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                    <div className="form-group">
                      <label className="form-label">{isRTL ? 'التاريخ' : 'Date'}</label>
                      <input
                        type="date"
                        required
                        className="form-input"
                        value={formData.appointmentDate}
                        onChange={(e) => setFormData({ ...formData, appointmentDate: e.target.value })}
                      />
                    </div>
                    <div className="form-group">
                      <label className="form-label">{isRTL ? 'التوقيت' : 'Heure'}</label>
                      <input
                        type="time"
                        required
                        className="form-input"
                        value={formData.startTime}
                        onChange={(e) => setFormData({ ...formData, startTime: e.target.value })}
                      />
                    </div>
                  </div>

                  <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '0.75rem', marginTop: '1.5rem' }}>
                    <button type="button" onClick={() => setShowModal(false)} className="btn btn-secondary">
                      Annuler
                    </button>
                    <button type="submit" className="btn btn-primary">
                      Confirmer & Envoyer WhatsApp
                    </button>
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
