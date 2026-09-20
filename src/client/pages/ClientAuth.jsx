import React, { useState } from 'react';
import { Mail, ArrowRight, KeyRound, Sparkles } from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import api from '../api/apiClient';

export default function ClientAuth({ onAuthSuccess }) {
  const [mode, setMode] = useState('signin'); // 'signin' | 'signup' | 'forgot' | 'onboarding'
  const [fullName, setFullName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [newPassword, setNewPassword] = useState('');
  
  // OTP Verification Modal State
  const [showOtpModal, setShowOtpModal] = useState(false);
  const [otpCode, setOtpCode] = useState('');
  const [statusMsg, setStatusMsg] = useState('');
  const [errorMsg, setErrorMsg] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);

  // Onboarding Wizard State
  const [onboardingRole, setOnboardingRole] = useState('Android & Full Stack Engineer');
  const [onboardingExp, setOnboardingExp] = useState('1-3 Years');
  const [onboardingSkills, setOnboardingSkills] = useState('Kotlin, Jetpack Compose, Python, FastAPI');

  const { login, startRegistration, verifyRegistration, updateUser } = useAuth();

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
      if (mode === 'signup') {
        await verifyRegistration(email.trim(), otpCode.trim(), password, fullName.trim());
        setShowOtpModal(false);
        setMode('onboarding');
      } else if (mode === 'forgot') {
        await api.forgotPasswordVerify(email.trim(), otpCode.trim(), newPassword);
        setShowOtpModal(false);
        setStatusMsg('Password reset successfully! Please sign in.');
        setMode('signin');
      }
    } catch (err) {
      setErrorMsg(err.message || 'Invalid or expired OTP code.');
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleStartForgot = async (e) => {
    e.preventDefault();
    setErrorMsg('');
    setIsSubmitting(true);
    try {
      const res = await api.forgotPasswordStart(email.trim());
      setStatusMsg(res?.message || 'Password reset OTP dispatched to your email!');
      setShowOtpModal(true);
    } catch (err) {
      setErrorMsg(err.message || 'Failed to send reset code.');
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleFinishOnboarding = () => {
    updateUser({
      targetRole: onboardingRole,
      experienceLevel: onboardingExp,
    });
    if (onAuthSuccess) onAuthSuccess();
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

        {/* Tab Toggle (if not onboarding) */}
        {mode !== 'onboarding' && (
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
              Sign Up (Email OTP)
            </button>
          </div>
        )}

        {errorMsg && (
          <div style={{ padding: '10px 14px', background: 'rgba(239, 68, 68, 0.15)', border: '1px solid rgba(239, 68, 68, 0.3)', borderRadius: '8px', color: '#F87171', fontSize: '0.8125rem', marginBottom: '16px' }}>
            ✕ {errorMsg}
          </div>
        )}

        {statusMsg && (
          <div style={{ padding: '10px 14px', background: 'rgba(16, 185, 129, 0.15)', border: '1px solid rgba(16, 185, 129, 0.3)', borderRadius: '8px', color: '#34D399', fontSize: '0.8125rem', marginBottom: '16px' }}>
            ✓ {statusMsg}
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
              <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '6px' }}>
                <label style={{ fontSize: '0.8125rem', fontWeight: 700, color: 'var(--app-text-secondary)' }}>
                  Password
                </label>
                <button
                  type="button"
                  onClick={() => { setMode('forgot'); setErrorMsg(''); }}
                  style={{ background: 'transparent', border: 'none', color: 'var(--app-orange)', fontSize: '0.75rem', cursor: 'pointer', padding: 0 }}
                >
                  Forgot Password?
                </button>
              </div>
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

        {/* FORGOT PASSWORD FORM */}
        {mode === 'forgot' && (
          <form onSubmit={handleStartForgot} style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
            <h3 style={{ fontSize: '1.125rem', fontWeight: 800, margin: 0 }}>Reset Password</h3>
            <p style={{ fontSize: '0.8125rem', color: 'var(--app-text-secondary)', margin: 0 }}>
              Enter your registered email to receive a 6-digit password reset verification code.
            </p>

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
                New Password (min 6 chars)
              </label>
              <input
                type="password"
                required
                minLength={6}
                value={newPassword}
                onChange={(e) => setNewPassword(e.target.value)}
                className="client-input"
                placeholder="New password"
              />
            </div>

            <div style={{ display: 'flex', gap: '10px' }}>
              <button
                type="button"
                onClick={() => setMode('signin')}
                className="client-btn client-btn-secondary"
                style={{ flex: 1 }}
              >
                Back to Sign In
              </button>
              <button
                type="submit"
                disabled={isSubmitting || !email || newPassword.length < 6}
                className="client-btn client-btn-primary"
                style={{ flex: 1 }}
              >
                <span>Send Reset OTP</span>
              </button>
            </div>
          </form>
        )}

        {/* ONBOARDING WIZARD */}
        {mode === 'onboarding' && (
          <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
            <div style={{ textAlign: 'center' }}>
              <div style={{ width: '48px', height: '48px', borderRadius: '50%', background: 'rgba(255, 106, 0, 0.15)', color: 'var(--app-orange)', display: 'flex', alignItems: 'center', justifyContent: 'center', margin: '0 auto 12px auto' }}>
                <Sparkles size={24} />
              </div>
              <h3 style={{ fontSize: '1.25rem', fontWeight: 800, margin: '0 0 6px 0' }}>Welcome to JobPilot!</h3>
              <p style={{ fontSize: '0.8125rem', color: 'var(--app-text-secondary)', margin: 0 }}>
                Let's configure your career compass in 60 seconds
              </p>
            </div>

            <div>
              <label style={{ display: 'block', fontSize: '0.8125rem', fontWeight: 700, marginBottom: '6px', color: 'var(--app-text-secondary)' }}>
                Target Engineering Role
              </label>
              <input
                type="text"
                value={onboardingRole}
                onChange={(e) => setOnboardingRole(e.target.value)}
                className="client-input"
              />
            </div>

            <div>
              <label style={{ display: 'block', fontSize: '0.8125rem', fontWeight: 700, marginBottom: '6px', color: 'var(--app-text-secondary)' }}>
                Experience Level
              </label>
              <select
                value={onboardingExp}
                onChange={(e) => setOnboardingExp(e.target.value)}
                className="client-input"
              >
                <option value="College Graduate">College Graduate / Student</option>
                <option value="1-3 Years">Junior / Associate (1-3 Years)</option>
                <option value="3-5 Years">Mid-Level (3-5 Years)</option>
                <option value="5+ Years">Senior / Lead (5+ Years)</option>
              </select>
            </div>

            <div>
              <label style={{ display: 'block', fontSize: '0.8125rem', fontWeight: 700, marginBottom: '6px', color: 'var(--app-text-secondary)' }}>
                Primary Technical Skills
              </label>
              <input
                type="text"
                value={onboardingSkills}
                onChange={(e) => setOnboardingSkills(e.target.value)}
                className="client-input"
              />
            </div>

            <button
              onClick={handleFinishOnboarding}
              className="client-btn client-btn-primary"
              style={{ width: '100%', padding: '14px' }}
            >
              <span>Launch My Career Copilot</span>
              <ArrowRight size={16} />
            </button>
          </div>
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
                  <span>{isSubmitting ? 'Verifying...' : 'Verify & Continue'}</span>
                </button>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
