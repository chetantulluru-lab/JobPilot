import React from 'react';
import SectionHeading from '../components/SectionHeading';
import StepCard from '../components/StepCard';
import PlayStoreButton from '../components/PlayStoreButton';
import GlowBackground from '../components/GlowBackground';
import GlassCard from '../components/GlassCard';
import { HOW_IT_WORKS_STEPS } from '../data/steps';

export default function HowItWorks() {
  return (
    <div className="page-how-it-works">
      {/* Header Section */}
      <section className="section" style={{ paddingTop: '60px', paddingBottom: '60px', position: 'relative' }}>
        <GlowBackground variant="top" />
        <div className="container">
          <SectionHeading
            eyebrow="Step-by-Step Walkthrough"
            title="How JobPilot pilots your"
            highlight="career search."
            subtitle="From raw resume parsing to intelligent matching and application tracking, explore how each stage works seamlessly on your Android device."
          />
        </div>
      </section>

      {/* Timeline Section */}
      <section className="section" style={{ paddingTop: '0', paddingBottom: '80px' }}>
        <div className="container">
          <div className="timeline-track">
            {HOW_IT_WORKS_STEPS.map((step, idx) => (
              <StepCard
                key={step.step}
                stepItem={step}
                isLast={idx === HOW_IT_WORKS_STEPS.length - 1}
              />
            ))}
          </div>
        </div>
      </section>

      {/* Philosophy Callout */}
      <section
        className="section"
        style={{
          background: 'linear-gradient(180deg, rgba(255, 255, 255, 0) 0%, rgba(255, 247, 237, 0.5) 50%, rgba(255, 255, 255, 0) 100%)',
        }}
      >
        <div className="container">
          <div style={{ maxWidth: '820px', margin: '0 auto', textAlign: 'center' }}>
            <GlassCard elevated padding="40px">
              <span className="badge badge-orange" style={{ marginBottom: '14px' }}>
                ✦ Continuous Optimization
              </span>
              <h3 style={{ fontSize: '1.75rem', fontWeight: '800', marginBottom: '14px' }}>
                Your career profile grows as you learn.
              </h3>
              <p style={{ color: 'var(--text-secondary)', lineHeight: '1.7', marginBottom: '24px' }}>
                Completed a new project? Added a certification? Learned Docker or GraphQL? Update your JobPilot profile once and see your match scores dynamically recalculate across all active opportunities.
              </p>
              <div style={{ display: 'flex', justifyContent: 'center' }}>
                <PlayStoreButton />
              </div>
            </GlassCard>
          </div>
        </div>
      </section>
    </div>
  );
}
