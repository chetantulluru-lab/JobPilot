import React from 'react';
import { 
  Home, 
  Compass, 
  FileText, 
  User, 
  Settings, 
  ArrowUpRight, 
  CheckCircle2, 
  Wifi, 
  Battery, 
  Signal,
  Flame,
  PlayCircle
} from 'lucide-react';

/**
 * Realistic Android Phone Mockup Component
 * Renders the JobPilot mobile application interface with glass UI,
 * daily learning streak, active roadmap card, and 5-tab navigation.
 */
export default function PhoneMockup({ className = '' }) {
  return (
    <div className={`phone-mockup-outer ${className}`}>
      {/* Ambient orange glow behind phone */}
      <div className="phone-ambient-reflection" />

      {/* Android Device Chassis */}
      <div className="phone-frame">
        <div className="phone-screen">
          {/* Status Bar */}
          <div className="phone-top-bar">
            <span>09:41</span>
            {/* Front Camera Hole-punch */}
            <div className="phone-notch-camera" />
            <div style={{ display: 'flex', alignItems: 'center', gap: '5px' }}>
              <Signal size={11} />
              <Wifi size={11} />
              <Battery size={13} />
            </div>
          </div>

          {/* App Header */}
          <div className="phone-app-header">
            <div className="phone-user-greeting">Welcome back, Candidate</div>
            <div className="phone-user-title">
              <span>JobPilot</span>
              <div
                style={{
                  width: '26px',
                  height: '26px',
                  borderRadius: '50%',
                  background: 'linear-gradient(135deg, #FF6A00 0%, #FF8A3D 100%)',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  color: '#FFFFFF',
                  fontSize: '0.6875rem',
                  fontWeight: '700',
                }}
              >
                ✦
              </div>
            </div>
          </div>

          {/* Scrollable App Body */}
          <div className="phone-app-content">
            {/* Learning Streak & Score Grid */}
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '8px', marginBottom: '10px' }}>
              <div style={{ background: '#FFFFFF', borderRadius: '12px', padding: '10px', border: '1px solid rgba(226, 232, 240, 0.8)' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                  <span style={{ fontSize: '0.625rem', color: '#64748B', fontWeight: '600' }}>Streak</span>
                  <Flame size={14} color="#FF6A00" />
                </div>
                <div style={{ fontSize: '1.125rem', fontWeight: '800', color: '#0F172A', marginTop: '4px' }}>
                  12 Days
                </div>
                <div style={{ fontSize: '0.5625rem', color: '#94A3B8' }}>Best: 18 days</div>
              </div>

              <div style={{ background: '#FFFFFF', borderRadius: '12px', padding: '10px', border: '1px solid rgba(226, 232, 240, 0.8)' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                  <span style={{ fontSize: '0.625rem', color: '#64748B', fontWeight: '600' }}>ATS Score</span>
                  <CheckCircle2 size={14} color="#10B981" />
                </div>
                <div style={{ fontSize: '1.125rem', fontWeight: '800', color: '#10B981', marginTop: '4px' }}>
                  84/100
                </div>
                <div style={{ fontSize: '0.5625rem', color: '#94A3B8' }}>ATS Ready</div>
              </div>
            </div>

            {/* Active Roadmap Card */}
            <div className="phone-job-card" style={{ borderLeft: '3px solid #FF6A00' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '6px' }}>
                <span style={{ fontSize: '0.5625rem', fontWeight: '700', color: '#EA580C', background: '#FFF7ED', padding: '2px 6px', borderRadius: '4px' }}>
                  ACTIVE ROADMAP • 6 MONTHS
                </span>
                <span style={{ fontSize: '0.6875rem', fontWeight: '700', color: '#FF6A00' }}>
                  38%
                </span>
              </div>

              <div className="phone-job-title" style={{ fontSize: '0.8125rem' }}>
                Python Backend Developer
              </div>
              <div style={{ fontSize: '0.6875rem', color: '#64748B', marginTop: '2px' }}>
                Phase 1: FastAPI & Asynchronous Python
              </div>

              {/* Mini progress bar */}
              <div style={{ width: '100%', height: '4px', background: '#E2E8F0', borderRadius: '2px', margin: '8px 0' }}>
                <div style={{ width: '38%', height: '100%', background: '#FF6A00', borderRadius: '2px' }} />
              </div>

              <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', fontSize: '0.625rem', color: '#64748B' }}>
                <span>Day 14: Dependency Injection</span>
                <span style={{ color: '#10B981', fontWeight: '600' }}>14/36 Days</span>
              </div>
            </div>

            {/* Multilingual Resource Spotlight */}
            <div style={{ background: '#FFFFFF', borderRadius: '12px', padding: '10px', border: '1px solid rgba(226, 232, 240, 0.8)', marginTop: '8px' }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '6px', marginBottom: '6px' }}>
                <PlayCircle size={14} color="#EF4444" />
                <span style={{ fontSize: '0.6875rem', fontWeight: '700', color: '#0F172A' }}>
                  Curated Video Resources
                </span>
              </div>
              <div style={{ display: 'flex', gap: '4px', marginBottom: '6px' }}>
                <span style={{ fontSize: '0.5625rem', background: '#FF6A00', color: '#FFFFFF', padding: '2px 6px', borderRadius: '4px', fontWeight: '600' }}>English</span>
                <span style={{ fontSize: '0.5625rem', background: '#F1F5F9', color: '#475569', padding: '2px 6px', borderRadius: '4px' }}>తెలుగు</span>
                <span style={{ fontSize: '0.5625rem', background: '#F1F5F9', color: '#475569', padding: '2px 6px', borderRadius: '4px' }}>हिन्दी</span>
              </div>
              <div style={{ fontSize: '0.625rem', color: '#64748B' }}>
                FastAPI Architecture & Practical Microservices
              </div>
            </div>
          </div>

          {/* Bottom App Navigation Bar (5 MVP Tabs) */}
          <div className="phone-nav-bar" style={{ display: 'flex', justifyContent: 'space-around' }}>
            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '2px', color: '#FF6A00' }}>
              <Home size={14} />
              <span style={{ fontSize: '0.5625rem' }}>Home</span>
            </div>
            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '2px', color: '#64748B' }}>
              <Compass size={14} />
              <span style={{ fontSize: '0.5625rem' }}>Roadmap</span>
            </div>
            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '2px', color: '#64748B' }}>
              <FileText size={14} />
              <span style={{ fontSize: '0.5625rem' }}>Resume</span>
            </div>
            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '2px', color: '#64748B' }}>
              <User size={14} />
              <span style={{ fontSize: '0.5625rem' }}>Profile</span>
            </div>
            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '2px', color: '#64748B' }}>
              <Settings size={14} />
              <span style={{ fontSize: '0.5625rem' }}>Settings</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
