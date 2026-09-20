import React, { useState, useEffect, useCallback } from 'react';
import { 
  User, 
  Sparkles, 
  GraduationCap, 
  Briefcase, 
  Plus, 
  Trash2, 
  CheckCircle2, 
  Save, 
  Compass
} from 'lucide-react';
import api from '../api/apiClient';
import { useAuth } from '../context/AuthContext';

export default function ClientProfile() {
  const { user, updateUser } = useAuth();
  const [profile, setProfile] = useState(null);
  const [activeTab, setActiveTab] = useState('overview'); // 'overview' | 'skills' | 'experience' | 'education' | 'smart'
  const [isSaving, setIsSaving] = useState(false);
  const [savedMsg, setSavedMsg] = useState('');

  // Edit fields
  const [fullName, setFullName] = useState('');
  const [headline, setHeadline] = useState('');
  const [email, setEmail] = useState('');
  const [phone, setPhone] = useState('');
  const [location, setLocation] = useState('');
  const [bio, setBio] = useState('');
  const [targetRole, setTargetRole] = useState('');

  // Collections
  const [skills, setSkills] = useState([]);
  const [newSkillName, setNewSkillName] = useState('');
  const [newSkillCategory, setNewSkillCategory] = useState('Backend');
  const [newSkillLevel, setNewSkillLevel] = useState('Advanced');

  const [education, setEducation] = useState([]);
  const [experience, setExperience] = useState([]);
  const [recommendations, setRecommendations] = useState([]);

  const loadProfile = useCallback(async () => {
    try {
      const p = await api.getProfile();
      setProfile(p);
      setFullName(p.fullName || user?.fullName || 'Chetan Tulluru');
      setHeadline(p.headline || 'Android & Full Stack AI Engineer');
      setEmail(p.email || user?.email || 'candidate@jobpilot.app');
      setPhone(p.phone || '+91 98765 43210');
      setLocation(p.location || 'Hyderabad, India / Remote');
      setBio(p.bio || 'Passionate software engineer building resilient mobile applications and high-throughput backend microservices.');
      setTargetRole(p.targetRole || user?.targetRole || 'Android & Full Stack Engineer');
      setSkills(p.skills || []);
      setEducation(p.education || []);
      setExperience(p.experience || []);

      const smart = await api.getSmartCompletion();
      setRecommendations(smart.recommendations || []);
    } catch (e) {
      console.warn('Profile load error:', e);
    }
  }, [user]);

  useEffect(() => {
    loadProfile();
  }, [loadProfile]);

  const handleSaveProfile = async () => {
    setIsSaving(true);
    setSavedMsg('');
    try {
      const updated = {
        ...profile,
        fullName,
        headline,
        email,
        phone,
        location,
        bio,
        targetRole,
        skills,
        education,
        experience,
      };
      await api.updateProfile(updated);
      setProfile(updated);
      updateUser({ fullName, targetRole });
      setSavedMsg('Profile successfully saved!');
      setTimeout(() => setSavedMsg(''), 3000);
    } catch {
      setSavedMsg('Profile updated locally.');
      setTimeout(() => setSavedMsg(''), 3000);
    } finally {
      setIsSaving(false);
    }
  };

  const addSkill = () => {
    if (!newSkillName.trim()) return;
    const item = {
      name: newSkillName.trim(),
      category: newSkillCategory,
      level: newSkillLevel,
    };
    setSkills([...skills, item]);
    setNewSkillName('');
  };

  const removeSkill = (index) => {
    setSkills(skills.filter((_, idx) => idx !== index));
  };

  const applyRecommendation = (rec) => {
    if (rec.type === 'skills') {
      setSkills([...skills, { name: 'Redis', category: 'Backend', level: 'Advanced' }]);
    } else if (rec.type === 'certification') {
      setBio((prev) => `${prev} Certified in AWS Cloud Architecture.`);
    }
    setRecommendations(recommendations.filter((r) => r.id !== rec.id));
    setSavedMsg(`Applied "${rec.title}"! Profile strength boosted.`);
    setTimeout(() => setSavedMsg(''), 3000);
  };

  return (
    <div style={{ maxWidth: '1080px', margin: '0 auto', display: 'flex', flexDirection: 'column', gap: '24px' }}>
      {/* Top Banner: Profile Strength & Quick Save */}
      <div className="client-card client-card-glow" style={{ padding: '24px 32px' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '20px' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '20px' }}>
            <div style={{ width: '70px', height: '70px', borderRadius: '50%', background: 'linear-gradient(135deg, #FF6A00 0%, #FF8A3D 100%)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#FFFFFF', fontSize: '1.75rem', fontWeight: 800 }}>
              {fullName.charAt(0) || 'C'}
            </div>
            <div>
              <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                <h2 style={{ fontSize: '1.5rem', fontWeight: 800, margin: 0 }}>{fullName}</h2>
                <span className="client-badge client-badge-orange">ACTIVE CANDIDATE</span>
              </div>
              <p style={{ color: 'var(--app-text-secondary)', margin: '4px 0 0 0', fontSize: '0.9375rem' }}>
                {headline} • {location}
              </p>
            </div>
          </div>

          <div style={{ display: 'flex', alignItems: 'center', gap: '24px' }}>
            <div>
              <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '0.8125rem', fontWeight: 700, marginBottom: '6px' }}>
                <span>Profile Strength</span>
                <span style={{ color: 'var(--app-orange)' }}>88% Complete</span>
              </div>
              <div className="roadmap-progress-bar" style={{ width: '180px', height: '8px' }}>
                <div className="roadmap-progress-fill" style={{ width: '88%' }} />
              </div>
            </div>

            <button
              onClick={handleSaveProfile}
              disabled={isSaving}
              className="client-btn client-btn-primary"
              style={{ padding: '10px 20px' }}
            >
              <Save size={16} />
              <span>{isSaving ? 'Saving...' : 'Save Profile'}</span>
            </button>
          </div>
        </div>

        {savedMsg && (
          <div style={{ marginTop: '16px', padding: '8px 14px', background: 'rgba(16, 185, 129, 0.15)', border: '1px solid rgba(16, 185, 129, 0.3)', borderRadius: '8px', color: '#34D399', fontSize: '0.8125rem', display: 'flex', alignItems: 'center', gap: '8px' }}>
            <CheckCircle2 size={16} />
            <span>{savedMsg}</span>
          </div>
        )}
      </div>

      {/* Tabs */}
      <div className="client-tabs">
        <button
          onClick={() => setActiveTab('overview')}
          className={`client-tab-btn ${activeTab === 'overview' ? 'active' : ''}`}
        >
          <User size={15} />
          <span>Personal Info</span>
        </button>
        <button
          onClick={() => setActiveTab('skills')}
          className={`client-tab-btn ${activeTab === 'skills' ? 'active' : ''}`}
        >
          <Sparkles size={15} />
          <span>Technical Skills ({skills.length})</span>
        </button>
        <button
          onClick={() => setActiveTab('experience')}
          className={`client-tab-btn ${activeTab === 'experience' ? 'active' : ''}`}
        >
          <Briefcase size={15} />
          <span>Work Experience ({experience.length})</span>
        </button>
        <button
          onClick={() => setActiveTab('education')}
          className={`client-tab-btn ${activeTab === 'education' ? 'active' : ''}`}
        >
          <GraduationCap size={15} />
          <span>Education ({education.length})</span>
        </button>
        <button
          onClick={() => setActiveTab('smart')}
          className={`client-tab-btn ${activeTab === 'smart' ? 'active' : ''}`}
        >
          <Compass size={15} />
          <span>Smart Completion ({recommendations.length})</span>
        </button>
      </div>

      {/* TAB 1: OVERVIEW */}
      {activeTab === 'overview' && (
        <div className="client-card" style={{ padding: '28px' }}>
          <h3 style={{ fontSize: '1.125rem', fontWeight: 800, marginBottom: '20px' }}>Contact & Bio</h3>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px', marginBottom: '20px' }}>
            <div>
              <label style={{ display: 'block', fontSize: '0.8125rem', fontWeight: 700, marginBottom: '6px', color: 'var(--app-text-secondary)' }}>Full Name</label>
              <input type="text" value={fullName} onChange={(e) => setFullName(e.target.value)} className="client-input" />
            </div>
            <div>
              <label style={{ display: 'block', fontSize: '0.8125rem', fontWeight: 700, marginBottom: '6px', color: 'var(--app-text-secondary)' }}>Headline</label>
              <input type="text" value={headline} onChange={(e) => setHeadline(e.target.value)} className="client-input" />
            </div>
            <div>
              <label style={{ display: 'block', fontSize: '0.8125rem', fontWeight: 700, marginBottom: '6px', color: 'var(--app-text-secondary)' }}>Email</label>
              <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} className="client-input" />
            </div>
            <div>
              <label style={{ display: 'block', fontSize: '0.8125rem', fontWeight: 700, marginBottom: '6px', color: 'var(--app-text-secondary)' }}>Phone</label>
              <input type="text" value={phone} onChange={(e) => setPhone(e.target.value)} className="client-input" />
            </div>
            <div>
              <label style={{ display: 'block', fontSize: '0.8125rem', fontWeight: 700, marginBottom: '6px', color: 'var(--app-text-secondary)' }}>Location</label>
              <input type="text" value={location} onChange={(e) => setLocation(e.target.value)} className="client-input" />
            </div>
            <div>
              <label style={{ display: 'block', fontSize: '0.8125rem', fontWeight: 700, marginBottom: '6px', color: 'var(--app-text-secondary)' }}>Target Engineering Role</label>
              <input type="text" value={targetRole} onChange={(e) => setTargetRole(e.target.value)} className="client-input" />
            </div>
          </div>

          <div>
            <label style={{ display: 'block', fontSize: '0.8125rem', fontWeight: 700, marginBottom: '6px', color: 'var(--app-text-secondary)' }}>Professional Summary / Bio</label>
            <textarea rows={4} value={bio} onChange={(e) => setBio(e.target.value)} className="client-input" style={{ width: '100%', resize: 'vertical' }} />
          </div>
        </div>
      )}

      {/* TAB 2: TECHNICAL SKILLS */}
      {activeTab === 'skills' && (
        <div className="client-card" style={{ padding: '28px' }}>
          <h3 style={{ fontSize: '1.125rem', fontWeight: 800, marginBottom: '16px' }}>Technical Competencies</h3>

          {/* Add Skill Row */}
          <div style={{ display: 'flex', gap: '10px', marginBottom: '24px', flexWrap: 'wrap' }}>
            <input
              type="text"
              placeholder="e.g. Redis, PyTorch, Kubernetes"
              value={newSkillName}
              onChange={(e) => setNewSkillName(e.target.value)}
              className="client-input"
              style={{ flex: 1, minWidth: '200px' }}
            />
            <select
              value={newSkillCategory}
              onChange={(e) => setNewSkillCategory(e.target.value)}
              className="client-input"
              style={{ width: '160px' }}
            >
              <option value="Mobile">Mobile</option>
              <option value="Backend">Backend</option>
              <option value="Frontend">Frontend</option>
              <option value="Database">Database</option>
              <option value="DevOps">DevOps</option>
              <option value="AI / ML">AI / ML</option>
            </select>
            <select
              value={newSkillLevel}
              onChange={(e) => setNewSkillLevel(e.target.value)}
              className="client-input"
              style={{ width: '150px' }}
            >
              <option value="Expert">Expert</option>
              <option value="Advanced">Advanced</option>
              <option value="Proficient">Proficient</option>
              <option value="Familiar">Familiar</option>
            </select>
            <button onClick={addSkill} className="client-btn client-btn-primary" style={{ padding: '0 20px' }}>
              <Plus size={16} />
              <span>Add Skill</span>
            </button>
          </div>

          {/* Skills Grid */}
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(220px, 1fr))', gap: '12px' }}>
            {skills.map((s, idx) => (
              <div
                key={idx}
                style={{
                  padding: '12px 16px',
                  background: 'var(--app-surface-light)',
                  border: '1px solid var(--app-border-subtle)',
                  borderRadius: '10px',
                  display: 'flex',
                  justifyContent: 'space-between',
                  alignItems: 'center',
                }}
              >
                <div>
                  <div style={{ fontWeight: 800, fontSize: '0.875rem', color: 'var(--app-text)' }}>{s.name}</div>
                  <span style={{ fontSize: '0.6875rem', color: 'var(--app-orange)', fontWeight: 600 }}>
                    {s.category} • {s.level}
                  </span>
                </div>
                <button onClick={() => removeSkill(idx)} style={{ background: 'transparent', border: 'none', color: '#EF4444', cursor: 'pointer' }}>
                  <Trash2 size={14} />
                </button>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* TAB 3: WORK EXPERIENCE */}
      {activeTab === 'experience' && (
        <div className="client-card" style={{ padding: '28px' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
            <h3 style={{ fontSize: '1.125rem', fontWeight: 800, margin: 0 }}>Work Experience</h3>
            <button
              onClick={() => {
                const role = prompt('Enter Role Title (e.g. Software Engineer):');
                if (!role) return;
                const company = prompt('Enter Company Name:');
                setExperience([...experience, { role, company: company || 'Tech Company', period: '2024 - Present', highlights: 'Engineered backend microservices.' }]);
              }}
              className="client-btn client-btn-secondary"
              style={{ padding: '6px 14px' }}
            >
              <Plus size={14} />
              <span>Add Experience</span>
            </button>
          </div>

          <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
            {experience.map((exp, idx) => (
              <div key={idx} style={{ padding: '16px', background: 'var(--app-surface-light)', borderRadius: '12px', border: '1px solid var(--app-border-subtle)' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
                  <div>
                    <h4 style={{ margin: '0 0 4px 0', fontSize: '1rem', fontWeight: 800, color: 'var(--app-text)' }}>{exp.role}</h4>
                    <span style={{ fontSize: '0.8125rem', color: 'var(--app-orange)', fontWeight: 600 }}>{exp.company} • {exp.period}</span>
                  </div>
                  <button onClick={() => setExperience(experience.filter((_, i) => i !== idx))} style={{ background: 'transparent', border: 'none', color: '#EF4444', cursor: 'pointer' }}>
                    <Trash2 size={15} />
                  </button>
                </div>
                <p style={{ margin: '8px 0 0 0', fontSize: '0.875rem', color: 'var(--app-text-secondary)' }}>
                  {exp.highlights}
                </p>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* TAB 4: EDUCATION */}
      {activeTab === 'education' && (
        <div className="client-card" style={{ padding: '28px' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
            <h3 style={{ fontSize: '1.125rem', fontWeight: 800, margin: 0 }}>Education & Academics</h3>
            <button
              onClick={() => {
                const degree = prompt('Enter Degree (e.g. B.Tech Computer Science):');
                if (!degree) return;
                const institution = prompt('Enter University / College:');
                setEducation([...education, { degree, institution: institution || 'Engineering Institute', year: '2021 - 2025', gpa: '8.5 / 10' }]);
              }}
              className="client-btn client-btn-secondary"
              style={{ padding: '6px 14px' }}
            >
              <Plus size={14} />
              <span>Add Education</span>
            </button>
          </div>

          <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
            {education.map((edu, idx) => (
              <div key={idx} style={{ padding: '16px', background: 'var(--app-surface-light)', borderRadius: '12px', border: '1px solid var(--app-border-subtle)' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
                  <div>
                    <h4 style={{ margin: '0 0 4px 0', fontSize: '1rem', fontWeight: 800, color: 'var(--app-text)' }}>{edu.degree}</h4>
                    <span style={{ fontSize: '0.8125rem', color: 'var(--app-orange)', fontWeight: 600 }}>{edu.institution} • {edu.year}</span>
                  </div>
                  <button onClick={() => setEducation(education.filter((_, i) => i !== idx))} style={{ background: 'transparent', border: 'none', color: '#EF4444', cursor: 'pointer' }}>
                    <Trash2 size={15} />
                  </button>
                </div>
                <div style={{ marginTop: '6px', fontSize: '0.8125rem', color: '#0369A1', fontWeight: 700 }}>
                  GPA / Grade: {edu.gpa}
                </div>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* TAB 5: SMART COMPLETION */}
      {activeTab === 'smart' && (
        <div className="client-card" style={{ padding: '28px' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '12px', marginBottom: '20px' }}>
            <div style={{ width: '40px', height: '40px', borderRadius: '10px', background: 'var(--app-orange-light)', color: 'var(--app-orange)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
              <Compass size={22} />
            </div>
            <div>
              <h3 style={{ fontSize: '1.125rem', fontWeight: 800, margin: 0 }}>Smart Profile Recommendations</h3>
              <p style={{ margin: 0, fontSize: '0.8125rem', color: 'var(--app-text-secondary)' }}>
                Automated ATS analysis to elevate your profile from 88% to 100%
              </p>
            </div>
          </div>

          <div style={{ display: 'flex', flexDirection: 'column', gap: '14px' }}>
            {recommendations.map((rec) => (
              <div
                key={rec.id}
                style={{
                  padding: '16px 20px',
                  background: 'var(--app-surface-light)',
                  border: '1px solid var(--app-border-subtle)',
                  borderRadius: '12px',
                  display: 'flex',
                  justifyContent: 'space-between',
                  alignItems: 'center',
                  flexWrap: 'wrap',
                  gap: '12px',
                }}
              >
                <div>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '10px', marginBottom: '4px' }}>
                    <span style={{ fontWeight: 800, fontSize: '0.9375rem', color: 'var(--app-text)' }}>{rec.title}</span>
                    <span className="client-badge client-badge-orange">{rec.impact}</span>
                  </div>
                  <p style={{ margin: 0, fontSize: '0.8125rem', color: 'var(--app-text-secondary)' }}>
                    {rec.reason}
                  </p>
                </div>

                <button
                  onClick={() => applyRecommendation(rec)}
                  className="client-btn client-btn-primary"
                  style={{ padding: '8px 16px', fontSize: '0.8125rem' }}
                >
                  <Plus size={14} />
                  <span>1-Click Apply</span>
                </button>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}
