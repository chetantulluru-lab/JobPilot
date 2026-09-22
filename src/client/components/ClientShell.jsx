import React, { useState } from 'react';
import { 
  Home, 
  Compass, 
  FileText, 
  User, 
  Settings, 
  Video, 
  Briefcase, 
  Kanban, 
  Bot, 
  Bell, 
  Flame, 
  ArrowLeft,
  LogOut
} from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import AIOrb from '../../components/AIOrb';
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

export default function ClientShell() {
  const [currentScreen, setCurrentScreen] = useState('home');
  const { user, logout } = useAuth();

  if (!user) {
    return <ClientAuth onAuthSuccess={() => setCurrentScreen('home')} />;
  }

  // MVP 5 Tabs matching Android NavGraph.kt
  const mainTabs = [
    { id: 'home', label: 'Home', icon: Home },
    { id: 'roadmap', label: 'Roadmap', icon: Compass },
    { id: 'resume', label: 'Resume', icon: FileText },
    { id: 'profile', label: 'Profile', icon: User },
    { id: 'settings', label: 'Settings', icon: Settings },
  ];

  const isSubScreen = !mainTabs.some(t => t.id === currentScreen);

  const getScreenTitle = () => {
    switch (currentScreen) {
      case 'interview': return 'AI Mock Interview';
      case 'coach': return 'AI Career Assistant';
      case 'jobs': return 'Explore Jobs & Outreach';
      case 'applications': return 'Applications Tracker';
      case 'notifications': return 'Notification Center';
      case 'roadmap': return 'Career Roadmaps';
      case 'resume': return 'Resume Hub & Tailor';
      case 'profile': return 'Career Profile';
      case 'settings': return 'Settings';
      case 'home':
      default:
        return 'JobPilot';
    }
  };

  return (
    <div className="app-container">
      {/* Top Application Bar matching Android Compose Scaffold TopBar */}
      <header className="app-topbar">
        <div className="app-topbar-left">
          {isSubScreen ? (
            <button 
              onClick={() => setCurrentScreen('home')} 
              className="app-icon-btn back-btn"
              title="Back to Home"
            >
              <ArrowLeft size={20} />
              <span className="back-text">Back</span>
            </button>
          ) : (
            <div className="app-brand">
              <div className="app-brand-badge">✦</div>
              <div className="app-brand-text">
                <span className="welcome-greeting">
                  {user?.fullName ? `Welcome, ${user.fullName}` : 'Welcome to JobPilot'}
                </span>
                <span className="brand-title">JobPilot</span>
              </div>
            </div>
          )}
        </div>

        <div className="app-topbar-right">
          {/* Learning Streak Badge */}
          <div 
            className="topbar-streak-chip" 
            onClick={() => setCurrentScreen('roadmap')}
            title="Daily Learning Streak"
          >
            <Flame size={18} className="flame-icon" />
            <span className="streak-count">{user?.streak || 3} Days</span>
          </div>

          {/* Notification Center */}
          <button 
            onClick={() => setCurrentScreen('notifications')} 
            className="app-icon-btn notification-btn"
            title="Notifications"
          >
            <Bell size={18} />
            <span className="unread-dot" />
          </button>

          {/* Glowing AI Orb (Opens AI Career Assistant) */}
          <div 
            onClick={() => setCurrentScreen('coach')} 
            className="topbar-orb-wrapper"
            title="Open AI Career Coach"
          >
            <AIOrb style={{ width: '40px', height: '40px', cursor: 'pointer' }} />
          </div>
        </div>
      </header>

      {/* Main Content Viewport */}
      <main className="app-viewport">
        <div className="app-screen-content">
          {currentScreen === 'home' && (
            <ClientDashboard onNavigate={(screen) => setCurrentScreen(screen)} />
          )}
          {currentScreen === 'roadmap' && (
            <ClientRoadmap />
          )}
          {currentScreen === 'resume' && (
            <ClientResumeTailor />
          )}
          {currentScreen === 'profile' && (
            <ClientProfile />
          )}
          {currentScreen === 'settings' && (
            <ClientSettings onLogout={logout} />
          )}

          {/* Sub-Screens */}
          {currentScreen === 'interview' && (
            <ClientMockInterview
              onBack={() => setCurrentScreen('home')}
              onNavigateToRoadmap={() => setCurrentScreen('roadmap')}
            />
          )}
          {currentScreen === 'coach' && (
            <ClientAICoach
              onNavigateToMockInterview={() => setCurrentScreen('interview')}
              onNavigateToRoadmap={() => setCurrentScreen('roadmap')}
            />
          )}
          {currentScreen === 'jobs' && (
            <ClientJobsOutreach onNavigateToResume={() => setCurrentScreen('resume')} />
          )}
          {currentScreen === 'applications' && (
            <ClientApplications />
          )}
          {currentScreen === 'notifications' && (
            <ClientNotifications onNavigateToSection={(section) => setCurrentScreen(section)} />
          )}
        </div>
      </main>

      {/* 5-Tab Bottom Navigation Bar matching Android Compose NavigationBar */}
      <nav className="app-bottom-nav">
        {mainTabs.map((tab) => {
          const Icon = tab.icon;
          const isActive = currentScreen === tab.id;
          return (
            <button
              key={tab.id}
              onClick={() => setCurrentScreen(tab.id)}
              className={`bottom-nav-item ${isActive ? 'active' : ''}`}
            >
              <div className="nav-icon-container">
                <Icon size={20} />
              </div>
              <span className="nav-label">{tab.label}</span>
            </button>
          );
        })}
      </nav>
    </div>
  );
}
