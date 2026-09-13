import React from 'react';
import {
  FileText,
  Target,
  UserCheck,
  TrendingUp,
  Sparkles,
  Kanban,
  Check,
  Layers,
} from 'lucide-react';
import GlassCard from './GlassCard';

const ICON_MAP = {
  FileText,
  Target,
  UserCheck,
  TrendingUp,
  Sparkles,
  Kanban,
  Layers,
};

/**
 * Feature Card component showcasing JobPilot core capabilities
 */
export default function FeatureCard({ feature, className = '' }) {
  const IconComponent = ICON_MAP[feature.icon] || Sparkles;

  return (
    <GlassCard interactive className={`feature-card ${className}`} padding="30px">
      <div
        style={{
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'space-between',
          marginBottom: '20px',
        }}
      >
        <div
          style={{
            width: '52px',
            height: '52px',
            borderRadius: '16px',
            background: 'linear-gradient(135deg, #FFF5EB 0%, #FFE5CE 100%)',
            border: '1px solid var(--orange-300)',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            color: 'var(--orange-600)',
          }}
        >
          <IconComponent size={26} />
        </div>

        {feature.badge && (
          <span className="badge badge-glass" style={{ fontSize: '0.75rem' }}>
            {feature.badge}
          </span>
        )}
      </div>

      <h3
        style={{
          fontSize: '1.25rem',
          fontWeight: '700',
          marginBottom: '10px',
          color: 'var(--text-primary)',
        }}
      >
        {feature.title}
      </h3>

      <p
        style={{
          fontSize: '0.9375rem',
          color: 'var(--text-secondary)',
          lineHeight: '1.6',
          marginBottom: '20px',
        }}
      >
        {feature.description}
      </p>

      {/* Feature Bullet Points */}
      {feature.highlights && feature.highlights.length > 0 && (
        <ul
          style={{
            listStyle: 'none',
            display: 'flex',
            flexDirection: 'column',
            gap: '8px',
            marginBottom: feature.disclaimer ? '16px' : '0',
          }}
        >
          {feature.highlights.map((item, idx) => (
            <li
              key={idx}
              style={{
                display: 'flex',
                alignItems: 'flex-start',
                gap: '8px',
                fontSize: '0.8125rem',
                color: 'var(--text-secondary)',
              }}
            >
              <Check
                size={14}
                color="var(--orange-500)"
                style={{ flexShrink: 0, marginTop: '3px' }}
              />
              <span>{item}</span>
            </li>
          ))}
        </ul>
      )}

      {/* Optional Disclaimer (e.g. for Application Assistance) */}
      {feature.disclaimer && (
        <div
          style={{
            marginTop: '16px',
            padding: '10px 14px',
            borderRadius: '10px',
            background: 'rgba(241, 245, 249, 0.7)',
            border: '1px solid rgba(203, 213, 225, 0.8)',
            fontSize: '0.75rem',
            color: 'var(--text-muted)',
            lineHeight: '1.45',
          }}
        >
          <strong>Note:</strong> {feature.disclaimer}
        </div>
      )}
    </GlassCard>
  );
}
