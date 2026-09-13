import React from 'react';
import {
  UserPlus,
  UploadCloud,
  Cpu,
  Search,
  Send,
  BarChart3,
  CheckCircle2,
} from 'lucide-react';

const STEP_ICON_MAP = {
  UserPlus,
  UploadCloud,
  Cpu,
  SearchCheck: Search,
  Send,
  BarChart3,
};

/**
 * StepCard for the How It Works step-by-step experience
 */
export default function StepCard({ stepItem, isLast = false }) {
  const IconComponent = STEP_ICON_MAP[stepItem.icon] || CheckCircle2;

  return (
    <div className="timeline-step-row">
      <div className="timeline-marker-col">
        <div className="timeline-step-bubble">{stepItem.step}</div>
        {!isLast && <div className="timeline-step-line" />}
      </div>

      <div className="timeline-content-card">
        <div
          style={{
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'space-between',
            gap: '12px',
            marginBottom: '8px',
          }}
        >
          <span className="badge badge-orange" style={{ fontSize: '0.75rem' }}>
            {stepItem.tagline}
          </span>
          <div
            style={{
              color: 'var(--orange-500)',
              background: 'var(--orange-50)',
              padding: '6px',
              borderRadius: '8px',
              display: 'flex',
            }}
          >
            <IconComponent size={18} />
          </div>
        </div>

        <h3
          style={{
            fontSize: '1.25rem',
            fontWeight: '700',
            color: 'var(--text-primary)',
            marginBottom: '8px',
          }}
        >
          {stepItem.title}
        </h3>

        <p
          style={{
            fontSize: '0.9375rem',
            color: 'var(--text-secondary)',
            lineHeight: '1.6',
            marginBottom: '16px',
          }}
        >
          {stepItem.description}
        </p>

        {stepItem.details && (
          <ul
            style={{
              listStyle: 'none',
              display: 'flex',
              flexDirection: 'column',
              gap: '6px',
            }}
          >
            {stepItem.details.map((detail, idx) => (
              <li
                key={idx}
                style={{
                  display: 'flex',
                  alignItems: 'center',
                  gap: '8px',
                  fontSize: '0.8125rem',
                  color: 'var(--text-muted)',
                }}
              >
                <span
                  style={{
                    width: '6px',
                    height: '6px',
                    borderRadius: '50%',
                    background: 'var(--orange-400)',
                    flexShrink: 0,
                  }}
                />
                <span>{detail}</span>
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
}
