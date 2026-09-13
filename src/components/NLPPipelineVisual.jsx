import React, { useState } from 'react';
import { 
  ArrowRight, 
  FileText, 
  Binary, 
  Cpu, 
  Layers, 
  UserCheck, 
  FileSearch, 
  GitCompare, 
  Target,
  Sparkles
} from 'lucide-react';
import { NLP_PIPELINE_STEPS, NLP_CONCEPTS } from '../data/mockJobs';
import GlassCard from './GlassCard';

const STEP_ICONS = [
  FileText,
  Binary,
  Cpu,
  Layers,
  UserCheck,
  FileSearch,
  GitCompare,
  Target,
];

/**
 * Interactive Glass NLP Intelligence Pipeline
 */
export default function NLPPipelineVisual({ className = '' }) {
  const [activeStepIndex, setActiveStepIndex] = useState(0);
  const activeStep = NLP_PIPELINE_STEPS[activeStepIndex];
  const ActiveIcon = STEP_ICONS[activeStepIndex];

  return (
    <div className={`nlp-pipeline-wrapper ${className}`}>
      {/* 8-Node Glass Interactive Grid */}
      <div className="nlp-pipeline-grid">
        {NLP_PIPELINE_STEPS.map((step, idx) => {
          const NodeIcon = STEP_ICONS[idx];
          const isSelected = activeStepIndex === idx;

          return (
            <div
              key={step.id}
              className={`nlp-pipeline-node ${isSelected ? 'active' : ''}`}
              onClick={() => setActiveStepIndex(idx)}
              role="button"
              tabIndex={0}
              onKeyDown={(e) => {
                if (e.key === 'Enter' || e.key === ' ') {
                  setActiveStepIndex(idx);
                }
              }}
              style={{
                cursor: 'pointer',
                borderColor: isSelected ? 'var(--orange-500)' : undefined,
                boxShadow: isSelected
                  ? '0 12px 30px -5px rgba(255, 106, 0, 0.25)'
                  : undefined,
              }}
            >
              <div
                style={{
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'space-between',
                  marginBottom: '10px',
                }}
              >
                <span className="nlp-node-num">STAGE {step.stepNumber}</span>
                <span
                  style={{
                    color: isSelected ? 'var(--orange-600)' : 'var(--text-muted)',
                    background: isSelected ? 'var(--orange-50)' : 'rgba(241, 245, 249, 0.8)',
                    padding: '4px',
                    borderRadius: '6px',
                    display: 'flex',
                  }}
                >
                  <NodeIcon size={16} />
                </span>
              </div>

              <h4
                style={{
                  fontSize: '1rem',
                  fontWeight: '700',
                  color: isSelected ? 'var(--orange-600)' : 'var(--text-primary)',
                  marginBottom: '4px',
                }}
              >
                {step.name}
              </h4>

              <div
                style={{
                  fontSize: '0.75rem',
                  color: 'var(--text-muted)',
                  fontWeight: '500',
                }}
              >
                {step.subtext}
              </div>

              {/* Progress Indicator Dots */}
              <div
                style={{
                  marginTop: '12px',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'space-between',
                }}
              >
                <span
                  style={{
                    fontSize: '0.6875rem',
                    fontWeight: '700',
                    color: isSelected ? 'var(--orange-600)' : '#94A3B8',
                    textTransform: 'uppercase',
                  }}
                >
                  {step.status}
                </span>
                {idx < NLP_PIPELINE_STEPS.length - 1 && (
                  <span style={{ color: isSelected ? 'var(--orange-500)' : '#CBD5E1' }}>
                    <ArrowRight size={14} />
                  </span>
                )}
              </div>
            </div>
          );
        })}
      </div>

      {/* Detailed Selected Step Deep Dive Card */}
      <GlassCard
        elevated
        style={{
          marginTop: '28px',
          padding: '30px',
          border: '1px solid rgba(255, 106, 0, 0.2)',
          background: 'linear-gradient(135deg, rgba(255, 255, 255, 0.95) 0%, rgba(255, 249, 242, 0.85) 100%)',
        }}
      >
        <div
          style={{
            display: 'flex',
            flexWrap: 'wrap',
            alignItems: 'center',
            justifyContent: 'space-between',
            gap: '16px',
            marginBottom: '16px',
          }}
        >
          <div style={{ display: 'flex', alignItems: 'center', gap: '14px' }}>
            <div
              style={{
                width: '46px',
                height: '46px',
                borderRadius: '14px',
                background: 'linear-gradient(135deg, #FF6A00 0%, #FF8A3D 100%)',
                color: '#FFFFFF',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                boxShadow: 'var(--shadow-orange-sm)',
              }}
            >
              <ActiveIcon size={22} />
            </div>
            <div>
              <div style={{ fontSize: '0.8125rem', fontWeight: '700', color: 'var(--orange-600)' }}>
                PHASE {activeStep.stepNumber} • {activeStep.status.toUpperCase()}
              </div>
              <h3 style={{ fontSize: '1.35rem', fontWeight: '800', color: 'var(--text-primary)' }}>
                {activeStep.name}: {activeStep.subtext}
              </h3>
            </div>
          </div>

          <div className="badge badge-orange">
            <Sparkles size={13} />
            <span>Interactive NLP Inspector</span>
          </div>
        </div>

        <p style={{ fontSize: '1rem', color: 'var(--text-secondary)', lineHeight: '1.6', marginBottom: '20px' }}>
          {activeStep.description}
        </p>

        <div>
          <div
            style={{
              fontSize: '0.75rem',
              fontWeight: '700',
              textTransform: 'uppercase',
              letterSpacing: '0.05em',
              color: 'var(--text-muted)',
              marginBottom: '8px',
            }}
          >
            Core Technologies & Mathematical Concepts (Conceptual Architecture)
          </div>
          <div style={{ display: 'flex', gap: '8px', flexWrap: 'wrap' }}>
            {activeStep.tech.map((t, idx) => (
              <span
                key={idx}
                style={{
                  background: '#FFFFFF',
                  color: 'var(--text-primary)',
                  padding: '6px 14px',
                  borderRadius: 'var(--radius-full)',
                  fontSize: '0.8125rem',
                  fontWeight: '600',
                  fontFamily: 'var(--font-mono)',
                  border: '1px solid rgba(226, 232, 240, 0.9)',
                  boxShadow: '0 2px 5px rgba(0,0,0,0.03)',
                }}
              >
                ⚙ {t}
              </span>
            ))}
          </div>
        </div>
      </GlassCard>

      {/* Conceptual NLP Technology Grid */}
      <div
        style={{
          display: 'grid',
          gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))',
          gap: '16px',
          marginTop: '28px',
        }}
      >
        {NLP_CONCEPTS.map((concept, idx) => (
          <GlassCard key={idx} padding="20px">
            <div
              style={{
                fontSize: '0.6875rem',
                fontWeight: '700',
                color: 'var(--orange-600)',
                textTransform: 'uppercase',
                letterSpacing: '0.05em',
                marginBottom: '6px',
              }}
            >
              {concept.badge}
            </div>
            <h5
              style={{
                fontSize: '1rem',
                fontWeight: '700',
                color: 'var(--text-primary)',
                marginBottom: '6px',
              }}
            >
              {concept.title}
            </h5>
            <p style={{ fontSize: '0.8125rem', color: 'var(--text-secondary)', lineHeight: '1.5' }}>
              {concept.description}
            </p>
          </GlassCard>
        ))}
      </div>
    </div>
  );
}
