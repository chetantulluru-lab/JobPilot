import React, { useState } from 'react';
import { 
  FileCheck2, 
  Sparkles, 
  Download, 
  Copy, 
  Check, 
  Upload, 
  FileText, 
  Layers, 
  CheckCircle2, 
  ArrowRight
} from 'lucide-react';
import api from '../api/apiClient';

export default function ClientResumeTailor() {
  const [activeTab, setActiveTab] = useState('tailor'); // 'hub' | 'upload' | 'tailor'

  // Tailor State
  const [jobTitle, setJobTitle] = useState('Python Backend Engineer');
  const [companyName, setCompanyName] = useState('TechCorp Cloud');
  const [jobDesc, setJobDesc] = useState(
    'Seeking an experienced Backend Engineer proficient in Python, FastAPI, PostgreSQL, Docker, and Asynchronous I/O to build resilient cloud microservices and handle high concurrent API workloads.'
  );
  const [isLoading, setIsLoading] = useState(false);
  const [tailorResult, setTailorResult] = useState(null);
  const [copied, setCopied] = useState(false);

  // Upload / NLP State
  const [resumeText, setResumeText] = useState('');
  const [isParsing, setIsParsing] = useState(false);
  const [parsedData, setParsedData] = useState(null);

  // Hub State
  const savedResumes = [
    { id: 1, name: 'Chetan_Tulluru_Software_Engineer.pdf', atsScore: 88, role: 'Full Stack & Android Engineer', date: 'Updated Yesterday' },
    { id: 2, name: 'Backend_FastAPI_Specialized.pdf', atsScore: 93, role: 'Python Microservices Architect', date: 'Updated 3 days ago' },
  ];

  const handleTailor = async () => {
    setIsLoading(true);
    try {
      const res = await api.tailorResume(jobTitle, jobDesc, {});
      setTailorResult(res);
    } finally {
      setIsLoading(false);
    }
  };

  const handleParseNLP = async () => {
    setIsParsing(true);
    try {
      const res = await api.parseResumeNLP(resumeText || 'Chetan Tulluru Resume');
      setParsedData(res);
    } finally {
      setIsParsing(false);
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
    <div style={{ maxWidth: '1100px', margin: '0 auto', display: 'flex', flexDirection: 'column', gap: '20px' }}>
      {/* Top Header */}
      <div className="client-card" style={{ padding: '20px 28px', display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '16px' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '14px' }}>
          <div style={{ width: '44px', height: '44px', borderRadius: '12px', background: 'linear-gradient(135deg, #FF6A00 0%, #FF8A3D 100%)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#FFFFFF' }}>
            <FileCheck2 size={22} />
          </div>
          <div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
              <h2 style={{ fontSize: '1.25rem', fontWeight: 800, margin: 0 }}>Resume Intelligence Suite</h2>
              <span className="client-badge client-badge-orange">ATS OPTIMIZED</span>
            </div>
            <span style={{ fontSize: '0.8125rem', color: 'var(--app-text-secondary)' }}>
              Upload resumes, run NLP skill extraction, and 1-click tailor for target jobs
            </span>
          </div>
        </div>

        <div className="client-tabs" style={{ marginBottom: 0 }}>
          <button
            onClick={() => setActiveTab('tailor')}
            className={`client-tab-btn ${activeTab === 'tailor' ? 'active' : ''}`}
            style={{ padding: '6px 14px', fontSize: '0.8125rem' }}
          >
            <Sparkles size={14} />
            <span>1-Click Tailor</span>
          </button>
          <button
            onClick={() => setActiveTab('upload')}
            className={`client-tab-btn ${activeTab === 'upload' ? 'active' : ''}`}
            style={{ padding: '6px 14px', fontSize: '0.8125rem' }}
          >
            <Upload size={14} />
            <span>Upload & Parse (NLP)</span>
          </button>
          <button
            onClick={() => setActiveTab('hub')}
            className={`client-tab-btn ${activeTab === 'hub' ? 'active' : ''}`}
            style={{ padding: '6px 14px', fontSize: '0.8125rem' }}
          >
            <Layers size={14} />
            <span>Resume Hub</span>
          </button>
        </div>
      </div>

      {/* TAB 1: 1-CLICK RESUME TAILOR */}
      {activeTab === 'tailor' && (
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(min(100%, 480px), 1fr))', gap: '20px', alignItems: 'start' }}>
          {/* Left: Input Job Details */}
          <div className="client-card" style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
            <h3 style={{ fontSize: '1.15rem', fontWeight: 800, margin: 0 }}>Target Job Specification</h3>

            <div>
              <label style={{ display: 'block', fontSize: '0.8125rem', fontWeight: 700, marginBottom: '6px', color: 'var(--app-text-secondary)' }}>
                Target Job Title
              </label>
              <input
                type="text"
                value={jobTitle}
                onChange={(e) => setJobTitle(e.target.value)}
                className="client-input"
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
              />
            </div>

            <div>
              <label style={{ display: 'block', fontSize: '0.8125rem', fontWeight: 700, marginBottom: '6px', color: 'var(--app-text-secondary)' }}>
                Job Description / Requirements
              </label>
              <textarea
                rows={6}
                value={jobDesc}
                onChange={(e) => setJobDesc(e.target.value)}
                className="client-input"
                style={{ width: '100%', resize: 'vertical' }}
              />
            </div>

            <button
              onClick={handleTailor}
              disabled={isLoading || !jobDesc.trim()}
              className="client-btn client-btn-primary"
              style={{ padding: '12px', marginTop: '4px' }}
            >
              <Sparkles size={16} />
              <span>{isLoading ? 'Analyzing & Tailoring...' : '1-Click Tailor Resume'}</span>
            </button>
          </div>

          {/* Right: Tailoring Report */}
          <div className="client-card client-card-glow" style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
            <h3 style={{ fontSize: '1.15rem', fontWeight: 800, margin: 0 }}>Optimization Results</h3>

            {!tailorResult ? (
              <div style={{ textAlign: 'center', padding: '40px 20px', color: 'var(--app-text-muted)' }}>
                <FileCheck2 size={40} style={{ margin: '0 auto 12px auto', opacity: 0.5 }} />
                <p style={{ margin: 0, fontSize: '0.875rem' }}>
                  Paste a job description and click <strong>1-Click Tailor Resume</strong> to generate optimized ATS metrics and tailored bullet points.
                </p>
              </div>
            ) : (
              <>
                {/* ATS Score Transformation */}
                <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', padding: '16px 20px', background: 'var(--app-surface-light)', borderRadius: '12px', border: '1px solid var(--app-border-subtle)' }}>
                  <div>
                    <div style={{ fontSize: '0.75rem', color: 'var(--app-text-secondary)', fontWeight: 600 }}>Original ATS Match</div>
                    <div style={{ fontSize: '1.5rem', fontWeight: 800, color: 'var(--app-text-muted)' }}>{tailorResult.original_ats_score}%</div>
                  </div>

                  <ArrowRight size={24} color="var(--app-orange)" />

                  <div style={{ textAlign: 'right' }}>
                    <div style={{ fontSize: '0.75rem', color: '#047857', fontWeight: 700 }}>Tailored ATS Match</div>
                    <div style={{ fontSize: '1.75rem', fontWeight: 800, color: '#047857' }}>{tailorResult.tailored_ats_score}%</div>
                  </div>
                </div>

                {/* Matched Keywords */}
                <div>
                  <div style={{ fontSize: '0.8125rem', fontWeight: 700, marginBottom: '8px', color: 'var(--app-text-secondary)' }}>
                    Injected High-Yield Keywords:
                  </div>
                  <div style={{ display: 'flex', gap: '6px', flexWrap: 'wrap' }}>
                    {tailorResult.matched_keywords?.map((kw, idx) => (
                      <span key={idx} className="client-badge client-badge-blue" style={{ fontSize: '0.6875rem' }}>
                        ✓ {kw}
                      </span>
                    ))}
                  </div>
                </div>

                {/* Tailored Bullets */}
                <div>
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '8px' }}>
                    <span style={{ fontSize: '0.8125rem', fontWeight: 700, color: 'var(--app-text-secondary)' }}>
                      Tailored Achievement Bullets:
                    </span>
                    <button onClick={copyBullets} className="client-btn client-btn-secondary" style={{ padding: '4px 8px', fontSize: '0.75rem' }}>
                      {copied ? <Check size={13} color="#10B981" /> : <Copy size={13} />}
                      <span>{copied ? 'Copied' : 'Copy'}</span>
                    </button>
                  </div>

                  <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
                    {tailorResult.suggested_bullets?.map((bullet, idx) => (
                      <div key={idx} style={{ fontSize: '0.8125rem', lineHeight: 1.5, padding: '10px 14px', background: 'var(--app-surface-light)', borderRadius: '8px', border: '1px solid var(--app-border-subtle)', borderLeft: '3px solid var(--app-orange)', color: 'var(--app-text)' }}>
                        {bullet}
                      </div>
                    ))}
                  </div>
                </div>

                <button
                  onClick={() => alert('Tailored ATS Resume downloaded as PDF!')}
                  className="client-btn client-btn-primary"
                  style={{ width: '100%', padding: '10px', marginTop: '8px' }}
                >
                  <Download size={16} />
                  <span>Download Tailored PDF Resume</span>
                </button>
              </>
            )}
          </div>
        </div>
      )}

      {/* TAB 2: UPLOAD & PARSE (NLP) */}
      {activeTab === 'upload' && (
        <div className="client-card" style={{ padding: '32px' }}>
          <h3 style={{ fontSize: '1.25rem', fontWeight: 800, margin: '0 0 8px 0' }}>Upload Existing Resume (PDF / DOCX)</h3>
          <p style={{ color: 'var(--app-text-secondary)', fontSize: '0.875rem', margin: '0 0 24px 0' }}>
            JobPilot NLP extracts skills, education, and career experience from your existing resume to automatically populate your career profile.
          </p>

          <div
            style={{
              border: '2px dashed rgba(255, 106, 0, 0.4)',
              borderRadius: '16px',
              padding: '40px 20px',
              textAlign: 'center',
              background: 'var(--app-orange-light)',
              marginBottom: '24px',
              cursor: 'pointer',
            }}
            onClick={() => handleParseNLP()}
          >
            <Upload size={36} color="var(--app-orange)" style={{ margin: '0 auto 12px auto' }} />
            <h4 style={{ margin: '0 0 4px 0', fontSize: '1rem', fontWeight: 800, color: 'var(--app-text)' }}>Click to Upload Resume or Drag & Drop</h4>
            <span style={{ fontSize: '0.75rem', color: 'var(--app-text-muted)' }}>PDF, DOCX up to 10MB</span>
          </div>

          <div style={{ marginBottom: '20px' }}>
            <label style={{ display: 'block', fontSize: '0.8125rem', fontWeight: 700, marginBottom: '6px', color: 'var(--app-text-secondary)' }}>
              Or Paste Plain Resume Text Below:
            </label>
            <textarea
              rows={5}
              value={resumeText}
              onChange={(e) => setResumeText(e.target.value)}
              placeholder="Paste summary, work history, and technical competencies..."
              className="client-textarea"
              style={{ width: '100%', resize: 'vertical' }}
            />
          </div>

          <button
            onClick={handleParseNLP}
            disabled={isParsing}
            className="client-btn client-btn-primary"
            style={{ padding: '10px 24px' }}
          >
            <Sparkles size={16} />
            <span>{isParsing ? 'Running NLP Extraction...' : 'Extract & Seed Profile'}</span>
          </button>

          {parsedData && (
            <div style={{ marginTop: '24px', padding: '20px', background: 'var(--app-success-bg)', border: '1px solid rgba(16, 185, 129, 0.3)', borderRadius: '12px' }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '8px', color: '#047857', fontWeight: 800, marginBottom: '12px' }}>
                <CheckCircle2 size={18} />
                <span>Resume Parsed Successfully!</span>
              </div>
              <div style={{ fontSize: '0.875rem', lineHeight: 1.6, color: 'var(--app-text)' }}>
                <div><strong>Candidate:</strong> {parsedData.extracted_name} ({parsedData.extracted_email})</div>
                <div><strong>Education:</strong> {parsedData.extracted_education}</div>
                <div><strong>Extracted Skills:</strong> {parsedData.extracted_skills?.join(', ')}</div>
                <div style={{ marginTop: '8px', color: 'var(--app-orange)', fontWeight: 700 }}>
                  Baseline ATS Score: {parsedData.ats_score}%
                </div>
              </div>
            </div>
          )}
        </div>
      )}

      {/* TAB 3: RESUME HUB */}
      {activeTab === 'hub' && (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
          {savedResumes.map((res) => (
            <div
              key={res.id}
              className="client-card"
              style={{ padding: '20px 24px', display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '16px' }}
            >
              <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
                <div style={{ width: '44px', height: '44px', borderRadius: '12px', background: 'rgba(2, 132, 199, 0.15)', color: '#38BDF8', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                  <FileText size={22} />
                </div>
                <div>
                  <h4 style={{ margin: '0 0 4px 0', fontSize: '1rem', fontWeight: 700 }}>{res.name}</h4>
                  <span style={{ fontSize: '0.75rem', color: 'var(--app-text-secondary)' }}>
                    {res.role} • {res.date}
                  </span>
                </div>
              </div>

              <div style={{ display: 'flex', alignItems: 'center', gap: '20px' }}>
                <div style={{ textAlign: 'right' }}>
                  <div style={{ fontSize: '0.6875rem', color: 'var(--app-text-secondary)' }}>ATS Compatibility</div>
                  <div style={{ fontSize: '1.25rem', fontWeight: 800, color: '#34D399' }}>{res.atsScore}%</div>
                </div>

                <button
                  onClick={() => alert(`Downloading ${res.name}...`)}
                  className="client-btn client-btn-secondary"
                  style={{ padding: '8px 16px' }}
                >
                  <Download size={15} />
                  <span>Download PDF</span>
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
