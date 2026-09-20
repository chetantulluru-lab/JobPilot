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
  GraduationCap,
  Video,
  Monitor,
  Flame,
  FileCheck2,
  Send,
  BookOpen,
  Zap,
  Award
} from 'lucide-react';
import { APP_CONFIG } from '../config/appConfig';
import Button from '../components/Button';
import PlayStoreButton from '../components/PlayStoreButton';
import ApkDownloadButton from '../components/ApkDownloadButton';
import WindowsDownloadButton from '../components/WindowsDownloadButton';
import AIOrb from '../components/AIOrb';
import CopilotBadge3D from '../components/CopilotBadge3D';
import PhoneMockup from '../components/PhoneMockup';
import LaptopMockup from '../components/LaptopMockup';
import JobMatchCard from '../components/JobMatchCard';
import NLPPipelineVisual from '../components/NLPPipelineVisual';
import SectionHeading from '../components/SectionHeading';
import GlassCard from '../components/GlassCard';
import GlowBackground from '../components/GlowBackground';
import DisassemblingShowcase from '../components/3d/DisassemblingShowcase';
import { STUDENT_CHALLENGES } from '../data/features';

export default function Home() {
  const trustItems = [
    { label: 'AI Mock Interview', icon: Video },
    { label: 'Face Detection AI', icon: EyeIcon },
    { label: '1-Click Resume Tailor', icon: FileCheck2 },
    { label: 'Android APK + Win EXE', icon: Monitor },
    { label: '100% Free OTP Auth', icon: ShieldCheck },
  ];

  const studentBullets = [
    { title: 'One career profile', desc: 'A single unified source of truth for your skills, coursework, and projects.' },
    { title: 'Resume intelligence', desc: 'Automatic extraction strips away repetitive formatting and highlights true strengths.' },
    { title: 'Job matching', desc: 'Context-aware scoring matches you to roles where your foundational skills align.' },
    { title: 'Skill insights', desc: 'Actionable suggestions show exact gaps (e.g. learning Docker) to reach the next tier.' },
    { title: 'Application tracking', desc: 'Never lose track of deadlines, interview dates, or recruiter follow-ups.' },
    { title: 'Mobile-first experience', desc: 'Review curated daily opportunities anywhere directly from your Android phone or PC.' },
  ];

  const new3DFeatures = [
    {
      icon: Video,
      color: '#FF6A00',
      title: 'AI Mock Interview Simulator',
      badge: 'ML Kit Face Detection',
      desc: 'Practice realistic 5-stage interviews grounded in your actual resume or target role. Uses on-device front-camera posture & eye contact analysis with full scoring reports and ideal model answers.',
    },
    {
      icon: FileCheck2,
      color: '#10B981',
      title: '1-Click AI Resume Tailor',
      badge: 'ATS Score Booster',
      desc: 'Instantly tailor your resume bullet points and summary to match any job description. Watch your ATS match score jump from 65% to 92%+ with instant PDF export.',
    },
    {
      icon: Send,
      color: '#0284C7',
      title: 'Cold Outreach & Cover Letters',
      badge: 'LinkedIn & Email',
      desc: 'Generate recruiter-ready LinkedIn connection notes (<300 chars), personalized cold emails, and formal cover letters formatted for specific companies in seconds.',
    },
    {
      icon: Flame,
      color: '#EF4444',
      title: 'Daily Quizzes & Streaks',
      badge: 'Gamified Learning',
      desc: 'Reinforce your career roadmap with 3 daily multiple choice challenge questions, instant explanations, and keep your daily streak flame 🔥 burning.',
    },
    {
      icon: BookOpen,
      color: '#8B5CF6',
      title: 'Personal Notes & Bookmark Hub',
      badge: 'Study Companion',
      desc: 'Save code snippets, architectural notes, and crucial takeaway points directly inside your daily learning sessions with 1-tap bookmark access.',
    },
    {
      icon: ShieldCheck,
      color: '#F59E0B',
      title: '100% Free OTP Registration',
      badge: 'Zero Password Leak',
      desc: 'Seamless passwordless authentication powered by Brevo & Resend HTTP APIs. Safe, secure, instant 6-digit email verification with zero cloud port blocks.',
    },
  ];

  return (
    <div className="page-home">
      {/* ====================================================================
          HERO SECTION
          ==================================================================== */}
      <section className="section section-hero" style={{ paddingTop: '50px', paddingBottom: '80px', position: 'relative' }}>
        <GlowBackground variant="top" />

        <div className="container">
          <div className="grid-hero">
            {/* Left Hero Copy */}
            <div style={{ zIndex: 1 }}>
              <div style={{ marginBottom: '18px' }}>
                <span className="badge badge-orange">
                  <Sparkles size={14} />
                  <span>Next-Gen Career Navigation • 3D Copilot</span>
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
                {APP_CONFIG.description} Now featuring on-device AI Mock Interviews with Face Detection, 1-Click Resume Tailoring, and standalone Windows Desktop & Android Mobile apps.
              </p>

              {/* Dual Action CTAs */}
              <div
                style={{
                  display: 'flex',
                  flexWrap: 'wrap',
                  gap: '14px',
                  alignItems: 'center',
                  marginBottom: '28px',
                }}
              >
                <ApkDownloadButton
                  size="lg"
                  label="Download Android APK"
                  subtext="Android 11+ • Signed Release"
                />

                <WindowsDownloadButton
                  size="lg"
                  variant="dark"
                  label="Download for Windows"
                  subtext="Win 10/11 • Standalone .EXE"
                />

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
                <span>Optimized for students & early career engineers • Android APK + Windows .EXE Available</span>
              </div>
            </div>

            {/* Right Hero Visual: 3D Holographic AI Orb & Copilot Badge */}
            <div style={{ position: 'relative', display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '20px' }}>
              <AIOrb />
              <CopilotBadge3D />
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
          3D DISASSEMBLING CYBERNETIC ROBOT SHOWCASE
          ==================================================================== */}
      <DisassemblingShowcase />

      {/* ====================================================================
          3D INTERACTIVE NEW FEATURES MATRIX
          ==================================================================== */}
      <section className="section" style={{ position: 'relative', padding: '80px 0' }}>
        <GlowBackground variant="left" />
        <div className="container">
          <SectionHeading
            eyebrow="New Major Capabilities"
            title="Engineered to Give You"
            highlight="The Competitive Edge"
            subtitle="Explore the latest suite of AI-driven career accelerators, designed to take you from candidate to hired engineer."
          />

          <div
            style={{
              display: 'grid',
              gridTemplateColumns: 'repeat(auto-fit, minmax(min(100%, 340px), 1fr))',
              gap: '24px',
              marginTop: '36px',
            }}
          >
            {new3DFeatures.map((feat, idx) => {
              const Icon = feat.icon;
              return (
                <GlassCard
                  key={idx}
                  elevated
                  className="card-3d"
                  padding="28px"
                  style={{
                    display: 'flex',
                    flexDirection: 'column',
                    justifyContent: 'space-between',
                    border: '1px solid rgba(226, 232, 240, 0.9)',
                  }}
                >
                  <div>
                    <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '16px' }}>
                      <div
                        style={{
                          width: '42px',
                          height: '42px',
                          borderRadius: '12px',
                          background: `${feat.color}15`,
                          color: feat.color,
                          display: 'flex',
                          alignItems: 'center',
                          justifyContent: 'center',
                        }}
                      >
                        <Icon size={22} />
                      </div>
                      <span
                        style={{
                          fontSize: '0.6875rem',
                          fontWeight: '700',
                          padding: '3px 8px',
                          borderRadius: '6px',
                          background: `${feat.color}15`,
                          color: feat.color,
                          fontFamily: 'var(--font-display)',
                        }}
                      >
                        {feat.badge}
                      </span>
                    </div>

                    <h3 style={{ fontSize: '1.25rem', fontWeight: '800', marginBottom: '10px' }}>
                      {feat.title}
                    </h3>
                    <p style={{ fontSize: '0.875rem', color: 'var(--text-secondary)', lineHeight: '1.6' }}>
                      {feat.desc}
                    </p>
                  </div>
                </GlassCard>
              );
            })}
          </div>
        </div>
      </section>

      {/* ====================================================================
          3D DUAL DEVICE SHOWCASE SECTION
          ==================================================================== */}
      <section
        className="section"
        style={{
          background: 'linear-gradient(180deg, #FFFFFF 0%, #FFF8F2 50%, #FFFFFF 100%)',
          position: 'relative',
          overflow: 'hidden',
          padding: '80px 0',
        }}
      >
        <GlowBackground variant="top" />
        <div className="container">
          <SectionHeading
            eyebrow="Unified Cross-Platform Experience"
            title="Available on Android Phone"
            highlight="& Windows Desktop"
            subtitle="Practice full-screen webcam mock interviews on your PC, and stay on top of daily learning streaks on your mobile phone."
          />

          <div
            style={{
              display: 'grid',
              gridTemplateColumns: 'repeat(auto-fit, minmax(min(100%, 380px), 1fr))',
              gap: '48px',
              alignItems: 'center',
              marginTop: '40px',
            }}
          >
            {/* Phone Mockup Left */}
            <div style={{ display: 'flex', justifyContent: 'center' }}>
              <PhoneMockup />
            </div>

            {/* Laptop Mockup Right */}
            <div style={{ display: 'flex', justifyContent: 'center' }}>
              <LaptopMockup />
            </div>
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
          <div className="grid-2col">
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
          FINAL CALL TO ACTION
          ==================================================================== */}
      <section className="section" style={{ paddingTop: '40px', paddingBottom: '100px' }}>
        <div className="container">
          <GlassCard
            elevated
            glow
            style={{
              padding: 'clamp(32px, 5vw, 64px) clamp(20px, 4vw, 32px)',
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
              Get started with JobPilot today.
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
              Transform your resume into structured intelligence, rehearse mock interviews with live video posture tracking, and accelerate your job search.
            </p>
            <div style={{ display: 'flex', justifyContent: 'center', gap: '16px', flexWrap: 'wrap' }}>
              <ApkDownloadButton size="lg" />
              <WindowsDownloadButton size="lg" variant="dark" />
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

// Inline EyeIcon for trust items
function EyeIcon(props) {
  return (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      width={props.size || 16}
      height={props.size || 16}
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth="2"
      strokeLinecap="round"
      strokeLinejoin="round"
      {...props}
    >
      <path d="M2 12s3-7 10-7 10 7 10 7-3 7-10 7-10-7-10-7Z" />
      <circle cx="12" cy="12" r="3" />
    </svg>
  );
}
