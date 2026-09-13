import { Smartphone, CheckCircle2, Compass } from 'lucide-react';
import PlayStoreButton from '../components/PlayStoreButton';
import PhoneMockup from '../components/PhoneMockup';
import QRPlaceholder from '../components/QRPlaceholder';
import SectionHeading from '../components/SectionHeading';
import GlassCard from '../components/GlassCard';
import GlowBackground from '../components/GlowBackground';
import Button from '../components/Button';

export default function Download() {
  const highlights = [
    'Instant NLP Resume Extraction and parsing',
    'Transparent 0-100% semantic job match calculation',
    'Skill gap diagnosis with clear learning recommendations',
    'End-to-end application pipeline tracking on mobile',
    'Completely free for students and early career job seekers',
  ];

  const systemSpecs = [
    { label: 'Platform', value: 'Android 11.0+ (Red Velvet Cake / API 30+)' },
    { label: 'Architecture', value: 'Optimized for ARM64 & modern Android devices' },
    { label: 'Category', value: 'Productivity & Career Guidance' },
    { label: 'Distribution', value: 'Official Google Play Store' },
  ];

  return (
    <div className="page-download">
      <section className="section" style={{ paddingTop: '50px', paddingBottom: '90px', position: 'relative' }}>
        <GlowBackground variant="top" />

        <div className="container">
          <div
            style={{
              display: 'grid',
              gridTemplateColumns: 'repeat(auto-fit, minmax(320px, 1fr))',
              gap: '64px',
              alignItems: 'center',
            }}
          >
            {/* Download Content Left */}
            <div>
              {/* Android 11+ Badge */}
              <div style={{ display: 'flex', gap: '8px', marginBottom: '18px' }}>
                <span className="badge badge-orange">
                  <Smartphone size={14} />
                  <span>Android 11+</span>
                </span>
                <span className="badge badge-glass">
                  <span>Official Release</span>
                </span>
              </div>

              <h1
                style={{
                  fontSize: 'clamp(2.5rem, 4.5vw, 3.75rem)',
                  fontWeight: '800',
                  letterSpacing: '-0.035em',
                  lineHeight: '1.1',
                  marginBottom: '20px',
                }}
              >
                Meet JobPilot{' '}
                <span className="text-gradient-orange">on Android.</span>
              </h1>

              <p
                style={{
                  fontSize: '1.125rem',
                  color: 'var(--text-secondary)',
                  lineHeight: '1.6',
                  marginBottom: '32px',
                }}
              >
                Get the app and take your career assistant with you. Transform your resume into structured career intelligence and find matching opportunities anywhere, anytime.
              </p>

              {/* Primary and Secondary CTAs */}
              <div
                style={{
                  display: 'flex',
                  flexWrap: 'wrap',
                  gap: '16px',
                  alignItems: 'center',
                  marginBottom: '36px',
                }}
              >
                <PlayStoreButton />

                <Button
                  to="/how-it-works"
                  variant="secondary"
                  size="lg"
                  icon={<Compass size={18} />}
                  iconPosition="left"
                >
                  Learn How JobPilot Works
                </Button>
              </div>

              {/* Feature Highlights List */}
              <div style={{ marginBottom: '36px' }}>
                <div
                  style={{
                    fontSize: '0.8125rem',
                    fontWeight: '700',
                    textTransform: 'uppercase',
                    letterSpacing: '0.05em',
                    color: 'var(--text-muted)',
                    marginBottom: '14px',
                  }}
                >
                  Key Mobile Capabilities
                </div>
                <div style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
                  {highlights.map((item, idx) => (
                    <div key={idx} style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                      <span style={{ color: '#10B981', display: 'flex' }}>
                        <CheckCircle2 size={16} />
                      </span>
                      <span style={{ fontSize: '0.9375rem', color: 'var(--text-secondary)' }}>
                        {item}
                      </span>
                    </div>
                  ))}
                </div>
              </div>

              {/* QR Code & Scan To Download Section */}
              <GlassCard
                padding="20px"
                style={{
                  display: 'flex',
                  alignItems: 'center',
                  gap: '24px',
                  maxWidth: '440px',
                }}
              >
                <QRPlaceholder size={110} caption="Scan to download" />
                <div>
                  <div style={{ fontSize: '0.9375rem', fontWeight: '700', color: 'var(--text-primary)', marginBottom: '4px' }}>
                    Scan with your Android camera
                  </div>
                  <p style={{ fontSize: '0.8125rem', color: 'var(--text-muted)', lineHeight: '1.4' }}>
                    Point your phone camera to open the JobPilot listing on Google Play directly.
                  </p>
                </div>
              </GlassCard>
            </div>

            {/* Android Phone Mockup Right */}
            <div style={{ display: 'flex', justifyContent: 'center' }}>
              <PhoneMockup />
            </div>
          </div>
        </div>
      </section>

      {/* System Requirements & Specifications */}
      <section
        className="section"
        style={{
          background: 'linear-gradient(180deg, rgba(255, 255, 255, 0) 0%, rgba(255, 247, 237, 0.5) 50%, rgba(255, 255, 255, 0) 100%)',
          paddingTop: '60px',
          paddingBottom: '90px',
        }}
      >
        <div className="container">
          <div style={{ maxWidth: '800px', margin: '0 auto' }}>
            <SectionHeading
              eyebrow="Device Compatibility"
              title="System Requirements &"
              highlight="Specifications"
              subtitle="JobPilot is engineered specifically for modern Android environments with efficient background sync and offline-friendly profile viewing."
            />

            <GlassCard elevated padding="32px">
              <div
                style={{
                  display: 'grid',
                  gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))',
                  gap: '24px',
                }}
              >
                {systemSpecs.map((spec, idx) => (
                  <div key={idx} style={{ borderBottom: '1px solid rgba(226, 232, 240, 0.7)', paddingBottom: '14px' }}>
                    <div style={{ fontSize: '0.75rem', textTransform: 'uppercase', letterSpacing: '0.05em', color: 'var(--text-muted)', fontWeight: '700', marginBottom: '4px' }}>
                      {spec.label}
                    </div>
                    <div style={{ fontSize: '0.9375rem', fontWeight: '700', color: 'var(--text-primary)' }}>
                      {spec.value}
                    </div>
                  </div>
                ))}
              </div>
            </GlassCard>
          </div>
        </div>
      </section>
    </div>
  );
}
