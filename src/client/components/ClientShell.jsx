import React, { useState } from 'react';
import { 
  LayoutDashboard, 
  Video, 
  FileCheck2, 
  Compass, 
  Briefcase, 
  Kanban, 
  LogOut, 
  ExternalLink, 
  Flame, 
  Sparkles 
} from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import ClientDashboard from '../pages/ClientDashboard';
import ClientMockInterview from '../pages/ClientMockInterview';
import ClientResumeTailor from '../pages/ClientResumeTailor';
import ClientRoadmap from '../pages/ClientRoadmap';
import ClientJobsOutreach from '../pages/ClientJobsOutreach';
import ClientApplications from '../pages/ClientApplications';
import { Link } from 'react-router-dom';

export default function ClientShell() {
  const [currentTab, setCurrentTab] = useState('dashboard');
  const { user, logout } = useAuth();

  const navItems = [
    { id: 'dashboard', label: 'Dashboard', icon: LayoutDashboard },
    { id: 'interview', label: 'AI Mock Interview', icon: Video, badge: 'Face AI' },
    { id: 'resume', label: '1-Click Resume Tailor', icon: FileCheck2 },
    { id: 'roadmap', label: 'Career Roadmaps & Quiz', icon: Compass, badge: 'Quizzes' },
    { id: 'jobs', label: 'Jobs & Cold Outreach', icon: Briefcase },
    { id: 'applications', label: 'Application Tracker', icon: Kanban },
  ];

  const getTitle = () => {
    switch (currentTab) {
      case 'interview': return 'AI Mock Interview Simulator';
      case 'resume': return '1-Click AI Resume Tailor';
      case 'roadmap': return 'Career Roadmaps & Daily Quizzes';
      case 'jobs': return 'Job Catalog & Cold Recruiter Outreach';
      case 'applications': return 'Application Pipeline Tracker';
      case 'dashboard':
      default:
        return 'JobPilot Desktop Workspace';
    }
  };

  return (
    <div className="app-shell">
      {/* Sidebar */}
      <aside className="app-sidebar">
        <div className="app-sidebar-brand">
          <div className="app-sidebar-logo">
            <span>JobPilot</span>
            <span className="symbol">✦</span>
          </div>
          <span className="client-badge client-badge-orange" style={{ fontSize: '0.625rem' }}>
            DESKTOP
          </span>
        </div>

        {/* Navigation Links */}
        <nav className="app-sidebar-nav">
          {navItems.map((item) => {
            const Icon = item.icon;
            const isActive = currentTab === item.id;
            return (
              <button
                key={item.id}
                onClick={() => setCurrentTab(item.id)}
                className={`app-nav-item ${isActive ? 'active' : ''}`}
              >
                <Icon size={18} />
                <span style={{ flex: 1, textAlign: 'left' }}>{item.label}</span>
                {item.badge && (
                  <span className="client-badge client-badge-orange" style={{ fontSize: '0.5625rem', padding: '1px 5px' }}>
                    {item.badge}
                  </span>
                )}
              </button>
            );
          })}
        </nav>

        {/* Sidebar Footer */}
        <div className="app-sidebar-footer">
          <Link
            to="/"
            className="app-nav-item"
            style={{ color: '#38BDF8', background: 'rgba(2, 132, 199, 0.08)' }}
          >
            <Sparkles size={16} />
            <span>3D Interactive Website</span>
            <ExternalLink size={13} style={{ marginLeft: 'auto' }} />
          </Link>

          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', paddingTop: '8px', borderTop: '1px solid var(--app-card-border)' }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
              <div className="app-user-avatar">
                {(user?.fullName || 'C')[0].toUpperCase()}
              </div>
              <div style={{ fontSize: '0.75rem', fontWeight: 700, color: '#FFFFFF' }}>
                {user?.fullName || 'Candidate'}
              </div>
            </div>
            <button
              onClick={logout}
              title="Sign Out"
              className="client-btn"
              style={{ background: 'transparent', padding: '4px', color: '#94A3B8' }}
            >
              <LogOut size={16} />
            </button>
          </div>
        </div>
      </aside>

      {/* Main Workspace Area */}
      <main className="app-main">
        {/* Top Header */}
        <header className="app-header">
          <div className="app-header-title">
            <span>{getTitle()}</span>
          </div>

          <div className="app-header-actions">
            {/* Streak Indicator */}
            <div className="app-streak-badge">
              <Flame size={15} color="#FF6A00" />
              <span>{user?.streak || 3} Days Streak 🔥</span>
            </div>

            {/* User Profile Chip */}
            <div className="app-user-chip">
              <div className="app-user-avatar">
                {(user?.fullName || 'C')[0].toUpperCase()}
              </div>
              <span>{user?.fullName || 'Candidate'}</span>
            </div>
          </div>
        </header>

        {/* Scrollable Page Body */}
        <div className="app-content">
          {currentTab === 'dashboard' && <ClientDashboard onNavigate={(tab) => setCurrentTab(tab)} />}
          {currentTab === 'interview' && <ClientMockInterview onBack={() => setCurrentTab('dashboard')} />}
          {currentTab === 'resume' && <ClientResumeTailor />}
          {currentTab === 'roadmap' && <ClientRoadmap />}
          {currentTab === 'jobs' && <ClientJobsOutreach onNavigateToResume={() => setCurrentTab('resume')} />}
          {currentTab === 'applications' && <ClientApplications />}
        </div>
      </main>
    </div>
  );
}
