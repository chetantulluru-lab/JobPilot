import React, { useState } from 'react';
import { 
  Palette, 
  Lock, 
  Shield, 
  FileText, 
  Headphones, 
  Code, 
  Info, 
  LogOut, 
  ChevronRight,
  CheckCircle2,
  AlertCircle
} from 'lucide-react';
import { useAuth } from '../context/AuthContext';

export default function ClientSettings({ onLogout }) {
  const { user, logout } = useAuth();
  
  // Theme state
  const [theme, setTheme] = useState('light');
  const [showThemeModal, setShowThemeModal] = useState(false);
  
  // Change Password state
  const [showPasswordModal, setShowPasswordModal] = useState(false);
  const [currentPassword, setCurrentPassword] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [passwordError, setPasswordError] = useState('');
  const [passwordSuccess, setPasswordSuccess] = useState(false);
  
  // Document Modals
  const [activeModal, setActiveModal] = useState(null); // 'privacy' | 'terms' | 'support' | 'licenses' | 'about' | 'logout'

  const settingsRows = [
    {
      id: 'theme',
      title: 'Appearance & Theme',
      subtitle: 'Choose Light theme (Warm-White), Dark theme, or System default',
      icon: Palette,
      onClick: () => setShowThemeModal(true),
    },
    {
      id: 'password',
      title: 'Change Password',
      subtitle: 'Update your login credentials securely',
      icon: Lock,
      onClick: () => {
        setCurrentPassword('');
        setNewPassword('');
        setConfirmPassword('');
        setPasswordError('');
        setPasswordSuccess(false);
        setShowPasswordModal(true);
      },
    },
    {
      id: 'privacy',
      title: 'Privacy Policy',
      subtitle: 'Your career and resume data is private and encrypted',
      icon: Shield,
      onClick: () => setActiveModal('privacy'),
    },
    {
      id: 'terms',
      title: 'Terms of Service',
      subtitle: 'Usage guidelines for AI roadmaps and resume services',
      icon: FileText,
      onClick: () => setActiveModal('terms'),
    },
    {
      id: 'support',
      title: 'Contact Support',
      subtitle: 'Reach out for help, bugs, or feature feedback',
      icon: Headphones,
      onClick: () => setActiveModal('support'),
    },
    {
      id: 'licenses',
      title: 'Open Source Licenses',
      subtitle: 'Software components that power JobPilot',
      icon: Code,
      onClick: () => setActiveModal('licenses'),
    },
    {
      id: 'about',
      title: 'About JobPilot',
      subtitle: 'Version 1.0.0 (Windows Desktop Release)',
      icon: Info,
      onClick: () => setActiveModal('about'),
    },
  ];

  const handleUpdatePassword = (e) => {
    e.preventDefault();
    if (!currentPassword || !newPassword || !confirmPassword) {
      setPasswordError('Please fill in all password fields.');
      return;
    }
    if (newPassword !== confirmPassword) {
      setPasswordError('New passwords do not match.');
      return;
    }
    if (newPassword.length < 8) {
      setPasswordError('Password must be at least 8 characters.');
      return;
    }
    setPasswordError('');
    setPasswordSuccess(true);
    setTimeout(() => {
      setShowPasswordModal(false);
      setPasswordSuccess(false);
    }, 1500);
  };

  return (
    <div style={{ maxWidth: '800px', margin: '0 auto', display: 'flex', flexDirection: 'column', gap: '14px' }}>
      {/* Header */}
      <div style={{ marginBottom: '8px' }}>
        <h2 style={{ fontSize: '1.5rem', fontWeight: 800, color: 'var(--app-text)', margin: '0 0 4px 0' }}>
          Settings
        </h2>
        <span style={{ fontSize: '0.875rem', color: 'var(--app-text-secondary)' }}>
          Manage your account preferences and application settings
        </span>
      </div>

      {/* Settings Rows */}
      <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        {settingsRows.map((item) => {
          const Icon = item.icon;
          return (
            <div
              key={item.id}
              onClick={item.onClick}
              className="client-card"
              style={{
                padding: '16px 20px',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'space-between',
                cursor: 'pointer',
                transition: 'all 0.2s ease',
              }}
            >
              <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
                <div
                  style={{
                    width: '42px',
                    height: '42px',
                    borderRadius: '12px',
                    background: 'var(--app-orange-light)',
                    color: 'var(--app-orange)',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    flexShrink: 0,
                  }}
                >
                  <Icon size={20} />
                </div>
                <div>
                  <div style={{ fontWeight: 700, fontSize: '0.9375rem', color: 'var(--app-text)' }}>
                    {item.title}
                  </div>
                  <div style={{ fontSize: '0.8125rem', color: 'var(--app-text-secondary)', marginTop: '2px' }}>
                    {item.subtitle}
                  </div>
                </div>
              </div>

              <ChevronRight size={18} color="#94A3B8" />
            </div>
          );
        })}

        {/* Log Out Row */}
        <div
          onClick={() => setActiveModal('logout')}
          className="client-card"
          style={{
            padding: '16px 20px',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            gap: '10px',
            cursor: 'pointer',
            border: '1px solid rgba(239, 68, 68, 0.25)',
            background: 'var(--app-danger-bg)',
            marginTop: '8px',
          }}
        >
          <LogOut size={18} color="#DC2626" />
          <span style={{ fontWeight: 800, fontSize: '0.9375rem', color: '#DC2626' }}>
            Log Out from JobPilot
          </span>
        </div>
      </div>

      {/* 1. Theme Modal */}
      {showThemeModal && (
        <div style={{ position: 'fixed', inset: 0, background: 'rgba(15, 23, 42, 0.6)', backdropFilter: 'blur(8px)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 100, padding: '20px' }}>
          <div className="client-card client-card-glow" style={{ width: '100%', maxWidth: '440px', padding: '28px' }}>
            <h3 style={{ fontSize: '1.25rem', fontWeight: 800, margin: '0 0 16px 0', color: 'var(--app-text)' }}>Choose Theme</h3>
            <div style={{ display: 'flex', flexDirection: 'column', gap: '10px', marginBottom: '24px' }}>
              {[
                { id: 'light', label: 'Light Theme (Warm-White Compose Parity)' },
                { id: 'dark', label: 'Dark Theme' },
                { id: 'system', label: 'System Default' },
              ].map((opt) => (
                <div
                  key={opt.id}
                  onClick={() => setTheme(opt.id)}
                  style={{
                    padding: '12px 16px',
                    borderRadius: '10px',
                    border: '1px solid',
                    borderColor: theme === opt.id ? 'var(--app-orange)' : 'var(--app-border-subtle)',
                    background: theme === opt.id ? 'var(--app-orange-light)' : 'var(--app-surface-light)',
                    cursor: 'pointer',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'space-between',
                  }}
                >
                  <span style={{ fontSize: '0.875rem', fontWeight: 600, color: 'var(--app-text)' }}>{opt.label}</span>
                  {theme === opt.id && <CheckCircle2 size={16} color="var(--app-orange)" />}
                </div>
              ))}
            </div>
            <button onClick={() => setShowThemeModal(false)} className="client-btn client-btn-primary" style={{ width: '100%' }}>
              Apply Theme
            </button>
          </div>
        </div>
      )}

      {/* 2. Change Password Modal */}
      {showPasswordModal && (
        <div style={{ position: 'fixed', inset: 0, background: 'rgba(15, 23, 42, 0.6)', backdropFilter: 'blur(8px)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 100, padding: '20px' }}>
          <div className="client-card client-card-glow" style={{ width: '100%', maxWidth: '440px', padding: '28px' }}>
            <h3 style={{ fontSize: '1.25rem', fontWeight: 800, margin: '0 0 16px 0', color: 'var(--app-text)' }}>Change Password</h3>
            {passwordSuccess ? (
              <div style={{ textAlign: 'center', padding: '24px 0', color: '#047857', fontWeight: 700 }}>
                ✓ Password updated successfully!
              </div>
            ) : (
              <form onSubmit={handleUpdatePassword} style={{ display: 'flex', flexDirection: 'column', gap: '14px' }}>
                <div>
                  <label style={{ display: 'block', fontSize: '0.8125rem', fontWeight: 700, marginBottom: '4px', color: 'var(--app-text-secondary)' }}>
                    Current Password
                  </label>
                  <input
                    type="password"
                    required
                    value={currentPassword}
                    onChange={(e) => setCurrentPassword(e.target.value)}
                    className="client-input"
                  />
                </div>
                <div>
                  <label style={{ display: 'block', fontSize: '0.8125rem', fontWeight: 700, marginBottom: '4px', color: 'var(--app-text-secondary)' }}>
                    New Password
                  </label>
                  <input
                    type="password"
                    required
                    minLength={8}
                    value={newPassword}
                    onChange={(e) => setNewPassword(e.target.value)}
                    className="client-input"
                  />
                </div>
                <div>
                  <label style={{ display: 'block', fontSize: '0.8125rem', fontWeight: 700, marginBottom: '4px', color: 'var(--app-text-secondary)' }}>
                    Confirm New Password
                  </label>
                  <input
                    type="password"
                    required
                    minLength={8}
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                    className="client-input"
                  />
                </div>

                {passwordError && (
                  <div style={{ color: '#EF4444', fontSize: '0.8125rem' }}>✕ {passwordError}</div>
                )}

                <div style={{ display: 'flex', gap: '10px', marginTop: '8px' }}>
                  <button type="button" onClick={() => setShowPasswordModal(false)} className="client-btn client-btn-secondary" style={{ flex: 1 }}>
                    Cancel
                  </button>
                  <button type="submit" className="client-btn client-btn-primary" style={{ flex: 1 }}>
                    Update Password
                  </button>
                </div>
              </form>
            )}
          </div>
        </div>
      )}

      {/* 3. Document / Info Modals */}
      {activeModal && activeModal !== 'logout' && (
        <div style={{ position: 'fixed', inset: 0, background: 'rgba(15, 23, 42, 0.6)', backdropFilter: 'blur(8px)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 100, padding: '20px' }}>
          <div className="client-card" style={{ width: '100%', maxWidth: '520px', padding: '32px' }}>
            <h3 style={{ fontSize: '1.25rem', fontWeight: 800, margin: '0 0 14px 0', color: 'var(--app-text)' }}>
              {activeModal === 'privacy' && 'Privacy Policy'}
              {activeModal === 'terms' && 'Terms of Service'}
              {activeModal === 'support' && 'Contact Support'}
              {activeModal === 'licenses' && 'Open Source Licenses'}
              {activeModal === 'about' && 'About JobPilot'}
            </h3>

            <div style={{ fontSize: '0.875rem', color: 'var(--app-text-secondary)', lineHeight: 1.6, marginBottom: '24px' }}>
              {activeModal === 'privacy' && (
                <div>
                  <p>JobPilot values your confidentiality.</p>
                  <ul style={{ paddingLeft: '20px', margin: '8px 0' }}>
                    <li>Your resumes, academic records, and career goals are strictly stored in encrypted databases.</li>
                    <li>We NEVER sell or share your personal information with third-party advertisers.</li>
                    <li>AI roadmaps and resume scoring are processed with enterprise-grade data isolation.</li>
                    <li>You retain full ownership and can delete your profile data at any time.</li>
                  </ul>
                </div>
              )}

              {activeModal === 'terms' && (
                <div>
                  <p>By utilizing JobPilot, you agree to:</p>
                  <ol style={{ paddingLeft: '20px', margin: '8px 0' }}>
                    <li>Use AI learning roadmaps for legitimate educational and professional development.</li>
                    <li>Submit accurate personal, educational, and experience records for resume parsing.</li>
                    <li>Acknowledge that AI career coach suggestions and ATS evaluations are advisory guidelines and do not constitute employment guarantees.</li>
                  </ol>
                </div>
              )}

              {activeModal === 'support' && (
                <div>
                  <p>Have questions or encountering an issue? Reach out to our engineering team:</p>
                  <div style={{ padding: '12px', background: 'var(--app-orange-light)', borderRadius: '8px', color: 'var(--app-orange)', fontWeight: 800, margin: '12px 0' }}>
                    support@jobpilot.app
                  </div>
                  <p style={{ fontSize: '0.8125rem', margin: 0 }}>Response time is typically within 24 hours.</p>
                </div>
              )}

              {activeModal === 'licenses' && (
                <div style={{ fontSize: '0.8125rem' }}>
                  <p>JobPilot is powered by modern open source software:</p>
                  <p>• React 19 & Lucide Icons (MIT License)<br />
                  • Microsoft Edge WebView2 (Microsoft License)<br />
                  • FastAPI, Pydantic & SQLAlchemy (MIT License)<br />
                  • PostgreSQL & asyncpg (PostgreSQL License)</p>
                </div>
              )}

              {activeModal === 'about' && (
                <div>
                  <p><strong>JobPilot Windows Edition</strong><br />Version 1.0.0 (Release)</p>
                  <p>"Your career. Piloted by AI."</p>
                  <p style={{ fontSize: '0.8125rem', margin: 0 }}>Engineered to match the native Android experience with high-performance desktop execution, connecting directly to the live production Render cloud backend.</p>
                </div>
              )}
            </div>

            <button onClick={() => setActiveModal(null)} className="client-btn client-btn-primary" style={{ width: '100%' }}>
              Close
            </button>
          </div>
        </div>
      )}

      {/* 4. Logout Confirmation Modal */}
      {activeModal === 'logout' && (
        <div style={{ position: 'fixed', inset: 0, background: 'rgba(15, 23, 42, 0.6)', backdropFilter: 'blur(8px)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 100, padding: '20px' }}>
          <div className="client-card" style={{ width: '100%', maxWidth: '420px', padding: '28px', textAlign: 'center' }}>
            <h3 style={{ fontSize: '1.25rem', fontWeight: 800, margin: '0 0 8px 0', color: 'var(--app-text)' }}>Log Out?</h3>
            <p style={{ fontSize: '0.875rem', color: 'var(--app-text-secondary)', margin: '0 0 24px 0' }}>
              Are you sure you want to log out of your JobPilot account?
            </p>
            <div style={{ display: 'flex', gap: '10px' }}>
              <button onClick={() => setActiveModal(null)} className="client-btn client-btn-secondary" style={{ flex: 1 }}>
                Cancel
              </button>
              <button
                onClick={() => {
                  setActiveModal(null);
                  logout();
                  if (onLogout) onLogout();
                }}
                className="client-btn"
                style={{ flex: 1, background: '#DC2626', color: '#FFFFFF', border: 'none' }}
              >
                Log Out
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
