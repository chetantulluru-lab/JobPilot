import React from 'react';
import { Download } from 'lucide-react';
import { APP_CONFIG } from '../config/appConfig';

/**
 * Standardized Direct APK Download Button
 * Links directly to the signed release APK at APP_CONFIG.APK_DOWNLOAD_URL
 */
export default function ApkDownloadButton({
  className = '',
  size = 'md', // 'sm' | 'md' | 'lg'
  variant = 'primary', // 'primary' | 'dark' | 'outline'
  showSubtext = true,
  label = 'Download APK',
  subtext = 'Android 11+ • Signed Release',
  onClick,
}) {
  const getVariantClass = () => {
    switch (variant) {
      case 'dark':
        return 'btn-apk-dark';
      case 'outline':
        return 'btn-apk-outline';
      case 'primary':
      default:
        return 'btn-apk-primary';
    }
  };

  return (
    <a
      href={APP_CONFIG.APK_DOWNLOAD_URL}
      download={APP_CONFIG.APK_FILENAME}
      className={`btn-apk btn-apk-${size} ${getVariantClass()} ${className}`}
      onClick={onClick}
      aria-label={`Download JobPilot Android APK (${subtext})`}
    >
      {/* Download Icon */}
      <span className="btn-apk-icon" aria-hidden="true">
        <Download size={size === 'lg' ? 22 : size === 'sm' ? 16 : 18} />
      </span>

      <div className="btn-apk-text">
        <span className="btn-apk-title">{label}</span>
        {showSubtext && <span className="btn-apk-sub">{subtext}</span>}
      </div>
    </a>
  );
}
