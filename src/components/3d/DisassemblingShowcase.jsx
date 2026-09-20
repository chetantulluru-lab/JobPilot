import React, { useState } from 'react';
import { Sparkles, Video, FileCheck2, Flame, Cpu, Wrench } from 'lucide-react';
import Robot3DCanvas from './Robot3DCanvas';

export default function DisassemblingShowcase() {
  const [exploded, setExploded] = useState(false);
  const [activeModule, setActiveModule] = useState('all'); // 'all' | 'head' | 'core' | 'arms' | 'visor'

  const modules = [
    {
      id: 'all',
      title: 'Unified Cybernetic Copilot',
      subtitle: 'Complete AI Career Platform',
      progress: exploded ? 0.85 : 0.0,
      description:
        'The JobPilot AI Copilot is an integrated autonomous career assistant. Toggle the exploded view below to inspect its sub-assemblies in 3D.',
      icon: Cpu,
      color: '#FF6A00',
    },
    {
      id: 'head',
      title: 'NLP Cognitive Head Module',
      subtitle: 'Resume Parsing & Keyword Extraction',
      progress: 0.9,
      description:
        'Analyzes unstructured PDF resumes, extracts competencies, and structures your career graph into standardized intelligence.',
      icon: Sparkles,
      color: '#0284C7',
    },
    {
      id: 'visor',
      title: 'Optic Face Tracking Visor',
      subtitle: 'On-Device Computer Vision',
      progress: 0.95,
      description:
        'Powers real-time face detection, posture alignment, and gaze tracking during AI Mock Interviews without sending video to any cloud server.',
      icon: Video,
      color: '#10B981',
    },
    {
      id: 'core',
      title: 'Arc-Reactor Semantic Match Engine',
      subtitle: '0-100% Job Compatibility Scoring',
      progress: 0.8,
      description:
        'Deep vector comparison engine calculating context-aware fit between your profile and live job market demands.',
      icon: Flame,
      color: '#FF8533',
    },
    {
      id: 'arms',
      title: 'Kinetic Resume Tailor & Outreach Actuator',
      subtitle: 'Automated Job Applications',
      progress: 1.0,
      description:
        'Instantly restructures your bullet points to achieve 92%+ ATS scores and drafts customized LinkedIn recruiter outreach notes.',
      icon: FileCheck2,
      color: '#8B5CF6',
    },
  ];

  const currentModule = modules.find((m) => m.id === activeModule) || modules[0];

  return (
    <section
      className="section"
      style={{
        position: 'relative',
        padding: '70px 0',
        overflow: 'hidden',
        background: 'radial-gradient(ellipse at center, rgba(30, 41, 59, 0.4) 0%, transparent 70%)',
      }}
    >
      <div className="container">
        {/* Section Heading */}
        <div style={{ textAlign: 'center', maxWidth: '780px', margin: '0 auto 36px auto' }}>
          <span className="badge badge-orange" style={{ marginBottom: '14px' }}>
            <Wrench size={13} />
            <span>Interactive 3D Mechanical Disassembly</span>
          </span>
          <h2
            style={{
              fontSize: 'clamp(2.1rem, 4vw, 3.2rem)',
              fontWeight: 800,
              letterSpacing: '-0.03em',
              lineHeight: 1.15,
              marginBottom: '16px',
            }}
          >
            Explore the AI Architecture in{' '}
            <span className="text-gradient-orange">Exploded 3D.</span>
          </h2>
          <p style={{ color: 'var(--text-secondary)', fontSize: '1.0625rem', lineHeight: 1.6 }}>
            Click the subsystems or toggle the disassembly button to see the 3D cybernetic assistant separate into floating modular components in real-time WebGL.
          </p>
        </div>

        {/* Interactive Module Selectors */}
        <div
          style={{
            display: 'flex',
            justifyContent: 'center',
            flexWrap: 'wrap',
            gap: '8px',
            marginBottom: '32px',
          }}
        >
          {modules.map((mod) => {
            const Icon = mod.icon;
            const isSelected = activeModule === mod.id;
            return (
              <button
                key={mod.id}
                onClick={() => {
                  setActiveModule(mod.id);
                  if (mod.id === 'all') {
                    setExploded(false);
                  } else {
                    setExploded(true);
                  }
                }}
                className="client-btn"
                style={{
                  padding: '8px 16px',
                  borderRadius: '24px',
                  background: isSelected ? 'var(--app-orange)' : 'rgba(255, 255, 255, 0.8)',
                  color: isSelected ? '#FFFFFF' : 'var(--text-primary)',
                  boxShadow: isSelected ? '0 4px 15px rgba(255, 106, 0, 0.35)' : 'none',
                  border: '1px solid rgba(226, 232, 240, 0.8)',
                  fontSize: '0.8125rem',
                  fontWeight: 700,
                }}
              >
                <Icon size={14} />
                <span>{mod.title}</span>
              </button>
            );
          })}
        </div>

        {/* 3D WebGL Canvas Stage */}
        <div
          style={{
            position: 'relative',
            width: '100%',
            height: '520px',
            borderRadius: '24px',
            background: 'linear-gradient(180deg, rgba(15, 23, 42, 0.95) 0%, rgba(11, 15, 25, 0.98) 100%)',
            border: '1.5px solid rgba(255, 106, 0, 0.25)',
            boxShadow: '0 25px 60px -15px rgba(15, 23, 42, 0.6), 0 0 35px rgba(255, 106, 0, 0.12)',
            overflow: 'hidden',
          }}
        >
          {/* WebGL 3D Robot */}
          <Robot3DCanvas explodedProgress={currentModule.progress} />

          {/* Floating Telemetry HUD Cards over 3D space */}
          <div
            style={{
              position: 'absolute',
              top: '20px',
              left: '24px',
              maxWidth: '340px',
              pointerEvents: 'none',
            }}
          >
            <div
              style={{
                background: 'rgba(15, 23, 42, 0.88)',
                backdropFilter: 'blur(12px)',
                border: '1px solid rgba(255, 255, 255, 0.12)',
                borderRadius: '14px',
                padding: '18px',
                boxShadow: '0 10px 25px rgba(0, 0, 0, 0.5)',
              }}
            >
              <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '8px' }}>
                <span
                  style={{
                    width: '8px',
                    height: '8px',
                    borderRadius: '50%',
                    background: currentModule.color,
                    boxShadow: `0 0 8px ${currentModule.color}`,
                  }}
                />
                <span style={{ fontSize: '0.6875rem', fontWeight: 800, color: currentModule.color, textTransform: 'uppercase', letterSpacing: '0.05em' }}>
                  {currentModule.subtitle}
                </span>
              </div>
              <h4 style={{ fontSize: '1.125rem', fontWeight: 800, color: '#FFFFFF', margin: '0 0 6px 0' }}>
                {currentModule.title}
              </h4>
              <p style={{ fontSize: '0.8125rem', color: '#94A3B8', margin: 0, lineHeight: 1.5 }}>
                {currentModule.description}
              </p>
            </div>
          </div>

          {/* Floating Disassemble Control Bar at Bottom */}
          <div
            style={{
              position: 'absolute',
              bottom: '24px',
              left: '50%',
              transform: 'translateX(-50%)',
              display: 'flex',
              alignItems: 'center',
              gap: '12px',
              background: 'rgba(15, 23, 42, 0.85)',
              backdropFilter: 'blur(12px)',
              padding: '8px 16px',
              borderRadius: '30px',
              border: '1px solid rgba(255, 255, 255, 0.15)',
              boxShadow: '0 10px 30px rgba(0, 0, 0, 0.6)',
            }}
          >
            <button
              onClick={() => {
                setExploded(!exploded);
                if (exploded) {
                  setActiveModule('all');
                } else {
                  setActiveModule('visor');
                }
              }}
              className="client-btn client-btn-primary"
              style={{ padding: '8px 18px', borderRadius: '20px', fontSize: '0.8125rem' }}
            >
              <Sparkles size={14} />
              <span>{exploded ? 'Reassemble 3D Robot ✦' : 'Disassemble 3D Robot ⚙️'}</span>
            </button>
            <span style={{ fontSize: '0.75rem', color: '#94A3B8' }}>
              Drag cursor to rotate 360°
            </span>
          </div>
        </div>
      </div>
    </section>
  );
}
