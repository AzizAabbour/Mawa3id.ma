import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { apiFetch } from '../services/api';
import { 
  Calendar as CalendarIcon, 
  Clock, 
  User, 
  Phone, 
  MapPin, 
  CheckCircle2, 
  Scissors, 
  ArrowRight, 
  Check,
  ChevronLeft,
  ChevronRight,
  Globe
} from 'lucide-react';

export const PublicBookingPage: React.FC = () => {
  const { slug } = useParams<{ slug: string }>();
  const [business, setBusiness] = useState<any>(null);
  const [loading, setLoading] = useState(true);
  const [step, setStep] = useState(1); // 1: Service, 2: Employee/Staff, 3: Date/Time, 4: Customer Details, 5: Confirmed Success
  const [selectedService, setSelectedService] = useState<any>(null);
  const [selectedEmployee, setSelectedEmployee] = useState<any>(null);
  const [selectedDate, setSelectedDate] = useState<string>(new Date().toISOString().split('T')[0]);
  const [selectedSlot, setSelectedSlot] = useState<string>('10:00');
  const [slots, setSlots] = useState<any[]>([]);
  const [lang, setLang] = useState<'fr' | 'ar'>('fr');

  // Customer Form
  const [customerName, setCustomerName] = useState('');
  const [customerPhone, setCustomerPhone] = useState('');
  const [customerEmail, setCustomerEmail] = useState('');
  const [notes, setNotes] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const [confirmedAppt, setConfirmedAppt] = useState<any>(null);

  const isRTL = lang === 'ar';

  useEffect(() => {
    loadBusiness();
  }, [slug]);

  useEffect(() => {
    if (selectedService && selectedDate) {
      loadSlots();
    }
  }, [selectedService, selectedDate, selectedEmployee]);

  const loadBusiness = async () => {
    try {
      const data = await apiFetch<any>(`/booking/${slug || 'demo'}`);
      setBusiness(data);
    } catch {
      // Demo fallback business
      setBusiness({
        name: 'Salon Riad Beauty',
        slug: slug || 'salon-riad-beauty',
        description: 'Salon de coiffure & esthétique haut de gamme au cœur de Casablanca.',
        city: 'Casablanca',
        address: '14 Bd d’Anfa, Casablanca, Maroc',
        phone: '+212 5 22 12 34 56',
        whatsappNumber: '+212 6 12 34 56 78',
        services: [
          { id: 1, name: 'Coupe Homme & Coiffage', priceMad: 60, durationMinutes: 30, description: 'Shampoing et coupe ciseaux/tondeuse' },
          { id: 2, name: 'Taille & Soin de Barbe', priceMad: 40, durationMinutes: 25, description: 'Serviette chaude et huile d’argan' },
          { id: 3, name: 'Formule VIP Coupe + Barbe', priceMad: 90, durationMinutes: 50, description: 'Coupe complète avec masque noir détox' },
          { id: 4, name: 'Soin Visage Hydratant', priceMad: 250, durationMinutes: 45, description: 'Nettoyage en profondeur' }
        ]
      });
    } finally {
      setLoading(false);
    }
  };

  const loadSlots = async () => {
    try {
      const empParam = selectedEmployee ? `&employeeId=${selectedEmployee.id}` : '';
      const slotsData = await apiFetch<any[]>(`/booking/${slug || 'demo'}/slots?serviceId=${selectedService.id}&date=${selectedDate}${empParam}`);
      setSlots(slotsData || []);
    } catch {
      // Fallback slots
      setSlots([
        { startTime: '09:30', available: true },
        { startTime: '10:00', available: true },
        { startTime: '10:30', available: true },
        { startTime: '11:00', available: true },
        { startTime: '11:30', available: true },
        { startTime: '14:30', available: true },
        { startTime: '15:00', available: true },
        { startTime: '15:30', available: true },
        { startTime: '16:00', available: true },
        { startTime: '16:30', available: true },
        { startTime: '17:00', available: true },
        { startTime: '17:30', available: true }
      ]);
    }
  };

  const handleBooking = async (e: React.FormEvent) => {
    e.preventDefault();
    setSubmitting(true);
    try {
      const res = await apiFetch<any>(`/booking/${slug || 'demo'}`, {
        method: 'POST',
        body: JSON.stringify({
          serviceId: selectedService.id,
          employeeId: selectedEmployee?.id,
          appointmentDate: selectedDate,
          startTime: selectedSlot,
          customerName,
          customerPhone,
          customerEmail,
          notes
        })
      });
      setConfirmedAppt(res);
      setStep(5);
    } catch (err: any) {
      alert(err.message || 'Erreur lors de la réservation');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="zellige-bg" style={{ minHeight: '100vh', display: 'flex', flexDirection: 'column', direction: isRTL ? 'rtl' : 'ltr' }}>
      {/* Top Header */}
      <header style={{
        backgroundColor: 'var(--color-surface)',
        borderBottom: '1px solid var(--color-border)',
        padding: '0.875rem 0'
      }}>
        <div className="container" style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '0.625rem' }}>
            <div style={{ width: '32px', height: '32px', borderRadius: '8px', backgroundColor: 'var(--color-primary)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
              <CalendarIcon size={16} color="#C5982E" />
            </div>
            <span style={{ fontWeight: 800, fontSize: '1.15rem', color: 'var(--color-primary)' }}>
              Mawa3id<span style={{ color: 'var(--color-gold)' }}>.ma</span>
            </span>
          </div>

          <button
            onClick={() => setLang(lang === 'fr' ? 'ar' : 'fr')}
            className="btn btn-secondary"
            style={{ padding: '0.35rem 0.75rem', fontSize: '0.8rem' }}
          >
            <Globe size={14} />
            <span>{lang === 'fr' ? 'العربية' : 'Français'}</span>
          </button>
        </div>
      </header>

      {/* Main Container */}
      <main style={{ flex: 1, padding: '2.5rem 1.25rem' }}>
        <div style={{ maxWidth: '680px', margin: '0 auto' }}>
          {/* Business Banner Card */}
          <div className="card" style={{ marginBottom: '1.5rem', textAlign: 'center', borderTop: '4px solid var(--color-primary)' }}>
            <h1 style={{ fontSize: '1.6rem', fontWeight: 800, color: 'var(--color-text-main)' }}>
              {business?.name || 'Salon Riad Beauty'}
            </h1>
            <p style={{ fontSize: '0.875rem', color: 'var(--color-text-muted)', marginTop: '0.25rem' }}>
              {business?.description}
            </p>
            <div style={{ display: 'flex', justifyContent: 'center', gap: '1rem', marginTop: '0.75rem', fontSize: '0.8rem', color: 'var(--color-text-muted)' }}>
              <span style={{ display: 'flex', alignItems: 'center', gap: '0.25rem' }}>
                <MapPin size={14} /> {business?.address}
              </span>
            </div>
          </div>

          {/* Stepper Wizard Progress */}
          {step < 5 && (
            <div style={{ display: 'flex', justifyContent: 'center', gap: '0.5rem', marginBottom: '1.5rem' }}>
              {[1, 2, 3, 4].map((s) => (
                <div
                  key={s}
                  style={{
                    flex: 1,
                    height: '6px',
                    borderRadius: '3px',
                    backgroundColor: step >= s ? 'var(--color-primary)' : 'var(--color-border)'
                  }}
                />
              ))}
            </div>
          )}

          {/* Step 1: Select Service */}
          {step === 1 && (
            <div className="card">
              <h2 style={{ fontSize: '1.25rem', fontWeight: 800, marginBottom: '1.25rem' }}>
                {isRTL ? '1. اختر الخدمة المطلوبة' : '1. Choisissez une prestation'}
              </h2>

              <div style={{ display: 'flex', flexDirection: 'column', gap: '0.75rem' }}>
                {business?.services?.map((serv: any) => (
                  <div
                    key={serv.id}
                    onClick={() => {
                      setSelectedService(serv);
                      setStep(2);
                    }}
                    style={{
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'space-between',
                      padding: '1rem 1.25rem',
                      border: selectedService?.id === serv.id ? '2px solid var(--color-primary)' : '1px solid var(--color-border)',
                      borderRadius: 'var(--radius-md)',
                      cursor: 'pointer',
                      transition: 'all 0.15s ease',
                      backgroundColor: selectedService?.id === serv.id ? 'var(--color-primary-light)' : 'var(--color-surface)'
                    }}
                  >
                    <div>
                      <div style={{ fontWeight: 700, fontSize: '0.95rem' }}>{serv.name}</div>
                      <div style={{ fontSize: '0.8rem', color: 'var(--color-text-muted)' }}>{serv.durationMinutes} min • {serv.description}</div>
                    </div>

                    <div style={{ fontWeight: 800, fontSize: '1.1rem', color: 'var(--color-gold)' }}>
                      {serv.priceMad} DH
                    </div>
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* Step 2: Select Date & Time */}
          {step === 2 && (
            <div className="card">
              <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '1.25rem' }}>
                <h2 style={{ fontSize: '1.25rem', fontWeight: 800 }}>
                  {isRTL ? '2. اختر التاريخ والتوقيت' : '2. Date & Créneau horaire'}
                </h2>
                <button onClick={() => setStep(1)} className="btn btn-secondary" style={{ padding: '0.3rem 0.6rem', fontSize: '0.75rem' }}>
                  Modifier prestation
                </button>
              </div>

              <div className="form-group">
                <label className="form-label">{isRTL ? 'تاريخ الموعد' : 'Date de réservation'}</label>
                <input
                  type="date"
                  className="form-input"
                  min={new Date().toISOString().split('T')[0]}
                  value={selectedDate}
                  onChange={(e) => setSelectedDate(e.target.value)}
                />
              </div>

              <label className="form-label" style={{ marginTop: '1rem' }}>
                {isRTL ? 'Créneaux disponibles' : 'Créneaux horaires disponibles'}
              </label>

              <div style={{ display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: '0.5rem', marginTop: '0.5rem' }}>
                {slots.map((slot, idx) => (
                  <button
                    key={idx}
                    type="button"
                    onClick={() => {
                      setSelectedSlot(slot.startTime);
                      setStep(3);
                    }}
                    style={{
                      padding: '0.625rem',
                      borderRadius: 'var(--radius-md)',
                      border: selectedSlot === slot.startTime ? '2px solid var(--color-primary)' : '1px solid var(--color-border)',
                      backgroundColor: selectedSlot === slot.startTime ? 'var(--color-primary)' : 'var(--color-surface)',
                      color: selectedSlot === slot.startTime ? '#FFFFFF' : 'var(--color-text-main)',
                      fontWeight: 700,
                      cursor: 'pointer'
                    }}
                  >
                    {slot.startTime}
                  </button>
                ))}
              </div>
            </div>
          )}

          {/* Step 3: Customer Details */}
          {step === 3 && (
            <div className="card">
              <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '1.25rem' }}>
                <h2 style={{ fontSize: '1.25rem', fontWeight: 800 }}>
                  {isRTL ? '3. معلوماتك لتأكيد الحجز' : '3. Vos coordonnées'}
                </h2>
                <button onClick={() => setStep(2)} className="btn btn-secondary" style={{ padding: '0.3rem 0.6rem', fontSize: '0.75rem' }}>
                  Modifier heure
                </button>
              </div>

              {/* Booking Summary Box */}
              <div style={{
                backgroundColor: 'var(--color-surface-subtle)',
                padding: '0.875rem 1rem',
                borderRadius: 'var(--radius-md)',
                marginBottom: '1.5rem',
                border: '1px solid var(--color-border)'
              }}>
                <div style={{ fontWeight: 700, color: 'var(--color-primary)' }}>{selectedService?.name}</div>
                <div style={{ fontSize: '0.85rem', color: 'var(--color-text-muted)', marginTop: '0.2rem' }}>
                  📅 {selectedDate} à ⏰ {selectedSlot} • <strong>{selectedService?.priceMad} DH</strong>
                </div>
              </div>

              <form onSubmit={handleBooking}>
                <div className="form-group">
                  <label className="form-label">{isRTL ? 'الاسم الكامل' : 'Nom complet'}</label>
                  <input
                    type="text"
                    required
                    className="form-input"
                    placeholder="ex: Yassine Bennani"
                    value={customerName}
                    onChange={(e) => setCustomerName(e.target.value)}
                  />
                </div>

                <div className="form-group">
                  <label className="form-label">{isRTL ? 'رقم الهاتف / الواتساب (لتأكيد الموعد)' : 'Numéro de WhatsApp (pour le rappel)'}</label>
                  <input
                    type="tel"
                    required
                    className="form-input"
                    placeholder="+212 6 12 34 56 78"
                    value={customerPhone}
                    onChange={(e) => setCustomerPhone(e.target.value)}
                  />
                </div>

                <div className="form-group">
                  <label className="form-label">{isRTL ? 'البريد الإلكتروني (اختياري)' : 'Adresse Email (Optionnel)'}</label>
                  <input
                    type="email"
                    className="form-input"
                    placeholder="nom@gmail.com"
                    value={customerEmail}
                    onChange={(e) => setCustomerEmail(e.target.value)}
                  />
                </div>

                <div className="form-group">
                  <label className="form-label">{isRTL ? 'ملاحظة خاصة' : 'Remarque spéciale'}</label>
                  <textarea
                    className="form-textarea"
                    rows={2}
                    placeholder="ex: Première visite, demande particulière..."
                    value={notes}
                    onChange={(e) => setNotes(e.target.value)}
                  />
                </div>

                <button
                  type="submit"
                  disabled={submitting}
                  className="btn btn-primary"
                  style={{ width: '100%', padding: '0.875rem', fontSize: '1rem', marginTop: '1rem' }}
                >
                  {submitting ? 'Validation en cours...' : (
                    <>
                      <span>{isRTL ? 'تأكيد الحجز فوراً' : 'Confirmer mon rendez-vous'}</span>
                      <CheckCircle2 size={18} />
                    </>
                  )}
                </button>
              </form>
            </div>
          )}

          {/* Step 5: Success Confirmation Screen */}
          {step === 5 && (
            <div className="card" style={{ textAlign: 'center', padding: '3rem 2rem' }}>
              <div style={{
                width: '64px',
                height: '64px',
                borderRadius: '50%',
                backgroundColor: 'var(--color-success-bg)',
                color: 'var(--color-success)',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                margin: '0 auto 1.5rem auto'
              }}>
                <CheckCircle2 size={36} />
              </div>

              <h2 style={{ fontSize: '1.5rem', fontWeight: 800, color: 'var(--color-text-main)', marginBottom: '0.5rem' }}>
                {isRTL ? 'تم تأكيد موعدك بنجاح !' : 'Rendez-vous confirmé avec succès !'}
              </h2>
              <p style={{ fontSize: '0.95rem', color: 'var(--color-text-muted)', marginBottom: '1.75rem' }}>
                {isRTL ? 'تم إرسال رسالة تأكيد إلى رقم الواتساب الخاص بك.' : 'Un rappel automatique vous a été envoyé directement sur WhatsApp.'}
              </p>

              <div style={{
                backgroundColor: 'var(--color-surface-subtle)',
                padding: '1.25rem',
                borderRadius: 'var(--radius-md)',
                maxWidth: '400px',
                margin: '0 auto 2rem auto',
                textAlign: 'left'
              }}>
                <div style={{ fontWeight: 700, fontSize: '0.95rem' }}>{selectedService?.name}</div>
                <div style={{ fontSize: '0.85rem', color: 'var(--color-text-muted)', margin: '0.35rem 0' }}>
                  📅 <strong>{selectedDate}</strong> à <strong>{selectedSlot}</strong>
                </div>
                <div style={{ fontSize: '0.85rem', color: 'var(--color-text-muted)' }}>
                  📍 {business?.address}
                </div>
                <div style={{ fontSize: '1rem', fontWeight: 800, color: 'var(--color-gold)', marginTop: '0.5rem' }}>
                  {selectedService?.priceMad} DH
                </div>
              </div>

              <button
                onClick={() => {
                  setStep(1);
                  setSelectedService(null);
                }}
                className="btn btn-secondary"
              >
                Prendre un autre rendez-vous
              </button>
            </div>
          )}
        </div>
      </main>
    </div>
  );
};
