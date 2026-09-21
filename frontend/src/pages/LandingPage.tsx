import React from 'react';
import { Link } from 'react-router-dom';
import { useLanguage } from '../contexts/LanguageContext';
import { Navbar } from '../components/Navbar';
import { 
  Calendar, 
  MessageSquare, 
  Smartphone, 
  Clock, 
  CheckCircle2, 
  TrendingUp, 
  ShieldCheck, 
  Sparkles,
  ArrowRight,
  Star,
  Users
} from 'lucide-react';

export const LandingPage: React.FC = () => {
  const { t, isRTL } = useLanguage();

  return (
    <div style={{ minHeight: '100vh', display: 'flex', flexDirection: 'column' }}>
      <Navbar />

      {/* Hero Section with subtle Moroccan Zellige pattern */}
      <section className="zellige-hero" style={{ padding: '5rem 0 6rem 0', position: 'relative', overflow: 'hidden' }}>
        <div className="container" style={{ textAlign: 'center', position: 'relative', zIndex: 10 }}>
          <div style={{
            display: 'inline-flex',
            alignItems: 'center',
            gap: '0.5rem',
            padding: '0.4rem 1rem',
            backgroundColor: 'rgba(255, 255, 255, 0.12)',
            backdropFilter: 'blur(8px)',
            borderRadius: 'var(--radius-full)',
            border: '1px solid rgba(197, 152, 46, 0.4)',
            fontSize: '0.85rem',
            fontWeight: 600,
            color: 'var(--color-sand)',
            marginBottom: '1.5rem'
          }}>
            <Sparkles size={16} color="var(--color-gold)" />
            <span>{isRTL ? 'الحل الأول المخصص للمقاولات والخدمات في المغرب' : 'La solution #1 de prise de rendez-vous au Maroc'}</span>
          </div>

          <h1 style={{
            fontSize: 'clamp(2.2rem, 5vw, 3.8rem)',
            fontWeight: 800,
            lineHeight: 1.15,
            maxWidth: '920px',
            margin: '0 auto 1.5rem auto',
            letterSpacing: '-0.02em'
          }}>
            {t('tagline')}
          </h1>

          <p style={{
            fontSize: '1.15rem',
            maxWidth: '680px',
            margin: '0 auto 2.5rem auto',
            color: 'rgba(250, 247, 242, 0.85)',
            lineHeight: 1.6
          }}>
            {t('subtitle')}
          </p>

          <div style={{ display: 'flex', justifyContent: 'center', gap: '1rem', flexWrap: 'wrap' }}>
            <Link
              to="/register"
              className="btn btn-gold"
              style={{
                padding: '0.875rem 2rem',
                fontSize: '1.05rem',
                borderRadius: 'var(--radius-md)',
                boxShadow: '0 8px 20px rgba(197, 152, 46, 0.35)'
              }}
            >
              <span>{t('start_free')}</span>
              <ArrowRight size={18} />
            </Link>

            <a
              href="#demo-section"
              className="btn"
              style={{
                backgroundColor: 'rgba(255, 255, 255, 0.12)',
                color: '#FFFFFF',
                border: '1px solid rgba(255, 255, 255, 0.25)',
                padding: '0.875rem 1.75rem',
                fontSize: '1.05rem'
              }}
            >
              {t('see_demo')}
            </a>
          </div>

          {/* Social Proof */}
          <div style={{
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            gap: '1.5rem',
            marginTop: '3.5rem',
            fontSize: '0.875rem',
            color: 'rgba(255, 255, 255, 0.75)'
          }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '0.35rem' }}>
              <CheckCircle2 size={16} color="var(--color-gold)" />
              <span>{isRTL ? 'بدون بطاقة بنكية' : 'Sans carte bancaire'}</span>
            </div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '0.35rem' }}>
              <CheckCircle2 size={16} color="var(--color-gold)" />
              <span>{isRTL ? 'إعداد في دقيقتين' : 'Configuration en 2 min'}</span>
            </div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '0.35rem' }}>
              <CheckCircle2 size={16} color="var(--color-gold)" />
              <span>{isRTL ? 'دعم محلي بالدارجة والفرنسية' : 'Support marocain 7j/7'}</span>
            </div>
          </div>
        </div>
      </section>

      {/* Target Audience / Moroccan Businesses Section */}
      <section style={{ padding: '3.5rem 0', backgroundColor: 'var(--color-surface)', borderBottom: '1px solid var(--color-border)' }}>
        <div className="container">
          <p style={{ textAlign: 'center', fontSize: '0.9rem', fontWeight: 700, color: 'var(--color-text-muted)', textTransform: 'uppercase', letterSpacing: '0.05em', marginBottom: '1.5rem' }}>
            {isRTL ? 'مصمم خصيصاً للمهن والأنشطة المغربية' : 'Conçu pour tous les professionnels au Maroc'}
          </p>
          <div style={{
            display: 'flex',
            justifyContent: 'center',
            gap: '2rem',
            flexWrap: 'wrap',
            color: 'var(--color-text-muted)',
            fontWeight: 600,
            fontSize: '0.95rem'
          }}>
            <span>💇 Salons de coiffure</span>
            <span>💈 Barbiers (الحلاقة)</span>
            <span>💅 Instituts de beauté</span>
            <span>🦷 Cabinets dentaires</span>
            <span>🩺 Médecins & Cliniques</span>
            <span>🏋️ Coachs & Fitness</span>
            <span>🚗 Garages & Auto</span>
            <span>📚 Centres de formation</span>
          </div>
        </div>
      </section>

      {/* Features Grid */}
      <section id="features" style={{ padding: '5rem 0' }}>
        <div className="container">
          <div style={{ textAlign: 'center', maxWidth: '640px', margin: '0 auto 3.5rem auto' }}>
            <h2 style={{ fontSize: '2.25rem', fontWeight: 800, color: 'var(--color-text-main)', marginBottom: '1rem' }}>
              {isRTL ? 'كل ما تحتاجه لإدارة مواعيدك باحترافية' : 'Tout pour gérer votre activité sans stress'}
            </h2>
            <p style={{ color: 'var(--color-text-muted)', fontSize: '1.05rem' }}>
              {isRTL ? 'نظام متكامل يقلل من نسبة غياب الزبناء بنسبة تصل إلى 70%' : 'Réduisez le taux de no-show jusqu’à 70% grâce à nos automatisations pensées pour le Maroc.'}
            </p>
          </div>

          <div className="grid-cols-3">
            <div className="card">
              <div style={{
                width: '48px',
                height: '48px',
                borderRadius: '12px',
                backgroundColor: 'var(--color-primary-light)',
                color: 'var(--color-primary)',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                marginBottom: '1.25rem'
              }}>
                <MessageSquare size={24} />
              </div>
              <h3 style={{ fontSize: '1.25rem', fontWeight: 700, marginBottom: '0.5rem' }}>
                {isRTL ? 'تذكيرات واتساب أوتوماتيكية' : 'Rappels WhatsApp Automatisés'}
              </h3>
              <p style={{ color: 'var(--color-text-muted)', fontSize: '0.925rem', lineHeight: 1.6 }}>
                {isRTL ? 'تأكيد الحجز فوراً وتذكير الزبون قبل 24 ساعة وساعتين عبر الواتساب لتفادي النسيان.' : 'Confirmation instantanée et rappels 24h & 2h avant le rendez-vous directement sur WhatsApp.'}
              </p>
            </div>

            <div className="card">
              <div style={{
                width: '48px',
                height: '48px',
                borderRadius: '12px',
                backgroundColor: 'var(--color-secondary-light)',
                color: 'var(--color-secondary)',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                marginBottom: '1.25rem'
              }}>
                <Smartphone size={24} />
              </div>
              <h3 style={{ fontSize: '1.25rem', fontWeight: 700, marginBottom: '0.5rem' }}>
                {isRTL ? 'رابط حجز خاص لمشروعك' : 'Lien Public de Réservation'}
              </h3>
              <p style={{ color: 'var(--color-text-muted)', fontSize: '0.925rem', lineHeight: 1.6 }}>
                {isRTL ? 'شارك رابطك على إنستغرام أو تيك توك. الزبون يحجز موعده في 30 ثانية بدون تطبيق.' : 'Partagez mawa3id.ma/booking/votre-nom sur Instagram & Google. Prise de RDV fluide 24/7.'}
              </p>
            </div>

            <div className="card">
              <div style={{
                width: '48px',
                height: '48px',
                borderRadius: '12px',
                backgroundColor: 'var(--color-gold-light)',
                color: 'var(--color-gold)',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                marginBottom: '1.25rem'
              }}>
                <TrendingUp size={24} />
              </div>
              <h3 style={{ fontSize: '1.25rem', fontWeight: 700, marginBottom: '0.5rem' }}>
                {isRTL ? 'لوحة تحكم وإحصائيات دقيقة' : 'Tableau de Bord & Statistiques'}
              </h3>
              <p style={{ color: 'var(--color-text-muted)', fontSize: '0.925rem', lineHeight: 1.6 }}>
                {isRTL ? 'تتبع مداخيلك بالدرهم، عدد المواعيد، ونسبة حضور الزبناء في كل وقت.' : 'Suivez vos revenus en MAD, vos clients fidèles et votre calendrier d’équipe en un coup d’œil.'}
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* WhatsApp Live Demo Mockup Section */}
      <section id="whatsapp" style={{ padding: '5rem 0', backgroundColor: 'var(--color-sand-light)', borderTop: '1px solid var(--color-border)', borderBottom: '1px solid var(--color-border)' }}>
        <div className="container">
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '3.5rem', alignItems: 'center' }}>
            <div>
              <div style={{
                display: 'inline-block',
                padding: '0.35rem 0.75rem',
                backgroundColor: 'var(--color-primary-light)',
                color: 'var(--color-primary)',
                fontWeight: 700,
                fontSize: '0.85rem',
                borderRadius: 'var(--radius-full)',
                marginBottom: '1rem'
              }}>
                {isRTL ? 'الواتساب في المغرب' : 'WhatsApp Business Intégré'}
              </div>
              <h2 style={{ fontSize: '2.2rem', fontWeight: 800, color: 'var(--color-text-main)', marginBottom: '1rem', lineHeight: 1.25 }}>
                {isRTL ? 'تواصل مع زبنائك في التطبيق المفضل لديهم' : '98% des Marocains ouvrent WhatsApp dans les 3 minutes'}
              </h2>
              <p style={{ color: 'var(--color-text-muted)', fontSize: '1rem', lineHeight: 1.6, marginBottom: '1.5rem' }}>
                {isRTL ? 'تخلص نهائياً من المكالمات المتكررة والرسائل الضائعة. رسائلنا تصل بدقة وتتضمن موقع محلك على Google Maps.' : 'Finies les relances manuelles par téléphone. Vos clients reçoivent des notifications claires avec l’heure exacte, la prestation et votre adresse Google Maps.'}
              </p>
              <ul style={{ listStyle: 'none', display: 'flex', flexDirection: 'column', gap: '0.75rem' }}>
                <li style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', fontWeight: 600 }}>
                  <CheckCircle2 size={18} color="var(--color-primary)" />
                  <span>Confirmation immédiate avec récapitulatif du tarif (MAD)</span>
                </li>
                <li style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', fontWeight: 600 }}>
                  <CheckCircle2 size={18} color="var(--color-primary)" />
                  <span>Rappel automatique 24h avant pour valider la présence</span>
                </li>
                <li style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', fontWeight: 600 }}>
                  <CheckCircle2 size={18} color="var(--color-primary)" />
                  <span>Modèles de messages 100% personnalisables en Français et Darija</span>
                </li>
              </ul>
            </div>

            {/* Chat Mockup */}
            <div style={{
              backgroundColor: '#ECE5DD',
              borderRadius: '24px',
              padding: '1.5rem',
              boxShadow: 'var(--shadow-xl)',
              maxWidth: '420px',
              margin: '0 auto',
              border: '8px solid #FFFFFF'
            }}>
              <div style={{ backgroundColor: '#075E54', color: '#FFFFFF', padding: '0.75rem 1rem', borderRadius: '12px', display: 'flex', alignItems: 'center', gap: '0.75rem', marginBottom: '1.25rem' }}>
                <div style={{ width: '36px', height: '36px', borderRadius: '50%', backgroundColor: '#FFFFFF', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#075E54', fontWeight: 700 }}>
                  💈
                </div>
                <div>
                  <div style={{ fontWeight: 700, fontSize: '0.9rem' }}>Salon Riad Beauty</div>
                  <div style={{ fontSize: '0.7rem', opacity: 0.85 }}>Compte Entreprise Vérifié</div>
                </div>
              </div>

              {/* Message Bubble */}
              <div style={{
                backgroundColor: '#FFFFFF',
                borderRadius: '12px',
                borderTopLeftRadius: isRTL ? '12px' : '2px',
                borderTopRightRadius: isRTL ? '2px' : '12px',
                padding: '1rem',
                fontSize: '0.875rem',
                lineHeight: 1.5,
                boxShadow: '0 1px 2px rgba(0,0,0,0.1)'
              }}>
                <p><strong>Bonjour Karim 👋</strong>,</p>
                <p style={{ margin: '0.5rem 0' }}>Votre rendez-vous chez <strong>Salon Riad Beauty</strong> est confirmé !</p>
                <div style={{ backgroundColor: '#F5F7F6', padding: '0.5rem 0.75rem', borderRadius: '6px', margin: '0.5rem 0', fontSize: '0.8rem' }}>
                  <div>✂️ <strong>Prestation :</strong> Coupe & Barbe (80 DH)</div>
                  <div>📅 <strong>Date :</strong> Demain à 15:00</div>
                  <div>📍 <strong>Adresse :</strong> 14 Bd d'Anfa, Casablanca</div>
                </div>
                <p style={{ fontSize: '0.75rem', color: '#777', textAlign: 'right', marginTop: '0.35rem' }}>14:32 ✓✓</p>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Pricing Section in Moroccan Dirhams (MAD) */}
      <section id="pricing" style={{ padding: '5rem 0' }}>
        <div className="container">
          <div style={{ textAlign: 'center', maxWidth: '640px', margin: '0 auto 3.5rem auto' }}>
            <h2 style={{ fontSize: '2.25rem', fontWeight: 800, color: 'var(--color-text-main)', marginBottom: '1rem' }}>
              {isRTL ? 'أسعار شفافة وبسيطة بالدرهم المغربي' : 'Des tarifs transparents en Dirhams (DH)'}
            </h2>
            <p style={{ color: 'var(--color-text-muted)', fontSize: '1.05rem' }}>
              {isRTL ? 'اختر الباقة المناسبة لحجم نشاطك. بدون التزام ويمكنك الإلغاء في أي وقت.' : 'Sans engagement. Commencez gratuitement et faites évoluer votre forfait selon vos besoins.'}
            </p>
          </div>

          <div className="grid-cols-4">
            {/* Free */}
            <div className="card" style={{ display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}>
              <div>
                <h3 style={{ fontSize: '1.25rem', fontWeight: 700 }}>Gratuit</h3>
                <p style={{ fontSize: '0.85rem', color: 'var(--color-text-muted)', marginBottom: '1rem' }}>Pour démarrer sans frais</p>
                <div style={{ fontSize: '2rem', fontWeight: 800, color: 'var(--color-text-main)', marginBottom: '1.25rem' }}>
                  0 <span style={{ fontSize: '1rem', fontWeight: 600 }}>DH/mois</span>
                </div>
                <ul style={{ listStyle: 'none', display: 'flex', flexDirection: 'column', gap: '0.625rem', fontSize: '0.85rem' }}>
                  <li>✓ 30 rendez-vous / mois</li>
                  <li>✓ 50 clients enregistrés</li>
                  <li>✓ 1 collaborateur</li>
                  <li>✓ Page de réservation publique</li>
                </ul>
              </div>
              <Link to="/register" className="btn btn-secondary" style={{ marginTop: '1.5rem', width: '100%' }}>
                Commencer
              </Link>
            </div>

            {/* Starter */}
            <div className="card" style={{ display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}>
              <div>
                <h3 style={{ fontSize: '1.25rem', fontWeight: 700 }}>Starter</h3>
                <p style={{ fontSize: '0.85rem', color: 'var(--color-text-muted)', marginBottom: '1rem' }}>Pour les indépendants</p>
                <div style={{ fontSize: '2rem', fontWeight: 800, color: 'var(--color-primary)', marginBottom: '1.25rem' }}>
                  79 <span style={{ fontSize: '1rem', fontWeight: 600 }}>DH/mois</span>
                </div>
                <ul style={{ listStyle: 'none', display: 'flex', flexDirection: 'column', gap: '0.625rem', fontSize: '0.85rem' }}>
                  <li>✓ 300 rendez-vous / mois</li>
                  <li>✓ Clients illimités</li>
                  <li>✓ 2 collaborateurs</li>
                  <li>✓ Rappels WhatsApp inclus</li>
                  <li>✓ Statistiques de base</li>
                </ul>
              </div>
              <Link to="/register" className="btn btn-primary" style={{ marginTop: '1.5rem', width: '100%' }}>
                Essayer 14 jours
              </Link>
            </div>

            {/* Business (Popular) */}
            <div className="card" style={{
              display: 'flex',
              flexDirection: 'column',
              justifyContent: 'space-between',
              borderColor: 'var(--color-gold)',
              boxShadow: '0 8px 24px rgba(197, 152, 46, 0.15)',
              position: 'relative'
            }}>
              <div style={{
                position: 'absolute',
                top: '-12px',
                left: '50%',
                transform: 'translateX(-50%)',
                backgroundColor: 'var(--color-gold)',
                color: '#FFFFFF',
                padding: '0.2rem 0.75rem',
                borderRadius: 'var(--radius-full)',
                fontSize: '0.75rem',
                fontWeight: 700
              }}>
                LE PLUS POPULAIRE
              </div>
              <div>
                <h3 style={{ fontSize: '1.25rem', fontWeight: 700 }}>Business</h3>
                <p style={{ fontSize: '0.85rem', color: 'var(--color-text-muted)', marginBottom: '1rem' }}>Pour salons & cliniques</p>
                <div style={{ fontSize: '2rem', fontWeight: 800, color: 'var(--color-primary)', marginBottom: '1.25rem' }}>
                  149 <span style={{ fontSize: '1rem', fontWeight: 600 }}>DH/mois</span>
                </div>
                <ul style={{ listStyle: 'none', display: 'flex', flexDirection: 'column', gap: '0.625rem', fontSize: '0.85rem' }}>
                  <li>✓ <strong>Rendez-vous illimités</strong></li>
                  <li>✓ Clients illimités</li>
                  <li>✓ Jusqu'à 10 collaborateurs</li>
                  <li>✓ WhatsApp & SMS automatiques</li>
                  <li>✓ Modèles personnalisables</li>
                  <li>✓ Support prioritaire 7j/7</li>
                </ul>
              </div>
              <Link to="/register" className="btn btn-gold" style={{ marginTop: '1.5rem', width: '100%' }}>
                Démarrer l'essai
              </Link>
            </div>

            {/* Pro */}
            <div className="card" style={{ display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}>
              <div>
                <h3 style={{ fontSize: '1.25rem', fontWeight: 700 }}>Pro</h3>
                <p style={{ fontSize: '0.85rem', color: 'var(--color-text-muted)', marginBottom: '1rem' }}>Pour centres & franchises</p>
                <div style={{ fontSize: '2rem', fontWeight: 800, color: 'var(--color-text-main)', marginBottom: '1.25rem' }}>
                  299 <span style={{ fontSize: '1rem', fontWeight: 600 }}>DH/mois</span>
                </div>
                <ul style={{ listStyle: 'none', display: 'flex', flexDirection: 'column', gap: '0.625rem', fontSize: '0.85rem' }}>
                  <li>✓ Tout en illimité</li>
                  <li>✓ Multi-succursales</li>
                  <li>✓ Collaborateurs illimités</li>
                  <li>✓ Accès API & Intégrations</li>
                  <li>✓ Gestionnaire de compte dédié</li>
                </ul>
              </div>
              <Link to="/register" className="btn btn-secondary" style={{ marginTop: '1.5rem', width: '100%' }}>
                Nous contacter
              </Link>
            </div>
          </div>
        </div>
      </section>

      {/* Footer with Moroccan touch */}
      <footer style={{ backgroundColor: 'var(--color-surface)', borderTop: '1px solid var(--color-border)', padding: '3.5rem 0 2rem 0', marginTop: 'auto' }}>
        <div className="container">
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '1.5rem', borderBottom: '1px solid var(--color-border)', paddingBottom: '2rem' }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '0.625rem' }}>
              <div style={{ width: '32px', height: '32px', borderRadius: '8px', backgroundColor: 'var(--color-primary)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                <Calendar size={16} color="#C5982E" />
              </div>
              <span style={{ fontWeight: 800, fontSize: '1.15rem', color: 'var(--color-primary)' }}>
                Mawa3id<span style={{ color: 'var(--color-gold)' }}>.ma</span>
              </span>
            </div>
            <p style={{ fontSize: '0.85rem', color: 'var(--color-text-muted)' }}>
              Fait avec passion au Maroc 🇲🇦 pour nos entrepreneurs.
            </p>
          </div>
          <div style={{ textAlign: 'center', fontSize: '0.8rem', color: 'var(--color-text-muted)', marginTop: '1.5rem' }}>
            © {new Date().getFullYear()} Mawa3id.ma — Tous droits réservés.
          </div>
        </div>
      </footer>
    </div>
  );
};
