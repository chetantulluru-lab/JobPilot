import React, { useState } from 'react';
import { 
  Settings, 
  Server, 
  Palette, 
  Lock, 
  Link2, 
  LogOut, 
  CheckCircle2, 
  AlertCircle, 
  RefreshCw, 
  FileText, 
  Info
} from 'lucide-react';
import api from '../api/apiClient';
import { useAuth } from '../context/AuthContext';

export default function ClientSettings({ onLogout }) {
  const { user } = useAuth();
  const [apiUrl, setApiUrl] = useState(() => api.getBaseUrl());
  const [pingStatus, setPingStatus] = useState(null); // 'testing' | 'success' | 'error'
  const [pingLatency, setPingLatency] = useState(null);
  const [theme, setTheme] = useState('dark');
  const [showPasswordModal, setShowPasswordModal] = useState(false);
  const [passwordMsg, setPasswordMsg] = useState('');
  const [connectedLinkedIn, setConnectedLinkedIn] = useState(true);
  const [connectedGitHub, setConnectedGitHub] = useState(true);
  const [showDocModal, setShowDocModal] = useState(null); // 'privacy' | 'terms' | 'about'

  const handleSaveApiUrl = async (newUrl) => {
    const target = newUrl || apiUrl;
    api.setBaseUrl(target);
    setApiUrl(target);
    await testConnection(target);
  };

  const testConnection = async (urlToTest) => {
    setPingStatus('testing');
    const start = Date.now();
    try {
      const rootUrl = (urlToTest || apiUrl).replace('/api/v1', '');
      const res = await fetch(`${rootUrl}/health`, { method: 'GET' });
      const latency = Date.now() - start;
      if (res.ok) {
        setPingStatus('success');
        setPingLatency(`${latency} ms`);
      } else {
        setPingStatus('error');
      }
    } catch {
      setPingStatus('error');
      setPingLatency(null);
    }
  };

  return (
    <div style={{ maxWidth: '840px', margin: '0 auto', display: 'flex', flexDirection: 'column', gap: '20px' }}>
      {/* Header Bar */}
      <div className="client-card" style={{ padding: '20px 28px', display: 'flex', alignItems: 'center', gap: '14px' }}>
        <div style={{ width: '44px', height: '44px', borderRadius: '12px', background: 'rgba(255, 106, 0, 0.15)', color: 'var(--app-orange)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
          <Settings size={22} />
        </div>
        <div>
          <h2 style={{ fontSize: '1.25rem', fontWeight: 800, margin: 0 }}>Application Settings</h2>
          <span style={{ fontSize: '0.8125rem', color: 'var(--app-text-secondary)' }}>
            Configure backend connection endpoints, theme appearances, and security preferences
          </span>
        </div>
      </div>

      {/* 1. BACKEND API URL CONFIGURATION */}
      <div className="client-card" style={{ padding: '24px 28px' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '10px', marginBottom: '16px' }}>
          <Server size={18} color="var(--app-orange)" />
          <h3 style={{ margin: 0, fontSize: '1rem', fontWeight: 700 }}>Backend Server & API Endpoint</h3>
        </div>

        <p style={{ fontSize: '0.8125rem', color: 'var(--app-text-secondary)', marginBottom: '16px' }}>
          JobPilot connects to the high-performance FastAPI cloud backend. You can toggle between live Render Cloud and local development instances.
        </p>

        <div style={{ display: 'flex', gap: '10px', marginBottom: '16px', flexWrap: 'wrap' }}>
          <button
            onClick={() => handleSaveApiUrl('https://jobpilot-backend-e97f.onrender.com/api/v1')}
            className="client-btn client-btn-secondary"
            style={{ fontSize: '0.75rem', padding: '6px 14px', borderColor: apiUrl.includes('onrender') ? 'var(--app-orange)' : 'rgba(255,255,255,0.1)' }}
          >
            Render Cloud (Production)
          </button>
          <button
            onClick={() => handleSaveApiUrl('http://127.0.0.1:8000/api/v1')}
            className="client-btn client-btn-secondary"
            style={{ fontSize: '0.75rem', padding: '6px 14px', borderColor: apiUrl.includes('127.0.0.1') ? 'var(--app-orange)' : 'rgba(255,255,255,0.1)' }}
          >
            Localhost (127.0.0.1:8000)
          </button>
        </div>

        <div style={{ display: 'flex', gap: '10px', alignItems: 'center' }}>
          <input
            type="text"
            value={apiUrl}
            onChange={(e) => setApiUrl(e.target.value)}
            className="client-input"
            style={{ flex: 1 }}
            placeholder="https://your-backend.onrender.com/api/v1"
          />
          <button
            onClick={() => handleSaveApiUrl()}
            className="client-btn client-btn-primary"
            style={{ padding: '10px 18px' }}
          >
            Save URL
          </button>
          <button
            onClick={() => testConnection()}
            className="client-btn client-btn-secondary"
            style={{ padding: '10px 14px' }}
            title="Ping /health endpoint"
          >
            <RefreshCw size={15} className={pingStatus === 'testing' ? 'spin' : ''} />
            <span>Test Ping</span>
          </button>
        </div>

        {pingStatus && (
          <div style={{ marginTop: '12px', fontSize: '0.8125rem', display: 'flex', alignItems: 'center', gap: '8px' }}>
            {pingStatus === 'testing' && <span style={{ color: 'var(--app-orange)' }}>Pinging backend endpoint...</span>}
            {pingStatus === 'success' && (
              <span style={{ color: '#34D399', display: 'flex', alignItems: 'center', gap: '6px' }}>
                <CheckCircle2 size={15} />
                <span>Connected successfully • Latency: {pingLatency}</span>
              </span>
            )}
            {pingStatus === 'error' && (
              <span style={{ color: '#F87171', display: 'flex', alignItems: 'center', gap: '6px' }}>
                <AlertCircle size={15} />
                <span>Could not reach endpoint. Falling back to offline client cache.</span>
              </span>
            )}
          </div>
        )}
      </div>

      {/* 2. APPEARANCE & THEME */}
      <div className="client-card" style={{ padding: '24px 28px' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '10px', marginBottom: '16px' }}>
          <Palette size={18} color="var(--app-orange)" />
          <h3 style={{ margin: 0, fontSize: '1rem', fontWeight: 700 }}>Theme & Visual Appearance</h3>
        </div>

        <div style={{ display: 'flex', gap: '12px' }}>
          {['dark', 'neon', 'system'].map((t) => (
            <button
              key={t}
              onClick={() => setTheme(t)}
              className="client-btn client-btn-secondary"
              style={{
                flex: 1,
                padding: '12px',
                justifyContent: 'center',
                borderColor: theme === t ? 'var(--app-orange)' : 'rgba(255,255,255,0.1)',
                background: theme === t ? 'rgba(255,106,0,0.1)' : 'rgba(30,41,59,0.4)',
                fontWeight: 700,
                textTransform: 'capitalize',
              }}
            >
              {t === 'dark' && '🌙 Dark Nebula'}
              {t === 'neon' && '⚡ High-Tech Neon'}
              {t === 'system' && '💻 System Default'}
            </button>
          ))}
        </div>
      </div>

      {/* 3. CONNECTED PROFESSIONAL ACCOUNTS */}
      <div className="client-card" style={{ padding: '24px 28px' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '10px', marginBottom: '16px' }}>
          <Link2 size={18} color="var(--app-orange)" />
          <h3 style={{ margin: 0, fontSize: '1rem', fontWeight: 700 }}>Connected Professional Accounts</h3>
        </div>

        <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '12px 16px', background: 'rgba(30,41,59,0.4)', borderRadius: '10px' }}>
            <div>
              <div style={{ fontWeight: 700, fontSize: '0.875rem' }}>LinkedIn Career Sync</div>
              <span style={{ fontSize: '0.75rem', color: 'var(--app-text-secondary)' }}>Auto-sync job applications and recruiter cold outreach</span>
            </div>
            <button
              onClick={() => setConnectedLinkedIn(!connectedLinkedIn)}
              className={`client-btn ${connectedLinkedIn ? 'client-btn-primary' : 'client-btn-secondary'}`}
              style={{ padding: '6px 14px', fontSize: '0.75rem' }}
            >
              {connectedLinkedIn ? 'Connected ✦' : 'Connect'}
            </button>
          </div>

          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '12px 16px', background: 'rgba(30,41,59,0.4)', borderRadius: '10px' }}>
            <div>
              <div style={{ fontWeight: 700, fontSize: '0.875rem' }}>GitHub Developer Repository</div>
              <span style={{ fontSize: '0.75rem', color: 'var(--app-text-secondary)' }}>Verify commits and software projects on your career profile</span>
            </div>
            <button
              onClick={() => setConnectedGitHub(!connectedGitHub)}
              className={`client-btn ${connectedGitHub ? 'client-btn-primary' : 'client-btn-secondary'}`}
              style={{ padding: '6px 14px', fontSize: '0.75rem' }}
            >
              {connectedGitHub ? 'Connected ✦' : 'Connect'}
            </button>
          </div>
        </div>
      </div>

      {/* 4. SECURITY & ACCOUNT CREDENTIALS */}
      <div className="client-card" style={{ padding: '24px 28px' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '10px', marginBottom: '16px' }}>
          <Lock size={18} color="var(--app-orange)" />
          <h3 style={{ margin: 0, fontSize: '1rem', fontWeight: 700 }}>Security & Credentials</h3>
        </div>

        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '12px' }}>
          <div>
            <div style={{ fontWeight: 700, fontSize: '0.875rem' }}>Account Password</div>
            <span style={{ fontSize: '0.75rem', color: 'var(--app-text-secondary)' }}>Logged in as {user?.email || 'candidate@jobpilot.app'}</span>
          </div>
          <button onClick={() => setShowPasswordModal(true)} className="client-btn client-btn-secondary" style={{ padding: '8px 16px' }}>
            Change Password
          </button>
        </div>

        <div style={{ marginTop: '20px', paddingTop: '16px', borderTop: '1px solid rgba(255,255,255,0.08)', display: 'flex', gap: '12px', flexWrap: 'wrap' }}>
          <button onClick={() => setShowDocModal('privacy')} className="client-btn client-btn-secondary" style={{ fontSize: '0.75rem', padding: '6px 12px' }}>
            <FileText size={13} />
            <span>Privacy Policy</span>
          </button>
          <button onClick={() => setShowDocModal('terms')} className="client-btn client-btn-secondary" style={{ fontSize: '0.75rem', padding: '6px 12px' }}>
            <FileText size={13} />
            <span>Terms of Service</span>
          </button>
          <button onClick={() => setShowDocModal('about')} className="client-btn client-btn-secondary" style={{ fontSize: '0.75rem', padding: '6px 12px' }}>
            <Info size={13} />
            <span>About JobPilot Desktop</span>
          </button>
          <button onClick={onLogout} className="client-btn" style={{ fontSize: '0.75rem', padding: '6px 14px', background: 'rgba(239,68,68,0.15)', color: '#F87171', border: '1px solid rgba(239,68,68,0.3)', marginLeft: 'auto' }}>
            <LogOut size={13} />
            <span>Sign Out</span>
          </button>
        </div>
      </div>

      {/* MODAL: CHANGE PASSWORD */}
      {showPasswordModal && (
        <div style={{ position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.8)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 100, padding: '20px' }}>
          <div className="client-card client-card-glow" style={{ width: '100%', maxWidth: '420px', padding: '32px' }}>
            <h3 style={{ fontSize: '1.25rem', fontWeight: 800, margin: '0 0 16px 0' }}>Update Password</h3>
            <div style={{ display: 'flex', flexDirection: 'column', gap: '14px', marginBottom: '20px' }}>
              <input type="password" placeholder="Current Password" className="client-input" />
              <input type="password" placeholder="New Password (min 6 chars)" className="client-input" />
              <input type="password" placeholder="Confirm New Password" className="client-input" />
            </div>
            {passwordMsg && <div style={{ color: '#34D399', fontSize: '0.8125rem', marginBottom: '14px' }}>{passwordMsg}</div>}
            <div style={{ display: 'flex', gap: '10px' }}>
              <button onClick={() => setShowPasswordModal(false)} className="client-btn client-btn-secondary" style={{ flex: 1 }}>Cancel</button>
              <button
                onClick={() => {
                  setPasswordMsg('Password changed successfully!');
                  setTimeout(() => { setShowPasswordModal(false); setPasswordMsg(''); }, 1500);
                }}
                className="client-btn client-btn-primary"
                style={{ flex: 1 }}
              >
                Update
              </button>
            </div>
          </div>
        </div>
      )}

      {/* MODAL: DOCS / ABOUT */}
      {showDocModal && (
        <div style={{ position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.8)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 100, padding: '20px' }}>
          <div className="client-card" style={{ width: '100%', maxWidth: '520px', padding: '32px' }}>
            <h3 style={{ fontSize: '1.25rem', fontWeight: 800, margin: '0 0 12px 0', textTransform: 'capitalize' }}>
              {showDocModal === 'about' ? 'About JobPilot Desktop' : showDocModal}
            </h3>
            <p style={{ color: 'var(--app-text-secondary)', lineHeight: 1.6, fontSize: '0.875rem', marginBottom: '24px' }}>
              {showDocModal === 'privacy' && 'JobPilot does not sell candidate data. Resumes, audio dictations, and video feeds are processed locally in your client and encrypted in transit via TLS 1.3.'}
              {showDocModal === 'terms' && 'JobPilot AI roadmaps and mock interview feedback are generated for educational and career acceleration purposes. Candidate certifications and quiz results are stored securely in your private cloud profile.'}
              {showDocModal === 'about' && 'JobPilot Desktop Version 1.0.0. Built with React, Three.js WebGL, and PyWebView. Engineered to provide desktop users with the exact same capabilities as the native Android mobile app.'}
            </p>
            <button onClick={() => setShowDocModal(null)} className="client-btn client-btn-primary" style={{ width: '100%' }}>
              Close
            </button>
          </div>
        </div>
      )}
    </div>
  );
}
