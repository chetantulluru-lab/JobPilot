import React from 'react';

/**
 * QR Code Placeholder for Android App Download
 * Visual placeholder ready to be swapped with dynamic or production QR image upon Google Play launch.
 */
export default function QRPlaceholder({ size = 140, caption = 'Scan to download for Android' }) {
  return (
    <div className="qr-container" role="region" aria-label="QR Code to Download JobPilot Android App">
      <svg
        className="qr-placeholder-svg"
        viewBox="0 0 100 100"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
        aria-hidden="true"
        style={{ width: size, height: size }}
      >
        {/* Background Card */}
        <rect width="100" height="100" rx="10" fill="#FFFFFF" />
        
        {/* Corner Position Detection Patterns */}
        {/* Top-Left */}
        <rect x="10" y="10" width="24" height="24" rx="4" fill="#0F172A" />
        <rect x="14" y="14" width="16" height="16" rx="2" fill="#FFFFFF" />
        <rect x="18" y="18" width="8" height="8" rx="1" fill="#FF6A00" />
        
        {/* Top-Right */}
        <rect x="66" y="10" width="24" height="24" rx="4" fill="#0F172A" />
        <rect x="70" y="14" width="16" height="16" rx="2" fill="#FFFFFF" />
        <rect x="74" y="18" width="8" height="8" rx="1" fill="#FF6A00" />
        
        {/* Bottom-Left */}
        <rect x="10" y="66" width="24" height="24" rx="4" fill="#0F172A" />
        <rect x="14" y="70" width="16" height="16" rx="2" fill="#FFFFFF" />
        <rect x="18" y="74" width="8" height="8" rx="1" fill="#FF6A00" />

        {/* Data Pattern Grid (Stylized QR Matrix) */}
        <rect x="38" y="12" width="4" height="4" fill="#334155" />
        <rect x="46" y="12" width="4" height="8" fill="#334155" />
        <rect x="54" y="14" width="6" height="4" fill="#FF7E29" />

        <rect x="38" y="24" width="8" height="4" fill="#334155" />
        <rect x="50" y="22" width="4" height="6" fill="#334155" />
        <rect x="58" y="26" width="4" height="4" fill="#334155" />

        <rect x="12" y="40" width="6" height="4" fill="#334155" />
        <rect x="22" y="38" width="4" height="8" fill="#FF6A00" />
        <rect x="30" y="44" width="6" height="4" fill="#334155" />

        {/* Center JobPilot Emblem */}
        <rect x="40" y="40" width="20" height="20" rx="5" fill="#0F172A" />
        <path d="M50 44L51.5 48.5L56 50L51.5 51.5L50 56L48.5 51.5L44 50L48.5 48.5L50 44Z" fill="#FF6A00" />

        <rect x="66" y="42" width="6" height="4" fill="#334155" />
        <rect x="76" y="38" width="4" height="8" fill="#FF7E29" />
        <rect x="84" y="44" width="6" height="4" fill="#334155" />

        <rect x="38" y="68" width="6" height="4" fill="#334155" />
        <rect x="48" y="66" width="4" height="8" fill="#334155" />
        <rect x="56" y="72" width="6" height="4" fill="#FF6A00" />

        <rect x="66" y="68" width="8" height="4" fill="#334155" />
        <rect x="78" y="66" width="4" height="6" fill="#334155" />
        <rect x="84" y="74" width="6" height="6" fill="#334155" />
      </svg>
      {caption && <span className="qr-caption">{caption}</span>}
    </div>
  );
}
