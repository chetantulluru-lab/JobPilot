import React, { useState, useEffect, useCallback } from 'react';
import { 
  Bell, 
  CheckCheck, 
  Flame, 
  Briefcase, 
  Video, 
  FileCheck2, 
  Trash2, 
  CheckCircle2
} from 'lucide-react';
import api from '../api/apiClient';

export default function ClientNotifications({ onNavigateToSection: _onNavigateToSection }) {
  const [notifications, setNotifications] = useState([]);
  const [filter, setFilter] = useState('all'); // 'all' | 'unread'

  const loadNotifications = useCallback(async () => {
    const list = await api.getNotifications();
    setNotifications(list);
  }, []);

  useEffect(() => {
    loadNotifications();
  }, [loadNotifications]);

  const markAllRead = () => {
    setNotifications(notifications.map((n) => ({ ...n, unread: false })));
  };

  const markRead = (id) => {
    setNotifications(
      notifications.map((n) => (n.id === id ? { ...n, unread: false } : n))
    );
  };

  const deleteNotification = (id) => {
    setNotifications(notifications.filter((n) => n.id !== id));
  };

  const filtered = notifications.filter((n) => {
    if (filter === 'unread') return n.unread;
    return true;
  });

  const getIcon = (type) => {
    switch (type) {
      case 'streak':
        return <Flame size={20} color="#FF6A00" />;
      case 'application':
        return <Briefcase size={20} color="#0284C7" />;
      case 'interview':
        return <Video size={20} color="#10B981" />;
      case 'resume':
        return <FileCheck2 size={20} color="#8B5CF6" />;
      default:
        return <Bell size={20} color="#FF6A00" />;
    }
  };

  return (
    <div style={{ maxWidth: '840px', margin: '0 auto', display: 'flex', flexDirection: 'column', gap: '20px' }}>
      {/* Header Bar */}
      <div className="client-card" style={{ padding: '20px 28px', display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '16px' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
          <div style={{ width: '44px', height: '44px', borderRadius: '12px', background: 'rgba(255, 106, 0, 0.15)', color: 'var(--app-orange)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
            <Bell size={22} />
          </div>
          <div>
            <h2 style={{ fontSize: '1.25rem', fontWeight: 800, margin: 0 }}>Notification Center</h2>
            <span style={{ fontSize: '0.8125rem', color: 'var(--app-text-secondary)' }}>
              Stay updated on application progress, daily streaks, and interview invitations
            </span>
          </div>
        </div>

        <div style={{ display: 'flex', gap: '10px', alignItems: 'center' }}>
          <div className="client-tabs" style={{ marginBottom: 0 }}>
            <button
              onClick={() => setFilter('all')}
              className={`client-tab-btn ${filter === 'all' ? 'active' : ''}`}
              style={{ padding: '6px 14px', fontSize: '0.75rem' }}
            >
              All ({notifications.length})
            </button>
            <button
              onClick={() => setFilter('unread')}
              className={`client-tab-btn ${filter === 'unread' ? 'active' : ''}`}
              style={{ padding: '6px 14px', fontSize: '0.75rem' }}
            >
              Unread ({notifications.filter((n) => n.unread).length})
            </button>
          </div>

          <button
            onClick={markAllRead}
            className="client-btn client-btn-secondary"
            style={{ padding: '6px 12px', fontSize: '0.75rem' }}
          >
            <CheckCheck size={14} />
            <span>Mark All Read</span>
          </button>
        </div>
      </div>

      {/* Notifications List */}
      {filtered.length === 0 ? (
        <div className="client-card" style={{ padding: '48px', textAlign: 'center' }}>
          <CheckCircle2 size={44} color="#10B981" style={{ margin: '0 auto 16px auto' }} />
          <h3 style={{ fontSize: '1.125rem', fontWeight: 700, margin: '0 0 6px 0' }}>All Caught Up!</h3>
          <p style={{ color: 'var(--app-text-secondary)', margin: 0, fontSize: '0.875rem' }}>
            You have no unread notifications. Keep continuing your daily practice streak.
          </p>
        </div>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
          {filtered.map((item) => (
            <div
              key={item.id}
              onClick={() => markRead(item.id)}
              className="client-card"
              style={{
                padding: '18px 24px',
                cursor: 'pointer',
                background: item.unread ? 'rgba(255, 106, 0, 0.08)' : 'rgba(30, 41, 59, 0.4)',
                border: item.unread ? '1px solid rgba(255, 106, 0, 0.35)' : '1px solid rgba(255, 255, 255, 0.08)',
                display: 'flex',
                gap: '16px',
                alignItems: 'flex-start',
                position: 'relative',
              }}
            >
              <div style={{ width: '40px', height: '40px', borderRadius: '10px', background: 'rgba(15, 23, 42, 0.6)', display: 'flex', alignItems: 'center', justifyContent: 'center', flexShrink: 0 }}>
                {getIcon(item.type)}
              </div>

              <div style={{ flex: 1 }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '4px' }}>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                    <h4 style={{ margin: 0, fontSize: '0.9375rem', fontWeight: 700 }}>{item.title}</h4>
                    {item.unread && (
                      <span style={{ width: '8px', height: '8px', borderRadius: '50%', background: 'var(--app-orange)' }} />
                    )}
                  </div>
                  <span style={{ fontSize: '0.75rem', color: '#64748B' }}>{item.time}</span>
                </div>

                <p style={{ margin: 0, fontSize: '0.875rem', color: 'var(--app-text-secondary)', lineHeight: 1.5 }}>
                  {item.message}
                </p>
              </div>

              <button
                onClick={(e) => {
                  e.stopPropagation();
                  deleteNotification(item.id);
                }}
                style={{ background: 'transparent', border: 'none', color: '#64748B', cursor: 'pointer', padding: '4px' }}
                title="Dismiss"
              >
                <Trash2 size={15} />
              </button>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
