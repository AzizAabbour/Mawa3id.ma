import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { useLanguage } from '../contexts/LanguageContext';
import { apiFetch } from '../services/api';
import { Calendar, ArrowRight } from 'lucide-react';

export const RegisterPage: React.FC = () => {
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    phone: '',
    password: '',
    businessName: '',
    businessCategory: 'HAIR_SALON',
    city: 'Casablanca'
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const { login } = useAuth();
  const { isRTL } = useLanguage();
  const navigate = useNavigate();

  const moroccanCities = [
    'Casablanca', 'Rabat', 'Marrakech', 'Tanger', 'Fès',
    'Agadir', 'Meknès', 'Oujda', 'Kenitra', 'Tetouan', 'El Jadida', 'Mohammedia'
  ];

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const data = await apiFetch<any>('/auth/register', {
        method: 'POST',
        body: JSON.stringify(formData)
      });

      login(data.accessToken, data.refreshToken, {
        userId: data.userId,
        email: data.email,
        firstName: data.firstName,
        lastName: data.lastName,
        role: data.role,
        businessId: data.businessId,
        businessName: data.businessName,
        businessSlug: data.businessSlug
      });

      navigate('/dashboard');
    } catch (err: any) {
      setError(err.message || "Erreur lors de l'inscription");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="zellige-bg" style={{ minHeight: '100vh', display: 'flex', alignItems: 'center', justifyContent: 'center', padding: '2rem 1.5rem' }}>
      <div className="card" style={{ maxWidth: '520px', width: '100%', padding: '2.5rem', boxShadow: 'var(--shadow-xl)', borderTop: '4px solid var(--color-gold)' }}>
        <div style={{ textAlign: 'center', marginBottom: '1.75rem' }}>
          <Link to="/" style={{ display: 'inline-flex', alignItems: 'center', gap: '0.5rem', marginBottom: '0.75rem' }}>
            <div style={{ width: '36px', height: '36px', borderRadius: '10px', backgroundColor: 'var(--color-primary)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
              <Calendar size={20} color="#C5982E" />
            </div>
            <span style={{ fontWeight: 800, fontSize: '1.35rem', color: 'var(--color-primary)' }}>
              Mawa3id<span style={{ color: 'var(--color-gold)' }}>.ma</span>
            </span>
          </Link>
          <h2 style={{ fontSize: '1.35rem', fontWeight: 800, color: 'var(--color-text-main)' }}>
            {isRTL ? 'إنشاء حساب مقاولة جديدة' : 'Créer votre compte professionnel'}
          </h2>
          <p style={{ fontSize: '0.85rem', color: 'var(--color-text-muted)' }}>
            14 jours d'essai gratuit • Sans carte bancaire
          </p>
        </div>

        {error && (
          <div style={{ backgroundColor: 'var(--color-danger-bg)', color: 'var(--color-danger)', padding: '0.75rem', borderRadius: 'var(--radius-md)', fontSize: '0.85rem', marginBottom: '1.25rem' }}>
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit}>
          {/* Personal Info */}
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
            <div className="form-group">
              <label className="form-label">{isRTL ? 'الاسم الشخصي' : 'Prénom'}</label>
              <input
                type="text"
                required
                className="form-input"
                placeholder="Aziz"
                value={formData.firstName}
                onChange={(e) => setFormData({ ...formData, firstName: e.target.value })}
              />
            </div>
            <div className="form-group">
              <label className="form-label">{isRTL ? 'الاسم العائلي' : 'Nom'}</label>
              <input
                type="text"
                required
                className="form-input"
                placeholder="Aabbour"
                value={formData.lastName}
                onChange={(e) => setFormData({ ...formData, lastName: e.target.value })}
              />
            </div>
          </div>

          <div className="form-group">
            <label className="form-label">{isRTL ? 'البريد الإلكتروني' : 'Adresse Email'}</label>
            <input
              type="email"
              required
              className="form-input"
              placeholder="contact@mon-salon.ma"
              value={formData.email}
              onChange={(e) => setFormData({ ...formData, email: e.target.value })}
            />
          </div>

          <div className="form-group">
            <label className="form-label">{isRTL ? 'رقم الهاتف / الواتساب' : 'Téléphone / WhatsApp'}</label>
            <input
              type="tel"
              required
              className="form-input"
              placeholder="+212612345678"
              value={formData.phone}
              onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
            />
          </div>

          <div className="form-group">
            <label className="form-label">{isRTL ? 'اسم المحل / العيادة / المركز' : "Nom de l'établissement"}</label>
            <input
              type="text"
              required
              className="form-input"
              placeholder="Salon Riad Beauty"
              value={formData.businessName}
              onChange={(e) => setFormData({ ...formData, businessName: e.target.value })}
            />
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
            <div className="form-group">
              <label className="form-label">{isRTL ? 'النشاط' : 'Activité'}</label>
              <select
                className="form-select"
                value={formData.businessCategory}
                onChange={(e) => setFormData({ ...formData, businessCategory: e.target.value })}
              >
                <option value="HAIR_SALON">Salon de coiffure</option>
                <option value="BARBER">Barbier (الحلاقة)</option>
                <option value="BEAUTY_SALON">Institut de beauté</option>
                <option value="DENTIST">Dentiste</option>
                <option value="DOCTOR">Médecin / Cabinet</option>
                <option value="CLINIC">Clinique</option>
                <option value="FITNESS_COACH">Coach / Fitness</option>
                <option value="CAR_GARAGE">Garage Auto</option>
                <option value="OTHER">Autre activité</option>
              </select>
            </div>

            <div className="form-group">
              <label className="form-label">{isRTL ? 'المدينة' : 'Ville'}</label>
              <select
                className="form-select"
                value={formData.city}
                onChange={(e) => setFormData({ ...formData, city: e.target.value })}
              >
                {moroccanCities.map(city => (
                  <option key={city} value={city}>{city}</option>
                ))}
              </select>
            </div>
          </div>

          <div className="form-group">
            <label className="form-label">{isRTL ? 'كلمة المرور' : 'Mot de passe (8+ caractères)'}</label>
            <input
              type="password"
              required
              minLength={8}
              className="form-input"
              placeholder="••••••••"
              value={formData.password}
              onChange={(e) => setFormData({ ...formData, password: e.target.value })}
            />
          </div>

          <button
            type="submit"
            disabled={loading}
            className="btn btn-primary"
            style={{ width: '100%', padding: '0.8rem', marginTop: '0.5rem' }}
          >
            {loading ? 'Création du compte...' : (
              <>
                <span>{isRTL ? 'ابدأ الآن مجاناً' : 'Démarrer gratuitement'}</span>
                <ArrowRight size={16} />
              </>
            )}
          </button>
        </form>

        <div style={{ textAlign: 'center', marginTop: '1.25rem', fontSize: '0.85rem', color: 'var(--color-text-muted)' }}>
          Vous avez déjà un compte ?{' '}
          <Link to="/login" style={{ color: 'var(--color-primary)', fontWeight: 700 }}>
            Se connecter
          </Link>
        </div>
      </div>
    </div>
  );
};
