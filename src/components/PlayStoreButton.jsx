import React from 'react';
import { APP_CONFIG } from '../config/appConfig';

/**
 * Standardized Google Play Store Button
 *
 * CRITICAL ARCHITECTURE RULE:
 * This component ALWAYS reads APP_CONFIG.PLAY_STORE_URL as its single source of truth.
 * When the real Android app is published on Google Play, updating APP_CONFIG.PLAY_STORE_URL
 * instantly updates every download CTA across the entire site.
 */
export default function PlayStoreButton({
  className = '',
  size = 'md',
  showSubtext = true,
  onClick,
}) {
  return (
    <a
      href={APP_CONFIG.PLAY_STORE_URL}
      className={`btn-playstore btn-playstore-${size} ${className}`}
      target={APP_CONFIG.PLAY_STORE_URL.startsWith('http') ? '_blank' : undefined}
      rel={APP_CONFIG.PLAY_STORE_URL.startsWith('http') ? 'noopener noreferrer' : undefined}
      onClick={onClick}
      aria-label="Get JobPilot on Google Play"
    >
      {/* Google Play Vector Icon (Pure SVG, no external assets) */}
      <svg
        width="26"
        height="28"
        viewBox="0 0 24 26"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
        aria-hidden="true"
        style={{ flexShrink: 0 }}
      >
        <path
          d="M1.08 1.48C0.84 1.76 0.7 2.2 0.7 2.76V23.24C0.7 23.8 0.84 24.24 1.08 24.52L1.16 24.59L12.72 13.03V12.79L1.16 1.41L1.08 1.48Z"
          fill="#00D2FF"
        />
        <path
          d="M16.58 16.89L12.72 13.03V12.79L16.58 8.93L16.67 8.98L21.23 11.57C22.53 12.31 22.53 13.51 21.23 14.25L16.67 16.84L16.58 16.89Z"
          fill="#FFD200"
        />
        <path
          d="M16.67 16.84L12.72 12.89L1.08 24.52C1.52 24.98 2.24 25.04 3.06 24.58L16.67 16.84Z"
          fill="#FF3333"
        />
        <path
          d="M16.67 8.98L3.06 1.24C2.24 0.78 1.52 0.84 1.08 1.3L12.72 12.93L16.67 8.98Z"
          fill="#00E676"
        />
      </svg>
      <div>
        {showSubtext && <span className="playstore-sub">Get it on</span>}
        <span className="playstore-title">Google Play</span>
      </div>
    </a>
  );
}
