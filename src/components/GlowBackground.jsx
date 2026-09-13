import React from 'react';

/**
 * Ambient glow lighting effects for hero and section accents
 */
export default function GlowBackground({ variant = 'top' }) {
  if (variant === 'top') {
    return <div className="ambient-glow-top" aria-hidden="true" />;
  }

  if (variant === 'left') {
    return (
      <div
        className="ambient-glow-side"
        style={{ left: '-150px', top: '20%' }}
        aria-hidden="true"
      />
    );
  }

  if (variant === 'right') {
    return (
      <div
        className="ambient-glow-side"
        style={{ right: '-150px', top: '40%' }}
        aria-hidden="true"
      />
    );
  }

  return null;
}
