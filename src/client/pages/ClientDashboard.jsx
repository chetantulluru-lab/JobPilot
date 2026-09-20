import React from 'react';
import { 
  Sparkles, 
  Video, 
  FileCheck2, 
  Send, 
  Flame, 
  CheckCircle2, 
  ArrowRight, 
  TrendingUp, 
  Award, 
  Briefcase,
  PlayCircle
} from 'lucide-react';
import { useAuth } from '../context/AuthContext';

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
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', flexWrap: 'wrap', gap: '16px' }}>
          <div>
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
            <h1 style={{ fontSize: '1.85rem', fontWeight: '800', margin: '0 0 6px 0' }}>
              Welcome back, {user?.fullName || 'Candidate'}!
            </h1>
            <p style={{ color: 'var(--app-text-secondary)', margin: 0, fontSize: '0.9375rem' }}>
              Target Role: <strong style={{ color: '#FFFFFF' }}>{user?.targetRole || 'Software Engineer'}</strong>
            </p>
          </div>

          <div style={{ display: 'flex', gap: '10px', flexWrap: 'wrap' }}>
            <button
              onClick={() => onNavigate('interview')}
              className="client-btn client-btn-primary"
            >
              <Video size={16} />
              <span>Start Mock Interview</span>
            </button>
            <button
              onClick={() => onNavigate('resume')}
              className="client-btn client-btn-secondary"
            >
              <FileCheck2 size={16} />
              <span>Tailor Resume</span>
            </button>
          </div>
        </div>
      </div>

      {/* 4 Core Telemetry Metric Cards */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(min(100%, 240px), 1fr))', gap: '18px' }}>
        {/* Metric 1: Readiness Score */}
        <div className="client-card" style={{ padding: '20px' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', color: 'var(--app-text-secondary)', fontSize: '0.8125rem', fontWeight: 600 }}>
            <span>Interview Readiness</span>
            <Award size={16} color="#10B981" />
          </div>
          <div style={{ fontSize: '1.75rem', fontWeight: '800', color: '#10B981', marginTop: '6px' }}>
            {user?.readinessScore || 88}%
          </div>
          <div style={{ fontSize: '0.75rem', color: 'var(--app-text-muted)', marginTop: '2px' }}>
            Badge: <strong style={{ color: '#34D399' }}>INTERVIEW READY</strong>
          </div>
        </div>

        {/* Metric 2: Streak Flame */}
        <div className="client-card" style={{ padding: '20px' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', color: 'var(--app-text-secondary)', fontSize: '0.8125rem', fontWeight: 600 }}>
            <span>Learning Streak</span>
            <Flame size={16} color="#FF6A00" />
          </div>
          <div style={{ fontSize: '1.75rem', fontWeight: '800', color: '#FF6A00', marginTop: '6px' }}>
            {user?.streak || 3} Days 🔥
          </div>
          <div style={{ fontSize: '0.75rem', color: 'var(--app-text-muted)', marginTop: '2px' }}>
            Take today's quiz to reach { (user?.streak || 3) + 1 } days!
          </div>
        </div>

        {/* Metric 3: ATS Resume Score */}
        <div className="client-card" style={{ padding: '20px' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', color: 'var(--app-text-secondary)', fontSize: '0.8125rem', fontWeight: 600 }}>
            <span>ATS Resume Score</span>
            <TrendingUp size={16} color="#0284C7" />
          </div>
          <div style={{ fontSize: '1.75rem', fontWeight: '800', color: '#38BDF8', marginTop: '6px' }}>
            {user?.atsScore || 84}/100
          </div>
          <div style={{ fontSize: '0.75rem', color: 'var(--app-text-muted)', marginTop: '2px' }}>
            ATS Optimized for top hiring filters
          </div>
        </div>

        {/* Metric 4: Active Applications */}
        <div className="client-card" style={{ padding: '20px' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', color: 'var(--app-text-secondary)', fontSize: '0.8125rem', fontWeight: 600 }}>
            <span>Active Pipeline</span>
            <Briefcase size={16} color="#A78BFA" />
          </div>
          <div style={{ fontSize: '1.75rem', fontWeight: '800', color: '#A78BFA', marginTop: '6px' }}>
            4 Roles
          </div>
          <div style={{ fontSize: '0.75rem', color: 'var(--app-text-muted)', marginTop: '2px' }}>
            2 Under Review • 1 Interview
          </div>
        </div>
      </div>

      {/* Active Roadmap Banner */}
      <div className="client-card" style={{ borderLeft: '4px solid var(--app-orange)' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '8px', flexWrap: 'wrap', gap: '8px' }}>
          <span className="client-badge client-badge-orange">
            ACTIVE ROADMAP • 6 MONTH CURRICULUM
          </span>
          <span style={{ fontSize: '0.8125rem', color: 'var(--app-orange)', fontWeight: '700' }}>
            Day 14 of 36 (38% Completed)
          </span>
        </div>
        <h3 style={{ fontSize: '1.25rem', fontWeight: '800', margin: '6px 0' }}>
          {user?.activeRoadmap || 'Python Backend & Microservices'}
        </h3>
        <p style={{ color: 'var(--app-text-secondary)', fontSize: '0.875rem', margin: '0 0 14px 0' }}>
          Current Focus: <strong>Day 14 — Asynchronous Task Processing & Worker Architectures (Celery, Redis)</strong>
        </p>
        
        <div style={{ width: '100%', height: '6px', background: 'rgba(255, 255, 255, 0.1)', borderRadius: '3px', marginBottom: '16px' }}>
          <div style={{ width: '38%', height: '100%', background: 'var(--app-orange)', borderRadius: '3px' }} />
        </div>

        <div style={{ display: 'flex', gap: '12px', flexWrap: 'wrap' }}>
          <button
            onClick={() => onNavigate('roadmap')}
            className="client-btn client-btn-primary"
          >
            <PlayCircle size={16} />
            <span>Resume Lesson & Video</span>
          </button>
          <button
            onClick={() => onNavigate('roadmap')}
            className="client-btn client-btn-secondary"
          >
            <Flame size={16} />
            <span>Take Day 14 Quiz</span>
          </button>
        </div>
      </div>

      {/* Recommended Matched Jobs */}
      <div>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '14px' }}>
          <h2 style={{ fontSize: '1.25rem', fontWeight: '800', margin: 0 }}>
            Top Semantic Job Matches
          </h2>
          <button
            onClick={() => onNavigate('jobs')}
            className="client-btn client-btn-secondary"
            style={{ padding: '6px 12px', fontSize: '0.8125rem' }}
          >
            <span>View All Jobs</span>
            <ArrowRight size={14} />
          </button>
        </div>

        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(min(100%, 340px), 1fr))', gap: '16px' }}>
          {mockJobs.map((job) => (
            <div key={job.id} className="client-card" style={{ display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}>
              <div>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '10px' }}>
                  <span className="client-badge client-badge-green" style={{ fontSize: '0.8125rem' }}>
                    {job.match}% Semantic Match
                  </span>
                  <span style={{ fontSize: '0.75rem', color: 'var(--app-text-muted)' }}>{job.location}</span>
                </div>
                <h4 style={{ fontSize: '1.0625rem', fontWeight: '800', margin: '0 0 4px 0' }}>
                  {job.title}
                </h4>
                <div style={{ fontSize: '0.8125rem', color: 'var(--app-orange)', fontWeight: 600, marginBottom: '12px' }}>
                  {job.company}
                </div>

                <div style={{ display: 'flex', flexWrap: 'wrap', gap: '6px', marginBottom: '16px' }}>
                  {job.skills.map((skill, idx) => (
                    <span key={idx} style={{ fontSize: '0.6875rem', padding: '2px 8px', borderRadius: '4px', background: 'rgba(255, 255, 255, 0.06)', color: 'var(--app-text-secondary)' }}>
                      {skill}
                    </span>
                  ))}
                </div>
              </div>

              <div style={{ display: 'flex', gap: '8px' }}>
                <button
                  onClick={() => onNavigate('resume')}
                  className="client-btn client-btn-primary"
                  style={{ flex: 1, padding: '8px 12px', fontSize: '0.8125rem' }}
                >
                  <FileCheck2 size={14} />
                  <span>1-Click Tailor</span>
                </button>
                <button
                  onClick={() => onNavigate('jobs')}
                  className="client-btn client-btn-secondary"
                  style={{ flex: 1, padding: '8px 12px', fontSize: '0.8125rem' }}
                >
                  <Send size={14} />
                  <span>Outreach</span>
                </button>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
