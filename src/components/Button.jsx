import React from 'react';
import { Link } from 'react-router-dom';

/**
 * Universal Button component supporting router Links, external links, and standard buttons
 */
export default function Button({
  children,
  to,
  href,
  variant = 'primary', // 'primary' | 'secondary' | 'ghost'
  size = 'md', // 'sm' | 'md' | 'lg'
  className = '',
  icon = null,
  iconPosition = 'right',
  target,
  rel,
  onClick,
  ...props
}) {
  const baseClasses = `btn btn-${variant} btn-${size} ${className}`.trim();

  const content = (
    <>
      {icon && iconPosition === 'left' && <span className="btn-icon-left">{icon}</span>}
      <span>{children}</span>
      {icon && iconPosition === 'right' && <span className="btn-icon-right">{icon}</span>}
    </>
  );

  if (to) {
    return (
      <Link to={to} className={baseClasses} onClick={onClick} {...props}>
        {content}
      </Link>
    );
  }

  if (href) {
    return (
      <a
        href={href}
        className={baseClasses}
        target={target || (href.startsWith('http') ? '_blank' : undefined)}
        rel={rel || (href.startsWith('http') ? 'noopener noreferrer' : undefined)}
        onClick={onClick}
        {...props}
      >
        {content}
      </a>
    );
  }

  return (
    <button className={baseClasses} onClick={onClick} {...props}>
      {content}
    </button>
  );
}
