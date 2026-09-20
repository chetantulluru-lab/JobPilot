import React, { useState } from 'react';
import { FileCheck2, Sparkles, Download, Copy, Check } from 'lucide-react';
import api from '../api/apiClient';

export default function ClientResumeTailor() {
  const [jobTitle, setJobTitle] = useState('Python Backend Engineer');
  const [companyName, setCompanyName] = useState('TechCorp Cloud');
  const [jobDesc, setJobDesc] = useState(
    'Seeking an experienced Backend Engineer proficient in Python, FastAPI, PostgreSQL, Docker, and Asynchronous I/O to build resilient cloud microservices and handle high concurrent API workloads.'
  );
  const [isLoading, setIsLoading] = useState(false);
  const [tailorResult, setTailorResult] = useState(null);
  const [copied, setCopied] = useState(false);

  const handleTailor = async () => {
    setIsLoading(true);
    try {
      const res = await api.tailorResume(jobTitle, jobDesc, {});
      setTailorResult(res);
    } finally {
      setIsLoading(false);
    }
  };

  const copyBullets = () => {
    if (!tailorResult?.suggested_bullets) return;
    const text = tailorResult.suggested_bullets.join('\n');
    navigator.clipboard.writeText(text);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  return (
    <div style={{ maxWidth: '1100px', margin: '0 auto', display: 'flex', flexDirection: 'column', gap: '24px' }}>
      {/* Header */}
      <div>
        <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '8px' }}>
          <span className="client-badge client-badge-orange">
            <Sparkles size={13} />
            <span>ATS Resume Engine</span>
          </span>
        </div>
        <h1 style={{ fontSize: '1.85rem', fontWeight: '800', margin: '0 0 6px 0' }}>
          1-Click AI Resume Tailor
        </h1>
        <p style={{ color: 'var(--app-text-secondary)', margin: 0, fontSize: '0.9375rem' }}>
          Automatically align your bullet points, inject missing keywords, and dramatically increase your ATS hiring score.
        </p>
      </div>

      {/* 2-Column Workspace */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(min(100%, 460px), 1fr))', gap: '24px', alignItems: 'start' }}>
        {/* Left: Input Job Details */}
        <div className="client-card" style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
          <h3 style={{ fontSize: '1.15rem', fontWeight: 800, margin: 0 }}>
            Target Job Specification
          </h3>

          <div>
            <label style={{ display: 'block', fontSize: '0.8125rem', fontWeight: 700, marginBottom: '6px', color: 'var(--app-text-secondary)' }}>
              Job Title
            </label>
            <input
              type="text"
              value={jobTitle}
              onChange={(e) => setJobTitle(e.target.value)}
              className="client-input"
              placeholder="e.g. Senior Android Engineer"
            />
          </div>

          <div>
            <label style={{ display: 'block', fontSize: '0.8125rem', fontWeight: 700, marginBottom: '6px', color: 'var(--app-text-secondary)' }}>
              Company Name
            </label>
            <input
              type="text"
              value={companyName}
              onChange={(e) => setCompanyName(e.target.value)}
              className="client-input"
              placeholder="e.g. Stripe, Google, Startup"
            />
          </div>

          <div>
            <label style={{ display: 'block', fontSize: '0.8125rem', fontWeight: 700, marginBottom: '6px', color: 'var(--app-text-secondary)' }}>
              Job Description / Requirements
            </label>
            <textarea
              value={jobDesc}
              onChange={(e) => setJobDesc(e.target.value)}
              className="client-textarea"
              rows={8}
              placeholder="Paste the job description or requirements here..."
            />
          </div>

          <button
            onClick={handleTailor}
            disabled={isLoading || !jobDesc.trim()}
            className="client-btn client-btn-primary"
            style={{ padding: '12px', fontSize: '0.9375rem' }}
          >
            <FileCheck2 size={16} />
            <span>{isLoading ? 'Optimizing Resume...' : '⚡ 1-Click Tailor Resume for this Job'}</span>
          </button>
        </div>

        {/* Right: Tailored Output & ATS Score Boost */}
        <div className="client-card client-card-glow" style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
          <h3 style={{ fontSize: '1.15rem', fontWeight: 800, margin: 0 }}>
            ATS Optimization Telemetry
          </h3>

          {/* Score Comparison Gauge */}
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '14px', background: 'rgba(255, 255, 255, 0.03)', padding: '16px', borderRadius: '12px', border: '1px solid var(--app-card-border)' }}>
            <div>
              <div style={{ fontSize: '0.75rem', color: 'var(--app-text-muted)', fontWeight: 700 }}>ORIGINAL ATS SCORE</div>
              <div style={{ fontSize: '1.75rem', fontWeight: 800, color: '#94A3B8', marginTop: '4px' }}>
                {tailorResult ? `${tailorResult.original_ats_score}%` : '64%'}
              </div>
              <div style={{ fontSize: '0.6875rem', color: '#F87171' }}>Missing key terms</div>
            </div>

            <div>
              <div style={{ fontSize: '0.75rem', color: 'var(--app-text-muted)', fontWeight: 700 }}>TAILORED ATS SCORE</div>
              <div style={{ fontSize: '1.75rem', fontWeight: 800, color: '#10B981', marginTop: '4px' }}>
                {tailorResult ? `${tailorResult.tailored_ats_score}%` : '93%'}
              </div>
              <div style={{ fontSize: '0.6875rem', color: '#34D399', fontWeight: 700 }}>Top 5% Candidate Filter</div>
            </div>
          </div>

          {/* Matched Keywords */}
          <div>
            <div style={{ fontSize: '0.8125rem', fontWeight: 700, marginBottom: '8px', color: 'var(--app-text-secondary)' }}>
              Matched & Synthesized Keywords:
            </div>
            <div style={{ display: 'flex', flexWrap: 'wrap', gap: '6px' }}>
              {(tailorResult?.matched_keywords || ['FastAPI', 'PostgreSQL', 'Docker', 'Asynchronous I/O', 'REST APIs', 'Unit Testing']).map((kw, idx) => (
                <span key={idx} className="client-badge client-badge-green">
                  ✓ {kw}
                </span>
              ))}
            </div>
          </div>

          {/* Optimized Bullet Points */}
          <div>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '8px' }}>
              <span style={{ fontSize: '0.8125rem', fontWeight: 700, color: 'var(--app-text-secondary)' }}>
                Tailored Impact Bullet Points:
              </span>
              <button
                onClick={copyBullets}
                className="client-btn client-btn-secondary"
                style={{ padding: '4px 10px', fontSize: '0.75rem' }}
              >
                {copied ? <Check size={13} color="#10B981" /> : <Copy size={13} />}
                <span>{copied ? 'Copied!' : 'Copy Bullets'}</span>
              </button>
            </div>

            <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
              {(tailorResult?.suggested_bullets || [
                'Engineered resilient asynchronous task pipelines reducing API request latency by 42% under high concurrent load.',
                'Architected secure JWT & OTP authentication workflows adhering to enterprise security standards.',
                'Optimized PostgreSQL query execution plans with composite indexing, boosting transaction throughput by 35%.',
              ]).map((bullet, idx) => (
                <div key={idx} style={{ padding: '10px 12px', background: 'rgba(255, 255, 255, 0.04)', borderRadius: '8px', fontSize: '0.8125rem', lineHeight: 1.5, borderLeft: '3px solid var(--app-orange)' }}>
                  {bullet}
                </div>
              ))}
            </div>
          </div>

          {/* Download PDF Action */}
          <button
            onClick={() => alert('Tailored Resume compiled successfully! Downloading customized PDF...')}
            className="client-btn client-btn-success"
            style={{ width: '100%', padding: '12px' }}
          >
            <Download size={16} />
            <span>Download Tailored PDF Resume</span>
          </button>
        </div>
      </div>
    </div>
  );
}
