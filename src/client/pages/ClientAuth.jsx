import React, { useState } from 'react';
import { Mail, ArrowRight, KeyRound } from 'lucide-react';
import { useAuth } from '../context/AuthContext';

export default function ClientAuth({ onAuthSuccess }) {
  const [mode, setMode] = useState('signin'); // 'signin' | 'signup'
  const [fullName, setFullName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  
  // OTP Verification Modal State
  const [showOtpModal, setShowOtpModal] = useState(false);
  const [otpCode, setOtpCode] = useState('');
  const [statusMsg, setStatusMsg] = useState('');
  const [errorMsg, setErrorMsg] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);

  const { login, startRegistration, verifyRegistration } = useAuth();

  const handleSignIn = async (e) => {
    e.preventDefault();
    setErrorMsg('');
    setIsSubmitting(true);
    try {
      await login(email.trim(), password);
      if (onAuthSuccess) onAuthSuccess();
    } catch (err) {
      setErrorMsg(err.message || 'Login failed. Please check credentials.');
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleStartSignUp = async (e) => {
    e.preventDefault();
    setErrorMsg('');
    setIsSubmitting(true);
    try {
      const res = await startRegistration(fullName.trim(), email.trim(), password);
      setStatusMsg(res?.message || 'Verification OTP dispatched! Please check your email inbox.');
      setShowOtpModal(true);
    } catch (err) {
      setErrorMsg(err.message || 'Failed to send OTP code.');
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleConfirmOtp = async () => {
    if (otpCode.trim().length !== 6) {
      setErrorMsg('Please enter the full 6-digit numeric verification code.');
      return;
    }
    setErrorMsg('');
    setIsSubmitting(true);
    try {
      await verifyRegistration(email.trim(), otpCode.trim(), password, fullName.trim());
      setShowOtpModal(false);
      if (onAuthSuccess) onAuthSuccess();
    } catch (err) {
      setErrorMsg(err.message || 'Invalid or expired OTP code.');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div style={{ minHeight: '80vh', display: 'flex', alignItems: 'center', justifyContent: 'center', padding: '24px' }}>
      <div className="client-card client-card-glow" style={{ width: '100%', maxWidth: '460px', padding: '36px' }}>
        {/* Header */}
        <div style={{ textAlign: 'center', marginBottom: '28px' }}>
          <div style={{ display: 'inline-flex', alignItems: 'center', gap: '8px', marginBottom: '8px' }}>
            <span style={{ fontSize: '1.5rem', color: 'var(--app-orange)' }}>✦</span>
            <span style={{ fontSize: '1.5rem', fontWeight: 800, color: '#FFFFFF' }}>JobPilot</span>
          </div>
          <p style={{ fontSize: '0.875rem', color: 'var(--app-text-secondary)', margin: 0 }}>
            Your career. Piloted by AI.
          </p>
        </div>

        {/* Tab Toggle */}
        <div className="client-tabs" style={{ width: '100%', justifyContent: 'center', marginBottom: '24px' }}>
          <button
            onClick={() => { setMode('signin'); setErrorMsg(''); }}
            className={`client-tab-btn ${mode === 'signin' ? 'active' : ''}`}
            style={{ flex: 1 }}
          >
            Sign In
          </button>
          <button
            onClick={() => { setMode('signup'); setErrorMsg(''); }}
            className={`client-tab-btn ${mode === 'signup' ? 'active' : ''}`}
            style={{ flex: 1 }}
          >
            Create Account (Email OTP)
          </button>
        </div>

        {errorMsg && (
          <div style={{ padding: '10px 14px', background: 'rgba(239, 68, 68, 0.15)', border: '1px solid rgba(239, 68, 68, 0.3)', borderRadius: '8px', color: '#F87171', fontSize: '0.8125rem', marginBottom: '16px' }}>
            ✕ {errorMsg}
          </div>
        )}

        {/* SIGN IN FORM */}
        {mode === 'signin' && (
          <form onSubmit={handleSignIn} style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
            <div>
              <label style={{ display: 'block', fontSize: '0.8125rem', fontWeight: 700, marginBottom: '6px', color: 'var(--app-text-secondary)' }}>
                Email Address
              </label>
              <input
                type="email"
                required
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="client-input"
                placeholder="name@example.com"
              />
            </div>

            <div>
              <label style={{ display: 'block', fontSize: '0.8125rem', fontWeight: 700, marginBottom: '6px', color: 'var(--app-text-secondary)' }}>
                Password
              </label>
              <input
                type="password"
                required
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="client-input"
                placeholder="••••••••"
              />
            </div>

            <button
              type="submit"
              disabled={isSubmitting || !email || !password}
              className="client-btn client-btn-primary"
              style={{ padding: '12px', marginTop: '8px' }}
            >
              <span>{isSubmitting ? 'Signing in...' : 'Sign In to JobPilot'}</span>
              <ArrowRight size={16} />
            </button>
          </form>
        )}

        {/* SIGN UP FORM (EMAIL OTP) */}
        {mode === 'signup' && (
          <form onSubmit={handleStartSignUp} style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
            <div>
              <label style={{ display: 'block', fontSize: '0.8125rem', fontWeight: 700, marginBottom: '6px', color: 'var(--app-text-secondary)' }}>
                Full Name
              </label>
              <input
                type="text"
                required
                value={fullName}
                onChange={(e) => setFullName(e.target.value)}
                className="client-input"
                placeholder="e.g. Chetan Tulluru"
              />
            </div>

            <div>
              <label style={{ display: 'block', fontSize: '0.8125rem', fontWeight: 700, marginBottom: '6px', color: 'var(--app-text-secondary)' }}>
                Email Address (Receives 6-digit OTP)
              </label>
              <input
                type="email"
                required
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="client-input"
                placeholder="name@example.com"
              />
            </div>

            <div>
              <label style={{ display: 'block', fontSize: '0.8125rem', fontWeight: 700, marginBottom: '6px', color: 'var(--app-text-secondary)' }}>
                Create Password
              </label>
              <input
                type="password"
                required
                minLength={6}
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="client-input"
                placeholder="Minimum 6 characters"
              />
            </div>

            <button
              type="submit"
              disabled={isSubmitting || !fullName || !email || password.length < 6}
              className="client-btn client-btn-primary"
              style={{ padding: '12px', marginTop: '8px' }}
            >
              <Mail size={16} />
              <span>{isSubmitting ? 'Sending Verification OTP...' : 'Send Email Verification OTP'}</span>
            </button>
          </form>
        )}

        {/* OTP VERIFICATION MODAL DIALOG */}
        {showOtpModal && (
          <div
            style={{
              position: 'fixed',
              inset: 0,
              background: 'rgba(0, 0, 0, 0.8)',
              backdropFilter: 'blur(8px)',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              padding: '20px',
              zIndex: 200,
            }}
          >
            <div className="client-card client-card-glow" style={{ width: '100%', maxWidth: '420px', padding: '32px', textAlign: 'center' }}>
              <div style={{ width: '48px', height: '48px', borderRadius: '50%', background: 'rgba(255, 106, 0, 0.15)', color: 'var(--app-orange)', display: 'flex', alignItems: 'center', justifyContent: 'center', margin: '0 auto 16px auto' }}>
                <KeyRound size={24} />
              </div>

              <h3 style={{ fontSize: '1.25rem', fontWeight: 800, margin: '0 0 6px 0' }}>
                Enter Verification Code
              </h3>
              <p style={{ fontSize: '0.8125rem', color: 'var(--app-text-secondary)', margin: '0 0 20px 0' }}>
                We sent a 6-digit numeric verification code to <strong style={{ color: '#FFFFFF' }}>{email}</strong>
              </p>

              {statusMsg && (
                <div style={{ fontSize: '0.75rem', color: '#34D399', background: 'rgba(16, 185, 129, 0.1)', padding: '8px 12px', borderRadius: '6px', marginBottom: '16px' }}>
                  {statusMsg}
                </div>
              )}

              <input
                type="text"
                maxLength={6}
                value={otpCode}
                onChange={(e) => setOtpCode(e.target.value.replace(/\D/g, ''))}
                className="client-input"
                style={{ fontSize: '1.5rem', letterSpacing: '0.4em', textAlign: 'center', fontWeight: 800, marginBottom: '20px' }}
                placeholder="••••••"
                autoFocus
              />

              <div style={{ display: 'flex', gap: '10px' }}>
                <button
                  onClick={() => setShowOtpModal(false)}
                  className="client-btn client-btn-secondary"
                  style={{ flex: 1 }}
                >
                  Cancel
                </button>
                <button
                  onClick={handleConfirmOtp}
                  disabled={isSubmitting || otpCode.length !== 6}
                  className="client-btn client-btn-primary"
                  style={{ flex: 1 }}
                >
                  <span>{isSubmitting ? 'Verifying...' : 'Verify & Enter'}</span>
                </button>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
