import React from 'react';
import { 
  Sparkles, 
  Search, 
  Bookmark, 
  User, 
  ArrowUpRight, 
  ChevronRight,
  Wifi,
  Battery,
  Signal
} from 'lucide-react';

/**
 * Realistic Android Phone Mockup Component
 * Renders the JobPilot mobile application interface with glass UI,
 * profile strength meter, and recommended match cards.
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
            <div className="phone-user-greeting">Good morning, Chetan</div>
            <div className="phone-user-title">
              <span>Career Feed</span>
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
            {/* Profile Strength Card */}
            <div className="phone-profile-strength-card">
              <div className="phone-strength-meter">
                <div className="phone-strength-circle">
                  <div className="phone-strength-inner">86%</div>
                </div>
                <div>
                  <div style={{ fontSize: '0.8125rem', fontWeight: '700', color: '#0F172A' }}>
                    Profile Strength
                  </div>
                  <div style={{ fontSize: '0.6875rem', color: '#64748B' }}>
                    3 recommended updates
                  </div>
                </div>
              </div>
              <ChevronRight size={16} color="#94A3B8" />
            </div>

            {/* Recommended Section Heading */}
            <div className="phone-section-title">
              Recommended for you
            </div>

            {/* Job Card 1: Python Developer Intern */}
            <div className="phone-job-card" style={{ borderLeft: '3px solid #10B981' }}>
              <div className="phone-job-header">
                <div>
                  <div className="phone-job-title">Python Developer Intern</div>
                  <div style={{ fontSize: '0.6875rem', color: '#64748B', marginTop: '2px' }}>
                    Nexus Cloud Labs • Remote
                  </div>
                </div>
                <span className="phone-match-pill">94% Match</span>
              </div>
              <div style={{ display: 'flex', gap: '4px', flexWrap: 'wrap', margin: '6px 0 8px' }}>
                <span style={{ fontSize: '0.625rem', background: '#F1F5F9', padding: '2px 5px', borderRadius: '4px' }}>Python</span>
                <span style={{ fontSize: '0.625rem', background: '#F1F5F9', padding: '2px 5px', borderRadius: '4px' }}>SQL</span>
                <span style={{ fontSize: '0.625rem', background: '#F1F5F9', padding: '2px 5px', borderRadius: '4px' }}>FastAPI</span>
              </div>
              <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', fontSize: '0.6875rem', color: '#FF6A00', fontWeight: '600' }}>
                <span>AI Insight: 3 strong skill matches</span>
                <ArrowUpRight size={13} />
              </div>
            </div>

            {/* Job Card 2: AI / ML Intern */}
            <div className="phone-job-card" style={{ borderLeft: '3px solid #3B82F6' }}>
              <div className="phone-job-header">
                <div>
                  <div className="phone-job-title">AI / ML Intern</div>
                  <div style={{ fontSize: '0.6875rem', color: '#64748B', marginTop: '2px' }}>
                    Cortex Vision • Hybrid
                  </div>
                </div>
                <span className="phone-match-pill" style={{ background: '#EFF6FF', color: '#1D4ED8', borderColor: '#BFDBFE' }}>
                  89% Match
                </span>
              </div>
              <div style={{ display: 'flex', gap: '4px', flexWrap: 'wrap', margin: '6px 0 8px' }}>
                <span style={{ fontSize: '0.625rem', background: '#F1F5F9', padding: '2px 5px', borderRadius: '4px' }}>PyTorch</span>
                <span style={{ fontSize: '0.625rem', background: '#F1F5F9', padding: '2px 5px', borderRadius: '4px' }}>NLP</span>
                <span style={{ fontSize: '0.625rem', background: '#F1F5F9', padding: '2px 5px', borderRadius: '4px' }}>Pandas</span>
              </div>
              <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', fontSize: '0.6875rem', color: '#FF6A00', fontWeight: '600' }}>
                <span>AI Insight: NLP profile alignment</span>
                <ArrowUpRight size={13} />
              </div>
            </div>
          </div>

          {/* Bottom App Navigation Bar */}
          <div className="phone-nav-bar">
            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '2px', color: '#FF6A00' }}>
              <Sparkles size={16} />
              <span>Matches</span>
            </div>
            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '2px' }}>
              <Search size={16} />
              <span>Explore</span>
            </div>
            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '2px' }}>
              <Bookmark size={16} />
              <span>Tracking</span>
            </div>
            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '2px' }}>
              <User size={16} />
              <span>Profile</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
