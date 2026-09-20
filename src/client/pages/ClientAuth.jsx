import React, { useState } from 'react';
import { 
  Mail, 
  ArrowRight, 
  KeyRound, 
  Sparkles, 
  Upload, 
  FileText, 
  ArrowLeft,
  CheckCircle2
} from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import api from '../api/apiClient';
import AIOrb from '../../components/AIOrb';

export default function ClientAuth({ onAuthSuccess }) {
  // Screen views matching Android NavGraph: 'login' | 'register' | 'forgot' | 'onboarding'
  const [screenView, setScreenView] = useState('login');
  
  // Login & Register Form State
  const [fullName, setFullName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [newPassword, setNewPassword] = useState('');
  
  // OTP Verification Modal State
  const [showOtpDialog, setShowOtpDialog] = useState(false);
  const [enteredOtp, setEnteredOtp] = useState('');
  const [statusMsg, setStatusMsg] = useState('');
  const [errorMsg, setErrorMsg] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);

  // Career Profile Onboarding State (Steps 1 to 3)
  const [onboardingStep, setOnboardingStep] = useState(1);
  const [age, setAge] = useState('22');
  const [college, setCollege] = useState('Vellore Institute of Technology');
  const [degree, setDegree] = useState('B.Tech in Computer Science & Engineering');
  const [branch, setBranch] = useState('Computer Science');
  const [targetRole, setTargetRole] = useState('Android & Full Stack Engineer');

  const { login, startRegistration, verifyRegistration, updateUser } = useAuth();

  // Clear messages on screen switch
  const switchScreen = (view) => {
    setErrorMsg('');
    setStatusMsg('');
    setScreenView(view);
  };

  // 1. Handle Login
  const handleLogin = async (e) => {
    e.preventDefault();
    setErrorMsg('');
    setIsSubmitting(true);
    try {
      await login(email.trim(), password);
      if (onAuthSuccess) onAuthSuccess();
    } catch (err) {
      setErrorMsg(err.message || 'Incorrect email or password.');
    } finally {
      setIsSubmitting(false);
    }
  };

  // 2. Handle Start Registration (Brevo OTP)
  const handleStartRegister = async (e) => {
    e.preventDefault();
    setErrorMsg('');
    setIsSubmitting(true);
    try {
      const res = await startRegistration(fullName.trim(), email.trim(), password);
      setStatusMsg(res?.message || 'Verification code dispatched to your email.');
      setEnteredOtp('');
      setShowOtpDialog(true);
    } catch (err) {
      setErrorMsg(err.message || 'Failed to send OTP code.');
    } finally {
      setIsSubmitting(false);
    }
  };

  // 3. Handle Verify Registration OTP
  const handleVerifyOtp = async () => {
    if (enteredOtp.trim().length !== 6) {
      setErrorMsg('Please enter the 6-digit numeric verification code.');
      return;
    }
    setErrorMsg('');
    setIsSubmitting(true);
    try {
      if (screenView === 'register') {
        await verifyRegistration(email.trim(), enteredOtp.trim(), password, fullName.trim());
        setShowOtpDialog(false);
        setScreenView('onboarding');
      } else if (screenView === 'forgot') {
        await api.forgotPasswordVerify(email.trim(), enteredOtp.trim(), newPassword);
        setShowOtpDialog(false);
        setStatusMsg('Password reset successfully! Please sign in.');
        setScreenView('login');
      }
    } catch (err) {
      setErrorMsg(err.message || 'Invalid or expired OTP code.');
    } finally {
      setIsSubmitting(false);
    }
  };

  // 4. Handle Start Forgot Password
  const handleStartForgot = async (e) => {
    e.preventDefault();
    setErrorMsg('');
    setIsSubmitting(true);
    try {
      const res = await api.forgotPasswordStart(email.trim());
      setStatusMsg(res?.message || 'Password reset OTP dispatched to your email!');
      setShowOtpDialog(true);
    } catch (err) {
      setErrorMsg(err.message || 'Failed to send reset code.');
    } finally {
      setIsSubmitting(false);
    }
  };

  // 5. Complete Career Onboarding
  const handleFinishOnboarding = () => {
    updateUser({
      fullName: fullName || 'Candidate',
      targetRole: targetRole,
    });
    if (onAuthSuccess) onAuthSuccess();
  };

  return (
    <div style={{ minHeight: '100vh', width: '100vw', display: 'flex', alignItems: 'center', justifyContent: 'center', background: 'var(--app-bg)', padding: '24px', boxSizing: 'border-box' }}>
      
      {/* =========================================================================
          VIEW 1: LOGIN SCREEN (1:1 Android LoginScreen.kt)
          ========================================================================= */}
      {screenView === 'login' && (
        <div style={{ width: '100%', maxWidth: '440px', display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
          <AIOrb style={{ width: '90px', height: '90px', marginBottom: '20px' }} />

          <h1 style={{ fontSize: '1.75rem', fontWeight: 800, color: 'var(--app-text)', margin: '0 0 6px 0', textAlign: 'center', letterSpacing: '-0.02em' }}>
            Welcome to JobPilot
          </h1>
          <p style={{ fontSize: '0.875rem', color: 'var(--app-text-secondary)', margin: '0 0 24px 0', textAlign: 'center' }}>
            Log in to pilot your career opportunities
          </p>

          <div className="client-card client-card-glow" style={{ width: '100%', padding: '32px', boxSizing: 'border-box' }}>
            {/* Top Tab Toggle: Sign In / Create Profile */}
            <div className="client-tabs">
              <button
                type="button"
                onClick={() => switchScreen('login')}
                className="client-tab-btn active"
              >
                Sign In
              </button>
              <button
                type="button"
                onClick={() => switchScreen('register')}
                className="client-tab-btn"
              >
                Create Account
              </button>
            </div>

            {errorMsg && (
              <div style={{ padding: '12px 14px', background: 'var(--app-danger-bg)', border: '1px solid rgba(239, 68, 68, 0.3)', borderRadius: '10px', color: '#DC2626', fontSize: '0.8125rem', marginBottom: '18px', display: 'flex', alignItems: 'center', gap: '8px' }}>
                <span>✕</span>
                <span>{errorMsg}</span>
              </div>
            )}

            {statusMsg && (
              <div style={{ padding: '12px 14px', background: 'var(--app-success-bg)', border: '1px solid rgba(16, 185, 129, 0.3)', borderRadius: '10px', color: '#047857', fontSize: '0.8125rem', marginBottom: '18px', display: 'flex', alignItems: 'center', gap: '8px' }}>
                <CheckCircle2 size={16} />
                <span>{statusMsg}</span>
              </div>
            )}

            <form onSubmit={handleLogin} style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
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
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '6px' }}>
                  <label style={{ fontSize: '0.8125rem', fontWeight: 700, color: 'var(--app-text-secondary)' }}>
                    Password
                  </label>
                  <button
                    type="button"
                    onClick={() => switchScreen('forgot')}
                    style={{ background: 'transparent', border: 'none', color: 'var(--app-orange)', fontSize: '0.8125rem', fontWeight: 700, cursor: 'pointer', padding: 0 }}
                  >
                    Forgot password?
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
                disabled={isSubmitting || !email.trim() || !password}
                className="client-btn-primary"
                style={{ width: '100%', marginTop: '8px' }}
              >
                <span>{isSubmitting ? 'Signing in...' : 'Sign In'}</span>
                <ArrowRight size={18} />
              </button>
            </form>
          </div>

          <div style={{ marginTop: '24px', textAlign: 'center', fontSize: '0.875rem', color: 'var(--app-text-secondary)' }}>
            <span>Don't have an account? </span>
            <button
              type="button"
              onClick={() => switchScreen('register')}
              style={{ background: 'transparent', border: 'none', color: 'var(--app-orange)', fontWeight: 800, cursor: 'pointer', padding: 0, fontSize: '0.875rem' }}
            >
              Create Profile
            </button>
          </div>
        </div>
      )}

      {/* =========================================================================
          VIEW 2: REGISTER SCREEN (1:1 Android RegisterScreen.kt)
          ========================================================================= */}
      {screenView === 'register' && (
        <div style={{ width: '100%', maxWidth: '440px', display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
          <AIOrb style={{ width: '80px', height: '80px', marginBottom: '18px' }} />

          <h1 style={{ fontSize: '1.75rem', fontWeight: 800, color: 'var(--app-text)', margin: '0 0 6px 0', textAlign: 'center', letterSpacing: '-0.02em' }}>
            Create Your Profile
          </h1>
          <p style={{ fontSize: '0.875rem', color: 'var(--app-text-secondary)', margin: '0 0 24px 0', textAlign: 'center' }}>
            Verify with email OTP to begin your AI career journey
          </p>

          <div className="client-card client-card-glow" style={{ width: '100%', padding: '32px', boxSizing: 'border-box' }}>
            {/* Top Tab Toggle: Sign In / Create Profile */}
            <div className="client-tabs">
              <button
                type="button"
                onClick={() => switchScreen('login')}
                className="client-tab-btn"
              >
                Sign In
              </button>
              <button
                type="button"
                onClick={() => switchScreen('register')}
                className="client-tab-btn active"
              >
                Create Account
              </button>
            </div>

            {errorMsg && (
              <div style={{ padding: '12px 14px', background: 'var(--app-danger-bg)', border: '1px solid rgba(239, 68, 68, 0.3)', borderRadius: '10px', color: '#DC2626', fontSize: '0.8125rem', marginBottom: '18px', display: 'flex', alignItems: 'center', gap: '8px' }}>
                <span>✕</span>
                <span>{errorMsg}</span>
              </div>
            )}

            <form onSubmit={handleStartRegister} style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
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
                  College / Personal Email
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
                  Password (min 6 characters)
                </label>
                <input
                  type="password"
                  required
                  minLength={6}
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  className="client-input"
                  placeholder="••••••••"
                />
              </div>

              <button
                type="submit"
                disabled={isSubmitting || !fullName.trim() || !email.trim() || password.length < 6}
                className="client-btn-primary"
                style={{ width: '100%', marginTop: '8px' }}
              >
                <Mail size={18} />
                <span>{isSubmitting ? 'Sending OTP Code...' : 'Send Email Verification OTP'}</span>
              </button>
            </form>
          </div>

          <div style={{ marginTop: '24px', textAlign: 'center', fontSize: '0.875rem', color: 'var(--app-text-secondary)' }}>
            <span>Already have an account? </span>
            <button
              type="button"
              onClick={() => switchScreen('login')}
              style={{ background: 'transparent', border: 'none', color: 'var(--app-orange)', fontWeight: 800, cursor: 'pointer', padding: 0, fontSize: '0.875rem' }}
            >
              Sign In
            </button>
          </div>
        </div>
      )}

      {/* =========================================================================
          VIEW 3: FORGOT PASSWORD SCREEN (1:1 Android ForgotPasswordScreen.kt)
          ========================================================================= */}
      {screenView === 'forgot' && (
        <div style={{ width: '100%', maxWidth: '440px', display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
          <AIOrb style={{ width: '80px', height: '80px', marginBottom: '18px' }} />

          <h1 style={{ fontSize: '1.75rem', fontWeight: 800, color: 'var(--app-text)', margin: '0 0 6px 0', textAlign: 'center', letterSpacing: '-0.02em' }}>
            Reset Password
          </h1>
          <p style={{ fontSize: '0.875rem', color: 'var(--app-text-secondary)', margin: '0 0 24px 0', textAlign: 'center' }}>
            Enter your registered email to receive a password reset verification code
          </p>

          <div className="client-card client-card-glow" style={{ width: '100%', padding: '32px', boxSizing: 'border-box' }}>
            {errorMsg && (
              <div style={{ padding: '12px 14px', background: 'var(--app-danger-bg)', border: '1px solid rgba(239, 68, 68, 0.3)', borderRadius: '10px', color: '#DC2626', fontSize: '0.8125rem', marginBottom: '18px' }}>
                ✕ {errorMsg}
              </div>
            )}

            <form onSubmit={handleStartForgot} style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
              <div>
                <label style={{ display: 'block', fontSize: '0.8125rem', fontWeight: 700, marginBottom: '6px', color: 'var(--app-text-secondary)' }}>
                  Registered Email Address
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
                  New Password (min 6 characters)
                </label>
                <input
                  type="password"
                  required
                  minLength={6}
                  value={newPassword}
                  onChange={(e) => setNewPassword(e.target.value)}
                  className="client-input"
                  placeholder="••••••••"
                />
              </div>

              <div style={{ display: 'flex', gap: '12px', marginTop: '8px' }}>
                <button
                  type="button"
                  onClick={() => switchScreen('login')}
                  className="client-btn-secondary"
                  style={{ flex: 1 }}
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  disabled={isSubmitting || !email.trim() || newPassword.length < 6}
                  className="client-btn-primary"
                  style={{ flex: 1.5 }}
                >
                  <span>Send Reset OTP</span>
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* =========================================================================
          VIEW 4: CAREER PROFILE ONBOARDING (1:1 Android CareerProfileOnboardingScreen.kt)
          ========================================================================= */}
      {screenView === 'onboarding' && (
        <div style={{ width: '100%', maxWidth: '520px', display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
          <div style={{ width: '100%', display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
            {onboardingStep > 1 ? (
              <button
                onClick={() => setOnboardingStep((prev) => prev - 1)}
                className="client-btn-secondary"
                style={{ padding: '8px 14px' }}
              >
                <ArrowLeft size={16} />
                <span>Back</span>
              </button>
            ) : <div />}

            <span style={{ fontSize: '0.875rem', fontWeight: 700, color: 'var(--app-orange)' }}>
              Step {onboardingStep} of 3
            </span>

            <button
              onClick={handleFinishOnboarding}
              style={{ background: 'transparent', border: 'none', color: 'var(--app-text-muted)', fontWeight: 600, cursor: 'pointer', fontSize: '0.875rem' }}
            >
              Skip
            </button>
          </div>

          <AIOrb style={{ width: '70px', height: '70px', marginBottom: '16px' }} />

          {onboardingStep === 1 && (
            <div className="client-card client-card-glow" style={{ width: '100%', padding: '32px' }}>
              <h2 style={{ fontSize: '1.4rem', fontWeight: 800, margin: '0 0 6px 0', textAlign: 'center' }}>Tell Us About Yourself</h2>
              <p style={{ fontSize: '0.8125rem', color: 'var(--app-text-secondary)', margin: '0 0 24px 0', textAlign: 'center' }}>
                Set up your candidate identity for personalized career roadmaps
              </p>

              <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
                <div>
                  <label style={{ display: 'block', fontSize: '0.8125rem', fontWeight: 700, marginBottom: '6px', color: 'var(--app-text-secondary)' }}>
                    Full Name *
                  </label>
                  <input
                    type="text"
                    value={fullName}
                    onChange={(e) => setFullName(e.target.value)}
                    className="client-input"
                  />
                </div>

                <div>
                  <label style={{ display: 'block', fontSize: '0.8125rem', fontWeight: 700, marginBottom: '6px', color: 'var(--app-text-secondary)' }}>
                    Age
                  </label>
                  <input
                    type="number"
                    value={age}
                    onChange={(e) => setAge(e.target.value)}
                    className="client-input"
                  />
                </div>

                <div>
                  <label style={{ display: 'block', fontSize: '0.8125rem', fontWeight: 700, marginBottom: '6px', color: 'var(--app-text-secondary)' }}>
                    Target Engineering Role
                  </label>
                  <input
                    type="text"
                    value={targetRole}
                    onChange={(e) => setTargetRole(e.target.value)}
                    className="client-input"
                  />
                </div>

                <button
                  onClick={() => setOnboardingStep(2)}
                  disabled={!fullName.trim()}
                  className="client-btn-primary"
                  style={{ width: '100%', marginTop: '8px' }}
                >
                  <span>Next: Academic Background</span>
                  <ArrowRight size={18} />
                </button>
              </div>
            </div>
          )}

          {onboardingStep === 2 && (
            <div className="client-card client-card-glow" style={{ width: '100%', padding: '32px' }}>
              <h2 style={{ fontSize: '1.4rem', fontWeight: 800, margin: '0 0 6px 0', textAlign: 'center' }}>Academic Background</h2>
              <p style={{ fontSize: '0.8125rem', color: 'var(--app-text-secondary)', margin: '0 0 24px 0', textAlign: 'center' }}>
                Helps AI customize roadmaps and job matches to your degree level
              </p>

              <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
                <div>
                  <label style={{ display: 'block', fontSize: '0.8125rem', fontWeight: 700, marginBottom: '6px', color: 'var(--app-text-secondary)' }}>
                    College / University *
                  </label>
                  <input
                    type="text"
                    value={college}
                    onChange={(e) => setCollege(e.target.value)}
                    className="client-input"
                  />
                </div>

                <div>
                  <label style={{ display: 'block', fontSize: '0.8125rem', fontWeight: 700, marginBottom: '6px', color: 'var(--app-text-secondary)' }}>
                    Degree (e.g., B.Tech, MCA, B.S.)
                  </label>
                  <input
                    type="text"
                    value={degree}
                    onChange={(e) => setDegree(e.target.value)}
                    className="client-input"
                  />
                </div>

                <div>
                  <label style={{ display: 'block', fontSize: '0.8125rem', fontWeight: 700, marginBottom: '6px', color: 'var(--app-text-secondary)' }}>
                    Branch / Major
                  </label>
                  <input
                    type="text"
                    value={branch}
                    onChange={(e) => setBranch(e.target.value)}
                    className="client-input"
                  />
                </div>

                <button
                  onClick={() => setOnboardingStep(3)}
                  className="client-btn-primary"
                  style={{ width: '100%', marginTop: '8px' }}
                >
                  <span>Next: Resume Setup</span>
                  <ArrowRight size={18} />
                </button>
              </div>
            </div>
          )}

          {onboardingStep === 3 && (
            <div className="client-card client-card-glow" style={{ width: '100%', padding: '32px' }}>
              <h2 style={{ fontSize: '1.4rem', fontWeight: 800, margin: '0 0 6px 0', textAlign: 'center' }}>Optional: Add Your Resume</h2>
              <p style={{ fontSize: '0.8125rem', color: 'var(--app-text-secondary)', margin: '0 0 24px 0', textAlign: 'center' }}>
                Upload an existing resume or create a fresh one anytime
              </p>

              <div style={{ display: 'flex', flexDirection: 'column', gap: '14px', marginBottom: '24px' }}>
                <div
                  onClick={handleFinishOnboarding}
                  className="client-card"
                  style={{ padding: '16px', display: 'flex', alignItems: 'center', gap: '16px', cursor: 'pointer', border: '1.5px solid #CBD5E1' }}
                >
                  <div style={{ width: '44px', height: '44px', borderRadius: '12px', background: 'var(--app-orange-light)', color: 'var(--app-orange)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                    <Upload size={22} />
                  </div>
                  <div>
                    <h4 style={{ margin: '0 0 2px 0', fontSize: '0.9375rem', fontWeight: 800 }}>Upload Existing Resume</h4>
                    <span style={{ fontSize: '0.75rem', color: 'var(--app-text-secondary)' }}>PDF or DOCX with instant NLP extraction</span>
                  </div>
                </div>

                <div
                  onClick={handleFinishOnboarding}
                  className="client-card"
                  style={{ padding: '16px', display: 'flex', alignItems: 'center', gap: '16px', cursor: 'pointer', border: '1.5px solid #CBD5E1' }}
                >
                  <div style={{ width: '44px', height: '44px', borderRadius: '12px', background: 'var(--app-orange-light)', color: 'var(--app-orange)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                    <FileText size={22} />
                  </div>
                  <div>
                    <h4 style={{ margin: '0 0 2px 0', fontSize: '0.9375rem', fontWeight: 800 }}>Create Resume with AI</h4>
                    <span style={{ fontSize: '0.75rem', color: 'var(--app-text-secondary)' }}>ATS-friendly templates crafted in minutes</span>
                  </div>
                </div>
              </div>

              <button
                onClick={handleFinishOnboarding}
                className="client-btn-primary"
                style={{ width: '100%' }}
              >
                <Sparkles size={18} />
                <span>Finish & Launch Copilot</span>
              </button>
            </div>
          )}
        </div>
      )}

      {/* =========================================================================
          OTP VERIFICATION DIALOG MODAL (1:1 Android AlertDialog in RegisterScreen.kt)
          ========================================================================= */}
      {showOtpDialog && (
        <div className="client-modal-backdrop">
          <div className="client-card client-card-glow" style={{ width: '100%', maxWidth: '420px', padding: '32px', textAlign: 'center' }}>
            <div style={{ width: '52px', height: '52px', borderRadius: '16px', background: 'var(--app-orange-light)', color: 'var(--app-orange)', display: 'flex', alignItems: 'center', justifyContent: 'center', margin: '0 auto 16px auto' }}>
              <KeyRound size={26} />
            </div>

            <h3 style={{ fontSize: '1.35rem', fontWeight: 800, margin: '0 0 6px 0', color: 'var(--app-text)' }}>
              Verify Your Email
            </h3>
            <p style={{ fontSize: '0.875rem', color: 'var(--app-text-secondary)', margin: '0 0 20px 0', lineHeight: 1.5 }}>
              We sent a 6-digit verification code to:<br />
              <strong style={{ color: 'var(--app-text)' }}>{email}</strong>
            </p>

            {errorMsg && (
              <div style={{ padding: '10px 12px', background: 'var(--app-danger-bg)', border: '1px solid rgba(239, 68, 68, 0.3)', borderRadius: '8px', color: '#DC2626', fontSize: '0.8125rem', marginBottom: '16px' }}>
                ✕ {errorMsg}
              </div>
            )}

            <input
              type="text"
              maxLength={6}
              value={enteredOtp}
              onChange={(e) => setEnteredOtp(e.target.value.replace(/\D/g, ''))}
              className="client-input"
              style={{ fontSize: '1.75rem', letterSpacing: '0.4em', textAlign: 'center', fontWeight: 800, marginBottom: '12px' }}
              placeholder="••••••"
              autoFocus
            />

            <div style={{ display: 'flex', justifyContent: 'flex-end', marginBottom: '20px' }}>
              <button
                type="button"
                onClick={screenView === 'register' ? handleStartRegister : handleStartForgot}
                style={{ background: 'transparent', border: 'none', color: 'var(--app-orange)', fontSize: '0.8125rem', fontWeight: 700, cursor: 'pointer', padding: 0 }}
              >
                Resend Code
              </button>
            </div>

            <div style={{ display: 'flex', gap: '12px' }}>
              <button
                onClick={() => setShowOtpDialog(false)}
                className="client-btn-secondary"
                style={{ flex: 1 }}
              >
                Cancel
              </button>
              <button
                onClick={handleVerifyOtp}
                disabled={isSubmitting || enteredOtp.length !== 6}
                className="client-btn-primary"
                style={{ flex: 1.5 }}
              >
                <span>{isSubmitting ? 'Verifying...' : 'Verify & Continue'}</span>
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

