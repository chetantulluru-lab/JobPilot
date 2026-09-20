import React, { useState } from 'react';
import { Plus, Sparkles, Trash2, ArrowRight, ArrowLeft } from 'lucide-react';

export default function ClientApplications() {
  const [apps, setApps] = useState([
    { id: 1, title: 'Python Backend Engineer', company: 'TechCorp Cloud', status: 'Interviewing', date: 'Yesterday', match: 94, salary: '$95,000' },
    { id: 2, title: 'Android Compose Developer', company: 'AppScale Studio', status: 'Applied', date: '3 days ago', match: 89, salary: '$110,000' },
    { id: 3, title: 'Distributed Systems Architect', company: 'Nexlify Labs', status: 'Applied', date: '5 days ago', match: 86, salary: '$130,000' },
    { id: 4, title: 'Full Stack Engineer', company: 'CloudBase AI', status: 'Saved', date: '1 week ago', match: 91, salary: '$105,000' },
    { id: 5, title: 'Junior Cloud Developer', company: 'FinTech Global', status: 'Offered', date: 'Just now', match: 95, salary: '$85,000' },
  ]);

  const [showAddModal, setShowAddModal] = useState(false);
  const [newTitle, setNewTitle] = useState('');
  const [newCompany, setNewCompany] = useState('');
  const [newSalary, setNewSalary] = useState('');
  const [newStage, setNewStage] = useState('Saved');

  const stages = ['Saved', 'Applied', 'Interviewing', 'Offered', 'Rejected'];

  const moveStage = (appId, direction) => {
    setApps((prev) =>
      prev.map((a) => {
        if (a.id !== appId) return a;
        const currentIdx = stages.indexOf(a.status);
        const nextIdx = currentIdx + direction;
        if (nextIdx >= 0 && nextIdx < stages.length) {
          return { ...a, status: stages[nextIdx] };
        }
        return a;
      })
    );
  };

  const deleteApp = (appId) => {
    setApps((prev) => prev.filter((a) => a.id !== appId));
  };

  const handleAddApplication = (e) => {
    e.preventDefault();
    if (!newTitle.trim() || !newCompany.trim()) return;

    const newEntry = {
      id: Date.now(),
      title: newTitle.trim(),
      company: newCompany.trim(),
      salary: newSalary.trim() || 'Competitive',
      status: newStage,
      date: 'Today',
      match: Math.floor(Math.random() * 15) + 85,
    };

    setApps([newEntry, ...apps]);
    setNewTitle('');
    setNewCompany('');
    setNewSalary('');
    setShowAddModal(false);
  };

  return (
    <div style={{ maxWidth: '1240px', margin: '0 auto', display: 'flex', flexDirection: 'column', gap: '20px' }}>
      {/* Header */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '12px' }}>
        <div>
          <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '6px' }}>
            <span className="client-badge client-badge-orange">
              <Sparkles size={13} />
              <span>Pipeline Intelligence</span>
            </span>
          </div>
          <h1 style={{ fontSize: '1.75rem', fontWeight: '800', margin: '0 0 4px 0' }}>
            Application Kanban Tracker
          </h1>
          <p style={{ color: 'var(--app-text-secondary)', margin: 0, fontSize: '0.875rem' }}>
            Track application submissions, technical interview rounds, and job offers.
          </p>
        </div>

        <button
          onClick={() => setShowAddModal(true)}
          className="client-btn client-btn-primary"
          style={{ padding: '10px 20px' }}
        >
          <Plus size={16} />
          <span>Add Application</span>
        </button>
      </div>

      {/* Kanban Board Columns */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))', gap: '16px', alignItems: 'start' }}>
        {stages.map((stage) => {
          const stageApps = apps.filter((a) => a.status === stage);
          let badgeColor = '#94A3B8';
          if (stage === 'Applied') badgeColor = '#38BDF8';
          if (stage === 'Interviewing') badgeColor = '#FF6A00';
          if (stage === 'Offered') badgeColor = '#10B981';
          if (stage === 'Rejected') badgeColor = '#EF4444';

          return (
            <div
              key={stage}
              style={{
                background: 'rgba(15, 23, 42, 0.6)',
                borderRadius: '14px',
                border: '1px solid rgba(255, 255, 255, 0.08)',
                padding: '16px',
                display: 'flex',
                flexDirection: 'column',
                gap: '12px',
                minHeight: '400px',
              }}
            >
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', paddingBottom: '10px', borderBottom: '1px solid rgba(255, 255, 255, 0.08)' }}>
                <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                  <span style={{ width: '8px', height: '8px', borderRadius: '50%', background: badgeColor }} />
                  <span style={{ fontSize: '0.875rem', fontWeight: 800, color: '#FFFFFF' }}>{stage}</span>
                </div>
                <span className="client-badge client-badge-blue" style={{ fontSize: '0.6875rem', padding: '2px 8px' }}>
                  {stageApps.length}
                </span>
              </div>

              {stageApps.length === 0 ? (
                <div style={{ padding: '30px 10px', textAlign: 'center', color: '#64748B', fontSize: '0.75rem' }}>
                  No roles in {stage}
                </div>
              ) : (
                stageApps.map((app) => (
                  <div
                    key={app.id}
                    className="client-card"
                    style={{
                      padding: '14px',
                      background: 'rgba(30, 41, 59, 0.6)',
                      border: '1px solid rgba(255, 255, 255, 0.08)',
                      display: 'flex',
                      flexDirection: 'column',
                      gap: '8px',
                    }}
                  >
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
                      <div style={{ fontWeight: 700, fontSize: '0.875rem', color: '#FFFFFF', lineHeight: 1.3 }}>
                        {app.title}
                      </div>
                      <button
                        onClick={() => deleteApp(app.id)}
                        style={{ background: 'transparent', border: 'none', color: '#64748B', cursor: 'pointer', padding: 0 }}
                        title="Delete application"
                      >
                        <Trash2 size={13} />
                      </button>
                    </div>

                    <div style={{ fontSize: '0.75rem', color: 'var(--app-orange)', fontWeight: 600 }}>
                      {app.company}
                    </div>

                    <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '0.6875rem', color: '#94A3B8' }}>
                      <span>{app.salary}</span>
                      <span style={{ color: '#34D399', fontWeight: 700 }}>{app.match}% Match</span>
                    </div>

                    {/* Move Stage Controls */}
                    <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: '6px', paddingTop: '6px', borderTop: '1px solid rgba(255, 255, 255, 0.06)' }}>
                      <button
                        onClick={() => moveStage(app.id, -1)}
                        disabled={stage === stages[0]}
                        className="client-btn"
                        style={{ padding: '3px 8px', fontSize: '0.6875rem', opacity: stage === stages[0] ? 0.3 : 1 }}
                        title="Move left"
                      >
                        <ArrowLeft size={11} />
                      </button>
                      <button
                        onClick={() => moveStage(app.id, 1)}
                        disabled={stage === stages[stages.length - 1]}
                        className="client-btn"
                        style={{ padding: '3px 8px', fontSize: '0.6875rem', opacity: stage === stages[stages.length - 1] ? 0.3 : 1 }}
                        title="Move right"
                      >
                        <ArrowRight size={11} />
                      </button>
                    </div>
                  </div>
                ))
              )}
            </div>
          );
        })}
      </div>

      {/* MODAL: ADD APPLICATION */}
      {showAddModal && (
        <div style={{ position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.8)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 100, padding: '20px' }}>
          <form onSubmit={handleAddApplication} className="client-card client-card-glow" style={{ width: '100%', maxWidth: '460px', padding: '32px' }}>
            <h3 style={{ fontSize: '1.25rem', fontWeight: 800, margin: '0 0 16px 0' }}>Add Target Application</h3>
            <div style={{ display: 'flex', flexDirection: 'column', gap: '14px', marginBottom: '20px' }}>
              <div>
                <label style={{ display: 'block', fontSize: '0.8125rem', fontWeight: 700, marginBottom: '6px', color: 'var(--app-text-secondary)' }}>Job Title</label>
                <input required type="text" placeholder="e.g. Senior Backend Engineer" value={newTitle} onChange={(e) => setNewTitle(e.target.value)} className="client-input" />
              </div>
              <div>
                <label style={{ display: 'block', fontSize: '0.8125rem', fontWeight: 700, marginBottom: '6px', color: 'var(--app-text-secondary)' }}>Company Name</label>
                <input required type="text" placeholder="e.g. TechCorp Cloud" value={newCompany} onChange={(e) => setNewCompany(e.target.value)} className="client-input" />
              </div>
              <div>
                <label style={{ display: 'block', fontSize: '0.8125rem', fontWeight: 700, marginBottom: '6px', color: 'var(--app-text-secondary)' }}>Estimated Salary</label>
                <input type="text" placeholder="e.g. $110,000 / ₹24 LPA" value={newSalary} onChange={(e) => setNewSalary(e.target.value)} className="client-input" />
              </div>
              <div>
                <label style={{ display: 'block', fontSize: '0.8125rem', fontWeight: 700, marginBottom: '6px', color: 'var(--app-text-secondary)' }}>Initial Pipeline Stage</label>
                <select value={newStage} onChange={(e) => setNewStage(e.target.value)} className="client-input">
                  {stages.map((s) => (
                    <option key={s} value={s}>{s}</option>
                  ))}
                </select>
              </div>
            </div>

            <div style={{ display: 'flex', gap: '10px' }}>
              <button type="button" onClick={() => setShowAddModal(false)} className="client-btn client-btn-secondary" style={{ flex: 1 }}>Cancel</button>
              <button type="submit" className="client-btn client-btn-primary" style={{ flex: 1 }}>Save Application</button>
            </div>
          </form>
        </div>
      )}
    </div>
  );
}
