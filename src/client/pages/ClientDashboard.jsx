import React from 'react';
import { 
  Sparkles, 
  Video, 
  FileCheck2, 
  Send, 
  Flame, 
  CheckCircle2, 
  ArrowRight, 
  Briefcase,
  PlayCircle,
  Bot,
  User
} from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import AIOrb from '../../components/AIOrb';

export default function ClientDashboard({ onNavigate }) {
  const { user } = useAuth();

  const mockJobs = [
    {
      id: 1,
      title: 'Python Backend & Microservices Engineer',
      company: 'TechCorp Cloud',
      location: 'Remote / Bengaluru',
      match: 94,
      skills: ['FastAPI', 'PostgreSQL', 'Docker', 'AsyncIO'],
    },
    {
      id: 2,
      title: 'Android Jetpack Compose Developer',
      company: 'AppScale Studio',
      location: 'Remote / Hyderabad',
      match: 89,
      skills: ['Kotlin', 'Jetpack Compose', 'Coroutines', 'ML Kit'],
    },
    {
      id: 3,
      title: 'Full Stack Distributed Systems Engineer',
      company: 'Nexlify Labs',
      location: 'Hybrid / Pune',
      match: 86,
      skills: ['React', 'FastAPI', 'Redis', 'WebSockets'],
    },
  ];

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '24px', maxWidth: '1180px', margin: '0 auto' }}>
      {/* Welcome & Target Role Hero */}
      <div className="client-card client-card-glow" style={{ padding: '28px 32px' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '20px' }}>
          <div style={{ flex: '1 1 500px' }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '8px' }}>
              <span className="client-badge client-badge-orange">
                <Sparkles size={13} />
                <span>AI Career Pilot Active</span>
              </span>
              <span className="client-badge client-badge-green">
                <CheckCircle2 size={13} />
                <span>Cloud Sync Connected</span>
              </span>
            </div>
            <h1 style={{ fontSize: '2.1rem', fontWeight: '800', margin: '0 0 6px 0', letterSpacing: '-0.02em', color: 'var(--app-text)' }}>
              Welcome back, {user?.fullName || 'Candidate'}!
            </h1>
            <p style={{ color: 'var(--app-text-secondary)', margin: 0, fontSize: '1rem' }}>
              Target Role: <strong style={{ color: 'var(--app-text)' }}>{user?.targetRole || 'Android & Full Stack Engineer'}</strong>
            </p>
          </div>

          <div style={{ display: 'flex', alignItems: 'center', gap: '20px' }}>
            {/* Holographic AI Orb visual */}
            <div style={{ width: '80px', height: '80px', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
              <AIOrb style={{ width: '80px', height: '80px' }} />
            </div>

            {/* Daily Streak Flame Indicator */}
            <div className="streak-indicator" style={{ padding: '12px 22px', borderRadius: '16px' }}>
              <Flame size={28} className="streak-flame-icon" />
              <div>
                <div style={{ fontSize: '1.25rem', fontWeight: '900', lineHeight: 1.1 }}>
                  {user?.streak || 3} Days
                </div>
                <div style={{ fontSize: '0.6875rem', opacity: 0.9, textTransform: 'uppercase', letterSpacing: '0.05em' }}>
                  Active Streak 🔥
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Readiness Scores Gauge Strip */}
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '16px', marginTop: '24px', paddingTop: '20px', borderTop: '1px solid var(--app-border-subtle)' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
            <div className="readiness-gauge-circle" style={{ width: '52px', height: '52px', borderRadius: '50%', background: 'var(--app-orange-light)', border: '2px solid var(--app-orange)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: 'var(--app-orange)', fontWeight: '800', fontSize: '1.1rem' }}>
              {user?.readinessScore || 88}%
            </div>
            <div>
              <div style={{ fontSize: '0.8125rem', color: 'var(--app-text-secondary)', fontWeight: 600 }}>Interview Readiness</div>
              <div style={{ fontSize: '0.875rem', fontWeight: 700, color: '#047857' }}>Interview Ready</div>
            </div>
          </div>

          <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
            <div className="readiness-gauge-circle" style={{ width: '52px', height: '52px', borderRadius: '50%', background: 'var(--app-info-bg)', border: '2px solid #0284C7', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#0369A1', fontWeight: '800', fontSize: '1.1rem' }}>
              {user?.atsScore || 86}%
            </div>
            <div>
              <div style={{ fontSize: '0.8125rem', color: 'var(--app-text-secondary)', fontWeight: 600 }}>ATS Resume Score</div>
              <div style={{ fontSize: '0.875rem', fontWeight: 700, color: '#0369A1' }}>Top 10% Candidate</div>
            </div>
          </div>

          <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
            <div className="readiness-gauge-circle" style={{ width: '52px', height: '52px', borderRadius: '50%', background: 'var(--app-success-bg)', border: '2px solid #10B981', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#047857', fontWeight: '800', fontSize: '1.1rem' }}>
              14/36
            </div>
            <div>
              <div style={{ fontSize: '0.8125rem', color: 'var(--app-text-secondary)', fontWeight: 600 }}>Curriculum Progress</div>
              <div style={{ fontSize: '0.875rem', fontWeight: 700, color: 'var(--app-text)' }}>Day 14 Active</div>
            </div>
          </div>
        </div>
      </div>

      {/* Active Roadmap Progress Card */}
      <div className="client-card" style={{ padding: '24px' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '14px', flexWrap: 'wrap', gap: '8px' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
            <PlayCircle size={22} color="var(--app-orange)" />
            <h3 style={{ margin: 0, fontSize: '1.15rem', fontWeight: 800 }}>
              Active Learning: {user?.activeRoadmap || 'Python Backend & Microservices'}
            </h3>
          </div>
          <button onClick={() => onNavigate('roadmap')} className="client-btn client-btn-secondary" style={{ padding: '6px 14px', fontSize: '0.8125rem' }}>
            <span>Resume Day 14</span>
            <ArrowRight size={14} />
          </button>
        </div>

        <div style={{ marginBottom: '8px', display: 'flex', justifyContent: 'space-between', fontSize: '0.8125rem', color: 'var(--app-text-secondary)' }}>
          <span>Current Topic: <strong>Asynchronous Workers & Redis Concurrency</strong></span>
          <span>Day {user?.roadmapDay || 14} of {user?.totalDays || 36} (39% Complete)</span>
        </div>

        <div className="roadmap-progress-bar">
          <div className="roadmap-progress-fill" style={{ width: '39%' }} />
        </div>
      </div>

      {/* All Operations Quick Launch Grid */}
      <div>
        <h3 style={{ fontSize: '1.25rem', fontWeight: 800, margin: '0 0 16px 0' }}>
          Autonomous Career Capabilities
        </h3>

        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(260px, 1fr))', gap: '16px' }}>
          {/* Tile 1: AI Mock Interview */}
          <div
            onClick={() => onNavigate('interview')}
            className="client-card client-card-glow"
            style={{ padding: '24px', cursor: 'pointer', display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}
          >
            <div>
              <div style={{ width: '44px', height: '44px', borderRadius: '12px', background: 'rgba(255, 106, 0, 0.15)', color: 'var(--app-orange)', display: 'flex', alignItems: 'center', justifyContent: 'center', marginBottom: '14px' }}>
                <Video size={22} />
              </div>
              <h4 style={{ fontSize: '1.05rem', fontWeight: 800, margin: '0 0 6px 0' }}>AI Mock Interview</h4>
              <p style={{ color: 'var(--app-text-secondary)', fontSize: '0.8125rem', lineHeight: 1.5, margin: 0 }}>
                Live front webcam stream, ML face presence HUD, audio TTS reading, and candidate model answers.
              </p>
            </div>
            <div style={{ marginTop: '16px', display: 'flex', alignItems: 'center', gap: '6px', color: 'var(--app-orange)', fontSize: '0.8125rem', fontWeight: 700 }}>
              <span>Launch Simulator</span>
              <ArrowRight size={14} />
            </div>
          </div>

          {/* Tile 2: Resume Tailor & NLP */}
          <div
            onClick={() => onNavigate('resume')}
            className="client-card client-card-glow"
            style={{ padding: '24px', cursor: 'pointer', display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}
          >
            <div>
              <div style={{ width: '44px', height: '44px', borderRadius: '12px', background: 'rgba(2, 132, 199, 0.15)', color: '#38BDF8', display: 'flex', alignItems: 'center', justifyContent: 'center', marginBottom: '14px' }}>
                <FileCheck2 size={22} />
              </div>
              <h4 style={{ fontSize: '1.05rem', fontWeight: 800, margin: '0 0 6px 0' }}>Resume Intelligence & Tailor</h4>
              <p style={{ color: 'var(--app-text-secondary)', fontSize: '0.8125rem', lineHeight: 1.5, margin: 0 }}>
                Upload PDF/DOCX for NLP skill extraction, seed your profile, and 1-click tailor for 93%+ ATS scores.
              </p>
            </div>
            <div style={{ marginTop: '16px', display: 'flex', alignItems: 'center', gap: '6px', color: '#38BDF8', fontSize: '0.8125rem', fontWeight: 700 }}>
              <span>Tailor Resume</span>
              <ArrowRight size={14} />
            </div>
          </div>

          {/* Tile 3: AI Career Coach Chat */}
          <div
            onClick={() => onNavigate('coach')}
            className="client-card client-card-glow"
            style={{ padding: '24px', cursor: 'pointer', display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}
          >
            <div>
              <div style={{ width: '44px', height: '44px', borderRadius: '12px', background: 'rgba(16, 185, 129, 0.15)', color: '#10B981', display: 'flex', alignItems: 'center', justifyContent: 'center', marginBottom: '14px' }}>
                <Bot size={22} />
              </div>
              <h4 style={{ fontSize: '1.05rem', fontWeight: 800, margin: '0 0 6px 0' }}>AI Career Coach Chat</h4>
              <p style={{ color: 'var(--app-text-secondary)', fontSize: '0.8125rem', lineHeight: 1.5, margin: 0 }}>
                Context-aware conversational advisor grounded in your career profile and active learning goals.
              </p>
            </div>
            <div style={{ marginTop: '16px', display: 'flex', alignItems: 'center', gap: '6px', color: '#10B981', fontSize: '0.8125rem', fontWeight: 700 }}>
              <span>Talk with Coach</span>
              <ArrowRight size={14} />
            </div>
          </div>

          {/* Tile 4: Jobs & Recruiter Cold Outreach */}
          <div
            onClick={() => onNavigate('jobs')}
            className="client-card client-card-glow"
            style={{ padding: '24px', cursor: 'pointer', display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}
          >
            <div>
              <div style={{ width: '44px', height: '44px', borderRadius: '12px', background: 'rgba(139, 92, 246, 0.15)', color: '#A78BFA', display: 'flex', alignItems: 'center', justifyContent: 'center', marginBottom: '14px' }}>
                <Send size={22} />
              </div>
              <h4 style={{ fontSize: '1.05rem', fontWeight: 800, margin: '0 0 6px 0' }}>Jobs & Recruiter Outreach</h4>
              <p style={{ color: 'var(--app-text-secondary)', fontSize: '0.8125rem', lineHeight: 1.5, margin: 0 }}>
                Matched jobs catalog, skill gap analyzer, and 1-click generated LinkedIn recruiter notes.
              </p>
            </div>
            <div style={{ marginTop: '16px', display: 'flex', alignItems: 'center', gap: '6px', color: '#A78BFA', fontSize: '0.8125rem', fontWeight: 700 }}>
              <span>Explore Roles</span>
              <ArrowRight size={14} />
            </div>
          </div>

          {/* Tile 5: Application Kanban Pipeline */}
          <div
            onClick={() => onNavigate('applications')}
            className="client-card client-card-glow"
            style={{ padding: '24px', cursor: 'pointer', display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}
          >
            <div>
              <div style={{ width: '44px', height: '44px', borderRadius: '12px', background: 'rgba(234, 179, 8, 0.15)', color: '#FACC15', display: 'flex', alignItems: 'center', justifyContent: 'center', marginBottom: '14px' }}>
                <Briefcase size={22} />
              </div>
              <h4 style={{ fontSize: '1.05rem', fontWeight: 800, margin: '0 0 6px 0' }}>Application Kanban Tracker</h4>
              <p style={{ color: 'var(--app-text-secondary)', fontSize: '0.8125rem', lineHeight: 1.5, margin: 0 }}>
                Visual board tracking Saved, Applied, Interviewing, and Offered pipeline stages with custom notes.
              </p>
            </div>
            <div style={{ marginTop: '16px', display: 'flex', alignItems: 'center', gap: '6px', color: '#FACC15', fontSize: '0.8125rem', fontWeight: 700 }}>
              <span>View Pipeline</span>
              <ArrowRight size={14} />
            </div>
          </div>

          {/* Tile 6: Career Profile & Smart Completion */}
          <div
            onClick={() => onNavigate('profile')}
            className="client-card client-card-glow"
            style={{ padding: '24px', cursor: 'pointer', display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}
          >
            <div>
              <div style={{ width: '44px', height: '44px', borderRadius: '12px', background: 'rgba(244, 63, 94, 0.15)', color: '#FB7185', display: 'flex', alignItems: 'center', justifyContent: 'center', marginBottom: '14px' }}>
                <User size={22} />
              </div>
              <h4 style={{ fontSize: '1.05rem', fontWeight: 800, margin: '0 0 6px 0' }}>Career Profile & Smart Gaps</h4>
              <p style={{ color: 'var(--app-text-secondary)', fontSize: '0.8125rem', lineHeight: 1.5, margin: 0 }}>
                Manage skills, education, and work experience. 1-click recommendations to reach 100% profile strength.
              </p>
            </div>
            <div style={{ marginTop: '16px', display: 'flex', alignItems: 'center', gap: '6px', color: '#FB7185', fontSize: '0.8125rem', fontWeight: 700 }}>
              <span>Manage Profile</span>
              <ArrowRight size={14} />
            </div>
          </div>
        </div>
      </div>

      {/* Matched Jobs Section */}
      <div className="client-card" style={{ padding: '24px' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px' }}>
          <div>
            <h3 style={{ margin: 0, fontSize: '1.15rem', fontWeight: 800 }}>Top Matched Roles for Your Profile</h3>
            <span style={{ fontSize: '0.8125rem', color: 'var(--app-text-secondary)' }}>Calculated using deep semantic vector comparison</span>
          </div>
          <button onClick={() => onNavigate('jobs')} className="client-btn client-btn-secondary" style={{ padding: '6px 14px', fontSize: '0.8125rem' }}>
            <span>View All Jobs</span>
            <ArrowRight size={14} />
          </button>
        </div>

        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))', gap: '14px' }}>
          {mockJobs.map((job) => (
            <div
              key={job.id}
              style={{
                padding: '18px',
                background: 'var(--app-surface-light)',
                border: '1px solid var(--app-border-subtle)',
                borderRadius: '12px',
                display: 'flex',
                flexDirection: 'column',
                justifyContent: 'space-between',
                gap: '12px',
              }}
            >
              <div>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '6px' }}>
                  <div style={{ fontWeight: 800, fontSize: '0.9375rem', color: 'var(--app-text)' }}>{job.title}</div>
                  <span className="client-badge client-badge-green">{job.match}% Fit</span>
                </div>
                <div style={{ fontSize: '0.75rem', color: 'var(--app-orange)', fontWeight: 600, marginBottom: '8px' }}>
                  {job.company} • {job.location}
                </div>
                <div style={{ display: 'flex', gap: '6px', flexWrap: 'wrap' }}>
                  {job.skills.map((s, idx) => (
                    <span key={idx} className="client-badge client-badge-blue" style={{ fontSize: '0.6875rem' }}>{s}</span>
                  ))}
                </div>
              </div>

              <button
                onClick={() => onNavigate('jobs')}
                className="client-btn client-btn-secondary"
                style={{ width: '100%', padding: '8px', fontSize: '0.75rem', justifyContent: 'center' }}
              >
                <span>Review & Draft Outreach</span>
              </button>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
