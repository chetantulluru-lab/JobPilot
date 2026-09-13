import React from 'react';
import { ShieldCheck } from 'lucide-react';
import SectionHeading from '../components/SectionHeading';
import FeatureCard from '../components/FeatureCard';
import PlayStoreButton from '../components/PlayStoreButton';
import GlowBackground from '../components/GlowBackground';
import GlassCard from '../components/GlassCard';
import { FEATURES } from '../data/features';

export default function Features() {
  return (
    <div className="page-features">
      {/* Header Section */}
      <section className="section" style={{ paddingTop: '60px', paddingBottom: '60px', position: 'relative' }}>
        <GlowBackground variant="top" />
        <div className="container">
          <SectionHeading
            eyebrow="Core Architecture & Capabilities"
            title="Everything you need to navigate"
            highlight="your next opportunity."
            subtitle="JobPilot blends Natural Language Processing with structured career modeling to bridge the gap between candidate resumes and modern company requirements."
          />
        </div>
      </section>

      {/* Feature Cards Grid */}
      <section className="section" style={{ paddingTop: '0', paddingBottom: '80px' }}>
        <div className="container">
          <div
            style={{
              display: 'grid',
              gridTemplateColumns: 'repeat(auto-fit, minmax(320px, 1fr))',
              gap: '28px',
            }}
          >
            {FEATURES.map((feature) => (
              <FeatureCard key={feature.id} feature={feature} />
            ))}
          </div>
        </div>
      </section>

      {/* Trust & Methodology Philosophy */}
      <section
        className="section"
        style={{
          background: 'linear-gradient(180deg, rgba(255, 255, 255, 0) 0%, rgba(255, 247, 237, 0.6) 50%, rgba(255, 255, 255, 0) 100%)',
        }}
      >
        <div className="container">
          <div style={{ maxWidth: '860px', margin: '0 auto' }}>
            <GlassCard elevated padding="40px">
              <div style={{ display: 'flex', alignItems: 'center', gap: '12px', marginBottom: '16px' }}>
                <span className="badge badge-orange">
                  <ShieldCheck size={14} />
                  <span>Ethical AI Principles</span>
                </span>
              </div>
              <h3 style={{ fontSize: '1.5rem', fontWeight: '800', marginBottom: '14px' }}>
                Designed with transparency and user sovereignty.
              </h3>
              <p style={{ color: 'var(--text-secondary)', lineHeight: '1.7', marginBottom: '18px' }}>
                We believe career guidance tools should empower candidates rather than treat their profiles like black boxes. JobPilot never submits an application without explicit user review, never invents credentials, and always clearly displays why an opportunity matches or where gaps exist.
              </p>
              <div
                style={{
                  display: 'grid',
                  gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))',
                  gap: '16px',
                  borderTop: '1px solid rgba(226, 232, 240, 0.8)',
                  paddingTop: '20px',
                }}
              >
                <div>
                  <div style={{ fontWeight: '700', fontSize: '0.9375rem', color: 'var(--text-primary)', marginBottom: '4px' }}>
                    Transparent Scoring
                  </div>
                  <div style={{ fontSize: '0.8125rem', color: 'var(--text-muted)' }}>
                    Detailed breakdown of matching proficiencies and gaps.
                  </div>
                </div>
                <div>
                  <div style={{ fontWeight: '700', fontSize: '0.9375rem', color: 'var(--text-primary)', marginBottom: '4px' }}>
                    User in the Loop
                  </div>
                  <div style={{ fontSize: '0.8125rem', color: 'var(--text-muted)' }}>
                    You inspect and approve all application details before submission.
                  </div>
                </div>
                <div>
                  <div style={{ fontWeight: '700', fontSize: '0.9375rem', color: 'var(--text-primary)', marginBottom: '4px' }}>
                    Mobile First
                  </div>
                  <div style={{ fontSize: '0.8125rem', color: 'var(--text-muted)' }}>
                    Built specifically for native performance on Android 11+.
                  </div>
                </div>
              </div>
            </GlassCard>
          </div>
        </div>
      </section>

      {/* Download CTA Banner */}
      <section className="section" style={{ paddingBottom: '100px' }}>
        <div className="container">
          <GlassCard
            elevated
            glow
            style={{
              padding: '48px 32px',
              textAlign: 'center',
              background: 'linear-gradient(135deg, #FFFFFF 0%, #FFF7ED 100%)',
              border: '1px solid rgba(255, 106, 0, 0.2)',
            }}
          >
            <h2 style={{ fontSize: '2rem', fontWeight: '800', marginBottom: '12px' }}>
              Experience all features on Android.
            </h2>
            <p style={{ color: 'var(--text-secondary)', maxWidth: '560px', margin: '0 auto 28px' }}>
              Download JobPilot to start building your verified career profile and explore matching roles right away.
            </p>
            <div style={{ display: 'flex', justifyContent: 'center' }}>
              <PlayStoreButton />
            </div>
          </GlassCard>
        </div>
      </section>
    </div>
  );
}
