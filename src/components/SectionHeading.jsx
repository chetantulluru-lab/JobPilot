import React from 'react';

/**
 * Standardized Section Heading with optional eyebrow badge, gradient title, and subtitle
 */
export default function SectionHeading({
  eyebrow,
  title,
  highlight,
  subtitle,
  align = 'center', // 'left' | 'center'
  className = '',
}) {
  const isCenter = align === 'center';

  return (
    <div
      className={`section-heading ${className}`}
      style={{
        textAlign: align,
        maxWidth: isCenter ? '760px' : '640px',
        margin: isCenter ? '0 auto 56px' : '0 0 40px',
      }}
    >
      {eyebrow && (
        <div style={{ marginBottom: '14px' }}>
          <span className="badge badge-orange">{eyebrow}</span>
        </div>
      )}

      <h2
        style={{
          fontSize: 'clamp(2rem, 3.5vw, 2.75rem)',
          fontWeight: '800',
          letterSpacing: '-0.03em',
          lineHeight: '1.18',
          marginBottom: '16px',
        }}
      >
        {title}{' '}
        {highlight && <span className="text-gradient-orange">{highlight}</span>}
      </h2>

      {subtitle && (
        <p
          style={{
            fontSize: 'clamp(1rem, 1.25vw, 1.125rem)',
            color: 'var(--text-secondary)',
            lineHeight: '1.6',
          }}
        >
          {subtitle}
        </p>
      )}
    </div>
  );
}
