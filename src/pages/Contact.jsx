import React, { useState } from 'react';
import { Mail, MessageSquare, Send, CheckCircle2, HelpCircle, Bug, Handshake } from 'lucide-react';
import SectionHeading from '../components/SectionHeading';
import GlassCard from '../components/GlassCard';
import GlowBackground from '../components/GlowBackground';
import Button from '../components/Button';
import { APP_CONFIG } from '../config/appConfig';

export default function Contact() {
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    subject: 'General questions',
    message: '',
  });

  const [submitted, setSubmitted] = useState(false);
  const [loading, setLoading] = useState(false);

  const inquiryOptions = [
    { value: 'General questions', label: 'General questions', icon: HelpCircle },
    { value: 'Feedback', label: 'Product Feedback', icon: MessageSquare },
    { value: 'Technical support', label: 'Technical support', icon: Bug },
    { value: 'Partnership inquiries', label: 'Partnership inquiries', icon: Handshake },
  ];

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!formData.name || !formData.email || !formData.message) return;

    setLoading(true);
    // Simulate lightweight client-side send for Phase 1
    setTimeout(() => {
      setLoading(false);
      setSubmitted(true);
    }, 600);
  };

  const handleReset = () => {
    setFormData({
      name: '',
      email: '',
      subject: 'General questions',
      message: '',
    });
    setSubmitted(false);
  };

  return (
    <div className="page-contact">
      <section className="section" style={{ paddingTop: '60px', paddingBottom: '90px', position: 'relative' }}>
        <GlowBackground variant="top" />

        <div className="container container-narrow">
          <SectionHeading
            eyebrow="Get In Touch"
            title="Let's"
            highlight="talk."
            subtitle="Have a question about JobPilot, feedback on the Android app, or an inquiry regarding our NLP technology? We'd love to hear from you."
          />

          <div
            style={{
              display: 'grid',
              gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))',
              gap: '32px',
              alignItems: 'start',
            }}
          >
            {/* Contact Options / Channels */}
            <div>
              <GlassCard padding="32px" elevated style={{ marginBottom: '24px' }}>
                <h3 style={{ fontSize: '1.25rem', fontWeight: '800', marginBottom: '16px' }}>
                  Inquiry Channels
                </h3>

                <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
                  {inquiryOptions.map((opt) => {
                    const Icon = opt.icon;
                    const isSelected = formData.subject === opt.value;

                    return (
                      <div
                        key={opt.value}
                        onClick={() => setFormData({ ...formData, subject: opt.value })}
                        role="button"
                        tabIndex={0}
                        onKeyDown={(e) => {
                          if (e.key === 'Enter' || e.key === ' ') {
                            setFormData({ ...formData, subject: opt.value });
                          }
                        }}
                        style={{
                          padding: '12px 16px',
                          borderRadius: 'var(--radius-md)',
                          background: isSelected ? 'var(--orange-50)' : 'rgba(255, 255, 255, 0.7)',
                          border: isSelected ? '1px solid var(--orange-400)' : '1px solid var(--border-subtle)',
                          display: 'flex',
                          alignItems: 'center',
                          gap: '12px',
                          cursor: 'pointer',
                          transition: 'all 0.2s ease',
                        }}
                      >
                        <div
                          style={{
                            color: isSelected ? 'var(--orange-600)' : 'var(--text-muted)',
                            display: 'flex',
                          }}
                        >
                          <Icon size={18} />
                        </div>
                        <div style={{ fontSize: '0.9375rem', fontWeight: isSelected ? '700' : '500', color: isSelected ? 'var(--orange-700)' : 'var(--text-primary)' }}>
                          {opt.label}
                        </div>
                      </div>
                    );
                  })}
                </div>
              </GlassCard>

              {/* Direct Support Point */}
              <GlassCard padding="24px">
                <div style={{ display: 'flex', alignItems: 'center', gap: '10px', marginBottom: '8px' }}>
                  <Mail size={18} color="var(--orange-500)" />
                  <span style={{ fontWeight: '700', fontSize: '0.9375rem' }}>Project Support Email</span>
                </div>
                <div style={{ fontSize: '0.875rem', color: 'var(--text-secondary)', marginBottom: '6px' }}>
                  {APP_CONFIG.supportEmail}
                </div>
                <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>
                  (Project contact placeholder for Phase 1 inquiries)
                </div>
              </GlassCard>
            </div>

            {/* Contact Form UI */}
            <GlassCard elevated padding="36px">
              {submitted ? (
                <div style={{ textAlign: 'center', padding: '40px 10px' }}>
                  <div
                    style={{
                      width: '60px',
                      height: '60px',
                      borderRadius: '50%',
                      background: '#ECFDF5',
                      color: '#059669',
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                      margin: '0 auto 20px',
                    }}
                  >
                    <CheckCircle2 size={32} />
                  </div>
                  <h3 style={{ fontSize: '1.5rem', fontWeight: '800', marginBottom: '10px' }}>
                    Message Sent!
                  </h3>
                  <p style={{ color: 'var(--text-secondary)', fontSize: '0.9375rem', lineHeight: '1.6', marginBottom: '24px' }}>
                    Thank you for reaching out to JobPilot. Our team has received your note under{' '}
                    <strong>"{formData.subject}"</strong>.
                  </p>
                  <Button variant="secondary" onClick={handleReset}>
                    Send Another Note
                  </Button>
                </div>
              ) : (
                <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
                  <div>
                    <label
                      htmlFor="contact-name"
                      style={{
                        display: 'block',
                        fontSize: '0.875rem',
                        fontWeight: '700',
                        color: 'var(--text-primary)',
                        marginBottom: '6px',
                      }}
                    >
                      Your Name
                    </label>
                    <input
                      id="contact-name"
                      type="text"
                      required
                      placeholder="e.g. Alex Rivera"
                      value={formData.name}
                      onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                      style={{
                        width: '100%',
                        padding: '12px 16px',
                        borderRadius: 'var(--radius-md)',
                        border: '1px solid var(--border-subtle)',
                        background: '#FFFFFF',
                        fontFamily: 'inherit',
                        fontSize: '0.9375rem',
                      }}
                    />
                  </div>

                  <div>
                    <label
                      htmlFor="contact-email"
                      style={{
                        display: 'block',
                        fontSize: '0.875rem',
                        fontWeight: '700',
                        color: 'var(--text-primary)',
                        marginBottom: '6px',
                      }}
                    >
                      Your Email Address
                    </label>
                    <input
                      id="contact-email"
                      type="email"
                      required
                      placeholder="alex@university.edu"
                      value={formData.email}
                      onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                      style={{
                        width: '100%',
                        padding: '12px 16px',
                        borderRadius: 'var(--radius-md)',
                        border: '1px solid var(--border-subtle)',
                        background: '#FFFFFF',
                        fontFamily: 'inherit',
                        fontSize: '0.9375rem',
                      }}
                    />
                  </div>

                  <div>
                    <label
                      htmlFor="contact-subject"
                      style={{
                        display: 'block',
                        fontSize: '0.875rem',
                        fontWeight: '700',
                        color: 'var(--text-primary)',
                        marginBottom: '6px',
                      }}
                    >
                      Inquiry Category
                    </label>
                    <select
                      id="contact-subject"
                      value={formData.subject}
                      onChange={(e) => setFormData({ ...formData, subject: e.target.value })}
                      style={{
                        width: '100%',
                        padding: '12px 16px',
                        borderRadius: 'var(--radius-md)',
                        border: '1px solid var(--border-subtle)',
                        background: '#FFFFFF',
                        fontFamily: 'inherit',
                        fontSize: '0.9375rem',
                        cursor: 'pointer',
                      }}
                    >
                      {inquiryOptions.map((opt) => (
                        <option key={opt.value} value={opt.value}>
                          {opt.label}
                        </option>
                      ))}
                    </select>
                  </div>

                  <div>
                    <label
                      htmlFor="contact-message"
                      style={{
                        display: 'block',
                        fontSize: '0.875rem',
                        fontWeight: '700',
                        color: 'var(--text-primary)',
                        marginBottom: '6px',
                      }}
                    >
                      Your Message
                    </label>
                    <textarea
                      id="contact-message"
                      required
                      rows={5}
                      placeholder="Tell us what you are thinking or how we can help..."
                      value={formData.message}
                      onChange={(e) => setFormData({ ...formData, message: e.target.value })}
                      style={{
                        width: '100%',
                        padding: '12px 16px',
                        borderRadius: 'var(--radius-md)',
                        border: '1px solid var(--border-subtle)',
                        background: '#FFFFFF',
                        fontFamily: 'inherit',
                        fontSize: '0.9375rem',
                        resize: 'vertical',
                      }}
                    />
                  </div>

                  <Button
                    type="submit"
                    variant="primary"
                    size="lg"
                    icon={<Send size={16} />}
                    disabled={loading}
                    style={{ marginTop: '8px' }}
                  >
                    {loading ? 'Sending Message...' : 'Send Message'}
                  </Button>
                </form>
              )}
            </GlassCard>
          </div>
        </div>
      </section>
    </div>
  );
}
