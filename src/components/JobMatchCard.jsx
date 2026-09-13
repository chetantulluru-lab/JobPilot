import React, { useState } from 'react';
import { CheckCircle2, AlertCircle, Sparkles, MapPin, Building } from 'lucide-react';
import { MOCK_JOBS } from '../data/mockJobs';

/**
 * Interactive Job Match Demonstration Card
 * Displays realistic matching breakdown with strong matches, skill gaps, and AI rationale.
 */
export default function JobMatchCard({ className = '' }) {
  const [selectedJobIndex, setSelectedJobIndex] = useState(0);
  const currentJob = MOCK_JOBS[selectedJobIndex];

  return (
    <div className={`job-match-card-main ${className}`}>
      {/* Job Switcher Tabs */}
      <div
        style={{
          display: 'flex',
          gap: '8px',
          overflowX: 'auto',
          paddingBottom: '12px',
          borderBottom: '1px solid rgba(226, 232, 240, 0.8)',
          marginBottom: '24px',
        }}
        role="tablist"
      >
        {MOCK_JOBS.map((job, idx) => (
          <button
            key={job.id}
            role="tab"
            aria-selected={selectedJobIndex === idx}
            onClick={() => setSelectedJobIndex(idx)}
            style={{
              padding: '8px 16px',
              borderRadius: '9999px',
              fontSize: '0.875rem',
              fontWeight: '600',
              fontFamily: 'var(--font-display)',
              background: selectedJobIndex === idx ? 'var(--orange-500)' : 'rgba(241, 245, 249, 0.8)',
              color: selectedJobIndex === idx ? '#FFFFFF' : 'var(--text-secondary)',
              border: selectedJobIndex === idx ? '1px solid var(--orange-600)' : '1px solid transparent',
              transition: 'all 0.2s ease',
              whiteSpace: 'nowrap',
            }}
          >
            {job.title} ({job.matchScore}%)
          </button>
        ))}
      </div>

      {/* Main Role Header */}
      <div
        style={{
          display: 'flex',
          flexWrap: 'wrap',
          alignItems: 'flex-start',
          justifyContent: 'space-between',
          gap: '16px',
          marginBottom: '20px',
        }}
      >
        <div>
          <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '6px' }}>
            <span className="badge badge-orange">{currentJob.matchTier}</span>
            <span style={{ fontSize: '0.8125rem', color: 'var(--text-muted)' }}>
              {currentJob.type}
            </span>
          </div>
          <h3 style={{ fontSize: '1.5rem', fontWeight: '800', color: 'var(--text-primary)' }}>
            {currentJob.title}
          </h3>
          <div
            style={{
              display: 'flex',
              alignItems: 'center',
              gap: '16px',
              marginTop: '6px',
              fontSize: '0.875rem',
              color: 'var(--text-secondary)',
            }}
          >
            <span style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
              <Building size={15} color="var(--orange-500)" />
              {currentJob.company}
            </span>
            <span style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
              <MapPin size={15} color="var(--orange-500)" />
              {currentJob.location}
            </span>
          </div>
        </div>

        {/* Visual Match Score Pill */}
        <div className="match-score-badge-large">
          <div
            style={{
              width: '48px',
              height: '48px',
              borderRadius: '50%',
              background: `conic-gradient(var(--orange-500) 0% ${currentJob.matchScore}%, #E2E8F0 ${currentJob.matchScore}% 100%)`,
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              padding: '4px',
            }}
          >
            <div
              style={{
                width: '100%',
                height: '100%',
                background: '#FFFFFF',
                borderRadius: '50%',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                fontSize: '0.8125rem',
                fontWeight: '800',
                color: 'var(--orange-600)',
              }}
            >
              {currentJob.matchScore}%
            </div>
          </div>
          <div>
            <div style={{ fontSize: '0.6875rem', textTransform: 'uppercase', letterSpacing: '0.05em', color: 'var(--text-muted)', fontWeight: '700' }}>
              Relevance Index
            </div>
            <div style={{ fontSize: '1.0625rem', fontWeight: '800', color: 'var(--text-primary)' }}>
              {currentJob.matchScore}% Match
            </div>
          </div>
        </div>
      </div>

      <p style={{ color: 'var(--text-secondary)', fontSize: '0.9375rem', marginBottom: '24px' }}>
        {currentJob.summary}
      </p>

      {/* Skills Matrix Breakdown */}
      <div
        style={{
          display: 'grid',
          gridTemplateColumns: 'repeat(auto-fit, minmax(min(100%, 200px), 1fr))',
          gap: '16px',
          background: 'rgba(255, 255, 255, 0.7)',
          padding: 'clamp(14px, 3vw, 20px)',
          borderRadius: 'var(--radius-lg)',
          border: '1px solid rgba(226, 232, 240, 0.8)',
          marginBottom: '20px',
        }}
      >
        {/* Strong Matches */}
        <div>
          <div
            style={{
              display: 'flex',
              alignItems: 'center',
              gap: '6px',
              fontSize: '0.875rem',
              fontWeight: '700',
              color: '#065F46',
              marginBottom: '10px',
            }}
          >
            <CheckCircle2 size={16} />
            <span>Strong Matches ({currentJob.strongMatches.length})</span>
          </div>
          <div style={{ display: 'flex', gap: '8px', flexWrap: 'wrap' }}>
            {currentJob.strongMatches.map((skill) => (
              <span key={skill} className="skill-tag skill-tag-strong">
                ✓ {skill}
              </span>
            ))}
          </div>
        </div>

        {/* Potential Skill Gap */}
        <div>
          <div
            style={{
              display: 'flex',
              alignItems: 'center',
              gap: '6px',
              fontSize: '0.875rem',
              fontWeight: '700',
              color: '#C2410C',
              marginBottom: '10px',
            }}
          >
            <AlertCircle size={16} />
            <span>Potential Skill Gap ({currentJob.skillGaps.length})</span>
          </div>
          <div style={{ display: 'flex', gap: '8px', flexWrap: 'wrap' }}>
            {currentJob.skillGaps.map((skill) => (
              <span key={skill} className="skill-tag skill-tag-gap">
                Δ {skill}
              </span>
            ))}
          </div>
        </div>
      </div>

      {/* AI Rationale Insight Callout */}
      <div
        style={{
          display: 'flex',
          gap: '12px',
          alignItems: 'flex-start',
          background: 'linear-gradient(135deg, rgba(255, 247, 237, 0.8) 0%, rgba(255, 255, 255, 0.9) 100%)',
          padding: '16px 20px',
          borderRadius: 'var(--radius-md)',
          border: '1px solid rgba(255, 106, 0, 0.2)',
        }}
      >
        <div
          style={{
            background: 'var(--orange-500)',
            color: '#FFFFFF',
            padding: '6px',
            borderRadius: '8px',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            marginTop: '2px',
          }}
        >
          <Sparkles size={16} />
        </div>
        <div>
          <div style={{ fontSize: '0.8125rem', fontWeight: '700', color: 'var(--orange-700)', marginBottom: '2px' }}>
            JobPilot NLP Rationale
          </div>
          <div style={{ fontSize: '0.875rem', color: 'var(--text-secondary)', lineHeight: '1.5' }}>
            {currentJob.aiInsight}
          </div>
        </div>
      </div>
    </div>
  );
}
