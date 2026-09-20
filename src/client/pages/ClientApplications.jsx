import React, { useState } from 'react';
import { Plus, Sparkles } from 'lucide-react';

export default function ClientApplications() {
  const [apps, setApps] = useState([
    { id: 1, title: 'Python Backend Engineer', company: 'TechCorp Cloud', status: 'Interviewing', date: 'Yesterday', match: 94 },
    { id: 2, title: 'Android Compose Developer', company: 'AppScale Studio', status: 'Applied', date: '3 days ago', match: 89 },
    { id: 3, title: 'Distributed Systems Architect', company: 'Nexlify Labs', status: 'Applied', date: '5 days ago', match: 86 },
    { id: 4, title: 'Full Stack Engineer', company: 'CloudBase AI', status: 'Saved', date: '1 week ago', match: 91 },
    { id: 5, title: 'Junior Cloud Developer', company: 'FinTech Global', status: 'Offered', date: 'Just now', match: 95 },
  ]);

  const stages = ['Saved', 'Applied', 'Interviewing', 'Offered'];

  const moveStage = (appId, nextStage) => {
    setApps((prev) =>
      prev.map((a) => (a.id === appId ? { ...a, status: nextStage } : a))
    );
  };

  return (
    <div style={{ maxWidth: '1180px', margin: '0 auto', display: 'flex', flexDirection: 'column', gap: '24px' }}>
      {/* Header */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '12px' }}>
        <div>
          <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '6px' }}>
            <span className="client-badge client-badge-purple">
              <Sparkles size={13} />
              <span>Pipeline Intelligence</span>
            </span>
          </div>
          <h1 style={{ fontSize: '1.85rem', fontWeight: '800', margin: '0 0 4px 0' }}>
            Application Kanban Tracker
          </h1>
          <p style={{ color: 'var(--app-text-secondary)', margin: 0, fontSize: '0.9375rem' }}>
            Monitor your live interview rounds, submission dates, and recruiter status.
          </p>
        </div>

        <button
          onClick={() => alert('New application modal opened!')}
          className="client-btn client-btn-primary"
        >
          <Plus size={16} />
          <span>Add Application</span>
        </button>
      </div>

      {/* Kanban Board Columns */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(min(100%, 250px), 1fr))', gap: '16px', alignItems: 'start' }}>
        {stages.map((stage) => {
          const stageApps = apps.filter((a) => a.status === stage);
          let badgeClass = 'client-badge-blue';
          if (stage === 'Interviewing') badgeClass = 'client-badge-orange';
          if (stage === 'Offered') badgeClass = 'client-badge-green';

          return (
            <div key={stage} style={{ background: 'rgba(15, 23, 42, 0.5)', borderRadius: '14px', border: '1px solid var(--app-card-border)', padding: '16px', display: 'flex', flexDirection: 'column', gap: '12px', minHeight: '340px' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', paddingBottom: '8px', borderBottom: '1px solid var(--app-card-border)' }}>
                <span style={{ fontSize: '0.875rem', fontWeight: 800, color: '#FFFFFF' }}>
                  {stage}
                </span>
                <span className={`client-badge ${badgeClass}`}>
                  {stageApps.length}
                </span>
              </div>

              <div style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
                {stageApps.map((app) => (
                  <div key={app.id} className="client-card" style={{ padding: '14px', background: 'rgba(25, 34, 57, 0.7)' }}>
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '6px' }}>
                      <span className="client-badge client-badge-green" style={{ fontSize: '0.6875rem' }}>
                        {app.match}% Match
                      </span>
                      <span style={{ fontSize: '0.6875rem', color: 'var(--app-text-muted)' }}>
                        {app.date}
                      </span>
                    </div>

                    <h4 style={{ fontSize: '0.9375rem', fontWeight: 700, margin: '0 0 4px 0' }}>
                      {app.title}
                    </h4>
                    <div style={{ fontSize: '0.8125rem', color: 'var(--app-orange)', fontWeight: 600, marginBottom: '10px' }}>
                      {app.company}
                    </div>

                    {/* Move Stage Selector */}
                    <div style={{ display: 'flex', gap: '4px', flexWrap: 'wrap' }}>
                      {stages.filter((s) => s !== stage).map((target) => (
                        <button
                          key={target}
                          onClick={() => moveStage(app.id, target)}
                          style={{
                            padding: '3px 6px',
                            borderRadius: '4px',
                            background: 'rgba(255, 255, 255, 0.05)',
                            border: 'none',
                            color: 'var(--app-text-secondary)',
                            fontSize: '0.625rem',
                            fontWeight: 700,
                            cursor: 'pointer',
                          }}
                        >
                          $\rightarrow$ {target}
                        </button>
                      ))}
                    </div>
                  </div>
                ))}

                {stageApps.length === 0 && (
                  <div style={{ textAlign: 'center', padding: '32px 12px', color: 'var(--app-text-muted)', fontSize: '0.8125rem' }}>
                    No roles in {stage}
                  </div>
                )}
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
}
