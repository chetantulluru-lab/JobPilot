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
  Bot,
  User,
  Bell,
  Settings
} from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import ClientDashboard from '../pages/ClientDashboard';
import ClientMockInterview from '../pages/ClientMockInterview';
import ClientResumeTailor from '../pages/ClientResumeTailor';
import ClientRoadmap from '../pages/ClientRoadmap';
import ClientJobsOutreach from '../pages/ClientJobsOutreach';
import ClientApplications from '../pages/ClientApplications';
import ClientAICoach from '../pages/ClientAICoach';
import ClientProfile from '../pages/ClientProfile';
import ClientNotifications from '../pages/ClientNotifications';
import ClientSettings from '../pages/ClientSettings';
import ClientAuth from '../pages/ClientAuth';
import { Link } from 'react-router-dom';

export default function ClientShell() {
  const [currentTab, setCurrentTab] = useState('dashboard');
  const { user, logout } = useAuth();

  // If user explicitly logged out
  if (!user) {
    return <ClientAuth onAuthSuccess={() => setCurrentTab('dashboard')} />;
  }

  const navItems = [
    { id: 'dashboard', label: 'Dashboard', icon: LayoutDashboard },
    { id: 'interview', label: 'AI Mock Interview', icon: Video, badge: 'Face AI' },
    { id: 'resume', label: 'Resume Suite & Tailor', icon: FileCheck2 },
    { id: 'roadmap', label: 'Roadmaps & Quizzes', icon: Compass, badge: 'Quizzes' },
    { id: 'coach', label: 'AI Career Coach', icon: Bot, badge: 'AI Chat' },
    { id: 'jobs', label: 'Jobs & Cold Outreach', icon: Briefcase },
    { id: 'applications', label: 'Kanban Applications', icon: Kanban },
    { id: 'profile', label: 'Career Profile & Gaps', icon: User },
    { id: 'notifications', label: 'Notification Center', icon: Bell },
    { id: 'settings', label: 'Settings & Config', icon: Settings },
  ];

  const getTitle = () => {
    switch (currentTab) {
      case 'interview': return 'AI Mock Interview Simulator';
      case 'resume': return '1-Click AI Resume Tailor & NLP Parser';
      case 'roadmap': return 'Career Roadmaps & Daily Quizzes';
      case 'coach': return 'AI Career Coach (Grounded in Profile)';
      case 'jobs': return 'Job Catalog & Recruiter Cold Outreach';
      case 'applications': return 'Application Kanban Tracker';
      case 'profile': return 'Career Profile & Smart Completion';
      case 'notifications': return 'Notification Center';
      case 'settings': return 'Settings & API Configuration';
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

        <div style={{ padding: '0 20px', marginBottom: '16px' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '10px', padding: '10px', background: 'rgba(30, 41, 59, 0.5)', borderRadius: '10px', border: '1px solid rgba(255, 255, 255, 0.06)' }}>
            <div style={{ width: '32px', height: '32px', borderRadius: '50%', background: 'linear-gradient(135deg, #FF6A00 0%, #FF8A3D 100%)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#FFFFFF', fontWeight: 800, fontSize: '0.875rem' }}>
              {user?.fullName?.charAt(0) || 'C'}
            </div>
            <div style={{ overflow: 'hidden' }}>
              <div style={{ fontSize: '0.8125rem', fontWeight: 700, color: '#FFFFFF', whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>
                {user?.fullName || 'Candidate'}
              </div>
              <div style={{ fontSize: '0.6875rem', color: 'var(--app-orange)', whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>
                {user?.targetRole || 'Software Engineer'}
              </div>
            </div>
          </div>
        </div>

        <nav className="app-sidebar-nav">
          {navItems.map((item) => {
            const Icon = item.icon;
            const isActive = currentTab === item.id;
            return (
              <button
                key={item.id}
                onClick={() => setCurrentTab(item.id)}
                className={`app-sidebar-link ${isActive ? 'active' : ''}`}
              >
                <Icon size={18} />
                <span style={{ flex: 1 }}>{item.label}</span>
                {item.badge && (
                  <span className="client-badge client-badge-orange" style={{ fontSize: '0.625rem', padding: '2px 6px' }}>
                    {item.badge}
                  </span>
                )}
              </button>
            );
          })}
        </nav>

        <div className="app-sidebar-footer">
          <Link to="/" className="app-sidebar-link" title="Open 3D Promotional Website">
            <ExternalLink size={16} />
            <span>Visit 3D Website</span>
          </Link>
          <button onClick={logout} className="app-sidebar-link" style={{ color: '#F87171' }}>
            <LogOut size={16} />
            <span>Sign Out</span>
          </button>
        </div>
      </aside>

      {/* Main Content Area */}
      <div className="app-main-content">
        {/* Header Bar */}
        <header className="app-header">
          <div className="app-header-title">
            <h2 style={{ fontSize: '1.25rem', fontWeight: 800, margin: 0 }}>
              {getTitle()}
            </h2>
          </div>

          <div className="app-header-actions">
            {/* Streak Flame Badge */}
            <div className="streak-indicator" style={{ cursor: 'pointer' }} onClick={() => setCurrentTab('roadmap')}>
              <Flame size={16} className="streak-flame-icon" />
              <span>{user?.streak || 3} Days Streak</span>
            </div>

            {/* Notification Bell with Unread Indicator */}
            <button
              onClick={() => setCurrentTab('notifications')}
              className="client-btn client-btn-secondary"
              style={{ padding: '8px', position: 'relative' }}
              title="Notifications"
            >
              <Bell size={16} />
              <span style={{ position: 'absolute', top: '4px', right: '4px', width: '8px', height: '8px', borderRadius: '50%', background: 'var(--app-orange)' }} />
            </button>

            {/* Settings Gear */}
            <button
              onClick={() => setCurrentTab('settings')}
              className="client-btn client-btn-secondary"
              style={{ padding: '8px' }}
              title="Settings"
            >
              <Settings size={16} />
            </button>

            {/* Profile Avatar */}
            <button
              onClick={() => setCurrentTab('profile')}
              className="client-btn client-btn-secondary"
              style={{ padding: '4px 10px', gap: '8px' }}
              title="View Profile"
            >
              <div style={{ width: '22px', height: '22px', borderRadius: '50%', background: 'var(--app-orange)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#FFFFFF', fontSize: '0.6875rem', fontWeight: 800 }}>
                {user?.fullName?.charAt(0) || 'C'}
              </div>
              <span style={{ fontSize: '0.8125rem', fontWeight: 600 }}>Profile</span>
            </button>
          </div>
        </header>

        {/* Dynamic Page Views */}
        <div className="app-body">
          {currentTab === 'dashboard' && (
            <ClientDashboard onNavigate={(tab) => setCurrentTab(tab)} />
          )}
          {currentTab === 'interview' && (
            <ClientMockInterview onBack={() => setCurrentTab('dashboard')} />
          )}
          {currentTab === 'resume' && (
            <ClientResumeTailor />
          )}
          {currentTab === 'roadmap' && (
            <ClientRoadmap />
          )}
          {currentTab === 'coach' && (
            <ClientAICoach
              onNavigateToMockInterview={() => setCurrentTab('interview')}
              onNavigateToRoadmap={() => setCurrentTab('roadmap')}
            />
          )}
          {currentTab === 'jobs' && (
            <ClientJobsOutreach onNavigateToResume={() => setCurrentTab('resume')} />
          )}
          {currentTab === 'applications' && (
            <ClientApplications />
          )}
          {currentTab === 'profile' && (
            <ClientProfile />
          )}
          {currentTab === 'notifications' && (
            <ClientNotifications onNavigateToSection={(section) => setCurrentTab(section)} />
          )}
          {currentTab === 'settings' && (
            <ClientSettings onLogout={logout} />
          )}
        </div>
      </div>
    </div>
  );
}
