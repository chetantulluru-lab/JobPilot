import React from 'react';

/**
 * Reusable Glassmorphism Card container
 */
export default function GlassCard({
  children,
  className = '',
  elevated = false,
  interactive = false,
  glow = false,
  padding = '24px',
  ...props
}) {
  const classes = [
    elevated ? 'glass-panel-elevated' : 'glass-panel',
    interactive ? 'glass-panel-interactive' : '',
    glow ? 'shadow-glow' : '',
    className,
  ]
    .filter(Boolean)
    .join(' ');

  return (
    <div className={classes} style={{ padding }} {...props}>
      {children}
    </div>
  );
}
