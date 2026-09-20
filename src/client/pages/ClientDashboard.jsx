import React from 'react';
import { 
  Mic, 
  Briefcase, 
  Kanban, 
  ChevronRight, 
  Flame, 
  User, 
  Sparkles,
  ArrowRight,
  PlayCircle,
  Plus
} from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import AIOrb from '../../components/AIOrb';

export default function ClientDashboard({ onNavigate }) {
  const { user } = useAuth();

  const currentStreak = user?.streak || 3;
  const longestStreak = user?.longestStreak || 7;
  const profileStrength = user?.profileStrength || 86;

  const mockRoadmaps = [
    {
      id: 'roadmap-1',
      title: 'Python Backend & Microservices',
      duration: '4 Weeks',
      goal: 'Master FastAPI, AsyncIO, PostgreSQL, Redis, Docker',
      progressPercentage: 39,
      completedDays: 14,
      totalDays: 36,
      isCompleted: false
    },
    {
      id: 'roadmap-2',
      title: 'Android Jetpack Compose & ML Kit',
      duration: '6 Weeks',
      goal: 'Kotlin, Compose UI, Coroutines, CameraX & ML Kit',
      progressPercentage: 65,
      completedDays: 26,
      totalDays: 40,
      isCompleted: false
    }
  ];

  const activeRoadmap = mockRoadmaps[0];

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
    <div className="dashboard-container">
      {/* 1. Metric Cards: Learning Streak & Profile Strength */}
      <div className="metrics-row">
        {/* Daily Streak Card */}
        <div 
          className="android-glass-card metric-card"
          onClick={() => onNavigate('roadmap')}
        >
          <div className="metric-header">
            <span className="metric-title">Learning Streak</span>
            <span className="metric-icon-fire">🔥</span>
          </div>
          <div className="metric-value">{currentStreak} Days</div>
          <div className="metric-sub">Best: {longestStreak} days</div>
        </div>

        {/* Profile Strength Card */}
        <div 
          className="android-glass-card metric-card"
          onClick={() => onNavigate('profile')}
        >
          <div className="metric-header">
            <span className="metric-title">Profile Score</span>
            <User size={18} color="var(--app-orange)" />
          </div>
          <div className="metric-value" style={{ color: profileStrength >= 80 ? '#10B981' : 'var(--app-orange)' }}>
            {profileStrength}%
          </div>
          <div className="metric-sub">Tap to update profile</div>
        </div>
      </div>

      {/* 2. AI Mock Interview Spotlight Card */}
      <div 
        className="android-glass-card interview-spotlight-card"
        onClick={() => onNavigate('interview')}
      >
        <div className="spotlight-icon-box">
          <Mic size={24} color="#FFFFFF" />
        </div>
        <div className="spotlight-content">
          <div className="spotlight-title-row">
            <span className="spotlight-title">AI Mock Interview</span>
            <span className="android-pill-badge">NEW</span>
          </div>
          <div className="spotlight-description">
            Live video simulator with ML Kit face detection & realistic scoring
          </div>
        </div>
        <ChevronRight size={20} color="var(--app-orange)" />
      </div>

      {/* 3. Quick Jump Row: Explore Jobs & Track Applications */}
      <div className="quick-jump-row">
        <div 
          className="android-glass-card quick-jump-card"
          onClick={() => onNavigate('jobs')}
        >
          <Briefcase size={20} color="var(--app-orange)" />
          <div className="quick-jump-info">
            <div className="quick-jump-title">Explore Jobs</div>
            <div className="quick-jump-sub">AI Match & Gap</div>
          </div>
        </div>

        <div 
          className="android-glass-card quick-jump-card"
          onClick={() => onNavigate('applications')}
        >
          <Kanban size={20} color="var(--app-orange)" />
          <div className="quick-jump-info">
            <div className="quick-jump-title">Applications</div>
            <div className="quick-jump-sub">Pipeline tracker</div>
          </div>
        </div>
      </div>

      {/* 4. Active Roadmap Card */}
      <div className="android-glass-card active-roadmap-card">
        {activeRoadmap ? (
          <div>
            <div className="roadmap-card-header">
              <div className="roadmap-status-pill">
                ACTIVE ROADMAP • {activeRoadmap.duration.toUpperCase()}
              </div>
              <div className="roadmap-pct-value">
                {activeRoadmap.progressPercentage}%
              </div>
            </div>

            <h3 className="roadmap-main-title">{activeRoadmap.title}</h3>
            <p className="roadmap-goal-text">Goal: {activeRoadmap.goal}</p>

            <div className="android-progress-track">
              <div 
                className="android-progress-fill" 
                style={{ width: `${activeRoadmap.progressPercentage}%` }} 
              />
            </div>

            <div className="roadmap-completed-text">
              {activeRoadmap.completedDays} of {activeRoadmap.totalDays} days completed
            </div>

            <button 
              onClick={() => onNavigate('roadmap')} 
              className="android-primary-btn"
              style={{ marginTop: '14px' }}
            >
              <span>{activeRoadmap.isCompleted ? 'Review Roadmap' : 'Continue Learning →'}</span>
            </button>
          </div>
        ) : (
          <div style={{ textAlign: 'center', padding: '16px 0' }}>
            <div style={{ display: 'flex', justifyContent: 'center', marginBottom: '12px' }}>
              <AIOrb style={{ width: '56px', height: '56px' }} />
            </div>
            <h4 style={{ fontWeight: 800, margin: '0 0 4px 0' }}>No Active Roadmap</h4>
            <p style={{ color: 'var(--app-text-muted)', fontSize: '0.8125rem', margin: '0 0 16px 0' }}>
              Generate a day-by-day structured curriculum with multilingual practice tasks.
            </p>
            <button onClick={() => onNavigate('roadmap')} className="android-primary-btn">
              <span>+ Create Career Roadmap</span>
            </button>
          </div>
        )}
      </div>

      {/* 5. AI Coach Spotlight Card */}
      <div 
        className="android-glass-card coach-spotlight-card"
        onClick={() => onNavigate('coach')}
      >
        <div style={{ flexShrink: 0 }}>
          <AIOrb style={{ width: '50px', height: '50px' }} />
        </div>
        <div className="coach-content">
          <div className="coach-title">AI Career Coach</div>
          <div className="coach-sub">
            Instant guidance on skills, interview questions, and ATS resume improvements.
          </div>
        </div>
        <ChevronRight size={20} color="var(--app-orange)" />
      </div>

      {/* 6. My Roadmaps Section */}
      <div className="section-block">
        <div className="section-header-row">
          <h3 className="section-heading">My Roadmaps</h3>
          <button 
            onClick={() => onNavigate('roadmap')} 
            className="section-link-btn"
          >
            + New Roadmap
          </button>
        </div>

        <div className="mini-roadmaps-scroll">
          {mockRoadmaps.map((rm) => (
            <div 
              key={rm.id}
              className="android-glass-card mini-roadmap-card"
              onClick={() => onNavigate('roadmap')}
            >
              <div className="mini-rm-header">
                <span className="mini-rm-duration">{rm.duration}</span>
                <span className="mini-rm-pct">{rm.progressPercentage}%</span>
              </div>
              <div className="mini-rm-title">{rm.title}</div>
              <div className="mini-rm-goal">{rm.goal}</div>
              <div className="android-progress-track" style={{ height: '4px', marginTop: '8px' }}>
                <div 
                  className="android-progress-fill" 
                  style={{ width: `${rm.progressPercentage}%` }} 
                />
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* 7. Matched Jobs Catalog */}
      <div className="section-block">
        <div className="section-header-row">
          <div>
            <h3 className="section-heading">Top Matched Roles</h3>
            <span style={{ fontSize: '0.75rem', color: 'var(--app-text-muted)' }}>
              Deep semantic vector matches for your profile
            </span>
          </div>
          <button 
            onClick={() => onNavigate('jobs')} 
            className="section-link-btn"
          >
            View All →
          </button>
        </div>

        <div className="jobs-list">
          {mockJobs.map((job) => (
            <div 
              key={job.id}
              className="android-glass-card job-item-card"
              onClick={() => onNavigate('jobs')}
            >
              <div className="job-item-top">
                <div>
                  <div className="job-item-title">{job.title}</div>
                  <div className="job-item-company">{job.company} • {job.location}</div>
                </div>
                <span className="match-badge">{job.match}% Fit</span>
              </div>
              <div className="job-skills-row">
                {job.skills.map((skill, idx) => (
                  <span key={idx} className="skill-pill">{skill}</span>
                ))}
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
