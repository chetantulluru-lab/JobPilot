import React, { useState } from 'react';
import { Send, Copy, Check, Sparkles, MapPin, X, FileCheck2 } from 'lucide-react';
import api from '../api/apiClient';

export default function ClientJobsOutreach({ onNavigateToResume }) {
  const [selectedJob, setSelectedJob] = useState(null);
  const [outreachData, setOutreachData] = useState(null);
  const [activeTab, setActiveTab] = useState('linkedin'); // 'linkedin' | 'email' | 'letter'
  const [copied, setCopied] = useState(false);
  const [isLoading, setIsLoading] = useState(false);

  const jobs = [
    {
      id: 1,
      title: 'Python Backend & Microservices Engineer',
      company: 'TechCorp Cloud',
      location: 'Remote / Bengaluru',
      match: 94,
      skills: ['FastAPI', 'PostgreSQL', 'Docker', 'Asynchronous I/O', 'Redis'],
      hiringLead: 'Sarah Jenkins (Engineering Manager)',
      desc: 'Building high-throughput microservices handling 50k+ daily transactions with low latency.',
    },
    {
      id: 2,
      title: 'Android Jetpack Compose & ML Engineer',
      company: 'AppScale Studio',
      location: 'Remote / Hyderabad',
      match: 89,
      skills: ['Kotlin', 'Jetpack Compose', 'Coroutines', 'ML Kit Face Detection'],
      hiringLead: 'Alex Chen (Lead Mobile Architect)',
      desc: 'Developing on-device computer vision and reactive mobile interfaces for modern Android 11+ devices.',
    },
    {
      id: 3,
      title: 'Full Stack Distributed Systems Engineer',
      company: 'Nexlify Labs',
      location: 'Hybrid / Pune',
      match: 86,
      skills: ['React', 'FastAPI', 'Redis', 'WebSockets', 'PostgreSQL'],
      hiringLead: 'David Miller (VP of Engineering)',
      desc: 'Architecting end-to-end real-time collaboration platforms with live telemetry and async messaging.',
    },
  ];

  const handleOpenOutreach = async (job) => {
    setSelectedJob(job);
    setIsLoading(true);
    try {
      const data = await api.generateOutreach(job.title, job.company, job.hiringLead.split(' ')[0]);
      setOutreachData(data);
    } finally {
      setIsLoading(false);
    }
  };

  const handleCopy = (text) => {
    navigator.clipboard.writeText(text);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  return (
    <div style={{ maxWidth: '1100px', margin: '0 auto', display: 'flex', flexDirection: 'column', gap: '24px' }}>
      {/* Header */}
      <div>
        <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '8px' }}>
          <span className="client-badge client-badge-blue">
            <Sparkles size={13} />
            <span>Semantic Matching & Outreach</span>
          </span>
        </div>
        <h1 style={{ fontSize: '1.85rem', fontWeight: '800', margin: '0 0 6px 0' }}>
          Jobs & Cold Recruiter Outreach
        </h1>
        <p style={{ color: 'var(--app-text-secondary)', margin: 0, fontSize: '0.9375rem' }}>
          Discover roles scored against your digital profile and instantly generate high-converting outreach notes.
        </p>
      </div>

      {/* Jobs Catalog Grid */}
      <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
        {jobs.map((job) => (
          <div key={job.id} className="client-card" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '16px' }}>
            <div style={{ flex: '1 1 500px' }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '10px', marginBottom: '8px' }}>
                <span className="client-badge client-badge-green" style={{ fontSize: '0.8125rem' }}>
                  {job.match}% Semantic Match
                </span>
                <span style={{ fontSize: '0.8125rem', color: 'var(--app-text-muted)', display: 'flex', alignItems: 'center', gap: '4px' }}>
                  <MapPin size={13} /> {job.location}
                </span>
              </div>

              <h3 style={{ fontSize: '1.25rem', fontWeight: '800', margin: '0 0 4px 0' }}>
                {job.title}
              </h3>
              <div style={{ fontSize: '0.875rem', color: 'var(--app-orange)', fontWeight: 700, marginBottom: '8px' }}>
                {job.company} • Hiring Lead: {job.hiringLead}
              </div>
              <p style={{ fontSize: '0.8125rem', color: 'var(--app-text-secondary)', margin: '0 0 12px 0', lineHeight: 1.5 }}>
                {job.desc}
              </p>

              <div style={{ display: 'flex', flexWrap: 'wrap', gap: '6px' }}>
                {job.skills.map((skill, idx) => (
                  <span key={idx} style={{ fontSize: '0.6875rem', padding: '3px 8px', borderRadius: '4px', background: 'var(--app-surface-light)', border: '1px solid var(--app-border-subtle)', color: 'var(--app-text-secondary)' }}>
                    {skill}
                  </span>
                ))}
              </div>
            </div>

            <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', minWidth: '180px' }}>
              <button
                onClick={() => handleOpenOutreach(job)}
                className="client-btn client-btn-primary"
                style={{ width: '100%', fontSize: '0.8125rem' }}
              >
                <Send size={14} />
                <span>AI Cold Outreach</span>
              </button>
              <button
                onClick={onNavigateToResume}
                className="client-btn client-btn-secondary"
                style={{ width: '100%', fontSize: '0.8125rem' }}
              >
                <FileCheck2 size={14} />
                <span>Tailor Resume</span>
              </button>
            </div>
          </div>
        ))}
      </div>

      {/* Interactive Outreach Modal Dialog */}
      {selectedJob && (
        <div
          style={{
            position: 'fixed',
            inset: 0,
            background: 'rgba(15, 23, 42, 0.65)',
            backdropFilter: 'blur(8px)',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            padding: '20px',
            zIndex: 100,
          }}
        >
          <div className="client-card client-card-glow" style={{ width: '100%', maxWidth: '680px', maxHeight: '90vh', overflowY: 'auto', padding: '28px' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px' }}>
              <div>
                <h3 style={{ fontSize: '1.25rem', fontWeight: 800, margin: '0 0 4px 0', color: 'var(--app-text)' }}>
                  AI Recruiter Outreach Artifacts
                </h3>
                <span style={{ fontSize: '0.8125rem', color: 'var(--app-text-secondary)' }}>
                  For {selectedJob.title} at {selectedJob.company}
                </span>
              </div>
              <button onClick={() => setSelectedJob(null)} className="client-btn" style={{ background: 'transparent', padding: '6px' }}>
                <X size={20} color="#94A3B8" />
              </button>
            </div>

            {/* Artifact Tabs */}
            <div className="client-tabs" style={{ marginBottom: '16px' }}>
              <button
                onClick={() => setActiveTab('linkedin')}
                className={`client-tab-btn ${activeTab === 'linkedin' ? 'active' : ''}`}
              >
                LinkedIn Note (&lt;300 chars)
              </button>
              <button
                onClick={() => setActiveTab('email')}
                className={`client-tab-btn ${activeTab === 'email' ? 'active' : ''}`}
              >
                Cold Recruiter Email
              </button>
              <button
                onClick={() => setActiveTab('letter')}
                className={`client-tab-btn ${activeTab === 'letter' ? 'active' : ''}`}
              >
                Formal Cover Letter
              </button>
            </div>

            {/* Tab 1: LinkedIn Note */}
            {activeTab === 'linkedin' && outreachData && (
              <div>
                <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '0.75rem', color: 'var(--app-text-muted)', marginBottom: '8px' }}>
                  <span>Tailored Hook & Skills Alignment</span>
                  <span>{outreachData.linkedin_note.length} / 300 Characters (Strict Limit)</span>
                </div>
                <div style={{ padding: '16px', background: 'var(--app-surface-light)', borderRadius: '10px', fontSize: '0.875rem', lineHeight: 1.6, marginBottom: '16px', border: '1px solid var(--app-border-subtle)', color: 'var(--app-text)' }}>
                  {outreachData.linkedin_note}
                </div>
                <button
                  onClick={() => handleCopy(outreachData.linkedin_note)}
                  className="client-btn client-btn-primary"
                  style={{ width: '100%' }}
                >
                  {copied ? <Check size={16} /> : <Copy size={16} />}
                  <span>{copied ? 'Copied to Clipboard!' : 'Copy LinkedIn Connection Note'}</span>
                </button>
              </div>
            )}

            {/* Tab 2: Cold Email */}
            {activeTab === 'email' && outreachData && (
              <div>
                <div style={{ fontSize: '0.8125rem', fontWeight: 700, marginBottom: '6px', color: 'var(--app-text-secondary)' }}>
                  Subject Line:
                </div>
                <div style={{ padding: '10px 14px', background: 'var(--app-surface-light)', border: '1px solid var(--app-border-subtle)', borderRadius: '8px', fontSize: '0.875rem', marginBottom: '14px', fontWeight: 700, color: 'var(--app-text)' }}>
                  {outreachData.cold_email.subject}
                </div>

                <div style={{ fontSize: '0.8125rem', fontWeight: 700, marginBottom: '6px', color: 'var(--app-text-secondary)' }}>
                  Email Body:
                </div>
                <div style={{ padding: '16px', background: 'var(--app-surface-light)', border: '1px solid var(--app-border-subtle)', borderRadius: '10px', fontSize: '0.8125rem', lineHeight: 1.6, marginBottom: '16px', whiteSpace: 'pre-wrap', color: 'var(--app-text)' }}>
                  {outreachData.cold_email.body}
                </div>
                <button
                  onClick={() => handleCopy(`${outreachData.cold_email.subject}\n\n${outreachData.cold_email.body}`)}
                  className="client-btn client-btn-primary"
                  style={{ width: '100%' }}
                >
                  {copied ? <Check size={16} /> : <Copy size={16} />}
                  <span>{copied ? 'Copied to Clipboard!' : 'Copy Full Email & Subject'}</span>
                </button>
              </div>
            )}

            {/* Tab 3: Formal Cover Letter */}
            {activeTab === 'letter' && outreachData && (
              <div>
                <div style={{ padding: '16px', background: 'var(--app-surface-light)', border: '1px solid var(--app-border-subtle)', borderRadius: '10px', fontSize: '0.8125rem', lineHeight: 1.6, marginBottom: '16px', whiteSpace: 'pre-wrap', color: 'var(--app-text)' }}>
                  {outreachData.cover_letter}
                </div>
                <button
                  onClick={() => handleCopy(outreachData.cover_letter)}
                  className="client-btn client-btn-primary"
                  style={{ width: '100%' }}
                >
                  {copied ? <Check size={16} /> : <Copy size={16} />}
                  <span>{copied ? 'Copied to Clipboard!' : 'Copy Formal Cover Letter'}</span>
                </button>
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  );
}
