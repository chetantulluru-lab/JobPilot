import React from 'react';
import { 
  Smartphone, 
  Monitor, 
  CheckCircle2, 
  Compass, 
  ShieldCheck, 
  Sparkles, 
  Cpu, 
  Video, 
  Zap, 
  Download as DownloadIcon 
} from 'lucide-react';
import PlayStoreButton from '../components/PlayStoreButton';
import ApkDownloadButton from '../components/ApkDownloadButton';
import WindowsDownloadButton from '../components/WindowsDownloadButton';
import PhoneMockup from '../components/PhoneMockup';
import LaptopMockup from '../components/LaptopMockup';
import QRPlaceholder from '../components/QRPlaceholder';
import SectionHeading from '../components/SectionHeading';
import GlassCard from '../components/GlassCard';
import GlowBackground from '../components/GlowBackground';
import Button from '../components/Button';
import { APP_CONFIG } from '../config/appConfig';

export default function Download() {
  const mobileHighlights = [
    'Instant NLP Resume Extraction & Parsing on device',
    'AI Mock Interview with front-camera ML Kit Face Detection',
    '1-Click AI Resume Tailoring for matching jobs',
    'Recruiter Cold Outreach & Cover Letter generation',
    'Gamified roadmap daily quizzes with streak flames 🔥',
    'Personal note taking & roadmap bookmark hub',
    '100% Free OTP Email Registration (Resend/Brevo API)',
  ];

  const desktopHighlights = [
    'Native Windows Chromium runtime with full 3D WebGL acceleration',
    'Full-screen AI Mock Interview Simulator with high-res webcam tracking',
    'Split-screen live coding & voice dictation response preview',
    'One-click PDF resume download & instant clipboard copying',
    'Official Windows Setup Wizard with Start Menu & Desktop shortcuts',
    'Direct connection to production Render cloud backend',
  ];

  const androidSpecs = [
    { label: 'Target Platform', value: 'Android 11.0+ (API 30+)' },
    { label: 'Package Format', value: 'Signed Release APK (JobPilot.apk)' },
    { label: 'File Size', value: APP_CONFIG.APK_FILE_SIZE },
    { label: 'Permissions', value: 'Camera (Face Detection), Microphone (Voice)' },
  ];

  const windowsSpecs = [
    { label: 'Target Platform', value: 'Windows 10 / Windows 11 (64-bit)' },
    { label: 'Package Format', value: 'Windows Setup Installer (JobPilot-Setup.exe)' },
    { label: 'File Size', value: APP_CONFIG.EXE_FILE_SIZE },
    { label: 'Runtime Engine', value: 'Microsoft Edge WebView2 (Chromium)' },
  ];

  return (
    <div className="page-download">
      {/* ====================================================================
          HERO DOWNLOAD SECTION
          ==================================================================== */}
      <section className="section" style={{ paddingTop: '50px', paddingBottom: '90px', position: 'relative' }}>
        <GlowBackground variant="top" />

        <div className="container">
          {/* Header Badges & Title */}
          <div style={{ textAlign: 'center', maxWidth: '820px', margin: '0 auto 48px' }}>
            <div style={{ display: 'inline-flex', gap: '8px', marginBottom: '18px', flexWrap: 'wrap', justifyContent: 'center' }}>
              <span className="badge badge-orange">
                <Sparkles size={14} />
                <span>Cross-Platform AI Suite</span>
              </span>
              <span className="badge badge-glass">
                <Smartphone size={14} />
                <span>Android APK</span>
              </span>
              <span className="badge badge-glass">
                <Monitor size={14} />
                <span>Windows App (.EXE)</span>
              </span>
            </div>

            <h1
              style={{
                fontSize: 'clamp(2.4rem, 4.8vw, 3.85rem)',
                fontWeight: '800',
                letterSpacing: '-0.035em',
                lineHeight: '1.1',
                marginBottom: '20px',
              }}
            >
              Download JobPilot{' '}
              <span className="text-gradient-orange">Everywhere.</span>
            </h1>

            <p
              style={{
                fontSize: '1.15rem',
                color: 'var(--text-secondary)',
                lineHeight: '1.6',
                maxWidth: '680px',
                margin: '0 auto',
              }}
            >
              Choose your platform: Take your career assistant on your Android phone or run the full-screen AI Mock Interview Simulator on your Windows PC.
            </p>
          </div>

          {/* DUAL DOWNLOAD CARDS (Android vs Windows) */}
          <div
            style={{
              display: 'grid',
              gridTemplateColumns: 'repeat(auto-fit, minmax(min(100%, 480px), 1fr))',
              gap: '32px',
              marginBottom: '70px',
            }}
          >
            {/* Card 1: Android Mobile Release */}
            <GlassCard
              elevated
              glow
              className="card-3d"
              style={{
                padding: 'clamp(24px, 4vw, 36px)',
                display: 'flex',
                flexDirection: 'column',
                justifyContent: 'space-between',
                border: '1.5px solid rgba(255, 106, 0, 0.25)',
                background: 'linear-gradient(135deg, rgba(255, 255, 255, 0.95) 0%, rgba(255, 247, 237, 0.85) 100%)',
              }}
            >
              <div>
                <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '16px' }}>
                  <div
                    style={{
                      width: '46px',
                      height: '46px',
                      borderRadius: '14px',
                      background: 'linear-gradient(135deg, #FF6A00 0%, #FF8533 100%)',
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                      color: '#FFFFFF',
                      boxShadow: '0 8px 20px -4px rgba(255, 106, 0, 0.4)',
                    }}
                  >
                    <Smartphone size={24} />
                  </div>
                  <span className="badge badge-orange">Official Signed APK</span>
                </div>

                <h3 style={{ fontSize: '1.5rem', fontWeight: '800', marginBottom: '8px' }}>
                  JobPilot for Android
                </h3>
                <p style={{ color: 'var(--text-secondary)', fontSize: '0.9375rem', lineHeight: '1.5', marginBottom: '24px' }}>
                  Full mobile application with Material 3 design, offline profile storage, front-camera face tracking, and career roadmaps.
                </p>

                {/* Mobile Feature Checklist */}
                <div style={{ display: 'flex', flexDirection: 'column', gap: '10px', marginBottom: '28px' }}>
                  {mobileHighlights.map((item, idx) => (
                    <div key={idx} style={{ display: 'flex', alignItems: 'center', gap: '10px', fontSize: '0.875rem', color: 'var(--text-secondary)' }}>
                      <span style={{ color: '#10B981', display: 'flex', flexShrink: 0 }}>
                        <CheckCircle2 size={16} />
                      </span>
                      <span>{item}</span>
                    </div>
                  ))}
                </div>
              </div>

              <div>
                <div style={{ display: 'flex', flexWrap: 'wrap', gap: '14px', alignItems: 'center', marginBottom: '20px' }}>
                  <ApkDownloadButton
                    size="lg"
                    label="Download Android APK"
                    subtext={`Direct Install • Android 11+ (${APP_CONFIG.APK_FILE_SIZE})`}
                  />
                  <PlayStoreButton />
                </div>

                {/* Scan to Phone QR */}
                <div
                  style={{
                    display: 'flex',
                    alignItems: 'center',
                    gap: '16px',
                    padding: '14px 18px',
                    background: 'rgba(255, 255, 255, 0.8)',
                    borderRadius: 'var(--radius-md)',
                    border: '1px solid rgba(226, 232, 240, 0.8)',
                  }}
                >
                  <QRPlaceholder size={72} caption="Scan APK" />
                  <div>
                    <div style={{ fontSize: '0.875rem', fontWeight: '700', color: 'var(--text-primary)' }}>
                      Scan to Install on Phone
                    </div>
                    <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)', marginTop: '2px' }}>
                      Point your mobile camera to download the APK directly to your Android device.
                    </div>
                  </div>
                </div>
              </div>
            </GlassCard>

            {/* Card 2: Windows Desktop Release (.EXE) */}
            <GlassCard
              elevated
              glow
              className="card-3d"
              style={{
                padding: 'clamp(24px, 4vw, 36px)',
                display: 'flex',
                flexDirection: 'column',
                justifyContent: 'space-between',
                border: '1.5px solid rgba(15, 23, 42, 0.15)',
                background: 'linear-gradient(135deg, rgba(255, 255, 255, 0.95) 0%, rgba(241, 245, 249, 0.85) 100%)',
              }}
            >
              <div>
                <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '16px' }}>
                  <div
                    style={{
                      width: '46px',
                      height: '46px',
                      borderRadius: '14px',
                      background: 'linear-gradient(135deg, #0F172A 0%, #334155 100%)',
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                      color: '#FFFFFF',
                      boxShadow: '0 8px 20px -4px rgba(15, 23, 42, 0.3)',
                    }}
                  >
                    <Monitor size={24} />
                  </div>
                  <span className="badge badge-neutral" style={{ background: '#0F172A', color: '#FFFFFF' }}>
                    Official Setup Installer (.EXE)
                  </span>
                </div>

                <h3 style={{ fontSize: '1.5rem', fontWeight: '800', marginBottom: '8px' }}>
                  JobPilot for Windows
                </h3>
                <p style={{ color: 'var(--text-secondary)', fontSize: '0.9375rem', lineHeight: '1.5', marginBottom: '24px' }}>
                  Dedicated desktop executable with full-screen interview experience, webcam face tracking, instant PDF export, and seamless backend sync.
                </p>

                {/* Desktop Feature Checklist */}
                <div style={{ display: 'flex', flexDirection: 'column', gap: '10px', marginBottom: '28px' }}>
                  {desktopHighlights.map((item, idx) => (
                    <div key={idx} style={{ display: 'flex', alignItems: 'center', gap: '10px', fontSize: '0.875rem', color: 'var(--text-secondary)' }}>
                      <span style={{ color: '#0284C7', display: 'flex', flexShrink: 0 }}>
                        <CheckCircle2 size={16} />
                      </span>
                      <span>{item}</span>
                    </div>
                  ))}
                </div>
              </div>

              <div>
                <div style={{ display: 'flex', flexWrap: 'wrap', gap: '14px', alignItems: 'center', marginBottom: '20px' }}>
                  <WindowsDownloadButton
                    size="lg"
                    variant="dark"
                    label="Download Windows Installer"
                    subtext={`Official Setup • Win 10/11 (${APP_CONFIG.EXE_FILE_SIZE})`}
                  />
                  <Button
                    to="/how-it-works"
                    variant="secondary"
                    size="lg"
                    icon={<Compass size={18} />}
                    iconPosition="left"
                  >
                    Tour Features
                  </Button>
                </div>

                {/* Setup Installer Note */}
                <div
                  style={{
                    padding: '14px 18px',
                    background: 'rgba(255, 255, 255, 0.8)',
                    borderRadius: 'var(--radius-md)',
                    border: '1px solid rgba(226, 232, 240, 0.8)',
                  }}
                >
                  <div style={{ fontSize: '0.875rem', fontWeight: '700', color: 'var(--text-primary)' }}>
                    📦 Official Setup Wizard
                  </div>
                  <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)', marginTop: '2px' }}>
                    Standard Windows Installer with Start Menu & Desktop shortcuts. Download, run <code style={{ color: '#FF6A00' }}>JobPilot-Setup.exe</code>, and launch the application.
                  </div>
                </div>
              </div>
            </GlassCard>
          </div>

          {/* DUAL 3D DEVICE SHOWCASE */}
          <div style={{ marginTop: '40px', marginBottom: '80px' }}>
            <SectionHeading
              eyebrow="Dual Device Synergy"
              title="Experience JobPilot on"
              highlight="Mobile & Desktop"
              subtitle="Seamlessly transition between interview prep on your laptop and daily roadmap progress on your mobile phone."
            />

            <div
              style={{
                display: 'grid',
                gridTemplateColumns: 'repeat(auto-fit, minmax(min(100%, 360px), 1fr))',
                gap: '40px',
                alignItems: 'center',
                marginTop: '32px',
              }}
            >
              <div style={{ display: 'flex', justifyContent: 'center' }}>
                <PhoneMockup />
              </div>
              <div style={{ display: 'flex', justifyContent: 'center' }}>
                <LaptopMockup />
              </div>
            </div>
          </div>

          {/* DUAL SPECIFICATIONS TABLE */}
          <div>
            <SectionHeading
              eyebrow="Technical Compatibility"
              title="Device & System"
              highlight="Specifications"
              subtitle="Engineered for high performance, low latency, and zero bloatware across both platforms."
            />

            <div
              style={{
                display: 'grid',
                gridTemplateColumns: 'repeat(auto-fit, minmax(min(100%, 380px), 1fr))',
                gap: '24px',
                maxWidth: '960px',
                margin: '0 auto',
              }}
            >
              {/* Android Specs */}
              <GlassCard elevated padding="clamp(20px, 3vw, 28px)">
                <div style={{ display: 'flex', alignItems: 'center', gap: '10px', marginBottom: '16px' }}>
                  <Smartphone size={20} color="#FF6A00" />
                  <h4 style={{ fontSize: '1.1rem', fontWeight: '800' }}>Android Specifications</h4>
                </div>
                <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
                  {androidSpecs.map((spec, idx) => (
                    <div key={idx} style={{ borderBottom: '1px solid rgba(226, 232, 240, 0.6)', paddingBottom: '10px' }}>
                      <div style={{ fontSize: '0.75rem', textTransform: 'uppercase', letterSpacing: '0.05em', color: 'var(--text-muted)', fontWeight: '700' }}>
                        {spec.label}
                      </div>
                      <div style={{ fontSize: '0.9375rem', fontWeight: '700', color: 'var(--text-primary)', marginTop: '2px' }}>
                        {spec.value}
                      </div>
                    </div>
                  ))}
                </div>
              </GlassCard>

              {/* Windows Specs */}
              <GlassCard elevated padding="clamp(20px, 3vw, 28px)">
                <div style={{ display: 'flex', alignItems: 'center', gap: '10px', marginBottom: '16px' }}>
                  <Monitor size={20} color="#0284C7" />
                  <h4 style={{ fontSize: '1.1rem', fontWeight: '800' }}>Windows Specifications</h4>
                </div>
                <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
                  {windowsSpecs.map((spec, idx) => (
                    <div key={idx} style={{ borderBottom: '1px solid rgba(226, 232, 240, 0.6)', paddingBottom: '10px' }}>
                      <div style={{ fontSize: '0.75rem', textTransform: 'uppercase', letterSpacing: '0.05em', color: 'var(--text-muted)', fontWeight: '700' }}>
                        {spec.label}
                      </div>
                      <div style={{ fontSize: '0.9375rem', fontWeight: '700', color: 'var(--text-primary)', marginTop: '2px' }}>
                        {spec.value}
                      </div>
                    </div>
                  ))}
                </div>
              </GlassCard>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
}
