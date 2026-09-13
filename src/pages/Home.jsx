import React from 'react';
import { 
  ArrowRight, 
  Sparkles, 
  CheckCircle2, 
  Smartphone, 
  ShieldCheck, 
  Cpu, 
  Target, 
  Compass, 
  GraduationCap
} from 'lucide-react';
import { APP_CONFIG } from '../config/appConfig';
import Button from '../components/Button';
import PlayStoreButton from '../components/PlayStoreButton';
import AIOrb from '../components/AIOrb';
import PhoneMockup from '../components/PhoneMockup';
import JobMatchCard from '../components/JobMatchCard';
import NLPPipelineVisual from '../components/NLPPipelineVisual';
import SectionHeading from '../components/SectionHeading';
import GlassCard from '../components/GlassCard';
import QRPlaceholder from '../components/QRPlaceholder';
import GlowBackground from '../components/GlowBackground';
import { STUDENT_CHALLENGES } from '../data/features';

export default function Home() {
  const trustItems = [
    { label: 'AI-Powered', icon: Sparkles },
    { label: 'NLP Intelligence', icon: Cpu },
    { label: 'Smart Job Matching', icon: Target },
    { label: 'Android App', icon: Smartphone },
    { label: 'Secure Profile', icon: ShieldCheck },
  ];

  const studentBullets = [
    { title: 'One career profile', desc: 'A single unified source of truth for your skills, coursework, and projects.' },
    { title: 'Resume intelligence', desc: 'Automatic extraction strips away repetitive formatting and highlights true strengths.' },
    { title: 'Job matching', desc: 'Context-aware scoring matches you to roles where your foundational skills align.' },
    { title: 'Skill insights', desc: 'Actionable suggestions show exact gaps (e.g. learning Docker) to reach the next tier.' },
    { title: 'Application tracking', desc: 'Never lose track of deadlines, interview dates, or recruiter follow-ups.' },
    { title: 'Mobile-first experience', desc: 'Review curated daily opportunities anywhere directly from your Android phone.' },
  ];

  return (
    <div className="page-home">
      {/* ====================================================================
          HERO SECTION
          ==================================================================== */}
      <section className="section section-hero" style={{ paddingTop: '50px', paddingBottom: '80px', position: 'relative' }}>
        <GlowBackground variant="top" />

        <div className="container">
          <div
            style={{
              display: 'grid',
              gridTemplateColumns: 'repeat(auto-fit, minmax(320px, 1fr))',
              gap: '48px',
              alignItems: 'center',
            }}
          >
            {/* Left Hero Copy */}
            <div style={{ zIndex: 1 }}>
              <div style={{ marginBottom: '18px' }}>
                <span className="badge badge-orange">
                  <Sparkles size={14} />
                  <span>Next-Gen Career Navigation</span>
                </span>
              </div>

              <h1
                style={{
                  fontSize: 'clamp(2.75rem, 5.2vw, 4.25rem)',
                  fontWeight: '800',
                  letterSpacing: '-0.035em',
                  lineHeight: '1.08',
                  marginBottom: '24px',
                }}
              >
                Your career.{' '}
                <span className="text-gradient-orange" style={{ display: 'block' }}>
                  Piloted by AI.
                </span>
              </h1>

              <p
                style={{
                  fontSize: 'clamp(1.0625rem, 1.35vw, 1.25rem)',
                  color: 'var(--text-secondary)',
                  lineHeight: '1.6',
                  marginBottom: '36px',
                  maxWidth: '560px',
                }}
              >
                {APP_CONFIG.description}
              </p>

              {/* CTAs */}
              <div
                style={{
                  display: 'flex',
                  flexWrap: 'wrap',
                  gap: '16px',
                  alignItems: 'center',
                  marginBottom: '28px',
                }}
              >
                <Button
                  to="/download"
                  variant="primary"
                  size="lg"
                  icon={<ArrowRight size={18} />}
                >
                  Get the Android App
                </Button>

                <Button
                  to="/how-it-works"
                  variant="secondary"
                  size="lg"
                  icon={<Compass size={18} />}
                  iconPosition="left"
                >
                  See How It Works
                </Button>
              </div>

              {/* Subtle Trust Note */}
              <div
                style={{
                  display: 'flex',
                  alignItems: 'center',
                  gap: '12px',
                  fontSize: '0.8125rem',
                  color: 'var(--text-muted)',
                }}
              >
                <span style={{ display: 'inline-flex', color: '#10B981' }}>
                  <CheckCircle2 size={16} />
                </span>
                <span>Optimized for students & early career engineers • Android 11+</span>
              </div>
            </div>

            {/* Right Hero Visual: Floating AI Orb */}
            <div style={{ position: 'relative', display: 'flex', justifyContent: 'center' }}>
              <AIOrb />
            </div>
          </div>
        </div>
      </section>

      {/* ====================================================================
          MINIMAL TRUST / VALUE STRIP
          ==================================================================== */}
      <section
        style={{
          borderTop: '1px solid rgba(226, 232, 240, 0.7)',
          borderBottom: '1px solid rgba(226, 232, 240, 0.7)',
          background: 'rgba(255, 255, 255, 0.65)',
          backdropFilter: 'blur(10px)',
          padding: '24px 0',
        }}
      >
        <div className="container">
          <div
            style={{
              display: 'flex',
              flexWrap: 'wrap',
              alignItems: 'center',
              justifyContent: 'space-around',
              gap: '24px',
            }}
          >
            {trustItems.map((item, idx) => {
              const Icon = item.icon;
              return (
                <div
                  key={idx}
                  style={{
                    display: 'flex',
                    alignItems: 'center',
                    gap: '10px',
                    fontFamily: 'var(--font-display)',
                    fontSize: '0.9375rem',
                    fontWeight: '700',
                    color: 'var(--text-secondary)',
                  }}
                >
                  <span
                    style={{
                      color: 'var(--orange-500)',
                      background: 'var(--orange-50)',
                      padding: '6px',
                      borderRadius: '8px',
                      display: 'flex',
                    }}
                  >
                    <Icon size={16} />
                  </span>
                  <span>{item.label}</span>
                </div>
              );
            })}
          </div>
        </div>
      </section>

      {/* ====================================================================
          NLP PIPELINE SECTION
          ==================================================================== */}
      <section className="section" style={{ position: 'relative' }}>
        <GlowBackground variant="left" />
        <div className="container">
          <SectionHeading
            eyebrow="Natural Language Processing"
            title="Your resume isn't just a document."
            highlight="It's your career data."
            subtitle="JobPilot uses Natural Language Processing to understand unstructured career information, extract core proficiencies, and build a normalized digital profile."
          />

          <NLPPipelineVisual />
        </div>
      </section>

      {/* ====================================================================
          JOB MATCHING SECTION
          ==================================================================== */}
      <section
        className="section"
        style={{
          background: 'linear-gradient(180deg, rgba(255, 255, 255, 0) 0%, rgba(255, 249, 242, 0.5) 50%, rgba(255, 255, 255, 0) 100%)',
          position: 'relative',
        }}
      >
        <GlowBackground variant="right" />
        <div className="container">
          <SectionHeading
            eyebrow="Transparent Matching"
            title="See why a job matches your profile —"
            highlight="not just whether it does."
            subtitle="Traditional job boards give you arbitrary search results. JobPilot calculates deep semantic compatibility and explains strong matches as well as actionable skill gaps."
          />

          <div style={{ maxWidth: '960px', margin: '0 auto' }}>
            <JobMatchCard />
          </div>
        </div>
      </section>

      {/* ====================================================================
          FOR STUDENTS SECTION
          ==================================================================== */}
      <section className="section">
        <div className="container">
          <div
            style={{
              display: 'grid',
              gridTemplateColumns: 'repeat(auto-fit, minmax(320px, 1fr))',
              gap: '48px',
              alignItems: 'center',
            }}
          >
            <div>
              <SectionHeading
                align="left"
                eyebrow="Targeted Solution"
                title="Built for students starting"
                highlight="their careers."
                subtitle="Searching for internships and entry-level positions can feel fragmented and exhausting. JobPilot eliminates repetitive data entry and gives you clear clarity."
              />

              <div style={{ display: 'flex', flexDirection: 'column', gap: '14px', marginTop: '24px' }}>
                {studentBullets.map((bullet, idx) => (
                  <div
                    key={idx}
                    style={{
                      display: 'flex',
                      alignItems: 'flex-start',
                      gap: '12px',
                    }}
                  >
                    <div
                      style={{
                        width: '24px',
                        height: '24px',
                        borderRadius: '50%',
                        background: 'var(--orange-50)',
                        color: 'var(--orange-600)',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        flexShrink: 0,
                        marginTop: '2px',
                      }}
                    >
                      <CheckCircle2 size={15} />
                    </div>
                    <div>
                      <div style={{ fontSize: '0.9375rem', fontWeight: '700', color: 'var(--text-primary)' }}>
                        {bullet.title}
                      </div>
                      <div style={{ fontSize: '0.875rem', color: 'var(--text-muted)', lineHeight: '1.5' }}>
                        {bullet.desc}
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            </div>

            {/* Student Challenge Comparison Card */}
            <div>
              <GlassCard elevated padding="32px">
                <div style={{ display: 'flex', alignItems: 'center', gap: '10px', marginBottom: '20px' }}>
                  <div
                    style={{
                      width: '40px',
                      height: '40px',
                      borderRadius: '12px',
                      background: 'var(--orange-500)',
                      color: '#FFFFFF',
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                    }}
                  >
                    <GraduationCap size={22} />
                  </div>
                  <div>
                    <h3 style={{ fontSize: '1.2rem', fontWeight: '800' }}>Student Journey Simplified</h3>
                    <span style={{ fontSize: '0.8125rem', color: 'var(--text-muted)' }}>From Scattered to Structured</span>
                  </div>
                </div>

                <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
                  {STUDENT_CHALLENGES.map((item, idx) => (
                    <div
                      key={idx}
                      style={{
                        padding: '12px 16px',
                        borderRadius: '12px',
                        background: 'rgba(255, 255, 255, 0.8)',
                        border: '1px solid rgba(226, 232, 240, 0.8)',
                      }}
                    >
                      <div style={{ fontSize: '0.8125rem', color: '#EF4444', fontWeight: '600', marginBottom: '4px' }}>
                        ✕ {item.problem}
                      </div>
                      <div style={{ fontSize: '0.875rem', color: '#047857', fontWeight: '600' }}>
                        ✓ {item.solution}
                      </div>
                    </div>
                  ))}
                </div>
              </GlassCard>
            </div>
          </div>
        </div>
      </section>

      {/* ====================================================================
          ANDROID PROMOTIONAL SECTION
          ==================================================================== */}
      <section
        className="section section-android-app"
        style={{
          background: 'linear-gradient(180deg, #FFFFFF 0%, #FFF8F2 50%, #FFFFFF 100%)',
          position: 'relative',
          overflow: 'hidden',
        }}
      >
        <GlowBackground variant="top" />

        <div className="container">
          <div
            style={{
              display: 'grid',
              gridTemplateColumns: 'repeat(auto-fit, minmax(320px, 1fr))',
              gap: '56px',
              alignItems: 'center',
            }}
          >
            {/* Phone Mockup Left */}
            <div style={{ display: 'flex', justifyContent: 'center' }}>
              <PhoneMockup />
            </div>

            {/* App Promotion Right */}
            <div>
              <span className="badge badge-orange" style={{ marginBottom: '16px' }}>
                <Smartphone size={14} />
                <span>Android Exclusive Mobile App</span>
              </span>

              <h2
                style={{
                  fontSize: 'clamp(2.25rem, 3.8vw, 3rem)',
                  fontWeight: '800',
                  letterSpacing: '-0.03em',
                  lineHeight: '1.15',
                  marginBottom: '20px',
                }}
              >
                Your career assistant,{' '}
                <span className="text-gradient-orange">in your pocket.</span>
              </h2>

              <p
                style={{
                  fontSize: '1.125rem',
                  color: 'var(--text-secondary)',
                  lineHeight: '1.6',
                  marginBottom: '28px',
                }}
              >
                Take JobPilot with you. Build your profile, explore opportunities and manage your career journey from your Android device.
              </p>

              {/* Requirement Badge */}
              <div
                style={{
                  display: 'inline-flex',
                  alignItems: 'center',
                  gap: '8px',
                  padding: '8px 16px',
                  borderRadius: 'var(--radius-full)',
                  background: 'rgba(255, 255, 255, 0.9)',
                  border: '1px solid var(--border-subtle)',
                  fontSize: '0.875rem',
                  fontWeight: '700',
                  color: 'var(--text-primary)',
                  marginBottom: '32px',
                }}
              >
                <span
                  style={{
                    width: '8px',
                    height: '8px',
                    borderRadius: '50%',
                    background: '#10B981',
                  }}
                />
                <span>Available for Android 11+</span>
              </div>

              {/* Play Store Download & QR Code Callout */}
              <div
                style={{
                  display: 'flex',
                  flexWrap: 'wrap',
                  gap: '24px',
                  alignItems: 'center',
                }}
              >
                <div>
                  <PlayStoreButton />
                  <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)', marginTop: '8px' }}>
                    Free for students • Instant activation
                  </div>
                </div>

                <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
                  <QRPlaceholder size={90} caption="Scan to download" />
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* ====================================================================
          FINAL CALL TO ACTION
          ==================================================================== */}
      <section className="section" style={{ paddingTop: '40px', paddingBottom: '100px' }}>
        <div className="container">
          <GlassCard
            elevated
            glow
            style={{
              padding: '64px 32px',
              textAlign: 'center',
              position: 'relative',
              overflow: 'hidden',
              background: 'linear-gradient(135deg, rgba(255, 255, 255, 0.96) 0%, rgba(255, 247, 237, 0.9) 100%)',
              border: '1px solid rgba(255, 106, 0, 0.25)',
            }}
          >
            <span className="badge badge-orange" style={{ marginBottom: '16px' }}>
              ✦ Ready to Pilot Your Career?
            </span>
            <h2
              style={{
                fontSize: 'clamp(2rem, 3.5vw, 2.75rem)',
                fontWeight: '800',
                letterSpacing: '-0.03em',
                marginBottom: '16px',
              }}
            >
              Get started with JobPilot on Android today.
            </h2>
            <p
              style={{
                fontSize: '1.0625rem',
                color: 'var(--text-secondary)',
                maxWidth: '620px',
                margin: '0 auto 36px',
                lineHeight: '1.6',
              }}
            >
              Transform your resume into structured intelligence and navigate your career path with clarity and confidence.
            </p>
            <div style={{ display: 'flex', justifyContent: 'center', gap: '16px', flexWrap: 'wrap' }}>
              <PlayStoreButton />
              <Button to="/features" variant="secondary" size="lg">
                Explore All Features
              </Button>
            </div>
          </GlassCard>
        </div>
      </section>
    </div>
  );
}
