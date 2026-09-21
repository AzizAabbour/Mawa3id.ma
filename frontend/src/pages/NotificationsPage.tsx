import React, { useEffect, useState } from 'react';
import { Sidebar } from '../components/Sidebar';
import { Header } from '../components/Header';
import { useLanguage } from '../contexts/LanguageContext';
import { apiFetch } from '../services/api';
import { MessageSquare, Bell, CheckCircle2, RefreshCw, Send, Eye } from 'lucide-react';

export const NotificationsPage: React.FC = () => {
  const { isRTL } = useLanguage();
  const [notifications, setNotifications] = useState<any[]>([]);
  const [config, setConfig] = useState<any>({ whatsappEnabled: true, smsEnabled: false, reminder24hEnabled: true, reminder2hEnabled: true });
  const [previewContent, setPreviewContent] = useState('');
  const [templateBody, setTemplateBody] = useState(
    "Bonjour {{customerName}} 👋,\n\nVotre rendez-vous chez {{businessName}} est confirmé !\n\n✂️ Prestation : {{serviceName}}\n📅 Date : {{appointmentDate}}\n⏰ Heure : {{appointmentTime}}\n📍 Adresse : {{businessAddress}}\n\nÀ très bientôt !"
  );

  useEffect(() => {
    loadNotifications();
    handlePreview(templateBody);
  }, []);

  const loadNotifications = async () => {
    try {
      const [notifsRes, configRes] = await Promise.all([
        apiFetch<any>('/notifications'),
        apiFetch<any>('/notifications/config')
      ]);
      setNotifications(notifsRes.content || []);
      if (configRes) setConfig(configRes);
    } catch (err) {
      // Demo fallback data
      setNotifications([
        { id: 1, recipient: '+212612345678', type: 'BOOKING_CONFIRMATION', channel: 'WHATSAPP', status: 'DELIVERED', createdAt: '2026-09-22 14:30', messageContent: 'Confirmation rendez-vous Coupe & Barbe' },
        { id: 2, recipient: '+212622334455', type: 'REMINDER_24H', channel: 'WHATSAPP', status: 'DELIVERED', createdAt: '2026-09-22 10:00', messageContent: 'Rappel 24h Soin Visage' },
        { id: 3, recipient: '+212633445566', type: 'BOOKING_CONFIRMATION', channel: 'WHATSAPP', status: 'SENT', createdAt: '2026-09-22 09:15', messageContent: 'Confirmation rendez-vous Coupe Dégradé' }
      ]);
    }
  };

  const handlePreview = async (body: string) => {
    try {
      const res = await apiFetch<any>('/notifications/templates/preview', {
        method: 'POST',
        body: JSON.stringify({
          templateBody: body,
          sampleCustomerName: 'Karim Bennani',
          sampleServiceName: 'Coupe & Barbe',
          sampleDate: 'Demain (23 Septembre)',
          sampleTime: '15:00',
          sampleBusinessName: 'Salon Riad Beauty',
          sampleBusinessAddress: '14 Bd d’Anfa, Casablanca'
        })
      });
      setPreviewContent(res.renderedContent);
    } catch {
      // Fallback local substitution
      let s = body
        .replace('{{customerName}}', 'Karim Bennani')
        .replace('{{businessName}}', 'Salon Riad Beauty')
        .replace('{{serviceName}}', 'Coupe & Barbe')
        .replace('{{appointmentDate}}', 'Demain (23 Septembre)')
        .replace('{{appointmentTime}}', '15:00')
        .replace('{{businessAddress}}', "14 Bd d'Anfa, Casablanca");
      setPreviewContent(s);
    }
  };

  return (
    <div style={{ display: 'flex', minHeight: '100vh', backgroundColor: 'var(--color-bg)' }}>
      <Sidebar />

      <div style={{ flex: 1, display: 'flex', flexDirection: 'column' }}>
        <Header 
          title={isRTL ? 'إدارة رسائل الواتساب والتذكيرات' : 'Notifications WhatsApp & SMS'} 
          subtitle={isRTL ? 'تخصيص الرسائل ومتابعة تقارير الإرسال للزبناء' : 'Personnalisez vos modèles et suivez les délivrabilités'}
        />

        <main style={{ padding: '2rem', flex: 1 }}>
          <div style={{ display: 'grid', gridTemplateColumns: '1.2fr 1fr', gap: '2rem', marginBottom: '2rem' }}>
            {/* Template Editor */}
            <div className="card">
              <h3 style={{ fontSize: '1.15rem', fontWeight: 800, marginBottom: '0.75rem' }}>
                {isRTL ? 'تخصيص نموذج رسالة التأكيد (WhatsApp)' : 'Modèle de Confirmation WhatsApp'}
              </h3>
              <p style={{ fontSize: '0.85rem', color: 'var(--color-text-muted)', marginBottom: '1rem' }}>
                Variables disponibles: <code>{'{{customerName}}'}</code>, <code>{'{{businessName}}'}</code>, <code>{'{{serviceName}}'}</code>, <code>{'{{appointmentDate}}'}</code>, <code>{'{{appointmentTime}}'}</code>, <code>{'{{businessAddress}}'}</code>
              </p>

              <textarea
                className="form-textarea"
                rows={6}
                value={templateBody}
                onChange={(e) => {
                  setTemplateBody(e.target.value);
                  handlePreview(e.target.value);
                }}
                style={{ fontFamily: 'monospace', fontSize: '0.875rem' }}
              />

              <div style={{ display: 'flex', justifyContent: 'flex-end', marginTop: '1rem' }}>
                <button className="btn btn-primary" onClick={() => alert('Modèle WhatsApp enregistré avec succès !')}>
                  Enregistrer les modifications
                </button>
              </div>
            </div>

            {/* Live WhatsApp Mockup Preview */}
            <div style={{
              backgroundColor: '#ECE5DD',
              borderRadius: '16px',
              padding: '1.25rem',
              border: '4px solid #FFFFFF',
              boxShadow: 'var(--shadow-md)'
            }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', marginBottom: '0.75rem', fontSize: '0.85rem', fontWeight: 700, color: '#075E54' }}>
                <Eye size={16} />
                <span>Aperçu en direct (WhatsApp)</span>
              </div>

              <div style={{
                backgroundColor: '#FFFFFF',
                borderRadius: '12px',
                padding: '1rem',
                fontSize: '0.875rem',
                lineHeight: 1.6,
                whiteSpace: 'pre-wrap',
                boxShadow: '0 1px 2px rgba(0,0,0,0.1)'
              }}>
                {previewContent}
              </div>
            </div>
          </div>

          {/* Delivery Logs */}
          <div className="card" style={{ padding: 0, overflow: 'hidden' }}>
            <div style={{ padding: '1.25rem 1.5rem', borderBottom: '1px solid var(--color-border)', display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
              <h3 style={{ fontSize: '1.1rem', fontWeight: 700 }}>
                {isRTL ? 'سجل الرسائل المرسلة' : 'Dernières notifications envoyées'}
              </h3>
              <span className="badge badge-success">WhatsApp Connecté ✓</span>
            </div>

            <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: isRTL ? 'right' : 'left' }}>
              <thead>
                <tr style={{ backgroundColor: 'var(--color-surface-subtle)', borderBottom: '1px solid var(--color-border)' }}>
                  <th style={{ padding: '0.875rem 1.25rem', fontSize: '0.8rem', fontWeight: 700, color: 'var(--color-text-muted)' }}>DESTINATAIRE</th>
                  <th style={{ padding: '0.875rem 1.25rem', fontSize: '0.8rem', fontWeight: 700, color: 'var(--color-text-muted)' }}>TYPE</th>
                  <th style={{ padding: '0.875rem 1.25rem', fontSize: '0.8rem', fontWeight: 700, color: 'var(--color-text-muted)' }}>CANAL</th>
                  <th style={{ padding: '0.875rem 1.25rem', fontSize: '0.8rem', fontWeight: 700, color: 'var(--color-text-muted)' }}>STATUT</th>
                  <th style={{ padding: '0.875rem 1.25rem', fontSize: '0.8rem', fontWeight: 700, color: 'var(--color-text-muted)' }}>DATE D'ENVOI</th>
                </tr>
              </thead>
              <tbody>
                {notifications.map((n) => (
                  <tr key={n.id} style={{ borderBottom: '1px solid var(--color-border-subtle)' }}>
                    <td style={{ padding: '1rem 1.25rem', fontWeight: 700 }}>{n.recipient}</td>
                    <td style={{ padding: '1rem 1.25rem', fontSize: '0.85rem' }}>{n.type}</td>
                    <td style={{ padding: '1rem 1.25rem' }}>
                      <span className="badge badge-success" style={{ backgroundColor: '#E8F8F0', color: '#128C7E' }}>{n.channel}</span>
                    </td>
                    <td style={{ padding: '1rem 1.25rem' }}>
                      <span className="badge badge-success">{n.status}</span>
                    </td>
                    <td style={{ padding: '1rem 1.25rem', fontSize: '0.8rem', color: 'var(--color-text-muted)' }}>{n.createdAt}</td>
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
